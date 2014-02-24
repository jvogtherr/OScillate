package model;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import enums.Buslinie;
import util.Log;

/**
 * Modelliert alle Busse einer bestimmen Linie. 
 */
public class Bus {
	
	/**
	 * Nummer der Buslinie
	 */
	private final Buslinie linie;
	
	/**
	 * Fahrtzeit der Buslinie von Haltestelle zu Haltestelle in Ticks (1 = 5 Minuten) 
	 */
	private final int fahrtzeit;
	
	/**
	 * Fahrgäste (Studenten) mit verbleibender Fahrtzeit in Ticks
	 */
	private Map<Student, Integer> map;
	
	/**
	 * Erstellt einen Bus.
	 * Dem Bus wird beim Erstellen eine Linie zugewiesen (11/21).
	 * @param linie Buslinie
	 */
	public Bus(Buslinie linie) {
		this.linie = linie;
		this.fahrtzeit = linie == Buslinie.EINS ? 2 : 3;		
		this.map = new ConcurrentHashMap<Student, Integer>();
	}
	
	/**
	 * Diese Methode wird von der Busverbindung in Intervallen von einem Tick aufgerufen und 
	 * berechnet die verbleibende Fahrtzeit für jeden Fahrgast. 
	 */
	public void verbleibendeFahrtzeitBerechnen() {
		for (Student student : this.map.keySet()) {
			Integer neueFahrtzeit = this.map.get(student) - 1;
			this.map.put(student, neueFahrtzeit);
		}
	}
	
	/**
	 * Ordnet einen neuen Studenten mit der initialen Fahrtzeit in den Bus ein.
	 * @param student einsteigender Student
	 */
	public void einsteigen(Student student) {
		Log.info("Student ist in "+linie+" eingestiegen");
		this.map.put(student, fahrtzeit);
	}
	
	/**
	 * Gibt alle Studenten als Liste zurück, die zu dem Tick, an dem die Methode aufgerufen wurde, 
	 * an der entsprechenden Haltestelle aussteigen. Die Studenten werden außerdem aus der Bus-Map
	 * ausgetragen. 
	 * @param uni True für die Studenten an der Uni, False für die Studenten am Neumarkt
	 * @return Liste der aussteigenden Studenten.
	 */
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
	
	/**
	 * Liefert die Anzahl aller Studenten in der Buslinie, die zuletzt am Neumarkt eingestiegen sind
	 * und noch nicht losgefahren sind.
	 * @return Anzahl
	 */
	public int getBusAmNeumarkt() {
		int result = 0;
		for (Student student : this.map.keySet()) {
			if (student.getFahrtZurUni() && map.get(student) == fahrtzeit) result++;
		}
		return result;
	}
	
	/**
	 * Liefert die Anzahl aller Studenten in der Buslinie, die zuletzt an der Uni eingestiegen sind
	 * und noch nicht losgefahren sind.
	 * @return Anzahl
	 */
	public int getBusAnUni() {
		int result = 0;
		for (Student student : this.map.keySet()) {
			if (!student.getFahrtZurUni() && map.get(student) == fahrtzeit) result++;
		}
		return result;
	}
	
	/**
	 * Liefert die Anzahl aller Studenten in der Buslinie, die in Richtung der Universität fahren. 
	 * @return Anzahl
	 */
	public int getNeumarktZuUni() {
		int result = 0;
		for (Student student : this.map.keySet()) {
			if (student.getFahrtZurUni()) result++;
		}
		return result;
	}
	
	/**
	 * Liefert die Anzahl aller Studenten in der Buslinie, die in Richtung des Neumarkts fahren. 
	 * @return Anzahl
	 */
	public int getUniZuNeumarkt() {
		int result = 0;
		for (Student student : this.map.keySet()) {
			if (!student.getFahrtZurUni()) result++;
		}
		return result;
	}
	
	/**
	 * Liefert alle Studenten in der Buslinie.
	 * @return Studenten
	 */
	public Set<Student> getStudenten() {
		return map.keySet();
	}
	
}
