import java.awt.Point;
import java.util.ArrayList;


public class Turnstile {
	public ArrayList<Person> enterList;
	public ArrayList<Person> approach;
	public ArrayList<Person> exitList;
	public Point position;

	boolean swiping;
	boolean enterPassing;
	boolean exitPassing;
	boolean passingDirection;  //true=enter

	int passingTime;
	

	public Turnstile(int x){
		approach = new ArrayList<Person>();
		enterList = new ArrayList<Person>();
		exitList = new ArrayList<Person>();

		position=new Point(x*90+110,400);

		updatePosition();
		passingTime=0;
	}

	public void update(){
		for(int x=0;x<approach.size();x++){
			if(approach.get(x).atDestin()){
				if(approach.get(x).isEnter()){
					enterList.add(approach.remove(x));
					x--;
				}
				else{
					exitList.add(approach.remove(x));
					x--;
				}
				updatePosition();
			}
		}


		if(enterPassing||exitPassing){
			passingTime++;
			if(passingTime>20){
				if(passingDirection){
					enterPassing=false;
				}
				else{
					exitPassing=false;
				}
			}
			if(passingTime>29){
				exitPassing=false;
				enterPassing=false;
				passingTime=0;
			}
		}

		if(exitList.size()>0&&exitList.get(0).atExactDestin()&&!exitPassing&&!swiping){
			exitList.remove(0).start();
			updatePosition();
			exitPassing=true;
			enterPassing=true;
			passingDirection=false;
			passingTime=0;

		}

		if(enterList.size()==0){ 
			swiping=false;
			return; 
		}
		if(enterList.get(0).atExactDestin()&&!enterPassing){
			firstSwipe();
			swiping=true;
		}

		if(enterList.get(0).passing){
			swiping=false;
			exitPassing=true;
			enterPassing=true;
			passingDirection=true;
			passingTime=0;
			enterList.remove(0);
			updatePosition();

		}

	}
	public void firstSwipe(){
		enterList.get(0).start();
	}


	public void updatePosition(){
		for(int a=0;a<approach.size();a++){
			if(approach.get(a).isEnter()){
				approach.get(a).destin=getEnterPoint();
			}
			else{
				approach.get(a).destin=getExitPoint();
			}
		}
		
		for(int x=0;x<enterList.size();x++){
			if(x==0){
				if(!enterList.get(x).swiping){
					enterList.get(x).destin=new Point(position.x,position.y+x*40+60);
				}
			}
			else{
				enterList.get(x).destin=new Point(position.x+((int)(Math.random()*40)-20),position.y+x*40+60);
			}
		}

		for(int x=0;x<exitList.size();x++){
			if(x==0){
				exitList.get(x).destin=new Point(position.x,position.y-x*40-170);
			}
			else{
				exitList.get(x).destin=new Point(position.x+((int)(Math.random()*40)-20),position.y-x*40-170);
			}
		}
	}
	
	public boolean canPass(){
		return passingDirection&&enterPassing;
	}

	public Point getEnterPoint(){
		return new Point(position.x,position.y+enterList.size()*40);
	}

	public Point getExitPoint(){
		return new Point(position.x,position.y-exitList.size()*40-170);
	}

	public void addPerson(Person p){
		approach.add(p);
	}

	public void addPersonLane(Person p){
		enterList.add(p);
	}

	public void removePerson(Person p){
		approach.remove(p);
		enterList.remove(p);
		exitList.remove(p);
	}

	public int getLineSize(){
		return enterList.size();
	}
	
	public int getExitLaneSize(){
		return exitList.size();
	}

	public Person removeLastInLine(){
		if(enterList.size()<1){
			return null;
		}
		return enterList.remove(enterList.size()-1);
	}
	
	public Person removeLastInExitLine(){
		if(exitList.size()<1){
			return null;
		}
		return exitList.remove(exitList.size()-1);
	}

	public int passingEnterScore(){
		int score=0;//=(30-passingTime)/5;
		if(enterPassing){
			score=0;
		}
		else if(exitPassing){
			score=20;
		}
		score+=enterList.size();
		score+=exitList.size()*3;
		if(score<0){
			return 0;
		}
		return score;
	}
	
	public int passingExitScore(){
		int score=0;//=(30-passingTime)/5;
		if(exitPassing){
			score=0;
		}
		else if(enterPassing){
			score=20;
		}
		score+=exitList.size();
		score+=enterList.size()*5;
		if(score<0){
			return 0;
		}
		return score;
	}

	public boolean isExiting(){
		return exitPassing;
	}

	public boolean lastSwiping(){
		if(enterList.size()==0){
			return false;
		}
		return enterList.get(enterList.size()-1).swiping;
	}
	public boolean lastExitSwiping(){
		if(exitList.size()==0){
			return false;
		}
		return exitList.get(exitList.size()-1).swiping;
	}

}
