import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.dist.ContDistExponential;
import desmoj.core.simulator.*;

public class ArrivalGenerator extends SimProcess{
	
	public ArrivalGenerator(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
	}

	@Override
	public void lifeCycle() throws SuspendExecution {
		ClinicModel model = (ClinicModel)getModel();
		while(model.presentTime().getTimeAsDouble() < 720) {
			ContDistExponential interarrivalTimes = pickInterarrivalTime(model);
			
			
		}
		
	}
	
	private ContDistExponential pickInterarrivalTime(ClinicModel model) {
		double time = model.presentTime().getTimeAsDouble();
		if(time <= 120) {
			return model.interarrivalTimes8am;
		} else if(time > 240 && time <= 480) {
			return model.interarrivalTimes10am;
		} else {
			return model.interarrivalTimes4pm;
		}
	}

}
