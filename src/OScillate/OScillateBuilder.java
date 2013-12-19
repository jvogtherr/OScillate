package OScillate;

import java.util.LinkedList;
import java.util.List;

import repast.simphony.context.Context;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;

public class OScillateBuilder implements ContextBuilder<Object>{

	@Override
	public Context build(Context<Object> context) {

		//parameter fun
		Parameters p = RunEnvironment.getInstance().getParameters();
		int num_studenten = (Integer)p.getValue("num_Studenten");
		List<Student> studenten = new LinkedList<Student>();
		
		//Sachen erstellen
		for(int i=0; i<num_studenten; i++){
			studenten.add(new Student());
		}
		
		Haltestelle neumarkt = new Haltestelle();
		Haltestelle uni = new Haltestelle();
		
		BusGenerator bus_generator = new BusGenerator();
		
		return null;
	}
	
	
}
