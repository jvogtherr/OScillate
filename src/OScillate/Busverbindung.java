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

public class Busverbindung {

	private List<Student> studentenZuhause;
	private List<Student> studentenNeumarkt;
	private List<Student> studentenUnihaltestelle;
	private Map<Student, Integer> studentenUni; // student, aufenthaltszeit
	
	private Bus eins;
	private Bus zwei;
	
	private Normal n8uhr;
	private Normal n10uhr;
	private Normal n12uhr;
	
	private static int fuelle = 4;
	
	/*static {
		Parameters p = RunEnvironment.getInstance().getParameters();
		fuelle = (Integer)p.getValue("fuelle");
	}*/
	
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
				double h = 0.01 + (double)fuelle / 1000.0;
				double lastfunktion = -(0.000004*Math.pow(tickcount-144.0, 4.0))+(h*Math.pow(tickcount-144.0, 2.0))+10;
				if(lastfunktion < 0)
					lastfunktion = 0;
				return (int)lastfunktion;
			default:
				return 0;
		}

	}
	
	//fuer graphen
	public int getUnstaticFuelle(){
		return Busverbindung.getFuelle(1);
	}
	
	public void neuerStudent(Student student) {
		Log.info("neuer Student zuhause");
		this.studentenZuhause.add(student);
	}
	
	/***** update methoden *****/
	
	@ScheduledMethod(start = 1.5, interval = 1.0)
	public void verbleibendeBusZeitBerechnen() {
		eins.verbleibendeFahrtzeitBerechnen();
		zwei.verbleibendeFahrtzeitBerechnen();
	}
	
	@ScheduledMethod(start = 1.5, interval = 1.0)
	public void verbleibendeUniZeitBerechnen() {
		for (Student student : this.studentenUni.keySet()) {
			Integer neueUniZeit = this.studentenUni.get(student) - 1;
			this.studentenUni.put(student, neueUniZeit);
		}
	}
	
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
	
	/***** neumarkt zu uni *****/
	
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
	
	/***** uni zu neumarkt *****/
	
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
	
	@ScheduledMethod(start = 1.3, interval = 1.0)
	public void aussteigenNeumarktZuhause() {
		List<Student> studenten = new LinkedList<Student>(eins.aussteigen(false));
		studenten.addAll(zwei.aussteigen(false));
		for (Student student : studenten) {
			studentenZuhause.add(student);
			Log.info("Student kommt zuhause an");
		}
	}
	
	/***** dataset methoden *****/
	
	public int getStudentenZuhause() {
		return this.studentenZuhause.size();
	}
	
	public int getStudentenNeumarkt() {
		return this.studentenNeumarkt.size();
	}
	
	public int getStudentenUnihaltestelle() {
		return this.studentenUnihaltestelle.size();
	}
	
	public int getStudentenUni() {
		return this.studentenUni.size();
	}
	
	public int getEinsNeumarktZuUni() {
		return this.eins.getNeumarktZuUni();
	}
	
	public int getZweiNeumarktZuUni() {
		return this.zwei.getNeumarktZuUni();
	}
	
	public int getEinsUniZuNeumarkt() {
		return this.eins.getUniZuNeumarkt();
	}
	
	public int getZweiUniZuNeumarkt() {
		return this.zwei.getUniZuNeumarkt();
	}
	
	public int getNeumarktZuUniGesamt() {
		return this.eins.getNeumarktZuUni() + this.zwei.getNeumarktZuUni();
	}
	
	public int getUniZuNeumarktGesamt() {
		return this.eins.getUniZuNeumarkt() + this.zwei.getUniZuNeumarkt();
	}
	
	public int getBusGesamt() {
		return getNeumarktZuUniGesamt() + getUniZuNeumarktGesamt() + getStudentenNeumarkt() + getStudentenUnihaltestelle();
	}
	
	public int getEinsGesamt() {
		return this.eins.getNeumarktZuUni() + this.eins.getUniZuNeumarkt();
	}
	
	public int getZweiGesamt() {
		return this.zwei.getNeumarktZuUni() + this.zwei.getUniZuNeumarkt();
	}
		
	public int getTrendEins() {
		return Student.getTrendEins();
	}
	
	public int getTrendZwei() {
		return Student.getTrendZwei();
	}
	
}
