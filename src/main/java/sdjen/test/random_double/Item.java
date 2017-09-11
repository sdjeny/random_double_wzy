package sdjen.test.random_double;

import java.util.ArrayList;
import java.util.List;

public class Item {
	String title;
	List<String> items;
	int from;
	int to;

	public List<String> getValue() {
		List<String> aList = getClone();
		int size = Math.min(to + 1, aList.size());
		List<Integer> counts = new ArrayList<Integer>();
		for (int i = from; i < size; i++)
			counts.add(i);
		size = counts.get(((int) (Math.random() * 10000)) % counts.size());
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < size; i++) {
			result.add(random_one(aList));
		}
		return result;
	}

	public List<String> getClone() {
		List<String> result = new ArrayList<String>();
		for (String item : items)
			result.add(item);
		return result;
	}

	private static String random_one(List<String> list) {
		int index = ((int) (Math.random() * 10000)) % list.size();
		String result = list.get(index);
		list.remove(index);
		return result;
	}
}
