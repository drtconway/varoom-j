package org.petermac.pathos.varoom;


import org.apache.commons.cli.*;
import org.petermac.pathos.varoom.hgvs.HgvsGFormatter;
import org.petermac.pathos.varoom.hgvs.HgvsGProcessor;
import org.petermac.pathos.varoom.hgvs.HgvsGVcfHandler;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

public class Main {
    public static FileFactory fileFactory;
    public static SequenceFactory sequenceFactory;
    public static TranscriptIndex txIdx;
    public static OutputStream outputStream;

    public static void main(String[] args) throws Exception {
        Options opts = new Options();
        opts.addOption("H", false, "print the variants from VCF files as un-normalised HGVSg");
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

        if (cmd.hasOption("x")) {
            String txFileName = cmd.getOptionValue("x");
            RefGeneReader r = new RefGeneReader(fileFactory, txIdx);
            r.process(txFileName);

        }

        String outputFileName = "-";
        if (cmd.hasOption("o")) {
            outputFileName = cmd.getOptionValue("o");
        }
        outputStream = fileFactory.out(outputFileName);

        if (cmd.hasOption("H")) {
            PrintStream out = new PrintStream(outputStream);
            HgvsGProcessor hgvsGPrinter = new HgvsGFormatter(out::println);
            HgvsGVcfHandler hgvsGConverter = new HgvsGVcfHandler(hgvsGPrinter);
            VcfFlow vf = new VcfFlow(hgvsGConverter);

            List<String> vcfNames = cmd.getArgList();
            for (int i = 0; i < vcfNames.size(); i++) {
                vf.process(vcfNames.get(i));
            }
            return;
        }


    }

}
