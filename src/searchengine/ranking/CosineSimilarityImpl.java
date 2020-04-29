package searchengine.ranking;

import searchengine.tokenizer.Posting;

public class CosineSimilarityImpl implements CosineSimilarity {

  @Override
  public double calculateTF(Posting document, String givenTerm) {
    Double frequencyOfGivenTerm = document.getTermFreqInDoc(givenTerm);
    return frequencyOfGivenTerm / document.getTotalFrequency();
  }

  @Override
  public double calculateIDF(int totalDocuments, int noOfDocsWithGivenTerm) {
    return 1 + Math.log(totalDocuments / noOfDocsWithGivenTerm);
  }

  @Override
  public double calculateCosineSimilarity(double query, double document) {
    double dotProduct = 0.0;
    double magnitude1 = 0.0;
    double magnitude2 = 0.0;
    double cosineSimilarity = 0.0;

    dotProduct += query * document;  //a.b
    magnitude1 += Math.pow(query, 2);  //(a^2)
    magnitude2 += Math.pow(document, 2); //(b^2)

    magnitude1 = Math.sqrt(magnitude1);//sqrt(a^2)
    magnitude2 = Math.sqrt(magnitude2);//sqrt(b^2)

    if (magnitude1 != 0.0 | magnitude2 != 0.0) {
      cosineSimilarity = dotProduct / (magnitude1 * magnitude2); // [a.b/|a||b|]
    } else {
      return 0.0;
    }
    return cosineSimilarity;
  }


}
