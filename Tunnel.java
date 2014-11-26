package practice11;

public class Tunnel {
	public synchronized void run(String string) throws InterruptedException
	{
		System.out.println(string+"is running");
		Thread.sleep(100);
		System.out.println(string+"has leaving");
		this.notify();
	}
	
}
