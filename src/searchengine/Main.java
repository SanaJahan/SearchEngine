package searchengine;

import java.io.File;

import searchengine.tokenizer.Token;

public class Main {

    public static void main(String[] args) {
                Token token = new Token();
                File file = new File("./resource/cfxml/");
                token.listFilesForFolder(file);
        }
}
