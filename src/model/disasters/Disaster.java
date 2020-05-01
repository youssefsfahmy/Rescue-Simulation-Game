package model.disasters;

import exceptions.BuildingAlreadyCollapsedException;
import exceptions.CitizenAlreadyDeadException;
import exceptions.DisasterException;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Rescuable;
import simulation.Simulatable;

public abstract class Disaster implements Simulatable {
	private int startCycle;
	private Rescuable target;
	private boolean active;

	public Disaster(int startCycle, Rescuable target) {
		this.startCycle = startCycle;
		this.target = target;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getStartCycle() {
		return startCycle;
	}

	public Rescuable getTarget() {
		return target;
	}

	public void strike() throws DisasterException {

		if (this.getTarget() instanceof Citizen) {
			Citizen c = (Citizen) this.getTarget();
			if (c.getState().equals(CitizenState.DECEASED))
				throw new CitizenAlreadyDeadException(this, "You're trying to strike an already dead citizen");

		} else if (this.getTarget() instanceof ResidentialBuilding) {
			ResidentialBuilding r = (ResidentialBuilding) this.getTarget();
			if (r.getStructuralIntegrity() == 0)
				throw new BuildingAlreadyCollapsedException(this,
						"You're trying to strike an already collapsed building");
		}
			target.struckBy(this);
			active = true;
		}

	public abstract String getDisasterName();

	public String toLabelString() {
		return "**The Disaster " + this.getDisasterName() +" has struck " + "the target:" + this.getTarget().getTargetName();
	
	}
	public String toDLabelString() {
		return "<html> The Disaster " + this.getDisasterName() +" has struck " + "<BR>the target:" + this.getTarget().getTargetName()+"</html>";
	
	}
	}

