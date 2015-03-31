package alice.af.client.renderer.entity;

import org.lwjgl.opengl.GL11;

import alice.af.client.entity.EntityFillerFrame;
import alice.af.tileentity.TileEntityAdvFiller;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ResourceLocation;

public final class RenderFillerFrame extends Render
{
	@Override
	public void doRender(Entity entity, double x, double y, double z, float f1, float f2)
	{
		this.render((EntityFillerFrame)entity, x, y, z);
	}

	private void render(EntityFillerFrame e, double x, double y, double z)
	{
		TileEntityAdvFiller tile = e.filler.get();
		if(tile == null)
		{
			return;
		}

		double fromX = tile.startX - tile.xCoord;
		double toX = tile.endX - tile.xCoord + 1;
		double fromY = tile.startY - tile.yCoord;
		double toY = tile.endY - tile.yCoord + 1;
		double fromZ = tile.startZ - tile.zCoord;
		double toZ = tile.endZ - tile.zCoord + 1;

		tile = null;
		AxisAlignedBB bb = AxisAlignedBB.getBoundingBox(fromX, fromY, fromZ, toX, toY, toZ);

		GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
		GL11.glPushMatrix();

		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_LIGHTING);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

		GL11.glTranslated(x - 0.5, y, z - 0.5);
		GL11.glColor4d(1, 0, 0, 0.5);
		GL11.glLineWidth(2F);
		GL11.glDepthMask(false);
		RenderGlobal.drawOutlinedBoundingBox(bb, -1);
		GL11.glDepthMask(true);

		GL11.glPopMatrix();
		GL11.glPopAttrib();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return null;
	}
}
