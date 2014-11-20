/**
 * OBJECTIVE
 *   Implement a vector space retrieval system using a UIMA framework
 *   
 * 
 * DESCRIPTION
 * This is the main function of this retrieval system.
 * The analysis engine is SentenceAnalysis
 * It composed of three analysis components, i.e.
 * 1. DocumentReader  :: identify the query id and relevant scores
 * 2. DocumentVectorAnnotator    :: identify the word and word frequency in each sentence
 * 3. RetrievalEvaluator :: compute the similarity and performance metric of the collection 
 * 
 * 
 * PROCEDURE 
 * This system is fully functional but it is not working correctly.
 * The input text collection is available at "dat_tiny".
 * Your tasks are
 * 0. Correctly extract bag of words feature vector from the input text collection
 * 1. Compute the cosine similarity between two sentences in the text collection
 * 2. Compute the Mean Reciprocal Rank (metric) for all sentences in the text collection
 * 3. (optional) Improve the efficiency of the program and reduce the runtime of the retrieval system
 * 
 */
package edu.cmu.lti.Intelligent_Information_System__Baseline_Question_Answer_and_Evaluation_System_;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.analysis_engine.AnalysisEngineDescription;
import org.apache.uima.cas.CAS;
import org.apache.uima.util.XMLInputSource;

public class VectorSpaceRetrieval {
	
	public static void main(String [] args) 
			throws Exception {
			
		String sLine;
		long startTime=System.currentTimeMillis();
		
		URL descUrl = VectorSpaceRetrieval.class.getResource("/descriptors/retrievalsystem/VectorSpaceRetrieval.xml");
	   if (descUrl == null) {
	      throw new IllegalArgumentException("Error opening VectorSpaceRetrieval.xml");
	   }
		// create AnalysisEngine		
		XMLInputSource input = new XMLInputSource(descUrl);
		AnalysisEngineDescription desc = UIMAFramework.getXMLParser().parseAnalysisEngineDescription(input);
		AnalysisEngine anAnalysisEngine = UIMAFramework.produceAnalysisEngine(desc);
		CAS aCas = anAnalysisEngine.newCAS();

	  URL docUrl = VectorSpaceRetrieval.class.getResource("/data/documents.txt");
    if (docUrl == null) {
       throw new IllegalArgumentException("Error opening data/documents.txt");
    }
		BufferedReader br = new BufferedReader(new InputStreamReader(docUrl.openStream()));
		while ((sLine = br.readLine()) != null)   {
			aCas.setDocumentText(sLine);
			anAnalysisEngine.process(aCas);
			aCas.reset();
		}
		br.close();
		br=null;
		anAnalysisEngine.collectionProcessComplete();
		anAnalysisEngine.destroy();	
		long endTime=System.currentTimeMillis();
		
		double totalTime=(endTime-startTime)/1000.0;
		System.out.println("Total time taken: "+totalTime);
		

	}

}
