Zuerst folgendes installieren:
[ ] JDK 8
[ ] jetty-distribution-9.3.5.v20151012
[ ] apache-maven-3.3.3
[ ] IntelliJ IDEA Community Edition 15.0

So kriegst Du es zum laufen:
[ ] git master clonen
[ ] "mvn clean install" im Folder mit der pom.xml ausf�hren
[ ] Maven l�dt die notwendigen dependencies aus den repositories und baut das WAR File
[ ] Das WAR file findest Du im target folder
[ ] WAR File in den jetty folder "webapps" kopieren
[ ] Jetty mittels "start.jar" im jetty root starten und warten bis das WAR vollst�ndig deployt ist

Jetzt kannst du die App testen:
[ ] oeffne http://localhost:8080/taskit/

Um einen neuen Task zu erzeugen folgende URL im Browser aufrufen: http://localhost:8080/taskit/rest/jsonServices/print/HelloWorld
Erzeigter (dummy) Task wird als JSON im Browser angezeigt.