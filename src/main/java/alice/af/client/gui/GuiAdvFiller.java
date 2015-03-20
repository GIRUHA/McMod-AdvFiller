package alice.af.client.gui;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import alice.af.inventory.ContainerAdvFiller;
import alice.af.tileentity.TileEntityAdvFiller;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

@SideOnly(Side.CLIENT)
public final class GuiAdvFiller extends GuiContainer
{
	private static final ResourceLocation GUI = new ResourceLocation("advfiller", "textures/gui/container/advfiller.png");
	private String guiCaption;

	public GuiAdvFiller(TileEntityAdvFiller filler, EntityPlayer player)
	{
		super(new ContainerAdvFiller(filler, player));

		this.xSize = 256;
		this.ySize = 244;
		this.guiCaption = StatCollector.translateToLocal(filler.getInventoryName());
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j)
	{
		GL11.glPushAttrib(GL11.GL_LIGHTING);
		GL11.glColor4f(1, 1, 1, 1);
		this.mc.renderEngine.bindTexture(GUI);

		int xCenter = (this.width - this.xSize) / 2;
		int yCenter = (this.height - this.ySize) / 2;

		this.drawTexturedModalRect(xCenter, yCenter, 0, 0, this.xSize, this.ySize);

		GL11.glPopAttrib();
	}
}
