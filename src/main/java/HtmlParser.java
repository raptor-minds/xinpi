import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

public class HtmlParser {

    private static String domainName = "dp2.nifa.org.cn";
    private static String baseUrl = "http://" + domainName + "/publicnifa/HomePage?method=getTargetOrgInfo&sorganation=";
    private static String originUrl = "http://" + domainName + "/publicnifa/HomePage?method=getOperateInfo&currentPage=";
    public static int succeedNum = 0;
    public static int failNum = 0;
    public static Set<String> badCompany = new HashSet<String>();
    public static List<Integer> monthIds = new LinkedList<Integer>();
    public static Set<String> allCompany = new HashSet<String>();

    private static Set<String> getSinglePageCompanyNo(int page) {
        Set<String> companyIds = new LinkedHashSet<String>();
//        String result = HttpClientHelper.httpsRequest(originUrl + page, "GET",null);
        String result = HttpClientHelper.sendGet(originUrl + page,null);
        Document doc = Jsoup.parse(result);

        Elements divs = doc.select("tbody#runinfotbody").select("a");
        for (Element element : divs) {
            String base = element.toString();
            int begin = base.indexOf("sorganation=");
            int end = base.indexOf("&amp;location=");
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
//        String result = HttpClientHelper.httpsRequest(
//                baseUrl + url, "GET",
//                 null);

        String result = HttpClientHelper.sendGet(
                baseUrl + url,
                null);

        Document doc = (Document) Jsoup.parse(result);

        Elements divs = doc.select("div#site-plate");
        List<String> comNames = new LinkedList<String>();
        //String companyName = divs.select("tr").get(1).select("td").get(1).text();
        String companyName = url;
        comNames.add(companyName);
        System.out.println("processing " + companyName + " " + ExcelWriter.nameAndNo.get(companyName.trim().toUpperCase()) + " ...");


        Map<Integer, List<String>> tradeListMap = new HashMap<Integer, List<String>>();
        Elements tradeLog = doc.select("div#trade-log");
        List<String> dates = new LinkedList<String>();
        Elements tables = tradeLog.select("table");

        int i = 0;
        // left table is date info
        for (Element element : tables.get(0).children().select("tr")) {
            String date = element.select("td").text();
            // first line is trash
            if (i != 0 && ExcelWriter.dates.contains(date)) {
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

        if (!tradeListMap.isEmpty()){
            allCompany.add(companyName);
        }

        try {
            ExcelWriter.writeToExcel("./test2.xls", "sheet1",
                    tradeListMap, comNames, dates);
            System.out.println("successfully processed " + companyName + " " + ExcelWriter.nameAndNo.get(companyName.trim().toUpperCase()) + ".");
            succeedNum++;
        } catch (Exception e) {
            e.printStackTrace();
            failNum++;
            badCompany.add(companyName + " " + ExcelWriter.nameAndNo.get(companyName.trim().toUpperCase()));
        }
        monthIds.clear();
    }

    public static void main(String[] args) {

        ExcelWriter.getHtmlPageNo();
        List<String> title = ExcelWriter.getTitle();
        ExcelWriter.getDateInfo();
        System.out.println(getSinglePageCompanyNo(1));
        writeAllCompanyInfo();
    }
}
