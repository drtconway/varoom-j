package org.petermac.pathos.vcfflow.hgvs;

public interface HgvsCProcessor {

    void sub(String accession, Locus pos, String ref, String alt);

    void ins(String accession, Locus before, Locus after, String alt);

    void del(String accession, Locus first, Locus last);

    void delins(String accession, Locus first, Locus last, String alt);

    void dup(String accession, Locus first, Locus last);

    void inv(String accession, Locus first, Locus last);

    void rep(String accession, Locus first, String ref, Integer num);
}
