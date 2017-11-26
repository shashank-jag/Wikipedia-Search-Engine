import java.util.HashMap;
import java.util.TreeMap;
import java.util.Map;
import java.util.Map.Entry;
import java.io.*;

public class writeIndex {
	
	//...............tag descriptors
	static public char tagger(int i) {
		if(i==0)return 't';
		if(i==1)return 'T';
		if(i==2)return 'i';
		if(i==3)return 'C';
		if(i==4)return 'r';
		if(i==5)return 'E';
		return '?';
	}
			
	//...............returns string representation of a string
	public static String NodeToString(Node x) {
		StringBuilder ret=new StringBuilder();
		for(int i=0;i<6;i++)if(x.count[i]>0) {
			ret.append(tagger(i));
//			ret .append( Integer.toHexString(x.count[i])+',');
			ret .append( Integer.toHexString(x.count[i]));
		}
		ret.append(';');
		return ret.toString();
	}
	
	//..............TF file
	public static void writerTF(long presFreqCounter) {
		System.out.println("\t\t\tFreqCounter "+presFreqCounter+" made");
		String outputFile = "./PartialFreq";
		File dir = new File(outputFile);
		if (! dir.exists()){
	        dir.mkdir();
	    }
		outputFile = "./PartialFreq/PartialFreq_"+presFreqCounter+".txt";
		File file = new File(outputFile);
		PrintWriter fileWriter;
		try {
			fileWriter = new PrintWriter(file);
		for(Entry<String, String> entry: saxParser.totalFreq.entrySet()) {
			StringBuilder s = new StringBuilder();
			s.append(entry.getKey()+"-");
			s.append(entry.getValue());
			fileWriter.println(s.toString());
		}
		fileWriter.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	
	public static void writer() {
		System.out.println("Time elapsed for Parsing"+saxParser.count + "-->" + (System.currentTimeMillis()-index_creater.start) +"miliseconds");
		index_creater.start = System.currentTimeMillis();
		String outputFile = "./PartialIndex";
		File dir = new File(outputFile);
		if (! dir.exists()){
	        dir.mkdir();
	    }
		outputFile += '/' + "PartialIndex" + '_' + saxParser.count + ".txt";
		File file = new File(outputFile);
		try {
			file.createNewFile();
		
		PrintWriter fileWriter =new PrintWriter(file,"UTF-8");
//		fileWriter.println(saxParser.baseID);
		for(Entry<String, TreeMap<String, Node>> entry : saxParser.postingList.entrySet()) {
			StringBuilder s = new StringBuilder();
			s.append(entry.getKey()+"-");
			TreeMap<String, Node> val = entry.getValue();
			for(Entry<String,Node> loc_entry:val.entrySet()) {
				s.append((loc_entry.getKey())+':');
				Node x = loc_entry.getValue();
				s.append(NodeToString(x));
			}
			fileWriter.println(s);
			s.setLength(0);
		}
		fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		saxParser.postingList.clear();
	}
}
