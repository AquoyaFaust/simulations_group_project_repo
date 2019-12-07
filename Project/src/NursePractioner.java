import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;

public class NursePractioner extends SimProcess{

	public NursePractioner(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
	}

	@Override
	public void lifeCycle() throws SuspendExecution {
		ClinicModel model = (ClinicModel)getModel();
		while(true) {
			if(model.nurseQueue.isEmpty()) {
				passivate();
			}
		}
	}

}
