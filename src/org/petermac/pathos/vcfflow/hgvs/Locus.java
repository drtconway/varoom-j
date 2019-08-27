package org.petermac.pathos.vcfflow.hgvs;

public class Locus {
    public enum Kind { EXON, INTRON, UTR5, UTR3 }

    public Kind kind;
    public Integer txPos;
    public Integer relPos;

    public String toString() {
        switch (kind) {
            case EXON:
                assert txPos > 0;
                assert relPos == null;
                return txPos.toString();
            case UTR5:
                assert txPos < 0;
                if (relPos == null) {
                    return txPos.toString();
                }
                if (relPos > 0) {
                    return txPos.toString() + "+" + relPos.toString();
                } else {
                    return txPos.toString() + relPos.toString();
                }
            case UTR3:
                assert txPos > 0;
                if (relPos == null) {
                    return "*" + txPos.toString();
                }
                if (relPos > 0) {
                    return "*" + txPos.toString() + "+" + relPos.toString();
                } else {
                    return "*" + txPos.toString() + relPos.toString();
                }
            case INTRON:
                assert txPos > 0;
                assert relPos != null;
                assert relPos != 0;
                if (relPos > 0) {
                    return txPos.toString() + "+" + relPos.toString();
                } else {
                    return txPos.toString() + relPos.toString();
                }
        }
        return null;
    }
}
