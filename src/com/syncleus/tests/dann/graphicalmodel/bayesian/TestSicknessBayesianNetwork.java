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

public class TestSicknessBayesianNetwork
{
	private static enum BooleanState
	{
		TRUE,FALSE;
	}

	private static enum SeasonState
	{
		WINTER,SUMMER,SPRING,FALL;
	}

	private static enum AgeState
	{
		BABY,CHILD,TEENAGER,ADULT,SENIOR;
	}

	private static enum FeverState
	{
		LOW,NONE,WARM,HOT
	}
	
	private static Random RANDOM = new Random();

	private SimpleBayesianNetwork network = new SimpleBayesianNetwork();

	//create nodes
	private BayesianNode<Enum> season = new SimpleBayesianNode<Enum>(SeasonState.WINTER, network);
	private BayesianNode<Enum> age = new SimpleBayesianNode<Enum>(AgeState.BABY, network);
	private BayesianNode<Enum> stuffyNose = new SimpleBayesianNode<Enum>(BooleanState.TRUE, network);
	private BayesianNode<Enum> fever = new SimpleBayesianNode<Enum>(FeverState.HOT, network);
	private BayesianNode<Enum> tired = new SimpleBayesianNode<Enum>(BooleanState.FALSE, network);
	private BayesianNode<Enum> sick = new SimpleBayesianNode<Enum>(BooleanState.FALSE, network);

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
		network.connect(season, stuffyNose);
		network.connect(season, fever);
		network.connect(season, tired);
		network.connect(season, sick);
		network.connect(age, stuffyNose);
		network.connect(age, fever);
		network.connect(age, tired);
		network.connect(age, sick);
		network.connect(tired, fever);
		network.connect(tired, stuffyNose);
		network.connect(tired, sick);
		network.connect(stuffyNose, fever);
		network.connect(stuffyNose, sick);
		network.connect(fever, sick);

		//let the network learn
		for(int sampleCount = 0; sampleCount < 10000; sampleCount++)
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
		double sickScore = 0.0;

		SeasonState seasonState = (SeasonState.values())[RANDOM.nextInt(SeasonState.values().length)];
		season.setState(seasonState);
		switch( seasonState )
		{
		case WINTER:
			sickScore += 10;
			break;
		case SUMMER:
			sickScore += 2;
			break;
		case FALL:
		case SPRING:
			sickScore -= 4;
		}

		AgeState ageState = (AgeState.values())[RANDOM.nextInt(AgeState.values().length)];
		age.setState(ageState);
		switch( ageState )
		{
		case BABY:
			sickScore += 7;
			break;
		case CHILD:
			sickScore -= 2;
			break;
		case TEENAGER:
			sickScore -= 10;
			break;
		case ADULT:
			break;
		case SENIOR:
			sickScore += 10;
		}

		FeverState feverState = (FeverState.values())[RANDOM.nextInt(FeverState.values().length)];
		fever.setState(feverState);
		switch( feverState )
		{
		case LOW:
			sickScore += 5;
			break;
		case NONE:
			sickScore -= 25;
			break;
		case WARM:
			sickScore += 10;
			break;
		case HOT:
			sickScore += 40;
			break;
		}


		BooleanState noseState = (BooleanState.values())[RANDOM.nextInt(BooleanState.values().length)];
		stuffyNose.setState(noseState);
		switch( noseState )
		{
		case TRUE:
			sickScore += 7;
			break;
		case FALSE:
			sickScore -= 2;
			break;
		}

		BooleanState tiredState = (BooleanState.values())[RANDOM.nextInt(BooleanState.values().length)];
		tired.setState(tiredState);
		switch( tiredState )
		{
		case TRUE:
			sickScore += 3;
			break;
		case FALSE:
			sickScore -= 10;
			break;
		}

		BooleanState sickState = (sickScore > 0 ? BooleanState.TRUE : BooleanState.FALSE);
		sick.setState(sickState);

		network.learnStates();
	}
}
