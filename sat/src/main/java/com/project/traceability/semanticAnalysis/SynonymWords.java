package com.project.traceability.semanticAnalysis;

import com.project.traceability.ir.LevenshteinDistance;

import edu.smu.tspell.wordnet.Synset;
import edu.smu.tspell.wordnet.WordNetDatabase;

/**1 Dec 2014
 * @author K.Kamalan
 *
 */
public class SynonymWords {
	
	public static String[] wordForms;
	public static String simpleWord1,simpleWord2 ;
	public static WordNetDatabase database = WordNetDatabase.getFileInstance();
	public static String[] getSynSetWords(String term){
		System.setProperty("wordnet.database.dir", "C:\\Program Files (x86)\\WordNet\\2.1\\dict");		
			String wordForm = term;
			//  Get the synsets containing the wrod form
//			System.out.println(term);
			Synset[] synsets = database.getSynsets(wordForm);
			//  Display the word forms and definitions for synsets retrieved
			if (synsets.length > 0)
			{
				for (int i = 0; i < synsets.length; i++)
				{
//					System.out.println("");
					wordForms = synsets[i].getWordForms();
					for (int j = 0; j < wordForms.length; j++)
					{
//						System.out.print((j > 0 ? ", " : "") +
//								wordForms[j]);
					}
					
				}
			}
			else
			{
//				System.err.println("No synsets exist that contain " +
//						"the word form '" + wordForm + "'");
			}
		
		return wordForms;
	}
	
	
	
	
	
	public static boolean checkSymilarity(String term1,String term2){
		//if the term cotains sub string "NO" at last then change the sub string to "NUMBER"
 		if(term1.substring(term1.length()-2).equalsIgnoreCase("No")){
 			simpleWord1 =  term1.replace(term1.substring(term1.length()-2), "Number");
// 			System.out.println("KAMALABALAN"+simpleWord1); 					
		}else
			simpleWord1 = term1;
 		if(term2.substring(term2.length()-2).equalsIgnoreCase("No")){
 			simpleWord2 = term2.replace(term2.substring(term2.length()-2), "Number");
// 			System.out.println("KAMALABALAN"+simpleWord2);
		}else
			simpleWord2 = term2;
		
		
		//remove get or set word from term to check similarity from term1
		if(term1.contains("get") || term1.contains("set")){
			simpleWord1 = simpleWord1.substring(3);
//			System.out.println("************************"+simpleWord1);						
		}
		
		//remove get or set word from term to check similarity from term2
		if(term2.contains("get") || term2.contains("set")){
			simpleWord2 = simpleWord2.substring(3);
//			System.out.println("************************"+simpleWord2);			
		}
		
		boolean status = false;
//		System.out.println(simpleWord1+" KAMAL "+simpleWord2);
		
		//get similar words from WordNet dictionary
		String[] similarWordForTerm1 = getSynSetWords(simpleWord1);
		String[] similarWordForTerm2 = getSynSetWords(simpleWord2);
		
		//compare words which get from word net dictionary to get the relationship files
		if(similarWordForTerm1 !=null && similarWordForTerm2!=null){
		for(int i=0;i<similarWordForTerm1.length;i++){
			for(int j=0;j<similarWordForTerm2.length;j++){
				if(similarWordForTerm1[i].equalsIgnoreCase(similarWordForTerm2[j])){
					status = true;
					break;
				}
			}
			if(status)
				break;
		}
		
		}else if(similarWordForTerm1==null && similarWordForTerm2 != null){
			for(int i=0;i<similarWordForTerm2.length;i++){
				if(simpleWord1.equalsIgnoreCase(similarWordForTerm2[i])){
					status = true;
					break;
				}
			}
			
		}else if(similarWordForTerm2 == null && similarWordForTerm1 !=null){
			for(int i=0;i<similarWordForTerm1.length;i++){
				if(simpleWord2.equalsIgnoreCase(similarWordForTerm1[i])){
					status = true;
					break;
				}
			}
			
		}else{
			status = false;
		}
		
		
		
		if(isFirstletterChanged(term1,term2) ){
			return false;			
		}else if(term1.equalsIgnoreCase(term2) || LevenshteinDistance.similarity(term1, term2)>.85){
			return true;
		}else
			return false;
		
		
//		System.out.println(status);
		
		//check only 1st letter changed remaining unchanged 
//		if(isFirstletterChanged(term1,term2) ){
//			return false;
//		}
//		//check similarity get the edit distance & if >.85 then it will be ok
//		else if( 
//				term1.equalsIgnoreCase(term2) ||
//						LevenshteinDistance.similarity(term1, term2)>.85){
//			
//			System.out.println(term1.equalsIgnoreCase(term2)+" : "+LevenshteinDistance.similarity(term1, term2)+" : "+ term1+"**************"+term2+"TRUE");
//			return true;
//		}
//		else
//			return false;
		
		
	}
	
	
	
	
	public static boolean isFirstletterChanged(String term1,String term2){
		if(term1.substring(1).equalsIgnoreCase(term2.substring(1))  && term1.charAt(0)!=term2.charAt(0) ){
//			System.out.println(term1+" : "+term2);
			return true;
		}else
			return false;
		
	}
	
	

}
