package cop3024;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class textAnalyzer_GUI {
	
	//using parts of an old cop2805 project to make the gui... 
	public static void contructGui() {
		JFrame.setDefaultLookAndFeelDecorated(true);
		AGui frame = new AGui();
		frame.setTitle("Word Analyze");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		int frameWidth = 210;
		int frameHeight = 180;
		Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setBounds((int) (screensize.getWidth()/2) - frameWidth, (int) (screensize.getHeight()/2) - frameHeight, frameWidth, frameHeight);
	}
	
	public static void main(String[] args) {
		//start up the gui
		
		//all previous methods moved to the AGui class below
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				contructGui();
			}
		});
	}
}

class AGui extends JFrame {
	private static final long serialVersionUID = 1L;
	
	//declaring JFrame elements here
	JButton findWords;
	JTextField topEntry;
	JTextArea output;
	JScrollPane outputScroll;
	
	//going to keep track of words using a hashmap, the key as the word
	HashMap<String, Integer> word = new HashMap<String, Integer>();
		
	//to find the poem, the program is going to look for set flags
	String beginFlag = "<h1>The Raven</h1>";
	String endFlag = "</div><!--end chapter-->";
	Boolean active = false;
	
	public AGui() {
		super();
		
		//using text file for website content, txt file placed in package with java file...
		try {
			FileInputStream poemContent = new FileInputStream((new File("")).getAbsolutePath()+"/src/cop3024/TheRavenPoemWithHTMLTags.txt");
			Scanner scanner = new Scanner(poemContent);
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				//setting active by flags
				this.active = (line.contains(this.beginFlag))?true:(line.contains(this.endFlag))?false:active;
				if(this.active) {
					this.cleanAndLog(line);
				}
			}
			scanner.close();		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		
		init();
	}

	//function to log words and remove unwanted stuff
	public String cleanAndLog(String content) {
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
				Integer wordCount = this.word.containsKey(w) ? this.word.get(w) : 1;
				this.word.put(w, wordCount + 1);
			}
		}
		return content;
	}
	
	//needed this because the button can be clicked multiple times, and previous results would be lost due to the .remove function, oops!
	public HashMap<String, Integer> deepClone(HashMap<String, Integer> map, HashMap<String, Integer> target) {
		for(HashMap.Entry<String, Integer> key : target.entrySet()) {
			map.put(key.getKey(), key.getValue());
		}
		return map;
	}
	
	//function to find the top x word
	public HashMap<String, Integer> findTop(HashMap<String, Integer> thisMap, Integer topCount) {
		HashMap<String, Integer> copyMap = deepClone(new HashMap<String, Integer>(), thisMap);
		LinkedHashMap<String, Integer> tempMap = new LinkedHashMap<String, Integer>();
		Integer max = 0;
		Integer esc = 0;
		String sk = "";
		while(tempMap.size()<topCount) {
				for(HashMap.Entry<String, Integer> key : copyMap.entrySet()) {
				if(max < key.getValue()) {
					max = key.getValue();
					sk = key.getKey();
				}
			}
			tempMap.put(sk, max);
			copyMap.remove(sk);
			max = 0;
			esc++;
			sk = "";
			if(esc > 100) break;
		}
		return tempMap;
	}
	
	public void process(String button) {
		if(button == "Find Top Words") {
			HashMap<String, Integer> result = this.findTop(this.word, Integer.parseInt(this.topEntry.getText()));
			this.output.setText("Results:\n");
			Integer i = 0;
			for(HashMap.Entry<String, Integer> key : result.entrySet()) {
				i++;
				this.output.append(String.format("%d:%17s\n", key.getValue(), key.getKey()));
			}
		}
	}
	private void init() {
		this.setLayout(new FlowLayout(5));
		this.findWords = new JButton("Find Top Words");
		this.findWords.addActionListener(new Command(this));
		
		this.add(findWords);
		
		this.topEntry = new JTextField(2);
		this.topEntry.setText("20");
		this.topEntry.setColumns(2);
		
		this.output = new JTextArea(8, 16);
		this.output.setEditable(false);
		
		this.outputScroll = new JScrollPane(this.output);
		
		this.add(topEntry);
		this.add(outputScroll);
		
		this.setVisible(true);
	}
}
class Command implements ActionListener {
	AGui frame;
	public Command(AGui f) {
		frame = f;
	}
	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		this.frame.process(source.getText());
	}
}
