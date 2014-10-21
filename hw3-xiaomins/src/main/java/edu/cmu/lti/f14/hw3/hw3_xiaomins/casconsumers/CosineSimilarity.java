package edu.cmu.lti.f14.hw3.hw3_xiaomins.casconsumers;

import java.util.ArrayList;
import java.util.HashMap;

import edu.cmu.lti.f14.hw3.hw3_xiaomins.utils.Vector;

/**
 * This class can be called to compute the cosine similarity 
 * between query vector and document vector
 */

public class CosineSimilarity {
  /**
   * This function will firstly Store the result in self defined data structure called Doc
   * And then compute the cosine similarity between query vector and document vector
   * 
   * @param queryVector
   * @param docVetor
   * @return cosine_similarity
   */
  public double computeCosineSimilarity(ArrayList<Vector> queryVector, ArrayList<Vector> docVector) {
    double cosine_similarity = 0.0;

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
//    return docMol;
  }
}
