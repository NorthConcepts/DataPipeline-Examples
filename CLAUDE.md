# CLAUDE.md

Guidance for Claude Code / agents working in **DataPipeline-Examples** — the public
repository of DataPipeline's Java code examples. Each example file is the content of
a page under `https://datapipeline.io/docs/examples/`, pulled from GitHub `master` by
the DataPipeline.io website at render time.

## Canonical guidance (read these first)

This file is intentionally thin. The authoritative rules live in:

- **[`.github/copilot-instructions.md`](.github/copilot-instructions.md)** — the
  canonical contributor/agent guide (scope, stack, layout, the example contract,
  fixtures, the publishing boundary, change discipline). **Defer to it on any
  conflict.**
- **[`docs/authoring/`](docs/authoring/)** — one-screen **recipes** for the few
  things that get done here, plus copy-paste
  [`_skeletons/`](docs/authoring/_skeletons/) and curated
  [`exemplars.yaml`](docs/authoring/exemplars.yaml). Load the **one** recipe that
  matches your task and copy a skeleton instead of synthesizing or reading dozens
  of existing examples.

## North star: cheap AI assistance

An agent should be able to add, update, or review an example by reading ~2 small
files (a recipe + its exemplar), not by surveying 400. Every example follows one
shape, so the recipes are short and the skeletons are nearly complete. When you
notice a missing or stale recipe, fix it in the same spirit: thin, real in-tree
exemplars, no duplication of `copilot-instructions.md`. The plan for this system is
**[`docs/authoring/ROADMAP.md`](docs/authoring/ROADMAP.md)**.

## Pick the right recipe (quick map)

| You want to… | Recipe | Copy |
|---|---|---|
| Add a new example (new capability, new module, new variant) | [Example.md](docs/authoring/Example.md) | [`_skeletons/`](docs/authoring/_skeletons/) `FileExample` / `MemoryExample` / `ExternalServiceExample` / `CustomComponentExample` |
| Add or change an input data file | [DataFixture.md](docs/authoring/DataFixture.md) | — |
| Change an example that already exists (API change, bug, version bump) | [UpdateExample.md](docs/authoring/UpdateExample.md) | — |
| Review an example PR | [ReviewExample.md](docs/authoring/ReviewExample.md) | — |
| Get the example onto datapipeline.io (the CMS record) | [PublishExample.md](docs/authoring/PublishExample.md) | — |
| Understand what already exists for an area | `ls src/main/java/com/northconcepts/datapipeline/examples/<area>` (or `foundations/examples/<area>`); file names are the page titles | — |

For *which DataPipeline class to use* (reader/writer/transformer/filter/lookup
APIs), the sibling **core** repo's guides apply:
`DataPipeline/docs/authoring/*.md` — the examples here are the public, runnable
illustrations of those contracts (`cookbook/customization/My*.java` are the
canonical "write my own X" examples).

## Build

CI-equivalent check (JDK 21; there are no tests), then run one example:

```bat
gradlew.bat compileJava --no-daemon
gradlew.bat run --quiet -PclassToExecute=com.northconcepts.datapipeline.examples.cookbook.WriteACsvFileToFixedWidth
```

Java 21 source/target, Gradle 8.5 (vendored wrapper), published DataPipeline
artifacts from `mavenLocal()` + `maven.northconcepts.com` at the single `version`
in `build.gradle`. Running needs the gitignored
`src/main/resources/NorthConcepts-DataPipeline.license` and the **repo root as
working directory**. See `copilot-instructions.md` → "Tech stack and build".

## Most-missed rules

- **The file is the page.** Write for a reader: one capability, 20–60 lines,
  `Job.run(reader, writer)`, placeholder constants for credentials, no scaffolding.
- **Java 21 toolchain, plain style** — lambdas are fine; avoid `var`, records, text
  blocks and switch expressions unless they clearly read better on the page.
- **No comments / no Javadoc by default; never a copyright header** (this public
  repo is Apache-2.0; the proprietary header belongs to the core repo's copies).
- **Don't rename, move, or delete** an existing example or fixture without a
  ticket — each is a live URL, and `data/input/transformer-input.*` is fetched by
  DataConverter.io's CI straight from `master`.
- **Publish sequence:** merge the code to `master` first, then create/publish the
  CMS record; a code-only fix to a published example is invisible on the site until
  something is re-published in the CMS (the website caches raw files).
- Branch/commit/PR start with the Jira ticket (`DP-1234 …`); one example set per PR.

## Related repositories

`DataPipeline` (core), `DataPipeline-Foundations`, `DataPipeline-Integrations`,
`DataPipeline-FileSystems` publish the artifacts these examples compile against.
`DataPipeline.io` hosts the CMS that owns the page records and the website that
renders them (`website/.../Examples3Resource.java`, `DocsService.java`;
authoring recipes in `DataPipeline.io/website/docs/authoring/`). `DataConverter.io`
consumes `data/input/` fixtures in its tests. In this workspace they are sibling
folders (`../DataPipeline`, `../DataPipeline.io`, …).
