package org.petermac.pathos.varoom.hgvs;

import org.petermac.pathos.varoom.Fasta;
import org.petermac.pathos.varoom.SeqUtils;
import org.petermac.pathos.varoom.SequenceFactory;

import static java.lang.Integer.min;

public class HgvsGNormaliser implements HgvsGProcessor {
    public boolean checkReferenceMatches = false;
    public boolean enable3primeShifts = false;

    private final SequenceFactory sfac;
    private final HgvsGProcessor proc;
    private Fasta cached;

    public HgvsGNormaliser(SequenceFactory sfac, HgvsGProcessor proc) {
        this.sfac = sfac;
        this.proc = proc;
        this.cached = null;
    }

    @Override
    public void sub(String accession, int pos, String ref, String alt) throws Exception {
        if (checkReferenceMatches) {
            Fasta refSeq = getAccession(accession);
            String realRef = refSeq.sequence.substring(pos-1, pos);
            if (!ref.equals(realRef)) {
                proc.error(String.format("for accession '%s', the reference has '%s', but the variant has '%s'", accession, realRef, ref));
                return;
            }
        }
        proc.sub(accession, pos, ref, alt);
    }

    @Override
    public void ins(String accession, int before, int after, String alt) throws Exception {
        final Fasta refSeq = getAccession(accession);
        final int n = alt.length();

        // Shift the insertion as far as we can in the 3' direction (maybe!)
        if (enable3primeShifts) {
            while (true) {
                System.out.println(String.format("%d_%d", before, after));
                final int alleleEnd = min(after + 2*n, refSeq.sequence.length());
                final String refAllele = refSeq.sequence.substring(after-1, alleleEnd);
                final String altAllele = alt + refAllele;
                int found = -1;
                for (int i = 1; i <= refAllele.length(); i++) {
                    String newAllele = refAllele.substring(0, i) + alt + refAllele.substring(i);
                    if (newAllele.equals(altAllele)) {
                        found = i;
                    }
                }
                if (found == -1) {
                    break;
                }
                before += found;
                after = before + 1;
            }
        }

        // Promote to a dup if we can.
        //
        if (refSeq.sequence.startsWith(alt, before - n)) {
            proc.dup(accession, before - n + 1, before);
            return;
        }
        proc.ins(accession, before, after, alt);
    }

    @Override
    public void del(String accession, int first, int last) throws Exception {
        // Shift the insertion as far as we can in the 3' direction (maybe!)
        if (enable3primeShifts) {
            final Fasta refSeq = getAccession(accession);
            final int n = last - first + 1;

            while (true) {
                final int alleleEnd = min(last + 2*n, refSeq.sequence.length());
                final String refAllele = refSeq.sequence.substring(first - 1, alleleEnd);
                final String altAllele = refAllele.substring(n);
                int found = -1;
                for (int i = 1; i <= refAllele.length(); i++) {
                    String newAllele = refAllele.substring(0, i) + refAllele.substring(i + n);
                    if (newAllele.equals(altAllele)) {
                        found = i;
                    }
                }
                if (found == -1) {
                    break;
                }
                first += found;
                last += + 1;
            }
        }

        proc.del(accession, first, last);
    }

    @Override
    public void delins(String accession, int first, int last, String alt) throws Exception {
        final int n = last - first + 1;

        if (enable3primeShifts) {
            final Fasta refSeq = getAccession(accession);

            while (true) {
                final int alleleEnd = min(last + 2*n, refSeq.sequence.length());
                final String refAllele = refSeq.sequence.substring(first - 1, alleleEnd);
                final String altAllele = "" + alt + refAllele.substring(n);
                int found = -1;
                for (int i = 1; i <= refAllele.length(); i++) {
                    String newAllele = refAllele.substring(0, i) + alt + refAllele.substring(i + n);
                    if (newAllele.equals(altAllele)) {
                        found = i;
                    }
                }
                if (found == -1) {
                    break;
                }
                first += found;
                last += + 1;
            }
        }

        // Promote to an inv if we can.
        if (alt.length() == n) {
            final Fasta refSeq = getAccession(accession);
            String ref = refSeq.sequence.substring(first - 1, last);
            String refRc = SeqUtils.reverseComplement(ref);
            if (alt.equals(refRc)) {
                proc.inv(accession, first, last);
                return;
            }
        }

        proc.delins(accession, first, last, alt);
    }

    @Override
    public void dup(String accession, int first, int last) throws Exception {
        final Fasta refSeq = getAccession(accession);
        String alt = refSeq.sequence.substring(first-1, last);
        proc.ins(accession, last, last+1, alt);
    }

    @Override
    public void inv(String accession, int first, int last) throws Exception {
        final Fasta refSeq = getAccession(accession);
        String alt = refSeq.sequence.substring(first-1, last);
        proc.delins(accession, first, last, alt);
    }

    @Override
    public void rep(String accession, int first, String ref, int num) throws Exception {

    }

    @Override
    public void error(String message) throws Exception {

    }

    private Fasta getAccession(String accession) {
        if (cached == null || !cached.label.equals(accession)) {
            cached = sfac.getAccession(accession);
        }
        return cached;
    }

}
