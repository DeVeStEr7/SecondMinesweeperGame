import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    final static int rowL = 20; //number of rows
    final static int colL = 20; // number of columns
    final static int mineCounter = rowL * colL / 40;

    static int[][] board = new int[rowL][colL]; // size of the board
    static boolean[][] seenBoard = new boolean[rowL][colL];
    private static ArrayList<Location> minesList = new ArrayList<>();
    static Scanner input = new Scanner(System.in);
    static Location startLoc = new Location(-1,0);
    static boolean discoveredBoard = false;
    static boolean gameOver = false;
    static boolean victory = false;
//    static Location currentLoc = new Location(0,0);
//    static Location above = new Location(currentLoc.getRow()-1, currentLoc.getCol());
//    static Location below = new Location(currentLoc.getRow()+1, currentLoc.getCol());
//    static Location left = new Location(currentLoc.getRow(), currentLoc.getCol()-1);
//    static Location right = new Location(currentLoc.getRow(), currentLoc.getCol()+1);
//
//    static Location aboveLeft = new Location(currentLoc.getRow()-1, currentLoc.getCol()-1);
//    static Location aboveRight = new Location(currentLoc.getRow()-1, currentLoc.getCol()+1);
//    static Location belowLeft = new Location(currentLoc.getRow()+1, currentLoc.getCol()-1);
//    static Location belowRight = new Location(currentLoc.getRow()+1, currentLoc.getCol()+1);


    public static void main(String[] args) {
        printBoard();
        System.out.println("Please choose a location. Write as _,_: ");
        String chosenSpot = input.nextLine();
        startLoc.setRow(Integer.parseInt(chosenSpot.substring(0, chosenSpot.indexOf(","))));
        startLoc.setCol(Integer.parseInt(chosenSpot.substring(chosenSpot.indexOf(",") + 1)));
        board[startLoc.getRow()][startLoc.getCol()] = 10;
        seenBoard[startLoc.getRow()][startLoc.getCol()] = true;
        createMines();
        placeMines();
        findMines();
        clearSpace(startLoc);
        System.out.println(minesList.size());
        System.out.println(minesList.size());
        System.out.println(minesList.size());
        while(!gameOver) {
            printBoard();
            System.out.println("Please choose another location. Write as _,_: ");
            chosenSpot = input.nextLine();
            Location currentLoc = new Location(0,0);
            currentLoc.setRow(Integer.parseInt(chosenSpot.substring(0, chosenSpot.indexOf(","))));
            currentLoc.setCol(Integer.parseInt(chosenSpot.substring(chosenSpot.indexOf(",") + 1)));
            while(seenBoard[currentLoc.getRow()][currentLoc.getCol()]) {
                System.out.println("Already seen. Please choose another location. Write as _,_: ");
                chosenSpot = input.nextLine();
                currentLoc.setRow(Integer.parseInt(chosenSpot.substring(0, chosenSpot.indexOf(","))));
                currentLoc.setCol(Integer.parseInt(chosenSpot.substring(chosenSpot.indexOf(",") + 1)));
            }
            //board[currentLoc.getRow()][currentLoc.getCol()] += 10;
            isGameOver(currentLoc);
            if(!gameOver) {
                clearSpace(currentLoc);
            }
        }
        if(victory) {
            System.out.println("Congratulations. You have found all of the mines");
        }
        printBoard();
    }

    //mines
    public static void createMines() {
        for(int i = 0; i < mineCounter; i++){
            Location newMine = new Location(randomNumGen(),randomNumGen());
            //System.out.println("Started " + i + " at position " + newMine.getRow() + "," + newMine.getCol());
            minesList.add(newMine);

            boolean allMinesCleared = false;
            while (!allMinesCleared) {
                allMinesCleared = true;
                for (int j = 0; j < minesList.size(); j++) {
                    Location currentMine = minesList.get(j);
                    Location aboveMine = new Location(currentMine.getRow() - 1, currentMine.getCol());
                    Location belowMine = new Location(currentMine.getRow() + 1, currentMine.getCol());
                    Location leftMine = new Location(currentMine.getRow(), currentMine.getCol() - 1);
                    Location rightMine = new Location(currentMine.getRow(), currentMine.getCol() + 1);

                    Location aboveLeftMine = new Location(currentMine.getRow() - 1, currentMine.getCol() - 1);
                    Location aboveRightMine = new Location(currentMine.getRow() - 1, currentMine.getCol() + 1);
                    Location belowLeftMine = new Location(currentMine.getRow() + 1, currentMine.getCol() - 1);
                    Location belowRightMine = new Location(currentMine.getRow() + 1, currentMine.getCol() + 1);
                    if ((currentMine.getCol() == startLoc.getCol() && currentMine.getRow() == startLoc.getRow()) ||
                            (aboveMine.getCol() == startLoc.getCol() && aboveMine.getRow() == startLoc.getRow()) ||
                            (belowMine.getCol() == startLoc.getCol() && belowMine.getRow() == startLoc.getRow()) ||
                            (leftMine.getCol() == startLoc.getCol() && leftMine.getRow() == startLoc.getRow()) ||
                            (rightMine.getCol() == startLoc.getCol() && rightMine.getRow() == startLoc.getRow()) ||
                            (aboveLeftMine.getCol() == startLoc.getCol() && aboveLeftMine.getRow() == startLoc.getRow()) ||
                            (aboveRightMine.getCol() == startLoc.getCol() && aboveRightMine.getRow() == startLoc.getRow()) ||
                            (belowLeftMine.getCol() == startLoc.getCol() && belowLeftMine.getRow() == startLoc.getRow()) ||
                            (belowRightMine.getCol() == startLoc.getCol() && belowRightMine.getRow() == startLoc.getRow())) {
                        allMinesCleared = false;
                        currentMine.setCol(randomNumGen());
                        currentMine.setRow(randomNumGen());
                        //System.out.println("Moved " + j + " to " + currentMine.getRow() + "," + currentMine.getCol());
                        j+=minesList.size();
                    }
                }
            }
            //System.out.println("Completed " + i + " at position " + newMine.getRow() + "," + newMine.getCol());
        }
    }

    public static void placeMines() {
        for(int i = 0; i < minesList.size(); i++){
            Location currentMine = minesList.get(i);
            board[currentMine.getRow()][currentMine.getCol()] = -1;
        }
    }

    //numbers for around mines
    public static void findMines() {
        for(int i = 0; i < rowL; i++) {
            for(int j = 0; j < colL; j++) {
                Location currentLoc = new Location(i,j);
                if(board[currentLoc.getRow()][currentLoc.getCol()] == -1) {

                }
                else {
                    int surrondingMines = 0;

                    Location above = new Location(currentLoc.getRow() - 1, currentLoc.getCol());
                    Location below = new Location(currentLoc.getRow() + 1, currentLoc.getCol());
                    Location left = new Location(currentLoc.getRow(), currentLoc.getCol() - 1);
                    Location right = new Location(currentLoc.getRow(), currentLoc.getCol() + 1);

                    Location aboveLeft = new Location(currentLoc.getRow() - 1, currentLoc.getCol() - 1);
                    Location aboveRight = new Location(currentLoc.getRow() - 1, currentLoc.getCol() + 1);
                    Location belowLeft = new Location(currentLoc.getRow() + 1, currentLoc.getCol() - 1);
                    Location belowRight = new Location(currentLoc.getRow() + 1, currentLoc.getCol() + 1);

                    if (isLocValid(above)) {
                        if (board[above.getRow()][above.getCol()] == -1) {
                            surrondingMines += 1;
                        }
                    }
                    if (isLocValid(below)) {
                        if (board[below.getRow()][below.getCol()] == -1) {
                            surrondingMines += 1;
                        }
                    }
                    if (isLocValid(left)) {
                        if (board[left.getRow()][left.getCol()] == -1) {
                            surrondingMines += 1;
                        }
                    }
                    if (isLocValid(right)) {
                        if (board[right.getRow()][right.getCol()] == -1) {
                            surrondingMines += 1;
                        }
                    }
                    if (isLocValid(aboveLeft)) {
                        if (board[aboveLeft.getRow()][aboveLeft.getCol()] == -1) {
                            surrondingMines += 1;
                        }
                    }
                    if (isLocValid(aboveRight)) {
                        if (board[aboveRight.getRow()][aboveRight.getCol()] == -1) {
                            surrondingMines += 1;
                        }
                    }
                    if (isLocValid(belowLeft)) {
                        if (board[belowLeft.getRow()][belowLeft.getCol()] == -1) {
                            surrondingMines += 1;
                        }
                    }
                    if (isLocValid(belowRight)) {
                        if (board[belowRight.getRow()][belowRight.getCol()] == -1) {
                            surrondingMines += 1;
                        }
                    }

                    board[currentLoc.getRow()][currentLoc.getCol()] = surrondingMines;
                }
            }
        }

    }
    //empty spaces
    public static void clearSpace(Location chosenLoc) {

        //base case
        if (!isLocValid(chosenLoc)) {
            return;
        }

        seenBoard[chosenLoc.getRow()][chosenLoc.getCol()] = true;
        if(board[chosenLoc.getRow()][chosenLoc.getCol()] != 0)
            return;

        board[chosenLoc.getRow()][chosenLoc.getCol()] += 10;

        Location above = new Location(chosenLoc.getRow() - 1, chosenLoc.getCol());
        clearSpace(above);
        Location below = new Location(chosenLoc.getRow() + 1, chosenLoc.getCol());
        clearSpace(below);
        Location left = new Location(chosenLoc.getRow(), chosenLoc.getCol() - 1);
        clearSpace(left);
        Location right = new Location(chosenLoc.getRow(), chosenLoc.getCol() + 1);
        clearSpace(right);

        Location aboveLeft = new Location(chosenLoc.getRow() - 1, chosenLoc.getCol() - 1);
        clearSpace(aboveLeft);
        Location aboveRight = new Location(chosenLoc.getRow() - 1, chosenLoc.getCol() + 1);
        clearSpace(aboveRight);
        Location belowLeft = new Location(chosenLoc.getRow() + 1, chosenLoc.getCol() - 1);
        clearSpace(belowLeft);
        Location belowRight = new Location(chosenLoc.getRow() + 1, chosenLoc.getCol() + 1);
        clearSpace(belowRight);
    }

    public static int randomNumGen() {
        return (int)(Math.random() * rowL);
    }

    public static void printBoard() {
        System.out.print("#   0  1  2  3  4  5  6  7  8  9 10 11 12 13 14 15 16 17 18 19");
        System.out.println();
        System.out.println();
        for(int i = 0; i < rowL; i++) {
            System.out.print(i + "  ");
            for(int j = 0; j < colL; j++) {
                Location currectLoc = new Location(i, j);
                if(discoveredBoard) {
                    if (board[i][j] == -1) {
                        System.out.print(" X ");
                    } else if (board[i][j] == 10 || ((i == startLoc.getRow()) && (j == startLoc.getCol()))) {
                        System.out.print(" * ");
                    } else if (seenBoard[i][j]) {
                        System.out.print(" " + board[i][j] + " ");
                    } else {
                        System.out.print(" _ ");
                    }
                }
                else {
                    if (board[i][j] == 10  || ((i == startLoc.getRow()) && (j == startLoc.getCol()))) {
                        System.out.print(" * ");
                    } else if(board[i][j] > 10 || seenBoard[i][j]) {
                        if(!seenBoard[i][j]) {
                            board[i][j] -= 10;
                        }
                        seenBoard[i][j] = true;
                        System.out.print(" " + board[i][j] + " ");
                    }
                    else {
                        System.out.print(" _ ");
                    }
                }
            }
            System.out.println();
        }
    }


    private static void isGameOver(Location chosenLoc) {
        if(checkBoardCleared()) {
            if (board[chosenLoc.getRow()][chosenLoc.getCol()] == -1) {
                board[chosenLoc.getRow()][chosenLoc.getCol()] = -1;
                gameOver = true;
                discoveredBoard = true;
            }
            clearSpace(chosenLoc);
            gameOver = true;
            victory = true;
        }
        else {
            for (int i = 0; i < minesList.size(); i++) {
                Location currentMine = minesList.get(i);
                if (chosenLoc.getRow() == currentMine.getRow() && chosenLoc.getCol() == currentMine.getCol()) {
                    board[chosenLoc.getRow()][chosenLoc.getCol()] = -1;
                    gameOver = true;
                    discoveredBoard = true;
                }
            }
        }
    }


    private static boolean isLocValid(Location loc) {
        return loc.getRow() >= 0 && loc.getRow() < board.length && loc.getCol() >= 0 && loc.getCol() < board[0].length;
    }

    private static boolean checkBoardCleared() {
        int numCheck = 0;
        for(int i = 0; i < rowL; i++) {
            for(int j = 0; j < colL; j++) {
                if (seenBoard[i][j]) {
                    numCheck++;
                }
            }
        }
        System.out.println(numCheck);
        return numCheck == rowL * colL - minesList.size();
    }
}
