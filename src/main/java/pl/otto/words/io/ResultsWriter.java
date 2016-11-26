package pl.otto.words.io;

import org.apache.log4j.Logger;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

/**
 * Created by tomek on 26.11.16.
 */
public class ResultsWriter {

    private static final Logger LOGGER = Logger.getLogger(ResultsWriter.class);

    private String outFile;

    public ResultsWriter(String outFile) {
        this.outFile = outFile;
    }

    public void writeResults(Map<Character, Integer> resultMap) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outFile))) {

            for (Map.Entry<Character, Integer> entry : resultMap.entrySet()) {
                String res = entry.getKey() + "=" + entry.getValue();
                writer.write(res, 0, res.length());
                writer.newLine();
            };
        } catch (IOException e) {
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
