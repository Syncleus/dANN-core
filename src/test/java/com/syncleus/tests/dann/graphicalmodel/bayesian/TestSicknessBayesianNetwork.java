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

import java.io.StringReader;
import java.io.StringWriter;
import java.util.*;
import com.syncleus.dann.graphicalmodel.bayesian.*;
import com.syncleus.dann.graphicalmodel.bayesian.xml.BayesianNetworkXml;
import org.junit.*;
import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlRootElement;

public class TestSicknessBayesianNetwork
{
    @XmlRootElement
	private static enum BooleanState
	{
		TRUE, FALSE
	}

    @XmlRootElement
	private static enum SeasonState
	{
		WINTER, SUMMER, SPRING, FALL
	}

    @XmlRootElement
	private static enum AgeState
	{
		BABY, CHILD, TEENAGER, ADULT, SENIOR
	}

    @XmlRootElement
	private static enum FeverState
	{
		LOW, NONE, WARM, HOT
	}

	private static final Random RANDOM = new Random();
	private MutableBayesianAdjacencyNetwork network = new MutableBayesianAdjacencyNetwork();
	//create nodes
	private BayesianNode<SeasonState> season = new SimpleBayesianNode<SeasonState>(SeasonState.WINTER);
	private BayesianNode<AgeState> age = new SimpleBayesianNode<AgeState>(AgeState.BABY);
	private BayesianNode<BooleanState> stuffyNose = new SimpleBayesianNode<BooleanState>(BooleanState.TRUE);
	private BayesianNode<FeverState> fever = new SimpleBayesianNode<FeverState>(FeverState.HOT);
	private BayesianNode<BooleanState> tired = new SimpleBayesianNode<BooleanState>(BooleanState.FALSE);
	private BayesianNode<BooleanState> sick = new SimpleBayesianNode<BooleanState>(BooleanState.FALSE);

    @Test
    public void testXml() throws Exception
    {
        testOverall();

        //mashall it
        JAXBContext context = JAXBContext.newInstance(BayesianNetworkXml.class, TestSicknessBayesianNetwork.FeverState.class, TestSicknessBayesianNetwork.AgeState.class, TestSicknessBayesianNetwork.BooleanState.class, TestSicknessBayesianNetwork.SeasonState.class);
        Marshaller marshal = context.createMarshaller();

        StringWriter writer = new StringWriter();
        marshal.marshal(network.toXml(), writer);

        //unmarshall it
        StringReader reader = new StringReader(writer.toString());
        BayesianNetworkXml xml = JAXB.unmarshal(reader, BayesianNetworkXml.class);

        Assert.assertTrue("could not unmarshal object!", xml != null);
        Assert.assertTrue("Wrong number of edges after unmarshaling: " + xml.getEdges().getEdges().size(), xml.getEdges().getEdges().size() == 14);
        Assert.assertTrue("Wrong number of nodes after unmarshaling: " + xml.getNodes().getNodes().size(), xml.getNodes().getNodes().size() == 6);
    }

	@Test
	public void testOverallRepeated()
	{
		for(int i = 0; i < 10; i++)
		{
			testOverall();

			this.network = new MutableBayesianAdjacencyNetwork();
			this.season = new SimpleBayesianNode<SeasonState>(SeasonState.WINTER);
			this.age = new SimpleBayesianNode<AgeState>(AgeState.BABY);
			this.stuffyNose = new SimpleBayesianNode<BooleanState>(BooleanState.TRUE);
			this.fever = new SimpleBayesianNode<FeverState>(FeverState.HOT);
			this.tired = new SimpleBayesianNode<BooleanState>(BooleanState.FALSE);
			this.sick = new SimpleBayesianNode<BooleanState>(BooleanState.FALSE);
		}
	}

	@Test
	public void testOverall()
	{
		//add nodes
		network.add(this.season);
		network.add(this.age);
		network.add(this.stuffyNose);
		network.add(this.fever);
		network.add(this.tired);
		network.add(this.sick);
		//connect nodes
		network.add(new SimpleBayesianEdge<BayesianNode>(this.season, this.stuffyNose));
		network.add(new SimpleBayesianEdge<BayesianNode>(this.season, this.fever));
		network.add(new SimpleBayesianEdge<BayesianNode>(this.season, this.tired));
		network.add(new SimpleBayesianEdge<BayesianNode>(this.season, this.sick));
		network.add(new SimpleBayesianEdge<BayesianNode>(this.age, this.stuffyNose));
		network.add(new SimpleBayesianEdge<BayesianNode>(this.age, this.fever));
		network.add(new SimpleBayesianEdge<BayesianNode>(this.age, this.tired));
		network.add(new SimpleBayesianEdge<BayesianNode>(this.age, this.sick));
		network.add(new SimpleBayesianEdge<BayesianNode>(this.tired, this.fever));
		network.add(new SimpleBayesianEdge<BayesianNode>(this.tired, this.stuffyNose));
		network.add(new SimpleBayesianEdge<BayesianNode>(this.tired, this.sick));
		network.add(new SimpleBayesianEdge<BayesianNode>(this.stuffyNose, this.fever));
		network.add(new SimpleBayesianEdge<BayesianNode>(this.stuffyNose, this.sick));
		network.add(new SimpleBayesianEdge<BayesianNode>(this.fever, this.sick));
		//let the network learn
		for(int sampleCount = 0; sampleCount < 10; sampleCount++)
			this.sampleState();
		//lets check some probabilities
		final Set<BayesianNode> goals = new HashSet<BayesianNode>();
		goals.add(this.sick);
		final Set<BayesianNode> influences = new HashSet<BayesianNode>();
		influences.add(this.fever);
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
