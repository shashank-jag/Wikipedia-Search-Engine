import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class refinementOf {
	public static Set<String> stopWordsHashed = new  HashSet<String> ();
	public static String stop_words[]= {"coord","gr","com","tr","td","nbsp","http","https","www","a","about","above","across","after","again","against","all","almost","alone","along","already","also","although","always","among","an","and","another","any","anybody","anyone","anything","anywhere","are","area","areas","around","as","ask","asked","asking","asks","at","away","b","back","backed","backing","backs","be","became","because","become","becomes","been","before","began","behind","being","beings","best","better","between","big","both","but","by","c","came","can","cannot","case","cases","certain","certainly","clear","clearly","come","could","d","did","differ","different","differently","do","does","done","down","down","downed","downing","downs","during","e","each","early","either","end","ended","ending","ends","enough","even","evenly","ever","every","everybody","everyone","everything","everywhere","f","face","faces","fact","facts","far","felt","few","find","finds","first","for","four","from","full","fully","further","furthered","furthering","furthers","g","gave","general","generally","get","gets","give","given","gives","go","going","good","goods","got","great","greater","greatest","group","grouped","grouping","groups","h","had","has","have","having","he","her","here","herself","high","high","high","higher","highest","him","himself","his","how","however","i","if","important","in","interest","interested","interesting","interests","into","is","it","its","itself","j","just","k","keep","keeps","kind","knew","know","known","knows","l","large","largely","last","later","latest","least","less","let","lets","like","likely","long","longer","longest","m","made","make","making","man","many","may","me","member","members","men","might","more","most","mostly","mr","mrs","much","must","my","myself","n","necessary","need","needed","needing","needs","never","new","new","newer","newest","next","no","nobody","non","noone","not","nothing","now","nowhere","number","numbers","o","of","off","often","old","older","oldest","on","once","one","only","open","opened","opening","opens","or","order","ordered","ordering","orders","other","others","our","out","over","p","part","parted","parting","parts","per","perhaps","place","places","point","pointed","pointing","points","possible","present","presented","presenting","presents","problem","problems","put","puts","q","quite","r","rather","really","right","right","room","rooms","s","said","same","saw","say","says","second","seconds","see","seem","seemed","seeming","seems","sees","several","shall","she","should","show","showed","showing","shows","side","sides","since","small","smaller","smallest","so","some","somebody","someone","something","somewhere","state","states","still","still","such","sure","t","take","taken","than","that","the","their","them","then","there","therefore","these","they","thing","things","think","thinks","this","those","though","thought","thoughts","three","through","thus","to","today","together","too","took","toward","turn","turned","turning","turns","two","u","under","until","up","upon","us","use","used","uses","v","very","w","want","wanted","wanting","wants","was","way","ways","we","well","wells","went","were","what","when","where","whether","which","while","who","whole","whose","why","will","with","within","without","work","worked","working","works","would","x","y","year","years","yet","you","young","younger","youngest","your","yours","z"};
	
	public static Map<String,Node> tempPosting = new TreeMap<String,Node>();
	
	public refinementOf() {
		for(String word : stop_words)stopWordsHashed.add(word);
	}
	//.............................Preprocessing
	String process(String text) {
		text = text.toLowerCase();
		text = text.trim();
		return text;
	}
	
	//..............................Removing Stop Words
	String[] remStop(String text) {
		String[] tokens = text.split("[^a-zA-Z0-9]");
		int n=0;
		for(String s:tokens)if((!stopWordsHashed.contains(s)) && s.length() <=25 && s.length() >=2 )n++;
		String[] refinedTokens = new String [n];
		n=0;
		for(String s:tokens)if((!stopWordsHashed.contains(s)) && s.length() <=25 && s.length() >=2 ) {refinedTokens[n]=s;n++;}
		return  refinedTokens;
	}
	
	//...............................Making posting list
	void pushPosting(String id) {
		for(Map.Entry<String, Node> entry : tempPosting.entrySet()) {
			TreeMap<String, Node> tem = saxParser.postingList.get(entry.getKey());
			if(tem==null)tem = new TreeMap<String, Node>();
			tem.put(saxParser.presentID,entry.getValue());
			saxParser.postingList.put(entry.getKey(),tem);
		}
		tempPosting.clear();
	}
	
	//...............................Stemming by porter stemmer
	void stem(String[] word){
		saxParser.wordsInPage += word.length;  //........tf of document
		try {
			for(int i=0;i<word.length;i++) {
				Stemmer s = new Stemmer();
    				s.add(word[i].toCharArray(),word[i].length());
    				word[i]=s.stem();
			}
		}catch(Exception e) {
			System.out.println("*_*...oops");
		}
	}
	
	
	//...............................Create temporary posting list 
	void dump(String[] words,int tag) {
		for(String wo: words) {
			if(wo.length()<=2)continue;
			Node x;
			if(tempPosting.get(wo)==null)x = new Node();
			else {
				x = tempPosting.get(wo);
				}
			x.count[tag]++;
			tempPosting.put(wo,x);
		}
	}
	
	
	//..............................handling infobox
	
	String processInfoAndCite(String text) {
		StringBuilder ret=new StringBuilder();
		StringBuilder infText=new StringBuilder();
		StringBuilder categText=new StringBuilder();
		StringBuilder refText=new StringBuilder();
		StringBuilder externalText=new StringBuilder();
		int ct=0;
		for(int i=0;i<text.length();) {
			ct=0;							//bracket counter
			if(i+8<text.length() && text.substring(i,i+8).equalsIgnoreCase("{infobox")) {
				infText.append(text.charAt(i));
				i+=8;ct++;
				while(i<text.length() && ct>0) {
					if(text.charAt(i)=='{')ct++;
					else if(text.charAt(i)=='}')ct--;
					infText.append(text.charAt(i));
					i++;
				}
//				i--;
			}
			// handle fields which are not required
			else if(i+5<text.length() && text.substring(i,i+5).equalsIgnoreCase("{cite") || i+3<text.length() && text.substring(i,i+3).compareTo("{gr")==0 || i+6<text.length() && text.substring(i,i+6).compareTo("{coord")==0) {
				i+=4;ct++;
				while(i<text.length() && ct>0) {
					if(text.charAt(i)=='{')ct++;
					else if(text.charAt(i)=='}')ct--;
					i++;
				}
//				i--;
			}
			
			else if(i+7<text.length() && text.substring(i, i+7).equalsIgnoreCase("[image:") || i+6<text.length() && text.substring(i, i+6).equalsIgnoreCase("[file:") ) {
				i++;ct++;
				while(i<text.length() && ct>0) {
					if(text.charAt(i)=='[')ct++;
					else if(text.charAt(i)==']')ct--;
					i++;
				}
//				i--;
			}
			
			
			// handle category
			else if(i+10<text.length() && text.substring(i,i+10).equalsIgnoreCase("[Category:")) {
				categText.append(text.charAt(i));
				i+=10;ct++;
				while(i<text.length() && ct>0) {
					if(text.charAt(i)=='[')ct++;
					else if(text.charAt(i)==']')ct--;
					categText.append(text.charAt(i));
					i++;
				}
//				i--;
			}
			
			// references tag and external tag
			else if(i+16<text.length() && text.substring(i,i+16).equalsIgnoreCase("== References ==") || i+16<text.length() && text.substring(i,i+14).equalsIgnoreCase("==References==")) {
				i+=16;
				while(i+2<=text.length() && (!text.substring(i,i+2).equals("=="))) {
					refText.append(text.charAt(i));
					i++;
				}
			}
			
			else if(i+5<text.length() && text.substring(i,i+5).equalsIgnoreCase("<ref>")) {
				i+=4;
				while(i+6<text.length() && (!text.substring(i,i+6).equalsIgnoreCase("</ref>"))) {
					refText.append(text.charAt(i));
					i++;
				}
			}
			
			else if(i+18<text.length() && text.substring(i,i+18).equalsIgnoreCase("==External links==")) {
				i+=18;
				while(i+2<=text.length() && (!text.substring(i,i+2).equals("[["))) {
					while(i<text.length() && text.charAt(i)!='\n') {
						externalText.append(text.charAt(i));
						i++;
					}externalText.append(" ");i++;
				};
			}
			else {
				ret.append(text.charAt(i));
				i++;
			}
			if(i>=text.length())break;
		}
	
		String[] tokens ;
		if(infText.length()>0) {
			text = process(infText.toString());
			tokens = text.split("[^a-zA-Z0-9]");
			stem(tokens);
			dump(tokens,2);
		}
		if(categText.length()>0) {
			text = process(categText.toString());
			tokens = text.split("[^a-zA-Z0-9]");
			stem(tokens);
			dump(tokens,3);
		}
		if(refText.length()>0) {
			text = process(refText.toString());
			tokens = text.split("[^a-zA-Z0-9]");
			stem(tokens);
			dump(tokens,4);
		}
		if(externalText.length()>0) {
			text = process(externalText.toString());
			tokens = text.split("[^a-zA-Z0-9]");
			stem(tokens);
			dump(tokens,5);
		}
		return ret.toString();
	}
	
	
	//...............................Callers..........*_*
	void stripNotStopWords(String text,int tag) {
		// Case folding
		text = process(text);
		String[] tokens = text.split("[^a-zA-Z0-9]");
		stem(tokens);
		dump(tokens,tag);
	}
	
	void stripStopWords(String text,int tag) {
		// Case folding
		text = process(text);
		if(tag == 1)text = processInfoAndCite(text);
		String[] tokens = remStop(text);
		stem(tokens);
		dump(tokens,tag);
	}
}
