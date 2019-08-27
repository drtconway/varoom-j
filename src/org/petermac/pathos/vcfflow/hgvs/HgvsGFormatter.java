package org.petermac.pathos.vcfflow.hgvs;

import java.util.function.Consumer;

public class HgvsGFormatter implements HgvsGProcessor {
    private final Consumer<String> consumer;

    public HgvsGFormatter(Consumer<String> consumer) {
        this.consumer = consumer;
    }

    public void sub(String accession, int pos, String ref, String alt) {
        consumer.accept(accession + "g." + pos + ref + ">" + alt);
    }

    public void ins(String accession, int before, int after, String alt) {
        consumer.accept(accession + ":g." + before + "_" + after + "ins" + alt);
    }

    public void del(String accession, int first, int last) {
        if (first == last) {
            consumer.accept(accession + ":g." + first + "del");
        } else {
            consumer.accept(accession + ":g." + first + "_" + last + "del");
        }
    }

    public void delins(String accession, int first, int last, String alt) {
        if (first == last) {
            consumer.accept(accession + ":g." + first + "delins" + alt);
        } else {
            consumer.accept(accession + ":g." + first + "_" + last + "delins" + alt);
        }
    }

    public void dup(String accession, int first, int last) {
        if (first == last) {
            consumer.accept(accession + ":g." + first + "dup");
        } else {
            consumer.accept(accession + ":g." + first + "_" + last + "dup");
        }
    }

    public void inv(String accession, int first, int last) {
        consumer.accept(accession + ":g." + first + "_" + last + "inv");
    }

    public void rep(String accession, int first, String ref, int num) {
        consumer.accept(accession + ":g." + first + ref + "[" + num + "]");
    }

}
