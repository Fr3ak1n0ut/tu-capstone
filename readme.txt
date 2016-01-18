Readme - Capstone Projekt

Felix Wohnhaas

Zusatzfunktionen:

- Farbwechsel der W�nde des Labyrinths

- Hintergrundmusik

- Pause-Menu, das sich zentriert �ber das aktuelle Level legt und das aktuelle Spiel im Hintergrund anzeigt

- Level-Generator, der es dem Spieler erlaubt, einen eigenen Level ganz nach den individuellen W�nschen zu erstellen und zu spielen

- Richtungsanzeige des Spielers

- Mehrere Speicher / Ladeslots

- Collectibles

- Score

Orientierung im Projekt:

- Die Grundfunktionen befinden sich im Package "Core". Dabei befindet sich die grundlegende Spielmechanik in der Klasse Core, die Klasse mit Main-Methode ist Game.
- Alle Menus befinden sich im Package "Menus". Jedes Menu erbt von der Klasse Menu und nutzt/�berschreibt dessen vorgegebene Methoden sinnvoll.
- Alle Symbole, die im Spiel vorkommen, befinden sich im Package "Symbols". Jedes Symbol erbt von der Klasse Symbol und nutzt/�berschreibt dessen vorgegebene Methoden sinnvoll.

Zus�tzliche Dateien

- Zus�tzlich zu den .java/.class files wird eine "level.properties" Datei mit der entsprechenden Levelinformation ben�tigt.
- Um die Hintergrundmusik zu aktivieren, muss die Datei "music.wav" im Projektverzeichnis befinden.
- Die Datei "parameters.txt" ist wichtig f�r den Levelgenerator und muss sich im Projektverzeichnis befinden - andernfalls wird diese beim Generieren erstellt.
- Die externe Bibliothek "lanterna-2.1.9.jar" wird f�r das Projekt ben�tigt.
- Um die Speicherslots zu testen, sollten die Dateien "save1.properties", "save2.properties", save3.properties" und "save4.properties" im Projektverzeichnis vorhanden sein - andernfalls werden diese beim Speichern erstellt.