## graphify

This project has a nodesify-graphify knowledge graph at .graphify/.

Rules:
- MUST read .graphify/graph_report.md before searching files for architecture or codebase questions
- MUST use `nodesify-graphify query "<question>"`, `nodesify-graphify path "<A>" "<B>"`, or `nodesify-graphify explain "<concept>"` for cross-module questions — do NOT grep/read files directly for these
- After modifying code files in this session, run `nodesify-graphify update .` to keep the graph current

## code mod preflight (MANDATORY)

### Tool availability matrix (current environment)

| Tool | Status | How to use |
|------|--------|------------|
| `nodesify-graphify query` | ✅ Available | `npx -y @nodesify/graphify query "<question>"` |
| `codegraph query` (≈explore) | ✅ Available | `npx @colbymchenry/codegraph query "<symbol>"` |
| `codegraph callers` | ✅ Available | `npx @colbymchenry/codegraph callers "<symbol>"` |
| `codegraph impact` | ✅ Available | `npx @colbymchenry/codegraph impact "<symbol>"` |

All codegraph commands run via `npx @colbymchenry/codegraph` — no MCP server needed.

### Preflight checklist (before ANY edit/write)

Run this in reasoning, do not skip any step:

1. `[graphify pre-flight] already queried? ✅/❌  query: "<symbol>"`
2. `[codegraph pre-flight] already queried? ✅/❌  query: "<symbol>"`
   - codegraph query → for searching symbols
   - codegraph callers → for finding callers
   - codegraph impact → for impact analysis
3. If any tool is ❌ → run: `npx @colbymchenry/codegraph status` first, then retry
4. If tool truly unavailable despite config → state the gap explicitly, do NOT substitute with grep/read

### Initial setup (one-time)
```bash
# Already done: codegraph init completed with 54K+ nodes
npx @colbymchenry/codegraph status  # verify index health
```

<!-- gitnexus:start -->
# GitNexus — Code Intelligence

This project is indexed by GitNexus as **新建文件夹 (2)** (34626 symbols, 79680 relationships, 300 execution flows). Use the GitNexus MCP tools to understand code, assess impact, and navigate safely.

> Index stale? Run `node .gitnexus/run.cjs analyze` from the project root — it auto-selects an available runner. No `.gitnexus/run.cjs` yet? `npx gitnexus analyze` (npm 11 crash → `npm i -g gitnexus`; #1939).

## Always Do

- **MUST run impact analysis before editing any symbol.** Before modifying a function, class, or method, run `impact({target: "symbolName", direction: "upstream"})` and report the blast radius (direct callers, affected processes, risk level) to the user.
- **MUST run `detect_changes()` before committing** to verify your changes only affect expected symbols and execution flows. For regression review, compare against the default branch: `detect_changes({scope: "compare", base_ref: "main"})`.
- **MUST warn the user** if impact analysis returns HIGH or CRITICAL risk before proceeding with edits.
- When exploring unfamiliar code, use `query({query: "concept"})` to find execution flows instead of grepping. It returns process-grouped results ranked by relevance.
- When you need full context on a specific symbol — callers, callees, which execution flows it participates in — use `context({name: "symbolName"})`.

## Never Do

- NEVER edit a function, class, or method without first running `impact` on it.
- NEVER ignore HIGH or CRITICAL risk warnings from impact analysis.
- NEVER rename symbols with find-and-replace — use `rename` which understands the call graph.
- NEVER commit changes without running `detect_changes()` to check affected scope.

## Resources

| Resource | Use for |
|----------|---------|
| `gitnexus://repo/新建文件夹 (2)/context` | Codebase overview, check index freshness |
| `gitnexus://repo/新建文件夹 (2)/clusters` | All functional areas |
| `gitnexus://repo/新建文件夹 (2)/processes` | All execution flows |
| `gitnexus://repo/新建文件夹 (2)/process/{name}` | Step-by-step execution trace |

## CLI

| Task | Read this skill file |
|------|---------------------|
| Understand architecture / "How does X work?" | `.claude/skills/gitnexus/gitnexus-exploring/SKILL.md` |
| Blast radius / "What breaks if I change X?" | `.claude/skills/gitnexus/gitnexus-impact-analysis/SKILL.md` |
| Trace bugs / "Why is X failing?" | `.claude/skills/gitnexus/gitnexus-debugging/SKILL.md` |
| Rename / extract / split / refactor | `.claude/skills/gitnexus/gitnexus-refactoring/SKILL.md` |
| Tools, resources, schema reference | `.claude/skills/gitnexus/gitnexus-guide/SKILL.md` |
| Index, status, clean, wiki CLI commands | `.claude/skills/gitnexus/gitnexus-cli/SKILL.md` |

<!-- gitnexus:end -->
