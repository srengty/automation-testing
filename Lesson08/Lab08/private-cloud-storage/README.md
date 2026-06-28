# Private Cloud Storage App — Lab 08 Starter

A small **Spring Boot** backend where each user manages files and folders in
their **own** isolated cloud storage. New users get a **50 MB** quota, can manage
their profile, and can delete their own account.

This starter ships with a test suite that demonstrates **all ten testing methods**
from Lesson 08, driven by **JUnit 5 / AssertJ** and **Playwright (Java)**, and
reported with **Allure**.

> This is a *starter*. Read the TODOs, extend the features, and — most
> importantly — **write tests that exercise every testing method** before you
> submit.

---

## Tech stack

| Concern        | Choice                                   |
|----------------|------------------------------------------|
| Language       | Java 17                                  |
| Framework      | Spring Boot 3.2 (Web, Data JPA, Validation) |
| Database       | H2 (in-memory)                           |
| UI             | Thymeleaf (server-rendered)              |
| Unit/Integration tests | JUnit 5 + AssertJ + Hamcrest      |
| HTTP/API + browser tests | Playwright for Java            |
| Reporting      | Allure                                   |
| Build          | Maven                                    |

---

## Project layout

```
private-cloud-storage/
├── src/main/java/edu/itc/cloud/
│   ├── PrivateCloudStorageApplication.java
│   ├── model/        # User, Folder, FileEntity
│   ├── repository/   # Spring Data JPA repositories
│   ├── service/      # UserService, StorageService, exceptions
│   └── web/          # REST controllers, AuthService, DTOs, error handler
│       └── ui/       # WebUiController (Thymeleaf, session-based)
├── src/main/resources/
│   ├── application.properties
│   └── templates/    # login.html, dashboard.html (Thymeleaf UI)
├── src/test/java/edu/itc/cloud/
│   ├── TestingMethodsServiceTest.java   # methods 1–8 + isolation + deletion
│   ├── CloudApiPlaywrightTest.java      # schema/regex/contains over HTTP
│   ├── CloudUiPlaywrightTest.java       # browser UI test + screenshot snapshot
│   └── support/TestFiles.java
├── .github/workflows/ci.yml
├── pom.xml
└── README.md
```

---

## Run the app

```bash
mvn spring-boot:run
# Web UI  on http://localhost:8080/        (register, then manage files/folders)
# REST API on http://localhost:8080/api/...
# H2 console on http://localhost:8080/h2-console  (JDBC URL: jdbc:h2:mem:cloud)
```

Open <http://localhost:8080/> in a browser, click **Register** (you get 50 MB),
then create folders and upload files from the dashboard.

### Try it with curl

```bash
# Register (returns a token) — new user gets 50 MB
curl -s -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"sok@itc.edu","password":"Secret123!"}'

TOKEN=...   # paste the token from the response

# Profile + quota usage
curl -s http://localhost:8080/api/me -H "Authorization: Bearer $TOKEN"

# Create a folder
curl -s -X POST http://localhost:8080/api/folders \
  -H "Authorization: Bearer $TOKEN" -H "Content-Type: application/json" \
  -d '{"name":"Documents"}'

# Upload a file (multipart)
curl -s -X POST http://localhost:8080/api/files \
  -H "Authorization: Bearer $TOKEN" -F "file=@./photo.png"
```

---

## Run the tests

```bash
mvn test
```

> On the first run Playwright downloads its driver and a Chromium browser (for
> the UI test), so the first build needs internet access. Later runs are offline.

---

## View the Allure report

```bash
# Option A — Allure CLI (https://allurereport.org)
allure serve target/allure-results

# Option B — Maven plugin (no separate CLI needed)
mvn allure:serve
# or build static HTML into target/site/allure-maven-plugin
mvn allure:report
```

---

## Testing methods → where they are demonstrated

Fill in / extend this table as you add your own tests. The starter already
covers every row at least once.

| # | Testing method        | Demonstrated in                                              |
|---|-----------------------|-------------------------------------------------------------|
| 1 | Content equals        | `TestingMethodsServiceTest.newUserQuotaEqualsFiftyMb`       |
| 2 | Contains              | `TestingMethodsServiceTest.listingContainsUploadedName` · `CloudApiPlaywrightTest.folderListingContainsNewFolder` |
| 3 | Regex matched         | `TestingMethodsServiceTest.shareLinkAndEmailMatchPatterns` · `CloudApiPlaywrightTest.profileContract` |
| 4 | Formula matched       | `TestingMethodsServiceTest.freeSpaceFollowsFormula`         |
| 5 | Predicate             | `TestingMethodsServiceTest.usageNeverExceedsQuota`          |
| 6 | Collection            | `TestingMethodsServiceTest.collectionShapeIsExact`          |
| 7 | Exception             | `TestingMethodsServiceTest.overQuotaThrows` · `CloudApiPlaywrightTest.unauthenticatedIsForbidden` |
| 8 | Tolerance / range     | `TestingMethodsServiceTest.toleranceAndRange`               |
| 9 | Schema / JSON         | `CloudApiPlaywrightTest.profileContract`                    |
| 10| Visual / snapshot     | `CloudUiPlaywrightTest.registerThroughUiAndSeeQuota` (browser screenshot) |

Bonus coverage: per-user **isolation** (`usersAreIsolated`) and **account
deletion** (`deleteAccountWipesData`).

---

## What to do for the lab

1. Extend the app so all functional requirements (R1–R8) are solid.
2. Make sure **every testing method** is demonstrated and listed in the table above.
3. Generate the Allure report (commit `target/allure-report/` or screenshots).
4. Push to a **public** GitHub repository.
5. **Submit the GitHub link to Moodle.**

### Ideas to extend the starter
- Replace the toy password hash with **BCrypt** and wire **Spring Security**.
- Add file **move** between folders and folder **rename** tests.
- Add a thin **Thymeleaf** UI and a browser-driven Playwright test with a real
  screenshot snapshot.
- Add **JSON-schema** validation (e.g. `networknt/json-schema-validator`) for a
  stronger contract test.

---

## Notes & caveats (read before grading yourself)

- The in-memory bearer token store (`web/AuthService`) and the password hash in
  `UserService` are **deliberately minimal** for the starter — harden them.
- The database is in-memory and resets on restart.
- File bytes are stored in the DB as a BLOB to keep the project self-contained.
