package searchengine.tokenizer;

import java.util.Map;
import java.util.Objects;

/**
 * Represents data structure to store the document id and is used as a index value.
 */
public class Posting {
  private String documentID;
  private Map<String, Double> specificTermFreq;
  private int totalFrequency;


  public Map<String, Double> getSpecificTermFreq() {
    return specificTermFreq;
  }

  public Double getTermFreqInDoc(String documentID) {
    return specificTermFreq.get(documentID);
  }

  public void setSpecificTermFreq(Map<String, Double> specificTermFreq) {
    this.specificTermFreq = specificTermFreq;
  }

  public int getTotalFrequency() {
    return totalFrequency;
  }

  public void setTotalFrequency(int totalFrequency) {
    this.totalFrequency = totalFrequency;
  }

  public String getDocumentID() {
    return documentID;
  }

  public void setDocumentID(String documentID) {
    this.documentID = documentID;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Posting posting = (Posting) o;
    return documentID.equals(posting.documentID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(documentID);
  }
}
