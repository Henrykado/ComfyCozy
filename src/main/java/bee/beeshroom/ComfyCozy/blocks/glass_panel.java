package bee.beeshroom.ComfyCozy.blocks;

import java.util.Random;

import javax.annotation.Nullable;

import bee.beeshroom.ComfyCozy.Main;
import bee.beeshroom.ComfyCozy.init.ModBlocks;
import bee.beeshroom.ComfyCozy.init.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//thank-you turtywurty for your custom block model tutorial on youtube and thank-you to end rod code from vanilla

public class glass_panel extends BlockDirectional
{

	 public static final PropertyDirection FACING = BlockHorizontal.FACING;
	    public static final PropertyBool OPEN = PropertyBool.create("open");
	    public static final PropertyEnum<BlockTrapDoor.DoorHalf> HALF = PropertyEnum.<BlockTrapDoor.DoorHalf>create("half", BlockTrapDoor.DoorHalf.class);
/*	protected static final AxisAlignedBB PANEL_EAST = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 1.0D);
    protected static final AxisAlignedBB PANEL_WEST = new AxisAlignedBB(0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
    protected static final AxisAlignedBB PANEL_SOUTH = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
    protected static final AxisAlignedBB PANEL_NORTH = new AxisAlignedBB(0.0D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);*/
  //  protected static final AxisAlignedBB PANEL_TOP = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.1875D, 1.0D);
//    protected static final AxisAlignedBB PANEL_BOTTOM = new AxisAlignedBB(0.0D, 0.8125D, 0.0D, 1.0D, 1.0D, 1.0D);
	    protected static final AxisAlignedBB EAST_OPEN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 0.1875D, 1.0D, 1.0D);
	    protected static final AxisAlignedBB WEST_OPEN_AABB = new AxisAlignedBB(0.8125D, 0.0D, 0.0D, 1.0D, 1.0D, 1.0D);
	    protected static final AxisAlignedBB SOUTH_OPEN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 1.0D, 0.1875D);
	    protected static final AxisAlignedBB NORTH_OPEN_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.8125D, 1.0D, 1.0D, 1.0D);
	    protected static final AxisAlignedBB BOTTOM_AABB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.1875D, 1.0D);
	    protected static final AxisAlignedBB TOP_AABB = new AxisAlignedBB(0.0D, 0.8125D, 0.0D, 1.0D, 1.0D, 1.0D);


    
	    public glass_panel(String name, Material material) 
	    {
		super(material);
		 this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
		setSoundType(SoundType.GLASS);
		setHardness(0.5F);
		setResistance(0.1F);
		setHarvestLevel("pickaxe", 0);
		setRegistryName(name);
		setTranslationKey(name);
		
		setCreativeTab(Main.comfycozytab);
		ModBlocks.BLOCKS.add(this);
		ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));
	}  
	
	@Override
	public boolean isFullBlock(IBlockState state) 
	{
		return false;
	}

	 protected boolean canSilkHarvest()
	    {
	        return true;
	    }
		
	    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	    {
	        AxisAlignedBB axisalignedbb;

	        if (((Boolean)state.getValue(OPEN)).booleanValue())
	        {
	            switch ((EnumFacing)state.getValue(FACING))
	            {
	                case NORTH:
	                default:
	                    axisalignedbb = NORTH_OPEN_AABB;
	                    break;
	                case SOUTH:
	                    axisalignedbb = SOUTH_OPEN_AABB;
	                    break;
	                case WEST:
	                    axisalignedbb = WEST_OPEN_AABB;
	                    break;
	                case EAST:
	                    axisalignedbb = EAST_OPEN_AABB;
	            }
	        }
	        else if (state.getValue(HALF) == BlockTrapDoor.DoorHalf.TOP)
	        {
	            axisalignedbb = TOP_AABB;
	        }
	        else
	        {
	            axisalignedbb = BOTTOM_AABB;
	        }

	        return axisalignedbb;
	    }

	    /**
	     * Used to determine ambient occlusion and culling when rebuilding chunks for render
	     */
	    public boolean isOpaqueCube(IBlockState state)
	    {
	        return false;
	    }

	    public boolean isFullCube(IBlockState state)
	    {
	        return false;
	    }

	    /**
	     * Determines if an entity can path through this block
	     */
	    public boolean isPassable(IBlockAccess worldIn, BlockPos pos)
	    {
	        return !((Boolean)worldIn.getBlockState(pos).getValue(OPEN)).booleanValue();
	    }

	    /**
	     * Called when the block is right clicked by a player.
	     */
	    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	    {
	        if (this.material == Material.IRON)
	        {
	            return false;
	        }
	        else
	        {
	            state = state.cycleProperty(OPEN);
	            worldIn.setBlockState(pos, state, 2);
	            this.playSound(playerIn, worldIn, pos, ((Boolean)state.getValue(OPEN)).booleanValue());
	            return true;
	        }
	    }

	    protected void playSound(@Nullable EntityPlayer player, World worldIn, BlockPos pos, boolean p_185731_4_)
	    {
	        if (p_185731_4_)
	        {
	            int i = this.material == Material.IRON ? 1037 : 1007;
	            worldIn.playEvent(player, i, pos, 0);
	        }
	        else
	        {
	            int j = this.material == Material.IRON ? 1036 : 1013;
	            worldIn.playEvent(player, j, pos, 0);
	        }
	    }

	    /**
	     * Called when a neighboring block was changed and marks that this state should perform any checks during a neighbor
	     * change. Cases may include when redstone power is updated, cactus blocks popping off due to a neighboring solid
	     * block, etc.
	     */
	    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	    {
	        if (!worldIn.isRemote)
	        {
	            boolean flag = worldIn.isBlockPowered(pos);

	            if (flag || blockIn.getDefaultState().canProvidePower())
	            {
	                boolean flag1 = ((Boolean)state.getValue(OPEN)).booleanValue();

	                if (flag1 != flag)
	                {
	                    worldIn.setBlockState(pos, state.withProperty(OPEN, Boolean.valueOf(flag)), 2);
	                    this.playSound((EntityPlayer)null, worldIn, pos, flag);
	                }
	            }
	        }
	    }

	    /**
	     * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
	     * IBlockstate
	     */
	    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	    {
	        IBlockState iblockstate = this.getDefaultState();

	        if (facing.getAxis().isHorizontal())
	        {
	            iblockstate = iblockstate.withProperty(FACING, facing).withProperty(OPEN, Boolean.valueOf(false));
	            iblockstate = iblockstate.withProperty(HALF, hitY > 0.5F ? BlockTrapDoor.DoorHalf.TOP : BlockTrapDoor.DoorHalf.BOTTOM);
	        }
	        else
	        {
	            iblockstate = iblockstate.withProperty(FACING, placer.getHorizontalFacing().getOpposite()).withProperty(OPEN, Boolean.valueOf(true)); //changed this to true :>
	            iblockstate = iblockstate.withProperty(HALF, facing == EnumFacing.UP ? BlockTrapDoor.DoorHalf.BOTTOM : BlockTrapDoor.DoorHalf.TOP);
	        }

	        if (worldIn.isBlockPowered(pos))
	        {
	            iblockstate = iblockstate.withProperty(OPEN, Boolean.valueOf(true));
	        }

	        return iblockstate;
	    }

	    /**
	     * Check whether this Block can be placed at pos, while aiming at the specified side of an adjacent block
	     */
	    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side)
	    {
	        return true;
	    }

	    protected static EnumFacing getFacing(int meta)
	    {
	        switch (meta & 3)
	        {
	            case 0:
	                return EnumFacing.NORTH;
	            case 1:
	                return EnumFacing.SOUTH;
	            case 2:
	                return EnumFacing.WEST;
	            case 3:
	            default:
	                return EnumFacing.EAST;
	        }
	    }

	    protected static int getMetaForFacing(EnumFacing facing)
	    {
	        switch (facing)
	        {
	            case NORTH:
	                return 0;
	            case SOUTH:
	                return 1;
	            case WEST:
	                return 2;
	            case EAST:
	            default:
	                return 3;
	        }
	    }

	    /**
	     * Convert the given metadata into a BlockState for this Block
	     */
	    public IBlockState getStateFromMeta(int meta)
	    {
	        return this.getDefaultState().withProperty(FACING, getFacing(meta)).withProperty(OPEN, Boolean.valueOf((meta & 4) != 0)).withProperty(HALF, (meta & 8) == 0 ? BlockTrapDoor.DoorHalf.BOTTOM : BlockTrapDoor.DoorHalf.TOP);
	    }

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
	        int i = 0;
	        i = i | getMetaForFacing((EnumFacing)state.getValue(FACING));

	        if (((Boolean)state.getValue(OPEN)).booleanValue())
	        {
	            i |= 4;
	        }

	        if (state.getValue(HALF) == BlockTrapDoor.DoorHalf.TOP)
	        {
	            i |= 8;
	        }

	        return i;
	    }

	    /**
	     * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
	     * blockstate.
	     */
	    public IBlockState withRotation(IBlockState state, Rotation rot)
	    {
	        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
	    }

	    /**
	     * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
	     * blockstate.
	     */
	    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
	    {
	        return state.withRotation(mirrorIn.toRotation((EnumFacing)state.getValue(FACING)));
	    }

	    protected BlockStateContainer createBlockState()
	    {
	        return new BlockStateContainer(this, new IProperty[] {FACING, OPEN, HALF});
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
	        return (face == EnumFacing.UP && state.getValue(HALF) == BlockTrapDoor.DoorHalf.TOP || face == EnumFacing.DOWN && state.getValue(HALF) == BlockTrapDoor.DoorHalf.BOTTOM) && !((Boolean)state.getValue(OPEN)).booleanValue() ? BlockFaceShape.SOLID : BlockFaceShape.UNDEFINED;
	    }

	    @Override
	    public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity)
	    {
	        if (state.getValue(OPEN))
	        {
	            IBlockState down = world.getBlockState(pos.down());
	            if (down.getBlock() == net.minecraft.init.Blocks.LADDER)
	                return down.getValue(BlockLadder.FACING) == state.getValue(FACING);
	        }
	        return false;
	    }

	    public static enum DoorHalf implements IStringSerializable
	    {
	        TOP("top"),
	        BOTTOM("bottom");

	        private final String name;

	        private DoorHalf(String name)
	        {
	            this.name = name;
	        }

	        public String toString()
	        {
	            return this.name;
	        }

	        public String getName()
	        {
	            return this.name;
	        }
	    }
	}