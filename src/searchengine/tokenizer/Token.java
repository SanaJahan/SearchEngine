package searchengine.tokenizer;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

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
    String fileContents = documentReader.readFile(Charset.forName("utf-8"), this.fileName);;
    this.fileContents = fileContents;
    //this.tokenizeDocument();
  }

  /*private void removeStringGarbage() {
    this.fileContents = this.fileContents.replaceAll("(?:&#[0-9]*;)", " ");
    this.fileContents = this.fileContents.replaceAll("\\,*", "");
    this.fileContents = this.fileContents.replaceAll("\\.*", "");
  };

  *//**
   * Splits the file contents.
   * Compression techniques are here.
   * Document ID is parsed using the NEWID tag.
   * We remove the html tags, and split by parsing the REUTERS HEADER.
   * WE apply the stemmer here, after that the documents are normalized/tokenized.
   *//*
  private void splitFileContents() {
    // Consists of docId and list of all documents
    this.documentList = new ArrayList<DocumentIndex>();

    // Splitting string by keeping delimiter
    List<String> news = new ArrayList<String>();

    news.addAll(Arrays.asList(this.fileContents.split("(?=(?:<REUTERS)(?:.)+(?:NEWID=\".+\">))+")));

    news.remove(0);

    //For each word in the news.
    for (String tokens : news) {
      String docID = parseDocumentID(tokens.split("\n")[0]);
      tokens = Jsoup.clean(tokens, Whitelist.none());
      tokens = cleanToken(tokens);

      //COMPRESSION TECHNIQUES
      tokens = removeNumbers(tokens);
      tokens = caseFolding(tokens);
      //tokens = apply30stopwords(tokens);
      //tokens = apply150stopwords(tokens);

      //Splits to get the terms.
      String[] terms = tokens.split("\\s");

      //filling up the documentlist.
      DocumentIndex docIndex = new DocumentIndex(docID, terms);
      this.documentList.add(docIndex);
    }

    //PorterStemmer
    //applyStemmer();
  }
  *//**
   * Removing useless special characters found in the collection that were interfering with the creating of a good index.
   * @param tokens the string to clean
   * @return the cleaned token.
   *//*
  private static String cleanToken(String tokens){
    tokens = tokens.replaceAll("\n|\r", " ");
    tokens = tokens.replaceAll("\"", "");
    tokens = tokens.replaceAll("&lt;", "");
    tokens = tokens.replaceAll("&gt;", "");
    tokens = tokens.replaceAll("\\+", "");
    tokens = tokens.replaceAll("\\(|\\)", "");
    tokens = tokens.replaceAll("\\*", "");
    tokens = tokens.replaceAll("'", "");
    tokens = tokens.replaceAll("&amp;", "");
    tokens = tokens.replaceAll("-", "");
    return tokens;
  }


  //COMPRESSION TECHNIQUES
  private static String removeNumbers(String tokens){
    tokens = tokens.replaceAll("[0-9]+", "");
    return tokens;
  }

  private static String caseFolding(String tokens){
    tokens = tokens.toLowerCase();
    return tokens;
  }

  @SuppressWarnings("unused")
  private static String apply30stopwords(String tokens){
    tokens = removeStopWords(tokens,30);
    return tokens;
  }

  @SuppressWarnings("unused")
  private static String apply150stopwords(String tokens){
    tokens = removeStopWords(tokens,150);
    return tokens;
  }


  *//**
   * Parses the document id from the NEWID tag from the reuters header.
   * @param reutersHeader
   * @return the docid (NEWID)
   *//*
  private static String parseDocumentID(String reutersHeader) {
    Document doc = Jsoup.parse(reutersHeader);
    Element reuters = doc.select("REUTERS").first();
    return reuters.attr("NEWID");
  }*/
}
