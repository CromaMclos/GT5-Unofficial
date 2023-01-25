package gregtech.api.multitileentity.multiblock.casing;

import gregtech.api.multitileentity.interfaces.IMultiBlockController;
import gregtech.api.multitileentity.multiblock.base.MultiBlockPart;
import net.minecraft.nbt.NBTTagCompound;

public abstract class AdvancedCasing extends MultiBlockPart {

    protected int mTier = 0;

    @Override
    public int getPartTier() {
        return mTier;
    }
    @Override
    public void setTarget(IMultiBlockController aTarget, int aAllowedModes) {
        super.setTarget(aTarget, aAllowedModes);
        if (mTarget != null) customWork(mTarget);
    }

    @Override
    public void readMultiTileNBT(NBTTagCompound aNBT) {
        super.readMultiTileNBT(aNBT);
        mTier = aNBT.getInteger("mTier");
    }

    @Override
    public void writeMultiTileNBT(NBTTagCompound aNBT) {
        super.writeMultiTileNBT(aNBT);
        aNBT.setInteger("mTier", mTier);
    }

    protected abstract void customWork(IMultiBlockController aTarget);
}
