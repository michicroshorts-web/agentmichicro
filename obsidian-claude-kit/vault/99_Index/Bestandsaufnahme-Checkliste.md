---
type: index
status: canon
created: 2026-05-27
---

# Schritt 0 — Bestandsaufnahme & Bewertung

Grundprinzip: **erst prüfen, dann anpassen.** Nichts blind überschreiben. Geh diese Liste durch, bevor du Dateien aus dem Kit kopierst — kopiere danach nur, was fehlt, und passe Vorhandenes gezielt an.

## A. Vault-Struktur

- [ ] Welche Top-Level-Ordner existieren bereits?
- [ ] Gibt es schon eine Inbox (oder etwas Vergleichbares)?
- [ ] Welche Frontmatter-Felder benutzt du heute schon (`type`, `tags`, `status` …)?
- [ ] Wird der Vault bereits als Git-Repo geführt? (Empfohlen — sonst `git init`.)

→ Ergebnis: Passt die Taxonomie in `Vault-Regeln.md` zu deiner Realität, oder muss sie angepasst werden?

## B. Bestehender Sortier-Agent

- [ ] Existiert er als Datei? Wo? (`.claude/agents/…`, anderes Skript, manueller Prozess?)
- [ ] Was genau tut er — klassifizieren, verlinken, verschieben, löschen?
- [ ] **Test an echten Beispielen:** 3–5 Inbox-Notizen durchlaufen lassen.
  - [ ] Klassifiziert er den Typ korrekt?
  - [ ] Setzt er sinnvolle `[[Wikilinks]]`?
  - [ ] Landet alles im richtigen Ordner?
  - [ ] Macht er Fehlgriffe? Löscht/überschreibt er etwas, das er nicht sollte?
- [ ] Grobe **Fehlerrate** notieren (z.B. „2 von 5 falsch einsortiert").

→ Entscheidung je nach Ergebnis:
- **Behalten** (zuverlässig) → nur sicherstellen, dass er das `type: memory`-Format der Schreibschicht versteht.
- **Verbessern** (meist richtig, einzelne Fehler) → Prompt/Guardrails schärfen (Vorlage: `.claude/agents/vault-sorter.md`).
- **Ersetzen** (fehlt oder zu unzuverlässig) → den `vault-sorter` aus dem Kit übernehmen.

## C. Verbindungen / Plugins

- [ ] Ist „Local REST API" (oder ein anderer MCP-Server) schon installiert/aktiv?
- [ ] Ist Claude Desktop oder Claude Code bereits irgendwie an den Vault angebunden?
- [ ] Falls ja: funktioniert Schreiben in die Inbox schon? Dann nur die Memory-Konvention angleichen.

## D. Ergebnis festhalten

Kurze Ist-Aufnahme notieren und je Komponente entscheiden: **behalten · verbessern · ersetzen · neu bauen**. Erst dann die Setup-Schritte (`SETUP-Claude-Obsidian.md`) ausführen.
