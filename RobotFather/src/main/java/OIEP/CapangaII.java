package OIEP;

//import static robocode.util.Utils.normalRelativeAngleDegrees;
import robocode.HitRobotEvent;
//import robocode.ScannedRobotEvent;
//import java.awt.Color;
//import java.awt.geom.*;
//import robocode.util.*;
import robocode.*;
import robocode.Droid;
import robocode.MessageEvent;
import robocode.TeamRobot;
import static robocode.util.Utils.normalRelativeAngleDegrees;

import java.util.Vector;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;

import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;
import org.drools.runtime.rule.QueryResultsRow;
import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.HitRobotEvent;
import robocode.HitWallEvent;
import robocode.RobotDeathEvent;

public class CapangaII extends TeamRobot implements Droid {
	public static String RULES_FILE = "OIEP/rules/Capanga2Rules.drl";
	public static String CONSULT_ACTIONS = "consult_actions";
	
	private KnowledgeBuilder kbuilder;
	private KnowledgeBase kbase;
    private StatefulKnowledgeSession ksession;
    private Vector<FactHandle> currentReferencedFacts = new Vector<FactHandle>();
	
	double moveAmount;
	boolean First = true;
	double previousEnergy = 100;
 	int movementDirection = 1;
	boolean leaderIsDead = false;
	 
	public void run() {
		DEBUG.enableDebugMode(System.getProperty("robot.debug", "true").equals("true"));

    	// Creates a knowledge base
    	createKnowledgeBase();
    	DEBUG.message("KBase created");
		
		out.println("CapangaII ready.");

		// Move as much as possible
		moveAmount = 9999;
		setAhead(moveAmount);
		turnRight(90);

		while (true) {
			setAhead(moveAmount);
			if(leaderIsDead){
				setTurnGunRight(360);
				setFire(2);
			}
			execute();
		}
	}
	
	private void createKnowledgeBase() {
        String rulesFile = System.getProperty("robot.rules", CapangaII.RULES_FILE);

        DEBUG.message("Creating knowledge base");
        kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        
        DEBUG.message("Loading rules since "+rulesFile);
        kbuilder.add(ResourceFactory.newClassPathResource(rulesFile, CapangaII.class), ResourceType.DRL);
        if (kbuilder.hasErrors()) {
            System.err.println(kbuilder.getErrors().toString());
        }

        kbase = KnowledgeBaseFactory.newKnowledgeBase();
        kbase.addKnowledgePackages(kbuilder.getKnowledgePackages());
        
        DEBUG.message("Creating session)");
        ksession = kbase.newStatefulKnowledgeSession();
    }
    
    private void cleanAnteriorFacts() {
    	DEBUG.message("clean anterior facts");
        for (FactHandle referencedFact : this.currentReferencedFacts) {
            ksession.retract(referencedFact);
        }
        this.currentReferencedFacts.clear();
    }

    private Vector<Capanga2Action> loadActions() {
    	DEBUG.message("load actions");
    	Capanga2Action action;
        Vector<Capanga2Action> actionsList = new Vector<Capanga2Action>();

        for (QueryResultsRow result : ksession.getQueryResults(CapangaII.CONSULT_ACTIONS)) {
            action = (Capanga2Action) result.get("action");  			// get the Action object
            action.setRobot(this);                      		// link it to the current robot
            actionsList.add(action);
            ksession.retract(result.getFactHandle("action")); 	// clears the fact from the active memory
        }

        return actionsList;
    }

    private void executeActions(Vector<Capanga2Action> actions) {
    	DEBUG.message("execute actions");
        for (Capanga2Action action : actions) {
            action.startExecution();
        }
    }

	public void onMessageReceived(MessageEvent e) {
		System.out.println("Received");

		if (e.getMessage() instanceof Info) {
			Info i = (Info) e.getMessage();
			turnGunRight(getBearing(i.getX(), i.getY()));
			fire(FirePower(getDistance(i.getX(), i.getY())));

			double changeInEnergy = previousEnergy - i.getEnergy();

			if ((changeInEnergy > 0) && (changeInEnergy <= 3)) {
				movementDirection = -movementDirection;
				setAhead((i.getDistance() / 4 + 25) * movementDirection);
			}
		}
		else if (e.getMessage() instanceof RobotColors) {
			RobotColors c = (RobotColors) e.getMessage();
			setBodyColor(c.bodyColor);
			setGunColor(c.gunColor);
			setRadarColor(c.radarColor);
			setScanColor(c.scanColor);
			setBulletColor(c.bulletColor);
		}
	}
	
	public void onHitRobot(HitRobotEvent e) {
		// If it is an enemy robot, shoot it, because the chances of error are minimal.
		if (!isTeammate(e.getName())) {
			turnGunRight(e.getBearing());
			fire(2);
		}
		turnRight(e.getBearing() + 90);
	}
	
	public void onHitWall(HitWallEvent e) {
		turnRight(e.getBearing() + 90);
	}		
	
	public void onRobotDeath(RobotDeathEvent e) {
		String Str = e.getName();
		if(Str.contains("DomCorleone")){
			leaderIsDead = true;
		}
	}
	
	public double getBearing(double X, double Y) {
		double dx = X - getX();
		double dy = Y - getY();
		double theta = Math.toDegrees(Math.atan2(dx, dy));

        return normalRelativeAngleDegrees(theta - getGunHeading());
	}
	
	public double getBearingRadians(double X, double Y) {
		double dx = X - getX();
		double dy = Y - getY();
		double theta = Math.toDegrees(Math.atan2(dx, dy));

        return Math.toRadians(normalRelativeAngleDegrees(theta - getGunHeading()));
	}
	
	public double FirePower(double robotDistance) {
		if (robotDistance > 300) 
		{
			return 1.0;
		} 
		else if (robotDistance > 50) 
		{
			return 2.0;
		} 
		else 
		{
			return 3.0;
		}
	}	
	
	public double getDistance(double X, double Y) {
		return Math.sqrt(Math.pow((Y - getY()), 2) + Math.pow((X - getX()), 2));
	}
}