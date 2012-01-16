package net.crsr.statespace.fifteenpuzzle;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import net.crsr.statespace.Search;

public class FifteenPuzzle
{

  public static void main(String[] args)
  {
    System.out.println("Uninformed, breadth first");
    Queue<FState> fifo = new ArrayDeque<FState>();
    runSearch(fifo, new HashSet<FState>(), FState.oneOut());

    System.out.println("Heuristic search");
    Queue<FState> priority = new PriorityQueue<FState>();
    runSearch(priority, new HashSet<FState>(), FState.oneOut());

    System.out.println("Heuristic search: manhattan distance");
    priority = new PriorityQueue<FState>();
    runSearch(priority, new HashSet<FState>(), FState.harder());

    System.out.println("Heuristic search: out of position");
    priority = new PriorityQueue<FState>(100, new FState.OutOfPosition());
    runSearch(priority, new HashSet<FState>(), FState.harder());
  }

  private static void runSearch(Queue<FState> queue, Set<FState> seen, FState initial)
  {
    Search<FState> search = new Search<FState>(queue, seen, initial);
    FState end = search.findGoal();
    if (end != null)
    {
      System.out.format("Solution:\n");
      for (FState st = end; st != null; st = st.previous)
      {
        System.out.format("%s\n", st);
      }
    }
    else
    {
      System.out.format("No solution found\n");
    }
    System.out.format("%d states examined\n", search.statesExamined());
  }
}
