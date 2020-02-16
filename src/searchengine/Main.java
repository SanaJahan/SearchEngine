package searchengine;

import java.io.File;

import searchengine.tokenizer.Token;

public class Main {
    private static long startTime = System.currentTimeMillis();

    public static void main(String[] args) {
      Token token = new Token();
      File folder = new File("./resource/cfxml/");
      token.readFilesFromFolder(folder);


      long endTime = System.currentTimeMillis();
      System.out.println("Creating the index took " + (endTime - startTime)/1000 + " seconds");

      File file = new File("index.properties");
      double bytes = file.length();
      double kilobytes = (bytes / 1024);
      System.out.println("Space taken by the index in kilobytes : " + kilobytes);
    }
}
