package com.freecrm.reports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.net.InetAddress;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Singleton that initialises ExtentReports once per suite run.
 * Thread-safe via double-checked locking.
 */
public class ExtentReportManager {

    private static volatile ExtentReports extentReports;

    private ExtentReportManager() {
    }

    /**
     * Primary entry point – called from ExtentReportsListener.onStart().
     *
     * @param suiteName   from {@code <suite name="...">} in TestNG XML
     * @param author      from {@code <parameter name="author">} in TestNG XML; falls back to OS username
     * @param application from {@code <parameter name="application">} in TestNG XML; falls back to suite name
     */
    public static ExtentReports getInstance(String suiteName, String author, String application) {
        if (extentReports == null) {
            synchronized (ExtentReportManager.class) {
                if (extentReports == null) {
                    extentReports = createInstance(suiteName, author, application);
                }
            }
        }
        return extentReports;
    }

    /** Fallback overload – suite name only. */
    public static ExtentReports getInstance(String suiteName) {
        return getInstance(suiteName, null, null);
    }

    /** Fallback – nothing available yet. */
    public static ExtentReports getInstance() {
        return getInstance("TestSuite", null, null);
    }

    private static ExtentReports createInstance(String suiteName, String author, String application) {

        String timestamp    = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
        String safeFileName = suiteName.replaceAll("[^a-zA-Z0-9_\\-]", "_");
        String reportPath   = System.getProperty("user.dir")
                + "/test-output/extent-reports/" + safeFileName + "_" + timestamp + ".html";

        ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);

        // ── Visual configuration ───────────────────────────────────────────
        String appName = resolveValue(application, "Free CRM");
        spark.config().setTheme(Theme.DARK);
        spark.config().setDocumentTitle(appName + " – " + suiteName);
        spark.config().setReportName(suiteName);
        spark.config().setEncoding("utf-8");

        ExtentReports er = new ExtentReports();
        er.attachReporter(spark);

        // ── System info ────────────────────────────────────────────────────
        // From TestNG XML <parameter> tags (cannot be auto-detected):
        er.setSystemInfo("Application",  appName);
        er.setSystemInfo("Author",       resolveValue(author, System.getProperty("user.name")));

        // Auto-detected:
        er.setSystemInfo("Environment",  resolveEnvironment());
        er.setSystemInfo("Browser",      resolveBrowser());
        er.setSystemInfo("OS",           System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ")");
        er.setSystemInfo("Java Version", System.getProperty("java.version"));
        er.setSystemInfo("Host",         resolveExecutionHost());

        System.out.println("[ExtentReports] Report will be generated at: " + reportPath);
        return er;
    }

    // ── Flush ──────────────────────────────────────────────────────────────

    public static void flush() {
        if (extentReports != null) {
            extentReports.flush();
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────

    private static String resolveValue(String value, String fallback) {
        return (value != null && !value.isBlank()) ? value : fallback;
    }

    private static String resolveEnvironment() {
        String env = System.getProperty("env");
        if (env == null || env.isBlank()) env = System.getenv("ENV");
        return (env == null || env.isBlank()) ? "svt" : env;
    }

    private static String resolveBrowser() {
        // 1. -Dbrowser system property (e.g. mvn test -Dbrowser=edge)
        String browser = System.getProperty("browser");
        if (browser != null && !browser.isBlank()) return browser;
        // 2. properties file via Base (reads env-specific file e.g. svt.properties)
        try { browser = new com.freecrm.base.Base().getProperty("browser"); } catch (Exception ignored) {}
        return (browser != null && !browser.isBlank()) ? browser : "chrome";
    }

    /**
     * Detects WHERE tests are running.
     * CI/CD platforms expose well-known environment variables.
     * Falls back to hostname + local OS for developer machines.
     */
    private static String resolveExecutionHost() {

        if (isEnvSet("JENKINS_URL") || isEnvSet("JENKINS_HOME"))
            return "Jenkins CI [" + hostname() + "]";

        if (isEnvSet("GITHUB_ACTIONS"))
            return "GitHub Actions [runner: " + envOrDefault("RUNNER_NAME", "unknown") + "]";

        if (isEnvSet("GITLAB_CI"))
            return "GitLab CI [" + envOrDefault("CI_RUNNER_DESCRIPTION", hostname()) + "]";

        if (isEnvSet("CIRCLECI"))
            return "CircleCI [" + hostname() + "]";

        if (isEnvSet("TF_BUILD"))                          // Azure DevOps
            return "Azure DevOps [" + envOrDefault("AGENT_NAME", hostname()) + "]";

        if (isEnvSet("CI"))                                // Generic CI flag
            return "CI/CD [" + hostname() + "]";

        // ── Local machine ──────────────────────────────────────────────────
        String os = System.getProperty("os.name", "").toLowerCase();
        String platform = os.contains("win") ? "Windows" : os.contains("mac") ? "macOS" : "Linux";
        return platform + " Local [" + hostname() + "]";
    }

    private static boolean isEnvSet(String key) {
        String v = System.getenv(key);
        return v != null && !v.isBlank();
    }

    private static String envOrDefault(String key, String def) {
        String v = System.getenv(key);
        return (v != null && !v.isBlank()) ? v : def;
    }

    private static String hostname() {
        try { return InetAddress.getLocalHost().getHostName(); }
        catch (Exception e) { return "unknown-host"; }
    }
}