package OScillate;

import repast.simphony.context.Context;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;

public class OScillateBuilder implements ContextBuilder<Object>{

	@Override
	public Context build(Context<Object> context) {
		Parameters p = RunEnvironment.getInstance().getParameters();
		int num_studenten = (Integer)p.getValue("num_Studenten");		

		// Haltestellen erstellen
		Haltestelle neumarkt = new Haltestelle();
		Haltestelle uni = new Haltestelle();		
		context.add(neumarkt);
		context.add(uni);		
		
		// Studenten erstellen		
		for (int i = 0; i < num_studenten; i++) {
			context.add(new Student(neumarkt, uni));
		}				
		
		return context;
	}	
	
}
