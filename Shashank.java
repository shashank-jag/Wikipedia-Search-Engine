/*import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

class champNode {
	String list;
	int net;
	Double tf;
	
}
class Comp1 implements Comparator<champNode>{
	@Override
	public int compare(champNode o1,champNode o2) {
		return o2.net-o1.net;

		// if(o.net*(o.tf) > this.net*(this.tf)) return 1;
		// else if (o.net*(o.tf) < this.net*(this.tf)) return -1;
		// return 0;
		
	}

	
}
public class Shashank {
	
	static TreeMap <String,Integer> numberOfWords = new TreeMap<String,Integer>();
	
	static void getWordCount() throws IOException {
		String f = "TotalFreq.txt";
		File file = new File(f);
		BufferedReader reader = new BufferedReader(new FileReader("./"+f));
		String s;
		while((s=reader.readLine())!=null) {
			if(s.length()==0)continue;
			String[] arr = s.split("-");
			numberOfWords.put(arr[0],Integer.parseInt(arr[1]));
		}
	}
	
//	static public int istag(char i) {
//		if (i=='t')return 0;
//		if (i=='T')return 1; 
//		if (i=='i')return 2; 
//		if (i=='C')return 3;
//		if (i=='r')return 4 ;
//		if (i=='E')return 5;
//		return -1;
//	}	
	
	
	
	
	public static void main(String[] args) throws IOException, InterruptedException {

		HashMap<Character, Integer> istag = new HashMap<>();
		istag.put('t', 0);
		istag.put('T', 1);
		istag.put('i', 2);
		istag.put('C', 3);
		istag.put('r', 4);
		istag.put('E', 5);
		long real_start = System.currentTimeMillis(),cur = real_start;
		String list;		
		System.out.println("Generating Champion list #B-)");
		File f = new File("/home/vinisha/Documents/IRE/Shashank/");// your folder path
		String[] fileList = f.list(); // It gives list of all files in the folder.
		
		long ctr=0,offset=0,offset_ter=0;
		
		for(int i=Integer.parseInt(args[0]),stp=0 ; i<=Integer.parseInt(args[1]) && stp < 100;i++) {
			String str = "Segmented_Index"+i+".txt";
//			File fChk = new File("/home/vinisha/Documents/IRE/Shashank/"+str);
//			if(!fChk.exists()){
//				// TimeUnit.SECONDS.sleep(10);
//				System.out.println(str);
//				Thread.sleep(4000);
//				i--;
//				stp++;
//				continue;
//			}
			stp=0;
//			System.out.println(str);
			// if(str.charAt(0)!='S')continue;
			System.out.println("Generating Posting for "+ str);
			BufferedReader reader = new BufferedReader(new FileReader("/home/vinisha/Documents/IRE/Shashank/"+str));
			File file = new File("/home/vinisha/Documents/IRE/Shashank/ChampList/"+str);
//			File dir = new File("/home/vinisha/Documents/IRE/Shashank/ChampList");
//			if (! dir.exists()){
//		        dir.mkdir();
//		    }
			PrintWriter fileWriter =new PrintWriter(file,"UTF-8");
			if (!file.exists()) {
				file.createNewFile();
			}
			String list1 = reader.readLine();
			while(list1!=null) {
//				if(list.length()==0)continue;
//				System.out.println("For line number "+(++ctr));
//				System.out.println(s + "*");

				//***************************
				//list = list.replace("\n","");
				// int col = 0;for(int in=0;in<list.length();in++)if(list.charAt(in) == ';')col++;
				// if(col < 5 ) return list;
				// else System.out.println(col);
				


				// int total=0;
				// for(int i1=0;i1<list.length();i1++) {
				// 	if(list.charAt(i1) == ';')total++;
				// }
				// System.out.println(total);

				String word = list1.substring(0,list1.indexOf('-'));
				list1 = list1.substring(list1.indexOf('-')+1);
				

				String entries [] = list1.split(";");

				int total = entries.length;
//				System.out.println(total);
//				StringBuilder finalList = new StringBuilder();
				ArrayList <champNode> weightedList = new ArrayList<champNode> () ;
				// while(list.length()>0 && list.indexOf(';')!=-1) {
				String node;
				for(String nod : entries){

					// String nod = list.substring(0,list.indexOf(';')),node;
//					nod = entries[ei];
					if(nod.equals(""))continue;
					try {
					list1 = list1.substring(list1.indexOf(';')+1);
					}
					catch(Exception e)
					{
						System.out.println(e.getMessage());
					}
						
				
					//all processing here
					champNode new_node = new champNode();
					new_node.list =  nod  ;// = nod.substring(0,list.indexOf(':'));
					node = nod.substring(nod.indexOf(':')+1);
					int[] count = new int[6];
					int pres = -1,ind = 0,prev = 0;
					while(ind < node.length()) {
						pres =istag.get(node.charAt(ind));
						ind++;
						prev = ind;
						String num="";
//						System.out.println(node+"---"+ind);
						while(ind<node.length() && (node.charAt(ind)>='0' && node.charAt(ind)<='9' ||node.charAt(ind)>='a' && node.charAt(ind)<='f'))
						{
							num+=node.charAt(ind);
							ind++;
						}
//						if(ind<node.length())
//							System.out.println(node.charAt(ind));
						
//						while(ind<node.length() && istag(node.charAt(ind))==-1)ind++;
//						if(ind == node.length()) {count[pres] = (int) Long.parseLong(num, 16);break;}
						count[pres] = (int) Long.parseLong(num, 16);
					}

					new_node.net = count[0]*100+count[1]*5+count[2]*60+count[3]*50+count[4]*10+count[5]*10;
					
					new_node.tf = (double) (1/total );

					weightedList.add(new_node);
					
					//System.out.print('.');
				}
				Comp1 comp = new Comp1();
				Collections.sort(weightedList,comp);
				list1 = reader.readLine();
//				finalList.append(word+'-');
				
//				for(champNode x : weightedList) {
//					finalList.append(x.list+';');
//				}
//				fileWriter.wr(finalList.toString());
				//System.out.println();
			}
			
			offset=0;
			fileWriter.close();
		}
		
		System.out.println("Time elapsed for ChampList -->"+(System.currentTimeMillis()-cur) +"miliseconds");
		cur = System.currentTimeMillis();
		System.out.println("Total time elapsed for ChampList -->"+(System.currentTimeMillis()-real_start) +"miliseconds");
	}

}

package wikipedia_vinisha;
*/

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

class scoredoc {

	String docs;
	Integer score;
}

class Comp2 implements Comparator<scoredoc> {
	@Override
	public int compare(scoredoc o1, scoredoc o2) {
		return (int) (o2.score - o1.score);
		// return o1.score > o2.score ? -1 :(o1.score < o2.score ? 1 : 0); // Descending

	}
}

public class Shashank {
	public static void main(String[] args) throws Exception {	
	
//	public void rank() throws IOException {
//		@SuppressWarnings("resource")
//		BufferedReader reader = new BufferedReader(
//				new FileReader("/home/vinisha/Documents/IRE/IndexCreation/sampleOutput/FinalDatabase"));

		
//		File secondop = new File("/home/vinisha/Documents/IRE/Shashank/SecondDatabase");
//		if (secondop.exists())
//			secondop.createNewFile();
//		@SuppressWarnings("resource")
//		BufferedWriter wsec = new BufferedWriter(new FileWriter(secondop));
//
//		
//		File tertop = new File("/home/vinisha/Documents/IRE/Shashank/TertiaryIndex");
//		if (tertop.exists())
//			tertop.createNewFile();
//		@SuppressWarnings("resource")
//		BufferedWriter wter = new BufferedWriter(new FileWriter(tertop));

		
		//................................................
		
		for(int i=Integer.parseInt(args[0]);i<=Integer.parseInt(args[1]);i++) {
			
			String str = "Segmented_Index"+i+".txt";
		BufferedReader reader = new BufferedReader(
				new FileReader("./Index/"+str));

		File finalop = new File("./ChampList/"+str);
		File inup = new File("./Index/"+str);

		if(finalop.exists() && finalop.length() == inup.length())continue;

		System.out.println(str+" being made because" + finalop.exists() + " " + finalop.length() +"*"+ inup.length());

		if (finalop.exists())
			finalop.createNewFile();
		@SuppressWarnings("resource")
		BufferedWriter wpr = new BufferedWriter(new FileWriter(finalop));

		
		int countLines = 0;
		int countSec=0;
		String nextString = reader.readLine();
		long charCount = 0;
		long charSec=0;
		while (nextString != null) {

			String word = nextString.substring(0, nextString.indexOf('-'));
			String doclist = nextString.substring(nextString.indexOf('-') + 1);
			String[] docs = doclist.split(";");
			ArrayList<scoredoc> list = new ArrayList<scoredoc>();
			int bcount = 2, tcount = 1000, ecount = 1, ccount = 20, icount = 25, rcount = 1;
			for (String doc : docs) {
				// System.out.println("IIIIIII "+doc);
				if (doc.length() > 0)
					assignScoreAndSort(docs, list, bcount, tcount, ecount, ccount, icount, rcount, doc);
			}
			Collections.sort(list, new Comp2());

			
			
//			if (countLines % 100 == 0) {
//				if(countSec%100==0)
//				{
////					System.out.println("LLLLLLLLLLLLLLLLLLL");
//					wter.write(word+":"+charSec+"\n");
//				}
//				wsec.write(word + ":" + charCount+"\n");
//				String temp=""+charCount;
//				charSec+=word.length()+temp.length()+2;
//				countSec++;
//				
//			}

			wpr.write(word + "-");
			charCount+=word.length()+1;
			for (scoredoc sd : list) {
				wpr.write(sd.docs + ';');
				charCount += sd.docs.length() + 1;
			}
			charCount += 1;
			wpr.write("\n");
			
			nextString = reader.readLine();
			countLines++;

		}
		System.out.println("DONE" + countLines);
		wpr.close();
		}
//		wsec.close();
//		wter.close();
		System.out.println("DONE");
	}

	private static void assignScoreAndSort(String[] docs, ArrayList<scoredoc> list, int bcount, int tcount, int ecount,
			int ccount, int icount, int rcount, String doc) {
		scoredoc sd = new scoredoc();
		sd.docs = doc;
		if (doc.contains(":")) {
			String fields = doc.substring(doc.indexOf(':')+1);
			int title = 0, body = 0, info = 0, ref = 0, cat = 0, ext = 0;

			char cur = fields.charAt(0);
			for (int i = 1; i < fields.length();) {
				String num = "";
				while (i < fields.length() && (fields.charAt(i) >= '0' && fields.charAt(i) <= '9' || fields.charAt(i) >= 'a' && fields.charAt(i) <= 'f')) {
					num += fields.charAt(i);
					i++;
				}

				// System.out.println(num+" " + cur +"\n");
				if (cur == 't')
					title = Integer.parseInt(num,16);
				if (cur == 'b')
					body = Integer.parseInt(num,16);
				if (cur == 'i')
					info = Integer.parseInt(num,16);
				if (cur == 'r')
					ref = Integer.parseInt(num,16);
				if (cur == 'c')
					cat = Integer.parseInt(num,16);
				if (cur == 'e')
					ext = Integer.parseInt(num,16);

				if (i < fields.length())
					cur = fields.charAt(i);
				i++;
			}
			sd.score = title * tcount + body * bcount + info * icount + ref * rcount + ext * ecount + cat * ccount;
			// System.out.println(sd.docs+":"+sd.score+"\n");
//			sd.score *= (Sample.count / docs.length);
			// System.out.println(Sample.count);
			list.add(sd);
		}
	}
}
