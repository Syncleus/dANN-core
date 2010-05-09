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

import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.GraphicsConfiguration;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.syncleus.dann.graph.Graph;
import com.syncleus.dann.graph.drawing.hyperassociativemap.HyperassociativeMap;

/**
 * A Canvas3D specifically for displaying a HyperassociativeMap.
 *
 * @author Jeffrey Phillips Freeman
 * @since 1.0
 */
public class HyperassociativeMapCanvas<G extends Graph<N, ?>, N> extends Canvas3D
{
	private static final long serialVersionUID = -2387160322569579373L;
	private final HyperassociativeMap<G, N> map;
	private HyperassociativeMapVisualization<HyperassociativeMap<G, N>, G, N> mapVisual;

	/**
	 * Initializes a new HyperassociativeMapCanvas to represent the specified
	 * HyperassociativeMap.
	 *
	 * @param map The HyperassociativeMap to display.
	 * @since 1.0
	 */
	public HyperassociativeMapCanvas(final HyperassociativeMap<G, N> map)
	{
		this(map, SimpleUniverse.getPreferredConfiguration());
	}

	/**
	 * Initializes a new HyperassociativeMapCanvas to represent the specified
	 * HyperassociativeMap and with nodes represented graphically as spheres
	 * with the specified radius.
	 *
	 * @param map The HyperassociativeMap to display.
	 * @param nodeRadius The radius of the spheres representing each node.
	 * @since 1.0
	 */
	public HyperassociativeMapCanvas(final HyperassociativeMap<G, N> map, final float nodeRadius)
	{
		this(map, SimpleUniverse.getPreferredConfiguration(), nodeRadius);
	}

	/**
	 * Initializes a new HyperassociativeMapCanvas to represent the specified
	 * HyperassociativeMap using the specified GraphicsConfiguration.
	 *
	 * @param map The HyperassociativeMap to display.
	 * @param configuration The GraphicsConfiguration to use for configuring the
	 * canvas.
	 * @since 1.0
	 */
	public HyperassociativeMapCanvas(final HyperassociativeMap<G, N> map, final GraphicsConfiguration configuration)
	{
		this(map, configuration, 0.07F);
	}

	/**
	 * Initializes a new HyperassociativeMapCanvas to represent the specified
	 * HyperassociativeMap using the specified GraphicsConfiguration and with
	 * nodes represented graphically as spheres with the specified radius.
	 *
	 * @param map The HyperassociativeMap to display.
	 * @param configuration The GraphicsConfiguration to use for configuring the
	 * canvas.
	 * @param nodeRadius The radius of the spheres representing each node.
	 * @since 1.0
	 */
	public HyperassociativeMapCanvas(final HyperassociativeMap<G, N> map, final GraphicsConfiguration configuration, final float nodeRadius)
	{
		super(configuration);
		this.map = map;
		final BranchGroup root = createRoot();
		this.mapVisual = new HyperassociativeMapVisualization<HyperassociativeMap<G, N>, G, N>(map, nodeRadius);
		this.mapVisual.refresh();
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

	public HyperassociativeMap<G, N> getHyperassociativeMap()
	{
		return this.map;
	}

	/**
	 * Gets all the latest locations from the map and refreshes the graphical
	 * representation accordingly.
	 *
	 * @since 1.0
	 */
	public void refresh()
	{
		this.mapVisual.refresh();
	}

	private static BranchGroup createRoot()
	{
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
