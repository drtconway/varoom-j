package org.petermac.pathos.varoom;


import org.apache.commons.cli.*;

public class Main {
    public static FileFactory fileFactory;
    public static SequenceFactory sequenceFactory;
    public static TranscriptIndex txIdx;

    public static void main(String[] args) throws Exception {
        Options opts = new Options();
        opts.addOption("x", true, "transcript data file");
        opts.addOption("g", true, "home directory for genome data");
        opts.addOption("o", true, "output file");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(opts, args);

        String genomeHome = ".";
        if (cmd.hasOption("g")) {
            genomeHome = cmd.getOptionValue("g");
        }
        fileFactory = new FileFactory();
        sequenceFactory = new SequenceFactory(fileFactory, genomeHome);
        txIdx = new TranscriptIndex();

        String txFileName = genomeHome + "/refGene.txt.gz";
        if (cmd.hasOption("x")) {
            txFileName = cmd.getOptionValue("x");
        }
        RefGeneReader r = new RefGeneReader(fileFactory, txIdx);
        r.process(txFileName);
    }

}
