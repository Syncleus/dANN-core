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
package com.syncleus.dann.graphicalmodel.dynamic;

import com.syncleus.dann.graph.BidirectedEdge;
import com.syncleus.dann.graphicalmodel.*;

import java.util.*;

public class SimpleDynamicGraphicalModelNode<S> extends SimpleGraphicalModelNode<S> implements DynamicGraphicalModelNode<S> {
    //0 index is most recent
    private final List<SimpleGraphicalModelNode<S>> historicalNodes;

    public SimpleDynamicGraphicalModelNode(final int historyCapacity, final S initialState) {
        super(initialState);

        if (historyCapacity < 0)
            throw new IllegalArgumentException("historyCapacity can not be less than 0");
        if (initialState == null)
            throw new IllegalArgumentException("initialState can not be null");

        final List<SimpleGraphicalModelNode<S>> newHistoricalNodes = new ArrayList<SimpleGraphicalModelNode<S>>(historyCapacity);
        for (int historyIndex = 0; historyIndex < historyCapacity; historyIndex++)
            newHistoricalNodes.add(new SimpleGraphicalModelNode<S>(null));
        this.historicalNodes = Collections.unmodifiableList(newHistoricalNodes);
    }

    public SimpleDynamicGraphicalModelNode(final List<S> history, final S initialState) {
        super(initialState);
        if (history == null)
            throw new IllegalArgumentException("history can not be null");
        if (initialState == null)
            throw new IllegalArgumentException("initialState can not be null");
        final List<SimpleGraphicalModelNode<S>> newHistoricalNodes = new ArrayList<SimpleGraphicalModelNode<S>>(history.size());
        for (final S aHistory : history)
            newHistoricalNodes.add(new SimpleGraphicalModelNode<S>(aHistory));
        this.historicalNodes = Collections.unmodifiableList(newHistoricalNodes);
    }

    /**
     * If we leave a network, lets clear the states.
     */
    @Override
    public boolean joiningGraph(final GraphicalModel<GraphicalModelNode<S>, BidirectedEdge<GraphicalModelNode<S>>> graph) {
        if (super.joiningGraph(graph)) {
            //let all our historical nodes also know were leaves
            for (SimpleGraphicalModelNode<S> historicalNode : this.historicalNodes)
                if (!historicalNode.joiningGraph(graph))
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
    public boolean leavingGraph(final GraphicalModel<GraphicalModelNode<S>, BidirectedEdge<GraphicalModelNode<S>>> graph) {
        if (super.leavingGraph(graph)) {
            //let all our historical nodes also know were leaves
            for (SimpleGraphicalModelNode<S> historicalNode : this.historicalNodes)
                if (!historicalNode.leavingGraph(graph))
                    throw new IllegalStateException("historical node will not detach from graph when its parent will");
            return true;
        }
        else
            return false;
    }

    @Override
    public int getStateHistoryCapacity() {
        return this.historicalNodes.size();
    }

    @Override
    public List<S> getStateHistory() {
        final List<S> historyStates = new ArrayList<S>(this.getStateHistoryCapacity());
        for (final SimpleGraphicalModelNode<S> node : this.historicalNodes)
            historyStates.add(node.getState());
        return Collections.unmodifiableList(historyStates);
    }

    @Override
    public void setStateHistory(final List<S> history) {
        for (int historyIndex = 0; historyIndex < this.historicalNodes.size(); historyIndex++)
            this.historicalNodes.get(historyIndex).setState((history.size() > historyIndex ? history.get(historyIndex) : null));
    }

    @Override
    public void learnState(final boolean updateHistory) {
        super.learnState();

        if (updateHistory) {
            //move the state down the line making each state one step older
            for (int historyIndex = 0; historyIndex < (this.historicalNodes.size() - 1); historyIndex++)
                this.historicalNodes.get(historyIndex + 1).setState(this.historicalNodes.get(historyIndex).getState());

            //add the new state to history
            this.historicalNodes.get(0).setState(this.getState());
        }
    }

    @Override
    public void learnState() {
        this.learnState(true);
    }

    @Override
    protected Set<GraphicalModelNode> getInfluencingNodes() {
        final Set<GraphicalModelNode> influences = new HashSet<GraphicalModelNode>(super.getInfluencingNodes());
        influences.addAll(this.historicalNodes);
        return Collections.unmodifiableSet(influences);
    }
}
