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

import com.syncleus.dann.*;
import com.syncleus.dann.activation.ActivationFunction;
import com.syncleus.dann.activation.HyperbolicTangentActivationFunction;
import com.syncleus.grail.graph.GrailGraph;
import com.syncleus.grail.graph.action.*;
import java.util.*;
import org.junit.Assert;
import org.junit.Test;

public class NestedXor2InputTest {
    private static final ActivationFunction ACTIVATION_FUNCTION = new HyperbolicTangentActivationFunction();
    //PrioritySerialTrigger propagateTrigger;

    @Test
    public void testNestedXor() {
        final GrailGraph graph = BlankGraphFactory.makeTinkerGraph();
        
        final NestedLayerVertex biasLayer = graph.addFramedVertex(SimpleNestedLayerVertex.class);
        final NestedLayerVertex inputLayer = graph.addFramedVertex(SimpleNestedLayerVertex.class);
        final NestedLayerVertex hiddenLayer = graph.addFramedVertex(SimpleNestedLayerVertex.class);
        final NestedLayerVertex outputLayer = graph.addFramedVertex(SimpleNestedLayerVertex.class);
        
        biasLayer.setProperty("layer", "bias");
        biasLayer.setNestedGraphName("xorGraph");
        inputLayer.setProperty("layer", "input");
        inputLayer.setNestedGraphName("xorGraph");
        hiddenLayer.setProperty("layer", "hidden");
        hiddenLayer.setNestedGraphName("xorGraph");
        outputLayer.setProperty("layer", "output");
        outputLayer.setNestedGraphName("xorGraph");
        
        biasLayer.setVertexCount(1, AbstractBackpropNeuron.class, AbstractBackpropSynapse.class);
        inputLayer.setVertexCount(2, AbstractBackpropNeuron.class, AbstractBackpropSynapse.class);
        hiddenLayer.setVertexCount(4, AbstractBackpropNeuron.class, AbstractBackpropSynapse.class);
        outputLayer.setVertexCount(1, AbstractBackpropNeuron.class, AbstractBackpropSynapse.class);
        
        graph.addFramedEdge(biasLayer, hiddenLayer, "signals", SimpleNestedFullyConnectedEdge.class).reconnectSubedges(AbstractBackpropSynapse.class);
        graph.addFramedEdge(biasLayer, outputLayer, "signals", SimpleNestedFullyConnectedEdge.class).reconnectSubedges(AbstractBackpropSynapse.class);
        graph.addFramedEdge(inputLayer, hiddenLayer, "signals", SimpleNestedFullyConnectedEdge.class).reconnectSubedges(AbstractBackpropSynapse.class);
        graph.addFramedEdge(hiddenLayer, outputLayer, "signals", SimpleNestedFullyConnectedEdge.class).reconnectSubedges(AbstractBackpropSynapse.class);
        
        biasLayer.makeBias();
        biasLayer.setActivationFunctionClass(HyperbolicTangentActivationFunction.class);
        biasLayer.setLearningRate(0.09);
        inputLayer.setActivationFunctionClass(HyperbolicTangentActivationFunction.class);
        inputLayer.setLearningRate(0.09);
        hiddenLayer.setActivationFunctionClass(HyperbolicTangentActivationFunction.class);
        hiddenLayer.setLearningRate(0.09);
        outputLayer.setActivationFunctionClass(HyperbolicTangentActivationFunction.class);
        outputLayer.setLearningRate(0.09);
        
        final PrioritySerialTrigger propagateTrigger = graph.addFramedVertex(AbstractPrioritySerialTrigger.class);
        propagateTrigger.setProperty("layer", "propagateTrigger");
        PrioritySerialTriggerEdge triggerEdge = graph.addFramedEdge(propagateTrigger, hiddenLayer, "triggers", PrioritySerialTriggerEdge.class);
        triggerEdge.setTriggerAction("propagate");
        triggerEdge.setTriggerPriority(1);
        triggerEdge = graph.addFramedEdge(propagateTrigger, outputLayer, "triggers", PrioritySerialTriggerEdge.class);
        triggerEdge.setTriggerAction("propagate");
        triggerEdge.setTriggerPriority(0);
                
        final PrioritySerialTrigger backpropagateTrigger = graph.addFramedVertex(AbstractPrioritySerialTrigger.class);
        backpropagateTrigger.setProperty("layer", "backpropagateTrigger");
        triggerEdge = graph.addFramedEdge(backpropagateTrigger, biasLayer, "triggers", PrioritySerialTriggerEdge.class);
        triggerEdge.setTriggerAction("backpropagate");
        triggerEdge.setTriggerPriority(1);
        triggerEdge = graph.addFramedEdge(backpropagateTrigger, inputLayer, "triggers", PrioritySerialTriggerEdge.class);
        triggerEdge.setTriggerAction("backpropagate");
        triggerEdge.setTriggerPriority(2);
        triggerEdge = graph.addFramedEdge(backpropagateTrigger, hiddenLayer, "triggers", PrioritySerialTriggerEdge.class);
        triggerEdge.setTriggerAction("backpropagate");
        triggerEdge.setTriggerPriority(3);
        

        /*
        final List<BackpropNeuron> newInputNeurons = new ArrayList<BackpropNeuron>(2);
        newInputNeurons.add(createNeuron(graph, "input"));
        newInputNeurons.add(createNeuron(graph, "input"));
        final List<BackpropNeuron> newHiddenNeurons = new ArrayList<BackpropNeuron>(4);
        newHiddenNeurons.add(createNeuron(graph, "hidden"));
        newHiddenNeurons.add(createNeuron(graph, "hidden"));
        newHiddenNeurons.add(createNeuron(graph, "hidden"));
        newHiddenNeurons.add(createNeuron(graph, "hidden"));
        final BackpropNeuron newOutputNeuron = createNeuron(graph, "output");
        newOutputNeuron.setActivationFunctionClass(HyperbolicTangentActivationFunction.class);
        newOutputNeuron.setLearningRate(0.09);
        final BackpropNeuron biasNeuron = createNeuron(graph, "bias");
        biasNeuron.setSignal(1.0);
        biasNeuron.setActivationFunctionClass(HyperbolicTangentActivationFunction.class);
        biasNeuron.setLearningRate(0.09);

        //connect all input neurons to hidden neurons
        for( BackpropNeuron inputNeuron : newInputNeurons ) {
            //make sure all input neurons use tanH activation function
            inputNeuron.setActivationFunctionClass(HyperbolicTangentActivationFunction.class);
            inputNeuron.setLearningRate(0.09);
            for( BackpropNeuron hiddenNeuron : newHiddenNeurons ) {
                graph.addFramedEdge(inputNeuron, hiddenNeuron, "signals", AbstractBackpropSynapse.class);
            }
        }
        //connect all hidden neurons to the output neuron
        for( BackpropNeuron hiddenNeuron : newHiddenNeurons ) {
            graph.addFramedEdge(hiddenNeuron, newOutputNeuron, "signals", AbstractBackpropSynapse.class);

            //all hidden neurons shoudl use tanh activation function
            hiddenNeuron.setActivationFunctionClass(HyperbolicTangentActivationFunction.class);
            hiddenNeuron.setLearningRate(0.09);

            //create bias neuron
            graph.addFramedEdge( biasNeuron, hiddenNeuron, "signals", AbstractBackpropSynapse.class);
        }
        //create bias neuron for output neuron
        graph.addFramedEdge( biasNeuron, newOutputNeuron, "signals", AbstractBackpropSynapse.class);
        graph.commit();
              */

        for(int i = 0; i < 1500; i++) {
            train(graph, -1.0, 1.0, 1.0);
            train(graph, 1.0, -1.0, 1.0);
            train(graph, 1.0, 1.0, -1.0);
            train(graph, -1.0, -1.0, -1.0);
            if( i%50 == 0 && calculateError(graph) < 0.1 )
                break;
        }
        Assert.assertTrue(propagate(graph, 1.0, 1.0) < 0.0);
        Assert.assertTrue(propagate(graph, -1.0, -1.0) < 0.0);
        Assert.assertTrue(propagate(graph, 1.0, -1.0) > 0.0);
        Assert.assertTrue(propagate(graph, -1.0, 1.0) > 0.0);
    }

    private double calculateError(GrailGraph graph) {
        double actual = propagate(graph, 1.0, 1.0);
        double error = Math.abs(actual + 1.0) / 2.0;

        actual = propagate(graph, -1.0, -1.0);
        error += Math.abs(actual + 1.0) / 2.0;

        actual = propagate(graph, 1.0, -1.0);
        error += Math.abs(actual - 1.0) / 2.0;

        actual = propagate(graph, -1.0, 1.0);
        error += Math.abs(actual - 1.0) / 2.0;

        return error/4.0;
    }

    private void train(final GrailGraph graph, final double input1, final double input2, final double expected) {
        propagate(graph, input1, input2);

        final NestedLayerVertex outputLayer = graph.getFramedVertices("layer", "output", SimpleNestedLayerVertex.class).iterator().next();
        final Iterator<? extends BackpropNeuron> outputNeurons = outputLayer.getNestedNeurons().iterator();
        final BackpropNeuron outputNeuron = outputNeurons.next();
        Assert.assertTrue(!outputNeurons.hasNext());
        outputNeuron.setDeltaTrain((expected - outputNeuron.getSignal()) * ACTIVATION_FUNCTION.activateDerivative(outputNeuron.getActivity()));
        graph.commit();
        
        final ActionTrigger trigger = graph.getFramedVertices("layer", "backpropagateTrigger", AbstractPrioritySerialTrigger.class).iterator().next();
        trigger.trigger();
    }

    private double propagate(final GrailGraph graph, final double input1, final double input2) {
        final NestedLayerVertex inputLayer = graph.getFramedVertices("layer", "input", SimpleNestedLayerVertex.class).iterator().next();
        final Iterator<? extends BackpropNeuron> inputNeurons = inputLayer.getNestedNeurons().iterator();
        inputNeurons.next().setSignal(input1);
        inputNeurons.next().setSignal(input2);
        Assert.assertTrue(!inputNeurons.hasNext());
        graph.commit();
        
        final ActionTrigger trigger = graph.v().has("layer", "propagateTrigger").frame(AbstractPrioritySerialTrigger.class).iterator().next();
        trigger.trigger();

        final NestedLayerVertex outputLayer = graph.getFramedVertices("layer", "output", SimpleNestedLayerVertex.class).iterator().next();
        final Iterator<? extends BackpropNeuron> outputNeurons  = outputLayer.getNestedNeurons().iterator();
        final BackpropNeuron outputNeuron = outputNeurons.next();
        Assert.assertTrue(!outputNeurons.hasNext());
        outputNeuron.propagate();
        graph.commit();
        
        return outputNeuron.getSignal();
    }
}
