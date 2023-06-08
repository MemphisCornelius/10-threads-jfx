package ohm.softa.a10.kitchen;

import ohm.softa.a10.model.Dish;
import ohm.softa.a10.model.Order;

import java.util.Deque;
import java.util.LinkedList;

public class KitchenHatchImpl implements KitchenHatch {

	private final int maxMeals;
	private final Deque<Order> orders;
	private final Deque<Dish> dishes;

	public KitchenHatchImpl(int maxMeals, Deque<Order> orders) {
		this.maxMeals = maxMeals;
		this.orders = orders;
		this.dishes = new LinkedList<>();
	}

	@Override
	public int getMaxDishes() {
		return maxMeals;
	}

	@Override
	public Order dequeueOrder(long timeout) {
		Order order;
		synchronized (orders) {
			order = orders.pop();
		}
		return order;
	}

	@Override
	public int getOrderCount() {
		return orders.size();
	}

	@Override
	public Dish dequeueDish(long timeout) {
		Dish dish;
		synchronized (dishes) {
			while(getDishesCount() == 0) {
				try {
					dishes.wait(timeout);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}
			dish = dishes.pop();
			dishes.notifyAll();
		}
		return dish;
	}

	@Override
	public void enqueueDish(Dish m) {
		synchronized (dishes) {
			while (getDishesCount() == maxMeals) {
				try {
					dishes.wait();
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			}

			dishes.add(m);
			dishes.notifyAll();
		}
	}

	@Override
	public int getDishesCount() {
		return dishes.size();
	}
}
