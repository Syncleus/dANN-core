package com.syncleus.dann.graph;

import java.util.*;

public class MutableRootedTreeAdjacencyGraph<N, E extends DirectedEdge<N>> extends AbstractRootedTreeAdjacencyGraph<N, E> implements MutableRootedTreeGraph<N, E>
{
	private static final long serialVersionUID = -93813209434709L;

	public MutableRootedTreeAdjacencyGraph()
	{
		super();
	}

	public MutableRootedTreeAdjacencyGraph(final DirectedGraph<N, E> copyGraph)
	{
		super(copyGraph);
	}

	public MutableRootedTreeAdjacencyGraph(final Set<N> nodes, final Set<E> edges)
	{
		super(nodes, edges);
	}

	@Override
	public boolean add(final E newEdge)
	{
		if( newEdge == null )
			throw new IllegalArgumentException("newEdge can not be null");
		if( !this.getNodes().containsAll(newEdge.getNodes()) )
			throw new IllegalArgumentException("newEdge has a node as an end point that is not part of the graph");

		//if context is enabled lets check if it can join
		if( this.isContextEnabled() && (newEdge instanceof ContextGraphElement))
			if( !((ContextGraphElement)newEdge).joiningGraph(this) )
				return false;

		if( this.getInternalEdges().add(newEdge) )
		{
			for(final N currentNode : newEdge.getNodes())
			{
				this.getInternalAdjacencyEdges().get(currentNode).add(newEdge);

				final List<N> newAdjacentNodes = new ArrayList<N>(newEdge.getNodes());
				newAdjacentNodes.remove(currentNode);
				for(final N newAdjacentNode : newAdjacentNodes)
					this.getInternalAdjacencyNodes().get(currentNode).add(newAdjacentNode);
			}
			return true;
		}

		return false;
	}

	@Override
	public boolean add(final N newNode)
	{
		if( newNode == null )
			throw new IllegalArgumentException("newNode can not be null");

		if( this.getInternalAdjacencyEdges().containsKey(newNode) )
			return false;

		//if context is enabled lets check if it can join
		if( this.isContextEnabled() && (newNode instanceof ContextGraphElement))
			if( !((ContextGraphElement)newNode).joiningGraph(this) )
				return false;

		this.getInternalAdjacencyEdges().put(newNode, new HashSet<E>());
		this.getInternalAdjacencyNodes().put(newNode, new ArrayList<N>());
		return true;
	}

	@Override
	public boolean remove(final E edgeToRemove)
	{
		if( edgeToRemove == null )
			throw new IllegalArgumentException("removeSynapse can not be null");

		if( !this.getInternalEdges().contains(edgeToRemove) )
			return false;

		//if context is enabled lets check if it can join
		if( this.isContextEnabled() && (edgeToRemove instanceof ContextGraphElement))
			if( !((ContextGraphElement)edgeToRemove).leavingGraph(this) )
				return false;

		if( !this.getInternalEdges().remove(edgeToRemove) )
			return false;

		for(final N removeNode : edgeToRemove.getNodes())
		{
			this.getInternalAdjacencyEdges().get(removeNode).remove(edgeToRemove);

			final List<N> removeAdjacentNodes = new ArrayList<N>(edgeToRemove.getNodes());
			removeAdjacentNodes.remove(removeNode);
			for(final N removeAdjacentNode : removeAdjacentNodes)
				this.getInternalAdjacencyNodes().get(removeNode).remove(removeAdjacentNode);
		}
		return true;
	}

	@Override
	public boolean remove(final N nodeToRemove)
	{
		if( nodeToRemove == null )
			throw new IllegalArgumentException("node can not be null");

		if( !this.getInternalAdjacencyEdges().containsKey(nodeToRemove) )
			return false;

		//if context is enabled lets check if it can join
		if( this.isContextEnabled() && (nodeToRemove instanceof ContextGraphElement))
			if( !((ContextGraphElement)nodeToRemove).leavingGraph(this) )
				return false;

		final Set<E> removeEdges = this.getInternalAdjacencyEdges().get(nodeToRemove);

		//remove all the edges
		for(final E removeEdge : removeEdges)
			this.remove(removeEdge);

		//modify edges by removing the node to remove
		final Set<E> newEdges = new HashSet<E>();
		for(final E removeEdge : removeEdges)
		{
			E newEdge = (E) removeEdge.disconnect(nodeToRemove);
			while( (newEdge.getNodes().contains(nodeToRemove)) && (newEdge != null) )
				newEdge = (E) removeEdge.disconnect(nodeToRemove);
			if( newEdge != null )
				newEdges.add(newEdge);
		}

		//add the modified edges
		for(final E newEdge : newEdges)
			this.add(newEdge);

		//remove the node itself
		this.getInternalAdjacencyEdges().remove(nodeToRemove);
		this.getInternalAdjacencyNodes().remove(nodeToRemove);

		return true;
	}

	@Override
	public MutableRootedTreeAdjacencyGraph<N, E> cloneAdd(final E newEdge)
	{
		return (MutableRootedTreeAdjacencyGraph<N, E>) super.cloneAdd(newEdge);
	}

	@Override
	public MutableRootedTreeAdjacencyGraph<N, E> cloneAdd(final N newNode)
	{
		return (MutableRootedTreeAdjacencyGraph<N, E>) super.cloneAdd(newNode);
	}

	@Override
	public MutableRootedTreeAdjacencyGraph<N, E> cloneAdd(final Set<N> newNodes, final Set<E> newEdges)
	{
		return (MutableRootedTreeAdjacencyGraph<N, E>) super.cloneAdd(newNodes, newEdges);
	}

	@Override
	public MutableRootedTreeAdjacencyGraph<N, E> cloneRemove(final E edgeToRemove)
	{
		return (MutableRootedTreeAdjacencyGraph<N, E>) super.cloneRemove(edgeToRemove);
	}

	@Override
	public MutableRootedTreeAdjacencyGraph<N, E> cloneRemove(final N nodeToRemove)
	{
		return (MutableRootedTreeAdjacencyGraph<N, E>) super.cloneRemove(nodeToRemove);
	}

	@Override
	public MutableRootedTreeAdjacencyGraph<N, E> cloneRemove(final Set<N> deleteNodes, final Set<E> deleteEdges)
	{
		return (MutableRootedTreeAdjacencyGraph<N, E>) super.cloneRemove(deleteNodes, deleteEdges);
	}

	@Override
	public MutableRootedTreeAdjacencyGraph<N, E> clone()
	{
		return (MutableRootedTreeAdjacencyGraph<N, E>) super.clone();
	}
}
