import desmoj.core.simulator.*;

public class StoppingCondition extends ModelCondition {
	
	public StoppingCondition(Model owner, String name, boolean showInTrace) {
		super(owner, name, showInTrace);
	}

	@Override
	public boolean check() {
		ClinicModel model = (ClinicModel)getModel();
		if(model.presentTime().getTimeAsDouble() >= 720 && model.numberInSystem.getValue() == 0) {
			return true;
		}
		return false;
	}
}
