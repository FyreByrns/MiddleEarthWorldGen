package org.fyrebyrns.narya.mewg;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static org.fyrebyrns.narya.mewg.MathHelper.*;
import static org.fyrebyrns.narya.mewg.OpenSimplex2S.noise2;
import static org.fyrebyrns.narya.mewg.generation.Elevation.getElevation;

public class LOTRMap {
    public static int BLOCKS_PER_MAP_CELL = 200;
    public static int ABSOLUTE_MAX_WORLD_HEIGHT = 384;

    public static int MAP_WIDTH = 3200;
    public static int MAP_HEIGHT = 4000;

    public static int SEA_LEVEL = 40;
    public static int BASE_TERRAIN_LEVEL = 50;
    public static int BASE_HILLS_LEVEL = 60;
    public static int PEAK_HILLS_LEVEL = 110;
    public static int BASE_FOOTHILLS_LEVEL = 90;
    public static int PEAK_FOOTHILLS_LEVEL = 200;
    public static int BASE_MOUNTAINS_LEVEL = 170;
    public static int PEAK_MOUNTAINS_LEVEL = 384;

    public static BufferedImage originalMapColour;
    public static BufferedImage indexedOriginalBiomes;
    public static BufferedImage waterMask;

    private static ArrayList<Color> waterColours = new ArrayList<>();
    private static ArrayList<Color> forestColours = new ArrayList<>();

    static {
        try {
            originalMapColour = ImageIO.read(getStream("/assets/narya/map/original-colour-map.png"));
            indexedOriginalBiomes = ImageIO.read(getStream("/assets/narya/map/map-indexed-original-biomes.png"));
            waterMask = ImageIO.read(getStream("/assets/narya/map/water-mask.png"));

            // generate the map of colours -> features
            BufferedImage waterColourMap = ImageIO.read(getStream("/assets/narya/map/water-colours.png"));
            for(int i = 0; i < waterColourMap.getWidth(); i++) {
                waterColours.add(new Color(waterColourMap.getRGB(i, 0)));
            }
            BufferedImage forestColourMap = ImageIO.read(getStream("/assets/narya/map/forest-colours.png"));
            for(int i = 0; i < forestColourMap.getWidth(); i++){
                forestColours.add(new Color(forestColourMap.getRGB(i, 0)));
            }
        } catch (IOException e) {
            Mewg.LOGGER.error("exception loading map as resource: {}", e.toString());
        }
    }

    public static boolean isUnderWaterMask(int blockX, int blockZ) {
        MapPosition mapPos = getMapPos(blockX, blockZ);
        return isUnderWaterMask(mapPos);
    }
    public static boolean isUnderWaterMask(MapPosition position) {
        Color waterMaskColour = new Color(waterMask.getRGB(position.x(), position.z()));
        return waterMaskColour.getAlpha() > 0;
    }

    // get an input stream from a path
    private static InputStream getStream(String path) {
        return LOTRMap.class.getResourceAsStream(path);
    }

    public static int coordinateOffsetNoise(int x, int z) {
        double zxSampleOffsetStretch = (double)BLOCKS_PER_MAP_CELL / 3.0;
        double zxSampleOffsetMagnitude = (double)BLOCKS_PER_MAP_CELL / 20.0 * 3.0;
        return (int) (
                noise2(
                        2,
                        (double)x / zxSampleOffsetStretch,
                        (double)z / zxSampleOffsetStretch)
                        * zxSampleOffsetMagnitude
        );
    }

    public static int getMapHeight(int x, int z) {
        int ox = x;
        int oz = z;
        int coNoise = coordinateOffsetNoise(x, z);
        x += coNoise;
        z += coNoise;

        int offset = (BLOCKS_PER_MAP_CELL / 2);
        int bottom = z - offset;
        int top    = z + offset;
        int left   = x - offset;
        int right  = x + offset;

        BiomeOrDirectHeight lb = getBiome(getMapPos(left , bottom));
        BiomeOrDirectHeight rb = getBiome(getMapPos(right, bottom));
        BiomeOrDirectHeight lt = getBiome(getMapPos(left , top   ));
        BiomeOrDirectHeight rt = getBiome(getMapPos(right, top   ));

        float rightCells  = (x + offset) % BLOCKS_PER_MAP_CELL;
        float leftCells   =  2 * offset  - rightCells;
        float topCells    = (z + offset) % BLOCKS_PER_MAP_CELL;
        float bottomCells =  2 * offset  - topCells;
        float average = (float) ((
                        + (bottomCells) * (leftCells ) * lb.heightOrBiomeHeight()
                        + (bottomCells) * (rightCells) * rb.heightOrBiomeHeight()
                        + (topCells   ) * (leftCells ) * lt.heightOrBiomeHeight()
                        + (topCells   ) * (rightCells) * rt.heightOrBiomeHeight()
                ) / square(offset * 2));
        int elevation = (int)average;

        double stretch = 50.0;
        double magnitude = 0.05;
        double noise = noise2(1, x / stretch, z / stretch) * magnitude;
        noise += noise2(1, x / (stretch / 2.0), z / (stretch / 2.0)) * magnitude / 2.0;
//        noise = 0; // disable noise for testing overall shape
        elevation = (int)((double)elevation * (1.0 + noise));

        return elevation;
    }

    public static int getWaterHeight(int x, int z) {
        if(!isUnderWaterMask(x, z)) {
            return SEA_LEVEL;
        }

        return getBiome(getMapPos(x, z)).height;
    }
    public static int getTerrainHeight(int x, int z) {
        // TODO: Properly get the underwater terrain height from a map
        // TODO: .. for now, just make the underwater terrain height
        // TODO: .. 8 blocks deeper than the water.
        if(!isUnderWaterMask(x, z)) return getBiome(getMapPos(x, z)).height;
        return getWaterHeight(x, z) - 8;
    }

    public static BiomeOrDirectHeight getBiome(MapPosition position) {
        Color colour = new Color(indexedOriginalBiomes.getRGB(position.x(), position.z()));
        int id = colour.getBlue();
        ResourceKey<Biome> biome = DefaultLOTRBiomes.BiomesByID.get(id);
        int height = getElevation(biome);

        if(isUnderWaterMask(position)) {
            Color waterMaskColour = new Color(waterMask.getRGB(position.x(), position.z()));
            int wR = waterMaskColour.getRed();
            int wG = waterMaskColour.getGreen();
            int wB = waterMaskColour.getBlue();
            int waterTotalHeight = wR + wG + wB;

            height += waterTotalHeight;
        }

        return new BiomeOrDirectHeight(biome, height);
    }

    public static MapPosition getMapPos(int blockX, int blockZ) {
        blockX /= BLOCKS_PER_MAP_CELL;
        blockZ /= BLOCKS_PER_MAP_CELL;

        blockX = Math.max(0, Math.min(blockX, MAP_WIDTH - 1));
        blockZ = Math.max(0, Math.min(blockZ, MAP_HEIGHT - 1));

        return new MapPosition(blockX, blockZ);
    }

    public record BiomeOrDirectHeight(ResourceKey<Biome> biome, int height) {
        private static final int NO_HEIGHT = -696969;

        public boolean hasBiome() { return biome != null; }
        public boolean hasHeight() { return height != NO_HEIGHT; }

        /** Return the explicit height if set, or the base biome height. */
        public int heightOrBiomeHeight() {
            if(hasHeight()) return height;
            if(hasBiome()) return getElevation(biome);
            return 0;
        }

        public static BiomeOrDirectHeight makeBiome(ResourceKey<Biome> biome) { return new BiomeOrDirectHeight(biome, NO_HEIGHT); }
        public static BiomeOrDirectHeight makeHeight(int height) { return new BiomeOrDirectHeight(null, height); }
    }
}

