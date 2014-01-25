OScillate
=========
####Vorlesung Regelbasierte Modelle WS 2013/14
Übungsprojekt von Timmy Schüller und Jan Philipp Vogtherr

##Leitfrage 
Wann entscheidet sich ein Student für die 11 und wann für die 21 und welche Faktoren haben darauf Einfluss?

---

##Modellierung

####Allgemeines
* 1 Tick = 5 Minuten, 288 Ticks = 1 Tag
* UOS hat insgesamt ca. 12000 Studierende => 4000-8000 Studenten für die Simulation (Batch-Runs?)

####Agent: Student
* Eigenschaften:
	* bevorzugter Bus (welchen Bus mag ich lieber?)
	* Startzeit (wann muss ich zur Uni fahren?)
	* Sozialfaktor (vermeide ich zu volle Busse?)
	* Erstie (kenne ich die 11?)

####Umwelt: Busverbindung
* modelliert die Aufenthaltsorte der Studenten (Zuhause, Neumarkt, Uni)
* Managerklasse für den Busverkehr
* updated regelmäßig den Status der Simulation (Scheduled Methods)
* wird als einzige Klasse dem Context hinzugefügt, liefert Daten für die Data Sets

####Bus
* eine Instanz modelliert eine Buslinie mit allen Bussen, die darauf fahren
* merkt sich die Fahrtrichtung der Studenten und die verbleibende Fahrzeit



##Diagramme
* __(Diagramm)__
	* (was ist zu erkennen)
* __Studenten Zuhause/Uni__
	* Stoßzeiten
	* Hin- und Rückfahrtzeitpunkte
	* Aufenthaltsdauer
	* Vorlesungsende
* __Studenten Uni Hinweg__
	* Normalverteilung um die Stoßzeiten 
		* wenige kommen zu früh an
		* viele kommen zum richtigen Zeitpunkt an
		* wenige kommen zu spät an
* __Busse Hinweg__
	* Abfahrtszeiten der Buslinien
	* Bevorzugung der 11
		* dennoch sind die absoluten Fahrgastzahlen sehr ähnlich
* __Studenten Uni Rueckweg__
	* die ersten fahren nach Hause, wenn die letzten kommen (=> Aufenthaltsdauer)
	* Peaks an den Haltestellen zu Vorlesungsende (Bus-Kapazitäten erschöpft?)
* __Busse Rueckweg__
	* Abfahrtszeiten der Buslinien
	* sehr starke Bevorzugung der 11
		* absolute Fahrgastzahlen werden unterschiedlicher, 21 verliert Beliebtheit
* __Busse Gesamt__
	* Mischung aus Busse Hinweg + Busse Rueckweg
* __Busse Trends__
	* 11 ist deutlich beliebter
	* zwei Graden mit unterschiedlicher Steigung, 21 steigt sehr langsam
	* keine Dynamik
		* entweder zu feste Regeln (11 wird künstlich bevorzugt)
		* oder realistisch aufgrund der kürzeren Fahrzeit und dem daraus folgenden höheren Durchsatz



##Erste Ergebnisse
* Simulation funktioniert gut, ist jedoch sehr statisch
	* ändert die Dynamik über mehrere Tage nicht
	* nur kleine Unterschiede durch die Zufallsentscheidungen
* starker Abstraktionsgrad
	* nimmt Genauigkeit
	* bietet interessante Möglichkeiten zur Auswertung	



##Aussicht für die Abgabe
* ...