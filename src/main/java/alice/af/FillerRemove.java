package alice.af;

import alice.af.tileentity.TileEntityAdvFiller;

public final class FillerRemove extends FillerBase
{
	protected boolean drop;

	public FillerRemove(TileEntityAdvFiller filler)
	{
		super(filler);
		this.drop = filler.drop;
	}

	public void doWork()
	{
		
	}
}
