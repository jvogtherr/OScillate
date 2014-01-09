package OScillate;

import java.util.LinkedList;
import java.util.List;

import repast.simphony.engine.schedule.ScheduledMethod;

public class Bus {

	public static final boolean elf = false;
	public static final boolean einundzwanzig = true;
	
	private final int sitzplaetze;
	private List<Person> fahrgaeste = new LinkedList<Person>();
	//wenn alle Busse losfahren==true, dann gehe weiter in der Zeit
	public boolean losfahren;
	public final boolean bid;
	public int fahrzeit;
	
	Bus(boolean bid, double fuelle, int sitzplaetze){
		this.bid = bid;
		init(fuelle);
		this.sitzplaetze = sitzplaetze;
		this.losfahren=false;
	}
	
	public void init(double fuelle){
		double grenze = fuelle*(double)sitzplaetze;
		for(int i=0; i<(int)grenze; i++){
			fahrgaeste.add(new Person());
		}
	}
	
	public boolean addStudent(Student s){
		if(fahrgaeste.size() >= sitzplaetze){
			fahrgaeste.add(s);
			return true;
		} else
			return false;
	}
	
	public void aussteigen(){
		for(Person p : fahrgaeste){
			if(p instanceof Student){
				((Student)p).nextZustand();
			}
		}
	}
	
	//darf jemand einsteigen?
	public boolean blockiert(){
		if(this.fahrgaeste.size()>=this.sitzplaetze)
			return true;
		else
			return !losfahren;
	}
}
