package edu.cmu.lti.Intelligent_Information_System__Baseline_Question_Answer_and_Evaluation_System_.annotators;

/**
 * This class provide function to remove punctuations from the tokens
 * 
 * @author Ryan Sun
 *
 */
public class PunctuationRemove {
  /**
   * main function that remove punctuation
   * @param text
   * @return
   */
  public static String PunctuationRemover(String text){
   text =text.replace(",", "");
   text =text.replace(".", "");
   text =text.replace("!", "");
   text =text.replace("?", "");
   text =text.replace(";", "");
   text =text.replace("'s", "");
   text =text.replace("s'", "");
   return text;
  }
  
  /**
   * unit text function
   */
  
  public static void main(String args[]) {
    String origText = "testeds' tester's tested gone indices indices super_testers"
                    + "had, having? been; was's were would! could might";
    
    System.out.println(PunctuationRemover(origText));
    System.out.println("==================");
    System.out.println(origText);
  }
}
