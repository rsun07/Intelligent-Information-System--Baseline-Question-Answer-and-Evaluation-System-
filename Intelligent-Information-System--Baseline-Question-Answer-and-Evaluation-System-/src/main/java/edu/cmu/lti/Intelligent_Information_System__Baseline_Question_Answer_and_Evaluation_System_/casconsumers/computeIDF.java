package edu.cmu.lti.Intelligent_Information_System__Baseline_Question_Answer_and_Evaluation_System_.casconsumers;

import java.util.ArrayList;
import java.util.HashMap;

import edu.cmu.lti.f14.hw3.hw3_xiaomins.utils.Vector;

/**
 * This class can be called to calculate the IDF
 * 
 * @author Ryan Sun
 *
 */
public class computeIDF {
  /**
   * This function will compute the Dice Coefficient between query vector and document vector
   * @param vector
   * @return
   */
  public static  HashMap<String, Double> computeIDF(ArrayList<Vector> vectors) {
    HashMap<String, Double> idf = new HashMap<String, Double>();
    int N = vectors.size();
    for (Vector v : vectors) {
      String token = v.getToken();
        if (!idf.containsKey(token)){
          idf.put(token, 1.0);
        }else{
          idf.put(token, idf.get(token) + 1.0);
        }
    }
    
    for (String token : idf.keySet())
      idf.put(token, Math.log((N + 1)/ idf.get(token)));
    
    return idf;
  }
}
