import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * The type Merge data.
 */
public final class MergeData {

    private static int noChange = 40;
    private static List<String> newHead = new LinkedList<String>();
    private static List<String> oldHead = new LinkedList<String>();
    private static List<String> matchValue = new LinkedList<String>();
    private static List<Integer> newHeadChangedIndex = new LinkedList<Integer>();

    /**
     * Gets title.
     */
    private static void getTitle() {

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

        HSSFSheet newSheet = workbook.getSheet("sheet2");

        HSSFSheet oldSheet = workbook.getSheet("sheet5");

        HSSFSheet matchSheet = workbook.getSheet("sheet6");

        // get the total columns of new and old sheet
        int rowCount = matchSheet.getLastRowNum() + 1;

        int oldColCount = 0;
        while (!oldSheet.getRow(0).getCell(oldColCount).getStringCellValue().isEmpty()) {
            oldHead.add(oldSheet.getRow(0).getCell(oldColCount).getStringCellValue());
            oldColCount++;
        }

        int newColCount = 0;
        while (!newSheet.getRow(0).getCell(newColCount).getStringCellValue().isEmpty()) {
            newHead.add(newSheet.getRow(0).getCell(newColCount).getStringCellValue());
            newColCount++;
        }


        for (int i = 0; i < rowCount; i++) {
            matchValue.add(matchSheet.getRow(i).getCell(0).getStringCellValue());
            newHeadChangedIndex.add(i);
            System.out.println(matchValue.get(i));
        }

        for (int j = 3; j < matchValue.size(); j++) {

            if (matchValue.get(j - 3).isEmpty()) {
                newHeadChangedIndex.set(j, noChange++);
                continue;
            }

            for (int i = 3; i < newColCount; i++) {
                if (null != newHead.get(i) && newHead.get(i).equals(matchValue.get(j - 3))) {
                    newHeadChangedIndex.set(j, i);
                    System.out.print(newHeadChangedIndex.get(j) + "old" + i + " " + j + " end");
                    break;
                }
            }
        }

    }

    public static void exchange2column(int indexOrigin, int indexDes) {

        File file = new File("./resources/test.xls");
        if (!file.exists()) {
            System.out.println("test doesn't exist.");
        }
        HSSFWorkbook workbook = null;
        try {
            workbook = new HSSFWorkbook(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //流
        FileOutputStream out = null;

        HSSFSheet newSheet = workbook.getSheet("sheet1");

        int rowCount = newSheet.getLastRowNum() + 1;

        // do swapping
        for (int i = 0; i < rowCount; i++) {
            Cell originCell = newSheet.getRow(i).getCell(indexOrigin);
            Cell desCell = newSheet.getRow(i).getCell(indexDes);
            DataFormatter dataFormatter = new DataFormatter();
            String originStr = dataFormatter.formatCellValue(originCell);
            String temp = String.copyValueOf(originStr.toCharArray());
            String desStr = dataFormatter.formatCellValue(desCell);
            originCell.setCellValue(desStr);
            desCell.setCellValue(temp);
        }

        try {
            out = new FileOutputStream(file);
            workbook.write(out);
        } catch (Exception e) {
            try {
                throw e;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * The entry point of application.
     *
     * @param args
     *         the input arguments
     */
    public static void main(String[] args) {
        getTitle();
        for (int i = 0; i < newHead.size(); i++) {
            System.out.println(i + "  " + newHeadChangedIndex.get(i));
            System.out.println(newHeadChangedIndex.get(i) >= 40 ? " " : newHead.get(newHeadChangedIndex.get(i)));
            System.out.println(i >= oldHead.size() ? " " : oldHead.get(i));
        }

        for (int i = 0; i < newHeadChangedIndex.size(); i++) {
            System.out.print("index" + i + " " + newHeadChangedIndex.get(i) + " ");
        }
    }
}
