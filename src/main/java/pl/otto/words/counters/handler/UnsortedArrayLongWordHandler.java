package pl.otto.words.counters.handler;

import pl.otto.words.counters.DistinctCounter;

import java.util.Arrays;
import java.util.stream.IntStream;

/**
 * Created by tomek on 26.11.16.
 */
public class UnsortedArrayLongWordHandler extends ArrayBasedLongWordHandler {

    private DistinctCounter counter;

    public UnsortedArrayLongWordHandler(DistinctCounter counter) {
        this.counter = counter;
        init();
    }

    @Override
    public void handleWord(String word, long numberFromWord) {
        long wordNumber = counter.wordToNumber(word);

        int foundIdx = IntStream.range(0, cacheForLongWordsSize)
                .filter(i -> cacheForLongWords[i] == wordNumber)
                .findFirst().orElse(-1);

        if (foundIdx == -1) {
            cacheForLongWords[cacheForLongWordsSize++] = wordNumber;
            counter.updateUniqueCharsMap(word);
            onBackedArraySizeChanged();
        }
    }
}
