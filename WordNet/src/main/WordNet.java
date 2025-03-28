package main;

import edu.princeton.cs.algs4.In;
import ngrams.TimeSeries;

import java.sql.Time;
import java.util.*;

public class WordNet {

    private Graph graph;
    private HashMap<String, List<Integer>> wordToId;
    private HashMap<String, TimeSeries> wordFrequency;


    public WordNet(String synsetFileName, String hyponymsFileName, String frequencyFileName, String countFileName) {

        graph = new Graph();

        In parser = new In(synsetFileName);
        while (parser.hasNextLine()) {
            String currLine = parser.readLine();
            String[] splitLine = currLine.split(",");
            int SynsetId = Integer.valueOf(splitLine[0]);
            String[] wordList = splitLine[1].split(" ");
            for (String k: wordList) {
                graph.addNode(SynsetId, k);
            }
        }
        In hyponymParser = new In(hyponymsFileName);
        while (hyponymParser.hasNextLine()) {
            String currLine = hyponymParser.readLine();
            String[] splitLine = currLine.split(",");
            int SynsetId = Integer.valueOf(splitLine[0]);
            String[] intList = Arrays.copyOfRange(splitLine, 1, splitLine.length);
            for (String k: intList) {
                graph.addEdge(SynsetId, Integer.valueOf(k));
            }
        }
        In frequencyParser = new In(frequencyFileName);
        wordFrequency = new HashMap<>();
        while (frequencyParser.hasNextLine()) {
            String currLine = frequencyParser.readLine();
            String[] splitLine = currLine.split("\t");
            String word = splitLine[0];
            int currYear = Integer.valueOf(splitLine[1]);
            double wordCount = Double.parseDouble(splitLine[2]);
            if (wordFrequency.containsKey(word)) {
                wordFrequency.get(word).put(currYear, wordCount);
            } else {
                TimeSeries x = new TimeSeries();
                x.put(currYear, wordCount);
                wordFrequency.put(word, x);
            }

        }
    }

    //returns set of hyponym words
    public Set<String> hyponyms(String s) {
        Set<Integer> temp = graph.hyponymIds(s);
        Set<String> toReturn = new HashSet<>();
        for (Integer k: temp) {
            toReturn.addAll(graph.wordsAtId(k));
        }
        return toReturn;
    }

    //returns ordered list of hyponyms
    public List<String> hyponymsToList(Set<String> hs) {
        List<String> toReturn = new ArrayList<>(hs);
        Collections.sort(toReturn);
        return toReturn;
    }

    public TimeSeries getTs(String s, int startYear, int endYear) {
        TimeSeries temp = wordFrequency.getOrDefault(s, null);
        if (temp == null) {
            return null;
        }
        TimeSeries reqTs = new TimeSeries(temp, startYear, endYear);
        return reqTs;
    }


    public Double getTsData(TimeSeries reqTs) {
        List<Double> ls = reqTs.data();
        double sum = 0.0;
        for (double i: ls) {
            sum += i;
        }
        return sum;
    }

}
