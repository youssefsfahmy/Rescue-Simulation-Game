package model.units;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import model.disasters.Infection;
import model.disasters.Injury;
import model.events.WorldListener;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;

public class DiseaseControlUnit extends MedicalUnit {

	public DiseaseControlUnit(String unitID, Address location, int stepsPerCycle, WorldListener worldListener) {
		super(unitID, location, stepsPerCycle, worldListener);
	}

	@Override
	public void treat() {
		getTarget().getDisaster().setActive(false);
		Citizen target = (Citizen) getTarget();
		if (target.getHp() == 0) {
			jobsDone();
			return;
		} else if (target.getToxicity() > 0) {
			target.setToxicity(target.getToxicity() - getTreatmentAmount());
			if (target.getToxicity() == 0)
				target.setState(CitizenState.RESCUED);
		}

		else if (target.getToxicity() == 0)
			heal();

	}

	public void respond(Rescuable r) throws IncompatibleTargetException, CannotTreatException {
		if (r instanceof Citizen) {
			Citizen s = (Citizen) r;
			if (!(this.canTreat(s)) || s.getToxicity() == 0) {
				throw new CannotTreatException(this, r,
						"You are trying to send a Disease control unit to a safe Citizen, duh");
			} else if (!(s.getDisaster() instanceof Infection)) {
				throw new CannotTreatException(this, s,
						"You're NOT trying to send a DiseaseControlUnit to an infected citizen, Ya Batee5a");
			} else if (getTarget() != null && ((Citizen) getTarget()).getToxicity() > 0
					&& getState() == UnitState.TREATING)
				reactivateDisaster();
			finishRespond(r);
		} else
			throw new IncompatibleTargetException(this, r,
					"You're trying to send a DiseaseControlUnit to a non-citzen, Ya Bala7a");

	}
	
	public String tohtmlString(){
		String x = "NO TRAGET";
		if(this.getTarget()!=null){
			x = this.getTarget().getTargetName();
		}
		return "<html>" + 
		"Unit type:   Disease control unit  <BR>" +
		"Unit ID: " + this.getUnitID() + " <BR>" + 
		"Unit's Location:" + this.getLocation().getX() + "," + this.getLocation().getY() + "<BR>" +
		"Unit's Steps per Cycle: " + this.getStepsPerCycle() + " <BR>" + 
		"Unit's Target: Citizen: " + " <BR>"+ x + " <BR>" + 
		"Units State: " + this.getState();
		
		
		
		
	}
}
