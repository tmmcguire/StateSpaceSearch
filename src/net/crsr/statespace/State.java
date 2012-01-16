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

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * The "state" in state-space search. At a minimum, a state is responsible for
 * <ol>
 * <li>Generating a list of new states by evaluating the valid operations on itself, and
 * <li>Deciding whether it is a satisfactory goal state.
 * </ol>
 * 
 * <p>
 * Because a {@link Set} of previously-seen states is also kept, implementations of this class should also
 * <ul>
 * <li>have reasonably valid implementations of <code>hashCode</code> and <code>equals</code>, if a {@link HashSet} is to be used.
 * <li>be an implementation of {@link Comparable}, if a {@link TreeSet} is to be used. A {@link Comparator} could be passed to the TreeSet constructor as well.
 * </ul>
 * 
 * <p>
 * For reasons of genericity, the state class should be defined as: <code>class MyState implements State&lt;MyState></code>.
 * 
 * @author mcguire
 * 
 * @param <T>
 *          The state class itself. See above.
 */
public interface State<T>
{
  /**
   * Expand the state by evaluating all of the valid operations and create a list of new states.
   * 
   * @return {@link List} of States immediately reachable from this state.
   */
  public List<T> expand();

  /**
   * Test whether the state satisfies the search's goal conditions.
   * 
   * @return True, if the state is a good goal state.
   */
  public boolean isGoal();
}
