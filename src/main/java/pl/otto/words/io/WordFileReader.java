package pl.otto.words.io;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by tomek on 26.11.16.
 */
public class WordFileReader {

    private static final Logger LOGGER = Logger.getLogger(WordFileReader.class);

    private String inFile;
    private BufferedReader backedReader;
    private LinkedList<String> wordsInLine;

    public WordFileReader(String inFile) {
        this.inFile = inFile;
        wordsInLine = new LinkedList<>();
    }

    public String readWord() {
        try {
            if (backedReader == null) {
                backedReader = new BufferedReader(new FileReader(inFile));
            }

            if (wordsInLine.isEmpty()) {
                String line = backedReader.readLine();

                if (line == null) {
                    backedReader.close();
                    return null;
                }

                Arrays.asList( line.trim().split("\\s+") ).forEach(word ->
                    wordsInLine.add(word)
                );
            }

            return wordsInLine.removeFirst();

        } catch (FileNotFoundException e) {
            LOGGER.error("Cannot find file: " + e.getMessage());
            throw new RuntimeException(e);
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public String getInFile() {
        return inFile;
    }
}
