# Playbook: publish an example to datapipeline.io

How a file in this repo becomes `https://datapipeline.io/docs/examples/<slug>`.
The record lives in the **DataPipeline.io CMS** (content type `example`); nothing
in that repo's website code changes for a new example. This playbook documents the
boundary from this side; CMS/website internals are in
`DataPipeline.io/cms/.github/copilot-instructions.md` and
`DataPipeline.io/website/docs/authoring/`.

## 0. Sequence (order matters)
1. Code PR merged to **`master`** here. The site fetches
   `raw.githubusercontent.com/NorthConcepts/DataPipeline-Examples/master/...`, so an
   unmerged branch cannot render.
2. Create the CMS record (or edit the existing one), **preview**, then **publish**.
3. Within 15 minutes the website's `DocsService` sees the new `published_on`, drops
   its cached GitHub sources, rebuilds search, and the page is live in its lists.

## 1. The CMS record (one per example)
| Field | Value | Notes |
|---|---|---|
| Type | `example` | fixed |
| Title | the how-to phrase with spaces — "Write a CSV File to Fixed Width" | = the class name, worded for humans |
| Slug | kebab-case of the title — `write-a-csv-file-to-fixed-width` | becomes the URL; never change after publish without a redirect plan |
| Category | one of `basic-io` · `data-manipulation` · `customization` · `datapipeline-foundations` | drives the home-page cards and `/category/<code>` lists; Foundations examples always `datapipeline-foundations` |
| Tags | from the CMS tag vocabulary, e.g. `csv`, `xml`, `json`, `excel`, `jdbc`, `parquet`, `orc`, `avro`, `amazon-s3`, `google`, `transform`, `filter`, `lookup`, `aggregate`, `validate`, `debug`, `performance`, `threading`, `exceptions`, `data-lineage`, `pipeline`, `schema`, `data-mapping`, `decision-table`, `decision-tree`, `declarative`, `el`, `template`, `in-memory`, `stdout`, `simple`, `customize` | 2–4 tags; `datapipeline-foundations` is also a tag on DPF examples; add a new tag only via the CMS tag admin |
| GitHub URL | `https://github.com/NorthConcepts/DataPipeline-Examples/blob/master/src/main/java/com/northconcepts/datapipeline/examples/<area>/<Class>.java` | the **blob** form on `master`; the CMS auto-corrects `/tree/` → `/blob/` but nothing else |
| Description | one sentence for SEO | if empty the site shows "How to <title>" (titles starting "My " are used as-is) |
| Summary | optional short teaser for lists | |
| Body | **leave empty** for a code-only page | the site then renders `<pre class="prettyprint lang-java linenums">${github.getRawContent(githubUrl)}</pre>` |

## 2. When the page needs prose
Write the body in the CMS as HTML with FreeMarker in scope. Embed the code where
it belongs with:

```
<pre class="prettyprint lang-java linenums">
${github.getRawContent(record.githubUrl)}
</pre>
```

`${datapipelineVersion}` and `${datapipelineReleaseDate}` are also available. Keep
the prose in the CMS — never in the Java file.

## 3. After a code change to a published example
The website caches each fetched source **until a CMS publish changes the latest
`published_on`** (polled every 15 min) or the webapp restarts. So after merging a
fix: open the record → edit (a no-op save is fine) → publish. Nothing else flushes it.

## 4. Renames and removals
- Rename/move in this repo ⇒ update `github_url` in the same release, or the page
  renders an empty `<pre>`.
- Retiring an example ⇒ unpublish/trash the CMS record **first**, then delete the
  file; the old GitHub URL still 404s, so keep the file if third parties link to it.

## 5. What this repo should hand over (in the PR description)
```
Title:    Read a Parquet File from Amazon S3
Slug:     read-a-parquet-file-from-amazon-s3
Category: basic-io
Tags:     parquet, amazon-s3
GitHub:   https://github.com/NorthConcepts/DataPipeline-Examples/blob/master/src/main/java/com/northconcepts/datapipeline/examples/amazons3/ReadParquetFromAmazonS3.java
Body:     (none)  |  (one-paragraph intro suggested: ...)
```

## 6. Known state (2026-10)
- The CMS's local dev database only holds the V9 seed (242 examples imported from
  the legacy site); real records exist only on the production droplet.
- DataPipeline.io's own `Example.md` recipe (website-side) is still planned in its
  `website/docs/authoring/ROADMAP.md`; when it lands, link it from here.
