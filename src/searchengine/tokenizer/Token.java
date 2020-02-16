package searchengine.tokenizer;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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

  private String fileContents;
  private DocumentReader documentReader;
  private String documentID;
  private HashMap<String, Tuple> map = new HashMap<>();

  public Token() {
    this.documentReader = new DocumentReader();
  }


  public void listFilesForFolder(final File folder) {
    for (final File fileEntry : folder.listFiles()) {
      if (fileEntry.isDirectory()) {
        listFilesForFolder(fileEntry);
      } else {
        readFile(fileEntry.getAbsolutePath());
      }
    }
    documentReader.printInvertedIndex(map);
  }




  public void readFile(String absolutePath) {
    File file = new File(absolutePath);
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
    this.fileContents = this.fileContents.replaceAll("[^a-zA-Z\\s+]", "");
    this.fileContents = removeStopWords();
    indexingTerms();
  }

  // TODO: Currently two problems, the posting is not being added as a list.
  private void indexingTerms() {
    // change the string into array and for each term, add in hashMap
    String[] allTerms = fileContents.split(" ");
    for (int i = 0; i < allTerms.length; i++) {
      if (!allTerms[i].equals("")) {
        if (!map.containsKey(allTerms[i].trim())) {
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
          if (!tuple.getPostings().contains(posting)) {
            tuple.getPostings().add(posting);
          }
          tuple.setFrequencyOfTerms(tuple.getFrequencyOfTerms() + 1);
          map.put(allTerms[i], tuple);
        }
      }
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
