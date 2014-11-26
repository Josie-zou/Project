package practice11;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Tunnel tunnel = new Tunnel();
		
		Runnable leftstrain1 = new Strain(tunnel, "i am the First Leftstrain");
		Runnable leftstrain2 = new Strain(tunnel, "i am the Second Leftstrain");

		Runnable rightstrain1 = new Strain(tunnel, "i am the First Rightstrain");
		Runnable rightstrain2 = new Strain(tunnel, "i am the Second Rightstrain");

		Thread a1 = new Thread(leftstrain1);
		Thread b1 = new Thread(rightstrain1);
		Thread a2 = new Thread(leftstrain2);
		Thread b2 = new Thread(rightstrain2);
		
		a1.start();
		b1.start();
		a2.start();
		b2.start();



	}

}
