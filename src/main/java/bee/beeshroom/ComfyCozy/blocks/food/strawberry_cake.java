package bee.beeshroom.ComfyCozy.blocks.food;

import java.util.Random;

import bee.beeshroom.ComfyCozy.Main;
import bee.beeshroom.ComfyCozy.blocks.BlockBase;
import bee.beeshroom.ComfyCozy.init.ModBlocks;
import bee.beeshroom.ComfyCozy.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class strawberry_cake extends BlockBase
{

	 public static final PropertyInteger BITES = PropertyInteger.create("bites", 0, 4);
	    protected static final AxisAlignedBB[] CAKE_AABB = new AxisAlignedBB[] 
	    	   {new AxisAlignedBB(0.1875D, 0.0D, 0.1875D, 0.8125D, 0.5D, 0.8125D), 
	    		new AxisAlignedBB(0.3125D, 0.0D, 0.1875D, 0.8125D, 0.5D, 0.8125D), 
	    		new AxisAlignedBB(0.4375D, 0.0D, 0.1875D, 0.8125D, 0.5D, 0.8125D), 
	    		new AxisAlignedBB(0.5625D, 0.0D, 0.1875D, 0.8125D, 0.5D, 0.8125D), 
	    		new AxisAlignedBB(0.6875D, 0.0D, 0.1875D, 0.8125D, 0.5D, 0.8125D)};

	    public strawberry_cake(String name, Material material)
	    {
	    	super(name, material);
	        this.setDefaultState(this.blockState.getBaseState().withProperty(BITES, Integer.valueOf(0)));
	        this.setTickRandomly(true);
	        setSoundType(SoundType.CLOTH);
			setHardness(0.4F);
			setResistance(0.1F);

			//this.setMaxStackSize(int 1) 
			//this.setMaxStackSize(1);
			 
			/*	setRegistryName(name);
				setTranslationKey(name);
				
				setCreativeTab(Main.comfycozytab);
				ModBlocks.BLOCKS.add(this);
				ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName())); */
	       
	    }

		private void setMaxStackSize(int i) {
		
			this.setMaxStackSize(1); 
			
		}

		@Override
	    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	    {
	        return CAKE_AABB[((Integer)state.getValue(BITES)).intValue()];
	    }

	    @Override
	    public boolean isFullCube(IBlockState state)
	    {
	        return false;
	    }

	    @Override
	    public boolean isOpaqueCube(IBlockState state)
	    {
	        return false;
	    }

	    /**
	     * Called when the block is right clicked by a player.
	     */
	    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	    {
	        if (!worldIn.isRemote)
	        {
	            return this.eatCake(worldIn, pos, state, playerIn);
	        }
	        else
	        {
	            ItemStack itemstack = playerIn.getHeldItem(hand);
	            return this.eatCake(worldIn, pos, state, playerIn) || itemstack.isEmpty();
	        }
	    }

	    private boolean eatCake(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
	    {
	        if (!player.canEat(false))
	        {
	            return false;
	        }
	        else
	        {
	            player.addStat(StatList.CAKE_SLICES_EATEN);
	            worldIn.playSound((EntityPlayer)null, pos, SoundEvents.ENTITY_GENERIC_EAT, SoundCategory.BLOCKS, 0.8F, 1.0F); 
	            player.getFoodStats().addStats(3, 0.1F);
	            int i = ((Integer)state.getValue(BITES)).intValue();

	            if (i < 4)
	            {
	                worldIn.setBlockState(pos, state.withProperty(BITES, Integer.valueOf(i + 1))); // , 3);
	                
	            }
	            else
	            {
	                worldIn.setBlockToAir(pos);
	            }

	            return true;
	        }
	    }

	    /**
	     * Checks if this block can be placed exactly at the given position.
	     */
	    public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	    {
	        return super.canPlaceBlockAt(worldIn, pos) ? this.canBlockStay(worldIn, pos) : false;
	    }

	    /**
	     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
	     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
	     * block, etc.
	     */
	    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	    {
	        if (!this.canBlockStay(worldIn, pos))
	        {
	            worldIn.setBlockToAir(pos);
	        }
	    }

	    private boolean canBlockStay(World worldIn, BlockPos pos)
	    {
	        return worldIn.getBlockState(pos.down()).getMaterial().isSolid();
	    }

	    /**
	     * Returns the quantity of items to drop on block destruction.
	     */
	    public int quantityDropped(Random random)
	    {
	        return 1;
	    }

	    /**
	     * Get the Item that this Block should drop when harvested.
	     */
	    public Item getItemDropped(IBlockState state, Random rand, int fortune)
	    {
	        return ModItems.STRAWBERRY;
	    }

	    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
	    {
	        return new ItemStack(ModBlocks.STRAWBERRY_CAKE);
	    }

	    /**
	     * Convert the given metadata into a BlockState for this Block
	     */
	    public IBlockState getStateFromMeta(int meta)
	    {
	        return this.getDefaultState().withProperty(BITES, Integer.valueOf(meta));
	    }

	    @Override
	    @SideOnly(Side.CLIENT)
	    public BlockRenderLayer getRenderLayer()
	    {
	        return BlockRenderLayer.CUTOUT;
	    }

	    /**
	     * Convert the BlockState into the correct metadata value
	     */
	    public int getMetaFromState(IBlockState state)
	    {
	        return ((Integer)state.getValue(BITES)).intValue();
	    }

	    protected BlockStateContainer createBlockState()
	    {
	        return new BlockStateContainer(this, new IProperty[] {BITES});
	    }

	    public int getComparatorInputOverride(IBlockState blockState, World worldIn, BlockPos pos)
	    {
	        return (7 - ((Integer)blockState.getValue(BITES)).intValue()) * 2;
	    }

	    public boolean hasComparatorInputOverride(IBlockState state)
	    {
	        return true;
	    }

	    /**
	     * Get the geometry of the queried face at the given position and state. This is used to decide whether things like
	     * buttons are allowed to be placed on the face, or how glass panes connect to the face, among other things.
	     * <p>
	     * Common values are {@code SOLID}, which is the default, and {@code UNDEFINED}, which represents something that
	     * does not fit the other descriptions and will generally cause other things not to connect to the face.
	     * 
	     * @return an approximation of the form of the given face
	     */
	    public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face)
	    {
	        return BlockFaceShape.UNDEFINED;
	    }
}
