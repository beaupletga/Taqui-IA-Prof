/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author gabriel
 */
public interface Searchable<State, Action> extends Comparable<Searchable<State,Action>>{

	/**
	 * returns all possible actions from the current state
	 * @return a list of all possible actions from the current state
	 */
	public List<PuzzleAction> getActions();
	
	/**
	 * the method return the state resulting from taking action <code>a</code> in the current state
	 * @param a is the action taken
	 * @return the state resulting from taking action a in the current state
	 */
	public State execute(PuzzleAction a);
	
	
	/**
	 * Set the current value of the cost from the initial state to the current state
	 * @param cost the currently known cost from the initial state to the current state
	 */
	public void set_G(int cost);
        
        /**
         * Set F which is the sum of H and G (heuristic and cost)
         */
        public void set_F();
	
	/**
	 * Get the current value of the cost from the initial state to the current state
	 * @return the currently known cost from the initial state to the current state
	 */
	public int get_G();
	
	/**
	 * compute the value of the heuristic. The value is stored and can be accessed through a getter
	 * 
	 */
        public void set_H(int heuristic_choice);
	
	/**
	 * get the value of the heuristic
	 * @return the value of the heuristic
	 */
	public int get_H();
        
        /**
         * get the value of F
         * @return 
         */
        public int get_F();
        
        /**
         * get the journey for a particular state
         * @return a list of Puzzle
         */
        public LinkedList<Puzzle> get_journey();
        
        /**
         * 
         * @return the puzzle
         */
        public String get_puzzle();
        
        /**
         * add the journey of the current state to the journey of the Puzzle
         * @param new_puzzle 
         */
        public void add_puzzle_to_journey(State new_puzzle);
        
        
        /**
         *
         * @param open_list is the current open_list
         * @return an int : 1=successor is better than the one in the list,2= the puzzle in the list is better,0=not in the list
         */
        public int check_list(ArrayList<State> open_list);
        
        /**
         * 
         * @param heuristic_choice is 31 for manhattan and 32 for misplaced tiles
         * @return a list of Puzzle which are the successors of the current puzzle
         * by the way, we set H,G,and F
         */
        public ArrayList<State> get_successors(int heuristic_choice);
}

