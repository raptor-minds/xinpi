import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The type Main.
 */
public class Main {

    /**
     * The entry point of application.
     *
     * @param args
     *         the input arguments
     *
     * @throws Exception
     *         the exception
     */
    public static void main(String[] args) throws Exception {
        ExcelWriter.deleteExcel("./test2.xls");

        System.out.println(ExcelWriter.getHtmlPageNo());
        List<String> title = ExcelWriter.getTitle();
        ExcelWriter.getDateInfo();

        ExcelWriter.createExcel("./test2.xls", "sheet1", title);

        //        HtmlParser.parseOneInstitute("91110105317977127Q");
        String isNewV = System.getProperty("para");

        if (isNewV != null) {
            HtmlParser.isNewVersion = !isNewV.equals("old");
        }

        if (HtmlParser.isNewVersion) {
            System.out.println("this is new version");
        } else {
            System.out.println("this is old version");
        }

        HtmlParser.writeAllCompanyInfo();
        System.out.println("-------------SUMMARY------------");
        int total = HtmlParser.succeedNum + HtmlParser.failNum;
        System.out.println("TOTAL COMPANY NUM: " + total);
        System.out.println("SUCCEED INJECTED NUM: " + HtmlParser.succeedNum);
        System.out.println("Failed INJECTED NUM: " + HtmlParser.failNum);
        System.out.println("Failed COMPANY: " + HtmlParser.badCompany.toString());

        Set<String> allCompany = HtmlParser.allCompany; // processed companies
        Set<String> company = new HashSet<String>();
        HtmlParser.allCompany.clear();
        HtmlParser.succeedNum = 0;
        HtmlParser.failNum = 0;
        HtmlParser.badCompany.clear();
    }
}
