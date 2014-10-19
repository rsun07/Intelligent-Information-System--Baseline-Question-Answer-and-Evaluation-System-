package edu.cmu.lti.f14.hw3.hw3_xiaomins.annotators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.uima.analysis_component.JCasAnnotator_ImplBase;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.FSIterator;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.cas.FSList;
import org.apache.uima.jcas.tcas.Annotation;

import edu.cmu.lti.f14.hw3.hw3_xiaomins.typesystems.Document;
import edu.cmu.lti.f14.hw3.hw3_xiaomins.typesystems.Token;
import edu.cmu.lti.f14.hw3.hw3_xiaomins.utils.StanfordLemmatizer;
import edu.cmu.lti.f14.hw3.hw3_xiaomins.utils.Utils;

/**
 * 
 * The DocumentVectorAnnotator will read the document from the Reader Creates sparse term vectors
 * for each word. 
 * Output these vectors to the RetrievalEvaluator
 * 
 * @author Ryan Sun
 *
 */
public class DocumentVectorAnnotator extends JCasAnnotator_ImplBase {

  @Override
  /**
   * The process() function is the main function in this class.
   * It will call the createFrequencyVector() function to create vector for every queries and documents
   * 
   */
  public void process(JCas jcas) throws AnalysisEngineProcessException {

    FSIterator<Annotation> iter = jcas.getAnnotationIndex().iterator();
    if (iter.isValid()) {
      iter.moveToNext();
      Document doc = (Document) iter.get();
      createFrequencyVector(jcas, doc);
    }
  }

  /**
   * A basic white-space tokenizer, it deliberately does not split on punctuation!
   *
   * @param doc
   *          input text
   * @return a list of tokens.
   */

  List<String> tokenize0(String doc) {
    List<String> res = new ArrayList<String>();

    for (String s : doc.split("\\s+"))
      res.add(s);
    return res;
  }

  /**
   * This function will read the tokens from the input document and calculate the term frequency
   * Then write all the information, such as token, term frequency and text into the Token type
   * Using the JCas pass it into the Evaluator
   * 
   * @param jcas
   * @param doc
   */

  private void createFrequencyVector(JCas jcas, Document doc) {

    String docText = doc.getText();
    // replace punctunates to avoid wrong parse
//     docText = docText.replace(",", "");
//     docText = docText.replace(".", "");
//     docText = docText.replace("!", "");
//     docText = docText.replace("?", "");
//     docText = docText.replace(";", "");
    // parse every token using the tokenize0() function
//     docText = StanfordLemmatizer.stemText(docText);
    List<String> tokens = tokenize0(docText);
    HashMap<String, Integer> frequency = new HashMap<String, Integer>();
    // read the tokens into the HashMap and calculate the term frequency
    for (String token : tokens) {
      int freq = 1;
      if (frequency.containsKey(token))
        freq += frequency.get(token);
      frequency.put(token, freq);
    }

    ArrayList<Token> tokenList = new ArrayList<Token>();
    for (Entry<String, Integer> entry : frequency.entrySet()) {
      Token token = new Token(jcas);
      token.setText(entry.getKey());
      token.setFrequency(entry.getValue());
      tokenList.add(token);
    }
    // add tokenList to cas and pass it to Evaluator
    FSList fslist = Utils.fromCollectionToFSList(jcas, tokenList);
    doc.setTokenList(fslist);
  }

}
