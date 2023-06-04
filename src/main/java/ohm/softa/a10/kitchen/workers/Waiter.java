package ohm.softa.a10.kitchen.workers;

import ohm.softa.a10.internals.displaying.ProgressReporter;
import ohm.softa.a10.kitchen.KitchenHatch;
import ohm.softa.a10.model.Dish;

import java.util.Random;
import java.util.stream.IntStream;

public class Waiter implements Runnable {

	private String name;
	private ProgressReporter progressReporter;
	private KitchenHatch kitchenHatch;

	private Random random;


	public Waiter(String name, ProgressReporter progressReporter, KitchenHatch kitchenHatch) {
		this.name = name;
		this.progressReporter = progressReporter;
		this.kitchenHatch = kitchenHatch;
		this.random = new Random();
	}


	@Override
	public void run() {
		while (kitchenHatch.getOrderCount() > 0 || kitchenHatch.getDishesCount() > 0) {
			Dish dish = kitchenHatch.dequeueDish();
			try {
				Thread.sleep(random.nextInt(1000));
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			progressReporter.updateProgress();
		}
		progressReporter.notifyWaiterLeaving();
	}
}
