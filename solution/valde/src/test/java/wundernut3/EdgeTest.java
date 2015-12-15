package wundernut3;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static wundernut3.Edge.*;

public class EdgeTest {
    @Test
    public void edgesWithDifferentPartsOfSameAnimalMatch() {
        assertTrue(FoxHead.matches(FoxTorso));
    }

    @Test
    public void edgesWithDifferentPartsOfDifferentAnimalsDontMatch() {
        assertFalse(FoxHead.matches(DeerTorso));
    }

    @Test
    public void edgesWithSamePartsOfSameAnimalDontMatch() {
        assertFalse(FoxHead.matches(FoxHead));
    }
}
