import javax.swing.*;
import java.awt.*;

public class Gridv2 extends JPanel{
    workerThread floor;
    int space;
    Constants con = new Constants();

    public Gridv2(workerThread fp) {
        super();
        space = (con.maxX - con.pad) / 10;
        this.setBounds(con.maxBoundX, con.maxBoundY, con.maxX, con.maxY);
        this.setOpaque(true);
        this.setSize(1000, 1000);
        this.floor = fp;
    }

    protected void paintComponent(Graphics g){
        //setup and getting the superclass to actually call the method.
        super.paintComponent(g);
        workerThread fp = Main.bestLayout;
        Graphics2D g2d = (Graphics2D)g;
        //for every gadget in the map draw its shape and its place
        fp.gadgets.values().forEach(Gadget->{
            //Get the Types color
            g2d.setColor(Gadget.color);

            //Fill a rectangle with that color
            g2d.fillRect((Gadget.row * space) + con.pad / 2, (Gadget.column * space) + con.pad / 2, space, space);

            //Time to draw the lines
            g2d.setColor(Color.black);

            //draw the x,y placement
            g2d.drawString("(" + Gadget.row + ", " + Gadget.column + ")", (Gadget.row * space + (space / 2)) + con.pad / 2 - 15, (Gadget.column * space + (space / 2)) + con.pad / 2);

            //A bit of logic to draw out what the shape of the square is, meant to be able to better check the correct
            //placement of the shapes on the map.
/*            if (Gadget.shape == Shapes.ShapeTypes.one_by_one_square) {
                g2d.drawString("(1x1)", (Gadget.row * space + (space / 2)) + con.pad / 2 - 15, ((Gadget.column * space + (space / 2)) + con.pad / 2) + 15);
            } else if (Gadget.shape == Shapes.ShapeTypes.line_two_wide) {
                g2d.drawString("(1x2)", (Gadget.row * space + (space / 2)) + con.pad / 2 - 15, ((Gadget.column * space + (space / 2)) + con.pad / 2) + 15);
            } else {
                g2d.drawString("(2x1)", (Gadget.row * space + (space / 2)) + con.pad / 2 - 15, ((Gadget.column * space + (space / 2)) + con.pad / 2) + 15);
            }*/
        });
    }
}
