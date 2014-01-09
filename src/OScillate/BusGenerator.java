package OScillate;

import repast.simphony.context.Context;
import repast.simphony.engine.schedule.ScheduledMethod;
import repast.simphony.util.ContextUtils;

/*
 * generiert busse,
 * verwaltet Bus.aussteigen() und Haltestelle.einsteigen() weil ja nicht in jedem Tick in jedem Bus
 * Studenten aussteigen bzw einsteigen sollen
 */
public class BusGenerator {

	private final Haltestelle NEUMARKT;
	private final Haltestelle UNI;
	
	public BusGenerator(){
		NEUMARKT = new Haltestelle();
		UNI = new Haltestelle();
	}
	
	//generiere jeden tick 11
	@ScheduledMethod(start= 1.0, interval= 1.0)
	public void generateElf(){
		NEUMARKT.addBus(new Bus(Bus.elf, 0, 100));
		UNI.addBus(new Bus(Bus.elf, 0, 100));
	}
	
	//generiere alle 2 ticks 21
	@ScheduledMethod(start= 1.0, interval= 2.0)
	public void generateEinundzwanzig(){
		NEUMARKT.addBus(new Bus(Bus.einundzwanzig, 0, 100));
		UNI.addBus(new Bus(Bus.einundzwanzig, 0, 100));
	}
}
