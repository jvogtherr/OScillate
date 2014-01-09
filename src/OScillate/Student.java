package OScillate;

import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.random.RandomHelper;

public class Student extends Person {
	
	class Zustand {
		public static final int ZUHAUSE = 0;
		public static final int WARTET = 1;
		public static final int FAEHRT_BUS = 2;
		public static final int STUDIERT = 3;
	}		
		
	private int ziel;
	private int zustand;	
	private int startzeit;
	private int endzeit;
	
	private boolean zurueckgestellt;
	
	private int lieblingsbus;
	
	private Haltestelle neumarkt;
	private Haltestelle uni;	
		
	public Student(Haltestelle neumarkt, Haltestelle uni) {
		lieblingsbus = Bus.Linie.EINUNDZWANZIG;
		zustand = Zustand.ZUHAUSE;
		ziel = Zustand.STUDIERT;
		//TODO: Zeitauswahl besser eingrenzen
		startzeit = RandomHelper.nextIntFromTo(0, 112);
		endzeit = RandomHelper.nextIntFromTo(startzeit, 116);
		zurueckgestellt = false;
		this.neumarkt = neumarkt;
		this.uni = uni;
	}
	
	@ScheduledMethod(start=0.9, interval=1.0)
	public void entscheide(){
		//TODO: aktuelle Zeit wird benötigt!
		//evtl. bekommt man von repast den tick count, dann ginge
		//Tick % 60, bzw. mehr als 60, wenn man zwischen den tagen
		//ein wenig puffer möchte
		//dann:
		//wenn zeit >= endzeit && zustand==studiert: An Uni anstellen && Zustand wechseln
		//ansonsten wenn zeit >= startzeit && zustand==schlaeft: An Neumarkt anstellen && Zustand wechseln
		//ansonsten do nothing
		
		//außerdem: Student braucht referenzen auf Haltestellen oder BusGenerator
		if(RandomHelper.nextDoubleFromTo(0, 1) >= 0.5){
			this.lieblingsbus = Bus.Linie.ELF;
		} else{
			this.lieblingsbus = Bus.Linie.EINUNDZWANZIG;
		}
		
		if (zustand == Zustand.ZUHAUSE) {
			neumarkt.addStudent(this);
		} else if (zustand == Zustand.STUDIERT) {
			uni.addStudent(this);
		}
		
	}
	
	public void nextZustand(){
		if (this.zustand == Zustand.FAEHRT_BUS){
			this.zustand = this.ziel;
			this.ziel = (this.ziel == Zustand.STUDIERT ? Zustand.ZUHAUSE : Zustand.STUDIERT);
		} else {
			// TODO: Exception?
		}		
	}

	public boolean getZurueckgestellt() {
		return zurueckgestellt;
	}
	
	public void setZurueckgestellt(boolean s){
		zurueckgestellt = s;
	}

	public int getLieblingsbus() {
		return lieblingsbus;
	}
	
	public void setZustand(int s){
		this.zustand = s;
	}
}
