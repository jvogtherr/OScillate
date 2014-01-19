OScillate
=========
####Vorlesung Regelbasierte Modelle WS 2013/14
Übungsprojekt von Timmy Schüller und Jan Philipp Vogtherr

##Leitfrage 
Wann entscheidet sich ein Student für die 11 und wann für die 21 und welche Faktoren haben darauf Einfluss?

##Modellierung

####Agent: Fahrgast
    Fahrgast -> Student | Sonstige(Hintergrundlast)

    Student[heterogene Eigenschaften](
	    Motivation (Beispiel: 0 <= Motivation <= 1, 0 = faul, 0.5 = egal 1 = eilig)
        Fülle/Sozialfaktor
        Erstie
        Aufenthaltsdauer (zufall, gewichtet)
        Startzeit (zufall, gewichtet)
        Zustand (
            zuhause
            wartet am Neumarkt(queue)
            fährt 21(3 zeitschritte)
            fährt 11(2 zeitschritte)
            studiert
            wartet an der uni(queue)
        )
    )


##Notizen
1 Tag = 180 Ticks => Start eines Tages um 8 Uhr morgens, Ende des Tages um 23 Uhr

Ein Student faehrt um 8 (25%), meistens um 10 (50%), vielleicht auch 12 (25%) los

Ein Student bleibt 4 (25%), meistens aber 6 (50%), vielleicht auch 8 Stunden (25%) in der Uni

=> ein Student ist fr�hestens um 12 zuhause und sp�testens um 20 Uhr

eins = 11 f�hrt 2 ticks = 10 Minuten

zwei = 21 f�hrt 3 ticks = 15 Minuten


###TODO
* Eigenschaften der Studenten f�r Entscheidung mit einbauen (Entscheidungsformel ausdenken)
* Hintergrundlast wieder einf�hren (evtl einfach nur Kapazit�t der Busse reduzieren)
* Kapazit�t der Busse fehlt? Kann ein Bus jemals voll sein?

###Diagramme
* Wie viele Studenten sind in welchem Zustand? (x: Zustände, y: Anzahl Studenten)
* Bus-Beliebtheit (x: Bus-Linie, y: Anzahl Studenten)

* Zustände über Zeit (x: Zeit, y: Anzahl der Studenten, pro Zustand ein Graph)
* Bus-Beliebtheit über Zeit (x: Zeit, y: Anzahl der Studenten, pro Bus-Linie ein Graph)

####Schlange
21 & 11 kommt, 
queue.head entscheidet sich für einen
wenn 21 oder 11 voll, suche anderen aus
evtl: biete möglichkeit stehenzubleiben/hinten anstellen(mit 'ttl') falls erstie & 11 oder bus zu voll (Sozialfaktor vs. Eile)

####Faulheits-Faktor und Eile-Faktor
verpasseter Bus erhöht Motivationsfaktor
