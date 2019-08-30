package org.petermac.pathos.varoom.hgvs;

import java.util.function.Consumer;

public class HgvsGFormatter implements HgvsGProcessor {
    private final Consumer<String> consumer;

    public HgvsGFormatter(Consumer<String> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void sub(String accession, int pos, String ref, String alt) throws Exception {
        consumer.accept(accession + "g." + pos + ref + ">" + alt);
    }

    @Override
    public void ins(String accession, int before, int after, String alt) throws Exception {
        consumer.accept(accession + ":g." + before + "_" + after + "ins" + alt);
    }

    @Override
    public void del(String accession, int first, int last) throws Exception {
        if (first == last) {
            consumer.accept(accession + ":g." + first + "del");
        } else {
            consumer.accept(accession + ":g." + first + "_" + last + "del");
        }
    }

    @Override
    public void delins(String accession, int first, int last, String alt) throws Exception {
        if (first == last) {
            consumer.accept(accession + ":g." + first + "delins" + alt);
        } else {
            consumer.accept(accession + ":g." + first + "_" + last + "delins" + alt);
        }
    }

    @Override
    public void dup(String accession, int first, int last) throws Exception {
        if (first == last) {
            consumer.accept(accession + ":g." + first + "dup");
        } else {
            consumer.accept(accession + ":g." + first + "_" + last + "dup");
        }
    }

    @Override
    public void inv(String accession, int first, int last) throws Exception {
        consumer.accept(accession + ":g." + first + "_" + last + "inv");
    }

    @Override
    public void rep(String accession, int first, String ref, int num) throws Exception {
        consumer.accept(accession + ":g." + first + ref + "[" + num + "]");
    }

    @Override
    public void error(String message) throws Exception {
        throw new Exception(message);
    }

}
