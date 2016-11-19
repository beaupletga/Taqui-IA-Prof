/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import java.util.Arrays;
import java.util.Scanner;

/**
 *
 * @author gabriel
 */
public class Search 
{
    public static void main(String[] args) throws Exception 
    {
        int choice=8;
        do
        {
            System.out.println("Choisir le mode :");
            System.out.println();
            System.out.println("1 : Tester BFS sur tout le fichier");
            System.out.println("2 : Tester DFS sur tout le fichier");
            System.out.println("3 : Tester Astar sur tout le fichier");
            System.out.println("4 : Tester les 3 sur tout le fichier");

            System.out.println();
            System.out.println("5 : Tester BFS pour un état intial et afficher le chemin");
            System.out.println("6 : Tester DFS pour un état intial et afficher le chemin");
            System.out.println("7 : Tester Astar pour un état intial et afficher le chemin (Manhattan par défault)");
            System.out.println();
            System.out.println("8 : Quitter"); 

            Scanner sc = new Scanner(System.in);
            try
            {
                choice = Integer.parseInt(sc.nextLine());
            }
            catch (Exception e)
            {
                System.out.println("Erreur lors de la saisie");
                choice=9;
            }
            switch(choice)
            {
                case 1:
                {
                    Read_File BFS_file=new Read_File(1);//BFS on the whole file
                    break;
                }
                case 2:
                {
                    Read_File DFS_file=new Read_File(2);//DFS on the whole file
                    break;
                }
                case 3:
                {
                    System.out.println();
                    System.out.println("Choisir l'heuristique :");
                    System.out.println("1 : Manhattan");
                    System.out.println("2 : Nb de cases mal placées");
                    Scanner scc = new Scanner(System.in);
                    int heuristic_choice = Integer.parseInt(scc.nextLine());
                    
                    switch(heuristic_choice)
                    {
                        case 1:
                        {
                            Read_File Astar_file=new Read_File(31);//Astar on the whole file with the manhattan heuristic
                            break;
                        }
                        case 2:
                        {
                            Read_File Astar_file=new Read_File(32);//Astar on the whole file with the misplaced tiles heuristic
                            break;
                        }
                            
                    }
                    break;
                }
                case 4:
                {
                    Read_File ALL_file=new Read_File();//Astar on the whole file with the misplaced tiles heuristic
                    break;
                }
                case 5:
                {
                    System.out.println("Entrez état initial");
                    Scanner initial_state_scanner = new Scanner(System.in);
                    String initial_state=initial_state_scanner.nextLine();
                    System.out.println();
                    if(is_valid(initial_state))//if the input puzzle is correct
                    {
                        Problem<Puzzle, PuzzleAction> eightpuzzle 
                    = new Problem<>(new Puzzle(initial_state), new Puzzle("012345678"));
                         Puzzle tmp=eightpuzzle.bfs();//solve with BFS
                         tmp.display_journey();//display the journey
                         System.out.println(tmp.get_G() + " coups");
                    }
                    
                    break;
                }
                case 6:
                {
                    System.out.println("Entrez état initial");
                    Scanner initial_state_scanner = new Scanner(System.in);
                    String initial_state=initial_state_scanner.nextLine();
                    System.out.println();
                    if(is_valid(initial_state))//if the input puzzle is correct
                    {
                        Problem<Puzzle, PuzzleAction> eightpuzzle 
                    = new Problem<>(new Puzzle(initial_state), new Puzzle("012345678"));
                        Puzzle tmp=eightpuzzle.dfs();//solve with DFS
                        tmp.display_journey();//display the journey
                        System.out.println(tmp.get_G() + " coups");
                    }
                    break;
                }
                case 7:
                {
                    System.out.println("Entrez état initial");
                    Scanner initial_state_scanner = new Scanner(System.in);
                    String initial_state=initial_state_scanner.nextLine();
                    System.out.println();
                    if(is_valid(initial_state))//if the input puzzle is correct
                    {
                        Problem<Puzzle, PuzzleAction> eightpuzzle 
                    = new Problem<>(new Puzzle(initial_state), new Puzzle("012345678"));
                        Puzzle tmp=eightpuzzle.aStar(31);//solve with astar (heuristic is manhattan by default)
                        tmp.display_journey();//display the journey
                        System.out.println(tmp.get_G() + " coups");
                    }
                    break;
                }
            }
            for(int i=0;i<5;i++)
            {
                System.out.println();
            }
        }while(choice!=8);
    }
    
    /**
     * check if a state's puzzle has a correct form
     * @param state
     * @return true if the state's puzzle has a correct form else false
     */
    public static boolean is_valid(String state)
    {
        char all_numbers[]=new char[]{'0','1','2','3','4','5','6','7','8'};
        
        if(state.length()==9)
        {
            char tmp[]=state.toCharArray();
            Arrays.sort(tmp);
            return Arrays.equals(all_numbers, tmp);
        }
        else
        {
            return false;
        }
    }
}
