/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Jeffrey Phillips Freeman                                              *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by Jeffrey Phillips Freeman at www.syncleus.com.   *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact Jeffrey Phillips Freeman at the information below if you cannot  *
 *  find a license:                                                            *
 *                                                                             *
 *  Jeffrey Phillips Freeman                                                             *
 *  2604 South 12th Street                                                     *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.dann.graphicalmodel.bayesian.dynamic;

import com.syncleus.dann.graphicalmodel.bayesian.BayesianNetwork;
import com.syncleus.dann.graphicalmodel.bayesian.BayesianNode;
import com.syncleus.dann.graphicalmodel.bayesian.SimpleBayesianNode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SimpleDynamicBayesianNode<S> extends SimpleBayesianNode<S> implements DynamicBayesianNode<S>
{
	//0 index is most recent
	private final List<BayesianNode<S>> historicalNodes;

	public SimpleDynamicBayesianNode(int historyCapacity, S initialState, BayesianNetwork network)
	{
		super(initialState,network);
		
		if(historyCapacity < 0)
			throw new IllegalArgumentException("historyCapacity can not be less than 0");
		if(initialState == null)
			throw new IllegalArgumentException("initialState can not be null");
		if(network == null)
			throw new IllegalArgumentException("network can not be null");

		List<BayesianNode<S>> newHistoricalNodes = new ArrayList<BayesianNode<S>>(historyCapacity);
		for(int historyIndex = 0; historyIndex < historyCapacity; historyIndex++)
			newHistoricalNodes.add(new SimpleBayesianNode<S>(null, network));
		this.historicalNodes = Collections.unmodifiableList(newHistoricalNodes);
	}

	public SimpleDynamicBayesianNode(List<S> history, S initialState, BayesianNetwork network)
	{
		super(initialState,network);

		if(history == null)
			throw new IllegalArgumentException("history can not be null");
		if(initialState == null)
			throw new IllegalArgumentException("initialState can not be null");
		if(network == null)
			throw new IllegalArgumentException("network can not be null");

		List<BayesianNode<S>> newHistoricalNodes = new ArrayList<BayesianNode<S>>(history.size());
		for(int historyIndex = 0; historyIndex < history.size(); historyIndex++)
			newHistoricalNodes.add(new SimpleBayesianNode<S>(history.get(historyIndex), network));

		this.historicalNodes = Collections.unmodifiableList(newHistoricalNodes);
	}
	
	public int getStateHistoryCapacity()
	{
		return this.historicalNodes.size();
	}

	public List<S> getStateHistory()
	{
		List<S> historyStates = new ArrayList<S>(this.getStateHistoryCapacity());
		for(BayesianNode<S> node : this.historicalNodes)
			historyStates.add(node.getState());
		return Collections.unmodifiableList(historyStates);
	}

	public void setStateHistory(List<S> history)
	{
		for(int historyIndex = 0; historyIndex < this.historicalNodes.size(); historyIndex++)
			this.historicalNodes.get(historyIndex).setState( (history.size() > historyIndex ? history.get(historyIndex) : null) );
	}

	public void learnState(boolean updateHistory)
	{
		super.learnState();

		if(updateHistory)
		{
			//move the state down the line making each state one step older
			for(int historyIndex = 0; historyIndex < (this.historicalNodes.size() - 1); historyIndex++)
				this.historicalNodes.get(historyIndex+1).setState(this.historicalNodes.get(historyIndex).getState());

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
		Set<BayesianNode> influences = new HashSet<BayesianNode>(super.getInfluencingNodes());
		influences.addAll(this.historicalNodes);
		return Collections.unmodifiableSet(influences);
	}
}
