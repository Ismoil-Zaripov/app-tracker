---
name: verify-task
description: End-of-step and end-of-phase checklists for phase-based development.
---

## End-of-step checklist (run after every step)

1. **Build**: `gradlew.bat assembleDebug` — must succeed with 0 errors.
2. **Fix loop**: if errors, fix and retry up to 3 times total. If still failing after 3 attempts, note BLOCKED in `docs/PROGRESS.md`, skip to next independent step.
3. **Update docs**: check off the step in `docs/PROGRESS.md`: `- [x] Step N: <title>`.
4. **Commit**: `git commit -m "phase N step M: verb noun"`.
5. **Continue** to the next step immediately without waiting for user input.

## End-of-phase checklist (run after the last step)

1. **Build**: `gradlew.bat assembleDebug` — must succeed.
2. **Unit tests**: `gradlew.bat testDebugUnitTest` — all pass.
3. **Fix loop**: up to 3 attempts. If still failing, mark BLOCKED with reason in `docs/PROGRESS.md`.
4. **Update docs**: mark phase DONE in `docs/PROGRESS.md`: change `` `NOT STARTED` `` to `` `DONE — YYYY-MM-DD` ``.
5. **Commit**: `git commit -m "phase N done"`.
6. **Stop**: print exactly `"PHASE N DONE. Run /clear, then start phase N+1."` and stop.
