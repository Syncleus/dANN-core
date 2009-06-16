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
package com.syncleus.dann.hyperassociativemap.visualization;

import com.syncleus.dann.neural.Neuron;
import com.syncleus.dann.neural.backprop.InputBackpropNeuron;
import com.syncleus.dann.neural.backprop.OutputBackpropNeuron;
import com.syncleus.dann.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.TextureLoader;
import com.syncleus.dann.hyperassociativemap.*;
import java.awt.image.BufferedImage;
import java.util.Set;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.*;
import java.util.Hashtable;


/**
 * A BranchGroup representing a HyperAssociativeMap
 *
 * <!-- Author: Jeffrey Phillips Freeman -->
 * @author Syncleus, Inc.
 * @since 1.0
 * @version 1.0
 */
public class HyperassociativeMapVisualization extends BranchGroup
{
    private HyperassociativeMap map;
    private Hashtable<HyperassociativeNode, TransformGroup> nodeGraphics = new Hashtable<HyperassociativeNode, TransformGroup>();
    private Hashtable<HyperassociativeNode, Hyperpoint> oldNodeLocations = new Hashtable<HyperassociativeNode, Hyperpoint>();
    private BranchGroup nestedRoot = new BranchGroup();
	private float nodeRadius;



	/**
	 * Initializes a new visualization to represent the specified map.
	 *
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @param map The map to represent by this BranchGroup
	 * @since 1.0
	 */
    public HyperassociativeMapVisualization(HyperassociativeMap map)
    {
		this(map, 0.01F);
    }

	/**
	 * Initializes a new visualization to represent the specified map and with
	 * nodes of the specified radius.
	 *
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @param map The map to represent by this BranchGroup
	 * @param nodeRadius The radius of the spheres representing each node.
	 * @since 1.0
	 */
	public HyperassociativeMapVisualization(HyperassociativeMap map, float nodeRadius)
	{
		this.nodeRadius = nodeRadius;
        this.map = map;

        this.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
        this.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
        this.setCapability(BranchGroup.ALLOW_DETACH);
        this.nestedRoot.setCapability(BranchGroup.ALLOW_DETACH);

        this.addChild(this.nestedRoot);

        this.refresh();
	}



	/**
	 * Gets all the latest locations from the map and refreshes the graphical
	 * representation accordingly.
	 *
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @since 1.0
	 */
    public void refresh()
    {

        boolean childrenRemoved = false;

        Hashtable<HyperassociativeNode, TransformGroup> newGraphicalNodes = new Hashtable<HyperassociativeNode, TransformGroup>();

        Set<HyperassociativeNode> nodes = this.map.getNodes();
        for (HyperassociativeNode node : nodes)
            if (this.nodeGraphics.containsKey(node) == false)
            {
                Color neuronColor = Color.GRAY;
                if (node instanceof NeuronHyperassociativeNode)
                {
                    Neuron neuron = ((NeuronHyperassociativeNode) node).getNeuron();
                    if (neuron instanceof OutputBackpropNeuron)
                        neuronColor = Color.RED;
                    else if (neuron instanceof InputBackpropNeuron)
                        neuronColor = Color.BLUE;
                }
                TransformGroup newVisual = this.createNeuronSphere("", "", neuronColor, (float) node.getLocation().getCoordinate(1), (float) node.getLocation().getCoordinate(2), (float) node.getLocation().getCoordinate(3), this.nodeRadius);
                if (childrenRemoved == false)
                {
                    this.removeAllChildren();
                    childrenRemoved = true;
                }

                this.nestedRoot.addChild(newVisual);
                newGraphicalNodes.put(node, newVisual);
                this.oldNodeLocations.put(node, new Hyperpoint(node.getLocation().getDimensions()));
            }
            else
            {
                TransformGroup oldVisual = this.nodeGraphics.remove(node);

                // Create the transform group node holding the sphere
                Transform3D neuronTransform = new Transform3D();
                Hyperpoint currentLocation = node.getLocation();
                Hyperpoint oldLocation = this.oldNodeLocations.get(node);
                neuronTransform.set(-1f, new Vector3f((float) oldLocation.getCoordinate(1), (float) oldLocation.getCoordinate(2), (float) oldLocation.getCoordinate(3)));
                oldVisual.setTransform(neuronTransform);
                neuronTransform.set(1f, new Vector3f((float) currentLocation.getCoordinate(1), (float) currentLocation.getCoordinate(2), (float) currentLocation.getCoordinate(3)));
                oldVisual.setTransform(neuronTransform);
                this.oldNodeLocations.put(node, currentLocation);

                newGraphicalNodes.put(node, oldVisual);
            }

        //remove any stale nodes
        for (HyperassociativeNode node : this.nodeGraphics.keySet())
        {
            this.nestedRoot.removeChild(this.nodeGraphics.get(node));
            this.oldNodeLocations.remove(node);
        }

        this.nodeGraphics = newGraphicalNodes;

        if (childrenRemoved)
            this.addChild(this.nestedRoot);
    }



    private TransformGroup createNeuronSphere(String textLine1, String textLine2, Color myColor, float posX, float posY, float posZ, float radius)
    {
        // Create the transform group node holding the sphere
        TransformGroup neuronTransformGroup = new TransformGroup();
        neuronTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        neuronTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform3D neuronTransform = new Transform3D();
        neuronTransform.set(1f, new Vector3f(posX, posY, posZ));
        neuronTransformGroup.setTransform(neuronTransform);

        // create a nice texture image for the 3d sphere
        BufferedImage neuronTextureImage = createNeuronTextureImage(textLine1, textLine2, myColor);
        Appearance neuronAppearance = makeMappingFromImage(neuronTextureImage);


        Sphere neuronSphere = new Sphere(radius, Primitive.GENERATE_TEXTURE_COORDS, 100, neuronAppearance); // animation ok on p4 2GHz and radeon X1600 GPU

        Alpha alpha = new Alpha(-1, 5000);
        Transform3D yAxis = new Transform3D();
        TransformGroup rotateTransform = new TransformGroup();
        rotateTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        rotateTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        RotationInterpolator rotator = new RotationInterpolator(alpha, rotateTransform, yAxis, (float) Math.PI * 2.0f, 0.0f);

        BoundingSphere bounds = new BoundingSphere(new Point3d(0, 0, 0), 10000f);
        rotator.setSchedulingBounds(bounds);

        rotateTransform.addChild(rotator);
        rotateTransform.addChild(neuronSphere);
        neuronTransformGroup.addChild(rotateTransform);

        return neuronTransformGroup;
    }



    private BufferedImage createNeuronTextureImage(String textLine1, String textLine2, Color myColor)
    {

        int imSizeX = 256; // high quality for now - we will optimize later
        int imSizeY = 128;

        BufferedImage myNeuronTextureImage = new BufferedImage(imSizeX, imSizeY, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = myNeuronTextureImage.createGraphics(); // creates a Graphics2D to draw in the BufferedImage
        g2d.setBackground(myColor);
        g2d.clearRect(0, 0, myNeuronTextureImage.getWidth(), myNeuronTextureImage.getHeight());

        int tempFontSize = 32; // high quality for now

        g2d.setFont(new Font("Arial", Font.BOLD, tempFontSize));
        g2d.setColor(Color.BLACK);
        FontMetrics fm = g2d.getFontMetrics();
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int tempStringWidth1 = fm.stringWidth(textLine1);
        int tempTextPosX1 = imSizeX / 2 - tempStringWidth1 / 2;
        g2d.drawString(textLine1, tempTextPosX1, 60);

        int tempStringWidth2 = fm.stringWidth(textLine1);
        int tempTextPosX2 = imSizeX / 2 - tempStringWidth2 / 2;

        g2d.drawString(textLine2, tempTextPosX2, 90);

        g2d.drawImage(myNeuronTextureImage, null, 0, 0);
        return (myNeuronTextureImage);
    }



    private Appearance makeMappingFromImage(BufferedImage myImage)
    {
        Appearance mapping = new Appearance();

        mapping.setCapability(Appearance.ALLOW_MATERIAL_READ);
        mapping.setCapability(Appearance.ALLOW_MATERIAL_WRITE);

        TextureLoader loader = new TextureLoader(myImage, TextureLoader.GENERATE_MIPMAP);
        ImageComponent2D image = loader.getImage();

        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();

        Texture2D texture = new Texture2D(Texture.MULTI_LEVEL_MIPMAP, Texture.RGB, imageWidth, imageHeight);

        // Mipmapping of the texture -- WARNING: original picture sizes have to be ^2 (e.g. 1024x512)
        int imageLevel = 0;
        texture.setImage(imageLevel, image);

        while (imageWidth > 1 || imageHeight > 1)
        {
            imageLevel++;
            if (imageWidth > 1)
                imageWidth /= 2;
            if (imageHeight > 1)
                imageHeight /= 2;
            image = loader.getScaledImage(imageWidth, imageHeight);
            texture.setImage(imageLevel, image);
        }

        // Texture quality
        texture.setMagFilter(Texture.BASE_LEVEL_LINEAR); //nice!
        texture.setMinFilter(Texture.MULTI_LEVEL_LINEAR); //nice!

        mapping.setTexture(texture);

        return mapping;

    }



	/**
	 * Gets the map this class is representing
	 *
	 * <!-- Author: Jeffrey Phillips Freeman -->
	 * @return The map this class is representing.
	 * @since 1.0
	 */
    HyperassociativeMap getMap()
    {
        return map;
    }
}
