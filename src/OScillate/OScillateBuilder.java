package OScillate;

import model.Student;
import repast.simphony.context.Context;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import util.Log;

public class OScillateBuilder implements ContextBuilder<Object> {

	@Override
	public Context build(Context<Object> context) {
		Log.info("building context ...");
		
		Parameters p = RunEnvironment.getInstance().getParameters();
		int num_studenten = (Integer)p.getValue("num_Studenten");
		
		Busverbindung busv = new Busverbindung();				
		for (int i = 0; i < num_studenten; i++) {
			Student student = new Student();
			busv.neuerStudent(student);
		}		
		context.add(busv);
		
		Log.info("context initialized");
		return context;
	}

	
	
}
