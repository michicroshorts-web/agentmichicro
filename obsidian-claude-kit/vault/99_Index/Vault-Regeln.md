---
type: index
status: canon
created: 2026-05-27
---

# Vault-Regeln (Single Source of Truth)

Diese Datei legt fest, wie der Vault strukturiert ist. Sie wird von Mensch, Schreibschicht (Claude Desktop/Code) **und** Sortier-Agent gelesen. Änderungen hier wirken überall.

## Status-Lebenszyklus

| status | Bedeutung |
|---|---|
| `inbox` | frisch von Claude/Import abgelegt, noch nicht eingeordnet |
| `reviewed` | vom Sortier-Agent klassifiziert/verlinkt, wartet auf deine Freigabe |
| `canon` | von dir bestätigt, gilt als wahr — **wird nie automatisch überschrieben** |

## Sichtbarkeit

`visibility:` einer von `private` · `player` · `dm-only` · `spoiler`.

## Typen → Zielordner

| type | Zielordner | Beispiel |
|---|---|---|
| `session` | `01_Sessions/` | Sitzungsprotokoll |
| `npc` | `02_World/NPCs/` | Person/Wesen |
| `location` | `02_World/Orte/` | Ort/Region |
| `faction` | `02_World/Fraktionen/` | Gruppe/Organisation |
| `item` | `02_World/Items/` | Gegenstand |
| `rule` | `03_Rules/` | Regel/Hausregel |
| `adventure` | `04_Adventures/` | Abenteuer/Quest |
| `player-knowledge` | `05_Player-Knowledge/` | was Spieler wissen dürfen |
| `dm-only` | `06_DM-Only/` | Geheimwissen |
| `memory` | bleibt in `00_Inbox/` | Roh-Eingang — Sortier-Agent setzt den echten Typ |

> `type: memory` ist nur der Roh-Zustand. Der Sortier-Agent ersetzt ihn durch den passenden Typ oben und verschiebt die Notiz.

## Frontmatter-Standard

```yaml
---
type: <siehe Tabelle>
status: inbox        # inbox → reviewed → canon
visibility: private
source: "<woher>"
tags: []
created: JJJJ-MM-TT
updated: JJJJ-MM-TT
---
```

## Erlaubte Überschriften (beim Einsortieren ergänzbar)

Der Sortier-Agent darf Inhalt **nur** unter diesen Überschriften hinzufügen — bestehender Text darunter bleibt erhalten:

- `## Neue Informationen`
- `## Quellen`
- `## Offene Fragen`
- `## Verbindungen`

## Guardrails (für jede automatische Verarbeitung)

1. **Nie löschen.** Notizen werden verschoben/ergänzt, nie entfernt.
2. **`canon` ist tabu** — nie überschreiben; bei Konflikt eine Review-Notiz erzeugen.
3. **Nur additiv** unter den erlaubten Überschriften ergänzen.
4. **Verlinken statt duplizieren:** existiert das Thema schon, per `[[Wikilink]]` verbinden, keine Dublette anlegen.
5. **Bei Unsicherheit** (Typ unklar, mögliche Dublette, Konflikt) → nichts einsortieren, stattdessen Notiz nach `00_Inbox/Review/` mit Begründung.
6. **Alles bleibt prüfbar:** Vault als Git-Repo, Änderungen via `git diff` kontrollierbar.
