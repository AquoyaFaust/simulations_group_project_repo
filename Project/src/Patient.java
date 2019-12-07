import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;

public class Patient extends SimProcess{
	
	private double arrivalTime;
	public Patient(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
	}

	@Override
	public void lifeCycle() throws SuspendExecution {
		ClinicModel model = (ClinicModel)getModel();
		arrivalTime = model.presentTime().getTimeAsDouble();
		model.numberInSystem.update();
		if(model.nurseQueue.length() >= model.queueThreshold) {
			model.totalCost.update(500);
			model.numberInSystem.update(-1);
			return;
		} 
		
		model.nurseQueue.insert(this);
		if(!model.idleNurseQueue.isEmpty()) {
			NursePractioner nurse = model.idleNurseQueue.removeFirst();
			nurse.activate();
		}
		passivate();
		
		/////////////////////////////////////////////////End Of Nurse Work//////////////
		if(model.refer.sample()) {
			if(model.presentTime().getTimeAsDouble()-arrivalTime > 30 || model.specialistQueue.length() >= model.numberExamRooms - 1) {
				model.numberInSystem.update(-1);
				model.totalCost.update(500);
				return;
			} else {
				model.specialistQueue.insert(this);
				if(!model.idleNurseQueue.isEmpty()) {
					model.idleSpecialistQueue.removeFirst().activate();
				}
				passivate();
			}
		}
		/////////////////////////////////////////////////End Of Sp Work//////////////
		model.numberInSystem.update();
	}

}
