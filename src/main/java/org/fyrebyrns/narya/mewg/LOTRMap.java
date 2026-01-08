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

    private static boolean mapLoaded = false;

    private static BufferedImage originalMapColour;
    private static BufferedImage indexedOriginalBiomes;

    private static BufferedImage waterColourMap;
    private static BufferedImage forestColourMap;
    private static ArrayList<Color> waterColours = new ArrayList<>();
    private static ArrayList<Color> forestColours = new ArrayList<>();

    private static void ensureMapLoaded() {
        if(!mapLoaded) {
            try {
                originalMapColour = ImageIO.read(getStream("/assets/narya/map/original-colour-map.png"));
                indexedOriginalBiomes = ImageIO.read(getStream("/assets/narya/map/map-indexed-original-biomes.png"));

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

    public static int getMapHeight(int x, int z) {
        ensureMapLoaded();

        double zxSampleOffsetStretch = (double)BLOCKS_PER_MAP_CELL / 3.0;
        double zxSampleOffsetMagnitude = (double)BLOCKS_PER_MAP_CELL / 20.0 * 3.0;
        int zxSampleOffsetNoise = (int) (
                        noise2(
                                2,
                                (double)x / zxSampleOffsetStretch,
                                (double)z / zxSampleOffsetStretch)
                                * zxSampleOffsetMagnitude
        );

        int ox = x;
        int oz = z;
        x += zxSampleOffsetNoise;
        z += zxSampleOffsetNoise;

        int offset = (BLOCKS_PER_MAP_CELL / 2);
        int bottom = z - offset;
        int top    = z + offset;
        int left   = x - offset;
        int right  = x + offset;
        float rightCells  = (x + offset) % BLOCKS_PER_MAP_CELL;
        float leftCells   =  2 * offset  - rightCells;
        float topCells    = (z + offset) % BLOCKS_PER_MAP_CELL;
        float bottomCells =  2 * offset  - topCells;
        float average = (float) ((
                        + (bottomCells) * (leftCells ) * getElevation(getBiome(getMapPos(left , bottom)))
                        + (bottomCells) * (rightCells) * getElevation(getBiome(getMapPos(right, bottom)))
                        + (topCells   ) * (leftCells ) * getElevation(getBiome(getMapPos(left , top   )))
                        + (topCells   ) * (rightCells) * getElevation(getBiome(getMapPos(right, top   )))
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

    private static double[][] convolve(double[][] input, double[][] kernel) {
        double[][] result = input;

        for(int x = 0; x < input.length; x++) {
            for(int y = 0; y < input.length; y++) {
                double accum = 0;

                for(int kx = 0; kx < kernel.length; kx++) {
                    for(int ky = 0; ky < kernel.length; ky++) {
                        int sampleX = x + (int)map(0, kernel.length, -kernel.length/2, kernel.length/2, kx);
                        int sampleY = y + (int)map(0, kernel.length, -kernel.length/2, kernel.length/2, ky);

                        if (sampleX >= input.length) sampleX -= input.length;
                        if (sampleY >= input.length) sampleY -= input.length;
                        if (sampleX < 0)             sampleX += input.length;
                        if (sampleY < 0)             sampleY += input.length;

                        accum += input[sampleX][sampleY] * kernel[kx][ky];
                    }
                }

                result[x][y] = accum;
            }
        }

        return result;
    }
    private static double mean(double[][] input) {
        int count = 0;
        double sum = 0;

        for (double[] doubles : input) {
            for (double d : doubles) {
                count++;
                sum += d;
            }
        }

        return sum / count;
    }

    public static ResourceKey<Biome> getBiome(MapPosition position) {
        ensureMapLoaded();
        Color color = new Color(indexedOriginalBiomes.getRGB(position.x(), position.z()));
        int id = color.getBlue();
        return DefaultLOTRBiomes.BiomesByID.get(id);
    }

    public static MapPosition getMapPos(int blockX, int blockZ) {
        ensureMapLoaded();
        blockX /= BLOCKS_PER_MAP_CELL;
        blockZ /= BLOCKS_PER_MAP_CELL;

        blockX = Math.max(0, Math.min(blockX, MAP_WIDTH - 1));
        blockZ = Math.max(0, Math.min(blockZ, MAP_HEIGHT - 1));

        return new MapPosition(blockX, blockZ);
    }
}

