package OScillate;

import repast.simphony.engine.environment.RunEnvironment;
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
		double tickcount = RunEnvironment.getInstance().getCurrentSchedule().getTickCount();
		
		//TODO: prüfen ob modulo auf double zu problemen führt
		if(zustand == Zustand.ZUHAUSE){
			if(startzeit>=(tickcount%120)){
				neumarkt.addStudent(this);
				zustand = Zustand.WARTET;
			}
		}
		
		if(zustand == Zustand.STUDIERT){
			if(endzeit>=(tickcount%120)){
				uni.addStudent(this);
				zustand = Zustand.WARTET;
			}
		}
		
		if(zustand == Zustand.WARTET){
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
	
	public int getZustand() {
		System.out.println("Zustand: "+this.zustand);
		return zustand;
	}
	
	public void setZustand(int s){
		this.zustand = s;
	}
	
	//für grafiken
	//entsprechende Datasets sind angelegt,
	//können aber aus irgendeinem grund nicht für ein
	//histogramchart verwendet werden...
	//TODO: fix.
	public int getPointForElf(){
		return (this.lieblingsbus == Bus.Linie.ELF) ? 1 : 0;
	}
	public int getPointForEinundzwanzig(){
		return (this.lieblingsbus == Bus.Linie.EINUNDZWANZIG) ? 1 : 0;
	}
	
	public boolean zustandIstZuhause(){
		return (this.zustand == Student.Zustand.ZUHAUSE);
	}
	
	public boolean zustandIstWartet(){
		return (this.zustand == Student.Zustand.WARTET);
	}
	
	public boolean zustandIstFaehrtBus(){
		return (this.zustand == Student.Zustand.FAEHRT_BUS);
	}
	
	public boolean zustandIstStudiert(){
		return (this.zustand == Student.Zustand.STUDIERT);
	}

}
