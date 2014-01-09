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
	
	
	public Haltestelle(){
		elf = null;
		einundzwanzig = null;
	}
	
	public void addStudent(Student s){
		queue.add(s);
	}
	
	@ScheduledMethod(start=1.0, interval=2.0)
	public void generateElf() {
		this.elf = new Bus(Bus.Linie.ELF, 0, 100);
	}
	
	@ScheduledMethod(start=1.0, interval=3.0)
	public void generateEinundzwanzig() {
		this.einundzwanzig = new Bus(Bus.Linie.EINUNDZWANZIG, 0, 100);
	}
	
	@ScheduledMethod(start=1.1, interval=1.0)
	public void einsteigen(){
		while(!queue.isEmpty() || !(queue.peek().getZurueckgestellt())){
			Student s = queue.poll();
			if(s.getLieblingsbus() == Bus.Linie.ELF){
				if(this.elf != null && !this.elf.blockiert()){
					this.elf.addStudent(s);
					s.setZustand(Student.Zustand.FAEHRT_BUS);
				} else {
					s.setZurueckgestellt(true);
					queue.add(s);
				}
			} else{
				//redundant, geht evtl besser
				if( this.einundzwanzig != null && !this.einundzwanzig.blockiert()){
					this.einundzwanzig.addStudent(s);
					s.setZustand(Student.Zustand.FAEHRT_BUS);
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
	
	//lasse Busse weiterfahren
	@ScheduledMethod(start=1.2, interval=1.0)
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
