import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public final class MergeData {

    private static List<String> newHead = new LinkedList<String>();
    private static List<String> oldHead = new LinkedList<String>();
    private static List<String> matchValue = new LinkedList<String>();
    private static List<Integer> newVsOld = new LinkedList<Integer>();

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
            newVsOld.add(oldColCount);
            oldColCount++;
        }

        int newColCount = 0;
        while (!newSheet.getRow(0).getCell(newColCount).getStringCellValue().isEmpty()) {
            newHead.add(newSheet.getRow(0).getCell(newColCount).getStringCellValue());
            newColCount++;
        }


        for (int i = 0; i < rowCount; i++) {
            matchValue.add(matchSheet.getRow(i).getCell(0).getStringCellValue());
            System.out.println(matchValue.get(i));
        }

        for (int j = 3; j < oldColCount && j < matchValue.size(); j++) {
            for (int i = 3; i < newColCount; i++) {
                if (newHead.get(i).equals(matchValue.get(j - 3))) {
                    newVsOld.set(j, i);
                    System.out.print(newVsOld.get(j) + " " + i + " " + j + " end");
                    break;
                }
            }
        }

    }

    public static void main(String[] args) {
        getTitle();
        for (int i = 0; i < newHead.size(); i++) {
            System.out.println(i + "  ");
            System.out.println(newHead.get(newVsOld.get(i)));
            System.out.println(i >= oldHead.size() ? " " : oldHead.get(i));
        }
    }
}
