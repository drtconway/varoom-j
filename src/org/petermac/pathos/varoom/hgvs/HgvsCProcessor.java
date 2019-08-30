package org.petermac.pathos.varoom.hgvs;

public interface HgvsCProcessor {

    void sub(String accession, Locus pos, String ref, String alt) throws Exception;

    void ins(String accession, Locus before, Locus after, String alt) throws Exception;

    void del(String accession, Locus first, Locus last) throws Exception;

    void delins(String accession, Locus first, Locus last, String alt) throws Exception;

    void dup(String accession, Locus first, Locus last) throws Exception;

    void inv(String accession, Locus first, Locus last) throws Exception;

    void rep(String accession, Locus first, String ref, Integer num) throws Exception;

    void error(String message) throws Exception;
}
