package com.freecrm.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelReaderUtility {

	XSSFWorkbook workbook;
	XSSFSheet sheet;
	private static final String BASE_PATH = System.getProperty("user.dir") + "/src/main/java/com/freecrm/testdata/";

	public ExcelReaderUtility(String sheetName, String fileName) {

		try (FileInputStream fis = new FileInputStream(BASE_PATH + fileName)) {
			this.workbook = new XSSFWorkbook(fis);
			this.sheet = workbook.getSheet(sheetName);

			System.out.println("Excel file loaded successfully: " + BASE_PATH + fileName);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public Object[][] getExcelData() {
		int rowCount = sheet.getLastRowNum();
		int colCount = sheet.getRow(0).getLastCellNum();

		Object[][] temp = new Object[rowCount][colCount];

		for (int i = 0; i < rowCount; i++) {
			for (int j = 0; j < colCount; j++) {
				temp[i][j] = sheet.getRow(i + 1).getCell(j).getStringCellValue();
			}
		}
		closeWorkbook();
		return temp;

	}

	public List<Object[]> getExcelDataAsList() {
		int rowCount = sheet.getLastRowNum();
		int colCount = sheet.getRow(0).getLastCellNum();

		Object[] temp;
		List<Object[]> oArray = new ArrayList<>();

		for (int i = 0; i < rowCount; i++) {
			temp = new Object[colCount];
			for (int j = 0; j < colCount; j++) {
				temp[j] = sheet.getRow(i + 1).getCell(j).getStringCellValue() == null ? ""
						: sheet.getRow(i + 1).getCell(j).getStringCellValue();
			}
			oArray.add(temp);
		}

		closeWorkbook();
		return oArray;

	}

	public void closeWorkbook() {
		try {
			if (workbook != null) {
				workbook.close();
				System.out.println("Excel workbook closed successfully.");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * public static void main(String[] args) { ExcelReaderUtility excelUtility =
	 * new ExcelReaderUtility("TestData", "create_contacts_testdata.xlsx");
	 * Object[][] data = excelUtility.getExcelData();
	 * 
	 * for (Object[] row : data) { for (Object cell : row) { System.out.print(cell +
	 * "  "); } System.out.println(); }
	 * 
	 * List<Object[]> list = excelUtility.getExcelDataAsList();
	 * 
	 * for (Object[] o : list) { for (Object s : o) { System.out.print(s.toString()
	 * + "  "); }
	 * 
	 * System.out.println(); }
	 * 
	 * ExcelWriterUtility ew = new ExcelWriterUtility("test", "output.xlsx");
	 * ew.writeTableInExcel(list);
	 * 
	 * }
	 */

}
