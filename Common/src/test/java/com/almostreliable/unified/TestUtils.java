package com.almostreliable.unified;

import com.almostreliable.unified.api.StoneStrataHandler;
import com.almostreliable.unified.config.Defaults;
import com.almostreliable.unified.config.UnifyConfig;
import com.almostreliable.unified.recipe.RecipeTransformer;
import com.almostreliable.unified.recipe.unifier.RecipeHandlerFactory;
import com.almostreliable.unified.utils.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public final class TestUtils {

    public static final String TEST_MOD_1 = "test_mod_1";
    public static final String TEST_MOD_2 = "test_mod_2";
    public static final String TEST_MOD_3 = "test_mod_3";
    public static final String TEST_MOD_4 = "test_mod_4";
    public static final String TEST_MOD_5 = "test_mod_5";

    public static final List<String> TEST_MOD_PRIORITIES = List.of(
            TEST_MOD_1,
            TEST_MOD_2,
            TEST_MOD_3,
            TEST_MOD_4,
            TEST_MOD_5
    );

    public static final UnifyConfig DEFAULT_UNIFY_CONFIG = new UnifyConfig(
            Defaults.STONE_STRATA,
            Defaults.MATERIALS,
            Defaults.getTags(Platform.FORGE),
            TEST_MOD_PRIORITIES,
            new HashMap<>(),
            new HashMap<>(),
            new HashSet<>(),
            new HashSet<>(),
            new HashSet<>(),
            new HashSet<>(),
            true
    );

    public static final JsonCompare.CompareSettings DEFAULT_COMPARE_SETTINGS = getDefaultCompareSettings();
    public static final JsonCompare.CompareSettings DEFAULT_SHAPED_COMPARE_SETTINGS = getDefaultShapedCompareSettings();

    private TestUtils() {}

    public static JsonCompare.CompareSettings getDefaultCompareSettings() {
        return Defaults.getDefaultDuplicateRules(Platform.FORGE);
    }

    public static JsonCompare.CompareSettings getDefaultShapedCompareSettings() {
        return Defaults.getDefaultDuplicateOverrides(Platform.FORGE).get(new ResourceLocation("crafting_shaped"));
    }

    public static final ResourceKey<Registry<Item>> FAKE_ITEM_REGISTRY = FakeResourceKeyRegistry.create("item");
    public static final UnifyTag<Item> BRONZE_ORES_TAG = tag("forge:ores/bronze");
    public static final UnifyTag<Item> INVAR_ORES_TAG = tag("forge:ores/invar");
    public static final UnifyTag<Item> TIN_ORES_TAG = tag("forge:ores/tin");
    public static final UnifyTag<Item> SILVER_ORES_TAG = tag("forge:ores/silver");
    public static final List<UnifyTag<Item>> TEST_ALLOWED_TAGS = List.of(
            BRONZE_ORES_TAG,
            INVAR_ORES_TAG,
            TIN_ORES_TAG,
            SILVER_ORES_TAG
    );

    /**
     * ResourceKey is null because otherwise tests can't run because Minecraft is not bootstrapped ...
     *
     * @param name the name of the tag
     * @return a TagKey for the given name
     */
    public static UnifyTag<Item> tag(String name) {
        return UnifyTag.item(new ResourceLocation(name));
    }

    public static ResourceLocation mod1RL(String name) {
        return new ResourceLocation(TEST_MOD_1, name);
    }

    public static ResourceLocation mod2RL(String name) {
        return new ResourceLocation(TEST_MOD_2, name);
    }

    public static ResourceLocation mod3RL(String name) {
        return new ResourceLocation(TEST_MOD_3, name);
    }

    public static ResourceLocation mod4RL(String name) {
        return new ResourceLocation(TEST_MOD_4, name);
    }

    public static ResourceLocation mod5RL(String name) {
        return new ResourceLocation(TEST_MOD_5, name);
    }

    public static StoneStrataHandler createTestStrataHandler() {
        return StoneStrataHandler.create(
                List.of(),
                Set.of(),
                TagMap.create(Set.of())
        );
    }

    public static RecipeTransformer basicTransformer(Consumer<RecipeHandlerFactory> consumer) {
        TagOwnerships tagOwnerships = new TagOwnerships(
                DEFAULT_UNIFY_CONFIG.bakeTags(),
                DEFAULT_UNIFY_CONFIG.getTagOwnerships()
        );
        ReplacementMap map = new ReplacementMap(
                DEFAULT_UNIFY_CONFIG,
                TagMapTests.testTagMap(),
                createTestStrataHandler(),
                tagOwnerships
        );
        RecipeHandlerFactory factory = new RecipeHandlerFactory();
        consumer.accept(factory);
        return new RecipeTransformer(factory, map, DEFAULT_UNIFY_CONFIG, null);
    }

    public static JsonObject json(String json) {
        return new Gson().fromJson(json, JsonObject.class);
    }

    public static JsonObject json(String json, Consumer<JsonObject> consumer) {
        Gson gson = new Gson();
        JsonObject obj = gson.fromJson(json, JsonObject.class);
        consumer.accept(obj);
        return obj;
    }

    public static final class Recipes {

        public static final String SMELTING = """
                {
                  "type": "minecraft:smelting",
                  "group": "coal",
                  "ingredient": {
                    "item": "minecraft:coal_ore"
                  },
                  "result": "minecraft:coal",
                  "experience": 0.1,
                  "cookingtime": 200
                }
                """;
        public static final String SHAPED_NO_MATCH_1 = """
                {
                  "type": "minecraft:crafting_shaped",
                  "pattern": [
                    "iii",
                    "ici",
                    "iii"
                  ],
                  "key": {
                    "i": {
                      "tag": "forge:raw_materials/iron"
                    },
                    "k": {
                      "item": "minecraft:carrot"
                    }
                  },
                  "result": "minecraft:iron_ingot"
                }
                """;
        public static final String SHAPED_NO_MATCH_2 = """
                {
                  "type": "minecraft:crafting_shaped",
                  "pattern": [
                    "iii",
                    "ici",
                    "iii"
                  ],
                  "key": {
                    "i": {
                      "tag": "forge:raw_materials/iron"
                    },
                    "k": {
                      "item": "minecraft:pumpkin"
                    }
                  },
                  "result": "minecraft:iron_ingot"
                }
                """;
        public static final String SHAPED_SPECIAL_MATCH_1 = """
                {
                  "type": "minecraft:crafting_shaped",
                  "pattern": [
                    "iii",
                    "iii",
                    "iii"
                  ],
                  "key": {
                    "i": {
                      "tag": "forge:raw_materials/iron"
                    }
                  },
                  "result": "minecraft:iron_ingot"
                }
                """;
        public static final String SHAPED_SPECIAL_MATCH_2 = """
                {
                  "type": "minecraft:crafting_shaped",
                  "pattern": [
                    "iii",
                    "iki",
                    "iii"
                  ],
                  "key": {
                    "i": {
                      "tag": "forge:raw_materials/iron"
                    },
                    "k": {
                      "tag": "forge:raw_materials/iron"
                    }
                  },
                  "result": "minecraft:iron_ingot"
                }
                """;
        public static final String SHAPED_SANITIZE_1 = """
                {
                  "type": "minecraft:crafting_shaped",
                  "pattern": [
                    "iii",
                    "iii",
                    "iii"
                  ],
                  "key": {
                    "i": {
                      "tag": "forge:raw_materials/iron"
                    }
                  },
                  "result": "minecraft:iron_ingot"
                }
                """;
        public static final String SHAPED_SANITIZE_2 = """
                {
                  "type": "minecraft:crafting_shaped",
                  "pattern": [
                    "iii",
                    "iii",
                    "iii"
                  ],
                  "key": {
                    "i": {
                      "tag": "forge:raw_materials/iron"
                    }
                  },
                  "result": {
                    "item": "minecraft:iron_ingot",
                    "count": 1
                  }
                }
                """;
        public static final String CRUSHING_NESTED_SANITIZE_1 = """
                {
                  "type": "create:crushing",
                  "ingredients": [{ "tag": "forge:raw_materials/lead" }],
                  "processingTime": 400,
                  "results": [
                    { "item": "emendatusenigmatica:crushed_lead_ore" },
                    { "chance": 0.75, "item": "create:experience_nugget" }
                  ]
                }
                """;
        public static final String CRUSHING_NESTED_SANITIZE_2 = """
                {
                  "type": "create:crushing",
                  "ingredients": [{ "tag": "forge:raw_materials/lead" }],
                  "processingTime": 400,
                  "results": [
                    { "count": 1, "item": "emendatusenigmatica:crushed_lead_ore" },
                    { "chance": 0.75, "count": 1, "item": "create:experience_nugget" }
                  ]
                }
                """;

        private Recipes() {}
    }
}
