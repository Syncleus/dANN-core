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
package com.syncleus.visualization;

import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.image.TextureLoader;
import com.syncleus.dann.associativemap.*;
import java.awt.image.BufferedImage;
import java.util.Set;
import javax.media.j3d.*;
import javax.vecmath.*;
import java.awt.*;


public class AssociativeMapVisualization extends BranchGroup
{
    private AssociativeMap map;



    public AssociativeMapVisualization(AssociativeMap map)
    {
        this.map = map;

        this.refresh();
    }



    public void refresh()
    {
        this.removeAllChildren();
        Set<AssociativeNode> nodes = this.map.getNodes();
        for(AssociativeNode node : nodes)
        {
            this.addChild(this.createNeuronSphere("", "", Color.RED, (float) node.getLocation().getCoordinate(1), (float) node.getLocation().getCoordinate(2), (float) node.getLocation().getCoordinate(3), 1.0F));
        }
    }



    public TransformGroup createNeuronSphere(String textLine1, String textLine2, Color myColor, float posX, float posY, float posZ, float radius)
    {

        // Create the transform group node holding the sphere
        TransformGroup myTransformGroup = new TransformGroup();
        myTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        myTransformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        Transform3D myTransform = new Transform3D();
        myTransform.set(1f, new Vector3f(posX, posY, posZ));
        myTransformGroup.setTransform(myTransform);

        // create a nice texture image for the 3d sphere
        BufferedImage myTextureImage = createNeuronTextureImage(textLine1, textLine2, myColor);
        Appearance myAppearance = makeMappingFromImage(myTextureImage);


        Sphere myNeuron = new Sphere(radius, Primitive.GENERATE_TEXTURE_COORDS, 100, myAppearance); // animation ok on p4 2GHz and radeon X1600 GPU

        Alpha myAlpha = new Alpha(-1, 5000);
        Transform3D yAxis = new Transform3D();
        TransformGroup myTgRot = new TransformGroup();
        myTgRot.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        myTgRot.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        RotationInterpolator myRot = new RotationInterpolator(myAlpha, myTgRot, yAxis, (float) Math.PI * 2.0f, 0.0f);

        BoundingSphere bounds = new BoundingSphere(new Point3d(0, 0, 0), 10000f);
        myRot.setSchedulingBounds(bounds);

        myTgRot.addChild(myRot);
        myTgRot.addChild(myNeuron);
        myTransformGroup.addChild(myTgRot);

        return myTransformGroup;
    }



    public BufferedImage createNeuronTextureImage(String textLine1, String textLine2, Color myColor)
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
        int tempTextPosX1 = Math.round(imSizeX / 2 - tempStringWidth1 / 2);
//		  int tempTextPosX = 100;
        g2d.drawString(textLine1, tempTextPosX1, 60);

        int tempStringWidth2 = fm.stringWidth(textLine1);
        int tempTextPosX2 = Math.round(imSizeX / 2 - tempStringWidth2 / 2);

        g2d.drawString(textLine2, tempTextPosX2, 90);

        g2d.drawImage(myNeuronTextureImage, null, 0, 0);
        return (myNeuronTextureImage);
    }
    
    
      public Appearance makeMappingFromImage(BufferedImage myImage) {
    	  Appearance mapping = new Appearance();

    	  mapping.setCapability(Appearance.ALLOW_MATERIAL_READ);
    	  mapping.setCapability(Appearance.ALLOW_MATERIAL_WRITE);

    	  TextureLoader loader = new TextureLoader(myImage, TextureLoader.GENERATE_MIPMAP);
    	  ImageComponent2D image = loader.getImage();

    	  int imageWidth = image.getWidth();
    	  int imageHeight = image.getHeight();

    	  Texture2D texture = new Texture2D(Texture.MULTI_LEVEL_MIPMAP,Texture.RGB, imageWidth, imageHeight);

    	  // Mipmapping of the texture -- WARNING: original picture sizes have to be ^2 (e.g. 1024x512)
    	  int imageLevel = 0;
    	  texture.setImage(imageLevel, image);

    	  while (imageWidth > 1 || imageHeight > 1) {
    		  imageLevel++;
    		  if (imageWidth > 1) imageWidth /= 2;
    		  if (imageHeight > 1) imageHeight /= 2;
    		  image = loader.getScaledImage(imageWidth, imageHeight);
    		  texture.setImage(imageLevel, image);
    		  System.out.println("From mipmapping in Brain3dView: image: Auto-generated image - width:"+imageWidth);
    	  }

    	  // Texture quality
    	  texture.setMagFilter(Texture.BASE_LEVEL_LINEAR); //nice!
    	  texture.setMinFilter(Texture.MULTI_LEVEL_LINEAR); //nice!
    	  
    	  mapping.setTexture(texture);

    	  return mapping;

      }
}
