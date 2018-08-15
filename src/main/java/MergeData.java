import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;

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
    private static boolean needGenNewMergeFile = true;
    private static List<String> newHead = new LinkedList<String>();
    private static List<String> oldHead = new LinkedList<String>();
    private static List<String> matchValue = new LinkedList<String>();

    private static String[] titles = {" 笔均融资金额(万元)", "历史项目逾期金额(万元)", "历史项目逾期率(%)"};

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

    private static List<Institute> markData(String fileName) {

        List<Institute> instituteList = new LinkedList<Institute>();

        File fileNewMerge = new File("./resources/" + fileName);
        if (!fileNewMerge.exists()) {
            log.error(fileName + " file doesn't exist.");
        }
        HSSFWorkbook workbookMerge = null;

        try {
            workbookMerge = new HSSFWorkbook(new FileInputStream(fileNewMerge));
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e);
        }

        assert workbookMerge != null;
        HSSFSheet mergeSheet = workbookMerge.getSheet("sheet1");

        int rowCount = mergeSheet.getLastRowNum() + 1;

        for (int i = 1; i < rowCount; i++) {
            HSSFRow readRow = workbookMerge.getSheet("sheet1").getRow(i);
            Cell cell = readRow.getCell(0);
            String instituteName;

            if (cell != null) {
                try {
                    instituteName = cell.getStringCellValue();
                } catch (Exception e) {
                    instituteName = "";
                }

                if (!instituteName.isEmpty()) {
                    Institute institute = new Institute();
                    institute.setBegin(i);
                    institute.setName(instituteName);
                    institute.setInstituteNo(readRow.getCell(1).getStringCellValue());
                    instituteList.add(institute);
                }
            }
        }


        for (int i = 0; i < instituteList.size() - 1; i++) {
            Institute institute = instituteList.get(i);
            institute.setEnd(instituteList.get(i + 1).getBegin() - 1);
            institute.setLength(institute.getEnd() - institute.getBegin() + 1);
        }

        return instituteList;
    }

    private static void mergePerInstitute(Institute newI, Institute old) {

        File fileOld = new File("./resources/old.xls");
        if (!fileOld.exists()) {
            log.error("old excel file doesn't exist.");
        }
        HSSFWorkbook oldWorkbook = null;

        try {
            oldWorkbook = new HSSFWorkbook(new FileInputStream(fileOld));
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e);
        }

        FileOutputStream outputStream = null;

        assert oldWorkbook != null;

        HSSFSheet oldSheet = oldWorkbook.getSheet("sheet1");

        File fileMerge = new File("./resources/new_merge.xls");

        if (!fileMerge.exists()) {
            log.error("file merge excel doesn't exist");
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

        File fileFinal = new File("./resources/final.xls");

        if (!fileFinal.exists()) {
            log.error("file final excel doesn't exist");
        }

        HSSFWorkbook workbookFinal = null;

        try {
            workbookFinal = new HSSFWorkbook(new FileInputStream(fileFinal));
        } catch (IOException e) {
            log.error(e);
            e.printStackTrace();
        }

        assert workbookFinal != null;
        HSSFSheet finalSheet = workbookFinal.getSheet("sheet1");

        int start = finalSheet.getLastRowNum() + 1;
        int originBegin = newI.getBegin();
        // first write the new data
        writeExcel(start, start + newI.getLength(), newI.getBegin(), matchValue.size() + 3,
                workbookMerge, workbookFinal);
        // second write the old data

        writeExcel(start + newI.getLength(), start + newI.getLength() + old.getLength(), old.getBegin(), oldHead.size(),
                oldWorkbook, workbookFinal);

        writeToFile(fileFinal, workbookFinal);
    }

    private static void writeExcel(int start, int end, int originPoint, int colNum,
                                   HSSFWorkbook workbookRead, HSSFWorkbook workbookWrite) {
        for (int row = start; row < end; row++, originPoint++) {
            log.debug("current line start index : " + row);
            for (int col = 0; col < colNum; col++) {
                HSSFRow writeRow = null;
                HSSFRow readRow = workbookRead.getSheet("sheet1").getRow(originPoint);

                if (col == 0) {
                    writeRow = workbookWrite.getSheet("sheet1").createRow(row);
                } else {
                    writeRow = workbookWrite.getSheet("sheet1").getRow(row);
                }

                HSSFCell cell = writeRow.createCell(col);
                Cell originCel = readRow.getCell(col);

                writeCellValue(col, cell, originCel);
            }
        }
    }

    private static void writeCellValue(int col, HSSFCell cell, Cell originCell) {
        if (originCell != null) {

            if (col < 3) {
                try {
                    cell.setCellValue(originCell.getStringCellValue());
                } catch (Exception e) {
                    cell.setCellValue("");
                }
            } else {
                try {
                    cell.setCellValue(originCell.getNumericCellValue());
                } catch (Exception e) {
                    cell.setCellValue("");
                }

            }
        }
    }


    private static void writeAllNewExcelByNewIndexHead(List<String> newHead) {

        File fileNew = new File("./resources/new.xls");
        if (!fileNew.exists()) {
            log.error("new excel file doesn't exist.");
        }
        HSSFWorkbook workbook = null;
        try {
            workbook = new HSSFWorkbook(new FileInputStream(fileNew));
        } catch (IOException e) {
            e.printStackTrace();
        }

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

                writeCellValue(col, cell, originCell);
            }
        }

        writeToFile(fileMerge, workbookMerge);
    }

    private static void writeToFile(File fileNew, HSSFWorkbook workbook) {

        FileOutputStream out = null;

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


    private static void writeNewExcelHead(List<String> newHead, String fileName) {

        String fileName1 = "./resources/" + fileName;

        int count = 0;
        for (int i = 0; i < newHead.size(); i++) {
            if (newHead.get(i).isEmpty()){
                newHead.set(i, titles[count++]);
            }
        }

        try {
            if (ExcelWriter.fileExist(fileName)) {
                ExcelWriter.deleteExcel(fileName);
            }
            ExcelWriter.createExcel(fileName1, "sheet1", newHead);
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
    public static void combineFile() {

        if (needGenNewMergeFile) {
            String fileName = "./resources/new_merge.xls";
            List<String> newExcelHead = genNewExcelHead();
            writeNewExcelHead(newExcelHead, "new_merge.xls");
            writeAllNewExcelByNewIndexHead(newExcelHead);
        }

        List<Institute> mergeInstitutes = markData("new_merge.xls");
        List<Institute> oldInstitutes = markData("old.xls");
        writeNewExcelHead(genNewExcelHead(), "final.xls");

        for (Institute institute : mergeInstitutes) {
            for (Institute instituteOld : oldInstitutes) {
                if (institute.getInstituteNo().equals(instituteOld.getInstituteNo())) {
                    log.debug("find old match company, name is : " + institute.getName()
                    + " NO is : " + institute.getInstituteNo());
                    mergePerInstitute(institute, instituteOld);
                    break;
                }
            }
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

        combineFile();
        System.out.println(markData("old.xls"));
    }


}
