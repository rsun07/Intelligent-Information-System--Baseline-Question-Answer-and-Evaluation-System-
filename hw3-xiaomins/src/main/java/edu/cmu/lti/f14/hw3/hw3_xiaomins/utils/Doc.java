package edu.cmu.lti.f14.hw3.hw3_xiaomins.utils;

import java.util.ArrayList;


public class Doc implements Comparable<Doc> {
  private ArrayList<Vector> vector;
  private int relevance;
  private double similarity;
  private String text;
  
  public Doc() {
    relevance = 0;
    vector = new ArrayList<Vector>();
    similarity = 0.0;
    text = "";
  }
  
  public Doc(int rel, String text, ArrayList<Vector> vector) {
    this.relevance = rel;
    this.vector = vector;
    this.text = text;
    similarity = 0.0;
  }

  public int compareTo(Doc doc) {
    if (this.similarity > doc.similarity)
      return -1;
    else if (this.similarity < doc.similarity)
      return 1;
    else
      return 0;
  }
  
  public void setVector(ArrayList<Vector> vector){
    this.vector = vector;
  }
  public ArrayList<Vector> getVector(){
    return vector;
  }
  
  public void setRelevance(int relevance){
    this.relevance = relevance;
  }
  public int getRelevance(){
    return relevance;
  }
  
  public void setText(String text){
    this.text = text;
  }
  public String getText(){
    return text;
  }
  
  public void setSimilarity(double sim){
    similarity = sim;
  }
  public double getSimilarity(){
    return similarity;
  }
  
  public String toString(){
    return String.format("rel=%d\tsimilarity=%.4f\ttext=%s\t%n", relevance, similarity, text);
  }
}

