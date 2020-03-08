package searchengine;

import java.io.File;

import searchengine.ranking.QueryParser;
import searchengine.tokenizer.Token;

public class Main {
    private static long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
      createSearchIndex();
    }

    public static void createSearchIndex() {
      Token token = new Token();
      File folder = new File("./resource/cfxml/");

      String query="Sana Jahan is?";

      QueryParser queryParser = new QueryParser(query);

      queryParser.calculateDocTfIdf(token.readFilesFromFolder(folder));

      long endTime = System.currentTimeMillis();
      System.out.println("Creating the index took " + (endTime - startTime)/1000 + " seconds");

      File file = new File("index.properties");
      double bytes = file.length();
      double kilobytes = (bytes / 1024);
      System.out.println("Space taken by the index in kilobytes : " + kilobytes);
    }

    public static void queryReader(){
      File file = new File("../resource/cfquery.xml");

    }

}
