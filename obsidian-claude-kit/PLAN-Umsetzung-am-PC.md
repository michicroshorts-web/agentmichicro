# Plan & Theorie — Obsidian-Claude-Gedächtnis (Umsetzung am PC)

> **Status: Planungsdokument.** Hier steht *was* und *warum*. Die eigentliche
> Umsetzung (Verbindung einrichten, Dateien in den echten Vault kopieren,
> Berechtigungen/Hook bauen, testen) passiert **am PC**, nicht hier in der Cloud.
> Die Kit-Dateien auf diesem Branch sind die fertigen Vorlagen, auf die sich
> dieser Plan bezieht.

---

## 1. Ziel (die Idee in einem Satz)

Claude (Desktop **und** Code) soll Erinnerungen/Erkenntnisse direkt in den
lokalen Obsidian-Vault schreiben können, ohne dass dabei jemals etwas
unkontrolliert verändert oder gelöscht wird — der Mensch behält über `git diff`
die letzte Kontrolle.

## 2. Grundidee / Theorie

Das Problem zerfällt in **zwei klar getrennte Schichten**. Diese Trennung ist
der Kern des ganzen Entwurfs:

1. **Schreibschicht (Capture).** Beide Claudes dürfen *nur* an *einen* Ort
   schreiben: `00_Inbox/`, immer als **neue** Datei, nie eine bestehende ändern.
   Das macht das Schreiben risikofrei — schlimmstenfalls liegt eine überflüssige
   Notiz in der Inbox.
2. **Sortier-Schicht (Organize).** Ein separater Agent (`vault-sorter`) räumt die
   Inbox später auf: klassifiziert den Typ, verlinkt, verschiebt in den richtigen
   Ordner. Er läuft *bewusst getrennt* und nur auf Anstoß (`/sort-inbox`), damit
   die riskantere Operation (bestehende Struktur anfassen) immer überprüfbar ist.

**Warum getrennt?** Capture muss bequem und proaktiv sein (oft, automatisch,
überall). Organize ist riskant (verschiebt/verknüpft, kann falsch liegen). Hätte
eine Schicht beide Aufgaben, müsste man entweder das Schreiben einschränken oder
das Sortieren blind vertrauen. Die Trennung erlaubt „großzügig schreiben, streng
sortieren".

**Single Source of Truth:** `99_Index/Vault-Regeln.md` definiert Typen,
Zielordner, Status, erlaubte Überschriften und Guardrails. Mensch, Schreibschicht
und Sortier-Agent lesen dieselbe Datei — eine Änderung dort wirkt überall.

## 3. Datenmodell (aus Vault-Regeln.md)

- **Status-Lebenszyklus:** `inbox` → `reviewed` → `canon`.
  `canon` = vom Menschen bestätigt, wird **nie** automatisch überschrieben.
- **Typ → Zielordner:** `session`→`01_Sessions/`, `npc`→`02_World/NPCs/`,
  `location`→`02_World/Orte/`, `faction`→`…/Fraktionen/`, `item`→`…/Items/`,
  `rule`→`03_Rules/`, `adventure`→`04_Adventures/`,
  `player-knowledge`→`05_Player-Knowledge/`, `dm-only`→`06_DM-Only/`.
- **`type: memory`** ist nur der Roh-Eingang. Erst der Sortier-Agent setzt den
  echten Typ und verschiebt die Notiz aus der Inbox.
- **Sichtbarkeit:** `private` · `player` · `dm-only` · `spoiler`.
- **Additiv ergänzbar nur unter:** `## Neue Informationen`, `## Quellen`,
  `## Offene Fragen`, `## Verbindungen`.

## 4. Guardrails & Sicherheit (nicht verhandelbar)

- **Nie löschen** — nur verschieben/ergänzen.
- **`canon` ist tabu** — bei Konflikt eine Review-Notiz statt Überschreiben.
- **Nur additiv** unter den erlaubten Überschriften.
- **Verlinken statt duplizieren** (`[[Wikilink]]`).
- **Bei Unsicherheit → `00_Inbox/Review/`** mit kurzer Begründung, nichts einsortieren.
- **Local REST API = voller Vault-Zugriff** über lokalen Port: API-Key geheim
  halten, nie committen, nur an `127.0.0.1` binden.
- **Berechtigungen bewusst eng:** `Read/Edit/Write/Grep/Glob` + `Bash(mv:*)`
  erlauben; `rm`, `git reset`, `git checkout`, auto-`commit`/`push` verbieten.
- Vault als Git-Repo führen; jede automatische Änderung per `git diff` prüfen.

## 5. Umsetzungsplan am PC (Reihenfolge)

> Vorlagen liegen alle in `obsidian-claude-kit/` auf diesem Branch.

- [ ] **Schritt 0 — Bestandsaufnahme** (`vault/99_Index/Bestandsaufnahme-Checkliste.md`).
      Erst prüfen: vorhandene Ordner, bestehender Sortier-Agent, Frontmatter-Felder,
      ob der Vault schon ein Git-Repo ist, ob Local REST API / MCP schon läuft.
      Pro Komponente entscheiden: **behalten · verbessern · ersetzen · neu bauen**.
- [ ] **Taxonomie abgleichen.** Passt `Vault-Regeln.md` zur Realität deines Vaults?
      Wenn nicht → dort anpassen (sie ist die SSOT), *bevor* du etwas kopierst.
- [ ] **Verbindung einrichten** (`SETUP-Claude-Obsidian.md`):
      Local-REST-API-Plugin installieren, API-Key sichern, Endpunkt verifizieren,
      Claude Code (`claude mcp add … --scope user`) und Claude Desktop
      (`mcp-remote`-Bridge) anbinden.
- [ ] **Dateien selektiv kopieren.** Inhalt von `vault/` in die Vault-Wurzel —
      aber nur, was fehlt; Vorhandenes vorher mit `git diff` vergleichen.
- [ ] **Berechtigungen festlegen.** Eigene `.claude/settings.json` im Vault nach
      Empfehlung oben (Kit liefert bewusst keine mit).
- [ ] **Desktop-Projekt anlegen.** „Obsidian-Gedächtnis"-Projekt mit den
      Custom-Instructions aus `SETUP` §3/§4 (Desktop hat keine `CLAUDE.md`).
- [ ] **End-to-End-Test** (`SETUP` §6): Desktop „merk dir …" → Inbox-Notiz prüfen →
      `/remember …` in Code → `/sort-inbox` → `git diff` → übernehmen/verwerfen.

## 6. Offene Entscheidungen / noch zu bauen

- [ ] **Optionaler Stop-Hook (nur Claude Code):** automatische Session-Zusammenfassung
      am Sitzungsende in die Inbox. Braucht ein kleines Skript + Eintrag in
      `.claude/settings.json`. Erst bauen, wenn die Grundverbindung steht
      (`SETUP` §5). → **Am PC zu erstellen.**
- [ ] **Sortier-Agent: behalten/verbessern/ersetzen** — hängt vom Ergebnis der
      Bestandsaufnahme ab (B). Der mitgelieferte `vault-sorter` ist nur ein Vorschlag.
- [ ] **Visibility-Default beim Sortieren:** aktuell „im Zweifel konservativ
      (`dm-only`)" — prüfen, ob das zu deinem Workflow passt.

## 7. Annahmen, die am PC zu verifizieren sind

- Genauer REST-API-Endpunkt/Port (`27124`/`27123`, Pfad `/mcp/`) kann je
  Plugin-Version abweichen → in den Plugin-Einstellungen verifizieren.
- TLS: selbst-signiertes Zertifikat kann bei Claude Code/`mcp-remote` Fehler
  werfen → ggf. HTTP-Port oder Zertifikat als vertrauenswürdig hinterlegen.
- Node.js muss für `npx mcp-remote` (Desktop-Bridge) vorhanden sein.

---

### Bezug zu den Kit-Dateien

| Thema | Datei |
|---|---|
| Überblick & Vorgehen | `README.md` |
| Verbindung Schritt für Schritt | `SETUP-Claude-Obsidian.md` |
| Regeln für Claude Code | `vault/CLAUDE.md` |
| SSOT (Typen/Status/Guardrails) | `vault/99_Index/Vault-Regeln.md` |
| Schritt 0 Prüfliste | `vault/99_Index/Bestandsaufnahme-Checkliste.md` |
| Sortier-Agent (Vorschlag) | `vault/.claude/agents/vault-sorter.md` |
| Commands | `vault/.claude/commands/{remember,sort-inbox}.md` |
| Vorlagen | `vault/90_Templates/{memory,npc,session}.md` |
