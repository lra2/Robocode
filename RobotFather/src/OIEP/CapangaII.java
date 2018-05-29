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

public class CapangaII extends TeamRobot implements Droid {
	double moveAmount;
	boolean First = true;
	double previousEnergy = 100;
 	int movementDirection = 1;
	boolean leaderIsDead = false;
	 
	public void run() {
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