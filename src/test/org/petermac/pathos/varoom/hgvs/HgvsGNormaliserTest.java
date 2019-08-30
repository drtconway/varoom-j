package test.org.petermac.pathos.varoom.hgvs; 

import org.junit.Test; 
import org.junit.Before; 
import org.junit.After;
import org.petermac.pathos.varoom.Fasta;
import org.petermac.pathos.varoom.FileFactory;
import org.petermac.pathos.varoom.SequenceFactory;
import org.petermac.pathos.varoom.StringFileFactory;
import org.petermac.pathos.varoom.hgvs.HgvsGFormatter;
import org.petermac.pathos.varoom.hgvs.HgvsGNormaliser;
import org.petermac.pathos.varoom.hgvs.HgvsGProcessor;

import java.util.ArrayList;
import java.util.function.Consumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * HgvsGNormaliser Tester.
 *
 * @author <Authors name>
 * @since <pre>Aug. 29, 2019</pre>
 * @version 1.0
 */
public class HgvsGNormaliserTest {
    static class TestSequenceFactory extends SequenceFactory {
        public TestSequenceFactory() {
            super(null, null);
        }

        @Override
        public Fasta getAccession(String accession) {
            return new Fasta(accession, "CCATATATATGG");
        }
    }

    SequenceFactory seqFac;
    Consumer<String> catcher;
    HgvsGNormaliser norm;
    ArrayList<String> results;

    @Before
    public void before() throws Exception {
        seqFac = new TestSequenceFactory();
        results = new ArrayList<>();
        catcher = (String str) ->  results.add(str);

        norm = new HgvsGNormaliser(seqFac, new HgvsGFormatter(catcher));
        norm.checkReferenceMatches = true;
        norm.enable3primeShifts = true;
    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testIns1() throws Exception {
        results.clear();
        norm.ins("chr1", 4, 5, "AT");
        assertEquals(1, results.size());
        assertEquals("chr1:g.9_10dup", results.get(0));
    }

    @Test
    public void testIns2() throws Exception {
        results.clear();
        norm.ins("chr1", 4, 5, "ATAT");
        assertEquals(1, results.size());
        assertEquals("chr1:g.7_10dup", results.get(0));
    }

    @Test
    public void testIns3() throws Exception {
        results.clear();
        norm.ins("chr1", 4, 5, "ATATATATAT");
        assertEquals(1, results.size());
        assertEquals("chr1:g.10_11insATATATATAT", results.get(0));
    }

    /**
     *
     * Method: del(String accession, int first, int last)
     *
     */
    @Test
    public void testDel() throws Exception {
//TODO: Test goes here... 
    }

    /**
     *
     * Method: delins(String accession, int first, int last, String alt)
     *
     */
    @Test
    public void testDelins() throws Exception {
//TODO: Test goes here... 
    }

    /**
     *
     * Method: dup(String accession, int first, int last)
     *
     */
    @Test
    public void testDup() throws Exception {
//TODO: Test goes here... 
    }

    /**
     *
     * Method: inv(String accession, int first, int last)
     *
     */
    @Test
    public void testInv() throws Exception {
//TODO: Test goes here... 
    }

    /**
     *
     * Method: rep(String accession, int first, String ref, int num)
     *
     */
    @Test
    public void testRep() throws Exception {
//TODO: Test goes here... 
    }

    /**
     *
     * Method: error(String message)
     *
     */
    @Test
    public void testError() throws Exception {
//TODO: Test goes here... 
    }


    /**
     *
     * Method: getAccession(String accession)
     *
     */
    @Test
    public void testGetAccession() throws Exception {
//TODO: Test goes here... 
/* 
try { 
   Method method = HgvsGNormaliser.getClass().getMethod("getAccession", String.class); 
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/
    }

} 
