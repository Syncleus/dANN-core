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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimpleMarkovChainEvidence<S> implements MarkovChainEvidence<S>
{
	private final int order;
	private final ArrayDeque<S> history;
	private final boolean isArbitraryStart;
	private final Map<List<S>, Map<S, Integer>> evidence;
	private final Set<S> observedStates;

	public SimpleMarkovChainEvidence(final boolean isArbitraryStart, final int order)
	{
		this.history = new ArrayDeque<S>(order);
		this.order = order;
		this.isArbitraryStart = isArbitraryStart;
		this.evidence = new HashMap<List<S>, Map<S, Integer>>();
		this.observedStates = new HashSet<S>();
	}

	public void newChain()
	{
		this.history.clear();
	}

	private void learnFromMemroy(final Collection<S> stateMemoryCollection, final S nextState)
	{
		final List<S> stateMemory = Collections.unmodifiableList(new ArrayList<S>(stateMemoryCollection));
		//get the current evidence for this state
		Map<S, Integer> transitions = this.evidence.get(stateMemory);
		//if there is no transistions then create a blank one
		if(transitions == null)
		{
			transitions = new HashMap<S,Integer>();
			this.evidence.put(stateMemory, transitions);
		}

		//update the transitions evidence for the new state
		Integer transition = transitions.get(nextState);
		if(transition == null)
			transition = 0;
		transitions.put(nextState, transition++);
	}

	public void learnStep(final S state)
	{
		final ArrayDeque<S> trainingMemory = new ArrayDeque<S>(this.history);
		learnFromMemroy(trainingMemory, state);

		if(this.isArbitraryStart)
		{
			while(trainingMemory.size() > 1)
			{
				trainingMemory.poll();
				learnFromMemroy(trainingMemory, state);
			}
		}

		this.history.add(state);
		while(this.history.size() > this.order)
			this.history.poll();

		this.observedStates.add(state);
	}

	public Set<S> getObservedStates()
	{
		return Collections.unmodifiableSet(this.observedStates);
	}

	public int getOrder()
	{
		return this.order;
	}

	public boolean isArbitraryStart()
	{
		return this.isArbitraryStart;
	}

	public MarkovChain getMarkovChain()
	{
		return null;
	}
}
