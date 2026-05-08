# Selenium Java Framework — Structure & Best Practices Cheatsheet

---

## 1. Project Structure

```
src/
├── main/java/com/yourproject/
│   ├── base/          → Base.java         (driver lifecycle, config loading)
│   ├── pages/         → Page Objects      (locators + UI actions only)
│   ├── utility/       → Helpers           (ExcelUtility, WaitHelper, etc.)
│   └── testdata/      → Data files        (.xlsx, .json, .csv)
│
└── test/java/com/yourproject/
    └── flows/         → Test Classes      (orchestration + assertions only)

src/main/resources/
    └── config/
        ├── svt.properties
        ├── dev.properties
        └── prod.properties
```

---

## 2. Layer Responsibilities — The Golden Rules

| Layer | OWNS | NEVER CONTAINS |
|---|---|---|
| **Page Object** | Locators, raw UI actions, waits, boolean state checks | Assertions, test data, loops, Excel reads, business logic |
| **Test Flow** | Orchestration, data loading, assertions, iteration | Locators, `driver.findElement`, `sendKeys` directly |
| **Base** | Driver init/quit, config loading, ThreadLocal driver | Page-specific logic, test logic |
| **Utility** | Reusable helpers (Excel, Wait, Screenshot) | Test or page logic |

---

## 3. Page Object Rules

```java
public class ContactPage extends Base {

    // ✅ All locators at the top using @FindBy
    @FindBy(xpath = "//button[text()='Create']") WebElement createButton;
    @FindBy(name = "first_name")                 WebElement firstNameInput;
    @FindBy(xpath = "//button[text()='Save']")   WebElement saveButton;

    public ContactPage() {
        PageFactory.initElements(getDriver(), this);  // ✅ Always init in constructor
    }

    // ✅ One action per method — small and focused
    public void clickCreate()                    { createButton.click(); }
    public void enterFirstName(String name)      { firstNameInput.sendKeys(name); }

    // ✅ Boolean state methods for assertions (no assert inside)
    public boolean isFormVisible()               { return firstNameInput.isDisplayed(); }
    public boolean isContactInList(String first, String last) {
        try {
            return getDriver().findElement(By.xpath(
                "//span[text()='" + first + " " + last + "']")).isDisplayed();
        } catch (NoSuchElementException e) { return false; }
    }

    // ✅ Return the NEXT page object when navigation occurs
    public ContactPage saveContact() {
        saveButton.click();
        return new ContactPage();
    }

    // ❌ WRONG — page should NOT loop, read Excel, or assert
    // public void fillAndSaveAll(String file) { for(...) { ... sa.assertEquals(...) } }
}
```

---

## 4. Test Flow Rules

```java
public class FCRM_8_Create_New_Contact_Val extends Base {

    LoginPage loginPage;
    HomePage  homePage;
    ContactPage contactPage;

    @BeforeMethod
    public void setUp() {
        initialization();                                          // ✅ driver start
        loginPage   = new LoginPage();
        homePage    = loginPage.login("user@mail.com", "Pass@1"); // ✅ chain page returns
        contactPage = homePage.clickContacts();
    }

    @Test
    public void createNewContact_Validation() {
        SoftAssert sa = new SoftAssert();                         // ✅ use SoftAssert

        // AC1 — Login successful
        sa.assertTrue(homePage.isHomeMenuDisplayed(),
            "AC1 FAILED: Home menu not visible after login");

        // AC2 — Correct page loaded
        sa.assertEquals(contactPage.contactPageUrl(),   "https://ui.freecrm.com/contacts",
            "AC2 FAILED: Wrong URL");
        sa.assertEquals(contactPage.contactPageTitle(), "Free CRM",
            "AC2 FAILED: Wrong title");

        // AC3 — Form opens
        contactPage.clickCreate();
        sa.assertTrue(contactPage.isFormVisible(),
            "AC3 FAILED: New contact form not visible");

        // AC4 — Fill form (action, no assertion needed)
        List<Object[]> data = new ExcelUtility("TestData", "src/main/java/.../data.xlsx")
                                  .getExcelDataAsList();
        for (Object[] row : data) {
            contactPage.fillContactForm(row);                     // ✅ data loaded in test
            contactPage.saveContact();

            // AC5 — Contact appears in list
            sa.assertTrue(contactPage.isContactInList(row[0].toString(), row[1].toString()),
                "AC5 FAILED: Contact not found in list → " + row[0] + " " + row[1]);

            homePage.clickContacts();                             // ✅ navigation in test
        }

        sa.assertAll();                                           // ✅ always at the end
    }

    @AfterMethod
    public void tearDown() { quitDriver(); }                      // ✅ always clean up
}
```

---

## 5. Assertion Strategy

| Scenario | Use | Why |
|---|---|---|
| Multiple ACs in one test | `SoftAssert` | All ACs run; full report on failure |
| Single critical gate | `Assert` (hard) | Fail fast if precondition is broken |
| Always end SoftAssert | `sa.assertAll()` | Without this, failures are silently ignored |
| Assertion messages | Always add 3rd arg | Instant readability in CI logs |

```java
// ✅ Correct SoftAssert usage
SoftAssert sa = new SoftAssert();
sa.assertTrue(condition,  "AC1 FAILED: reason");
sa.assertEquals(a, b,     "AC2 FAILED: reason");
sa.assertAll();            // ← NEVER skip this

// ✅ Hard assert for a precondition that makes the whole test meaningless if it fails
Assert.assertTrue(homePage.isHomeMenuDisplayed(), "Login failed — aborting test");
```

---

## 6. Page Chain Pattern

Every method that causes navigation should **return the next Page Object**.  
This keeps the test readable and avoids re-instantiating pages manually.

```java
// LoginPage
public HomePage login(String email, String pass) {
    emailInput.sendKeys(email);
    passwordInput.sendKeys(pass);
    loginButton.click();
    return new HomePage();           // ✅ returns the next page
}

// HomePage
public ContactPage clickContacts() {
    contactsMenu.click();
    return new ContactPage();        // ✅ returns the next page
}

// Test reads like a sentence:
ContactPage contactPage = new LoginPage()
    .login("user@mail.com", "Pass@1")
    .clickContacts();
```

---

## 7. Data Management

```
❌ Never hardcode file paths:   "C:\\Users\\thndr\\Desktop\\...\\file.xlsx"
✅ Use project-relative paths:  "src/main/java/com/freecrm/testdata/file.xlsx"
✅ Or put path in properties:   testdata.path=src/main/java/com/freecrm/testdata/file.xlsx
```

```java
// ✅ Data loading belongs in the TEST, not the page
List<Object[]> contacts = new ExcelUtility("TestData", getProperty("testdata.path"))
                              .getExcelDataAsList();
```

---

## 8. Naming Conventions

| Artifact | Convention | Example |
|---|---|---|
| Page class | `<PageName>Page` | `ContactPage`, `LoginPage` |
| Test class | `FCRM_<id>_<Feature>_<Type>` | `FCRM_8_Create_New_Contact_Val` |
| Test method | `camelCase`, describes the scenario | `createNewContact_Validation` |
| Boolean page methods | `is...` / `has...` | `isFormVisible()`, `isContactInList()` |
| Action page methods | verb first | `clickCreate()`, `fillContactForm()`, `saveContact()` |
| Navigation methods | returns next page | `clickContacts()` → returns `ContactPage` |

---

## 9. Quick Diagnostics — "Where does this code belong?"

```
Does it touch the DOM (findElement, click, sendKeys)?  →  PAGE
Does it assert something?                              →  TEST FLOW
Does it loop over test data?                           →  TEST FLOW
Does it read a file (Excel, CSV)?                      →  TEST FLOW  (call utility from here)
Is it a reusable helper (screenshot, wait)?            →  UTILITY
Is it browser/driver setup?                            →  BASE
```

---

*Framework: Java + Selenium + TestNG + Page Object Model (POM)*  
*Project: thndrbladers/FreeCrm*
