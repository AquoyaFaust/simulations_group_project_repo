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
		model.totalCost.update(1200);
		while(true) {
			if(model.nurseQueue.isEmpty()) {
				model.idleNurseQueue.insert(this);
				passivate();
			}
			Patient patient = model.nurseQueue.removeFirst();
			double nurseServiceTime = model.practitionerTreatmentTimes.sample();
			
		}
	}

}
