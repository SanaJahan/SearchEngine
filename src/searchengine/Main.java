package searchengine;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import searchengine.tokenizer.Token;

public class Main {

    public static void main(String[] args) {
        ExecutorService ex = Executors.newCachedThreadPool();
        for (int i = 9; i < 10; i++) {
          final int index = i;
          Runnable task = () -> {
                System.out.println(" Reading file cf7" + index + ".xml");
                Token token = new Token( "./resource/cf7" + index + ".xml");
            try {
              token.readDocuments();
            } catch (IOException e) {
              e.printStackTrace();
            }
          };
            ex.execute(task);
        }
        ex.shutdown();
    }
}
