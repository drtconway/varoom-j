package test.org.petermac.pathos.varoom.hgvs;

import org.junit.Test; 
import org.junit.Before; 
import org.junit.After;
import org.petermac.pathos.varoom.hgvs.Hgvs;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


/** 
* Hgvs Tester. 
* 
* @author <Authors name> 
* @since <pre>Aug. 27, 2019</pre> 
* @version 1.0 
*/ 
public class HgvsTest {

    @Before
    public void before()  {
    }

    @After
    public void after()  {
    }

    /**
     *
     * Method: hgvsg2map(String hgvsg)
     *
     */
    @Test
    public void testHgvsg2map()  {
//TODO: Test goes here... 
    }

    @Test
    public void testHgvsc2mapSub1() throws Exception {
        Map<String,Object> m = Hgvs.hgvsc2map("NM_016169.3:c.182+16C>T");
        assertNotNull(m);
        assertEquals(5, m.size());
        assertEquals("SUB", m.get("kind"));
        assertEquals("NM_016169.3", m.get("acc"));
        assertEquals("182+16", m.get("pos").toString());
        assertEquals("C", m.get("ref"));
        assertEquals("T", m.get("alt"));
    }

    @Test
    public void testHgvsc2mapSub2() throws Exception {
        Map<String,Object> m = Hgvs.hgvsc2map("NM_000141.4:c.*259C>T");
        assertNotNull(m);
        assertEquals(5, m.size());
        assertEquals("SUB", m.get("kind"));
        assertEquals("NM_000141.4", m.get("acc"));
        assertEquals("*259", m.get("pos").toString());
        assertEquals("C", m.get("ref"));
        assertEquals("T", m.get("alt"));
    }

    @Test
    public void testHgvsc2mapSub3() throws Exception {
        Map<String,Object> m = Hgvs.hgvsc2map("NM_002412.3:c.-193A>T");
        assertNotNull(m);
        assertEquals(5, m.size());
        assertEquals("SUB", m.get("kind"));
        assertEquals("NM_002412.3", m.get("acc"));
        assertEquals("-193", m.get("pos").toString());
        assertEquals("A", m.get("ref"));
        assertEquals("T", m.get("alt"));
    }

    @Test
    public void testHgvsc2mapIns1() throws Exception {
        Map<String,Object> m = Hgvs.hgvsc2map("NM_000314.4:c.253+106_253+107insTTATC");
        assertNotNull(m);
        assertEquals(5, m.size());
        assertEquals("INS", m.get("kind"));
        assertEquals("NM_000314.4", m.get("acc"));
        assertEquals("253+106", m.get("before").toString());
        assertEquals("253+107", m.get("after").toString());
        assertEquals("TTATC", m.get("alt"));
    }

    @Test
    public void testHgvsc2mapDel1() throws Exception {
        Map<String,Object> m = Hgvs.hgvsc2map("NM_001165.4:c.-1708_-1707del");
        assertNotNull(m);
        assertEquals(4, m.size());
        assertEquals("DEL", m.get("kind"));
        assertEquals("NM_001165.4", m.get("acc"));
        assertEquals("-1708", m.get("first").toString());
        assertEquals("-1707", m.get("last").toString());
    }

}
