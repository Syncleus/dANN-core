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
package com.syncleus.dann.attributes.hat;

import com.syncleus.dann.attributes.AbstractAttributePool;
import com.syncleus.dann.attributes.Attribute;
import com.syncleus.dann.attributes.AttributeChangeListener;
import com.syncleus.dann.graph.DirectedEdge;
import com.syncleus.dann.graph.MutableTreeGraph;

public abstract class AbstractHierarchicalAttributePool<T> extends AbstractAttributePool<T> implements HierarchicalAttributePool<T>
{
	private final ParentListener listener = new ParentListener();
	private HierarchicalAttributePool<T> parent = null;
	private final MutableTreeGraph<HierarchicalAttributePool, DirectedEdge<HierarchicalAttributePool>> owner;

	protected AbstractHierarchicalAttributePool(final MutableTreeGraph<HierarchicalAttributePool, DirectedEdge<HierarchicalAttributePool>> owner)
	{
		if (owner == null)
			throw new IllegalArgumentException("owner can not be null");
		this.owner = owner;
	}

	@Override
	public final HierarchicalAttributePool<T> getParent()
	{
		return this.parent;
	}

	// TODO this has package level access because only a HAT graph should set parents. consider changing this to an event / listener model
	final HierarchicalAttributePool<T> setParent(final HierarchicalAttributePool<T> newParent)
	{
		if( !this.owner.getNodes().contains(newParent) )
			throw new IllegalArgumentException("newParent is not in owning graph");

		final HierarchicalAttributePool<T> oldParent = this.parent;
		if( oldParent != null )
			oldParent.removeListener(this.listener);
		if( newParent != null )
			newParent.listenAll(this.listener);
		this.parent = newParent;
		return oldParent;
	}

	protected <C extends T, V extends C> V modifyChangedAttribute(final Attribute<?, C> attribute, final V attributeValue)
	{
		return attributeValue;
	}

	private final class ParentListener implements AttributeChangeListener<T>
	{
		@Override
		public <C extends T> void attributeChanged(final Attribute<?, C> attribute, final C attributeValue)
		{
			final C modiffiedAttributeValue = AbstractHierarchicalAttributePool.this.modifyChangedAttribute(attribute, attributeValue);
			AbstractHierarchicalAttributePool.this.notify(attribute, modiffiedAttributeValue);
		}
	}
}
