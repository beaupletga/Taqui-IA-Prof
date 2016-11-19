/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Stack;

/**
 *
 * @author gabriel
 */
public class Problem<State extends Searchable<State,Action>,Action> {

    /** initial state of the search problem */
    State initialState;
    /** final state of the search problem */
    State finalState;
	
    /** method for checking whether a solution has been found by comparing a state with the final state
     * @param s is the state to be considered
     * @return true if the state s is the final state
     */
	public boolean goal_test(State s){
		return finalState.equals(s);
	}

    /**
     * Constructeur initialize the initial state and the final state
     * @param start initial state
     * @param stop final state
     */
	public Problem(State start, State stop){
		initialState = start;
		finalState = stop;
	}

    /** Breadth-first search algorithm
     * @return return a final state if it founds a path from the initial state to the final state
     */
    
    public State bfs()
    {
        LinkedList<State> queue= new LinkedList<>();
        queue.add(initialState);
        boolean est_present;
        ArrayList<State> already_visited= new ArrayList<State>();
        while(queue.size()>0 && !goal_test(queue.getFirst()))//while th head of the queue isn't the solution or the list isn't empty
        {
            final State tmp_state=queue.pop();//we remove the head of the queue
            already_visited.add(tmp_state);
            for (PuzzleAction i : tmp_state.getActions())//and add its successsors to the queue
            {
                State son_state=tmp_state.execute(i);
                
                est_present=false;
                for(int j=0;j<already_visited.size();j++)//to avoid infinite loop
                {
                    if(son_state.equals(already_visited.get(j)))
                    {
                        est_present=true;
                        break;
                    }
                }
                if(!est_present)
                {
                    son_state.set_G(tmp_state.get_G()+1);
                    son_state.add_puzzle_to_journey(tmp_state);
                    queue.add(son_state);
                }
            }
        }
        return queue.getFirst();
    }
	
    /** Depth-first search algorithm
     * @return return a final state if it founds a path from the initial state to the final state
     */
    public State dfs()
    {
        Stack<State> stack= new Stack<>();
        boolean est_present;
        ArrayList<State> already_visited= new ArrayList<State>();
        stack.add(initialState);
        while(stack.size()>0 && !goal_test(stack.peek()))//while the head of the stack isn't the solution or the list isn't empty
        {
            State tmp_state=stack.pop();//we remove the head of the stack
            already_visited.add(tmp_state);
            
            for (PuzzleAction i :tmp_state.getActions())//and add its successors
            {
                State son_state=tmp_state.execute(i);
                est_present=false;
                for(int j=0;j<already_visited.size();j++)// to avoid infinite loop
                {
                    if(son_state.equals(already_visited.get(j)))
                    {
                        est_present=true;
                        break;
                    }
                }
                if(!est_present)
                {
                    son_state.set_G(tmp_state.get_G()+1);
                    son_state.add_puzzle_to_journey(tmp_state);
                    stack.push(son_state);
                }
                
            }
            
        }
        return stack.peek();	
    }

    /** A star search algorithm. The heuristic is encoded in the state (State must implement
     * the interface Searchable that has a method computeHeuristic and getHeuristic
     * @param heuristic_choice = 31 for manhattan heuristic and 32 for the misplaced tiles
     * @return return a final state if it founds a path from the initial state to the final state
     * @throws java.lang.Exception
     */
   
    public State aStar(int heuristic_choice) throws Exception//31=manhattan,32=misplaced_tiles
    {
        ArrayList<State> open_list= new ArrayList<>();
        ArrayList<State> closed_list= new ArrayList<>();
        
        initialState.set_H(heuristic_choice);//set the heuristic for the root state
        initialState.set_F();
        open_list.add(initialState);
        int choix1,choix2;
        
        while (open_list.size()>0)
        {        
            State current_state=get_least_heuristic_state(open_list);//get the lowest heuristic state
         
            ArrayList<State> successors_list= current_state.get_successors(heuristic_choice);//get the successors of this state

            if (current_state.get_H()==0)//if this state has a heuristic=0, then we are at the final state
            {
                return current_state;
            }
            
            State successor;
            for (int i=0;i<successors_list.size();i++)
            {
                successor=successors_list.get(i);
                
                choix1=successor.check_list(closed_list);
                choix2=successor.check_list(open_list);
                
                
                if(!(choix1==2 || choix2==2))//successor exit in open or in closed list but has a higher G
                {
                    open_list.add(successor);
                }
            }
            closed_list.add(current_state);//finally, we add the current state
        }
        System.out.println("Pas de solution");//if the open list is empty and no solution has been found then there isn't any solution
        throw new Exception("Pas de solution");
    }
    
    /**
     * 
     * @param state_list which is the current open list
     * @return the puzzle which has the lowest F
     */
    public State get_least_heuristic_state(ArrayList<State> state_list)
    {
        int min_heuristic_place=0;
        State tmp,minimum=state_list.get(0);
        for (int i=1;i<state_list.size();i++)
        {
            tmp=state_list.get(i);
            if(minimum.compareTo(tmp)>0)//in order to get the lowest F
            {
                minimum=tmp;
                min_heuristic_place=i;
            }
            else if(minimum.compareTo(tmp)==0)//if there are a few puzzle which have the same F
            {
                if(tmp.get_H()<minimum.get_H())//we take the one which have the lowest heuristic (the closest to the goal state)
                {
                    minimum=tmp;
                    min_heuristic_place=i;
                }
            }
        }
        State tmp_state=state_list.remove(min_heuristic_place);
        return tmp_state;
    }    
}

