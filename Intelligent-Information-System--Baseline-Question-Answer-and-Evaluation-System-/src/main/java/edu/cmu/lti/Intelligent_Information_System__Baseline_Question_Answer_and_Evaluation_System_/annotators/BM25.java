package edu.cmu.lti.Intelligent_Information_System__Baseline_Question_Answer_and_Evaluation_System_.annotators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.cmu.lti.f14.hw3.hw3_xiaomins.utils.Vector;

/**
 * This class can be called to calculate the BM25 similarity 
 * 
 * @author Ryan Sun
 *
 */
public class BM25 {
  /**
   * Constants that used to compute BM25
   */
  private static final double k1 = 1.2;
  private static final double b = 0.75;
  private static final double k3 = 0.6;
  /**
   * This function will compute the Dice Coefficient between query vector and document vector
   * @param queryVector
   * @param docVetor
   * @return
   */
  public double computeBM25(ArrayList<Vector> queryVector, ArrayList<Vector> docVector) {
    double bm25 = 0.0;
    
    double RSJ = 0.0;
    double tfWeight = 0.0;
    double usrWeight = 0.0;
    double df = 0.0;
    int tf = 0;
    int qtf = 0;
    
    //read the tokens from query
    HashSet<String> querySet = new HashSet<String>();
    for(Vector v : queryVector){
      querySet.add(v.getToken());
    }
    
    //compute the IDF for the queries
    HashMap<String, Double> IDF = computeIDF.computeIDF(queryVector);
    //try to calculate BM25
    for(Vector v : docVector){
      String token = v.getToken();
      if(querySet.contains(v.getToken())){
        //calculate the BM25
        df = IDF.get(token);
        
        tf = v.getFreq();
        RSJ = Math.log((IDF.size() - df + 0.5) / (df + 0.5));
        tfWeight = tf / (tf + k1 * ((1 - b) + b * querySet.size() / docVector.size() ));
      
        usrWeight = (k3 + 1) * qtf / (qtf + k3);
      
        bm25 += RSJ * tfWeight * usrWeight;
      }
    }

    return bm25;
  }
}
