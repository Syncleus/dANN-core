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

public class TestAbstractSimpleBayesianNetwork {

    public static enum BooleanState {

        TRUE, FALSE;
    }

    public static class SimpleBooleanNetwork<I> {

        private HashSet<BayesianNode> goals;
        private HashSet<BayesianNode> influences;
        private final SimpleBayesianNetwork network;
        //create nodes
        private final BayesianNode<I> influence;
        private final BayesianNode<BooleanState> goal;

        public SimpleBooleanNetwork(I initialState) {
            network = new SimpleBayesianNetwork();
            influence = new SimpleBayesianNode<I>(initialState, network);
            goal = new SimpleBayesianNode<BooleanState>(BooleanState.FALSE, network);
            goals = new HashSet<BayesianNode>();
            influences = new HashSet<BayesianNode>();

            //add nodes
            network.add(influence);
            network.add(goal);

            //connect nodes
            network.connect(influence, goal);

            goals.add(influence);
            influences.add(goal);

        }

        public BayesianNode<BooleanState> getGoal() {
            return goal;
        }
        
        public BayesianNode<I> getInfluence() {
            return influence;
        }

        public double getPercentage(I s) {
            influence.setState(s);
            return network.conditionalProbability(goals, influences);
        }

        public void learnThat(I i, BooleanState bs) {
            influence.setState(i);
            goal.setState(bs);
            network.learnStates();
        }
    }

    private static enum TwoState {
        TRUE, FALSE
    }
    private static enum ThreeState {
        A, B, C
    }

    private static enum FeverState {
        LOW, NONE, WARM, HOT
    }

    @Test public void testTwoState50() {
        SimpleBooleanNetwork<TwoState> n = new SimpleBooleanNetwork<TwoState>(TwoState.FALSE);
        {
            n.learnThat(TwoState.TRUE, BooleanState.TRUE);
            n.learnThat(TwoState.TRUE, BooleanState.FALSE);  //::

            n.learnThat(TwoState.FALSE, BooleanState.FALSE);
            n.learnThat(TwoState.FALSE, BooleanState.TRUE);  //::
        }
        n.getGoal().setState(BooleanState.TRUE);

        double truePercent = n.getPercentage(TwoState.TRUE);
        double falsePercent = n.getPercentage(TwoState.FALSE);

        Assert.assertTrue("incorrect true/false distribution: "+ truePercent + ":" + falsePercent, (truePercent == 0.5) && (falsePercent == 0.5));
    }

    @Test public void testThreeStateBalanced() {
        SimpleBooleanNetwork<ThreeState> n = new SimpleBooleanNetwork<ThreeState>(ThreeState.A);
        {
            n.learnThat(ThreeState.A, BooleanState.TRUE);
            n.learnThat(ThreeState.A, BooleanState.TRUE);

            n.learnThat(ThreeState.B, BooleanState.TRUE);
            n.learnThat(ThreeState.B, BooleanState.FALSE);

            n.learnThat(ThreeState.C, BooleanState.FALSE);
            n.learnThat(ThreeState.C, BooleanState.FALSE);
        }
        n.getGoal().setState(BooleanState.TRUE);

        double aPercent = n.getPercentage(ThreeState.A);
        double bPercent = n.getPercentage(ThreeState.B);
        double cPercent = n.getPercentage(ThreeState.C);
        System.out.println("3 state: " + aPercent + " " + bPercent + " " + cPercent);

        //Assert.assertTrue("incorrect true/false distribution: "+ truePercent + ":" + falsePercent, (truePercent == 0.5) && (falsePercent == 0.5));
    }

    @Test public void testTwoState75() {
        SimpleBooleanNetwork<TwoState> n = new SimpleBooleanNetwork<TwoState>(TwoState.FALSE);
        {
            n.learnThat(TwoState.TRUE, BooleanState.TRUE);
            n.learnThat(TwoState.TRUE, BooleanState.TRUE);
            n.learnThat(TwoState.TRUE, BooleanState.TRUE);
            n.learnThat(TwoState.TRUE, BooleanState.FALSE);  //::

            n.learnThat(TwoState.FALSE, BooleanState.FALSE);
            n.learnThat(TwoState.FALSE, BooleanState.FALSE);
            n.learnThat(TwoState.FALSE, BooleanState.FALSE);
            n.learnThat(TwoState.FALSE, BooleanState.TRUE);  //::
        }
        n.getGoal().setState(BooleanState.TRUE);

        double truePercent = n.getPercentage(TwoState.TRUE);
        double falsePercent = n.getPercentage(TwoState.FALSE);

        Assert.assertTrue("incorrect true/false distribution: "+ truePercent + ":" + falsePercent, (truePercent == 0.75) && (falsePercent == 0.25));
    }

    @Test public void testFeverState() {

        SimpleBooleanNetwork<FeverState> n = new SimpleBooleanNetwork<FeverState>(FeverState.HOT);
        {

            n.learnThat(FeverState.NONE, BooleanState.FALSE);
            n.learnThat(FeverState.NONE, BooleanState.FALSE);
            n.learnThat(FeverState.NONE, BooleanState.FALSE);
            n.learnThat(FeverState.NONE, BooleanState.FALSE);

            n.learnThat(FeverState.NONE, BooleanState.TRUE);

            n.learnThat(FeverState.LOW, BooleanState.FALSE);
            n.learnThat(FeverState.LOW, BooleanState.FALSE);
            n.learnThat(FeverState.LOW, BooleanState.FALSE);

            n.learnThat(FeverState.LOW, BooleanState.TRUE);
            n.learnThat(FeverState.LOW, BooleanState.TRUE);

            n.learnThat(FeverState.WARM, BooleanState.FALSE);
            n.learnThat(FeverState.WARM, BooleanState.FALSE);

            n.learnThat(FeverState.WARM, BooleanState.TRUE);
            n.learnThat(FeverState.WARM, BooleanState.TRUE);

            n.learnThat(FeverState.WARM, BooleanState.TRUE);

            n.learnThat(FeverState.HOT, BooleanState.FALSE);

            n.learnThat(FeverState.HOT, BooleanState.TRUE);
            n.learnThat(FeverState.HOT, BooleanState.TRUE);
            n.learnThat(FeverState.HOT, BooleanState.TRUE);
            n.learnThat(FeverState.HOT, BooleanState.TRUE);
        }

        n.getGoal().setState(BooleanState.TRUE);

        //check some probabilities
        double nonePercentage = n.getPercentage(FeverState.NONE);
        double lowPercentage = n.getPercentage(FeverState.LOW);
        double warmPercentage = n.getPercentage(FeverState.WARM);
        double hotPercentage = n.getPercentage(FeverState.HOT);

        boolean condition = (nonePercentage < lowPercentage) && (lowPercentage < warmPercentage) && (warmPercentage < hotPercentage);
        System.out.println("fever to sickness mapping! " + nonePercentage + " < " + lowPercentage + " < " + warmPercentage + " < " + hotPercentage);
        Assert.assertTrue("incorrect fever to sickness mapping! " + nonePercentage + " < " + lowPercentage + " < " + warmPercentage + " < " + hotPercentage, condition);
    }
}
