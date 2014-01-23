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
		int soz_min = (Integer)p.getValue("soz_min");
		int soz_max = (Integer)p.getValue("soz_max");
		int ersties = (Integer)p.getValue("ersties");
		//idee f�r erstie parameter: parameter einstellbar machen und im konstruktor entsprechend mitgeben
		
		Busverbindung busv = new Busverbindung();				
		for (int i = 0; i < num_studenten; i++) {
			Student student;
			if(ersties-- > 0){
				student = new Student(soz_min, soz_max, true);
			}else{
				student = new Student(soz_min, soz_max, false);
			}
				
			busv.neuerStudent(student);
		}		
		
		context.add(busv);
		
		Log.info("context initialized");
		return context;
	}

	
	
}
