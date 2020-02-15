package searchengine.tokenizer;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import searchengine.util.DocumentReader;

/**
 * Data structure for tokens used for indexing [term <docId, freq>]
 */
public class Token {

  private String fileName;
  private String fileContents;
  private DocumentReader documentReader;


  public Token(String filename) {
    this.fileName = filename;
    this.documentReader = new DocumentReader();
  }


  public void readDocuments() throws IOException {
    String fileContents = documentReader.readFile(Charset.forName("utf-8"), this.fileName);
    this.fileContents = fileContents;
    this.tokenizeContent();
  }

  // TODO: remove unnecessary things and add single whitespaces only.
  // TODO: split into array based on whitespaces. Ignore the stop words.

  private void tokenizeContent() throws IOException {
    this.fileContents = removeStopWords();
    this.fileContents = this.fileContents.replaceAll("(?:&#[0-9]*;)", " ");
    this.fileContents = this.fileContents.replaceAll("\\,*", "");
    this.fileContents = this.fileContents.replaceAll("\\.*", "");
    this.fileContents = this.fileContents.replaceAll("[0-9]+", "");
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
    FileWriter fileWriter = new FileWriter("Samplefile.txt");
    fileWriter.write(fileContents);
    fileWriter.close();

  }
  // TODO: Make RecordNum as DocID while splitting.
  //  TODO: Sort the terms and merge the common documents.
  //  TODO: Make the term as key, tuple as value.
  // TODO: I think one way to associate docID is by getting docId here and immediately creating the Posting out of that
  //  before sending it for read to document reader. So for every iteration, we would have the docId before the file is even read.
  /*public void indexingTerms() {
      // change the string into array and for each term, add in hashmap
    HashMap<String, Tuple> map = new HashMap<>();
    for (int i = 0; i < fileContents.length(); i++) {
       if(!map.containsKey(fileContents[i])) {
          Posting posting = new Posting();
          posting.setDocumentID(documentID);
          List<Posting> postings = new ArrayList<>();
          postings.add(posting);
          Tuple tuple = new Tuple();
          tuple.setPostings(postings);
          tuple.setFrequencyOfTerms(1);
          map.put(fileContents[i], tuple);
       }
       else {
         Tuple tuple = map.get(fileContents[i]);
         Posting posting = new Posting();
         posting.setDocumentID(docID);
         tuple.getPostings().add(posting);
         tuple.setFrequencyOfTerms(tuple.getFrequencyOfTerms()+1);
         map.put(fileContents[i], tuple);
       }
    }
  }*/

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
