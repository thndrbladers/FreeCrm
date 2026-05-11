package com.freecrm.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class FreeCrmRetryAnalyzer implements IRetryAnalyzer {

	int startCount = 0;
	int maxCount = 2;

	@Override
	public boolean retry(ITestResult result) {
		if (startCount <= maxCount) {
			startCount++;
			return true;
		}

		return false;
	}

}
