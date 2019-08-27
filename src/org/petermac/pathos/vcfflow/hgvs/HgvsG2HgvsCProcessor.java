package org.petermac.pathos.vcfflow.hgvs;

public class HgvsG2HgvsCProcessor implements HgvsGProcessor {
    private final HgvsCProcessor resultProcessor;

    public HgvsG2HgvsCProcessor(HgvsCProcessor resultProcessor) {
        this.resultProcessor = resultProcessor;
    }

    @Override
    public void sub(String accession, int pos, String ref, String alt) {
        Transcript tx = lookupTx(accession, pos);
    }

    @Override
    public void ins(String accession, int before, int after, String alt) {

    }

    @Override
    public void del(String accession, int first, int last) {

    }

    @Override
    public void delins(String accession, int first, int last, String alt) {

    }

    @Override
    public void dup(String accession, int first, int last) {

    }

    @Override
    public void inv(String accession, int first, int last) {

    }

    @Override
    public void rep(String accession, int first, String ref, int num) {

    }

    @Override
    public void error(String message) throws Exception {
        throw new Exception(message);
    }

    private Transcript lookupTx(String accession, Integer pos) {
        return null;
    }

}
