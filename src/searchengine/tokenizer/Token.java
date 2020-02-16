package searchengine.tokenizer;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import searchengine.util.DocumentReader;

/**
 * Data structure for tokens used for indexing [term <docId, freq>]
 */
public class Token {

  private String fileName;
  private String fileContents;
  private DocumentReader documentReader;
  private String documentID;


  public Token(String filename) {
    this.fileName = filename;
    this.documentReader = new DocumentReader();
  }


  public void readDocuments() {
    File file = new File(this.fileName);
    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(file);
      doc.getDocumentElement().normalize();
      NodeList nodeList = doc.getElementsByTagName("RECORD");
      for (int i = 0; i < nodeList.getLength(); i++) {
        fileContents = documentReader.readFile(StandardCharsets.UTF_8, nodeList, i);
        tokenizeContent();
      }
    } catch (ParserConfigurationException | SAXException | IOException e) {
      e.printStackTrace();
    }
  }

  // TODO: remove unnecessary things and add single whitespaces only.
  // TODO: split into array based on whitespaces. Ignore the stop words.

  private void tokenizeContent() {
    this.fileContents = this.fileContents.replaceAll("(?:&#[0-9]*;)", " ");
    this.fileContents = this.fileContents.replaceAll(",*", "");
    this.fileContents = this.fileContents.replaceAll("\\.*", "");
    this.fileContents = this.fileContents.replaceAll("\n|\r", " ");
    this.fileContents = this.fileContents.replaceAll("\"", "");
    this.fileContents = this.fileContents.replaceAll("&lt;", "");
    this.fileContents = this.fileContents.replaceAll("&gt;", "");
    this.fileContents = this.fileContents.replaceAll("\\+", "");
    this.fileContents = this.fileContents.replaceAll("\\(|\\)", "");
    this.fileContents = this.fileContents.replaceAll("\\*", "");
    this.fileContents = this.fileContents.replaceAll("'", "");
    this.fileContents = this.fileContents.replaceAll("&amp;", "");
    this.fileContents = this.fileContents.replaceAll("-", "");
    this.documentID = this.fileContents.split(" ")[2];
    this.fileContents = this.fileContents.replaceAll("[0-9]+", "");
    this.fileContents = removeStopWords();
    indexingTerms();
  }

  // TODO: Make RecordNum as DocID while splitting.
  //  TODO: Sort the terms and merge the common documents.
  //  TODO: Make the term as key, tuple as value.
  // TODO: I think one way to associate docID is by getting docId here and immediately creating the Posting out of that
  //  before sending it for read to document reader. So for every iteration, we would have the docId before the file is even read.
  private void indexingTerms() {
    // change the string into array and for each term, add in hashmap
    HashMap<String, Tuple> map = new HashMap<>();
    String[] allTerms = fileContents.split(" ");
    for (int i = 0; i < allTerms.length; i++) {
      if (!map.containsKey(allTerms[i])) {
        Posting posting = new Posting();
        posting.setDocumentID(documentID);
        List<Posting> postings = new ArrayList<>();
        postings.add(posting);
        Tuple tuple = new Tuple();
        tuple.setPostings(postings);
        tuple.setFrequencyOfTerms(1);
        map.put(allTerms[i], tuple);
      } else {
        Tuple tuple = map.get(allTerms[i]);
        Posting posting = new Posting();
        posting.setDocumentID(documentID);
        tuple.getPostings().add(posting);
        tuple.setFrequencyOfTerms(tuple.getFrequencyOfTerms() + 1);
        map.put(allTerms[i], tuple);
      }
    }
    ArrayList<String> sortedKeys = new ArrayList<>(map.keySet());
    Collections.sort(sortedKeys);
    Properties properties = new Properties();
    for (Map.Entry<String, Tuple> entry : map.entrySet()) {
      for (Posting p : entry.getValue().getPostings()) {
        properties.put(entry.getKey(), entry.getValue().getFrequencyOfTerms() + " " + p.getDocumentID());
      }
    }
    try {
      properties.store(new FileOutputStream("data.properties"), null);
        // Open given file in append mode.
      /*  BufferedWriter out = new BufferedWriter(
                new FileWriter("index.txt", true));
        out.write(properties.toString());
        out.close();*/
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  private String removeStopWords() {
    try {
      String[] stopWords = documentReader.getStopWords("./resource/stoplist.txt");
      ArrayList<String> allWords = Stream.of(this.fileContents.toLowerCase().split(" ")).collect(Collectors.toCollection(ArrayList::new));
      allWords.removeAll(Arrays.asList(stopWords));
      String result = allWords.stream().collect(Collectors.joining(" "));
      return result;
    } catch (IOException e) {
      System.err.println(e.getMessage());
    }
    return null;
  }
}
