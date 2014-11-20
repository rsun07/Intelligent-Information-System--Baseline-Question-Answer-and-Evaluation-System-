package edu.cmu.lti.Intelligent_Information_System__Baseline_Question_Answer_and_Evaluation_System_.utils;

public class Vector {
    private String token;
    private int freq;
    
    public Vector(){
      token = "";
      freq = 0;
    }
    
    public Vector(String token, int freq){
      this.token = token;
      this.freq = freq;
    }
    
    public void setToken(String token){
      this.token = token;
    }
    public String getToken(){
      return token;
    }
    
    public void setFreq(int freq){
      this.freq = freq;
    }
    public int getFreq(){
      return freq;
    }
}
