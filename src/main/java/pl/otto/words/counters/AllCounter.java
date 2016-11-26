package pl.otto.words.counters;

import org.apache.log4j.Logger;
import pl.otto.words.io.WordFileReader;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by tomek on 26.11.16.
 */
public class AllCounter {

    private static final Logger LOGGER = Logger.getLogger(AllCounter.class);

    private String inFile;

    private Map<Character, Integer> characterCounts;

    private int numWordsRead;

    public AllCounter(String inFile) {
        this.inFile = inFile;
    }

    public AllCounter init() {
        this.characterCounts = new TreeMap<>();
        return this;
    }

    public void handleInput() {
        WordFileReader reader = new WordFileReader(inFile);

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
    }

    public void handleWord(String word) {
        word.chars()
            .mapToObj(ci -> (char)ci)
            .forEach(c -> {
                Integer currentCount = characterCounts.get(c);
                if (currentCount == null) {
                    characterCounts.put(c, 1);
                } else{
                    characterCounts.put(c, currentCount+1);
                }
            });
    }

    public int getNumWordsRead() {
        return numWordsRead;
    }

    public Map<Character, Integer> getCharacterCounts() {
        return characterCounts;
    }
}
