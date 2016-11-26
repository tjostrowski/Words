package pl.otto.words;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import pl.otto.words.counters.AllCounter;
import pl.otto.words.generator.FileGenerator;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by tomek on 26.11.16.
 */
@RunWith(value = BlockJUnit4ClassRunner.class)
public class AllCountingTest {

    private AllCounter allCounter;

    @Before
    public void setUp() {
        allCounter = new AllCounter("mock").init();
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testAll_shortWords() {
        Arrays.stream("ALA MA KOTA KOT MA ALE".split(" "))
                .forEach(word -> allCounter.handleWord(word));

        Map<Character, Integer> occurrences = allCounter.getCharacterCounts();

        Assertions.assertThat( occurrences.size() ).isEqualTo(7);
        Assertions.assertThat( occurrences.get('A') ).isEqualTo(6);
        Assertions.assertThat( occurrences.get('E') ).isEqualTo(1);
        Assertions.assertThat( occurrences.get('K') ).isEqualTo(2);
        Assertions.assertThat( occurrences.get('L') ).isEqualTo(2);
        Assertions.assertThat( occurrences.get('M') ).isEqualTo(2);
        Assertions.assertThat( occurrences.get('O') ).isEqualTo(2);
        Assertions.assertThat( occurrences.get('T') ).isEqualTo(2);
    }

    @Test
    public void testAll_repeatedWords() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 50; ++i) {
            sb.append("KARMAZYNOWY PIRATPIRAT ABC ");
        }

        Arrays.stream(sb.toString().trim().split(" "))
                .forEach(word -> allCounter.handleWord(word));

        Map<Character, Integer> occurrences = allCounter.getCharacterCounts();

        Assertions.assertThat( occurrences.get('K') ).isEqualTo(50);
        Assertions.assertThat( occurrences.get('A') ).isEqualTo(5*50);
    }
}
