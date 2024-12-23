public class Render {

    public void render(GameMap g){
        int mode = g.mode;
        switch(mode){
            case 1: render5x7(g); break;
            case 2: render7x7(g); break;
            case -1: render4x4(g); break;
        }
    }


    public static void renderMove(GameMap g,int[] move){
        int mode = g.mode;
        if(mode == 1){
            renderMove5x7(g,move);
        }else if(mode == 2){
            renderMove7x7(g,move);
        }else if(mode == 3){
            renderMoveOctagon(g,move);
        }
    }

    private void render4x4(GameMap g) {
     for(int x = 0; x < g.board.length; x++){
         for(int y = 0; y < g.board[x].length; y++){
             System.out.print(g.board[x][y] + " ");
         }
         System.out.print("\n");
     }
    }
    private void render5x7(GameMap g){
        System.out.println("    _________    ");
        System.out.print("   |");
        for(int i = 0; i<g.board[0].length;i++){
            if(g.board[0][i] != -1 & g.board[0][i] != 0){
                System.out.print("(x)");
            }
            if(g.board[0][i] == 0){
                System.out.print("(o)");
            }
        }
        System.out.print("|\n");

        System.out.print(" __|");
        for(int i = 0; i<g.board[1].length;i++){
            if(g.board[1][i] != -1 & g.board[1][i] != 0){
                System.out.print("(x)");
            }
            if(g.board[1][i] == 0){
                System.out.print("(o)");
            }
        }
        System.out.print("|__\n");

        System.out.print("|");
        for(int i = 2; i<5;i++){
            if(i !=2){
                System.out.print("\n|");
            }
            for(int j = 0; j<g.board[i].length;j++){
                if(g.board[i][j] != -1 & g.board[i][j] != 0){
                    System.out.print("(x)");
                }
                if(g.board[i][j] == 0){
                    System.out.print("(o)");
                }
            }
            System.out.print("|");
        }

        System.out.print("\n --|");
        for(int i = 0; i<g.board[5].length;i++){
            if(g.board[5][i] != -1 & g.board[5][i] != 0){
                System.out.print("(x)");
            }
            if(g.board[5][i] == 0){
                System.out.print("(o)");
            }
        }
        System.out.print("|--\n");

        System.out.print("   |");
        for(int i = 0; i<g.board[6].length;i++){
            if(g.board[6][i] != -1 & g.board[6][i] != 0){
                System.out.print("(x)");
            }
            if(g.board[6][i] == 0){
                System.out.print("(o)");
            }
        }
        System.out.print("|\n");
        System.out.print("    ---------    \n");
    }

    private void render7x7(GameMap g){
        System.out.println("       _________");
        System.out.print("      |");
        for(int i = 0; i<g.board[0].length;i++){
            if(g.board[0][i] != -1 & g.board[0][i] != 0){
                System.out.print("(x)");
            }
            if(g.board[0][i] == 0){
                System.out.print("(o)");
            }
        }
        System.out.print("|\n");

        System.out.print(" _____|");
        for(int i = 0; i<g.board[1].length;i++){
            if(g.board[1][i] != -1 & g.board[1][i] != 0){
                System.out.print("(x)");
            }
            if(g.board[1][i] == 0){
                System.out.print("(o)");
            }
        }
        System.out.print("|_____\n");

        System.out.print("|");
        for(int i = 2; i<5;i++){
            if(i !=2){
                System.out.print("\n|");
            }
            for(int j = 0; j<g.board[i].length;j++){
                if(g.board[i][j] != -1 & g.board[i][j] != 0){
                    System.out.print("(x)");
                }
                if(g.board[i][j] == 0){
                    System.out.print("(o)");
                }
            }
            System.out.print("|");
        }

        System.out.print("\n -----|");
        for(int i = 0; i<g.board[5].length;i++){
            if(g.board[5][i] != -1 & g.board[5][i] != 0){
                System.out.print("(x)");
            }
            if(g.board[5][i] == 0){
                System.out.print("(o)");
            }
        }
        System.out.print("|-----\n");

        System.out.print("      |");
        for(int i = 0; i<g.board[6].length;i++){
            if(g.board[6][i] != -1 & g.board[6][i] != 0){
                System.out.print("(x)");
            }
            if(g.board[6][i] == 0){
                System.out.print("(o)");
            }
        }
        System.out.print("|\n");
        System.out.print("       ---------\n");
    }

    private static void renderMove5x7(GameMap g,int[] move){
        System.out.println("    _________    ");
        System.out.print("   |");
        for(int i = 0; i<g.board[0].length;i++){
            if(move[0] == 0 & move[1] == i){
                System.out.print("(S)");
            }else if(move[4] == 0 & move[5] == i){
                System.out.print("(F)");
            }else if(g.board[0][i] != -1 & g.board[0][i] != 0){
                System.out.print("(x)");
            }else if(g.board[0][i] == 0){
                System.out.print("(o)");
            }
        }
        System.out.print("|\n");

        System.out.print(" __|");
        for(int i = 0; i<g.board[1].length;i++){
            if(move[0] == 1 & move[1] == i){
                System.out.print("(S)");
            }else if(move[0] == 1 & move[5] == i){
                System.out.print("(F)");
            }else if(g.board[1][i] != -1 & g.board[1][i] != 0){
                System.out.print("(x)");
            }else if(g.board[1][i] == 0){
                System.out.print("(o)");
            }
        }
        System.out.print("|__\n");

        System.out.print("|");
        for(int i = 2; i<5;i++){
            if(i !=2){
                System.out.print("\n|");
            }
            for(int j = 0; j<g.board[i].length;j++){
                if(move[0] == i & move[1] == j){
                    System.out.print("(S)");
                }else if(move[4] == i & move[5] == j){
                    System.out.print("(F)");
                }else if(g.board[i][j] != -1 & g.board[i][j] != 0) {
                    System.out.print("(x)");
                }else if(g.board[i][j] == 0){
                    System.out.print("(o)");
                }
            }
            System.out.print("|");
        }

        System.out.print("\n --|");
        for(int i = 0; i<g.board[5].length;i++){
            if(move[0] == 5 & move[1] == i){
                System.out.print("(S)");
            }else if(move[4] == 5 & move[5] == i){
                System.out.print("(F)");
            }else if(g.board[5][i] != -1 & g.board[5][i] != 0){
                System.out.print("(x)");
            }else if(g.board[5][i] == 0){
                System.out.print("(o)");
            }
        }
        System.out.print("|--\n");

        System.out.print("   |");
        for(int i = 0; i<g.board[6].length;i++){
            if(move[0] == 6 & move[1] == i){
                System.out.print("(S)");
            }else if(move[4] == 6 & move[5] == i){
                System.out.print("(F)");
            }else if(g.board[6][i] != -1 & g.board[6][i] != 0){
                System.out.print("(x)");
            }else if(g.board[6][i] == 0){
                System.out.print("(o)");
            }
        }
        System.out.print("|\n");
        System.out.print("    ---------    \n");
    }

    private static void renderMove7x7(GameMap g,int[] move){
        System.out.println("       _________");
        System.out.print("      |");
        for(int i = 0; i<g.board[0].length;i++){
            if(move[0] == 0 & move[1] == i){
                System.out.print("(S)");
            }else if(move[4] == 0 & move[5] == i){
                System.out.print("(F)");
            }else if(g.board[0][i] != -1 & g.board[0][i] != 0){
                System.out.print("(x)");
            }else if(g.board[0][i] == 0){
                System.out.print("(o)");
            }
        }
        System.out.print("|\n");

        System.out.print(" _____|");
        for(int i = 0; i<g.board[1].length;i++){
            if(move[0] == 1 & move[1] == i){
                System.out.print("(S)");
            }else if(move[0] == 1 & move[5] == i){
                System.out.print("(F)");
            }else if(g.board[1][i] != -1 & g.board[1][i] != 0){
                System.out.print("(x)");
            }else if(g.board[1][i] == 0){
                System.out.print("(o)");
            }
        }
        System.out.print("|_____\n");

        System.out.print("|");
        for(int i = 2; i<5;i++){
            if(i !=2){
                System.out.print("\n|");
            }
            for(int j = 0; j<g.board[i].length;j++){
                if(move[0] == i & move[1] == j){
                    System.out.print("(S)");
                }else if(move[4] == i & move[5] == j){
                    System.out.print("(F)");
                }else if(g.board[i][j] != -1 & g.board[i][j] != 0){
                    System.out.print("(x)");
                }else if(g.board[i][j] == 0){
                    System.out.print("(o)");
                }
            }
            System.out.print("|");
        }

        System.out.print("\n -----|");
        for(int i = 0; i<g.board[5].length;i++){
            if(move[0] == 5 & move[1] == i){
                System.out.print("(S)");
            }else if(move[4] == 5 & move[5] == i){
                System.out.print("(F)");
            }else if(g.board[5][i] != -1 & g.board[5][i] != 0){
                System.out.print("(x)");
            }else if(g.board[5][i] == 0){
                System.out.print("(o)");
            }
        }
        System.out.print("|-----\n");

        System.out.print("      |");
        for(int i = 0; i<g.board[6].length;i++){
            if(move[0] == 6 & move[1] == i){
                System.out.print("(S)");
            }else if(move[4] == 6 & move[5] == i){
                System.out.print("(F)");
            }else if(g.board[6][i] != -1 & g.board[6][i] != 0){
                System.out.print("(x)");
            }else if(g.board[6][i] == 0){
                System.out.print("(o)");
            }
        }
        System.out.print("|\n");
        System.out.print("       ---------\n");
    }

    private static void renderMoveOctagon(GameMap g,int[] move){
        System.out.println("       _________");
        System.out.print("    __|");
        for(int i = 0; i<g.board[0].length;i++){
            if(move[0] == 0 & move[1] == i){
                System.out.print("(S)");
            }else if(move[4] == 0 & move[5] == i){
                System.out.print("(F)");
            }else if(g.board[0][i] != -1 & g.board[0][i] != 0){
                System.out.print("(x)");
            }else if(g.board[0][i] == 0){
                System.out.print("(o)");
            }
        }
        System.out.print("|__\n");

        System.out.print(" __|");
        for(int i = 0; i<g.board[1].length;i++){
            if(move[0] == 1 & move[1] == i){
                System.out.print("(S)");
            }else if(move[0] == 1 & move[5] == i){
                System.out.print("(F)");
            }else if(g.board[1][i] != -1 & g.board[1][i] != 0){
                System.out.print("(x)");
            }else if(g.board[1][i] == 0){
                System.out.print("(o)");
            }
        }
        System.out.print("|__\n");

        System.out.print("|");
        for(int i = 2; i<5;i++){
            if(i !=2){
                System.out.print("\n|");
            }
            for(int j = 0; j<g.board[i].length;j++){
                if(move[0] == i & move[1] == j){
                    System.out.print("(S)");
                }else if(move[4] == i & move[5] == j){
                    System.out.print("(F)");
                }else if(g.board[i][j] != -1 & g.board[i][j] != 0){
                    System.out.print("(x)");
                }else if(g.board[i][j] == 0){
                    System.out.print("(o)");
                }
            }
            System.out.print("|");
        }

        System.out.print("\n --|");
        for(int i = 0; i<g.board[5].length;i++){
            if(move[0] == 5 & move[1] == i){
                System.out.print("(S)");
            }else if(move[4] == 5 & move[5] == i){
                System.out.print("(F)");
            }else if(g.board[5][i] != -1 & g.board[5][i] != 0){
                System.out.print("(x)");
            }else if(g.board[5][i] == 0){
                System.out.print("(o)");
            }
        }
        System.out.print("|--\n");

        System.out.print("    --|");
        for(int i = 0; i<g.board[6].length;i++){
            if(move[0] == 6 & move[1] == i){
                System.out.print("(S)");
            }else if(move[4] == 6 & move[5] == i){
                System.out.print("(F)");
            }else if(g.board[6][i] != -1 & g.board[6][i] != 0){
                System.out.print("(x)");
            }else if(g.board[6][i] == 0){
                System.out.print("(o)");
            }
        }
        System.out.print("|--\n");
        System.out.print("       ---------\n");
    }

}
