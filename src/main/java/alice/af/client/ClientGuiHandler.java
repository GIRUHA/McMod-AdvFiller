package alice.af.client;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.Gui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import alice.af.GuiHandler;
import alice.af.client.gui.GuiAdvFiller;
import alice.af.tileentity.TileEntityAdvFiller;

@SideOnly(Side.CLIENT)
public final class ClientGuiHandler extends GuiHandler
{
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity tile = world.getTileEntity(x, y, z);
		if(tile == null)
		{
			return null;
		}

		Gui g = null;
		if(tile instanceof TileEntityAdvFiller)
		{
			g = new GuiAdvFiller((TileEntityAdvFiller)tile, player);
		}

		return g;
	}
}
