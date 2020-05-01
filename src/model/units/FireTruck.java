package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Collapse;
import model.disasters.Fire;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;
import simulation.Rescuable;

public class FireTruck extends FireUnit {

	public FireTruck(String unitID, Address location, int stepsPerCycle, WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	@Override
	public void treat() {
		getTarget().getDisaster().setActive(false);

		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (target.getStructuralIntegrity() == 0) {
			jobsDone();
			return;
		} else if (target.getFireDamage() > 0)

			target.setFireDamage(target.getFireDamage() - 10);

		if (target.getFireDamage() == 0)

			jobsDone();

	}

	public void respond(Rescuable r) throws IncompatibleTargetException, CannotTreatException {
		if (r instanceof ResidentialBuilding) {
					if (!(this.canTreat(r)) || ((ResidentialBuilding) r).getFireDamage() == 0) {
							throw new CannotTreatException(this, r,
									"You are trying to send a fire truck to a safe Residential Building");
					} else if (!(r.getDisaster() instanceof Fire)) {
							throw new CannotTreatException(this, r,
									"You're trying to send a Fire Truck to a NOT Fire damaged building, Dumb");
			} 		else
						super.respond(r);
		} else
			throw new IncompatibleTargetException(this, r,
					"You're  NOT trying to send a Fire truck to a Residential Building, Duh");
	}
	public String tohtmlString(){
		String x = "NO TRAGET";
		if(this.getTarget()!=null){
			x = this.getTarget().getTargetName();
		}
		return "<html>" + 
		"Unit type:   Fire Truck  <BR>" +
		"Unit ID: " + this.getUnitID() + " <BR>" + 
		"Unit's Location:" + this.getLocation().getX() + "," + this.getLocation().getY() + "<BR>" +
		"Unit's Steps per Cycle: " + this.getStepsPerCycle() + " <BR>" + 
		"Unit's Target: (Building) " + " <BR>"+ x + " <BR>" +
		"Units State: " + this.getState();
		
		
		
		
	}

}
