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
		protected ContDistExponential interarrivalTimes;
		double time = model.presentTime().getTimeAsDouble();
		if(time <= 600) {
			interarrivalTimes = model.interarrivalTimes8am;
		} else if(time > 600 && time <= 960) {
			interarrivalTimes = model.interarrivalTimes10am;
		} else {
			interarrivalTimes = model.interarrivalTimes4pm;
		}
		while(model.presentTime().getTimeAsDouble() >= 480 && model.presentTime().getTimeAsDouble() < 1200) {
			
		}
		
	}

}
