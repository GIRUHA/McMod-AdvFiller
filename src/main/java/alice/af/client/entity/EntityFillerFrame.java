package alice.af.client.entity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import alice.af.tileentity.TileEntityAdvFiller;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public final class EntityFillerFrame extends Entity
{
	public final TileEntityAdvFiller filler;

	public EntityFillerFrame(TileEntityAdvFiller filler)
	{
		super(filler.getWorldObj());
		this.filler = filler;

		this.setSize(1F, 1F);
		// レンダリングにはlastTickPosが使われるのでこのメソッドを使用
		this.setLocationAndAngles(filler.xCoord, filler.yCoord, filler.zCoord, 0, 0);
		this.prevPosX = filler.xCoord;
		this.prevPosY = filler.yCoord;
		this.prevPosZ = filler.zCoord;
	}

	@Override
	protected void entityInit()
	{
		this.ignoreFrustumCheck = true;
	}

	@Override
	protected boolean canTriggerWalking()
	{
		return false;
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return !this.isDead;
	}

	@Override
	public void onEntityUpdate()
	{
		super.onEntityUpdate();

		World world = this.filler.getWorldObj();
		TileEntity te = world.getTileEntity(this.filler.xCoord, this.filler.yCoord, this.filler.zCoord);
		if((te == null) || (te != this.filler))
		{
			world.weatherEffects.remove(this.filler.frame);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public float getShadowSize()
	{
		return 0;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tag)
	{
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tag)
	{
	}
}