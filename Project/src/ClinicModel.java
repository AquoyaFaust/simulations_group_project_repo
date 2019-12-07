import java.util.concurrent.TimeUnit;

import desmoj.core.dist.BoolDistBernoulli;
import desmoj.core.dist.ContDistExponential;
import desmoj.core.dist.DiscreteDistEmpirical;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.ProcessQueue;
import desmoj.core.simulator.TimeInstant;
import desmoj.core.statistic.Count;
import desmoj.core.statistic.Tally;

/**
 * 
 * @authors Mai Chen Aquoya Faust Remington Steele
 * 
 *          This Desmo-J based simulator will be a process oriented Model It
 *          simulates a 12 hour a day 8am-8pm clinic including Nurse Practioners
 *          and Specialists
 */
public class ClinicModel extends Model {
	/*Changeable*/
	int numberExamRooms = 4;
	int numberNurses = 1;
	int numberSpecialist = 1;
	/* COMPONENTS */
	ProcessQueue<NursePractioner> idleNurseQueue;
	ProcessQueue<Specialist> idleSpecialistQueue;
	
	protected ContDistExponential interarrivalTimes8am;
	protected ContDistExponential interarrivalTimes10am;
	protected ContDistExponential interarrivalTimes4pm;
	protected BoolDistBernoulli[] balks;
	protected ContDistExponential practitionerTreatmentTimes; 
	protected BoolDistBernoulli refer;
	protected ContDistExponential specialistTreatmentTimes;

	ProcessQueue<Patient> nurseQueue;
	ProcessQueue<Patient> specialistQueue;
	/* STATISTICS */
	protected Count numberInSystem;
	protected int queueThreshold;
	protected Tally totalCost;
	protected Count numberBalked;
	protected Count numberReffered;

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
		ArrivalGenerator gen = new ArrivalGenerator(this, "Arrival Generator", true);
		gen.activate();
		//need to activate all practioners and specialists
		
		totalCost.update(numberExamRooms*300);

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		interarrivalTimes8am = new ContDistExponential(this, "Inter Arrival Times for 8am-10am", 15, true, true);
		interarrivalTimes10am = new ContDistExponential(this, "Inter Arrival Times for 10am-4pm", 6, true, true);
		interarrivalTimes4pm = new ContDistExponential(this, "Inter Arrival Times for 4pm-8pm", 9, true, true);
		balks = new BoolDistBernoulli[9];
		for(int i = 0; i < 9; i++) {
			balks[i] = new BoolDistBernoulli(this, "Balk Probability", i/8, true, true);
		}
		practitionerTreatmentTimes = new ContDistExponential(this, "Practitioner Treatment Times", 8, true, true);
		refer = new BoolDistBernoulli(this, "Refferal Probability", .4, true, true);
		specialistTreatmentTimes = new ContDistExponential(this, "Specialist treatment Times", 25, true, true);
		totalCost = new Tally(this, "Total Cost", true, true);
		numberInSystem = new Count(this, "Number of Patients in system", true, true);
		numberBalked = new Count(this, "Number of Patients balked", true, true);
		numberReffered = new Count(this, "Number of Patients reffered to the specialist", true, true);

	}

	public static void main(String[] args) {
		// Set reference units to be in minutes
		Experiment.setReferenceUnit(TimeUnit.MINUTES);

		// Create model and experiment
		ClinicModel model = new ClinicModel(null, "Single Server Queue with DESMO-J Processes (v1)", true, true);
		Experiment exp = new Experiment("SSQExperiment");

		// connect both
		model.connectToExperiment(exp);

		// Set experiment parameters
		exp.setShowProgressBar(false); // display a progress bar (or not)
		exp.stop(new TimeInstant(8, TimeUnit.HOURS));
		// Set the period of the trace and debug
		exp.tracePeriod(new TimeInstant(0, TimeUnit.MINUTES), new TimeInstant(60, TimeUnit.MINUTES));
		exp.debugPeriod(new TimeInstant(0, TimeUnit.MINUTES), new TimeInstant(60, TimeUnit.MINUTES));

		// Start the experiment at simulation time 0.0
		exp.start();

		// --> now the simulation is running until it reaches its end criterion
		// ...
		// ...
		// <-- afterwards, the main thread returns here

		// Generate the report (and other output files)
		exp.report();

		// Stop all threads still alive and close all output files
		exp.finish();
	}

}
