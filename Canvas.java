import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.awt.event.*;


public class Canvas {
    // Note: The implementation of this class (specifically the handling of
    // shape identity and colors) is slightly more complex than necessary. This
    // is done on purpose to keep the interface and instance fields of the
    // shape objects in this project clean and simple for educational purposes.

    // ----- instance part -----

    private JFrame frame;
    private CanvasPane canvas;
    private Graphics2D graphic;
    private Color backgroundColour;
    private Image canvasImage;
    private List objects;
    private HashMap shapes;
    private Label myLabel;

    /**
     * Create a Canvas.
     * 
     * @param title
     *            title to appear in Canvas Frame
     * @param width
     *            the desired width for the canvas
     * @param height
     *            the desired height for the canvas
     * @param bgClour
     *            the desired background colour of the canvas
     */
    public Canvas(String title, int width, int height, Color bgColour)
    {
        frame = new JFrame();
        canvas = new CanvasPane();
        frame.setContentPane(canvas);
        frame.setTitle(title);
        canvas.setPreferredSize(new Dimension(width, height));
        myLabel = new Label("               ", 2);
        canvas.add(myLabel);
        backgroundColour = bgColour;
        frame.pack();
        objects = new ArrayList();
        shapes = new HashMap();
    }

    /**
     * Create a Canvas. creates canvas with a background of white
     * 
     * @param title
     *            title to appear in Canvas Frame
     * @param width
     *            the desired width for the canvas
     * @param height
     *            the desired height for the canvas
     * 
     * 
     */
    public Canvas(String title, int width, int height)
    {
        frame = new JFrame();
        canvas = new CanvasPane();
        frame.setContentPane(canvas);
        frame.setTitle(title);
        canvas.setPreferredSize(new Dimension(width, height));
        myLabel = new Label("", 1);
        canvas.add(myLabel);
        backgroundColour = new Color(255, 255, 255); // background is white
        frame.pack();
        objects = new ArrayList();
        shapes = new HashMap();
    }

    /**
     * Create a Canvas. creates canvas with a background of white and size of
     * 250 by 250
     * 
     * @param title
     *            title to appear in Canvas Frame
     * 
     * 
     */
    public Canvas(String title)
    {
        frame = new JFrame();
        canvas = new CanvasPane();
        frame.setContentPane(canvas);
        frame.setTitle(title);
        canvas.setPreferredSize(new Dimension(250, 250));
        myLabel = new Label("               ", 2);
        // canvas.add(myLabel);
        backgroundColour = new Color(255, 255, 255); // background is white
        frame.pack();
        objects = new ArrayList();
        shapes = new HashMap();
    }

    /**
     * Create a Canvas. creates canvas with a background of white and size of
     * 250 by 250
     */
    public Canvas()
    {
        frame = new JFrame();
        canvas = new CanvasPane();
        frame.setContentPane(canvas);
        frame.setTitle("CSC121 Canvas");
        canvas.setPreferredSize(new Dimension(250, 250));
        myLabel = new Label("               ", 2);
        // canvas.add(myLabel);
        backgroundColour = new Color(255, 255, 255); // background is white
        frame.pack();
        objects = new ArrayList();
        shapes = new HashMap();
    }

    public void setLabel(String newString)
    {
        myLabel.setText(newString);
    }

    public void setLabelSize(int newSize)
    {
        myLabel.setSize(newSize, myLabel.getHeight());
    }

    /**
     * Set the canvas frame to be the key event object
     */
    public void setListener(EventListener junk)
    {
        frame.addKeyListener((KeyListener) junk);
        canvas.addMouseListener((MouseListener) junk);
        canvas.addMouseMotionListener((MouseMotionListener) junk);
    }

    /**
     * Set the canvas visibility and brings canvas to the front of screen when
     * made visible. This method can also be used to bring an already visible
     * canvas to the front of other windows.
     * 
     * @param visible
     *            boolean value representing the desired visibility of the
     *            canvas (true or false)
     */
    public void setVisible(boolean visible)
    {
        if (graphic == null) {
            // first time: instantiate the offscreen image and fill it with
            // the background colour
            Dimension size = canvas.getSize();
            canvasImage = canvas.createImage(size.width, size.height);
            graphic = (Graphics2D) canvasImage.getGraphics();
            graphic.setColor(backgroundColour);
            graphic.fillRect(0, 0, size.width, size.height);
            graphic.setColor(Color.black);
        }
        frame.setVisible(visible);
    }

    /**
     * Draw a given shape onto the canvas.
     * 
     * @param referenceObject
     *            an object to define identity for this shape
     * @param color
     *            the color of the shape
     * @param shape
     *            the shape object to be drawn on the canvas
     */
    // Note: this is a slightly backwards way of maintaining the shape
    // objects. It is carefully designed to keep the visible shape interfaces
    // in this project clean and simple for educational purposes.
    public void draw(Object referenceObject, String color, Shape shape)
    {
        objects.remove(referenceObject); // just in case it was already there
        objects.add(referenceObject); // add at the end
        shapes.put(referenceObject, new ShapeDescription(shape, color));
        redraw();
    }

    /**
     * Erase a given shape's from the screen.
     * 
     * @param referenceObject
     *            the shape object to be erased
     */
    public void erase(Object referenceObject)
    {
        objects.remove(referenceObject); // just in case it was already there
        shapes.remove(referenceObject);
        redraw();
    }

    /**
     * eraseAll - clears all of the elements from the screen
     */
    public void eraseAll()
    {
        objects.clear();
        shapes.clear();
        redraw();
    }

    /**
     * Set the foreground colour of the Canvas.
     * 
     * @param newColour
     *            the new colour for the foreground of the Canvas
     */
    public void setForegroundColor(String colorString)
    {
        if (colorString.equals("red"))
            graphic.setColor(Color.red);
        else if (colorString.equals("black"))
            graphic.setColor(Color.black);
        else if (colorString.equals("blue"))
            graphic.setColor(Color.blue);
        else if (colorString.equals("yellow"))
            graphic.setColor(Color.yellow);
        else if (colorString.equals("green"))
            graphic.setColor(Color.green);
        else if (colorString.equals("magenta"))
            graphic.setColor(Color.magenta);
        else if (colorString.equals("white"))
            graphic.setColor(Color.white);
        else
            graphic.setColor(Color.black);
    }

    /**
     * Wait for a specified number of milliseconds before finishing. This
     * provides an easy way to specify a small delay which can be used when
     * producing animations.
     * 
     * @param milliseconds
     *            the number
     */
    public void wait(int milliseconds)
    {
        try {
            Thread.sleep(milliseconds);
        }
        catch (Exception e) {
            // ignoring exception at the moment
        }
    }

    /**
     * Returns the width of the canvas in pixels
     */
    public int getWidth()
    {
        return canvas.getWidth();
    }

    /**
     * Returns the height of the canvas in pixels
     */
    public int getHeight()
    {
        return canvas.getHeight();
    }

    /**
     * Redraw ell shapes currently on the Canvas.
     */
    private void redraw()
    {
        erase();
        for (Iterator i = objects.iterator(); i.hasNext();) {
            ((ShapeDescription) shapes.get(i.next())).draw(graphic);
        }
        canvas.repaint();
    }

    /**
     * Erase the whole canvas. (Does not repaint.)
     */
    private void erase()
    {
        Color original = graphic.getColor();
        graphic.setColor(backgroundColour);
        Dimension size = canvas.getSize();
        graphic.fill(new Rectangle(0, 0, size.width, size.height));
        graphic.setColor(original);
    }

    /***************************************************************************
     * Inner class CanvasPane - the actual canvas component contained in the
     * Canvas frame. This is essentially a JPanel with added capability to
     * refresh the image drawn on it.
     */
    private class CanvasPane extends JPanel {
        public void paint(Graphics g)
        {
            g.drawImage(canvasImage, 0, 0, null);
        }
    }

    /***************************************************************************
     * Inner class CanvasPane - the actual canvas component contained in the
     * Canvas frame. This is essentially a JPanel with added capability to
     * refresh the image drawn on it.
     */

    /***************************************************************************
     * Inner class ShapeDescription - something??
     */
    private class ShapeDescription {
        private Shape shape;
        private String colorString;

        public ShapeDescription(Shape shape, String color)
        {
            this.shape = shape;
            colorString = color;
        }

        public void draw(Graphics2D graphic)
        {
            setForegroundColor(colorString);
            graphic.fill(shape);
        }
    }

}
