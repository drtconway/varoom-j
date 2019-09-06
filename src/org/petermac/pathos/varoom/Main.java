package org.petermac.pathos.varoom;


import org.apache.commons.cli.*;
import org.petermac.pathos.varoom.hgvs.*;

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
        opts.addOption("G", false, "print the variants from VCF files as un-normalised HGVSg");
        opts.addOption("C", false, "print the variants from VCF files as un-normalised HGVSc");
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
            System.err.println(String.format("reading transcript data from '%s'", txFileName));
            RefGeneReader r = new RefGeneReader(fileFactory, txIdx);
            r.process(txFileName);
            System.err.println(txIdx.chroms());
        }

        String outputFileName = "-";
        if (cmd.hasOption("o")) {
            outputFileName = cmd.getOptionValue("o");
        }
        outputStream = fileFactory.out(outputFileName);

        if (cmd.hasOption("G")) {
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

        if (cmd.hasOption("C")) {
            PrintStream out = new PrintStream(outputStream);
            HgvsCProcessor hgvsCPrinter = new HgvsCFormatter(out::println);
            HgvsGProcessor hgvsCconverter = new HgvsG2HgvsCProcessor(txIdx, hgvsCPrinter);
            HgvsGVcfHandler hgvsGConverter = new HgvsGVcfHandler(hgvsCconverter);
            VcfFlow vf = new VcfFlow(hgvsGConverter);

            List<String> vcfNames = cmd.getArgList();
            for (int i = 0; i < vcfNames.size(); i++) {
                System.err.println("processing " + vcfNames.get(i));
                vf.process(vcfNames.get(i));
            }
            return;
        }


    }

}
