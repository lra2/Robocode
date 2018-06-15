package OIEP;

public class Info implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private double d = 0.0;
	private double e = 0.0;
	private double x = 0.0;
	private double y = 0.0;
	private double v = 0.0;
	private double h = 0.0;
	private double hr = 0.0;
	
	public Info(double d, double e,double x, double y, double h, double v, double hr) {
		this.d = d;		// Distance
		this.e = e;		// Energy
		this.x = x;
		this.y = y;
		this.h = h;		// Heading
		this.v = v;		// Velocity
		this.hr = hr;	// Heading in radians
	}

	public double getDistance() {
		return d;
	}

	public double getEnergy() {
		return e;
	}
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getVelocity() {
		return v;
	}

	public double getHeading() {
		return h;
	}

	public double getHedianRadians() {
		return hr;
	} 
}