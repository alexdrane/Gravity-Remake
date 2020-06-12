package main;
import java.awt.Color;
import java.awt.Graphics;

public class Body {
	double mass, radius;
	Vector pos, velocity;	
	public Body(Vector p, Vector v, double m, double r) {
		this.pos = p;
		this.velocity = v;
		this.mass = m;
		this.radius = r;
	}
	
	public double getSpeed() {
		return this.velocity.getLength();
	}
	
	public boolean getCollided(Body p) {
		return p.pos.sub(this.pos).getLength()<this.radius+p.radius;
	}
	
	public void moveByTimestep(double t) {
		this.pos = this.pos.add(this.velocity.scaleComponents(t));
	}
	
	public void accelerate(Vector a, double t) {
		this.velocity = this.velocity.add(a.scaleComponents(t));
	}
	
	
	public void updateVelocity(Vector newVelocity) {
		this.velocity=newVelocity;
	}
	
	public void render(Graphics g,double scale,Vector camera_pos) {
		//System.out.println("Hit");
		g.setColor(Color.LIGHT_GRAY);
		g.fillOval((int)(((this.pos.x-this.radius)-camera_pos.x)/scale), (int)(((this.pos.y-this.radius)-camera_pos.y)/scale), (int)(this.radius/scale)*2, (int)(this.radius/scale)*2);
	}
	public Body clone() {
		return new Body(this.pos,this.velocity,this.mass,this.radius);
	}
	
	public Vector getResolved(double s, double a) {
		//new Vector(s*Math.cos(a),s*Math.sin(a)).print();
		return new Vector(s*Math.cos(a),s*Math.sin(a));
	}
	
	public Vector getForce(Body[] bodies,int s_idx) {
		double G = 6.75 * Math.pow(10,-11);
		Vector net_acceleration = new Vector(0,0);
		for (int i = 0; i<bodies.length;i++) {
			if (!(i==s_idx)) {
				double acceleration_magnitude = (G*bodies[i].mass)/(this.pos.sub(bodies[i].pos).getSquaredDistance()*1000000);
				double acceleration_angle = bodies[i].pos.sub(this.pos).getRadian();
				//System.out.println(this.pos.sub(bodies[i].pos).getSquaredDistance());
				//
				//System.out.println(acceleration_angle);
				net_acceleration = net_acceleration.addResolved(acceleration_magnitude, acceleration_angle);
			}
		}
		//net_acceleration.print();
		return net_acceleration;
	}
}
