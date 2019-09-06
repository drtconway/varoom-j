package test.org.petermac.pathos.varoom.hgvs; 

import org.junit.Test; 
import org.junit.Before; 
import org.junit.After;
import org.petermac.pathos.varoom.SequenceFactory;
import org.petermac.pathos.varoom.hgvs.*;
import test.org.petermac.pathos.varoom.FakeSequenceFactory;

import static org.junit.Assert.assertEquals;

/** 
* HgvsVcfHandler Tester. 
* 
* @author <Authors name> 
* @since <pre>Aug. 30, 2019</pre> 
* @version 1.0 
*/ 
public class HgvsVcfHandlerTest {


    @Before
    public void before() throws Exception {

    }

    @After
    public void after() throws Exception {
    }

    @Test
    public void testApply() throws Exception {
        SequenceFactory sfac = new FakeSequenceFactory();
        HgvsGProcessor mapper = new HgvsGFormatter((String str) -> {
            System.out.println(str);
        });
        HgvsGNormaliser norm = new HgvsGNormaliser(sfac, mapper);
        norm.enable3primeShifts = true;
        HgvsGVcfHandler v = new HgvsGVcfHandler(norm);
        v.apply("chr1", 33476432, ".", "A", "AGGATGT", 0.0, "", null, null);
    }

} 
