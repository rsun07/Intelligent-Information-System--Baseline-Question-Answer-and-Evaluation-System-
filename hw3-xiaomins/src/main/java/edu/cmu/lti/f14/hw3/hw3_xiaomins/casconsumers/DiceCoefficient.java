package edu.cmu.lti.f14.hw3.hw3_xiaomins.casconsumers;

import java.util.ArrayList;
import java.util.HashSet;

import edu.cmu.lti.f14.hw3.hw3_xiaomins.utils.Vector;

/**
 * This class can be called to calculate the Dice Coefficient 
 * 
 * @author Ryan Sun
 *
 */
public class DiceCoefficient {
  
  /**
   * This function will compute the Dice Coefficient between query vector and document vector
   * @param queryVector
   * @param docVetor
   * @return
   */
  
  public double computeSimilarity(ArrayList<Vector> queryVector, ArrayList<Vector> docVector) {
        
    //calculate the mol for both queries and documents
    // avoid to use math.pow() because it can be too slow
        double mol = computeMol(queryVector) + computeMol(docVector);
        
        //calculate the intersections
        HashSet<String> querySet = new HashSet<String>();
        for(Vector v : queryVector){
          querySet.add(v.getToken());
        }
        
        HashSet<String> intersection = new HashSet<String>();
        for(Vector v : docVector){
          if(querySet.contains(v.getToken())){
            intersection.add(v.getToken());
          }
        }
        
        return 2.0*intersection.size()/mol;
      }
  
  /**
   * calculates the mol for vectors
   * 
   * @param vectors
   * @return the mol of all the vectors
   */
  public static double computeMol(ArrayList<Vector> vectors) {
    double mol = 0.0;
    for (Vector v : vectors) {
      int freq = v.getFreq();
      mol += freq*freq;
    }
    return mol;
  }
}
