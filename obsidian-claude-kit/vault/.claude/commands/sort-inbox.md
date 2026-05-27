---
description: Sortiert die Notizen aus 00_Inbox/ in die Wissensbasis ein (klassifizieren, verlinken, verschieben) — mit Review bei Unsicherheit.
---

Starte den `vault-sorter`-Subagenten, um `00_Inbox/` aufzuräumen.

Vorgehen:
1. Lies `99_Index/Vault-Regeln.md`.
2. Liste die Notizen in `00_Inbox/` (ohne `00_Inbox/Review/`).
3. Übergib die Aufgabe an den `vault-sorter` (siehe `.claude/agents/vault-sorter.md`) und halte dich strikt an dessen Guardrails: nichts löschen, `canon` nie überschreiben, nur additiv unter erlaubten Überschriften, bei Unsicherheit nach `00_Inbox/Review/`.
4. Gib am Ende die Zusammenfassungstabelle aus (Notiz → Typ → Ziel → Aktion).

Committe nichts — die Freigabe erfolgt durch den Nutzer per `git diff`.

$ARGUMENTS
