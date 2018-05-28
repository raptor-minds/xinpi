import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class HtmlParserProInfo {

    private static List<String> title = new ArrayList<String>();
    private static final String IP = "61.181.59.73";
    private static final String httpHeader = "http://" + IP + ":10080";
    private static final String baseUrl = "http://" + IP + ":10080/publicnifa/HomePage?method=getProjectsInfo";

    /**
     * this method is used to get all the urls from the main web page.
     *
     * @param page
     *
     * @return the list of the urls
     */
    public static List<String> getUrls(int page) {
        List<String> urls = new ArrayList<String>();

        String result = HttpClientHelper.sendGet(baseUrl, null);

        Document doc = Jsoup.parse(result);

        Elements tables = doc.select("tbody#runinfotbody");

        for (Element element : tables.select("tr")) {
            String base = element.toString();
            Element e = element.select("a").get(2);
            String baseUrl = e.attributes().toString();
            int begin = baseUrl.indexOf("/publicnifa");
            int end = baseUrl.indexOf("\" class=");
            String url = baseUrl.substring(begin, end).replace("&amp;", "&");
            urls.add(url);
        }

        return urls;

    }

    public static  Map<String, List<String>> processUrls(List<String> urls){
        Map<String, List<String>> map = new HashMap<String, List<String>>();

        int count = 0;
        for (String url : urls){

            int begin = url.indexOf("sfullnames=");

            String id = url.substring(begin + 11);

            List<String> content = getSinglePageInfo(url);

            map.put(id, content);

            System.out.println(id);
            System.out.println(content);

        }

        return map;
    }

    /**
     * http://61.181.59.73:10080/publicnifa/HomePage?method=getTargetProjectInfo&sorganation=911101085977302834&stheonlyid=9111010859773028345920992&sdebtortypeb=02&sfullnames=911101085977302834
     * @param url
     *
     * @return one map
     */
    public static List<String> getSinglePageInfo(String url) {

        List<String> content = new ArrayList<String>();
        Document doc = null;
        try {
            doc = Jsoup.parse(new URL(httpHeader +url).openStream(), "GBK", httpHeader +url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ;

        Elements baseInfo = doc.select("div#base-info");

        for (Element e : baseInfo.select("tr")){
            for (Element td : e.select("td")){
                content.add(td.text());
            }

        }

        return content;
    }

    public static void main(String[] args) throws Exception {
        ExcelWriter.deleteExcel("./test1.xls");
        List<String> names = getUrls(0);
        Map<String, List<String>> map = processUrls(names);
        try {
            ExcelWriter.createExcel("./test1.xls", "sheet1", title);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ExcelWriter.writeToExcel("./test1.xls", "sheet1", map);
    }

}
