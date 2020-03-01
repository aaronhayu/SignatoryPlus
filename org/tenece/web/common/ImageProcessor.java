/*
 * (c) Copyright 2009 The Tenece Professional Services.
 *
 * ============================================================
 * Project Info:  http://tenece.com
 * Project Lead:  Aaron Osikhena (aaron.osikhena@tenece.com);
 * ============================================================
 *
 *
 * Licensed under the Tenece Professional Services;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.tenece.com/
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in
 *   the documentation and/or other materials provided with the
 *   distribution.
 *
 * Neither the name of Strategiex. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "as is," without a warranty of any
 * kind. All express or implied conditions, representations and
 * warranties, including any implied warranty of merchantability,
 * fitness for a particular purpose or non-infringement, are hereby
 * excluded.
 * Tenece and its licensors shall not be liable for any damages
 * suffered by licensee as a result of using, modifying or
 * distributing the software or its derivatives. In no event will Strategiex
 * or its licensors be liable for any lost revenue, profit or data, or
 * for direct, indirect, special, consequential, incidental or
 * punitive damages, however caused and regardless of the theory of
 * liability, arising out of the use of or inability to use software,
 * even if Strategiex has been advised of the possibility of such damages.
 *
 * You acknowledge that Software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of
 * any nuclear facility.
 */

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.tenece.web.common;

import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;
import java.awt.Graphics;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author jeffry.amachree
 */

public class ImageProcessor {

    public static byte[] cropImages(File imageFile, int xPos, int yPos, int iWidth, int iHeight) throws IOException{

        int xIndex = xPos;//Integer.valueOf(req.getParameter("t")).intValue();
        int yIndex = yPos;//Integer.valueOf(req.getParameter("l")).intValue();
        int width = iWidth;//Integer.valueOf(req.getParameter("w")).intValue();
        int height = iHeight;//Integer.valueOf(req.getParameter("h")).intValue();
        File file = imageFile;

        String imagePath = file.getAbsolutePath();//getServletContext().getRealPath("") + System.getProperty("file.separator")  + req.getParameter("i");

        String[] formatNames = ImageIO.getReaderFormatNames();
        for(String formatName : formatNames){
            System.out.println("Format Name: " + formatName);
        }


        System.out.println("File Exist:" + file.exists());
        BufferedImage outImage = ImageIO.read(new FileInputStream(file));

        BufferedImage cropped = outImage.getSubimage(xIndex, yIndex, width, height);

        ByteArrayOutputStream croppedOut = new ByteArrayOutputStream();
        ImageIO.write(cropped, "PNG", croppedOut);

        return croppedOut.toByteArray();
    }

    public static byte[] cropImages(byte[] source, String fileName, int xPos, int yPos, int iWidth, int iHeight) throws IOException{

        
        File file = new File(fileName);

        DataOutputStream dOut = new DataOutputStream(new FileOutputStream(file));
        dOut.write(source);
        dOut.flush();
        dOut.close();

        byte[] croppedBytes = cropImages(file, xPos, yPos, iWidth, iHeight);

        return croppedBytes;
    }


    public static byte[] convertPDFToImage(String fileName, int page) throws Exception{
        File file = new File(fileName);
        return convertPDFToImage(file, page);
    }
    
    public static byte[] convertPDFToImage(File file, int page) throws Exception{
        List<byte[]> imageList = convertPDFtoImages(file);
        if(page == 0 || page > imageList.size()){
            throw new Exception("Invalid Page Index Specified.");
        }
        //index is zero base and we need to reduce our page number by one in our parameter
        return imageList.get(page - 1);
    }

    public static List convertPDFtoImages(String fileName) throws Exception{
        File file = new File(fileName);
        return convertPDFtoImages(file);
    }
    public static List convertPDFtoImages(File file) throws Exception{
        List<byte[]> imagePages = new ArrayList<byte[]>();

        //load a pdf from a byte buffer
        
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        FileChannel channel = raf.getChannel();
        ByteBuffer buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
        PDFFile pdffile = new PDFFile(buf);

        int numPgs = pdffile.getNumPages();

        //PDF documents can be reference using the exact page number and not a zero based index
        for (int i=1; i<=numPgs; i++)
        {
            // draw the first page to an image
            PDFPage page = pdffile.getPage(i);

            //get the width and height for the doc at the default zoom
            Rectangle rect = new Rectangle(0,0,
                    (int)page.getBBox().getWidth(),
                    (int)page.getBBox().getHeight());

            //generate the image
            Image img = page.getImage(
                    rect.width, rect.height, //width & height
                    rect, // clip rect
                    null, // null for the ImageObserver
                    true, // fill background with white
                    true  // block until drawing is done
                    );


            //save it as a file
            BufferedImage bImg = toBufferedImage( img );
            //File yourImageFile = new File("/home/amachree/page_" + i + ".png");
            //ImageIO.write( bImg,"png",yourImageFile);

            ByteArrayOutputStream pdfPage = new ByteArrayOutputStream();
            ImageIO.write(bImg, "PNG", pdfPage);

            imagePages.add(pdfPage.toByteArray());
        }
        return imagePages;
    }

    // This method returns a buffered image with the contents of an image
    public static BufferedImage toBufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage)image;
        }

        // This code ensures that all the pixels in the image are loaded
        image = new ImageIcon(image).getImage();

        // Determine if the image has transparent pixels; for this method's
        // implementation, see e661 Determining If an Image Has Transparent Pixels
        boolean hasAlpha = false;//hasAlpha(image);

        // Create a buffered image with a format that's compatible with the screen
        BufferedImage bimage = null;
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        try {
            // Determine the type of transparency of the new buffered image
            int transparency = Transparency.OPAQUE;
            if (hasAlpha) {
                transparency = Transparency.BITMASK;
            }

            // Create the buffered image
            GraphicsDevice gs = ge.getDefaultScreenDevice();
            GraphicsConfiguration gc = gs.getDefaultConfiguration();
            bimage = gc.createCompatibleImage(
                image.getWidth(null), image.getHeight(null), transparency);
        } catch (HeadlessException e) {
            // The system does not have a screen
        }

        if (bimage == null) {
            // Create a buffered image using the default color model
            int type = BufferedImage.TYPE_INT_RGB;
            if (hasAlpha) {
                type = BufferedImage.TYPE_INT_ARGB;
            }
            bimage = new BufferedImage(image.getWidth(null), image.getHeight(null), type);
        }

        // Copy image to buffered image
        Graphics g = bimage.createGraphics();

        // Paint the image onto the buffered image
        g.drawImage(image, 0, 0, null);
        g.dispose();

        return bimage;
}

    public static void main(String[] args){
        try{
            /*
            File file = new File("/home/amachree/Pictures/imgArchitecture.gif");
            File file2 = new File("/home/amachree/Pictures/imgArchitecture1.png");
            byte[] data1 = new byte[(int)file.length()];
            DataInputStream dIn = new DataInputStream(new FileInputStream(file));
            dIn.read(data1);
            
            byte[] data2 = ImageProcessor.cropImages(data1, file.getName(), 5, 165, 163, 232);
            DataOutputStream dOut = new DataOutputStream(new FileOutputStream(file2));
            dOut.write(data2);
             */
            File file = new File("/home/amachree/Documents/centagemortgageloanform.pdf");
            File file2 = new File("/home/amachree/Documents/centagemortgageloanform_1.png");
            byte[] content = ImageProcessor.convertPDFToImage(file.getAbsolutePath(), 2);
            DataOutputStream dOut = new DataOutputStream(new FileOutputStream(file2));
            dOut.write(content);
            dOut.flush();
            dOut.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
