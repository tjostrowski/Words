package pl.otto.words.generator;

import org.apache.log4j.Logger;
import pl.otto.words.utils.Utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.IntStream;

import static pl.otto.words.utils.Consts.A_CODE;
import static pl.otto.words.utils.Consts.Z_CODE;

/**
 * Created by tomek on 26.11.16.
 */
public class FileGenerator {

    private static final Logger LOGGER = Logger.getLogger(FileGenerator.class);

    private String outFile;
    private Random rnd;

    private Mode mode;
    private LimitedArrayList<String> generatedWordsCache;

    private static final int MIN_WORD_SIZE = 2;
    private static final int MAX_WORD_SIZE = 10;

    private static final int MAX_NUM_WORDS = 40_000_000;
    private static final int NUM_WORDS_IN_LINE = 10;

    public enum Mode {
        RANDOM,
        SOMETIMES_REPEAT,
        ONLY_SHORT;

        public static Mode fromString(String mode) {
            switch (mode) {
                case "random":
                    return Mode.RANDOM;
                case "short":
                    return Mode.ONLY_SHORT;
                case "repeat":
                default:
                    return Mode.SOMETIMES_REPEAT;
            }
        }
    }

    public FileGenerator(String outFile) {
        this(outFile, Mode.RANDOM);
    }

    public FileGenerator(String outFile, Mode mode) {
        this.outFile = outFile;
        this.mode = mode;
        this.rnd = new Random(System.currentTimeMillis());
    }

    public void generate() {
        if (mode == Mode.SOMETIMES_REPEAT || mode == Mode.ONLY_SHORT) {
            generatedWordsCache = new LimitedArrayList<>(1000);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))) {

            int numWordsGenerated = 0;
            final int numLinesToGenerate = MAX_NUM_WORDS / NUM_WORDS_IN_LINE;
            for (int i = 0; i < numLinesToGenerate; ++i) {
                final String line = generateLine();
                writer.write(line, 0, line.length());
                writer.newLine();
            }

            if ((numWordsGenerated += NUM_WORDS_IN_LINE) % 1_000_000 == 0) {
                System.out.print('*');
            }

        } catch (IOException e) {
            LOGGER.error("Cannot write to file: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private String generateLine() {
        StringBuffer strBuf = new StringBuffer();

        IntStream.range(0, NUM_WORDS_IN_LINE).forEach(i -> {
            String generatedWord = (mode == Mode.RANDOM)
                ? generateRandomWord()
                : ((rnd.nextBoolean() && !generatedWordsCache.isEmpty())
                    ? generatedWordsCache.get(rnd.nextInt(generatedWordsCache.size()))
                    : generateRandomWord());

            strBuf.append(generatedWord);

            if (i < NUM_WORDS_IN_LINE - 1) {
                strBuf.append(' ');
            }
        });

        return strBuf.toString();
    }

    private String generateRandomWord() {
        final int wordSize = Utils.randomRangeClosed(rnd, MIN_WORD_SIZE,
                (mode == Mode.ONLY_SHORT) ? 6 : MAX_WORD_SIZE);
        StringBuffer strBuf = new StringBuffer();
        IntStream.rangeClosed(1, wordSize).forEach(i -> strBuf.append( generateRandomCharacter() ));
        return strBuf.toString();
    }

    private char generateRandomCharacter() {
        return (char)Utils.randomRangeClosed(rnd, A_CODE, Z_CODE);
    }

    public String getOutFile() {
        return outFile;
    }

    public static class LimitedArrayList<V> extends ArrayList<V> {
        private int maxLength;

        public LimitedArrayList(int maxLength) {
            super();
            this.maxLength = maxLength;
        }

        @Override
        public boolean add(V v) {
            if (this.size() < maxLength) {
                return super.add(v);
            }
            return false;
        }
    }
}
