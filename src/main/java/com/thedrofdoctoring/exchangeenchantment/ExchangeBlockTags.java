package com.thedrofdoctoring.exchangeenchantment;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ExchangeBlockTags {
    //Any blocks in this tag will not be exchanged when broken
    public static final TagKey<Block> noExchangeTag = TagKey.create(Registries.BLOCK, new ResourceLocation("exchangeenchantment", "no_exchanging_tag"));
}
