package gregtech.api.util;

import mcp.mobius.waila.api.SpecialChars;

public abstract class GT_Waila {

    public static final int COLOR_ENERGY = 0xFFEEE600;
    public static final int COLOR_PROGRESS = 0xFF4CBB17;
    public static final int COLOR_PROGRESS_BORDER = 0xFF555555;
    public static final int COLOR_ITEMS_BORDER = 0xff00ffff;
    public static final int COLOR_STANDARD_GROUP_BORDER = 0xff787878;

    public static String getMachineProgressString(boolean isActive, int maxProgresstime, int progresstime) {
        return getMachineProgressString(isActive, (long) maxProgresstime, (long) progresstime);
    }

    public static String getMachineProgressString(boolean isActive, long maxProgresstime, long progresstime) {

        if (!isActive) return "Idle";

        StringBuilder ret = new StringBuilder("In progress: ")
            .append(String.format("%,.2f", (double) progresstime / 20))
            .append("s / ")
            .append(String.format("%,.2f", (double) maxProgresstime / 20))
            .append("s (")
            .append(GT_Utility.formatNumbers((Math.round((double) progresstime / maxProgresstime * 1000) / 10.0)))
            .append("%)");

        return ret.toString();
    }

    public static String getCompactMachineProgressString(boolean isActive, long maxProgresstime, long progresstime) {

        if (!isActive) return SpecialChars.YELLOW + "Idle";

        StringBuilder ret = new StringBuilder().append(String.format("%,.2f", (double) progresstime / 20))
            .append("s / ")
            .append(String.format("%,.2f", (double) maxProgresstime / 20))
            .append("s (")
            .append(GT_Utility.formatNumbers((Math.round((double) progresstime / maxProgresstime * 1000) / 10.0)))
            .append("%)");

        return ret.toString();
    }
}
