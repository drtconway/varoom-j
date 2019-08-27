package org.petermac.pathos.vcfflow.hgvs;

import java.util.HashMap;
import java.util.Map;

public class HgvsC2Map implements HgvsCProcessor {
    public Map<String,Object> map;

    @Override
    public void sub(String accession, Locus pos, String ref, String alt) {
        map = new HashMap<>();
        map.put("kind", "SUB");
        map.put("acc", accession);
        map.put("pos", pos);
        map.put("ref", ref);
        map.put("alt", alt);

    }

    @Override
    public void ins(String accession, Locus before, Locus after, String alt) {
        map = new HashMap<>();
        map.put("kind", "INS");
        map.put("acc", accession);
        map.put("before", before);
        map.put("after", after);
        map.put("alt", alt);
    }

    @Override
    public void del(String accession, Locus first, Locus last) {
        map = new HashMap<>();
        map.put("kind", "DEL");
        map.put("acc", accession);
        map.put("first", first);
        map.put("last", last);
    }

    @Override
    public void delins(String accession, Locus first, Locus last, String alt) {
        map = new HashMap<>();
        map.put("kind", "DELINS");
        map.put("acc", accession);
        map.put("first", first);
        map.put("last", last);
        map.put("alt", alt);
    }

    @Override
    public void dup(String accession, Locus first, Locus last) {
        map = new HashMap<>();
        map.put("kind", "DUP");
        map.put("acc", accession);
        map.put("first", first);
        map.put("last", last);

    }

    @Override
    public void inv(String accession, Locus first, Locus last) {
        map = new HashMap<>();
        map.put("kind", "INV");
        map.put("acc", accession);
        map.put("first", first);
        map.put("last", last);
    }

    @Override
    public void rep(String accession, Locus first, String ref, Integer num) {
        assert (false);
    }
}
