package pl.otto.words.counters.handler;

import pl.otto.words.counters.DistinctCounter;
import pl.otto.words.utils.Utils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static pl.otto.words.utils.Utils.bytesToLong;
import static pl.otto.words.utils.Utils.longToBytes;

/**
 * Created by tomek on 26.11.16.
 */
public class JdkWithCustomTypesHandler implements LongWordCounterHandler {

    private Set<CustomNumber> backedSet;
    private DistinctCounter counter;

    public JdkWithCustomTypesHandler(DistinctCounter counter) {
        this.counter = counter;
        backedSet = new HashSet<>();
    }

    @Override
    public void handleWord(String word, long numberFromWord) {
        long wordNumber = counter.wordToNumber(word);
        CustomNumber backedVal = new CustomNumber(wordNumber);
        if (!backedSet.contains(backedVal)) {
            counter.updateUniqueCharsMap(word);
            backedSet.add(backedVal);
        }
    }

    private static class CustomNumber {
        private byte[] backedArray;

        public CustomNumber(long value) {
            this.backedArray = longToBytes(value);
        }

        public byte[] getBackedArray() {
            return backedArray;
        }

        @Override
        public int hashCode() {
            return (int) bytesToLong(backedArray);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CustomNumber that = (CustomNumber) o;

            return Arrays.equals(backedArray, that.backedArray);
        }
    }
}
