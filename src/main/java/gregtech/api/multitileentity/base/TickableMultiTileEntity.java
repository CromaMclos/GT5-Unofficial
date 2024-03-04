package gregtech.api.multitileentity.base;

import static gregtech.GT_Mod.GT_FML_LOGGER;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.objects.XSTR;
import gregtech.api.enums.GT_Values;
import gregtech.api.task.TaskHost;
import gregtech.api.task.TickableTask;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Util;

public abstract class TickableMultiTileEntity extends MultiTileEntity implements TaskHost {

    /** Variable for seeing if the Tick Function is called right now. */
    private boolean isRunningTick = false;
    /** Timer Value */
    private final long startingTime = XSTR.XSTR_INSTANCE.nextInt(20);
    private long timer = startingTime;

    private final Map<String, TickableTask<?>> tasks = new HashMap<>();

    public TickableMultiTileEntity() {
        super(true);
    }

    @Override
    public final void registerTask(@Nonnull TickableTask<?> task) {
        if (tasks.containsKey(task.getName())) {
            throw new IllegalStateException(String.format("Task with name %s is already registered", task.getName()));
        }
        tasks.put(task.getName(), task);
    }

    @Nullable
    public TickableTask<?> getTask(@Nonnull String name) {
        return tasks.get(name);
    }

    @Override
    public final void updateEntity() {
        isRunningTick = true;
        final boolean isServerSide = isServerSide();
        try {
            if (timer++ == startingTime) {
                markDirty();
                GT_Util.markChunkDirty(this);
                onFirstTick(isServerSide);
            }
            onPreTick(timer, isServerSide);
            super.updateEntity();
            /*
             * if (!isServerSide && needsUpdate) {
             * worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
             * needsUpdate = false;
             * }
             */
            onTick(timer, isServerSide);
            for (TickableTask<?> task : tasks.values()) {
                task.update(timer, isServerSide);
            }
            onPostTick(timer, isServerSide);

        } catch (Throwable e) {
            GT_FML_LOGGER.error("UpdateEntity Failed", e);
            e.printStackTrace(GT_Log.err);
            try {
                onTickFailed(timer, isServerSide);
            } catch (Throwable e2) {
                GT_FML_LOGGER.error("UpdateEntity:onTickFailed Failed", e);
            }
        }
        isRunningTick = false;
    }

    /**
     * The very first Tick happening to this TileEntity.
     */
    protected void onFirstTick(boolean isServerSide) {}

    /**
     * The first part of the Tick, before block update.
     */
    protected void onPreTick(long tick, boolean isServerSide) {}

    /**
     * The regular Tick. After block update, before sending data to client.
     */
    protected void onTick(long tick, boolean isServerSide) {}

    /**
     * The absolute last part of the Tick, after sending data to client.
     */
    protected void onPostTick(long tick, boolean isServerSide) {}

    /**
     * Gets called when there is an Exception/Error happening during one of the Tick methods.
     */
    protected void onTickFailed(long tick, boolean isServerSide) {}

    protected final void readTasksNBT(NBTTagCompound nbt) {
        if (nbt.hasKey(GT_Values.NBT.TASKS)) {
            NBTTagCompound tasksTag = nbt.getCompoundTag(GT_Values.NBT.TASKS);
            for (TickableTask<?> task : tasks.values()) {
                if (tasksTag.hasKey(task.getName())) {
                    task.readFromNBT(tasksTag.getCompoundTag(task.getName()));
                }
            }
        }
    }

    protected final void writeTasksNBT(NBTTagCompound aNBT) {
        NBTTagCompound tasksTag = new NBTTagCompound();
        for (TickableTask<?> task : tasks.values()) {
            NBTTagCompound tag = new NBTTagCompound();
            task.writeToNBT(tag);
            tasksTag.setTag(task.getName(), tag);
        }
        aNBT.setTag(GT_Values.NBT.TASKS, tasksTag);
    }
}
