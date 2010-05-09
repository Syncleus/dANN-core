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
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.TextureLoader;
import com.syncleus.dann.graph.Graph;
import com.syncleus.dann.graph.drawing.GraphDrawer;
import com.syncleus.dann.math.Vector;
import com.syncleus.dann.neural.*;

/**
 * A BranchGroup representing a HyperAssociativeMap
 *
 * @author Jeffrey Phillips Freeman
 * @since 1.0
 */
public class HyperassociativeMapVisualization<D extends GraphDrawer<G, N>, G extends Graph<N, ?>, N> extends BranchGroup
{
	private D drawer;
	private Map<N, TransformGroup> nodeGraphics = new HashMap<N, TransformGroup>();
	private final Map<N, Vector> oldNodeLocations = new HashMap<N, Vector>();
	private final BranchGroup nestedRoot = new BranchGroup();
	private float nodeRadius;

	/**
	 * Initializes a new visualization to represent the specified drawer.
	 *
	 * @param map The drawer to represent by this BranchGroup
	 * @since 1.0
	 */
	public HyperassociativeMapVisualization(final D map)
	{
		this(map, 0.01F);
	}

	/**
	 * Initializes a new visualization to represent the specified drawer and with
	 * nodes of the specified radius.
	 *
	 * @param map The drawer to represent by this BranchGroup
	 * @param nodeRadius The radius of the spheres representing each node.
	 * @since 1.0
	 */
	public HyperassociativeMapVisualization(final D map, final float nodeRadius)
	{
		this.nodeRadius = nodeRadius;
		this.drawer = map;
		this.setCapability(BranchGroup.ALLOW_CHILDREN_WRITE);
		this.setCapability(BranchGroup.ALLOW_CHILDREN_EXTEND);
		this.setCapability(BranchGroup.ALLOW_DETACH);
		this.nestedRoot.setCapability(BranchGroup.ALLOW_DETACH);
		this.addChild(this.nestedRoot);
		this.refresh();
	}

	/**
	 * Gets all the latest locations from the drawer and refreshes the graphical
	 * representation accordingly.
	 *
	 * @since 1.0
	 */
	public final void refresh()
	{
		boolean childrenRemoved = false;
		final Hashtable<N, TransformGroup> newGraphicalNodes = new Hashtable<N, TransformGroup>();
		final Map<N, Vector> coordinates = this.drawer.getCoordinates();
		final Set<N> nodes = this.drawer.getGraph().getNodes();
		for(final N node : nodes)
			if (!this.nodeGraphics.containsKey(node))
			{
				Color neuronColor = Color.GRAY;
				if (node instanceof Neuron)
				{
					final Neuron neuron = ((Neuron) node);
					if (neuron instanceof OutputNeuron)
						neuronColor = Color.RED;
					else if (neuron instanceof InputNeuron)
						neuronColor = Color.BLUE;
					else if (neuron instanceof StaticNeuron)
						neuronColor = Color.YELLOW;
				}
				final TransformGroup newVisual = this.createNeuronSphere("", "", neuronColor, (float) coordinates.get(node).getCoordinate(1), (float) coordinates.get(node).getCoordinate(2), (float) coordinates.get(node).getCoordinate(3), this.nodeRadius);
				if (!childrenRemoved)
				{
					this.removeAllChildren();
					childrenRemoved = true;
				}
				this.nestedRoot.addChild(newVisual);
				newGraphicalNodes.put(node, newVisual);
				this.oldNodeLocations.put(node, new Vector(coordinates.get(node).getDimensions()));
			}
			else
			{
				final TransformGroup oldVisual = this.nodeGraphics.remove(node);
				// Create the transform group node holding the sphere
				final Transform3D neuronTransform = new Transform3D();
				final Vector currentLocation = coordinates.get(node);
				final Vector oldLocation = this.oldNodeLocations.get(node);
				neuronTransform.set(-1f, new Vector3f((float) oldLocation.getCoordinate(1), (float) oldLocation.getCoordinate(2), (float) oldLocation.getCoordinate(3)));
				oldVisual.setTransform(neuronTransform);
				neuronTransform.set(1f, new Vector3f((float) currentLocation.getCoordinate(1), (float) currentLocation.getCoordinate(2), (float) currentLocation.getCoordinate(3)));
				oldVisual.setTransform(neuronTransform);
				this.oldNodeLocations.put(node, currentLocation);
				newGraphicalNodes.put(node, oldVisual);
			}
		//remove any stale nodes
		for(Map.Entry<N, TransformGroup> nTransformGroupEntry : this.nodeGraphics.entrySet())
		{
			this.nestedRoot.removeChild(nTransformGroupEntry.getValue());
			this.oldNodeLocations.remove(nTransformGroupEntry.getKey());
		}
		this.nodeGraphics = newGraphicalNodes;
		if (childrenRemoved)
			this.addChild(this.nestedRoot);
	}

	private static TransformGroup createNeuronSphere(final String textLine1, final String textLine2, final Color myColor, final float posX, final float posY, final float posZ, final float radius)
	{
		// Create the transform group node holding the sphere
		final TransformGroup neuronTransformGroup = new TransformGroup();
		neuronTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		neuronTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		final Transform3D neuronTransform = new Transform3D();
		neuronTransform.set(1f, new Vector3f(posX, posY, posZ));
		neuronTransformGroup.setTransform(neuronTransform);
		// create a nice texture image for the 3d sphere
		final BufferedImage neuronTextureImage = createNeuronTextureImage(textLine1, textLine2, myColor);
		final Appearance neuronAppearance = makeMappingFromImage(neuronTextureImage);
		final Sphere neuronSphere = new Sphere(radius, Primitive.GENERATE_TEXTURE_COORDS, 100, neuronAppearance); // animation ok on p4 2GHz and radeon X1600 GPU
		final Alpha alpha = new Alpha(-1, 5000);
		final Transform3D yAxis = new Transform3D();
		final TransformGroup rotateTransform = new TransformGroup();
		rotateTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
		rotateTransform.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
		final RotationInterpolator rotator = new RotationInterpolator(alpha, rotateTransform, yAxis, (float) Math.PI * 2.0f, 0.0f);
		final BoundingSphere bounds = new BoundingSphere(new Point3d(0, 0, 0), 10000f);
		rotator.setSchedulingBounds(bounds);
		rotateTransform.addChild(rotator);
		rotateTransform.addChild(neuronSphere);
		neuronTransformGroup.addChild(rotateTransform);
		return neuronTransformGroup;
	}

	private static BufferedImage createNeuronTextureImage(final String textLine1, final String textLine2, final Color myColor)
	{
		final int imSizeX = 256; // high quality for now - we will optimize later
		final int imSizeY = 128;
		final BufferedImage myNeuronTextureImage = new BufferedImage(imSizeX, imSizeY, BufferedImage.TYPE_INT_RGB);
		final Graphics2D g2d = myNeuronTextureImage.createGraphics(); // creates a Graphics2D to draw in the BufferedImage
		g2d.setBackground(myColor);
		g2d.clearRect(0, 0, myNeuronTextureImage.getWidth(), myNeuronTextureImage.getHeight());
		final int tempFontSize = 32; // high quality for now
		g2d.setFont(new Font("Arial", Font.BOLD, tempFontSize));
		g2d.setColor(Color.BLACK);
		final FontMetrics fontMetrics = g2d.getFontMetrics();
		g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		final int tempStringWidth1 = fontMetrics.stringWidth(textLine1);
		final int tempTextPosX1 = imSizeX / 2 - tempStringWidth1 / 2;
		g2d.drawString(textLine1, tempTextPosX1, 60);
		final int tempStringWidth2 = fontMetrics.stringWidth(textLine1);
		final int tempTextPosX2 = imSizeX / 2 - tempStringWidth2 / 2;
		g2d.drawString(textLine2, tempTextPosX2, 90);
		g2d.drawImage(myNeuronTextureImage, null, 0, 0);
		return (myNeuronTextureImage);
	}

	private static Appearance makeMappingFromImage(final BufferedImage myImage)
	{
		final Appearance mapping = new Appearance();
		mapping.setCapability(Appearance.ALLOW_MATERIAL_READ);
		mapping.setCapability(Appearance.ALLOW_MATERIAL_WRITE);
		final TextureLoader loader = new TextureLoader(myImage, TextureLoader.GENERATE_MIPMAP);
		ImageComponent2D image = loader.getImage();
		int imageWidth = image.getWidth();
		int imageHeight = image.getHeight();
		final Texture2D texture = new Texture2D(Texture.MULTI_LEVEL_MIPMAP, Texture.RGB, imageWidth, imageHeight);
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
	 * Gets the drawer this class is representing
	 *
	 * @return The drawer this class is representing.
	 * @since 1.0
	 */
	GraphDrawer getMap()
	{
		return drawer;
	}
}
