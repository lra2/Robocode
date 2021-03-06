package OIEP;

import robocode.TeamRobot;

public class CapangaAction {
	private int type;
	private double parameter;
	private int priority;
	
	private TeamRobot capanga;
	
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
	
	public CapangaAction() {
	}
	
	public CapangaAction(int type, double parameter, int priority) {
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
		if(this.capanga != null) {
			switch(this.type) {
				case CapangaAction.SHOOT: capanga.setFire(parameter); break;
				case CapangaAction.FORWARD: capanga.setAhead(parameter); break;
				case CapangaAction.BACKWARD: capanga.setBack(parameter); break;
				case CapangaAction.STOP: capanga.setStop(); break;
				case CapangaAction.TURN_CANNON_LEFT: capanga.setTurnGunLeft(parameter); break;
				case CapangaAction.TURN_CANNON_RIGHT: capanga.setTurnGunRight(parameter); break;
				case CapangaAction.TURN_RADAR_LEFT: capanga.setTurnRadarLeft(parameter); break;
				case CapangaAction.TURN_RADAR_RIGHT: capanga.setTurnRadarRight(parameter); break;
                case CapangaAction.TURN_TANK_LEFT: capanga.setTurnLeft(parameter); break;
                case CapangaAction.TURN_TANK_RIGHT: capanga.setTurnRight(parameter); break;
			}
		}
	}
	
	void setRobot(TeamRobot capanga) {
		this.capanga = capanga;
	}
	
	public String toString() {
		String targetType = "";
		
		switch(this.type) {
			case CapangaAction.SHOOT: targetType = "Shoot"; break;
			case CapangaAction.FORWARD: targetType = "Go Forward"; break;
			case CapangaAction.BACKWARD: targetType = "Go Backwards"; break;
			case CapangaAction.STOP: targetType = "Stop"; break;
			case CapangaAction.TURN_CANNON_LEFT: targetType = "Turn Cannon Left"; break;
			case CapangaAction.TURN_CANNON_RIGHT: targetType = "Turn Cannon Right"; break;
			case CapangaAction.TURN_RADAR_LEFT: targetType = "Turn Radar Left"; break;
			case CapangaAction.TURN_RADAR_RIGHT: targetType = "Turn Radar Right"; break;
	        case CapangaAction.TURN_TANK_LEFT: targetType = "Turn Tank Left"; break;
	        case CapangaAction.TURN_TANK_RIGHT: targetType = "Turn Tank Right"; break;
		}
		
		return "Action[type: " + targetType + "| parameter: " + parameter + "| priority: " + priority + "]"; 
	}
}