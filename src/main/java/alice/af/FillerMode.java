package alice.af;

public enum FillerMode
{
	QUARRY, REMOVE, FILLING, FLATTEN, REMOVEE, TOFU;

	public FillerMode getNext()
	{
		switch(this)
		{
		case QUARRY:
			return REMOVE;
		case REMOVE:
			return FILLING;
		case FILLING:
			return FLATTEN;
		case FLATTEN:
			return REMOVEE;
		case REMOVEE:
			return TOFU;
		case TOFU:
			return QUARRY;
		}
		return QUARRY;
	}

	public static FillerMode getNext(FillerMode mode)
	{
		return mode.getNext();
	}

	public int toInteger()
	{
		switch(this)
		{
		case QUARRY:
			return 0;
		case REMOVE:
			return 1;
		case FILLING:
			return 2;
		case FLATTEN:
			return 3;
		case REMOVEE:
			return 4;
		case TOFU:
			return 5;
		}
		return 0;
	}

	public static int toInteger(FillerMode mode)
	{
		return mode.toInteger();
	}

	public static FillerMode getMode(int mode)
	{
		switch(mode)
		{
		case 0:
			return QUARRY;
		case 1:
			return REMOVE;
		case 2:
			return FILLING;
		case 3:
			return FLATTEN;
		case 4:
			return REMOVEE;
		case 5:
			return TOFU;
		}
		return QUARRY;
	}

	public String toString()
	{
		switch(this)
		{
		case QUARRY:
			return "Quarry Mode";
		case REMOVE:
			return "Remove Mode";
		case FILLING:
			return "Filling Mode";
		case FLATTEN:
			return "Flatten Mode";
		case REMOVEE:
			return "Exclusive Remove Mode";
		case TOFU:
			return "TofuBuild Mode";
		}
		return "";
	}
}
