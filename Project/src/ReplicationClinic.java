import java.util.concurrent.TimeUnit;

import desmoj.core.simulator.*;
import desmoj.core.statistic.ConfidenceCalculator;

public class ReplicationClinic extends Model {
	
	public static final int NUM_REPLICATIONS = 1;
	public static final boolean INCLUDE_OUTPUT = false;
	public static final boolean INCLUDE_REPORT = true;
	
	protected ConfidenceCalculator repTotalCost; //gained from Tally
	protected ConfidenceCalculator repArrival;
	protected ConfidenceCalculator repBalk;
	protected ConfidenceCalculator repNurseThenEr;
	protected ConfidenceCalculator repCompletedTreatment;
	protected ConfidenceCalculator repTimeToCompletion;
	protected ConfidenceCalculator repNurseUtilization;
	protected ConfidenceCalculator repSpecialistUtilization;
	protected ConfidenceCalculator repNumInWaitingRoom;
	
	
	
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
			Thread.sleep(20);
		} catch (InterruptedException e) {
			
		}
		
		exp.report();
		exp.finish();
		//TODO: Finish this 
		/* IMPORTANT STATISTICS*/
		
		//Check important statistics in this if statement
		if (model.presentTime().getTimeAsDouble() < 720 || false) {
			System.out.println("WARNING: Rep" + n + ": Runtime Exception...\nretrying...");
			
            return false;
        }
		
		//UPDATE REPLICATION STATISTICS HERE
		
		
		
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
        exp.tracePeriod(new TimeInstant(0), new TimeInstant(720, TimeUnit.MINUTES));
        exp.debugOff(new TimeInstant(0));
        exp.setSilent(true);
        
        exp.start();
        exp.report();
        exp.stop();
	}
	
	
}
