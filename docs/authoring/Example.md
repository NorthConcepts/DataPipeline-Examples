# Recipe: add a new example

> Load this instead of reading other examples. Copy a skeleton from
> [`_skeletons/`](_skeletons/); don't synthesize. Shared rules live in
> [`copilot-instructions.md`](../../.github/copilot-instructions.md) — this recipe
> only adds the workflow.

## 1. Purpose / when to pick this
A new example shows **one** DataPipeline capability as a runnable `main` class that
will be published as a page at `datapipeline.io/docs/examples/<slug>`. Pick this
when a ticket asks for "example code for X", when a new reader/writer/module ships,
or when a support question keeps recurring. If the capability already has an
example, prefer [UpdateExample.md](UpdateExample.md) or add a clearly-named
**variant** (`...UsingATemporaryFile`, `ReadAnXmlFile2`) rather than editing the
published one.

Pick the skeleton by what the example needs:

| The example… | Skeleton |
|---|---|
| reads a file fixture and writes stdout/a file | [FileExample.java.txt](_skeletons/FileExample.java.txt) |
| needs no input file (builds records in memory) | [MemoryExample.java.txt](_skeletons/MemoryExample.java.txt) |
| talks to a service that needs credentials/hosts | [ExternalServiceExample.java.txt](_skeletons/ExternalServiceExample.java.txt) |
| shows how to write your own reader/writer/transformer/filter/lookup | [CustomComponentExample.java.txt](_skeletons/CustomComponentExample.java.txt) (two files) |

## 2. Files you will touch
| # | File | When |
|---|---|---|
| 1 | `src/main/java/com/northconcepts/datapipeline/examples/<area>/<HowToPhrase>.java` (or `foundations/examples/<area>/`) | always |
| 2 | `example/data/input/<fixture>` (or `data/input/`) | only if no existing fixture fits — see [DataFixture.md](DataFixture.md) |
| 3 | `build.gradle` → one `implementation "com.northconcepts:northconcepts-datapipeline-integrations-<x>:${version}"` line | only for a module not yet listed (e.g. the LaTeX PR added `-integrations-latex`) |
| 4 | a companion class in the same package (`MyTransformer`) | only for the custom-component pattern |
| 5 | the CMS record (title, slug, category, tags, `github_url`) | after merge — [PublishExample.md](PublishExample.md) |

Nothing else: no registry, no index file, no website change.

## 3. The code contract
- One `public class` named as the how-to phrase; `public static void main(String[] args) throws Throwable`.
- Body order: constants (paths/credentials) → reader → transform/filter/lookup →
  writer → `Job.run(reader, writer)`. Optionally `System.out.println(...)` a count
  or a result after the run.
- Inputs by **relative path from the repo root**: `new File("example/data/input/credit-balance-01.csv")`.
  Outputs to `example/data/output/<Name>.<ext>` (gitignored) or
  `StreamWriter.newSystemOutWriter()`.
- Java 8 syntax only. `Job.run(...)`, not `JobTemplate`. Imports explicit (no `*`).
- Credentials/hosts are `private static final String` placeholders
  (`"YOUR ACCESS KEY"`, `"API_KEY"`, `"localhost"`). Never real, never from env.
- No comments unless the output would surprise; no Javadoc; no copyright header.
- 20–60 lines. If it's longer, you are showing two things — split it.

## 4. The naming contract
- Class = imperative how-to phrase in UpperCamelCase: `Read…`, `Write…`, `Convert…To…`,
  `Use…`, `Filter…`, `Add…To…`, `Handle…`, `Generate…`. Avoid `Example`, `Demo`,
  `Test` in the name.
- Package = product area: `examples/<module>` (`parquet`, `jira`, `amazons3`, …),
  `examples/cookbook` for core-library how-tos, `examples/cookbook/customization` for
  "write my own X", `foundations/examples/<area>` for Foundations.
- The CMS title is the phrase with spaces and natural capitalization ("Write a CSV
  File to Fixed Width"); the slug is its kebab-case. Choose the class name with the
  page title in mind.

## 5. Registration (build.gradle)
Only when the example uses a module not already on the classpath: add one
`implementation` line next to its siblings, same `${version}`. Nothing else is
registered anywhere.

## 6. Routing (where it lands on the site)
Not in this repo. The page URL is decided by the CMS record's **slug**; its list
placement by **category** (`basic-io` · `data-manipulation` · `customization` ·
`datapipeline-foundations`) and **tags**. Decide them now (they shape the class
name) and record them in the PR description for whoever creates the CMS record.

## 7. Content (fixtures / prose)
- Reuse fixtures: `credit-balance-01.csv` (CSV workhorse), `credit-balance-02*.csv`
  (bigger/variants), `purchases.csv`, `products.xml`, `json-01.json`, `jewelry.xlsx`,
  `data/input/transformer-input.*` (one dataset in every format),
  `example/data/input/{datamapping,pipeline,schema,template}/` for Foundations.
- The page body is optional prose written **in the CMS**, not here. If the example
  needs explanation beyond the code, put a one-paragraph suggestion in the PR
  description; the code itself stays clean.

## 8. Events / tracking
Not applicable (the website tracks page views; nothing here).

## 9. Generated content
Not applicable — the website renders the raw file; there is no generation step in
this repo.

## 10. Validation & limits
- Must compile on JDK 8 (`gradlew.bat compileJava --no-daemon`).
- Must run from the repo root with a license file present, when it needs no
  external service. If it needs one, it must still compile and its placeholders must
  make that obvious.
- Fixture ≤ a few KB unless the example is *about* volume (then LFS).

## 11. Tests / verification
There are no unit tests. Verify by:
1. `gradlew.bat compileJava --no-daemon` (JDK 8).
2. Run it — `gradlew.bat run --quiet -PclassToExecute=<fqcn>` (cwd is forced to the
   repo root) or the `main` from the IDE — and eyeball the output; paste the first
   lines into the PR description.
3. If it writes a file, open it; delete it afterwards (output dirs are gitignored —
   confirm with `git status`).

## 12. Antipatterns & gotchas
- Working directory ≠ repo root → `FileNotFoundException` on `example/data/input/...`.
- Java 9+ syntax compiles locally on a newer JDK and fails CI (JDK 8).
- Pasting an example from the core repo brings its proprietary copyright header — strip it.
- `JobTemplate.DEFAULT.transfer(...)` is the old API; use `Job.run(...)`.
- Using `data/input/transformer-input.*` as output or modifying it — those files feed another repo's CI.
- A second public class in the same file, or a `package-info`, or `args` parsing — none exist here; don't start.
- Naming the class after the API (`ParquetDataReaderDemo`) instead of the task (`ReadAParquetFile`).

## 13. Exemplar + checklist
Exemplars (see [exemplars.yaml](exemplars.yaml)):
`cookbook/WriteACsvFileToFixedWidth.java` (file → file),
`cookbook/ReadJsonLinesAsRecord.java` (file → stdout, `data/input` root),
`cookbook/WriteSimpleJsonFile.java` (memory → file),
`latex/WriteALatexFile.java` (new-module PR shape: one file + one `build.gradle`
line; a deliberate 77-line exception with justified comments — copy the shape, not
the length),
`amazons3/ReadFromAmazonS3.java` (external service, `BUCKET`/`KEY` placeholders, try/finally close),
`cookbook/customization/MyTransformer.java` + `WriteMyOwnTransformer.java` (custom component pair).

- [ ] class name is the how-to phrase; package is the product area
- [ ] skeleton copied; `// TODO` lines resolved or deleted
- [ ] Java 8; `Job.run`; relative paths from repo root; placeholders for secrets
- [ ] fixture reused, or a tiny new one added per [DataFixture.md](DataFixture.md)
- [ ] `build.gradle` line added only if the module was missing
- [ ] compiles on JDK 8; ran locally (or compiles + obviously needs a service)
- [ ] no comments/Javadoc/copyright header; 20–60 lines
- [ ] PR description carries: proposed title, slug, category, tags, sample output
- [ ] after merge: CMS record created and published ([PublishExample.md](PublishExample.md))
