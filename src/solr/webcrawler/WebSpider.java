package solr.webcrawler;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
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

  private final Set<String> links;
  private final long startTime;
  private static int count = 0;
  private HashSet<String> urlMap = new HashSet<>();

  public WebSpider(final URL startURL) throws IOException, ParseException {

    this.links = new HashSet<String>();
    this.startTime = System.currentTimeMillis();
    crawl(initURLs(startURL));
  }

  private void crawl(final Set<String> URLS) {
    URLS.removeAll(this.links);
    if (!URLS.isEmpty()) {
      final Set<String> newURLS = new HashSet<>();
      try {
        this.links.addAll(URLS);
        for (final String url : URLS) {
          final Document document = Jsoup.connect(url.toString()).get();
          final Elements linksOnPage = document.select("a[href]");
          for (final Element page : linksOnPage) {
            final String urlText = page.attr("abs:href").trim();
            // changes to make sure url is relevant.
            final URL discoveredURL = new URL(urlText);
            String urlPath = discoveredURL.toString().toLowerCase();
            /*if(!(urlPath.indexOf("facebook") != -1 || urlPath.indexOf("twitter") != -1 ||
                    urlPath.contains("support.theguardian.com") || urlPath.indexOf("preference") != -1 ||
                    urlPath.indexOf("subscription") != -1 || urlPath.indexOf("subscribe") != -1 ||
                    urlPath.indexOf("signin") != -1 || urlPath.indexOf("signout") != -1 ||
                    urlPath.indexOf("manage") != -1 || urlPath.indexOf("#maincontent") != -1 ||
                    urlPath.indexOf("contributions") != -1 || urlPath.indexOf("commentisfree") != -1||
                    urlPath.indexOf("theguardian.newspapers.com") != -1) || urlPath.indexOf("privacystatement") != -1
            || urlPath.indexOf("about-guardian-us") != -1 || urlPath.contains("profile.theguardian.com") ||
            urlPath.indexOf("www.google.co.uk")!= -1) */
            if(!urlPath.contains("facebook") && (urlPath.contains("travel") ||
                    urlPath.contains("food") || urlPath.contains("world") || urlPath.contains("recipes"))){
              newURLS.add(discoveredURL.toString());
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
    final Document document = Jsoup.connect(discoveredURL.toString()).get();
    //to prevent writing duplicate websites
      if(!urlMap.contains(discoveredURL.toString())){
        urlMap.add(discoveredURL.toString());
        String[] segments = discoveredURL.getPath().split("/");
        String title = segments[segments.length-1];
        title = title.replace("-", " ");
        title = title.substring(0, 1).toUpperCase() + title.substring(1);
        writeToFile(fileName, discoveredURL.toString(), title, extension, document);
      }
  }

  private void writeToFile(String fileName, String discoveredURL, String title, String extension, Document document) {
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

      //title element
      org.w3c.dom.Element titleName = doc.createElement("field");
      titleName.setAttribute("name","title");
      titleName.appendChild(doc.createTextNode(title));
      docu.appendChild(titleName);

      // content element
      org.w3c.dom.Element content = doc.createElement("field");
      content.setAttribute("name","content");
      content.appendChild(doc.createTextNode(document.text()));
      docu.appendChild(content);

      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      Transformer transformer = transformerFactory.newTransformer();
      DOMSource domSource = new DOMSource(doc);
      StreamResult streamResult = new StreamResult(new File("resource/travelData/" + fileName + extension));
      transformer.transform(domSource, streamResult);

      System.out.println("Done creating XML File for : " + discoveredURL);
    }catch (Exception e){
      System.out.println(e.getMessage());
    }
  }


  private Set<String> initURLs(final URL startURL) throws ParseException, IOException {
    Set<String> urls = new HashSet<>();
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
        urls.add(webUrl.get("webUrl").toString());
      }
    }
    return urls;
  }

  public static void main(String[] args) throws IOException, ParseException {
    String travelSection = "travel";
    String foodSection = "food";
    new WebSpider(new URL("http://content.guardianapis.com/sections?q=" + travelSection + "&api-key=" + "5299e22b-ca25-4d66-beef-07dadd4e2b5b"));
    new WebSpider(new URL("http://content.guardianapis.com/sections?q=" + foodSection + "&api-key=" + "5299e22b-ca25-4d66-beef-07dadd4e2b5b"));
  }
}
