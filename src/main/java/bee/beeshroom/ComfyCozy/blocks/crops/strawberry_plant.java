
package bee.beeshroom.ComfyCozy.blocks.crops;

import java.util.Random;

import bee.beeshroom.ComfyCozy.init.ModBlocks;
import bee.beeshroom.ComfyCozy.init.ModItems;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockPane;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

//thank you turty wurty ;w;
//https://www.youtube.com/watch?v=AUEnR5k9yFQ

public class strawberry_plant extends BlockCrops implements IGrowable
{
    public static final PropertyInteger AGE = PropertyInteger.create("age", 0, 3);
		private static final AxisAlignedBB[] strawberry_plant = new AxisAlignedBB[] {new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.3125D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.6375D, 1.0D), new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.8625D, 1.0D)};

				  public strawberry_plant(String name)
				    {
				    	this.setTranslationKey(name);
				    	this.setRegistryName(name);
				        this.setDefaultState(this.blockState.getBaseState().withProperty(this.getAgeProperty(), Integer.valueOf(0)));
				        this.setTickRandomly(true);
				        this.setHardness(0.0F);
				        this.setSoundType(SoundType.PLANT);
				        this.disableStats();
				        
				        ModBlocks.BLOCKS.add(this);
				    }

				    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
				    {
				        return strawberry_plant[((Integer)state.getValue(this.getAgeProperty())).intValue()];
				    }

				    /**
				     * Return true if the block can sustain a Bush
				     */
				    protected boolean canSustainBush(IBlockState state)
				    {
				        return state.getBlock() == Blocks.FARMLAND;
				    }

				    protected PropertyInteger getAgeProperty()
				    {
				        return AGE;
				    }

				    public int getMaxAge()
				    {
				        return 3;
				    }

				    protected int getAge(IBlockState state)
				    {
				        return ((Integer)state.getValue(this.getAgeProperty())).intValue();
				    }

				    public IBlockState withAge(int age)
				    {
				        return this.getDefaultState().withProperty(this.getAgeProperty(), Integer.valueOf(age));
				    }

				    public boolean isMaxAge(IBlockState state)
				    {
				        return ((Integer)state.getValue(this.getAgeProperty())).intValue() >= this.getMaxAge();
				    }

				    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
				    {
				        super.updateTick(worldIn, pos, state, rand);

				        if (!worldIn.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
				        if (worldIn.getLightFromNeighbors(pos.up()) >= 9)
				        {
				            int i = this.getAge(state);

				            if (i < this.getMaxAge())
				            {
				                float f = getGrowthChance(this, worldIn, pos);

				                if(net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextInt((int)(25.0F / f) + 1) == 0))
				                {
				                    worldIn.setBlockState(pos, this.withAge(i + 1), 2);
				                    net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state, worldIn.getBlockState(pos));
				                }
				            }
				        }
				    }

				    public void grow(World worldIn, BlockPos pos, IBlockState state)
				    {
				        int i = this.getAge(state) + this.getBonemealAgeIncrease(worldIn);
				        int j = this.getMaxAge();

				        if (i > j)
				        {
				            i = j;
				        }

				        worldIn.setBlockState(pos, this.withAge(i), 2);
				    }

				    protected int getBonemealAgeIncrease(World worldIn)
				    {
				        return MathHelper.getInt(worldIn.rand, 2, 5);
				    }

				    protected static float getGrowthChance(Block blockIn, World worldIn, BlockPos pos)
				    {
				        float f = 1.0F;
				        BlockPos blockpos = pos.down();

				        for (int i = -1; i <= 1; ++i)
				        {
				            for (int j = -1; j <= 1; ++j)
				            {
				                float f1 = 0.0F;
				                IBlockState iblockstate = worldIn.getBlockState(blockpos.add(i, 0, j));

				                if (iblockstate.getBlock().canSustainPlant(iblockstate, worldIn, blockpos.add(i, 0, j), net.minecraft.util.EnumFacing.UP, (net.minecraftforge.common.IPlantable)blockIn))
				                {
				                    f1 = 1.0F;

				                    if (iblockstate.getBlock().isFertile(worldIn, blockpos.add(i, 0, j)))
				                    {
				                        f1 = 3.0F;
				                    }
				                }

				                if (i != 0 || j != 0)
				                {
				                    f1 /= 4.0F;
				                }

				                f += f1;
				            }
				        }

				        BlockPos blockpos1 = pos.north();
				        BlockPos blockpos2 = pos.south();
				        BlockPos blockpos3 = pos.west();
				        BlockPos blockpos4 = pos.east();
				        boolean flag = blockIn == worldIn.getBlockState(blockpos3).getBlock() || blockIn == worldIn.getBlockState(blockpos4).getBlock();
				        boolean flag1 = blockIn == worldIn.getBlockState(blockpos1).getBlock() || blockIn == worldIn.getBlockState(blockpos2).getBlock();

				        if (flag && flag1)
				        {
				            f /= 2.0F;
				        }
				        else
				        {
				            boolean flag2 = blockIn == worldIn.getBlockState(blockpos3.north()).getBlock() || blockIn == worldIn.getBlockState(blockpos4.north()).getBlock() || blockIn == worldIn.getBlockState(blockpos4.south()).getBlock() || blockIn == worldIn.getBlockState(blockpos3.south()).getBlock();

				            if (flag2)
				            {
				                f /= 2.0F;
				            }
				        }

				        return f;
				    }

				    public boolean canBlockStay(World worldIn, BlockPos pos, IBlockState state)
				    {
				        IBlockState soil = worldIn.getBlockState(pos.down());
				        return (worldIn.getLight(pos) >= 8 || worldIn.canSeeSky(pos)) && soil.getBlock().canSustainPlant(soil, worldIn, pos.down(), net.minecraft.util.EnumFacing.UP, this);
				    }

				    protected Item getSeed()
				    {
				        return ModItems.STRAWBERRY_SEEDS;
				    }

				    protected Item getCrop()
				    {
				        return ModItems.STRAWBERRY;
				    }

				    @SuppressWarnings("unused")
					@Override
				    public void getDrops(net.minecraft.util.NonNullList<ItemStack> drops, net.minecraft.world.IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
				    {
				        super.getDrops(drops, world, pos, state, 0);
				        int age = getAge(state);
				        Random rand = world instanceof World ? ((World)world).rand : new Random();

					       if (this.isMaxAge(state) && RANDOM.nextInt(10) == 0)
			                    drops.add(new ItemStack(ModItems.STRAWBERRY, 4)); 
					       if (this.isMaxAge(state) && RANDOM.nextInt(3) == 0)
			                    drops.add(new ItemStack(ModItems.STRAWBERRY, 2)); 

				        if (age >= getMaxAge())
				        {
				        	/////////changed from 3 to 2
				         //   int k = 2 + fortune;

				            for (int i = 0; i < 3 + fortune; ++i)
				            {
				            	
				            	//changed to 1 from 2
				               // if (rand.nextInt(1 * getMaxAge()) <= age)
				            	if (getMaxAge() > age)
				                {
				                    drops.add(new ItemStack(this.getSeed(), 1, 0)); 
				                }
				            }
				        }
				    }

				    /**
				     * Spawns this Block's drops into the World as EntityItems.
				     */
			/*	    @SuppressWarnings("unused")
					public void dropBlockAsItemWithChance(World worldIn, BlockPos pos, IBlockState state, float chance, int fortune)
				    {
				        super.dropBlockAsItemWithChance(worldIn, pos, state, chance, fortune);

				        if (false && !worldIn.isRemote) // Forge: NOP all this.
				        {
				            int i = this.getAge(state);

				            if (i >= this.getMaxAge())
				            {
				                int j = 3 + fortune;

				                for (int k = 0; k < j; ++k)
				                {
				                    if (worldIn.rand.nextInt(2 * this.getMaxAge()) <= i)
				                    {
				                        spawnAsEntity(worldIn, pos, new ItemStack(this.getSeed()));
				                    }
				                }
				            }
				        }
				    } */

				    /**
				     * Get the Item that this Block should drop when harvested.
				     */
				    public Item getItemDropped(IBlockState state, Random rand, int fortune)
				    {
				        return this.isMaxAge(state) ? this.getCrop() : this.getSeed();
				    }
				    
				/*    public int quantityDropped(Random random)
				    {
				        return 3;
				    } */

			/*	    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
				    {
				        return new ItemStack(this.getSeed());
				    } */

				    /**
				     * Whether this IGrowable can grow
				     */
				    public boolean canGrow(World worldIn, BlockPos pos, IBlockState state, boolean isClient)
				    {
				        return !this.isMaxAge(state);
				    }

				    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, IBlockState state)
				    {
				        return true;
				    }

				    public void grow(World worldIn, Random rand, BlockPos pos, IBlockState state)
				    {
				        this.grow(worldIn, pos, state);
				    }

				    /**
				     * Convert the given metadata into a BlockState for this Block
				     */
				    public IBlockState getStateFromMeta(int meta)
				    {
				        return this.withAge(meta);
				    }

				    /**
				     * Convert the BlockState into the correct metadata value
				     */
				    public int getMetaFromState(IBlockState state)
				    {
				        return this.getAge(state);
				    }

				    protected BlockStateContainer createBlockState()
				    {
				        return new BlockStateContainer(this, new IProperty[] {AGE});
				    }
				    
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
    {
        return BlockFaceShape.UNDEFINED;
    }
	
	
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        if (!this.canBePlacedOn(worldIn, pos.down()))
        {
        	 spawnAsEntity(worldIn, pos, new ItemStack(this.getSeed()));  
            worldIn.setBlockToAir(pos);
        }
    }  
  
  private boolean canBePlacedOn(World worldIn, BlockPos pos)
    {
        return worldIn.getBlockState(pos).getBlock() instanceof BlockFarmland;
    }
	
	

	// considering letting you "pluck" the berries without having to break the , but i dont think i will bc that'd conflict 
 //with all those great mods that let you harvest + replant by right clicking 				    
/*	 public void onBlockActivated(net.minecraft.util.NonNullList<ItemStack> drops, net.minecraft.world.IBlockAccess world, World worldIn, BlockPos pos, EntityPlayer playerIn, IBlockState state) 
	 {
	        super.getDrops(drops, world, pos, state, 0);
	        int age = getAge(state);
	        Random rand = world instanceof World ? ((World)world).rand : new Random();
		 
		 
         int i = this.getAge(state);

         if (age > 2)
		{
		worldIn.setBlockState(pos, state.withProperty(AGE, 1));
        {
            drops.add(new ItemStack(this.getCrop(), 1, 0)); 
        }
		
	 }  
} */
	

}