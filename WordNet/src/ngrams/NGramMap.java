package ngrams;

import edu.princeton.cs.algs4.In;

import java.util.Collection;
import java.util.HashMap;

import static ngrams.TimeSeries.MAX_YEAR;
import static ngrams.TimeSeries.MIN_YEAR;

/**
 * An object that provides utility methods for making queries on the
 * Google NGrams dataset (or a subset thereof).
 *
 * An NGramMap stores pertinent data from a "words file" and a "counts
 * file". It is not a map in the strict sense, but it does provide additional
 * functionality.
 *
 * @author Josh Hug
 */
public class NGramMap {


    private TimeSeries totalCountProcessed;
    private HashMap<String, TimeSeries> allWords;
    /**
     * Constructs an NGramMap from WORDSFILENAME and COUNTSFILENAME.
     */
    public NGramMap(String wordsFilename, String countsFilename) {

        In parser = new In(countsFilename);
        totalCountProcessed = new TimeSeries();
        while (parser.hasNextLine()) {
            String currLine = parser.readLine();
            String[] splitLine = currLine.split(",");
            int currYear = Integer.valueOf(splitLine[0]);
            double wordCount = Double.parseDouble(splitLine[1]);
            totalCountProcessed.put(currYear, wordCount);
        }

        In wordParser = new In(wordsFilename);
        allWords = new HashMap<>();
        while (wordParser.hasNextLine()) {
            String currLine = wordParser.readLine();
            String[] splitLine = currLine.split("\t");
            String word = splitLine[0];
            int currYear = Integer.valueOf(splitLine[1]);
            double wordCount = Double.parseDouble(splitLine[2]);
            if (allWords.containsKey(word)) {
                allWords.get(word).put(currYear, wordCount);
            } else {
                TimeSeries x = new TimeSeries();
                x.put(currYear, wordCount);
                allWords.put(word, x);
            }

        }

    }

    /**
     * Provides the history of WORD between STARTYEAR and ENDYEAR, inclusive of both ends. The
     * returned TimeSeries should be a copy, not a link to this NGramMap's TimeSeries. In other
     * words, changes made to the object returned by this function should not also affect the
     * NGramMap. This is also known as a "defensive copy". If the word is not in the data files,
     * returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word, int startYear, int endYear) {
        if (allWords.containsKey(word)) {
            return new TimeSeries(allWords.get(word), startYear, endYear);
        }
        return new TimeSeries();
    }

    /**
     * Provides the history of WORD. The returned TimeSeries should be a copy, not a link to this
     * NGramMap's TimeSeries. In other words, changes made to the object returned by this function
     * should not also affect the NGramMap. This is also known as a "defensive copy". If the word
     * is not in the data files, returns an empty TimeSeries.
     */
    public TimeSeries countHistory(String word) {
        return countHistory(word, MIN_YEAR, MAX_YEAR);
    }

    /**
     * Returns a defensive copy of the total number of words recorded per year in all volumes.
     */
    public TimeSeries totalCountHistory() {
        return totalCountProcessed.plus(new TimeSeries());
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD between STARTYEAR
     * and ENDYEAR, inclusive of both ends. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word, int startYear, int endYear) {
        return countHistory(word, startYear, endYear).dividedBy(totalCountProcessed);
    }

    /**
     * Provides a TimeSeries containing the relative frequency per year of WORD compared to all
     * words recorded in that year. If the word is not in the data files, returns an empty
     * TimeSeries.
     */
    public TimeSeries weightHistory(String word) {
        return countHistory(word).dividedBy(totalCountProcessed);
    }

    /**
     * Provides the summed relative frequency per year of all words in WORDS between STARTYEAR and
     * ENDYEAR, inclusive of both ends. If a word does not exist in this time frame, ignore it
     * rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words,
                                          int startYear, int endYear) {
        TimeSeries augmented = new TimeSeries();
        for (String s: words) {
            augmented = augmented.plus(countHistory(s, startYear, endYear));
        }
        return augmented.dividedBy(totalCountProcessed);
    }

    /**
     * Returns the summed relative frequency per year of all words in WORDS. If a word does not
     * exist in this time frame, ignore it rather than throwing an exception.
     */
    public TimeSeries summedWeightHistory(Collection<String> words) {
        return summedWeightHistory(words, MIN_YEAR, MAX_YEAR);
    }

}
