package searchengine.ranking;

import java.util.Comparator;


public class ScoreComparator implements Comparator<ScoredDocument> {


  @Override
  public int compare(ScoredDocument x, ScoredDocument y) {
    if (x.getScore() > y.getScore()) {
      return -1;
    }
    if (x.getScore() < y.getScore()) {
      return 1;
    }
    return 0;
  }

}
