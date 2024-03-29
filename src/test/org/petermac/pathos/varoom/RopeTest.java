package test.org.petermac.pathos.varoom;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import org.petermac.pathos.varoom.FileFactory;
import org.petermac.pathos.varoom.Rope;
import org.petermac.pathos.varoom.StringFileFactory;

import java.io.UnsupportedEncodingException;

/** 
* Rope Tester. 
* 
* @author <Authors name> 
* @since <pre>Aug. 16, 2019</pre> 
* @version 1.0 
*/ 
public class RopeTest {


    @Test
    public void testAtom0() {
        Rope r = Rope.atom("");
        int rl = r.size();
        assertEquals("empty rope must have size 0", 0, rl);
    }

    @Test
    public void testAtom1() {
        String seq = "GAAGAGGTGAATGTAATTCCTCCACACACTCCAGTTAGGTATGAATTTTCCTACTTTTAATTATATTATAATTTTG";
        Rope r = Rope.atom(seq);
        int sl = seq.length();
        int rl = r.size();
        assertEquals("size of rope must match string length", sl, rl);
    }

    @Test
    public void testAtom2() {
        String seq = "GAAGAGGTGAATGTAATTCCTCCACACACTCCAGTTAGGTATGAATTTTCCTACTTTTAATTATATTATAATTTTG";
        int n = seq.length();
        Rope r = Rope.atom(seq);
        assertEquals("character access", 'G', r.getAt(0));
        assertEquals("substring access", "GAAG", r.getAt(0, 4));
        assertEquals("substring access", "AATTATATTATAATTTTG", r.getAt(58, n));
    }

    @Test
    public void testSubstr1() {
        String seq = "GAAGAGGTGAATGTAATTCCTCCACACACTCCAGTTAGGTATGAATTTTCCTACTTTTAATTATATTATAATTTTG";
        int n = seq.length();
        Rope r = Rope.atom(seq);
        Rope s = Rope.substr(r, 0, 0);
        int sl = s.size();
        assertEquals("empty substring", 0, sl);
    }

    @Test
    public void testSubstr2() {
        String seq = "GAAGAGGTGAATGTAATTCCTCCACACACTCCAGTTAGGTATGAATTTTCCTACTTTTAATTATATTATAATTTTG";
        int n = seq.length();
        Rope r = Rope.atom(seq);
        Rope s = Rope.substr(r, 10, 20);
        int sl = s.size();
        assertEquals("cons substring", 10, sl);
        assertEquals("cons substring", "ATGTAATTCC", s.toString());
    }

    @Test
    public void testBytes1() throws UnsupportedEncodingException {
        StringFileFactory fac = new StringFileFactory();
        fac.files.put("chr1", "GAAGAGGTGAATGTAATTCCTCCACACACTCCAGTTAGGTATGAATTTTCCTACTTTTAATTATATTATAATTTTG");
        FileFactory.MapResult m = fac.map("chr1");
        Rope r = Rope.atom(m.map, (int)m.length);
        Rope s = Rope.substr(r, 10, 20);
        int sl = s.size();
        assertEquals("cons substring", 10, sl);
        assertEquals("cons substring", "ATGTAATTCC", s.toString());
    }

}
