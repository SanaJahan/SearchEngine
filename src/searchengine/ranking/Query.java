package searchengine.ranking;

import java.util.List;

public class Query {
  private int queryId;
  private String queryText;
  private int totalRelevantDocs;
  private List<Integer> displayedRelevantDocIds;


  public Query(int queryId, String queryText, int totalRelevantDocs, List<Integer> displayedRelevantDocIds) {
    this.queryId = queryId;
    this.queryText = queryText;
    this.totalRelevantDocs = totalRelevantDocs;
    this.displayedRelevantDocIds = displayedRelevantDocIds;
  }

  public String getQueryText() {
    return queryText;
  }

  public int getTotalRelevantDocs() {
    return totalRelevantDocs;
  }

  public List<Integer> getDisplayedRelevantDocIds() {
    return displayedRelevantDocIds;
  }

  public double getRecall() {
    return displayedRelevantDocIds.size()/totalRelevantDocs;
  }

  public int getQueryId() {
    return queryId;
  }

  public double getPrecision() {
    return 10/10;
  }


  public double getAveragedPrecision() {
    double sum = 0.0;
    for (int i = 1; i < 10; i++) {
         sum += (i) / i;
    }
    return sum;
  }

  public double getAverageRecall() {
    double sum = 0.0;
    for (int i = 1; i < 10; i++) {
          sum += (i) / totalRelevantDocs;
    }
    return sum;
  }

}
