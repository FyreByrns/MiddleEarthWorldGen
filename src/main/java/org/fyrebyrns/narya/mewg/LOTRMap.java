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
    public static int BLOCKS_PER_MAP_CELL = 20;
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
        int bpms = BLOCKS_PER_MAP_CELL;
        double dbpms = (double)bpms;

        // the map cell this block falls within
        MapPosition position = getMapPos(x, z);
        // block position sub-map cell
        double subCellX = (x % bpms) + 0.5;
        double subCellZ = (z % bpms) + 0.5;

        // this map cell
        ResourceKey<Biome> cell = getBiome(position);
        int elevation = getElevation(cell);
        // .. distance to center
        double centerX = (dbpms / 2.0) + 0.5;
        double centerZ = (dbpms / 2.0) + 0.5;
        double distToCenter = distance(centerX, centerZ, subCellX, subCellZ) / dbpms;
        double idist = 1.0 - distToCenter;

        // neighbouring map cells
        double elevationNN = getElevation(getBiome(position.north()       ));
        double elevationSS = getElevation(getBiome(position.south()       ));
        double elevationWW = getElevation(getBiome(position.west()        ));
        double elevationEE = getElevation(getBiome(position.east()        ));
        double elevationNW = getElevation(getBiome(position.north().west()));
        double elevationNE = getElevation(getBiome(position.north().east()));
        double elevationSW = getElevation(getBiome(position.south().west()));
        double elevationSE = getElevation(getBiome(position.south().east()));

        double nnCenterZ = centerZ - bpms;
        double ssCenterZ = centerZ + bpms;
        double wwCenterX = centerX - bpms;
        double eeCenterX = centerX + bpms;

        // per-block smoothing
        // .. *ness values - how close the given block is to the specified edge.
        double nnness = 1.0 - (distance(subCellX, subCellZ, centerX, nnCenterZ) / (dbpms * sqrt(2)));
        double ssness = 1.0 - (distance(subCellX, subCellZ, centerX, ssCenterZ) / (dbpms * sqrt(2)));
        double wwness = 1.0 - (distance(subCellX, subCellZ, wwCenterX, centerZ) / (dbpms * sqrt(2)));
        double eeness = 1.0 - (distance(subCellX, subCellZ, eeCenterX, centerZ) / (dbpms * sqrt(2)));
        double nwness = 1.0 - (distance(subCellX, subCellZ, wwCenterX, nnCenterZ) / (dbpms * sqrt(2)));
        double neness = 1.0 - (distance(subCellX, subCellZ, eeCenterX, nnCenterZ) / (dbpms * sqrt(2)));
        double swness = 1.0 - (distance(subCellX, subCellZ, wwCenterX, ssCenterZ) / (dbpms * sqrt(2)));
        double seness = 1.0 - (distance(subCellX, subCellZ, eeCenterX, ssCenterZ) / (dbpms * sqrt(2)));
        if(nnness < 0) nnness = 0;
        if(ssness < 0) ssness = 0;
        if(wwness < 0) wwness = 0;
        if(eeness < 0) eeness = 0;
        if(nwness < 0) nwness = 0;
        if(neness < 0) neness = 0;
        if(swness < 0) swness = 0;
        if(seness < 0) seness = 0;

        double differenceNN = (elevationNN - elevation) / 2.0;
        double differenceSS = (elevationSS - elevation) / 2.0;
        double differenceWW = (elevationWW - elevation) / 2.0;
        double differenceEE = (elevationEE - elevation) / 2.0;
        double differenceNW = (elevationNW - elevation) / 2.0;
        double differenceNE = (elevationNE - elevation) / 2.0;
        double differenceSW = (elevationSW - elevation) / 2.0;
        double differenceSE = (elevationSE - elevation) / 2.0;

        elevation += (int)(
                  + (differenceNN * nnness)
                  + (differenceSS * ssness)
                  + (differenceWW * wwness)
                  + (differenceEE * eeness)
                  + (differenceNW * nwness)
                  + (differenceNE * neness)
                  + (differenceSW * swness)
                  + (differenceSE * seness)
        );

        double stretch = 50.0;
        double magnitude = 0.03;
        double noise = map(0, 1, -0.2, 1.3,
                noise2(1, x / stretch, z / stretch) * magnitude);
        noise = 0; // disable noise for testing overall shape
        elevation *= 1.0 + noise;

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

