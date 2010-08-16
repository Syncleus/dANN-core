package com.syncleus.dann.genetics.attributes.hat;

import com.syncleus.dann.genetics.attributes.AttributePool;

public interface HierarchicalAttributePool<T> extends AttributePool<T>
{
	HierarchicalAttributePool<T> getParent();
}
