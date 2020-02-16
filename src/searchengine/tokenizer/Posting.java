package searchengine.tokenizer;

import java.util.Objects;

/**
 * Represents data structure to store the document id and is used as a index value.
 */
public class Posting {
  private String documentID;

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
