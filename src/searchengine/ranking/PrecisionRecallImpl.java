package searchengine.ranking;

import java.util.ArrayList;
import java.util.List;

public class PrecisionRecallImpl implements PrecisionRecall {

  private List<String> answerList;
  private Query query;
  private List<Integer> docIds;

  public PrecisionRecallImpl(List<String> answerList, Query query) {
    this.answerList = answerList;
    docIds = new ArrayList<>();
    for (String number : answerList) {
      docIds.add(Integer.parseInt(number));
    }

    this.query = query;
  }

  @Override
  public double calculatePrecision() {
    double relevantDocs = 0.0;
    for (Integer docId : query.getDisplayedRelevantDocIds()) {
      if (docIds.contains(docId)) {
        relevantDocs++;
      }
    }
    return relevantDocs / 10;
  }

  @Override
  public double calculateAveragePrecision() {
    double averagePrecision = 0.0;
    double sum = 0.0;
    for (int i = 0; i < query.getDisplayedRelevantDocIds().size(); i++) {
      if (docIds.contains(query.getDisplayedRelevantDocIds().get(i))) {
        averagePrecision++;
      }
      sum += (averagePrecision) / Double.valueOf(i + 1);
    }
    return sum / 10.0;
  }

  @Override
  public double calculateRecall() {
    double recall = 0.0;
    for (Integer docId : query.getDisplayedRelevantDocIds()) {
      if (docIds.contains(docId)) {
        recall++;
      }
    }
    return recall / Double.valueOf(query.getTotalRelevantDocs());
  }

  @Override
  public double calculateAverageRecall() {
    double averageRecall = 0.0;
    double sum = 0.0;
    for (int i = 0; i < query.getDisplayedRelevantDocIds().size(); i++) {
      if (docIds.contains(query.getDisplayedRelevantDocIds().get(i))) {
        averageRecall++;
      }
      sum += (averageRecall) / Double.valueOf(query.getTotalRelevantDocs());
    }
    return sum / 10.0;
  }
}
