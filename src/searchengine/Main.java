package searchengine;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.parsers.ParserConfigurationException;

import searchengine.tokenizer.Token;

public class Main {

    public static void main(String[] args) {
        ExecutorService ex = Executors.newCachedThreadPool();
        for (int i = 4; i < 10; i++) {
          final int index = i;
          Runnable task = () -> {
                Token token = new Token( "../../../Information Retrieval/SearchEngine/cfc-xml/cf7" + index + ".xml");
                token.readDocuments();
          };
            ex.execute(task);
        }
        ex.shutdown();
    }
}
