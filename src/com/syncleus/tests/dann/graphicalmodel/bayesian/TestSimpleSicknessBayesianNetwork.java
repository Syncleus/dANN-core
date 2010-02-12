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
import org.junit.*;

public class TestSimpleSicknessBayesianNetwork
{
    private HashSet<BayesianNode> goals;
    private HashSet<BayesianNode> influences;

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
		goals = new HashSet<BayesianNode>();
		goals.add(sick);
		influences = new HashSet<BayesianNode>();
		influences.add(fever);

		double nonePercentage = getPercentage(FeverState.NONE);
		double lowPercentage = getPercentage(FeverState.LOW);
		double warmPercentage = getPercentage(FeverState.WARM);
		double hotPercentage = getPercentage(FeverState.HOT);

        boolean condition = (nonePercentage < lowPercentage) && (lowPercentage < warmPercentage) && (warmPercentage < hotPercentage);
        Assert.assertTrue("incorrect fever to sickness mapping! " + nonePercentage + " < " + lowPercentage + " < " + warmPercentage + " < " + hotPercentage, condition );
	}

    protected double getPercentage(FeverState s) {
        fever.setState(s);
        return network.conditionalProbability(goals, influences);
    }

    protected void learnThat(FeverState fs, BooleanState bs) {
		fever.setState(fs);
		sick.setState(bs);
		network.learnStates();
    }

	private void sampleState()
	{
        learnThat(FeverState.NONE, BooleanState.FALSE);
        learnThat(FeverState.NONE, BooleanState.FALSE);
        learnThat(FeverState.NONE, BooleanState.FALSE);
        learnThat(FeverState.NONE, BooleanState.FALSE);

        learnThat(FeverState.NONE, BooleanState.TRUE);

        learnThat(FeverState.LOW, BooleanState.FALSE);
        learnThat(FeverState.LOW, BooleanState.FALSE);
        learnThat(FeverState.LOW, BooleanState.FALSE);

        learnThat(FeverState.LOW, BooleanState.TRUE);
        learnThat(FeverState.LOW, BooleanState.TRUE);

        learnThat(FeverState.WARM, BooleanState.FALSE);
        learnThat(FeverState.WARM, BooleanState.FALSE);

        learnThat(FeverState.WARM, BooleanState.TRUE);
        learnThat(FeverState.WARM, BooleanState.TRUE);

        learnThat(FeverState.WARM, BooleanState.TRUE);

        learnThat(FeverState.HOT, BooleanState.FALSE);

        learnThat(FeverState.HOT, BooleanState.TRUE);
        learnThat(FeverState.HOT, BooleanState.TRUE);
        learnThat(FeverState.HOT, BooleanState.TRUE);
        learnThat(FeverState.HOT, BooleanState.TRUE);

	}
}
