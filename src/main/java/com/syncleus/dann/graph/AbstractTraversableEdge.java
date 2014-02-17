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
package com.syncleus.dann.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.syncleus.dann.UnexpectedDannError;
import com.syncleus.dann.graph.context.AbstractContextGraphElement;
import com.syncleus.dann.graph.context.ContextNode;
import com.syncleus.dann.graph.xml.EdgeElementXml;
import com.syncleus.dann.graph.xml.EdgeXml;
import com.syncleus.dann.xml.NameXml;
import com.syncleus.dann.xml.NamedValueXml;
import com.syncleus.dann.xml.Namer;
import com.syncleus.dann.xml.XmlSerializable;
import org.apache.log4j.Logger;

public abstract class AbstractTraversableEdge<N> extends AbstractContextGraphElement<Graph<N, ?>> implements TraversableEdge<N>
{
	private static final Logger LOGGER = Logger.getLogger(AbstractTraversableEdge.class);
	private final boolean contextEnabled;
	private List<N> nodes;

	protected AbstractTraversableEdge()
	{
		this(true, true);
	}

	protected AbstractTraversableEdge(final boolean allowJoiningMultipleGraphs, final boolean contextEnabled)
	{
		super(allowJoiningMultipleGraphs);
		this.contextEnabled = contextEnabled;
	}

	protected AbstractTraversableEdge(final List<N> ourNodes)
	{
		this(ourNodes, true, true);
	}

	protected AbstractTraversableEdge(final List<N> ourNodes, final boolean allowJoiningMultipleGraphs, final boolean contextEnabled)
	{
		super(allowJoiningMultipleGraphs);
		this.contextEnabled = contextEnabled;

		//make sure each node with context allows us to connect to it
		if(contextEnabled)
		{
			final List<N> nodesCopy = new ArrayList<N>(ourNodes.size());
			for(N ourNode : ourNodes)
			{
				if( this.contextEnabled && ( ourNode instanceof ContextNode ) && ( !((ContextNode)ourNode).connectingEdge(this) ))
					continue;
				nodesCopy.add(ourNode);
			}
			this.nodes = Collections.unmodifiableList(new ArrayList<N>(nodesCopy));
		}
		else
			this.nodes = Collections.unmodifiableList(new ArrayList<N>(ourNodes));
	}

	protected AbstractTraversableEdge(final N... ourNodes)
	{
		this(true, true, ourNodes);
	}

	protected AbstractTraversableEdge(final boolean allowJoiningMultipleGraphs, final boolean contextEnabled, final N... ourNodes)
	{
		this(Arrays.asList(ourNodes), allowJoiningMultipleGraphs, contextEnabled);
	}

	@Override
	public boolean isContextEnabled()
	{
		return this.contextEnabled;
	}

	protected AbstractTraversableEdge<N> add(final N node)
	{
		if( node == null )
			throw new IllegalArgumentException("node can not be null");

		final List<N> newNodes = new ArrayList<N>(this.nodes);
		newNodes.add(node);

		return createDeepCopy(newNodes);
	}

	protected AbstractTraversableEdge<N> add(final List<N> addNodes)
	{
		if( addNodes == null )
			throw new IllegalArgumentException("node can not be null");
		final List<N> newNodes = new ArrayList<N>(this.nodes);
		newNodes.addAll(addNodes);

		return createDeepCopy(newNodes);
	}

	protected AbstractTraversableEdge<N> remove(final N node)
	{
		if( node == null )
			throw new IllegalArgumentException("node can not be null");
		if( !this.nodes.contains(node) )
			throw new IllegalArgumentException("is not an endpoint");

		final List<N> newNodes = new ArrayList<N>(this.nodes);
		newNodes.remove(node);

		return createDeepCopy(newNodes);
	}

	protected AbstractTraversableEdge<N> remove(final List<N> removeNodes)
	{
		if( removeNodes == null )
			throw new IllegalArgumentException("removeNodes can not be null");
		if( !this.nodes.containsAll(removeNodes) )
			throw new IllegalArgumentException("removeNodes do not contain all valid end points");
		final List<N> newNodes = new ArrayList<N>(this.nodes);
		for(final N node : removeNodes)
			newNodes.remove(node);

		return createDeepCopy(newNodes);
	}

	/**
	 * Create a deep copy of this edge, but with a new set of nodes.
	 * @param newNodes the set of nodes to use instead of the current ones.
	 * @return a deep copy of this edge, but with a new set of nodes.
	 */
	private AbstractTraversableEdge<N> createDeepCopy(final List<N> newNodes)
	{
		try
		{
			final AbstractTraversableEdge<N> clonedEdge = (AbstractTraversableEdge<N>) super.clone();
			final List<N> clonedNodes = new ArrayList<N>(this.nodes.size());
			//add each node at a time to the clone considering context
			for(N newNode : newNodes)
			{
				if( this.contextEnabled && (newNode instanceof ContextNode) && ( !((ContextNode)newNode).connectingEdge(clonedEdge) ) )
					continue;
				clonedNodes.add(newNode);
			}
			clonedEdge.nodes = Collections.unmodifiableList(clonedNodes);
			return clonedEdge;
		}
		catch(CloneNotSupportedException caught)
		{
			LOGGER.error("Edge was unexpectidly not cloneable", caught);
			throw new UnexpectedDannError("Edge was unexpectidly not cloneable", caught);
		}
	}

	@Override
	public boolean isTraversable(final N node)
	{
		return (!this.getTraversableNodes(node).isEmpty());
	}

	@Override
	public final List<N> getNodes()
	{
		return this.nodes;
	}

	@Override
	public String toString()
	{
		final StringBuilder outString = new StringBuilder(this.nodes.size() * 10);
		for(final N node : this.nodes)
		{
			outString.append(':').append(node);
		}
		return outString.toString();
	}

    @Override
	public EdgeXml toXml()
	{
		final Namer namer = new Namer();
		final EdgeElementXml xml = new EdgeElementXml();

		xml.setNodeInstances(new EdgeElementXml.NodeInstances());
		final Set<N> writtenNodes = new HashSet<N>();
		for (N node : this.nodes)
		{
			if (writtenNodes.add(node))
			{
				final NamedValueXml named = new NamedValueXml();
				named.setName(namer.getNameOrCreate(node));
				if (node instanceof XmlSerializable)
				{
					named.setValue(((XmlSerializable) node).toXml(namer));
				}
				else
				{
					named.setValue(node);
				}
				xml.getNodeInstances().getNodes().add(named);
			}
		}
		this.toXml(xml, namer);

		return xml;
	}

	@Override
	public EdgeXml toXml(final Namer<Object> nodeNames)
	{
		if (nodeNames == null)
		{
			throw new IllegalArgumentException("nodeNames can not be null");
		}

		final EdgeXml xml = new EdgeXml();
		this.toXml(xml, nodeNames);
		return xml;
	}

	@Override
	public void toXml(final EdgeXml jaxbObject, final Namer<Object> nodeNames)
	{
		if (nodeNames == null)
		{
			throw new IllegalArgumentException("nodeNames can not be null");
		}
		if (jaxbObject == null)
		{
			throw new IllegalArgumentException("jaxbObject can not be null");
		}

		if (jaxbObject.getConnections() == null)
		{
			jaxbObject.setConnections(new EdgeXml.Connections());
		}
		for (N node : this.nodes)
		{
			final NameXml connection = new NameXml();
			connection.setName(nodeNames.getNameOrCreate(node));
			jaxbObject.getConnections().getNodes().add(connection);
		}
	}
}
