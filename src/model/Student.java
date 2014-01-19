package model;

import repast.simphony.random.RandomHelper;
import enums.Buslinie;

public class Student {
	
	private Buslinie bevorzugterBus;
	
	private boolean fahrtZurUni;
	
	public Student() {
		if (RandomHelper.nextIntFromTo(0, 1) < 0.5) {
			this.bevorzugterBus = Buslinie.EINS;
		} else {
			this.bevorzugterBus = Buslinie.ZWEI;
		}		
		this.fahrtZurUni = true;
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
	
	public Buslinie entscheide(boolean einsKommt, boolean zweiKommt) {
		// gibt null zurück wenn student nicht fahren will
		Buslinie entscheidung = null;
		
		// lieblingsbus prüfen
		if (einsKommt && bevorzugterBus == Buslinie.EINS) {
			entscheidung = Buslinie.EINS;
		} else if (zweiKommt && bevorzugterBus == Buslinie.ZWEI) {
			entscheidung = Buslinie.ZWEI;
		} else if (einsKommt) {
			entscheidung = Buslinie.EINS;
		} else if (zweiKommt) {
			entscheidung = Buslinie.ZWEI;
		}
		
		return entscheidung;
	}
	
}
