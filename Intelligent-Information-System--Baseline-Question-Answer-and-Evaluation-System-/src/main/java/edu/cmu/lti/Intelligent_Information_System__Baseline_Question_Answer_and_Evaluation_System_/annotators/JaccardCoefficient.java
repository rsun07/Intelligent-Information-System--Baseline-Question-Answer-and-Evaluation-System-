package edu.cmu.lti.Intelligent_Information_System__Baseline_Question_Answer_and_Evaluation_System_.annotators;

import java.util.ArrayList;
import java.util.HashSet;

import edu.cmu.lti.f14.hw3.hw3_xiaomins.utils.Vector;

/**
 * This class can be called to calculate the Jaccard Coefficient
 * 
 * @author Ryan Sun
 *
 */
public class JaccardCoefficient {
  
  /**
   * This function will compute the cosine similarity between query vector and document vector
   * Store the result in self defined data structure called Doc 
   * @param queryVector
   * @param docVector
   * @return jaccard_coefficient
   */
  public double computeSimilarity(ArrayList<Vector> queryVector, ArrayList<Vector> docVector) {
            
        // get the union of words from query
        HashSet<String> union = new HashSet<String>();
        for(Vector v : queryVector){
            union.add(v.getToken());
        }
        
        HashSet<String> querySet = new HashSet<String>(union);
//        querySet.add("");
        HashSet<String> intersection = new HashSet<String>(union);
        //get the union of words from document
        //at the same time, get the intersetion
        for(Vector v : docVector){
          String token = v.getToken();
          if(!querySet.contains(token)){
            union.add(token);
            intersection.remove(token);
          }
        }       
        return (double) intersection.size()/(double) union.size(); 
      }
}
