# Minimal Phone 📵

Ein minimalistischer Android-**Launcher**, der dein Handy auf bewusst ausgewählte
Apps reduziert. Statt des bunten App-Drawers siehst du nur noch eine schlichte
schwarz-weiße Liste der Apps, die du wirklich brauchst — der Rest verschwindet vom
Startbildschirm.

> **Variante:** „weicher" Launcher. Andere Apps werden **versteckt**, aber nicht
> technisch gesperrt. Wer will, kann sie über Umwege (z. B. Einstellungen) noch
> öffnen. Das hält ohne sensible Berechtigungen vom impulsiven Öffnen ab. Eine
> „harte" Blockierung über einen Accessibility-Service ist als Ausbaustufe
> vorgesehen (siehe *Roadmap*).

## Funktionen

- **Reduzierter Homescreen**: Große Uhr + eine Textliste der erlaubten Apps.
- **Whitelist**: Du legst selbst fest, welche Apps erscheinen.
- **Sinnvolle Voreinstellung**: Beim ersten Start werden die Essentials automatisch
  freigeschaltet — Telefon, Nachrichten, Kamera, Uhr/Wecker, Einstellungen, Kontakte.
- **Dunkles, ablenkungsarmes Design** (schwarzer Hintergrund, keine Icons/Badges).
- **Keine Internet-Berechtigung, kein Tracking, keine Cloud** — alles bleibt lokal.

## Projektstruktur

```
minimal-phone/
├─ app/
│  ├─ build.gradle.kts
│  └─ src/main/
│     ├─ AndroidManifest.xml          # registriert die App als HOME/Launcher
│     ├─ java/com/example/minimalphone/
│     │  ├─ MainActivity.kt           # Einstieg + Navigation Home/Einstellungen
│     │  ├─ LauncherViewModel.kt      # Zustand (Apps + Whitelist)
│     │  ├─ AppRepository.kt          # App-Liste laden, Whitelist speichern
│     │  ├─ model/AppInfo.kt
│     │  └─ ui/
│     │     ├─ HomeScreen.kt          # Uhr + Liste erlaubter Apps
│     │     └─ SettingsScreen.kt      # Apps per Schalter erlauben/sperren
│     └─ res/values/                  # strings.xml, themes.xml
├─ build.gradle.kts / settings.gradle.kts / gradle.properties
└─ gradle/ + gradlew                  # Gradle Wrapper (8.14.3)
```

## Bauen & Installieren

**Voraussetzungen:** Android Studio (aktuell) oder Android SDK + JDK 17.

1. In Android Studio: *File → Open* → den Ordner `minimal-phone` wählen.
   Gradle synct automatisch und lädt die Abhängigkeiten.
2. Gerät per USB anschließen (USB-Debugging an) oder einen Emulator starten.
3. **Run ▶**.

Per Kommandozeile (mit gesetztem `ANDROID_HOME` / `local.properties`):

```bash
cd minimal-phone
./gradlew assembleDebug          # APK bauen
./gradlew installDebug           # auf verbundenes Gerät installieren
```

> Hinweis: Im Build-Container hier wurde nur das Projekt-Gerüst inkl. Gradle-Wrapper
> erzeugt. Der eigentliche Compile/APK-Build braucht das Android SDK und einen
> Internetzugang für die Abhängigkeiten — das macht Android Studio beim ersten Sync.

## Als Standard-Launcher festlegen

Nach der Installation: **Home-Button** drücken → Android fragt, welcher Launcher
verwendet werden soll → *Minimal Phone* → *Immer*.

Zurück zum normalen Launcher: *Einstellungen → Apps → Standard-Apps → Start-App*.

## Wie es funktioniert (Kurz)

- Die `MainActivity` ist im Manifest mit `category.HOME` registriert → Android bietet
  sie als Startbildschirm an.
- `AppRepository.loadAllApps()` fragt per `PackageManager` alle Apps mit
  `LAUNCHER`-Eintrag ab (nötiges `<queries>`-Element im Manifest für Android 11+).
- Die Whitelist liegt in `SharedPreferences`. Beim ersten Start werden die Essentials
  über Standard-Intents aufgelöst (Dialer, SMS, Kamera, Wecker, Einstellungen, Kontakte).
- Der Homescreen zeigt nur Apps, deren Paketname in der Whitelist steht.

## Roadmap (Ausbaustufen)

- [ ] **Harte Blockierung** via `AccessibilityService`: erkennt die Vordergrund-App und
      schickt gesperrte Apps zurück zum Homescreen.
- [ ] **Selbstschutz**: PIN/Wartezeit, damit man die App nicht im Affekt deaktiviert.
- [ ] **Zeitfenster / Fokus-Zeiten** (z. B. nachts nur Telefon).
- [ ] **Graustufen-Hinweis** & schnelle Wähltasten für Favoriten-Kontakte.
- [ ] App-Icons optional zuschaltbar.

## Lizenz

Privates Beispielprojekt — frei zur eigenen Verwendung und Anpassung.
