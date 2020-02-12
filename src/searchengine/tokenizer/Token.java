package searchengine.tokenizer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import searchengine.util.DocumentReader;

/**
 * Data structure for tokens used for indexing
 *  [term <docId, freq>]
 */
public class Token {

  private String fileName;
  private String fileContents;

  private List<DocumentIndex> documentList;

  private DocumentReader documentReader;


  public Token(String filename) {
    this.fileName = filename;
    this.documentReader = new DocumentReader();
  }



  public void readDocuments() {
    String fileContents = "";
    try {
      fileContents = documentReader.readFile(Charset.forName("utf-8"), this.fileName);
      this.fileContents = fileContents;
      //this.tokenizeDocument();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (URISyntaxException use) {
      use.printStackTrace();
    }

  }

}
