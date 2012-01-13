//    This file is part of StateSpaceSearch.
//
//    StateSpaceSearch is free software: you can redistribute it and/or modify
//    it under the terms of the GNU General Public License as published by
//    the Free Software Foundation, either version 3 of the License, or
//    (at your option) any later version.
//
//    StateSpaceSearch is distributed in the hope that it will be useful,
//    but WITHOUT ANY WARRANTY; without even the implied warranty of
//    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//    GNU General Public License for more details.
//
//    You should have received a copy of the GNU General Public License
//    along with StateSpaceSearch.  If not, see <http://www.gnu.org/licenses/>.
    	
package net.crsr.statespace.rivercrossing;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;

import net.crsr.statespace.Search;

public class MissionariesCannibals
{

  @SuppressWarnings("serial")
  public static void main(String[] args)
  {
    Queue<MCState> fifo = new ArrayDeque<MCState>();
    System.out.println("FIFO queue:");
    runSearch(fifo, new HashSet<MCState>());
    Queue<MCState> lifo = new ArrayDeque<MCState>() {

      @Override
      public MCState remove()
      {
        return super.removeLast();
      }
      
    };
    System.out.println("LIFO queue:");
    runSearch(lifo, new HashSet<MCState>());
}

  private static void runSearch(Queue<MCState> queue, Set<MCState> seen)
  {
    Search<MCState> search = new Search<MCState>(queue, seen, new MCState());
    MCState end = search.findGoal();
    if (end != null)
    {
      System.out.println("Solution");
      for (MCState st = end; st != null; st = st.previous)
      {
        System.out.format("  %s\n", st);
      }
    }
    else
    {
      System.out.format("No solution found\n");
    }
    System.out.format("%d states examined\n", search.statesExamined());
  }

}
