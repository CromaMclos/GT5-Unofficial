package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.ModIDs.*;
import static gregtech.api.enums.ModIDs.ThaumicTinkerer;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.api.util.GT_RecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GT_RecipeConstants.FUEL_VALUE;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_RecipeConstants;

public class FuelRecipes implements Runnable {

    @Override
    public void run() {
        if (!GregTech_API.mIC2Classic) {
            GT_Values.RA.stdBuilder().itemInputs(GT_ModHandler.getIC2Item("biogasCell", 1L)).noItemOutputs()
                    .noFluidInputs().noFluidOutputs().metadata(FUEL_VALUE, 40).metadata(FUEL_TYPE, 1)
                    .addTo(GT_RecipeConstants.Fuel);

        }

        GT_Values.RA.stdBuilder().itemInputs(new ItemStack(Items.golden_apple, 1, 1))
                .itemOutputs(new ItemStack(Items.apple, 1)).noFluidInputs().noFluidOutputs().metadata(FUEL_VALUE, 6400)
                .metadata(FUEL_TYPE, 5).addTo(GT_RecipeConstants.Fuel);

        GT_Values.RA.stdBuilder().itemInputs(getModItem(Thaumcraft.modID, "ItemShard", 1L, 6)).noItemOutputs()
                .noFluidInputs().noFluidOutputs().metadata(FUEL_VALUE, 720).metadata(FUEL_TYPE, 5)
                .addTo(GT_RecipeConstants.Fuel);

        GT_Values.RA.stdBuilder().itemInputs(getModItem(ForbiddenMagic.modID, "GluttonyShard", 1L)).noItemOutputs()
                .noFluidInputs().noFluidOutputs().metadata(FUEL_VALUE, 720).metadata(FUEL_TYPE, 5)
                .addTo(GT_RecipeConstants.Fuel);

        GT_Values.RA.stdBuilder().itemInputs(getModItem(ForbiddenMagic.modID, "FMResource", 1L, 3)).noItemOutputs()
                .noFluidInputs().noFluidOutputs().metadata(FUEL_VALUE, 720).metadata(FUEL_TYPE, 5)
                .addTo(GT_RecipeConstants.Fuel);

        GT_Values.RA.stdBuilder().itemInputs(getModItem(ForbiddenMagic.modID, "NetherShard", 1L)).noItemOutputs()
                .noFluidInputs().noFluidOutputs().metadata(FUEL_VALUE, 720).metadata(FUEL_TYPE, 5)
                .addTo(GT_RecipeConstants.Fuel);

        GT_Values.RA.stdBuilder().itemInputs(getModItem(ForbiddenMagic.modID, "NetherShard", 1L, 1)).noItemOutputs()
                .noFluidInputs().noFluidOutputs().metadata(FUEL_VALUE, 720).metadata(FUEL_TYPE, 5)
                .addTo(GT_RecipeConstants.Fuel);

        GT_Values.RA.stdBuilder().itemInputs(getModItem(ForbiddenMagic.modID, "NetherShard", 1L, 2)).noItemOutputs()
                .noFluidInputs().noFluidOutputs().metadata(FUEL_VALUE, 720).metadata(FUEL_TYPE, 5)
                .addTo(GT_RecipeConstants.Fuel);

        GT_Values.RA.stdBuilder().itemInputs(getModItem(ForbiddenMagic.modID, "NetherShard", 1L, 3)).noItemOutputs()
                .noFluidInputs().noFluidOutputs().metadata(FUEL_VALUE, 720).metadata(FUEL_TYPE, 5)
                .addTo(GT_RecipeConstants.Fuel);

        GT_Values.RA.stdBuilder().itemInputs(getModItem(ForbiddenMagic.modID, "NetherShard", 1L, 4)).noItemOutputs()
                .noFluidInputs().noFluidOutputs().metadata(FUEL_VALUE, 720).metadata(FUEL_TYPE, 5)
                .addTo(GT_RecipeConstants.Fuel);

        GT_Values.RA.stdBuilder().itemInputs(getModItem(ForbiddenMagic.modID, "NetherShard", 1L, 5)).noItemOutputs()
                .noFluidInputs().noFluidOutputs().metadata(FUEL_VALUE, 720).metadata(FUEL_TYPE, 5)
                .addTo(GT_RecipeConstants.Fuel);

        GT_Values.RA.stdBuilder().itemInputs(getModItem(ForbiddenMagic.modID, "NetherShard", 1L, 6)).noItemOutputs()
                .noFluidInputs().noFluidOutputs().metadata(FUEL_VALUE, 720).metadata(FUEL_TYPE, 5)
                .addTo(GT_RecipeConstants.Fuel);

        GT_Values.RA.stdBuilder().itemInputs(getModItem(TaintedMagic.modID, "WarpedShard", 1L)).noItemOutputs()
                .noFluidInputs().noFluidOutputs().metadata(FUEL_VALUE, 720).metadata(FUEL_TYPE, 5)
                .addTo(GT_RecipeConstants.Fuel);

        GT_Values.RA.stdBuilder().itemInputs(getModItem(TaintedMagic.modID, "FluxShard", 1L)).noItemOutputs()
                .noFluidInputs().noFluidOutputs().metadata(FUEL_VALUE, 720).metadata(FUEL_TYPE, 5)
                .addTo(GT_RecipeConstants.Fuel);

        GT_Values.RA.stdBuilder().itemInputs(getModItem(TaintedMagic.modID, "EldritchShard", 1L)).noItemOutputs()
                .noFluidInputs().noFluidOutputs().metadata(FUEL_VALUE, 720).metadata(FUEL_TYPE, 5)
                .addTo(GT_RecipeConstants.Fuel);

        GT_Values.RA.stdBuilder().itemInputs(getModItem(ThaumicTinkerer.modID, "kamiResource", 1L, 6)).noItemOutputs()
                .noFluidInputs().noFluidOutputs().metadata(FUEL_VALUE, 720).metadata(FUEL_TYPE, 5)
                .addTo(GT_RecipeConstants.Fuel);

        GT_Values.RA.stdBuilder().itemInputs(getModItem(ThaumicTinkerer.modID, "kamiResource", 1L, 7)).noItemOutputs()
                .noFluidInputs().noFluidOutputs().metadata(FUEL_VALUE, 720).metadata(FUEL_TYPE, 5)
                .addTo(GT_RecipeConstants.Fuel);

    }
}
