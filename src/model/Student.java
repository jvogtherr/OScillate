package model;

import OScillate.Busverbindung;
import repast.simphony.random.RandomHelper;
import enums.Buslinie;
import util.Log;


public class Student {
	
	private Buslinie bevorzugterBus;
	private int losgehzeit;
	
	private boolean fahrtZurUni;
	
	private int sozialfaktor; //maximal ertr�gliche Anzahl von Personen im Bus
	private int punkteEins;
	private int punkteZwei;
	private boolean erstie;
	
	public static int trendEins = 0;
	public static int trendZwei = 0;
	
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
	
	public Buslinie getBevorzugterBus() {
		return this.bevorzugterBus;
	}
	
	public boolean getFahrtZurUni() {
		return this.fahrtZurUni;
	}
	
	public void setFahrtZurUni(boolean fahrtZurUni) {
		this.fahrtZurUni = fahrtZurUni;
	}
	
	public void setLosgehzeit(int zeit){
		this.losgehzeit = zeit;
	}
	
	public int getLosgehzeit(){
		return this.losgehzeit;
	}
	
	public int getSozialfaktor(){
		return this.sozialfaktor;
	}
	
	public boolean getErstie(){
		return this.erstie;
	}
	
//	public void setErstie(boolean erstie){
//		this.erstie = erstie;
//	}
	
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
	
	public void updateBevorzugterBus(int valueEins, int valueZwei){
		//Log.warning("UPDATE: " + punkteEins + " + " + valueEins + "; " + punkteZwei + " + " + valueZwei);
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
	
	public static int getTrendEins() {
		return trendEins;
	}
	
	public static int getTrendZwei() {
		return trendZwei;
	}
	
	public int getBevBusEins() {
		Log.warning(this.bevorzugterBus);
		if(this.bevorzugterBus == Buslinie.EINS)
			return 1;
		else
			return 0;
	}
	
	public int getBevBusZwei(){
		if(this.bevorzugterBus == Buslinie.ZWEI)
			return 1;
		else
			return 0;
	}
	
	public static void initTrends() {
		trendEins = 0;
		trendZwei = 0;
	}
	
}
