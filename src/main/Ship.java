package main;

import java.awt.Color;
import java.awt.Graphics;

@SuppressWarnings("unused")
public class Ship extends Body{
	double orientation,thrust;
	boolean accelerating = false, landed = false;
	double rotation;
	public Ship(Vector p, Vector v, double m, double r,double o, double t) {
		super(p, v, m, r);
		this.orientation = o;
		this.thrust = t;
	}
	
	public void turn(double r) {
		this.rotation = r;
	}
	
	
	public void burn() {
		this.accelerating = true;
	}
	
	public void stop_burn() {
		this.accelerating = false;
	}
	
	public void update(double t,Body[] bodies){
		//;
		this.orientation+=this.rotation;
		this.orientation%=(2*Math.PI);
		this.landed = false;
		for (int i = 0;i<bodies.length;i++) {
			if (this.getCollided(bodies[i])) {
				this.velocity = bodies[i].velocity;
				//System.out.println("Hit");
				this.landed = true;
				if (this.velocity.sub(bodies[i].velocity).getLength()<1000) {
				}
			}
		}
		if (!this.landed) {
			this.accelerate(this.getForce(bodies, -1), t);
		}
		if (this.accelerating) {
			this.accelerate(new Vector(thrust*Math.cos(this.orientation),thrust*Math.sin(this.orientation)), t);
		}
		this.moveByTimestep(t);
	}
	
	public Ship clone() {
		return new Ship(this.pos,this.velocity,this.mass,this.radius,this.orientation,this.thrust);
	}
	
	public int getOrbitingIdx(Body[] bodies) {
		double G = 6.75 * Math.pow(10,-11);
		double max_g = 0;
		int max_idx = -1;
		for (int i = 0;i<bodies.length;i++) {
			double acceleration_magnitude = (G*bodies[i].mass)/(this.pos.sub(bodies[i].pos).getSquaredDistance()*1000000);
			double acceleration_angle = bodies[i].pos.sub(this.pos).getRadian();
			Vector g = this.getResolved(acceleration_magnitude, acceleration_angle);
			if (g.getLength()>max_g) {
				max_g=g.getLength();
				max_idx = i;
			}
		}return max_idx;
	}
	
	public double[][][] calculateTrajectory(int steps, Body[] bodies, double t) {
		Ship prediction = this.clone(); 
		BodyCollection world_pred = new BodyCollection(bodies.length);
		Body[] temp = new Body[bodies.length];
		//int idx = this.getOrbitingIdx(bodies);
		for (int i = 0;i<bodies.length;i++) {
			temp[i] = bodies[i].clone();
		}
		world_pred.passbodies(temp);
		double[][] xPos = new double[steps][bodies.length];
		double[][] yPos = new double[steps][bodies.length];
		for (int i = 0; i < steps;i++) {
			for (int j = 0; j < bodies.length;j++) {
				xPos[i][j] = prediction.pos.sub(world_pred.bodies[j].pos).add(bodies[j].pos).x;
				yPos[i][j] = prediction.pos.sub(world_pred.bodies[j].pos).add(bodies[j].pos).y;
			}
			prediction.update(t, world_pred.bodies);
			world_pred.update(t);
		}
		return new double[][][] {xPos,yPos};
	}
	
	
	public void render(Graphics g,double scale,Vector camera_pos,Body[] bodies) {
		//System.out.println("Hit");
		super.render(g, scale, camera_pos);
		int n = 10000;
		double[][][] coords = this.calculateTrajectory(n, bodies, 1);
		int[][] processedXCoords = new int[bodies.length][n];
		int[][] processedYCoords = new int[bodies.length][n];
		//System.out.println(coords[1][0]);
		//System.out.println(this.pos.y);
		for (int i = 0; i < n;i++) {
			for (int j = 0; j < bodies.length;j++) {
				processedXCoords[j][i] = (int)((coords[0][i][j]-camera_pos.x)/scale);
				processedYCoords[j][i] = (int)((coords[1][i][j]-camera_pos.y)/scale);
			}
		}
		g.setColor(Color.GREEN);
		g.drawLine((int)((this.pos.x-camera_pos.x)/scale), (int)((this.pos.y-camera_pos.y)/scale), (int)((this.pos.x-camera_pos.x)/scale+100*Math.cos(this.orientation)),(int)((this.pos.y-camera_pos.y)/scale+100*Math.sin(this.orientation)));
		g.setColor(Color.CYAN);
		for (int j = 0; j < bodies.length;j++) {
			g.drawPolyline(processedXCoords[j], processedYCoords[j], n);
		}
		//System.out.println(processedXCoords[0]);
		//System.out.println(processedYCoords[0]);
	}
	
}
