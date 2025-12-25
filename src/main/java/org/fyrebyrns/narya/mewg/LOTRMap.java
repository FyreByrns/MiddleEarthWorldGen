package org.fyrebyrns.narya.mewg;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class LOTRMap {
    public static int BLOCKS_PER_MAP_CELL = 20;
    public static int ABSOLUTE_MAX_WORLD_HEIGHT = 384;

    public static int SEA_LEVEL = 40;
    public static int BASE_TERRAIN_LEVEL = 50;
    public static int BASE_HILLS_LEVEL = 60;
    public static int PEAK_HILLS_LEVEL = 110;
    public static int BASE_FOOTHILLS_LEVEL = 90;
    public static int PEAK_FOOTHILLS_LEVEL = 200;
    public static int BASE_MOUNTAINS_LEVEL = 170;
    public static int PEAK_MOUNTAINS_LEVEL = 384;

    private static boolean mapLoaded = false;

    private static BufferedImage map;
    private static BufferedImage mapBiomes;
    private static BufferedImage mapJustWater;
    private static BufferedImage mapDirectWater;
    private static BufferedImage mapNearWater;

    private static BufferedImage waterColourMap;
    private static BufferedImage forestColourMap;
    private static ArrayList<Color> waterColours = new ArrayList<>();
    private static ArrayList<Color> forestColours = new ArrayList<>();

    private static void ensureMapLoaded() {
        if(!mapLoaded) {
            try {
                map = ImageIO.read(getStream("/assets/narya/map/lotr-biome-map.png"));
                mapBiomes = ImageIO.read(getStream("/assets/narya/map/original-biome-map.png"));
                mapJustWater = ImageIO.read(getStream("/assets/narya/map/just-water.png"));
                mapDirectWater = ImageIO.read(getStream("/assets/narya/map/next-to-water.png"));
                mapNearWater = ImageIO.read(getStream("/assets/narya/map/near-water.png"));

                // generate the map of colours -> features
                waterColourMap = ImageIO.read(getStream("/assets/narya/map/water-colours.png"));
                for(int i = 0; i < waterColourMap.getWidth(); i++) {
                    waterColours.add(new Color(waterColourMap.getRGB(i, 0)));
                }
                forestColourMap = ImageIO.read(getStream("/assets/narya/map/forest-colours.png"));
                for(int i = 0; i < forestColourMap.getWidth(); i++){
                    forestColours.add(new Color(forestColourMap.getRGB(i, 0)));
                }

                mapLoaded = true;

            } catch (IOException e) {
                Mewg.LOGGER.error("exception loading map as resource: {}", e.toString());
            }
        }
    }

    // get an input stream from a path
    private static InputStream getStream(String path) {
        return LOTRMap.class.getResourceAsStream(path);
    }

    public static int getMapR(int sampleX, int sampleZ) {
        ensureMapLoaded();
        if(mapLoaded) {
            int sX = Math.max(0, Math.min(sampleX, map.getWidth()-1));
            int sY = Math.max(0, Math.min(sampleZ, map.getHeight()-1));

            Color color = new Color(map.getRGB(sX, sY));
            return color.getRed();
        }
        return 0;
    }
    public static int getMapG(int sampleX, int sampleZ) {
        ensureMapLoaded();
        if(mapLoaded) {
            int sX = Math.max(0, Math.min(sampleX, map.getWidth()-1));
            int sY = Math.max(0, Math.min(sampleZ, map.getHeight()-1));

            Color color = new Color(map.getRGB(sX, sY));
            return color.getGreen();
        }
        return 0;
    }
    public static int getMapB(int sampleX, int sampleZ) {
        ensureMapLoaded();
        if(mapLoaded) {
            int sX = Math.max(0, Math.min(sampleX, map.getWidth()-1));
            int sY = Math.max(0, Math.min(sampleZ, map.getHeight()-1));

            Color color = new Color(map.getRGB(sX, sY));
            return color.getBlue();
        }
        return 0;
    }

    public static boolean isWater(int r, int g, int b) {
        ensureMapLoaded();
        return waterColours.contains(new Color(r, g, b));
    }
    public static boolean isWater(int cellX, int cellZ) {
        return isWater(getMapR(cellX, cellZ), getMapG(cellX, cellZ), getMapB(cellX, cellZ));
    }
    public static boolean isWater(MapPosition position) {
        return isWater(position.x(), position.z());
    }

    public static boolean isForest(int r, int g, int b) {
        ensureMapLoaded();
        return forestColours.contains(new Color(r, g, b));
    }

    public static boolean hasDirectWaterAccess(int chunkX, int chunkZ) {
        ensureMapLoaded();
        if(mapLoaded) {
            int sX = Math.max(0, Math.min(chunkX, map.getWidth()));
            int sY = Math.max(0, Math.min(chunkZ, map.getHeight()));

            Color color = new Color(mapDirectWater.getRGB(sX, sY));
            return color.getRed() == 255;
        }
        return false;
    }
    public static boolean nearWater(int chunkX, int chunkZ) {
        ensureMapLoaded();
        if(mapLoaded) {
            int sX = Math.max(0, Math.min(chunkX, map.getWidth()));
            int sY = Math.max(0, Math.min(chunkZ, map.getHeight()));

            Color color = new Color(mapNearWater.getRGB(sX, sY));
            return color.getRed() == 255;
        }
        return false;
    }

    public static int getMapHeight(int x, int z) {
        ensureMapLoaded();

        // the map cell this block falls within
        MapPosition position = getMapPos(x, z);

        // this map cell
        ResourceKey<Biome> cell = getBiome(position);
        // neighbouring map cells
        ResourceKey<Biome> cellNorth = getBiome(position.north());
        ResourceKey<Biome> cellSouth = getBiome(position.south());
        ResourceKey<Biome> cellEast = getBiome(position.east());
        ResourceKey<Biome> cellWest = getBiome(position.west());

        // block position sub-map cell
        int subCellX = x % BLOCKS_PER_MAP_CELL;
        int subCellZ = z % BLOCKS_PER_MAP_CELL;
        // percentage of the way along the cell
        double percentX = (double)subCellX / (double)BLOCKS_PER_MAP_CELL;
        double percentZ = (double)subCellZ / (double)BLOCKS_PER_MAP_CELL;

        return SEA_LEVEL;
    }

    public static ResourceKey<Biome> getBiome(MapPosition position) {
        ensureMapLoaded();
        Color color = new Color(mapBiomes.getRGB(position.x(), position.z()));
        int id = color.getBlue();
        return DefaultLOTRBiomes.BiomesByID.get(id);
    }

    public static MapPosition getMapPos(int blockX, int blockZ) {
        ensureMapLoaded();
        blockX /= BLOCKS_PER_MAP_CELL;
        blockZ /= BLOCKS_PER_MAP_CELL;

        blockX = Math.max(0, Math.min(blockX, map.getWidth()-1));
        blockZ = Math.max(0, Math.min(blockZ, map.getHeight()-1));

        return new MapPosition(blockX, blockZ);
    }
}

