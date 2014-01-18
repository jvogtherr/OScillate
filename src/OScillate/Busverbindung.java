package OScillate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import model.Bus;
import model.Student;
import enums.Buslinie;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import util.Log;

public class Busverbindung {

	private List<Student> studentenZuhause;
	private List<Student> studentenNeumarkt;
	private List<Student> studentenUnihaltestelle;
	private Map<Student, Integer> studentenUni; // student, aufenthaltszeit
	
	private Bus eins;
	private Bus zwei;
	
	public Busverbindung() {
		this.studentenZuhause = new LinkedList<Student>();
		this.studentenNeumarkt = new LinkedList<Student>();
		
		this.studentenUni = new ConcurrentHashMap<Student, Integer>();
		this.studentenUnihaltestelle = new LinkedList<Student>();
		
		this.eins = new Bus(Buslinie.EINS);
		this.zwei = new Bus(Buslinie.ZWEI);
	}
	
	public void neuerStudent(Student student) {
		Log.info("neuer Sutdent zuhause");
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
			if (tickcount % 180 == 0) {
				student.setFahrtZurUni(true);
				Log.info("Student fährt wieder los");
			}
			
		}		
	}
	
	/***** neumarkt zu uni *****/
	
	@ScheduledMethod(start = 0.5, interval = 1.0)
	public void zuhauseNachNeumarkt() {		
		for (int i = 0; i < this.studentenZuhause.size(); i++) {
			Student student = this.studentenZuhause.get(i);			
			if (student.getFahrtZurUni() && Math.random() < 0.3) { // TODO: Entscheidung: muss student zur uni?
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
			boolean einsKommt = tickcount % 2 == 0;
			boolean zweiKommt = tickcount % 3 == 0;			
			if (einsKommt || zweiKommt) {
				Buslinie b = student.entscheide(einsKommt, zweiKommt);
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
		List<Student> studenten = new LinkedList<Student>(eins.aussteigen(true));
		studenten.addAll(zwei.aussteigen(true));
		for (Student student : studenten) {
			// Studenten in Uni-Map einordnen mit verbleibender Uni Zeit
			int uniZeit = 50;
			if (Math.random() > 0.5) {
				uniZeit += 20;
			}
			if (Math.random() > 0.5) {
				uniZeit += 30;
			}
			studentenUni.put(student, uniZeit);
			Log.info("Student kommt an Uni an");
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
				Buslinie b = student.entscheide(einsKommt, zweiKommt);
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
		
}
