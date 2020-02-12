package searchengine.tokenizer;

public class Posting {
  private String documentID;
  private int frequencyOfTerms;

  public String getDocumentID() {
    return documentID;
  }

  public void setDocumentID(String documentID) {
    this.documentID = documentID;
  }

  public int getFrequencyOfTerms() {
    return frequencyOfTerms;
  }

  public void setFrequencyOfTerms(int frequencyOfTerms) {
    this.frequencyOfTerms = frequencyOfTerms;
  }
}
