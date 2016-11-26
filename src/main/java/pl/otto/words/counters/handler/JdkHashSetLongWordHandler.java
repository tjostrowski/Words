package pl.otto.words.counters.handler;

import pl.otto.words.counters.DistinctCounter;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by tomek on 26.11.16.
 */
public class JdkHashSetLongWordHandler implements LongWordCounterHandler {

    private DistinctCounter counter;
    private Set<Long> longBackedSet; // Long OMG!!!
    private Set<Integer> intBackedSet; // Integer OMG!!!

    public JdkHashSetLongWordHandler(DistinctCounter counter) {
        this.counter = counter;
        longBackedSet = new HashSet<>();
        intBackedSet = new HashSet<>();
    }

    @Override
    public void handleWord(String word, long numberFromWord) {
        long wordNumber = counter.wordToNumber(word);
        if (wordNumber <= Integer.MAX_VALUE) {
            if (!intBackedSet.contains(wordNumber)) {
                counter.updateUniqueCharsMap(word);
                intBackedSet.add((int)wordNumber);
            }
        } else {
            if (!longBackedSet.contains(wordNumber)) {
                counter.updateUniqueCharsMap(word);
                longBackedSet.add(wordNumber);
            }
        }
    }
}
