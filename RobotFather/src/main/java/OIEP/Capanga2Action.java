package OIEP;

import robocode.TeamRobot;

public class Capanga2Action {
	private int type;
	private double parameter;
	private int priority;
	
	private TeamRobot capanga2;
	
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
	
	public Capanga2Action() {
	}
	
	public Capanga2Action(int type, double parameter, int priority) {
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
		if(this.capanga2 != null) {
			switch(this.type) {
				case Capanga2Action.SHOOT: capanga2.setFire(parameter); break;
				case Capanga2Action.FORWARD: capanga2.setAhead(parameter); break;
				case Capanga2Action.BACKWARD: capanga2.setBack(parameter); break;
				case Capanga2Action.STOP: capanga2.setStop(); break;
				case Capanga2Action.TURN_CANNON_LEFT: capanga2.setTurnGunLeft(parameter); break;
				case Capanga2Action.TURN_CANNON_RIGHT: capanga2.setTurnGunRight(parameter); break;
				case Capanga2Action.TURN_RADAR_LEFT: capanga2.setTurnRadarLeft(parameter); break;
				case Capanga2Action.TURN_RADAR_RIGHT: capanga2.setTurnRadarRight(parameter); break;
                case Capanga2Action.TURN_TANK_LEFT: capanga2.setTurnLeft(parameter); break;
                case Capanga2Action.TURN_TANK_RIGHT: capanga2.setTurnRight(parameter); break;
			}
		}
	}
	
	void setRobot(TeamRobot capanga2) {
		this.capanga2 = capanga2;
	}
	
	public String toString() {
		String targetType = "";
		
		switch(this.type) {
			case Capanga2Action.SHOOT: targetType = "Shoot"; break;
			case Capanga2Action.FORWARD: targetType = "Go Forward"; break;
			case Capanga2Action.BACKWARD: targetType = "Go Backwards"; break;
			case Capanga2Action.STOP: targetType = "Stop"; break;
			case Capanga2Action.TURN_CANNON_LEFT: targetType = "Turn Cannon Left"; break;
			case Capanga2Action.TURN_CANNON_RIGHT: targetType = "Turn Cannon Right"; break;
			case Capanga2Action.TURN_RADAR_LEFT: targetType = "Turn Radar Left"; break;
			case Capanga2Action.TURN_RADAR_RIGHT: targetType = "Turn Radar Right"; break;
	        case Capanga2Action.TURN_TANK_LEFT: targetType = "Turn Tank Left"; break;
	        case Capanga2Action.TURN_TANK_RIGHT: targetType = "Turn Tank Right"; break;
		}
		
		return "Action[type: " + targetType + "| parameter: " + parameter + "| priority: " + priority + "]"; 
	}
}