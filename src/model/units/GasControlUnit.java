package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Collapse;
import model.disasters.Fire;
import model.disasters.GasLeak;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;
import simulation.Rescuable;

public class GasControlUnit extends FireUnit {

	public GasControlUnit(String unitID, Address location, int stepsPerCycle, WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	public void treat() {
		getTarget().getDisaster().setActive(false);

		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (target.getStructuralIntegrity() == 0) {
			jobsDone();
			return;
		} else if (target.getGasLevel() > 0)
			target.setGasLevel(target.getGasLevel() - 10);

		if (target.getGasLevel() == 0)
			jobsDone();

	}

	public void respond(Rescuable r) throws IncompatibleTargetException, CannotTreatException {
		if (r instanceof ResidentialBuilding) {
			if (!(this.canTreat(r)) || ((ResidentialBuilding) r).getGasLevel() == 0) {
				throw new CannotTreatException(this, r,
						"You are trying to send a gas control unit to a safe Residential Building, Haha idiot");
			} else if (!(r.getDisaster() instanceof GasLeak)) {
				throw new CannotTreatException(this, r,
						"You're trying to send a Gas control unit to a NOT gas leaking building, Hawhaw");
			} else
				super.respond(r);
		} else
			throw new IncompatibleTargetException(this, r,
					"You're  NOT trying to send a Gas control unit to a Residential Building, meow");

	}
	public String tohtmlString(){
		String x = "NO TRAGET";
		if(this.getTarget()!=null){
			x = this.getTarget().getTargetName();
		}
		return "<html>" + 
		"Unit type:   Gas control unit  <BR>" +
		"Unit ID: " + this.getUnitID() + " <BR>" + 
		"Unit's Location:" + this.getLocation().getX() + "," + this.getLocation().getY() + "<BR>" +
		"Unit's Steps per Cycle: " + this.getStepsPerCycle() + " <BR>" + 
		"Unit's Target: Citizen: " + " <BR>"+ x + " <BR>" + 
		"Units State: " + this.getState();
		
		
		
		
	}

}
