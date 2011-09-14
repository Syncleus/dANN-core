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
package com.syncleus.dann.graphicalmodel.bayesian;

import java.util.*;
import com.syncleus.dann.graph.DirectedEdge;
import com.syncleus.dann.graph.SimpleDirectedEdge;
import com.syncleus.dann.graphicalmodel.GraphicalModelNode;
import com.syncleus.dann.graphicalmodel.SimpleGraphicalModelNode;
import org.junit.*;

public class TestSimpleBayesianNetwork
{
	private static enum SimpleEnum
	{
		TRUE, FALSE
	}

	@Test
	public void testDependentNode()
	{
		final MutableBayesianAdjacencyNetwork network = new MutableBayesianAdjacencyNetwork();
		final GraphicalModelNode<SimpleEnum> parentNode = new SimpleGraphicalModelNode<SimpleEnum>(SimpleEnum.TRUE);
		final GraphicalModelNode<SimpleEnum> childNode = new SimpleGraphicalModelNode<SimpleEnum>(SimpleEnum.TRUE);

		network.add(parentNode);
		network.add(childNode);

		final DirectedEdge<GraphicalModelNode> testEdge = new SimpleDirectedEdge<GraphicalModelNode>(parentNode, childNode);
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
		Assert.assertTrue("bad state probability (TRUE,TRUE)!", Math.abs(network.conditionalProbability(goals, influences) - 0.25) < 0.0001);

		parentNode.setState(SimpleEnum.TRUE);
		childNode.setState(SimpleEnum.FALSE);
		Assert.assertTrue("bad state probability (TRUE,FALSE)!", Math.abs(network.conditionalProbability(goals, influences) - 0.75) < 0.0001);

		parentNode.setState(SimpleEnum.FALSE);
		childNode.setState(SimpleEnum.TRUE);
		Assert.assertTrue("bad state probability (FALSE,TRUE)!", Math.abs(network.conditionalProbability(goals, influences) - 0.75) < 0.0001);

		parentNode.setState(SimpleEnum.FALSE);
		childNode.setState(SimpleEnum.FALSE);
		Assert.assertTrue("bad state probability (FALSE,FALSE)!", Math.abs(network.conditionalProbability(goals, influences) - 0.25) < 0.0001);
	}

	@Test
	public void testDependentNodeAsymmetrical()
	{
		final MutableBayesianAdjacencyNetwork network = new MutableBayesianAdjacencyNetwork();
		final GraphicalModelNode<SimpleEnum> parentNode = new SimpleGraphicalModelNode<SimpleEnum>(SimpleEnum.TRUE);
		final GraphicalModelNode<SimpleEnum> childNode = new SimpleGraphicalModelNode<SimpleEnum>(SimpleEnum.TRUE);

		network.add(parentNode);
		network.add(childNode);

		final DirectedEdge<GraphicalModelNode> testEdge = new SimpleDirectedEdge<GraphicalModelNode>(parentNode, childNode);
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
		Assert.assertTrue("bad state probability (TRUE,TRUE)!", Math.abs(network.conditionalProbability(goals, influences) - 0.25) < 0.0001);

		parentNode.setState(SimpleEnum.TRUE);
		childNode.setState(SimpleEnum.FALSE);
		Assert.assertTrue("bad state probability (TRUE,FALSE)!", Math.abs(network.conditionalProbability(goals, influences) - 0.75) < 0.0001);

		parentNode.setState(SimpleEnum.FALSE);
		childNode.setState(SimpleEnum.TRUE);
		Assert.assertTrue("bad state probability (FALSE,TRUE)!", Math.abs(network.conditionalProbability(goals, influences) - 0.5) < 0.0001);

		parentNode.setState(SimpleEnum.FALSE);
		childNode.setState(SimpleEnum.FALSE);
		Assert.assertTrue("bad state probability (FALSE,FALSE)!", Math.abs(network.conditionalProbability(goals, influences) - 0.5) < 0.0001);
	}
}
