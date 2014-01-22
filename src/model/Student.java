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
	
	
	public Student(int soz_min, int soz_max) {
		if (RandomHelper.nextIntFromTo(0, 1) < 0.5) {
			this.bevorzugterBus = Buslinie.EINS;
			this.punkteEins = 1;
			this.punkteZwei = 0;
		} else {
			this.bevorzugterBus = Buslinie.ZWEI;
			this.punkteZwei = 1;
			this.punkteEins = 0;
		}		
//		if(RandomHelper.nextDoubleFromTo(0, 1) > 0.75)
//			this.fahrtZurUni = true;
//		else
		this.fahrtZurUni = false;
		this.sozialfaktor = RandomHelper.nextIntFromTo(soz_min, soz_max);
		this.losgehzeit = -1;
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
	
	public Buslinie entscheide(boolean einsKommt, boolean zweiKommt, int einsFuelle, int zweiFuelle) {
		// gibt null zurück wenn student nicht fahren will
		Buslinie entscheidung = null;
		
		//Hintergrundlast
		einsFuelle = einsFuelle - Busverbindung.getFuelle(1);
		
		// lieblingsbus prüfen
		if (einsKommt && bevorzugterBus == Buslinie.EINS && sozialfaktor > einsFuelle+25) {
			entscheidung = Buslinie.EINS;
		} else if (zweiKommt && bevorzugterBus == Buslinie.ZWEI && sozialfaktor > zweiFuelle+25) {
			entscheidung = Buslinie.ZWEI;
		} else if (einsKommt && sozialfaktor > einsFuelle) {
			entscheidung = Buslinie.EINS;
		} else if (zweiKommt && sozialfaktor > zweiFuelle) {
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
		}
	}
}
