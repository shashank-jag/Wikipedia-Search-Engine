import java.io.File;
import java.io.IOException;
import java.util.*;

import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

//enwiki-latest-pages-articles-multistream.xml


public class index_creater {
	static long start,end,real_start;
	public static String inputFile;
    public static void main(String[] args) throws SAXException , IOException, InterruptedException {
//    		inputFile="wiki-search-small.xml";
    		inputFile = args[0];
    		start = System.currentTimeMillis();
    		real_start = start;
  //   		System.out.println(inputFile);
    		
    		
    		
  //   		XMLReader p= XMLReaderFactory.createXMLReader();
		// p.setContentHandler(new saxParser());
		// p.parse(inputFile);
		// end = System.currentTimeMillis();
		// System.out.println("Total time elapsed for Partial Indexing-->"+(end-real_start) +"miliseconds");
		
		
		// Making merging multiple indexes -------> Comment this part if running for small databases 
		
		mergerIndex x = new mergerIndex("Index",1024,512,1024*256);
		start = System.currentTimeMillis();
		x.go();
		
		mergerIndex y = new mergerIndex("Freq",1024,256,1024*256);
		y.go();
		
		end = System.currentTimeMillis();
		System.out.println();
		System.out.println("Total time elapsed for Indexing-->"+(end-real_start) +"miliseconds");
    
    }
}