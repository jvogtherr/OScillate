package OScillate;

import java.util.LinkedList;
import java.util.List;

public class Bus {

	private final int sitzplaetze;
	private List<Person> fahrgaeste = new LinkedList<Person>();
	
	Bus(double fuelle, int sitzplaetze){
		init(fuelle);
		this.sitzplaetze = sitzplaetze;
	}
	
	public void init(double fuelle){
		double grenze = fuelle*(double)sitzplaetze;
		for(int i=0; i<(int)grenze; i++){
			fahrgaeste.add(new Person());
		}
	}
}
