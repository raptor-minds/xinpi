import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public final class PlatformHtmlParser {

    private final static String url = "https://dp.nifa.org.cn/HomePage?method=getPublishedInfo&currentPage=";

    private static Logger log = Logger.getLogger(PlatformHtmlParser.class.getName());

    private static PlatInfo parseEachPlatform(String platId) {
        PlatInfo platInfo = new PlatInfo();
        String result;
        String url = "https://dp.nifa.org.cn/HomePage?method=getTargetOrgInfo&sorganation=" + platId;
        result = HttpClientHelper.httpsRequest(
                url, "GET",
                null);

        Document doc = (Document) Jsoup.parse(result);
        List<Element> elements = new ArrayList<Element>();
        Elements trs = doc.select("div#base-info").select("tr");
        Elements elements1 = doc.select("div#govern-info").select("tr");
        elements.addAll(trs);
        elements.addAll(elements1);
        for (Element tr : elements) {
            Elements tds = tr.select("td");
            switch (CompanyInfo.formValue(getElementKey(tds))) {
                case NAME:
                    platInfo.setCompanyName(getElementValue(tds));
                    break;
                case REG_CAPITAL:
                    platInfo.setRegCapital(getElementValue(tds));
                    break;
                case OPE_PRO:
                    platInfo.setOperateProv(getElementValue(tds));
                    break;
                case REG_CITY:
                    platInfo.setRegCity(getElementValue(tds));
                    break;
                case REG_COUNTRY:
                    platInfo.setRegCountry(getElementValue(tds));
                    break;
                case REG_PRO:
                    platInfo.setRegProv(getElementValue(tds));
                    break;
                case OPE_COUNTRY:
                    platInfo.setOperateCountry(getElementValue(tds));
                    break;
                case OPE_CITY:
                    platInfo.setOperateCity(getElementValue(tds));
                    break;
                case REG_DATE:
                    platInfo.setRegDate(getElementValue(tds));
                    break;
                case REG_DIGS_CODE:
                    platInfo.setRegDist(getElementValue(tds));
                    break;
                case FUND_DEPOSITORY:
                    platInfo.setFundDepository(getElementValue(tds));
                    break;
                case SAFETY_CERT_LEVEL:
                    platInfo.setSafetyCertLevel(getElementValue(tds));
                    break;
                default:
                    platInfo.setCompanyId(platId);
            }
        }
        return platInfo;
    }

    private static String getElementKey (Elements elements) {
        try {
            return elements.get(0).text().trim();
        } catch (Exception e) {
            return "公司ID";
        }
    }

    private static String getElementValue (Elements elements) {
        try {
            return elements.get(1).text().trim();
        } catch (Exception e) {
            return null;
        }
    }

    private static void writeOneCompany(String platId) {
        PlatInfo platInfo = parseEachPlatform(platId);

        try {
            ExcelWriter.writeBaseCompanyInfo("./test3.xls", "sheet1", platInfo);
            System.out.println("successfully processed " + platInfo.getCompanyName() + ".");
            log.debug("successfully processed " + platInfo.getCompanyName() + ".");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void writeAllCompany() {
        ExcelWriter.deleteExcel("./test3.xls");

        List<String> titles = ExcelWriter.getTitleForU("sheet8");

        try {
            ExcelWriter.createExcel("./test3.xls", "sheet1", titles);
        } catch (Exception e) {
            e.printStackTrace();
        }

        int i = 0, j = 0;
        do {
            i++;
            List<String> companyOfOnePage = BankHtmlParser.parseOnePage(i, url);

            for (String bank : companyOfOnePage) {
                writeOneCompany(bank);
                j++;
            }

        } while (i <= 22);

        System.out.println("process " + j + "companies.");
    }

    public static void main(String[] args) {
        writeAllCompany();
    }
}
