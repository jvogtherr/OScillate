package OScillate;

import repast.simphony.random.RandomHelper;

public class Student extends Person{

	public static final int schlaeft = 0;
	public static final int wartet = 1;
	public static final int faehrt_bus = 2;
	public static final int studiert = 3;
	public static final boolean elf = false;
	public static final boolean einundzwanzig = true;
	
	private int ziel;
	private int zustand;
	//Gemessen in 10min Zeitschritten (vorläufig) dh 8:00 == 0 und 18:00 == 60
	private int startzeit, endzeit;
	private boolean zurueckgestellt;
	private boolean lieblingsbus;
	
	
	Student(){
		lieblingsbus = this.einundzwanzig;
		zustand = schlaeft;
		ziel = studiert;
		//TODO: Zeitauswahl besser eingrenzen
		startzeit = RandomHelper.nextIntFromTo(0, 56);
		endzeit = RandomHelper.nextIntFromTo(startzeit, 58);
		zurueckgestellt = false;
	}
	
	public void entscheide(){
		//TODO: aktuelle Zeit wird benötigt!
		//evtl. bekommt man von repast den tick count, dann ginge
		//Tick % 60, bzw. mehr als 60, wenn man zwischen den tagen
		//ein wenig puffer möchte
		if(RandomHelper.nextDoubleFromTo(0, 1) >= 0.5){
			this.lieblingsbus = this.elf;
		} else{
			this.lieblingsbus = this.einundzwanzig;
		}
		
	}
	
	public void nextZustand(){
		if(this.zustand != this.faehrt_bus){
			//Exception? Fehler? nicht zulässig!
			return;
		}
		this.zustand = this.ziel;
		//wechsle Ziel zwischen studieren/schlafen
		this.ziel = ((this.ziel == this.studiert) ? this.schlaeft : this.studiert);
	}

	public boolean getZurueckgestellt() {
		return this.zurueckgestellt;
	}

	public boolean getLieblingsbus() {
		return this.lieblingsbus;
	}
}
