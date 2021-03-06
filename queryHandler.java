import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

public class queryHandler {
	
	static ArrayList<String> ternaryFreq ;
	static ArrayList<String> ternaryIndex ;
	
	static ArrayList<String> seconFreq ;
	static ArrayList<String> seconIndex ;
	
	static ArrayList<String> Freq ;
	static ArrayList<String> Index ;
	static ArrayList<String> Ranked ;
	
	static ArrayList<String> postings ;
	static ArrayList<String> rankedPostings ;


	static int index_experi ;
	static String[] sec_entry;

	public static Set<String> stopWordsHashed = new  HashSet<String> ();
	public static String stop_words[]= {"coord","gr","com","tr","td","nbsp","http","https","www","a","about","above","across","after","again","against","all","almost","alone","along","already","also","although","always","among","an","and","another","any","anybody","anyone","anything","anywhere","are","area","areas","around","as","ask","asked","asking","asks","at","away","b","back","backed","backing","backs","be","became","because","become","becomes","been","before","began","behind","being","beings","best","better","between","big","both","but","by","c","came","can","cannot","case","cases","certain","certainly","clear","clearly","come","could","d","did","differ","different","differently","do","does","done","down","down","downed","downing","downs","during","e","each","early","either","end","ended","ending","ends","enough","even","evenly","ever","every","everybody","everyone","everything","everywhere","f","face","faces","fact","facts","far","felt","few","find","finds","first","for","four","from","full","fully","further","furthered","furthering","furthers","g","gave","general","generally","get","gets","give","given","gives","go","going","good","goods","got","great","greater","greatest","group","grouped","grouping","groups","h","had","has","have","having","he","her","here","herself","high","high","high","higher","highest","him","himself","his","how","however","i","if","important","in","interest","interested","interesting","interests","into","is","it","its","itself","j","just","k","keep","keeps","kind","knew","know","known","knows","l","large","largely","last","later","latest","least","less","let","lets","like","likely","long","longer","longest","m","made","make","making","man","many","may","me","member","members","men","might","more","most","mostly","mr","mrs","much","must","my","myself","n","necessary","need","needed","needing","needs","never","new","new","newer","newest","next","no","nobody","non","noone","not","nothing","now","nowhere","number","numbers","o","of","off","often","old","older","oldest","on","once","one","only","open","opened","opening","opens","or","order","ordered","ordering","orders","other","others","our","out","over","p","part","parted","parting","parts","per","perhaps","place","places","point","pointed","pointing","points","possible","present","presented","presenting","presents","problem","problems","put","puts","q","quite","r","rather","really","right","right","room","rooms","s","said","same","saw","say","says","second","seconds","see","seem","seemed","seeming","seems","sees","several","shall","she","should","show","showed","showing","shows","side","sides","since","small","smaller","smallest","so","some","somebody","someone","something","somewhere","state","states","still","still","such","sure","t","take","taken","than","that","the","their","them","then","there","therefore","these","they","thing","things","think","thinks","this","those","though","thought","thoughts","three","through","thus","to","today","together","too","took","toward","turn","turned","turning","turns","two","u","under","until","up","upon","us","use","used","uses","v","very","w","want","wanted","wanting","wants","was","way","ways","we","well","wells","went","were","what","when","where","whether","which","while","who","whole","whose","why","will","with","within","without","work","worked","working","works","would","x","y","year","years","yet","you","young","younger","youngest","your","yours","z"};
	
	public static void makeStopAnalyser() {
		for(String word : stop_words)stopWordsHashed.add(word);
	}
	
	static void stem(String[] word){
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
	
	
	//...............Read number of lines(get) from file    -1 for all lines
	
	static ArrayList<String> load(String file,int getOffset,int offset) throws IOException {
		ArrayList <String> ret = new ArrayList <String>();
		File f = new File(file);
		RandomAccessFile br = new RandomAccessFile(file,"r");
		br.seek(offset);
		String read;
		while(getOffset == 0 && true) {
			read = br.readLine();
			if(read==null)break;
			if(read.equals("\n"))continue;
			ret.add(read);
		}
	
		while ( getOffset>0 ) {
			read = br.readLine();
			if(read==null)break;
			getOffset -= read.length()-1;
			ret.add(read);
		}
		
		br.close();
		return ret;
	}
	
	
	
	static void init() throws IOException {
		makeStopAnalyser();
		ternaryFreq = load("./TernaryFreq.txt",0,0);
		ternaryIndex = load("./TernaryIndex.txt",0,0);
		// ternaryIndex = load("./TernaryIndex.txt",0,0);
	}
	
	static String searchWord(String item,int level) {
		String ret="-*-";
//		System.out.println( item+"-*-"+level);
		int index;
		if(level == 0) {
			index = Collections.binarySearch(ternaryIndex, item);
			index =  index*(-1) - 2;
			if(index+1<ternaryIndex.size() && ternaryIndex.get(index+1).length()>2  && ternaryIndex.get(index+1).substring(0,ternaryIndex.get(index+1).indexOf('-')).equals(item))index++;
			String nextOffset = "0";
			if(index+1<ternaryIndex.size() && index+1>=0 && ternaryIndex.get(index+1).contains("-")) {
				String next[] = ternaryIndex.get(index+1).split("-");
				
				if(ternaryIndex.get(index).contains("-")) {
				Integer off = Integer.parseInt(next[1]) - Integer.parseInt(ternaryIndex.get(index).substring(ternaryIndex.get(index).indexOf('-')+1));
				nextOffset =off.toString();}
			}
			while(index>=ternaryIndex.size() || ternaryIndex.get(index).equals("\n"))index--;
			ret = ternaryIndex.get(index) + '-' + nextOffset;
		}
		else if(level == 1) {
			index = Collections.binarySearch(seconIndex, item);
			index = index*(-1) - 2;
			if(index+1<seconIndex.size() && seconIndex.get(index+1).substring(0,seconIndex.get(index+1).indexOf('-')).equals(item))index++;
			while(index>=seconIndex.size() || seconIndex.get(index).equals("\n"))index--;
			
			String nextOffset = "0";
			if(index+1<seconIndex.size()) {
				String next[] = seconIndex.get(index+1).split("-");
				String pres[] = seconIndex.get(index).split("-");
 				Integer off = 0;
 				if(next[1].equals(pres[1]))off = Integer.parseInt(next[2]) - Integer.parseInt(pres[2]) ; 
				nextOffset =off.toString();
			}
			
			ret = seconIndex.get(index) + '-' + nextOffset;
		}
		else {
			index = Collections.binarySearch(Index, item);
			index = index*(-1) - 2;
//			System.out.println(index+"_*_"+Index.get(index+1)+'\n'+"**"+Index.get(index+2));
			if(index+1<Index.size() && Index.get(index+1).substring(0,(Index.get(index+1).indexOf('-')!=-1 ? Index.get(index+1).indexOf('-') : Index.get(index+1).indexOf(':'))).equals(item))index++;
			while(index>=Index.size() || Index.get(index).equals("\n"))index--;
			ret = Index.get(index);
			index_experi = index ;
			
		}
//		System.out.println("****"+ret);
		return ret;
	}
	
	static String searchRankedWord(String item,int level) {
		
		String ret="-*-";
		int index;
		if(level == 0) {
			index = Collections.binarySearch(ternaryIndex, item);
			index =  index*(-1) - 2;
			if(index+1<ternaryIndex.size() && ternaryIndex.get(index+1).substring(0,ternaryIndex.get(index+1).indexOf('-')).equals(item))index++;
			String nextOffset = "0";
			if(index+1<ternaryIndex.size()) {
				
				String next[] = ternaryIndex.get(index+1).split("-");
				Integer off = Integer.parseInt(next[1]) - Integer.parseInt(ternaryIndex.get(index).substring(ternaryIndex.get(index).indexOf('-')+1));
				nextOffset =off.toString();;
			}
			while(index>=ternaryIndex.size() || ternaryIndex.get(index).equals("\n"))index--;
			ret = ternaryIndex.get(index) + '-' + nextOffset;
		}
		else if(level == 1) {
			index = Collections.binarySearch(seconIndex, item);
			index = index*(-1) - 2;
			if(index+1<seconIndex.size() && seconIndex.get(index+1).substring(0,seconIndex.get(index+1).indexOf('-')).equals(item))index++;
		
			while(index>=seconIndex.size() || seconIndex.get(index).equals("\n"))index--;
			
			String nextOffset = "0";
			if(index+1<seconIndex.size()) {
				String next[] = seconIndex.get(index+1).split("-");
				String pres[] = seconIndex.get(index).split("-");
 				Integer off = -1;
 				if(next[1].equals(pres[1]))off = Integer.parseInt(next[2]) - Integer.parseInt(pres[2]) ; 
				nextOffset =off.toString();
			}
			
			ret = seconIndex.get(index) + '-' + nextOffset;
		}
		else {
			index = Collections.binarySearch(Ranked, item);
			index = index*(-1) - 2;
			if(index+1<Ranked.size() && Ranked.get(index+1).indexOf('-') != -1 && Ranked.get(index+1).substring(0,Ranked.get(index+1).indexOf('-')).equals(item))index++;
			while(index>=Ranked.size() || Ranked.get(index).equals("\n"))index--;
			ret = Ranked.get(index);
		}
		
		return ret;
	}
	
	
	
	static String searchTitle(String item,int level) {
//		System.out.println(item);
		String ret="-*-";
		int index;
		if(level == 0) {
			index = Collections.binarySearch(ternaryFreq, item);
			index =  index*(-1) - 2;
			if(index+1<ternaryFreq.size() && ternaryFreq.get(index+1).substring(0,ternaryFreq.get(index+1).indexOf('-')).equals(item))index++;
			String nextOffset = "0";
			if(index+1<ternaryFreq.size()) {
				String next[] = ternaryFreq.get(index+1).split("-");
				Integer off = Integer.parseInt(next[1]) - Integer.parseInt(ternaryFreq.get(index).substring(ternaryFreq.get(index).indexOf('-')+1));
				nextOffset =off.toString();;
			}
			while(index>=ternaryFreq.size() || ternaryFreq.get(index).equals("\n"))index--;
			
			ret = ternaryFreq.get(index) + '-' + nextOffset;
		}
		else if(level == 1) {
			index = Collections.binarySearch(seconFreq, item);
			index = index*(-1) - 2;
			if(index+1<seconFreq.size() && seconFreq.get(index+1).substring(0,seconFreq.get(index+1).indexOf('-')).equals(item))index++;
			
			String nextOffset = "0";
			if(index+1<seconFreq.size()) {
				String next[] = seconFreq.get(index+1).split("-");
				String pres[] = seconFreq.get(index).split("-");
 				Integer off = -1;
 				if(next[1].equals(pres[1]))off = Integer.parseInt(next[2]) - Integer.parseInt(pres[2]) ; 
				nextOffset =off.toString();
			}
			while(index>=ternaryFreq.size() || ternaryFreq.get(index).equals("\n"))index--;

			ret = seconFreq.get(index) + '-' + nextOffset;

		}
		else {
			index = Collections.binarySearch(Freq, item);
			index = index*(-1) - 2;
			if(index+1<Freq.size() &&  Freq.get(index+1).indexOf('-') !=-1 && Freq.get(index+1).substring(0,Freq.get(index+1).indexOf('-')).equals(item))index++;
			while(index>=Freq.size() || Freq.get(index).equals("\n"))index--;
			ret = Freq.get(index);
		}
//		System.out.println(ret + "----->"+level);
		return ret;
	}
	
	
	static String getPosting(String word) {
		word = word.replace(" ", "");
		if(word.length()<2)return null;
		try {
			
			String[] ter_entry = searchWord(word,0).split("-");
			seconIndex = load("./SecondaryIndex.txt",Integer.parseInt(ter_entry[2]),Integer.parseInt(ter_entry[1]));
			sec_entry = searchWord(word,1).split("-");
			Index = load("./Index/Segmented_Index"+sec_entry[1]+".txt",Integer.parseInt(sec_entry[3]),Integer.parseInt(sec_entry[2]));
			String ret = searchWord(word,2);
//			System.out.println(word+"*****"+ret);
		return ret;
		} catch (Exception e) {
			e.printStackTrace();
		}return word;
	}
	
	
	static String getRankedPosting(String word) {
		word = word.replace(" ", "");
		if(word.length()<2)return null;
		try {
//			System.out.println(word+"********");
			// String[] ter_entry = searchRankedWord(word,0).split("-");
			// seconIndex = load("./SecondaryIndex.txt",Integer.parseInt(ter_entry[2]),Integer.parseInt(ter_entry[1]));
		    //String[] sec_entry = searchRankedWord(word,1).split("-");
			Ranked = load("./ChampList/Segmented_Index"+sec_entry[1]+".txt",Integer.parseInt(sec_entry[3]),Integer.parseInt(sec_entry[2]));
			
			String s = Ranked.get(index_experi)  ; //searchRankedWord(word,2); 
			return s;
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return word;
	}
	
	static public char tagger(int i) {
		if(i==0)return 't';
		if(i==1)return 'T';
		if(i==2)return 'i';
		if(i==3)return 'C';
		if(i==4)return 'r';
		if(i==5)return 'E';
		return '?';
	}
	
	static String WeightThis(String s ,int diff) {
		if(diff == -1)return s;
		String x = "";
		x+=s.substring(0,s.indexOf('-')+1);
		s=s.substring(s.indexOf('-')+1);
		String []words = s.split(";");
		for(String que : words) {
			if(que.contains(""+tagger(diff))) {
				x+=que.substring(0, que.indexOf(":")+1);
				int ind = que.indexOf(""+tagger(diff));
				x += que.charAt(ind);
				ind++;
				while(ind<que.length()&&que.charAt(ind)>='0' && que.charAt(ind)<='9' || ind<que.length()&&que.charAt(ind)>='a' && que.charAt(ind)<='f')x += que.charAt(ind++);
				x+=';';
			}
			
		}
		return x;
	}
	
	
	static String getPostingdiff(String word,int diff) {
		word = word.replace(" ", "");
		if(word.length() == 0)return null;
		String pos = getPosting(word);
//		System.out.println(pos);
		String rate = WeightThis(pos,diff);
//		System.out.println(rate);
		return rate;
	}
	
	
	
	static String getTitle(String word) {
		try {
			
			String[] ter_entry = searchTitle(word,0).split("-");
			seconFreq = load("./SecondaryFreq.txt",Integer.parseInt(ter_entry[2]),Integer.parseInt(ter_entry[1]));
			String[] sec_entry = searchTitle(word,1).split("-");
			Freq = load("./Freq/Segmented_Freq"+sec_entry[1]+".txt",Integer.parseInt(sec_entry[3]),Integer.parseInt(sec_entry[2]));
			return searchTitle(word,2);
		
		} catch (Exception e) {
			e.printStackTrace();
		}return word;
	}
	
	static Integer getWordCount(String word) {
		try {
			ternaryFreq = load("./TernaryFreq.txt",0,0);
			String[] ter_entry = searchTitle(word,0).split("-");
			seconFreq = load("./SecondaryFreq.txt",Integer.parseInt(ter_entry[2]),Integer.parseInt(ter_entry[1]));
			String[] sec_entry = searchTitle(word,1).split("-");
			Freq = load("./Freq/Segmented_Freq"+sec_entry[1]+".txt",Integer.parseInt(sec_entry[3].equals("")?"0":sec_entry[3]),Integer.parseInt(sec_entry[2].equals("")?"0":sec_entry[2]));
			String s[] = searchTitle(word,2).split("-");
			return Integer.parseInt(s[1]);
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	

	static void processSimple(String query) {
		String[] tokens = query.replaceAll("[^a-zA-Z0-9 :]", "").toLowerCase().split("\\s+");
		//......remove stop words from query
		int n=0;
		for(String s:tokens)if(!stopWordsHashed.contains(s))n++;
		String[] refinedTokens = new String [n];
		n=0;
		for(String s:tokens)if(!stopWordsHashed.contains(s)) {refinedTokens[n]=s;n++;}
		
		
		//.......stemming
		stem(refinedTokens);
		
			String ret = "";
		for(String word : refinedTokens) {
			ret = getPosting(word);
			if(ret == null)continue;
			postings.add(ret);
			String s=getRankedPosting(word);
			if(s==null)continue;
			rankedPostings.add(s);
		}
		
		// for(String word : refinedTokens) {
		// 	String s=getRankedPosting(word);
		// 	if(s==null)continue;
		// 	rankedPostings.add(s);
		// }
		
	}
	

	static int diff;
	static void processDiff(String st) {
		
		String[] tokens = st.split("\\s+");
		//......remove stop words from query
		
		postings = new ArrayList<>();
		rankedPostings = new ArrayList<>();
		
		
		for(String query : tokens) {
		int n=0;
		
		
		if(query.contains("T:"))diff=0;
		if(query.contains("B:"))diff=1;
		if(query.contains("I:"))diff=2;
		if(query.contains("C:"))diff=3;
		if(query.contains("R:"))diff=4;
		if(query.contains("E:"))diff=5;
		if(query.contains(":"))query=query.substring(query.lastIndexOf(":")+1);
		query = query.toLowerCase();
		query = query.replace("[^0-9a-zA-Z ]", "");
		//.......stemming
		String []sdf = new String [1];
		sdf[0]=query;
		
//		System.out.println(diff +" "+ sdf[0]);
		
		stem(sdf);
		String ret="";
		//.......get posting lists
		for(String df:sdf)
			ret=getPostingdiff(df,diff);
			if(ret==null)continue;
			postings.add(ret);
		
		}
	}
	
	public static void main(String[] args) throws IOException {
		init();
		
		//.......get posting lists
		
				postings = new ArrayList<>();
				rankedPostings = new ArrayList<>();

		System.out.println("*_*");
		String query = "Hyderabad";
//		System.out.println(searchWord("aorta", 0));
//		getPosting("apachecon09");
//		getTitle("apachecon09");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); 
		
		
		System.out.println("Enter number of queries --->");
		int n = Integer.parseInt(br.readLine());
		
		for(int i=0;i<n;i++) {
		try {
			System.out.println("Enter query number "+(i+1));
			query = br.readLine();
			if(query.contains("T:") || query.contains("B:")||query.contains("I:")||query.contains("C:")||query.contains("R:")||query.contains("E:")) {
				diff = -1;
				processDiff(query);
				RankList rk = new RankList(0);
		//		System.out.println("123");
				ArrayList <String> top = rk.getTop10();
				int ct=1;
				
				// System.out.println("***---***");
				
				for(String s:top) {
					String tit = getTitle(s);
					System.out.println(ct+" "+tit.substring( tit.lastIndexOf('-')+1));
					ct++;
					if(ct >= 15 )break;
				}
				

				// System.out.println("***---***");
			}
			else {	
				processSimple(query);
				// System.out.println("************************************");
				// for(String s : postings)System.out.println(s.substring(0, s.indexOf("-"))+" "+s);
				// System.out.println("************************************");
				// for(String s : rankedPostings)System.out.println(s.substring(0, s.indexOf("-"))+" "+s);
				// System.out.println("************************************");
				
			
				RankList rk = new RankList();
		//		System.out.println("123");
				ArrayList <String> top = rk.getTop10();
				int ct=1;
				
				// System.out.println("***---***");
				
				for(String s:top) {
					String tit = getTitle(s);
					System.out.println(ct+" "+tit.substring( tit.lastIndexOf('-')+1));
					ct++;
					if(ct >= 15 )break;
				}
		

			// System.out.println("***---***");
	}	
	}catch(Exception e) {
		System.out.println("Word not found");
		e.printStackTrace();
	}
}
}
}
