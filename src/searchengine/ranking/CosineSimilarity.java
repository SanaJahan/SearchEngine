package searchengine.ranking;

import java.util.List;
import java.util.Map;

import searchengine.tokenizer.Posting;

public interface CosineSimilarity {

  /**
   * Given the document,
   * this method finds the term frequency of a given term in the document.
   * @param document is the total number of terms present in a given document.
   * @param givenTerm is the term for which tf needs to be calculated.
   * @return the calculated term frequency
   */
  double calculateTF(Posting document, String givenTerm);

  /**
   * This method calculates the idf
   * @param totalDocuments present in the collection or corpus that contains the term
   * @param totalFreqOfTerm the total frequency of the term in the corpus
   * @return
   */
  double calculateIDF(int totalDocuments, int totalFreqOfTerm);

  /**
   * this method calculates the similarity score doing the dot product for query vector and document vector.
   * @param query for which we search the relevant documents
   * @param document whose idf score we calculate based on the terms
   * @return the calculated cosine similarity value
   */
  //public double calculateCosineSimilarity(double query, double document);

}
