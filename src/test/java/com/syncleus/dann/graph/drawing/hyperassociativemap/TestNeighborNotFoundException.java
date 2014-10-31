/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Syncleus, Inc.                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Syncleus, Inc. at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Syncleus, Inc. at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Syncleus, Inc.                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.dann.graph.drawing.hyperassociativemap;

import org.junit.Test;

public class TestNeighborNotFoundException {
    @Test(expected = NeighborNotFoundException.class)
    public void testDefault() throws NeighborNotFoundException {
        throw new NeighborNotFoundException();
    }

    @Test(expected = NeighborNotFoundException.class)
    public void testString() throws NeighborNotFoundException {
        throw new NeighborNotFoundException("This is just a test");
    }

    @Test(expected = NeighborNotFoundException.class)
    public void testCause() throws NeighborNotFoundException {
        throw new NeighborNotFoundException(new Exception());
    }

    @Test(expected = NeighborNotFoundException.class)
    public void testStringCause() throws NeighborNotFoundException {
        throw new NeighborNotFoundException("This is just a test", new Exception());
    }
}
