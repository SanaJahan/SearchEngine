package searchengine.tokenizer;

import java.util.List;

public class Tuple {
  private String term;
  private List<Posting> postings;

  public String getTerm() {
    return term;
  }

  public void setTerm(String term) {
    this.term = term;
  }

  public List<Posting> getPostings() {
    return postings;
  }

  public void setPostings(List<Posting> postings) {
    this.postings = postings;
  }
}
