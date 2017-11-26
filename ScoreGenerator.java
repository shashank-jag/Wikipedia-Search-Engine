import java.io.BufferedReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;

class champNode implements Comparable<champNode>{
	String list;
	int net;
	Double tf;
	@Override
	public int compareTo(champNode o) {
		if(o.net*(o.tf) > this.net*(this.tf)) return 1;
		else if (o.net*(o.tf) < this.net*(this.tf)) return -1;
		return 0;
		
	}
}

public class ScoreGenerator {
	
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
	
	static public int istag(char i) {
		if (i=='t')return 0;
		if (i=='T')return 1; 
		if (i=='i')return 2; 
		if (i=='C')return 3;
		if (i=='r')return 4 ;
		if (i=='E')return 5;
		return -1;
	}	
	
	public static void main(String[] args) throws IOException, InterruptedException {
		long real_start = System.currentTimeMillis(),cur = real_start;
		String list;		
		System.out.println("Generating Champion list #B-)");
		File f = new File("./Index");// your folder path
		String[] fileList = f.list(); // It gives list of all files in the folder.
		
		long ctr=0,offset=0,offset_ter=0;
		
		for(int i=Integer.parseInt(args[0]),stp=0 ; i<=Integer.parseInt(args[1]) && stp < 100;i++) {
			String str = "Segmented_Index"+i+".txt";
			File fChk = new File("./Index/"+str);
			if(!fChk.exists()){
				// TimeUnit.SECONDS.sleep(10);
				System.out.println(str);
				Thread.sleep(4000);
				i--;
				stp++;
				continue;
			}
			stp=0;
//			System.out.println(str);
			// if(str.charAt(0)!='S')continue;
			System.out.println("Generating Posting for "+ str);
			BufferedReader reader = new BufferedReader(new FileReader("./Index/"+str));
			File file = new File("./ChampList/"+str);
			File dir = new File("./ChampList");
			if (! dir.exists()){
		        dir.mkdir();
		    }
			PrintWriter fileWriter =new PrintWriter(file,"UTF-8");
			if (!file.exists()) {
				file.createNewFile();
			}
			while((list=reader.readLine())!=null) {
				if(list.length()==0)continue;
				System.out.println("For line number "+(++ctr));
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

				String word = list.substring(0,list.indexOf('-'));
				list = list.substring(list.indexOf('-')+1);
				

				String entries [] = list.split(";");

				int total = entries.length;
				System.out.println(total);
				StringBuilder finalList = new StringBuilder();
				ArrayList < champNode > weightedList = new ArrayList<champNode> () ;
				// while(list.length()>0 && list.indexOf(';')!=-1) {
				String nod = "",node;
				for(int ei=0 ; ei < entries.length ; ei++){

					// String nod = list.substring(0,list.indexOf(';')),node;
					nod = entries[ei];
					if(nod.equals(""))continue;
					list = list.substring(list.indexOf(';')+1);
				
					//all processing here
					champNode new_node = new champNode();
					new_node.list =  nod  ;// = nod.substring(0,list.indexOf(':'));
					node = nod.substring(nod.indexOf(':')+1);
					int[] count = new int[6];
					int pres = -1,ind = 0,prev = 0;
					while(ind < node.length()) {
						pres =istag(node.charAt(ind));
						ind++;
						prev = ind;
						while(ind<node.length() && istag(node.charAt(ind))==-1)ind++;
						if(ind == node.length()) {count[pres] = (int) Long.parseLong(node.substring(prev), 16);break;}
						else count[pres] = (int) Long.parseLong(node.substring(prev,ind), 16);
					}

					new_node.net = count[0]*100+count[1]*5+count[2]*60+count[3]*50+count[4]*10+count[5]*10;
					
					new_node.tf = (double) (1/total );

					weightedList.add(new_node);
					//System.out.print('.');
				}
				Collections.sort(weightedList);
				
				finalList.append(word+'-');
				
				for(champNode x : weightedList) {
					finalList.append(x.list+';');
				}
				fileWriter.println(finalList.toString());
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
