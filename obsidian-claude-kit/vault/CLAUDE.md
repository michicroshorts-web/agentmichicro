# CLAUDE.md — Regeln für Claude Code in diesem Vault

Dies ist ein Obsidian-Vault (Wissensbasis). Wenn du hier als Claude Code arbeitest, gelten folgende Regeln. Die vollständige Taxonomie steht in `99_Index/Vault-Regeln.md` — bei Unsicherheit dort nachsehen.

## Erinnerungen speichern (Gedächtnis-Schicht)

**Wann speichern:**
- **Auf Befehl:** wenn der Nutzer „merk dir das", „speichere das in Obsidian" o.ä. sagt (oder `/remember` nutzt).
- **Automatisch:** wenn im Gespräch etwas Wichtiges/Bleibendes entsteht (Entscheidung, Fakt, Erkenntnis, NPC/Ort/Regel), speichere es proaktiv — nenne danach kurz, dass und was du gespeichert hast.

**Wie speichern:**
- Immer als **neue Datei in `00_Inbox/`** — niemals woanders, niemals bestehende Notizen verändern.
- Dateiname: `00_Inbox/JJJJ-MM-TT-HHMM-kurztitel.md` (eindeutig, nichts überschreiben).
- Vorlage: `90_Templates/memory.md`. Frontmatter exakt einhalten:
  ```yaml
  ---
  type: memory
  status: inbox
  visibility: private
  source: "Gespräch mit Claude (Code), <Datum>"
  tags: []
  created: <JJJJ-MM-TT>
  ---
  ```
- Eine Notiz = ein Thema. Mehrere Themen → mehrere Notizen.
- Inhalt knapp und klar, mit Kontext (worum ging es, warum wichtig).

## Grenzen

- Schreibe ungefragt **nur** in `00_Inbox/`. In andere Ordner einsortiert wird **nur** über den Sortier-Agenten (`/sort-inbox`).
- **Niemals** Notizen löschen. **Niemals** `status: canon` überschreiben.
- Beim Einsortieren nur unter den erlaubten Überschriften ergänzen (siehe `Vault-Regeln.md`).

## Werkzeuge

- `/remember <text>` — Erinnerung sofort in die Inbox schreiben.
- `/sort-inbox` — Inbox klassifizieren, verlinken, einsortieren (mit Review).
