import java.util.concurrent.TimeUnit;

import desmoj.core.simulator.*;
import desmoj.core.statistic.ConfidenceCalculator;

public class ReplicationClinic extends Model {
	
	public static final int NUM_REPLICATIONS = 10;
	public static final boolean INCLUDE_OUTPUT = false;
	public static final boolean INCLUDE_REPORT = false;
	
	protected ConfidenceCalculator repTotalCost; //gained from Tally
	protected ConfidenceCalculator repArrival; //gained in numInSystem
	protected ConfidenceCalculator repBalk; //numBalked
	protected ConfidenceCalculator repNurseThenEr; //variable by name
	protected ConfidenceCalculator repCompletedTreatment; //completed
	protected ConfidenceCalculator repTimeToCompletion; //tally for completion
	protected ConfidenceCalculator repNurseUtilization; //idle nurse queue
	protected ConfidenceCalculator repSpecialistUtilization; //idle specialist queue
	protected ConfidenceCalculator repNumInWaitingRoom; //Captured by nurseQueue. avg length
	
	
	
	public ReplicationClinic(Model owner, String name, boolean showInReport,
			boolean showInTrace) {
		super(owner, name, showInReport, showInTrace);
	}

	@Override
	public String description() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void doInitialSchedules() {

		for(int i = 1; i <= NUM_REPLICATIONS; i++) {
			boolean success;
			do {
				success = run(i);
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					
				}
			} while (!success);
		}
		
	}

	private boolean run(int n) {
		ClinicModel model = new ClinicModel(null, "Clinic Model", true, true);
		
		String fileOut = "output/ClinicModel" + "_Repl_" + n;
		Experiment exp = new Experiment(fileOut, INCLUDE_REPORT);
		
		exp.setSeedGenerator(941 + 2*n);
		model.connectToExperiment(exp);
		exp.setShowProgressBar(false); // display a progress bar (or not)
		exp.stop(model.stop); //fix to stopping case
		// Set the period of the trace and debug
		exp.traceOff(new TimeInstant(0));
		exp.debugOff(new TimeInstant(0));
		
		try {
			exp.start();
		} catch(Exception e) {
			System.out.println("WARNING: Rep" + n + ": Runtime Exception...\nretrying...");
			exp.finish();
			return false;
		}
		
		if(exp.hasError() || exp.isAborted()) {
			System.out.println("WARNING: Rep" + n + ": Runtime Exception...\nretrying...");
			
			exp.finish();
			return false;
		}
		
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			
		}
		
		exp.report();
		exp.finish();
		/* IMPORTANT STATISTICS Grab here*/
		long totalCost = model.totalCost.getValue();
		long totalArrivals = model.numberInSystem.getObservations();
		long numberBalked = model.numberBalked.getValue();
		long numberToNurseThenER = model.numberToNurseThenEr.getValue();
		long numberCompletedTreatment = model.numberCompletedService.getValue();
		double avgTimeToCompletion = model.totalTimeUntilCompletion.getMean();
		double nurseUtilizationTime =  model.idleNurseQueue.averageLength() / model.NUMBER_OF_NURSES; //
		double specialistUtilizationTime = model.idleSpecialistQueue.averageLength() / model.NUMBER_OF_SPECIALISTS; //
		double avgNumberInWaitingRoom = model.nurseQueue.averageLength();
		//Check important statistics in this if statement
		//Sanity Checkk
		if (model.presentTime().getTimeAsDouble() < 720 || totalCost < (1200 + 1500 + 300) || totalArrivals < 0 || avgTimeToCompletion < 0 || 
				(nurseUtilizationTime >= 1 || nurseUtilizationTime < 0) || (specialistUtilizationTime >= 1 && specialistUtilizationTime < 0) || avgNumberInWaitingRoom < 0 ) {
			System.out.println("WARNING: Rep" + n + ": Runtime Exception...\nretrying...");
			
            return false;
        }
		
		//UPDATE REPLICATION STATISTICS HERE
		repTotalCost.update(totalCost);
		repArrival.update(totalArrivals);
		repBalk.update(numberBalked);
		repNurseThenEr.update(numberToNurseThenER);
		repCompletedTreatment.update(numberCompletedTreatment);
		repTimeToCompletion.update(avgTimeToCompletion);
		repNurseUtilization.update(nurseUtilizationTime);
		repSpecialistUtilization.update(specialistUtilizationTime);
		repNumInWaitingRoom.update(avgNumberInWaitingRoom);
		
		
		if (INCLUDE_OUTPUT) {
			System.out.println("SOME OUTPUT");
		}
		return true;
	}
	
	@Override
	public void init() {
		repTotalCost = new ConfidenceCalculator(this, 
				"Per Replication: Total Cost", true, false); //gained from Tally
		repArrival = new ConfidenceCalculator(this, 
				"Per Replication: Number of Patients arrived", true, false);
		repBalk = new ConfidenceCalculator(this, 
				"Per Replication: Number of Patients balked before Nurse Practioner sees them", true, false);
		repNurseThenEr = new ConfidenceCalculator(this, 
				"Per Replication: Number of Patients who see the nurse and get sent to ER", true, false);
		repCompletedTreatment = new ConfidenceCalculator(this, 
				"Per Replication: Number of Patients who complete treatment in the clinic", true, false);
		repTimeToCompletion = new ConfidenceCalculator(this, 
				"Per Replication: Time it takes patients to complete service if not sent t ER", true, false);
		repNurseUtilization = new ConfidenceCalculator(this, 
				"Per Replication: How much Nurses are used", true, false);
		repSpecialistUtilization = new ConfidenceCalculator(this, 
				"Per Replication: How much specialists are used", true, false);
		repNumInWaitingRoom = new ConfidenceCalculator(this, 
				"Per Replication: Average number of people in the waiting room", true, false);
	}
	
	public static void main(String[] args) {
		
		Experiment.setReferenceUnit(TimeUnit.MINUTES);
		
		ReplicationClinic repModel = new ReplicationClinic(null, "Replication Model for ClinicModel", true, true);
		Experiment exp = new Experiment("ClinicModel Replications");
		repModel.connectToExperiment(exp);
		
		exp.setShowProgressBar(false);
        exp.stop(new TimeInstant(0));
        exp.traceOff(new TimeInstant(0));
        exp.debugOff(new TimeInstant(0));
        exp.setSilent(true);
        
        exp.start();
        exp.report();
        exp.finish();
	}
	
	
}
