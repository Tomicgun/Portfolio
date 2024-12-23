import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class workerThread extends Thread implements Runnable {
    protected double[] affinity;
    final static Constants constants = new Constants();
    final private static Exchanger<HashMap<Integer,Gadget>> exchanger = new Exchanger<>();
    Gadget[][] localMap;
    public volatile HashMap<Integer,Gadget> gadgets = new HashMap<>();
    double score;
    private long tId;
    Lock lock = new ReentrantLock();

    public workerThread(double[] affinity) {
        this.localMap = initializeThread(affinity);
        this.gadgets = getGadgets(this.localMap);
        this.affinity = affinity;
        this.score = score(gadgets);
        tId = Thread.currentThread().threadId();
    }

    @Override
    public void run() {
        lock.lock();
        try{
            this.merge(this.gadgets);
            mutate(this.gadgets,this.localMap);
            placeLayout();
        }finally {
            lock.unlock();
        }
    }

    private void placeLayout() {
        Main.layoutPools.put(this.tId,this);
        for(Long Tid: Main.layoutPools.keySet()) {
            if(Main.bestLayout == null){
                Main.bestLayout = Main.layoutPools.get(Tid);
            } else if(Main.bestLayout.score <= this.score){
                Main.bestLayout = this;
            }
        }
    }

    /**
     * @description This method randomly selects a cell and Gadget and moves the Gadget.
     */
    private void mutate(HashMap<Integer,Gadget> gadgets, Gadget[][] map){
        Constants c = new Constants();
        int row;
        int column;
        int newRow;
        int newColumn;
        int direction;
        Gadget g;

        //finding a gadget to move
        do{
            g =  gadgets.get(ThreadLocalRandom.current().nextInt(gadgets.size()));
            row = g.row;
            column = g.column;
            newRow = ThreadLocalRandom.current().nextInt(map.length);
            newColumn = ThreadLocalRandom.current().nextInt(map[0].length);
            direction = moveCheck(g,newRow,newColumn,map);
        }while(direction == 0);

        //actually place the gadget
        if(g.shape == Shapes.ShapeTypes.one_by_one_square){
            //placing g
            map[newRow][newColumn] = g;

            //bookkeeping on g
            g.row = newRow;
            g.column = newColumn;

            //clean up old spot
            map[row][column] = null;

        } else if (g.shape == Shapes.ShapeTypes.line_two_wide) {
            //place g
            map[newRow][newColumn] = g;

            //bookkeeping on g
            g.row = newRow;
            g.column = newColumn;

            //clean up old spot
            map[row][column] = null;

            //placing g partner
            map[newRow][newColumn+(direction)] = g.partner;

            //clean up old spot
            map[g.partner.row][g.partner.column] = null;

            //bookkeeping on g partner
            g.partner.row = newRow;
            g.partner.column = newColumn+(direction);

        }else{
            //place g
            map[newRow][newColumn] = g;

            //bookkeeping on g
            g.row = newRow;
            g.column = newColumn;

            //clean up old spot
            map[row][column] = null;

            //placing g partner
            map[newRow+(direction)][newColumn] = g.partner;

            //clean up old spot
            map[g.partner.row][g.partner.column] = null;

            //bookkeeping on g partner
            g.partner.row = newRow+(direction);
            g.partner.column = newColumn;
        }
    }


    /**
     * @description This method is just checking if you can move said Gadget to a new spot
     * @param g Gadget to be moved
     * @param row g.row
     * @param column g.column
     * @return an int where 1 == positive, -1 == negative and 0 == can't move the Gadget there.
     */
    private static int moveCheck(Gadget g, int row, int column, Gadget[][] map){
        if(g.shape != Shapes.ShapeTypes.one_by_one_square){
            try{
                Gadget g2 = g.partner;
                //check to move g
                if(map[row][column] == null){
                    //check g shape
                    if(g2.shape == Shapes.ShapeTypes.line_two_wide){
                        //check if we can place g2
                        if(map[row][column+1] == null){
                            return 1;
                        }else if (map[row][column-1] == null){
                            return -1;
                        }
                    }else if (g2.shape == Shapes.ShapeTypes.line_two_tall){
                        if(map[row+1][column] == null){
                            return 1;
                        }else if (map[row-1][column] == null){
                            return -1;
                        }
                    }
                    return 0;
                }
            } catch (ArrayIndexOutOfBoundsException e){
                return 0;
            }
        }else{
            //check to move a one by one shape
            if(map[row][column] == null){
                return 1;
            }else{
                return 0;
            }
        }
        //all else just fail
        return 0;
    }

    /**
     * @description This method uses a simple distance multiplied by an affinity value to calculate a score
     * where higher scores are fitter while lower scores are less fit.
     * @return a double value of score
     */
    public static double score(HashMap<Integer,Gadget> gadgets){
        double score = 0.0;
        //take every gadget and score it against every other gadget
        ArrayList<Gadget> allGadgets = new ArrayList<>(gadgets.values());
        for (Gadget g : allGadgets) {
            for(Gadget gadget: allGadgets){
                if (!g.equals(gadget)) {
                    double distance = Math.sqrt(Math.pow(gadget.row - g.row, 2) + Math.pow(gadget.column - g.column, 2));
                    score += distance * g.affinity[gadget.Type];
                    //System.out.println("Gadget 1 Type is " + g.Type + " Gadget 2 Type is " + gadget.Type);
                    //System.out.println("The Distance is " + distance);
                    //System.out.println("The Affinity is " + g.affinity[gadget.Type]);
                    //System.out.println("The local score is " + distance * g.affinity[gadget.Type]);
                    //System.out.println("The total Score is " + score);
                }
                //System.out.println("\n");
            }
            //System.out.println("\n");
            //System.out.println("--------------------------------------");
            //System.out.println("\n");
        }
        return score;
    }

    /**
     * @description This method is a wrapper to merge the two best maps and score them
     */
    void merge(HashMap<Integer,Gadget> thisGadgets) {
        //merge
        HashMap<Integer,Gadget> otherGadgets = null;
        try{
            otherGadgets = exchanger.exchange(thisGadgets);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        localMap = mergeV2(thisGadgets,otherGadgets,affinity);
        gadgets = getGadgets(localMap);
        this.score = score(gadgets);
    }

    private Gadget[][] mergeV2(HashMap<Integer,Gadget> thisGadgets, HashMap<Integer,Gadget> otherGadgets,double[] affinity){
        Constants c = new Constants();
        c.affinity = affinity;

        if(otherGadgets == null){
            return new Gadget[c.X][c.Y];
        }
        ArrayList<Gadget> allGadgets = new ArrayList<>(thisGadgets.values());
        allGadgets.addAll(otherGadgets.values());
        Gadget[][] map = new Gadget[c.X][c.Y];

        //sort them into types
        ArrayList<Gadget> type0Gadgets = new ArrayList<>();
        ArrayList<Gadget> type1Gadgets = new ArrayList<>();
        ArrayList<Gadget> type2Gadgets = new ArrayList<>();
        ArrayList<Gadget> type3Gadgets = new ArrayList<>();

        for (Gadget gadget : allGadgets) {
            switch (gadget.Type) {
                case 0:
                    type0Gadgets.add(gadget);
                    break;
                case 1:
                    type1Gadgets.add(gadget);
                    break;
                case 2:
                    type2Gadgets.add(gadget);
                    break;
                case 3:
                    type3Gadgets.add(gadget);
                    break;
                default:
                    System.out.println("Invalid gadget type: " + gadget.Type);
            }
        }

        //get there proportions
        double type0Proportion = type0Gadgets.size() / (double) allGadgets.size();
        double type1Proportion = type1Gadgets.size() / (double) allGadgets.size();
        double type2Proportion = type2Gadgets.size() / (double) allGadgets.size();
        double type3Proportion = type3Gadgets.size() / (double) allGadgets.size();

        //get the number of each type to select
        int[] numType = new int[]{(int) (thisGadgets.size() * type0Proportion),
                (int) (c.numStations * type1Proportion),
                (int) (c.numStations * type2Proportion),
                (int) (c.numStations * type3Proportion)};
        int selected = Arrays.stream(numType).sum();
        if (selected < thisGadgets.size()) {
            int min = Integer.MAX_VALUE;
            for (int j : numType) {
                if (j < min) {
                    min = j;
                }
            }
            for (int i = 0; i < numType.length; i++) {
                if (numType[i] == min) {
                    numType[i] += thisGadgets.size() - selected;
                    break;
                }
            }
        }

        int num = 0;
        int flag = 0;
        int[] countType = new int[]{0,0,0,0};
        while(Arrays.stream(numType).sum() > Arrays.stream(countType).sum()) {
            for (int j = 0;j<c.numTypes;j++) {
                Gadget gadget = null;
                if(j == 0 & countType[j] != numType[0]){
                    do{
                        if(type0Gadgets.isEmpty()){
                            break;
                        }
                        //get random gadget
                        gadget = type0Gadgets.get(ThreadLocalRandom.current().nextInt(type0Gadgets.size()));

                        //remove from list to avoid repeats
                        type0Gadgets.remove(gadget);
                    }while(!this.place(gadget,map));
                    if(gadget != null){
                        countType[j]++;
                        gadgets.put(num,gadget);
                        num++;
                    }

                } else if (j == 1 & countType[j] != numType[1]) {
                    do{
                        if(type1Gadgets.isEmpty()){
                            break;
                        }

                        //get random gadget
                        gadget = type1Gadgets.get(ThreadLocalRandom.current().nextInt(type1Gadgets.size()));

                        //remove from list to avoid repeats
                        type1Gadgets.remove(gadget);
                    }while(!this.place(gadget,map));
                    if (gadget != null) {
                        countType[j]++;
                        gadgets.put(num,gadget);
                        num++;
                    }

                } else if (j == 2 & countType[j] != numType[2]) {
                    do{

                        if(type2Gadgets.isEmpty()){
                            break;
                        }

                        //get random gadget
                        gadget = type2Gadgets.get(ThreadLocalRandom.current().nextInt(type2Gadgets.size()));

                        //remove from list to avoid repeats
                        type2Gadgets.remove(gadget);
                    }while(!this.place(gadget,map));
                    if (gadget != null) {
                        countType[j]++;
                        gadgets.put(num,gadget);
                        num++;
                    }
                } else if(j == 3 & countType[j] != numType[3]){
                    do{

                        if(type3Gadgets.isEmpty()){
                            break;
                        }

                        //get random gadget
                        gadget = type3Gadgets.get(ThreadLocalRandom.current().nextInt(type3Gadgets.size()));

                        //remove from list to avoid repeats
                        type3Gadgets.remove(gadget);
                    }while(!this.place(gadget,map));
                    if (gadget != null) {
                        countType[j]++;
                        gadgets.put(num,gadget);
                        num++;
                    }
                }
            }
            flag++;
            if(flag > 60){
                break;
            }
        }

        //Double checks
        if (c.numStations != Arrays.stream(countType).sum()) {
            int typesNeeded0 = 0, typesNeeded1 = 0, typesNeeded2 = 0, typesNeeded3 = 0;
            if (numType[0] != countType[0]) {
                typesNeeded0 += Math.abs(numType[0] - countType[0]);
            }
            if (numType[1] != countType[1]) {
                typesNeeded1 += Math.abs(numType[1] - countType[1]);
            }
            if (numType[2] != countType[2]) {
                typesNeeded2 += Math.abs(numType[2] - countType[2]);
            }
            if (numType[3] != countType[3]) {
                typesNeeded3 += Math.abs(numType[3] - countType[3]);
            }
            int[] leftovers = new int[]{typesNeeded0, typesNeeded1, typesNeeded2, typesNeeded3};
            for (int i = 0; i < leftovers.length; i++) {
                for (int j = 0; j < leftovers[i]; j++) {
                    Gadget g = randomlyPlace(i,affinity, map);
                    gadgets.put(num, g);
                    num++;
                }
            }
        }
        return map;
    }

    private boolean place(Gadget gadget, Gadget[][] map){
        if(map[gadget.row][gadget.column] == null){
            if(gadget.partner != null){
                if(map[gadget.partner.row][gadget.partner.column] == null){
                    map[gadget.row][gadget.column] = gadget;
                    map[gadget.partner.row][gadget.partner.column] = gadget.partner;
                    return true;
                }else{
                    return false;
                }
            }else{
                map[gadget.row][gadget.column] = gadget;
                return true;
            }
        }else{
            return false;
        }
    }

    private Gadget randomlyPlace(int type, double[] affinity ,Gadget[][] map){
        Constants con = new Constants();
        int row,column;
        //set shape & displacement vector
        Shapes.ShapeTypes st = con.shapes.getShape();

        int[] disp = con.shapes.getShapeDisplacementVector(st);
        double[] af = affinity.clone();
        af[type] = 1.0;

        do{
            row = ThreadLocalRandom.current().nextInt(con.X);
            column = ThreadLocalRandom.current().nextInt(con.Y);
        }while (map[row][column] != null);

        //place origin point
        if(st != Shapes.ShapeTypes.one_by_one_square){
            map[row][column] = new Gadget(type, row, column, af, st,con.getColor(type));
            try{
                //try placing it above/bellow or left/right of initial point
                if(map[row+disp[0]][column+disp[1]] == null){
                    //set the partners and place the shape
                    map[row+disp[0]][column+disp[1]] = new Gadget(type, row+disp[0],column+disp[1], af, st, con.getColor(type));

                    map[row][column].partner = map[row+disp[0]][column+disp[1]];
                    map[row+disp[0]][column+disp[1]].partner = map[row][column];
                } else if (map[row-disp[0]][column-disp[1]] == null) {
                    //set the partners and place the shape
                    map[row-disp[0]][column-disp[1]] = new Gadget(type, row-disp[0],column-disp[1], af, st,con.getColor(type),map[row][column]);

                    map[row][column].partner = map[row-disp[0]][column-disp[1]];
                    map[row-disp[0]][column-disp[1]].partner = map[row][column];
                    //if it fails, make it a 1x1
                }else{
                    map[row][column].shape = Shapes.ShapeTypes.one_by_one_square;
                }
                //if you get an array out of bound, make it a 1x1
            }catch(ArrayIndexOutOfBoundsException e){
                map[row][column].shape = Shapes.ShapeTypes.one_by_one_square;

            }
            //if it is a 1x1, place it down
        }else{
            map[row][column] = new Gadget(type, row, column, affinity, st,con.getColor(type));
        }
        return map[row][column];
    }

    private HashMap<Integer,Gadget> getGadgets(Gadget[][] map) {
        HashMap<Integer,Gadget> gadgets = new HashMap<>(100);
        int num = 0;
        for(Gadget[] g1: map){
            for(Gadget g2: g1){
                if(g2 == null) continue;
                if(g2.partner == null & !gadgets.containsValue(g2)){
                    gadgets.put(num,g2);
                    num++;
                }else if(!gadgets.containsValue(g2) & !gadgets.containsValue(g2.partner)){
                    gadgets.put(num,g2);
                    num++;
                }
            }
        }
        return gadgets;
    }

    private Gadget[][] initializeThread(double[] affinity) {
        Constants con = new Constants();
        Gadget[][] Map;
        int row;
        int column;

        Map = new Gadget[con.X][con.Y];

        //for every number of stations
        int num = 0;
        for(int i = 0; i < con.numStations; i++) {


            double[] affinity_local = affinity.clone();

            //set type
            int type = ThreadLocalRandom.current().nextInt(con.numTypes);

            //set affinity
            affinity_local[type] = 1.00;

            //set shape & displacement vector
            Shapes.ShapeTypes st = con.shapes.getShape();
            int[] disp = con.shapes.getShapeDisplacementVector(st);

            do{
                row = ThreadLocalRandom.current().nextInt(con.X);
                column = ThreadLocalRandom.current().nextInt(con.Y);
            }while (Map[row][column] != null);

            //place origin point
            if(st != Shapes.ShapeTypes.one_by_one_square){
                Map[row][column] = new Gadget(type, row, column, affinity_local, st,con.getColor(type));
                //gadgets.put(num,Map[row][column]);
                //num++;
                try{
                    //try placing it above/bellow or left/right of initial point
                    if(Map[row+disp[0]][column+disp[1]] == null){
                        //set the partners and place the shape
                        Map[row+disp[0]][column+disp[1]] = new Gadget(type, row+disp[0],column+disp[1], affinity_local, st, con.getColor(type));

                        Map[row][column].partner = Map[row+disp[0]][column+disp[1]];
                        Map[row+disp[0]][column+disp[1]].partner = Map[row][column];
                    } else if (Map[row-disp[0]][column-disp[1]] == null) {
                        //set the partners and place the shape
                        Map[row-disp[0]][column-disp[1]] = new Gadget(type, row-disp[0],column-disp[1], affinity_local, st,con.getColor(type),Map[row][column]);

                        Map[row][column].partner = Map[row-disp[0]][column-disp[1]];
                        Map[row-disp[0]][column-disp[1]].partner = Map[row][column];
                        //if it fails, make it a 1x1
                    }else{
                        Map[row][column].shape = Shapes.ShapeTypes.one_by_one_square;
                    }
                    //if you get an array out of bound, make it a 1x1
                }catch(ArrayIndexOutOfBoundsException e){
                    Map[row][column].shape = Shapes.ShapeTypes.one_by_one_square;

                }
                //if it is a 1x1, place it down
            }else{
                Map[row][column] = new Gadget(type, row, column, affinity_local, st,con.getColor(type));
                //gadgets.put(num,Map[row][column]);
                //num++;
            }
        }
        return Map;
    }
}