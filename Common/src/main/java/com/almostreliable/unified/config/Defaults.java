package com.almostreliable.unified.config;

import com.almostreliable.unified.Platform;

import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public final class Defaults {
    public static final List<String> MOD_PRIORITIES = List.of(
            "minecraft",
            "kubejs",
            "crafttweaker",
            "create",
            "thermal",
            "immersiveengineering",
            "mekanism");
    public static final List<String> STONE_STRATA = List.of("stone",
            "nether",
            "deepslate",
            "granite",
            "diorite",
            "andesite");
    public static final List<String> MATERIALS = List.of("aeternium",
            "aluminum",
            "amber",
            "apatite",
            "bitumen",
            "brass",
            "bronze",
            "charcoal",
            "chrome",
            "cinnabar",
            "coal",
            "coal_coke",
            "cobalt",
            "constantan",
            "copper",
            "diamond",
            "electrum",
            "elementium",
            "emerald",
            "enderium",
            "fluorite",
            "gold",
            "graphite",
            "invar",
            "iridium",
            "iron",
            "lapis",
            "lead",
            "lumium",
            "mithril",
            "netherite",
            "nickel",
            "obsidian",
            "osmium",
            "peridot",
            "platinum",
            "potassium_nitrate",
            "ruby",
            "sapphire",
            "signalum",
            "silver",
            "steel",
            "sulfur",
            "tin",
            "tungsten",
            "uranium",
            "zinc"
    );
    public static final List<String> IGNORED_RECIPE_TYPES = List.of("cucumber:shaped_tag");

    private Defaults() {}

    public static List<String> getTags(Platform platform) {
        return switch (platform) {
            case FORGE -> List.of(
                    "forge:nuggets/{material}",
                    "forge:dusts/{material}",
                    "forge:gears/{material}",
                    "forge:gems/{material}",
                    "forge:ingots/{material}",
                    "forge:raw_materials/{material}",
                    "forge:ores/{material}",
                    "forge:plates/{material}",
                    "forge:rods/{material}",
                    "forge:storage_blocks/{material}",
					"forge:storage_blocks/raw_{material}"
            );
            case FABRIC -> List.of(
                    "c:nuggets/{material}",
                    "c:dusts/{material}",
                    "c:gears/{material}",
                    "c:gems/{material}",
                    "c:ingots/{material}",
                    "c:raw_materials/{material}",
                    "c:ores/{material}",
                    "c:plates/{material}",
                    "c:rods/{material}",
                    "c:storage_blocks/{material}",
                    // Modder's just can't decide
                    "c:{material}_nuggets",
                    "c:{material}_dusts",
                    "c:{material}_gears",
                    "c:{material}_gems",
                    "c:{material}_ingots",
                    "c:{material}_raw_materials",
                    "c:{material}_ores",
                    "c:{material}_plates",
                    "c:{material}_rods",
                    "c:{material}_storage_blocks"
            );
        };
    }
}
