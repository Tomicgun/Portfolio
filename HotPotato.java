package cse214hw1;
import java.util.LinkedList;
public class HotPotato {
    public static DoublyLinkedList<Integer> playWithDoublyLinkedList(int numberOfPlayers, int lengthOfPass) {
        DoublyLinkedList<Integer> players = new DoublyLinkedList<>();
        DoublyLinkedList<Integer> elimPlayers = new DoublyLinkedList<>();
        int flag = 0;
        for(int i = 0; i < numberOfPlayers; i++){
            players.add(i+1);
        }
        int length = players.getLength();
        while(elimPlayers.getLength() != length){
            if(lengthOfPass == 0){
                return players;
            }
            else {
                for(Integer player: players){
                    if(flag == lengthOfPass){
                        elimPlayers.add(player);
                        players.remove(player);
                        flag = 0;
                    } else {
                        flag++;
                    }
                }
            }
        }
        return elimPlayers;
    }



    public static LinkedList<Integer> playWithLinkedList(int numberOfPlayers, int lengthOfPass) {
        LinkedList<Integer> players = new LinkedList<>();
        LinkedList<Integer> elimPlayers = new LinkedList<>();
        int flag = 0;
        for(int i = 0; i < numberOfPlayers; i++){
            players.add(i+1);
        }
        while(elimPlayers.size() != players.size()){
            if(lengthOfPass == 0){
                return players;
            } else {
                for(Integer player: players) {
                    if(player != 0) {
                        if (flag == lengthOfPass) {
                            elimPlayers.add(player);
                            int index = players.indexOf(player);
                            players.set(index, 0);
                            flag = 0;
                        } else {
                            flag++;
                        }
                    }
                }
            }
        }
        return elimPlayers;
    }
    public static void main(String... args) {
// in both methods, the list is the order in which the players are eliminated
// the last player (i.e., the last element in the returned list) is the winner
        System.out.println(playWithDoublyLinkedList(5, 0));// expected output: [1, 2, 3, 4, 5]
        System.out.println("-------------");
        System.out.println(playWithDoublyLinkedList(4, 2));// [2,6,4,2,5,1]
        System.out.println("-------------");
        System.out.println(playWithDoublyLinkedList(3, 2)); // [3,1,2]
        System.out.println("-------------");
        System.out.println(playWithLinkedList(5, 1)); // expected output: [2, 4, 1, 5, 3]
        System.out.println("-------------");
        System.out.println(playWithLinkedList(3, 0)); // [1,2,3]
        System.out.println("-------------");
        System.out.println(playWithLinkedList(6, 2)); // [3,6,4,2,5,1]



    }
}
