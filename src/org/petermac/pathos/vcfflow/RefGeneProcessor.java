package org.petermac.pathos.vcfflow;

public interface RefGeneProcessor {
    enum Strand {POS, NEG}
    enum CdsStat {NONE, UNK, INCMPL, CMPL}

    void begin();

    void accept(String accession, String chrom, Strand strand, int txStart, int txEnd, int cdsStart, int cdsEnd,
                int exonCount, int[] exonStarts, int[] exonEnds, int[] exonFrames, String name, CdsStat cdsStartStat, CdsStat cdsEndStat);

    void end();
}
