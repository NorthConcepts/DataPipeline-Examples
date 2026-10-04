# Recipe: add or change an input data file

> Fixtures are read by published examples and, for `data/input/`, by another
> repository's CI. Shared rules: [`copilot-instructions.md`](../../.github/copilot-instructions.md)
> → "Data fixtures".

## 1. Purpose / when to pick this
Add a fixture only when no existing file demonstrates the capability (a new format,
a specific edge case such as nulls, BOMs, invalid Excel expressions, nested JSON).
Change an existing fixture only with a ticket — every example and page that reads
it changes too.

## 2. Files you will touch
| # | File | When |
|---|---|---|
| 1 | `example/data/input/<name>.<ext>` | default root for examples |
| 2 | `data/input/<name>.<ext>` | the "one dataset, every format" set (`transformer-input.*`, `nested-data.*`) and files other repos fetch |
| 3 | `.gitattributes` | only for a large/binary file that must go through LFS |
| 4 | every example that reads a *renamed* fixture (`grep -rl "<old-name>" src/main/java`) | renames only |

## 3. The content contract
- Small (a few KB; ≤ ~20 rows is plenty). Realistic but obviously synthetic data —
  no real people, accounts, keys, or customer names. Reuse the existing cast
  (`credit-balance` accounts, `purchases`, `products`, `countries`).
- Text files: UTF-8, header row for CSV/TSV, consistent line endings with the
  file's neighbours. Binary formats (xlsx, parquet, orc, avro, pdf, images) are
  committed as-is; keep them under ~250 KB.
- Name by content, lower-kebab or snake as its neighbours (`credit-balance-01.csv`,
  `call-center-inbound-call.xlsx`, `json-input-with-lines.jsonl`); an example-specific
  file may be prefixed with the class name (`WriteAJsonFileUsingFreeMarkerTemplates-header.json`).

## 4. The naming contract
See §3. Never reuse a name with different contents; add a suffix (`-2`, `-with-nulls`).

## 5. Registration
Not applicable — examples reference fixtures by relative path; nothing indexes them.

## 6. Routing
Not applicable.

## 7. Content
`data/input/transformer-input.<ext>` is the same small dataset in every supported
format. When adding a **format**, add `transformer-input.<newext>` converted from
`transformer-input.csv`/`.png` so the set stays one dataset (the WebP fixture was a
pixel-identical conversion of the PNG).

## 8. Events / tracking
Not applicable.

## 9. Generated content
If a fixture is produced by code (parquet/orc/avro from a CSV), say so in the PR
and name the generating example so it can be regenerated.

## 10. Validation & limits
- `git lfs ls-files` for anything matched by `.gitattributes`; CI checks out LFS.
- Output folders (`example/data/output/`, `data/output/`) are gitignored — a fixture
  must live under `input/` or it won't be committed.

## 11. Tests / verification
Run one example that reads the fixture from the repo root; check `git status`
shows only the intended files.

## 12. Antipatterns & gotchas
- **Changing `data/input/transformer-input.*` or `avro_schema_file.avsc` contents** —
  DataConverter.io's URL-upload tests fetch them from `master` and snapshot the
  result; a content change there is a test failure in another repo.
- Renaming a fixture without updating every example that reads it (there is no
  compile-time check for string paths).
- Committing a multi-MB file outside LFS.
- Putting a fixture next to the Java file — resources are not on the examples'
  path; they read from the two data roots only.

## 13. Exemplar + checklist
Exemplars: `example/data/input/credit-balance-01.csv` (classic CSV),
`data/input/transformer-input.jsonl` (format set member), `.gitattributes` (LFS rules).

- [ ] no existing fixture fits
- [ ] small, synthetic, named by content, under the right root
- [ ] LFS rule if large; `git lfs ls-files` confirms
- [ ] not a modification of a file another repo fetches
- [ ] every reading example updated if renamed
