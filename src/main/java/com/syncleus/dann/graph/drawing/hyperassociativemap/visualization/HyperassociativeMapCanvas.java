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
package com.syncleus.dann.graph.drawing.hyperassociativemap.visualization;

import com.syncleus.dann.ComponentUnavailableException;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.syncleus.dann.graph.Graph;
import com.syncleus.dann.graph.drawing.hyperassociativemap.HyperassociativeMap;
import java.awt.BorderLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;

/**
 * A Canvas3D specifically for displaying a HyperassociativeMap.
 *
 * @author Jeffrey Phillips Freeman
 * @since 1.0
 */
public class HyperassociativeMapCanvas<G extends Graph<N, ?>, N> extends JPanel {

    private static final long serialVersionUID = -2387160322569579373L;
    private final HyperassociativeMap<G, N> map;
    private static final float NODE_RADIUS = 0.07F;
    private float nodeRadius;
    private HyperassociativeMapCanvasCanvas c3d;
    private HyperassociativeMapVisualization<HyperassociativeMap<G, N>, G, N> mapVisual;

    public class HyperassociativeMapCanvasCanvas extends Canvas3D {

        public HyperassociativeMapCanvasCanvas(GraphicsConfiguration config) {
            super(config);

            final BranchGroup root = newRoot();
            mapVisual = new HyperassociativeMapVisualization<HyperassociativeMap<G, N>, G, N>(map, nodeRadius);
            mapVisual.refresh();
            root.addChild(mapVisual);
            final SimpleUniverse universe = new SimpleUniverse(this);
            universe.addBranchGraph(root);

            // Set the initial view position
            final TransformGroup viewTransformGroup = universe.getViewingPlatform().getViewPlatformTransform();
            final Transform3D viewTransform = new Transform3D();
            viewTransform.set(1f, new Vector3f(0f, 0f, 10f));
            viewTransformGroup.setTransform(viewTransform);
            // add an orbital mouse control to the scene
            final OrbitBehavior mouseOrbital = new OrbitBehavior(this);
            mouseOrbital.setRotationCenter(new Point3d(0f, 0f, -2f));
            mouseOrbital.setReverseRotate(true);
            mouseOrbital.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), Double.POSITIVE_INFINITY));
            universe.getViewingPlatform().setViewPlatformBehavior(mouseOrbital);
        }

        private BranchGroup newRoot() {
            // Create the branch group
            final BranchGroup branchGroup = new BranchGroup();
            // Create the bounding leaf node
            final BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0);
            final BoundingLeaf boundingLeaf = new BoundingLeaf(bounds);
            branchGroup.addChild(boundingLeaf);
            // Create the background
            final Background background = new Background(new Color3f(0.05f, 0.05f, 0.2f));
            background.setApplicationBounds(bounds);
            branchGroup.addChild(background);
            // Create the ambient light
            final AmbientLight ambLight = new AmbientLight(new Color3f(1.0f, 1.0f, 1.0f));
            ambLight.setInfluencingBounds(bounds);
            branchGroup.addChild(ambLight);
            // Create the directional light
            final Vector3f dir = new Vector3f(-1f, -1f, -1f);
            final DirectionalLight dirLight = new DirectionalLight(new Color3f(1.0f, 1.0f, 1.0f), dir);
            dirLight.setInfluencingBounds(bounds);
            branchGroup.addChild(dirLight);
            return branchGroup;
        }
    }

    /**
     * Initializes a new HyperassociativeMapCanvas to represent the specified
     * HyperassociativeMap.
     *
     * @param ourMap The HyperassociativeMap to display.
     * @since 1.0
     */
    public HyperassociativeMapCanvas(final HyperassociativeMap<G, N> ourMap) throws ComponentUnavailableException {
        this(ourMap, SimpleUniverse.getPreferredConfiguration());
    }

    /**
     * Initializes a new HyperassociativeMapCanvas to represent the specified
     * HyperassociativeMap and with nodes represented graphically as spheres with
     * the specified radius.
     *
     * @param ourMap The HyperassociativeMap to display.
     * @param nodeRadius The radius of the spheres representing each node.
     * @since 1.0
     */
    public HyperassociativeMapCanvas(final HyperassociativeMap<G, N> ourMap, final float nodeRadius) throws ComponentUnavailableException {
        this(ourMap, null, nodeRadius);
    }

    /**
     * Initializes a new HyperassociativeMapCanvas to represent the specified
     * HyperassociativeMap using the specified GraphicsConfiguration.
     *
     * @param ourMap The HyperassociativeMap to display.
     * @param configuration The GraphicsConfiguration to use for configuring the
     * canvas.
     * @since 1.0
     */
    public HyperassociativeMapCanvas(final HyperassociativeMap<G, N> ourMap, final GraphicsConfiguration configuration) throws ComponentUnavailableException {
        this(ourMap, configuration, NODE_RADIUS);
    }

    /**
     * Initializes a new HyperassociativeMapCanvas to represent the specified
     * HyperassociativeMap using the specified GraphicsConfiguration and with nodes
     * represented graphically as spheres with the specified radius.
     *
     * @param ourMap The HyperassociativeMap to display.
     * @param configuration The GraphicsConfiguration to use for configuring the
     * canvas.
     * @param nodeRadius The radius of the spheres representing each node.
     * @throws Java3DUnavailableException if Java3D support is not available.
     * @since 1.0
     */
    public HyperassociativeMapCanvas(final HyperassociativeMap<G, N> ourMap, GraphicsConfiguration configuration, final float nodeRadius) throws ComponentUnavailableException {
        super(new BorderLayout());

        this.map = ourMap;
        this.nodeRadius = nodeRadius;


        try {
            if (configuration == null) {
                //This is where Java3D will throw java.lang.UnsatisfiedLinkError: no j3dcore-ogl in java.library.path if Java3D not available.
                configuration = SimpleUniverse.getPreferredConfiguration();
            }

            c3d = new HyperassociativeMapCanvasCanvas(configuration);
            add(c3d, BorderLayout.CENTER);

        } catch (UnsatisfiedLinkError error) {
            throw new ComponentUnavailableException(error);

        }

        updateUI();

    }

    /**
     * Gets all the latest locations from the map and refreshes the graphical
     * representation accordingly.
     *
     * @since 1.0
     */
    public void refresh() {
        if (mapVisual != null) {
            mapVisual.refresh();
        }
    }

    public HyperassociativeMap<G, N> getHyperassociativeMap() {
        return this.map;
    }
}
