package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Injury;
import model.events.WorldListener;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;

public class Ambulance extends MedicalUnit {

	public Ambulance(String unitID, Address location, int stepsPerCycle, WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	@Override
	public void treat() {
		getTarget().getDisaster().setActive(false);

		Citizen target = (Citizen) getTarget();
		if (target.getHp() == 0) {
			jobsDone();
			return;
		} else if (target.getBloodLoss() > 0) {
			target.setBloodLoss(target.getBloodLoss() - getTreatmentAmount());
			if (target.getBloodLoss() == 0)
				target.setState(CitizenState.RESCUED);
		}

		else if (target.getBloodLoss() == 0)

			heal();

	}

	public void respond(Rescuable r) throws IncompatibleTargetException, CannotTreatException {
		if (r instanceof Citizen) {
			Citizen s = (Citizen) r;
			if (!(this.canTreat(s)) || s.getBloodLoss() == 0) {
				throw new CannotTreatException(this, r, "You are trying to send an ambulance to a safe Citizen");
			} else if (!(s.getDisaster() instanceof Injury)) {
				throw new CannotTreatException(this, s,
						"You're NOT trying to send an Ambulance to an injured citizen, Ya molo5ya");
			} else if (getTarget() != null && ((Citizen) getTarget()).getBloodLoss() > 0
					&& getState() == UnitState.TREATING)
				reactivateDisaster();
			finishRespond(r);
		} else
			throw new IncompatibleTargetException(this, r,
					"You're trying to send an Ambulance to a non-citzen, Ya Fasolia");
	}
	public String tohtmlString(){
		String x = "NO TRAGET";
		if(this.getTarget()!=null){
			x = this.getTarget().getTargetName();
		}
		return "<html>" + 
		"Unit type:   Ambulance  <BR>" +
		"Unit ID: " + this.getUnitID() + " <BR>" + 
		"Unit's Location:" + this.getLocation().getX() + "," + this.getLocation().getY() + "<BR>" +
		"Unit's Steps per Cycle: " + this.getStepsPerCycle() + " <BR>" + 
		"Unit's Target: Citizen: " + " <BR>"+ x + " <BR>" + 
		"Units State: " + this.getState() ;
//		"Units DTT: " + this.getDistanceToTarget();
		
		
		
		
		
	}

}
