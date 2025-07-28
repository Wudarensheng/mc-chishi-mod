
/*
 *    MCreator note: This file will be REGENERATED on each build.
 */
package net.mcreator.chishimod.init;

import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.DeferredRegister;

import net.minecraft.world.item.enchantment.Enchantment;

import net.mcreator.chishimod.enchantment.FearEnchantmentEnchantment;
import net.mcreator.chishimod.ChishimodMod;

public class ChishimodModEnchantments {
	public static final DeferredRegister<Enchantment> REGISTRY = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, ChishimodMod.MODID);
	public static final RegistryObject<Enchantment> FEAR_ENCHANTMENT = REGISTRY.register("fear_enchantment", () -> new FearEnchantmentEnchantment());
}
