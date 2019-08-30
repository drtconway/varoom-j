package org.petermac.pathos.varoom.hgvs;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Hgvs {
    public enum Kind { SIL, SUB, INS, DEL, DELINS, DUP, REP, INV, FS}

    public static class KindAndHgvsG {
        public Hgvs.Kind kind;
        public String hgvsg;
    }

    public static ArrayList<String> vcfVarToHgvsGs(String chrom, Integer pos, String ref, String alt) {
        chrom = normaliseChrom(chrom);
        ArrayList<String> res = new ArrayList<>();
        int p = 0;
        int q = alt.indexOf(',', p);
        while (q >= 0) {
            String s = alt.substring(p, q);
            String hgvsg = vcfVarToHgvsG(chrom, pos, ref, s);
            res.add(hgvsg);
            p = q + 1;
            q = alt.indexOf(',', p);
        }
        String s = alt.substring(p);
        String hgvsg = vcfVarToHgvsG(chrom, pos, ref, s);
        res.add(hgvsg);
        return res;
    }

    public static void vcfVarToHgvsGs(String chrom, Integer pos, String ref, String alt, ArrayList<KindAndHgvsG> res) {
        res.clear();

        chrom = normaliseChrom(chrom);
        int p = 0;
        int q = alt.indexOf(',', p);
        while (q >= 0) {
            String s = alt.substring(p, q);
            KindAndHgvsG v = new KindAndHgvsG();
            vcfVarToHgvsG(chrom, pos, ref, s, v);
            res.add(v);
            p = q + 1;
            q = alt.indexOf(',', p);
        }
        String s = alt.substring(p);
        KindAndHgvsG v = new KindAndHgvsG();
        vcfVarToHgvsG(chrom, pos, ref, s, v);
        res.add(v);
    }

    public static String vcfVarToHgvsG(String chrom, Integer pos, String ref, String alt) {
        assert(!alt.contains(","));
        if (ref.length() == 1 && alt.length() == 1) {
            return chrom + ":g." + pos.toString() + ref + ">" + alt;
        }
        if (ref.length() == 1 && alt.length() > 1) {
            Integer pos1 = pos + 1;
            return chrom + ":g." + pos.toString() + "_" + pos1.toString() + "ins" + alt.substring(1);
        }
        if (ref.length() > 1 && alt.length() == 1) {
            int n = ref.length() - 1;
            Integer pos1 = pos + 1;
            Integer pos1n = pos + 1 + n;
            return chrom + ":g." + pos1.toString() + "_" + pos1n.toString() + "del" + ref.substring(1);
        }
        int n = ref.length() - 1;
        int pos1 = pos + 1;
        int pos1n = pos + 1 + n;
        return chrom + ":g." + pos1 + "_" + pos1n + "delins" + alt.substring(1);
    }
    public static void vcfVarToHgvsG(String chrom, Integer pos, String ref, String alt, KindAndHgvsG res) {
        assert(!alt.contains(","));
        if (ref.length() == 1 && alt.length() == 1) {
            res.kind = Kind.SUB;
            res.hgvsg = chrom + ":g." + pos.toString() + ref + ">" + alt;
            return;
        }
        if (ref.length() == 1 && alt.length() > 1) {
            Integer pos1 = pos + 1;
            res.kind = Kind.INS;
            res.hgvsg = chrom + ":g." + pos.toString() + "_" + pos1.toString() + "ins" + alt.substring(1);
            return;
        }
        if (ref.length() > 1 && alt.length() == 1) {
            int n = ref.length() - 1;
            Integer pos1 = pos + 1;
            Integer pos1n = pos + 1 + n;
            res.kind = Kind.DEL;
            res.hgvsg = chrom + ":g." + pos1.toString() + "_" + pos1n.toString() + "del" + ref.substring(1);
            return;
        }
        int n = ref.length() - 1;
        int pos1 = pos + 1;
        int pos1n = pos + 1 + n;
        res.kind = Kind.DELINS;
        res.hgvsg = chrom + ":g." + pos1 + "_" + pos1n + "delins" + alt.substring(1);
    }

    public static Map<String,Object> hgvsg2map(String hgvsg) throws Exception {
        HgvsG2Map proc = new HgvsG2Map();
        apply(hgvsg, proc);
        return proc.map;
    }

    public static Map<String,Object> hgvsc2map(String hgvsc) throws Exception {
        HgvsC2Map proc = new HgvsC2Map();
        apply(hgvsc, proc);
        return proc.map;
    }

    public static void apply(String hgvs, HgvsGProcessor proc) throws Exception {
        Matcher m;

        m = hgvsgSil1.matcher(hgvs);
        if (m.matches()) {
            ArrayList<String> gs = new ArrayList<>();
            for (int i = 0; i < m.groupCount(); i++) {
                gs.add(m.group(i));
            }
            System.out.println(gs);
            return;
        }

        m = hgvsgSil2.matcher(hgvs);
        if (m.matches()) {
            ArrayList<String> gs = new ArrayList<>();
            for (int i = 0; i < m.groupCount(); i++) {
                gs.add(m.group(i));
            }
            System.out.println(gs);
            return;
        }

        m = hgvsgSub.matcher(hgvs);
        if (m.matches()) {
            proc.sub(m.group(1), Integer.parseInt(m.group(2)), m.group(3), m.group(4));
            return;
        }

        m = hgvsgIns.matcher(hgvs);
        if (m.matches()) {
            proc.ins(m.group(1), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)), m.group(4));
            return;
        }

        m = hgvsgDel1.matcher(hgvs);
        if (m.matches()) {
            Integer pos = Integer.parseInt(m.group(2));
            proc.del(m.group(1), pos, pos);
            return;
        }
        m = hgvsgDel2.matcher(hgvs);
        if (m.matches()) {
            proc.del(m.group(1), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)));
            return;
        }

        m = hgvsgDelIns1.matcher(hgvs);
        if (m.matches()) {
            Integer pos = Integer.parseInt(m.group(2));
            proc.delins(m.group(1), pos, pos, m.group(3));
            return;
        }
        m = hgvsgDelIns2.matcher(hgvs);
        if (m.matches()) {
            proc.delins(m.group(1), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)), m.group(4));
            return;
        }

        m = hgvsgDup1.matcher(hgvs);
        if (m.matches()) {
            int pos = Integer.parseInt(m.group(2));
            proc.dup(m.group(1), pos, pos);
            return;
        }
        m = hgvsgDup2.matcher(hgvs);
        if (m.matches()) {
            proc.dup(m.group(1), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)));
            return;
        }

        m = hgvsgInv.matcher(hgvs);
        if (m.matches()) {
            proc.inv(m.group(1), Integer.parseInt(m.group(2)), Integer.parseInt(m.group(3)));
            return;
        }

        m = hgvsgRep.matcher(hgvs);
        if (m.matches()) {
            proc.rep(m.group(1), Integer.parseInt(m.group(2)), m.group(3), Integer.parseInt(m.group(4)));
            return;
        }

        proc.error("couldn't parse hgvsg string: " + hgvs);
    }

    public static void apply(String hgvs, HgvsCProcessor proc) throws Exception {
        Matcher m;

        m = hgvscSub.matcher(hgvs);
        if (m.matches()) {
            proc.sub(m.group(1), mkLocus(m, 2), m.group(6), m.group(7));
            return;
        }

        m = hgvscIns.matcher(hgvs);
        if (m.matches()) {
            Locus bef = mkLocus(m, 2);
            Locus aft = mkLocus(m, 6);
            proc.ins(m.group(1), bef, aft, m.group(10));
            return;
        }

        m = hgvscDel1.matcher(hgvs);
        if (m.matches()) {
            Locus fst = mkLocus(m, 2);
            proc.del(m.group(1), fst, fst);
            return;
        }
        m = hgvscDel2.matcher(hgvs);
        if (m.matches()) {
            Locus fst = mkLocus(m, 2);
            Locus lst = mkLocus(m, 6);
            proc.del(m.group(1), fst, lst);
            return;
        }

        m = hgvscDelIns1.matcher(hgvs);
        if (m.matches()) {
            Locus fst = mkLocus(m, 2);
            proc.delins(m.group(1), fst, fst, m.group(6));
            return;
        }
        m = hgvscDelIns2.matcher(hgvs);
        if (m.matches()) {
            Locus fst = mkLocus(m, 2);
            Locus lst = mkLocus(m, 6);
            proc.delins(m.group(1), fst, lst, m.group(10));
            return;
        }

        m = hgvscDup1.matcher(hgvs);
        if (m.matches()) {
            Locus fst = mkLocus(m, 2);
            proc.dup(m.group(1), fst, fst);
            return;
        }
        m = hgvscDup2.matcher(hgvs);
        if (m.matches()) {
            Locus fst = mkLocus(m, 2);
            Locus lst = mkLocus(m, 6);
            proc.dup(m.group(1), fst, lst);
            return;
        }

    }

    private static Locus mkLocus(Matcher m, int i) {
        Locus l = new Locus();
        switch (m.group(i+1)) {
            case "":
                l.kind = Locus.Kind.EXON;
                break;
            case "*":
                l.kind = Locus.Kind.UTR3;
                break;
            case "-":
                l.kind = Locus.Kind.UTR5;
                break;
            default:
                assert false;
                return null;
        }
        l.txPos = Integer.parseInt(m.group(i+2));
        if (l.kind == Locus.Kind.UTR5) {
            l.txPos = -l.txPos;
        }
        if (m.group(i+3) != null) {
            l.relPos = Integer.parseInt(m.group(i+3));
            if (l.kind == Locus.Kind.EXON) {
                l.kind = Locus.Kind.INTRON;
            }
        }
        return l;
    }

    public static String normaliseChrom(String chrom) {
        if (!chrom.startsWith("chr")) {
            return "chr" + chrom;
        }
        return chrom;
    }

    private static final String accPat = "([^:]+)";
    private static final String gPosPat = "([0-9]+)";
    private static final String cPosPat = "(([-*]?)([0-9]+)([-+]?[0-9]+)?)";
    private static final String pPosPat = "([0-9]+)";
    private static final String aaPat = "([A-Z][a-z][a-z]|[*])";

    private static final Pattern hgvsgSil1 = Pattern.compile(accPat + ":g[.]" + gPosPat + "=" + "([ACGTacgt]?)");
    private static final Pattern hgvsgSil2 = Pattern.compile(accPat + ":g[.]" + gPosPat + "_" + gPosPat + "=" + "([ACGTacgt]*)");
    private static final Pattern hgvsgSub = Pattern.compile(accPat + ":g[.]" + gPosPat + "([ACGTacgt])>([ACGTacgt])");
    private static final Pattern hgvsgIns = Pattern.compile(accPat + ":g[.]" + gPosPat + "_" + gPosPat + "ins" + "([ACGTacgt]+)");
    private static final Pattern hgvsgDel1 = Pattern.compile(accPat + ":g[.]" + gPosPat + "del" + "([ACGTacgtNn]*)");
    private static final Pattern hgvsgDel2 = Pattern.compile(accPat + ":g[.]" + gPosPat + "_" + gPosPat + "del" + "([ACGTacgtNn]*)");
    private static final Pattern hgvsgDelIns1 = Pattern.compile(accPat + ":g[.]" + gPosPat + "delins" + "([ACGTacgt]+)");
    private static final Pattern hgvsgDelIns2 = Pattern.compile(accPat + ":g[.]" + gPosPat + "_" + gPosPat + "delins" + "([ACGTacgt]+)");
    private static final Pattern hgvsgDup1 = Pattern.compile(accPat + ":g[.]" + gPosPat + "dup" + "([ACGTacgt]*)");
    private static final Pattern hgvsgDup2 = Pattern.compile(accPat + ":g[.]" + gPosPat + "_" + gPosPat + "dup" + "([ACGTacgt]*)");
    private static final Pattern hgvsgInv = Pattern.compile(accPat + ":g[.]" + gPosPat + "_" + gPosPat + "inv" + "([ACGTacgt]*)");
    private static final Pattern hgvsgRep = Pattern.compile(accPat + ":g[.]" + gPosPat + "([ACGTacgt]+)\\[([0-9]+)\\]");

    private static final Pattern hgvscSub = Pattern.compile(accPat + ":c[.]" + cPosPat + "([ACGTacgt])>([ACGTacgt])");
    private static final Pattern hgvscIns = Pattern.compile(accPat + ":c[.]" + cPosPat + "_" + cPosPat + "ins" + "([ACGTacgt]+)");
    private static final Pattern hgvscDel1 = Pattern.compile(accPat + ":c[.]" + cPosPat + "del" + "([ACGTacgtNn]*)");
    private static final Pattern hgvscDel2 = Pattern.compile(accPat + ":c[.]" + cPosPat + "_" + cPosPat + "del" + "([ACGTacgtNn]*)");
    private static final Pattern hgvscDelIns1 = Pattern.compile(accPat + ":c[.]" + cPosPat + "delins" + "([ACGTacgtNn]*)");
    private static final Pattern hgvscDelIns2 = Pattern.compile(accPat + ":c[.]" + cPosPat + "_" + cPosPat + "delins" + "([ACGTacgtNn]*)");
    private static final Pattern hgvscDup1 = Pattern.compile(accPat + ":c[.]" + cPosPat + "dup" + "([ACGTacgt]*)");
    private static final Pattern hgvscDup2 = Pattern.compile(accPat + ":c[.]" + cPosPat + "_" + cPosPat + "dup" + "([ACGTacgt]*)");

    private static final Pattern hgvspSub1 = Pattern.compile(accPat + ":p[.]" + aaPat + pPosPat + aaPat);
    private static final Pattern hgvspSub2 = Pattern.compile(accPat + ":p[.]\\(" + aaPat + pPosPat + aaPat + "\\)");

    @Nullable
    public static Kind hgvsgKind(String hgvsg) {
        if (hgvsgSub.matcher(hgvsg).matches()) {
            return Kind.SUB;
        }
        if (hgvsgIns.matcher(hgvsg).matches()) {
            return Kind.INS;
        }
        if (hgvsgDel1.matcher(hgvsg).matches()) {
            return Kind.DEL;
        }
        if (hgvsgDel2.matcher(hgvsg).matches()) {
            return Kind.DEL;
        }
        if (hgvsgDelIns1.matcher(hgvsg).matches()) {
            return Kind.DELINS;
        }
        if (hgvsgDelIns2.matcher(hgvsg).matches()) {
            return Kind.DELINS;
        }
        if (hgvsgDup1.matcher(hgvsg).matches()) {
            return Kind.DUP;
        }
        if (hgvsgDup2.matcher(hgvsg).matches()) {
            return Kind.DUP;
        }
        if (hgvsgInv.matcher(hgvsg).matches()) {
            return Kind.INV;
        }
        if (hgvsgRep.matcher(hgvsg).matches()) {
            return Kind.REP;
        }
        if (hgvsgSil1.matcher(hgvsg).matches()) {
            return Kind.SIL;
        }
        if (hgvsgSil2.matcher(hgvsg).matches()) {
            return Kind.SIL;
        }
        System.out.println(hgvsg);
        return null;
    }

    public static boolean isSubHgvsG(String hgvsg) {
        return hgvsgSub.matcher(hgvsg).matches();
    }

    public static boolean isSubHgvsC(String hgvsc) {
        return hgvscSub.matcher(hgvsc).matches();
    }

    public static boolean isSubHgvsP(String hgvsp) {
        return hgvspSub1.matcher(hgvsp).matches()
            || hgvspSub2.matcher(hgvsp).matches();
    }

    public static boolean isSub(String hgvs) {
        return isSubHgvsG(hgvs) || isSubHgvsC(hgvs) || isSubHgvsG(hgvs);
    }
}
