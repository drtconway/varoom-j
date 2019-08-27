package test.org.petermac.pathos.vcfflow;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import org.petermac.pathos.vcfflow.hgvs.Hgvs;
import org.petermac.pathos.vcfflow.hgvs.HgvsGApplyer;
import org.petermac.pathos.vcfflow.Rope;

/** 
* HgvsApplyer Tester. 
* 
* @author <Authors name> 
* @since <pre>Aug. 16, 2019</pre> 
* @version 1.0 
*/ 
public class HgvsApplyerTest { 
    public static class Applyer extends HgvsGApplyer {
        private static final String orig = "GAAGAGGTGAATGTAATTCCTCCACACACTCCAGTTAGGTATGAATTTTCCTACTTTTAATTATATTATAATTTTG";
        public String sequence;

        public Applyer() {
            sequence = orig;
        }

        @Override
        protected Rope getAccession(String accession) {
            assertEquals("for testing, the accession must be chr1", "chr1", accession);

            return Rope.atom(sequence);
        }

        @Override
        protected void putResult(String accession, Rope mutant) {
            sequence = mutant.toString();
        }

        @Override
        public void error(String message) throws Exception {
            throw new Exception(message);
        }
    }
/** 
* 
* Method: sub(String accession, Integer pos, String ref, String alt) 
* 
*/ 
@Test
public void testSubG() throws Exception {
    Applyer a = new Applyer();
    String var = "chr1:g.15A>C";
    Hgvs.apply(var, a);
    String expected = "GAAGAGGTGAATGTCATTCCTCCACACACTCCAGTTAGGTATGAATTTTCCTACTTTTAATTATATTATAATTTTG";
    assertEquals(expected, a.sequence);
} 

/** 
* 
* Method: ins(String accession, Integer before, Integer after, String alt) 
* 
*/ 
@Test
public void testInsG() throws Exception {
    Applyer a = new Applyer();
    String var = "chr1:g.15_16insGGG";
    Hgvs.apply(var, a);
    String expected = "GAAGAGGTGAATGTAGGGATTCCTCCACACACTCCAGTTAGGTATGAATTTTCCTACTTTTAATTATATTATAATTTTG";
    assertEquals(expected, a.sequence);
}

    /**
     *
     * Method: del(String accession, Integer first, Integer last)
     *
     */
    @Test
    public void testDel1() throws Exception {
        Applyer a = new Applyer();
        String var = "chr1:g.15del";
        Hgvs.apply(var, a);
        String expected = "GAAGAGGTGAATGTATTCCTCCACACACTCCAGTTAGGTATGAATTTTCCTACTTTTAATTATATTATAATTTTG";
        assertEquals(expected, a.sequence);
    }

    /**
     *
     * Method: del(String accession, Integer first, Integer last)
     *
     */
    @Test
    public void testDel2() throws Exception {
        Applyer a = new Applyer();
        String var = "chr1:g.15_16del";
        Hgvs.apply(var, a);
        String expected = "GAAGAGGTGAATGTTTCCTCCACACACTCCAGTTAGGTATGAATTTTCCTACTTTTAATTATATTATAATTTTG";
        assertEquals(expected, a.sequence);
    }

    /**
     *
     * Method: delins(String accession, Integer first, Integer last, String alt)
     *
     */
    @Test
    public void testDelins1() throws Exception {
        Applyer a = new Applyer();
        String var = "chr1:g.15delinsGGG";
        Hgvs.apply(var, a);
        String expected = "GAAGAGGTGAATGTGGGATTCCTCCACACACTCCAGTTAGGTATGAATTTTCCTACTTTTAATTATATTATAATTTTG";
        assertEquals(expected, a.sequence);
    }
    /**
     *
     * Method: delins(String accession, Integer first, Integer last, String alt)
     *
     */
    @Test
    public void testDelins2() throws Exception {
        Applyer a = new Applyer();
        String var = "chr1:g.15_16delinsGGG";
        Hgvs.apply(var, a);
        String expected = "GAAGAGGTGAATGTGGGTTCCTCCACACACTCCAGTTAGGTATGAATTTTCCTACTTTTAATTATATTATAATTTTG";
        assertEquals(expected, a.sequence);
    }


    /**
     *
     * Method: dup(String accession, Integer first, Integer last)
     *
     */
    @Test
    public void testDup1() throws Exception {
        Applyer a = new Applyer();
        String var = "chr1:g.15dup";
        Hgvs.apply(var, a);
        String expected = "GAAGAGGTGAATGTAAATTCCTCCACACACTCCAGTTAGGTATGAATTTTCCTACTTTTAATTATATTATAATTTTG";
        assertEquals(expected, a.sequence);
    }
    /**
     *
     * Method: dup(String accession, Integer first, Integer last)
     *
     */
    @Test
    public void testDup2() throws Exception {
        Applyer a = new Applyer();
        String var = "chr1:g.15_17dup";
        Hgvs.apply(var, a);
        String expected = "GAAGAGGTGAATGTAATAATTCCTCCACACACTCCAGTTAGGTATGAATTTTCCTACTTTTAATTATATTATAATTTTG";
        assertEquals(expected, a.sequence);
    }

    /**
     *
     * Method: inv(String accession, Integer first, Integer last)
     *
     */
    @Test
    public void testInv() throws Exception {
        Applyer a = new Applyer();
        String var = "chr1:g.15_17inv";
        Hgvs.apply(var, a);
        String expected = "GAAGAGGTGAATGTATTTCCTCCACACACTCCAGTTAGGTATGAATTTTCCTACTTTTAATTATATTATAATTTTG";
        assertEquals(expected, a.sequence);
    }

    /**
     *
     * Method: rep(String accession, Integer first, String ref, Integer num)
     *
     */
    @Test
    public void testRep()  {
        // Rep is poorly defined, so it doesn't make sense to apply them!
    }

} 
