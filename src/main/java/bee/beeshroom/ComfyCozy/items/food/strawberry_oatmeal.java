package bee.beeshroom.ComfyCozy.items.food;

import bee.beeshroom.ComfyCozy.Main;
import bee.beeshroom.ComfyCozy.init.ModBlocks;
import bee.beeshroom.ComfyCozy.init.ModItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class strawberry_oatmeal  extends ItemFood
{
	public strawberry_oatmeal(String name, int amount, float saturation, boolean isWolfFood) 
	{
		super(amount, saturation, isWolfFood);
		setTranslationKey(name);
		setRegistryName(name);
		setCreativeTab(Main.comfycozytab);
		//setAlwaysEdible();
		 this.setMaxStackSize(1);
		
		ModItems.ITEMS.add(this);
	}

	 public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
	    {
	        super.onItemUseFinish(stack, worldIn, entityLiving);
	        return new ItemStack(Items.BOWL);
	    }
	
	 @Override
		public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) 
		{
			ItemStack stack = player.getHeldItem(hand);
			IBlockState state = worldIn.getBlockState(pos);
		
			if(player.isSneaking() && facing == EnumFacing.UP && player.canPlayerEdit(pos.offset(facing), facing, stack) && worldIn.isAirBlock(pos.up()))
			{
				worldIn.setBlockState(pos.up(), ModBlocks.BOWL_STRAWBERRY.getDefaultState());
				if(!player.capabilities.isCreativeMode && !worldIn.isRemote)
				{
					stack.shrink(1);
				}
				return EnumActionResult.SUCCESS;
			}
			return EnumActionResult.FAIL;
		}
}