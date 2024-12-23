import java.awt.*;
import javax.swing.*;

public class Grid extends JPanel{
    //get the constant values
        Constants con = new Constants();
    //Space is a buffer zone
        int space;
    //Graphics for drawing and Gadget map is for what to draw
        Graphics g;
        Gadget[][] map;

    public Grid(Gadget[][] fp) {
        super();
        space = (con.maxX-con.pad)/10;
        this.setBounds(con.maxBoundX,con.maxBoundY,con.maxX,con.maxY);
        this.setOpaque(true);
        this.setSize(1000,1000);
        this.map = fp;
    }

    /**
     * @description A method used to draw a 2d map by grid spaces
     * @param g the <code>Graphics</code> used to do the drawing
     */
    protected void paintComponent(Graphics g){
        //setup and getting the superclass to actually call the method.
        this.g = g;
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D)g;

        //for every gadget in the map draw its shape and its place
        for (Gadget[] gadgets : map) {
            for (Gadget gadget : gadgets) {
                if (gadget != null) {

                    //Get the Types color
                    g2d.setColor(gadget.color);

                    //Fill a rectangle with that color
                    g2d.fillRect((gadget.row * space) + con.pad / 2, (gadget.column * space) + con.pad / 2, space, space);

                    //Time to draw the lines
                    g2d.setColor(Color.black);

                    //draw the x,y placement
                    g2d.drawString("(" + gadget.row + ", " + gadget.column + ")", (gadget.row * space + (space / 2)) + con.pad / 2 - 15, (gadget.column * space + (space / 2)) + con.pad / 2);

                    //A bit of logic to draw out what the shape of the square is, meant to be able to better check the correct
                    //placement of the shapes on the map.
                    if(gadget.shape == Shapes.ShapeTypes.one_by_one_square){
                        g2d.drawString("(1x1)",(gadget.row * space + (space / 2)) + con.pad / 2 - 15,((gadget.column * space + (space / 2)) + con.pad / 2)+15);
                    } else if (gadget.shape == Shapes.ShapeTypes.line_two_wide) {
                        g2d.drawString("(1x2)",(gadget.row * space + (space / 2)) + con.pad / 2 - 15,((gadget.column * space + (space / 2)) + con.pad / 2)+15);
                    }else{
                        g2d.drawString("(2x1)",(gadget.row * space + (space / 2)) + con.pad / 2 - 15,((gadget.column * space + (space / 2)) + con.pad / 2)+15);
                    }

                }
            }
        }
    }
}
