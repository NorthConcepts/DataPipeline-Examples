# Playbook: review an example PR

What to check when reviewing a change to this repo, in the order that finds
problems fastest. Everything here is a consequence of one fact: **the file is the
published page.**

## 0. Scope the diff (30 seconds)
- One example (or one tightly related set) + its fixture + at most one
  `build.gradle` line? Anything else (reformatting, renames, deletions, unrelated
  files) needs a ticket reference in the description.
- Branch/commit/PR title start with `DP-NNNN`.
- No `example/data/output/*`, `data/output/*`, license file, `bin/`, `build/`.

## 1. Compile truth
- Style: Java 21 toolchain, but the page style is plain — flag `var`, `record`, `"""`,
  `switch ->` unless they clearly help the reader.
- New module used? The matching `implementation` line exists at `${version}`.
- Ideally: checkout + `gradlew.bat compileJava --no-daemon` on JDK 21 (CI does
  `build`; same thing here).

## 2. The example contract ([copilot-instructions](../../.github/copilot-instructions.md) → "What an example looks like")
- Name is the how-to phrase; package is the product area; no `Demo/Test/Example` suffix.
- One capability, 20–60 lines, reader → transform → writer → `Job.run`.
- Relative paths from the repo root; fixture exists (or is added, small, under `input/`).
- Secrets/hosts are obvious placeholders — **no real keys, tokens, bucket names,
  hostnames, emails** (this repo is public; treat any real-looking value as a
  blocker).
- No comments except a justified one-liner; no Javadoc; **no copyright header**
  (a header means it was pasted from the core repo).
- No `JobTemplate`, no `System.exit`, no arg parsing, no second public class.

## 3. Fixtures ([DataFixture.md](DataFixture.md))
- Not modifying `data/input/transformer-input.*` / `avro_schema_file.avsc` (other
  repo's CI).
- Not renaming a fixture other examples read (`grep -rl <name> src/main/java`).
- Large/binary files go through LFS (`.gitattributes`).
- Synthetic data only.

## 4. Publishing readiness ([PublishExample.md](PublishExample.md))
- Description carries proposed **title, slug, category, tags** and sample output, so
  the CMS record can be created right after merge.
- For an **edit** to a published example: the description notes that the CMS record
  must be re-published (cache) and, for a rename, that `github_url` changes.
- For an example that also exists in the core repo's `example/src/java`: the
  description says whether/how it will be mirrored.

## 5. Verdict shape
Approve when §1–§3 pass; request changes for any real-looking secret,
header, rename without ticket, or fixture change that hits another repo.
Everything in §4 is a comment, not a blocker — but it is the reason the next step
goes smoothly.
