package org.petermac.pathos.vcfflow;

import java.util.Arrays;

public abstract class Rope {
    private static class Atom extends Rope {
        final String txt;
        Atom(String txt) {
            super();
            this.txt = txt;
        }
        public final Integer size() {
            return txt.length();
        }

        public final char getAt(Integer idx) {
            return txt.charAt(idx);
        }

        public final String getAt(Integer from, Integer to) {
            return txt.substring(from, to);
        }
    }

    private static class Substr extends Rope {
        final Rope parent;
        final Integer begin;
        final Integer end;

        Substr(Rope parent, Integer begin, Integer end) {
            this.parent = parent;
            this.begin = begin;
            this.end = end;
        }

        public final Integer size() {
            return end - begin;
        }

        public final char getAt(Integer idx) {
            assert(idx >= 0 && idx < size());
            return parent.getAt(begin + idx);
        }

        public final String getAt(Integer from, Integer to) {
            return parent.getAt(begin + from, begin + to);
        }
    }

    private static class Concat extends Rope {
        final Rope lhs;
        final Rope rhs;

        Concat(Rope lhs, Rope rhs) {
            this.lhs = lhs;
            this.rhs = rhs;
        }

        public final Integer size() {
            return lhs.size() + rhs.size();
        }

        public final char getAt(Integer idx) {
            Integer n = lhs.size();
            if (idx < n) {
                return lhs.getAt(idx);
            } else {
                return rhs.getAt(idx - n);
            }
        }

        public final String getAt(Integer from, Integer to) {
            Integer n = lhs.size();
            if (to <= n) {
                return lhs.getAt(from, to);
            }
            if (from >= n) {
                return rhs.getAt(from - n, to - n);
            }
            // Split across the junction
            return lhs.getAt(from, n) + rhs.getAt(0, to - n);
        }
    }

    public static Rope atom(String txt) {
        return new Atom(txt);
    }

    public static Rope substr(Rope parent, Integer begin, Integer end) {
        return new Substr(parent, begin, end).simplify();
    }

    private static Rope concat(Rope lhs, Rope rhs) {
        return new Concat(lhs, rhs).simplify();
    }

    public static Rope[] split(Rope parent, Integer idx) {
        Rope[] pair = new Rope[2];
        pair[0] = substr(parent, 0, idx);
        pair[1] = substr(parent, idx, parent.size());
        return pair;
    }

    public static Rope join(Rope[] items) {
        Integer n = items.length;
        if (n == 1) {
            return items[0];
        } else {
            Integer m = n / 2;
            Rope[] lhsItems = Arrays.copyOfRange(items, 0, m);
            Rope[] rhsItems = Arrays.copyOfRange(items, m, n);
            Rope lhs = join(lhsItems);
            Rope rhs = join(rhsItems);
            return concat(lhs, rhs);
        }
    }

    public abstract Integer size();

    public abstract char getAt(Integer idx);

    public abstract String getAt(Integer from, Integer to);

    public final String toString() {
        Integer n = size();
        return getAt(0, n);
    }

    Rope simplify() {
        return this;
    }
}
