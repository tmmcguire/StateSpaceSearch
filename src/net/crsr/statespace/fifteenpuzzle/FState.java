package net.crsr.statespace.fifteenpuzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.crsr.statespace.State;

public class FState implements State<FState>, Comparable<FState>
{
  private static final int BOARD = 16;
  private static final int ROW = 4;

  private final List<Integer> board = new ArrayList<Integer>(BOARD);
  private final int empty;
  public final FState previous;

  public static FState oneOut()
  {
    return new FState(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 0, 15));
  }

  public static FState harder()
  {
    return new FState(Arrays.asList(15, 11, 4, 8, 5, 12, 3, 7, 9, 1, 10, 2, 0, 6, 14, 13));
  }

  private FState(List<Integer> board)
  {
    this.board.addAll(board);
    int emptyLocation = 0;
    for (int i = 0; i < BOARD; ++i)
    {
      if (board.get(i) == 0)
      {
        emptyLocation = i;
        break;
      }
    }
    this.empty = emptyLocation;
    this.previous = null;
  }

  private FState(FState origin, int swap)
  {
    this.board.addAll(origin.board);
    Collections.swap(this.board, origin.empty, swap);
    this.empty = swap;
    this.previous = origin;
  }

  @Override
  public boolean isGoal()
  {
    // The final square should be empty,
    // but I don't care.
    for (int i = 0; i < BOARD - 1; ++i)
    {
      if (board.get(i) != i + 1) { return false; }
    }
    return true;
  }

  @Override
  public List<FState> expand()
  {
    List<FState> result = new ArrayList<FState>();
    for (FState newState : Arrays.asList(this.left(), this.right(), this.up(), this.down()))
    {
      if (newState != null) { result.add(newState); }
    }
    return result;
  }

  @Override
  public String toString()
  {
    final StringBuilder sb = new StringBuilder();
    for (int i = 0; i < BOARD; ++i)
    {
      if (i == empty)
      {
        sb.append("   ");
      }
      else
      {
        sb.append(String.format("%3d", board.get(i)));
      }
      if (i % ROW == ROW - 1)
      {
        sb.append('\n');
      }
    }
    return sb.toString();
  }

  @Override
  public boolean equals(Object other)
  {
    if (!(other instanceof FState)) { return false; }
    FState otherState = (FState) other;
    return this.board.equals(otherState.board);
  }

  @Override
  public int hashCode()
  {
    return this.board.hashCode();
  }

  @Override
  public int compareTo(FState o)
  {
    return this.totalDistance() - o.totalDistance();
  }

  private FState left()
  {
    if (empty % ROW != 0)
    {
      // Empty location is not at left edge
      return new FState(this, empty - 1);
    }
    else
    {
      return null;
    }
  }

  private FState right()
  {
    if (empty % ROW != ROW - 1)
    {
      // Empty location not at right edge
      return new FState(this, empty + 1);
    }
    else
    {
      return null;
    }
  }

  private FState up()
  {
    if (empty / ROW != 0)
    {
      // Empty location not at top edge
      return new FState(this, empty - ROW);
    }
    else
    {
      return null;
    }
  }

  private FState down()
  {
    if (empty / ROW != ROW - 1)
    {
      // Empty location not at bottom edge
      return new FState(this, empty + ROW);
    }
    else
    {
      return null;
    }
  }

  public int totalDistance()
  {
    int out = 0;
    for (int i = 0; i < BOARD; ++i)
    {
      if (i == empty) continue;
      int position = board.get(i) - 1;
      out += Math.abs((i % ROW) - (position % ROW));
      out += Math.abs((i / ROW) - (position / ROW));
    }
    return out;
  }

  public int outOfPosition()
  {
    int out = 0;
    for (int i = 0; i < BOARD - 1; ++i)
    {
      if (board.get(i) != i + 1)
      {
        ++out;
      }
    }
    return out;
  }

  public static class OutOfPosition implements Comparator<FState>
  {

    @Override
    public int compare(FState o1, FState o2)
    {
      return o1.outOfPosition() - o2.outOfPosition();
    }

  }
}
