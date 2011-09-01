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
package com.syncleus.dann.graphicalmodel.bayesian.dynamic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.syncleus.dann.graphicalmodel.bayesian.BayesianEdge;
import com.syncleus.dann.graphicalmodel.bayesian.BayesianNetwork;
import com.syncleus.dann.graphicalmodel.bayesian.BayesianNode;
import com.syncleus.dann.graphicalmodel.bayesian.SimpleBayesianNode;

public class SimpleDynamicBayesianNode<S> extends SimpleBayesianNode<S> implements DynamicBayesianNode<S>
{
	//0 index is most recent
	private final List<SimpleBayesianNode<S>> historicalNodes;

	public SimpleDynamicBayesianNode(final int historyCapacity, final S initialState)
	{
		super(initialState);

		if( historyCapacity < 0 )
			throw new IllegalArgumentException("historyCapacity can not be less than 0");
		if( initialState == null )
			throw new IllegalArgumentException("initialState can not be null");

		final List<SimpleBayesianNode<S>> newHistoricalNodes = new ArrayList<SimpleBayesianNode<S>>(historyCapacity);
		for(int historyIndex = 0; historyIndex < historyCapacity; historyIndex++)
			newHistoricalNodes.add(new SimpleBayesianNode<S>(null));
		this.historicalNodes = Collections.unmodifiableList(newHistoricalNodes);
	}

	public SimpleDynamicBayesianNode(final List<S> history, final S initialState)
	{
		super(initialState);
		if( history == null )
			throw new IllegalArgumentException("history can not be null");
		if( initialState == null )
			throw new IllegalArgumentException("initialState can not be null");
		final List<SimpleBayesianNode<S>> newHistoricalNodes = new ArrayList<SimpleBayesianNode<S>>(history.size());
		for(final S aHistory : history)
			newHistoricalNodes.add(new com.syncleus.dann.graphicalmodel.bayesian.SimpleBayesianNode<S>(aHistory));
		this.historicalNodes = Collections.unmodifiableList(newHistoricalNodes);
	}

	/**
	 * If we leave a network, lets clear the states.
	 */
	@Override
	public boolean joiningGraph(final BayesianNetwork<BayesianNode<S>, BayesianEdge<BayesianNode<S>>> graph)
	{
		if( super.joiningGraph(graph) )
		{
			//let all our historical nodes also know were leaves
			for(SimpleBayesianNode<S> historicalNode : this.historicalNodes)
				if( !historicalNode.joiningGraph(graph))
					throw new IllegalStateException("historical node will not attach to graph when its parent will");
			return true;
		}
		else
			return false;
	}

	/**
	 * If we leave a network, lets clear the states.
	 */
	@Override
	public boolean leavingGraph(final BayesianNetwork<BayesianNode<S>, BayesianEdge<BayesianNode<S>>> graph)
	{
		if( super.leavingGraph(graph) )
		{
			//let all our historical nodes also know were leaves
			for(SimpleBayesianNode<S> historicalNode : this.historicalNodes)
				if( !historicalNode.leavingGraph(graph))
					throw new IllegalStateException("historical node will not detach from graph when its parent will");
			return true;
		}
		else
			return false;
	}

	@Override
	public int getStateHistoryCapacity()
	{
		return this.historicalNodes.size();
	}

	@Override
	public List<S> getStateHistory()
	{
		final List<S> historyStates = new ArrayList<S>(this.getStateHistoryCapacity());
		for(final BayesianNode<S> node : this.historicalNodes)
			historyStates.add(node.getState());
		return Collections.unmodifiableList(historyStates);
	}

	@Override
	public void setStateHistory(final List<S> history)
	{
		for(int historyIndex = 0; historyIndex < this.historicalNodes.size(); historyIndex++)
			this.historicalNodes.get(historyIndex).setState((history.size() > historyIndex ? history.get(historyIndex) : null));
	}

	@Override
	public void learnState(final boolean updateHistory)
	{
		super.learnState();

		if( updateHistory )
		{
			//move the state down the line making each state one step older
			for(int historyIndex = 0; historyIndex < (this.historicalNodes.size() - 1); historyIndex++)
				this.historicalNodes.get(historyIndex + 1).setState(this.historicalNodes.get(historyIndex).getState());

			//add the new state to history
			this.historicalNodes.get(0).setState(this.getState());
		}
	}

	@Override
	public void learnState()
	{
		this.learnState(true);
	}

	@Override
	protected Set<BayesianNode> getInfluencingNodes()
	{
		final Set<BayesianNode> influences = new HashSet<BayesianNode>(super.getInfluencingNodes());
		influences.addAll(this.historicalNodes);
		return Collections.unmodifiableSet(influences);
	}
}
