package com.freecrm.pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;

import com.freecrm.base.Base;

public class DealsPage extends Base {

	@FindBy(xpath = "//table/tbody//tr")
	List<WebElement> noOfTableRows;

	@FindBy(xpath = "//table/tbody//tr[position()=1]/td[position()>1 and position()<last()]")
	List<WebElement> noOfTableColumns;

	@FindBy(xpath = "//table//a/button/i[@class='edit icon']")
	List<WebElement> editButton;

	public DealsPage() {

		PageFactory.initElements(getDriver(), this);

	}

	public List<Object[]> getDealsTabularData() {

		getWait().until(ExpectedConditions.visibilityOfAllElements(editButton));

		List<Object[]> webTableData = new ArrayList<>();

		Object[] colData;

		for (int i = 1; i <= noOfTableRows.size(); i++) {

			colData = new Object[noOfTableColumns.size()];

			for (int j = 1; j <= noOfTableColumns.size(); j++) {
				colData[j - 1] = getWebTableCellData(i, j + 1);
			}

			webTableData.add(colData);

		}

		return webTableData;

	}

	public String getWebTableCellData(int row, int col) {

		return getDriver().findElement(By.xpath("//table/tbody//tr[" + row + "]//td[" + col + "]")).getText();

	}

}
