package com.thedrofdoctoring.exchangeenchantment;

import com.mojang.logging.LogUtils;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Exchange.MODID)
public class Exchange
{
    public static final String MODID = "exchangeenchantment";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, MODID);
    public static final RegistryObject<EnchantmentExchange> EXCHANGE_ENCHANTMENT = ENCHANTMENTS.register("exchange", () -> new EnchantmentExchange(Enchantment.Rarity.VERY_RARE));
    public Exchange()
    {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);

        ENCHANTMENTS.register(modEventBus);
    }




}
