package alice.af.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import alice.af.FillerMode;
import alice.af.block.BlockAdvFiller;
import alice.af.tileentity.TileEntityAdvFiller;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;

public final class ChangeFillerConfig implements IMessage, IMessageHandler<ChangeFillerConfig, IMessage>
{
	private int dimension;
	private int xCoord;
	private int yCoord;
	private int zCoord;
	private int left;
	private int right;
	private int up;
	private int down;
	private int forward;
	private FillerMode mode;
	private boolean loop;
	private boolean iterate;
	private boolean drop;

	public ChangeFillerConfig()
	{
	}

	public ChangeFillerConfig(TileEntityAdvFiller tile)
	{
		World world = tile.getWorldObj();

		this.dimension = world.provider.dimensionId;
		this.xCoord = tile.xCoord;
		this.yCoord = tile.yCoord;
		this.zCoord = tile.zCoord;
		this.left = tile.getLeft();
		this.right = tile.getRight();
		this.up = tile.getUpper();
		this.down = tile.getBottom();
		this.forward = tile.getForward();
		this.mode = tile.mode;
		this.loop = tile.loop;
		this.iterate = tile.iterate;
		this.drop = tile.drop;
	}

	private void updateTile(NetHandlerPlayServer server)
	{
		World world = server.playerEntity.worldObj;
		if((world == null) || (world.provider.dimensionId != this.dimension))
		{
			return;
		}
		TileEntity tile = world.getTileEntity(this.xCoord, this.yCoord, this.zCoord);
		if((tile == null) || !(tile instanceof TileEntityAdvFiller))
		{
			return;
		}

		TileEntityAdvFiller filler = (TileEntityAdvFiller)tile;

		filler.setLeft(this.left);
		filler.setRight(this.right);
		filler.setUpper(this.up);
		filler.setBottom(this.down);
		filler.setForward(this.forward);
		filler.mode = this.mode;
		filler.loop = this.loop;
		filler.iterate = this.iterate;
		filler.drop = this.drop;

		Block b = world.getBlock(this.xCoord, this.yCoord, this.zCoord);
		if(b instanceof BlockAdvFiller)
		{
			int m = world.getBlockMetadata(this.xCoord, this.yCoord, this.zCoord);
			world.setBlockMetadataWithNotify(this.xCoord, this.yCoord, this.zCoord, m, 2);
		}
	}

	@Override
	public IMessage onMessage(ChangeFillerConfig message, MessageContext ctx)
	{
		message.updateTile(ctx.getServerHandler());
		return null;
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.dimension = buf.readShort();
		this.xCoord = buf.readInt();
		this.yCoord = buf.readInt();
		this.zCoord = buf.readInt();
		this.left = buf.readShort();
		this.right = buf.readShort();
		this.up = buf.readShort();
		this.down = buf.readShort();
		this.forward = buf.readShort();
		this.mode = FillerMode.getMode(buf.readByte());
		this.loop = buf.readBoolean();
		this.iterate = buf.readBoolean();
		this.drop = buf.readBoolean();
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeShort(this.dimension);
		buf.writeInt(this.xCoord);
		buf.writeInt(this.yCoord);
		buf.writeInt(this.zCoord);
		buf.writeShort(this.left);
		buf.writeShort(this.right);
		buf.writeShort(this.up);
		buf.writeShort(this.down);
		buf.writeShort(this.forward);
		buf.writeByte(this.mode.toInteger());
		buf.writeBoolean(this.loop);
		buf.writeBoolean(this.iterate);
		buf.writeBoolean(this.drop);
	}
}
