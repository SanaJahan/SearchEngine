package searchengine.ranking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import searchengine.tokenizer.Posting;
import searchengine.tokenizer.Tuple;
import searchengine.util.DocumentReader;

public class QueryParser {

  private DocumentReader documentReader = new DocumentReader();
  private CosineSimilarity cosineSimilarity;
  private String query;

  public static final int K = 10;

  //TODO: parse the query xml


  public QueryParser(String query) {
    query = documentReader.normalizeStr(query);
    query = query.replaceAll("[^a-zA-Z\\s+]", "");
    this.query = query.toLowerCase();

    this.cosineSimilarity = new CosineSimilarityImpl();

  }


  /**
   * Iterate through all the terms in the query, and for all the term, retrieve the posting list
   * from the map. For each posting list call the calculateTf method and get the tf
   *
   * @param map
   * @return
   */
  public void calculateDocTfIdf(Map<String, Tuple> map) {
    PriorityQueue<ScoredDocument> heap = new PriorityQueue<>(K, new ScoreComparator());
    List<ScoredDocument> list = new ArrayList<>();
    double[] scores;
    for (String qTerm : this.query.split(" ")) {
      // query term matches the term in the index
      if (map.containsKey(qTerm)) {
        scores = new double[map.get(qTerm).getPostings().size()];
        int count = 0;
        double idf = cosineSimilarity.calculateIDF(documentReader.TOTAL_DOCUMENTS, map.get(qTerm).getFrequencyOfTerms());
        // wq,t
        double queryTf = 1.0 / this.query.split(" ").length;
        for (Posting posting : map.get(qTerm).getPostings()) {
          // d,tf term frequency of the query in the document which has the term
          double tf = posting.getSpecificTermFreq().get(qTerm);
          double tfIdfDoc = tf * idf;
          double tfIdfQuery = queryTf * idf;

          scores[count++] = tfIdfDoc * tfIdfQuery;
          ScoredDocument scoredDocument = new ScoredDocument(posting, tfIdfDoc, qTerm, tfIdfDoc * tfIdfQuery);
          list.add(scoredDocument);
        }
        for (int i = 0; i < count; i++) {
          scores[i] /= count;
        }
        for (int i = 0; i < count; i++) {
          ScoredDocument scoredDocument = list.get(i);
          if (!heap.contains(scoredDocument)) {
            heap.add(scoredDocument);
            if (heap.size() > K) {
              heap.remove();
            }
          }
        }
        list.clear();
      }
    }
    getRelevantDocuments(heap);
  }


  public void getRelevantDocuments(Queue<ScoredDocument> scoredDocuments) {
    for (ScoredDocument document : scoredDocuments) {
        System.out.println(document.getPosting().getDocumentID());
    }
  }

}
