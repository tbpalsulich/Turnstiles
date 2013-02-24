import java.awt.BasicStroke;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.util.ArrayList;

public abstract class Person {
	Point position;
	Point destin;
	int dx;
	int dy;	
	boolean passing=false;
	int passingTime;
	boolean swiping;
	ArrayList<Point> trail;
	int time;
	
	public abstract void start();
	public abstract boolean isEnter();
	public abstract Polygon getCard();
	
	public Person(){
		trail = new ArrayList<Point>();
	}
	
	public void update(){
		time++;
		if(destin!=null&&position!=destin){
			if(destin.x>position.x+5){
				dx=5;
			}
			else if(destin.x<position.x-5){
				dx=-5;
			}
			else{
				dx=destin.x-position.x;
			}
			if(destin.y>position.y+5){
				dy=5;
			}
			else if(destin.y<position.y-5){
				dy=-5;
			}
			else{
				dy=destin.y-position.y;
			}
			position.x+=dx;
			position.y+=dy;
		}
		trail.add(new Point(position.x+15,position.y+15));
		if(trail.size()>10){
			trail.remove(0);
		}
	}

	
	int getX(){
		return position.x;
	}
	int getY(){
		return position.y;
	}
	
	public boolean atDestin(){
	//	System.out.println(position +" " +destin);
		if(destin==null){return false;}
		return (Math.abs(position.x-destin.x)<50&&Math.abs(position.y-destin.y)<50);
	}
	
	public boolean atExactDestin(){
	//	System.out.println(position +" " +destin);
		if(destin==null){return false;}
		return (Math.abs(position.x-destin.x)<2&&Math.abs(position.y-destin.y)<2);
	}
	
	public void drawTrail(Graphics g){
		Graphics2D g2D = (Graphics2D) g;
		g2D.setStroke(new BasicStroke(10F));
		//Êg2D.drawOval(200, 100, 100, 50);
		for(int x=0;x<30;x++){
			if(x+2>=trail.size()){
				return;
			}
			Point p1=trail.get(x);
			Point p2=trail.get(x+2);
			//System.out.println("Point 1:"+p1+" Point 2:"+p2);

			g2D.drawLine(p1.x, p1.y, p2.x, p2.y);
		}
	}
	
}
