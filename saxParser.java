import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;

import java.awt.List;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.*;

public class saxParser extends DefaultHandler {
	
	public static Map<String, TreeMap<String, Node>> postingList = new TreeMap<String,TreeMap<String,Node>>();   
	
	//..................TF-IDF implementation
	public static Map <String,String> totalFreq = new TreeMap<String,String>();
	public StringBuilder titleText = new StringBuilder();
	public static long wordsInPage=0;
	
	
	//...................Handling title along with tf
	public static int titleCount=0;
	public static long totalOffSet=0;
	public static long presFreqCounter=0;
	static File file1;
	static PrintWriter fileWriter1 ;
	
	//..................Open new Document when one FreqList is full
	public static void reset() {
		try {
			if(presFreqCounter>0)fileWriter1.close();
			presFreqCounter++;
			file1 = new File("./FreqList_"+presFreqCounter);
			file1.createNewFile();
			fileWriter1 = new PrintWriter(file1);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	Boolean title_tag=false;    // 0----
	Boolean text_tag=false;     // 1----
	Boolean infobox_tag=false;  // 2----
	Boolean cat_tag=false;      // 3----
	Boolean id_tag=false; 
	Boolean read_id=false; 
	
	public static String presentID = new String();
	public static int baseID=0;
	public StringBuilder text = new StringBuilder();
	
	
	refinementOf check = new refinementOf();

	//.............To make memory
	void clearDS(){
		postingList.clear();
		totalFreq.clear();
		text.setLength(0);
	}
	
	
	
	// For multiple index <---- comment this area if only one index is required
	static int pageCount = 0;
	static int count = 0;
		
	// when parser starts parsing the document
	public void startDocument(){
		System.out.println("Begining to parsing document.......... *_*");
	}

	// when parser ands parsing the document
	public void endDocument(){
		count += 1;
		writeIndex.writer();
		writeIndex.writerTF(presFreqCounter+1);
		System.out.println("**************");
		clearDS();
		System.out.println("\nEnding parsing document.......... *_*"+(pageCount));
	}
	
	// starts parsing a tag inside the document---> specific element
	public void startElement(String nameSpaceURI, String localName, String qName,Attributes atts){
		if(qName.equalsIgnoreCase("title")) {titleText.setLength(0);title_tag=true;read_id=true;}
		else if(qName.equalsIgnoreCase("id") && !id_tag) {id_tag=true;}
		else if(qName.equalsIgnoreCase("text") && !id_tag) {text_tag=true;}
	}

	// ends parsing a tag inside the document---> specific element
	public void endElement(String nameSpaceURI, String localName, String qName){
		if(qName.equalsIgnoreCase("title")) {
			title_tag=false;}
		if(qName.equalsIgnoreCase("id")) {
			id_tag=false;
			if(!read_id)return;
			read_id=false;
			check.stripNotStopWords(text.toString(),0);
			text.setLength(0);
		}
		else if(qName.equalsIgnoreCase("text")) {
			id_tag=false;
			check.stripStopWords(text.toString(),1);
			text.setLength(0);
			check.pushPosting(presentID);
		}
		else if(qName.equalsIgnoreCase("page")) {
			check.pushPosting(presentID);
			
	// For making multiple indexes...... <---- comment this area if only one index is required
			// pageCount += 1;
			// if(pageCount % 1000 == 0) {
			// 	count += 1;
			// 	writeIndex.writer();
			// }
			
			
	// Storing doc ID with word count
			totalFreq.put(presentID, Long.toString(wordsInPage)+"-"+titleText.toString());
			titleCount+=1;
			if(titleCount % 5000 == 0) {
				
				count += 1;
				writeIndex.writer();

				writeIndex.writerTF(presFreqCounter+1);
				presFreqCounter++;
				totalFreq.clear();
			}
			
//			String s = presentID ;
//			if(titleCount % 100 == 0) {
//				fileWriter2.println(s+'-'+totalOffSet);
//			}
//			s+='-'+ Long.toString(wordsInPage)+"-"+titleText.toString()+'\n';
//		    fileWriter1.println(s);
//		    totalOffSet+=s.length()+1;
		}

	}
	
	// contains the data inside that element of the document --> can print the data
	public void characters(char[] ch,int start , int length){
		if(id_tag && read_id) {
			
			//......normal id 
			
			presentID=new String(ch,start,length);
			if(baseID==0) {
//				System.out.println(presentID);
				baseID=Integer.parseInt(presentID);
			}
			
			//...........TF
			wordsInPage = 0;
			
			//.......delta id
//			if(presentID.length()==0) {
//				baseID=Integer.parseInt(new String(ch,start,length));
//				presentID=Integer.toHexString(baseID);
//			}
//			else {
//				int x = Integer.parseInt(new String(ch,start,length))-baseID;
//				presentID = ""+Integer.toHexString(x);
//			}
		}
		text.append(new String(ch,start,length));

		//........storing title 
		if(title_tag) {
			titleText.append(new String(ch,start,length));
		}
	}
	
}
	
	