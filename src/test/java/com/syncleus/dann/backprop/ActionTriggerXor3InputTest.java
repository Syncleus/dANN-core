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

import com.syncleus.grail.graph.unit.action.SerialPriorityTrigger;
import com.syncleus.grail.graph.unit.action.PrioritySerialTriggerEdge;
import com.syncleus.grail.graph.unit.action.AbstractPriorityTrigger;
import com.syncleus.dann.BlankGraphFactory;
import com.syncleus.dann.activation.*;
import com.syncleus.grail.graph.GrailGraph;
import org.junit.*;

import java.lang.reflect.UndeclaredThrowableException;
import java.util.*;

public class ActionTriggerXor3InputTest {

    private static final ActivationFunction ACTIVATION_FUNCTION = new HyperbolicTangentActivationFunction();

    @Test
    public void testXor() {
        final GrailGraph graph = BlankGraphFactory.makeTinkerGraph();

        //
        //Construct the Neural Graph
        //
        final List<BackpropNeuron> newInputNeurons = new ArrayList<>(2);
        newInputNeurons.add(ActionTriggerXor3InputTest.createNeuron(graph, "input"));
        newInputNeurons.add(ActionTriggerXor3InputTest.createNeuron(graph, "input"));
        newInputNeurons.add(ActionTriggerXor3InputTest.createNeuron(graph, "input"));
        final List<BackpropNeuron> newHiddenNeurons = new ArrayList<>(4);
        newHiddenNeurons.add(ActionTriggerXor3InputTest.createNeuron(graph, "hidden"));
        newHiddenNeurons.add(ActionTriggerXor3InputTest.createNeuron(graph, "hidden"));
        newHiddenNeurons.add(ActionTriggerXor3InputTest.createNeuron(graph, "hidden"));
        final BackpropNeuron newOutputNeuron = ActionTriggerXor3InputTest.createNeuron(graph, "output");
        final BackpropNeuron biasNeuron = ActionTriggerXor3InputTest.createNeuron(graph, "bias");
        biasNeuron.setSignal(1.0);

        //connect all input neurons to hidden neurons
        for (final BackpropNeuron inputNeuron : newInputNeurons) {
            for (final BackpropNeuron hiddenNeuron : newHiddenNeurons) {
                graph.addFramedEdge(inputNeuron, hiddenNeuron, "signals", BackpropSynapse.class);
            }
        }
        //connect all hidden neurons to the output neuron
        for (final BackpropNeuron hiddenNeuron : newHiddenNeurons) {
            graph.addFramedEdge(hiddenNeuron, newOutputNeuron, "signals", BackpropSynapse.class);

            //create bias neuron
            graph.addFramedEdge(biasNeuron, hiddenNeuron, "signals", BackpropSynapse.class);
        }
        //create bias neuron for output neuron
        graph.addFramedEdge(biasNeuron, newOutputNeuron, "signals", BackpropSynapse.class);

        //
        //Construct the Action Triggers for the neural Graph
        //
        //First lets handle the output layer for propagation
        final AbstractPriorityTrigger propagateOutputTrigger = ActionTriggerXor3InputTest.createPrioritySerialTrigger(graph);
        //connect it to the output neuron with a priority of 0 (highest priority)
        final PrioritySerialTriggerEdge outputTriggerEdge = graph.addFramedEdge(propagateOutputTrigger, newOutputNeuron, "triggers", PrioritySerialTriggerEdge.class);
        outputTriggerEdge.setTriggerPriority(1000);
        outputTriggerEdge.setTriggerAction("propagate");

        //now lets handle the hidden layer for propagation
        final AbstractPriorityTrigger propagateHiddenTrigger = ActionTriggerXor3InputTest.createPrioritySerialTrigger(graph);
        propagateHiddenTrigger.setProperty("triggerPointer", "propagate");
        //connect it to each of the hidden neurons with a priority of 0 (highest priority)
        for (final BackpropNeuron hiddenNeuron : newHiddenNeurons) {
            final PrioritySerialTriggerEdge newEdge = graph.addFramedEdge(propagateHiddenTrigger, hiddenNeuron, "triggers", PrioritySerialTriggerEdge.class);
            newEdge.setTriggerPriority(1000);
            newEdge.setTriggerAction("propagate");
        }

        //chain the prop[agation of the hidden layer to the propagation of the output layer, but make sure it has less of a priority than the other triggers
        final PrioritySerialTriggerEdge chainTriggerPropagateEdge = graph.addFramedEdge(propagateHiddenTrigger, propagateOutputTrigger, "triggers", PrioritySerialTriggerEdge.class);
        chainTriggerPropagateEdge.setTriggerPriority(0);
        chainTriggerPropagateEdge.setTriggerAction("actionTrigger");

        //next lets handle the input layer for back propagation
        final AbstractPriorityTrigger backpropInputTrigger = ActionTriggerXor3InputTest.createPrioritySerialTrigger(graph);
        //connect it to each of the input neurons
        for (final BackpropNeuron inputNeuron : newInputNeurons) {
            final PrioritySerialTriggerEdge newEdge = graph.addFramedEdge(backpropInputTrigger, inputNeuron, "triggers", PrioritySerialTriggerEdge.class);
            newEdge.setTriggerPriority(1000);
            newEdge.setTriggerAction("backpropagate");
        }
        //also connect it to all the bias neurons
        final PrioritySerialTriggerEdge biasTriggerBackpropEdge = graph.addFramedEdge(backpropInputTrigger, biasNeuron, "triggers", PrioritySerialTriggerEdge.class);
        biasTriggerBackpropEdge.setTriggerPriority(1000);
        biasTriggerBackpropEdge.setTriggerAction("backpropagate");

        //create backpropagation trigger for the hidden layer
        final AbstractPriorityTrigger backpropHiddenTrigger = ActionTriggerXor3InputTest.createPrioritySerialTrigger(graph);
        backpropHiddenTrigger.setProperty("triggerPointer", "backpropagate");
        //connect it to each of the hidden neurons with a priority of 0 (highest priority)
        for (final BackpropNeuron hiddenNeuron : newHiddenNeurons) {
            final PrioritySerialTriggerEdge newEdge = graph.addFramedEdge(backpropHiddenTrigger, hiddenNeuron, "triggers", PrioritySerialTriggerEdge.class);
            newEdge.setTriggerPriority(1000);
            newEdge.setTriggerAction("backpropagate");
        }

        //chain the hidden layers back propagation to the input layers trigger
        final PrioritySerialTriggerEdge chainTriggerBackpropEdge = graph.addFramedEdge(backpropHiddenTrigger, backpropInputTrigger, "triggers", PrioritySerialTriggerEdge.class);
        chainTriggerBackpropEdge.setTriggerPriority(0);
        chainTriggerBackpropEdge.setTriggerAction("actionTrigger");

        //
        // Graph is constructed, just need to train and test our network now.
        //
        final int maxCycles = 10000;
        final int completionPeriod = 50;
        final double maxError = 0.75;
        for (int cycle = maxCycles; cycle >= 0; cycle--) {
            int finished = 0;
            for (int in1 = -1; in1 <= 1; in1 += 2) {
                for (int in2 = -1; in2 <= 1; in2 += 2) {
                    for (int in3 = -1; in3 <= 1; in3 += 2) {
                        boolean bi = in1 >= 0;
                        boolean bj = in2 >= 0;
                        boolean bk = in3 >= 0;
                        boolean expect = bi ^ bj ^ bk;
                        double expectD = expect ? 1.0 : -1.0;

                        train(graph, in1, in2, in3, expectD);

                        if (cycle % completionPeriod == 0 && calculateError(graph, in1, in2, in3, expectD) < maxError) {
                            finished++;
                        }
                    }
                }
            }
            if (finished == 8)
                break;
        }

        Assert.assertTrue(ActionTriggerXor3InputTest.propagate(graph, 1.0, 1.0, 1.0) > 0.0);
        Assert.assertTrue(ActionTriggerXor3InputTest.propagate(graph, -1.0, 1.0, 1.0) < 0.0);
        Assert.assertTrue(ActionTriggerXor3InputTest.propagate(graph, 1.0, -1.0, 1.0) < 0.0);
        Assert.assertTrue(ActionTriggerXor3InputTest.propagate(graph, 1.0, 1.0, -1.0) < 0.0);
        Assert.assertTrue(ActionTriggerXor3InputTest.propagate(graph, -1.0, -1.0, 1.0) > 0.0);
        Assert.assertTrue(ActionTriggerXor3InputTest.propagate(graph, -1.0, 1.0, -1.0) > 0.0);
        Assert.assertTrue(ActionTriggerXor3InputTest.propagate(graph, 1.0, -1.0, -1.0) > 0.0);
        Assert.assertTrue(ActionTriggerXor3InputTest.propagate(graph, -1.0, -1.0, -1.0) < 0.0);
    }

    private static double calculateError(GrailGraph graph, double in1, double in2, double in3, double expect) {
        double actual = ActionTriggerXor3InputTest.propagate(graph, in1, in2, in3);
        return Math.abs(actual - expect) / Math.abs(expect);
    }

    private static void train(final GrailGraph graph, final double input1, final double input2, final double input3, final double expected) {
        ActionTriggerXor3InputTest.propagate(graph, input1, input2, input3);

        final Iterator<? extends AbstractBackpropNeuron> outputNeurons = graph.getFramedVertices("layer", "output", AbstractBackpropNeuron.class).iterator();
        final BackpropNeuron outputNeuron = outputNeurons.next();
        Assert.assertTrue(!outputNeurons.hasNext());
        outputNeuron.setDeltaTrain((expected - outputNeuron.getSignal()) * ACTIVATION_FUNCTION.activateDerivative(outputNeuron.getActivity()));

        final Iterator<? extends AbstractPriorityTrigger> backpropTriggers = graph.getFramedVertices("triggerPointer", "backpropagate", AbstractPriorityTrigger.class).iterator();
        final AbstractPriorityTrigger backpropTrigger = backpropTriggers.next();
        Assert.assertTrue(!backpropTriggers.hasNext());
        backpropTrigger.trigger();
    }

    private static double propagate(final GrailGraph graph, final double input1, final double input2, final double input3) {
        final Iterator<? extends BackpropNeuron> inputNeurons = graph.getFramedVertices("layer", "input", BackpropNeuron.class).iterator();
        inputNeurons.next().setSignal(input1);
        inputNeurons.next().setSignal(input2);
        inputNeurons.next().setSignal(input3);
        Assert.assertTrue(!inputNeurons.hasNext());

        final Iterator<? extends SerialPriorityTrigger> propagateTriggers = graph.getFramedVertices("triggerPointer", "propagate", SerialPriorityTrigger.class).iterator();
        final AbstractPriorityTrigger propagateTrigger = propagateTriggers.next();
        Assert.assertTrue(!propagateTriggers.hasNext());
        try {
            propagateTrigger.trigger();
        } catch (final UndeclaredThrowableException caught) {
            caught.getUndeclaredThrowable().printStackTrace();
            throw caught;
        }

        final Iterator<? extends AbstractBackpropNeuron> outputNeurons = graph.getFramedVertices("layer", "output", AbstractBackpropNeuron.class).iterator();
        final BackpropNeuron outputNeuron = outputNeurons.next();
        Assert.assertTrue(!outputNeurons.hasNext());
        return outputNeuron.getSignal();
    }

    private static BackpropNeuron createNeuron(final GrailGraph graph, final String layer) {
        final BackpropNeuron neuron = graph.addFramedVertex(AbstractBackpropNeuron.class);
        neuron.setProperty("layer", layer);
        return neuron;
    }

    private static AbstractPriorityTrigger createPrioritySerialTrigger(final GrailGraph graph) {
        return graph.addFramedVertex(SerialPriorityTrigger.class);
    }
}
