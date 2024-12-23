import java.util.concurrent.ThreadLocalRandom;

public class Shapes {

    public enum ShapeTypes{
        line_two_tall,
        line_two_wide,
        one_by_one_square
    }

    /**
     * @description randomly returns a ShapeTypes object from the above enum
     * @return random shape r
     */
    public ShapeTypes getShape() {
        int num = ThreadLocalRandom.current().nextInt(3);
        return switch (num) {
            case 0 -> ShapeTypes.line_two_tall;
            case 1 -> ShapeTypes.one_by_one_square;
            case 2 -> ShapeTypes.line_two_wide;
            default -> null;
        };
    }

    /**
     * @description
     * shape @param 'shape enum type'
     * @return int[]
     */
    public int[] getShapeDisplacementVector(ShapeTypes shape){
        //Gets the displacement of the origin point by xy differance
        return switch (shape) {
            case line_two_wide -> new int[]{0, 1};
            case one_by_one_square -> new int[]{};
            case line_two_tall -> new int[]{1, 0};
        };
    }
}
