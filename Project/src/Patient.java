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
		if(balk(model)) {
			return;
		}
		
		seeNurse(model);
		passivate();
		
		/////////////////////////////////////////////////End Of Nurse Work//////////////
		if(model.refer.sample()) {
			if(timeInSystem(model) > 30 || model.specialistQueue.length() >= model.numberExamRooms - 1) {
				model.numberInSystem.update(-1);
				model.totalCost.update(500);
				model.numberToNurseThenEr.update();
				return;
			} else {
				model.specialistQueue.insert(this);
				if(!model.idleSpecialistQueue.isEmpty()) {
					model.idleSpecialistQueue.removeFirst().activate();
				}
				passivate();
			}
		}
		//Services completed
		model.numberCompletedService.update();
		model.totalTimeUntilCompletion.update(timeInSystem(model));
		model.numberInSystem.update(-1);
	}
	
	private boolean balk(ClinicModel model) {
		if(model.balks[model.nurseQueue.length()].sample()) {
			model.totalCost.update(500);
			model.numberInSystem.update(-1);
			model.numberBalked.update();
			return true;
		}
		return false;
	}
	
	private void seeNurse(ClinicModel model) {
		model.nurseQueue.insert(this);
		if(!model.idleNurseQueue.isEmpty()) {
			NursePractioner nurse = model.idleNurseQueue.removeFirst();
			nurse.activate();
		}
	}
	
	private double timeInSystem(ClinicModel model) {
		return model.presentTime().getTimeAsDouble() - arrivalTime;
		
	}
	
	
	
	

}
