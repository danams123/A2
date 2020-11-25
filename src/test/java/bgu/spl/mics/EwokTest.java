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
        try {
            assertTrue(E.getAvailable());
        }
        catch(AssertionError e){
            System.out.println(e);
        }
        E.acquire();
        try {
            assertFalse(E.getAvailable());
        }
        catch(AssertionError e){
            System.out.println(e);
        }
    }

    @Test
    void release() {
        try {
            assertFalse(E.getAvailable());
        }
        catch(AssertionError e){
            System.out.println(e);
        }
        E.release();
        try {
            assertTrue(E.getAvailable());
        }
        catch(AssertionError e){
            System.out.println(e);
        }
    }
}