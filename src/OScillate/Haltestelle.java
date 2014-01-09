package OScillate;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import repast.simphony.engine.schedule.ScheduledMethod;

public class Haltestelle {

	private Queue<Student> queue = new LinkedList<Student>();
	private List<Bus> fahrendeBusse = new LinkedList<Bus>();
	private Bus elf;
	private Bus einundzwanzig;
	
	
	Haltestelle(){
		elf = null;
		einundzwanzig = null;
	}
	
	public void addStudent(Student s){
		queue.add(s);
	}
	
	public void einsteigen(){
		while(!queue.isEmpty() || !(queue.peek().getZurueckgestellt())){
			Student s = queue.poll();
			if(s.getLieblingsbus() == Bus.elf){
				if(this.elf != null && !this.elf.blockiert()){
					this.elf.addStudent(s);
					s.setZustand(Student.faehrt_bus);
				} else {
					s.setZurueckgestellt(true);
					queue.add(s);
				}
			} else{
				//redundant, geht evtl besser
				if( this.einundzwanzig != null && !this.einundzwanzig.blockiert()){
					this.einundzwanzig.addStudent(s);
					s.setZustand(Student.faehrt_bus);
				} else {
					s.setZurueckgestellt(true);
					queue.add(s);
				}
			}
		}
		//Schlange leer oder Busse voll
		//=>Busse fahren weg
		if(this.elf != null){
			//behalte referenz, damit Bus nicht garbageCollected wird
			this.elf.fahrzeit=1;
			fahrendeBusse.add(this.elf);
			this.elf = null;
		}
		//gleiche Redundanzquelle wie oben
		if(this.einundzwanzig != null){
			this.einundzwanzig.fahrzeit=2;
			fahrendeBusse.add(this.einundzwanzig);
			this.einundzwanzig = null;
		}
	}
	
	public void addBus(Bus b){
		if(b.bid == Bus.elf){
			this.elf = b;
		} else {
			this.einundzwanzig = b;
		}
	}
	
	//lasse Busse weiterfahren
	@ScheduledMethod(start=1.1, interval=1)
	public void uptadeBusse(){
		for(Bus b : fahrendeBusse){
			//wenn Bus angekommen
			if(b.fahrzeit==0){
				b.aussteigen();
				//erst hier darf Garbage Collector zuschlagen
				fahrendeBusse.remove(b);
				continue;
			}
			b.fahrzeit--;
		}
	}
	
	public void deleteBus(Bus b){
		//könnte den falschen Bus löschen, untested
		fahrendeBusse.remove(b);
	}
}
