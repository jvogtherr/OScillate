package OScillate;

import java.util.LinkedList;
import java.util.List;



public class Bus {

	class Linie {
		public static final int ELF = 11;
		public static final int EINUNDZWANZIG = 21;
	}
	
	private final int sitzplaetze;
	private List<Person> fahrgaeste = new LinkedList<Person>();
	//wenn alle Busse losfahren==true, dann gehe weiter in der Zeit
	public boolean losfahren;
	public final int bid;
	public int fahrzeit;
	
	Bus(int bid, double fuelle, int sitzplaetze){
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
		System.out.println("STUDENT IM BUS");
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
			return false;
	}
	
}
