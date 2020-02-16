# SearchEngine
Project for Information Retrieval

## How to compile code
1. Download the zip file or take a clone <br>
2. Run the Main.java class. <br>
To be noted: Please remove the entries from the index.properties file to test the assignment afresh as it will append to the existing entries.<br>

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


