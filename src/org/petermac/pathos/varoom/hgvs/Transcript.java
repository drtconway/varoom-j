package org.petermac.pathos.varoom.hgvs;

import org.jetbrains.annotations.NotNull;
import org.petermac.pathos.varoom.RefGeneProcessor;
import org.petermac.pathos.varoom.SeqUtils;

public class Transcript {
    public final String accession;
    public final RefGeneProcessor.Strand strand;
    public final String chrom;
    public final int txStart;
    public final int txEnd;
    public final int cdsStart;
    public final int cdsEnd;
    public final int exonCount;
    public final int[] exonStarts;
    public final int[] exonEnds;

    /**
     * The txStartOffsets array contains the offset in the transcript of the exonStart position relative to cdsStart.
     */
    public final int[] txStartOffsets;

    /**
     * The txEndOffsets array contains the offset in the transcript of the exonStart position relative to cdsEnd.
     */
    public final int[] txEndOffsets;

    public final int[] exonLengths;
    public final int[] exonJunctions;

    public enum RelCds { UP, CODING, DOWN }

    public Transcript(String accession, RefGeneProcessor.Strand strand, String chrom, int txStart, int txEnd, int cdsStart, int cdsEnd, int exonCount, int[] exonStarts, int[] exonEnds) {
        this.accession = accession;
        this.chrom = chrom;
        this.strand = strand;
        this.txStart = txStart;
        this.txEnd = txEnd;
        this.cdsStart = cdsStart;
        this.cdsEnd = cdsEnd;
        this.exonCount = exonCount;
        this.exonStarts = exonStarts;
        this.exonEnds = exonEnds;
        this.txStartOffsets = new int[exonCount];
        this.txEndOffsets = new int[exonCount];
        this.exonLengths = new int[exonCount];
        this.exonJunctions = new int[exonCount];

        int cdsStartExon = -1;
        int cdsEndExon = -1;
        for (int i = 0; i < exonCount; ++i) {
            exonLengths[i] = exonEnds[i] - exonStarts[i];
            if (exonStarts[i] <= cdsStart && cdsStart < exonEnds[i]) {
                cdsStartExon = i;
            }
            if (exonStarts[i] <= cdsEnd && cdsEnd <= exonEnds[i]) {
                cdsEndExon = i;
            }
        }
        assert cdsStartExon >= 0 && cdsEndExon >= 0;

        txStartOffsets[cdsStartExon] = exonStarts[cdsStartExon] - cdsStart;
        for (int i = cdsStartExon - 1; i >= 0; i--) {
            txStartOffsets[i] = txStartOffsets[i+1] - exonLengths[i];
        }
        for (int i = cdsStartExon + 1; i < exonCount; i++) {
            txStartOffsets[i] = exonLengths[i-1] + txStartOffsets[i-1];
        }

        txEndOffsets[cdsEndExon] = exonEnds[cdsEndExon] - cdsEnd;
        for (int i = cdsEndExon - 1; i >= 0; i--) {
            txEndOffsets[i] = txEndOffsets[i+1] - exonLengths[i+1];
        }
        for (int i = cdsEndExon + 1; i < exonCount; i++) {
            txEndOffsets[i] = exonLengths[i-1] + txStartOffsets[i-1];
        }

        switch (strand) {
            case POS:
                for (int i = 0; i < exonCount; i++) {
                    exonJunctions[i] = exonLengths[i];
                    if (i > 0) {
                        exonJunctions[i] += exonJunctions[i-1];
                    }
                }
                break;
            case NEG:
                for (int i = exonCount-1; i >= 0; i--) {
                    exonJunctions[i] = exonLengths[i];
                    if (i < exonCount-1) {
                        exonJunctions[i] += exonJunctions[i+1];
                    }
                }
                break;
        }
    }

    public void dump() {
        printArray(exonCount, txStartOffsets);
        printArray(exonCount, txEndOffsets);
    }

    private void printArray(int n, int[] arr) {
        System.out.print('[');
        for (int i = 0; i < n; i++) {
            if (i > 0) {
                System.out.print(", ");
            }
            System.out.print(arr[i]);
        }
        System.out.println(']');
    }

    /**
     * Convert a 1-based genomic coordinate to a HGVS.c compatible locus.
     * @param pos is a 1-based genomic coordinate in the chromosome for which the refgene file provides an alignment to this transcript.
     * @param res is the Locus structure to be filled in.
     */
    public void makeLocus(int pos, Locus res) {
        //
        //* Because the refgene file uses 0-based addressing,
        //* first we convert the position to 0-based.
        //
        pos -= 1;
        if (pos < txStart) {
            intragenic5prime(pos, res);
            return;
        }
        if (pos >= txEnd) {
            intragenic3prime(pos, res);
            return;
        }
        for (int i = 0; i < exonCount; i++) {
            if (pos < exonStarts[i]) {
                assert i > 0;
                intronic(pos, i, res);
                return;
            }
            if (exonStarts[i] <= pos && pos < exonEnds[i]) {
                exonic(pos, i, res);
                return;
            }
        }
    }

    public String applyStrand(String seq) {
        switch (strand) {
            case POS:
                return seq;
            case NEG:
                return SeqUtils.reverseComplement(seq);
        }
        return null;
    }

    private void intragenic5prime(int pos, Locus res) {
        switch (strand) {
            case POS:
                res.kind = Locus.Kind.UTR5;
                res.txPos = txStartOffsets[0] - (exonStarts[0] - pos);
                res.relPos = null;
                return;
            case NEG:
                res.kind = Locus.Kind.UTR3;
                res.txPos = -txEndOffsets[0] - (exonStarts[0] - pos);
                res.relPos = null;
                return;
        }
    }

    private void intragenic3prime(int pos, Locus res) {
        int e = exonCount - 1;
        switch (strand) {
            case POS:
                res.kind = Locus.Kind.UTR3;
                res.txPos = txStartOffsets[e] + exonLengths[e] + (pos - exonEnds[e]);
                res.relPos = null;
                return;
            case NEG:
                res.kind = Locus.Kind.UTR5;
                res.txPos = - txEndOffsets[e] - (pos - exonEnds[e]) - 1;
                res.relPos = null;
                return;
        }
    }

    private void intronic(int pos, int xon, Locus res) {
        int lhsXon = xon - 1;
        int rhsXon = xon;
        assert lhsXon >= 0;
        assert rhsXon == lhsXon + 1;
        assert rhsXon < exonCount;
        int d5 = pos - exonEnds[lhsXon] + 1;
        int d3 = exonStarts[rhsXon] - pos;
        switch (strand) {
            case POS:
                switch (relCds(pos)) {
                    case UP:
                        res.kind = Locus.Kind.UTR5;
                        if (d3 <= d5) {
                            res.txPos = txStartOffsets[rhsXon];
                            res.relPos = -d3;
                        } else {
                            res.txPos = txStartOffsets[rhsXon];
                            res.relPos = d5;
                        }
                        return;
                    case CODING:
                        res.kind = Locus.Kind.INTRON;
                        if (d3 <= d5) {
                            res.txPos = txStartOffsets[rhsXon] + 1;
                            res.relPos = -d3;
                        } else {
                            res.txPos = txStartOffsets[rhsXon];
                            res.relPos = d5;
                        }
                        return;
                    case DOWN:
                        res.kind = Locus.Kind.UTR3;
                        if (d3 <= d5) {
                            res.txPos = txEndOffsets[rhsXon];
                            res.relPos = -d3;
                        } else {
                            res.txPos = txEndOffsets[rhsXon] - 1;
                            res.relPos = d5;
                        }
                        return;
                }
            case NEG:
                switch (relCds(pos)) {
                    case UP:
                        res.kind = Locus.Kind.UTR3;
                        if (d3 <= d5) {
                            res.txPos = -txStartOffsets[rhsXon];
                            res.relPos = d3;
                        } else {
                            res.txPos = -txStartOffsets[rhsXon] + 1;
                            res.relPos = -d5;
                        }
                        return;
                    case CODING:
                        res.kind = Locus.Kind.INTRON;
                        if (d3 <= d5) {
                            res.txPos = -txEndOffsets[lhsXon];
                            res.relPos = d3;
                        } else {
                            res.txPos = -txEndOffsets[lhsXon] + 1;
                            res.relPos = -d5;
                        }
                        return;
                    case DOWN:
                        res.kind = Locus.Kind.UTR5;
                        if (d3 <= d5) {
                            res.txPos = -txEndOffsets[lhsXon] - 1;
                            res.relPos = d3;
                        } else {
                            res.txPos = -txEndOffsets[lhsXon];
                            res.relPos = -d5;
                        }
                }
        }
    }

    private void exonic(int pos, int xon, Locus res) {
        switch (strand) {
            case POS:
                switch (relCds(pos)) {
                    case UP:
                        res.kind = Locus.Kind.UTR5;
                        res.txPos = txStartOffsets[xon] + (pos - exonStarts[xon]);
                        res.relPos = null;
                        return;
                    case CODING:
                        res.kind = Locus.Kind.EXON;
                        res.txPos = txStartOffsets[xon] + (pos - exonStarts[xon]) + 1;
                        res.relPos = null;
                        return;
                    case DOWN:
                        res.kind = Locus.Kind.UTR3;
                        res.txPos =  txEndOffsets[xon] - (exonEnds[xon] - pos) + 1;
                        res.relPos =null;
                        return;
                }
            case NEG:
                switch (relCds(pos)) {
                    case UP:
                        res.kind = Locus.Kind.UTR3;
                        res.txPos = (exonStarts[xon] - pos) - txStartOffsets[xon];
                        res.relPos = null;
                        return;
                    case CODING:
                        res.kind = Locus.Kind.EXON;
                        res.txPos = -(txEndOffsets[xon] + (pos - exonEnds[xon]));
                        res.relPos =null;
                        return;
                    case DOWN:
                        res.kind = Locus.Kind.UTR5;
                        res.txPos = (exonEnds[xon] - pos) - txEndOffsets[xon] - 1;
                        res.relPos =null;
                        return;
                }
        }
    }

    private @NotNull RelCds relCds(int pos) {
        if (pos < cdsStart) {
            return RelCds.UP;
        }
        if (pos >= cdsEnd) {
            return RelCds.DOWN;
        }
        // assert (cdsStart <= pos && pos < cdsEnd);
        return RelCds.CODING;
    }

}
