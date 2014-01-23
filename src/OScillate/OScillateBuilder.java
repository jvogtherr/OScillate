package OScillate;

import model.Student;
import repast.simphony.context.Context;
import repast.simphony.context.space.graph.NetworkBuilder;
import repast.simphony.context.space.graph.WattsBetaSmallWorldGenerator;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.parameter.Parameters;
import repast.simphony.space.graph.Network;
import util.Log;

public class OScillateBuilder implements ContextBuilder<Object> {

	@Override
	public Context<Object> build(Context<Object> context) {
		Log.info("building context ...");
		
		Parameters p = RunEnvironment.getInstance().getParameters();
		int num_studenten = (Integer)p.getValue("num_Studenten");
		int soz_min = (Integer)p.getValue("soz_min");
		int soz_max = (Integer)p.getValue("soz_max");
		int ersties = (Integer)p.getValue("ersties");
		
		
		Busverbindung busv = new Busverbindung();		
		
		//network
		NetworkBuilder<Object> netB = new NetworkBuilder<Object>("StudIP", context, true);
		Network<Object> network = netB.buildNetwork();
		
		for (int i = 0; i < num_studenten; i++) {
			Student student;
			if(ersties-- > 0){
				student = new Student(soz_min, soz_max, true);
			}else{
				student = new Student(soz_min, soz_max, false);
			}
			
			//for network:
			context.add(student); 
			
			busv.neuerStudent(student);
		}		
		
		//sw-network
		WattsBetaSmallWorldGenerator<Object> swnetgen = new WattsBetaSmallWorldGenerator<Object>(0.3, 4, false);
		swnetgen.createNetwork(network);
		//wegen small world werden aber immer Bekannte im Bus sein -> potentiell nutzlos
		
		context.add(busv);
		
		Log.info("context initialized");
		return context;
	}

	
	
}
