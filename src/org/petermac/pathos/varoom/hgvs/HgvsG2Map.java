package org.petermac.pathos.varoom.hgvs;

import java.util.HashMap;
import java.util.Map;

public class HgvsG2Map implements HgvsGProcessor {
    public Map<String,Object> map;

    public HgvsG2Map() {
    }

    @Override
    public void sub(String accession, int pos, String ref, String alt) {
        map = new HashMap<>();
        map.put("kind", "SUB");
        map.put("acc", accession);
        map.put("pos", pos);
        map.put("ref", ref);
        map.put("alt", alt);
    }

    @Override
    public void ins(String accession, int before, int after, String alt) throws Exception {
        map = new HashMap<>();
        map.put("kind", "INS");
        map.put("acc", accession);
        map.put("before", before);
        map.put("after", after);
        map.put("alt", alt);
    }

    @Override
    public void del(String accession, int first, int last) {
        map = new HashMap<>();
        map.put("kind", "DEL");
        map.put("acc", accession);
        map.put("first", first);
        map.put("last", last);
    }

    @Override
    public void delins(String accession, int first, int last, String alt) {
        map = new HashMap<>();
        map.put("kind", "DELINS");
        map.put("acc", accession);
        map.put("first", first);
        map.put("last", last);
        map.put("alt", alt);
    }

    @Override
    public void dup(String accession, int first, int last) {
        map = new HashMap<>();
        map.put("kind", "DUP");
        map.put("acc", accession);
        map.put("first", first);
        map.put("last", last);
    }

    @Override
    public void inv(String accession, int first, int last) {
        map = new HashMap<>();
        map.put("kind", "INV");
        map.put("acc", accession);
        map.put("first", first);
        map.put("last", last);
    }

    @Override
    public void rep(String accession, int first, String ref, int num) {
        assert(false);
    }

    @Override
    public void error(String message) throws Exception {
        throw new Exception(message);
    }
}
