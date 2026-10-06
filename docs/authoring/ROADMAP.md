# Authoring ROADMAP for DataPipeline-Examples

The plan for making it **cheap for LLMs/agents to understand, add, update, review,
and publish** DataPipeline examples — the code in this repo that becomes the pages
under `https://datapipeline.io/docs/examples/`. Referenced from
[`/CLAUDE.md`](../../CLAUDE.md) and
[`.github/copilot-instructions.md`](../../.github/copilot-instructions.md); modelled
on the sibling systems in `DataPipeline/docs/authoring/` (base-class contract
sheets) and `DataConverter.io/docs/authoring/` (task recipes + playbooks).

---

## 1. North star

An agent dropped into this repo should finish any of the five jobs below by
reading **~2 small files**, and never by surveying the 400+ existing examples:

| Job | Reads |
|---|---|
| understand what exists for an area | `CLAUDE.md` quick map + `ls` of the area package (names are page titles) |
| add an example | `Example.md` + one skeleton (+ the exemplar if unsure) |
| add/change a fixture | `DataFixture.md` |
| update a published example | `UpdateExample.md` |
| review a PR | `ReviewExample.md` |
| publish / fix the page | `PublishExample.md` |

Cost drops because every example has **one shape**, so the skeletons are nearly
complete and the rules fit on a screen; and because the **publishing boundary**
(what is code here vs. a record in the CMS vs. website behaviour) is written down
once instead of being rediscovered from `Examples3Resource`/`DocsService` each time.

### Principles
1. **One screen per file.** Link, don't inline.
2. **Don't duplicate `copilot-instructions.md`**; recipes add workflow only.
3. **Real exemplars**, the smallest in-tree file that shows the whole pattern,
   indexed once in `exemplars.yaml`.
4. **The file is the page** — every rule traces back to that.
5. **Documentation + scaffolding only** — this roadmap never changes an example's
   behaviour, the build, or CI without its own ticket.

---

## 2. Findings that ground the plan (verified 2026-10-04)

- 445 Java files (~415 runnable `main` classes + ~30 helper classes), one Gradle
  project, Java 21 source/target, Gradle 8.5 vendored wrapper, no tests; CI
  (`.github/workflows/gradle.yml`) = `./gradlew build` on JDK 21 with LFS.
- Dependencies are the published DataPipeline artifacts at one `version`
  (`11.0.0`), from `mavenLocal()` then `maven.northconcepts.com`. A new
  integration module = one `implementation` line (LaTeX, PDF PRs).
- Code conventions are strong and consistent: how-to class names, `Job.run`,
  relative fixture paths, placeholder constants, almost no comments (8 files have
  any Javadoc), **no copyright header** (2 legacy files) — unlike the core repo's
  copies of the same examples, which carry the proprietary header. ~136 examples
  exist in both repos; 84 are identical apart from header/whitespace.
- Two fixture roots: `example/data/input/` (classic, 94 files + 4 subfolders) and
  `data/input/` (27 files). `data/input/transformer-input.*` and
  `avro_schema_file.avsc` are fetched from `master` by **DataConverter.io's
  tests** (`FileConverterUrlBaseTest` et al.).
- Publishing: each page is a CMS record (type `example`) with `github_url` =
  blob URL on `master`; empty body ⇒ website renders
  `<pre>${github.getRawContent(githubUrl)}</pre>`. The website
  (`DataPipeline.io/website/.../GitHub.java`) caches fetched sources in a map that
  is cleared only in `DocsService.loadDocs()` when `maxPublishedOn` changes (15-min
  poll) — a code-only fix is invisible until a re-publish. Categories:
  `basic-io`, `data-manipulation`, `customization`, `datapipeline-foundations`
  (`dpf` was renamed in V11). The local CMS DB has only the V9 seed (242 examples).
- The README's documented run command (`gradlew run -PclassToExecute=…`) referred
  to a task `build.gradle` did not define (no `application` plugin, no `run` task);
  a `JavaExec` task was added in Phase 0 so the command works.
- Commit/PR convention: `DP-NNNN <title>` in 234 of 311 commits; squash-merge with
  `(#N)`.
- No agent guidance existed before this change: no `CLAUDE.md`, no
  `copilot-instructions.md`, no `docs/`.

---

## 3. Phase 0 — Seed the system (this change) ✅

- [x] `CLAUDE.md` — thin entry point, quick map, most-missed rules.
- [x] `.github/copilot-instructions.md` — canonical: stack, layout, the example
      contract, fixtures, publishing boundary, change discipline.
- [x] `docs/authoring/README.md` — index + 13-section convention.
- [x] `docs/authoring/Example.md` — the main recipe.
- [x] `docs/authoring/DataFixture.md` — fixtures (two roots, LFS, external consumers).
- [x] Playbooks: `UpdateExample.md`, `ReviewExample.md`, `PublishExample.md`.
- [x] `_skeletons/`: `FileExample`, `MemoryExample`, `ExternalServiceExample`,
      `CustomComponentExample` (`*.java.txt`).
- [x] `exemplars.yaml` — one verified in-tree exemplar per pattern.
- [x] `.github/PULL_REQUEST_TEMPLATE.md` — carries the publish hand-over block and
      the review checklist so PRs arrive complete.
- [x] `README.md` — points contributors/agents at `CLAUDE.md`.
- [x] `build.gradle` — `run` JavaExec task (`-PclassToExecute`, cwd = repo root) so
      the README's command and the recipes' verification step actually work.

**Acceptance:** an agent can add a new example from `CLAUDE.md` → `Example.md` →
one skeleton without opening any existing example, and knows the publish sequence.

---

## 4. Phase 1 — Make the build say what the docs say

- [ ] **Compile-only CI job name.** CI runs `build`, which here equals compile;
      document (or rename the step) so nobody expects tests to exist.
- [x] **JDK 21.** Done in DP-6034 (`build.gradle`, CI and the "Java 8 only" rule in
      `copilot-instructions.md` §"Tech stack" flipped together, following `DP-5946`).

---

## 5. Phase 2 — Guardrails that are cheap to keep

- [ ] **Fixture-reference check.** A tiny script (or Gradle task) that greps every
      `"example/data/input/…"` / `"data/input/…"` literal and fails when the file
      doesn't exist — catches renamed fixtures (no compile-time check today).
- [ ] **Header/Java-level lint.** Fail the build on `Copyright (c)` in
      `src/main/java` (except the 2 legacy files) and on syntax newer than the
      toolchain, so review doesn't have to.
- [ ] **CMS cross-check (read-only).** A script that lists examples in this repo
      with no published CMS record and CMS records whose `github_url` 404s on
      `master`. Needs read access to the CMS API or a DB export; outputs a report,
      changes nothing.

---

## 6. Phase 3 — Close the loop with the sibling repos

- [ ] **DataPipeline.io `Example.md`.** Its `website/docs/authoring/ROADMAP.md`
      still lists this recipe as planned. When written there, it should own the
      website/CMS internals and link `PublishExample.md` here for the
      examples-side hand-over (and vice versa).
- [ ] **Core mirror policy.** Decide and write down whether the ~136 shared
      examples in `DataPipeline/example/src/java` are synced from here (header
      added on the way in), maintained independently, or retired from the core
      repo. Until then the rule stays "mention it in the PR".
- [ ] **DataConverter.io fixture contract.** Record in DataConverter.io's guidance
      that `data/input/transformer-input.*` here is a frozen contract, and/or move
      those tests to a pinned commit instead of `master`.
- [ ] **Core `copilot-instructions.md` pointer.** One line in
      `DataPipeline/.github/copilot-instructions.md` ("public examples live in
      DataPipeline-Examples; see its `CLAUDE.md`") so an agent working on a new
      reader knows where the example goes.

---

## 7. Phase 4 — Optional polish

- [ ] A generated `docs/CATALOG.md` (class → package → fixtures → published slug)
      if agents keep paying to `ls`/grep; only worth it with the Phase 2 cross-check
      script keeping it fresh.
- [ ] Per-area notes for the integrations that need real services (what a local
      Kafka/Mongo/MySQL needs to look like for the example to run) — only if those
      examples start changing often.

---

## 8. Worked recipe — "add an example for a new writer" (the cheap path)

1. Read `CLAUDE.md` → quick map → `docs/authoring/Example.md`.
2. Copy `_skeletons/MemoryExample.java.txt` to
   `src/main/java/com/northconcepts/datapipeline/examples/<module>/WriteA<Format>File.java`;
   rename; replace the writer; delete the `// TODO`s.
3. If the module is new, add its `implementation` line to `build.gradle`.
4. `gradlew.bat compileJava --no-daemon`; run from the IDE; paste output into the
   PR using the template's hand-over block (title/slug/category/tags).
5. After merge: create and publish the CMS record per `PublishExample.md`.

Files read: 2–3 small ones. Existing examples opened: 0 (1 if the exemplar is
consulted). That is the win.

---

## 9. Non-goals & risks

- **Non-goal:** restructuring packages, renaming examples, or changing fixtures to
  be "cleaner" — every one is a live URL or an external contract.
- **Non-goal:** adding a test framework; the examples are verified by compiling
  and by being read.
- **Risk — drift between the site and the repo:** mitigated by `PublishExample.md`
  §3 (re-publish after code changes) and, later, the Phase 2 cross-check.
- **Risk — the Java floor moving:** one place to update (`copilot-instructions`
  §"Tech stack"); the recipes say "Java 21 plain style" by reference, not by repetition.
- **Risk — stale exemplar paths:** `exemplars.yaml` is the single place to fix.

---

## 10. Execution checklist

- [x] Phase 0 — seed (this change)
- [ ] Phase 1 — CI wording; JDK-floor follow-up
- [ ] Phase 2 — fixture-reference check; header/Java-level lint; CMS cross-check
- [ ] Phase 3 — DataPipeline.io `Example.md`; core mirror policy; DataConverter.io contract; core pointer
- [ ] Phase 4 — optional catalog / per-area service notes
