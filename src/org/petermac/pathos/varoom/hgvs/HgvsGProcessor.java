package org.petermac.pathos.varoom.hgvs;

public interface HgvsGProcessor {
    void sub(String accession, int pos, String ref, String alt) throws Exception;

    void ins(String accession, int before, int after, String alt) throws Exception;

    void del(String accession, int first, int last) throws Exception;

    void delins(String accession, int first, int last, String alt) throws Exception;

    void dup(String accession, int first, int last) throws Exception;

    void inv(String accession, int first, int last) throws Exception;

    void rep(String accession, int first, String ref, int num) throws Exception;

    void error(String message) throws Exception;
}
