package vortex.application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import vortex.support.AbstractComponent;

public class PickCounter extends AbstractComponent {
	private static final List<Integer> picks = Arrays.asList(42, 38, 41, 26, 13, 13, 26);
	private static final HashMap<Integer, Counter> counts = new HashMap<>();
	
	private static List<Counter> get(List<Counter> picked) {
		ArrayList<Counter> result = new ArrayList<>();
		for (int i = 1; i <= 45; ++i) {
			result.add(counts.computeIfAbsent(i, key -> new Counter().setNumber(key)));
		}
		return result;
	}
	
	public static void main(String[] args) {
		List<List<Counter>> result = new ArrayList<List<Counter>>();
		for (int i: picks) {
			counts.computeIfAbsent(i, key -> new Counter().setNumber(i)).increment();
		}
		counts.values().forEach(System.out::println);
	}

	private static class Counter {
		private int
			number,
			count;

		public int getNumber() {
			return number;
		}

		public Counter setNumber(int number) {
			this.number = number;
			return this;
		}

		public int count() {
			return count;
		}

		public Counter increment() {
			++count;
			return this;
		}
		
		@Override
		public String toString() {
			return number + ": " + count;
		}
	}
}
