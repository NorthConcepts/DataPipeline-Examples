<!-- Title: DP-NNNN <what changed>  (same as the Jira ticket) -->

## What

<!-- One line per example added/changed, and why. -->

## Publish hand-over (new or renamed examples — one block per example)

```
Title:    
Slug:     
Category: basic-io | data-manipulation | customization | datapipeline-foundations
Tags:     
GitHub:   https://github.com/NorthConcepts/DataPipeline-Examples/blob/master/src/main/java/com/northconcepts/datapipeline/.../<Class>.java
Body:     (none) | (one-paragraph intro suggested: ...)
```

<!-- Edited a published example? Note that the CMS record must be re-published to flush the site's cache. -->

## Sample output

```
```

## Checklist (see docs/authoring/ReviewExample.md)

- [ ] Compiles on JDK 21 (`gradlew.bat compileJava --no-daemon`); plain style (no `var`/records unless clearer)
- [ ] One capability, how-to class name, product-area package, `Job.run`, relative paths from repo root
- [ ] Credentials/hosts are placeholders; nothing real-looking (public repo)
- [ ] No comments/Javadoc beyond a justified one-liner; no copyright header
- [ ] Fixture reused, or new one is small/synthetic/under `input/`; `data/input/transformer-input.*` untouched
- [ ] `build.gradle` touched only to add a missing module line
- [ ] No output files, license file, or IDE output committed
- [ ] If this example also exists in the core repo's `example/src/java`: mirror noted
