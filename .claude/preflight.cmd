@echo off
rem claude-preflight.cmd — Lightweight tool availability check
echo =========================================
echo   PREFLIGHT CHECK
echo =========================================
if exist .codegraph\codegraph.db (
    echo [OK] codegraph DB found
) else (
    echo [MISSING] codegraph DB — run: npx @colbymchenry/codegraph init -i .
)
if exist .graphify\graph.json (
    echo [OK] graphify graph found
) else (
    echo [MISSING] graphify graph — run: npx -y @nodesify/graphify update .
)
echo =========================================
