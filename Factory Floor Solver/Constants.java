import java.awt.*;

public class Constants {
    public final int numStations = 48;
    public final double numSpaces = 100;
    public final int numTypes = 4;
    public final int X = 10;
    public final int Y = 10;
    public final int pad = 100;
    public final int maxX = 800;
    public final int maxY = 800;
    public final int maxBoundX = 600;
    public final int maxBoundY = 600;
    public final int nGen = 1000;
    public final int interval = 20;
    //public final int maxThreads = 2;
    //public final int nExchangers = maxThreads%2;
    //public final int interval = 10;
    public final Color type1 = Color.BLUE;
    public final Color type2 = Color.RED;
    public final Color type3 = Color.GREEN;
    public final Color type4 = Color.ORANGE;
    Shapes shapes = new Shapes();
    public double[] affinity;

    public Color getColor(int type){
        return switch (type) {
            case 0 -> type1;
            case 1 -> type2;
            case 2 -> type3;
            case 3 -> type4;
            default -> Color.BLACK;
        };
    }

}


