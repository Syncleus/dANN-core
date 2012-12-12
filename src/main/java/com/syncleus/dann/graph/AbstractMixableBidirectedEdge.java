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


import java.util.*;

public abstract class AbstractMixableBidirectedEdge<
	    E extends MixableBidirectedEdge.Endpoint<?>,
	  	LE extends E,
	  	RE extends E
	  > extends AbstractMixableEdge<E, LE, RE> implements MixableBidirectedEdge<E, LE, RE>
{
	@Override
	public boolean isIntroverted()
	{
		return (this.getRightEndpoint().getDirection() == MixableBidirectedEdge.Endpoint.Direction.INWARD) && (this.getLeftEndpoint().getDirection() == MixableBidirectedEdge.Endpoint.Direction.INWARD);
	}

	@Override
	public boolean isExtroverted()
	{
		return (this.getRightEndpoint().getDirection() == MixableBidirectedEdge.Endpoint.Direction.OUTWARD) && (this.getLeftEndpoint().getDirection() == MixableBidirectedEdge.Endpoint.Direction.OUTWARD);
	}

	@Override
	public boolean isDirected()
	{
		if( (this.getRightEndpoint().getDirection() == MixableBidirectedEdge.Endpoint.Direction.INWARD) && (this.getLeftEndpoint().getDirection() == MixableBidirectedEdge.Endpoint.Direction.OUTWARD) )
			return true;
		else if( (this.getRightEndpoint().getDirection() == MixableBidirectedEdge.Endpoint.Direction.OUTWARD) && (this.getLeftEndpoint().getDirection() == MixableBidirectedEdge.Endpoint.Direction.INWARD) )
			return true;
		return false;
	}

	@Override
	public boolean isHalfEdge()
	{
		if( (this.getRightEndpoint().getDirection() == MixableBidirectedEdge.Endpoint.Direction.NONE) && (this.getLeftEndpoint().getDirection() != MixableBidirectedEdge.Endpoint.Direction.NONE) )
			return true;
		else if( (this.getRightEndpoint().getDirection() != MixableBidirectedEdge.Endpoint.Direction.NONE) && (this.getLeftEndpoint().getDirection() == MixableBidirectedEdge.Endpoint.Direction.NONE) )
			return true;
		return false;
	}

	@Override
	public boolean isLooseEdge()
	{
		return (this.getRightEndpoint().getDirection() == MixableBidirectedEdge.Endpoint.Direction.NONE) && (this.getLeftEndpoint().getDirection() == MixableBidirectedEdge.Endpoint.Direction.NONE);
	}

	@Override
	public boolean isOrdinaryEdge()
	{
		return (!this.isHalfEdge()) && (!this.isLooseEdge());
	}

	@Override
	public boolean isLoop()
	{
		return this.getLeftEndpoint().getDirection().equals(this.getRightEndpoint().getDirection());
	}

	@Override
	public Set<E> getTraversableFrom(Cloud.Endpoint<?> source)
	{
		final Set<E> traversableEndpoints = new HashSet<E>();
		for( final E toEndpoint : getNeighbors(source))
			if( this.isTraversable(source, toEndpoint) )
				traversableEndpoints.add(toEndpoint);

		return Collections.unmodifiableSet(traversableEndpoints);
	}

	@Override
	public Set<E> getTraversableTo(Cloud.Endpoint<?> destination)
	{
		final Set<E> traversableEndpoints = new HashSet<E>();
		for( final E fromEndpoint : getNeighbors(destination) )
			if( this.isTraversable(fromEndpoint, destination))
				traversableEndpoints.add(fromEndpoint);

		return Collections.unmodifiableSet(traversableEndpoints);
	}

	@Override
	public boolean isTraversableFrom(Cloud.Endpoint<?> source)
	{
		return !this.getTraversableFrom(source).isEmpty();
	}

	@Override
	public boolean isTraversableTo(Cloud.Endpoint<?> destination)
	{
		return !this.getTraversableTo(destination).isEmpty();
	}

	@Override
	public String toString()
	{
		return this.getLeftEndpoint().getTarget().toString()
				+ endStateToString(this.getLeftEndpoint().getDirection(), true)
				+ '-'
				+ endStateToString(this.getRightEndpoint().getDirection(), false)
				+ this.getRightEndpoint().getTarget();
	}

	private static String endStateToString(final MixableBidirectedEdge.Endpoint.Direction state, final boolean isLeft)
	{
		switch(state)
		{
		case INWARD:
			return (isLeft ? ">" : "<");
		case OUTWARD:
			return (isLeft ? "<" : ">");
		default:
			return "";
		}
	}

	@Override
	protected AbstractMixableBidirectedEdge<E,LE,RE> clone()
	{
		return (AbstractMixableBidirectedEdge<E,LE,RE>) super.clone();
	}

/*
	@Override
	public BidirectedEdgeXml toXml()
	{
		final Namer namer = new Namer();
		final BidirectedEdgeElementXml xml = new BidirectedEdgeElementXml();

		xml.setNodeInstances(new BidirectedEdgeElementXml.NodeInstances());
		final Set<N> writtenNodes = new HashSet<N>();
		for (N node : this.getTargets())
		{
			if (writtenNodes.add(node))
			{
				final NamedValueXml named = new NamedValueXml();
				named.setName(namer.getNameOrCreate(node));
				if (node instanceof XmlSerializable )
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

		return xml;
	}

	@Override
	public BidirectedEdgeXml toXml(final Namer<Object> nodeNames)
	{
		if (nodeNames == null)
		{
			throw new IllegalArgumentException("nodeNames can not be null");
		}

		final BidirectedEdgeXml xml = new BidirectedEdgeXml();
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

		super.toXml(jaxbObject, nodeNames);

		if (jaxbObject instanceof BidirectedEdgeXml)
		{
			((BidirectedEdgeXml) jaxbObject).setLeftNode(nodeNames.getNameOrCreate(this.getLeftEndpoint().getTarget()));
			((BidirectedEdgeXml) jaxbObject).setRightNode(nodeNames.getNameOrCreate(this.getRightEndpoint().getTarget()));
			((BidirectedEdgeXml) jaxbObject).setLeftDirection(this.getLeftEndpoint().getDirection().toString().toLowerCase());
			((BidirectedEdgeXml) jaxbObject).setRightDirection(this.getRightEndpoint().getDirection().toString().toLowerCase());
		}
	}
*/

	protected abstract class AbstractEndpoint<T> extends AbstractMixableEdge<E,LE,RE>.AbstractEndpoint<T> implements MixableBidirectedEdge.Endpoint<T>
	{
		private Direction direction;

		protected AbstractEndpoint(Direction direction)
		{
			super();

			if( direction == null )
				throw new IllegalArgumentException("direction can not be null!");
			this.direction = direction;
		}

		protected AbstractEndpoint(T target, Direction direction)
		{
			super(target);

			if( direction == null )
				throw new IllegalArgumentException("direction can not be null!");
			this.direction = direction;
		}

		@Override
		public Direction getDirection()
		{
			return this.direction;
		}

		protected void setDirection(final Direction direction)
		{
			this.direction = direction;
		}

		@Override
		public boolean isTraversableFrom(Cloud.Endpoint<?> target)
		{
			return isTraversable(target, this);
		}

		@Override
		public boolean isTraversableTo(Cloud.Endpoint<?> target)
		{
			return isTraversable(this, target);
		}

		@Override
		public boolean isTraversableFrom()
		{
			return isTraversableFrom(this);
		}

		@Override
		public boolean isTraversableTo()
		{
			return isTraversableTo(this);
		}
	};
}