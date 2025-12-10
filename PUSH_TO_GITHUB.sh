#!/bin/bash

# ====================================
# GitHub Push Script
# Repository: Bundestagsverwaltungssystem
# ====================================

echo "🚀 Bereite Push zu GitHub vor..."
echo ""

cd "/Users/parsaaminian/Desktop/Projekte /Bundestag-Verwaltungssystem/uebung3"

# Git Status prüfen
echo "📊 Git Status:"
git status
echo ""

# Remote prüfen
echo "🔗 Remote Repository:"
git remote -v
echo ""

# Falls Remote noch nicht gesetzt ist
if ! git remote | grep -q "origin"; then
    echo "⚙️  Füge Remote hinzu..."
    git remote add origin https://github.com/parsa-aminian-dev/Bundestagsverwaltungssystem.git
fi

# Alle Änderungen hinzufügen
echo "➕ Füge Dateien hinzu..."
git add .

# Commit erstellen
echo "💾 Erstelle Commit..."
git commit -m "docs: Improve documentation and prepare for GitHub

- Enhanced .gitignore with Neo4j, IDE files, and logs
- Restructured README.md for GitHub with clear installation guide
- Added MIT License with academic notice
- Cleaned up code (removed TODOs, empty lines)
- Added JavaDoc comments to key classes
- Created GitHub preparation documentation"

# Push zu GitHub
echo ""
echo "🚢 Pushe zu GitHub..."
git push origin main

echo ""
echo "✅ Erfolgreich zu GitHub gepusht!"
echo "🌐 Repository: https://github.com/parsa-aminian-dev/Bundestagsverwaltungssystem"
