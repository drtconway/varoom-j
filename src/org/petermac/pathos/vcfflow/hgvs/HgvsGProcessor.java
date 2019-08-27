package org.petermac.pathos.vcfflow.hgvs;

public interface HgvsGProcessor {
    void sub(String accession, int pos, String ref, String alt);

    void ins(String accession, int before, int after, String alt);

    void del(String accession, int first, int last);

    void delins(String accession, int first, int last, String alt);

    void dup(String accession, int first, int last);

    void inv(String accession, int first, int last);

    void rep(String accession, int first, String ref, int num);

    void error(String message) throws Exception;
}
