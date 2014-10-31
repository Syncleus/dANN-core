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

import com.syncleus.dann.graph.ImmutableDirectedEdge;
import com.syncleus.dann.graphicalmodel.*;
import com.syncleus.dann.graphicalmodel.bayesian.xml.BayesianNetworkXml;
import org.junit.*;

import javax.xml.bind.*;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.*;
import java.util.*;

public class TestSicknessBayesianNetwork {
    private static final Random RANDOM = new Random();
    private MutableBayesianAdjacencyNetwork network = new MutableBayesianAdjacencyNetwork();
    //create nodes
    private GraphicalModelNode<SeasonState> season = new SimpleGraphicalModelNode<SeasonState>(SeasonState.WINTER);
    private GraphicalModelNode<AgeState> age = new SimpleGraphicalModelNode<AgeState>(AgeState.BABY);
    private GraphicalModelNode<BooleanState> stuffyNose = new SimpleGraphicalModelNode<BooleanState>(BooleanState.TRUE);
    private GraphicalModelNode<FeverState> fever = new SimpleGraphicalModelNode<FeverState>(FeverState.HOT);
    private GraphicalModelNode<BooleanState> tired = new SimpleGraphicalModelNode<BooleanState>(BooleanState.FALSE);
    private GraphicalModelNode<BooleanState> sick = new SimpleGraphicalModelNode<BooleanState>(BooleanState.FALSE);

    @Test
    public void testXml() throws Exception {
        this.testOverall();

        //mashall it
        JAXBContext context = JAXBContext.newInstance(BayesianNetworkXml.class, TestSicknessBayesianNetwork.FeverState.class, TestSicknessBayesianNetwork.AgeState.class, TestSicknessBayesianNetwork.BooleanState.class, TestSicknessBayesianNetwork.SeasonState.class);
        Marshaller marshal = context.createMarshaller();

        StringWriter writer = new StringWriter();
        marshal.marshal(this.network.toXml(), writer);

        //unmarshall it
        StringReader reader = new StringReader(writer.toString());
        BayesianNetworkXml xml = JAXB.unmarshal(reader, BayesianNetworkXml.class);

        Assert.assertTrue("could not unmarshal object!", xml != null);
        Assert.assertTrue("Wrong number of edges after unmarshaling: " + xml.getEdges().getEdges().size(), xml.getEdges().getEdges().size() == 14);
        Assert.assertTrue("Wrong number of nodes after unmarshaling: " + xml.getNodes().getNodes().size(), xml.getNodes().getNodes().size() == 6);
    }

    @Test
    public void testOverallRepeated() {
        for (int i = 0; i < 10; i++) {
            this.testOverall();

            this.network = new MutableBayesianAdjacencyNetwork();
            this.season = new SimpleGraphicalModelNode<SeasonState>(SeasonState.WINTER);
            this.age = new SimpleGraphicalModelNode<AgeState>(AgeState.BABY);
            this.stuffyNose = new SimpleGraphicalModelNode<BooleanState>(BooleanState.TRUE);
            this.fever = new SimpleGraphicalModelNode<FeverState>(FeverState.HOT);
            this.tired = new SimpleGraphicalModelNode<BooleanState>(BooleanState.FALSE);
            this.sick = new SimpleGraphicalModelNode<BooleanState>(BooleanState.FALSE);
        }
    }

    @Test
    public void testOverall() {
        //add nodes
        this.network.add(this.season);
        this.network.add(this.age);
        this.network.add(this.stuffyNose);
        this.network.add(this.fever);
        this.network.add(this.tired);
        this.network.add(this.sick);
        //connect nodes
        this.network.add(new ImmutableDirectedEdge<GraphicalModelNode>(this.season, this.stuffyNose));
        this.network.add(new ImmutableDirectedEdge<GraphicalModelNode>(this.season, this.fever));
        this.network.add(new ImmutableDirectedEdge<GraphicalModelNode>(this.season, this.tired));
        this.network.add(new ImmutableDirectedEdge<GraphicalModelNode>(this.season, this.sick));
        this.network.add(new ImmutableDirectedEdge<GraphicalModelNode>(this.age, this.stuffyNose));
        this.network.add(new ImmutableDirectedEdge<GraphicalModelNode>(this.age, this.fever));
        this.network.add(new ImmutableDirectedEdge<GraphicalModelNode>(this.age, this.tired));
        this.network.add(new ImmutableDirectedEdge<GraphicalModelNode>(this.age, this.sick));
        this.network.add(new ImmutableDirectedEdge<GraphicalModelNode>(this.tired, this.fever));
        this.network.add(new ImmutableDirectedEdge<GraphicalModelNode>(this.tired, this.stuffyNose));
        this.network.add(new ImmutableDirectedEdge<GraphicalModelNode>(this.tired, this.sick));
        this.network.add(new ImmutableDirectedEdge<GraphicalModelNode>(this.stuffyNose, this.fever));
        this.network.add(new ImmutableDirectedEdge<GraphicalModelNode>(this.stuffyNose, this.sick));
        this.network.add(new ImmutableDirectedEdge<GraphicalModelNode>(this.fever, this.sick));
        //let the network learn
        for (int sampleCount = 0; sampleCount < 10; sampleCount++)
            this.sampleState();
        //lets check some probabilities
        final Set<GraphicalModelNode> goals = new HashSet<GraphicalModelNode>();
        goals.add(this.sick);
        final Set<GraphicalModelNode> influences = new HashSet<GraphicalModelNode>();
        influences.add(this.fever);
        this.sick.setState(BooleanState.TRUE);
        this.fever.setState(FeverState.LOW);
        final double lowPercentage = this.network.conditionalProbability(goals, influences);
        this.fever.setState(FeverState.NONE);
        final double nonePercentage = this.network.conditionalProbability(goals, influences);
        this.fever.setState(FeverState.WARM);
        final double warmPercentage = this.network.conditionalProbability(goals, influences);
        this.fever.setState(FeverState.HOT);
        final double hotPercentage = this.network.conditionalProbability(goals, influences);

        Assert.assertTrue("incorrect fever to sickness mapping! " + nonePercentage + " < " + lowPercentage + " < " + warmPercentage + " < " + hotPercentage, (nonePercentage < lowPercentage) && (lowPercentage < warmPercentage) && (warmPercentage < hotPercentage));
    }

    private void sampleState() {
        final SeasonState seasonState = (SeasonState.values())[RANDOM.nextInt(SeasonState.values().length)];
        this.season.setState(seasonState);

        final AgeState ageState = (AgeState.values())[RANDOM.nextInt(AgeState.values().length)];
        this.age.setState(ageState);

        final BooleanState noseState = (BooleanState.values())[RANDOM.nextInt(BooleanState.values().length)];
        this.stuffyNose.setState(noseState);

        final BooleanState tiredState = (BooleanState.values())[RANDOM.nextInt(BooleanState.values().length)];
        this.tired.setState(tiredState);


        this.fever.setState(FeverState.NONE);
        this.sick.setState(BooleanState.FALSE);
        this.network.learnStates();
        this.fever.setState(FeverState.NONE);
        this.sick.setState(BooleanState.FALSE);
        this.network.learnStates();
        this.fever.setState(FeverState.NONE);
        this.sick.setState(BooleanState.FALSE);
        this.network.learnStates();
        this.fever.setState(FeverState.NONE);
        this.sick.setState(BooleanState.FALSE);
        this.network.learnStates();
        this.fever.setState(FeverState.NONE);
        this.sick.setState(BooleanState.TRUE);
        this.network.learnStates();

        this.fever.setState(FeverState.LOW);
        this.sick.setState(BooleanState.FALSE);
        this.network.learnStates();
        this.fever.setState(FeverState.LOW);
        this.sick.setState(BooleanState.FALSE);
        this.network.learnStates();
        this.fever.setState(FeverState.LOW);
        this.sick.setState(BooleanState.FALSE);
        this.network.learnStates();
        this.fever.setState(FeverState.LOW);
        this.sick.setState(BooleanState.TRUE);
        this.network.learnStates();
        this.fever.setState(FeverState.LOW);
        this.sick.setState(BooleanState.TRUE);
        this.network.learnStates();

        this.fever.setState(FeverState.WARM);
        this.sick.setState(BooleanState.FALSE);
        this.network.learnStates();
        this.fever.setState(FeverState.WARM);
        this.sick.setState(BooleanState.FALSE);
        this.network.learnStates();
        this.fever.setState(FeverState.WARM);
        this.sick.setState(BooleanState.TRUE);
        this.network.learnStates();
        this.fever.setState(FeverState.WARM);
        this.sick.setState(BooleanState.TRUE);
        this.network.learnStates();
        this.fever.setState(FeverState.WARM);
        this.sick.setState(BooleanState.TRUE);
        this.network.learnStates();

        this.fever.setState(FeverState.HOT);
        this.sick.setState(BooleanState.FALSE);
        this.network.learnStates();
        this.fever.setState(FeverState.HOT);
        this.sick.setState(BooleanState.TRUE);
        this.network.learnStates();
        this.fever.setState(FeverState.HOT);
        this.sick.setState(BooleanState.TRUE);
        this.network.learnStates();
        this.fever.setState(FeverState.HOT);
        this.sick.setState(BooleanState.TRUE);
        this.network.learnStates();
        this.fever.setState(FeverState.HOT);
        this.sick.setState(BooleanState.TRUE);
        this.network.learnStates();
    }

    @XmlRootElement
    private static enum BooleanState {
        TRUE, FALSE
    }

    @XmlRootElement
    private static enum SeasonState {
        WINTER, SUMMER, SPRING, FALL
    }

    @XmlRootElement
    private static enum AgeState {
        BABY, CHILD, TEENAGER, ADULT, SENIOR
    }

    @XmlRootElement
    private static enum FeverState {
        LOW, NONE, WARM, HOT
    }
}
