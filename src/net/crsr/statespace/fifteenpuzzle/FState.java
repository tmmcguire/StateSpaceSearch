package net.crsr.statespace.fifteenpuzzle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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
		return new FState(Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12,13,14,0,15));
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
		for (int i = 0; i < BOARD-1; ++i)
		{
			if (board.get(i) != i+1) { return false; }
		}
		return true;
	}

	@Override
	public List<FState> expand()
	{
		List<FState> result = new ArrayList<FState>();
		for (FState newState : Arrays.asList(this.left(), this.right(), this.up(), this.down()))
		{
			if (newState != null)
			{
				result.add(newState);
			}
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
				sb.append( String.format("%3d", board.get(i)) );
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
		return this.board == otherState.board && this.empty == otherState.empty;
	}
	
	@Override
	public int hashCode()
	{
		return this.board.hashCode() ^ this.empty;
	}

	@Override
	public int compareTo(FState o)
	{
		return this.outOfPosition() - o.outOfPosition();
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
		if (empty % ROW != ROW-1)
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
		if (empty / ROW != ROW-1)
		{
			// Empty location not at bottom edge
			return new FState(this, empty + ROW);
		}
		else
		{
			return null;
		}
	}
	
	private int outOfPosition()
	{
		int out = 0;
		for (int i = 0; i < BOARD-1; ++i)
		{
			if (board.get(i) != i+1)
			{
				++out;
			}
		}
		if (empty != 15)
		{
			++out;
		}
		return out;
	}
}
