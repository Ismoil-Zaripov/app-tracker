---
name: uzbek-strings
description: Rules for writing Uzbek Latin strings.xml entries consistently.
---

## Rules
- File: `app/src/main/res/values/strings.xml` (single file, no locale subfolder).
- Apostrophe style: `o'`, `g'` — these **must** be XML-escaped as `o&apos;` / `g&apos;` in XML attributes, but are fine as-is in element text if the string uses `"..."` quotes.
- Never use backtick or curly apostrophe; always straight apostrophe `'`.

## Canonical term table
| Concept | Uzbek Latin |
|---|---|
| Date | Sana |
| Hours worked | Ishlagan soat |
| Closed task count | Yopilgan task soni |
| Main topic | Asosiy mavzu |
| Understanding level | Tushunish darajasi |
| Energy | Energiya |
| Focus | Fokus |
| Today's biggest win | Bugungi eng katta g'alaba |
| Today's biggest challenge | Bugungi eng katta qiyinchilik |
| Save | Saqlash |
| Dashboard | Dashboard |
| History | Tarix |
| Weekly report | Haftalik hisobot |
| Current streak | Joriy streak |
| Average energy | O'rtacha energiya |
| Average focus | O'rtacha fokus |
| No entry today | Bugun yozuv kiritilmagan |

## XML escaping example
```xml
<string name="avg_energy">O’rtacha energiya</string>
<!-- preferred: use apostrophe entity -->
<string name="avg_energy">O&apos;rtacha energiya</string>
```
