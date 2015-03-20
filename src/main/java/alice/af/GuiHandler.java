package alice.af;

import alice.af.inventory.ContainerAdvFiller;
import alice.af.tileentity.TileEntityAdvFiller;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile == null)
		{
			return null;
		}

		Container c = null;
		if(tile instanceof TileEntityAdvFiller)
		{
			c = new ContainerAdvFiller((TileEntityAdvFiller)tile, player);
		}

		return c;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}
}
