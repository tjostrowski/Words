package pl.otto.words;

import org.apache.log4j.Logger;
import pl.otto.words.counters.AllCounter;
import pl.otto.words.counters.DistinctCounter;
import pl.otto.words.generator.FileGenerator;
import pl.otto.words.io.ResultsWriter;

/**
 * Created by tomek on 25.11.16.
 */
public class Main extends Thread {

    private static final Logger LOGGER = Logger.getLogger(Main.class);

    public static void main(String[] args) {

        if (args.length < 2) {
            LOGGER.error("Please specify what to do: genFile|countAll|countDistinct <file>");
            return;
        }

        final String cmd = args[0];
        final String file = args[1];

        switch (cmd) {
            case "genFile":
                LOGGER.debug("****** Start generating file !!!");

                FileGenerator.Mode mode = (args.length >= 3)
                        ? FileGenerator.Mode.fromString(args[2]) : FileGenerator.Mode.SOMETIMES_REPEAT;

                new FileGenerator(file, mode).generate();
                LOGGER.debug("*** File generated !!!");
                break;
            case "countAll":
                LOGGER.debug("****** Counting all occurrences of words !!!");

                AllCounter allCounter = new AllCounter(file).init();
                allCounter.handleInput();

                String outFile = (args.length >= 3) ? args[2] : "out.txt";
                new ResultsWriter(outFile).writeResults(allCounter.getCharacterCounts());

                LOGGER.debug("*** Finished counting !!!");
                break;
            case "countDistinct":
                LOGGER.debug("****** Counting occurrences in distinct words !!!");

                DistinctCounter distinctCounter = new DistinctCounter(file)
                        .init((args.length >= 4) ? args[3] : "jhash");
                distinctCounter.handleInput();

                outFile = (args.length >= 3) ? args[2] : "out.txt";
                new ResultsWriter(outFile).writeResults(distinctCounter.getCharacterCounts());

                LOGGER.debug("*** Finished counting !!!");
                break;
            default:
                LOGGER.error("Unrecognized command: " + cmd + ", please specify valid command");
        }
    }
}
