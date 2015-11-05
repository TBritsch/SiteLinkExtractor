# SiteLinkExtractor
Extrahiert Seiten-Links aus Wikipedia auf verschiedenen Wegen.


#Benötigte Fremdbibliotheken beziehungsweise Dateien.
-Für den eigentlichen SiteLinkExtractor:

	Die Dateien:
	core/src/main/scala/*

	aus dem extraction-framework von DBpedia (https://github.com/dbpedia/extraction-framework). Diese sind aus Gründen der Übersichtlichkeit auch Teil dieses Repositories. Die Quelltexte stehen unter der Lizenz GNU General Public License, version 2.

Weiterhin wird Apache Commons Compress™ benötigt.
Quelle: https://commons.apache.org/proper/commons-compress/


-Für die Tools:
Für das Tool PGrep wird die JAVA ASCII Table benötigt. Diese ist unter https://code.google.com/p/java-ascii-table/ zu finden.

#Installation:
Um die Programme selbst zu erstellen, reicht es aus, sätmliche benötigten Dateien im Classpath bereit zu halten und eine jar-Datei zu erstellen. Die benötigten Dateien sind im die jeweiligen Unterverzeichnisse eingeteilt. So benötigt man, um den SiteLinkExtractor zu erstellen sämtliche Dateien aus dem Ordner "extactor". Für die einzelnen Tools reichen die Dateien aus dem Unterverzeichnis tools/x/ für das jewilige Tool x aus.