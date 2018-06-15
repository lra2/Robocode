package OIEP;

//import java.awt.Color;
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

import robocode.*;
//import java.awt.geom.*;
//import robocode.util.*;

/**
	Follows the orders of the leader.
	If the leader is killed, he still tries to kill as many enemies as possible.
*/

public class Capanga extends TeamRobot implements Droid {
	public static String RULES_FILE = "RobotFather/rules/CapangaRules.drl";
	public static String CONSULT_ACTIONS = "consult_actions";
	
	private KnowledgeBuilder kbuilder;
	private KnowledgeBase kbase;
    private StatefulKnowledgeSession ksession;
    private Vector<FactHandle> currentReferencedFacts = new Vector<FactHandle>();
	
	boolean movingForward;
	boolean leaderIsDead = false;	

	public void run() {
		DEBUG.enableDebugMode(System.getProperty("robot.debug", "true").equals("true"));

    	// Creates a knowledge base
    	createKnowledgeBase();
    	DEBUG.message("KBase created");
		
		setAdjustGunForRobotTurn(true);
		
		while(true) {
			//If leaderIsDead, tries to kill the enemies shooting in all directions
			if(leaderIsDead) {
				setAdjustGunForRobotTurn(false);
				setTurnRight(10000);
				setFire(2);
				setMaxVelocity(5);
				setAhead(1000);
			}
			setAhead(1000);
			execute();
		}
	}
	
	private void createKnowledgeBase() {
        String rulesFile = System.getProperty("robot.rules", Capanga.RULES_FILE);

        DEBUG.message("Creating knowledge base");
        kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        
        DEBUG.message("Loading rules since "+rulesFile);
        kbuilder.add(ResourceFactory.newClassPathResource(rulesFile, Capanga.class), ResourceType.DRL);
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

    private Vector<CapangaAction> loadActions() {
    	DEBUG.message("load actions");
    	CapangaAction action;
        Vector<CapangaAction> actionsList = new Vector<CapangaAction>();

        for (QueryResultsRow result : ksession.getQueryResults(Capanga.CONSULT_ACTIONS)) {
            action = (CapangaAction) result.get("action");  			// get the Action object
            action.setRobot(this);                      		// link it to the current robot
            actionsList.add(action);
            ksession.retract(result.getFactHandle("action")); 	// clears the fact from the active memory
        }

        return actionsList;
    }

    private void executeActions(Vector<CapangaAction> actions) {
    	DEBUG.message("execute actions");
        for (CapangaAction action : actions) {
            action.startExecution();
        }
    }

	public void onMessageReceived(MessageEvent e) {
		System.out.println("Received");

		if (e.getMessage() instanceof Info) {
			Info i = (Info) e.getMessage(); 

			double X = i.getX();
			double Y = i.getY();

			if (movingForward) {
				setTurnRight(normalRelativeAngleDegrees(getBearing(X, Y) + 80));
			} else {
				setTurnRight(normalRelativeAngleDegrees(getBearing(X, Y) + 100));
			}

			turnGunRight(getBearing(X, Y));

			// Shoot according to distance
			setFire(FirePower(getDistance(X, Y)));
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
	
	public void onRobotDeath(RobotDeathEvent e) {	
		String Str = e.getName();
		
		if(Str.contains("DonCorleone")) {
			leaderIsDead = true;
		}
	}
	
	public double getBearing(double X, double Y) {
		double dx = X - getX();
		double dy = Y - getY();

		// Calculates the angle to aim at the target
		double theta = Math.toDegrees(Math.atan2(dx, dy));
        return normalRelativeAngleDegrees(theta - getGunHeading());
	}
	
	public double getBearingRadians(double X, double Y) {
		double dx = X - getX();
		double dy = Y - getY();

		// Calculates the angle to aim at the target.
		double theta = Math.toDegrees(Math.atan2(dx, dy));
        return Math.toRadians(normalRelativeAngleDegrees(theta - getGunHeading()));
	}

	// FirePower calculated according to distance
	public double FirePower(double robotDistance) {
		if (robotDistance > 300) 
		{
			return 1.0 ;
		} 
		else if (robotDistance > 50) 
		{
			return 2.0 ;
		} 
		else 
		{
			return 3.0 ;
		}
	}	
	
	public double getDistance(double X, double Y) {
		return Math.sqrt(Math.pow((Y - getY()), 2) + Math.pow((X - getX()), 2));
	}

	public void onHitRobot(HitRobotEvent e) {
		if (e.isMyFault()) {
			turnRight(e.getBearing() + 90);
			setAhead(200);
		}
	}

	public void onHitWall(HitWallEvent e) {
		turnRight(e.getBearing()+90);
		setAhead(200);
	}

	public void reverseDirection() {
		if (movingForward) {
			setBack(40000);
			movingForward = false;
		} else {
			setAhead(40000);
			movingForward = true;
		}
	}
}