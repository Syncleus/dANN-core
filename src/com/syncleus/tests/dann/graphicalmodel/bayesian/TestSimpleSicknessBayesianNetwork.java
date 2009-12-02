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

import com.syncleus.dann.graphicalmodel.bayesian.BayesianNode;
import com.syncleus.dann.graphicalmodel.bayesian.SimpleBayesianNetwork;
import com.syncleus.dann.graphicalmodel.bayesian.SimpleBayesianNode;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import org.junit.*;

public class TestSimpleSicknessBayesianNetwork
{
	private static enum BooleanState
	{
		TRUE,FALSE;
	}

	private static enum FeverState
	{
		LOW,NONE,WARM,HOT
	}

	private SimpleBayesianNetwork network = new SimpleBayesianNetwork();

	//create nodes
	private BayesianNode<FeverState> fever = new SimpleBayesianNode<FeverState>(FeverState.HOT, network);
	private BayesianNode<BooleanState> sick = new SimpleBayesianNode<BooleanState>(BooleanState.FALSE, network);

	@Test
	public void testOverall()
	{
		//add nodes
		network.add(fever);
		network.add(sick);

		//connect nodes
		network.connect(fever, sick);

		//let the network learn
		this.sampleState();

		//lets check some probabilities
		Set<BayesianNode> goals = new HashSet<BayesianNode>();
		goals.add(sick);
		Set<BayesianNode> influences = new HashSet<BayesianNode>();
		influences.add(fever);
		sick.setState(BooleanState.TRUE);

		fever.setState(FeverState.LOW);
		double lowPercentage = network.conditionalProbability(goals, influences);
		fever.setState(FeverState.NONE);
		double nonePercentage = network.conditionalProbability(goals, influences);
		fever.setState(FeverState.WARM);
		double warmPercentage = network.conditionalProbability(goals, influences);
		fever.setState(FeverState.HOT);
		double hotPercentage = network.conditionalProbability(goals, influences);

		Assert.assertTrue("incorrect fever to sickness mapping! " + nonePercentage + " < " + lowPercentage + " < " + warmPercentage + " < " + hotPercentage, (nonePercentage < lowPercentage) && (lowPercentage < warmPercentage) && (warmPercentage < hotPercentage) );
	}

	private void sampleState()
	{
		fever.setState(FeverState.NONE);
		sick.setState(BooleanState.FALSE);
		network.learnStates();
		fever.setState(FeverState.NONE);
		sick.setState(BooleanState.FALSE);
		network.learnStates();
		fever.setState(FeverState.NONE);
		sick.setState(BooleanState.FALSE);
		network.learnStates();
		fever.setState(FeverState.NONE);
		sick.setState(BooleanState.FALSE);
		network.learnStates();
		fever.setState(FeverState.NONE);
		sick.setState(BooleanState.TRUE);
		network.learnStates();

		fever.setState(FeverState.LOW);
		sick.setState(BooleanState.FALSE);
		network.learnStates();
		fever.setState(FeverState.LOW);
		sick.setState(BooleanState.FALSE);
		network.learnStates();
		fever.setState(FeverState.LOW);
		sick.setState(BooleanState.FALSE);
		network.learnStates();
		fever.setState(FeverState.LOW);
		sick.setState(BooleanState.TRUE);
		network.learnStates();
		fever.setState(FeverState.LOW);
		sick.setState(BooleanState.TRUE);
		network.learnStates();

		fever.setState(FeverState.WARM);
		sick.setState(BooleanState.FALSE);
		network.learnStates();
		fever.setState(FeverState.WARM);
		sick.setState(BooleanState.FALSE);
		network.learnStates();
		fever.setState(FeverState.WARM);
		sick.setState(BooleanState.TRUE);
		network.learnStates();
		fever.setState(FeverState.WARM);
		sick.setState(BooleanState.TRUE);
		network.learnStates();
		fever.setState(FeverState.WARM);
		sick.setState(BooleanState.TRUE);
		network.learnStates();

		fever.setState(FeverState.HOT);
		sick.setState(BooleanState.FALSE);
		network.learnStates();
		fever.setState(FeverState.HOT);
		sick.setState(BooleanState.TRUE);
		network.learnStates();
		fever.setState(FeverState.HOT);
		sick.setState(BooleanState.TRUE);
		network.learnStates();
		fever.setState(FeverState.HOT);
		sick.setState(BooleanState.TRUE);
		network.learnStates();
		fever.setState(FeverState.HOT);
		sick.setState(BooleanState.TRUE);
		network.learnStates();
	}
}
