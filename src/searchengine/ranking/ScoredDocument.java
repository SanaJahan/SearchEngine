package searchengine.ranking;

import java.util.Objects;

import searchengine.tokenizer.Posting;

public class ScoredDocument {
  Posting posting;
  String term;
  double tf_idf;
  double score;

  public ScoredDocument(Posting posting, double tfIdf, String qTerm,double score) {
    this.posting = posting;
    this.tf_idf = tfIdf;
    this.term = qTerm;
    this.score = score;
  }

  public String getTerm() {
    return term;
  }


  public double getTf_idf() {
    return tf_idf;
  }


  public Posting getPosting() {
    return posting;
  }

  public double getScore() {
    return score;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ScoredDocument that = (ScoredDocument) o;
    return Objects.equals(posting.getDocumentID(), that.posting.getDocumentID());
  }

  @Override
  public int hashCode() {
    return Objects.hash(posting);
  }
}
