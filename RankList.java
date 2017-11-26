import java.util.ArrayList;
import java.util.Collections;
import java.util.Map.Entry;
import java.util.TreeMap;


class postingItem {
	String list;
	Double net;
	Double tf;
	Double score;
	

	postingItem(String list){
		this.list = list;
		this.score = 0.0;
		String doc = list.substring(0,list.indexOf(':'));
		list = list.substring(list.indexOf(':')+1);
		int i=0;
		int[] count = new int[6];
		int pres = -1,ind = 0,prev = 0;
//		System.out.println(list);
		while(ind < list.length()) {
//			pres = ScoreGenerator.istag(list.charAt(ind));
//			ind++;
//			prev = ind;
//			while(ind<list.length() && ScoreGenerator.istag(list.charAt(ind))==-1)ind++;
//			if(ind == list.length()) {count[pres] = (int) Long.parseLong(list.substring(prev), 16);break;}
//			else count[pres] = (int) Long.parseLong(list.substring(prev,ind), 16);
//			System.out.println(ind+"*"+pres+"*"+list.substring(prev,ind));
//			if(count[pres]==0)count[pres]++;
			pres = ScoreGenerator.istag(list.charAt(ind));
			ind++;
			prev = ind;
			String s = "";
			while(ind<list.length() && ScoreGenerator.istag(list.charAt(ind))==-1){s+=list.charAt(ind);ind++;}//System.out.println(ind+"<-"+s);}
			count[pres] = (int) Long.parseLong(s, 16);
//			System.out.println(list+"*"+ind+"*"+pres+"*"+s+"*"+count[pres]);
		}
//		System.out.println(count[0]+ " "+count[1]+ " "+count[2]+ " "+count[3]+ " "+count[4]+ " "+count[5]+"******");
		this.net = new Double(count[0]*100+count[1]*5+count[2]*10+count[3]*50+count[4]*10+count[5]*10);
		
	}
	postingItem(String list,int diff){
		this.list = list;
		this.score = 0.0;
		String doc = list.substring(0,list.indexOf(':'));
		list = list.substring(list.indexOf(':')+1);
		int i=0;
		int[] count = new int[6];
		int pres = -1,ind = 0,prev = 0;
//		System.out.print(list);
		while(ind < list.length()) {
//			pres = ScoreGenerator.istag(list.charAt(ind));
//			ind++;
//			prev = ind;
//			while(ind<list.length() && ScoreGenerator.istag(list.charAt(ind))==-1)ind++;
//			if(ind == list.length()) {count[pres] = (int) Long.parseLong(list.substring(prev), 16);break;}
//			else count[pres] = (int) Long.parseLong(list.substring(prev,ind), 16);
//			System.out.println(ind+"*"+pres+"*"+list.substring(prev,ind));
//			if(count[pres]==0)count[pres]++;
			pres = ScoreGenerator.istag(list.charAt(ind));
			ind++;
			prev = ind;
			String s = "";
			while(ind<list.length() && ScoreGenerator.istag(list.charAt(ind))==-1){s+=list.charAt(ind);ind++;}//System.out.println(ind+"<-"+s);}
			count[pres] = (int) Long.parseLong(s, 16);
//			System.out.println(list+"*"+ind+"*"+pres+"*"+s+"*"+count[pres]);
		}
		
		this.net = new Double(count[0]*100+count[1]*100+count[2]*100+count[3]*100+count[4]*100+count[5]*100);
//		System.out.println(this.net+"<<<NET");
//		this.net = (double) (count[diff]*1000);
	}
}


class grain implements Comparable<grain>{
	String file;
	Double score;
	grain(Double score,String file){
		this.file=file;
		this.score = score;
	}
	@Override
	public int compareTo(grain o) {	
		if(o.score < this.score) return 1;
		else if (o.score > this.score) return -1;
		return 0;
	}
}



public class RankList {
	
	public static TreeMap <String,Double> map = new TreeMap <String,Double>();
	Boolean marker = false;
	
	
	void parse(String list,int make,int ct) {
		if(list.indexOf('-')==-1)return;
		Double tc = (double) (ct*.01);
		String backup = list;
		
		list = list.replace("\n","");
		String word = list.substring(0,list.indexOf('-'));
		list = list.substring(list.indexOf('-')+1);
//		System.out.println(list+"\n____"+make+"\n_____"+tc);
		if(make==1) {
			int ctr = 1 ;
			Double weight = 1.0;
			while(list.length()>0 && list.indexOf(';')!=-1) {
				String nod = list.substring(0,list.indexOf(';'));
				if(nod.equals("") || (!nod.contains(""+':')))continue;
				list = list.substring(list.indexOf(';')+1);
//				weightedList.add(new postingItem(nod));
				postingItem x;
				if(marker==false)x =  new postingItem(nod);
				else  x =  new postingItem(nod,0);
				String key = x.list.substring(0,x.list.indexOf(":"));
//				Double score =(double) ( weight - .001*weight > 2.5 ?weight - .001*weight:2.5);
				Double score = 0.0;
//				System.out.println(nod+"<<<<<<<RankedNODE");
				
				if(map.get(key)==null) {
					if(marker==false)x =  new postingItem(nod);
					else  x =  new postingItem(nod,0);
					if(map.get(key)==null) {
						map.put(key, x.net);
					}
				}
				
				weight = map.get(key);
				if(ctr <=100 )score = weight * .5;
				else if(ctr <=100 )score = weight * .30;
				else if(ctr <=1000 )score = weight * .15;
				else break;
				if(map.get(key)==null) {
					x =  new postingItem(nod);
//					map.put(key, x.net);
					map.put(key, x.net/tc);
//					System.out.print(map.get(key)+"->");
				}
				
				map.put(key,map.get(key)+score/tc); 
//				System.out.println(key+"***"+weight+ "<->" +score/tc+"****"+map.get(key)+"***"+ctr);
				ctr++;	
			}
			return;
		}
		
		list = backup;
		list = list.replace("\n","");
		word = list.substring(0,list.indexOf('-'));
		list = list.substring(list.indexOf('-')+1);
		while(list.length()>0 && list.indexOf(';')!=-1) {
			String nod = list.substring(0,list.indexOf(';'));
			list = list.substring(list.indexOf(';')+1);
			if(nod.equals("") || (!nod.contains(":"))) {continue;}
			
			// System.out.println(nod+"<<<<<<<NODE");
			postingItem x ;
			if(marker==false)x =  new postingItem(nod);
			else  x =  new postingItem(nod,0);
			String key = x.list.substring(0,x.list.indexOf(":"));
			if(map.get(key)==null) {
//				System.out.println(x.list+"*****"+x.net);
				map.put(key, x.net/tc);
//				map.put(key, x.net);
			}
			else {
				Double score = map.get(key);
				map.put(key,(score)+x.net/tc);
//				map.put(key,(score)+x.net);
			}
			// System.out.println(key+"-*-"+map.get(key)+"***"+ queryHandler.getTitle(key));
		}
		
	}
	
	
	
	
	TreeMap<String,Integer > qq =new TreeMap <String,Integer> ();
	
	RankList(){
		//postings,rankedPostings
		
		for(String x:queryHandler.postings) {
			if((!x.contains("-"))&&(!x.contains(":")))continue;
//			System.out.println("**"+x);
			if(!x.contains("-")) {
				int ind=x.indexOf(':');
				String ret=x.substring(0,ind)+'-'+x.substring(ind+1);
				x=ret;
			}
			
			int ct=0; for(int i=0;i<x.length();i++)if(x.charAt(i)==';')ct++;
//			System.out.println("**");
			qq.put(x.substring(0,x.indexOf("-")), ct);
			parse(x,0,ct);
		}
//System.out.println("Fuckkk");
		for(String x:queryHandler.rankedPostings) {
			int ct=0; for(int i=0;i<x.length();i++)if(x.charAt(i)==';')ct++;
			parse(x,1,ct);
		}

	}

	RankList(int opopopopo){
		//postings,rankedPostings
		marker = true;
		for(String x:queryHandler.postings) {
			if((!x.contains("-"))&&(!x.contains(":")))continue;
//			System.out.println("**"+x);
			if(!x.contains("-")) {
				int ind=x.indexOf(':');
				String ret=x.substring(0,ind)+'-'+x.substring(ind+1);
				x=ret;
			}
			
			int ct=0; for(int i=0;i<x.length();i++)if(x.charAt(i)==';')ct++;
//			System.out.println("**");
			qq.put(x.substring(0,x.indexOf("-")), ct);
			parse(x,0,ct);
		}
//System.out.println("Fuckkk");
		// for(String x:queryHandler.rankedPostings) {
		// 	parse(x,1,0);
		// }

	}
	
	ArrayList <String> getTop10(){
		ArrayList <grain> li = new ArrayList<>() ;
		ArrayList <String> ret = new ArrayList<>() ;
		for(Entry<String, Double> ent : map.entrySet()) {
			grain x = new grain(ent.getValue(),ent.getKey());
			///qq.get(ent.getKey().substring(ent.getKey().lastIndexOf("-")))
			li.add(x);
		}
		
		Collections.sort(li,Collections.reverseOrder());
		
		int ctr = 0;
		for(grain x : li) {
			System.out.println(x.file+ "-*-" + x.score + "_*_" + queryHandler.getTitle(x.file)+"***********");
			ret.add(x.file);
			ctr++;
			if(ctr >= 20)break;
		}
		return ret;
	}
	
}
