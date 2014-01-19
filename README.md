OScillate
=========
####Vorlesung Regelbasierte Modelle WS 2013/14
Ãœbungsprojekt von Timmy SchÃ¼ller und Jan Philipp Vogtherr

##Leitfrage 
Wann entscheidet sich ein Student fÃ¼r die 11 und wann fÃ¼r die 21 und welche Faktoren haben darauf Einfluss?

##Modellierung

####Agent: Fahrgast
    Fahrgast -> Student | Sonstige(Hintergrundlast)

    Student[heterogene Eigenschaften](
	    Motivation (Beispiel: 0 <= Motivation <= 1, 0 = faul, 0.5 = egal 1 = eilig)
        FÃ¼lle/Sozialfaktor
        Erstie
        Aufenthaltsdauer (zufall, gewichtet)
        Startzeit (zufall, gewichtet)
        Zustand (
            zuhause
            wartet am Neumarkt(queue)
            fÃ¤hrt 21(3 zeitschritte)
            fÃ¤hrt 11(2 zeitschritte)
            studiert
            wartet an der uni(queue)
        )
    )


##Notizen
1 Tag = 180 Ticks => Start eines Tages um 8 Uhr morgens, Ende des Tages um 23 Uhr
Ein Student faehrt um 8 (25%), meistens um 10 (50%), vielleicht auch 12 (25%) los
Ein Student bleibt 4 (25%), meistens aber 6 (50%), vielleicht auch 8 Stunden (25%) in der Uni
=> ein Student ist frühestens um 12 zuhause und spätestens um 20 Uhr
eins = 11 fährt 2 ticks = 10 Minuten
zwei = 21 fährt 3 ticks = 15 Minuten

Vorschlag: zufällige Startzeitgenerierung nicht wieder einbauen und bei Zufallswert bleiben, 
	Schranke für Zufallswert evtl. von Eigenschaften des Studenten abhängig machen (oder als Simulationsparameter setzen)

###TODO
* Eigenschaften der Studenten für Entscheidung mit einbauen (Entscheidungsformel ausdenken)
* Hintergrundlast wieder einführen (evtl einfach nur Kapazität der Busse reduzieren)
* Kapazität der Busse fehlt? Kann ein Bus jemals voll sein?

###Diagramme
* Wie viele Studenten sind in welchem Zustand? (x: ZustÃ¤nde, y: Anzahl Studenten)
* Bus-Beliebtheit (x: Bus-Linie, y: Anzahl Studenten)

* ZustÃ¤nde Ã¼ber Zeit (x: Zeit, y: Anzahl der Studenten, pro Zustand ein Graph)
* Bus-Beliebtheit Ã¼ber Zeit (x: Zeit, y: Anzahl der Studenten, pro Bus-Linie ein Graph)

####Schlange
21 & 11 kommt, 
queue.head entscheidet sich fÃ¼r einen
wenn 21 oder 11 voll, suche anderen aus
evtl: biete mÃ¶glichkeit stehenzubleiben/hinten anstellen(mit 'ttl') falls erstie & 11 oder bus zu voll (Sozialfaktor vs. Eile)

####Faulheits-Faktor und Eile-Faktor
verpasseter Bus erhÃ¶ht Motivationsfaktor
