package pl.otto.words.counters.handler;

import pl.otto.words.counters.DistinctCounter;

import java.util.Arrays;

/**
 * Created by tomek on 26.11.16.
 */
public class BinarySearchLongWordHandler extends ArrayBasedLongWordHandler {

    private DistinctCounter counter;

    public BinarySearchLongWordHandler(DistinctCounter counter) {
        this.counter = counter;
        init();
    }

    @Override
    public void handleWord(String word, long numberFromWord) {
        int searchRes = Arrays.binarySearch(cacheForLongWords, numberFromWord);
        if (searchRes >= 0) {
            return;
        }

        int insertPoint = -searchRes - 1;

        System.arraycopy(cacheForLongWords, insertPoint, cacheForLongWords, insertPoint+1,
                cacheForLongWordsSize - insertPoint);

        cacheForLongWords[insertPoint] = numberFromWord;
        cacheForLongWordsSize++;

        counter.updateUniqueCharsMap(word);

        onBackedArraySizeChanged();
    }
}
