package main;
public class Vector {
	double x, y;
	
	public Vector(double nx, double ny) {
		x = nx;
		y = ny;
	}
	
	public double getLength() {
		return Math.sqrt((Math.pow(x, 2)+Math.pow(y, 2)));
	}
	
	public void scale(double s) {
		x = x*s;
		y = y*s;
	}
	
	public void rotate(double r) {
		double a = (r/180)*Math.PI;
		x = x*Math.cos(a)-y*Math.sin(a);
		y  = y*Math.cos(a)+x*Math.sin(a);
	}
	
	public Vector getRotate(double r) {
		double a = (r/180)*Math.PI;
		return new Vector(x*Math.cos(a)-y*Math.sin(a), y*Math.cos(a)+x*Math.sin(a));
	}
	
	public void scaleTo(double l) {
		double s = l/getLength();
		scale(s);
	}
	public Vector scaleToV(double l) {
		double s = l/getLength();
		scale(s);
		//System.out.println(getLength());
		return new Vector(x,y);
	}
	
	public Vector sub(Vector v) {
		return new Vector(x-v.x, y-v.y);
	}
	public Vector add(Vector v) {
		return new Vector(x+v.x, y+v.y);
	}
	

	public Vector rotate_rad(double a) {
		return new Vector((x*Math.cos(a))-(y*Math.sin(a)),(y*Math.cos(a))+(x*Math.sin(a)));

	}
	public double getRadian() {
		double radian ;
		Vector s = this.scaleToV(1);
		radian = Math.asin(s.y);
		if (s.x < 0) {
			radian=Math.PI-radian;
		}
		if (radian < 0) radian+=Math.PI*2;
		return radian;
	}
	
	public double getDotProduct(Vector v) {
		return v.x*this.x+v.y*this.y;
	}
	
	
	public void print() {
		System.out.println(this.x);
		System.out.println(this.y);
	}
	
	public double getSquaredDistance() {
		return Math.pow(x, 2)+Math.pow(y, 2);
	}
	
	public double getDifference(Vector v) {
		double difference = this.getRadian()-v.getRadian();
		while (difference > 2*Math.PI){
			difference = difference - 2*Math.PI;
		}while (difference < 0){
			difference = difference + 2*Math.PI;
		}		
		return difference;
	}
	
	public Vector scaleComponents(double s) {
		return new Vector(this.x*s,this.y*s);
	}
	
	public Vector addResolved(double s, double a) {
		//new Vector(s*Math.cos(a),s*Math.sin(a)).print();
		return this.add(new Vector(s*Math.cos(a),s*Math.sin(a)));
	}

}
