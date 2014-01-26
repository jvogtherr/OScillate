package model;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import enums.Buslinie;
import util.Log;

// modelliert alle Busse einer bestimmten Linie
public class Bus {
	
	private final Buslinie linie;	
	private final int fahrtzeit;
	
	// Map mit Studenten und Rest-Fahrtzeit
	private Map<Student, Integer> map;
	
	public Bus(Buslinie linie) {
		this.linie = linie;
		this.fahrtzeit = linie == Buslinie.EINS ? 2 : 3;		
		this.map = new ConcurrentHashMap<Student, Integer>();
	}
		
	public void verbleibendeFahrtzeitBerechnen() {
		for (Student student : this.map.keySet()) {
			Integer neueFahrtzeit = this.map.get(student) - 1;
			this.map.put(student, neueFahrtzeit);
		}
	}
	
	// lässt einen Studenten aussteigen
	public void einsteigen(Student student) {
		Log.info("Student ist in "+linie+" eingestiegen");
		this.map.put(student, fahrtzeit);
	}
	
	public List<Student> aussteigen(boolean uni) {		
		int anzahl_mitfahrende = 0;
		for (Student student : this.map.keySet()) {
			if(this.map.get(student) <= 0 && student.getFahrtZurUni() == uni) 
				anzahl_mitfahrende++;
		}
		
		for (Student student : this.map.keySet()) {
			if(this.map.get(student) <= 0 && student.getFahrtZurUni() == uni) {
				//reflektiere Entscheidung
				if(anzahl_mitfahrende > student.getSozialfaktor()){
					if(this.linie == Buslinie.EINS){
						student.updateBevorzugterBus(-1, 0);
					} else {
						student.updateBevorzugterBus(0, -1);
					}	
				} else {
					if(this.linie == Buslinie.EINS){
						student.updateBevorzugterBus(1, 0);
					} else {
						student.updateBevorzugterBus(0, 1);
					}
				}
			}
		}
		List<Student> studenten = new LinkedList<Student>();
		for (Student student : this.map.keySet()) {
			//steige aus
			if (this.map.get(student) <= 0 && student.getFahrtZurUni() == uni) {
				studenten.add(student);
				this.map.remove(student);
			}
		}
		
		return studenten;
	}
	
	public int getBusAmNeumarkt() {
		int result = 0;
		for (Student student : this.map.keySet()) {
			if (student.getFahrtZurUni() && map.get(student) == fahrtzeit) result++;
		}
		return result;
	}
	
	public int getBusAnUni() {
		int result = 0;
		for (Student student : this.map.keySet()) {
			if (!student.getFahrtZurUni() && map.get(student) == fahrtzeit) result++;
		}
		return result;
	}
	
	public int getNeumarktZuUni() {
		int result = 0;
		for (Student student : this.map.keySet()) {
			if (student.getFahrtZurUni()) result++;
		}
		return result;
	}
	
	public int getUniZuNeumarkt() {
		int result = 0;
		for (Student student : this.map.keySet()) {
			if (!student.getFahrtZurUni()) result++;
		}
		return result;
	}
	
	public Set<Student> getStudenten() {
		return map.keySet();
	}
	
}
