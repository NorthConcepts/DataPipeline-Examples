# Playbook: update an existing example

For changing an example that is already published: find it, change it safely, keep
the page truthful. (A journey, not a 13-section recipe.) For something new, use
[Example.md](Example.md).

## 0. Locate it
- **From a page URL** `datapipeline.io/docs/examples/<slug>`: the class name is the
  slug in UpperCamelCase (`write-a-csv-file-to-fixed-width` →
  `WriteACsvFileToFixedWidth`). `find src -name "<Class>.java"`. If that misses, the
  CMS record's `github_url` is authoritative (ask for it or check the CMS).
- **From a capability**: `ls` the area package; names are page titles. Grep for the
  API (`grep -rl "ParquetDataReader" src/main/java`).
- **From a fixture**: `grep -rl "<fixture-name>" src/main/java`.

## 1. Decide the kind of change
| Changing… | Do | Watch |
|---|---|---|
| A bug / wrong API usage | edit in place | keep the class name and path (the URL and `github_url` depend on them) |
| Behaviour after a DataPipeline API change | edit in place, same shape | Java 8 syntax; `version` in `build.gradle` must already resolve the new API |
| Scope (the example now shows a second thing) | **don't** — add a variant class instead | the published page should keep doing one thing |
| Name or package | only with a ticket; treat as a content move | CMS `github_url` must be updated in the same release; old GitHub URL 404s |
| The fixture it reads | see [DataFixture.md](DataFixture.md) | other examples reading it; `data/input/` consumers in DataConverter.io |
| Dependency `version` (release bump) | one PR touching `build.gradle` only (pattern: "DP-4993 update version to 10.0.0") | compile the whole repo on JDK 8 — this is when API breaks surface |

## 2. Make the change
- Keep the file's existing style (indentation, import order, `throws` clause).
- Don't "improve" unrelated lines, add comments, or reformat — the diff is reviewed
  against a live page.
- Don't introduce Java 9+ syntax or `JobTemplate`.
- If the example is one of the ~136 that also ship in the core repo's
  `example/src/java`, say so in the PR so the mirror can be updated (don't copy its
  proprietary header into this repo).

## 3. Verify
1. `gradlew.bat compileJava --no-daemon` (JDK 8) — whole repo, not just your file.
2. Run the `main` from the repo root with a license file; compare output to the old
   behaviour; paste the delta into the PR.
3. `git status` — no stray output files.

## 4. Keep the site truthful (the step people miss)
- The website serves the **raw file from `master`** and **caches it in memory**; the
  cache clears only when a CMS publish changes the latest `published_on` or on
  restart. After the code merges, **re-publish the CMS record** (edit → save →
  publish) so the page picks up the new source — otherwise it can show the old code
  indefinitely.
- If the title/description/tags should change with the code, do it in that same CMS
  edit. See [PublishExample.md](PublishExample.md).
- If you moved/renamed, update `github_url` in the CMS before or with the merge.
