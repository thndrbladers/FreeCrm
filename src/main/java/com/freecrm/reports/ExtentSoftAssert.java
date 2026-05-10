package com.freecrm.reports;

import org.testng.asserts.IAssert;
import org.testng.asserts.SoftAssert;
import org.testng.collections.Maps;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;

import java.util.Map;

/**
 * Drop-in replacement for TestNG {@link SoftAssert} that logs every assertion
 * – pass or fail – with expected vs actual values directly into the current
 * thread's ExtentReports test node.
 *
 * <p>Usage in test classes:
 * <pre>
 *   ExtentSoftAssert sa = new ExtentSoftAssert();
 *   sa.assertEquals(actual, expected, "Page title");
 *   sa.assertTrue(condition, "Button visible");
 *   sa.assertAll(); // still throws on failures as normal
 * </pre>
 */
public class ExtentSoftAssert extends SoftAssert {

    /* Keeps failed assertions for the final assertAll() call, same as SoftAssert. */
    private final Map<AssertionError, IAssert<?>> failedAssertions = Maps.newLinkedHashMap();

    // ── Core hooks ────────────────────────────────────────────────────────

    @Override
    public void onAssertSuccess(IAssert<?> assertCommand) {
        logToReport(Status.PASS, assertCommand, null);
    }

    @Override
    public void onAssertFailure(IAssert<?> assertCommand, AssertionError ex) {
        failedAssertions.put(ex, assertCommand);
        logToReport(Status.FAIL, assertCommand, ex);
    }

    // ── assertAll override – throw on collected failures ──────────────────

    @Override
    public void assertAll() {
        if (!failedAssertions.isEmpty()) {
            StringBuilder sb = new StringBuilder("The following assertions failed:\n");
            int idx = 1;
            for (Map.Entry<AssertionError, IAssert<?>> entry : failedAssertions.entrySet()) {
                sb.append(idx++).append(") ").append(entry.getKey().getMessage()).append("\n");
            }
            throw new AssertionError(sb.toString());
        }
    }

    // ── Report helper ─────────────────────────────────────────────────────

    private void logToReport(Status status, IAssert<?> assertion, AssertionError ex) {
        ExtentTest test = ExtentTestManager.getTest();
        if (test == null) return;

        String message    = assertion.getMessage() != null ? assertion.getMessage() : "Assertion";
        Object actual     = assertion.getActual();
        Object expected   = assertion.getExpected();

        String icon = (status == Status.PASS) ? "✔" : "✘";

        StringBuilder log = new StringBuilder();
        log.append("<b>").append(icon).append(" ").append(message).append("</b>");

        if (expected != null) {
            log.append("<br>&nbsp;&nbsp;Expected : <code>").append(expected).append("</code>");
        }
        if (actual != null) {
            log.append("<br>&nbsp;&nbsp;Actual   : <code>").append(actual).append("</code>");
        }
        if (ex != null && status == Status.FAIL) {
            log.append("<br>&nbsp;&nbsp;<span style='color:red'>").append(ex.getMessage()).append("</span>");
        }

        test.log(status, log.toString());
    }
}
