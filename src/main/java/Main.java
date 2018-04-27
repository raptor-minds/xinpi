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

        System.out.println(ExcelWriter.getNameAndNo());
//        HtmlParser.parseOneInstitute("911101010975549066");
        HtmlParser.writeAllCompanyInfo();
        System.out.println("-------------SUMMARY------------");
        int total = HtmlParser.succeedNum + HtmlParser.failNum;
        System.out.println("TOTAL COMPANY NUM: " +total);
        System.out.println("SUCCEED INJECTED NUM: " + HtmlParser.succeedNum);
        System.out.println("Failed INJECTED NUM: " + HtmlParser.failNum);
        System.out.println("Failed COMPANY: " + HtmlParser.badCompany.toString());

        Set<String> allCompany = HtmlParser.allCompany; // processed companies
        Set<String> company = new HashSet<String>();
        for(String temp : ExcelWriter.getNameAndNo().keySet()){
            if(!allCompany.contains(temp)){
                System.out.println("!!!" + ExcelWriter.getNameAndNo().get(temp));
            }
        }
        HtmlParser.allCompany.clear();
        HtmlParser.succeedNum = 0;
        HtmlParser.failNum = 0;
        HtmlParser.badCompany.clear();
    }
}
