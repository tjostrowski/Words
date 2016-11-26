package pl.otto.words.counters;

import org.apache.log4j.Logger;
import pl.otto.words.counters.handler.*;
import pl.otto.words.io.WordFileReader;

import java.util.*;
import java.util.stream.Collectors;

import static pl.otto.words.utils.Consts.A_CODE;
import static pl.otto.words.utils.Consts.NUM_CHARS;

/**
 * Created by tomek on 26.11.16.
 */
public class DistinctCounter {

    private static final Logger LOGGER = Logger.getLogger(DistinctCounter.class);

    private String inFile;

    private static final int MAX_CHARS_PER_SHORT_CACHE = 6;

    private BitSet cacheForShortWords;

    private LongWordCounterHandler longWordsHandler;

    private Map<Character, Integer> characterCounts;

    private int numWordsRead;
    private int numShortWordsRead;
    private int numLongWordsRead;

    public DistinctCounter(String inFile) {
        this.inFile = inFile;
    }

    public DistinctCounter init(String longWordsHandlerStr) {
        cacheForShortWords = new BitSet((int)Math.pow(NUM_CHARS, MAX_CHARS_PER_SHORT_CACHE));

        longWordsHandler =
//                new JdkWithCustomTypesHandler(this);
                chooseLongWordsHandler(longWordsHandlerStr);
        LOGGER.debug("** Using long words handler: " + longWordsHandler.getClass().getSimpleName());

        characterCounts = new TreeMap<>();

        return this;
    }

    public void handleInput() {
        WordFileReader reader = new WordFileReader(inFile);

        numWordsRead = 0;
        numShortWordsRead = 0;
        numLongWordsRead = 0;

        String word;
        while ((word = reader.readWord()) != null /*&& numWordsRead < 1_000_000*/) {
            handleWord(word);

            if ((++numWordsRead % 100_000) == 0) {
                System.out.print('*');
                if (numWordsRead % 1_000_000 == 0) {
                    System.out.print("||");
                }
            }
        }

        LOGGER.debug("*********** STATISTICS ***********");
        LOGGER.debug("Number of words read = " + numWordsRead);
        LOGGER.debug("Number of short words read = " + numShortWordsRead);
        LOGGER.debug("Number of long words read = " + numLongWordsRead);
        LOGGER.debug("**********************************");
    }

    public void handleWord(String word) {
        long numberFromWord = wordToNumber(word);
        if (numberFromWord <= Integer.MAX_VALUE) {
            numShortWordsRead++;
            handleShortWord(word, numberFromWord);
        } else {
            numLongWordsRead++;
            longWordsHandler.handleWord(word, numberFromWord);
        }
    }

    public void handleWord_divideOnWordSize(String word) {
        final int wordSize = word.length();
        long numberFromWord = wordToNumber(word);
        if (wordSize <= MAX_CHARS_PER_SHORT_CACHE) {
            numShortWordsRead++;
            handleShortWord(word, numberFromWord);
        } else {
            numLongWordsRead++;
            longWordsHandler.handleWord(word, numberFromWord);
        }
    }

    private void handleShortWord(String word, long numberFromWord) {
        if (!cacheForShortWords.get((int)numberFromWord)) {
            updateUniqueCharsMap(word);
            cacheForShortWords.set((int)numberFromWord);
        }
    }

    public void updateUniqueCharsMap(String word) {
        uniqueChars(word)
            .forEach(c -> {
                Integer currentCount = characterCounts.get(c);
                if (currentCount == null) {
                    characterCounts.put(c, 1);
                } else{
                    characterCounts.put(c, currentCount+1);
                }
            });
    }

    private Set<Character> uniqueChars(String word) {
        return word.chars()
            .mapToObj(ci -> (char)ci)
            .collect(Collectors.toSet());
    }

    public long wordToNumber(String str) {
        return str.chars()
            .mapToLong(c -> c - A_CODE)
            .reduce(0L, (x, y) -> x*26+y);
    }

    public Map<Character, Integer> getCharacterCounts() {
        return characterCounts;
    }

    public int getNumWordsRead() {
        return numWordsRead;
    }

    public int getNumShortWordsRead() {
        return numShortWordsRead;
    }

    public int getNumLongWordsRead() {
        return numLongWordsRead;
    }

    private LongWordCounterHandler chooseLongWordsHandler(String handlerStr) {
        switch (handlerStr) {
            case "bsearch":
                return new BinarySearchLongWordHandler(this);
            case "tree":
                return new TreeLongWordHandler(this);
            case "array":
                return new UnsortedArrayLongWordHandler(this);
            case "customint":
                return new JdkWithCustomTypesHandler(this);
            case "jhash":
            default:
                return new JdkHashSetLongWordHandler(this);
        }
    }
}
