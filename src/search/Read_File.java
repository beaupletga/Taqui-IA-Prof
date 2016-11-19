/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package search;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gabriel
 */
public class Read_File 
{
    public Read_File(int choice) throws Exception//1=BFS,2=DFS,31=Astar(manhanttan),32=Astar(misplaced tiles)
    {
        BufferedReader reader;
        List<Long> runTimes = new ArrayList<>();
        long startTime=0,finishTime=0;
        try
        {
            reader = new BufferedReader(new FileReader("games.txt"));
            String line = reader.readLine();
            int problemSize=0;
            System.out.println();
            System.out.println("Nb de coups"+"\t"+"Temps");
            while (line!=null)
            {   
                if (line.charAt(0)=='%')
                {
                    if (!runTimes.isEmpty())
                    {
                        System.out.print(problemSize + "\t" + "\t" + mean(runTimes) +"("+runTimes.size()+")\t");
                        System.out.println();
                        
                        try 
                        {
                            FileWriter writer = new FileWriter("results.txt", true);
                            BufferedWriter bufferedWriter = new BufferedWriter(writer);

                            bufferedWriter.write(problemSize + "\t" + "\t" + mean(runTimes));
                            bufferedWriter.newLine(); 
                            bufferedWriter.close();
                        } 
                        catch (IOException e) 
                        {
                            e.printStackTrace();
                        }
                        runTimes.clear();//On supprime la liste précédente afin de que les résultats précedents n'influent pas sur les nouveaux
                    }
                    problemSize = Integer.parseInt(line.substring(2));
                    System.out.println();
                }
                else
                {
                    Problem<Puzzle, PuzzleAction> eightpuzzle 
                    = new Problem<>(new Puzzle(line), new Puzzle("012345678"));
                    switch (choice)
                    {
                        case 1:
                        {
                            startTime = System.nanoTime();
                            eightpuzzle.bfs();
                            finishTime = System.nanoTime();
                            runTimes.add(finishTime-startTime);
                            break;
                        }
                        case 2:
                        {
                            startTime = System.nanoTime();
                            eightpuzzle.dfs();
                            finishTime = System.nanoTime();
                            runTimes.add(finishTime-startTime);
                            break;
                        }
                        case 31:
                        {
                            startTime = System.nanoTime();
                            eightpuzzle.aStar(31);
                            finishTime = System.nanoTime();
                            runTimes.add(finishTime-startTime);
                            break;
                        }
                        case 32:
                        {
                            startTime = System.nanoTime();
                            eightpuzzle.aStar(32);
                            finishTime = System.nanoTime();
                            runTimes.add(finishTime-startTime);
                            break;
                        }
                    }
                }
                line = reader.readLine();
            }
        }
        catch(IOException e)
        {
                System.err.println(e);
        }
    }
    
    public Read_File() throws Exception //run all the algorithm
    {
		BufferedReader reader;
		List<Long> runTimesDFS = new ArrayList<Long>();
		List<Long> runTimesBFS = new ArrayList<Long>();
		List<Long> runTimesAStar_manhattan = new ArrayList<Long>();
		List<Long> runTimesAStar_mt = new ArrayList<Long>();

                try{
			reader = new BufferedReader(new FileReader("games.txt"));
			String line = reader.readLine();
			int problemSize=0;
                        System.out.println("Nb de coups"+"\t"+"DFS"+"\t"+"BFS"+"\t"+"Astar(manhattan)"+"\t"+"Astar(misplaced tiles)");

			while (line!=null){
				if (line.charAt(0)=='%'){
					if (!runTimesDFS.isEmpty()){
						System.out.print(problemSize + "\t" + mean(runTimesDFS) +"("+runTimesDFS.size()+")\t");
						System.out.print(mean(runTimesBFS) +"("+runTimesBFS.size()+")\t");
						System.out.print(mean(runTimesAStar_manhattan) +"("+runTimesAStar_manhattan.size()+")\t");
                                                System.out.print(mean(runTimesAStar_manhattan) +"("+runTimesAStar_manhattan.size()+")\t");
                                                System.out.print(mean(runTimesAStar_mt) +"("+runTimesAStar_mt.size()+")");
						System.out.println();
                                                runTimesDFS.clear();
                                                runTimesBFS.clear();
                                                runTimesAStar_manhattan.clear();
                                                runTimesAStar_mt.clear();
					}
					problemSize = Integer.parseInt(line.substring(2));
				}
				else
				{
					Problem<Puzzle, PuzzleAction> eightpuzzle 
					= new Problem<Puzzle,PuzzleAction>(new Puzzle(line), new Puzzle("012345678"));
					// run DFS
					long startTime = System.nanoTime();
					eightpuzzle.dfs();
					long finishTime = System.nanoTime();
					runTimesDFS.add(finishTime-startTime);
					
					// run BFS
					startTime = System.nanoTime();
					eightpuzzle.bfs();
					finishTime = System.nanoTime();
					runTimesBFS.add(finishTime-startTime);
					
					// run A star manhattan
					startTime = System.nanoTime();
					eightpuzzle.aStar(31);
					finishTime = System.nanoTime();
					runTimesAStar_manhattan.add(finishTime-startTime);
                                        
                                        // run A star misplaced tiles
					startTime = System.nanoTime();
					eightpuzzle.aStar(32);
					finishTime = System.nanoTime();
					runTimesAStar_mt.add(finishTime-startTime);
				}
				line = reader.readLine();
			}
		}catch(IOException e){
			System.err.println(e);
			e.printStackTrace();
		}
	}
    
    
    
    
    
    public static double mean(List<Long> l)
    {
        long res=0;
        for (Long val: l)
                res+=val;
        return Math.abs((1.0*res) / ((long)1000000000*l.size()));
    }
	
    public static double std(List<Long> l)
    {
        double m = mean(l);
        double res=0;
        for (Long val: l)
                res+= Math.pow(val/1000000000-m,2);
        return Math.sqrt(res / l.size());	
    }
}
