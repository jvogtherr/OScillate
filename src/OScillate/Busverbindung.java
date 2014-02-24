package OScillate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import cern.jet.random.Normal;
import model.Bus;
import model.Student;
import enums.Buslinie;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.parameter.Parameters;
import repast.simphony.random.RandomHelper;
import util.Log;

/**
 * Modelliert die Umwelt in der Simulation.
 */
public class Busverbindung {
	
	/**
	 * Studenten, die sich zuhause aufhalten
	 */
	private List<Student> studentenZuhause;
	
	/**
	 * Studenten, die sich am Neumarkt auf einen Bus warten
	 */
	private List<Student> studentenNeumarkt;
	
	/**
	 * Studenten, die sich an der Universität auf einen Bus warten
	 */
	private List<Student> studentenUnihaltestelle;
	
	/**
	 * Studenten in der Universität mit ihrer verbleibenden Aufenthaltszeit
	 */
	private Map<Student, Integer> studentenUni;
	
	/**
	 * Buslinie 11
	 */
	private Bus eins;
	
	/**
	 * Buslinie 21
	 */
	private Bus zwei;
	
	/**
	 * Normalverteilung um 8 Uhr in Ticks
	 */
	private Normal n8uhr;
	
	/**
	 * Normalverteilung um 10 Uhr in Ticks
	 */
	private Normal n10uhr;
	
	/**
	 * Normalverteilung um 12 Uhr in Ticks
	 */
	private Normal n12uhr;
	
	/**
	 * Die Fülle repräsentiert den Durchschnittswert anderer Busfahrgäste, die keine Studenten sind
	 */
	private static int fuelle = 4;
	
	/**
	 * Erstellt eine neue Busverbindung für die Simulation.
	 * Zuerst werden die Studenten-Listen initialisiert und die Buslinien erstellt,
	 * danach werden die Normalverteilungen für spätere Verwendung vorbereitet.
	 */
	public Busverbindung() {
		this.studentenZuhause = new LinkedList<Student>();
		this.studentenNeumarkt = new LinkedList<Student>();
		
		this.studentenUni = new ConcurrentHashMap<Student, Integer>();
		this.studentenUnihaltestelle = new LinkedList<Student>();
		
		this.eins = new Bus(Buslinie.EINS);
		this.zwei = new Bus(Buslinie.ZWEI);
		
		this.n8uhr = RandomHelper.createNormal(96, 2);
		this.n10uhr = RandomHelper.createNormal(120, 2);
		this.n12uhr = RandomHelper.createNormal(144, 2);
	}	
	
	/**
	 * Ordnet einen neuen Studenten in die Busverbindung ein.
	 * @param student neuer Student
	 */
	public void neuerStudent(Student student) {
		Log.info("neuer Student zuhause");
		this.studentenZuhause.add(student);
	}
	
	/**
	 * Stößt die Berechnung der verbleibenden Fahrtzeit aller Gäste beider Buslinien an.
	 */
	@ScheduledMethod(start = 1.5, interval = 1.0)
	public void verbleibendeBusZeitBerechnen() {
		eins.verbleibendeFahrtzeitBerechnen();
		zwei.verbleibendeFahrtzeitBerechnen();
	}
	
	/**
	 * Stößt die Berechnung der verbleibenden Aufenthaltszeit aller Studenten in der Uni an.
	 */
	@ScheduledMethod(start = 1.5, interval = 1.0)
	public void verbleibendeUniZeitBerechnen() {
		for (Student student : this.studentenUni.keySet()) {
			Integer neueUniZeit = this.studentenUni.get(student) - 1;
			this.studentenUni.put(student, neueUniZeit);
		}
	}
	
	/**
	 * Bestimmt die Losgehzeit für alle Studenten, die derzeit zuhause sind und bereitet sie für den neuen Tag vor.
	 */
	@ScheduledMethod(start = 1.3, interval = 1.0)
	public void schlafen() {
		int tickcount = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		for (Student student : this.studentenZuhause) {
			// Realistische Variante: Ankunftszeiten an Neumarkt sind Normalverteilt
			// Beispiel 8 Uhr: Schnellster Student ist um 7:35 am Neumarkt, 
			// der Langsamste um 8:25
			if(90 == tickcount % 288) {	
				if(RandomHelper.nextDoubleFromTo(0, 1) > 0.75){
					student.setLosgehzeit(n8uhr.nextInt());
				}
			} else if(114 == tickcount % 288){
				if(RandomHelper.nextDoubleFromTo(0, 1) > 0.33){
					student.setLosgehzeit(n10uhr.nextInt());
				}
			} else if(138 == tickcount % 288){
				student.setLosgehzeit(n12uhr.nextInt());
			}
			if(tickcount % 288 == student.getLosgehzeit()){
				student.setFahrtZurUni(true);
			}			
		}		
	}
	
	/**
	 * Ordnet alle Studenten, die von zuhause zum Neumarkt wollen, dort ein.
	 */	
	@ScheduledMethod(start = 0.5, interval = 1.0)
	public void zuhauseNachNeumarkt() {		
		for (int i = 0; i < this.studentenZuhause.size(); i++) {
			Student student = this.studentenZuhause.get(i);			
			if (student.getFahrtZurUni()) {
				Log.info("neuer Student am Neumarkt");
				student.setFahrtZurUni(true);
				this.studentenNeumarkt.add(student);
				this.studentenZuhause.remove(i);
			}
		}
	}
	
	/**
	 * Alle Studenten, die derzeit am Neumarkt stehen, können hier entscheiden, ob und welchen Bus sie nehmen und steigen ggf. ein.
	 */
	@ScheduledMethod(start = 1.2, interval = 1.0)
	public void einsteigenNeumarkt() {
		int tickcount = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		for (int i = 0; i < this.studentenNeumarkt.size(); i++) {
			Student student = this.studentenNeumarkt.get(i);
			//wenn zu sp�t, gehe wieder nach Hause
			if(tickcount % 288 > 192){		
				student.setFahrtZurUni(false);
				this.studentenNeumarkt.remove(i);
				this.studentenZuhause.add(student);
				continue;
			}
			boolean einsKommt = tickcount % 2 == 0;
			boolean zweiKommt = tickcount % 3 == 0;			
			if (einsKommt || zweiKommt) {
				Buslinie b = student.entscheide(einsKommt, zweiKommt, eins.getBusAmNeumarkt(), zwei.getBusAmNeumarkt());
				if (b == Buslinie.EINS) {
					Log.info("Student am Neumarkt steigt in 11 ein");				
					eins.einsteigen(student);
					this.studentenNeumarkt.remove(i);
				} else if (b == Buslinie.ZWEI) {
					Log.info("Student am Neumarkt steigt in 21 ein");
					zwei.einsteigen(student);
					this.studentenNeumarkt.remove(i);
				}
			}
		}		
	}
	
	/**
	 * Lässt Studenten aus dem Bus an der Uni aussteigen und berechnet die Anzahl der Vorlesungen, die sie hören werden abhängig von der aktuellen Tageszeit.
	 */
	@ScheduledMethod(start = 1.3, interval = 1.0)
	public void aussteigenUni() {
		int tickcount = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount() % 288;
		List<Student> studenten = new LinkedList<Student>(eins.aussteigen(true));
		studenten.addAll(zwei.aussteigen(true));
		for (Student student : studenten) {			
			int uniZeit = 24;
			int maxVorlesungen = 0;
			if (tickcount <= 96) { // 8 uhr
				uniZeit += 96 - tickcount;
				maxVorlesungen = 4;
			} else if (tickcount <= 120) { // 10 uhr
				uniZeit += 120 - tickcount;
				maxVorlesungen = 3;
			} else if (tickcount <= 144) { // 12 uhr
				uniZeit += 144 - tickcount;
				maxVorlesungen = 2;
			} else if (tickcount <= 168) { // 14 uhr
				uniZeit += 168 - tickcount;
				maxVorlesungen = 1;
			} else if (tickcount <= 192) { // später
				uniZeit += 192 - tickcount;
			} else {				
				uniZeit = 0;
			}
			
			
			for (int i = 0; i < maxVorlesungen; i++) {
				double p = 0.5;
				if (RandomHelper.nextDoubleFromTo(0,1) < p) uniZeit += 24;
			}
			
			uniZeit += RandomHelper.nextIntFromTo(0, 5); // etwas mehr unregelmäßigkeit
			
			studentenUni.put(student, uniZeit);
		}		
	}
	
	
	/**
	 * Alle Studenten, deren verbleibende Unizeit abgelaufen ist, werden an der Haltestelle an der Uni eingeordnet.
	 */
	@ScheduledMethod(start = 0.5, interval = 1.0)
	public void uniNachHaltestelle() {		
		for (Student student : this.studentenUni.keySet()) {
			if (this.studentenUni.get(student) <= 0) {
				student.setFahrtZurUni(false);
				this.studentenUnihaltestelle.add(student);
				this.studentenUni.remove(student);
				Log.info("Student geht von der Uni zum Bus");
			}
		}		
	}
	
	/**
	 * Alle Studenten, die derzeit an der Unihaltestelle stehen, können hier entscheiden, ob und welchen Bus sie nehmen und steigen ggf. ein.
	 */
	@ScheduledMethod(start = 1.2, interval = 1.0)
	public void einsteigenUniHaltestelle() {
		int tickcount = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		for (int i = 0; i < this.studentenUnihaltestelle.size(); i++) {
			Student student = this.studentenUnihaltestelle.get(i);
			boolean einsKommt = tickcount % 2 == 0;
			boolean zweiKommt = tickcount % 3 == 0;			
			if (einsKommt || zweiKommt) {
				Buslinie b = student.entscheide(einsKommt, zweiKommt, eins.getBusAnUni(), zwei.getBusAnUni());
				if (b == Buslinie.EINS) {
					Log.info("Student an der Uni steigt in 11 ein");				
					eins.einsteigen(student);
					this.studentenUnihaltestelle.remove(i);
				} else if (b == Buslinie.ZWEI) {
					Log.info("Student an der Uni steigt in 21 ein");
					zwei.einsteigen(student);
					this.studentenUnihaltestelle.remove(i);
				}
			}
		}
	}
	
	/**
	 * Lässt alle Studenten aus dem Bus von der Uni zum Neumarkt aussteigen und nach Hause gehen.
	 */
	@ScheduledMethod(start = 1.3, interval = 1.0)
	public void aussteigenNeumarktZuhause() {
		List<Student> studenten = new LinkedList<Student>(eins.aussteigen(false));
		studenten.addAll(zwei.aussteigen(false));
		for (Student student : studenten) {
			studentenZuhause.add(student);
			Log.info("Student kommt zuhause an");
		}
	}
	
	/**
	 * Liefert die Fülle der Busverbindung 
	 * @param id 0 oder 1
	 * @return Fülle
	 */
	public static int getFuelle(int id){
		int tickcount = (int) RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		tickcount = tickcount % 288;
		Parameters p = RunEnvironment.getInstance().getParameters();
		fuelle = (Integer)p.getValue("fuelle");
		if(fuelle == 0) return 0;
		switch(id){
			case 0:
				return 0;
			case 1:
				double h = 0.01 + (double) fuelle / 1000.0;
				double lastfunktion = -(0.000004*Math.pow(tickcount-144.0, 4.0))+(h*Math.pow(tickcount-144.0, 2.0))+10;
				if(lastfunktion < 0)
					lastfunktion = 0;
				return (int)lastfunktion;
			default:
				return 0;
		}

	}
	
	/**
	 * Gibt den statischen Wert der Fülle aus
	 * @return Fülle
	 */
	public int getUnstaticFuelle(){
		return Busverbindung.getFuelle(1);
	}
	
	/**
	 * Liefert die Anzahl der Studenten, die zuhause sind
	 * @return Anzahl
	 */
	public int getStudentenZuhause() {
		return this.studentenZuhause.size();
	}
	
	/**
	 * Liefert die Anzahl der Studenten, die am Neumarkt sind
	 * @return Anzahl
	 */
	public int getStudentenNeumarkt() {
		return this.studentenNeumarkt.size();
	}
	
	/**
	 * Liefert die Anzahl der Studenten, die an der Unihaltestelle sind
	 * @return Anzahl
	 */
	public int getStudentenUnihaltestelle() {
		return this.studentenUnihaltestelle.size();
	}
	
	/**
	 * Liefert die Anzahl der Studenten, die in der Uni sind
	 * @return Anzahl
	 */
	public int getStudentenUni() {
		return this.studentenUni.size();
	}
	
	/**
	 * Liefert die Anzahl der Studenten, die in der 11 zur Uni sind
	 * @return Anzahl
	 */
	public int getEinsNeumarktZuUni() {
		return this.eins.getNeumarktZuUni();
	}
	
	/**
	 * Liefert die Anzahl der Studenten, die in der 21 zur Uni sind
	 * @return Anzahl
	 */
	public int getZweiNeumarktZuUni() {
		return this.zwei.getNeumarktZuUni();
	}
	
	/**
	 * Liefert die Anzahl der Studenten, die in der 11 zum Neumarkt sind
	 * @return Anzahl
	 */
	public int getEinsUniZuNeumarkt() {
		return this.eins.getUniZuNeumarkt();
	}
	
	/**
	 * Liefert die Anzahl der Studenten, die in der 21 zum Neumarkt sind
	 * @return Anzahl
	 */
	public int getZweiUniZuNeumarkt() {
		return this.zwei.getUniZuNeumarkt();
	}
	
	/**
	 * Liefert die Anzahl der Studenten, die auf dem Weg zur Uni sind
	 * @return Anzahl
	 */
	public int getNeumarktZuUniGesamt() {
		return this.eins.getNeumarktZuUni() + this.zwei.getNeumarktZuUni();
	}
	
	/**
	 * Liefert die Anzahl der Studenten, die auf dem Weg nach Hause sind
	 * @return Anzahl
	 */
	public int getUniZuNeumarktGesamt() {
		return this.eins.getUniZuNeumarkt() + this.zwei.getUniZuNeumarkt();
	}
	
	/**
	 * Liefert die Anzahl der Studenten, die im Bus sitzen
	 * @return Anzahl
	 */
	public int getBusGesamt() {
		return getNeumarktZuUniGesamt() + getUniZuNeumarktGesamt() + getStudentenNeumarkt() + getStudentenUnihaltestelle();
	}
	
	/**
	 * Liefert die Anzahl der Studenten, die in einer 11 sitzen
	 * @return Anzahl
	 */
	public int getEinsGesamt() {
		return this.eins.getNeumarktZuUni() + this.eins.getUniZuNeumarkt();
	}
	
	/**
	 * Liefert die Anzahl der Studenten, die in einer 21 sitzen
	 * @return Anzahl
	 */
	public int getZweiGesamt() {
		return this.zwei.getNeumarktZuUni() + this.zwei.getUniZuNeumarkt();
	}
		
	/**
	 * Liefert die Anzahl der Entscheidungen für die Buslinie 11 über die gesamte Simulation
	 * @return Anzahl
	 */
	public int getTrendEins() {
		return Student.getTrendEins();
	}
	
	/**
	 * Liefert die Anzahl der Entscheidungen für die Buslinie 21 über die gesamte Simulation
	 * @return Anzahl
	 */
	public int getTrendZwei() {
		return Student.getTrendZwei();
	}
	
	/**
	 * Liefert alle Studenten der Simulation als Liste
	 * @return
	 */
	private List<Student> getStudenten() {
		List<Student> studenten = new LinkedList<Student>();
		studenten.addAll(studentenZuhause);
		studenten.addAll(studentenNeumarkt);
		studenten.addAll(studentenUnihaltestelle);
		studenten.addAll(studentenUni.keySet());
		studenten.addAll(eins.getStudenten());
		studenten.addAll(zwei.getStudenten());
		return studenten;
	}
	
	/**
	 * Liefert die Anzahl der Studenten, deren bevorzugter Bus die 11 ist
	 * @return Anzahl
	 */
	public int getStudentenBevorzugterBusEins() {
		int result = 0;
		for (Student student : getStudenten()) {
			if (student.getBevorzugterBus() == Buslinie.EINS) result++;
		}
		return result;
	}
	
	/**
	 * Liefert die Anzahl der Studenten, deren bevorzugter Bus die 21 ist
	 * @return Anzahl
	 */
	public int getStudentenBevorzugterBusZwei() {
		int result = 0;
		for (Student student : getStudenten()) {
			if (student.getBevorzugterBus() == Buslinie.ZWEI) result++;
		}
		return result;
	}
	
}
