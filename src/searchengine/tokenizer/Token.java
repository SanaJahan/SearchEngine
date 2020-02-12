package searchengine.tokenizer;

import java.nio.charset.Charset;
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
    documentReader.readFile(Charset.forName("utf-8"), "../../../Information Retrieval/SearchEngine/cfc-xml/");
    this.fileContents = fileContents;
    //this.tokenizeDocument();

  }

}
