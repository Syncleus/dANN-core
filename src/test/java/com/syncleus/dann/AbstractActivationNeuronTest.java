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
package com.syncleus.dann;

import com.syncleus.dann.activation.*;
import com.tinkerpop.frames.FramedTransactionalGraph;
import junit.framework.Assert;
import org.junit.Test;

import java.lang.reflect.*;

public class AbstractActivationNeuronTest {

    @Test( expected = UndeclaredThrowableException.class )
    public void testBadAccessActivation() {
        final FramedTransactionalGraph<?> graph = BlankGraphFactory.makeTinkerGraph();
        final ActivationNeuron neuron = graph.addVertex(null, ActivationNeuron.class);
        neuron.setActivationFunctionClass(BadAccessActivationFunction.class);
        try {
            neuron.propagate();
        }
        catch( final UndeclaredThrowableException caught ) {
            Assert.assertTrue(InvocationTargetException.class.equals(caught.getUndeclaredThrowable().getClass()));
            throw caught;
        }
    }

    @Test( expected = UndeclaredThrowableException.class )
    public void testNoDefaultConstructorActivation() {
        final FramedTransactionalGraph<?> graph = BlankGraphFactory.makeTinkerGraph();
        final ActivationNeuron neuron = graph.addVertex(null, ActivationNeuron.class);
        neuron.setActivationFunctionClass(NoDefaultConstructorActivationFunction.class);
        try {
            neuron.propagate();
        }
        catch( final UndeclaredThrowableException caught ) {
            Assert.assertTrue(InvocationTargetException.class.equals(caught.getUndeclaredThrowable().getClass()));
            throw caught;
        }
    }

    @Test
    public void testPropagateTwice() {
        final FramedTransactionalGraph<?> graph = BlankGraphFactory.makeTinkerGraph();
        final ActivationNeuron neuron = graph.addVertex(null, ActivationNeuron.class);
        neuron.setActivationFunctionClass(HyperbolicTangentActivationFunction.class);
        neuron.propagate();
        neuron.propagate();
        Assert.assertEquals(HyperbolicTangentActivationFunction.class, neuron.getActivationFunctionClass());
    }
}
