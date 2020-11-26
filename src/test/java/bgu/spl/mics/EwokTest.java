package bgu.spl.mics;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import bgu.spl.mics.application.passiveObjects.Ewok;

import static org.junit.jupiter.api.Assertions.*;

//Done
class EwokTest {

    private Ewok E;

    @BeforeEach
    void setUp() {
        E = new Ewok(0);
    }

    @AfterEach
    void tearDown() {
        E = null;
    }

    @Test
    void acquire() {
        assertTrue(E.getAvailable());
        E.acquire();
        assertFalse(E.getAvailable());
    }

    @Test
    void release() {
        assertFalse(E.getAvailable());
        E.release();
        assertTrue(E.getAvailable());
    }

}