# Setup: Claude ↔ Obsidian verbinden

Ziel: **Claude Desktop** und **Claude Code** dürfen direkt in deinen lokalen Obsidian-Vault schreiben (in `00_Inbox/`). Alles läuft lokal auf deinem Rechner.

> Reihenfolge: erst **Schritt 0** (Bestandsaufnahme, siehe `99_Index/Bestandsaufnahme-Checkliste.md`), dann die Schritte hier.

---

## 1. Obsidian-Plugin „Local REST API" installieren

1. Obsidian → Einstellungen → *Community-Plugins* → Durchsuchen → **„Local REST API"** (von *coddingtonbear*) installieren und aktivieren.
2. In den Plugin-Einstellungen:
   - **API-Key** kopieren (langer Token) → sicher aufbewahren, **nicht** ins Git committen.
   - Endpunkte notieren: HTTPS `https://127.0.0.1:27124` (selbst-signiertes Zertifikat) und optional HTTP `http://127.0.0.1:27123`.
   - Der eingebaute **MCP-Server** ist unter dem Pfad `/mcp/` erreichbar.
3. Sicherheit: Nur an `127.0.0.1` binden (Standard). Der Key ist wie ein Passwort für deinen ganzen Vault.

> Verifiziere den genauen Endpunkt in den Plugin-Einstellungen — Ports/Pfad können je nach Version abweichen. Test im Terminal:
> `curl -k -H "Authorization: Bearer DEIN_KEY" https://127.0.0.1:27124/`

---

## 2. Claude Code anbinden (lokal, gilt für alle Projekte)

Im Terminal auf deinem Rechner (nicht hier in der Cloud):

```bash
claude mcp add --transport http obsidian \
  https://127.0.0.1:27124/mcp/ \
  --scope user \
  --header "Authorization: Bearer DEIN_KEY"
```

- `--scope user` → in allen lokalen Claude-Code-Sessions verfügbar.
- Prüfen: `claude mcp list` → `obsidian` sollte erscheinen und „connected" melden.
- **Zertifikats-Hinweis:** Bei TLS-Fehlern wegen des selbst-signierten Zertifikats entweder den HTTP-Endpunkt nutzen
  (`http://127.0.0.1:27123/mcp/`) oder das Zertifikat des Plugins als vertrauenswürdig hinterlegen.

---

## 3. Claude Desktop anbinden

Bearbeite die Datei `claude_desktop_config.json`:

- **macOS:** `~/Library/Application Support/Claude/claude_desktop_config.json`
- **Windows:** `%APPDATA%\Claude\claude_desktop_config.json`

Trage den Obsidian-MCP-Server ein (Bridge per `mcp-remote`, da Desktop am robustesten so anbindet):

```json
{
  "mcpServers": {
    "obsidian": {
      "command": "npx",
      "args": [
        "-y", "mcp-remote",
        "https://127.0.0.1:27124/mcp/",
        "--header", "Authorization: Bearer DEIN_KEY",
        "--allow-http"
      ]
    }
  }
}
```

Danach Claude Desktop **komplett neu starten**. Im Chat sollte das Obsidian-Werkzeug (Werkzeug-Symbol) auftauchen.

> Voraussetzung: Node.js installiert (für `npx`). `--allow-http` nur nötig, falls du den HTTP-Port 27123 verwendest.

---

## 4. Damit beide Claudes gleich speichern

### Claude Code
Liest automatisch die `CLAUDE.md` aus der Vault-Wurzel (aus diesem Kit). Dort stehen die Gedächtnis-Regeln. Zusätzlich gibt es `/remember` und `/sort-inbox`.

### Claude Desktop
Hat keine `CLAUDE.md`. Lege ein **Projekt** „Obsidian-Gedächtnis" an und füge folgenden Block in die **Custom Instructions** ein:

```text
Du hast über das Obsidian-Werkzeug Schreibzugriff auf meinen Vault.

ERINNERUNGEN SPEICHERN — wann:
- Auf Befehl: wenn ich "merk dir das", "speichere das in Obsidian" o.ä. sage.
- Automatisch: wenn im Gespräch etwas Wichtiges/Bleibendes entsteht
  (Entscheidung, Fakt, Erkenntnis, NPC/Ort/Regel), speichere es proaktiv —
  ohne zu fragen, aber nenne mir kurz, dass du es gespeichert hast.

ERINNERUNGEN SPEICHERN — wie:
- IMMER als neue Datei im Ordner 00_Inbox/ — niemals woanders, niemals bestehende Notizen ändern.
- Dateiname: 00_Inbox/JJJJ-MM-TT-HHMM-kurztitel.md (eindeutig, nichts überschreiben).
- Frontmatter exakt so:
  ---
  type: memory
  status: inbox
  visibility: private
  source: "Gespräch mit Claude (Desktop), <Datum>"
  tags: []
  created: <JJJJ-MM-TT>
  ---
- Danach: kurze H1-Überschrift, dann der Inhalt klar und knapp, mit Kontext (worum ging es).
- Halte jede Notiz auf EIN Thema begrenzt. Mehrere Themen = mehrere Notizen.

Das Einsortieren übernimmt später ein separater Sortier-Agent — du legst nur sauber in der Inbox ab.
```

---

## 5. Automatische Erinnerung am Sitzungsende (optional, nur Claude Code)

Wenn Claude Code am Ende jeder Session automatisch eine Zusammenfassung in die Inbox legen soll, lässt sich ein **Stop-Hook** in `.claude/settings.json` einrichten, der ein kleines Skript aufruft. Das ist optional und sollte erst eingerichtet werden, wenn die Grundverbindung steht. Sag Bescheid, dann bauen wir das Skript dazu.

---

## 6. End-to-End-Test

1. **Desktop:** „Merk dir: NPC Gorath ist Schmied in Hafenstadt." → in Obsidian sollte `00_Inbox/…-gorath-schmied.md` mit korrektem Frontmatter erscheinen.
2. **Code:** `/remember Regelentscheidung: Überraschung gibt nur einen Vorteil, keine extra Runde.` → Notiz in `00_Inbox/` prüfen.
3. `/sort-inbox` → Notizen werden klassifiziert, verlinkt, einsortiert; nichts gelöscht.
4. `git diff` im Vault → übernehmen oder verwerfen.
