import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map.Entry;
import java.util.TreeMap;




class grain_ implements Comparable<grain_>{
	String file;
	Double score;
	@Override
	public int compareTo(grain_ o) {
		// TODO Auto-generated method stub
		return 0;
	}
}

class Comp3 implements Comparator<grain_> {
	@Override
	public int compare(grain_ o1, grain_ o2) {
		return (int) (o2.score - o1.score);
		// return o1.score > o2.score ? -1 :(o1.score < o2.score ? 1 : 0); // Descending

	}
}




public class finalRanker {
	
	public static TreeMap <String,Double> map = new TreeMap <String,Double>();
	Boolean marker = false;
	
	
	
	void parsePost(String list){
		int bcount = 20, tcount = 1000, ecount = 1, ccount = 20, icount = 25, rcount = 60;
		String word = list.substring(0,list.indexOf('-'));
		list = list.substring(list.indexOf("-")+1);
		String docs [] = list.split(";");
		Double score = 0.0;
		for(String doc : docs){
			if(doc.indexOf(":")==-1)continue;
			String ent = doc.substring(0,doc.indexOf(":"));
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
			score = (double) (title * tcount + body * bcount + info * icount + ref * rcount + ext * ecount + cat * ccount);
			if(map.get(ent)==null)map.put(ent,score);
			else map.put(ent,score+map.get(ent));
		}
	}



	void parseRank(String list){
		String key = list.substring(0,list.indexOf("-"));
		list = list.substring(list.indexOf("-")+1);
		String docs [] = list.split(";");
		int ctr=0;
		Double score =0.0;
		for(String doc : docs){
			if(doc.indexOf(":")==-1)continue;
			String ent = doc.substring(0,doc.indexOf(":"));
			String fields = doc.substring(doc.indexOf(':')+1);
			
			if(map.get(ent)==null){
				//new_node.net = count[0]*100+count[1]*5+count[2]*60+count[3]*50+count[4]*10+count[5]*10;
				int bcount = 5, tcount = 100, ecount = 60, ccount = 50, icount = 10, rcount = 10;
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
				score = (double) (title * tcount + body * bcount + info * icount + ref * rcount + ext * ecount + cat * ccount);
				if(map.get(ent)==null)map.put(ent,score);
				else map.put(ent,score+map.get(ent));
			}
		
			Double weight = map.get(ent);
			if(ctr <=5 )score = weight * .5;
			else if(ctr <=10 )score = weight * .30;
			else if(ctr <=100 )score = weight * .15;

			ctr ++ ;
			map.put(ent,map.get(ent)+score); 
				
		}
	}



	
	// TreeMap<String,Integer > qq =new TreeMap <String,Integer> ();
	
	finalRanker(Boolean flag){
//		System.out.println(flag);
		for(String x:finalQueryHandler.postings) {
			if((!x.contains("-"))&&(!x.contains(":")))continue;
			if(!x.contains("-")) {
				int ind=x.indexOf(':');
				String ret=x.substring(0,ind)+'-'+x.substring(ind+1);
				x=ret;
			}
			parsePost(x);
		}

		if(flag){
			for(String x:finalQueryHandler.rankedPostings) {
				if((!x.contains("-"))&&(!x.contains(":")))continue;
				if(!x.contains("-")) {
					int ind=x.indexOf(':');
					String ret=x.substring(0,ind)+'-'+x.substring(ind+1);
					x=ret;
				}
				parseRank(x);
			}
		}

	}

	
	
	ArrayList <String> getTop10(){
		ArrayList <grain_> li = new ArrayList<>() ;
		ArrayList <String> ret = new ArrayList<>() ;
		for(Entry<String, Double> ent : map.entrySet()) {
			grain_ x = new grain_();
			x.score = ent.getValue();
			x.file  = ent.getKey();
			li.add(x);
		}
		
//		Collections.sort(li,new Comp3());
		Collections.sort(li, new Comp3());
		
		int ctr = 0;
		Boolean fl = true;
		for(grain_ x : li) {
			fl = true;
//			try {
//				String s = finalQueryHandler.getTitle(x.file);
//			}
//			catch(Exception e) {
//				fl =false;
//			}
			System.out.println(x.file+ "-*-" + x.score + "_*_" + finalQueryHandler.getTitle(x.file)+"***********");
			if(fl)ret.add(x.file);
			ctr++;
			if(ctr >= 20)break;
		}
		return ret;
	}
	
}
