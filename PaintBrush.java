//********************************************************************************************************************************************
// PaintBrush.java         Author:Ali Berk Karaarslan     Date:07.04.2023
//
// Basic version of PaintBrush program.
// Contains: Drawing Rectangles,Ovals and Moving these shapes. Drawing Lines. Using Pen and Brush.Choosing Colors .Saving and Loading Files.
//
//********************************************************************************************************************************************

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

class MenuBar extends JMenuBar {
    //MenuBar Of the program. Contains "File","Settings","Help" Sections.

    public MenuBar(DrawablePanel drawingArea){

        JMenu fileMenu = new JMenu("File");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem load = new JMenuItem("Load");

        JMenu settings = new JMenu("Settings");
        JMenu brushSize = new JMenu("Brush Size");
        JMenuItem brushSize10 = new JMenuItem("10");
        JMenuItem brushSize20 = new JMenuItem("20");
        JMenuItem brushSize30 = new JMenuItem("30");
        JMenuItem brushSize40 = new JMenuItem("40");

        JMenu help = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");
        JMenuItem saveInfo = new JMenuItem("Saving Info");

        //If user presses the save option under fileMenu
        save.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){

                //Getting the save location
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Specify a path to save");
                File defaultPath = new File("C:\\Users\\Ali Berk KARAARSLAN\\Desktop");
                fileChooser.setCurrentDirectory(defaultPath);
                fileChooser.setFileFilter(new FileNameExtensionFilter("*.abk", "abk"));

                //Save location
                int userSelection = fileChooser.showSaveDialog(new JFrame());

                //If save location is valid then saves the files with new format.
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    saveFile(fileToSave.getAbsolutePath()+".abk", drawingArea);
                }
            }
        });
        //If user presses the load option under fileMenu
        load.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){

                //Getting the load location
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setDialogTitle("Select a file to load");
                File defaultPath = new File("C:\\Users\\Ali Berk KARAARSLAN\\Desktop");
                fileChooser.setCurrentDirectory(defaultPath);
                fileChooser.setFileFilter(new FileNameExtensionFilter("*.abk", "abk"));

                //Load location
                int userSelection = fileChooser.showSaveDialog(new JFrame());

                //If load location is valid then loads the files.
                if (userSelection == JFileChooser.APPROVE_OPTION) {
                    File fileToSave = fileChooser.getSelectedFile();
                    loadFile(fileToSave.getAbsolutePath(), drawingArea);
                }
            }
        });
        fileMenu.add(save);
        fileMenu.add(load);
        add(fileMenu);

        //Setting the BrushSize option buttons
        brushSize10.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawingArea.brushRadius = 10;
            }
        });
        brushSize20.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawingArea.brushRadius = 20;
            }
        });
        brushSize30.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawingArea.brushRadius = 30;
            }
        });
        brushSize40.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                drawingArea.brushRadius = 40;
            }
        });

        brushSize.add(brushSize10);
        brushSize.add(brushSize20);
        brushSize.add(brushSize30);
        brushSize.add(brushSize40);

        settings.add(brushSize);
        add(settings);

        //If user presses About option under help menu then creates AboutFrame
        about.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new AboutFrame();
            }
        });
        //If user presses Saving Info option under help menu then creates SaveInfoFrame
        saveInfo.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SaveInfoFrame();
            }
        });

        help.add(saveInfo);
        help.add(about);
        add(help);

        setVisible(true);
    }

    //Saves the file in given location. (Saves "figures" ArrayList in DrawablePanel)
    public void saveFile(String address,DrawablePanel drawingArea){

        ObjectOutputStream writer = null;

        try{
            writer =new ObjectOutputStream(new FileOutputStream(address));

            writer.writeInt(drawingArea.figures.size());

            //Saves all elements of "figures" ArrayList in DrawablePanel
            for(Figure figure: drawingArea.figures){
                writer.writeObject(figure);
            }

            writer.close();
            System.out.println("File Saved Successfully in: " + address);

        }catch(IOException e){
            System.out.println("Saving Exception Has Occurred");
        }
    }
    //Loads the file from given location. (Recreates "figures" ArrayList in DrawablePanel)
    public void loadFile(String address,DrawablePanel drawingArea){

        ObjectInputStream loader = null;

        try{
            loader = new ObjectInputStream(new FileInputStream(address));
            int size = loader.readInt();

            //Loads all elements of "figures" ArrayList in DrawablePanel
            drawingArea.figures.clear();
            for(int i =0; i< size; i++){
                drawingArea.figures.add((Figure) loader.readObject());
            }

            drawingArea.repaint();
            loader.close();

            System.out.println("File Loaded Successfully from: " + address);

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Loading Exception Has Occurred");
        }
    }

    //Contains Information about saving
    class SaveInfoFrame extends JFrame{

        public SaveInfoFrame(){

            setSize(350,150);
            setLayout(new BorderLayout());
            setLocationRelativeTo(null);
            setTitle("Information About Saving Files");

            JLabel text1 = new JLabel("Files are saved in ABK (.abk) format.", SwingConstants.CENTER);
            JLabel text2 = new JLabel("Therefore only ABK formatted files could be loaded.", SwingConstants.CENTER);
            JLabel text3 = new JLabel("(ABK format has specially designed for this program.)", SwingConstants.CENTER);

            //Makes texts Italic
            Font italicFont=new Font(text1.getFont().getName(),Font.ITALIC,text1.getFont().getSize());
            text1.setFont(italicFont);
            text2.setFont(italicFont);
            text3.setFont(italicFont);

            JPanel centerPanel = new JPanel();
            centerPanel.setLayout(new BorderLayout());

            centerPanel.add(text1,BorderLayout.NORTH);
            centerPanel.add(text2,BorderLayout.CENTER);
            centerPanel.add(text3,BorderLayout.SOUTH);

            add(centerPanel,BorderLayout.CENTER);

            JPanel buttons = new JPanel();
            buttons.setLayout(new FlowLayout());

            JButton close = new JButton("Close");
            close.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    dispose();
                }
            });

            buttons.add(close);

            JPanel southPanel = new JPanel();
            southPanel.setLayout(new BorderLayout());

            southPanel.add(buttons,BorderLayout.EAST);
            add(southPanel,BorderLayout.SOUTH);

            setVisible(true);
        }
    }

    //Contains Information about Programmer and Program
    class AboutFrame extends JFrame{

        public AboutFrame(){

            setSize(300,150);
            setLayout(new BorderLayout());
            setLocationRelativeTo(null);
            setTitle("About PaintBrush");

            JLabel text1 = new JLabel("PaintBrush", SwingConstants.CENTER);
            JLabel text2 = new JLabel("Made By Ali Berk Karaarslan", SwingConstants.CENTER);
            JLabel text3 = new JLabel("07.04.2023 (Made in Java)", SwingConstants.CENTER);

            //Makes texts Italic
            Font italicFont=new Font(text1.getFont().getName(),Font.ITALIC,text1.getFont().getSize());
            text2.setFont(italicFont);
            text3.setFont(italicFont);

            JPanel centerPanel = new JPanel();
            centerPanel.setLayout(new BorderLayout());

            centerPanel.add(text1,BorderLayout.NORTH);
            centerPanel.add(text2,BorderLayout.CENTER);
            centerPanel.add(text3,BorderLayout.SOUTH);

            add(centerPanel,BorderLayout.CENTER);

            JPanel buttons = new JPanel();
            buttons.setLayout(new FlowLayout());

            JButton close = new JButton("Close");
            close.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    dispose();
                }
            });

            buttons.add(close);

            JPanel southPanel = new JPanel();
            southPanel.setLayout(new BorderLayout());

            southPanel.add(buttons,BorderLayout.EAST);
            add(southPanel,BorderLayout.SOUTH);

            setVisible(true);
        }
    }
}

class ConfirmFrame extends JFrame{
    //Asks user if she/he wants to exit the program.
    //If user presses "Yes", saves the file then exits the program.
    //If user presses "No", do not save the file and exit the program
    //If user presses "Cancel", returns to program

    public ConfirmFrame(DrawablePanel drawingArea){

        setSize(300,170);
        setLayout(new BorderLayout());
        setTitle("Confirm Exit");
        //Sets pop up location to center
        setLocationRelativeTo(null);

        JLabel text = new JLabel("Do you want to save changes?", SwingConstants.CENTER);
        add(text,BorderLayout.CENTER);

        JPanel buttons = new JPanel();
        buttons.setLayout(new FlowLayout());

        JButton yes = new JButton("Yes");
        JButton no = new JButton("No");
        JButton cancel = new JButton("Cancel");

        //Assigning buttons actions

        //If user presses yes button then asks for save location. After that saves the current file in ABK formatted
        //(Calls MenuBar's saveFile() method)
        yes.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){

                    //Gets the save location
                    JFileChooser fileChooser = new JFileChooser();
                    fileChooser.setDialogTitle("Specify a path to save");
                    File defaultPath = new File("C:\\Users\\Ali Berk KARAARSLAN\\Desktop");
                    fileChooser.setCurrentDirectory(defaultPath);

                    //Creates MenuBar object to use methods of it
                    MenuBar menubar = new MenuBar(drawingArea);

                    //Save location
                    int userSelection = fileChooser.showSaveDialog(new JFrame());

                    //If save location is valid then saves the files with new format. (calls the saveFile() method in MenuBar)
                    if (userSelection == JFileChooser.APPROVE_OPTION) {
                        File fileToSave = fileChooser.getSelectedFile();
                        menubar.saveFile(fileToSave.getAbsolutePath()+".abk", drawingArea);
                    }
                    System.exit(0);
                }
        });
        //If user presses no then exits the program with doing nothing
        no.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    System.exit(0);
                }
        });
        //If user presses cancel then closes confirm frame
        cancel.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                dispose();
            }
        });

        //Adding the buttons and setting the layout
        buttons.add(yes);
        buttons.add(no);
        buttons.add(cancel);

        JPanel southPanel = new JPanel();
        southPanel.setLayout(new BorderLayout());

        southPanel.add(buttons,BorderLayout.CENTER);
        add(southPanel,BorderLayout.SOUTH);

        setVisible(true);
    }
}

class Frame extends JFrame implements WindowListener {
    //Combines all the components of the program. Draw Area, Tools, Colors, MenuBar.

    //Initializing main components of frame
    DrawablePanel drawingArea;
    ToolPanel tools;
    MenuBar menuBar;
    ColorPanel colors;

    public Frame(){

        setSize(800,600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setTitle("PaintBrush");
        //Sets pop up location to center
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        //Adding drawable area
        drawingArea = new DrawablePanel();
        add(drawingArea,BorderLayout.CENTER);

        //Adding tools (draw rect , draw oval etc.)
        tools = new ToolPanel(drawingArea);
        add(tools,BorderLayout.NORTH);

        //Adding colors panel
        colors = new ColorPanel(drawingArea);
        tools.add(colors,BorderLayout.NORTH);

        //Adding menu bar
        menuBar = new MenuBar(drawingArea);
        setJMenuBar(menuBar);

        addWindowListener(this);

        setVisible(true);
    }

    //If user presses close button then pops up confirm frame (Asks do you want to save changes)
    public void windowClosing(WindowEvent e) {ConfirmFrame cf = new ConfirmFrame(drawingArea);}
    public void windowOpened(WindowEvent e) {}
    public void windowClosed(WindowEvent e){}
    public void windowIconified(WindowEvent e) {}
    public void windowDeiconified(WindowEvent e) {}
    public void windowActivated(WindowEvent e) {}
    public void windowDeactivated(WindowEvent e) {}
}

class DrawablePanel extends JPanel implements MouseInputListener {
    //Contains a panel that can be drawn on.

    int mousePosX;
    int mousePosY;
    int oldX;
    int oldY;

    //Checks if mouse is pressed
    boolean pressed = false;

    //Checks which tool is in use
    boolean drawRectangle = false;
    boolean drawOval = false;
    boolean drawLine = false;
    boolean moveShape = false;
    boolean brush = false;
    boolean drawStraightLine = false;

    //Checks if user is moving a shape
    boolean withinShape = false;

    //Default draw color (black)
    Color drawColor = Color.BLACK;

    //Default brush radius
    int brushRadius = 20;

    //Contains all figures(shapes and lines) of the frame with create order (First Index means It's first figure)
    //If user moves a shape then its index becomes the last index.
    ArrayList<Figure> figures =new ArrayList<>();

    Shape topmostShape = null;
    int topMostShapeX = -1;
    int topMostShapeY = -1;

    public DrawablePanel() {
        addMouseListener(this);
        addMouseMotionListener(this);
        setBackground(Color.WHITE);
    }

    public void paint(Graphics g) {

        super.paint(g);
        paintAllFigures(g);

        g.setColor(drawColor);

        //Gets the properties of shape (Start coordinates ,width,height)
        int[] position =findProperties(oldX,oldY,mousePosX,mousePosY);

        //Drawing rectangle
        if (pressed && drawRectangle) {
            g.setColor(drawColor);
            g.fillRect(position[0],position[1],position[2],position[3]);
        }

        //Drawing oval
        if (pressed && drawOval) {
            g.setColor(drawColor);
            g.fillOval(position[0],position[1],position[2],position[3]);
        }

        //Drawing line
        if (pressed && drawLine) {
            g.setColor(drawColor);
            g.drawLine(oldX, oldY, mousePosX, mousePosY);
        }

        //Brush
        if (brush) {
            g.setColor(Color.BLACK);
            g.drawOval(mousePosX-(brushRadius/2), mousePosY-(brushRadius/2), brushRadius, brushRadius);
            g.setColor(drawColor);
            g.fillOval(mousePosX-(brushRadius/2), mousePosY-(brushRadius/2), brushRadius, brushRadius);
        }

        //Drawing straight line
        if (pressed && drawStraightLine) {
            g.setColor(drawColor);
            g.drawLine(oldX, oldY, mousePosX, mousePosY);
        }
    }

    //Paints all the figures in create order (with figures ArrayLista)
    public void paintAllFigures(Graphics g) {

        for (Figure figure : figures) {

            //If figure is a shape (rectangle, oval etc.)
            if (figure instanceof Shape) {

                Shape currShape = (Shape) figure;
                g.setColor(currShape.color);
                //Gets the properties of current shape
                int[] position = findProperties(currShape.startX, currShape.startY, currShape.endX, currShape.endY);

                //Paints if it is rectangle
                if (currShape.shapeType.equals("rectangle"))
                    g.fillRect(position[0], position[1], position[2], position[3]);

                    //Paints if it is oval
                else if (currShape.shapeType.equals("oval"))
                    g.fillOval(position[0], position[1], position[2], position[3]);

            }//If figure is line
            else if (figure instanceof Line) {
                Line currLine = (Line) figure;
                g.setColor(currLine.color);

                //Calls the paintLine() method in Line class
                currLine.paintLine(g);

            }//If figure is Brush
            else if (figure instanceof Brush) {
                Brush currBrush = (Brush) figure;

                //Calls makeBrush() method in Brush class
                currBrush.makeBrush(g);
            }
        }
    }

    //Finds the properties of the shape
    //Returns start coordinates , width ,height as array [startX,startY,width,height]
    public int[] findProperties(int oldX,int oldY, int mousePosX,int mousePosY){

        int startX = -1;
        int startY = -1;
        int width =  0;
        int height = 0;

        //Mouse is in southeast of the start point (Down-Right)
        if (mousePosX - oldX >= 0 && mousePosY - oldY >= 0) {
            startX = oldX;
            startY = oldY;
            width = mousePosX - oldX;
            height = mousePosY - oldY;

        }//Mouse is in northeast of the start point (Up-Right)
        else if (mousePosX - oldX >= 0 && mousePosY - oldY < 0) {
            startX = oldX;
            startY = mousePosY;
            width = mousePosX - oldX;
            height = oldY - mousePosY;

        }//Mouse is in southwest of the start point (Down-Left)
        else if (mousePosX - oldX < 0 && mousePosY - oldY >= 0) {
            startX = mousePosX;
            startY = oldY;
            width = oldX - mousePosX;
            height = mousePosY - oldY;

        }//Mouse is in northeast of the start point (Up-Left)
        else if (mousePosX - oldX < 0 && mousePosY - oldY < 0) {
            startX = mousePosX;
            startY = mousePosY;
            width = oldX - mousePosX;
            height = oldY - mousePosY;

        }
        return new int[]{startX,startY,width,height};
    }

    //Overrides of MouseActionListener class' methods

    @Override
    public void mousePressed(MouseEvent e) {
        //Updates the mouse coordinates
        mousePosX = e.getX();
        mousePosY = e.getY();

        oldX = mousePosX;
        oldY = mousePosY;
        pressed = true;

        //If user presses a shape with MoveShape tool ()
        if(moveShape){

            // Finding the topmost shape (If it exists)
            for (Figure figure : figures) {

                if (figure instanceof Shape) {
                    Shape currShape = (Shape) figure;
                    int[] position = findProperties(currShape.startX, currShape.startY, currShape.endX, currShape.endY);

                    //If the shape is rectangle
                    if (currShape.shapeType.equals("rectangle")) {

                        if ((mousePosX <= position[0] + position[2]) && (mousePosX >= position[0])) {
                            if ((mousePosY <= position[1] + position[3]) && (mousePosY >= position[1])) {
                                topmostShape = currShape;
                                topMostShapeX = position[0];
                                topMostShapeY = position[1];
                            }
                        }
                    }
                    //If the shape is oval then checks if It's in the oval by using oval formula
                    //Oval formula:  ((x^2)/a^2) + ((y^2)/b^2) = 1
                    else if (currShape.shapeType.equals("oval")) {

                        int ovalWidthHalf = position[2] / 2;
                        int ovalHeightHalf = position[3] / 2;

                        //Moving the mousePositions and getting the square of them
                        double firstParam = Math.pow(mousePosX - (position[0] + ovalWidthHalf), 2);
                        double secondParam = Math.pow(mousePosY - (position[1] + ovalHeightHalf), 2);

                        //Checks if It's in the oval
                        if ((firstParam / (ovalWidthHalf * ovalWidthHalf)) + (secondParam / (ovalHeightHalf * ovalHeightHalf)) <= 1) {
                            topmostShape = currShape;
                            topMostShapeX = position[0];
                            topMostShapeY = position[1];
                        }
                    }
                }
            }
            //User is moving a shape
            if(topmostShape!=null)
                withinShape = true;
        }

        //Creates circles and makes it look like brushed
        if(brush){
            //Updates the brush location
            mousePosX = e.getX();
            mousePosY = e.getY();

            //Creates and adds it to figures ArrayList
            Brush currBrush = new Brush(mousePosX-(brushRadius/2),mousePosY-(brushRadius/2),brushRadius,drawColor);
            figures.add(currBrush);
            repaint();
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //Resets the values
        pressed = false;
        withinShape = true;
        topmostShape=null;
        topMostShapeX=-1;
        topMostShapeY=-1;

        //User finished drawing rectangle
        if (drawRectangle) {
            Shape rect = new Shape(oldX, oldY, mousePosX, mousePosY, drawColor,"rectangle");
            figures.add(rect);
        }
        //User finished drawing oval
        if (drawOval) {
            Shape oval = new Shape(oldX, oldY, mousePosX, mousePosY, drawColor,"oval");
            figures.add(oval);
        }
        //User finished drawing straight line
        if(drawStraightLine){
            Line line = new Line(new int[]{oldX,oldY,mousePosX,mousePosY}, drawColor);
            figures.add(line);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        //User is drawing a rectangle.
        if (pressed && drawRectangle) {
            mousePosX = e.getX();
            mousePosY = e.getY();
            repaint();
        }
        //User is drawing a oval.
        if (pressed && drawOval) {
            mousePosX = e.getX();
            mousePosY = e.getY();
            repaint();
        }
        //User is drawing a line.
        if (pressed && drawLine) {

            //Old coordinates is mouse's previous coordinates
            oldX = mousePosX;
            oldY = mousePosY;

            //Updates mouse coordinates
            mousePosX = e.getX();
            mousePosY = e.getY();

            //Creates a small line parts and ands add them to figures ArrayList
            int[] currLineParts = new int[]{oldX,oldY,mousePosX,mousePosY};
            Line line = new Line(currLineParts,drawColor);
            figures.add(line);

            repaint();
        }

        //User is moving a shape
        if (pressed && withinShape) {
            if(topmostShape!=null){
                int index = figures.indexOf(topmostShape);
                Shape currShape = (Shape) figures.get(index);

                int[] position =findProperties(currShape.startX, currShape.startY, currShape.endX, currShape.endY);

                //Calculates the coordinates where mouse holds the shape
                int startXDistance = mousePosX-topMostShapeX;
                int startYDistance = mousePosY-topMostShapeY;
                int endXDistance = topMostShapeX+position[2] - mousePosX;
                int endYDistance = topMostShapeY+position[3] - mousePosY;

                //Updates shape's coordinates
                currShape.startX= e.getX()-startXDistance;
                currShape.startY= e.getY()-startYDistance;
                currShape.endX= e.getX()+endXDistance;
                currShape.endY= e.getY()+endYDistance;

                //Removes the shape from figures ArrayList and adds it again to the end of it.
                figures.add(figures.remove(index));
            }
            repaint();
        }

        //Creates Circles
        if (pressed && brush) {
            //Updates the brush location
            mousePosX = e.getX();
            mousePosY = e.getY();

            //Creates and adds it to figures ArrayList
            Brush currBrush = new Brush(mousePosX-(brushRadius/2),mousePosY-(brushRadius/2),brushRadius,drawColor);
            figures.add(currBrush);
            repaint();
        }

        //User is drawing a straight line.
        if(pressed && drawStraightLine){
            //Updates the mouse location
            mousePosX = e.getX();
            mousePosY = e.getY();

            repaint();
        }
    }
    @Override
    public void mouseMoved(MouseEvent e) {
        //Updates the brush location
        if(brush){
            mousePosX = e.getX();
            mousePosY = e.getY();
            repaint();
        }
    }
    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
    @Override
    public void mouseClicked(MouseEvent e) {}
}

class ToolPanel extends JPanel{
    //Contains all the tools. Line, Shapes, Pen, Undo options.

    public ToolPanel(DrawablePanel panel){

        setLayout(new BorderLayout());

        JPanel tools = new JPanel();
        tools.setLayout(new FlowLayout());
        tools.setBackground(Color.LIGHT_GRAY);

        //Making the buttons and assigning them actions
        JButton drawRectButton = new JButton("Rectangle");
        JButton drawOvalButton = new JButton("Oval");
        JButton drawStraightLineButton = new JButton("Line");
        JButton drawLineButton = new JButton("Pen");
        JButton brushButton = new JButton("Brush");
        JButton moveButton = new JButton("Move");
        JButton clearButton= new JButton("Clear");
        JButton undoButton = new JButton("Undo");

        //Making Center Buttons
        drawRectButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                panel.drawRectangle=true;
                panel.drawOval=false;
                panel.drawStraightLine=false;
                panel.drawLine=false;
                panel.brush=false;
                panel.moveShape=false;

                panel.repaint();
            }
        });
        drawOvalButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                panel.drawRectangle=false;
                panel.drawOval=true;
                panel.drawStraightLine=false;
                panel.drawLine=false;
                panel.brush=false;
                panel.moveShape=false;

                panel.repaint();
            }
        });
        drawStraightLineButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                panel.drawRectangle=false;
                panel.drawOval=false;
                panel.drawStraightLine=true;
                panel.drawLine=false;
                panel.brush=false;
                panel.moveShape=false;

                panel.repaint();
            }
        });
        drawLineButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                panel.drawRectangle=false;
                panel.drawOval=false;
                panel.drawStraightLine=false;
                panel.drawLine=true;
                panel.brush=false;
                panel.moveShape=false;

                panel.repaint();
            }
        });
        brushButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                panel.drawRectangle=false;
                panel.drawOval=false;
                panel.drawStraightLine=false;
                panel.drawLine=false;
                panel.brush=true;
                panel.moveShape=false;

                panel.repaint();
            }
        });
        moveButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                panel.drawRectangle=false;
                panel.drawOval=false;
                panel.drawStraightLine=false;
                panel.drawLine=false;
                panel.brush=false;
                panel.moveShape=true;

                panel.repaint();
            }
        });

        //Adding the buttons
        tools.add(drawRectButton);
        tools.add(drawOvalButton);
        tools.add(drawStraightLineButton);
        tools.add(drawLineButton);
        tools.add(brushButton);
        tools.add(moveButton);


        //Making the west tools
        JPanel westTools = new JPanel();
        westTools.setLayout(new FlowLayout());
        westTools.setBackground(Color.LIGHT_GRAY);

        undoButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                if(!panel.figures.isEmpty())
                    panel.figures.remove(panel.figures.size()-1);
                panel.repaint();
            }
        });

        westTools.add(undoButton);

        //Making the east tools
        JPanel eastTools = new JPanel();
        eastTools.setLayout(new FlowLayout());
        eastTools.setBackground(Color.LIGHT_GRAY);

        clearButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e) {
                panel.figures.clear();
                panel.repaint();
            }
        });

        eastTools.add(clearButton);

        //Adding all tools to panel
        add(tools,BorderLayout.CENTER);
        add(westTools,BorderLayout.WEST);
        add(eastTools,BorderLayout.EAST);
    }
}

class ColorPanel extends JPanel{
    //Contains all the usable colors.

    public ColorPanel(DrawablePanel drawingArea){
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);

        JPanel colors = new JPanel();
        colors.setLayout(new GridLayout());
        colors.setBackground(Color.LIGHT_GRAY);

        //Creating the colors from ColorArea
        ColorArea red = new ColorArea(Color.RED,drawingArea);
        red.setBackground(Color.RED);

        ColorArea orange = new ColorArea(Color.ORANGE,drawingArea);
        orange.setBackground(Color.ORANGE);

        ColorArea yellow = new ColorArea(Color.YELLOW,drawingArea);
        yellow.setBackground(Color.YELLOW);

        ColorArea green = new ColorArea(Color.GREEN,drawingArea);
        green.setBackground(Color.GREEN);

        ColorArea blue = new ColorArea(Color.BLUE,drawingArea);
        blue.setBackground(Color.BLUE);

        ColorArea magenta = new ColorArea(Color.MAGENTA,drawingArea);
        magenta.setBackground(Color.MAGENTA);

        ColorArea white = new ColorArea(Color.WHITE,drawingArea);
        white.setBackground(Color.WHITE);

        ColorArea gray = new ColorArea(Color.GRAY,drawingArea);
        gray.setBackground(Color.gray);

        ColorArea black = new ColorArea(Color.BLACK,drawingArea);
        black.setBackground(Color.BLACK);

        //This frameButtons for layout managing. They do not appear in program but they made height higher.
        JButton frameButton1 = new JButton(" ");
        JButton frameButton2 = new JButton(" ");
        JButton frameButton3 = new JButton(" ");
        JButton frameButton4 = new JButton(" ");
        frameButton1.setVisible(false);
        frameButton2.setVisible(false);
        frameButton3.setVisible(false);
        frameButton4.setVisible(false);

        //Adding all the colors
        colors.add(frameButton1);
        colors.add(frameButton2);
        colors.add(red);
        colors.add(orange);
        colors.add(yellow);
        colors.add(green);
        colors.add(blue);
        colors.add(magenta);
        colors.add(white);
        colors.add(gray);
        colors.add(black);
        colors.add(frameButton3);
        colors.add(frameButton4);

        add(colors , BorderLayout.CENTER);
        setVisible(true);

    }

    //Creates color areas with panels. Changes drawing color of drawingArea.
    class ColorArea extends JPanel implements MouseInputListener{

        Color color;
        DrawablePanel drawingArea;

        public ColorArea(Color color, DrawablePanel drawingArea){
            this.color = color;
            this.drawingArea = drawingArea;

            addMouseListener(this);
        }

        //If user presses to ColorArea changes the drawing color of drawingArea
        public void mousePressed(MouseEvent e) {
            drawingArea.drawColor=color;
        }
        public void mouseClicked(MouseEvent e) {}
        public void mouseReleased(MouseEvent e) {}
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
        public void mouseDragged(MouseEvent e) {}
        public void mouseMoved(MouseEvent e) {}
    }
}

class Figure implements Serializable{
    //Parent class of Shape ,Line adn Brush classes.
}

class Shape extends Figure implements Serializable{
    //Contains rectangle and oval shapes attributes

    int startX;
    int startY;
    int endX;
    int endY;
    Color color;
    String shapeType;

    public Shape(int startX, int startY, int endX , int endY, Color color,String shapeType){

        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
        this.color = color;
        this.shapeType = shapeType;
    }
}

class Line extends Figure implements Serializable{
    //Contains line's attributes

    //coordinates[] -> [startX,startY,endX,endY]
    int[] coordinates;
    Color color;

    public Line(int[] parts , Color color){
        this.coordinates = parts;
        this.color=color;
    }

    //Draws the line
    public void paintLine(Graphics g){
        g.setColor(color);
        g.drawLine(coordinates[0],coordinates[1],coordinates[2],coordinates[3]);
    }
}

class Brush extends Figure implements Serializable{
    //Contains Brush's attributes (Actually an oval)

    int startX;
    int startY;
    int radius;
    Color color;

    public Brush(int startX, int startY , int radius,Color color){
        this.startX = startX;
        this.startY = startY;
        this.radius = radius;
        this.color = color;
    }

    //Paints the brush
    public void makeBrush(Graphics g){
        g.setColor(color);
        g.fillOval(startX,startY,radius,radius);
    }
}

public class PaintBrush {
    public static void main(String[] args){
        new Frame();
    }
}
