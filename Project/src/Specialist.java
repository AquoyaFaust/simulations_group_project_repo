import co.paralleluniverse.fibers.SuspendExecution;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;
import desmoj.core.simulator.TimeSpan;

public class Specialist extends SimProcess {

	public Specialist(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void lifeCycle() throws SuspendExecution {
		// TODO Auto-generated method stub
		ClinicModel model = (ClinicModel) getModel();
		model.totalCost.update(1500);

		while (true) {
			if (model.specialistQueue.isEmpty()) {
				model.idleSpecialistQueue.insert(this);
				this.passivate();

			}
			Patient patient = model.specialistQueue.removeFirst();
			model.totalCost.update(100);
			double ta = model.specialistTreatmentTimes.sample();
			this.hold(new TimeSpan(ta));
			patient.activate();

		}
	}

}
