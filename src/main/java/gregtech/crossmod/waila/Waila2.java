package gregtech.crossmod.waila;

import cpw.mods.fml.common.event.FMLInterModComms;
import gregtech.api.enums.Mods;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import mcp.mobius.waila.api.elements.IProbeDataProvider;
import mcp.mobius.waila.api.elements.IProbeRegistrar;

/**
 * new Waila integration class
 */
public class Waila2 {

    public static void callbackRegister(IProbeRegistrar register) {
        final IProbeDataProvider gregtechProvider = new GregtechProbeDataProvider();

        register.registerProbeProvider(gregtechProvider, BaseMetaTileEntity.class);
    }

    public static void init() {
        FMLInterModComms.sendMessage(Mods.Waila.ID, "elementregister", Waila2.class.getName() + ".callbackRegister");
    }
}
