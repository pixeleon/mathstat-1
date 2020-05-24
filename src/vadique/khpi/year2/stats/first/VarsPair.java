package vadique.khpi.year2.stats.first;

public class VarsPair {
	double x;
	double y;
	
	public VarsPair() {
	}

	public VarsPair(double x, double y) {
		this.x = x;
		this.y = y;
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
	
	public void setXY(double x, double y) {
		this.x = x;
		this.y = y;
	}
}
