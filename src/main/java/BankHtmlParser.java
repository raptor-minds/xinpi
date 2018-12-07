import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * The type InstituteInBank html parser.
 */
public final class BankHtmlParser {


    private static String url = "https://dp.nifa.org.cn/HomePage?method=getCapitalInfo&currentPage=";

    private static Logger log = Logger.getLogger(BankHtmlParser.class.getName());

    public static List<String> parseOnePage(int pageNO, String url) {
        String result;
        List<String> bankList = new LinkedList<String>();

        result = HttpClientHelper.httpsRequest(
                url + String.valueOf(pageNO), "GET",
                null);

        Document doc = (Document) Jsoup.parse(result);
        Elements divBaseInfo = doc.select("ul#jigou").select("input");

        for (Element element : divBaseInfo) {
            bankList.add(element.attr("value"));
        }
        return bankList;
    }

    private static List<InstituteInBank> parseEachBank(String bankId) {
        String result;
        String url = "https://dp.nifa.org.cn/HomePage?method=getTargetOrgInfo&sorganation=" + bankId + "&location=zj";
        result = HttpClientHelper.httpsRequest(
                url, "GET",
                null);

        Document doc = (Document) Jsoup.parse(result);
        Elements trs = doc.select("div#base-info").select("tr");
        List<InstituteInBank> instituteInBanks = new ArrayList<InstituteInBank>();

        for (int i = 2; i < trs.size(); i++) {
            Element element = trs.get(i);
            Elements tds = element.select("td");
            InstituteInBank instituteInBank = new InstituteInBank.Builder()
                    .fullName(tds.get(1).text()).shortName(tds.get(2).text()).
                            signDate(tds.get(3).text()).onlineDate(tds.get(4).text()).id(tds.get(5).text())
                    .storageName(tds.get(6).text()).storageVersion(tds.get(7).text()).build();
            instituteInBanks.add(instituteInBank);

            log.debug("a new institute generated: " + instituteInBank);
        }

        return instituteInBanks;
    }

    private static String getBankName(String bankId) {
        String result;
        String url = "https://dp.nifa.org.cn/HomePage?method=getTargetOrgInfo&sorganation=" + bankId + "&location=zj";
        result = HttpClientHelper.httpsRequest(
                url, "GET",
                null);

        Document doc = (Document) Jsoup.parse(result);
        Elements span = doc.select("div.intro-line").select("span");
        return span.get(0).text();
    }

    private static void writeEachBank(String bankId) {
        List<InstituteInBank> instituteInBanks = parseEachBank(bankId);
        String bankName = getBankName(bankId);

        try {
            ExcelWriter.writeBankInfoToExcel("./test1.xls", "sheet1", bankName, instituteInBanks);
            System.out.println("successfully processed " + bankName + ".");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean isCurrentPageExist(int pageNO) {
        String result;
        List<String> bankList = new LinkedList<String>();

        result = HttpClientHelper.httpsRequest(
                url + String.valueOf(pageNO), "GET",
                null);

        Document doc = (Document) Jsoup.parse(result);
        Elements divBaseInfo = doc.select("ul#jigou").select("input");

        return  divBaseInfo.size() != 0;
    }

    public static void processAllBanks() {

        ExcelWriter.deleteExcel("./test1.xls");

        List<String> titles = ExcelWriter.getTitleForU("sheet7");

        try {
            ExcelWriter.createExcel("./test1.xls", "sheet1", titles);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int i = 1;
        do {
            List<String> banksOfOnePage = parseOnePage(i, url);

            for (String bank : banksOfOnePage) {
                writeEachBank(bank);
            }
        } while (isCurrentPageExist(i++));

    }

    public static void main(String[] args) {
//        System.out.println(parseOnePage(2));
//
//        System.out.println(parseEachBank("911100001000044477"));
//
//        System.out.println(getBankName("911100001000044477"));
//
//        System.out.println(isCurrentPageExist(5));
//
//        System.out.println(isCurrentPageExist(7));

        processAllBanks();
    }

}
