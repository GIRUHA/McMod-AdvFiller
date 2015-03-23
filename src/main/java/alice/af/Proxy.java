package alice.af;

import java.util.List;

import alice.af.block.BlockAdvFiller;
import alice.af.network.ChangeFillerConfig;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public class Proxy
{
	protected static ItemStack advfiller;
	protected static IRecipe useGear;
	protected static IRecipe noGear;

	public final SimpleNetworkWrapper simpleNetWrapper;
	protected GuiHandler guiHandler;

	public Proxy()
	{
		this.simpleNetWrapper = new SimpleNetworkWrapper("advfiller"); // 20文字以上を指定してはいけない
		this.guiHandler = new GuiHandler();
	}

	public void forgePreInitialization(FMLPreInitializationEvent event)
	{
		advfiller = new ItemStack(new BlockAdvFiller());
	}

	@SuppressWarnings("unchecked")
	protected static List<IRecipe> getReclipeList()
	{
		CraftingManager craft = CraftingManager.getInstance();
		return craft.getRecipeList();
	}

	public void forgeInitialization(FMLInitializationEvent event)
	{
		List<IRecipe> recipeList = getReclipeList();

		advfiller = new ItemStack(Blocks.bedrock, 1);
		advfiller.setStackDisplayName("tile.advfiller.name");

		useGear = new ShapedOreRecipe(advfiller, "IEI", "GIG", "DPD", 'I', "gearIron", 'G', "gearGold", 'D', "gearDiamond", 'E', "gemEmerald", 'P', Items.diamond_pickaxe);
		noGear = new ShapedOreRecipe(advfiller, "IEI", "GIG", "DPD", 'I', "ingotIron", 'G', "ingotGold", 'D', "gemDiamond", 'E', "gemEmerald", 'P', Items.diamond_pickaxe);

		recipeList.add(useGear);
		recipeList.add(noGear);

		NetworkRegistry network = NetworkRegistry.INSTANCE;
		network.registerGuiHandler(AdvancedFiller.INSTANCE, this.guiHandler);

		this.simpleNetWrapper.registerMessage(ChangeFillerConfig.class, ChangeFillerConfig.class, 0, Side.SERVER);
	}

	public void forgePostInitialization(FMLPostInitializationEvent event)
	{
		List<IRecipe> recipeList = getReclipeList();

		List<ItemStack> gearList = OreDictionary.getOres("gearIron");
		if(gearList.isEmpty())
		{
			recipeList.remove(useGear);
		}

		gearList = OreDictionary.getOres("gearGold");
		if(gearList.isEmpty() && recipeList.contains(useGear))
		{
			recipeList.remove(useGear);
		}

		gearList = OreDictionary.getOres("gearDiamond");
		if(gearList.isEmpty() && recipeList.contains(useGear))
		{
			recipeList.remove(useGear);
		}

		if(recipeList.contains(useGear))
		{
			recipeList.remove(noGear);
		}
	}
}
