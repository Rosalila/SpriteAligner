/*
 * fightermakerView.java
 */

package rosalila.studio;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Frame;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.TreeSelectionEvent;
import org.jdesktop.application.Action;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.SingleFrameApplication;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.TaskMonitor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;
import javax.swing.Icon;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.imageio.ImageIO;


import java.io.File;
import java.io.FileOutputStream;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.util.ArrayList;
import javax.swing.JOptionPane;
/**
 * The application's main frame.
 */
public class fightermakerView extends FrameView implements TreeSelectionListener {

    JFrame main_frame;
    String frame_title;
    String click_target_point;
    boolean click_target_flag;
    Color current_point_color;
    
    Document main_doc;
    Document sfx_doc;
    Document sprites_doc;
    String directory_path;
    
    Element current_move;
    Element current_sprite;
    
    int x,y;
    
    NodeList listOfMoves_main_file;
    NodeList listOfMoves_sprites_file;
    
    String hitbox_color_selected;
    int hitbox_index_selected;
    int frame_index_selected;    
    
    public fightermakerView(SingleFrameApplication app) {
        super(app);
        
        current_move=null;
        current_sprite=null;
        
        x=0;
        y=0;
        
        listOfMoves_main_file=null;
        listOfMoves_sprites_file=null;
        
        hitbox_color_selected="";
        hitbox_index_selected=-1;
        frame_index_selected=-1;
        
        click_target_point="1";
        click_target_flag=false;
        
        frame_title = "Rosalila engine hitboxes editor";
        main_frame = new JFrame(frame_title);
        current_point_color = new Color(23,114,0);
        
        this.setFrame(main_frame);
        
        //bad code to remove warning
        java.util.logging.Logger logger = java.util.logging.Logger.getLogger(org.jdesktop.application.SessionStorage.class.getName());
        logger.setLevel(java.util.logging.Level.OFF);

        initComponents();
        
        // status bar initialization - message timeout, idle icon and busy animation, etc
        ResourceMap resourceMap = getResourceMap();
        int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            }
        });
        messageTimer.setRepeats(false);
        int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");

        // connecting action tasks to status bar via TaskMonitor
        TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                String propertyName = evt.getPropertyName();
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                } else if ("message".equals(propertyName)) {
                    String text = (String)(evt.getNewValue());
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {

                }
            }
        });
        
        JFrame mainFrame = fightermaker.getApplication().getMainFrame();
        mainFrame.setTitle("Buurn baby!");
        
    }

    private DefaultMutableTreeNode processHierarchy(Object[] hierarchy) {
    DefaultMutableTreeNode node =
      new DefaultMutableTreeNode(hierarchy[0]);
    DefaultMutableTreeNode child;
    for(int i=1; i<hierarchy.length; i++) {
      Object nodeSpecifier = hierarchy[i];
      if (nodeSpecifier instanceof Object[])  // Ie node with children
        child = processHierarchy((Object[])nodeSpecifier);
      else
        child = new DefaultMutableTreeNode(nodeSpecifier); // Ie Leaf
      node.add(child);
    }
    return(node);
  }
    
    @Action
    public void showAboutBox() {
        if (aboutBox == null) {
            JFrame mainFrame = fightermaker.getApplication().getMainFrame();
            aboutBox = new fightermakerAboutBox(mainFrame);
            aboutBox.setLocationRelativeTo(mainFrame);
        }
        fightermaker.getApplication().show(aboutBox);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        list_moves = new javax.swing.JList();
        jLabel9 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        list_frames = new javax.swing.JList();
        label_current_sprite = new javax.swing.JLabel();
        image_panel = new rosalila.studio.ImagePanel();
        jLabel2 = new javax.swing.JLabel();
        spinner_x = new javax.swing.JSpinner();
        jLabel3 = new javax.swing.JLabel();
        spinner_y = new javax.swing.JSpinner();
        button_set_bg_image = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        javax.swing.JMenu fileMenu = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        javax.swing.JMenuItem exitMenuItem = new javax.swing.JMenuItem();
        javax.swing.JMenu helpMenu = new javax.swing.JMenu();
        javax.swing.JMenuItem aboutMenuItem = new javax.swing.JMenuItem();

        mainPanel.setName("panel_main"); // NOI18N

        jScrollPane1.setName("jScrollPane1"); // NOI18N

        jPanel1.setAlignmentX(0.0F);
        jPanel1.setAlignmentY(0.0F);
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.setName("jPanel1"); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(1408, 800));

        jScrollPane8.setName("jScrollPane8"); // NOI18N

        list_moves.setName("list_moves"); // NOI18N
        list_moves.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                list_movesMousePressed(evt);
            }
        });
        jScrollPane8.setViewportView(list_moves);

        org.jdesktop.application.ResourceMap resourceMap = org.jdesktop.application.Application.getInstance(rosalila.studio.fightermaker.class).getContext().getResourceMap(fightermakerView.class);
        jLabel9.setFont(resourceMap.getFont("jLabel9.font")); // NOI18N
        jLabel9.setText(resourceMap.getString("jLabel9.text")); // NOI18N
        jLabel9.setName("jLabel9"); // NOI18N

        jLabel1.setFont(resourceMap.getFont("jLabel1.font")); // NOI18N
        jLabel1.setText(resourceMap.getString("jLabel1.text")); // NOI18N
        jLabel1.setName("jLabel1"); // NOI18N

        jScrollPane10.setName("jScrollPane10"); // NOI18N

        list_frames.setName("list_frames"); // NOI18N
        list_frames.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                list_framesMousePressed(evt);
            }
        });
        jScrollPane10.setViewportView(list_frames);

        label_current_sprite.setFont(resourceMap.getFont("label_current_sprite.font")); // NOI18N
        label_current_sprite.setText(resourceMap.getString("label_current_sprite.text")); // NOI18N
        label_current_sprite.setName("label_current_sprite"); // NOI18N

        image_panel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        image_panel.setName("image_panel"); // NOI18N
        image_panel.addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                image_panelMouseWheelMoved(evt);
            }
        });
        image_panel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                image_panelMousePressed(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                image_panelMouseClicked(evt);
            }
        });
        image_panel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                image_panelMouseMoved(evt);
            }
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                image_panelMouseDragged(evt);
            }
        });

        javax.swing.GroupLayout image_panelLayout = new javax.swing.GroupLayout(image_panel);
        image_panel.setLayout(image_panelLayout);
        image_panelLayout.setHorizontalGroup(
            image_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 357, Short.MAX_VALUE)
        );
        image_panelLayout.setVerticalGroup(
            image_panelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 634, Short.MAX_VALUE)
        );

        jLabel2.setText(resourceMap.getString("jLabel2.text")); // NOI18N
        jLabel2.setName("jLabel2"); // NOI18N

        spinner_x.setName("spinner_x"); // NOI18N
        spinner_x.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinner_xStateChanged(evt);
            }
        });

        jLabel3.setText(resourceMap.getString("jLabel3.text")); // NOI18N
        jLabel3.setName("jLabel3"); // NOI18N

        spinner_y.setName("spinner_y"); // NOI18N
        spinner_y.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                spinner_yStateChanged(evt);
            }
        });

        button_set_bg_image.setText(resourceMap.getString("button_set_bg_image.text")); // NOI18N
        button_set_bg_image.setName("button_set_bg_image"); // NOI18N
        button_set_bg_image.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button_set_bg_imageMousePressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(label_current_sprite)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel1)
                            .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(spinner_x, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(spinner_y, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(button_set_bg_image))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(image_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(403, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 23, Short.MAX_VALUE)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, 638, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(84, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(spinner_x, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(spinner_y, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(button_set_bg_image)
                .addGap(350, 350, 350)
                .addComponent(label_current_sprite)
                .addContainerGap(130, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(image_panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(166, Short.MAX_VALUE))
        );

        jScrollPane1.setViewportView(jPanel1);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1112, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 756, Short.MAX_VALUE)
        );

        jScrollPane1.getAccessibleContext().setAccessibleParent(mainPanel);

        mainPanel.getAccessibleContext().setAccessibleName(resourceMap.getString("panel_main.AccessibleContext.accessibleName")); // NOI18N
        mainPanel.getAccessibleContext().setAccessibleDescription(resourceMap.getString("panel_main.AccessibleContext.accessibleDescription")); // NOI18N

        menuBar.setName("menuBar"); // NOI18N

        fileMenu.setText(resourceMap.getString("fileMenu.text")); // NOI18N
        fileMenu.setName("fileMenu"); // NOI18N

        jMenuItem1.setText(resourceMap.getString("jMenuItem1.text")); // NOI18N
        jMenuItem1.setName("jMenuItem1"); // NOI18N
        jMenuItem1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jMenuItem1MousePressed(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jMenuItem1MouseClicked(evt);
            }
        });
        fileMenu.add(jMenuItem1);

        javax.swing.ActionMap actionMap = org.jdesktop.application.Application.getInstance(rosalila.studio.fightermaker.class).getContext().getActionMap(fightermakerView.class, this);
        exitMenuItem.setAction(actionMap.get("quit")); // NOI18N
        exitMenuItem.setName("exitMenuItem"); // NOI18N
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        helpMenu.setText(resourceMap.getString("helpMenu.text")); // NOI18N
        helpMenu.setName("helpMenu"); // NOI18N

        aboutMenuItem.setAction(actionMap.get("showAboutBox")); // NOI18N
        aboutMenuItem.setName("aboutMenuItem"); // NOI18N
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setComponent(mainPanel);
        setMenuBar(menuBar);
    }// </editor-fold>//GEN-END:initComponents

private void jMenuItem1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem1MouseClicked
    //Erase me if you can
}//GEN-LAST:event_jMenuItem1MouseClicked

private void jMenuItem1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jMenuItem1MousePressed
    try {

            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            JFrame mainFrame = fightermaker.getApplication().getMainFrame();
            
            //Load XML file            
            JFileChooser chooser = new JFileChooser(); 
            chooser.setCurrentDirectory(new java.io.File("."));
            chooser.setDialogTitle("Choose you character");
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setAcceptAllFileFilterUsed(false);
            chooser.showOpenDialog(mainFrame);

            File selFile = chooser.getSelectedFile();
            directory_path = selFile.getPath();

            //Parse XML file            
            main_doc = docBuilder.parse (new File(directory_path+"/main.xml"));
            sfx_doc = docBuilder.parse (new File(directory_path+"/sfx.xml"));
            sprites_doc = docBuilder.parse (new File(directory_path+"/sprites.xml"));

            // normalize text representation
            main_doc.getDocumentElement ().normalize ();

            listOfMoves_main_file = main_doc.getElementsByTagName("Move");

            DefaultListModel model = new DefaultListModel();
            
            for(int s=0; s<listOfMoves_main_file.getLength() ; s++)
            {
                model.add(s, ((Element)listOfMoves_main_file.item(s)).getAttribute("name"));
            }
            
            list_moves.setModel(model);

        }catch (SAXParseException err) {
        System.out.println ("** Parsing error" + ", line " 
             + err.getLineNumber () + ", uri " + err.getSystemId ());
        System.out.println(" " + err.getMessage ());

        }catch (SAXException e) {
        Exception x = e.getException ();
        ((x == null) ? e : x).printStackTrace ();

        }catch (Throwable t) {
        t.printStackTrace ();
        }
}//GEN-LAST:event_jMenuItem1MousePressed

private void list_movesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list_movesMousePressed
    current_move=null;
    current_sprite=null;
    hitbox_index_selected=-1;
    frame_index_selected=-1;
    //Add frames
    listOfMoves_main_file = main_doc.getElementsByTagName("Move");

    DefaultListModel model = new DefaultListModel();

    for(int s=0; s<listOfMoves_main_file.getLength() ; s++)
    {
        Node move_node = listOfMoves_main_file.item(s);
        Element move_element = (Element)move_node;
        if(move_element.getAttribute("name").equals(list_moves.getSelectedValue()))
        {
            int frames = Integer.parseInt(move_element.getAttribute("frames"));
            for(int i=0;i<frames;i++)
            {
                model.add(i, "frame "+(i+1));
            }
            break;
        }
    }

    list_frames.setModel(model);

    //Clear hitboxes list
    DefaultListModel clean_model = new DefaultListModel();
    
    //Upadate current_move
    listOfMoves_sprites_file = sprites_doc.getElementsByTagName("Move");
    for(int s=0; s<listOfMoves_sprites_file.getLength() ; s++)//Move loop
    {
        Node move_node = listOfMoves_sprites_file.item(s);
        Element move_element = (Element)move_node;
        if(move_element.getAttribute("name").equals(list_moves.getSelectedValue()))
        {
            current_move=move_element;
        }
    }
}//GEN-LAST:event_list_movesMousePressed

private void list_framesMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_list_framesMousePressed
    current_sprite=null;
    hitbox_index_selected=-1;
    frame_index_selected=Integer.parseInt(""+list_frames.getSelectedIndex());
    //NodeList listOfMoves = hitboxes_doc.getElementsByTagName("Move");
    
    listOfMoves_sprites_file = sprites_doc.getElementsByTagName("Move");

    boolean frame_found=false;
    
    for(int s=0; s<listOfMoves_sprites_file.getLength() ; s++)//Move loop
    {
        Node move_node = listOfMoves_sprites_file.item(s);
        Element move_element = (Element)move_node;
        if(move_element.getAttribute("name").equals(list_moves.getSelectedValue()))
        {
            for(Node frame=move_element.getFirstChild();frame!=null;frame=frame.getNextSibling())//Frame loop
            {
                if(frame.getNodeName().equals("Sprite"))
                {
                    String frame_number = "frame " + ((Element)frame).getAttribute("frame_number");
                    if( frame_number.equals((String)list_frames.getSelectedValue()) )
                    {
                        current_sprite=(Element) frame;
                        updateSprites(current_sprite);
                        frame_found=true;
                    }
                }
            }
        }
    }
    printSprite();
}//GEN-LAST:event_list_framesMousePressed

void updateSprites(Element frame)
{ 
    frame.setAttribute("align_x", ""+spinner_x.getValue());
    frame.setAttribute("align_y", ""+spinner_y.getValue());    
    saveSprite();
}

void saveSprite()
{
    try {
        //Save XML file
        OutputFormat format = new OutputFormat(sprites_doc);
        format.setIndenting(true);
        XMLSerializer serializer;
        serializer = new XMLSerializer(new FileOutputStream(new File(directory_path+"/sprites.xml")), format);
        serializer.serialize(sprites_doc);
    } catch (FileNotFoundException ex) {
        Logger.getLogger(fightermakerView.class.getName()).log(Level.SEVERE, null, ex);
    } catch (IOException ex) {
        Logger.getLogger(fightermakerView.class.getName()).log(Level.SEVERE, null, ex);
    }
}

void printSprite()
{
    //Get the current sprite path

    NodeList listOfMovesSprites = sprites_doc.getElementsByTagName("Move");
    
    boolean sprite_found=false;

    for(int s=0; s<listOfMovesSprites.getLength() ; s++)
    {
        Node move_node = listOfMovesSprites.item(s);
        Element move_element = (Element)move_node;
        if(move_element.getAttribute("name").equals(list_moves.getSelectedValue()))
        {
            for(Node sprite=move_node.getFirstChild();sprite!=null;sprite=sprite.getNextSibling())//Sprite loop
            {
                if(sprite.getNodeName().equals("Sprite"))
                {
                    String frame_number = "frame " + ((Element)sprite).getAttribute("frame_number");
                    if(frame_number.equals((String)list_frames.getSelectedValue()) )
                    {
                        sprite_found=true;
                        String sprite_path="/"+((Element)sprite).getAttribute("path");
                        int align_x=Integer.parseInt(((Element)sprite).getAttribute("align_x"));
                        int align_y=Integer.parseInt(((Element)sprite).getAttribute("align_y"));
                        //Print sprite
                        ((ImagePanel)image_panel).setImage(directory_path+sprite_path,align_x,align_y);
                        label_current_sprite.setText(sprite_path);
                    }
                }
            }
        }
    }
    if(!sprite_found)
    {
        ((ImagePanel)image_panel).setImage("assets/LogoEngine.png");
        label_current_sprite.setText("assets/LogoEngine.png");
    }
}

private void image_panelMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_image_panelMouseDragged
    // TODO add your handling code here:
    //((ImagePanel)this.jPanel1).setLocation(evt.getX(), evt.getY());
//    ((ImagePanel)this.image_panel).x=evt.getX();
//    ((ImagePanel)this.image_panel).y=evt.getY();
//    ((ImagePanel)this.image_panel).repaint();
}//GEN-LAST:event_image_panelMouseDragged

private void image_panelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_image_panelMouseClicked
    // TODO add your handling code here:
}//GEN-LAST:event_image_panelMouseClicked

private void image_panelMouseWheelMoved(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_image_panelMouseWheelMoved

//    if(evt.getWheelRotation()<0) {
//        ((ImagePanel)this.image_panel).scale-=0.01;
//    }else {
//        ((ImagePanel)this.image_panel).scale+=0.01;
//    }
//    ((ImagePanel)this.image_panel).repaint();
}//GEN-LAST:event_image_panelMouseWheelMoved

private void image_panelMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_image_panelMouseMoved
    int pos_x = (evt.getX()-image_panel.getWidth()/2);
    int pos_y = (image_panel.getHeight()-evt.getY());
    main_frame.setTitle(frame_title+" ["+pos_x+","+pos_y+"]");
}//GEN-LAST:event_image_panelMouseMoved

private void image_panelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_image_panelMousePressed
    ((ImagePanel)image_panel).setCurrentAsBackground();
}//GEN-LAST:event_image_panelMousePressed

private void spinner_xStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinner_xStateChanged
    updateSprites(current_sprite);
    printSprite();
}//GEN-LAST:event_spinner_xStateChanged

private void button_set_bg_imageMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_button_set_bg_imageMousePressed
    ((ImagePanel)image_panel).setCurrentAsBackground();
}//GEN-LAST:event_button_set_bg_imageMousePressed

private void spinner_yStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_spinner_yStateChanged
    updateSprites(current_sprite);
    printSprite();
}//GEN-LAST:event_spinner_yStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton button_set_bg_image;
    private javax.swing.JPanel image_panel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JLabel label_current_sprite;
    private javax.swing.JList list_frames;
    private javax.swing.JList list_moves;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JSpinner spinner_x;
    private javax.swing.JSpinner spinner_y;
    // End of variables declaration//GEN-END:variables

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private JDialog aboutBox;

    public void valueChanged(TreeSelectionEvent tse) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
