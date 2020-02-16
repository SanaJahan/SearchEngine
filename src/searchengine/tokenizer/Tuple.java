package searchengine.tokenizer;

import java.util.List;

/**
 * Represents the index data structure.
 * frequencyOfTerms attribute gives the total frequency of the term in the list of documents given.
 * postings holds the list of ids of all documents where the term has occurred at least once.
 */

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
