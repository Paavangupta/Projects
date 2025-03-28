package main;

import browser.NgordnetQuery;
import browser.NgordnetQueryHandler;
import ngrams.NGramMap;
import ngrams.TimeSeries;
import org.knowm.xchart.XYChart;
import plotting.Plotter;

import java.util.ArrayList;

public class HistoryHandler extends NgordnetQueryHandler {

    private NGramMap ngm;

    public HistoryHandler(NGramMap map) {
        ngm = map;
    }


    @Override
    public String handle(NgordnetQuery q) {

        ArrayList<TimeSeries> lts = new ArrayList<>();
        ArrayList<String> labels = new ArrayList<>();

        for (String s : q.words()) {
            labels.add(s);
            lts.add(ngm.weightHistory(s, q.startYear(), q.endYear()));
        }

        XYChart chart = Plotter.generateTimeSeriesChart(labels, lts);
        String encodedImage = Plotter.encodeChartAsString(chart);

        return encodedImage;
    }
}
