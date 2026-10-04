# Copilot instructions for DataPipeline-Examples

## Scope and goals
- This is the **public** GitHub repository of DataPipeline's Java code examples
  (`NorthConcepts/DataPipeline-Examples`, Apache-2.0 `LICENSE`). Every example is a
  small, self-contained `main` class that shows **one** DataPipeline capability.
- Each example becomes a page under `https://datapipeline.io/docs/examples/<slug>`.
  The page is a CMS record (DataPipeline.io's CMS) whose `github_url` points at the
  file **on `master`**; the website fetches the raw source from GitHub at render time.
  **The code in this repo is the published content.** Write it to be read.
- Use the name "DataPipeline" (not "Data Pipeline") in new text.
- Keep changes minimal and additive: new examples are new files; existing examples
  are public URLs with inbound links, so renaming/moving/deleting is a content
  decision, not a refactor (see "Change discipline").

## Tech stack and build
- Java **8** source/target (`sourceCompatibility = 1.8`) — no `var`, records,
  text blocks, switch expressions, or `List.of`. CI compiles on JDK 8 (Zulu).
- Single Gradle project; wrapper is **Gradle 8.5**, vendored at
  `gradle/wrapper/gradle-8.5-bin.zip` (the `distributionUrl` is relative, so the
  build never downloads Gradle). `.gitignore` lists `gradle/`, so a new file under
  `gradle/` needs `git add -f`.
- Dependencies are the **published** DataPipeline artifacts, resolved from
  `mavenLocal()` first and then `https://maven.northconcepts.com/public/repositories/datapipeline`:
  `northconcepts-datapipeline-small-business` + `-foundations` + one line per
  `-integrations-*` / `-filesystems-*` module, all at the single `version` in
  `build.gradle` (currently `10.2.0-SNAPSHOT`; it tracks the DataPipeline release in
  progress and is bumped with each release, e.g. "DP-4993 update version to 10.0.0").
- A **license file** is required to *run* (not to compile):
  `src/main/resources/NorthConcepts-DataPipeline.license` (gitignored). Never commit
  it and never copy one in from another repo.
- **Git LFS** holds `*.jar`, `*.zip`, and the large CSV fixtures (`patient-visits-raw-*`,
  `credit-balance-02-*`, `trades.csv`). Clone with LFS or those files are pointers.
- Windows: Parquet/ORC/HDFS-backed examples need `HADOOP_HOME` + `%HADOOP_HOME%\bin`
  on `PATH` (winutils) — see `README.md`.
- The three commands below, in order: `compileJava` is what CI effectively checks
  (there are no tests); `build` is the exact CI task (`.github/workflows/gradle.yml`);
  `run` is the JavaExec task in `build.gradle` that runs one example with the repo
  root as cwd.

```bat
gradlew.bat compileJava --no-daemon
gradlew.bat build --no-daemon
gradlew.bat run --quiet -PclassToExecute=com.northconcepts.datapipeline.examples.cookbook.WriteACsvFileToFixedWidth
```

Or run the `main` from the IDE (Eclipse `.project`/`.classpath` are committed; any
IDE that imports Gradle works) with the **repository root as the working
directory** — every example uses relative paths like `example/data/input/...`.

## Project layout
```
src/main/java/com/northconcepts/datapipeline/
  examples/<area>/            core + integration examples (one package per product area)
    cookbook/                 the big catch-all (~200): read/write/transform/filter/jdbc/excel/...
    cookbook/customization/   "write my own X" pairs: MyX.java (the class) + WriteMyOwnX.java (the main)
    cookbook/blog/            code that accompanies blog posts
    cookbook/common/          shared helpers (SlowProxyReader, card/*) — not examples themselves
    amazons3/ avro/ bloomberg/ email/ google/{analytics,calendar,contacts,drive,gmail}/
    jira/ jsonata/ kafka/ latex/ lookups/ mongodb/ orc/ parquet/ pdf/ security/
    shopify/ template/ trello/ twitter/ twitter2/ userguide/eventbus/ ...
  foundations/examples/<area>/  DataPipeline Foundations examples
    schema/ pipeline/ datamapping/ decisiontable/ decisiontree/ flatfile/ flatfile2/
    jdbc/ tree/ eventbus/ difference/ number/ time/
src/main/resources/log4j.properties   DEBUG logging for com.northconcepts.datapipeline
example/data/input/           the classic fixture root (+ datamapping/ pipeline/ schema/ template/)
example/data/output/          gitignored run output (only .keep is tracked)
data/input/                   the newer fixture root — ALSO consumed by DataConverter.io's CI (see below)
data/output/                  gitignored run output
.github/workflows/gradle.yml  CI: checkout with LFS → JDK 8 → ./gradlew build
docs/authoring/               agent recipes, skeletons, exemplars, ROADMAP (start at /CLAUDE.md)
```

Two legacy outliers exist — `com.northconcepts.datapipeline.mailchimp` (no
`examples` segment) and `com.northconcepts.datapipeline.UpsertWithMultipleJdbcConnections`
at the package root. Don't copy that; new examples go under
`examples/<area>` or `foundations/examples/<area>`.

## What an example looks like (the contract)
- **One public class per capability**, named as the imperative how-to phrase the
  page will carry: `ReadACsvFile`, `WriteACsvFileToFixedWidth`,
  `UseRetryingOperationWithRetryCondition`. Variants get a numeric suffix
  (`ReadAnXmlFile2`) or a qualifier (`...UsingATemporaryFile`). The CMS title is the
  same phrase with spaces; the slug is its kebab-case.
- `public static void main(String[] args) throws Throwable` (or no `throws` when
  nothing is checked). No argument parsing, no frameworks, no `System.exit`.
- Shape: build the reader → (transform/filter/lookup) → build the writer →
  `Job.run(reader, writer)`. Output goes to `StreamWriter.newSystemOutWriter()` or
  to a file under `example/data/output/` (or `data/output/`). Prefer stdout when the
  format is readable; a file when the point *is* the file format.
- Inputs: reuse a fixture that already exists in `example/data/input/` or
  `data/input/` (`credit-balance-01.csv` is the workhorse). Add a new fixture only
  when the capability needs it, and keep it tiny — the page shows the code, not the
  data.
- **Keep it short** (typically 20–60 lines). No scaffolding, no helper methods
  unless the API needs a class (then put the class in the same package, e.g.
  `MyTransformer` + `WriteMyOwnTransformer`).
- **Credentials and endpoints are placeholder constants** at the top of the class:
  `private static final String ACCESS_KEY = "YOUR ACCESS KEY";`,
  `JIRA_API_KEY = "API_KEY"`. Never a real value, never read from env/system
  properties (no example does — the page must be self-explanatory), never a real
  bucket/host that isn't obviously a sample.
- **Comments:** default to none. Names and the fluent API carry the meaning. A short
  `//` is warranted only where the output would surprise a reader (e.g. which
  characters a LaTeX writer escapes). Never Javadoc, never ticket numbers, never a
  class comment restating the class name.
- **No copyright headers.** The core `DataPipeline` repo's copies of these examples
  carry the proprietary NorthConcepts header; this public Apache-2.0 repo does not
  (2 legacy files do — leave them, don't add more, never paste the proprietary
  header in here).
- Java 8 only (see above). Also no `JobTemplate` — `Job.run(...)` is the current API.
- Examples that need an external service (S3, Kafka, Mongo, MySQL, Jira, Google,
  Twitter, Shopify, Trello, email, Bloomberg) can't run in CI and that's expected;
  they must still **compile** on JDK 8. Database examples that *can* run use the
  in-memory HSQLDB/H2 drivers already on the classpath (`examples/database/DB.java`
  is the HSQLDB helper); MySQL examples assume `localhost/datapipeline`, user/pw `etl`.

## Data fixtures (shared with other repos — handle with care)
- `data/input/transformer-input.*` (csv/tsv/json/jsonl/xml/xls/xlsx/xlsb/avro/orc/
  parquet/pdf/png/jpg/webp), `data/input/avro_schema_file.avsc` and friends are
  **fetched by DataConverter.io's tests** from
  `https://raw.githubusercontent.com/NorthConcepts/DataPipeline-Examples/refs/heads/master/data/input/...`.
  Renaming, moving, deleting, or changing their contents breaks that repo's CI the
  moment the change lands on `master`. Add new variants; don't alter existing ones
  without coordinating there.
- Fixtures referenced by a published example are part of that page's story:
  renaming one means editing every example that reads it (`grep -rl <name> src`).
- Keep new fixtures small (a few KB). Anything large goes through LFS and needs a
  `.gitattributes` line.

## Publishing boundary (what lives where)
| Concern | Lives in |
|---|---|
| The example code, fixtures, build | **this repo** |
| The page: title, slug, category, tags, description, optional prose body, `github_url` | the **DataPipeline.io CMS** (type `example`) — a CMS record, not a file in any repo |
| Rendering, lists, categories, tags, search | DataPipeline.io `website` (`Examples3Resource`, `DocsService`) — needs **no change** for a new example |

Consequences:
- The raw URL points at **`master`**, so a page can only render after the code PR is
  merged. Sequence: code PR → merge → create/publish the CMS record.
- The website caches each fetched file in memory and only drops the cache when a
  CMS publish changes the latest `published_on` (checked every 15 min) or on
  restart. A code-only fix to an already-published example does **not** appear on
  the site until something is re-published in the CMS.
- Categories are fixed: `basic-io`, `data-manipulation`, `customization`,
  `datapipeline-foundations`. Tags come from the CMS tag vocabulary (`csv`, `xml`,
  `json`, `excel`, `jdbc`, `transform`, `filter`, `lookup`, `parquet`, `amazon-s3`,
  `pipeline`, `schema`, `data-mapping`, …). Details: `docs/authoring/PublishExample.md`.

## Change discipline
- Branch, commit subject, and PR title start with the Jira ticket:
  `DP-1234 Add Avro reader example`. PRs are squash-merged to `master`.
- One example (or one tightly related set) per PR, plus its fixture and, for a new
  integration module, the one `build.gradle` dependency line.
- Don't reformat, rename, move, or delete existing examples unless the ticket says
  so — each is a live URL. If a rename is required, the CMS record's `github_url`
  must be updated in the same release.
- Don't change `build.gradle` beyond adding a module line or the coordinated
  version bump. Don't add test frameworks or an `application`-plugin main class —
  there is intentionally no single entry point (the `run` task takes the class via
  `-PclassToExecute`).
- Don't commit `example/data/output/*`, `data/output/*`, the license file, or
  IDE output (`bin/`, `build/`).
- Core repo mirror: ~136 of these examples also ship inside the DataPipeline
  download (`DataPipeline/example/src/java`, with the proprietary header). When you
  change one that exists there, say so in the PR so it can be mirrored; don't copy
  the header back here.

## Authoring guides
Short, agent-readable recipes live under `docs/authoring/` — load the one that
matches the task instead of surveying 400 files:

- `docs/authoring/Example.md` — add a new example (the main recipe)
- `docs/authoring/DataFixture.md` — add or change an input data file
- `docs/authoring/UpdateExample.md` — change an existing example safely (playbook)
- `docs/authoring/ReviewExample.md` — review an example PR (playbook/checklist)
- `docs/authoring/PublishExample.md` — the CMS side: fields, vocabulary, sequencing (playbook)
- `docs/authoring/exemplars.yaml` — curated in-tree exemplars per pattern
- `docs/authoring/_skeletons/` — copy-paste skeletons (`*.java.txt`, never compiled)
- `docs/authoring/ROADMAP.md` — the plan for this guidance system
