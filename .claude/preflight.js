#!/usr/bin/env node
/**
 * claude-preflight.js — Run before ANY code modification
 * Usage: node .claude/preflight.js [symbol]
 *
 * Lightweight check — only verifies tool indices exist.
 * Actual tool queries are run via bash in the preflight checklist.
 */

const fs = require('fs');
const path = require('path');

const symbol = process.argv[2] || '(see reasoning)';
const root = path.resolve(__dirname, '..');

const checks = [
  { name: 'codegraph DB',      path: '.codegraph/codegraph.db',      cmd: 'npx @colbymchenry/codegraph query|callers|impact' },
  { name: 'graphify graph',    path: '.graphify/graph.json',         cmd: 'npx -y @nodesify/graphify query' },
];

let allOk = true;
console.log('═══════════════════════════════════════');
console.log('  PREFLIGHT CHECK — Files only');
console.log('═══════════════════════════════════════');
console.log(`  Symbol: ${symbol}`);
console.log('');

checks.forEach(c => {
  const fp = path.join(root, c.path);
  const ok = fs.existsSync(fp);
  if (!ok) allOk = false;
  const size = ok ? `(${(fs.statSync(fp).size / 1024 / 1024).toFixed(1)}MB)` : '';
  console.log(`  ${ok ? '✅' : '❌'} ${c.name} ${size}`);
  if (!ok) console.log(`     Run: ${c.cmd}`);
});

console.log('');
console.log(`  Result: ${allOk ? '✅ PASS' : '❌ FAIL — missing indices'}`);
console.log('═══════════════════════════════════════');
