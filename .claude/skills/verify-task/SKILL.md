---
name: verify-task
description: End-of-task checklist: build, test, fix, document, commit.
---

## Checklist (run in order)

1. **Build**: `gradlew.bat assembleDebug` — must succeed with 0 errors.
2. **Unit tests**: `gradlew.bat testDebugUnitTest` — all pass.
3. **Fix loop**: if errors, fix and retry up to 3 times total. If still failing after 3 attempts, note blocker in `docs/PROGRESS.md` and stop.
4. **Update docs**: append one line to `docs/PROGRESS.md`:
   `- [x] Task N: <title> — <date YYYY-MM-DD>`
5. **Commit**:
   ```
   git add <changed files>
   git commit -m "task N: <lowercase title>"
   ```
   Message format: `task N: verb noun` (e.g. `task 3: add room database`).
6. **Continue** to the next task immediately without waiting for user input.
