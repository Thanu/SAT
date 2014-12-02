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
	
	public static String[] getSynSetWords(String term){
		System.setProperty("wordnet.database.dir", "E:\\SAT\\sat\\wordNet\\dict\\");		
			String wordForm = term;
			//  Get the synsets containing the wrod form
			WordNetDatabase database = WordNetDatabase.getFileInstance();
			Synset[] synsets = database.getSynsets(wordForm);
			//  Display the word forms and definitions for synsets retrieved
			if (synsets.length > 0)
			{
				for (int i = 0; i < synsets.length; i++)
				{
					System.out.println("");
					wordForms = synsets[i].getWordForms();
					for (int j = 0; j < wordForms.length; j++)
					{
						System.out.print((j > 0 ? ", " : "") +
								wordForms[j]);
					}
					
				}
			}
			else
			{
				System.err.println("No synsets exist that contain " +
						"the word form '" + wordForm + "'");
			}
		
		return wordForms;
	}
	
	
	public static boolean checkSymilarity(String term1,String term2){
		boolean status = false;
		String[] similarWordForTerm1 = getSynSetWords(term1);
		String[] similarWordForTerm2 = getSynSetWords(term2);
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
		
		}
		
		if(isFirstletterChanged(term1,term2) ){
			return false;
		}
		
		else if(status || 
				term1.equalsIgnoreCase(term2) ||
						LevenshteinDistance.similarity(term1, term2)>.85){
			return true;
		}
		else
			return false;
		
		
	}
	
	public static boolean isFirstletterChanged(String term1,String term2){
		if(term1.substring(1).equalsIgnoreCase(term2.substring(1))  && term1.charAt(0)==term2.charAt(0) ){
			return true;
		}else
			return false;
		
	}
	
	

}
