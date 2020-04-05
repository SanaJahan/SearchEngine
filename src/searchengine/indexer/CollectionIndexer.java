package searchengine.indexer;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.common.SolrInputDocument;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import searchengine.util.DocumentReader;

public class CollectionIndexer {
  private static final String urlString = "http://localhost:8983/solr/travelandeat/";
  private static final SolrClient solr = new HttpSolrClient.Builder(urlString).build();
  private static DocumentReader documentReader = new DocumentReader();

  public static void main(String[] args) throws IOException, SolrServerException {
    /*if (args.length != 1) {
      throw new IllegalArgumentException(
              "You must only indicate the path of the dataset.");
    }*/
    //String datasetPath = args[0];

    System.out.println("Clearing previous collection.");
    // First deleting previous collection.
    // Deleting the documents from Solr
    solr.deleteByQuery("*");

    // Saving the document
    solr.commit();
    System.out.println("Documents deleted");

    File folder = new File("./resource/crawledData/");

    readFolderToIndex(folder);

  }

  private static void readFolderToIndex(File folder) {
    List<SolrInputDocument> documentsList = new ArrayList<>();

    int documentsProcessed = 0;

    try {
      for (final File fileEntry : folder.listFiles()) {
        if (fileEntry.isDirectory()) {
          readFolderToIndex(fileEntry);
        } else {
          // file being read and tokenized
          readFile(fileEntry.getAbsolutePath(), documentsList);
          documentsProcessed++;
          if (documentsProcessed == 10000) {
            solr.add(documentsList);
            solr.commit();
            documentsList.clear();
            documentsProcessed = 0;
          }
        }
      }
      solr.add(documentsList);
      solr.commit();
      System.out.println("Documents indexed");
    } catch (Exception e) {
      System.out.println("Documents could not be indexed");
      e.printStackTrace();
    }
  }

  private static void readFile(String absolutePath, List<SolrInputDocument> documentsList) {
    SolrInputDocument document = new SolrInputDocument();
    File file = new File(absolutePath);
    try {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(file);
      doc.getDocumentElement().normalize();
      NodeList nodeList = doc.getElementsByTagName("field");
      for (int i = 0; i < nodeList.getLength(); i++) {
        Node nNode = nodeList.item(i);
        String textContent = nNode.getTextContent();
        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
          if (nNode.getAttributes().item(0).getNodeValue().equals("docId")) {
            String docId = normalize(nNode.getAttributes().item(0).getNodeValue());

            document.addField(docId, textContent);
          } else if (nNode.getAttributes().item(0).getNodeValue().equals("url")) {
            String url = normalize(nNode.getAttributes().item(0).getNodeValue());
            document.addField(url, textContent);
          } else if (nNode.getAttributes().item(0).getNodeValue().equals("content")) {
            String content = normalize(nNode.getAttributes().item(0).getNodeValue());
            document.addField(content, textContent);
          }
        }
      }
      documentsList.add(document);
    } catch (ParserConfigurationException | SAXException | IOException e) {
      e.printStackTrace();
    }
  }

  private static String normalize(String fileContents) {
    fileContents = documentReader.normalizeStr(fileContents);
    fileContents = fileContents.replaceAll("[^a-zA-Z\\s+]", "");
    return fileContents;
  }

}
