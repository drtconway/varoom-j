package org.petermac.pathos.varoom.hgvs;

import org.petermac.pathos.varoom.ThrowingConsumer;
import org.petermac.pathos.varoom.TranscriptIndex;

import java.util.HashSet;
import java.util.Set;

public class HgvsG2HgvsCProcessor implements HgvsGProcessor {
    private final TranscriptIndex txIdx;
    private final HgvsCProcessor resultProcessor;

    public HgvsG2HgvsCProcessor(TranscriptIndex txIdx, HgvsCProcessor resultProcessor) {
        this.txIdx = txIdx;
        this.resultProcessor = resultProcessor;
    }

    @Override
    public void sub(String accession, int pos, String ref, String alt) throws Exception {
        final int gPos = pos;
        txIdx.with(accession, pos, (Transcript tx) -> {
            Locus cPos = tx.makeLocus(gPos);
            String cRef = tx.applyStrand(ref);
            String cAlt = tx.applyStrand(alt);
            resultProcessor.sub(tx.accession, cPos, cRef, cAlt);
        });
    }

    @Override
    public void ins(String accession, int before, int after, String alt) throws Exception {
        final int gBefore = before;
        final int gAfter = after;
        Set<String> seen = new HashSet<>();
        ThrowingConsumer<Transcript> hx = (Transcript tx) -> {
            if (seen.contains(tx.accession)) {
                return;
            }
            seen.add(tx.accession);
            Locus cBefore = tx.makeLocus(gBefore);
            Locus cAfter = tx.makeLocus(gAfter);
            String cAlt = tx.applyStrand(alt);
            resultProcessor.ins(tx.accession, cBefore, cAfter, cAlt);
        };
        txIdx.with(accession, gBefore, hx);
        txIdx.with(accession, gAfter, hx);
    }

    @Override
    public void del(String accession, int first, int last) throws Exception {
        final int gFirst = first;
        final int gLast = last;
        Set<String> seen = new HashSet<>();
        ThrowingConsumer<Transcript> hx = (Transcript tx) -> {
            if (seen.contains(tx.accession)) {
                return;
            }
            seen.add(tx.accession);
            Locus cFirst = tx.makeLocus(gFirst);
            Locus cLast = tx.makeLocus(gLast);
            resultProcessor.del(tx.accession, cFirst, cLast);
        };
        txIdx.with(accession, gFirst, hx);
        txIdx.with(accession, gLast, hx);
    }

    @Override
    public void delins(String accession, int first, int last, String alt) throws Exception {
        final int gFirst = first;
        final int gLast = last;
        Set<String> seen = new HashSet<>();
        ThrowingConsumer<Transcript> hx = (Transcript tx) -> {
            if (seen.contains(tx.accession)) {
                return;
            }
            seen.add(tx.accession);
            Locus cFirst = tx.makeLocus(gFirst);
            Locus cLast = tx.makeLocus(gLast);
            String cAlt = tx.applyStrand(alt);
            resultProcessor.delins(tx.accession, cFirst, cLast, cAlt);
        };
        txIdx.with(accession, gFirst, hx);
        txIdx.with(accession, gLast, hx);
    }

    @Override
    public void dup(String accession, int first, int last) throws Exception {
        final int gFirst = first;
        final int gLast = last;
        Set<String> seen = new HashSet<>();
        ThrowingConsumer<Transcript> hx = (Transcript tx) -> {
            if (seen.contains(tx.accession)) {
                return;
            }
            seen.add(tx.accession);
            Locus cFirst = tx.makeLocus(gFirst);
            Locus cLast = tx.makeLocus(gLast);
            resultProcessor.dup(tx.accession, cFirst, cLast);
        };
        txIdx.with(accession, gFirst, hx);
        txIdx.with(accession, gLast, hx);    }

    @Override
    public void inv(String accession, int first, int last) throws Exception {
        final int gFirst = first;
        final int gLast = last;
        Set<String> seen = new HashSet<>();
        ThrowingConsumer<Transcript> hx = (Transcript tx) -> {
            if (seen.contains(tx.accession)) {
                return;
            }
            seen.add(tx.accession);
            Locus cFirst = tx.makeLocus(gFirst);
            Locus cLast = tx.makeLocus(gLast);
            resultProcessor.inv(tx.accession, cFirst, cLast);
        };
        txIdx.with(accession, gFirst, hx);
        txIdx.with(accession, gLast, hx);
    }

    @Override
    public void rep(String accession, int first, String ref, int num) throws Exception {
        throw new Exception("rep variants not handled.");
    }

    @Override
    public void error(String message) throws Exception {
        throw new Exception(message);
    }
}
