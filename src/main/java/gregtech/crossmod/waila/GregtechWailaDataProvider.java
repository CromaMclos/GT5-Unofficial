package gregtech.crossmod.waila;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.config.Configuration;

import gregtech.api.interfaces.tileentity.IGregtechWailaProvider;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.impl.ConfigHandler;
import mcp.mobius.waila.utils.Constants;

public class GregtechWailaDataProvider implements IWailaDataProvider {

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        if (!ConfigHandler.instance()
            .getConfig(Configuration.CATEGORY_GENERAL, Constants.CFG_WAILA_FORCE_LEGACY_MODE, false)) {
            return currenttip;
        }

        final TileEntity tile = accessor.getTileEntity();
        if (tile instanceof IGregtechWailaProvider) {
            ((IGregtechWailaProvider) tile).getWailaBody(itemStack, currenttip, accessor, config);
        }

        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(final EntityPlayerMP player, final TileEntity tile, final NBTTagCompound tag,
        final World world, int x, int y, int z) {
        if (tile instanceof IGregtechWailaProvider) {
            ((IGregtechWailaProvider) tile).getWailaNBTData(player, tile, tag, world, x, y, z);
        }

        return tag;
    }
}
