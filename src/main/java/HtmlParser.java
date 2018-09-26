import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

public class HtmlParser {

    public static boolean isNewVersion = false;
    private final static String newDomainName = "dp2.nifa.org.cn";
    private final static String oldDomainName = "dp.nifa.org.cn";
    private final static String baseUrl = isNewVersion ?
            "http://" + newDomainName + "/HomePage?method=getTargetOrgInfo&sorganation="
            : "https://" + oldDomainName + "/HomePage?method=getTargetOrgInfo&sorganation=";
    private final static String originUrl = isNewVersion ?
            "http://" + newDomainName + "/HomePage?method=getOperateInfo&currentPage="
            : "https://" + oldDomainName + "/HomePage?method=getOperateInfo&currentPage=";
    public static int succeedNum = 0;
    public static int failNum = 0;
    public static Set<String> badCompany = new HashSet<String>();
    public static List<Integer> monthIds = new LinkedList<Integer>();
    public static Set<String> allCompany = new HashSet<String>();

    private static Set<String> getSinglePageCompanyNo(int page) {
        Set<String> companyIds = new LinkedHashSet<String>();
        String result;
        result = HttpClientHelper.httpsRequest(originUrl + page, "GET", null);

        Document doc = Jsoup.parse(result);

        Elements divs = doc.select("tbody#runinfotbody").select("a");
        for (Element element : divs) {
            String base = element.toString();
            int begin = base.indexOf("sorganation=");
            int end = base.indexOf("&amp;location=yy");
            String companyId = base.substring(begin + 12, end);
            companyIds.add(companyId);
        }

        return companyIds;
    }

    public static Set<String> getAllCompanyIds() {
        Set<String> companyIds = new LinkedHashSet<String>();
        for (int i = 1; i < ExcelWriter.htmlPageNo; i++) {
            Set<String> temp = getSinglePageCompanyNo(i);
            companyIds.addAll(temp);
        }
        return companyIds;
    }

    public static void writeAllCompanyInfo() {
        Set<String> companyIds = new LinkedHashSet<String>();
        for (int i = 1; i < ExcelWriter.htmlPageNo; i++) {
            Set<String> temp = getSinglePageCompanyNo(i);
            companyIds.addAll(temp);
        }
        for (String comId : companyIds) {
            parseOneInstitute(comId);
        }
    }

    public static void parseOneInstitute(String url) {
        String result;

        result = HttpClientHelper.httpsRequest(
                baseUrl + url, "GET",
                null);

        Document doc = (Document) Jsoup.parse(result);
        Elements divBaseInfo = doc.select("div#base-info");
        String companyName = divBaseInfo.select("tr").get(1).select("td").get(1).text().trim();
        List<String> comNames = new LinkedList<String>();
        String companyId = url;
        comNames.add(companyId);
        System.out.println("processing " + companyId + " " + companyName + " ...");


        Map<Integer, List<String>> tradeListMap = new HashMap<Integer, List<String>>();
        Elements tradeLog = null;
        if (isNewVersion) {
            tradeLog = doc.select("div#trade-log");
        } else {
            tradeLog = doc.select("div#out-info");
        }
        List<String> dates = new LinkedList<String>();
        Elements tables = tradeLog.select("table");

        int i = 0;
        // left table is date info
        for (Element element : tables.get(0).children().select("tr")) {
            String date = element.select("td").text();
            // first line is trash
            if (i != 0 && ExcelWriter.dates.contains(date) && !dates.contains(date)) {
                monthIds.add(i);
                dates.add(date);
            }
            i++;
        }

        i = 0;
        // right table is transaction info

        for (Element element : tables.get(1).children().select("tr")) {
            List<String> numbers = new ArrayList<String>();
            if (i != 0 && monthIds.contains(i)) {
                for (Element e : element.select("td")) {
                    numbers.add(e.text());
                }

                tradeListMap.put(i, numbers);
            }
            i++;
        }

        if (!tradeListMap.isEmpty()) {
            allCompany.add(companyId);
        }

        try {
            ExcelWriter.writeToExcel("./test2.xls", "sheet1",
                    tradeListMap, comNames, dates, companyName);
            System.out.println("successfully processed " + companyId + " " + companyName + ".");
            succeedNum++;
        } catch (Exception e) {
            e.printStackTrace();
            failNum++;
            badCompany.add(companyId + " " + companyName);
        }
        monthIds.clear();
    }

    public static void main(String[] args) {

        //        ExcelWriter.getHtmlPageNo();
        //        List<String> title = ExcelWriter.getTitle();
        //        ExcelWriter.getDateInfo();
        //        System.out.println(getSinglePageCompanyNo(1));
        //        writeAllCompanyInfo();
        HtmlParser.parseOneInstitute("91110000095356957P");
    }
}
