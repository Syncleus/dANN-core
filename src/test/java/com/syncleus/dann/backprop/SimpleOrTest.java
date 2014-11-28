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
package com.syncleus.dann.backprop;

import com.syncleus.dann.activation.*;
import com.syncleus.dann.BlankGraphFactory;
import com.syncleus.ferma.FramedVertex;
import com.syncleus.grail.graph.GrailGraph;
import org.junit.*;
import java.util.*;

public class SimpleOrTest {

    private static final ActivationFunction ACTIVATION_FUNCTION = new SineActivationFunction();

    @Test
    public void testOr() {
        final GrailGraph graph = BlankGraphFactory.makeTinkerGraph();

        final List<BackpropNeuron> newInputNeurons = new ArrayList<BackpropNeuron>(2);
        newInputNeurons.add(SimpleOrTest.createNeuron(graph, "input"));
        newInputNeurons.add(SimpleOrTest.createNeuron(graph, "input"));
        final BackpropNeuron newOutputNeuron = SimpleOrTest.createNeuron(graph, "output");

        //connect all hidden neurons to the output neuron
        for( BackpropNeuron inputNeuron : newInputNeurons ) {
            graph.addFramedEdge((FramedVertex) inputNeuron, (FramedVertex) newOutputNeuron, "signals", AbstractBackpropSynapse.class);//.asEdge().setProperty("type", "BackpropSynapse");
        }
        //create bias neuron for output neuron
        final BackpropNeuron biasNeuron = SimpleOrTest.createNeuron(graph, "bias");
        biasNeuron.setSignal(1.0);
        graph.addFramedEdge((FramedVertex) biasNeuron, (FramedVertex) newOutputNeuron, "signals", AbstractBackpropSynapse.class);//.asEdge().setProperty("type", "BackpropSynapse");
        graph.commit();

        for(int i = 0; i < 10000; i++) {
            SimpleOrTest.train(graph, -1.0, 1.0, 1.0);
            SimpleOrTest.train(graph, 1.0, -1.0, 1.0);
            SimpleOrTest.train(graph, 1.0, 1.0, 1.0);
            SimpleOrTest.train(graph, -1.0, -1.0, -1.0);
            if( i%50 == 0 && SimpleOrTest.calculateError(graph) < 0.2 )
                break;
        }

        Assert.assertTrue("expected >0.0, got: " + SimpleOrTest.propagate(graph, 1.0, 1.0), SimpleOrTest.propagate(graph, 1.0, 1.0) > 0.0);
        Assert.assertTrue("expected <0.0, got: " + SimpleOrTest.propagate(graph, -1.0, -1.0), SimpleOrTest.propagate(graph, -1.0, -1.0) < 0.0);
        Assert.assertTrue("expected >0.0, got: " + SimpleOrTest.propagate(graph, 1.0, -1.0), SimpleOrTest.propagate(graph, 1.0, -1.0) > 0.0);
        Assert.assertTrue("expected >0.0, got: " + SimpleOrTest.propagate(graph, -1.0, 1.0), SimpleOrTest.propagate(graph, -1.0, 1.0) > 0.0);
    }

    private static double calculateError(GrailGraph graph) {
        double actual = SimpleOrTest.propagate(graph, 1.0, 1.0);
        double error = Math.abs(actual - 1.0) / 2.0;

        actual = SimpleOrTest.propagate(graph, -1.0, -1.0);
        error += Math.abs(actual + 1.0) / 2.0;

        actual = SimpleOrTest.propagate(graph, 1.0, -1.0);
        error += Math.abs(actual - 1.0) / 2.0;

        actual = SimpleOrTest.propagate(graph, -1.0, 1.0);
        error += Math.abs(actual - 1.0) / 2.0;

        return error/4.0;
    }

    private static void train(final GrailGraph graph, final double input1, final double input2, final double expected) {
        SimpleOrTest.propagate(graph, input1, input2);

        final Iterator<AbstractBackpropNeuron> outputNeurons = graph.getFramedVertices("layer", "output", AbstractBackpropNeuron.class).iterator();
        final BackpropNeuron outputNeuron = outputNeurons.next();
        Assert.assertTrue(!outputNeurons.hasNext());
        outputNeuron.setDeltaTrain((expected - outputNeuron.getSignal()) * ACTIVATION_FUNCTION.activateDerivative(outputNeuron.getActivity()));
        graph.commit();

        final Iterator<AbstractBackpropNeuron> inputNeurons = graph.getFramedVertices("layer", "input", AbstractBackpropNeuron.class).iterator();
        inputNeurons.next().backpropagate();
        inputNeurons.next().backpropagate();
        Assert.assertTrue(!inputNeurons.hasNext());
        graph.commit();

        final Iterator<AbstractBackpropNeuron> biasNeurons = graph.getFramedVertices("layer", "bias", AbstractBackpropNeuron.class).iterator();
        biasNeurons.next().backpropagate();
        Assert.assertTrue(!biasNeurons.hasNext());
        graph.commit();
    }

    private static double propagate(final GrailGraph graph, final double input1, final double input2) {
        final Iterator<AbstractBackpropNeuron> inputNeurons = graph.getFramedVertices("layer", "input", AbstractBackpropNeuron.class).iterator();
        inputNeurons.next().setSignal(input1);
        inputNeurons.next().setSignal(input2);
        Assert.assertTrue(!inputNeurons.hasNext());
        graph.commit();

        final Iterator<AbstractBackpropNeuron> outputNeurons = graph.getFramedVertices("layer", "output", AbstractBackpropNeuron.class).iterator();
        final BackpropNeuron outputNeuron = outputNeurons.next();
        Assert.assertTrue(!outputNeurons.hasNext());
        outputNeuron.propagate();
        graph.commit();
        return outputNeuron.getSignal();
    }

    private static BackpropNeuron createNeuron(final GrailGraph graph, final String layer) {
        final BackpropNeuron neuron = graph.addFramedVertex(AbstractBackpropNeuron.class);
        ((FramedVertex)neuron).setProperty("layer", layer);
        return neuron;
    }
}
