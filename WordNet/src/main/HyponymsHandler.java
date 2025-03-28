package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.TimeSeries;

import java.util.*;

public class HyponymsHandler extends NgordnetQueryHandler {

    private WordNet wordNet;

    public HyponymsHandler(WordNet wordNet) {
        this.wordNet = wordNet;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();
        int k = q.k();

        Set<String> toConvert = new HashSet<>();
        Set<String>[] arrayOfSets = new HashSet[words.size()];
        int i = 0;
        for (String s: words) {
            arrayOfSets[i] = wordNet.hyponyms(s);
            i += 1;
        }
        for (String s: arrayOfSets[0]) {
            boolean toAdd = true;
            for (Set set: arrayOfSets) {
                if (!set.contains(s)) {
                    toAdd = false;
                    continue;
                }
            }
            if (toAdd) {
                toConvert.add(s);
            }
        }

        class Pair implements Comparable<Pair> {
            Double value;
            String s;
            public Pair(Double v, String s) {
                value = v;
                this.s = s;
            }
            @Override
            public int compareTo(Pair o) {
                if (o.value > this.value) {
                    return 1;
                } else if (o.value < this.value) {
                    return -1;
                }
                return 0;
            }
        }

        if (k != 0) {
            PriorityQueue<Pair> pq = new PriorityQueue<>();
            for (String s: toConvert) {
                TimeSeries ts = wordNet.getTs(s, startYear, endYear);
                if (ts != null) {
                    double totalFrequency = wordNet.getTsData(ts);
                    pq.add(new Pair(totalFrequency, s));
                }


            }
            List<String> ret = new ArrayList<>();
            while (k > 0 && !pq.isEmpty()) {
                ret.add(pq.poll().s);
                k -= 1;
            }
            Collections.sort(ret);
            return ret.toString();
        }

        List<String> toReturn = wordNet.hyponymsToList(toConvert);


        return toReturn.toString();


    }
}
