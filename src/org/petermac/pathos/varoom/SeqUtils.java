package org.petermac.pathos.varoom;

import java.util.HashMap;
import java.util.Map;

public class SeqUtils {
    public static class Hg19 {
        public static Map<String,String> chr2RefSeq = makeChr2RefSeq();
        public static Map<String,String> refSeq2Chr = makeRefSeq2Chr();

        private static Map<String, String> makeChr2RefSeq() {
            Map<String,String> res = new HashMap<>();
            res.put("chr1", "NC_000001.10");
            res.put("chr2", "NC_000002.11");
            res.put("chr3", "NC_000003.11");
            res.put("chr4", "NC_000004.11");
            res.put("chr5", "NC_000005.9");
            res.put("chr6", "NC_000006.11");
            res.put("chr7", "NC_000007.13");
            res.put("chr8", "NC_000008.10");
            res.put("chr9", "NC_000009.11");
            res.put("chr10", "NC_000010.10");
            res.put("chr11", "NC_000011.9");
            res.put("chr12", "NC_000012.11");
            res.put("chr13", "NC_000013.10");
            res.put("chr14", "NC_000014.8");
            res.put("chr15", "NC_000015.9");
            res.put("chr16", "NC_000016.9");
            res.put("chr17", "NC_000017.10");
            res.put("chr18", "NC_000018.9");
            res.put("chr19", "NC_000019.9");
            res.put("chr20", "NC_000020.10");
            res.put("chr21", "NC_000021.8");
            res.put("chr22", "NC_000022.10");
            res.put("chrX", "NC_000023.10");
            res.put("chrY", "NC_000024.9");
            return res;
        }

        private static Map<String, String> makeRefSeq2Chr() {
            Map<String,String> res = new HashMap<>();
            makeChr2RefSeq().forEach((String c, String r) -> res.put(r, c));
            return res;
        }
    }
    public static class Hg38 {
        public static Map<String,String> chr2RefSeq = makeChr2RefSeq();
        public static Map<String,String> refSeq2Chr = makeRefSeq2Chr();

        private static Map<String, String> makeChr2RefSeq() {
            Map<String,String> res = new HashMap<>();
            res.put("chr1", "NC_000001.11");
            res.put("chr2", "NC_000002.12");
            res.put("chr3", "NC_000003.12");
            res.put("chr4", "NC_000004.12");
            res.put("chr5", "NC_000005.10");
            res.put("chr6", "NC_000006.12");
            res.put("chr7", "NC_000007.14");
            res.put("chr8", "NC_000008.11");
            res.put("chr9", "NC_000009.12");
            res.put("chr10", "NC_000010.11");
            res.put("chr11", "NC_000011.10");
            res.put("chr12", "NC_000012.12");
            res.put("chr13", "NC_000013.11");
            res.put("chr14", "NC_000014.9");
            res.put("chr15", "NC_000015.10");
            res.put("chr16", "NC_000016.10");
            res.put("chr17", "NC_000017.11");
            res.put("chr18", "NC_000018.10");
            res.put("chr19", "NC_000019.10");
            res.put("chr20", "NC_000020.11");
            res.put("chr21", "NC_000021.9");
            res.put("chr22", "NC_000022.11");
            res.put("chrX", "NC_000023.11");
            res.put("chrY", "NC_000024.10");
            return res;
        }
    }

    private static Map<String, String> makeRefSeq2Chr() {
        Map<String,String> res = new HashMap<>();
        makeRefSeq2Chr().forEach((String c, String r) -> res.put(r, c));
        return res;
    }

    public static String reverseComplement(String seq) {
        int n = seq.length();
        StringBuilder bldr = new StringBuilder(n);
        for (int i = 0; i < n; i++) {
            char c = seq.charAt(i);
            switch (c) {
                case 'a':
                    c = 't';
                    break;
                case 'A':
                    c = 'T';
                    break;
                case 'c':
                    c = 'g';
                    break;
                case 'C':
                    c = 'G';
                    break;
                case 'g':
                    c = 'c';
                    break;
                case 'G':
                    c = 'C';
                    break;
                case 't':
                    c = 'a';
                    break;
                case 'T':
                    c = 'A';
                    break;
            }
            bldr.append(c);
        }
        bldr.reverse();
        return bldr.toString();
    }

    static String codon2aa3(String codon) {
        switch (codon) {
            case "AAA":
                return "Lys";
            case "AAC":
                return "Asn";
            case "AAG":
                return "Lys";
            case "AAT":
                return "Asn";
            case "ACA":
                return "Thr";
            case "ACC":
                return "Thr";
            case "ACG":
                return "Thr";
            case "ACT":
                return "Thr";
            case "AGA":
                return "Arg";
            case "AGC":
                return "Ser";
            case "AGG":
                return "Arg";
            case "AGT":
                return "Ser";
            case "ATA":
                return "Ile";
            case "ATC":
                return "Ile";
            case "ATG":
                return "Met";
            case "ATT":
                return "Ile";
            case "CAA":
                return "Gln";
            case "CAC":
                return "His";
            case "CAG":
                return "Gln";
            case "CAT":
                return "His";
            case "CCA":
                return "Pro";
            case "CCC":
                return "Pro";
            case "CCG":
                return "Pro";
            case "CCT":
                return "Pro";
            case "CGA":
                return "Arg";
            case "CGC":
                return "Arg";
            case "CGG":
                return "Arg";
            case "CGT":
                return "Arg";
            case "CTA":
                return "Leu";
            case "CTC":
                return "Leu";
            case "CTG":
                return "Leu";
            case "CTT":
                return "Leu";
            case "GAA":
                return "Glu";
            case "GAC":
                return "Asp";
            case "GAG":
                return "Glu";
            case "GAT":
                return "Asp";
            case "GCA":
                return "Ala";
            case "GCC":
                return "Ala";
            case "GCG":
                return "Ala";
            case "GCT":
                return "Ala";
            case "GGA":
                return "Gly";
            case "GGC":
                return "Gly";
            case "GGG":
                return "Gly";
            case "GGT":
                return "Gly";
            case "GTA":
                return "Val";
            case "GTC":
                return "Val";
            case "GTG":
                return "Val";
            case "GTT":
                return "Val";
            case "TAA":
                return "END";
            case "TAC":
                return "Tyr";
            case "TAG":
                return "END";
            case "TAT":
                return "Tyr";
            case "TCA":
                return "Ser";
            case "TCC":
                return "Ser";
            case "TCG":
                return "Ser";
            case "TCT":
                return "Ser";
            case "TGA":
                return "END";
            case "TGC":
                return "Cys";
            case "TGG":
                return "Trp";
            case "TGT":
                return "Cys";
            case "TTA":
                return "Leu";
            case "TTC":
                return "Phe";
            case "TTG":
                return "Leu";
            case "TTT":
                return "Phe";
        }
        return null;
    }

    static String codon2aa1(String codon) {
        switch (codon) {
            case "AAA":
                return "K";
            case "AAC":
                return "N";
            case "AAG":
                return "K";
            case "AAT":
                return "N";
            case "ACA":
                return "T";
            case "ACC":
                return "T";
            case "ACG":
                return "T";
            case "ACT":
                return "T";
            case "AGA":
                return "R";
            case "AGC":
                return "S";
            case "AGG":
                return "R";
            case "AGT":
                return "S";
            case "ATA":
                return "I";
            case "ATC":
                return "I";
            case "ATG":
                return "M";
            case "ATT":
                return "I";
            case "CAA":
                return "Q";
            case "CAC":
                return "H";
            case "CAG":
                return "Q";
            case "CAT":
                return "H";
            case "CCA":
                return "P";
            case "CCC":
                return "P";
            case "CCG":
                return "P";
            case "CCT":
                return "P";
            case "CGA":
                return "R";
            case "CGC":
                return "R";
            case "CGG":
                return "R";
            case "CGT":
                return "R";
            case "CTA":
                return "L";
            case "CTC":
                return "L";
            case "CTG":
                return "L";
            case "CTT":
                return "L";
            case "GAA":
                return "E";
            case "GAC":
                return "D";
            case "GAG":
                return "E";
            case "GAT":
                return "D";
            case "GCA":
                return "A";
            case "GCC":
                return "A";
            case "GCG":
                return "A";
            case "GCT":
                return "A";
            case "GGA":
                return "G";
            case "GGC":
                return "G";
            case "GGG":
                return "G";
            case "GGT":
                return "G";
            case "GTA":
                return "V";
            case "GTC":
                return "V";
            case "GTG":
                return "V";
            case "GTT":
                return "V";
            case "TAA":
                return "$";
            case "TAC":
                return "Y";
            case "TAG":
                return "$";
            case "TAT":
                return "Y";
            case "TCA":
                return "S";
            case "TCC":
                return "S";
            case "TCG":
                return "S";
            case "TCT":
                return "S";
            case "TGA":
                return "$";
            case "TGC":
                return "C";
            case "TGG":
                return "W";
            case "TGT":
                return "C";
            case "TTA":
                return "L";
            case "TTC":
                return "F";
            case "TTG":
                return "L";
            case "TTT":
                return "F";
        }
        return null;
    }
}
