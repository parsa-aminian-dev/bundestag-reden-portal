# 🏛️ Bundestag Reden-Portal - Übung 3

**Programmierpraktikum (PPR) - WiSe 2025/2026**  
**Goethe Universität Frankfurt am Main**  
**Texttechnology Lab**

---

Eine webbasierte Anwendung zur Visualisierung, Analyse und Durchsuchung von Bundestagsreden der 20. Wahlperiode mit **Javalin**, **FreeMarker** und **jQuery**.

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/)
[![Javalin](https://img.shields.io/badge/Javalin-6.7.0-blue.svg)](https://javalin.io/)
[![FreeMarker](https://img.shields.io/badge/FreeMarker-2.3.34-green.svg)](https://freemarker.apache.org/)
[![License](https://img.shields.io/badge/License-Academic-yellow.svg)]()

---

## 📋 Inhaltsverzeichnis

1. [Projektübersicht](#-projektübersicht)
2. [Features](#-features)
3. [Technologie-Stack](#-technologie-stack)
4. [Systemanforderungen](#-systemanforderungen)
5. [Installation & Setup](#-installation--setup)
6. [Verwendung](#-verwendung)
7. [Projektstruktur](#-projektstruktur)
10. [Frontend-Technologien](#-frontend-technologien)
11. [Statistiken & Analytics](#-statistiken--analytics)
12. [Screenshots](#-screenshots)
13. [Konfiguration](#-konfiguration)
14. [Entwicklung](#-entwicklung)




---

## 🎯 Projektübersicht

Das **Bundestag Reden-Portal** ist eine umfassende Webanwendung zur Aufbereitung und Visualisierung parlamentarischer Daten. Die Applikation baut auf **Übung 2** auf und erweitert die Backend-Infrastruktur um eine moderne, benutzerfreundliche Weboberfläche.

### Motivation

Die Transparenz parlamentarischer Prozesse ist ein Grundpfeiler der Demokratie. Diese Anwendung macht Bundestagsreden zugänglich, durchsuchbar und analysierbar, um:
- **Bürgern** einen besseren Einblick in politische Debatten zu geben
- **Forschern** die Analyse parlamentarischer Diskurse zu erleichtern
- **Entwicklern** Best Practices für Web-Development mit Java zu demonstrieren

### Projektziele

1. ✅ Vollständige Webvisualisierung von Bundestagsprotokollen
2. ✅ Leistungsfähige Such- und Filterfunktionen
3. ✅ Umfassende statistische Auswertungen
4. ✅ RESTful API für programmatischen Zugriff
5. ✅ Responsive und barrierefreies Design

---

## ✨ Features

### 🔍 Kernfunktionalität

- **Live-Suche**: Echtzeit-Suche nach Abgeordneten mit Auto-Suggest
- **Detailansichten**:
    - Abgeordneten-Profile mit allen Reden
    - Volltext-Ansicht einzelner Reden mit Metadaten
    - Kommentare zu Reden
- **Filter & Sortierung**:
    - Nach Fraktion filtern
    - Nach Name (A-Z) sortieren
    - Nach Anzahl der Reden sortieren
- **Statistik-Dashboard**:
    - Gesamtübersicht (Abgeordnete, Reden, Fraktionen)
    - Top-Redner Rankings
    - Fraktions-Vergleiche
    - Durchschnittliche Redelängen

### 🌐 Web-Technologien

- **Server-Side Rendering** mit FreeMarker
- **AJAX-basierte Suche** mit jQuery
- **Responsive Design** für mobile Geräte
- **RESTful API** für Datenabfragen

### 📊 Datenverarbeitung

- **XML-Parsing** von Plenarprotokollen
- **In-Memory Repository** für schnellen Zugriff
- **Factory Pattern** für zentrale Datenverwaltung
- **Service Layer** für Business Logic

---

## 🛠️ Technologie-Stack

### Backend

| Technologie | Version | Verwendung |
|------------|---------|------------|
| **Java** | 21 | Programmiersprache |
| **Maven** | 3.8+ | Build-Management & Dependency Resolution |
| **Javalin** | 6.7.0 | Web-Framework (Routing, HTTP-Handler) |
| **FreeMarker** | 2.3.34 | Template Engine für HTML-Generierung |
| **JSON** | 20250517 | Datenformat für REST-API |
| **SLF4J** | 2.0.16 | Logging-Framework |

### Frontend

| Technologie | Version | Verwendung |
|------------|---------|------------|
| **HTML5** | - | Markup-Struktur |
| **CSS3** | - | Styling & Layout |
| **JavaScript ES6** | - | Client-seitige Logik |
| **jQuery** | 3.6.0 | AJAX, DOM-Manipulation |

### Architektur-Patterns

- **MVC (Model-View-Controller)**: Trennung von Datenmodell, Präsentation und Steuerung
- **Repository Pattern**: Abstraktion der Datenzugriffs-Schicht
- **Factory Pattern**: Zentrale Objekterstellung und -verwaltung
- **Service Layer Pattern**: Kapselung der Business Logic






---

## 💻 Verwendung

### Startseite

Die Startseite bietet:
- **Suchleiste**: Tippen Sie einen Namen ein (z.B. "Scholz")
- **Filter**: Wählen Sie eine Fraktion aus dem Dropdown
- **Sortierung**: Nach Name (A-Z) oder Anzahl der Reden
- **Abgeordneten-Karten**: Klicken Sie auf "Details ansehen"

### Abgeordneten-Profil

Für jeden Abgeordneten sehen Sie:
- Persönliche Informationen (Name, Fraktion)
- Anzahl der gehaltenen Reden
- Durchschnittliche Redelänge
- Liste aller Reden mit Vorschau

### Rede-Detailansicht

Jede Rede zeigt:
- Vollständiger Redetext
- Datum und Uhrzeit
- Zugehöriger Tagesordnungspunkt
- Alle Kommentare (Zwischenrufe)
- Länge in Zeichen

### Statistik-Dashboard

Das Dashboard präsentiert:
- **Übersichtskarten**: Gesamtzahlen auf einen Blick
- **Top 5 Redner**: Ranking nach Redeanzahl
- **Fraktions-Vergleich**: Mitglieder, Reden, Durchschnitte
- **Visualisierungen**: Tabellen und Statistiken

---

## 📁 Projektstruktur
```
Uebung3/
│
├── pom.xml                                      # Maven Build-Konfiguration
├── README.md                                    # Diese Datei
├── .gitignore                                   # Git Ignore-Regeln
│
├── diagrams/                                    # UML-Diagramme (Aufgabe 1)
│   ├── use-case-diagramm.png
│   ├── klassendiagramm.png
│   └── package-diagramm.png
│
├── screenshots/                                 # Screenshots für Dokumentation
│   ├── startseite.png
│   ├── abgeordneten-profil.png
│   ├── rede-detail.png
│   └── statistiken.png
│
└── src/
    ├── main/
    │   ├── java/de/uni/ppr/uebung3/
    │   │   │
    │   │   ├── app/
    │   │   │   └── Main.java                   # Einstiegspunkt der Anwendung
    │   │   │
    │   │   ├── rest/
    │   │   │   ├── RESTHandler.java            # REST-Routen & HTML-Rendering
    │   │   │   └── RestConfig.java             # Konfigurationsklasse (extends Properties)
    │   │   │
    │   │   ├── service/
    │   │   │   ├── AbgeordnetenService.java    # Business Logic für Abgeordnete
    │   │   │   ├── RedenService.java           # Business Logic für Reden
    │   │   │   └── StatistikService.java       # Statistische Auswertungen
    │   │   │
    │   │   ├── factory/
    │   │   │   ├── RedenPortalFactory.java     # Factory Pattern (aus Übung 2)
    │   │   │   ├── Repository.java             # Repository Interface
    │   │   │   ├── InMemoryRepository.java     # In-Memory Implementierung
    │   │   │   ├── AbgeordneterRepository.java
    │   │   │   ├── FraktionRepository.java
    │   │   │   ├── SitzungRepository.java
    │   │   │   ├── RedeRepository.java
    │   │   │   ├── TOPRepository.java
    │   │   │   └── KommentarRepository.java
    │   │   │
    │   │   ├── model/
    │   │   │   ├── Entity.java                 # Abstrakte Basisklasse
    │   │   │   ├── Abgeordneter.java           # Modell für Abgeordnete
    │   │   │   ├── Fraktion.java               # Modell für Fraktionen
    │   │   │   ├── Sitzung.java                # Modell für Sitzungen
    │   │   │   ├── Tagesordnungspunkt.java     # Modell für TOPs
    │   │   │   ├── Rede.java                   # Modell für Reden
    │   │   │   └── Kommentar.java              # Modell für Kommentare
    │   │   │
    │   │   └── loader/
    │   │       └── XmlProtokollLoader.java     # XML-Parser (aus Übung 2)
    │   │
    │   └── resources/
    │       │
    │       ├── config.properties               # Server-Konfiguration
    │       │
    │       ├── templates/                      # FreeMarker-Templates
    │       │   ├── index.ftl                   # Startseite
    │       │   ├── abgeordneter.ftl            # Abgeordneten-Profil
    │       │   ├── rede.ftl                    # Rede-Detailansicht
    │       │   └── statistiken.ftl             # Statistik-Dashboard
    │       │
    │       └── static/                         # Statische Ressourcen
    │           ├── css/
    │           │   └── style.css               # Haupt-Stylesheet
    │           ├── js/
    │           │   └── main.js                 # JavaScript (jQuery)
    │           └── images/
    │               └── logo.png
    │
    └── test/
        └── java/de/uni/ppr/uebung3/
            ├── service/
            ...


```


### jQuery-Integration

#### Live-Suche
```javascript
$('#searchInput').on('input', function() {
    const query = $(this).val();
    
    $.ajax({
        url: '/api/abgeordnete/search',
        data: { q: query },
        success: function(data) {
            displayResults(data);
        }
    });
});
```

#### Dynamisches Laden
```javascript
$.get('/api/statistiken', function(data) {
    updateStatistics(data);
});
```

### CSS-Features

- **CSS Grid** für Layout
- **Flexbox** für flexible Komponenten
- **CSS Variables** für Theming
- **Media Queries** für Responsive Design
- **CSS Animations** für Übergänge

---

## 📊 Statistiken & Analytics

### Verfügbare Metriken

1. **Grundzahlen**
    - Anzahl Abgeordnete
    - Anzahl Reden
    - Anzahl Fraktionen

2. **Durchschnittswerte**
    - Durchschnittliche Redelänge (gesamt)
    - Durchschnittliche Redelänge pro Abgeordnetem
    - Durchschnittliche Redelänge pro Fraktion
    - Durchschnittliche Reden pro Abgeordnetem

3. **Rankings**
    - Top-Redner nach Anzahl
    - Längste Reden
    - Aktivste Fraktionen

4. **Fraktions-Analysen**
    - Mitgliederzahl
    - Gesamt-Reden
    - Durchschnitt pro Mitglied

---

## 📸 Screenshots

### Startseite
![Startseite](screenshots/startseite.png)
*Übersicht aller Abgeordneten mit Such- und Filterfunktion*

### Abgeordneten-Profil
![Abgeordneten-Profil](screenshots/abgeordneten-profil.png)
*Detailansicht mit allen Reden eines Abgeordneten*

### Rede-Detailansicht
![Rede-Detail](screenshots/rede-detail.png)
![Rede-Detail](screenshots/rede-detail2.png)
*Volltext einer Rede mit Metadaten und Kommentaren*

### Statistik-Dashboard
![Statistiken](screenshots/statistiken.png)
*Umfassende Statistiken und Rankings*

---

## ⚙️ Konfiguration

### config.properties
```properties
# Server-Konfiguration
server.port=7070
server.host=localhost

# Template-Engine
template.path=/templates
template.cache=false

# Static Files
static.path=/static


app.name=Bundestag Reden-Portal
app.version=1.0


database.path=data/neo4j
```

### Umgebungsvariablen
```bash
export SERVER_PORT=8080
export TEMPLATE_CACHE=true
```

### Custom Port starten
```bash
java -jar target/Uebung3-1.0.jar -Dserver.port=8080
```

---

## 👨‍💻 Entwicklung

### Projekt kompilieren
```bash
mvn clean compile
```

### Tests ausführen
```bash
mvn test
```

### JavaDoc generieren
```bash
mvn javadoc:javadoc
```

Ausgabe: `target/site/apidocs/index.html`

### Code-Style

Das Projekt folgt den [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html) Konventionen.

### Git Workflow
```bash
# Feature-Branch erstellen
git checkout -b feature/neue-funktion

# Änderungen committen
git add .
git commit -m "feat: Neue Suchfunktion implementiert"

# Pushen
git push origin feature/neue-funktion

# Merge Request erstellen
```

---






