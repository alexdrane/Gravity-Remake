package main;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class Screen extends JPanel implements MouseMotionListener,KeyListener, MouseWheelListener{
	
	private static final long serialVersionUID = -616365487435413161L;
	static int W = 1800, H = 900;
	static JFrame frame;
	int x,y;
	boolean frame1 = true;
	BodyCollection world = new BodyCollection(20);
	Body b1 = new Body(new Vector(149000000,0),new Vector(0,30),5.9*Math.pow(10, 24),6300);
	Body b2 = new Body(new Vector(0,0),new Vector(0,0),1.9*Math.pow(10,27),6960000);
	Body b3 = new Body(new Vector((149000000-6300-390000),0),new Vector(0,0),7.3*Math.pow(10, 22),1700);
	/*
	 * Body b4 = new Body(new Vector(10000000,-14000000),new
	 * Vector(7000,0),7.6*Math.pow(10, 24),56000); Body b5 = new Body(new
	 * Vector(10000000,-10000000),new Vector(-5000,0),2.5*Math.pow(10, 23),26000);
	 * Body b6 = new Body(new Vector(10000000,-9700000),new
	 * Vector(3000,0),3.7*Math.pow(10, 22),5400);
	 */
	Ship ship = new Ship(new Vector(149000000+6300+0.1,0), new Vector(0,0), 100, 0.01, 0, 80);
	Body[] bodies = new Body[] {b1,b2,b3};
	boolean count = true;
	public Screen() {
		setSize(new Dimension(W,H));
		setPreferredSize(new Dimension(W,H));
		addMouseMotionListener((MouseMotionListener) this);
		addKeyListener((KeyListener) this);
		addMouseWheelListener((MouseWheelListener) this);
		setFocusable(true);
	}
	public static void main(String[] args) {	
		Screen screen = new Screen();	
		frame = new JFrame("My Drawing");
		frame.add(screen);
		frame.pack();
    	frame.setVisible(true);
	};
	
	public void paint(Graphics g) {
		if (frame1) {
			world.passbodies(bodies);
		}
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, W, H);
		//b1.velocity.print();
		//b1.accelerate(b1.getForce(bodies, 0), 1);
		//b2.accelerate(b2.getForce(bodies, 1), 1);
		//b1.moveByTimestep(1);
		//b2.moveByTimestep(1);
		//b1.render(g,scale, camera_pos);
		//b2.render(g,scale, camera_pos);
		for (int i = 0; i < 100; i++) {
			world.update(0.0001);
			ship.update(0.0001,world.bodies);
		}
		//ship.velocity.print(); 
		//world.bodies[0].velocity.print();
		//System.out.println();
		 
		world.camera_track(ship.pos, W, H);
		world.render(g);
		ship.render(g, world.scale, world.camera_pos,world.bodies);
		repaint();
	}
	@Override
	public void mouseDragged(MouseEvent e) {
		
		
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		x =  e.getXOnScreen();
		y = e.getYOnScreen();
	}
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
		int n = e.getWheelRotation();
		world.cameraZoom(n, x, y);
	}
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_W||key == KeyEvent.VK_SPACE){
	        ship.burn();
	    }
	    if(key == KeyEvent.VK_A){
	        ship.turn(-0.0001);
	    }
	    if(key == KeyEvent.VK_D){
	    	ship.turn(0.0001);
	    }
}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_W||key == KeyEvent.VK_SPACE){
	        ship.stop_burn();
	    }
	    if(key == KeyEvent.VK_A){
	        ship.turn(0);
	    }
	    if(key == KeyEvent.VK_D){
	    	ship.turn(0);
	    }
	}
	
	
}
