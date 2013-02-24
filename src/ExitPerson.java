import java.awt.Point;
import java.awt.Polygon;

public class ExitPerson extends Person {

	boolean passing=false;

	public ExitPerson(){
		super();
		position = new Point((int)(Math.random()*800),-200+(int)(Math.random()*100));
		destin = position;
	}
	
	public boolean isEnter(){
		return false;
	}
	
	public void update(){
		super.update();
		
		if(passing){
			destin=null;
			passingTime++;
			position.y+=4;
			if(passingTime>55){
				position.y+=2;

			}
			else if(passingTime>45){
				position.x+=4;
			}
		}
	}

	public void start(){
		passing=true;
		MainPanel.getSharedPanel().addExitTime(time);

	}

	public Polygon getCard(){
		return new Polygon();
	}
}
