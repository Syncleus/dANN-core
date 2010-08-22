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

import com.syncleus.dann.graphicalmodel.bayesian.*;
import org.junit.*;

public class TestSimpleBayesianNode
{
	private static enum SimpleEnum
	{
		TRUE, FALSE
	}

	@Test
	public void testSingleNode()
	{
		final MutableBayesianAdjacencyNetwork network = new MutableBayesianAdjacencyNetwork();
		final BayesianNode<SimpleEnum> testNode = new SimpleBayesianNode<SimpleEnum>(SimpleEnum.TRUE);

		network.add(testNode);

		Assert.assertTrue("initial state not retained!", testNode.getState() == SimpleEnum.TRUE);

		testNode.setState(SimpleEnum.FALSE);
		Assert.assertTrue("set state not retained!", testNode.getState() == SimpleEnum.FALSE);

		testNode.learnState();
		Assert.assertTrue("state not learned!", testNode.getLearnedStates().contains(SimpleEnum.FALSE));
		Assert.assertTrue("state not learned!", !(testNode.getLearnedStates().contains(SimpleEnum.TRUE)));

		testNode.learnState();
		testNode.learnState();
		testNode.setState(SimpleEnum.TRUE);
		testNode.learnState();
		Assert.assertTrue("state not learned!", testNode.getLearnedStates().contains(SimpleEnum.FALSE));
		Assert.assertTrue("state not learned!", testNode.getLearnedStates().contains(SimpleEnum.TRUE));
		Assert.assertTrue("bad state probability!", Math.abs(testNode.stateProbability() - 0.25) < 0.0001);
	}

	@Test
	public void testDependentNode()
	{
		final MutableBayesianAdjacencyNetwork network = new MutableBayesianAdjacencyNetwork();
		final BayesianNode<SimpleEnum> parentNode = new SimpleBayesianNode<SimpleEnum>(SimpleEnum.TRUE);
		final BayesianNode<SimpleEnum> childNode = new SimpleBayesianNode<SimpleEnum>(SimpleEnum.TRUE);

		network.add(parentNode);
		network.add(childNode);

		final BayesianEdge<BayesianNode<SimpleEnum>> testEdge = new SimpleBayesianEdge<BayesianNode<SimpleEnum>>(parentNode, childNode);
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

		parentNode.setState(SimpleEnum.TRUE);
		childNode.setState(SimpleEnum.TRUE);
		Assert.assertTrue("bad state probability (TRUE,TRUE)!", Math.abs(childNode.stateProbability() - 0.25) < 0.0001);

		parentNode.setState(SimpleEnum.TRUE);
		childNode.setState(SimpleEnum.FALSE);
		Assert.assertTrue("bad state probability (TRUE,FALSE)!", Math.abs(childNode.stateProbability() - 0.75) < 0.0001);

		parentNode.setState(SimpleEnum.FALSE);
		childNode.setState(SimpleEnum.TRUE);
		Assert.assertTrue("bad state probability (FALSE,TRUE)!", Math.abs(childNode.stateProbability() - 0.75) < 0.0001);

		parentNode.setState(SimpleEnum.FALSE);
		childNode.setState(SimpleEnum.FALSE);
		Assert.assertTrue("bad state probability (FALSE,FALSE)!", Math.abs(childNode.stateProbability() - 0.25) < 0.0001);
	}
}
