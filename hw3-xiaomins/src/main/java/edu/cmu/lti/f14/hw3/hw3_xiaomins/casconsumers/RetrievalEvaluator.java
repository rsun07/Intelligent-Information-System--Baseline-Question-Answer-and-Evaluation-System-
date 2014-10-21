package edu.cmu.lti.f14.hw3.hw3_xiaomins.casconsumers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.apache.uima.cas.CAS;
import org.apache.uima.cas.CASException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.collection.CasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.jcas.tcas.Annotation;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.resource.ResourceProcessException;
import org.apache.uima.util.ProcessTrace;

import edu.cmu.lti.f14.hw3.hw3_xiaomins.typesystems.Document;
import edu.cmu.lti.f14.hw3.hw3_xiaomins.typesystems.Token;
import edu.cmu.lti.f14.hw3.hw3_xiaomins.utils.Doc;
import edu.cmu.lti.f14.hw3.hw3_xiaomins.utils.Utils;
import edu.cmu.lti.f14.hw3.hw3_xiaomins.utils.Vector;

/**
 * The RetrievalEvaluator will firstly store all the information from the DocumentVectorAnnotator into proper data structure.
 * Then it will compute the cosine similarity between query and related documents, 
 * and compute the rank of the relevant document to calculate the MRR metric.
 * Finally, it will write the output into the report.txt in required format.
 * 
 * 
 * @author Ryan Sun
 *
 */

public class RetrievalEvaluator extends CasConsumer_ImplBase {

//  /** query id number **/
//  public ArrayList<Integer> qIdList;
  
	/** vector : query/document text and query/document frequency **/
	private ArrayList<Vector> vector;

	/** query id and query vector **/
	private LinkedHashMap<Integer, ArrayList<Vector>> queryList;
	
//  /** document vector : document text and document frequency **/
//	private Doc docVector;
	 
	 /** document id and the Docvector, relevance, cosineSimilarity and text **/
	 private HashMap<Integer, ArrayList<Doc>> docList;
	 
	 // Output data document
	 private String outputFile;
	
	// to write the document/result
	private BufferedWriter bufWriter;

	/**
	 * initialize() function will initialize the data structure to store the data 
	 * and initial a BufferedReader that will write the output later.
	 */
	public void initialize() throws ResourceInitializationException {
	  //qIdList = new ArrayList<Integer>();
	  //queryVector = new HashMap<String, Integer>();
	  queryList = new LinkedHashMap<Integer, ArrayList<Vector>>();
	  //docVector = new HashMap<String, Integer>();
	  docList = new HashMap<Integer, ArrayList<Doc>>();
	  initBufWriter();
	}
	
	  /**
	   * this function will initialize the BufferedReader for write the output
	   */
	    private void initBufWriter(){
	      outputFile = ((String) getConfigParameterValue("OutputFile")).trim();
	      try {
	        bufWriter = new BufferedWriter(new FileWriter(outputFile, false));
	      } catch (Exception e) {
	        e.printStackTrace();
	      }
	    }

	/**
	 * The processCas() function will read the information from the DocumentVectorAnnotator,then
	 * 
	 * 1. construct the global word dictionary 
	 * 2. keep the word frequency for each sentence
	 */
	@Override
	public void processCas(CAS aCas) throws ResourceProcessException {

		JCas jcas;
		try {
			jcas =aCas.getJCas();
		} catch (CASException e) {
			throw new ResourceProcessException(e);
		}

		FSIterator<Annotation> it = jcas.getAnnotationIndex(Document.type).iterator();
	
		if (it.hasNext()) {
			Document doc = (Document) it.next();

			FSList fsTokenList = doc.getTokenList();
			ArrayList<Token> tokenList = Utils.fromFSListToCollection(fsTokenList, Token.class);			
			// get the information from doc and tokenList, then distinguish query from documents and store necessary information
			processInfo(doc, tokenList);
		}
	}
	
	/**
	 * This function will sparse information from document and tokenList
	 * 1. distinguish queries from documents
	 * 2. store queries and documents in proper data structure for later recall
	 * @param doc
	 * @param tokenList
	 */
	  private void processInfo(Document doc, ArrayList<Token> tokenList){
	    //get information from doc
	    int id = doc.getQueryID();
	    int rel = doc.getRelevanceValue();
	    String text = doc.getText();
	    //System.out.println(id + "  " + rel + "  " + text);
	    
	    //build the vector for both query and document
	    vector = new ArrayList<Vector>();
	    for(Token token : tokenList){ 
        Vector curVector = new Vector(token.getText(), token.getFrequency());
        //System.out.println(token.getText() + "  " + token.getFrequency() + "  " + text);
	      vector.add(curVector);
	    }
	    
	    // distinguish queries from all the input documents
	    if(rel == 99){
	      queryList.put(id, vector);
      } else {
        ArrayList<Doc> tmpDoc;
        Doc curDoc = new Doc(rel, text, vector);
        if(docList.containsKey(id)){
          tmpDoc = docList.get(id);
        }else{
          tmpDoc = new ArrayList<Doc>();
        }
        tmpDoc.add(curDoc);
        docList.put(id, tmpDoc);
        //System.out.println(curDoc);
       }
    }

	/**
	 * This function has three main functions
	 * 1. Compute Cosine Similarity and rank the retrieved sentences 
	 * 2.Compute the MRR metric
	 * 3. Write the output data into report.txt
	 */
	@Override
	public void collectionProcessComplete(ProcessTrace arg0)
			throws ResourceProcessException, IOException {

		super.collectionProcessComplete(arg0);

		ArrayList<Integer>  mrrList = new ArrayList<Integer>();
		// compute the cosine similarity measure
		for (Entry<Integer, ArrayList<Vector>> entry : queryList.entrySet()){
		  int id = entry.getKey();
		  ArrayList<Vector> queryVector = entry.getValue();
		  ArrayList<Doc> docs = docList.get(id);
		  for(Doc curDoc : docs){
		    double cosSim = computeCosineSimilarity(queryVector, curDoc.getVector());
//		    double jacc = computeJaccardCoefficient(queryVector, curDoc.getVector());
		    curDoc.setCosSim(cosSim);
		  }
      Collections.sort(docs);
      // compute the rank of retrieved sentences
      int rank = computeRank(docs);
      mrrList.add(rank);
      writeOutput(id, rank, docs);
		}
			
		// compute the metric:: mean reciprocal rank
		double metric_mrr = compute_mrr(mrrList);
    String output = String.format("MRR=%.4f", metric_mrr);
    //System.out.println(output);
    bufWriter.write(output);
		System.out.println(" (MRR) Mean Reciprocal Rank ::" + output);
	}

	/**
	 * This function will compute the cosine similarity between query vector and document vector
	 * Store the result in self defined data structure called Doc
	 * @return cosine_similarity
	 */
	private double computeCosineSimilarity(ArrayList<Vector> queryVector, ArrayList<Vector> docVector) {
		double cosine_similarity = 0.0;
		
		/*
		//only for test use
		for(Vector v : queryVector){
		  System.out.println(v.getToken() + "   " + v.getFreq());
		}
		*/
		// compute cosine similarity between two sentences
		double product = 0.0;
		double queryMol = 0.0;
		HashMap<String, Integer> docs = new HashMap<String, Integer>();
		// this function will transform ArrayList<Vector> into HashMap<String, Integer> 
		//and calculate the mol of documents at the same time
		double docMol = fromListToMap(docVector, docs);
		
		//calculate the product of queryVector and docVecotor
		for(Vector qv : queryVector){
		  String token = qv.getToken();
		  double queryFreq = qv.getFreq();
		  //System.out.println("q"+ "  "+ token + "  " + queryFreq);
		  if(docs.containsKey(token)){
		    double docFreq = docs.get(token);
		    //System.out.println("d"+ "  " + token + "  " + queryFreq + "  " + docFreq);
		    product += queryFreq*docFreq;
		  }
		  //calculate the mol of query
		  queryMol += queryFreq*queryFreq;
		}
		queryMol = Math.sqrt(queryMol);
		//System.out.println(product + "  " + docMol+ "  " + queryMol );
		cosine_similarity = (double) product/(docMol*queryMol);
		//System.out.println(cosine_similarity);
		return cosine_similarity;
	}
	
	      /**
	       * This function will transform ArrayList<Vector> into HashMap<String, Integer> for later search
	       * At the same time it can calculate the mol of documents for later normalization
	       * @param docVector
	       * @return
	       */
	      private double fromListToMap(ArrayList<Vector> docVector, HashMap<String, Integer> docs){
	        double docMol = 0.0;
	        for(Vector dv : docVector){
	          docs.put(dv.getToken(), dv.getFreq());
	          int docFreq = dv.getFreq();
	          docMol += docFreq*docFreq;
	        }
	        return Math.sqrt(docMol);
	        //for jaccard_coefficient use only
//	        return docMol;
	      }
	  
	/**
	 * Calculate the rank of the first relevant answer
	 * @return rank
	 */
	private int computeRank(ArrayList<Doc> docs){
	  int rank = 1;
	  for(Doc curDoc : docs){
	    if(curDoc.getRelevance() == 1)
	      break;
	    rank++;
	  }
	  return rank;
	}
	
	/**
	 * write the output file in required format
	 */
	private void writeOutput(int id, int rank, ArrayList<Doc> docs){
	  Doc answerDoc = docs.get(rank - 1);
	  int rel = answerDoc.getRelevance();
	  String text = answerDoc.getText();
	  double cosSim = answerDoc.getCosSim();
	  String output = 
	          String.format("cosine=%.4f\trank=%d\tqid=%d\trel=%d\t%s%n", cosSim, rank, id, rel, text);
    //System.out.println(output);
	  try {
      bufWriter.write(output);
    } catch (IOException e) {
      e.printStackTrace();
    }
	}

	/**
	 * This function will compute the Mean Reciprocal Rank (MRR) of each query
	 * @return mrr
	 */
	private double compute_mrr(ArrayList<Integer>  mrrList) {
		double metric_mrr=0.0;
		int count = 0;
		// compute Mean Reciprocal Rank (MRR) of the text collection
		for(int rank : mrrList){
		  metric_mrr += (double)1 / rank;
		  count++;
		}
		return metric_mrr/count;
	}
	
	/**
	 * The function will be run at the end of the pipeline, to end the BufferedWriter
	 */
  public void destroy() {
    try {
      bufWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
