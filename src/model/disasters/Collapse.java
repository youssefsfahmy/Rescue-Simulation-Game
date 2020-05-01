package model.disasters;

import exceptions.DisasterException;
import model.infrastructure.ResidentialBuilding;

public class Collapse extends Disaster {

	public Collapse(int startCycle, ResidentialBuilding target) {
		super(startCycle, target);

	}

	public void strike() throws DisasterException {
		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		target.setFoundationDamage(target.getFoundationDamage() + 10);
		super.strike();
	}

	public void cycleStep() {
		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		target.setFoundationDamage(target.getFoundationDamage() + 10);
	}

	@Override
	public String getDisasterName() {
		return "Collapse";
	}

}
