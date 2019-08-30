package org.petermac.pathos.varoom;


import org.apache.commons.cli.*;

public class Main {

    public static void main(String[] args) throws Exception {
        Options opts = new Options();
        opts.addOption("X", true, "indeX fasta file for efficient processing");
        opts.addOption("g", true, "home directory for genome data");
        opts.addOption("o", true, "output file");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(opts, args);

        String genomeHome = ".";
        if (cmd.hasOption("g")) {
            genomeHome = cmd.getOptionValue("g");
        }
        FileFactory fileFactory = new FileFactory();
        SequenceFactory sequenceFactory = new SequenceFactory(fileFactory, genomeHome);

    }
}
