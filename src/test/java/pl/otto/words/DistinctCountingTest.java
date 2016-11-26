package pl.otto.words;

import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import pl.otto.words.counters.DistinctCounter;

import java.util.Arrays;
import java.util.Map;

import static pl.otto.words.utils.Utils.bytesToLong;
import static pl.otto.words.utils.Utils.longToBytes;

/**
 * Created by tomek on 26.11.16.
 */
@RunWith(value = BlockJUnit4ClassRunner.class)
public class DistinctCountingTest {

    private DistinctCounter distinctCounter;

    @Before
    public void setUp() {
        distinctCounter = new DistinctCounter("mock").init("jhash");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testDistinct_shortWords() {
        Arrays.stream("ALA MA KOTA KOT MA ALE".split(" "))
            .forEach(word -> distinctCounter.handleWord(word));

        Map<Character, Integer> occurrences = distinctCounter.getCharacterCounts();

        Assertions.assertThat( occurrences.size() ).isEqualTo(7);
        Assertions.assertThat( occurrences.get('A') ).isEqualTo(4);
        Assertions.assertThat( occurrences.get('E') ).isEqualTo(1);
        Assertions.assertThat( occurrences.get('K') ).isEqualTo(2);
        Assertions.assertThat( occurrences.get('L') ).isEqualTo(2);
        Assertions.assertThat( occurrences.get('M') ).isEqualTo(1);
        Assertions.assertThat( occurrences.get('O') ).isEqualTo(2);
        Assertions.assertThat( occurrences.get('T') ).isEqualTo(2);
    }

    @Test
    public void testDistinct_shortAndLongWords() {
        Arrays.stream("ALA MA KOTAKOTAR KOT MANCYKARY ALE".split(" "))
                .forEach(word -> distinctCounter.handleWord(word));

        Map<Character, Integer> occurrences = distinctCounter.getCharacterCounts();

        Assertions.assertThat( occurrences.get('K') ).isEqualTo(3);
        Assertions.assertThat( occurrences.get('R') ).isEqualTo(2);
        Assertions.assertThat( occurrences.get('O') ).isEqualTo(2);
        Assertions.assertThat( occurrences.get('M') ).isEqualTo(2);
        Assertions.assertThat( distinctCounter.getNumLongWordsRead() ).isEqualTo(2);
    }

    @Test
    public void testDistinct_shortAndLongWords_longRepeated() {
        Arrays.stream("ALA MA KOTAKOTAR KOT MANCYKARY MANCYKARY ALE".split(" "))
                .forEach(word -> distinctCounter.handleWord(word));

        Map<Character, Integer> occurrences = distinctCounter.getCharacterCounts();

        Assertions.assertThat( occurrences.get('K') ).isEqualTo(3);
        Assertions.assertThat( occurrences.get('R') ).isEqualTo(2);
        Assertions.assertThat( occurrences.get('O') ).isEqualTo(2);
        Assertions.assertThat( occurrences.get('M') ).isEqualTo(2);
        Assertions.assertThat( distinctCounter.getNumLongWordsRead() ).isEqualTo(3);
    }

    @Test
    public void testDistinct_repeatedWords() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 50; ++i) {
            sb.append("KARMAZYNOWY PIRATPIRAT ABC ");
        }

        Arrays.stream(sb.toString().trim().split(" "))
                .forEach(word -> distinctCounter.handleWord(word));

        Map<Character, Integer> occurrences = distinctCounter.getCharacterCounts();

        Assertions.assertThat( occurrences.get('K') ).isEqualTo(1);
        Assertions.assertThat( occurrences.get('A') ).isEqualTo(3);
    }

    @Test
    public void testLongConversion() {
        long val = Long.MAX_VALUE;
        Assertions.assertThat(bytesToLong(longToBytes(val))).isEqualTo(val);
        Assertions.assertThat(bytesToLong(longToBytes(val-1))).isEqualTo(val-1);
        Assertions.assertThat(bytesToLong(longToBytes(val >> 8 ))).isEqualTo(val >> 8);
        Assertions.assertThat(bytesToLong(longToBytes((val >> 8) - 10))).isEqualTo((val >> 8) - 10);
        Assertions.assertThat(bytesToLong(longToBytes((val >> 16) - 10))).isEqualTo((val >> 16) - 10);
    }

}
