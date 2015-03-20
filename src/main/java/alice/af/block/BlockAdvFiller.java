package alice.af.block;

import alice.af.AdvancedFiller;
import alice.af.tileentity.TileEntityAdvFiller;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public final class BlockAdvFiller extends BlockContainer
{
	private IIcon frontOff;
	private IIcon frontOn;
	private IIcon side;
	private IIcon top;

	public BlockAdvFiller()
	{
		super(Material.iron);

		this.disableStats();
		this.setBlockName("advfiller");
		this.setCreativeTab(CreativeTabs.tabRedstone);
		this.setHardness(1.6F);

		GameRegistry.registerBlock(this, "advfiller");
		GameRegistry.registerTileEntity(TileEntityAdvFiller.class, "advfiller");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister icon)
	{
		this.frontOff = icon.registerIcon("advfiller:filler_front_off");
		this.frontOn = icon.registerIcon("advfiller:filler_front_on");
		this.side = icon.registerIcon("advfiller:filler_side");
		this.top = icon.registerIcon("advfiller:filler_top");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		IIcon icon;
		byte sideForFace;

		switch(side)
		{
		case 0: // Bottom
			icon = this.side;
			break;
		case 1: // Top
			icon = this.top;
			break;
		default:
			switch(meta & 3)
			{
			case 0:
				sideForFace = 3; // South
				break;
			case 1:
				sideForFace = 4; // West
				break;
			case 2:
				sideForFace = 2; // North
				break;
			case 3:
				sideForFace = 5; // East
				break;
			default:
				sideForFace = 0; // North
				break;
			}

			if(side == sideForFace)
			{
				icon = ((meta & 0x4) != 0) ? this.frontOn : this.frontOff;
			}
			else
			{
				icon = this.side;
			}
			break;
		}
		return icon;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		TileEntityAdvFiller t = new TileEntityAdvFiller();
		t.setWorldObj(world);
		return t;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float px, float py, float pz)
	{
		if(world.isRemote)
		{
			return true;
		}

		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if((tileEntity == null) || !(tileEntity instanceof TileEntityAdvFiller))
		{
			//ModLogger.chatNotify(world, "§4ERROR: Bad tile entity located at %d, %d, %d!§r", x, y, z);
			return false;
		}

		player.openGui(AdvancedFiller.INSTANCE, 0, world, x, y, z);

		return true;
	}

	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int meta)
	{
		// SERVER ONLY

		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if((tileEntity == null) || !(tileEntity instanceof TileEntityAdvFiller))
		{
			return;
		}
		TileEntityAdvFiller filler = (TileEntityAdvFiller)tileEntity;

		int inventorySize = filler.getSizeInventory();
		for(int index = 0; index < inventorySize; index++)
		{
			ItemStack item = filler.getStackInSlot(index);
			if(item == null)
			{
				continue;
			}

			EntityItem itemDrop = new EntityItem(world, 0.5 + x, 0.5 + y, 0.5 + z, item);
			world.spawnEntityInWorld(itemDrop);
		}

		super.breakBlock(world, x, y, z, block, meta);
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack itemStack)
	{
		if(entity == null)
		{
			return;
		}

		double yaw = Math.floor(((entity.rotationYaw / 360) * 4) + 0.5);
		byte direction = (byte)((int)yaw & 3);

		switch(direction)
		{
		case 0:
			direction = 2;
			break;
		case 1:
			direction = 3;
			break;
		case 2:
			direction = 0;
			break;
		case 3:
			direction = 1;
			break;
		}

		world.setBlockMetadataWithNotify(x, y, z, direction, 2);
	}
}
