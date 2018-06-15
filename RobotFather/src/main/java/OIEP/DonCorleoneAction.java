package OIEP;

import robocode.TeamRobot;

public class DonCorleoneAction {
	private int type;
	private double parameter;
	private int priority;
	
	private TeamRobot donCorleone;
	
	public static final int FORWARD = 1;
	public static final int BACKWARD = 2;
	public static final int STOP = 3;
	public static final int SHOOT = 4;
	public static final int TURN_TANK_RIGHT = 5;
	public static final int TURN_TANK_LEFT = 6;
	public static final int TURN_RADAR_RIGHT = 7;
	public static final int TURN_RADAR_LEFT = 8;
	public static final int TURN_CANNON_RIGHT = 9;
	public static final int TURN_CANNON_LEFT = 10;
	
	public DonCorleoneAction() {
	}
	
	public DonCorleoneAction(int type, double parameter, int priority) {
		this.type = type;
		this.parameter = parameter;
		this.priority = priority;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public double getParameter() {
		return parameter;
	}
	
	public void setParameter(double parameter) {
		this.parameter = parameter;
	}
	
	public int getPriority() {
		return priority;
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	public void startExecution() {
		if(this.donCorleone != null) {
			switch(this.type) {
				case DonCorleoneAction.SHOOT: donCorleone.setFire(parameter); break;
				case DonCorleoneAction.FORWARD: donCorleone.setAhead(parameter); break;
				case DonCorleoneAction.BACKWARD: donCorleone.setBack(parameter); break;
				case DonCorleoneAction.STOP: donCorleone.setStop(); break;
				case DonCorleoneAction.TURN_CANNON_LEFT: donCorleone.setTurnGunLeft(parameter); break;
				case DonCorleoneAction.TURN_CANNON_RIGHT: donCorleone.setTurnGunRight(parameter); break;
				case DonCorleoneAction.TURN_RADAR_LEFT: donCorleone.setTurnRadarLeft(parameter); break;
				case DonCorleoneAction.TURN_RADAR_RIGHT: donCorleone.setTurnRadarRight(parameter); break;
                case DonCorleoneAction.TURN_TANK_LEFT: donCorleone.setTurnLeft(parameter); break;
                case DonCorleoneAction.TURN_TANK_RIGHT: donCorleone.setTurnRight(parameter); break;
			}
		}
	}
	
	void setRobot(TeamRobot donCorleone) {
		this.donCorleone = donCorleone;
	}
	
	public String toString() {
		String targetType = "";
		
		switch(this.type) {
			case DonCorleoneAction.SHOOT: targetType = "Shoot"; break;
			case DonCorleoneAction.FORWARD: targetType = "Go Forward"; break;
			case DonCorleoneAction.BACKWARD: targetType = "Go Backwards"; break;
			case DonCorleoneAction.STOP: targetType = "Stop"; break;
			case DonCorleoneAction.TURN_CANNON_LEFT: targetType = "Turn Cannon Left"; break;
			case DonCorleoneAction.TURN_CANNON_RIGHT: targetType = "Turn Cannon Right"; break;
			case DonCorleoneAction.TURN_RADAR_LEFT: targetType = "Turn Radar Left"; break;
			case DonCorleoneAction.TURN_RADAR_RIGHT: targetType = "Turn Radar Right"; break;
	        case DonCorleoneAction.TURN_TANK_LEFT: targetType = "Turn Tank Left"; break;
	        case DonCorleoneAction.TURN_TANK_RIGHT: targetType = "Turn Tank Right"; break;
		}
		
		return "Action[type: " + targetType + "| parameter: " + parameter + "| priority: " + priority + "]"; 
	}
}
