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

import java.util.*;

public class SimpleMarkovChainEvidence<S> implements MarkovChainEvidence<S>
{
	private final int order;
	private final ArrayDeque<S> history;
	private final boolean isArbitraryStart;
	private final Map<List<S>, StateCounter<S>> evidence;
	private final Set<S> observedStates;

	public SimpleMarkovChainEvidence(final boolean isArbitraryStart, final int order)
	{
		this.history = new ArrayDeque<S>(order);
		this.order = order;
		this.isArbitraryStart = isArbitraryStart;
		this.evidence = new HashMap<List<S>, StateCounter<S>>();
		this.observedStates = new HashSet<S>();
		// TODO observed states shouldnt need a null, instead the markov chain should know its an implicit state (it fails without this)
		this.observedStates.add(null);
	}

	public void newChain()
	{
		this.history.clear();
	}

	private void learnFromMemory(final Collection<S> stateMemoryCollection, final S nextState)
	{
		final List<S> stateMemory = Collections.unmodifiableList(new ArrayList<S>(stateMemoryCollection));
		
		//get the current evidence for this state
		StateCounter<S> transitions = this.evidence.get(stateMemory);
		//if there is no transistions then create a blank one
		if( transitions == null )
		{
			transitions = new StateCounter<S>();
			this.evidence.put(stateMemory, transitions);
		}

		//update the transitions evidence for the new state
		transitions.increment(nextState);
	}

	public void learnStep(final S state)
	{
		//update the evidence
		learnFromMemory(this.history, state);

		//if there is an arbitrary starting place update the evidance for the
		//various sub-states of shorter order
		if( (this.isArbitraryStart) && (this.order > 1) )
		{
			final ArrayDeque<S> trainingMemory = new ArrayDeque<S>(this.history);
			while( trainingMemory.size() > 1 )
			{
				trainingMemory.poll();
				learnFromMemory(trainingMemory, state);
			}
		}

		//update the history
		this.history.add(state);
		while( this.history.size() > this.order )
			this.history.poll();

		//update the set of observed states
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

	public MarkovChain<S> getMarkovChain()
	{
		Map<List<S>, Map<S,Double>> transitionProbabilities = new HashMap<List<S>, Map<S,Double>>(this.evidence.size());
		for(Map.Entry<List<S>,StateCounter<S>> countEntry : this.evidence.entrySet())
			transitionProbabilities.put(countEntry.getKey(), countEntry.getValue().probabilities());
		return new SimpleMarkovChain<S>(transitionProbabilities, this.order, this.observedStates);
	}

	private static class StateCounter<S>
	{
		private final Map<S, Integer> stateCount = new HashMap<S,Integer>();
		private long totalEvidence;

		public StateCounter()
		{
		}

		public void increment(S state)
		{
			Integer count = this.stateCount.get(state);
			if(count == null)
				count = 1;
			else
				count++;
			this.stateCount.put(state, count);
			this.totalEvidence++;
		}

		public double probability(S state)
		{
			Integer count = this.stateCount.get(state);
			if(count == null)
				count = 0;
			return count.doubleValue() / ((double)totalEvidence);
		}

		public Map<S, Double> probabilities()
		{
			Map<S, Double> prob = new HashMap<S,Double>(this.stateCount.size());
			for(Map.Entry<S,Integer> countEntry : this.stateCount.entrySet())
				prob.put(countEntry.getKey(), countEntry.getValue().doubleValue() / ((double)this.totalEvidence));
			return prob;
		}
	}
}
