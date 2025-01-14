package com.almostreliable.unified.utils;

import com.almostreliable.unified.TestUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TagMapTests {

    public static TagMap testTagMap() {
        TagMap tagMap = new TagMap();
        UnifyTag<Item> bronzeOreTag = UnifyTag.item(new ResourceLocation("forge:ores/bronze"));
        UnifyTag<Item> invarOreTag = UnifyTag.item(new ResourceLocation("forge:ores/invar"));
        UnifyTag<Item> tinOreTag = UnifyTag.item(new ResourceLocation("forge:ores/tin"));
        UnifyTag<Item> silverOreTag = UnifyTag.item(new ResourceLocation("forge:ores/silver"));

        tagMap.put(bronzeOreTag, TestUtils.mod1RL("bronze_ore"));
        tagMap.put(bronzeOreTag, TestUtils.mod2RL("bronze_ore"));
        tagMap.put(bronzeOreTag, TestUtils.mod3RL("bronze_ore"));

        tagMap.put(invarOreTag, TestUtils.mod1RL("invar_ore"));
        tagMap.put(invarOreTag, TestUtils.mod2RL("invar_ore"));
        tagMap.put(invarOreTag, TestUtils.mod3RL("invar_ore"));
        tagMap.put(invarOreTag, TestUtils.mod4RL("invar_ore"));

        tagMap.put(tinOreTag, TestUtils.mod3RL("tin_ore"));
        tagMap.put(tinOreTag, TestUtils.mod4RL("tin_ore"));

        tagMap.put(silverOreTag, TestUtils.mod3RL("silver_ore"));
        tagMap.put(silverOreTag, TestUtils.mod4RL("silver_ore"));
        tagMap.put(silverOreTag, TestUtils.mod5RL("silver_ore"));
        return tagMap;
    }

    @Test
    public void simpleCheck() {
        TagMap tagMap = new TagMap();
        UnifyTag<Item> bronzeOreTag = UnifyTag.item(new ResourceLocation("forge:ores/bronze"));
        tagMap.put(bronzeOreTag, TestUtils.mod1RL("bronze_ore"));
        tagMap.put(bronzeOreTag, TestUtils.mod2RL("bronze_ore"));
        tagMap.put(bronzeOreTag, TestUtils.mod3RL("bronze_ore"));
        tagMap.put(bronzeOreTag, TestUtils.mod4RL("bronze_ore"));
        tagMap.put(bronzeOreTag, TestUtils.mod5RL("bronze_ore"));

        assertEquals(tagMap.getItemsByTag(bronzeOreTag).size(), 5);
        assertEquals(tagMap.getTagsByItem(TestUtils.mod1RL("bronze_ore")).size(), 1);
        assertEquals(tagMap.getTagsByItem(TestUtils.mod2RL("bronze_ore")).size(), 1);
        assertEquals(tagMap.getTagsByItem(TestUtils.mod3RL("bronze_ore")).size(), 1);
        assertEquals(tagMap.getTagsByItem(TestUtils.mod4RL("bronze_ore")).size(), 1);
        assertEquals(tagMap.getTagsByItem(TestUtils.mod5RL("bronze_ore")).size(), 1);

        tagMap.put(UnifyTag.item(new ResourceLocation("forge:ores/invar")), TestUtils.mod1RL("invar_ore"));

        assertEquals(tagMap.tagSize(), 2);
        assertEquals(tagMap.itemSize(), 6);
    }
}
