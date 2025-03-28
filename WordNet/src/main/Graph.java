package main;

import java.util.*;

public class Graph {

    private HashMap<Integer, List<String>> nodeMap;
    private HashMap<Integer, List<Integer>> edgeMap;
    private HashMap<String, List<Integer>> wordMap;

    public Graph() {
        nodeMap = new HashMap<>();
        edgeMap = new HashMap<>();
        wordMap = new HashMap<>();
    }

    //Store words(siblings) with their Synset Id
    public void addNode(Integer n, String s) {
        List<String> temp = nodeMap.getOrDefault(n, new ArrayList<>());
        temp.add(s);
        nodeMap.put(n, temp);
        List<Integer> tp = wordMap.getOrDefault(s, new ArrayList<>());
        tp.add(n);
        wordMap.put(s, tp);
    }

    // Store Hyponyms for integer node[Id]
    public void addEdge(Integer n, Integer ln) {
        List<Integer> temp = edgeMap.getOrDefault(n, new ArrayList<>());
        temp.add(ln);
        edgeMap.put(n, temp);
    }

    //Returns the hyponyms ids [Not the siblings]
    public Set<Integer> getHyponymIds(Integer n) {
        HashSet<Integer> mark = new HashSet<>();
        helper(n, mark);
        return mark;
    }

    private void helper(Integer k, HashSet<Integer> marked) {
        marked.add(k);
        for (int i : edgeMap.getOrDefault(k, new ArrayList<>())) {
            if (!marked.contains(i)) {
                helper(i, marked);
            }
        }
    }

    //Ids of the same word
    public List<Integer> wordId(String s) {
        return wordMap.getOrDefault(s, new ArrayList<>());
    }

    //returns hyponym ids for a word
    public Set<Integer> hyponymIds(String s) {
        List<Integer> ids = wordId(s);
        HashSet<Integer> toConvert = new HashSet<>();
        for (int i: ids) {
            toConvert.addAll(getHyponymIds(i));
        }
        return toConvert;
    }

    //words of the same id
    public List<String> wordsAtId(Integer n) {
        return nodeMap.getOrDefault(n, new ArrayList<>());
    }

}
