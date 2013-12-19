package OScillate;

import java.util.LinkedList;
import java.util.Queue;

public class Haltestelle {

	private Queue<Student> queue = new LinkedList<Student>();
	
	Haltestelle(){
		
	}
	
	public void addStudent(Student s){
		queue.add(s);
	}
	
	public void einsteigen(){
		while(!queue.isEmpty() || !(queue.peek().getZurueckgestellt())){
			Student s = queue.poll();
			if(s.getLieblingsbus() == Student.elf){
				//TODO: Haltestelle braucht referenzen auf an ihr generierte Busse
				//if 11 vorhanden
				// if 11.einsteigen()
				// else 11.losfahren; queue.add(s);
			} else{
				
			}
		}
	}
}
