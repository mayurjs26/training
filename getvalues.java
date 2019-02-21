package practice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Test {

	public static void main(String[] args) {
		HashMap<Integer, Integer> map = new HashMap<>();
		map.put(1, 10);
		map.put(2, 100);
		map.put(3, 20);
		
		
		List<Integer> sortedValues= new ArrayList<> (getValues(map));
		
		for (Integer integer : sortedValues) {
			System.out.println(integer);
		}
		

	}
	
	public static List getValues(HashMap map) {
		List<Integer> values= new ArrayList<> (map.values());
		Collections.sort(values);
		
		return values;
	}

}
