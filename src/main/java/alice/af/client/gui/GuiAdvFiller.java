package alice.af.client.gui;

import java.util.List;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import alice.af.AdvancedFiller;
import alice.af.FillerMode;
import alice.af.Proxy;
import alice.af.inventory.ContainerAdvFiller;
import alice.af.network.ChangeFillerConfig;
import alice.af.tileentity.TileEntityAdvFiller;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
//import net.minecraft.util.StatCollector;

@SideOnly(Side.CLIENT)
public final class GuiAdvFiller extends GuiContainer
{
	private static final ResourceLocation GUI = new ResourceLocation("advfiller", "textures/gui/container/advfiller.png");
//	private String guiCaption;
	private SimpleNetworkWrapper netWrapper;
	private int cx;
	private int cy;
	private TileEntityAdvFiller filler;

	private int left;
	private int right;
	private int up;
	private int down;
	private int forward;
	private FillerMode type;
	private boolean loopMode;
	private boolean iterate;
	private boolean drop;

	public GuiAdvFiller(TileEntityAdvFiller filler, EntityPlayer player)
	{
		super(new ContainerAdvFiller(filler, player));

		this.xSize = 256;
		this.ySize = 244;
		this.filler = filler;

//		this.guiCaption = StatCollector.translateToLocal(filler.getInventoryName());

		this.left = filler.getLeft();
		this.right = filler.getRight();
		this.up = filler.getUpper();
		this.down = filler.getBottom();
		this.forward = filler.getForward();
		this.type = filler.mode;
		this.loopMode = filler.loop;
		this.iterate = filler.iterate;
		this.drop = filler.drop;
	}

	@Override
	public void initGui()
	{
		super.initGui();

		Proxy proxy = AdvancedFiller.PROXY;
		this.netWrapper = proxy.simpleNetWrapper;

		this.cx = (this.width - this.xSize) / 2;
		this.cy = (this.height - this.ySize) / 2;

		this.buttonList.clear();
		@SuppressWarnings("unchecked")
		List<GuiButton> _b = (List<GuiButton>)this.buttonList;

		_b.add(new GuiButton(1, this.cx + 154, this.cy + 72, 48, 20, "Type"));
		_b.add(new GuiButton(2, this.cx + 104, this.cy + 72, 48, 20, "Set"));

		byte cnt = 0;
		byte m = -32; // 位置補正＋間隔取り
		String str[] = { "--", "-", "+", "++" };
		for (int i = 3; i <= 22; i++)
		{
			_b.add(new GuiButton(i, this.cx + i * 12 + m, this.cy + 38, 12, 20, str[cnt]));
			cnt++;
			if(cnt > 3)
			{
				cnt = 0;
				m += 2;
			}
		}
		_b.add(new GuiButton(23, this.cx + 204, this.cy + 72, 48, 20, "Loop:" + (this.loopMode ? "ON" : "OFF")));
		_b.add(new GuiButton(24, this.cx + 4, this.cy + 72, 48, 20, (this.iterate ? "ASCEND" : "DESCEND")));
		_b.add(new GuiButton(25, this.cx + 54, this.cy + 72, 48, 20, "Drop:" + (this.drop ? "ON" : "OFF")));
	}

	@Override
	protected void actionPerformed(GuiButton button)
	{
		switch(button.id)
		{
		case 1:
			this.type = this.type.getNext();
			break;
		case 2:
			this.netWrapper.sendToServer(this.createMessage());
			this.mc.displayGuiScreen(null);
			this.mc.setIngameFocus();
			break;
		// Left
		case 3:
			this.left = creasesNumber(this.left, -16);
			break;
		case 4:
			this.left = creasesNumber(this.left, -1);
			break;
		case 5:
			this.left = creasesNumber(this.left, 1);
			break;
		case 6:
			this.left = creasesNumber(this.left, 16);
			break;
		// Right
		case 7:
			this.right = creasesNumber(this.right, -16);
			break;
		case 8:
			this.right = creasesNumber(this.right, -1);
			break;
		case 9:
			this.right = creasesNumber(this.right, 1);
			break;
		case 10:
			this.right = creasesNumber(this.right, 16);
			break;
		// Up
		case 11:
			this.up = creasesNumber(this.up, -16);
			break;
		case 12:
			this.up = creasesNumber(this.up, -1);
			break;
		case 13:
			this.up = creasesNumber(this.up, 1);
			break;
		case 14:
			this.up = creasesNumber(this.up, 16);
			break;
		// Down
		case 15:
			this.down = creasesNumber(this.down, -16);
			break;
		case 16:
			this.down = creasesNumber(this.down, -1);
			break;
		case 17:
			this.down = creasesNumber(this.down, 1);
			break;
		case 18:
			this.down = creasesNumber(this.down, 16);
			break;
		// Forward
		case 19:
			this.forward = creasesNumber(this.forward, -16, 1, 256);
			break;
		case 20:
			this.forward = creasesNumber(this.forward, -1, 1, 256);
			break;
		case 21:
			this.forward = creasesNumber(this.forward, 1, 1, 256);
			break;
		case 22:
			this.forward = creasesNumber(this.forward, 16, 1, 256);
			break;
		case 23:
			this.loopMode = !this.loopMode;
			button.displayString = "Loop:" + (this.loopMode ? "ON" : "OFF");
			break;
		case 24:
			this.iterate = !this.iterate;
			button.displayString = this.iterate ? "ASCEND" : "DESCEND";
			break;
		case 25:
			this.drop = !this.drop;
			button.displayString = "Drop:" + (this.drop ? "ON" : "OFF");
			break;
		}
	}

	private IMessage createMessage()
	{
		this.filler.setLeft(this.left);
		this.filler.setRight(this.right);
		this.filler.setUpper(this.up);
		this.filler.setBottom(this.down);
		this.filler.setForward(this.forward);
		this.filler.mode = this.type;
		this.filler.loop = this.loopMode;
		this.filler.iterate = this.iterate;
		this.filler.drop = this.drop;
		return new ChangeFillerConfig(this.filler);
	}

	public int creasesNumber(int current, int add)
	{
		return this.creasesNumber(current, add, 0, 256);
	}

	public int creasesNumber(int current, int add, int min, int max)
	{
		current += add;
		if (current < min)
		{
			current = min;
		}
		else if (current > max)
		{
			current = max;
		}
		return current;
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

	@Override
	public void drawScreen(int par1, int par2, float par3)
	{
		this.drawDefaultBackground();
		super.drawScreen(par1, par2, par3);

		String str = String.valueOf(this.left);

		this.fontRendererObj.drawString(str, this.cx + 29 - this.fontRendererObj.getStringWidth(str) / 2, this.cy + 23, 65280);
		str = String.valueOf(this.right);
		this.fontRendererObj.drawString(str, this.cx + 79 - this.fontRendererObj.getStringWidth(str) / 2, this.cy + 23, 65280);
		str = String.valueOf(this.up);
		this.fontRendererObj.drawString(str, this.cx + 129 - this.fontRendererObj.getStringWidth(str) / 2, this.cy + 23, 65280);
		str = String.valueOf(this.down);
		this.fontRendererObj.drawString(str, this.cx + 179 - this.fontRendererObj.getStringWidth(str) / 2, this.cy + 23, 65280);
		str = String.valueOf(this.forward);
		this.fontRendererObj.drawString(str, this.cx + 229 - this.fontRendererObj.getStringWidth(str) / 2, this.cy + 23, 65280);
		str = "LEFT:";
		this.fontRendererObj.drawString(str, this.cx + 5, this.cy + 10, 5197647);
		str = "RIGHT:";
		this.fontRendererObj.drawString(str, this.cx + 55, this.cy + 10, 5197647);
		str = "UP:";
		this.fontRendererObj.drawString(str, this.cx + 105, this.cy + 10, 5197647);
		str = "DOWN:";
		this.fontRendererObj.drawString(str, this.cx + 155, this.cy + 10, 5197647);
		str = "FORWARD:";
		this.fontRendererObj.drawString(str, this.cx + 205, this.cy + 10, 5197647);
		this.fontRendererObj.drawString(type.toString(), this.cx + 7, this.cy + 107, 65280);
		str = "Iteration:";
		this.fontRendererObj.drawString(str, this.cx + 5, this.cy + 60, 5197647);
		str = "Type:";
		this.fontRendererObj.drawString(str, this.cx + 5, this.cy + 95, 5197647);
	}
}
