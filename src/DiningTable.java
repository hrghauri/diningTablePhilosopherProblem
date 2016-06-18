/*
 * DiningPhilosopherProblem
 * Haris Ghauri
 * 18/06/2016
 */


public class DiningTable {

	private Fork[] forks;
	private Philosopher[] philosophers;
	private int numPhilosophers;

	public DiningTable(int n){
		numPhilosophers = n;
		forks = new Fork[numPhilosophers];
		for (int i = 0 ; i<numPhilosophers ; i++){
			forks[i] = new Fork(i);
		}

		philosophers = new Philosopher[numPhilosophers];
		for (int i = 0 ; i< numPhilosophers ; i++){
			philosophers[i] = new Philosopher(i,forks[i], forks[(i+1)%numPhilosophers]);
		}
	}


	public void start(){
		for (int i = 0; i < numPhilosophers; i++){
			new Thread(philosophers[i]).start();;
		}
	}


	private class Philosopher implements Runnable{

		Fork leftFork;
		Fork rightFork;
		int id;

		public Philosopher(int id, Fork leftFork, Fork rightFork){
			this.leftFork = leftFork;
			this.rightFork = rightFork;
			this.id = id;
			System.out.println("My id:"+id+" LeftForkId:"+leftFork.getId()+" RightForkId:"+rightFork.getId());
		}

		@Override
		public void run() {
			startEat();
			endEat();
		}

		public int getId(){
			return id;
		}

		public void startEat(){
			synchronized(leftFork){
				while(leftFork.isInUse()){
					try{
						leftFork.wait();
					}catch(InterruptedException e){
						return;
					}
				}
			}
			leftFork.pickMe();
			synchronized(rightFork){
				while(rightFork.isInUse()){
					try{
						rightFork.wait();
					}catch(InterruptedException e){
						return;
					}
				}
			}
			rightFork.pickMe();
		}


		public void endEat(){
			synchronized(leftFork){
			leftFork.dropMe();
			leftFork.notifyAll();}
			synchronized(rightFork){
			rightFork.dropMe();
			rightFork.notifyAll();}
			System.out.println("Philosopher:"+getId()+" done eating");
		}

	}	

	private class Fork{
		private boolean inUse;
		private int id;

		public Fork(int id){
			this.id = id;
		}

		public int getId(){
			return id;
		}

		public boolean isInUse() {
			return inUse;
		}

		public void pickMe(){
			inUse = true;
		}

		public void dropMe(){
			inUse = false;
		}
	}


	public static void main(String[] args){
		DiningTable table = new DiningTable(5);
		table.start();
	}




}

