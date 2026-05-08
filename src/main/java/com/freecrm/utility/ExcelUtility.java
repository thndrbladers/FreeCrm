package com.freecrm.utility;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class ExcelUtility {

	XSSFWorkbook workbook;
	XSSFSheet sheet;

	public ExcelUtility(String sheetName, String filePath) {
		try (FileInputStream is = new FileInputStream(filePath)) {
			this.workbook = new XSSFWorkbook(is);
			this.sheet = workbook.getSheet(sheetName);

			System.out.println("Excel file loaded successfully: " + filePath);

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
				temp[j] = sheet.getRow(i + 1).getCell(j).getStringCellValue();
			}
			oArray.add(temp);
		}

		return oArray;

	}

	/*
	 * public static void main(String[] args) { ExcelUtility excelUtility = new
	 * ExcelUtility("TestData", System.getProperty("user.dir") +
	 * "/src/main/java/com/freecrm/testdata/selenium_test_data_10_rows.xlsx");
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
	 * System.out.println(list.toString());
	 * 
	 * }
	 */

}
