package OScillate;

import java.util.LinkedList;
import java.util.List;

public class Bus {

	private final int sitzplaetze;
	private List<Person> fahrgaeste = new LinkedList<Person>();
	//wenn alle Busse losfahren==true, dann gehe weiter in der Zeit
	private boolean losfahren;
	
	Bus(double fuelle, int sitzplaetze){
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
}
