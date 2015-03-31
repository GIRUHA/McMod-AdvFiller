package alice.af.tileentity;

import alice.af.FillerMode;
import alice.af.client.entity.EntityFillerFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public final class TileEntityAdvFiller extends TileEntity implements IInventory
{
	private final ItemStack[] inventory = new ItemStack[26];

	public int startX;
	public int startY;
	public int startZ;
	public int endX;
	public int endY;
	public int endZ;
	public FillerMode mode = FillerMode.QUARRY;
	public boolean loop;
	public boolean iterate;
	public boolean drop;

	public EntityFillerFrame frame;

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		World world = this.worldObj;

		if(world.isRemote)
		{
			this.spawnFrameEntity();
			return;
		}

		int meta = world.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
		int newMeta = meta;

		if(world.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord))
		{
			newMeta |= 0x4;
		}
		else
		{
			newMeta &= 0xB;
		}

		if(newMeta != meta)
		{
			world.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, newMeta, 2);
		}
	}

	public void spawnFrameEntity()
	{
		if(this.frame == null)
		{
			this.frame = new EntityFillerFrame(this);
			this.worldObj.addWeatherEffect(this.frame);
		}
	}

	@Override
	public Packet getDescriptionPacket()
	{
		NBTTagCompound tag = new NBTTagCompound();
		this.writeNBT(tag, true);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	{
		this.readNBT(pkt.func_148857_g(), true);
	}

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);
		this.readNBT(tag, true);
	}

	private void readNBT(NBTTagCompound tag, boolean updateInventory)
	{
		this.startX = tag.getInteger("stx");
		this.startY = tag.getInteger("sty");
		this.startZ = tag.getInteger("stz");
		this.endX = tag.getInteger("enx");
		this.endY = tag.getInteger("eny");
		this.endZ = tag.getInteger("enz");
		this.mode = FillerMode.getMode(tag.getByte("mod"));
		this.loop = tag.getBoolean("lop");
		this.iterate = tag.getBoolean("itr");
		this.drop = tag.getBoolean("drp");

		if(updateInventory && tag.hasKey("inv"))
		{
			NBTTagList inventoryNbt = tag.getTagList("inv", 10);
			int inventoryNbtSize = inventoryNbt.tagCount();
			for(int indexNbt = 0; indexNbt < inventoryNbtSize; indexNbt++)
			{
				NBTTagCompound itemNbt = inventoryNbt.getCompoundTagAt(indexNbt);
				if(itemNbt == null)
				{
					continue;
				}

				ItemStack item = null;
				try
				{
					item = ItemStack.loadItemStackFromNBT(itemNbt);
				}
				catch(Exception e)
				{
				}
				if(item != null)
				{
					int index = itemNbt.getByte("idx") - 1;
					this.setInventorySlotContents(index, item);
				}
			}
		}
	}

	@Override
	public void writeToNBT(NBTTagCompound tag)
	{
		super.writeToNBT(tag);
		this.writeNBT(tag, true);
	}

	private void writeNBT(NBTTagCompound tag, boolean sendInventory)
	{
		tag.setInteger("stx", this.startX);
		tag.setInteger("sty", this.startY);
		tag.setInteger("stz", this.startZ);
		tag.setInteger("enx", this.endX);
		tag.setInteger("eny", this.endY);
		tag.setInteger("enz", this.endZ);
		tag.setByte("mod", (byte)this.mode.toInteger());
		tag.setBoolean("lop", this.loop);
		tag.setBoolean("itr", this.iterate);
		tag.setBoolean("drp", this.drop);

		if(sendInventory)
		{
			NBTTagList inventoryNbt = new NBTTagList();

			for(int i = 0; i < this.getSizeInventory(); i++)
			{
				ItemStack item = this.getStackInSlot(i);
				if((item == null) || (item.stackSize < 0))
				{
					continue;
				}

				NBTTagCompound itemNbt = new NBTTagCompound();
				item.writeToNBT(itemNbt);
				itemNbt.setByte("idx", (byte)(1 + i));

				inventoryNbt.appendTag(itemNbt);
			}

			tag.setTag("inv", inventoryNbt);
		}
	}

	public int getLeft()
	{
		int meta = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
		switch(meta & 3)
		{
		case 0:
			return this.xCoord - this.startX;
		case 1:
			return this.zCoord - this.startZ;
		case 2:
			return this.endX - this.xCoord;
		case 3:
			return this.endZ - this.zCoord;
		}
		return 0;
	}

	public void setLeft(int blocks)
	{
		int meta = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
		switch(meta & 3)
		{
		case 0:
			this.startX = this.xCoord - blocks;
			break;
		case 1:
			this.startZ = this.zCoord - blocks;
			break;
		case 2:
			this.endX = this.xCoord + blocks;
			break;
		case 3:
			this.endZ = this.zCoord + blocks;
			break;
		}
	}

	public int getRight()
	{
		int meta = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
		switch(meta & 3)
		{
		case 0:
			return this.endX - this.xCoord;
		case 1:
			return this.endZ - this.zCoord;
		case 2:
			return this.xCoord - this.startX;
		case 3:
			return this.zCoord - this.startZ;
		}
		return 0;
	}

	public void setRight(int blocks)
	{
		int meta = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
		switch(meta & 3)
		{
		case 0:
			this.endX = this.xCoord + blocks;
			break;
		case 1:
			this.endZ = this.zCoord + blocks;
			break;
		case 2:
			this.startX = this.xCoord - blocks;
			break;
		case 3:
			this.startZ = this.zCoord - blocks;
			break;
		}
	}

	public int getUpper()
	{
		return this.endY - this.yCoord;
	}

	public void setUpper(int blocks)
	{
		this.endY = this.yCoord + blocks;
	}

	public int getBottom()
	{
		return this.yCoord - this.startY;
	}

	public void setBottom(int blocks)
	{
		this.startY = this.yCoord - blocks;
	}

	public int getForward()
	{
		int meta = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
		switch(meta & 3)
		{
		case 0:
			return this.zCoord - this.startZ;
		case 1:
			return this.endX - this.xCoord;
		case 2:
			return this.endZ - this.zCoord;
		case 3:
			return this.xCoord - this.startX;
		}
		return 0;
	}

	public void setForward(int blocks)
	{
		int meta = this.worldObj.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
		switch(meta & 3)
		{
		case 0:
			this.startZ = this.zCoord - blocks;
			break;
		case 1:
			this.endX = this.xCoord + blocks;
			break;
		case 2:
			this.endZ = this.zCoord + blocks;
			break;
		case 3:
			this.startX = this.xCoord - blocks;
			break;
		}
	}

	@Override
	public int getSizeInventory()
	{
		return inventory.length;
	}

	@Override
	public ItemStack getStackInSlot(int index)
	{
		if((index < 0) || (index >= getSizeInventory()))
		{
			return null;
		}

		return this.inventory[index];
	}

	@Override
	public ItemStack decrStackSize(int index, int amount)
	{
		ItemStack item = this.getStackInSlot(index);
		if(item == null)
		{
			return null;
		}

		if(item.stackSize >= amount)
		{
			item.stackSize -= amount;
			if(item.stackSize <= 0)
			{
				this.setInventorySlotContents(index, null);
			}
			item = item.copy();
			item.stackSize = amount;
		}

		return item;
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int index)
	{
		return null;
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack item)
	{
		if((index >= 0) && (index < getSizeInventory()))
		{
			if((item != null) && (item.stackSize > 0))
			{
				this.inventory[index] = item;
			}
			else
			{
				this.inventory[index] = null;
			}
		}
	}

	@Override
	public String getInventoryName()
	{
		return "container.advfiller";
	}

	@Override
	public boolean hasCustomInventoryName()
	{
		return false;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return true;
	}

	@Override
	public void openInventory()
	{
	}

	@Override
	public void closeInventory()
	{
	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack item)
	{
		return ((index >= 0) && (index < getSizeInventory()));
	}
}
