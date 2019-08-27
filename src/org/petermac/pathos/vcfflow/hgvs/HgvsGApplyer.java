package org.petermac.pathos.vcfflow.hgvs;

import org.petermac.pathos.vcfflow.Rope;
import org.petermac.pathos.vcfflow.SeqUtils;

public abstract class HgvsGApplyer implements HgvsGProcessor {

    @Override
    public void sub(String accession, int pos, String ref, String alt) {
        assert(ref.length() == 1);
        assert(alt.length() == 1);

        Rope orig = getAccession(accession);
        Integer n = orig.size();

        Integer idx = pos - 1;
        assert(idx >= 0 && idx < n);
        assert(orig.getAt(idx) == ref.charAt(0));

        Rope[] parts = {Rope.substr(orig, 0, idx), Rope.atom(alt), Rope.substr(orig, idx+1, n)};
        Rope mutant = Rope.join(parts);
        putResult(accession, mutant);
    }

    public void ins(String accession, int before, int after, String alt) {
        assert(before + 1 == after);
        assert(alt.length() > 0);

        Rope orig = getAccession(accession);
        Integer n = orig.size();

        Integer idx = before;
        assert(idx >= 0 && idx < n);

        Rope[] parts = {Rope.substr(orig, 0, idx), Rope.atom(alt), Rope.substr(orig, idx, n)};
        Rope mutant = Rope.join(parts);
        putResult(accession, mutant);
    }

    public void del(String accession, int first, int last) {
        assert(first <= last);

        Rope orig = getAccession(accession);
        Integer n = orig.size();

        Integer end1 = first - 1;
        Integer beg2 = last;
        assert(beg2 >= 0 && beg2 <= n);

        Rope[] parts = {Rope.substr(orig, 0, end1), Rope.substr(orig, beg2, n)};
        Rope mutant = Rope.join(parts);
        putResult(accession, mutant);
    }

    public void delins(String accession, int first, int last, String alt) {
        assert(first <= last);

        Rope orig = getAccession(accession);
        Integer n = orig.size();

        Integer end1 = first - 1;
        Integer beg2 = last;
        assert(beg2 >= 0 && beg2 <= n);

        Rope[] parts = {Rope.substr(orig, 0, end1), Rope.atom(alt), Rope.substr(orig, beg2, n)};
        Rope mutant = Rope.join(parts);
        putResult(accession, mutant);
    }

    public void dup(String accession, int first, int last) {
        assert(first <= last);

        Rope orig = getAccession(accession);
        Integer n = orig.size();

        Integer beg2 = first - 1;
        Integer end2 = last;
        assert(beg2 >= 0 && beg2 <= n);

        Rope[] parts = {Rope.substr(orig, 0, end2), Rope.substr(orig, beg2, end2), Rope.substr(orig, end2, n)};
        Rope mutant = Rope.join(parts);
        putResult(accession, mutant);
    }

    public void inv(String accession, int first, int last) {
        assert(first <= last);

        Rope orig = getAccession(accession);
        Integer n = orig.size();

        Integer beg2 = first - 1;
        Integer end2 = last;
        assert(beg2 >= 0 && beg2 <= n);

        String seq = orig.getAt(beg2, end2);
        String inv = SeqUtils.reverseComplement(seq);

        Rope[] parts = {Rope.substr(orig, 0, beg2), Rope.atom(inv), Rope.substr(orig, end2, n)};
        Rope mutant = Rope.join(parts);
        putResult(accession, mutant);
    }

    public void rep(String accession, int first, String ref, int num) {
        // Rep variants are poorly defined!
        assert(false);
    }

    protected abstract Rope getAccession(String accession);

    protected abstract void putResult(String accession, Rope mutant);

}
