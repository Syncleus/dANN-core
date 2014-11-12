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
package com.syncleus.dann;

import com.syncleus.dann.backprop.*;
import com.syncleus.grail.graph.GrailGraphFactory;
import com.thinkaurelius.titan.core.*;
import com.thinkaurelius.titan.graphdb.configuration.GraphDatabaseConfiguration;
import com.tinkerpop.frames.*;
import com.tinkerpop.frames.modules.Module;
import org.apache.commons.configuration.*;

import java.io.File;
import java.util.*;

import static com.thinkaurelius.titan.graphdb.configuration.GraphDatabaseConfiguration.*;

public final class BlankGraphFactory {
    private static final FramedGraphFactory FACTORY = new GrailGraphFactory(Collections.<Module>emptyList(),
            new HashSet<Class<?>>(Arrays.asList(new Class<?>[]{
                    ActivationNeuron.class,
                    BackpropNeuron.class,
                    BackpropSynapse.class})));

    public static FramedTransactionalGraph makeTitanGraph(final String location) {
        BaseConfiguration config = new BaseConfiguration();
        Configuration storage = config.subset(GraphDatabaseConfiguration.STORAGE_NAMESPACE);
        // configuring local backend
        storage.setProperty(GraphDatabaseConfiguration.STORAGE_BACKEND_KEY, "local");
        storage.setProperty(GraphDatabaseConfiguration.STORAGE_DIRECTORY_KEY, location);
        // configuring elastic search index
        Configuration index = storage.subset(GraphDatabaseConfiguration.INDEX_NAMESPACE).subset("search");
        index.setProperty(INDEX_BACKEND_KEY, "elasticsearch");
        index.setProperty("local-mode", true);
        index.setProperty("client-only", false);
        index.setProperty(STORAGE_DIRECTORY_KEY, "./target/TitanBackpropDB" + File.separator + "es");

        TitanGraph graph = TitanFactory.open(config);

        return FACTORY.create(graph);
    }

    public static FramedTransactionalGraph makeTinkerGraph() {
        return FACTORY.create(new MockTransactionalTinkerGraph());
    }
}
