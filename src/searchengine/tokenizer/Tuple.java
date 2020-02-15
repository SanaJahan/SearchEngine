package searchengine.tokenizer;

import java.util.List;

public class Tuple {
  private List<Posting> postings;
  private int frequencyOfTerms;

  public List<Posting> getPostings() {
    return postings;
  }

  public void setPostings(List<Posting> postings) {
    this.postings = postings;
  }

  public int getFrequencyOfTerms() {
    return frequencyOfTerms;
  }

  public void setFrequencyOfTerms(int frequencyOfTerms) {
    this.frequencyOfTerms = frequencyOfTerms;
  }
}
