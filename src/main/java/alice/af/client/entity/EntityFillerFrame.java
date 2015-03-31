package alice.af.client.entity;

import java.lang.ref.WeakReference;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import alice.af.AdvancedFiller;
import alice.af.tileentity.TileEntityAdvFiller;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public final class EntityFillerFrame extends Entity
{
	public final Logger LOG;
	public final WeakReference<TileEntityAdvFiller> filler;

	public EntityFillerFrame(TileEntityAdvFiller filler)
	{
		super(filler.getWorldObj());
		this.filler = new WeakReference<TileEntityAdvFiller>(filler);

		this.setSize(1F, 1F);
		// レンダリングにはlastTickPosが使われるのでこのメソッドを使用
		this.setLocationAndAngles(filler.xCoord + 0.5, filler.yCoord, filler.zCoord + 0.5, 0, 0);

		this.prevPosX = filler.xCoord;
		this.prevPosY = filler.yCoord;
		this.prevPosZ = filler.zCoord;

		LOG = AdvancedFiller.LOG;
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

		World world = this.worldObj;

		if(!world.isRemote)
		{
			throw new IllegalStateException("SERVER SIDE!");
		}

		TileEntityAdvFiller f = this.filler.get();
		if(f == null)
		{
			LOG.info("TileEntity at {},{},{} has gone.", this.posX, this.posY, this.posZ);
			this.setDead();
			return;
		}

		TileEntity te =  world.getTileEntity(f.xCoord, f.yCoord, f.zCoord);
		if((te == null) || (te != f))
		{
			LOG.info("Instance cur={} org={}", (te != null) ? te.hashCode() : 0, f.hashCode());
			f.frame = null;
			f = null;
			this.setDead();
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public int getBrightnessForRender(float dummy)
	{
		return 0xF000F0;
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
