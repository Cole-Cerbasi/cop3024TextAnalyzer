package cop3024;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.junit.Test;

public class textAnalyzer_findTopTest {

	@Test
	public void test() {
		AGui analyze = new AGui();
		
		HashMap<String, Integer> testMap = new HashMap<String, Integer>();
		
		testMap.put("the", 58);
		testMap.put("in", 30);
		testMap.put("by", 32);
		testMap.put("then", 48);
		testMap.put("have", 12);
		testMap.put("gone", 4);
		
		LinkedHashMap<String, Integer> expectedMap = new LinkedHashMap<String, Integer>();
		
		expectedMap.put("the", 58);
		expectedMap.put("then", 48);
		expectedMap.put("by", 32);
		expectedMap.put("in", 30);
		expectedMap.put("have", 12);
		
		//find top 5 words
		assertEquals(expectedMap, analyze.findTop(testMap, 5));
	}
}
