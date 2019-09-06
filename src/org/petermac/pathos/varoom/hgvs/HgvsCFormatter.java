package org.petermac.pathos.varoom.hgvs;

import java.util.function.Consumer;

public class HgvsCFormatter implements HgvsCProcessor {
    private final Consumer<String> consumer;

    public HgvsCFormatter(Consumer<String> consumer) {
        this.consumer = consumer;
    }

    @Override
    public void sub(String accession, Locus pos, String ref, String alt) throws Exception {
        //System.err.println("ping!");
        consumer.accept(accession + ":c." + pos.toString() + ref + ">" + alt);
    }

    @Override
    public void ins(String accession, Locus before, Locus after, String alt) throws Exception {
        consumer.accept(accession + ":c." + before.toString() + "_" + after.toString() + "ins" + alt);
    }

    @Override
    public void del(String accession, Locus first, Locus last) throws Exception {
        if (first.equals(last)) {
            consumer.accept(accession + ":c." + first.toString() + "del");
        } else {
            consumer.accept(accession + ":c." + first.toString() + "_" + last.toString() + "del");
        }
    }

    @Override
    public void delins(String accession, Locus first, Locus last, String alt) throws Exception {
        if (first.equals(last)) {
            consumer.accept(accession + ":c." + first.toString() + "delins" + alt);
        } else {
            consumer.accept(accession + ":c." + first.toString() + "_" + last.toString() + "delins" + alt);
        }
    }

    @Override
    public void dup(String accession, Locus first, Locus last) throws Exception {
        if (first.equals(last)) {
            consumer.accept(accession + ":c." + first + "dup");
        } else {
            consumer.accept(accession + ":c." + first + "_" + last + "dup");
        }
    }

    @Override
    public void inv(String accession, Locus first, Locus last) throws Exception {
        consumer.accept(accession + ":c." + first.toString() + "_" + last.toString() + "inv");
    }

    @Override
    public void rep(String accession, Locus first, String ref, Integer num) throws Exception {
        consumer.accept(accession + ":c." + first.toString() + ref + "[" + num + "]");
    }

    @Override
    public void error(String message) throws Exception {
        throw new Exception(message);
    }
}
