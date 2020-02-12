package searchengine;

import searchengine.tokenizer.Token;

public class Main {

    public static void main(String[] args) {
        Token token = new Token("cf74.xml");
        token.readDocuments();
    }
}
