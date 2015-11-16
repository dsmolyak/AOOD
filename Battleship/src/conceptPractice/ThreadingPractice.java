package conceptPractice;

public class ThreadingPractice implements Runnable {

	@Override
	public void run() {
		double[] array = new double[5];
		for (int i = 0; i < array.length; i++) {
			array[i] = Math.pow(2, i);
		}
		for (int i = 0; i < array.length; i++) {
			System.out.println(array[i]);
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		Thread t = new Thread(new ThreadingPractice());
		t.start();
		t.join();
		System.out.println("finished");
	}

	

}
