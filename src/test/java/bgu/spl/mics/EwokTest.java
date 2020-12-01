//package bgu.spl.mics;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import bgu.spl.mics.application.passiveObjects.Ewok;
//import static org.junit.jupiter.api.Assertions.*;
//
//
//class EwokTest {
//
//    private Ewok E;
//
//    @BeforeEach
//    void setUp() {
//        E = new Ewok(0);
//    }
//
//    @Test
//    void acquire() {
//        assertTrue(E.getAvailable());
//        E.acquire();
//        assertFalse(E.getAvailable());
//    }
//
//    @Test
//    void release() {
//        E.acquire(); // the field is true in the beginning
//        assertFalse(E.getAvailable());
//        E.release();
//        assertTrue(E.getAvailable());
//    }
//
//}