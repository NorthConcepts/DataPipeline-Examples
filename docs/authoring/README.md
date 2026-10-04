# Authoring recipes for DataPipeline-Examples

Short, agent-readable **recipes** for the few things that get done in this repo.
Each is compact (about one screen) so an LLM/agent loads a single file instead of
surveying 400 example classes.

If you are an LLM/agent adding or changing an example, load the **one** recipe that
matches your task and **copy** the matching skeleton from [`_skeletons/`](_skeletons/)
rather than synthesizing from scratch.

These recipes complement — they do **not** duplicate —
[`.github/copilot-instructions.md`](../../.github/copilot-instructions.md)
(canonical rules). A recipe expands the *workflow* for one task and links back for
everything else.

## Why recipes shaped this way

This repo is **content, not a framework**: every example is the same small shape
(reader → optional transform → writer → `Job.run`) and becomes a web page whose
record lives in another system (the DataPipeline.io CMS). So the recipes are
*multi-step checklists* over code + fixture + build line + publish record, and the
cost win comes from the skeletons being almost complete.

## Index

| Task | Recipe | Skeleton(s) | Status |
|---|---|---|---|
| Add a new example | [Example.md](Example.md) | [FileExample](_skeletons/FileExample.java.txt) · [MemoryExample](_skeletons/MemoryExample.java.txt) · [ExternalServiceExample](_skeletons/ExternalServiceExample.java.txt) · [CustomComponentExample](_skeletons/CustomComponentExample.java.txt) | ✅ available |
| Add or change an input data file | [DataFixture.md](DataFixture.md) | — | ✅ available |

## Playbooks (end-to-end journeys)

Journeys, not 13-section recipes — they orchestrate the recipes above.

| Journey | Playbook | Status |
|---|---|---|
| Change an example that already exists (API change, bug, dependency bump) | [UpdateExample.md](UpdateExample.md) | ✅ available |
| Review an example PR | [ReviewExample.md](ReviewExample.md) | ✅ available |
| Get an example onto datapipeline.io (the CMS record) | [PublishExample.md](PublishExample.md) | ✅ available |

See also:

- [exemplars.yaml](exemplars.yaml) — one curated, in-tree exemplar per example
  pattern (the known-good file to copy from).
- [ROADMAP.md](ROADMAP.md) — the plan behind this system and what is still open.
- [`/CLAUDE.md`](../../CLAUDE.md) — the repo entry point and quick map.
- The sibling repos' guides for *which DataPipeline API to use*:
  `DataPipeline/docs/authoring/` (core bases), and for the website/CMS internals
  `DataPipeline.io/website/docs/authoring/` and `DataPipeline.io/cms/`.

## Conventions

- Skeletons are named `*.java.txt` on purpose, so they are never compiled.
- Recipes follow the same **fixed 13-section order** as the sibling repos
  (see [Example.md](Example.md)): 1 Purpose · 2 Files you will touch · 3 The code
  contract · 4 The naming contract · 5 Registration (build.gradle) · 6 Routing
  (where it lands on the site) · 7 Content (fixtures / prose) · 8 Events/tracking ·
  9 Generated content · 10 Validation & limits · 11 Tests/verification · 12
  Antipatterns & gotchas · 13 Exemplar + checklist. A section that doesn't apply
  keeps its heading and says *"not applicable"* so the order stays stable.
- A recipe never restates a rule already in `copilot-instructions.md`; it links.
- Exemplars point at the **smallest** in-tree file that shows the whole pattern;
  their paths live in `exemplars.yaml` (single place to fix on drift).
