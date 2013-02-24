import java.awt.Point;
import java.awt.Polygon;

public class EnterPerson extends Person {

	int swipeProgress;
	int moveProgress;

	public EnterPerson(){
		super();
		position = new Point((int)(Math.random()*800),1000+(int)(Math.random()*100));
		destin = position;
		swiping=false;
		moveProgress=0;
	}
	
	public boolean isEnter(){
		return true;
	}

	public void update(){
		super.update();
		
		if(swiping){
			moveProgress++;
			if(moveProgress>10){
				swipeProgress++;
			}
			else{
				//System.out.println("Move "+moveProgress+" Swipe "+swipeProgress+" Destin");
				position.y-=5;

			}
		}	
		if(swipeProgress>55){
			if(passing||(int)(Math.random()*5)!=0){
				destin=null;
				passing=true;
				swiping=false;
				MainPanel.getSharedPanel().addEnterTime(time);
				swipeProgress=0;
			}
			else{
				swiping=false;
				start();
				moveProgress=20;
			}
		}
		if(passing){
			destin=null;
			passingTime++;
			position.y-=4;
			if(passingTime>39){
				position.y-=2;
			}
			else if(passingTime>29){
				position.x+=4;
			}
		}
	}

	public void start(){
		if(swiping){return;}
		swiping=true;
		swipeProgress=0;
		moveProgress=0;
		destin=null;
	}


	public Polygon getCard(){
		if(!swiping){ return new Polygon();}
		int xOff=20;
		int yOff=-10;
		if(swipeProgress<25){
			xOff+=swipeProgress;
			yOff+=2*swipeProgress;
		}
		else if(swipeProgress<40){
			xOff+=25;
			yOff+=50-(swipeProgress*3-75);
		}
		else if(swipeProgress<55){
			xOff+=25-(swipeProgress*2-80);
			yOff+=5;
		}
		else{

		}

		int xp[]={position.x+xOff,position.x+xOff+8,position.x+xOff+8,position.x+xOff};
		int yp[]={position.y-yOff,position.y-yOff,position.y-yOff+12,position.y-yOff+12};

		return new Polygon(xp,yp,4);

	}
}
