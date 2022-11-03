package gregtech.common.covers;

import static gregtech.api.enums.GT_Values.E;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import gregtech.api.gui.modularui.GT_CoverUIBuildContext;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollower_SlotWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollower_ToggleButtonWidget;
import io.netty.buffer.ByteBuf;
import javax.annotation.Nonnull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class GT_Cover_Fluidfilter extends GT_CoverBehaviorBase<GT_Cover_Fluidfilter.FluidFilterData> {

    // Uses the lower 3 bits of the cover variable, so we have 8 options to work with (0-7)
    private final int FILTER_INPUT_DENY_OUTPUT = 0; //  000
    private final int INVERT_INPUT_DENY_OUTPUT = 1; //  001
    private final int FILTER_INPUT_ANY_OUTPUT = 2; //  010
    private final int INVERT_INPUT_ANY_OUTPUT = 3; //  011
    private final int DENY_INPUT_FILTER_OUTPUT = 4; //  100
    private final int DENY_INPUT_INVERT_OUTPUT = 5; //  101
    private final int ANY_INPUT_FILTER_OUTPUT = 6; //  110
    private final int ANY_INPUT_INVERT_OUTPUT = 7; //  111

    /**
     * @deprecated use {@link #GT_Cover_Fluidfilter(ITexture coverTexture)} instead
     */
    @Deprecated
    GT_Cover_Fluidfilter() {
        this(null);
    }

    public GT_Cover_Fluidfilter(ITexture coverTexture) {
        super(FluidFilterData.class, coverTexture);
    }

    @Override
    public FluidFilterData createDataObject() {
        return new FluidFilterData(-1, 0);
    }

    @Override
    public FluidFilterData createDataObject(int aLegacyData) {
        return new FluidFilterData(aLegacyData >>> 3, aLegacyData & 0x7);
    }

    @Override
    protected String getDescriptionImpl(
            byte aSide, int aCoverID, FluidFilterData aCoverVariable, ICoverable aTileEntity) {
        final Fluid fluid = FluidRegistry.getFluid(aCoverVariable.mFluidID);
        if (fluid == null) return E;

        final FluidStack sFluid = new FluidStack(fluid, 1000);
        return (String.format(
                "Filtering Fluid: %s - %s", sFluid.getLocalizedName(), getFilterMode(aCoverVariable.mFilterMode)));
    }

    @Override
    protected boolean isRedstoneSensitiveImpl(
            byte aSide, int aCoverID, FluidFilterData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return false;
    }

    @Override
    protected FluidFilterData doCoverThingsImpl(
            byte aSide,
            byte aInputRedstone,
            int aCoverID,
            FluidFilterData aCoverVariable,
            ICoverable aTileEntity,
            long aTimer) {
        return aCoverVariable;
    }

    public String getFilterMode(int aFilterMode) {
        switch (aFilterMode) {
            case FILTER_INPUT_DENY_OUTPUT:
                return GT_Utility.trans("043", "Filter input, Deny output");
            case INVERT_INPUT_DENY_OUTPUT:
                return GT_Utility.trans("044", "Invert input, Deny output");
            case FILTER_INPUT_ANY_OUTPUT:
                return GT_Utility.trans("045", "Filter input, Permit any output");
            case INVERT_INPUT_ANY_OUTPUT:
                return GT_Utility.trans("046", "Invert input, Permit any output");
            case DENY_INPUT_FILTER_OUTPUT:
                return GT_Utility.trans("307", "Deny input, Filter output");
            case DENY_INPUT_INVERT_OUTPUT:
                return GT_Utility.trans("308", "Deny input, Invert output");
            case ANY_INPUT_FILTER_OUTPUT:
                return GT_Utility.trans("309", "Permit any input, Filter output");
            case ANY_INPUT_INVERT_OUTPUT:
                return GT_Utility.trans("310", "Permit any input, Invert output");
            default:
                return ("UNKNOWN");
        }
    }

    @Override
    protected FluidFilterData onCoverScrewdriverClickImpl(
            byte aSide,
            int aCoverID,
            FluidFilterData aCoverVariable,
            ICoverable aTileEntity,
            EntityPlayer aPlayer,
            float aX,
            float aY,
            float aZ) {
        aCoverVariable.mFilterMode = (aCoverVariable.mFilterMode + (aPlayer.isSneaking() ? -1 : 1)) % 8;
        if (aCoverVariable.mFilterMode < 0) {
            aCoverVariable.mFilterMode = 7;
        }

        GT_Utility.sendChatToPlayer(aPlayer, getFilterMode(aCoverVariable.mFilterMode));

        return aCoverVariable;
    }

    @Override
    protected boolean onCoverRightClickImpl(
            byte aSide,
            int aCoverID,
            FluidFilterData aCoverVariable,
            ICoverable aTileEntity,
            EntityPlayer aPlayer,
            float aX,
            float aY,
            float aZ) {
        if (((aX > 0.375D) && (aX < 0.625D))
                || ((aSide > 3) && ((aY > 0.375D) && (aY < 0.625D)))
                || ((aSide < 2) && ((aZ > 0.375D) && (aZ < 0.625D)))
                || (aSide == 2)
                || (aSide == 3)) {
            ItemStack tStack = aPlayer.inventory.getCurrentItem();
            if (tStack == null) return true;

            FluidStack tFluid = GT_Utility.getFluidForFilledItem(tStack, true);
            if (tFluid != null) {
                int aFluid = tFluid.getFluidID();
                aCoverVariable.mFluidID = aFluid;
                aTileEntity.setCoverDataAtSide(aSide, aCoverVariable);
                FluidStack sFluid = new FluidStack(FluidRegistry.getFluid(aFluid), 1000);
                GT_Utility.sendChatToPlayer(
                        aPlayer, GT_Utility.trans("047", "Filter Fluid: ") + sFluid.getLocalizedName());
            }
            return true;
        }
        return false;
    }

    @Override
    protected boolean letsRedstoneGoInImpl(
            byte aSide, int aCoverID, GT_Cover_Fluidfilter.FluidFilterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsRedstoneGoOutImpl(
            byte aSide, int aCoverID, GT_Cover_Fluidfilter.FluidFilterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsEnergyInImpl(
            byte aSide, int aCoverID, GT_Cover_Fluidfilter.FluidFilterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsEnergyOutImpl(
            byte aSide, int aCoverID, GT_Cover_Fluidfilter.FluidFilterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsInImpl(
            byte aSide,
            int aCoverID,
            GT_Cover_Fluidfilter.FluidFilterData aCoverVariable,
            int aSlot,
            ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsOutImpl(
            byte aSide,
            int aCoverID,
            GT_Cover_Fluidfilter.FluidFilterData aCoverVariable,
            int aSlot,
            ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsFluidInImpl(
            byte aSide, int aCoverID, FluidFilterData aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        if (aFluid == null) return true;

        int aFilterMode = aCoverVariable.mFilterMode;
        int aFilterFluid = aCoverVariable.mFluidID;

        if (aFilterMode == DENY_INPUT_FILTER_OUTPUT || aFilterMode == DENY_INPUT_INVERT_OUTPUT) return false;
        else if (aFilterMode == ANY_INPUT_FILTER_OUTPUT || aFilterMode == ANY_INPUT_INVERT_OUTPUT) return true;
        else if (aFluid.getID() == aFilterFluid)
            return aFilterMode == FILTER_INPUT_DENY_OUTPUT || aFilterMode == FILTER_INPUT_ANY_OUTPUT;
        else return aFilterMode == INVERT_INPUT_DENY_OUTPUT || aFilterMode == INVERT_INPUT_ANY_OUTPUT;
    }

    @Override
    protected boolean letsFluidOutImpl(
            byte aSide, int aCoverID, FluidFilterData aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        if (aFluid == null) return true;

        int aFilterMode = aCoverVariable.mFilterMode;
        int aFilterFluid = aCoverVariable.mFluidID;

        if (aFilterMode == FILTER_INPUT_DENY_OUTPUT || aFilterMode == INVERT_INPUT_DENY_OUTPUT) return false;
        else if (aFilterMode == FILTER_INPUT_ANY_OUTPUT || aFilterMode == INVERT_INPUT_ANY_OUTPUT) return true;
        else if (aFluid.getID() == aFilterFluid)
            return aFilterMode == DENY_INPUT_FILTER_OUTPUT || aFilterMode == ANY_INPUT_FILTER_OUTPUT;
        else return aFilterMode == DENY_INPUT_INVERT_OUTPUT || aFilterMode == ANY_INPUT_INVERT_OUTPUT;
    }

    @Override
    protected boolean alwaysLookConnectedImpl(
            byte aSide, int aCoverID, FluidFilterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected int getTickRateImpl(byte aSide, int aCoverID, FluidFilterData aCoverVariable, ICoverable aTileEntity) {
        return 0;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(GT_CoverUIBuildContext buildContext) {
        return new FluidFilterUIFactory(buildContext).createWindow();
    }

    private class FluidFilterUIFactory extends UIFactory {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public FluidFilterUIFactory(GT_CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @SuppressWarnings("PointlessArithmeticExpression")
        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            builder.widget(new CoverDataControllerWidget.CoverDataIndexedControllerWidget_ToggleButtons<>(
                                    this::getCoverData,
                                    this::setCoverData,
                                    GT_Cover_Fluidfilter.this,
                                    (id, coverData) -> !getClickable(id, coverData),
                                    (id, coverData) -> {
                                        coverData.mFilterMode = getNewFilterMode(id, coverData);
                                        return coverData;
                                    })
                            .addToggleButton(
                                    0,
                                    CoverDataFollower_ToggleButtonWidget.ofDisableable(),
                                    widget -> widget.setStaticTexture(GT_UITextures.OVERLAY_BUTTON_IMPORT)
                                            .addTooltip(GT_Utility.trans("232", "Filter Input"))
                                            .setPos(spaceX * 0, spaceY * 0))
                            .addToggleButton(
                                    1,
                                    CoverDataFollower_ToggleButtonWidget.ofDisableable(),
                                    widget -> widget.setStaticTexture(GT_UITextures.OVERLAY_BUTTON_EXPORT)
                                            .addTooltip(GT_Utility.trans("233", "Filter Output"))
                                            .setPos(spaceX * 1, spaceY * 0))
                            .addToggleButton(
                                    2,
                                    CoverDataFollower_ToggleButtonWidget.ofDisableable(),
                                    widget -> widget.setStaticTexture(GT_UITextures.OVERLAY_BUTTON_BLOCK_INPUT)
                                            .addTooltip(GT_Utility.trans("234", "Block Output"))
                                            .setPos(spaceX * 0, spaceY * 2))
                            .addToggleButton(
                                    3,
                                    CoverDataFollower_ToggleButtonWidget.ofDisableable(),
                                    widget -> widget.setStaticTexture(GT_UITextures.OVERLAY_BUTTON_ALLOW_INPUT)
                                            .addTooltip(GT_Utility.trans("235", "Allow Output"))
                                            .setPos(spaceX * 1, spaceY * 2))
                            .addToggleButton(
                                    4,
                                    CoverDataFollower_ToggleButtonWidget.ofDisableable(),
                                    widget -> widget.setStaticTexture(GT_UITextures.OVERLAY_BUTTON_WHITELIST)
                                            .addTooltip(GT_Utility.trans("236", "Whitelist Fluid"))
                                            .setPos(spaceX * 0, spaceY * 1))
                            .addToggleButton(
                                    5,
                                    CoverDataFollower_ToggleButtonWidget.ofDisableable(),
                                    widget -> widget.setStaticTexture(GT_UITextures.OVERLAY_BUTTON_BLACKLIST)
                                            .addTooltip(GT_Utility.trans("237", "Blacklist Fluid"))
                                            .setPos(spaceX * 1, spaceY * 1))
                            .addFollower(
                                    new CoverDataFollower_SlotWidget<FluidFilterData>(new ItemStackHandler(), 0, true) {
                                        @Override
                                        protected void putClickedStack(ItemStack stack, int mouseButton) {
                                            if (stack != null
                                                    && GT_Utility.getFluidFromContainerOrFluidDisplay(stack) == null)
                                                return;
                                            super.putClickedStack(
                                                    GT_Utility.getFluidDisplayStack(
                                                            GT_Utility.getFluidFromContainerOrFluidDisplay(stack),
                                                            false),
                                                    mouseButton);
                                        }
                                    },
                                    this::getFluidDisplayItem,
                                    (coverData, stack) -> {
                                        if (stack == null) {
                                            coverData.mFluidID = -1;
                                        } else {
                                            FluidStack fluid = GT_Utility.getFluidFromDisplayStack(stack);
                                            if (fluid != null && fluid.getFluid() != null) {
                                                coverData.mFluidID =
                                                        fluid.getFluid().getID();
                                            }
                                        }
                                        return coverData;
                                    },
                                    widget -> widget.setBackground(GT_UITextures.SLOT_DARK_GRAY)
                                            .setPos(0, spaceY * 3 + 2))
                            .setPos(startX, startY))
                    .widget(new TextWidget(GT_Utility.trans("238", "Filter Direction"))
                            .setDefaultColor(COLOR_TEXT_GRAY.get())
                            .setPos(startX + spaceX * 2, 3 + startY + spaceY * 0))
                    .widget(new TextWidget(GT_Utility.trans("239", "Filter Type"))
                            .setDefaultColor(COLOR_TEXT_GRAY.get())
                            .setPos(startX + spaceX * 2, 3 + startY + spaceY * 1))
                    .widget(new TextWidget(GT_Utility.trans("240", "Block Flow"))
                            .setDefaultColor(COLOR_TEXT_GRAY.get())
                            .setPos(startX + spaceX * 2, 3 + startY + spaceY * 2))
                    .widget(TextWidget.dynamicString(() -> {
                                if (getCoverData() != null) {
                                    ItemStack fluidDisplay = getFluidDisplayItem(getCoverData());
                                    if (fluidDisplay != null) {
                                        return fluidDisplay.getDisplayName();
                                    }
                                }
                                return GT_Utility.trans("315", "Filter Empty");
                            })
                            .setSynced(false)
                            .setDefaultColor(COLOR_TITLE.get())
                            .setPos(startX + spaceX + 3, 4 + startY + spaceY * 3));
        }

        private int getNewFilterMode(int id, FluidFilterData coverVariable) {
            switch (id) {
                case 0:
                    return (coverVariable.mFilterMode & 0x3);
                case 1:
                    return (coverVariable.mFilterMode | 0x4);
                case 2:
                    return (coverVariable.mFilterMode & 0x5);
                case 3:
                    return (coverVariable.mFilterMode | 0x2);
                case 4:
                    return (coverVariable.mFilterMode & 0x6);
                case 5:
                    return (coverVariable.mFilterMode | 0x1);
            }
            return coverVariable.mFilterMode;
        }

        private boolean getClickable(int id, FluidFilterData coverVariable) {
            switch (id) {
                case 0:
                case 1:
                    return (coverVariable.mFilterMode >> 2 & 0x1) != (id & 0x1);
                case 2:
                case 3:
                    return (coverVariable.mFilterMode >> 1 & 0x1) != (id & 0x1);
                case 4:
                case 5:
                    return (coverVariable.mFilterMode & 0x1) != (id & 0x1);
            }
            return false;
        }

        private ItemStack getFluidDisplayItem(FluidFilterData coverData) {
            Fluid fluid = FluidRegistry.getFluid(coverData.mFluidID);
            return GT_Utility.getFluidDisplayStack(fluid);
        }
    }

    public static class FluidFilterData implements ISerializableObject {
        private int mFluidID;
        private int mFilterMode;

        public FluidFilterData(int mFluidID, int mFilterMode) {
            this.mFluidID = mFluidID;
            this.mFilterMode = mFilterMode;
        }

        @Override
        @Nonnull
        public ISerializableObject copy() {
            return new FluidFilterData(mFluidID, mFilterMode);
        }

        @Override
        @Nonnull
        public NBTBase saveDataToNBT() {
            NBTTagCompound tNBT = new NBTTagCompound();
            tNBT.setInteger("mFilterMode", mFilterMode);
            if (mFluidID >= 0)
                tNBT.setString("mFluid", FluidRegistry.getFluid(mFluidID).getName());
            return tNBT;
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            aBuf.writeByte(mFilterMode).writeInt(mFluidID);
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            if (aNBT instanceof NBTTagCompound) {
                NBTTagCompound tNBT = (NBTTagCompound) aNBT;
                mFilterMode = tNBT.getInteger("mFilterMode");
                if (tNBT.hasKey("mFluid", NBT.TAG_STRING))
                    mFluidID = FluidRegistry.getFluidID(tNBT.getString("mFluid"));
                else mFluidID = -1;
            }
        }

        @Override
        @Nonnull
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, EntityPlayerMP aPlayer) {
            mFilterMode = aBuf.readByte();
            mFluidID = aBuf.readInt();
            return this;
        }
    }
}
