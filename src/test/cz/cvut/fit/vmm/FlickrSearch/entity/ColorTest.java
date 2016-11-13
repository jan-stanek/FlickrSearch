package cz.cvut.fit.vmm.FlickrSearch.entity;

import junit.framework.TestCase;

/**
 * Created by jan on 09.11.2016.
 */
public class ColorTest extends TestCase {
    private static final double eps = 0.001;

    public void testDistanceTo() throws Exception {
        Color c1 = new Color(0, 0, 0);
        Color c2 = new Color(255, 255, 255);
        Color c3 = new Color(255, 0, 0);
        Color c4 = new Color(0, 255, 0);
        Color c5 = new Color(0, 0, 255);
        Color c6 = new Color(255, 0, 255);

        assertEquals(100, c1.distanceTo(c2), eps);
        assertEquals(117.3447, c1.distanceTo(c3), eps);
        assertEquals(148.4745, c1.distanceTo(c4), eps);
        assertEquals(137.6595, c1.distanceTo(c5), eps);
        assertEquals(130.362, c1.distanceTo(c6), eps);
    }
}