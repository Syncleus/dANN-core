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
package com.syncleus.tests.dann.neural.realtime;

import com.syncleus.dann.neural.realtime.AbstractRealtimeBrain;
import com.syncleus.dann.neural.realtime.RealtimeNeuron;
import com.syncleus.dann.neural.realtime.neuron.IzhikevichNeuron;
import com.syncleus.dann.neural.realtime.synapse.ShortTermPlasticitySynapse;
import java.util.LinkedList;
import java.util.List;
import org.junit.*;

public class TestRealtimeNetwork {

    @Test
    public void testAccessors() {
        final AbstractRealtimeBrain brain = new AbstractRealtimeBrain();

        int numNeurons = 64;
        double synapseProbability = 0.1;
        int cycles = 128;
        double cycleDT = 0.01;


        for (int n = 0; n < numNeurons; n++) {
            brain.addNeuron(new IzhikevichNeuron());
        }

        List<RealtimeNeuron> existingNodes = new LinkedList(brain.getNodes());
        for (int a = 0; a < numNeurons; a++) {
            for (int b = 0; b < numNeurons; b++) {
                if (a != b) {
                    if (Math.random() < synapseProbability) {
                        RealtimeNeuron s = existingNodes.get(a);
                        RealtimeNeuron t = existingNodes.get(b);
                        brain.addSynapse(new ShortTermPlasticitySynapse(s, t));
                    }
                }
            }
        }

        for (int i = 0; i < cycles; i++) {
            brain.update(cycleDT);
        }



//		testSynapse.setInput(TEST_INPUT);
//		Assert.assertTrue(Math.abs(testSynapse.getInput() - TEST_INPUT) < 0.000001);
//		testSynapse.setWeight(TEST_WEIGHT);
//		Assert.assertTrue(Math.abs(testSynapse.getWeight() - TEST_WEIGHT) < 0.000001);
//		Assert.assertTrue(testSynapse.getSourceNode() == sourceNeuron);
//		Assert.assertTrue(testSynapse.getDestinationNode() == destinationNeuron);

    }
}
