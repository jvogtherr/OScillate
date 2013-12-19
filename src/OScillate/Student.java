package OScillate;

import repast.simphony.random.RandomHelper;

public class Student extends Person{

	public static final int schlaeft = 0;
	public static final int wartet = 1;
	public static final int faehrt_bus = 2;
	public static final int studiert = 3;
	
	private int zustand;
	//Gemessen in 10min Zeitschritten (vorläufig) dh 8:00 == 0 und 18:00 == 60
	private int startzeit, endzeit;
	
	
	Student(){
		zustand = schlaeft;
		//TODO: Zeitauswahl besser eingrenzen
		startzeit = RandomHelper.nextIntFromTo(0, 56);
		endzeit = RandomHelper.nextIntFromTo(startzeit, 58);
	}
	
	
}
