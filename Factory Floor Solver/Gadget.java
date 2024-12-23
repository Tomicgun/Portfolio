import java.awt.*;

public class Gadget{
    int Type;
    int row;
    int column;
    double[] affinity;
    Shapes.ShapeTypes shape;
    public Color color;
    Gadget partner;

    public Gadget(int Type, int row, int column, double[] affinity, Shapes.ShapeTypes shape, Color color){
        this.Type = Type;
        this.row = row;
        this.column = column;
        this.affinity = affinity;
        this.shape = shape;
        this.color = color;
        this.partner = null;
    }

    public Gadget(int Type, int row, int column, double[] affinity, Shapes.ShapeTypes shape, Color color, Gadget partner){
        this.Type = Type;
        this.row = row;
        this.column = column;
        this.affinity = affinity;
        this.shape = shape;
        this.color = color;
        this.partner = partner;
    }

}
