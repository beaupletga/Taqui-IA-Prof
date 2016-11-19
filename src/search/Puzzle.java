/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;

/**
 *
 * @author gabriel
 */

public class Puzzle implements Searchable<Puzzle, PuzzleAction>, Cloneable {
	
	/** String representation of a puzzle: the string contains 9 digits and represent the 
	 * matrix entered row by row. The digit 0 represents the empty cell.
	 */
        private final int size=3;//size of the array
        
        private final char puzzle_solution[][]= new char[][]{{'0','1','2'},{'3','4','5'},{'6','7','8'}};//make easier to get the manhattan distance
        private final String puzzle_string="0123456789";
        public LinkedList<Puzzle> journey;// list of Puzzle, to remember the journey 
    
	private String puzzle;
        
        private int G;
                
        private int H;
        
        private int F=G+H;
	
	/**
	 * Default constructor, the game is empty
	 */
	Puzzle()
        {
            G=0;
            puzzle = "";
            H=0;
            F=0;
            journey=new LinkedList<Puzzle>();
	}
	
	/**
	 * Builds an instance of the 8 puzzle, the initial state is represented by the string passed in parameter
	 * @param s the string representation of the initial state
	 */
	Puzzle(String s)
        {
            G=0;
            H=0;
            puzzle = "";		
            if (s.length()!=9)
            {
                System.out.println("la chaine n'est pas de la bonne longueur" + s.length());
            }
            puzzle = s;
            journey=new LinkedList<Puzzle>();
            journey.add(this);
	}
	
	/**
	 * returns a String representation of the state
	 *  @return a string representation of the state (3x3 matrix)
	 */
	public String tooString(){
		String result="";
		for (int i=0;i<9;i++){
			if (puzzle.charAt(i)=='0')
				result += ". ";
			else	
				result += puzzle.charAt(i)+" ";
			if (i==2 || i==5 || i==8)
				result += "\n";
		}
		return result;
	}
	
	/**
	 * clone method
	 * @return clone of the 8puzzle
	 */
	@Override public Puzzle clone(){
		return new Puzzle(puzzle);
	}
	
	/**
	 * test the equality between two states
	 * @return true if the two states are equal, false otherwise
	 */
    @Override
    public boolean equals(Object p) {
        if (p instanceof Puzzle){
            Puzzle q = (Puzzle) p;
            return puzzle.equals(q.puzzle);
        }
        else
            return false;
    }
        
        @Override
        public void set_G(int cost)
        {
            G=cost;
        }
	

        @Override
	public int get_G()
        {
            return G;
        }

        @Override
        public int get_H()
        {
            return H;
        }
        
        @Override
        public String get_puzzle()
        {
            return puzzle;
        }
        
        
        
        /**
         * set H, which is the heuristic
         * @param heuristic_choice = 31(manhattan) or 32 for misplaced tiles
         */
        @Override
        public void set_H(int heuristic_choice)
        {
            switch(heuristic_choice)
            {
                case 31:
                {
                    H=get_manhattan();
                    break;
                }
                case 32:
                {
                    int total=0;
                    for (int i=0;i<puzzle.length();i++)
                    {
                        if(puzzle.charAt(i)!=puzzle_string.charAt(i))
                        {
                            total++;
                        }
                    }
                    H=total;
                    break;
                }
            }
	}
        

        @Override
        public void set_F()
        {
            F=G+H;
        }
        
        @Override
        public int get_F()
        {
            return F;
        }
        
        @Override
        public LinkedList<Puzzle> get_journey()
        {
            return journey;
        }
        
        /**
         * Add all the puzzle from the parent's journey then add the parent's puzzle
         * @param new_puzzle 
         */
        
        @Override
        public void add_puzzle_to_journey(Puzzle new_puzzle)
        {
            journey.addAll(0,new_puzzle.journey);
            journey.add(this);
        }
	
	/**
	 * this method returns the possible actions that can be taken from that state
	 * @return a list of action that are available from that state
	 */
        @Override
	public ArrayList<PuzzleAction> getActions(){
		ArrayList<PuzzleAction> l = new ArrayList<>();
		int vide = puzzle.indexOf("0");
		if (vide<3)// ligne du haut
			l.add(PuzzleAction.up);
		if (2<vide && vide<6){ // ligne du milieu
			l.add(PuzzleAction.up); l.add(PuzzleAction.down);
		}	
		if (vide>5)// ligne du bas
			l.add(PuzzleAction.down);
		if (vide%3==0)//colonne de gauche
			l.add(PuzzleAction.left); 
		if ((vide-1)%3==0){ // colonne du milieu
			l.add(PuzzleAction.left); l.add(PuzzleAction.right);
			}
		if ((vide+1)%3==0) // colonne de droite
				l.add(PuzzleAction.right);
		return l;
	}
        
        
	
	/**
	 * return the state that results from taking action <code>a</code> in the current state
	 * @param a action to be taken (we assume it is a valid action)
	 * @return a new puzzle that is the result of taking action <code>a</code> in the current state
	 */
        @Override
	public Puzzle execute(PuzzleAction a){
		Puzzle q = new Puzzle();
		int vide = puzzle.indexOf("0");
		switch (a){
			case up:
				q.puzzle = 	puzzle.substring(0, vide) 
					+ puzzle.substring(vide+3, vide+4) 
					+ puzzle.substring(vide+1, vide+3)
					+ "0"
					+ puzzle.substring(vide+4, 9); 
				break;
			case down:
				q.puzzle = puzzle.substring(0, vide-3) 
					+ "0"			
					+ puzzle.substring(vide-2, vide) 
					+ puzzle.substring(vide-3, vide-2)
					+ puzzle.substring(vide+1, 9);
				break;
			case left:
				q.puzzle = puzzle.substring(0, vide)
				 + puzzle.substring(vide+1, vide+2) 
				 + "0"
				 + puzzle.substring(vide+2, 9);
				break;
			case right:
				q.puzzle = puzzle.substring(0, vide-1)
						 + "0"
						 + puzzle.substring(vide-1, vide)
						 + puzzle.substring(vide+1, 9); 
				break;  
                }
		return q;
	}
	
        
        
        
        /**
         * Tranform the puzzle string in 2D array
         * @param puzzle_string is the puzzle
         * @return 2d array of int
         */
        public int[][] string_to_table(String puzzle_string)
        {
            int [][] puzzle_table=new int[size][size];
            int count=0;
            for (int i=0;i<size;i++)
            {
                for (int j=0;j<size;j++)
                {
                    puzzle_table[i][j]=(puzzle_string.charAt(count));
                    //System.out.println(Character.getNumericValue((puzzle_string.charAt(count))));
                    count++;
                }
            }
            return puzzle_table;
        }
        
        /**
         * 
         * @param nb which is a number between 0 and 8
         * @return a pair(x,y) which are its position on the solution puzzle
         * ex:param=3 -> return (1,0)
         */
        
        public Pair get_position(char nb)
        {
            Pair tmp= new Pair();
            for (int i=0;i<size;i++)
            {
                for (int j=0;j<size;j++)
                {
                    if (puzzle_solution[i][j]==nb)
                    {
                        tmp.x=i;
                        tmp.y=j;
                        return tmp;
                    }
                }
            }
            return tmp;
        }
    
        /**
         * 
         * @return the Manhattan heuristic which is the distance between the current and the solution place
         * of a tile
         */
        public int get_manhattan()
        {
            int total=0;
            int x_string,y_string,x,y;
            char current_char;
            for(int i=0;i<puzzle.length();i++)
            {
                current_char=puzzle.charAt(i);
                x_string=i/size;
                y_string=i%3;
                x=get_position(current_char).x;
                y=get_position(current_char).y;
                total+=Math.abs(x-x_string)+Math.abs(y-y_string);
            }
            return total;
        }



        public class Pair
        {
            public int x;
            public int y;
        }
        
        
        
    /***
     * compare two state
     * @param o the other State to compare
     * @return >0 if the current state is higher,<0 if the current state is lower>, =0 else
     */

    @Override
    public int compareTo(Searchable<Puzzle, PuzzleAction> o) 
    {
        return (G+H-o.get_F());
    }
    
    /**
     *
     * @param open_list is the current open_list
     * @return an int : 1=successor is better than the one in the list,2= the puzzle in the list is better,0=not in the list
     */
    
        @Override
    public int check_list(ArrayList<Puzzle> list)
    {
        for (int i=0;i<list.size();i++)
        {
            if (list.get(i).equals(this))
            {
                if(list.get(i).G>G)
                {
                    list.remove(i);// we delete the one in the list then we add the successor (after the call of this fonction)
                    return 1;
                }
                else
                {
                    return 2;
                }
            }
        }
        return 0;
    }
   
    /**
     * 
     * @param heuristic_choice is 31 for manhattan and 32 for misplaced tiles
     * @return a list of Puzzle which are the successors of the current puzzle
     * by the way, we set H,G,and F
     */
    @Override
    public ArrayList<Puzzle> get_successors(int heuristic_choice)
    {
        ArrayList<Puzzle> successors=new ArrayList<>();
        final List<PuzzleAction> actions=getActions();
        
        int parent_G=get_G()+1;
        
        for(PuzzleAction son : actions)
        {
            Puzzle new_son=execute(son);
            new_son.set_G(parent_G);
            new_son.set_H(heuristic_choice);
            new_son.set_F();
            new_son.add_puzzle_to_journey(this);
            successors.add(new_son);
        }
        return successors;
    }
    
    /**
     * display a puzzle when we need to display the journey for instance
     * @param array 
     */
    public void display_array(int[][] array)
    {
        for(int[] i:array)
        {
            for(int j:i)
            {
                System.out.print(j-48+ " |");
            }
            System.out.println();
        }
    }
    
    
    /**
     * display all the puzzle in the journey
     */
    public void display_journey()
    {
        for(int i=0;i<journey.size();i++)
        {
            display_array(string_to_table(journey.get(i).puzzle));
            System.out.println();
        }
    }
	
}
