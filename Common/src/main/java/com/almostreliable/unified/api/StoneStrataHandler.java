package com.almostreliable.unified.api;

import com.almostreliable.unified.AlmostUnifiedPlatform;
import com.almostreliable.unified.utils.TagMap;
import com.almostreliable.unified.utils.UnifyTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.*;
import java.util.regex.Pattern;

public class StoneStrataHandler {

    private final List<String> stoneStrata;
    private final Pattern tagMatcher;
    private final TagMap stoneStrataTagMap;

    // don't clear the caches, so they are available for the runtime and KubeJS binding
    // the runtime holding this handler is automatically yeeted on reload
    private final Map<UnifyTag<?>, Boolean> stoneStrataTagCache;
    private final Map<ResourceLocation, String> stoneStrataCache;

    private StoneStrataHandler(List<String> stoneStrata, Pattern tagMatcher, TagMap stoneStrataTagMap) {
        this.stoneStrata = createSortedStoneStrata(stoneStrata);
        this.tagMatcher = tagMatcher;
        this.stoneStrataTagMap = stoneStrataTagMap;
        this.stoneStrataTagCache = new HashMap<>();
        this.stoneStrataCache = new HashMap<>();
    }

    /**
     * Returns the stone strata list sorted from longest to shortest.
     * <p>
     * This is required to ensure that the longest strata is returned and no sub-matches happen.<br>
     * Example: "nether" and "blue_nether" would both match "nether" if the list is not sorted.
     *
     * @param stoneStrata The stone strata list to sort.
     * @return The sorted stone strata list.
     */
    private static List<String> createSortedStoneStrata(List<String> stoneStrata) {
        return stoneStrata.stream().sorted(Comparator.comparingInt(String::length).reversed()).toList();
    }

    public static StoneStrataHandler create(List<String> stoneStrataIds, Set<UnifyTag<Item>> stoneStrataTags, TagMap tagMap) {
        TagMap stoneStrataTagMap = tagMap.filtered(stoneStrataTags::contains, item -> true);
        Pattern tagMatcher = Pattern.compile(switch (AlmostUnifiedPlatform.INSTANCE.getPlatform()) {
            case FORGE -> "forge:ores/.+";
            case FABRIC -> "(c:ores/.+|c:.+_ores)";
        });
        return new StoneStrataHandler(stoneStrataIds, tagMatcher, stoneStrataTagMap);
    }

    /**
     * Returns the stone strata from the given item. Assumes that the item has a stone strata tag.
     * Use {@link #isStoneStrataTag(UnifyTag)} to ensure this requirement.
     *
     * @param item The item to get the stone strata from.
     * @return The stone strata of the item. Clean stone strata returns an empty string for later sorting as a
     * fallback variant.
     */
    public String getStoneStrata(ResourceLocation item) {
        return stoneStrataCache.computeIfAbsent(item, this::computeStoneStrata);
    }

    /**
     * Implementation logic for {@link #getStoneStrata(ResourceLocation)}.
     *
     * @param item The item to get the stone strata from.
     * @return The stone strata of the item. Clean stone strata returns an empty string for later sorting as a
     * fallback variant.
     */
    private String computeStoneStrata(ResourceLocation item) {
        String strata = stoneStrataTagMap
                .getTagsByItem(item)
                .stream()
                .findFirst()
                .map(UnifyTag::location)
                .map(ResourceLocation::toString)
                .map(s -> {
                    int i = s.lastIndexOf('/');
                    return i == -1 ? null : s.substring(i + 1);
                })
                .orElse(null);

        if (strata != null) {
            if (strata.equals("stone")) {
                return "";
            }
            return strata;
        }

        for (String stone : stoneStrata) {
            if (item.getPath().contains(stone + "_")) {
                if (stone.equals("stone")) {
                    return "";
                }
                return stone;
            }
        }

        return "";
    }

    public boolean isStoneStrataTag(UnifyTag<Item> tag) {
        return stoneStrataTagCache.computeIfAbsent(tag, t -> tagMatcher.matcher(t.location().toString()).matches());
    }
}
