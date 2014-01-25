OScillate
=========
####Vorlesung Regelbasierte Modelle WS 2013/14
Übungsprojekt von Timmy Schüller und Jan Philipp Vogtherr

##Leitfrage 
Wann entscheidet sich ein Student für die 11 und wann für die 21 und welche Faktoren haben darauf Einfluss?

##Modellierung

###Agent: Student
Eigenschaften:
* bevorzugter Bus (welchen Bus mag ich liebe?)
* Startzeit (wann muss ich zur Uni fahren?)
* Sozialfaktor (vermeide ich zu volle Busse?)
* Erstie (kenne ich die 11?)

###Umwelt: Busverbindung
* modelliert die Aufenthaltsorte der Studenten (Zuhause, Neumarkt, Uni)
* Manager für den Busverkehr
* updated regelmäßig den Status der Simulation (Scheduled Methods)
* wird als einzige Klasse dem Context hinzugefügt, liefert Daten für die Data Sets

###Bus
* eine Instanz modelliert eine Buslinie mit allen Bussen, die darauf fahren
* merkt sich die Fahrtrichtung der Studenten und die verbleibende Fahrzeit

###Allgemeines
* 1 Tick = 5 Minuten, 288 Ticks = 1 Tag


##Diagramme
* (Diagramm)
	* (was ist zu erkennen)
* Studenten Zuhause/Uni
	* Stoßzeiten
	* Hin- und Rückfahrtzeitpunkte
	* Aufenthaltsdauer
	* Vorlesungsende
* Studenten Uni Hinweg
	* Normalverteilung um die Stoßzeiten 
		* wenige kommen zu früh an
		* viele kommen zum richtigen Zeitpunkt an
		* wenige kommen zu spät an
* Busse Hinweg
	* Abfahrtszeiten der Buslinien
	* Bevorzugung der 11
		* dennoch sind die absoluten Fahrgastzahlen sehr ähnlich
* Studenten Uni Rueckweg
	* die ersten fahren nach Hause, wenn die letzten kommen (=> Aufenthaltsdauer)
	* Peaks an den Haltestellen zu Vorlesungsende (Bus-Kapazitäten erschöpft?)
* Busse Rueckweg
	* Abfahrtszeiten der Buslinien
	* sehr starke Bevorzugung der 11
		* absolute Fahrgastzahlen werden unterschiedlicher, 21 verliert Beliebtheit
* Busse Gesamt
* Busse Trends
	* 11 ist deutlich beliebter
	* zwei Graden mit unterschiedlicher Steigung, 21 steigt sehr langsam
	* keine Dynamik
		* entweder zu feste Regeln (11 wird künstlich bevorzugt)
		* oder realistisch aufgrund der kürzeren Fahrzeit und dem daraus folgenden höheren Durchsatz


##Notizen
1 Tag = 288 Ticks
25% der Studenten fahren gleichverteilt um 8, 50% um 10 und der Rest um 12 los
Ein Student bleibt 4 (25%), meistens aber 6 (50%), vielleicht auch 8 Stunden (25%) in der Uni
der Plan hierbei ist:
nach obiger Entscheidung schaue, welcher Zeitpunkt (144, 168, 192, 216 oder 240) am nahesten bei 'jetzt + entschiedene Aufenthaltsdauer' liegt und setzte uniZeit auf 'bester passender endzeitpunkt - jetzt'
Problem dabei ist: uniZeit muss folglich auch am Neumarkt und im Bus runtergezählt werden...

eins = 11 faehrt 2 ticks = 10 Minuten
zwei = 21 faehrt 3 ticks = 15 Minuten
