package searchengine.webcrawler;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.xml.sax.InputSource;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class WebSpider {

  private final Set<URL> links;
  private final long startTime;
  private static int count = 0;


  public WebSpider(final URL startURL) throws IOException, ParseException {

    this.links = new HashSet<>();
    this.startTime = System.currentTimeMillis();
    crawl(initURLs(startURL));
  }

  private void crawl(final Set<URL> URLS) {
    URLS.removeAll(this.links);
    if (!URLS.isEmpty()) {
      final Set<URL> newURLS = new HashSet<>();
      try {
        this.links.addAll(URLS);
        for (final URL url : URLS) {
          System.out.println("time = " + (System.currentTimeMillis() - this.startTime) +
                  " connect to : " + url);
          final Document document = Jsoup.connect(url.toString()).get();
          final Elements linksOnPage = document.select("a[href]");
          for (final Element page : linksOnPage) {
            if (page.text().matches("^.*\\b(www.theguardian.com|travel|TRAVEL|Travel)\\b.*$")) {
              final String urlText = page.attr("abs:href").trim();
              final URL discoveredURL = new URL(urlText);
              newURLS.add(discoveredURL);
              writeResults("document" + count++, discoveredURL, ".xml");
            }
          }
        }
      } catch (final Exception | Error ignored) {
      }
      crawl(newURLS);
    }
  }


  private void writeResults(String fileName, URL discoveredURL, String extension) throws IOException {
    System.out.println(discoveredURL);
    final Document document = Jsoup.connect(discoveredURL.toString()).get();
    writeToFile(fileName, discoveredURL.toString(), extension, document);
  }

  private void writeToFile(String fileName, String discoveredURL, String extension, Document document) {
    try {

      DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
      DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
      org.w3c.dom.Document doc = documentBuilder.newDocument();
      // root element
      org.w3c.dom.Element root = doc.createElement("add");
      doc.appendChild(root);
      // doc element
      org.w3c.dom.Element docu = doc.createElement("doc");
      root.appendChild(docu);
      // doc id
      org.w3c.dom.Element docID = doc.createElement("field");
      docID.setAttribute("name","docId");
      docID.appendChild(doc.createTextNode(fileName));
      docu.appendChild(docID);
      // URL element
      org.w3c.dom.Element url = doc.createElement("field");
      url.setAttribute("name","url");
      url.appendChild(doc.createTextNode(discoveredURL));
      docu.appendChild(url);
      // content element
      org.w3c.dom.Element content = doc.createElement("field");
      content.setAttribute("name","content");
      content.appendChild(doc.createTextNode(document.text()));
      docu.appendChild(content);

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource domSource = new DOMSource(doc);
      StreamResult streamResult = new StreamResult(new File("resource/crawledData/" + fileName + extension));
      transformer.transform(domSource, streamResult);

      System.out.println("Done creating XML File");
    }catch (Exception e){
      System.out.println(e.getMessage());
    }
  }


  private Set<URL> initURLs(final URL startURL) throws ParseException, IOException {
    Set<URL> urls = new HashSet<>();
    final Document document = Jsoup.connect(startURL.toString()).ignoreContentType(true).get();
    for (int i = 0; i < document.body().childNodeSize(); i++) {
      final Node linksOnPage = document.body().childNode(i);
      JSONParser parser = new JSONParser();
      JSONObject json = (JSONObject) parser.parse(linksOnPage.toString());
      JSONObject jsonResponse = (JSONObject) parser.parse(json.get("response").toString());
      JSONArray recs = (JSONArray) jsonResponse.get("results");
      JSONObject editions = (JSONObject) parser.parse(recs.get(0).toString());
      JSONArray n = (JSONArray) editions.get("editions");
      for (int j = 0; j < n.size(); j++) {
        JSONObject webUrl = (JSONObject) parser.parse(n.get(j).toString());
        urls.add(new URL(webUrl.get("webUrl").toString()));
      }
    }
    return urls;
  }

  public static void main(String[] args) throws IOException, ParseException {
    String section = "travel";
    new WebSpider(new URL("http://content.guardianapis.com/sections?q=" + section + "&api-key=" + "5299e22b-ca25-4d66-beef-07dadd4e2b5b"));
  }
}