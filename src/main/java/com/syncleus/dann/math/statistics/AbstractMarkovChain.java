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
package com.syncleus.dann.math.statistics;

public abstract class AbstractMarkovChain<S> implements MarkovChain<S> {
    @Override
    public S generateTransition() {
        return this.generateTransition(true);
    }

    @Override
    public S getCurrentState() {
        return this.getStateHistory().get(0);
    }

    @Override
    public double getProbability(final S futureState, final int steps) {
        return this.getProbability(steps).get(futureState);
    }

    @Override
    public double getSteadyStateProbability(final S futureState) {
        return this.getSteadyStateProbability().get(futureState);
    }
}
