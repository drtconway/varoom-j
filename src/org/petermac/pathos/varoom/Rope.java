package org.petermac.pathos.varoom;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public abstract class Rope {
    private static class Atom extends Rope {
        final String txt;
        Atom(String txt) {
            super();
            this.txt = txt;
        }
        public final int size() {
            return txt.length();
        }

        public final char getAt(int idx) {
            return txt.charAt(idx);
        }

        public final String getAt(int from, int to) {
            return txt.substring(from, to);
        }
    }

    private static class Bytes extends Rope {
        final ByteBuffer bytes;
        final int offset;
        final int length;

        Bytes(ByteBuffer txt, int length) {
            super();
            this.bytes = txt;
            this.offset = 0;
            this.length = length;
        }

        Bytes(ByteBuffer txt, int offset, int length) {
            super();
            this.bytes = txt;
            this.offset = offset;
            this.length = length;
        }
        public final int size() {
            return length;
        }

        public final char getAt(int idx) {
            return bytes.getChar(offset + idx);
        }

        public final String getAt(int from, int to) {
            final int n = to - from;
            byte[] resBytes = new byte[n];
            for (int i = 0; i < n; i++) {
                resBytes[i] = bytes.get(offset + from + i);
            }
            return new String(resBytes, StandardCharsets.US_ASCII);
        }
    }

    private static class Substr extends Rope {
        final Rope parent;
        final int begin;
        final int end;

        Substr(Rope parent, int begin, int end) {
            this.parent = parent;
            this.begin = begin;
            this.end = end;
        }

        public final int size() {
            return end - begin;
        }

        public final char getAt(int idx) {
            assert(idx >= 0 && idx < size());
            return parent.getAt(begin + idx);
        }

        public final String getAt(int from, int to) {
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

        public final int size() {
            return lhs.size() + rhs.size();
        }

        public final char getAt(int idx) {
            int n = lhs.size();
            if (idx < n) {
                return lhs.getAt(idx);
            } else {
                return rhs.getAt(idx - n);
            }
        }

        public final String getAt(int from, int to) {
            int n = lhs.size();
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

    public static Rope atom(ByteBuffer bytes, int length) {
        return new Bytes(bytes, length);
    }

    public static Rope substr(Rope parent, int begin, int end) {
        return new Substr(parent, begin, end).simplify();
    }

    private static Rope concat(Rope lhs, Rope rhs) {
        return new Concat(lhs, rhs).simplify();
    }

    public static Rope[] split(Rope parent, int idx) {
        Rope[] pair = new Rope[2];
        pair[0] = substr(parent, 0, idx);
        pair[1] = substr(parent, idx, parent.size());
        return pair;
    }

    public static Rope join(Rope[] items) {
        int n = items.length;
        if (n == 1) {
            return items[0];
        } else {
            int m = n / 2;
            Rope[] lhsItems = Arrays.copyOfRange(items, 0, m);
            Rope[] rhsItems = Arrays.copyOfRange(items, m, n);
            Rope lhs = join(lhsItems);
            Rope rhs = join(rhsItems);
            return concat(lhs, rhs);
        }
    }

    public abstract int size();

    public abstract char getAt(int idx);

    public abstract String getAt(int from, int to);

    public final String toString() {
        int n = size();
        return getAt(0, n);
    }

    Rope simplify() {
        return this;
    }
}
