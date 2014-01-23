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
	
	public Student(int soz_min, int soz_max, boolean erstie) {
		
		//initialentscheidung zufaellig
		if(erstie){
			this.bevorzugterBus = Buslinie.ZWEI;
			this.punkteZwei = 10;
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
		einsFuelle = einsFuelle + Busverbindung.getFuelle(0);
		
		if(erstie){
			if(zweiKommt && sozialfaktor > zweiFuelle)
				return Buslinie.ZWEI;
			else
				return null;
		}
		
		// lieblingsbus prüfen
		if (einsKommt && (bevorzugterBus == Buslinie.EINS) && (sozialfaktor > einsFuelle)) {
			entscheidung = Buslinie.EINS;
		} else if (zweiKommt && (bevorzugterBus == Buslinie.ZWEI) && (sozialfaktor > zweiFuelle)) {
			entscheidung = Buslinie.ZWEI;
		} else if (einsKommt && (sozialfaktor > einsFuelle)) {
			entscheidung = Buslinie.EINS;
		} else if (zweiKommt && (sozialfaktor > zweiFuelle)) {
			entscheidung = Buslinie.ZWEI;
		}

		return entscheidung;
	}
	
	public void updateBevorzugterBus(int valueEins, int valueZwei){
		//Log.info("UPDATE: " + punkteEins + " + " + valueEins + "; " + punkteZwei + " + " + valueZwei);
		punkteEins += valueEins;
		punkteZwei += valueZwei;
		Buslinie previous = bevorzugterBus;
		if(punkteEins == punkteZwei); //wechsle nur wenn eine Punktzahl groesser!
		else
			bevorzugterBus = punkteEins > punkteZwei ? Buslinie.EINS : Buslinie.ZWEI;
		if(previous != bevorzugterBus){
			Log.error("Student hat bevorzugten Bus gewechselt");
			if(erstie)
				erstie = false;
		}
	}
}
