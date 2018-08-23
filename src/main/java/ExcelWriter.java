import org.apache.poi.hssf.usermodel.*;

import java.io.*;
import java.util.*;

/**
 * The type Excel writer.
 */
public class ExcelWriter {
    private static HSSFWorkbook workbook = null;
    private static List<String> title = new ArrayList<String>();
    /**
     * The constant dates.
     */
    public static List<String> dates = new ArrayList<String>();
    private static int monthNums = 0;
    /**
     * The constant htmlPageNo.
     */
    public static int htmlPageNo = 2;

    /**
     * Gets title.
     *
     * @return the title
     */
    public static List<String> getTitle() {

        File file = new File("./companyNames.xls");
        if (!file.exists()) {
            System.out.println("companyNames doesn't exist.");
        }
        HSSFWorkbook workbook = null;
        try {
            workbook = new HSSFWorkbook(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //流
        FileOutputStream out = null;
        HSSFSheet sheet = null;
        if (!HtmlParser.isNewVersion) {
            sheet = workbook.getSheet("sheet2");
        } else {
            sheet = workbook.getSheet("sheet5");
        }
        // 获取表格的总行数
        int colCount = sheet.getRow(0).getLastCellNum(); // 需要加一

        for (int i = 0; i < colCount; i++) {
            if (!sheet.getRow(0).getCell(i).getStringCellValue().isEmpty()) {
                title.add(sheet.getRow(0).getCell(i).getStringCellValue());
            }
        }
        return title;
    }

    /**
     * Gets date info.
     *
     * @return the date info
     */
    public static List<String> getDateInfo() {

        File file = new File("./companyNames.xls");
        if (!file.exists()) {
            System.out.println("companyNames doesn't exist.");
        }
        HSSFWorkbook workbook = null;
        try {
            workbook = new HSSFWorkbook(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //流
        FileOutputStream out = null;
        HSSFSheet sheet = workbook.getSheet("sheet4");
        // 获取表格的总行数

        monthNums = (int) sheet.getRow(0).getCell(0).getNumericCellValue();
        dates.add(String.valueOf(monthNums));
        for (int i = 0; i < monthNums; i++) {
            dates.add(sheet.getRow(i + 1).getCell(0).getStringCellValue().replace(",", ""));
        }
        return dates;
    }


    /**
     * Gets html page no.
     *
     * @return the html page no
     */
    public static int getHtmlPageNo() {

        File file = new File("./companyNames.xls");
        if (!file.exists()) {
            System.out.println("companyNames doesn't exist.");
        }
        HSSFWorkbook workbook = null;
        try {
            workbook = new HSSFWorkbook(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //流
        FileOutputStream out = null;
        HSSFSheet sheet = workbook.getSheet("sheet3");
        // 获取表格的总行数

        htmlPageNo = (int) sheet.getRow(0).getCell(0).getNumericCellValue();
        return htmlPageNo;
    }

    /**
     * 判断文件是否存在.
     *
     * @param fileDir
     *         文件路径
     *
     * @return boolean boolean
     */
    public static boolean fileExist(String fileDir) {
        boolean flag = false;
        File file = new File(fileDir);
        flag = file.exists();
        return flag;
    }

    /**
     * 判断文件的sheet是否存在.
     *
     * @param fileDir
     *         文件路径
     * @param sheetName
     *         表格索引名
     *
     * @return boolean boolean
     * @throws Exception
     *         the exception
     */
    public static boolean sheetExist(String fileDir, String sheetName) throws Exception {
        boolean flag = false;
        File file = new File(fileDir);
        if (file.exists()) {    //文件存在
            //创建workbook
            try {
                workbook = new HSSFWorkbook(new FileInputStream(file));
                //添加Worksheet（不添加sheet时生成的xls文件打开时会报错)
                HSSFSheet sheet = workbook.getSheet(sheetName);
                if (sheet != null)
                    flag = true;
            } catch (Exception e) {
                throw e;
            }

        } else {    //文件不存在
            flag = false;
        }
        return flag;
    }


    /**
     * 创建新excel.
     *
     * @param fileDir
     *         excel的路径
     * @param sheetName
     *         要创建的表格索引
     * @param titleRow
     *         excel的第一行即表格头
     *
     * @throws Exception
     *         the exception
     */
    public static void createExcel(String fileDir, String sheetName, List<String> titleRow) throws Exception {
        //创建workbook
        workbook = new HSSFWorkbook();
        //添加Worksheet（不添加sheet时生成的xls文件打开时会报错)
        HSSFSheet sheet1 = workbook.createSheet(sheetName);
        //新建文件
        FileOutputStream out = null;
        try {
            //添加表头
            HSSFRow row = workbook.getSheet(sheetName).createRow(0);    //创建第一行
            HSSFFont font = workbook.createFont();
            font.setCharSet(HSSFFont.DEFAULT_CHARSET);
            font.setFontName("宋体");
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            HSSFCellStyle cellStyle = workbook.createCellStyle();
            cellStyle.setFont(font);
            for (short i = 0; i < titleRow.size(); i++) {
                HSSFCell cell = row.createCell(i);
                cell.setCellValue(titleRow.get(i));
            }
            out = new FileOutputStream(fileDir);
            workbook.write(out);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 删除文件.
     *
     * @param fileDir
     *         文件路径
     *
     * @return the boolean
     */
    public static boolean deleteExcel(String fileDir) {
        boolean flag = false;
        File file = new File(fileDir);
        // 判断目录或文件是否存在
        if (!file.exists()) {  // 不存在返回 false
            return flag;
        } else {
            // 判断是否为文件
            if (file.isFile()) {  // 为文件时调用删除文件方法
                file.delete();
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 往excel中写入(已存在的数据无法写入).
     *
     * @param fileDir
     *         文件路径
     * @param sheetName
     *         表格索引
     * @param mapList
     *         the map list
     *
     * @throws Exception
     *         the exception
     */
    public static void writeToExcel(String fileDir, String sheetName, List<Map> mapList) throws Exception {
        //创建workbook
        File file = new File(fileDir);
        try {
            workbook = new HSSFWorkbook(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //流
        FileOutputStream out = null;
        HSSFSheet sheet = workbook.getSheet(sheetName);
        // 获取表格的总行数
        // int rowCount = sheet.getLastRowNum() + 1; // 需要加一
        // 获取表头的列数
        int columnCount = sheet.getRow(0).getLastCellNum();

        // 获得表头行对象
        HSSFRow titleRow = sheet.getRow(0);
        if (titleRow != null) {
            for (int rowId = 0; rowId < mapList.size(); rowId++) {
                Map map = mapList.get(rowId);
                HSSFRow newRow = sheet.createRow(rowId + 1);
                for (short columnIndex = 0; columnIndex < columnCount; columnIndex++) {  //遍历表头
                    String mapKey = titleRow.getCell(columnIndex).toString().trim().toString().trim();
                    HSSFCell cell = newRow.createCell(columnIndex, 0);
                    cell.setCellValue(map.get(mapKey) == null ? null : map.get(mapKey).toString());
                }
            }
        }
        wirteToWorkBook(fileDir, out);
    }

    private static void wirteToWorkBook(String fileDir, FileOutputStream out) throws Exception {
        try {
            out = new FileOutputStream(fileDir);
            workbook.write(out);
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Write to excel.
     *
     * @param fileDir
     *         the file dir
     * @param sheetName
     *         the sheet name
     * @param dataMap
     *         the data map
     * @param companyIDs
     *         the company i ds
     * @param dates
     *         the dates
     * @param companyName
     *         the company name
     *
     * @throws Exception
     *         the exception
     */
    public static void writeToExcel(String fileDir, String sheetName,
                                    Map<Integer, List<String>> dataMap,
                                    List<String> companyIDs, List<String> dates, String companyName) throws Exception {
        //创建workbook
        File file = new File(fileDir);
        try {
            workbook = new HSSFWorkbook(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //流
        FileOutputStream out = null;
        HSSFSheet sheet = workbook.getSheet(sheetName);
        HSSFFont font = workbook.createFont();
        font.setCharSet(HSSFFont.DEFAULT_CHARSET);
        font.setFontName("宋体");
        font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        //        cellStyle.setFillBackgroundColor(BLUE.index);
        // 获取表格的总行数
        int rowCount = sheet.getLastRowNum(); // 需要加一
        // 获取表头的列数
        int columnCount = sheet.getRow(0).getLastCellNum();

        System.out.println("current line is " + rowCount);
        // 获得表头行对象
        HSSFRow titleRow = sheet.getRow(0);
        if (titleRow != null) {
            for (int rowId = 0; rowId < dataMap.size(); rowId++) {
                HSSFRow newRow = sheet.createRow(rowId + rowCount + 1);
                //company name
                if (rowId == 0) {
                    String companyId = companyIDs.get((rowId / monthNums)).trim().toUpperCase();
                    newRow.createCell(1).setCellValue(companyId);
                } else {
                    newRow.createCell(1).setCellValue("");
                }
                newRow.createCell(0).setCellValue(companyName);
                //trade date
                newRow.createCell(2).setCellValue(dates.get(rowId));
                List<String> cols = dataMap.get(HtmlParser.monthIds.get(rowId));
                if (cols == null || cols.isEmpty()) {
                    continue;
                }
                for (short columnIndex = 0; columnIndex < columnCount - 3 && columnIndex < cols.size(); columnIndex++) {  //遍历表头
                    HSSFCell cell = newRow.createCell(columnIndex + 3, 0);
                    if (cols.get(columnIndex) != null && !cols.get(columnIndex).isEmpty()) {
                        cell.setCellValue(Double.valueOf(cols.get(columnIndex)));
                    } else {
                        cell.setCellValue("");
                    }
                }
            }
        }
        wirteToWorkBook(fileDir, out);
    }

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
        //        判断文件是否存在
        //        System.out.println(sheetExist("./src/main/resources/Book1.xls", "Sheet1"))

    }
}