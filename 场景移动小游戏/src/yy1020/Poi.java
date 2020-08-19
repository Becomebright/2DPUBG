package yy1020;

import static java.lang.Math.sqrt;

/**
 * Created by Howard on 2017/12/29.
 */
public class Poi {
	private int kind = 3;
    public double x, y;
    
    public Poi() {
        x = y = 0.0;
    }
    public Poi(double a, double b) {
        x = a;
        y = b;
    }
    
    
    public int getKind() {
		return kind;
	}
	public void setKind(int kind) {
		this.kind = kind;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	
	public double dis(Poi a) {
        return sqrt(sqr(a.x - x) + sqr(a.y - y));
    }

    private double sqr(double v) {
        return v * v;
    }
}
