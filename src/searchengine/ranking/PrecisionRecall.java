package searchengine.ranking;

public interface PrecisionRecall {

  double calculatePrecision();
  double calculateAveragePrecision();
  double calculateRecall();
  double calculateAverageRecall();
}
