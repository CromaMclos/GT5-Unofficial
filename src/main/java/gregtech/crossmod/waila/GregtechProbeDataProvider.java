package gregtech.crossmod.waila;

import gregtech.api.interfaces.tileentity.IGregtechWailaProvider;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.ProbeMode;
import mcp.mobius.waila.api.elements.IProbeDataProvider;
import mcp.mobius.waila.api.elements.IProbeInfo;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GregtechProbeDataProvider implements IProbeDataProvider {
    @Override
    public void addProbeInfo(ProbeMode probeMode, ItemStack itemStack, IProbeInfo iProbeInfo, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        final TileEntity tile = accessor.getTileEntity();
        if (tile instanceof IProbeDataProvider) {
            ((IProbeDataProvider) tile).addProbeInfo(probeMode, itemStack, iProbeInfo, accessor, config);
        }
    }

    @Override
    public NBTTagCompound getNBTData(final EntityPlayerMP player, final TileEntity tile, final NBTTagCompound tag,
                                     final World world, int x, int y, int z) {
        if (tile instanceof IProbeDataProvider) {
            ((IProbeDataProvider) tile).getNBTData(player, tile, tag, world, x, y, z);
        }

        return tag;
    }
}
