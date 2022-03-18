package org.dumbo.engine;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class DummyTest {

    @Before
    public void setUp() {
        System.out.println("Running setUp...");
    }

    @Test
    public void dummyTest() {
        assertTrue(true);
    }

    @After
    public void tearDown() {
        System.out.println("Running tearDown...");
    }
}

