package alice.af.tileentity;

import alice.af.client.entity.EntityFillerFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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

	public boolean doRender;
	public EntityFillerFrame frame;

	@Override
	public void updateEntity()
	{
		super.updateEntity();

		this.startX = this.xCoord - 8;
		this.startY = this.yCoord;
		this.startZ = this.zCoord;
		this.endX = this.xCoord + 8;
		this.endY = this.yCoord + 16;
		this.endZ = this.zCoord - 16;

		World world = this.worldObj;
		if(world.isRemote)
		{
			// 描画用エンティティがスポーンしてない場合はスポーンさせる
			if(!this.doRender)
			{
				// 描画フラグと共有
				this.doRender = true;
				frame = new EntityFillerFrame(this);
				// 天候エフェクトのふり
				world.addWeatherEffect(this.frame);
			}
			else
			{
				TileEntity te = world.getTileEntity(xCoord, yCoord, zCoord);
				if(te != this)
				{
					world.weatherEffects.remove(this.frame);
				}
			}
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

	@Override
	public void readFromNBT(NBTTagCompound tag)
	{
		super.readFromNBT(tag);

		this.startX = tag.getInteger("stx");
		this.startY = tag.getInteger("sty");
		this.startZ = tag.getInteger("stz");
		this.endX = tag.getInteger("enx");
		this.endY = tag.getInteger("eny");
		this.endZ = tag.getInteger("enz");

		if(tag.hasKey("inv"))
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

		tag.setInteger("stx", this.startX);
		tag.setInteger("sty", this.startY);
		tag.setInteger("stz", this.startZ);
		tag.setInteger("enx", this.endX);
		tag.setInteger("eny", this.endY);
		tag.setInteger("enz", this.endZ);

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
