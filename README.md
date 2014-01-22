OScillate
=========
####Vorlesung Regelbasierte Modelle WS 2013/14
Übungsprojekt von Timmy Schüller und Jan Philipp Vogtherr

##Leitfrage 
Wann entscheidet sich ein Student für die 11 und wann für die 21 und welche Faktoren haben darauf Einfluss?

##Modellierung

####Agent: Student

    Student[heterogene Eigenschaften](
	bevorzugterBus
        Sozialfaktor
        Erstie
        uniZeit
        losgehzeit
        punkteEins (Meinung zu jeweiligem Bus)
        punkteZwei (maximum der beiden bestimmt bevorzugterBus)
    )


##Notizen
1 Tag = 288 Ticks
25% der Studenten fahren gleichverteilt um 8, 50% um 10 und der Rest um 12 los
Ein Student bleibt 4 (25%), meistens aber 6 (50%), vielleicht auch 8 Stunden (25%) in der Uni
der Plan hierbei ist:
nach obiger Entscheidung schaue, welcher Zeitpunkt (144, 168, 192, 216 oder 240) am nahesten bei 'jetzt + entschiedene Aufenthaltsdauer' liegt und setzte uniZeit auf 'bester passender endzeitpunkt - jetzt'
Problem dabei ist: uniZeit muss folglich auch am Neumarkt und im Bus runtergezählt werden...

eins = 11 f�hrt 2 ticks = 10 Minuten

zwei = 21 f�hrt 3 ticks = 15 Minuten


###TODO
* Graphen fuer bevorzugterBus in Student (habe ich nicht hinbekommen)
* Entscheidung "wie lange bleibe ich" muss umgebaut werden in "wann fahre ich nach hause" mit festen Zeiten ab 12 bis 20 uhr alle 2 Stunden und muss schon stattfinden in 'geheZuNeumarkt'

###Ideen:
* soziales Netzwerk korrelliert mit sozialfaktor

###Diagramme
* Wieviele Fahrgäste hatten die Linien insgesamt? (stetig steigender Zeitgraph)
* wieviele Studenten bevorzugen welchen Bus? (Histogramm?)

