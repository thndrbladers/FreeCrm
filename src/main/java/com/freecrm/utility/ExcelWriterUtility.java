package com.freecrm.utility;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ExcelWriterUtility {

	XSSFWorkbook workbook;
	XSSFSheet sheet;
	List<Object[]> tableData;
	FileOutputStream fos;
	private static final String BASE_PATH = System.getProperty("user.dir") + "/src/test/resources/output/";

	public ExcelWriterUtility(String sheetName, String fileName) {

		try {
			fos = new FileOutputStream(BASE_PATH + fileName);
			this.workbook = new XSSFWorkbook();
			this.sheet = workbook.createSheet(sheetName);
			System.out.println("Excel file loaded successfully: " + BASE_PATH + fileName);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ExcelWriterUtility(String sheetName) {
		this(sheetName, "output_File" + System.currentTimeMillis());
	}

	public ExcelWriterUtility() {
		this("output_sheet", "output_File" + System.currentTimeMillis() + ".xlsx");
	}

	public void writeTableInExcel(List<Object[]> tableData) {

		int rowSize = tableData.size();
		int colSize = tableData.get(0).length;
		Row row;

		for (int i = 0; i < rowSize; i++) {

			row = sheet.createRow(i);
			for (int j = 0; j < colSize; j++) {
				Object[] temp = tableData.get(i);

				row.createCell(j).setCellValue(temp[j] == null ? "" : temp[j].toString());
			}
		}

		try {
			workbook.write(fos);
		} catch (IOException e) {
			e.printStackTrace();
		}
		closeWorkbook();

	}

	public List<Object[]> writeTableInExcel(WebDriver driver) {

		WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
		wait.until(ExpectedConditions
				.invisibilityOf(driver.findElement(By.xpath("//div[contains(@class,'inline loader')]"))));

		int webTablerowCount = driver.findElements(By.xpath("//table/tbody/tr")).size();
		int webTableColCount = driver.findElements(By.xpath("//table/tbody/tr[1]/td")).size();

		List<Object[]> tableData = new ArrayList<>();

		Object[] temp;

		for (int i = 0; i < webTablerowCount; i++) {
			temp = new Object[webTableColCount];
			for (int j = 0; j < webTableColCount; j++) {
				temp[j] = driver.findElement(By.xpath("//table/tbody/tr[" + (i + 1) + "]/td[" + (j + 1) + "]"))
						.getText();
			}
			tableData.add(temp);
		}

		writeTableInExcel(tableData);

		return tableData;

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
