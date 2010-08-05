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

import java.util.*;
import com.syncleus.dann.graphicalmodel.bayesian.*;
import com.syncleus.dann.xml.Namer;
import com.syncleus.dann.graphicalmodel.bayesian.xml.BayesianNodeXml;

public class SimpleDynamicBayesianNode<S> extends SimpleBayesianNode<S> implements DynamicBayesianNode<S>
{
	//0 index is most recent
	private final List<BayesianNode<S>> historicalNodes;

	public SimpleDynamicBayesianNode(final int historyCapacity, final S initialState, final BayesianNetwork network)
	{
		super(initialState, network);

		if( historyCapacity < 0 )
			throw new IllegalArgumentException("historyCapacity can not be less than 0");
		if( initialState == null )
			throw new IllegalArgumentException("initialState can not be null");
		if( network == null )
			throw new IllegalArgumentException("network can not be null");

		final List<BayesianNode<S>> newHistoricalNodes = new ArrayList<BayesianNode<S>>(historyCapacity);
		for(int historyIndex = 0; historyIndex < historyCapacity; historyIndex++)
			newHistoricalNodes.add(new SimpleBayesianNode<S>(null, network));
		this.historicalNodes = Collections.unmodifiableList(newHistoricalNodes);
	}

	public SimpleDynamicBayesianNode(final List<S> history, final S initialState, final BayesianNetwork network)
	{
		super(initialState, network);
		if( history == null )
			throw new IllegalArgumentException("history can not be null");
		if( initialState == null )
			throw new IllegalArgumentException("initialState can not be null");
		if( network == null )
			throw new IllegalArgumentException("network can not be null");
		final List<BayesianNode<S>> newHistoricalNodes = new ArrayList<BayesianNode<S>>(history.size());
		for(final S aHistory : history)
			newHistoricalNodes.add(new com.syncleus.dann.graphicalmodel.bayesian.SimpleBayesianNode<S>(aHistory, network));
		this.historicalNodes = Collections.unmodifiableList(newHistoricalNodes);
	}

	public int getStateHistoryCapacity()
	{
		return this.historicalNodes.size();
	}

	public List<S> getStateHistory()
	{
		final List<S> historyStates = new ArrayList<S>(this.getStateHistoryCapacity());
		for(final BayesianNode<S> node : this.historicalNodes)
			historyStates.add(node.getState());
		return Collections.unmodifiableList(historyStates);
	}

	public void setStateHistory(final List<S> history)
	{
		for(int historyIndex = 0; historyIndex < this.historicalNodes.size(); historyIndex++)
			this.historicalNodes.get(historyIndex).setState((history.size() > historyIndex ? history.get(historyIndex) : null));
	}

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

	public void toXml(BayesianNodeXml jaxbObject, Namer namer) {
		throw new UnsupportedOperationException("toXML not yet implemented");
	}
}
