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

import java.util.HashSet;
import com.syncleus.dann.graphicalmodel.bayesian.*;
import org.junit.*;

/**
 * tests SimpleBooleanNetwork (extending BayesianNetwork)
 */
public class TestSimpleBooleanNetwork
{
	public static enum BooleanState
	{
		TRUE, FALSE
	}

	private static enum TwoState
	{
		A, B
	}

	private static enum ThreeState
	{
		A, B, C
	}

	/**
	 * a four-state profile
	 */
	private static enum FeverState
	{
		LOW, NONE, WARM, HOT
	}

	/**
	 * boolean-goaled bayesian network
	 */
	private static class SimpleBooleanNetwork<I> extends MutableBayesianAdjacencyNetwork
	{
		private final HashSet<BayesianNode> goals;
		private final HashSet<BayesianNode> influences;
		//create nodes
		private final BayesianNode<I> influence;
		private final BayesianNode<BooleanState> goal;
		private static final long serialVersionUID = -5435852374754248557L;

		public SimpleBooleanNetwork(final I initialState)
		{
			super();
			this.influence = new SimpleBayesianNode<I>(initialState);
			this.goal = new SimpleBayesianNode<BooleanState>(BooleanState.FALSE);
			this.goals = new HashSet<BayesianNode>();
			this.influences = new HashSet<BayesianNode>();
			//add nodes
			add(this.influence);
			add(this.goal);
			//connect nodes
			final BayesianEdge<BayesianNode> testEdge = new SimpleBayesianEdge<BayesianNode>(this.influence, this.goal);
			this.add(testEdge);
			goals.add(this.goal);
			influences.add(this.influence);
		}

		public BayesianNode<BooleanState> getGoal()
		{
			return this.goal;
		}

		public BayesianNode<I> getInfluence()
		{
			return this.influence;
		}

		public double getPercentage(final I influenceState)
		{
			influence.setState(influenceState);
			return this.conditionalProbability(this.goals, this.influences);
		}

		public void learn(final I influenceState, final boolean goalState)
		{
			learn(influenceState, goalState ? BooleanState.TRUE : BooleanState.FALSE);
		}

		public void learn(final I influenceState, final BooleanState goalState)
		{
			influence.setState(influenceState);
			goal.setState(goalState);
			learnStates();
		}
	}

	/**
	 * tests two states with equal probability distribution
	 */
	@Test
	public void testTwoState50()
	{
		final SimpleBooleanNetwork<TwoState> n = new SimpleBooleanNetwork<TwoState>(TwoState.B);
		n.learn(TwoState.A, true);
		n.learn(TwoState.A, false);  //::
		n.learn(TwoState.B, false);
		n.learn(TwoState.B, true);  //::
		n.getGoal().setState(BooleanState.TRUE);
		final double truePercent = n.getPercentage(TwoState.A);
		final double falsePercent = n.getPercentage(TwoState.B);
		Assert.assertTrue("incorrect true/false distribution: " + truePercent + ':' + falsePercent, (Math.abs(truePercent - (1f / 2f)) < 0.000001) && (Math.abs(falsePercent - (1f / 2f)) < 0.000001));
	}

	@Test
	public void testTwoState50Repeated()
	{
		for(int iteration = 0; iteration < 1000; iteration++)
			testTwoState50();
	}

	/**
	 * three state balanced probability. A=100%, B=50%, C=0%
	 */
	@Test
	public void testThreeStateBalanced()
	{
		final SimpleBooleanNetwork<ThreeState> n = new SimpleBooleanNetwork<ThreeState>(ThreeState.A);
		n.learn(ThreeState.A, true);
		n.learn(ThreeState.A, true);
		n.learn(ThreeState.B, true);
		n.learn(ThreeState.B, false);
		n.learn(ThreeState.C, false);
		n.learn(ThreeState.C, false);
		n.getGoal().setState(BooleanState.TRUE);
		final double aPercent = n.getPercentage(ThreeState.A);
		final double bPercent = n.getPercentage(ThreeState.B);
		final double cPercent = n.getPercentage(ThreeState.C);
		final boolean condition = ((Math.abs(aPercent - 1f) < 0.000001) && (Math.abs(bPercent - 0.5f) < 0.000001) && (Math.abs(cPercent) < 0.00000001));
		Assert.assertTrue("incorrect a/b/c distribution" + aPercent + " == 1f && " + bPercent + " == 0.5f && " + cPercent + " == 0.0", condition);
	}

	@Test
	public void testThreeStateBalancedRepeated()
	{
		for(int iteration = 0; iteration < 1000; iteration++)
			testThreeStateBalanced();
	}

	/**
	 * tests two-state 3/4 balance
	 */
	@Test
	public void testTwoState75()
	{
		final SimpleBooleanNetwork<TwoState> n = new SimpleBooleanNetwork<TwoState>(TwoState.B);
		n.learn(TwoState.A, true);
		n.learn(TwoState.A, true);
		n.learn(TwoState.A, true);
		n.learn(TwoState.A, false);  //::
		n.learn(TwoState.B, false);
		n.learn(TwoState.B, false);
		n.learn(TwoState.B, false);
		n.learn(TwoState.B, true);  //::
		n.getGoal().setState(BooleanState.TRUE);
		final double truePercent = n.getPercentage(TwoState.A);
		final double falsePercent = n.getPercentage(TwoState.B);
		Assert.assertTrue("incorrect true/false distribution: " + truePercent + ':' + falsePercent, (Math.abs(truePercent - (3f / 4f)) < 0.000001) && (Math.abs(falsePercent - (1f / 4f)) < 0.000001));
	}

	@Test
	public void testTwoState75Repeated()
	{
		for(int iteration = 0; iteration < 1000; iteration++)
			testTwoState75();
	}

	/**
	 * tests four-state boolean bayesian network, mapping "fevers' -> sickness
	 */
	@Test
	public void testFeverState()
	{

		final SimpleBooleanNetwork<FeverState> n = new SimpleBooleanNetwork<FeverState>(FeverState.HOT);
		n.learn(FeverState.NONE, false);
		n.learn(FeverState.NONE, false);
		n.learn(FeverState.NONE, false);
		n.learn(FeverState.NONE, false);

		n.learn(FeverState.NONE, true);

		n.learn(FeverState.LOW, false);
		n.learn(FeverState.LOW, false);
		n.learn(FeverState.LOW, false);

		n.learn(FeverState.LOW, true);
		n.learn(FeverState.LOW, true);

		n.learn(FeverState.WARM, false);
		n.learn(FeverState.WARM, false);

		n.learn(FeverState.WARM, true);
		n.learn(FeverState.WARM, true);

		n.learn(FeverState.WARM, true);

		n.learn(FeverState.HOT, false);

		n.learn(FeverState.HOT, true);
		n.learn(FeverState.HOT, true);
		n.learn(FeverState.HOT, true);
		n.learn(FeverState.HOT, true);

		n.getGoal().setState(BooleanState.TRUE);

		//check some probabilities
		final double nonePercentage = n.getPercentage(FeverState.NONE);
		final double lowPercentage = n.getPercentage(FeverState.LOW);
		final double warmPercentage = n.getPercentage(FeverState.WARM);
		final double hotPercentage = n.getPercentage(FeverState.HOT);

		final boolean condition = (nonePercentage < lowPercentage) && (lowPercentage < warmPercentage) && (warmPercentage < hotPercentage);

		Assert.assertTrue("incorrect fever to sickness mapping! " + nonePercentage + " < " + lowPercentage + " < " + warmPercentage + " < " + hotPercentage, condition);
	}

	@Test
	public void testFeverStateRepeated()
	{
		for(int iteration = 0; iteration < 1000; iteration++)
			testFeverState();
	}
}
