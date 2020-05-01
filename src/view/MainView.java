package view;

import javax.swing.*;

import controller.CommandCenter;
import model.events.WorldListener;
import model.people.Citizen;
import model.units.Unit;
import exceptions.SimulationException;
import simulation.Simulator;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class MainView extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7452431479327386979L;
	private JPanel upperPanel, leftPanel, leftUpper, leftMiddle,
			leftBottom,margin, rightPanel,rightBottom,rightBottomLower,rightBottomUpper, rightUpper, rightMiddle, centerPanel;
	private JPanel bottomPanel;
	//private Container contentPane;
	private JLabel casualitiesLabel, currentCycleLabel;
	
	private JButton[][] JButtonGrid = new JButton[10][10];
	private JLabel l = new JLabel();
	private JLabel m = new JLabel();
	private JLabel UnitInfoLabel = new JLabel();
	private String [] logList= new String [1000];
	int counter = 0;
	DefaultListModel dlm = new DefaultListModel();
	JScrollPane ScrollPanel;

	


	private ArrayList<JButton> occupantsLeftPanel = new ArrayList<>();

	public MainView() {
		super("The Game");

		setSize(1500, 1000);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		JLabel contentPane = new JLabel();
		ImageIcon icon1 = new ImageIcon("src/resources/BackGround.png");
		contentPane.setPreferredSize(new Dimension(1500,1000));
		contentPane.setIcon( icon1 );
		contentPane.setLayout( new BorderLayout() );
		this.setContentPane( contentPane );
		
//		contentPane = getContentPane();
//		contentPane.setBackground(Color.BLUE);
//		
//		contentPane.setLayout(new BorderLayout());



		upperPanel = new JPanel();
		upperPanel.setBackground(Color.GRAY);
		upperPanel.setOpaque(false);
		upperPanel.setLayout(new FlowLayout());
		upperPanel.setPreferredSize(new Dimension(1000, 50));
		upperPanel.setVisible(true);
		contentPane.add(upperPanel, BorderLayout.NORTH);

		casualitiesLabel = new JLabel();
		currentCycleLabel = new JLabel();

		upperPanel.add(casualitiesLabel);
		upperPanel.add(currentCycleLabel);

		updateUpperPanel(0, 0);

		leftPanel = new JPanel();
		leftPanel.setBackground(Color.WHITE);
		leftPanel.setOpaque(false);
		leftPanel.setLayout(new BorderLayout());
		leftPanel.setPreferredSize(new Dimension(300, 1000));
		leftPanel.setAutoscrolls(true);
		leftPanel.setVisible(true);

		
		
		

		leftUpper = new JPanel();
		leftUpper.setBackground(Color.WHITE);
		leftUpper.setOpaque(false);
		leftUpper.setLayout(new FlowLayout());
		leftUpper.setPreferredSize(new Dimension(250, 377));
		leftUpper.setAutoscrolls(true);
		leftUpper.setVisible(true);
		leftUpper.setBorder(BorderFactory.createEmptyBorder(85, 40, 0, 40));
		leftPanel.add(leftUpper, BorderLayout.NORTH);
		

		leftMiddle = new JPanel();
		leftMiddle.setBackground(Color.RED);
		leftMiddle.setOpaque(false);
		leftMiddle.setLayout(new FlowLayout());
		leftMiddle.setPreferredSize(new Dimension(300, 340));
		leftMiddle.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));
		leftMiddle.setAutoscrolls(true);
		leftMiddle.setVisible(true);
		leftPanel.add(leftMiddle, BorderLayout.CENTER);

		leftBottom = new JPanel();
		leftBottom.setBackground(Color.WHITE);
		leftBottom.setOpaque(false);
		leftBottom.setLayout(new FlowLayout());
		leftBottom.setPreferredSize(new Dimension(250, 300));
		leftBottom.setBorder(BorderFactory.createEmptyBorder(80, 25, 0, 25));

		leftBottom.setAutoscrolls(true);
		leftBottom.setVisible(true);
		leftPanel.add(leftBottom, BorderLayout.SOUTH);

		contentPane.add(leftPanel, BorderLayout.WEST);

		rightPanel = new JPanel();
		rightPanel.setBackground(Color.WHITE);
		rightPanel.setOpaque(false);
		rightPanel.setLayout(new FlowLayout());
		rightPanel.setPreferredSize(new Dimension(300, 1000));
		rightPanel.setAutoscrolls(true);
		rightPanel.setVisible(true);
		contentPane.add(rightPanel, BorderLayout.EAST);


		rightUpper = new JPanel();
		rightUpper.setBackground(Color.WHITE);
		rightUpper.setOpaque(false);
		rightUpper.setLayout(new FlowLayout());
		rightUpper.setPreferredSize(new Dimension(300, 285));
		rightUpper.setAutoscrolls(true);
		rightUpper.setVisible(true);
		rightUpper.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));
		rightPanel.add(rightUpper, BorderLayout.NORTH);
		JLabel l = new JLabel();
		l.setPreferredSize(new Dimension(300,65));
		rightUpper.add(l);


		rightMiddle = new JPanel();
		rightMiddle.setBackground(Color.BLUE);
		rightMiddle.setOpaque(false);
		rightMiddle.setLayout(new FlowLayout());
		rightMiddle.setPreferredSize(new Dimension(300, 280));
		rightMiddle.setAutoscrolls(true);
		rightMiddle.setVisible(true);
		rightMiddle.setBorder(BorderFactory.createEmptyBorder(25, 40, 0, 40));
		rightPanel.add(rightMiddle, BorderLayout.CENTER);
		
		rightBottom = new JPanel();
		rightBottom.setBackground(Color.BLUE);
		rightBottom.setOpaque(false);
		rightBottom.setLayout(new BorderLayout());
		rightBottom.setPreferredSize(new Dimension(300, 300));
		rightBottom.setAutoscrolls(true);
		rightBottom.setVisible(true);
		
		rightPanel.add(rightBottom, BorderLayout.SOUTH);

		
		
		rightBottomUpper = new JPanel();
		rightBottomUpper.setBackground(Color.BLUE);
		rightBottomUpper.setOpaque(false);
		rightBottomUpper.setLayout(new FlowLayout());
		rightBottomUpper.setPreferredSize(new Dimension(300, 200));
		rightBottomUpper.setAutoscrolls(true);
		rightBottomUpper.setVisible(true);
		rightBottomUpper.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));
		rightBottom.add(rightBottomUpper, BorderLayout.NORTH);
		
		rightBottomLower = new JPanel();
		rightBottomLower.setBackground(Color.BLUE);
		rightBottomLower.setOpaque(false);
		rightBottomLower.setLayout(new FlowLayout());
		rightBottomLower.setPreferredSize(new Dimension(300, 150));
		rightBottomLower.setAutoscrolls(true);
		rightBottomLower.setVisible(true);
		rightBottomLower.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 40));
		rightBottom.add(rightBottomLower, BorderLayout.SOUTH);

//		JList<String> logJList = new JList<String>(logList);
//		logJList.setPreferredSize(new Dimension(1000,50));
		bottomPanel = new JPanel();
		bottomPanel.setBackground(Color.GRAY);
		bottomPanel.setOpaque(false);
		bottomPanel.setLayout(new FlowLayout());
		bottomPanel.setPreferredSize(new Dimension(1000, 50));
		bottomPanel.setVisible(true);

//		JList b = new JList(logList);
//		bottomPanel.add(b);
		
		JList list = new JList(dlm);
		ScrollPanel = new JScrollPane(list);
		ScrollPanel.setPreferredSize(new Dimension(1000,40));
		bottomPanel.add(ScrollPanel);
		
		contentPane.add(bottomPanel, BorderLayout.SOUTH);

		centerPanel = new JPanel();
		centerPanel.setBackground(Color.LIGHT_GRAY);
		centerPanel.setOpaque(false);
		centerPanel.setLayout(new GridLayout(10, 10));
		centerPanel.setPreferredSize(new Dimension(600, 200));
		centerPanel.setVisible(true);
		contentPane.add(centerPanel, BorderLayout.CENTER);

		setVisible(true);
		validate();

		CreateGrid();
		validate();

	}

	public void CreateGrid() {
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				JButton x = new JButton("Empty");
				x.setEnabled(false);
				
//				Color c = new Color(0, 0, 0, 100);
//				
//				x.setBackground(c);

				
				
				
				centerPanel.add(x);
				JButtonGrid[j][i] = x;
			}
		}
		centerPanel.validate();
	}

	public void updateGrid() {
		centerPanel.removeAll();
		centerPanel.revalidate();
		centerPanel.repaint();
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {

				centerPanel.add(JButtonGrid[j][i]);
			}
		}
		centerPanel.validate();
		this.validate();
	}

	public void addRescJButton(int x, int y, JButton UnitJButton) {
		JButtonGrid[x][y] = UnitJButton;
		centerPanel.validate();
	}

	public void addCitizenJButtons(JButton UnitJButton) {
		occupantsLeftPanel.add(UnitJButton);
	}

	public void updateUpperPanel(int currentCasualities, int currentCycle) {
		casualitiesLabel.setText("Casualities: " + currentCasualities);
		currentCycleLabel.setText("Current Cycle: " + currentCycle);

		upperPanel.validate();
	}


	public void showError(String message) {
		JOptionPane.showMessageDialog(getContentPane(),"  " + message);
	}
	public void showhelp() {
		JOptionPane.showMessageDialog(getContentPane(),"The Rescue Mission of Nasr City \n \n"
+"\n In 2050, Nasr city is currently under attack where some disasters are happening to the local"
+"\n citizens and residential buildings. You are the command of the emergency units to be sent, "
+ "\n for the sake of minimizing as much damages and the number of casualties occurring. With your "
+ "\ntough decisions, it shall be saved. \n \n"

+"\n Guidelines:"
+"\n The number of casualties is always displayed on top. \n"
 
+"\n Right Panel:"
+"\n All emergency units are displayed on the upper right box and the currently clicked unit’s "
+"\n information (Type, ID, Location, Steps per Cycle, Target, and State) is displayed below it."
+"\n In Disasters box, you find all the current disasters the have struck along with its target upon "
+"\n choosing it. \n"
+"\n Left Panel:"
+"\n The currently clicked cell’s information is displayed on the upper left box ."
+"\n In case of residential building, the occupants are displayed in the middle box."
+"\n The currently clicked occupants’ information is displayed in the bottom box."

+"\n A full summary of what happens each cycle is always displayed at the bottom."
+"\n Hovering over any not empty cell , graphically represents some information. \n"
+"\n How to play:"
+"\n       Next Cycle button: "
+"\n       Initially, click it to start the game. "
+"\n       After making your move, use it to update what happens next. \n"
+"\n Rescuing:"
+"\n       Upon choosing what do you want to rescue now, choose wisely which unit you want to send."
+"\n       Click the unit you wish to send and then click the target you wish to save."
+ "\n      If you wish to unclick, select the unit again, the curser will go back to default."
+"\n       You now successfully finished one move. \n"

+"\n UNITS: "
+"\n     WHITE:	Ambulance ---> Injury disaster."
+"\n	     ORANGE:	Disease Control Unit ---> Infection disaster."
+"\n	     GREY: 		Evacuator ---> Collapse disaster."
+"\n	     RED:		 Fire truck ---> Fire disaster."
+"\n	     BLUE: 		Gas Control Unit ---> Gas Leak disaster."
);
	}

	public void infoPanel(String string) {
		leftUpper.removeAll();
		leftUpper.revalidate();
		leftUpper.repaint();
		l= new JLabel();
		l.setText(string);
		l.setVisible(true);
		leftUpper.add(l);

	}

	public void unitInfoPanel(String string) {
		rightMiddle.removeAll();
		rightMiddle.revalidate();
		rightMiddle.repaint();
		UnitInfoLabel.setPreferredSize(new Dimension(300,200));

		UnitInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);
		UnitInfoLabel.setText(string);
		rightMiddle.add(UnitInfoLabel);
	}
	
	public void clearLeftPanel(){
		leftBottom.removeAll();
		leftBottom.revalidate();
		leftBottom.repaint();
		leftUpper.removeAll();
		leftUpper.revalidate();
		leftUpper.repaint();
		leftMiddle.removeAll();
		leftMiddle.revalidate();
		leftMiddle.repaint();
	}

	public void clearCitList() {
		occupantsLeftPanel.clear();
		
	}

	public void addUnitJButton(JButton unitJButton) {
		rightUpper.add(unitJButton);
		validate();

	}

	public void updateLeftPanel() {
		leftMiddle.removeAll();
		leftMiddle.revalidate();
		leftMiddle.repaint();
		leftMiddle.setPreferredSize(new Dimension(250, 340));
		leftUpper.add(l);

		for (final JButton oc : occupantsLeftPanel) {
			leftMiddle.add(oc);
		}
		leftPanel.validate();
	}

	public void infoPanelBottom(String string) {
		leftBottom.removeAll();
		m = new JLabel(string);
//		m.setText(string);
//		m.setVisible(true);
//		m.repaint();
		leftBottom.add(m, BorderLayout.SOUTH);
		leftBottom.revalidate();
//		leftBottom.repaint();

	}

	public void addhelp(JButton unclick) {
		upperPanel.add(unclick,BorderLayout.EAST);
		unclick.setVisible(true);
		validate();
		
	}

	public void clearRightPanel() {
		rightMiddle.removeAll();
		rightMiddle.revalidate();
		rightMiddle.repaint();

		
	}

	public void addEvacJButton(JButton unitJButton) {

		rightMiddle.add(unitJButton);
		
		
	}
	
	public void addBaseJButton(JButton unitJButton) {
		leftUpper.add(l);
		unitJButton.setVisible(true);
		leftMiddle.add(unitJButton);
		leftPanel.validate();
		
	}

	public void addNextCycleButton(JButton nextCycle) {
		upperPanel.add(nextCycle);
		
	}
	public void addLogLabel(JLabel c){	
		dlm.addElement(c.getText());
		JScrollBar vertical = ScrollPanel.getVerticalScrollBar();
		vertical.setValue( vertical.getMaximum() );
		ScrollPanel.setVerticalScrollBar(vertical);
		revalidate();
		
	}
	public void scrollDown(){
		JScrollBar vertical = ScrollPanel.getVerticalScrollBar();
		vertical.setValue( vertical.getMaximum() );
		ScrollPanel.setVerticalScrollBar(vertical);
		revalidate();
	}

	public void clearleftBottomPanel() {
		leftBottom.removeAll();
		leftBottom.repaint();
		leftBottom.revalidate();
		
	}

	public void addDisasterJButton(JButton unitJButton) {
		rightBottomUpper.add(unitJButton);
		
	}

	public void disasterInfoPanel(String labelString) {
		JLabel l = new JLabel(labelString);
		l.setPreferredSize(new Dimension(100, 100));
		rightBottomLower.add(new JLabel(labelString));
		
	}

	public void cleardisasterPanel() {
		rightBottomLower.removeAll();
		rightBottomLower.repaint();
		rightBottomLower.revalidate();
		
	}

	public void cleardisasterButtonPanel() {
		rightBottomUpper.removeAll();
		rightBottomUpper.repaint();
		rightBottomUpper.revalidate();
		
	}

	public void endGame(int c) {
		JOptionPane.showMessageDialog(getContentPane(),"Game Over \nYour Causalty count is    " + c + "\nYour score is  " + (10000 - c*100) );		
		this.dispose();
	}
	


}