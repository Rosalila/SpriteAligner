/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rosalila.studio;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class ImagePanel extends JPanel{

    public BufferedImage original_image;
    public BufferedImage scaled_image;
    public BufferedImage axis;
    public int x;
    public int y;
    int axis_x,axis_y;
    public double scale;
    Graphics g;

    public ImagePanel() {
        x=0;
        y=0;
        axis_x=0;
        axis_y=0;
        scale=1;
       try {
          original_image = ImageIO.read(new File("assets/LogoEngine.png"));
          axis = ImageIO.read(new File("assets/Axis.png"));
       } catch (IOException ex) {
            // handle exception...
       }

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        myScale();
        
        int pos_x=this.getWidth()/2-scaled_image.getWidth()/2+x;
        int pos_y=this.getHeight()-scaled_image.getHeight()-y;
        g.drawImage(scaled_image, pos_x,pos_y, null);
        
        axis_x=this.getWidth()/2-axis.getWidth()/2;
        axis_y=this.getHeight()-axis.getHeight()-3;
        g.drawImage(axis, axis_x,axis_y, null);
    }

    void myScale()
    {
        scaled_image = new BufferedImage((int)((double)original_image.getWidth()*scale),(int)((double)original_image.getHeight()*scale), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = scaled_image.createGraphics();
        AffineTransform xform = AffineTransform.getScaleInstance(scale, scale);
        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        graphics2D.drawImage(original_image, xform, null);
        graphics2D.dispose();
    }
    void setImage(String path)
    {
        try {
          original_image = ImageIO.read(new File(path));
        } catch (IOException ex) {
            // handle exception...
        }
        repaint();
    }
    
    void setImage(String path,int x,int y)
    {
        try {
          original_image = ImageIO.read(new File(path));
          this.x=x;
          this.y=y;
//          this.setPreferredSize(new Dimension(original_image.getWidth()+Math.abs(x)*2, original_image.getHeight()+Math.abs(y)*2));
//          int size_x_parrent=this.getX()+this.getPreferredSize().width;
//          int size_y_parrent = this.getY()+this.getPreferredSize().height;
//          if(size_x_parrent<1408)
//              size_x_parrent=1408;
//          if(size_y_parrent<800)
//              size_y_parrent=800;
//          this.getParent().setPreferredSize(new Dimension(size_x_parrent,size_y_parrent));
//          this.revalidate();
        } catch (IOException ex) {
            // handle exception...
        }
        repaint();
    }
}