package cop3024;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.LinkedHashMap;

import org.junit.Test;

public class textAnalyzer_cleanAndLogTest {

	@Test
	public void test() {
		AGui analyze = new AGui();
		
		String testString = "<html><body> This string should be, without html tags & it should also lack symbols and special characters! §</body></html>";
		
		//should just get the words
		assertEquals(" This string should be without html tags  it should also lack symbols and special characters ", analyze.cleanAndLog(testString));
	}
}
