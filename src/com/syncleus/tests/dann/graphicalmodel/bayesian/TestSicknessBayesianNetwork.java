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
import java.util.*;
import org.junit.*;

public class TestSicknessBayesianNetwork
{
	private static enum BooleanState
	{
		TRUE, FALSE
	}

	private static enum SeasonState
	{
		WINTER, SUMMER, SPRING, FALL
	}

	private static enum AgeState
	{
		BABY, CHILD, TEENAGER, ADULT, SENIOR
	}

	private static enum FeverState
	{
		LOW, NONE, WARM, HOT
	}

	private static final Random RANDOM = new Random();
	private MutableBayesianAdjacencyNetwork network = new MutableBayesianAdjacencyNetwork();
	//create nodes
	private BayesianNode<SeasonState> season = new SimpleBayesianNode<SeasonState>(SeasonState.WINTER, network);
	private BayesianNode<AgeState> age = new SimpleBayesianNode<AgeState>(AgeState.BABY, network);
	private BayesianNode<BooleanState> stuffyNose = new SimpleBayesianNode<BooleanState>(BooleanState.TRUE, network);
	private BayesianNode<FeverState> fever = new SimpleBayesianNode<FeverState>(FeverState.HOT, network);
	private BayesianNode<BooleanState> tired = new SimpleBayesianNode<BooleanState>(BooleanState.FALSE, network);
	private BayesianNode<BooleanState> sick = new SimpleBayesianNode<BooleanState>(BooleanState.FALSE, network);

	@Test
	public void testOverallRepeated()
	{
		for(int i = 0; i < 10; i++)
		{
			testOverall();
			this.network = new MutableBayesianAdjacencyNetwork();
			this.season = new SimpleBayesianNode<SeasonState>(SeasonState.WINTER, network);
			this.age = new SimpleBayesianNode<AgeState>(AgeState.BABY, network);
			this.stuffyNose = new SimpleBayesianNode<BooleanState>(BooleanState.TRUE, network);
			this.fever = new SimpleBayesianNode<FeverState>(FeverState.HOT, network);
			this.tired = new SimpleBayesianNode<BooleanState>(BooleanState.FALSE, network);
			this.sick = new SimpleBayesianNode<BooleanState>(BooleanState.FALSE, network);
		}
	}

	@Test
	public void testOverall()
	{
		//add nodes
		network.add(season);
		network.add(age);
		network.add(stuffyNose);
		network.add(fever);
		network.add(tired);
		network.add(sick);
		//connect nodes
		network.add(new SimpleBayesianEdge<BayesianNode>(season, stuffyNose));
		network.add(new SimpleBayesianEdge<BayesianNode>(season, fever));
		network.add(new SimpleBayesianEdge<BayesianNode>(season, tired));
		network.add(new SimpleBayesianEdge<BayesianNode>(season, sick));
		network.add(new SimpleBayesianEdge<BayesianNode>(age, stuffyNose));
		network.add(new SimpleBayesianEdge<BayesianNode>(age, fever));
		network.add(new SimpleBayesianEdge<BayesianNode>(age, tired));
		network.add(new SimpleBayesianEdge<BayesianNode>(age, sick));
		network.add(new SimpleBayesianEdge<BayesianNode>(tired, fever));
		network.add(new SimpleBayesianEdge<BayesianNode>(tired, stuffyNose));
		network.add(new SimpleBayesianEdge<BayesianNode>(tired, sick));
		network.add(new SimpleBayesianEdge<BayesianNode>(stuffyNose, fever));
		network.add(new SimpleBayesianEdge<BayesianNode>(stuffyNose, sick));
		network.add(new SimpleBayesianEdge<BayesianNode>(fever, sick));
		//let the network learn
		for(int sampleCount = 0; sampleCount < 10; sampleCount++)
			this.sampleState();
		//lets check some probabilities
		final Set<BayesianNode> goals = new HashSet<BayesianNode>();
		goals.add(sick);
		final Set<BayesianNode> influences = new HashSet<BayesianNode>();
		influences.add(fever);
		sick.setState(BooleanState.TRUE);
		fever.setState(FeverState.LOW);
		final double lowPercentage = network.conditionalProbability(goals, influences);
		fever.setState(FeverState.NONE);
		final double nonePercentage = network.conditionalProbability(goals, influences);
		fever.setState(FeverState.WARM);
		final double warmPercentage = network.conditionalProbability(goals, influences);
		fever.setState(FeverState.HOT);
		final double hotPercentage = network.conditionalProbability(goals, influences);
		Assert.assertTrue("incorrect fever to sickness mapping! " + nonePercentage + " < " + lowPercentage + " < " + warmPercentage + " < " + hotPercentage, (nonePercentage < lowPercentage) && (lowPercentage < warmPercentage) && (warmPercentage < hotPercentage));
	}

	private void sampleState()
	{
		final SeasonState seasonState = (SeasonState.values())[RANDOM.nextInt(SeasonState.values().length)];
		season.setState(seasonState);
		final AgeState ageState = (AgeState.values())[RANDOM.nextInt(AgeState.values().length)];
		age.setState(ageState);
		final BooleanState noseState = (BooleanState.values())[RANDOM.nextInt(BooleanState.values().length)];
		stuffyNose.setState(noseState);
		final BooleanState tiredState = (BooleanState.values())[RANDOM.nextInt(BooleanState.values().length)];
		tired.setState(tiredState);
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
