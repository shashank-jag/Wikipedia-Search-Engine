import java.io.*;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.concurrent.CountDownLatch;

class pqNode {
	String word;
	String list;
	int id;
}

class Stringcomp implements Comparator<pqNode>{
	public int compare(pqNode x, pqNode y) 
    { 
        return x.word.compareTo(y.word);
} 
}

public class mergerIndex {
	int total_docs,docs_done;
	int countOfWords ;
	int countOfDistWords ;
	int indexCounter ;
	int secondaryCounter;
	int secJump;
	int terJump;
	int totalPerFile;
	
	
	BufferedReader fileReader[] ;
	boolean[] toRead ;
	Stringcomp comparator=new Stringcomp();
	PriorityQueue<pqNode> pq = new PriorityQueue<pqNode>(saxParser.count+2,comparator);
	String outputDir; 
	String read,word,posting;
	public StringBuilder text = new StringBuilder();
	public StringBuilder secondary = new StringBuilder();
	public StringBuilder ternary = new StringBuilder();
	File fileSec,fileTer;
	PrintWriter fileWriterSec,fileWriterTer; 
	
	
	mergerIndex(String inFolder,int sec,int ter,int net) throws IOException, InterruptedException{
		totalPerFile=net;
		secJump=sec;
		terJump=ter;
		countOfWords = 0;
		indexCounter = 1;
		secondaryCounter = 0;
		docs_done = 0;
		total_docs = 0;
		outputDir = inFolder;
		File dir = new File("./Partial"+outputDir);
		if (! dir.exists()){
	        dir.mkdir();
	    }
		
		File[] listOfFiles = dir.listFiles();
		total_docs = listOfFiles.length;
		
		fileReader = new BufferedReader[total_docs+2];
		toRead = new boolean[total_docs+2];
		
		int ct = 0;
		for(int i=1;i<=total_docs;i++) {
//			if(listOfFiles[i-1].toString().charAt(0)=='P')toRead[i] = true;
//			else {ct++;System.out.print(listOfFiles[i-1].toString().charAt(0));}
			if(listOfFiles[i-1].toString().contains("Partial"))toRead[i] = true;
			else {ct++;}
		}
		
		total_docs-=ct;
		
		for(int i=1;i<=total_docs;i++) {
//			System.out.println();
//			String cmd = "ls -al";
//			Runtime run = Runtime.getRuntime();
//			Process pr = run.exec(cmd);
//			pr.waitFor();
//			BufferedReader buf = new BufferedReader(new InputStreamReader(pr.getInputStream()));
//			String line = "";
//
//			while ((line=buf.readLine())!=null) {
//
//			System.out.println(line);
//
//			}
			
			fileReader[i] = new BufferedReader(new FileReader("./Partial"+outputDir + '/' + "Partial"+outputDir + '_' + (i) + ".txt"));
		}
		
		//..........secondary list
		String outputFile = "./Secondary"+outputDir+".txt";
		fileSec = new File(outputFile);
		try {
//			System.out.println(outputFile);
			fileSec.createNewFile();
			fileWriterSec=new PrintWriter(fileSec,"UTF-8");
		}
		catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		
		
		//..........ternary initilization
		outputFile = "./Ternary"+outputDir+".txt";
		fileTer= new File(outputFile);
		try {
//			System.out.println(outputFile);
			fileTer.createNewFile();
			fileWriterTer =new PrintWriter(fileTer,"UTF-8");
		}
		catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		
	}
	
	
	void mergeWriter(int index) {
		String outputFile = "./"+outputDir + "/Segmented_" + outputDir + index + ".txt";
		File file = new File(outputFile);
		String outputDir_ = "./"+outputDir;
		File dir = new File(outputDir_);
		if (! dir.exists()){
	        dir.mkdir();
	    }
		try {
//			System.out.println(outputFile);
			file.createNewFile();
			PrintWriter fileWriter =new PrintWriter(file,"UTF-8");
			fileWriter.print(text);
			
//			String s = text.substring(0,text.indexOf("-"))+"-"+index+"\n";
//			System.out.println(s);
//			secondary.append(s);
			text.setLength(0);
			fileWriter.close();
		}
		catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}
	
	void writeSecondary() {
		String outputFile = "./Secondary"+outputDir+".txt";
		File file = new File(outputFile);
		try {
//			System.out.println(outputFile);
			file.createNewFile();
			String secondaryString = secondary.toString();
			PrintWriter fileWriter =new PrintWriter(file,"UTF-8");
			fileWriter.println(secondaryString);
			fileWriter.close();
		}
		catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}
	
	void writeTernary() {
		String outputFile = "./Ternary"+outputDir+".txt";
		File file = new File(outputFile);
		try {
//			System.out.println(outputFile);
			file.createNewFile();
			String ternaryString = ternary.toString();
			PrintWriter fileWriter =new PrintWriter(file,"UTF-8");
			fileWriter.println(ternaryString+'\n');
			fileWriter.close();
		}
		catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
	}
	
	
	void deletePartialIndex() {
		System.out.print("Removing thrash");
		File index = new File("./Partial"+outputDir);
		String[]entries = index.list();
		for(String s: entries){
		    File currentFile = new File(index.getPath(),s);
		    currentFile.delete();
		    System.out.print('.');
		}
		index.delete();
		System.out.println();
	}
	
	
	void go() throws IOException {
		
		System.out.println("Begining to Merge-----> *_*<------"+outputDir);
		long real_start = System.currentTimeMillis();
		for(int i=1;i<=total_docs;i++) {
			if(toRead[i]) {
				read = fileReader[i].readLine();
				if(read == null) {
					docs_done += 1;
					System.out.println("File "+i+" is done (^_^)");
					toRead[i] = false;
					fileReader[i].close();
				}
				else {
					pqNode no = new pqNode();
					no.word = read.substring(0,read.indexOf('-'));
					no.list = read.substring(read.indexOf('-')+1);
					no.id = i;
					pq.add(no);
				}
			}
		}
	
//		System.out.println(docs_done+"<--->"+total_docs);
		
		long offsetReal = 0,offsetSec = 0;
		


		String outputFile = "./"+outputDir + "/Segmented_" + outputDir + indexCounter + ".txt";
		File file = new File(outputFile);
		String outputDir_ = "./"+outputDir;
		File dir = new File(outputDir_);
		if (! dir.exists()){
	        dir.mkdir();
	    }
		try {
//			System.out.println(outputFile);
			file.createNewFile();

		}
		catch (Exception e){
			System.out.println("File could not be created.......");
		}

		PrintWriter fileWriter = new PrintWriter(file,"UTF-8");
		int counterOfByte = 0;



//...........champ list

		// Boolean fl = outputDir.equalsIgnoreCase("Index");
		// file = new File("./ChampList/"+ "Segmented_" + outputDir + indexCounter + ".txt");
		// dir = new File("./ChampList");
		// if (! dir.exists()){
		//     dir.mkdir();
		// }
		// PrintWriter fileWriter_Champ =new PrintWriter(file,"UTF-8");
		// if (fl && (!file.exists())) {
		// 	file.createNewFile();
		// }



		



		while(docs_done<total_docs && pq.size() > 0 ) {
			word = pq.peek().word;
			posting = "";
			pqNode x = null;
			while(docs_done<total_docs && (x=pq.poll()).word.equals(word)  && pq.size() > 0 ) {
				read = fileReader[x.id].readLine();
				if(read == null) {
					docs_done += 1;
					toRead[x.id] = false;
					fileReader[x.id].close();
				}
				else {
					pqNode no = new pqNode();
					no.word = read.substring(0,read.indexOf('-'));
					no.list = read.substring(read.indexOf('-')+1);
					no.id = x.id;
					pq.add(no);
				}
				posting += x.list.replace("\n", "");
				countOfWords++;
				x=null;
			}
			if(pq.size()>0)pq.add(x);
			posting = posting.replace("\n", "");
//			System.out.println(posting);
//			System.out.println("***********"+x.word);
			String wr = word+'-'+posting+'\n';
			// text.append(wr);
			// System.out.print("*"+indexCounter+"*");
			fileWriter.print(wr);
			// System.out.print(indexCounter+"*");
//			System.out.println(text);
			// String put;

			// if(fl){
			// 	put = ScoreGenerator.createChamp(wr);
			// 	fileWriter_Champ.print(put);
			// }
			// System.out.print(indexCounter+"*_");



			//Make Index.....
			
			
			if(countOfDistWords % secJump == 0) {
//				System.out.print("*");
				// String s = word + "-" + indexCounter + "-" + text.lastIndexOf(word);
				String s = word + "-" + indexCounter + "-" + offsetReal;
//				System.out.println(s);
//				secondary.append(s);
				fileWriterSec.println(s);
				if(secondaryCounter % terJump==0) {
//					System.out.print("-");
					String st = word + "-" + offsetSec;
//					ternary.append(s);
					fileWriterTer.println(st);
				}
				secondaryCounter ++ ;
				offsetSec += s.length() + 1;
			}
			offsetReal += wr.length() + 1;	
			countOfDistWords++;
			
//			if(countOfWords>2500) {
//			countOfWords%=2500;
//			mergeWriter(indexCounter);
//			indexCounter++;
//		    }
			
			if(countOfDistWords>=totalPerFile) {
				countOfDistWords = 0;
				secondaryCounter = 0;
				offsetReal = 0;
				// mergeWriter(indexCounter);

				fileWriter.close();
				indexCounter++;
				outputFile = "./"+outputDir + "/Segmented_" + outputDir + indexCounter + ".txt";
				file = new File(outputFile);
				try {
					file.createNewFile();

				}
				catch (Exception e){
					System.out.println("File could not be created.......");
				}
				fileWriter = new PrintWriter(file,"UTF-8");

				// fl=false;
				// if(fl){
				// 	fileWriter_Champ.close();
				// 	file = new File("./ChampList/"+ "Segmented_" + outputDir + indexCounter + ".txt");
				// 	try {
				// 		file.createNewFile();

				// 	}
				// 	catch (Exception e){
				// 		System.out.println("File could not be created.......");
				// 	}
				// 	fileWriter_Champ = new PrintWriter(file,"UTF-8");
				// 	System.out.print("ChampList number "+(indexCounter-1)+" made  ");
				// }


				System.out.print(outputDir + " number "+(indexCounter-1)+" made  ");
				System.out.println("In-->"+(System.currentTimeMillis()-real_start)+" Total---->"+(System.currentTimeMillis()-index_creater.real_start));
				real_start = System.currentTimeMillis();
				
			}
			
//			System.out.println("=====****(*_*)****=====");
			
		}
		
		
		
		// mergeWriter(indexCounter);
		fileWriter.close();
		// fileWriter_Champ.close();
		// deletePartialIndex();
		
		
		fileWriterSec.close();
		fileWriterTer.close();
//		writeSecondary();
//		writeTernary();
		
	}
}
