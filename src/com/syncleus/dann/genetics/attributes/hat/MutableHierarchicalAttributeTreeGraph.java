package com.syncleus.dann.genetics.attributes.hat;

import java.util.Set;
import com.syncleus.dann.graph.*;

public interface MutableHierarchicalAttributeTreeGraph<N extends HierarchicalAttributePool<T>, E extends DirectedEdge<N>, T> extends HierarchicalAttributeTreeGraph<N, E, T>, MutableTreeGraph<N, E>
{
}
