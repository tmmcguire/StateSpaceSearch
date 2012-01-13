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

import java.util.ArrayList;
import java.util.List;

import net.crsr.statespace.State;

public class MCState implements State<MCState>
{
  private static enum BoatSide { THIS, THAT };
  
  private static class RiverSide
  {
    public final int missionaries;
    public final int cannibals;
    
    public RiverSide(int missionaries, int cannibals)
    {
      this.missionaries = missionaries;
      this.cannibals = cannibals;
    }
    
    public RiverSide missionaries(int n)
    {
      return new RiverSide(missionaries + n, cannibals);
    }

    public RiverSide cannibals(int n)
    {
      return new RiverSide(missionaries, cannibals + n);
    }

    public RiverSide both(int n)
    {
      return new RiverSide(missionaries + n, cannibals + n);
    }
    
    @Override
    public boolean equals(Object o)
    {
      if (! (o instanceof RiverSide)) return false;
      final RiverSide other = (RiverSide) o;
      return this.missionaries == other.missionaries
          && this.cannibals == other.cannibals;
    }
    
    @Override
    public int hashCode()
    {
      return missionaries ^ cannibals;
    }
    
    @Override
    public String toString()
    {
      return String.format("[%d missionaries, %d cannibals]", missionaries, cannibals);
    }
  }
  
  public final BoatSide boatSide;
  public final RiverSide thisSide;
  public final RiverSide thatSide;
  public final MCState previous;
  
  public MCState()
  {
    boatSide = BoatSide.THIS;
    thisSide = new RiverSide(3, 3);
    thatSide = new RiverSide(0, 0);
    previous = null;
  }
  
  private MCState(MCState previous, RiverSide thisSide, RiverSide thatSide)
  {
    this.boatSide = previous.boatSide == BoatSide.THIS ? BoatSide.THAT : BoatSide.THIS;
    this.thisSide = thisSide;
    this.thatSide = thatSide;
    this.previous = previous;
  }
  
  @Override
  public boolean isGoal()
  {
    return thatSide.missionaries == 3 && thatSide.cannibals == 3;
  }

  @Override
  public List<MCState> expand()
  {
    List<MCState> newStates = new ArrayList<MCState>();
    if (boatSide == BoatSide.THIS)
    {
      if (thisSide.missionaries > 0)
      {
        final MCState newState =
            new MCState(this, thisSide.missionaries(-1), thatSide.missionaries(1));
        queueNewState(newStates, newState);
      }
      if (thisSide.missionaries > 1)
      {
        final MCState newState =
            new MCState(this, thisSide.missionaries(-2), thatSide.missionaries(2));
        queueNewState(newStates, newState);
      }
      if (thisSide.cannibals > 0)
      {
        final MCState newState =
            new MCState(this, thisSide.cannibals(-1), thatSide.cannibals(1));
        queueNewState(newStates, newState);
      }
      if (thisSide.cannibals > 1)
      {
        final MCState newState =
            new MCState(this, thisSide.cannibals(-2), thatSide.cannibals(2));
        queueNewState(newStates, newState);
      }
      if (thisSide.missionaries > 0 && thisSide.cannibals > 0)
      {
        final MCState newState =
            new MCState(this, thisSide.both(-1), thatSide.both(1));
        queueNewState(newStates, newState);
      }
    }
    else
    {
      if (thatSide.missionaries > 0)
      {
        final MCState newState =
            new MCState(this,thisSide.missionaries(1), thatSide.missionaries(-1));
        queueNewState(newStates, newState);
      }
      if (thatSide.missionaries > 1)
      {
        final MCState newState =
            new MCState(this,thisSide.missionaries(2), thatSide.missionaries(-2));
        queueNewState(newStates, newState);
      }
      if (thatSide.cannibals > 0)
      {
        final MCState newState =
            new MCState(this,thisSide.cannibals(1), thatSide.cannibals(-1));
        queueNewState(newStates, newState);
      }
      if (thatSide.cannibals > 1)
      {
        final MCState newState =
            new MCState(this,thisSide.cannibals(2), thatSide.cannibals(-2));
        queueNewState(newStates, newState);
      }
      if (thatSide.missionaries > 0 && thatSide.cannibals > 0)
      {
        final MCState newState =
            new MCState(this,thisSide.both(1), thatSide.both(-1));
        queueNewState(newStates, newState);
      }
    }
    return newStates;
  }
  
  private void queueNewState(final List<MCState> newStates, final MCState newState)
  {
    if (newState.isValid())
    {
      newStates.add(newState);
    }
  }
  
  private boolean isValid()
  {
    return (thisSide.missionaries == 0 || thisSide.missionaries >= thisSide.cannibals)
        && (thatSide.missionaries == 0 || thatSide.missionaries >= thatSide.cannibals);
  }

  @Override
  public String toString()
  {
    final String format = (boatSide == BoatSide.THIS) ? "%s * -> %s" : "%s -> * %s";
    return String.format(format, thisSide, thatSide);
  }
  
  @Override
  public boolean equals(final Object o)
  {
    if (! (o instanceof MCState)) return false;
    final MCState other = (MCState) o;
    return this.thisSide.equals(other.thisSide)
        && this.thatSide.equals(other.thatSide)
        && this.boatSide == other.boatSide;
  }
  
  @Override
  public int hashCode()
  {
    return thisSide.hashCode() ^ thatSide.hashCode() ^ boatSide.hashCode();
  }
}
