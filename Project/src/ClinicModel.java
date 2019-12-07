import desmoj.core.simulator.*;

/**
 * 
 * @authors 
 * Mai Chen
 * Aquoya Faust
 * Remington Steele
 * 
 * This Desmo-J based simulator will be a process oriented Model
 * It simulates a 12 hour a day 8am-8pm clinic including Nurse Practioners and Specialists
 */
public class ClinicModel extends Model {
	/* COMPONENTS */
	ProcessQueue<NursePractioner> idleNurseQueue;
	ProcessQueue<Specialist> idleSpecialistQueue;
	
	ProcessQueue<Patient> nurseQueue;
	ProcessQueue<Patient> specialistQueue;
	/* STATISTICS*/
	
	public ClinicModel(Model owner, String modelName, boolean showInReport, boolean showInTrace) {
		super(owner, modelName, showInReport, showInTrace);
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doInitialSchedules() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	
}
