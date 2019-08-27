package org.petermac.pathos.vcfflow;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class RefGeneReader {
    private final FileFactory fileFactory;
    private final RefGeneProcessor processor;

    public RefGeneReader(FileFactory fileFactory, RefGeneProcessor processor) {
        this.fileFactory = fileFactory;
        this.processor = processor;
    }

    public void process(String fileName) {
        try (Scanner scanner = new Scanner(fileFactory.in(fileName))) {
            processor.begin();
            while (scanner.hasNext()){
                final String line = scanner.nextLine();
                boolean cont = processLine(line);
                if (!cont) {
                    break;
                }
            }
            processor.end();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean processLine(String line) {
        if (line.startsWith("#")) {
            return true;
        }
        ArrayList<String> flds = splitLine(line);
        int txStart = Integer.parseInt(flds.get(4));
        int txEnd = Integer.parseInt(flds.get(5));
        int cdsStart = Integer.parseInt((flds.get(6)));
        int cdsEnd = Integer.parseInt(flds.get(7));
        int exonCount = Integer.parseInt(flds.get(8));
        int[] exonStarts = strToIntList(exonCount, flds.get(9));
        int[] exonEnds = strToIntList(exonCount, flds.get(10));
        int[] exonFrames = strToIntList(exonCount, flds.get(15));
        processor.accept(flds.get(1), flds.get(2), strToStrand(flds.get(3)), txStart, txEnd, cdsStart, cdsEnd,
                exonCount, exonStarts, exonEnds, exonFrames, flds.get(12), strToCdsStat(flds.get(13)), strToCdsStat(flds.get(14)));

        return true;
    }

    static private RefGeneProcessor.Strand strToStrand(String s) {
        switch (s) {
            case "+":
                return RefGeneProcessor.Strand.POS;
            case "-":
                return RefGeneProcessor.Strand.NEG;
            default:
                return null;
        }
    }

    static private RefGeneProcessor.CdsStat strToCdsStat(String s) {
        switch (s) {
            case "none":
                return RefGeneProcessor.CdsStat.NONE;
            case "unk":
                return RefGeneProcessor.CdsStat.UNK;
            case "incmpl":
                return RefGeneProcessor.CdsStat.INCMPL;
            case "cmpl":
                return RefGeneProcessor.CdsStat.CMPL;
            default:
                return null;
        }
    }

    private int[] strToIntList(Integer exonCount, String s) {
        int[] res = new int[exonCount];
        int p = 0;
        int q = s.indexOf(',', p);
        for (int i = 0; i < exonCount; i++) {
            Integer x = Integer.parseInt(s.substring(p, q));
            res[i] = x;
            p = q + 1;
            q = s.indexOf(',', p);
        }
        return res;
    }

    private ArrayList<String> splitLine(String line) {
        ArrayList<String> res = new ArrayList<>();
        int p = 0;
        int q = line.indexOf("\t");
        return getStrings(line, res, p, q);
    }

    @NotNull
    private static ArrayList<String> getStrings(String line, ArrayList<String> res, int p, int q) {
        while (q >= 0) {
            String fld = line.substring(p, q);
            res.add(fld);
            p = q + 1;
            q = line.indexOf("\t", p);
        }
        String fld = line.substring(p);
        res.add(fld);
        return res;
    }
}
