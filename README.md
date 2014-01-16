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
	    Faulheit
        Eile (zufällig)
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
Faulheit und Eile zusammenfassen? => Beispiel: 0 <= Motivation <= 1, 0 = faul, 0.5 = egal 1 = eilig
verpasseter Bus erhöht Motivationsfaktor
