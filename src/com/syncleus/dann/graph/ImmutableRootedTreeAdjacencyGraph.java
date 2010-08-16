package com.syncleus.dann.graph;

import java.util.*;

public class ImmutableRootedTreeAdjacencyGraph<N, E extends DirectedEdge<N>> extends AbstractRootedTreeAdjacencyGraph<N, E>
{
	private static final long serialVersionUID = 723494372937234023L;

	public ImmutableRootedTreeAdjacencyGraph()
	{
		super();
	}

	public ImmutableRootedTreeAdjacencyGraph(final Graph<N, E> copyGraph)
	{
		super(copyGraph.getNodes(), copyGraph.getEdges());
	}

	public ImmutableRootedTreeAdjacencyGraph(final Set<N> nodes, final Set<E> edges)
	{
		super(nodes, edges);
	}

	@Override
	protected Set<E> getInternalEdges()
	{
		return Collections.unmodifiableSet(super.getInternalEdges());
	}

	@Override
	protected Map<N, Set<E>> getInternalAdjacencyEdges()
	{
		final Map<N, Set<E>> newAdjacentEdges = new HashMap<N, Set<E>>();
		for(final Map.Entry<N, Set<E>> neighborEdgeEntry : super.getInternalAdjacencyEdges().entrySet())
			newAdjacentEdges.put(neighborEdgeEntry.getKey(), new HashSet<E>(neighborEdgeEntry.getValue()));
		return newAdjacentEdges;
	}

	@Override
	protected Map<N, List<N>> getInternalAdjacencyNodes()
	{
		final Map<N, List<N>> newAdjacentNodes = new HashMap<N, List<N>>();
		for(final Map.Entry<N, List<N>> neighborNodeEntry : super.getInternalAdjacencyNodes().entrySet())
			newAdjacentNodes.put(neighborNodeEntry.getKey(), new ArrayList<N>(neighborNodeEntry.getValue()));
		return newAdjacentNodes;
	}

	@Override
	public ImmutableRootedTreeAdjacencyGraph<N, E> cloneAdd(final E newEdge)
	{
		return (ImmutableRootedTreeAdjacencyGraph<N, E>) super.cloneAdd(newEdge);
	}

	@Override
	public ImmutableRootedTreeAdjacencyGraph<N, E> cloneAdd(final N newNode)
	{
		return (ImmutableRootedTreeAdjacencyGraph<N, E>) super.cloneAdd(newNode);
	}

	@Override
	public ImmutableRootedTreeAdjacencyGraph<N, E> cloneAdd(final Set<N> newNodes, final Set<E> newEdges)
	{
		return (ImmutableRootedTreeAdjacencyGraph<N, E>) super.cloneAdd(newNodes, newEdges);
	}

	@Override
	public ImmutableRootedTreeAdjacencyGraph<N, E> cloneRemove(final E edgeToRemove)
	{
		return (ImmutableRootedTreeAdjacencyGraph<N, E>) super.cloneRemove(edgeToRemove);
	}

	@Override
	public ImmutableRootedTreeAdjacencyGraph<N, E> cloneRemove(final N nodeToRemove)
	{
		return (ImmutableRootedTreeAdjacencyGraph<N, E>) super.cloneRemove(nodeToRemove);
	}

	@Override
	public ImmutableRootedTreeAdjacencyGraph<N, E> cloneRemove(final Set<N> deleteNodes, final Set<E> deleteEdges)
	{
		return (ImmutableRootedTreeAdjacencyGraph<N, E>) super.cloneRemove(deleteNodes, deleteEdges);
	}

	@Override
	public ImmutableRootedTreeAdjacencyGraph<N, E> clone()
	{
		return (ImmutableRootedTreeAdjacencyGraph<N, E>) super.clone();
	}
}
