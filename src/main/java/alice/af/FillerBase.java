package alice.af;

import alice.af.tileentity.TileEntityAdvFiller;

public abstract class FillerBase
{
	protected int startX;
	protected int startY;
	protected int startZ;
	protected int endX;
	protected int endY;
	protected int endZ;
	protected boolean iterate;
	protected boolean loop;

	protected int x;
	protected int y;
	protected int z;

	protected boolean complete;

	public FillerBase(TileEntityAdvFiller filler)
	{
		this.startX = filler.startX;
		this.startY = filler.startY;
		this.startZ = filler.startZ;
		this.endX = filler.endX;
		this.endY = filler.endY;
		this.endZ = filler.endZ;
		this.iterate = filler.iterate;
		this.loop = filler.loop;

		this.x = this.startX;
		if(this.iterate)
		{
			this.y = this.startY;
		}
		else
		{
			this.y = this.endY;
		}
		this.z = this.startZ;

		this.complete = false;
	}

	public abstract void doWork();

	public boolean isComplete()
	{
		return this.complete;
	}

	protected void nextBlock()
	{
		if(this.complete)
		{
			return;
		}

		int nextX = this.x + 1;
		if(nextX >= this.endX)
		{
			this.x = this.startX;
			int nextZ = this.z + 1;
			if(nextZ >= this.endZ)
			{
				this.z = this.startZ;
				int nextY = this.y;
				if(this.iterate)
				{
					nextY++;
				}
				else
				{
					nextY--;
				}
				if((iterate && (nextY >= this.endY)) || (!iterate && (nextY <= this.startY)))
				{
					this.complete = true;
					return;
				}
				else
				{
					this.y = nextY;
				}
			}
			else
			{
				this.z = nextZ;
			}
		}
		else
		{
			this.x = nextX;
		}
	}
}
