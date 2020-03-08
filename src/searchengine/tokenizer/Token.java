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
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import searchengine.util.DocumentReader;

/**
 * Data structure for tokens used for indexing [term = frequency docID docID ...]
 */
public class Token {

  private String fileContents;
  private DocumentReader documentReader;
  private String documentID;
  private HashMap<String, Tuple> map = new HashMap<>();

  public Token() {
    this.documentReader = new DocumentReader();
  }


  /**
   * Entry point for the folder from where all the files will be read for tokenizing and indexing.
   * @param folder - the location for reading all the files.
   */
  public Map<String,Tuple> readFilesFromFolder(final File folder) {
    for (final File fileEntry : folder.listFiles()) {
      if (fileEntry.isDirectory()) {
        readFilesFromFolder(fileEntry);
      } else {
        // file being read and tokenized
        readFile(fileEntry.getAbsolutePath());
      }
    }
    // final step: sort the index & write it into the disk.
    //documentReader.createInvertedIndex(map);
    return map;
  }


  private void readFile(String absolutePath) {
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

  /**
   *
   * @param fileLocation
   */

  public String readFromFile(final String fileLocation) {
    readFile(fileLocation);
    return documentReader.writeToHTML(map);
  }



  /**
   * Removes all the unnecessary special characters, numbers.
   * stores the RECORDNUM as the documentID for each record which will be used for indexing.
   * calls the indexing method to index the document contents.
   */
  private void tokenizeContent() {
    this.fileContents = documentReader.normalizeStr(this.fileContents);
    this.documentID = fileContents.split(" ")[2];
    this.fileContents = fileContents.replaceAll("[^a-zA-Z\\s+]", "");
    this.fileContents = removeStopWords();
    indexingTerms();
  }


  /**
   * Indexing follows the single-pass in-memory indexing [SPIMI] algorithm.
   * Indexing algorithm: The entire document contents converted to an array of terms.
   * For each term, an index is created which stores the term as key, and a list of postings as value,
   * where each posting has the record of the documentID.
   * If the term is already present in the hashMap, then just add the new posting in the value for the given term.
   */
  private void indexingTerms() {
    // change the string into array and for each term, add in hashMap
    String[] allTerms = fileContents.split(" ");
    for (int i = 0; i < allTerms.length; i++) {
      // TODO: check why "" is being indexed. For now add a check.
      if (!allTerms[i].equals("")) {
        if (!map.containsKey(allTerms[i].trim())) {
          Posting posting = new Posting();
          posting.setDocumentID(documentID);
          //set the occurrence of the all the terms in that specific document
          posting.setTotalFrequency(allTerms.length);
          // set the frequency of that term in that specific document
          Map<String, Double> termFreqMap = new HashMap<>();
          termFreqMap.put(allTerms[i],1.0);
          posting.setSpecificTermFreq(termFreqMap);

          List<Posting> postings = new ArrayList<>();
          postings.add(posting);

          Tuple tuple = new Tuple();
          tuple.setPostings(postings);
          tuple.setFrequencyOfTerms(1);

          // add the term frequency in each document
          Map<String,Posting> termFreqInDocMap = new HashMap<>();
          termFreqInDocMap.put(posting.getDocumentID(),posting);
          tuple.setTermFreqInDoc(termFreqInDocMap);

          map.put(allTerms[i], tuple);
        } else {
          Tuple tuple = map.get(allTerms[i]);
          Posting posting = new Posting();
          posting.setDocumentID(documentID);

          // if the list of postings does not contain the document, add it to the list
          if (!tuple.getPostings().contains(posting)) {
            Map<String, Double> termFreqMap = new HashMap<>();
            termFreqMap.put(allTerms[i],1.0);
            posting.setSpecificTermFreq(termFreqMap);
            posting.setTotalFrequency(allTerms.length);


            tuple.getPostings().add(posting);
            Map<String,Posting> tupleMap = tuple.getTermFreqInDoc();
            tupleMap.put(documentID,posting);
          }
          else{
            Posting existingPosting = tuple.getTermFreqInDoc().get(documentID);
            Map<String,Double> increaseTermFreq = existingPosting.getSpecificTermFreq();
            Double freq = increaseTermFreq.get(allTerms[i]) + 1.0;
            increaseTermFreq.put(allTerms[i],freq);
            existingPosting.setSpecificTermFreq(increaseTermFreq);
            tuple.getTermFreqInDoc().put(documentID,existingPosting);
          }
          tuple.setFrequencyOfTerms(tuple.getFrequencyOfTerms() + 1);

          // set the entry of the tuple to the hashMap or dictionary
          map.put(allTerms[i], tuple);
        }
      }
    }
  }


  /**
   * Removing stop words from the contents.
   * @return a string of contents removing all the stopwords from it.
   */
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
