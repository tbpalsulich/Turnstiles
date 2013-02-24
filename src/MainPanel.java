import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Timer;

@SuppressWarnings("serial")
public class MainPanel extends javax.swing.JPanel implements KeyListener, ActionListener {
	public static MainPanel sharedPanel;
	Timer timer;
	ArrayList<Person> people;
	ArrayList<Person> exitPeople;
	ArrayList<Point> destin;
	ArrayList<Turnstile> turnstiles;
	ArrayList<Integer> enterTimes;
	ArrayList<Integer> exitTimes;

	boolean trails;


	int enterTime=25;
	int enterSize=1;
	int exitTime=300;
	int exitSize=20;
	int time;


	boolean testing=false;
	int testingTime=10000;

	public MainPanel() {
		this.setBackground(Color.gray);
		setBorder(BorderFactory.createLineBorder(Color.gray));
		this.setFocusable(true);
		this.addKeyListener(this);
		this.setDoubleBuffered(true);


		enterTimes = new ArrayList<Integer>();
		exitTimes = new ArrayList<Integer>();
		people=new ArrayList<Person>();
		turnstiles=new ArrayList<Turnstile>();
		trails=false;

		for(int x=0;x<7;x++){
			turnstiles.add(new Turnstile(x));
		}

		for(int x=0;x<30;x++){
			Person p = new EnterPerson();
			people.add(p);
			turnstiles.get(getClosest(p)).addPerson(p);
		}
		for(int x=0;x<30;x++){
			Person p = new ExitPerson();
			people.add(p);
			turnstiles.get(getClosest(p)).addPerson(p);
		}		
		int interval=30;
		if(testing){
			interval=0;
		}
		sharedPanel=this;

			timer = new Timer(interval, this);
			timer.start();
	}

	public static MainPanel getSharedPanel(){
		return sharedPanel;
	}

	public Point getDestin(){
		int index=(int)(Math.random()*destin.size());
		return destin.get(index);
	}

	public void actionPerformed(ActionEvent e) {
		sort();

		time++;
		if(time%enterTime==0){
			for(int x=0;x<enterSize;x++){
				Person p = new EnterPerson();
				people.add(p);
				turnstiles.get(getClosest(p)).addPerson(p);
			}
		}
		if(time%exitTime==0){
			for(int x=0;x<exitSize;x++){
				Person p = new ExitPerson();
				people.add(p);
				turnstiles.get(getClosest(p)).addPerson(p);
			}
		}

		for(Turnstile t: turnstiles){
			t.update();
		}
		for(Person p : people ){
			p.update();

		}
		
		for(int x=0;x<people.size();x++){
			int y=people.get(x).getY();
			if(people.get(x).isEnter()&&y<-300){
				people.remove(x);
				x=0;
			}
			else if(y>1400){
				people.remove(x);
				x=0;
			}
		}

		if(!testing){
			repaint();
		}
		else{
			if(testingTime<time){
				timer.stop();
				Toolkit.getDefaultToolkit().beep();
				System.out.println("Done! Test ran for "+time+" cycles");
				System.out.println("EnterTime:"+enterTime+" EnterSize:"+enterSize);
				System.out.println("ExitTime:"+exitTime+" ExitSize:"+exitSize);
				System.out.println("Average Enter Time:"+averageTime(enterTimes));
				System.out.println("Average Exit Time:"+averageTime(exitTimes));


			}
			else{
				if(time%1000==0){
					System.out.println((double)time*100/(double)testingTime+"%");
				}
			}
		}
	}

	public void sort(){
		for(int a=0;a<turnstiles.size();a++){
			System.out.print(turnstiles.get(a).passingExitScore()+" ");
			System.out.print(turnstiles.get(a).exitPassing+"     ");

		}
		System.out.println();
		
		for(int b=0;b<turnstiles.size();b++){
			System.out.print(turnstiles.get(b).passingEnterScore()+" ");
			System.out.print(turnstiles.get(b).enterPassing+"      ");

		}
		System.out.println();
		for(int c=0;c<turnstiles.size();c++){
			//System.out.print(turnstiles.get(b).passingEnterScore()+" ");
			System.out.print(turnstiles.get(c).swiping+" ");

		}
		System.out.println();
		System.out.println();
		for(int x=0;x<turnstiles.size();x++){
			if(turnstiles.get(x).lastSwiping() || turnstiles.get(x).lastExitSwiping()){continue;}
			int currentEnter=turnstiles.get(x).passingEnterScore();
			int currentExit=turnstiles.get(x).passingExitScore();

			int enterLeft = x>0?turnstiles.get(x-1).passingEnterScore():1000;
			int enterRight = x<(turnstiles.size()-1)?turnstiles.get(x+1).passingEnterScore():1000;

			int exitLeft = x>0?turnstiles.get(x-1).passingExitScore():1000;
			int exitRight = x<(turnstiles.size()-1)?turnstiles.get(x+1).passingExitScore():1000;

			//System.out.println("Current: "+current+" Left: "+left+" Right: "+right);

			if(enterLeft<enterRight){
				if(currentEnter-enterLeft>1&&!turnstiles.get(x-1).isExiting()){
					if(!turnstiles.get(x).isExiting()&&currentEnter<2){
						continue;
					}
					Person p = turnstiles.get(x).removeLastInLine();
					if(p!=null){
						turnstiles.get(x-1).addPerson(p);
					}
				}
			}
			else if (enterRight<=enterLeft){
				if(currentEnter-enterRight>1&&!turnstiles.get(x+1).isExiting()){
					if(!turnstiles.get(x).isExiting()&&currentEnter<2){
						continue;
					}
					Person p = turnstiles.get(x).removeLastInLine();
					if(p!=null){
						turnstiles.get(x+1).addPerson(p);
					}				
				}
			}

			if(exitLeft<exitRight){
				if(currentExit-exitLeft>1&&turnstiles.get(x-1).isExiting()){
					if(turnstiles.get(x).isExiting()&&currentExit<2){
						continue;
					}
					Person p = turnstiles.get(x).removeLastInExitLine();
					if(p!=null){
						turnstiles.get(x-1).addPerson(p);
					}
				}
			}
			else if (exitRight<=exitLeft&&x<turnstiles.size()){
				if(currentExit-exitRight>1&&turnstiles.get(x+1).isExiting()){
					if(turnstiles.get(x).isExiting()&&currentExit<2){
						continue;
					}
					Person p = turnstiles.get(x).removeLastInExitLine();
					if(p!=null){
						turnstiles.get(x+1).addPerson(p);
					}				
				}
			}
			
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(800, 800);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		for(int a=0;a<8;a++){
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(a*90+60, 320, 30, 100);

			if(a!=0){
				g.setColor(Color.DARK_GRAY);
				g.fillRect(a*90+70, 376, 10, 20);
			}
		}

		for(int x=0;x<turnstiles.size();x++){
			if(turnstiles.get(x).canPass()){
				g.setColor(Color.green);
			}
			else{
				g.setColor(Color.red);
			}
			g.fillOval(x*90+159, 403, 12, 12);
		}
		if(trails){
			for(int x=0;x<people.size();x++){
				if(people.get(x).isEnter()){
					g.setColor(Color.green);
				}
				else
				{
					g.setColor(Color.red);
				}
				people.get(x).drawTrail(g);
			}
		}


		for(int x=0;x<people.size();x++){
			g.setColor(Color.yellow);
			g.fillPolygon(people.get(x).getCard());

			if(people.get(x).isEnter()){
				g.setColor(new Color(0,100,0));
			}
			else{
				g.setColor(new Color(100,0,0));

			}
			g.fillOval(people.get(x).getX(), people.get(x).getY(), 30, 30);

		}

	}

	public int getClosest(Person person){
		Point p = person.position;
		double a[]=new double[turnstiles.size()];
		for(int x=0;x<turnstiles.size();x++){

			Point d;
			if(person.isEnter()){
				d=turnstiles.get(x).getEnterPoint();
			}
			else{
				d=turnstiles.get(x).getExitPoint();
			}

			a[x]=(Math.sqrt(Math.pow((d.x-p.x), 2)+Math.pow((d.y-p.y), 2))/30);

			if(person.isEnter()){
				a[x]+=turnstiles.get(x).passingEnterScore();
			}
			else{
				a[x]+=turnstiles.get(x).passingExitScore();
			}
			//			System.out.println("D: "+d+" P: "+p+" Distance: "+a[x]);

		}
		int index=0;
		double min=3000;
		for(int x=0;x<turnstiles.size();x++){
			if(a[x]<min){
				min=a[x];
				index=x;
			}
		}
		return index;
	}
	
	public void addEnterTime(int time){
		Integer t=new Integer(time);
		enterTimes.add(t);
	}
	
	public void addExitTime(int time){
		Integer t=new Integer(time);
		exitTimes.add(t);
	}
	
	public int averageTime(ArrayList<Integer> times){
		int total=0;
		for(int x=0;x<times.size();x++){
			total+=times.get(x).intValue();
		}
		total=total/times.size();
		return total;
	}


	public void keyPressed(KeyEvent e) {
		int i = e.getKeyCode();
		if(i==KeyEvent.VK_T){
			trails=!trails;
		}
		else if(i==KeyEvent.VK_SPACE){
			if(timer.isRunning()){
				timer.stop();
			}
			else{
				timer.start();
			}
		}
	}

	public void keyReleased(KeyEvent e) {
		e.getKeyCode();	
	}


	public void keyTyped(KeyEvent e) {
	}

}
