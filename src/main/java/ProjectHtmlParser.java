import java.util.Date;

public final class ProjectHtmlParser {
    private static String url = "http://221.238.206.17/HomePage?method=getProjects1Info&currentPage=";
    private static String companyName = "&saccountnump=91110105317977127&random=5659";
    private static ProjectHtmlParser ourInstance = new ProjectHtmlParser();

    public static ProjectHtmlParser getInstance() {
        return ourInstance;
    }

    private ProjectHtmlParser() {
    }

    private static int forLoop() {
        int sum = 0;
        for (int i = 0; i < 36; i++) {
            for (int j = 0; j < 36; j++) {
                sum += 1;
            }
        }
        return sum;
    }

    public static void main(String[] args) {
        Date date = new Date();
        int count = 0;
        for (int i = 0; i < 36; i++) {
            for (int j = 0; j < 36; j++) {
                count += forLoop();
            }
        }
        Date date1 = new Date();
        System.out.println("time cost : " + date1 + date + "total value is " + count);
    }
}
