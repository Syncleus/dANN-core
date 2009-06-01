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
package com.syncleus.dann.visualization;

import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.syncleus.dann.hyperassociativemap.HyperassociativeMap;
import java.awt.GraphicsConfiguration;
import javax.media.j3d.*;
import javax.vecmath.*;


public class HyperassociativeMapCanvas extends Canvas3D
{
    private static final long serialVersionUID = 1L;

    private HyperassociativeMapVisualization mapVisual;
    
    public HyperassociativeMapCanvas(HyperassociativeMap map)
    {
        this(map, SimpleUniverse.getPreferredConfiguration());
    }

    public HyperassociativeMapCanvas(HyperassociativeMap map, float nodeRadius)
    {
        this(map, SimpleUniverse.getPreferredConfiguration(), nodeRadius);
    }

    public HyperassociativeMapCanvas(HyperassociativeMap map, GraphicsConfiguration configuration)
    {
        this(map, configuration, 0.07F);
    }
    
    public HyperassociativeMapCanvas(HyperassociativeMap map, GraphicsConfiguration configuration, float nodeRadius)
    {
        super(configuration);
        

        BranchGroup root = createRoot();

        this.mapVisual = new HyperassociativeMapVisualization(map, nodeRadius);
        this.mapVisual.refresh();

        root.addChild(mapVisual);

        SimpleUniverse universe = new SimpleUniverse(this);
        universe.addBranchGraph(root);

        // Set the initial view position
        TransformGroup viewTransformGroup = universe.getViewingPlatform().getViewPlatformTransform();
        Transform3D viewTransform = new Transform3D();
        viewTransform.set(1f, new Vector3f(0f, 0f, 10f));
        viewTransformGroup.setTransform(viewTransform);

        // add an orbital mouse control to the scene
        OrbitBehavior mouseOrbital = new OrbitBehavior(this);
        mouseOrbital.setRotationCenter(new Point3d(0f, 0f, -2f));
        mouseOrbital.setReverseRotate(true);
        mouseOrbital.setSchedulingBounds(new BoundingSphere(new Point3d(0.0, 0.0, 0.0), Double.POSITIVE_INFINITY));
        universe.getViewingPlatform().setViewPlatformBehavior(mouseOrbital);
    }

    public HyperassociativeMap getHyperassociativeMap()
    {
        return this.mapVisual.getMap();
    }
    
    
    public void refresh()
    {
        this.mapVisual.refresh();
    }



    private static BranchGroup createRoot()
    {

        // Create the branch group
        BranchGroup branchGroup = new BranchGroup();

        // Create the bounding leaf node
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 1000.0);
        BoundingLeaf boundingLeaf = new BoundingLeaf(bounds);
        branchGroup.addChild(boundingLeaf);

        // Create the background
        Background bg = new Background(new Color3f(0.05f, 0.05f, 0.2f));
        bg.setApplicationBounds(bounds);
        branchGroup.addChild(bg);

        // Create the ambient light
        AmbientLight ambLight = new AmbientLight(new Color3f(1.0f, 1.0f, 1.0f));
        ambLight.setInfluencingBounds(bounds);
        branchGroup.addChild(ambLight);

        // Create the directional light
        Vector3f dir = new Vector3f(-1f, -1f, -1f);
        DirectionalLight dirLight = new DirectionalLight(new Color3f(1.0f, 1.0f, 1.0f), dir);
        dirLight.setInfluencingBounds(bounds);
        branchGroup.addChild(dirLight);

        return branchGroup;
    }
}
