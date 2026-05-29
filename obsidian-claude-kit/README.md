# Obsidian-Claude-Kit

Werkzeugkasten, um **Claude Desktop** *und* **Claude Code** direkt in dein lokales **Obsidian** schreiben zu lassen — Erinnerungen/Erkenntnisse landen in der Inbox, ein Sortier-Agent räumt sie ein.

> Dieses Git-Repo ist eigentlich eine Website (MichiCroShorts). Das Kit liegt hier nur als Ablage. **Es gehört in deinen lokalen Obsidian-Vault** — siehe unten.

## Was hier drin ist

```
obsidian-claude-kit/
├── README.md                      ← diese Datei
├── SETUP-Claude-Obsidian.md       ← Schritt-für-Schritt: Verbindung lokal einrichten (zuerst lesen!)
└── vault/                         ← Inhalt in die WURZEL deines Vaults kopieren
    ├── CLAUDE.md                  ← Gedächtnis-Regeln für Claude Code
    ├── .claude/
    │   ├── agents/vault-sorter.md ← Sortier-Agent (Vorschlag — erst deinen prüfen!)
    │   └── commands/
    │       ├── remember.md        ← /remember  (Erinnerung auf Befehl)
    │       └── sort-inbox.md      ← /sort-inbox (Inbox einsortieren)
    ├── 00_Inbox/                  ← Eingang für alles, was Claude schreibt
    ├── 90_Templates/              ← Vorlagen (memory, npc, session …)
    └── 99_Index/
        ├── Vault-Regeln.md        ← Single Source of Truth (Typen, Ordner, Guardrails)
        └── Bestandsaufnahme-Checkliste.md ← Schritt 0: erst prüfen, dann anpassen
```

## So gehst du vor

1. **Schritt 0 — Bestandsaufnahme** (`vault/99_Index/Bestandsaufnahme-Checkliste.md`): erst prüfen, was du schon hast (Ordner, bestehender Sortier-Agent, Plugins). Nichts blind überschreiben.
2. **Verbindung einrichten** (`SETUP-Claude-Obsidian.md`): Local-REST-API-Plugin, API-Key, Claude Desktop + Claude Code anbinden.
3. **Dateien kopieren**: Inhalt von `vault/` in die Wurzel deines Vaults legen (vorhandene Dateien vorher vergleichen).
4. **Testen**: „merk dir das" in Desktop/Code → Notiz in `00_Inbox/` → `/sort-inbox` → einsortiert → mit `git diff` prüfen.

## Wichtig zur Sicherheit

- Die Local REST API gibt **vollen Vault-Zugriff** über einen lokalen Port frei. **API-Key geheim halten**, niemals committen.
- Nur an `127.0.0.1` binden.
- Die echte Kontrolle bleibt bei dir: Vault als Git-Repo führen und Änderungen per `git diff` prüfen.

## Berechtigungen (selbst verwalten)

Das Kit liefert bewusst **keine** `.claude/settings.json` mit — Berechtigungen legst du selbst fest. Empfehlung für den Vault: `Read/Edit/Write/Grep/Glob` und `Bash(mv:*)` erlauben, destruktive Befehle (`rm`, `git reset`, `git checkout`, auto-`commit`/`push`) verbieten. So bleibt das Einsortieren möglich, aber nichts wird gelöscht oder ungefragt committet.
