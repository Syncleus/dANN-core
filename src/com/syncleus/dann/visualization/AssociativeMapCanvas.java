/******************************************************************************
 *                                                                             *
 *  Copyright: (c) Jeffrey Phillips Freeman                                    *
 *                                                                             *
 *  You may redistribute and modify this source code under the terms and       *
 *  conditions of the Open Source Community License - Type C version 1.0       *
 *  or any later version as published by syncleus at http://www.syncleus.com.  *
 *  There should be a copy of the license included with this file. If a copy   *
 *  of the license is not included you are granted no right to distribute or   *
 *  otherwise use this file except through a legal and valid license. You      *
 *  should also contact syncleus at the information below if you cannot find   *
 *  a license:                                                                 *
 *                                                                             *
 *  Syncleus                                                                   *
 *  1116 McClellan St.                                                         *
 *  Philadelphia, PA 19148                                                     *
 *                                                                             *
 ******************************************************************************/
package com.syncleus.dann.visualization;

import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.behaviors.vp.OrbitBehavior;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.syncleus.dann.associativemap.AssociativeMap;
import java.awt.GraphicsConfiguration;
import javax.media.j3d.*;
import javax.vecmath.*;


public class AssociativeMapCanvas extends Canvas3D
{
    private AssociativeMapVisualization mapVisual;
    
    public AssociativeMapCanvas(AssociativeMap map)
    {
        this(map, SimpleUniverse.getPreferredConfiguration());
    }
    
    public AssociativeMap getAssociativeMap()
    {
        return this.mapVisual.getMap();
    }



    public AssociativeMapCanvas(AssociativeMap map, GraphicsConfiguration configuration)
    {
        super(configuration);
        

        BranchGroup root = createRoot();

        this.mapVisual = new AssociativeMapVisualization(map);
        this.mapVisual.refresh();

        root.addChild(mapVisual);

        SimpleUniverse universe = new SimpleUniverse(this);
        universe.addBranchGraph(root);

        // Use parallel projection.
        View view = universe.getViewer().getView();

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

        // a zoom control with the mouse wheel
        MouseWheelZoom mouseZoom = new MouseWheelZoom(viewTransformGroup);
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
