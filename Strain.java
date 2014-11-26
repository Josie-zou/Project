package practice11;

public class Strain implements Runnable {

	public Tunnel tunnel;
	public String string;
	public Strain(Tunnel tunnel,String string) {
		// TODO Auto-generated constructor stub
		this.tunnel = tunnel;
		this.string = string;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub

		try {
			tunnel.run(string);
			

		} catch (Exception e) {
			// TODO: handle exception
		}
	}


}
