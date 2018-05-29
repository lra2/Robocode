package OIEP;

//import java.awt.Color;
import robocode.Droid;
import robocode.MessageEvent;
import robocode.TeamRobot;
import static robocode.util.Utils.normalRelativeAngleDegrees;
import robocode.*;
//import java.awt.geom.*;
//import robocode.util.*;

/**
	Follows the orders of the leader.
	If the leader is killed, he still tries to kill as many enemies as possible.
*/

public class Capanga extends TeamRobot implements Droid {
	boolean movingForward;
	boolean leaderIsDead = false;	

	public void run() {
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