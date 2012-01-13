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

package net.crsr.statespace;

import java.util.Queue;
import java.util.Set;

public class Search<T extends State<T>>
{
  private int statesExamined;
  private final Queue<T> queue;
  private final Set<T> seen;
  
  /**
   * Initialize a Search. The {@link Queue} will be used to store states
   * (which I would imagine would always be empty), The {@link Set} will be
   * used to record previously seen states (which I think would also
   * always be empty), and the {@link State} is the start state for the
   * search.
   * 
   * <p>The initial state will be added to the queue as the first step.
   * 
   * @param queue A {@link Queue} used to store to-be-examined states
   * during the search.
   * @param seen A {@link Set} used to store previously-examined states
   * during the search.
   * @param initialState The initial {@link State} for the search.
   */
  public Search(Queue<T> queue, Set<T> seen, T initialState)
  {
    this.queue = queue;
    queue.add(initialState);
    this.seen = seen;
    this.statesExamined = 0;
  }
  
  public T findGoal()
  {
    while (! queue.isEmpty())
    {
      statesExamined++;
      
      T current = queue.remove();
      if (current.isGoal())
      {
        return current;
      }
      for (T newState : current.expand())
      {
        if (! seen.contains(newState))
        {
          seen.add(newState);
          queue.add(newState);
        }
      }
    }
    return null;
  }
  
  public int statesExamined()
  {
    return statesExamined;
  }
}
