package model;

import OScillate.Busverbindung;
import repast.simphony.random.RandomHelper;
import enums.Buslinie;

public class Student {
	
	private Buslinie bevorzugterBus;
	private int losgehzeit;
	
	private boolean fahrtZurUni;
	
	//private double motivation; //nahe bei 0 => 21, nahe bei 1 => 11
	private int sozialfaktor; //maximal erträgliche Anzahl von Personen im Bus
	/*
	 * führt zu lustigem effekt:
	 * Die ersten Busse sind immer voll weil:
	 * zuerst sind die Busse leer, dh bis zur 50. Person steigt jeder ein.
	 * Danach gibt es immer Personen die noch reinwollen, wenn viele Leute am Neumarkt stehen
	 * => Leute mit niedrigem Sozialfaktor sind in vollem Bus wenn viel los ist (Realitätsnah!)
	 */
	
	public Student(int soz_min, int soz_max) {
		if (RandomHelper.nextIntFromTo(0, 1) < 0.5) {
			this.bevorzugterBus = Buslinie.EINS;
		} else {
			this.bevorzugterBus = Buslinie.ZWEI;
		}		
//		if(RandomHelper.nextDoubleFromTo(0, 1) > 0.75)
//			this.fahrtZurUni = true;
//		else
			this.fahrtZurUni = false;
		//this.motivation = RandomHelper.nextDoubleFromTo(0, 1);
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
	
	public Buslinie entscheide(boolean einsKommt, boolean zweiKommt, int einsFuelle, int zweiFuelle) {
		// gibt null zurÃ¼ck wenn student nicht fahren will
		Buslinie entscheidung = null;
		
		//Hintergrundlast
		einsFuelle = einsFuelle - Busverbindung.getFuelle(1);
		
		// lieblingsbus prÃ¼fen
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
	
}
