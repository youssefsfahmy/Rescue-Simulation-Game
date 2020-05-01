package controller;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import com.sun.javafx.tk.Toolkit;

import exceptions.CannotTreatException;
import exceptions.IncompatibleTargetException;
import exceptions.SimulationException;
import model.disasters.Disaster;
import model.events.SOSListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import model.units.Ambulance;
import model.units.DiseaseControlUnit;
import model.units.Evacuator;
import model.units.FireTruck;
import model.units.GasControlUnit;
import model.units.PoliceUnit;
import model.units.Unit;
import model.units.UnitState;
import simulation.Rescuable;
import simulation.Simulator;
import view.MainView;

public class CommandCenter implements SOSListener {
	private Unit clickedUnit;
	private boolean isSecondClick;
	private JButton Unclick;
	private JButton nextCycle;
	private Simulator engine;

	private ArrayList<ResidentialBuilding> visibleBuildings;
	private ArrayList<Citizen> visibleCitizens;
	private ArrayList<Unit> emergencyUnits;

	private ArrayList<Citizen> deceasedCitizens= new ArrayList<Citizen>();
	private ArrayList<ResidentialBuilding> destroyedBuildings= new ArrayList<ResidentialBuilding>();

	private MainView mainView;

	private int currentCycle;

	public CommandCenter() throws Exception {
		
		visibleBuildings = new ArrayList<ResidentialBuilding>();
		visibleCitizens = new ArrayList<Citizen>();
		engine = new Simulator(this);
		mainView = new MainView();

		emergencyUnits = engine.getEmergencyUnits();
		currentCycle = 0;

		this.addNextCycleButton();
		this.addBase(engine);
		this.addCitizenButton();
		this.addBuildingButton();
		mainView.updateGrid();
		this.addUnitButton();
		this.addUnclickButton();

	}

	private void addUnclickButton() {
		Unclick = new JButton("HELP");
		Unclick.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainView.showhelp();

			}
		});

		mainView.addhelp(Unclick);

	}

	@Override
	public void receiveSOSCall(Rescuable r) {

		if (r instanceof ResidentialBuilding) {

			if (!visibleBuildings.contains(r))
				visibleBuildings.add((ResidentialBuilding) r);

		} else {

			if (!visibleCitizens.contains(r))
				visibleCitizens.add((Citizen) r);
		}

	}

	public void addNextCycleButton() {
		nextCycle = new JButton("Next Cycle");
		nextCycle.addActionListener(new ActionListener() {
			// To do: add game over check
			@Override
			public void actionPerformed(ActionEvent arg0) {
				try {
					engine.nextCycle();
					currentCycle++;
					mainView.updateUpperPanel(engine.calculateCasualties(),
							currentCycle);
					addBuildingButton();
					addCitizenButton();
					mainView.updateGrid();
					addBase(engine);
					updateLog();
					addDisasterButton();
				} catch (SimulationException e) {
					mainView.showError(e.getMessage());
				}
				mainView.clearLeftPanel();
				mainView.clearRightPanel();
				if (engine.checkGameOver()) {
					mainView.endGame(engine.calculateCasualties());
				}
				mainView.scrollDown();

			}
		});
		mainView.addNextCycleButton(nextCycle);
	}

	public void addUnitButton() {
		for (final Unit rb : emergencyUnits) {
			ImageIcon icon2;
			
			if(rb instanceof FireTruck){
				icon2 = new ImageIcon("src/resources/FireTruck.png");
			}
			else if(rb instanceof GasControlUnit){
				icon2 = new ImageIcon("src/resources/GasControlUnit.png");
			}
			else if(rb instanceof DiseaseControlUnit){
				icon2 = new ImageIcon("src/resources/DiseaseControlUnit.png");
			}
			else if(rb instanceof Ambulance){
				icon2 = new ImageIcon("src/resources/Ambulance.png");
				
			}
			else{
				icon2 = new ImageIcon("src/resources/Evacuator.png");
			}
			JButton unitJButton = new JButton();
			unitJButton.setIcon(icon2);
			unitJButton.setPreferredSize(new Dimension(50,50));
			unitJButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					
					mainView.unitInfoPanel(rb.tohtmlString());
					if (isSecondClick) {
						
						isSecondClick = false;
						Cursor cursors = new Cursor(Cursor.DEFAULT_CURSOR);
						mainView.setCursor(cursors);
					} else {
						playSound("src/sounds/AmbulanceStart.wav");
						clickedUnit = rb;
						isSecondClick = true;

						Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
						mainView.setCursor(cursor);
					}
					
					if (rb instanceof Evacuator) {
						mainView.clearCitList();

						for (final Citizen rb : ((Evacuator) rb)
								.getPassengers()) {
							ImageIcon icon1 = new ImageIcon("src/resources/CitizenButton.png");
							JButton citJButton = new JButton();
							citJButton.setIcon(icon1);
							citJButton.setPreferredSize(new Dimension(30,30));

							citJButton.addActionListener(new ActionListener() {
								public void actionPerformed(ActionEvent arg0) {

									mainView.infoPanelBottom(rb.tohtmlString());

								}

							});

							mainView.addCitizenJButtons(citJButton);
							mainView.updateLeftPanel();

						}
					}

				}

			});

			mainView.addUnitJButton(unitJButton);
		}

	}

	public void addBuildingButton() {
		for (final ResidentialBuilding rb : visibleBuildings) {
			JButton unitJButton = new JButton();
			ImageIcon icon2 = new ImageIcon("src/resources/"
					+ formBeforeBuildingString(rb) + ".png");
			
			ImageIcon icon1 = new ImageIcon("src/resources/"
					+ formAfterBuildingString(rb) + ".png");
			unitJButton.setRolloverIcon(icon1);
			unitJButton.setIcon(icon2);
			unitJButton.setVisible(true);
			int posX = rb.getLocation().getX();
			int posY = rb.getLocation().getY();

			unitJButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {

					mainView.infoPanel(rb.tohtmlString());
					mainView.clearCitList();
					citizensOfBuilding(rb.getOccupants());
					mainView.updateLeftPanel();

					if (isSecondClick) {
						try {
							clickedUnit.respond(rb);
							playSound("src/sounds/AmbulanceOMW.wav");
							mainView.unitInfoPanel(clickedUnit.tohtmlString());
							JButton unitJButton = new JButton();
							ImageIcon icon2 = new ImageIcon("src/resources/"
									+ formBeforeBuildingString(rb) + ".png");
							
							ImageIcon icon1 = new ImageIcon("src/resources/"
									+ formAfterBuildingString(rb) + ".png");
							unitJButton.setRolloverIcon(icon1);
							unitJButton.setIcon(icon2);
						} catch (IncompatibleTargetException e) {
							mainView.showError(e.getMessage());
						} catch (CannotTreatException e) {
							mainView.showError(e.getMessage());
						}
					}
					Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
					mainView.setCursor(cursor);
					isSecondClick = false;
				

				}

			});

			mainView.addRescJButton(posX, posY, unitJButton);

		}

	}



	public void addCitizenButton() {
		for (final Citizen rb : visibleCitizens) {
			ImageIcon icon1 = new ImageIcon("src/resources/"
					+ formAfterCitizenString(rb) + ".png");

			
			JButton unitJButton = new JButton();
			unitJButton.setIcon(icon1);
			int posX = rb.getLocation().getX();
			int posY = rb.getLocation().getY();

			unitJButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					mainView.clearLeftPanel();
					mainView.infoPanel(rb.tohtmlString());

					if (isSecondClick) {
						try {
							clickedUnit.respond(rb);
							mainView.unitInfoPanel(clickedUnit.tohtmlString());
							playSound("src/sounds/AmbulanceOMW.wav");

						} catch (IncompatibleTargetException e) {
							mainView.showError(e.getMessage());
						} catch (CannotTreatException e) {
							mainView.showError(e.getMessage());
						}
					}
					Cursor cursor = new Cursor(Cursor.DEFAULT_CURSOR);
					mainView.setCursor(cursor);
					isSecondClick = false;
			

				}

			});

			mainView.addRescJButton(posX, posY, unitJButton);

		}

	}

	public void citizensOfBuilding(ArrayList<Citizen> occupants) {
		for (final Citizen oc : occupants) {
			ImageIcon icon1 = new ImageIcon("src/resources/CitizenButton.png");
			JButton citJButton = new JButton();
			citJButton.setPreferredSize(new Dimension(30,30));
			citJButton.setIcon(icon1);
			citJButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {

					mainView.infoPanelBottom(oc.tohtmlString());
				}
			});
			mainView.addCitizenJButtons(citJButton);
		}
	}

	public void addBase(Simulator e) {
		JButton BaseJButton = new JButton();
		ImageIcon icon1 = new ImageIcon("src/resources/base.png");

		BaseJButton.setIcon(icon1);
		BaseJButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				mainView.clearLeftPanel();
				mainView.infoPanel("<html> <BR><BR><BR><BR><BR>BASE</html>");
				for (final Citizen rb : e.getCitizens()) {
					if (rb.getLocation().getX() == 0
							&& rb.getLocation().getY() == 0) {
						
						ImageIcon icon1 = new ImageIcon("src/resources/CitizenButton.png");
						JButton citJButton = new JButton();
						citJButton.setIcon(icon1);
						citJButton.setPreferredSize(new Dimension(30,30));

						citJButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								mainView.clearleftBottomPanel();
								mainView.infoPanelBottom(rb.tohtmlString());

							}

						});

						mainView.addBaseJButton(citJButton);
					}
				}

				for (final Unit rb : e.getEmergencyUnits()) {
					if (rb.getLocation().getX() == 0
							&& rb.getLocation().getY() == 0) {
						JButton unitJButton = new JButton();
						ImageIcon icon2;
						
						if(rb instanceof FireTruck){
							icon2 = new ImageIcon("src/resources/FireTruck1.png");
						}
						else if(rb instanceof GasControlUnit){
							icon2 = new ImageIcon("src/resources/GasControlUnit1.png");
						}
						else if(rb instanceof DiseaseControlUnit){
							icon2 = new ImageIcon("src/resources/DiseaseControlUnit1.png");
						}
						else if(rb instanceof Ambulance){
							icon2 = new ImageIcon("src/resources/Ambulance1.png");
						}
						else{
							icon2 = new ImageIcon("src/resources/Evacuator1.png");
						}
						unitJButton.setIcon(icon2);
						unitJButton.setPreferredSize(new Dimension(30,30));
						unitJButton.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent arg0) {
								mainView.clearleftBottomPanel();
								mainView.infoPanelBottom(rb.tohtmlString());

							}

						});

						mainView.addBaseJButton(unitJButton);

					}
				}
			}
		});
		mainView.clearLeftPanel();
		mainView.addRescJButton(0, 0, BaseJButton);
		mainView.revalidate();
	}

	
	public String formAfterCitizenString(Citizen C){
		String Alive = "";

		String Units = "";


		for (final Unit rb : emergencyUnits) {
			if (C.getLocation().equals(rb.getLocation())) {
				Units = "Unit";
			}
		}
		if(C.getState()==CitizenState.DECEASED){
			Alive = "Dead";
		}
		else{
			Alive = "Alive";
		}
		return "Citizen" + Alive + Units;
	}
	public String formAfterBuildingString(ResidentialBuilding B) {
		String colour = "";
		Unit unit = null;
		String citizens = "";
		String Units = "";
		String Empty = "";

		for (final Unit rb : emergencyUnits) {
			if (B.getLocation().equals(rb.getLocation())) {
				Units = "U";
				unit = rb;
			}
		}
		if (!(B.getDisaster().isActive()) && unit != null
				&& unit.getState() == UnitState.IDLE) {
			colour = "G";
		}



		if (!(B.getDisaster().isActive()) && unit == null) {
			colour = "G";
		}
		if (B.getOccupants().size() != 0) {
			citizens = "C";
		}

		if (unit != null) {
			if (!(unit.getState().equals(UnitState.IDLE))) {
				colour = "B";
			}
		}

		if (B.getDisaster().isActive() && unit == null) {
			colour = "R";
		}
		if (citizens.equals("") && Units.equals("")) {
			Empty = "E";
		}
		if(B.getStructuralIntegrity()==0){
			return "destroyed";
		}

		return colour + citizens + Units + Empty;

	}
	
	public String formBeforeBuildingString(ResidentialBuilding B) {
		String colour = "";
		Unit unit = null;
		String citizens = "";
		String Units = "";
		String Empty = "";

		for (final Unit rb : emergencyUnits) {
			if (B.getLocation().equals(rb.getLocation())) {
				Units = "U";
				unit = rb;
			}
		}
		if (!(B.getDisaster().isActive()) && unit != null
				&& unit.getState() == UnitState.IDLE) {
			colour = "G";
		}



		if (!(B.getDisaster().isActive()) && unit == null) {
			colour = "G";
		}
		if (B.getOccupants().size() != 0) {
			citizens = "C";
		}

		if (unit != null) {
			if (!(unit.getState().equals(UnitState.IDLE))) {
				colour = "B";
			}
		}

		if (B.getDisaster().isActive() && unit == null) {
			colour = "R";
		}
		if (citizens.equals("") && Units.equals("")) {
			Empty = "E";
		}
		if(B.getStructuralIntegrity()==0){
			return "destroyed";
		}

		return colour;

	}

	public void updateLog(){
		for(final  Disaster d : engine.getExecutedDisasters()){
			if(d.getStartCycle()==currentCycle){
				
				JLabel g = new JLabel("Cycle:	"+ currentCycle +d.toLabelString());
				g.setPreferredSize(new Dimension(1000,20));
				mainView.addLogLabel(g);
			}
		}
		for(final  Citizen c : engine.getCitizens()){
			if(c.getState()==CitizenState.DECEASED){
			if(!deceasedCitizens.contains(c)){
//				playSound("src/sounds/yalahwyragel.wav");
				JLabel g = new JLabel("Cycle:	"+ currentCycle +c.toLabelString());
				g.setPreferredSize(new Dimension(1000,20));
				mainView.addLogLabel(g);
				deceasedCitizens.add(c);
			}
			}
		}
		for(final  ResidentialBuilding B : engine.getBuildings()){
			if(B.getStructuralIntegrity()==0){
			if(!destroyedBuildings.contains(B)){
				JLabel g = new JLabel("Cycle:	"+ currentCycle +B.toLabelString());
				g.setPreferredSize(new Dimension(1000,20));
				mainView.addLogLabel(g);
				destroyedBuildings.add(B);
			}
			}
		}
		
	}
	public void addDisasterButton() {
		
		mainView.cleardisasterButtonPanel();
		for (final Disaster rb : engine.getExecutedDisasters()) {
			if(rb.isActive()){
			JButton unitJButton = new JButton(rb.getDisasterName());
			unitJButton.setPreferredSize(new Dimension(100,20));
			unitJButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent arg0) {
					mainView.cleardisasterPanel();
					mainView.disasterInfoPanel(rb.toDLabelString());
					

				}

			});

			mainView.addDisasterJButton(unitJButton);
		}
		}

	}
	public static void main(String[] args) throws Exception {
		CommandCenter commandCenter = new CommandCenter();
	}
	public void playSound(String soundName)
	 {
	   try 
	   {
	    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile( ));
	    Clip clip = AudioSystem.getClip( );
	    clip.open(audioInputStream);
	    clip.start( );
	   }
	   catch(Exception ex)
	   {
	     System.out.println("Error with playing sound.");
	     ex.printStackTrace( );
	   }
	 }
}
