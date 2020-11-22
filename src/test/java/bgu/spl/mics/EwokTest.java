package bgu.spl.mics;

import bgu.spl.mics.application.passiveObjects.Ewok;

import static org.junit.jupiter.api.Assertions.*;

//Done
class EwokTest {

    private Ewok E;

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        E = new Ewok(0);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() {
        E = null;
    }

    @org.junit.jupiter.api.Test
    void acquire() {
        assertTrue(E.getAvailable());
        E.acquire();
        try {
            assertFalse(E.getAvailable());
        }
        catch(AssertionError e){
            System.out.println(e);
        }
    }

    @org.junit.jupiter.api.Test
    void release() {
        assertFalse(E.getAvailable());
        E.release();
        try {
            assertTrue(E.getAvailable());
        }
        catch(AssertionError e){
            System.out.println(e);
        }
    }
}