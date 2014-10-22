package edu.cmu.lti.f14.hw3.hw3_xiaomins.annotators;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * this class proved main function to remove the stop words from tokens
 * 
 * @author Ryan Sun
 *
 */
public class StopWordRemover {
  
  private static BufferedReader bufReader;
  private static ArrayList<String> stopWords;
  
  public static void initialize(){
    try {
      bufReader = new BufferedReader(new FileReader("stopwords.txt"));
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    
    stopWords = new ArrayList<String>();
    
    try {
    for(String line = bufReader.readLine(); line != null; line = bufReader.readLine()){
        stopWords.add(line);
      }
    } catch (IOException e) {
        e.printStackTrace();
    }
      
  }
  /**
   * the main function for remove stop words
   * 
   */
  public static boolean isStopWord(String token){
      if(stopWords.contains(token))
        return true;
      return false;
  }
  
  /**
   * close bufferedReader after reading
   * @throws IOException
   */
  public void close() throws IOException {
    try {
      bufReader.close();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
  
  /*
   * Some stupid test.
   */
  public static void main(String args[]) {
    String origText = "An American go to the station for a ticket that is important ";
    
    System.out.println(isStopWord(origText));
    System.out.println("==================");
    System.out.println(origText);
  }
}
