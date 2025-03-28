package main;

import browser.NgordnetQueryHandler;


public class AutograderBuddy {
    /** Returns a HyponymHandler */
    public static NgordnetQueryHandler getHyponymsHandler(
            String wordFile, String countFile,
            String synsetFile, String hyponymFile) {
        WordNet wn = new WordNet(synsetFile, hyponymFile, wordFile, countFile);
        return new HyponymsHandler(wn);

        //throw new RuntimeException("Please fill out AutograderBuddy.java!");
    }
}
