import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;

public class Patient extends SimProcess{
	
	public Patient(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void lifeCycle() throws SuspendExecution {
		ClinicModel model = (ClinicModel)getModel();
		
		model.numberInSystem.update();
		if(model.nurseQueue.length() >= model.queueThreshold) {
			model.totalCost.update(500);
			model.numberInSystem.update(-1);
			return;
		} 
		
		model.nurseQueue.insert(this);
		if(model.idleNurseQueue.isEmpty()) {
			passivate();
		} else {
			NursePractioner nurse = model.idleNurseQueue.removeLast();
			nurse.activate();
		}
		if(model.refer.sample()) {
			
		}
		/////////////////////////////////////////////////End Of Nurse Practioners Work//////////////
		
	}

}
