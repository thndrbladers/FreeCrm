# Free CRM — Test Automation Framework

A clean, batteries-included **Selenium + TestNG + REST Assured** automation framework built around the [Free CRM](https://ui.freecrm.com/) application.

Think of this less as "a bunch of tests for one app" and more as a **starter skeleton you can grow into a full-blown framework** — for a personal side project, a professional QA team, or an enterprise test practice. The plumbing that usually takes weeks to get right (thread-safe drivers, reporting, logging, config-per-environment, retries, data-driven tests, API + DB validation) is already wired up. You just add pages and tests.

---

## Why this exists

Most "framework" repos are either a single `main()` method with a hard-coded ChromeDriver, or a 200-file monster you can't understand. This sits deliberately in the middle:

- **Small enough to read in an afternoon.**
- **Complete enough to run real UI, API and database checks in parallel with rich reports.**
- **Opinionated where it matters** (thread safety, separation of concerns) and **flexible where you'll want to customize** (browsers, environments, logging levels).

If you're learning automation, you can trace every layer. If you're starting a real project, you can delete the Free CRM specifics and keep the engine.

---

## What's in the box

| Capability | How it's done |
|---|---|
| **UI automation** | Selenium 4 with the Page Object Model + PageFactory |
| **API testing** | REST Assured with a reusable `ApiClient` and typed POJOs (Jackson) |
| **Database validation** | Oracle JDBC via a thread-safe `DatabaseUtil` |
| **Parallel execution** | TestNG suites run classes in parallel, driver isolated per thread |
| **Data-driven tests** | Excel (Apache POI) and JSON test data, plus JavaFaker for fake data |
| **Rich HTML reports** | ExtentReports with inline, click-to-zoom screenshots on every step |
| **Automatic screenshots & element highlighting** | A custom `WebDriverListener` scrolls to, outlines, and snapshots each element |
| **Smart logging** | Log4j2 with bridges so Selenium/POI/SLF4J logs all land in one file |
| **Auto-retry of flaky tests** | A global retry analyzer re-runs failures (up to 2 times) |
| **Multi-environment config** | Swap `svt` / `dev` / `prod` property files at runtime |
| **Soft assertions in reports** | `ExtentSoftAssert` logs every expected-vs-actual, pass or fail |

---

## Tech stack

- **Java 21**
- **Maven** (build & dependency management)
- **Selenium 4.44** — browser automation
- **TestNG 7.12** — test runner, suites, groups, parallelism, retries
- **REST Assured 6.0** — API testing
- **Jackson 3.2** — JSON ⇄ POJO mapping
- **Apache POI 5.5** — reading/writing Excel test data
- **JavaFaker** — generating realistic fake test data
- **ExtentReports 5.1** — HTML reporting
- **Log4j2 2.26** (+ JUL and SLF4J bridges) — logging
- **Oracle JDBC (ojdbc11)** — database checks

---

## How the project is organized

```
FreeCrm/
├── pom.xml                       # Dependencies + Maven Surefire (test runner) config
├── smoke.xml                     # TestNG suite: quick smoke checks
├── regression.xml                # TestNG suite: fuller regression run
├── FeatureXmlsByReleases/        # Suites grouped by release (e.g. Release_1)
│
├── src/main/java/com/freecrm/
│   ├── base/
│   │   ├── Base.java             # WebDriver factory, thread-local driver/wait, config
│   │   └── ConfigManager.java    # Singleton config loader (env-aware)
│   ├── pages/                    # Page Objects (LoginPage, HomePage, ContactPage, ...)
│   ├── apiclients/               # ApiClient + endpoint clients (Login, Companies)
│   ├── pojo/                     # Request/response models for the API
│   ├── listeners/                # WebDriver event listener + retry logic
│   ├── reports/                  # ExtentReports manager, step logger, soft asserts
│   ├── utility/                  # Excel read/write, screenshots, database access
│   └── testdata/                 # Excel + JSON data files
│
├── src/main/resources/
│   ├── svt.properties            # Config for the SVT environment (default)
│   ├── dev.properties            # Config for the Dev environment
│   ├── prod.properties           # Config for the Prod environment
│   └── log4j2.properties         # Logging configuration (heavily commented)
│
├── src/test/java/com/freecrm/
│   ├── tests/                    # Focused UI tests (e.g. login page)
│   ├── flows/                    # End-to-end business flows
│   └── apiTests/                 # API validation tests
│
└── logs/                         # Timestamped run logs land here
```

### The layers, in plain English

- **`Base`** is the heart. It reads the right properties file, spins up the browser you asked for (Chrome/Edge/Firefox/Safari, optionally headless), and stores the driver in a `ThreadLocal` so parallel tests never step on each other. Every driver is wrapped in an **event listener** so interactions are logged, highlighted, and screenshotted automatically.
- **Page Objects** (`pages/`) describe *what a page can do* using PageFactory `@FindBy` locators. Tests read like sentences: `loginPage.login(email, password)`.
- **API clients** (`apiclients/`) wrap REST Assured so your API tests aren't full of boilerplate. POJOs (`pojo/`) give you typed request/response bodies instead of raw JSON strings.
- **Reports & logging** (`reports/`, `utility/ScreenshotUtility`) turn every run into a shareable HTML report with screenshots, plus a detailed log file.
- **Config** (`ConfigManager`, `*.properties`) lets one codebase target many environments without a single code change.

---

## Getting started

### Prerequisites

- **Java 21** (JDK) installed and on your `PATH`
- **Maven 3.9+**
- A supported browser (Chrome by default — Selenium 4 manages the driver binary for you)
- *(Optional)* An Oracle database if you want to run the database-validation tests

Check your setup:

```bash
java -version
mvn -version
```

### Run the tests

The suite file is passed in at runtime (there's no default), so pick the suite you want:

```bash
# Smoke suite on the default (svt) environment
mvn test -DsuiteXmlFile=smoke.xml

# Full regression suite
mvn test -DsuiteXmlFile=regression.xml

# A release-specific suite
mvn test -DsuiteXmlFile=FeatureXmlsByReleases/Release_1/main.xml
```

### Pick an environment

Environment is resolved in this order: `-Denv` system property → `ENV` OS variable → falls back to `svt`.

```bash
mvn test -DsuiteXmlFile=smoke.xml -Denv=dev
```

This loads `dev.properties` (URL, credentials, timeouts, browser, DB settings, etc.).

### Choose a browser / go headless

Browser and headless mode come from the active properties file:

```properties
browser=chrome      # chrome | edge | firefox | safari
headless=false      # true to run without a visible window (great for CI)
```

---

## Where to find the results

- **HTML report** — an ExtentReports file is generated per run (author, application, OS, host, CI platform, Java version, environment and browser are captured automatically). Open it in any browser to see step-by-step logs and screenshots.
- **Screenshots** — saved to `src/test/resources/screenshots/` and also embedded (click-to-zoom) directly in the HTML report.
- **Logs** — a timestamped file appears in `logs/` (e.g. `FreeCrm_log_2026-07-14_21-44-57.txt`). Selenium, POI and SLF4J-based library logs are all funneled into this single file.

---

## Writing your own tests

### A UI test (Page Object style)

```java
public class FCRM_3_LoginFlow extends Base {

    LoginPage loginPage;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        initialization();               // launches browser, opens the app URL
        loginPage = new LoginPage();
    }

    @Test(description = "Verify that user can login with valid credentials",
          groups = { "smoke", "Release_1" })
    public void verify_valid_login() {
        ExtentSoftAssert sa = new ExtentSoftAssert();
        HomePage homePage = loginPage.login(Base.getConfig("username"),
                                            Base.getConfig("password"));
        sa.assertTrue(homePage.isHomeMenuDisplayed(), "Home menu should be visible");
        sa.assertAll();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        quitDriver();
    }
}
```

Notice: you never touch `WebDriver` directly in the test. The Page Object hides the locators, and the listener quietly logs and screenshots each action.

### An API test

```java
@Test(dataProvider = "loginData")
public void apiLoginVal(String email, String password, String expectedStatusCode) {
    LoginRequest lr = new LoginRequest();
    lr.setEmail(email);
    lr.setPassword(password);
    Assert.assertEquals(expectedStatusCode,
        String.valueOf(loginClient.getAuthRawResponse(lr).getStatusCode()));
}
```

### Data-driven with Excel

Drop an `.xlsx` file into `src/main/java/com/freecrm/testdata/` and feed it to a TestNG `@DataProvider`:

```java
@DataProvider(name = "loginData")
public Iterator<Object[]> loginData() {
    ExcelReaderUtility eru = new ExcelReaderUtility("td", "api_login_testdata.xlsx");
    return eru.getExcelDataAsList().iterator();
}
```

### Database validation

```java
List<Object[]> rows = DatabaseUtil.getQueryData("SELECT * FROM companies");
```

Connection details come from the active environment's `db.url`, `db.username`, `db.password`.

---

## Handy features you get for free

- **Parallel runs** — suites are configured with `parallel="classes"` and `thread-count="3"`. Because the driver lives in a `ThreadLocal`, tests stay isolated. Bump the thread count in the suite XML to scale up.
- **Automatic retries** — `FreeCrmAnnotationTransformer` applies `FreeCrmRetryAnalyzer` to every test, so a flaky failure is retried before being reported as failed. Add the transformer as a listener in your suite XML to enable it.
- **Test grouping** — tests are tagged with TestNG groups (`smoke`, `Release_1`, etc.). Run a subset by including a group in the suite's `<groups>` block.
- **Step logging** — call `StepLogger.info("...")`, `StepLogger.pass("...")`, etc. from anywhere to add a line (and optionally a screenshot) to the report.
- **Soft assertions that show up in the report** — `ExtentSoftAssert` records every assertion with expected-vs-actual, then still fails the test at `assertAll()`.

---

## Tuning logging

`src/main/resources/log4j2.properties` is thoroughly commented with "kill switches." A few common tweaks:

- Silence everything: `rootLogger.level = off`
- Quiet Selenium's internal chatter: set `logger.selenium.level = warn`
- Stop writing to disk: remove `file` from the appender refs
- Keep history across runs: set `appender.file.append = true`

---

## Making it yours

To repurpose this as the base for a different application:

1. Swap the URLs, credentials and DB settings in the `*.properties` files.
2. Replace the `pages/` classes with Page Objects for your app.
3. Point the `apiclients/` and `pojo/` classes at your API.
4. Update the `testdata/` files and the TestNG suite XMLs.
5. Keep everything in `base/`, `listeners/`, `reports/` and `utility/` — that's the reusable engine.

---

## A note on credentials

The property files currently contain demo credentials for the public Free CRM sandbox. For anything real, move secrets out of source control (environment variables or a secrets manager) before you commit.

---

Happy testing. Build on it, break it, extend it — that's what a starter framework is for.
