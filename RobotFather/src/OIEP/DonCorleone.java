package OIEP;

import java.awt.Color;
import robocode.HitByBulletEvent;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;
import robocode.*;
import java.io.IOException;
import java.util.Vector;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.rule.FactHandle;


/**
 	Leader of the team. Once it detects where the enemy robot is, sends information about that robot to the rest of the team.
 	His focus is on survival.
 	It only shoots when it crashes into an enemy robot, so we are sure that it will not miss the target.
 */

public class DonCorleone extends TeamRobot {

	public static String RULES_FILE = "RobotFather/rules/DonCorleoneRules.drl";
	public static String CONSULT_ACTIONS = "consult_actions";
	
	private KnowledgeBuilder kbuilder;
	private KnowledgeBase kbase;
    private StatefulKnowledgeSession ksession;
    private Vector<FactHandle> referenciasHechosActuales = new Vector<FactHandle>();
	
	double previousEnergy = 100;
 	int movementDirection = 1;
  	int gunDirection = 1;

	static double direction;

	// Is set to true when setAhead is called, set to false on setBackboolean movingForward;
	boolean movingForward;

	public void run() {

		// Setting up colors
		RobotColors c = new RobotColors();
	
		c.bodyColor = Color.cyan;
		c.gunColor = Color.blue;
		c.radarColor = Color.blue;
		c.scanColor = Color.yellow;
		c.bulletColor = Color.green;

	
		setBodyColor(c.bodyColor);
		setGunColor(c.gunColor);
		setRadarColor(c.radarColor);
		setScanColor(c.scanColor);
		setBulletColor(c.bulletColor);

		// Sends the color object to the team
		try {
			broadcastMessage(c);
			System.out.println("Colors setted.");
		} catch (IOException ignored) {}
		
		while (true) {
			setTurnRadarRight(10000);
			setAhead(movementDirection * 10000);

			if (Math.random() < 0.5) {
				setTurnRight(10000);
			} else {
				setTurnLeft(10000);
			}
			execute();
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		// If it is a robot of your team, do not attack.
		if (isTeammate(e.getName())) {
			return;
		}

		// Calculates the angle of the enemy
		double enemyBearing = this.getHeading() + e.getBearing();

		// Calculates the position of the enemy
		double enemyX = getX() + e.getDistance() * Math.sin(Math.toRadians(enemyBearing));
		double enemyY = getY() + e.getDistance() * Math.cos(Math.toRadians(enemyBearing));

		// Sends the enemy information to the team
		try {
			broadcastMessage(new Info(e.getDistance(), e.getEnergy(), enemyX, enemyY, getHeading(), getVelocity(), getHeadingRadians()));
			out.println("Message sent");
		} catch (IOException ex) {
			out.println("Message not sent");
			ex.printStackTrace(out);
		}
		
		setTurnRight(e.getBearing()+90-30*movementDirection);
    	
    	// Strategy to dodge the bullets
    	double changeInEnergy = previousEnergy - e.getEnergy();

    	//Checks if the energy of the enemy robot has decreased, if yes, it is because the enemy shot.
    	if ((changeInEnergy > 0) && (changeInEnergy <= 3)) {
	         movementDirection = -movementDirection;
	         double d = (e.getDistance() / 4 + 25) * movementDirection;
			 setAhead(d);
	     }
		 previousEnergy = e.getEnergy();
		 scan();
	}
		
	// If hit, changes direction
	public void onHitByBullet(HitByBulletEvent e) {
		movementDirection = -movementDirection;
	}

	public void onHitWall(HitWallEvent e) {
		double angle = e.getBearing();
		turnRight(angle - 100);
	}
	
	public void onHitRobot(HitRobotEvent e) {
		reverseDirection();

		// If it is an enemy robot, shoot it, because the chances of error are minimal.
		if (!isTeammate(e.getName())) {
			turnGunRight(e.getBearing());
			fire(2);
		}
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