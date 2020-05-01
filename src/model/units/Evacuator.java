package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Collapse;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import simulation.Address;
import simulation.Rescuable;

public class Evacuator extends PoliceUnit {

	public Evacuator(String unitID, Address location, int stepsPerCycle, WorldListener worldListener, int maxCapacity) {
		super(unitID, location, stepsPerCycle, worldListener, maxCapacity);

	}

	@Override
	public void treat() {
		ResidentialBuilding target = (ResidentialBuilding) getTarget();
		if (target.getStructuralIntegrity() == 0 || target.getOccupants().size() == 0) {
			
			jobsDone();
			return;
		}

		for (int i = 0; getPassengers().size() != getMaxCapacity() && i < target.getOccupants().size(); i++) {
			getPassengers().add(target.getOccupants().remove(i));
			i--;
		}

		setDistanceToBase(target.getLocation().getX() + target.getLocation().getY());

	}

	public void respond(Rescuable r) throws IncompatibleTargetException, CannotTreatException {
		if (r instanceof ResidentialBuilding) {
			if (!(this.canTreat(r)) || !(r.getDisaster() instanceof Collapse)) {
				throw new CannotTreatException(this, r,
						"You are trying to send an Evacuator to a safe Residential Building");
			} 
			else
				super.respond(r);
		} else
			throw new IncompatibleTargetException(this, r,
					"You're  NOT trying to send an Evacutor to a Residential Building, Cat");

	}
	public String tohtmlString(){
		String x = "NO TRAGET";
		if(this.getTarget()!=null){
			x = this.getTarget().getTargetName();
		}
		String y = "";
		if(this.getPassengers().size()!=0){
			y = "(passengers are in the <BR> location occupants on the left)";
		}
		
		return "<html>" + 
		"Unit type:   Evacuator  <BR>" +
		"Unit ID: " + this.getUnitID() + " <BR>" + 
		"Unit's Location:" + this.getLocation().getX() + "," + this.getLocation().getY() + "<BR>" +
		"Unit's Steps per Cycle: " + this.getStepsPerCycle() + " <BR>" + 
		"Unit's Target: Building: " + " <BR>" + x + " <BR>" +
		"Units State: " + this.getState() + "<BR>" +
		"Number of Passengers:  " + this.getPassengers().size() + "<BR>" +
		y;
		
		
		
		
		
	}
}
