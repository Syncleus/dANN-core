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
package com.syncleus.tests.dann.graphicalmodel.bayesian;

import com.syncleus.dann.graphicalmodel.bayesian.BayesianEdge;
import com.syncleus.dann.graphicalmodel.bayesian.BayesianNode;
import com.syncleus.dann.graphicalmodel.bayesian.MutableBayesianAdjacencyNetwork;
import com.syncleus.dann.graphicalmodel.bayesian.SimpleBayesianEdge;
import com.syncleus.dann.graphicalmodel.bayesian.SimpleBayesianNode;
import java.util.HashSet;
import java.util.Set;
import org.junit.*;

public class TestSimpleBayesianNetwork
{
	private static enum SimpleEnum{ TRUE,FALSE; }
	
	@Test
	public void testDependentNode()
	{
		MutableBayesianAdjacencyNetwork network = new MutableBayesianAdjacencyNetwork();
		BayesianNode<SimpleEnum> parentNode = new SimpleBayesianNode<SimpleEnum>(SimpleEnum.TRUE, network);
		BayesianNode<SimpleEnum> childNode = new SimpleBayesianNode<SimpleEnum>(SimpleEnum.TRUE, network);

		network.add(parentNode);
		network.add(childNode);

		BayesianEdge<BayesianNode> testEdge = new SimpleBayesianEdge<BayesianNode>(parentNode, childNode);
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

		Set<BayesianNode> goals = new HashSet<BayesianNode>();
		goals.add(childNode);
		Set<BayesianNode> influences = new HashSet<BayesianNode>();
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
}
