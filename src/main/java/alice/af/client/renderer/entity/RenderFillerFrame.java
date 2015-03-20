package alice.af.client.renderer.entity;

import org.lwjgl.opengl.GL11;

import alice.af.client.entity.EntityFillerFrame;
import alice.af.tileentity.TileEntityAdvFiller;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
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
		TileEntityAdvFiller tile = e.filler;

		if(!e.filler.doRender)
		{
			return;
		}

		double fromX = -(tile.xCoord - tile.startX);
		double toX = -(tile.xCoord - tile.endX) + 1;
		double fromY = -(tile.yCoord - tile.startY);
		double toY = -(tile.yCoord - tile.endY) + 1;
		double fromZ = -(tile.zCoord - tile.startZ);
		double toZ = -(tile.zCoord - tile.endZ) + 1;

		GL11.glPushAttrib(GL11.GL_TEXTURE_2D);
//		GL11.glPushAttrib(GL11.GL_LIGHTING);
//		GL11.glPushAttrib(GL11.GL_CULL_FACE);
		GL11.glPushAttrib(GL11.GL_BLEND);

		GL11.glDisable(GL11.GL_TEXTURE_2D);
//		GL11.glDisable(GL11.GL_LIGHTING);
//		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_BLEND);

		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		RenderGlobal.drawOutlinedBoundingBox(AxisAlignedBB.getBoundingBox(fromX, fromY, fromZ, toX, toY, toZ), 0xFFFFFF);
		GL11.glPopMatrix();

		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_BLEND);
//		GL11.glEnable(GL11.GL_LIGHTING);
//		GL11.glEnable(GL11.GL_CULL_FACE);

//		GL11.glPopAttrib();
//		GL11.glPopAttrib();
		GL11.glPopAttrib();
		GL11.glPopAttrib();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return null;
	}
}
