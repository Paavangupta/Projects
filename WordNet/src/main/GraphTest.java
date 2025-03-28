package main;

import browser.NgordnetQuery;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.google.common.truth.Truth.assertThat;
import static main.Main.*;

public class GraphTest {
    @Test
    public void tester() {
        WordNet wn = new WordNet("data/wordnet/synsets.txt", "data/wordnet/hyponyms.txt", "data/ngrams/top_14377_words.csv", "data/ngrams/total_counts.csv");
        HyponymsHandler hh = new HyponymsHandler(wn);
        List<String> l = new ArrayList<>();
        l.add("city");
        NgordnetQuery nq = new NgordnetQuery(l, 1470, 2019, 3);
        String actual = hh.handle(nq);
        String expected = "[city]";
        assertThat(actual).isEqualTo(expected);
    }
}
