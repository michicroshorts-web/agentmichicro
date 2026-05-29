---
description: Speichert eine Erinnerung/Erkenntnis als neue Notiz in 00_Inbox/.
---

Speichere den folgenden Inhalt als **neue** Memory-Notiz in `00_Inbox/`.

Inhalt: $ARGUMENTS

Regeln:
- Neue Datei `00_Inbox/JJJJ-MM-TT-HHMM-kurztitel.md` — nichts überschreiben.
- Frontmatter exakt:
  ```yaml
  ---
  type: memory
  status: inbox
  visibility: private
  source: "Gespräch mit Claude (Code), <heutiges Datum>"
  tags: []
  created: <heutiges Datum>
  ---
  ```
- Danach eine kurze H1-Überschrift und der Inhalt klar/knapp, mit Kontext.
- Ein Thema pro Notiz. Mehrere Themen → mehrere Notizen.
- Schreibe ausschließlich in `00_Inbox/`. Bestätige danach kurz Dateiname und Inhalt.
