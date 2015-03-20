package alice.af.client;

import alice.af.Proxy;
import alice.af.client.entity.EntityFillerFrame;
import alice.af.client.renderer.entity.RenderFillerFrame;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class ClientProxy extends Proxy
{
	public ClientProxy()
	{
		this.guiHandler = new ClientGuiHandler();
	}

	@Override
	public void forgeInitialization(FMLInitializationEvent event)
	{
		super.forgeInitialization(event);

		RenderingRegistry.registerEntityRenderingHandler(EntityFillerFrame.class, new RenderFillerFrame());
	}
}
