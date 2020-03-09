package searchengine;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import searchengine.ranking.PrecisionRecall;
import searchengine.ranking.PrecisionRecallImpl;
import searchengine.ranking.Query;
import searchengine.ranking.QueryParser;
import searchengine.tokenizer.Token;

public class Main {
  private static long startTime = System.currentTimeMillis();


  public static void main(String[] args) throws IOException {
    createSearchIndex();
  }

  public static void createSearchIndex() throws IOException {
    Token token = new Token();
    File folder = new File("./resource/cfxml/");

    queryReader(token, folder);

    long endTime = System.currentTimeMillis();
    System.out.println("Creating the index took " + (endTime - startTime) / 1000 + " seconds");

    File file = new File("data.properties");
    double bytes = file.length();
    double kilobytes = (bytes / 1024);
    System.out.println("Space taken by the index in kilobytes : " + kilobytes);

  }

  public static void queryReader(Token token, File folder) throws IOException {


    FileWriter csvWriter2 = new FileWriter("sana.csv");
    csvWriter2.append("QueryId");
    csvWriter2.append(",");
    csvWriter2.append("Precision");
    csvWriter2.append(",");
    csvWriter2.append("AveragePrecision");
    csvWriter2.append(",");
    csvWriter2.append("Recall");
    csvWriter2.append(",");
    csvWriter2.append("AverageRecall");
    csvWriter2.append("\n");
    QueryParser queryParser = new QueryParser();
    //documents retrieved for 100 queries
    List<Query> queries = queryParser.parseQuery("./resource/cfquery.xml");
    for (Query query : queries) {
      // answer-documents for every query
      QueryParser queryParser1 = new QueryParser(query.getQueryText());
      List<String> answerList = queryParser1.calculateDocTfIdf(token.readFilesFromFolder(folder));
      PrecisionRecall precisionRecall = new PrecisionRecallImpl(answerList, query);


      csvWriter2.append(String.join(",", String.valueOf(query.getQueryId()) + ","));
      csvWriter2.append(String.join(",", String.valueOf(precisionRecall.calculatePrecision()) + ","));
      csvWriter2.append(String.join(",", String.valueOf(precisionRecall.calculateAveragePrecision()) + ","));
      csvWriter2.append(String.join(",", String.valueOf(precisionRecall.calculateRecall()) + ","));
      csvWriter2.append(String.join(",", String.valueOf(precisionRecall.calculateAverageRecall()) ));
      csvWriter2.append("\n");
    }
    csvWriter2.flush();
    csvWriter2.close();
  }

}
