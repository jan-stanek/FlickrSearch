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
        assertEquals(50.4067, c1.distanceTo(c3), eps);
        assertEquals(87.8676, c1.distanceTo(c4), eps);
        assertEquals(39.6845, c1.distanceTo(c5), eps);
        assertEquals(56.7104, c1.distanceTo(c6), eps);

        Color c7 = new Color(44, 237, 13);
        Color c8 = new Color(42, 196, 67);

        assertEquals(11.0456, c7.distanceTo(c8), eps);
    }
}