package com.syncleus.dann.genetics.attributes.hat;

import com.syncleus.dann.graph.*;
import com.syncleus.dann.graphicalmodel.bayesian.BayesianEdge;
import com.syncleus.dann.graphicalmodel.bayesian.BayesianNode;

public interface HierarchicalAttributeTreeGraph<N extends HierarchicalAttributePool<T>, E extends DirectedEdge<N>, T> extends TreeGraph<N, E>
{
}
