package main.java.bgu.spl.mics.application.passiveObjects;

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
            asserFalse(E.getAvailable());
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
            asserTrue(E.getAvailable());
        }
        catch(AssertionError e){
            System.out.println(e);
        }
    }
    }
}