package alice.af.inventory;

import alice.af.tileentity.TileEntityAdvFiller;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public final class ContainerAdvFiller extends Container
{
	private TileEntityAdvFiller filler;

	public ContainerAdvFiller(TileEntityAdvFiller filler, EntityPlayer player)
	{
		this.filler = filler;

		for(int y = 0; y < 2; y++)
		{
			for(int x = 0; x < 13; x++)
			{
				this.addSlotToContainer(new Slot(filler, x + (y * 13), 12 + (x * 18), 122 + (y * 18)));
			}
		}

		if(player != null)
		{
			InventoryPlayer playerInventory = player.inventory;

			for(int y = 0; y < 3; y++)
			{
				for(int x = 0; x < 9; x++)
				{
					this.addSlotToContainer(new Slot(playerInventory, 9 + x + (y * 9), 48 + (x * 18), 162 + (y * 18)));
				}
			}
			for(int x = 0; x < 9; x++)
			{
				this.addSlotToContainer(new Slot(playerInventory, x, 48 + (x * 18), 220));
			}
		}
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index)
	{
		Slot slot = (Slot)this.inventorySlots.get(index);

		ItemStack itemStack = null;
		if((slot != null) && (slot.getHasStack()))
		{
			ItemStack newItemStack = slot.getStack();
			itemStack = newItemStack.copy();

			if(index < 26)
			{
				// 取引スロットからの移動は、インベントリへ戻す
				if(!this.mergeItemStack(newItemStack, 26, 36, false))
				{
					return null;
				}
				slot.onSlotChange(newItemStack, itemStack);
			}
			else
			{
				// インベントリから入れるときの処理
				if(!this.mergeItemStack(newItemStack, 0, 26, false))
				{
					return null;
				}
				slot.onSlotChange(newItemStack, itemStack);
			}

			if(newItemStack.stackSize <= 0)
			{
				slot.putStack(null);
			}
			else
			{
				slot.onSlotChanged();
			}

			if(newItemStack.stackSize == itemStack.stackSize)
			{
				return null;
			}

			slot.onPickupFromSlot(player, newItemStack);
		}

		return itemStack;
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return this.filler.isUseableByPlayer(player);
	}
}
