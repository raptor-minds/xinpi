import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
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

    private static int noChange = 38;
    private static List<String> newHead = new LinkedList<String>();
    private static List<String> oldHead = new LinkedList<String>();
    private static List<String> matchValue = new LinkedList<String>();

    // the new head changed index contain the first three columns in the new head.
    private static List<Integer> newHeadChangedIndex = new LinkedList<Integer>();
    private static Logger log = Logger.getLogger(MergeData.class.getName());

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

    private static void mergePerInstitute() {

    }


    private static void writeAllNewExcelByNewIndexHead(List<String> newHead) {

        File fileNew = new File("./resources/new.xls");
        if (!fileNew.exists()) {
            log.error("test doesn't exist.");
        }
        HSSFWorkbook workbook = null;
        try {
            workbook = new HSSFWorkbook(new FileInputStream(fileNew));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //流
        FileOutputStream out = null;

        assert workbook != null;
        HSSFSheet newSheet = workbook.getSheet("sheet1");

        File fileMerge = new File("./resources/new_merge.xls");
        if (!fileMerge.exists()) {
            log.error("new merge file doesn't exist");
        }

        HSSFWorkbook workbookMerge = null;

        try {
            workbookMerge = new HSSFWorkbook(new FileInputStream(fileMerge));
        } catch (IOException e) {
            log.error(e);
            e.printStackTrace();
        }

        assert workbookMerge != null;
        HSSFSheet mergeSheet = workbookMerge.getSheet("sheet1");

        int rowCount = newSheet.getLastRowNum() + 1;

        for (int col = 0; col < newHead.size(); col++) {
            for (int row = 1; row < rowCount; row++) {
                HSSFRow writeRow = null;
                HSSFRow readRow = workbook.getSheet("sheet1").getRow(row);

                if (col == 0) {
                    writeRow = workbookMerge.getSheet("sheet1").createRow(row);
                } else {
                    writeRow = workbookMerge.getSheet("sheet1").getRow(row);
                }

                HSSFCell cell = writeRow.createCell(col);
                Cell originCell = null;

                originCell = readRow.getCell(newHeadChangedIndex.get(col));

                if (originCell != null) {

                    if (col < 3) {
                        try {
                            cell.setCellValue(originCell.getStringCellValue());
                        } catch (Exception e){
                            cell.setCellValue("");
                        }
                    } else {
                        try {
                            cell.setCellValue(originCell.getNumericCellValue());
                        } catch (Exception e){
                            cell.setCellValue("");
                        }

                    }
                }
            }
        }

        writeToFile(fileMerge, workbookMerge, out);
    }

    private static void writeToFile(File fileNew, HSSFWorkbook workbook, FileOutputStream out) {
        try {
            out = new FileOutputStream(fileNew);
            workbook.write(out);
        } catch (Exception e) {
            try {
                throw e;
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                assert out != null;
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private static void writeNewExcelHead(List<String> newHead) {

        String fileName = "./resources/new_merge.xls";

        try {
            if (ExcelWriter.fileExist(fileName)) {
                ExcelWriter.deleteExcel(fileName);
            }
            ExcelWriter.createExcel(fileName, "sheet1", newHead);
        } catch (Exception e) {
            log.error(e);
            e.printStackTrace();
        }
    }

    private static List<String> genNewExcelHead() {
        List<String> newExcelHead = new LinkedList<String>();

        // first three elements are the fixed date info
        for (int i = 0; i < 3; i++) {
            newExcelHead.add(newHead.get(i));
        }

        for (int i = 3; i < matchValue.size(); i++) {
            newExcelHead.add(matchValue.get(i - 3));
        }

        return newExcelHead;
    }

    /**
     * Write data in the combine file.
     */
    public static void writeDataInTheCombineFile() {
        String fileName = "./resources/new_merge.xls";
        List<String> newExcelHead = genNewExcelHead();
        writeNewExcelHead(newExcelHead);
        writeAllNewExcelByNewIndexHead(newExcelHead);
    }

    /**
     * Swap all columns in the new excel.
     *
     * @param newHeadChangedIndex
     *         the new head changed index
     */
    public static void swapAllColumnsInTheNewExcel(List<Integer> newHeadChangedIndex) {

        for (int indexOrigin = 0; indexOrigin < newHead.size(); indexOrigin++) {

            File file = new File("./resources/new.xls");
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

            assert workbook != null;
            HSSFSheet newSheet = workbook.getSheet("sheet1");

            int rowCount = newSheet.getLastRowNum() + 1;

            int indexDes = newHeadChangedIndex.get(indexOrigin);

            if (indexOrigin == indexDes) {
                continue;
            }

            String tempHead = "";
            // do swapping

            // swap header
            Cell originHeadCell = newSheet.getRow(0).getCell(indexOrigin);
            Cell desHeadCell = newSheet.getRow(0).getCell(indexDes);
            DataFormatter dataFormatter = new DataFormatter();
            String originHeadStr = dataFormatter.formatCellValue(originHeadCell);
            tempHead = String.copyValueOf(originHeadStr.toCharArray());
            String desHeadStr = dataFormatter.formatCellValue(desHeadCell);

            //            String originHeadStr = newHead.get(indexOrigin);
            //            if (indexDes >= 40) {
            //                desHeadStr = "";
            //            } else {
            //                desHeadStr = newHead.get(indexDes);
            //            }
            //            tempHead = String.copyValueOf(originHeadStr.toCharArray());

            if (originHeadCell == null) {
                originHeadCell = newSheet.getRow(0).createCell(indexOrigin);
            }
            originHeadCell.setCellValue(String.valueOf(desHeadStr));
            log.debug("original cell [ " + indexOrigin + "" +
                    " ] change from " + originHeadStr + " to [ " + indexDes + " ] " + desHeadStr);

            if (desHeadCell == null) {
                desHeadCell = newSheet.getRow(0).createCell(indexDes);
            }
            desHeadCell.setCellValue(tempHead);

            // swap data under header
            Double temp = null, desValue = null;
            for (int i = 1; i < rowCount; i++) {
                Cell originCell = newSheet.getRow(i).getCell(indexOrigin);
                Cell desCell = newSheet.getRow(i).getCell(indexDes);

                temp = getCell(newSheet, temp, i, originCell);

                desValue = getCell(newSheet, desValue, i, desCell);

                if (desCell == null) {
                    desCell = newSheet.getRow(i).createCell(indexDes);
                }

                if (originCell == null) {
                    originCell = newSheet.getRow(i).createCell(indexOrigin);
                }

                if (desValue != null) {
                    originCell.setCellValue(desValue);
                } else {
                    originCell.setCellValue("");
                }

                if (temp != null) {
                    desCell.setCellValue(temp);
                } else {
                    desCell.setCellValue("");
                }
            }
            writeToFile(file, workbook, out);
        }


    }

    private static void printExcelHead(String excelName, List<Integer> indexList) {
        File file = new File("./resources/" + excelName);
        if (!file.exists()) {
            System.out.println(excelName + " doesn't exist.");
        }
        HSSFWorkbook workbook = null;
        try {
            workbook = new HSSFWorkbook(new FileInputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        //流
        FileOutputStream out = null;

        assert workbook != null;
        HSSFSheet newSheet = workbook.getSheet("sheet1");

        int count = 0;
        if (indexList.isEmpty()) {
            for (int i = 0; i < 42; i++) {
                Cell cell = newSheet.getRow(0).getCell(i);
                if (cell != null) {
                    System.out.print("[ " + i + " ] " + cell.getStringCellValue() + "  ");
                    count++;
                } else {
                    System.out.print("[ " + i + " ] hello world ");
                }
                if (i % 10 == 0) {
                    System.out.println();
                }
            }

        } else {
            for (int i = 0; i < indexList.size(); i++) {
                Cell cell = newSheet.getRow(0).getCell(indexList.get(i));
                if (cell != null) {
                    System.out.print("[ " + i + " ] " + cell.getStringCellValue() + "  ");
                    count++;
                } else {
                    System.out.print("[ " + i + " ] hello world ");
                }
                if (i % 10 == 0) {
                    System.out.println();
                }
            }
        }
        System.out.println("total num is :" + count);
    }

    private static Double getCell(HSSFSheet newSheet, Double temp, int i, Cell cell) {
        if (cell != null) {
            try {
                temp = cell.getNumericCellValue();
            } catch (Exception e) {
                temp = null;
            }
        }
        return temp;
    }


    /**
     * The entry point of application.
     *
     * @param args
     *         the input arguments
     */
    public static void main(String[] args) {
        getTitle();
        //        for (int i = 0; i < newHead.size(); i++) {
        //            System.out.println(i + "  " + newHeadChangedIndex.get(i));
        //            System.out.println(newHeadChangedIndex.get(i) >= 38 ? " " : newHead.get(newHeadChangedIndex.get(i)));
        //            System.out.println(i >= oldHead.size() ? " " : oldHead.get(i));
        //        }

        System.out.println("\n ******************** \n");
        for (int i = 0; i < newHeadChangedIndex.size(); i++) {
            System.out.print("index" + i + " " + newHeadChangedIndex.get(i) + " ");
        }

        //        printExcelHead("new_back.xls", newHeadChangedIndex);
        //
        //        printExcelHead("new.xls", new ArrayList<Integer>());
        //        swapAllColumnsInTheNewExcel(newHeadChangedIndex);
        //        printExcelHead("new.xls", new ArrayList<Integer>());
        //
        //        System.out.println(genNewExcelHead());

        writeDataInTheCombineFile();
    }


}
