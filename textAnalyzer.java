package cop3024;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class textAnalyzer {
	//going to keep track of words using a hashmap, the key as the word
	static HashMap<String, Integer> word = new HashMap<String, Integer>();
	
	//to find the poem, the program is going to look for set flags
	static String beginFlag = "<h1>The Raven</h1>";
	static String endFlag = "</div><!--end chapter-->";
	static Boolean active = false;
	
	//function to log words and remove unwanted stuff
	public static String cleanAndLog(String content) {
		//html tag junk
		content = content.replaceAll("<[^>]*>", "");
		//other junk
		content = content.replaceAll("[,;.!?'\"]", "");
		//weird chars in the poem
		content = content.replaceAll("[^a-zA-Z ]", "");
		
		//iterating though line...
		String[] words = content.split(" ");
		for(String w : words) {
			w = w.toLowerCase();
			if(w != "") {
				//adding word count to word hashmap
				Integer wordCount = word.containsKey(w) ? word.get(w) : 1;
				word.put(w, wordCount + 1);
			}
		}
		return content;
	}
	
	//function to find the top x word
	public static HashMap<String, Integer> findTop(HashMap<String, Integer> thisMap, Integer topCount) {
		LinkedHashMap<String, Integer> tempMap = new LinkedHashMap<String, Integer>();
		Integer max = 0;
		Integer esc = 0;
		String sk = "";
		while(tempMap.size()<topCount) {
			for(HashMap.Entry<String, Integer> key : thisMap.entrySet()) {
				if(max < key.getValue()) {
					max = key.getValue();
					sk = key.getKey();
				}
			}
			tempMap.put(sk, max);
			thisMap.remove(sk);
			max = 0;
			esc++;
			sk = "";
			if(esc > 100) break;
		}
		return tempMap;
	}
	
	public static void main(String[] args) {
		
		//using text file for website content, txt file placed in package with java file...
		try {
			FileInputStream poemContent = new FileInputStream((new File("")).getAbsolutePath()+"/src/cop3024/TheRavenPoemWithHTMLTags.txt");
			Scanner scanner = new Scanner(poemContent);
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				
				//setting active by flags
				active = (line.contains(beginFlag))?true:(line.contains(endFlag))?false:active;
				
				if(active) {
					//System.out.println(cleanAndLog(line));
					cleanAndLog(line);
				}
			}
			scanner.close();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}  
		
		//finding top 20 words...
		HashMap<String, Integer> result = findTop(word, 20);
		
		//Done analyzing, printing hashmap
		System.out.println(String.format("%2s %15s %10s", "#", "Word", "Freq."));
		Integer i = 0;
		for(HashMap.Entry<String, Integer> key : result.entrySet()) {
			i++;
			System.out.println(String.format("%2d:%15s:%10d", i, key.getKey(), key.getValue()));
		}
	}
}

/* Though process:
 * I need to find a way to get the website content
 * I need a way to cut just the poem out of the content
 * Remove all the html tags with replaceAll method
 * Remove all the other stuff like periods, commas
 * Using a hash map to link a word and it's occurance in the poem
 * After creating the hashmap with all of the words, I need to make a function that sorts the top 20 words into a linkedhashmap
 * Then I need to loop through the result hashmap and output results 
 */
