package pl.otto.words.counters.handler;

import java.util.Arrays;

/**
 * Created by tomek on 26.11.16.
 */
public abstract class ArrayBasedLongWordHandler implements LongWordCounterHandler {

    protected long[] cacheForLongWords;
    protected int cacheForLongWordsSize;

    private static final int INIT_CACHE_SIZE_FOR_LONG_WORDS = (1 << 20);
    private static final float CACHE_RESIZE_FACTOR = 1.5f;

    protected void init() {
        cacheForLongWords = new long[INIT_CACHE_SIZE_FOR_LONG_WORDS];
        cacheForLongWordsSize = 0;
        Arrays.fill(cacheForLongWords, Long.MAX_VALUE);
    }

    protected void onBackedArraySizeChanged() {
        if (cacheForLongWordsSize >= cacheForLongWords.length) {
            long[] newLongCache = new long[(int)(cacheForLongWordsSize * CACHE_RESIZE_FACTOR)];
            Arrays.fill(newLongCache, Long.MAX_VALUE);
            System.arraycopy(cacheForLongWords, 0, newLongCache, 0, cacheForLongWordsSize);
            cacheForLongWords = newLongCache;
        }
    }

}
