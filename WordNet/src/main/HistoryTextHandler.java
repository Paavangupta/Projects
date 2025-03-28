package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;

import java.util.List;


public class HistoryTextHandler extends NgordnetQueryHandler {

    private NGramMap ngm;

    public HistoryTextHandler(NGramMap map) {
        ngm = map;
    }

    @Override
    public String handle(NgordnetQuery q) {
        List<String> words = q.words();
        int startYear = q.startYear();
        int endYear = q.endYear();

        StringBuilder response = new StringBuilder();

        for (String s : words) {
            TimeSeries temp = ngm.weightHistory(s, startYear, endYear);
            response.append(s).append(": ").append(temp.toString()).append("\n");
        }
        return response.toString();
    }
}


