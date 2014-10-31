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
package com.syncleus.dann.graphicalmodel.markovrandomfield;

import com.syncleus.dann.graph.*;
import com.syncleus.dann.graphicalmodel.*;
import org.junit.*;

import java.util.*;

public class TestSimpleMarkovRandomField {
    @Test
    public void testDependentNode() {
        final MutableMarkovRandomFieldAdjacencyGraph network = new MutableMarkovRandomFieldAdjacencyGraph();
        final GraphicalModelNode<SimpleEnum> parentNode = new SimpleGraphicalModelNode<SimpleEnum>(SimpleEnum.TRUE);
        final GraphicalModelNode<SimpleEnum> childNode = new SimpleGraphicalModelNode<SimpleEnum>(SimpleEnum.TRUE);

        network.add(parentNode);
        network.add(childNode);

        final UndirectedEdge<GraphicalModelNode> testEdge = new ImmutableUndirectedEdge<GraphicalModelNode>(parentNode, childNode);
        network.add(testEdge);

        parentNode.setState(SimpleEnum.TRUE);
        childNode.setState(SimpleEnum.FALSE);
        network.learnStates();
        network.learnStates();
        network.learnStates();
        childNode.setState(SimpleEnum.TRUE);
        network.learnStates();

        parentNode.setState(SimpleEnum.FALSE);
        childNode.setState(SimpleEnum.TRUE);
        network.learnStates();
        network.learnStates();
        network.learnStates();
        childNode.setState(SimpleEnum.FALSE);
        network.learnStates();

        final Set<GraphicalModelNode> goals = new HashSet<GraphicalModelNode>();
        goals.add(childNode);
        final Set<GraphicalModelNode> influences = new HashSet<GraphicalModelNode>();
        influences.add(parentNode);

        parentNode.setState(SimpleEnum.TRUE);
        childNode.setState(SimpleEnum.TRUE);
        double probability = network.conditionalProbability(goals, influences);
        Assert.assertTrue("bad state probability (TRUE,TRUE)! conditionalProbability: " + probability + " jointProbability: " + network.jointProbability() + " child probability: " + childNode.stateProbability() + " parent probability: " + parentNode.stateProbability(), Math.abs(probability - 0.25) < 0.0001);

        parentNode.setState(SimpleEnum.TRUE);
        childNode.setState(SimpleEnum.FALSE);
        probability = network.conditionalProbability(goals, influences);
        Assert.assertTrue("bad state probability (TRUE,FALSE)! conditionalProbability: " + probability + " jointProbability: " + network.jointProbability() + " child probability: " + childNode.stateProbability() + " parent probability: " + parentNode.stateProbability(), Math.abs(probability - 0.75) < 0.0001);

        parentNode.setState(SimpleEnum.FALSE);
        childNode.setState(SimpleEnum.TRUE);
        probability = network.conditionalProbability(goals, influences);
        Assert.assertTrue("bad state probability (FALSE,TRUE)! conditionalProbability: " + probability + " jointProbability: " + network.jointProbability() + " child probability: " + childNode.stateProbability() + " parent probability: " + parentNode.stateProbability(), Math.abs(probability - 0.75) < 0.0001);

        parentNode.setState(SimpleEnum.FALSE);
        childNode.setState(SimpleEnum.FALSE);
        probability = network.conditionalProbability(goals, influences);
        Assert.assertTrue("bad state probability (FALSE,FALSE)! conditionalProbability: " + probability + " jointProbability: " + network.jointProbability() + " child probability: " + childNode.stateProbability() + " parent probability: " + parentNode.stateProbability(), Math.abs(probability - 0.25) < 0.0001);
    }

    @Test
    public void testDependentNodeAsymmetrical() {
        final MutableMarkovRandomFieldAdjacencyGraph network = new MutableMarkovRandomFieldAdjacencyGraph();
        final GraphicalModelNode<SimpleEnum> parentNode = new SimpleGraphicalModelNode<SimpleEnum>(SimpleEnum.TRUE);
        final GraphicalModelNode<SimpleEnum> childNode = new SimpleGraphicalModelNode<SimpleEnum>(SimpleEnum.TRUE);

        network.add(parentNode);
        network.add(childNode);

        final UndirectedEdge<GraphicalModelNode> testEdge = new ImmutableUndirectedEdge<GraphicalModelNode>(parentNode, childNode);
        network.add(testEdge);

        parentNode.setState(SimpleEnum.TRUE);
        childNode.setState(SimpleEnum.FALSE);
        network.learnStates();
        network.learnStates();
        network.learnStates();
        childNode.setState(SimpleEnum.TRUE);
        network.learnStates();

        parentNode.setState(SimpleEnum.FALSE);
        childNode.setState(SimpleEnum.TRUE);
        network.learnStates();
        network.learnStates();
        childNode.setState(SimpleEnum.FALSE);
        network.learnStates();
        network.learnStates();

        final Set<GraphicalModelNode> goals = new HashSet<GraphicalModelNode>();
        goals.add(childNode);
        final Set<GraphicalModelNode> influences = new HashSet<GraphicalModelNode>();
        influences.add(parentNode);

        parentNode.setState(SimpleEnum.TRUE);
        childNode.setState(SimpleEnum.TRUE);
        double probability = network.conditionalProbability(goals, influences);
        Assert.assertTrue("bad state probability (TRUE,TRUE)! conditionalProbability: " + probability + " jointProbability: " + network.jointProbability() + " child probability: " + childNode.stateProbability() + " parent probability: " + parentNode.stateProbability(), Math.abs(probability - 0.25) < 0.0001);

        parentNode.setState(SimpleEnum.TRUE);
        childNode.setState(SimpleEnum.FALSE);
        probability = network.conditionalProbability(goals, influences);
        Assert.assertTrue("bad state probability (TRUE,FALSE)! conditionalProbability: " + probability + " jointProbability: " + network.jointProbability() + " child probability: " + childNode.stateProbability() + " parent probability: " + parentNode.stateProbability(), Math.abs(probability - 0.75) < 0.0001);

        parentNode.setState(SimpleEnum.FALSE);
        childNode.setState(SimpleEnum.TRUE);
        probability = network.conditionalProbability(goals, influences);
        Assert.assertTrue("bad state probability (FALSE,TRUE)! conditionalProbability: " + probability + " jointProbability: " + network.jointProbability() + " child probability: " + childNode.stateProbability() + " parent probability: " + parentNode.stateProbability(), Math.abs(probability - 0.5) < 0.0001);

        parentNode.setState(SimpleEnum.FALSE);
        childNode.setState(SimpleEnum.FALSE);
        probability = network.conditionalProbability(goals, influences);
        Assert.assertTrue("bad state probability (FALSE,FALSE)! conditionalProbability: " + probability + " jointProbability: " + network.jointProbability() + " child probability: " + childNode.stateProbability() + " parent probability: " + parentNode.stateProbability(), Math.abs(probability - 0.5) < 0.0001);
    }

    private static enum SimpleEnum {
        TRUE, FALSE
    }
}
