package pl.otto.words.counters.handler;

/**
 * Created by tomek on 26.11.16.
 */
public interface LongWordCounterHandler {

    void handleWord(String word, long numberFromWord);
}
