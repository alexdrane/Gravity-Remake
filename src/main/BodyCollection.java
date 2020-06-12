package main;
import java.awt.Color;
import java.awt.Graphics;

public class BodyCollection {
	int number_of_bodies;
	Body[] bodies;
	int r = 4;
	double scale = 10;
	Vector camera_pos = new Vector(0,0);
	boolean tracking = false;
	Vector[][] positions;
	public BodyCollection(int n) {
		this.number_of_bodies = n;
		//this.generatebodies(1800, 900);
	}
	
	public void passbodies(Body[] b) {
		this.bodies = b;
		this.number_of_bodies = b.length;
	}
	
	public void generatebodies(int W,int H) {
		bodies = new Body[this.number_of_bodies];
		for (int i = 0;i<this.number_of_bodies;i++) {
			bodies[i] = new Body(new Vector((Math.random()*W*10000),(Math.random()*H*10000)),new Vector((Math.random()-0.5)*10000,(Math.random()-0.5)*10000),Math.pow(10, 24),100000);    //2*r+Math.random()*(W-4*r),2*r+Math.random()*(H-4*r)
		}//int r2 = 50;
		//bodies[this.number_of_bodies-1] = new Body(new Vector(600,550),new Vector((Math.random()-0.5)*10,(Math.random()-0.5)*10),100,r2);
	}
	
	public void render(Graphics g) {
		for (int i = 0;i<this.number_of_bodies;i++) {
			bodies[i].render(g, this.scale,this.camera_pos);
		}
	}
	
	public void camera_track(Vector pos,int W, int H) {
		this.camera_pos = pos.sub(new Vector(W*this.scale/2,H*this.scale/2));
		tracking = true;
	}
	
	
	public void startCalculating(double time,int W, int H) {
		this.positions = new Vector[(int) (time*60)][this.number_of_bodies];
		for (int i = 0; i<time*60;i++) {
			double t = 0;
			while (t<1.0/60.0) {
				double step = this.calculateSmalestTimestep();
				if (step>(1.0/60.0)-t) {
					step = (1.0/60.0)-t;
				};
				this.update(step);
				t+=step;
			}positions[i]=this.addPosition();
			System.out.println(100*i/(time*60));
		}
	}
	
	public Vector[] addPosition() {
		Vector[] posArr = new Vector[this.number_of_bodies];
		for (int i = 0;i<this.number_of_bodies;i++) {
			posArr[i] = bodies[i].pos;
		}return posArr;
	}
	
	public void renderRecorded(Graphics g, int count) {
		Vector[] posArr = this.positions[count%this.positions.length];
		double radius;
		for (int i = 0;i<this.number_of_bodies;i++) {
			radius = bodies[i].radius;
			g.setColor(Color.BLUE);
			g.fillOval((int)(posArr[i].x-radius), (int)(posArr[i].y-radius), (int)radius*2, (int)radius*2);
		}
	}
	
	public void update(double t) {
		for (int i = 0;i<this.number_of_bodies;i++) {
			bodies[i].accelerate(bodies[i].getForce(this.bodies, i),t);
			for (int j = 0;j<this.number_of_bodies;j++) {
				if (bodies[j].getCollided(bodies[i])&&!(i==j)){
					Vector difference = bodies[i].pos.sub(bodies[j].pos);
					double angle = difference.getRadian();
					Vector transformedVelocityJ = bodies[j].velocity.rotate_rad(-angle);
					Vector transformedVelocityI = bodies[i].velocity.rotate_rad(-angle);
					double Mj = bodies[j].mass;
					double Mi = bodies[i].mass;
					double Ujx = transformedVelocityJ.x;
					double Uix = transformedVelocityI.x;
					double Vjx = Ujx*(Mj-Mi)/(Mj+Mi)+(2*Uix*Mi)/(Mj+Mi);
					double Vix = Uix*(Mi-Mj)/(Mj+Mi)+(2*Ujx*Mj)/(Mj+Mi);
					Vector transformedNewVelocityJ = new Vector(Vjx,transformedVelocityJ.y);
					Vector transformedNewVelocityI = new Vector(Vix,transformedVelocityI.y);
					bodies[i].updateVelocity(transformedNewVelocityI.rotate_rad(angle));
					bodies[j].updateVelocity(transformedNewVelocityJ.rotate_rad(angle));
					//bodies[i].moveByTimestep(t);
					//bodies[j].moveByTimestep(t);
				}
			}
			bodies[i].moveByTimestep(t);
		}
	}
	
	public void cameraZoom(double s, double x, double y) {
		double x_p = x*this.scale;
		double y_p = y*this.scale;
		this.scale *= Math.pow(1.1,s);
		if (!this.tracking) {
			this.camera_pos=this.camera_pos.add(new Vector(x_p-x*scale,y_p-y*scale));
		}
	}
	
	public double calculateSmalestTimestep() {
		double greatestSpeed = 0;
		//double smallestRadius = 1000000;
		for (int i = 0;i<this.number_of_bodies;i++) {
			if (bodies[i].getSpeed()>greatestSpeed) {
				greatestSpeed = bodies[i].getSpeed();
			}//if (bodies[i].radius/2<smallestRadius) {
			//	smallestRadius = bodies[i].getSpeed();
			//}
		}
		//System.out.println(greatestSpeed);
		//System.out.println(2/greatestSpeed);
		return (r/2)/greatestSpeed;
	}
}
