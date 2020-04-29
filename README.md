# Vertical Search Engine

Folder structure:
The src folder has the solr directory. In the solr directory,
1. indexer folder contains the script to index the dataset to the solr server.
2. webcrawler folder contains the script to run the web crawler to create the dataset.
<br>For the UI,
- user-interface folder contains the UI that connects the solr server to the user-interface.

## How to run the project
1. Download the solr folder, the user-interface folder & the resource folder. Create the travelData folder in the resource folder.
2. The system should have  apache solr installed as prerequisite.
3. Run the solr server in "http://localhost:8983/" and create the core "travelandeat"
4. Give the location of the solr folder path in the <b>instanceDir<b> option in solr server to create the data folder in the local solr folder.
5. Run the WebSpider script to crawl and create the dataset, which is in XML format.
6. Run the CollectionIndexer script to index the dataset.
7. Go to query option in solr server and run execute to check if the dataset has been indexed.
8. In order to run the user-interface in "http://localhost:3000/", first run <i>yarn</i> to install all the dependencies and 
then run <i>yarn start</i> to run the UI.

![Result Image1](/images/tourism.png)
<br>
<br>
<br>
<br>

***Please note in some results the title can have similar names but they are different urls taking us to a different webpage***


# SearchEngine
###### Implementation of various phases of Search Engine.
1. Indexing
2. Inverted Index
3. Ranking documents ( using cosine similarity )

## How to compile code
1. Download the zip file or take a clone <br>
2. Run the Main.java class. <br>
To be noted: Please remove the entries from the index.properties file to test the functionality afresh as it will append to the existing entries.<br>

## Description of the solution to create the inverted index
##### The algorithm used is ** Single-Pass-In-Memory ** indexing. 
The data structure that represents each entry for the inverted index is a tuple.<br>
The tuple has term ( which is treated as the key ), frequency of the term in all the documents and list of postings.<br>
> posting is another data structure which holds the documentId of the document where the term has occurred.

First, all the contents of each document is traversed and converted into tokens.<br><br>
For tokenizing, all the special characters are removed, numbers are removed, stop words are removed from the contents. <br><br>
After tokening is done, all the tokens are indexed into a hashmap.<br><br>
In case, the hashmap already contains the ** tuple ** then the new posting is added to the existing posting list of that term and the frequency is incremented by 1. <br><br>
After the hashmap is created, it is sorted using LinkedHashMap before it is written into the disk. <br>
Finally, the sorted hashmap is written into the "index.properties" file. <br><br>
***Please note, there is no condition to check if memory becomes full on adding the tuples since, in java, Hashmap internally takes care of creating extra memory if the memory becomes full.***

## Description of the solution for scoring the documents
##### The algorithm used is *** Cosine Similarity *** using the concept of tf-idf



