package model;

import OScillate.Busverbindung;
import repast.simphony.random.RandomHelper;
import enums.Buslinie;
import util.Log;

/**
 * Modelliert einen Agenten in der Simulation.
 */
public class Student {
	
	/**
	 * die derzeitig bevorzugte Buslinie
	 */
	private Buslinie bevorzugterBus;
	
	/**
	 * true, wenn der Student ein Erstsemesterstudent ist
	 * false sonst
	 */
	private boolean erstie;
	
	/**
	 * die Tageszeit in Ticks, zu der der Student morgens zum Bus geht
	 */
	private int losgehzeit;
	
	/**
	 * true, wenn sich der Student auf dem Weg zur Uni befindet
	 * false, wenn sich der Student auf dem Weg von der Uni nach Hause befindet
	 */
	private boolean fahrtZurUni;
	
	/**
	 * die Bereitschaft des Studenten, in einen überfüllten Bus einzusteigen
	 */
	private int sozialfaktor;
	
	/**
	 * Punkte bei der Entscheidungsfindung für Buslinie 11
	 */
	private int punkteEins;
	
	/**
	 * Punkte bei der Entscheidungsfindung für Buslinie 21
	 */
	private int punkteZwei;
	
	/**
	 * Zähler für die Entscheidungen für Buslinie 11
	 */
	public static int trendEins = 0;
	
	/**
	 * Zähler für die Entscheidungen für Buslinie 21
	 */
	public static int trendZwei = 0;
	
	/**
	 * initialisiert die Trend-Zähler
	 */
	public static void initTrends() {
		trendEins = 0;
		trendZwei = 0;
	}
	
	/**
	 * Erstellt einen Studenten
	 * @param soz_min minimaler Sozialfaktor
	 * @param soz_max maximaler Sozialfaktor
	 * @param erstie true, wenn der Student ein Erstesemesterstudent ist, false sonst
	 */
	public Student(int soz_min, int soz_max, boolean erstie) {
		
		//initialentscheidung zufaellig
		if(erstie){
			this.bevorzugterBus = Buslinie.ZWEI;
			this.punkteZwei = 2;
			this.punkteEins = 0;
		} else if (RandomHelper.nextIntFromTo(0, 1) < 0.5) {
			this.bevorzugterBus = Buslinie.EINS;
			this.punkteEins = 1;
			this.punkteZwei = 0;
		} else {
			this.bevorzugterBus = Buslinie.ZWEI;
			this.punkteZwei = 1;
			this.punkteEins = 0;
		}		
		
		this.fahrtZurUni = false;
		this.sozialfaktor = RandomHelper.nextIntFromTo(soz_min, soz_max);
		this.losgehzeit = -1;
		this.erstie = erstie;
	}	
	
	/**
	 * Gibt die Entscheidung des Studenten zurück, wenn er sich einen Bus aussuchen soll. Dabei geht
	 * der Student so vor, dass er, wenn dieser nicht zu voll ist, seinen bevorzugten Bus nimmt.
	 * Sollte sein bevorzugter Bus nicht da sein, nimmt der Student den anderen Bus, sofern dieser 
	 * nicht zu voll ist. Sollte das allerdings der Fall sein, so nimmt der Student gar keinen Bus
	 * und warten auf die nächsten Busse.  
	 * @param einsKommt true, wenn die 11 an der Haltestelle steht, false sonst
	 * @param zweiKommt true, wenn die 21 an der Haltestelle steht, false sonst
	 * @param einsFuelle Anzahl der Personen im Bus 11
	 * @param zweiFuelle Anzahl der Personen im Bus 21
	 * @return
	 */
	public Buslinie entscheide(boolean einsKommt, boolean zweiKommt, int einsFuelle, int zweiFuelle) {
		// gibt null zurück wenn student nicht fahren will
		Buslinie entscheidung = null;
		
		//Hintergrundlast
		einsFuelle = einsFuelle + Busverbindung.getFuelle(1);
		zweiFuelle = zweiFuelle + Busverbindung.getFuelle(1);
		
		if(erstie){
			if(zweiKommt && sozialfaktor > zweiFuelle)
				return Buslinie.ZWEI;
			else
				return null;
		}
		
		// lieblingsbus prüfen
		if (einsKommt && (bevorzugterBus == Buslinie.EINS) && (sozialfaktor >= einsFuelle)) {
			entscheidung = Buslinie.EINS;
		} else if (zweiKommt && (bevorzugterBus == Buslinie.ZWEI) && (sozialfaktor >= zweiFuelle)) {
			entscheidung = Buslinie.ZWEI;
		} else if (einsKommt && (sozialfaktor >= einsFuelle)) {
			entscheidung = Buslinie.EINS;
		} else if (zweiKommt && (sozialfaktor >= zweiFuelle)) {
			entscheidung = Buslinie.ZWEI;
		}
			
		if (entscheidung == Buslinie.EINS){
			trendEins++;
		} else if (entscheidung == Buslinie.ZWEI){
			trendZwei++;
		}
		
		return entscheidung;
	}
	
	/**
	 * Berechnet den neuen bevorzugten Bus aufgrund der Punkte, die bei der Evaluation der Entscheidung
	 * gesammelt wurden.
	 * @param valueEins Punkte für Bus 11
	 * @param valueZwei Punkte für Bus 21
	 */
	public void updateBevorzugterBus(int valueEins, int valueZwei){		
		punkteEins += valueEins;
		punkteZwei += valueZwei;
		//grenzwert
		int g = 5;
		if(punkteEins > g)
			punkteEins = g;
		if(punkteEins < -g)
			punkteEins = -g;
		if(punkteZwei > g)
			punkteZwei = g;
		if(punkteZwei < -g)
			punkteZwei = -g;
		Buslinie previous = bevorzugterBus;
		if(punkteEins == punkteZwei); //wechsle nur wenn eine Punktzahl groesser!
		else
			bevorzugterBus = punkteEins > punkteZwei ? Buslinie.EINS : Buslinie.ZWEI;
		if(previous != bevorzugterBus){
			Log.info("Student hat bevorzugten Bus gewechselt");
			if(erstie)
				erstie = false;
		}
	}
	
	/**
	 * Gibt den Trend-Zählerstand für Bus 11 zurück.
	 * @return Zählerstand
	 */
	public static int getTrendEins() {
		return trendEins;
	}
	
	/**
	 * Gibt den Trend-Zählerstand für Bus 21 zurück.
	 * @return Zählerstand
	 */
	public static int getTrendZwei() {
		return trendZwei;
	}
	/**
	 * Gibt den bevorzugten Bus zurück.
	 * @return Buslinie
	 */	
	public Buslinie getBevorzugterBus() {
		return this.bevorzugterBus;
	}
	
	/**
	 * Gibt zurück, ob der Student zur Uni fährt oder nicht.
	 * @return true, wenn der Student zur Uni fährt, false sonst
	 */
	public boolean getFahrtZurUni() {
		return this.fahrtZurUni;
	}
	
	/**
	 * Setzt die Fahrt zur Uni.
	 * @param fahrtZurUni true, wenn der Student zur Uni fährt, false sonst
	 */
	public void setFahrtZurUni(boolean fahrtZurUni) {
		this.fahrtZurUni = fahrtZurUni;
	}
	
	/**
	 * Setzt die Losgehzeit eines Studenten
	 * @param zeit Losgehzeit
	 */
	public void setLosgehzeit(int zeit){
		this.losgehzeit = zeit;
	}
	
	/**
	 * Gibt die Losgehzeit eines Studenten zurück
	 * @return losgehzeit
	 */
	public int getLosgehzeit(){
		return this.losgehzeit;
	}
	
	/**
	 * Gibt den Sozialfaktor eines Studente zurück. 
	 * @return Sozialfaktor
	 */
	public int getSozialfaktor(){
		return this.sozialfaktor;
	}
	
	/**
	 * Gibt zurück, ob der Student ein Erstie ist oder nicht.
	 * @return true, wenn der Student ein Erstie ist, false sonst
	 */
	public boolean getErstie(){
		return this.erstie;
	}
	
	/**
	 * Gibt zurück, ob der bevorzugte Bus die Buslinie 11 ist oder nicht.
	 * @return 1, wenn 11, 0 sonst
	 */
	public int getBevBusEins() {
		if(this.bevorzugterBus == Buslinie.EINS)
			return 1;
		else
			return 0;
	}
	
	/**
	 * Gibt zurück, ob der bevorzugte Bus die Buslinie 21 ist oder nicht.
	 * @return 1, wenn 21, 0 sonst
	 */
	public int getBevBusZwei(){
		if(this.bevorzugterBus == Buslinie.ZWEI)
			return 1;
		else
			return 0;
	}
	
}
