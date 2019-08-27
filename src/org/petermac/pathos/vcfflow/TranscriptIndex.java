package org.petermac.pathos.vcfflow;

import org.petermac.pathos.vcfflow.hgvs.Transcript;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

public class TranscriptIndex implements RefGeneProcessor {

    static class TxList extends ArrayList<Transcript> {
    }

    private static class Index extends  HashMap<String,TxList> {

    }

    private final Index index;
    private final int W;

    public TranscriptIndex() {
        this.index = new Index();
        W = 250;
    }

    @Override
    public void begin() {
    }

    @Override
    public void accept(String accession, String chrom, Strand strand, int txStart, int txEnd, int cdsStart, int cdsEnd,
                       int exonCount, int[] exonStarts, int[] exonEnds, int[] exonFrames, String name, CdsStat cdsStartStat, CdsStat cdsEndStat) {

        Transcript tx = new Transcript(accession, strand, chrom, txStart, txEnd, cdsStart, cdsEnd, exonCount, exonStarts, exonEnds);
        if (!index.containsKey(chrom)) {
            index.put(chrom, new TxList());
        }
        index.get(chrom).add(tx);
    }

    @Override
    public void end() {
    }

    public void with(String chrom, int pos, Consumer<Transcript> handler) {
        if (!index.containsKey(chrom)) {
            return;
        }
        TxList txList = index.get(chrom);
        for (Transcript tx : txList) {
            if (tx.txStart - W <= pos && pos <= tx.txEnd + W) {
                handler.accept(tx);
            }
        }
    }
}
