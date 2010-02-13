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

public class TestSimpleBooleanNetwork {

    public static enum BooleanState {
        TRUE, FALSE;
    }

    private static enum TwoState {
        A, B;
    }

    private static enum ThreeState {
        A, B, C
    }

    /** a four-state profile */
    private static enum FeverState {
        LOW, NONE, WARM, HOT
    }

    public static class SimpleBooleanNetwork<I> extends SimpleBayesianNetwork {

        private HashSet<BayesianNode> goals;
        private HashSet<BayesianNode> influences;
        //create nodes
        private final BayesianNode<I> influence;
        private final BayesianNode<BooleanState> goal;

        public SimpleBooleanNetwork(I initialState) {
            super();
            influence = new SimpleBayesianNode<I>(initialState, this);
            goal = new SimpleBayesianNode<BooleanState>(BooleanState.FALSE, this);
            goals = new HashSet<BayesianNode>();
            influences = new HashSet<BayesianNode>();

            //add nodes
            add(influence);
            add(goal);

            //connect nodes
            connect(influence, goal);

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
            return conditionalProbability(goals, influences);
        }
        public void learn(I i, boolean b) {
            learn(i, b ? BooleanState.TRUE : BooleanState.FALSE);
        }

        public void learn(I i, BooleanState bs) {
            influence.setState(i);
            goal.setState(bs);
            learnStates();
        }
    }


    @Test public void testTwoState50() {
        SimpleBooleanNetwork<TwoState> n = new SimpleBooleanNetwork<TwoState>(TwoState.B);
        {
            n.learn(TwoState.A, true);
            n.learn(TwoState.A, false);  //::

            n.learn(TwoState.B, false);
            n.learn(TwoState.B, true);  //::
        }
        n.getGoal().setState(BooleanState.TRUE);

        double truePercent = n.getPercentage(TwoState.A);
        double falsePercent = n.getPercentage(TwoState.B);

        Assert.assertTrue("incorrect true/false distribution: "+ truePercent + ":" + falsePercent, (truePercent == 0.5) && (falsePercent == 0.5));
    }

    @Test public void testThreeStateBalanced() {
        SimpleBooleanNetwork<ThreeState> n = new SimpleBooleanNetwork<ThreeState>(ThreeState.A);
        {
            n.learn(ThreeState.A, true);
            n.learn(ThreeState.A, true);

            n.learn(ThreeState.B, true);
            n.learn(ThreeState.B, false);

            n.learn(ThreeState.C, false);
            n.learn(ThreeState.C, false);
        }
        n.getGoal().setState(BooleanState.TRUE);

        double aPercent = n.getPercentage(ThreeState.A);
        double bPercent = n.getPercentage(ThreeState.B);
        double cPercent = n.getPercentage(ThreeState.C);
        //System.out.println("3 state: " + aPercent + " " + bPercent + " " + cPercent);

        boolean condition = (aPercent==1.0) && (bPercent==0.5) && (cPercent==0.0);
        Assert.assertTrue("incorrect a/b/c distribution", condition);
    }

    @Test public void testTwoState75() {
        SimpleBooleanNetwork<TwoState> n = new SimpleBooleanNetwork<TwoState>(TwoState.B);
        {
            n.learn(TwoState.A, true);
            n.learn(TwoState.A, true);
            n.learn(TwoState.A, true);
            n.learn(TwoState.A, false);  //::

            n.learn(TwoState.B, false);
            n.learn(TwoState.B, false);
            n.learn(TwoState.B, false);
            n.learn(TwoState.B, true);  //::
        }
        n.getGoal().setState(BooleanState.TRUE);

        double truePercent = n.getPercentage(TwoState.A);
        double falsePercent = n.getPercentage(TwoState.B);

        Assert.assertTrue("incorrect true/false distribution: "+ truePercent + ":" + falsePercent, (truePercent == 0.75) && (falsePercent == 0.25));
    }

    @Test public void testFeverState() {

        SimpleBooleanNetwork<FeverState> n = new SimpleBooleanNetwork<FeverState>(FeverState.HOT);
        {

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
        }

        n.getGoal().setState(BooleanState.TRUE);

        //check some probabilities
        double nonePercentage = n.getPercentage(FeverState.NONE);
        double lowPercentage = n.getPercentage(FeverState.LOW);
        double warmPercentage = n.getPercentage(FeverState.WARM);
        double hotPercentage = n.getPercentage(FeverState.HOT);

        boolean condition = (nonePercentage < lowPercentage) && (lowPercentage < warmPercentage) && (warmPercentage < hotPercentage);
        
        //System.out.println("fever to sickness mapping! " + nonePercentage + " < " + lowPercentage + " < " + warmPercentage + " < " + hotPercentage);
        Assert.assertTrue("incorrect fever to sickness mapping! " + nonePercentage + " < " + lowPercentage + " < " + warmPercentage + " < " + hotPercentage, condition);
    }
}
