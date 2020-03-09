package searchengine.ranking;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import searchengine.tokenizer.Posting;
import searchengine.tokenizer.Tuple;
import searchengine.util.DocumentReader;

public class QueryParser {

  private DocumentReader documentReader = new DocumentReader();
  private CosineSimilarity cosineSimilarity;
  private String query;

  public static final int K = 10;

  //TODO: parse the query xml


  public QueryParser() {
  }

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
  public List<String> calculateDocTfIdf(Map<String, Tuple> map) {
    PriorityQueue<ScoredDocument> heap = new PriorityQueue<>(K, new ScoreComparator());
    List<ScoredDocument> list = new ArrayList<>();
    double[] scores;
    for (String qTerm : this.query.split(" ")) {
      // query term matches the term in the index
      if (map.containsKey(qTerm)) {
        scores = new double[map.get(qTerm).getPostings().size()];
        int count = 0;
        double idf = cosineSimilarity.calculateIDF(documentReader.TOTAL_DOCUMENTS, map.get(qTerm).getPostings().size());
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
    return getRelevantDocuments(heap);
  }


  private List<String> getRelevantDocuments(Queue<ScoredDocument> scoredDocuments) {
    List<String> answerList = new ArrayList<>();
    for (ScoredDocument document : scoredDocuments) {
          String docId = document.getPosting().getDocumentID();
          documentReader.writeSearchResult(docId);
          answerList.add(docId);
    }
    return answerList;
  }

  public List<Query> parseQuery(String absolutePath){
      File file = new File(absolutePath);
      List<Query> queries = new ArrayList<>();
      try {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName("QUERY");
        for (int i = 0; i < nodeList.getLength(); i++) {
          List<Integer> displayedRelevantDocs = new ArrayList<>();
          // add all the relevant documents to the list. for recall
          for (int j = 0; j < ((Element)nodeList.item(i)).getElementsByTagName("Item").getLength() ; j++) {
            displayedRelevantDocs.add(Integer.parseInt(((Element)nodeList.item(i)).getElementsByTagName("Item").item(j).getTextContent()));
          }
          int totalRelevantDocs = Integer.parseInt(((Element)nodeList.item(i)).getElementsByTagName("Results").item(0).getTextContent());
          String queryText = ((Element)nodeList.item(i)).getElementsByTagName("QueryText").item(0).getTextContent();
          int queryId = Integer.parseInt(((Element)nodeList.item(i)).getElementsByTagName("QueryNumber").item(0).getTextContent());
          Query query = new Query(queryId,queryText,totalRelevantDocs,displayedRelevantDocs);
          queries.add(query);
        }
      } catch (ParserConfigurationException | SAXException | IOException e) {
        e.printStackTrace();
      }
      return queries;
  }

}
