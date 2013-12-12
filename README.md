OScillate
=========
Vorlesung Regelbasierte Modelle WS 2013/14
Timmy Schüller, Jan Philipp Vogtherr

##Leitfrage 
Wann entscheidet sich ein Student für die 11 und wann für die 21 und welche Faktoren haben darauf Einfluss?

##Modellierung

###Agent: Fahrgast
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
21 & 11 kommt, 
queue.head entscheidet sich für einen
wenn 21 oder 11 voll, suche anderen aus
evtl: biete möglichkeit stehenzubleiben/hinten anstellen(mit 'ttl') falls erstie & 11 oder bus zu voll (Sozialfaktor vs. Eile)
