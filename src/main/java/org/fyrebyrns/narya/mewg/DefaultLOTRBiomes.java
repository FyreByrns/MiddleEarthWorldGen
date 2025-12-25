package org.fyrebyrns.narya.mewg;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import java.util.ArrayList;
import java.util.HashMap;

public class DefaultLOTRBiomes {
    public static HashMap<Integer, ResourceKey<Biome>> BiomesByID = new HashMap<>();
    public static HashMap<ResourceKey<Biome>, Integer> IDsByBiome = new HashMap<>();
    public static HashMap<String, ResourceKey<Biome>> BiomesByName = new HashMap<>();

    public static final ResourceKey<Biome> FORODWAITH = register(1, "forodwaith");
    public static final ResourceKey<Biome> MOUNTAINS_FORODWAITH = register(2, "mountains_forodwaith");
    public static final ResourceKey<Biome> MOUNTAINS_BLUE = register(3, "mountains_blue");
    public static final ResourceKey<Biome> FOOTHILLS_BLUE = register(4, "foothills_blue");
    public static final ResourceKey<Biome> NORTHLANDS = register(5, "northlands");
    public static final ResourceKey<Biome> ANGMAR = register(6, "angmar");
    public static final ResourceKey<Biome> MOUNTAINS_ANGMAR = register(7, "mountains_angmar");
    public static final ResourceKey<Biome> MOUNTAINS_GREY = register(8, "mountains_grey");
    public static final ResourceKey<Biome> MOUNTAINS_MISTY = register(9, "mountains_misty");
    public static final ResourceKey<Biome> FOOTHILLS_MISTY = register(10, "foothills_misty");
    public static final ResourceKey<Biome> ETTENMOORS = register(11, "ettenmoors");
    public static final ResourceKey<Biome> COLDFELLS = register(12, "coldfells");
    public static final ResourceKey<Biome> FOOTHILLS_GREY = register(13, "foothills_grey");
    public static final ResourceKey<Biome> ISLAND = register(14, "island");
    public static final ResourceKey<Biome> FOREST_NORTHERN = register(15, "forest_northern");
    public static final ResourceKey<Biome> ERIADOR = register(16, "eriador");
    public static final ResourceKey<Biome> LINDON = register(17, "lindon");
    public static final ResourceKey<Biome> FOREST_LINDON = register(18, "forest_lindon");
    public static final ResourceKey<Biome> HILLS_TOWER = register(19, "hills_tower");
    public static final ResourceKey<Biome> DOWNS_ERIADOR = register(20, "downs_eriador");
    public static final ResourceKey<Biome> MOORS_NORTH = register(21, "moors_north");
    public static final ResourceKey<Biome> THE_SHIRE = register(22, "the_shire");
    public static final ResourceKey<Biome> DOWNS_WHITE = register(23, "downs_white");
    public static final ResourceKey<Biome> FOREST_SHIRE = register(24, "forest_shire");
    public static final ResourceKey<Biome> MARSHES_SHIRE = register(25, "marshes_shire");
    public static final ResourceKey<Biome> FOREST_OLD = register(26, "forest_old");
    public static final ResourceKey<Biome> DOWNS_BARROW = register(27, "downs_barrow");
    public static final ResourceKey<Biome> BREE_LAND = register(28, "bree_land");
    public static final ResourceKey<Biome> CHETWOOD = register(29, "chetwood");
    public static final ResourceKey<Biome> MIDGEWATER = register(30, "midgewater");
    public static final ResourceKey<Biome> LONE_LANDS = register(31, "lone_lands");
    public static final ResourceKey<Biome> HILLS_WEATHER = register(32, "hills_weather");
    public static final ResourceKey<Biome> THE_ANGLE = register(33, "the_angle");
    public static final ResourceKey<Biome> TROLLSHAWS = register(34, "trollshaws");
    public static final ResourceKey<Biome> RIVENDELL = register(35, "rivendell");
    public static final ResourceKey<Biome> HILLS_RIVENDELL = register(36, "hills_rivendell");
    public static final ResourceKey<Biome> MINHIRIATH = register(37, "minhiriath");
    public static final ResourceKey<Biome> ERYN_VORN = register(38, "eryn_vorn");
    public static final ResourceKey<Biome> SWANFLEET = register(39, "swanfleet");
    public static final ResourceKey<Biome> ERESION = register(40, "eresion");
    public static final ResourceKey<Biome> ENEDWAITH = register(41, "enedwaith");
    public static final ResourceKey<Biome> DUNLAND =  register(42, "dunland");
    public static final ResourceKey<Biome> FANGORN = register(43, "fangorn");
    public static final ResourceKey<Biome> HILLS_ANDUIN = register(44, "hills_anduin");
    public static final ResourceKey<Biome> VALE_ANDUIN = register(45, "vale_anduin");
    public static final ResourceKey<Biome> FIELDS_GLADDEN = register(46, "fields_gladden");
    public static final ResourceKey<Biome> LOTHLORIEN = register(47, "lothlorien");
    public static final ResourceKey<Biome> EAVES_LOTHLORIEN = register(48, "eaves_lothlorien");
    public static final ResourceKey<Biome> FIELDS_CELEBRANT = register(49, "fields_celebrant");
    public static final ResourceKey<Biome> THE_WOLD = register(50, "the_wold");
    public static final ResourceKey<Biome> ROHAN = register(51, "rohan");
    public static final ResourceKey<Biome> BROWNLANDS = register(52, "brownlands");
    public static final ResourceKey<Biome> FOREST_NORTH_MIRKWOOD = register(53, "forest_north_mirkwood");
    public static final ResourceKey<Biome> FOREST_MIRKWOOD = register(54, "forest_mirkwood");
    public static final ResourceKey<Biome> MOUNTAINS_MIRKWOOD = register(55, "mountains_mirkwood");
    public static final ResourceKey<Biome> DOL_GULDUR = register(56, "dol_guldur");
    public static final ResourceKey<Biome> WOODLAND_REALM = register(57, "woodland_realm");
    public static final ResourceKey<Biome> HILLS_WOODLAND_REALM = register(58, "hills_woodland_realm");
    public static final ResourceKey<Biome> EAST_BIGHT = register(59, "east_bight");
    public static final ResourceKey<Biome> WILDERLAND = register(60, "wilderland");
    public static final ResourceKey<Biome> NORTHERN_WILDERLAND = register(61, "northern_wilderland");
    public static final ResourceKey<Biome> MARSHES_LONG = register(62, "marshes_long");
    public static final ResourceKey<Biome> EREBOR = register(63, "erebor");
    public static final ResourceKey<Biome> DALE = register(64, "dale");
    public static final ResourceKey<Biome> RHUN = register(65, "rhun");
    public static final ResourceKey<Biome> HILLS_IRON = register(66, "hills_iron");
    public static final ResourceKey<Biome> MOUNTAINS_RED = register(67, "mountains_red");
    public static final ResourceKey<Biome> FOOTHILLS_RED = register(68, "foothills_red");
    public static final ResourceKey<Biome> FOREST_RHUN = register(69, "forest_rhun");
    public static final ResourceKey<Biome> DORWINION = register(70, "dorwinion");
    public static final ResourceKey<Biome> ERYN_GARAN = register(71, "eryn_garan");
    public static final ResourceKey<Biome> STEPPE_RHUDEL = register(72, "steppe_rhudel");
    public static final ResourceKey<Biome> EMYN_WINON = register(73, "emyn_winon");
    public static final ResourceKey<Biome> RHUDEL = register(74, "rhudel");
    public static final ResourceKey<Biome> HILLS_RHUDEL = register(75, "hills_rhudel");
    public static final ResourceKey<Biome> ERYN_RHUNEAR = register(76, "eryn_rhunear");
    public static final ResourceKey<Biome> TOL_RHUNEAR = register(77, "tol_rhunear");
    public static final ResourceKey<Biome> DESERT_LAST = register(78, "desert_last");
    public static final ResourceKey<Biome> MOUNTAINS_WIND = register(79, "mountains_wind");
    public static final ResourceKey<Biome> FOOTHILLS_WIND = register(80, "foothills_wind");
    public static final ResourceKey<Biome> NAN_CURNIR = register(81, "nan_curnir");
    public static final ResourceKey<Biome> URUK_HIGHLANDS = register(82, "uruk_highlands");
    public static final ResourceKey<Biome> WASTELAND_FANGORN = register(83, "wasteland_fangorn");
    public static final ResourceKey<Biome> FANGORN_CLEARING = register(84, "fangorn_clearing");
    public static final ResourceKey<Biome> ADORNLAND = register(85, "adornland");
    public static final ResourceKey<Biome> DRUWAITH_IAUR = register(86, "druwaith_iaur");
    public static final ResourceKey<Biome> MOUNTAINS_WHITE = register(87, "mountains_white");
    public static final ResourceKey<Biome> FOOTHILLS_WHITE = register(88, "foothills_white");
    public static final ResourceKey<Biome> ANDRAST = register(89, "andrast");
    public static final ResourceKey<Biome> GONDOR = register(90, "gondor");
    public static final ResourceKey<Biome> PINNATH_GELIN = register(91, "pinnath_gelin");
    public static final ResourceKey<Biome> VALE_BLACKROOT = register(92, "vale_blackroot");
    public static final ResourceKey<Biome> LAMEDON = register(93, "lamedon");
    public static final ResourceKey<Biome> HILLS_LAMEDON = register(94, "hills_lamedon");
    public static final ResourceKey<Biome> FOREST_GONDOR = register(95, "forest_gondor");
    public static final ResourceKey<Biome> FOREST_ROHAN = register(96, "forest_rohan");
    public static final ResourceKey<Biome> MOUNTS_OF_ENTWASH = register(97, "mounts_of_entwash");
    public static final ResourceKey<Biome> EMIN_MUIL = register(98, "emin_muil");
    public static final ResourceKey<Biome> MARSHES_DEAD = register(99, "marshes_dead");
    public static final ResourceKey<Biome> NINDALF = register(100, "nindalf");
    public static final ResourceKey<Biome> DAGORLAND = register(101, "dagorland");
    public static final ResourceKey<Biome> WASTELAND_ITHILIEN = register(102, "wasteland_ithilien");
    public static final ResourceKey<Biome> ITHILIEN = register(103, "ithilien");
    public static final ResourceKey<Biome> HILLS_ITHILIEN = register(104, "hills_ithilien");
    public static final ResourceKey<Biome> PELENNOR_FIELDS = register(105, "pelennor_fields");
    public static final ResourceKey<Biome> IMLOTH_MELUI = register(106, "imloth_melui");
    public static final ResourceKey<Biome> LOSSARNACH = register(107, "lossarnach");
    public static final ResourceKey<Biome> LEBENNIN = register(108, "lebennin");
    public static final ResourceKey<Biome> PELARGIR = register(109, "pelargir");
    public static final ResourceKey<Biome> DOR_EN_ERNIL = register(110, "dor_en_ernil");
    public static final ResourceKey<Biome> FIELDS_DOR_EN_ERNIL = register(111, "fields_dor_en_ernil");
    public static final ResourceKey<Biome> TOLFALAS = register(112, "tolfalas");
    public static final ResourceKey<Biome> MOUTHS_OF_ANDUIN = register(113, "mouths_of_anduin");
    public static final ResourceKey<Biome> HARONDOR = register(114, "harondor");
    public static final ResourceKey<Biome> VALE_MORGUL = register(115, "vale_morgul");
    public static final ResourceKey<Biome> MOUNTAINS_MORDOR = register(116, "mountains_mordor");
    public static final ResourceKey<Biome> MORDOR = register(117, "mordor");
    public static final ResourceKey<Biome> WASTELAND_EASTERN = register(118, "wasteland_eastern");
    public static final ResourceKey<Biome> GORGOROTH = register(119, "gorgoroth");
    public static final ResourceKey<Biome> NURN = register(120, "nurn");
    public static final ResourceKey<Biome> UDUN = register(121, "udun");
    public static final ResourceKey<Biome> NAN_UNGOL = register(122, "nan_ungol");
    public static final ResourceKey<Biome> MARSHES_NURN = register(123, "marshes_nurn");
    public static final ResourceKey<Biome> LOSTLADEN = register(124, "lostladen");
    public static final ResourceKey<Biome> DESERT_HALF = register(125, "desert_half");
    public static final ResourceKey<Biome> DESERT_GREAT = register(126, "desert_great");
    public static final ResourceKey<Biome> DESERT_HILLS = register(127, "desert_hills");
    public static final ResourceKey<Biome> HARNENNOR = register(128, "harnennor");
    public static final ResourceKey<Biome> COASTS_SOUTHRON = register(129, "coasts_southron");
    public static final ResourceKey<Biome> HILLS_UMBAR = register(130, "hills_umbar");
    public static final ResourceKey<Biome> UMBAR = register(131, "umbar");
    public static final ResourceKey<Biome> FOREST_UMBAR = register(132, "forest_umbar");
    public static final ResourceKey<Biome> FOREST_SOUTHRON = register(133, "forest_southron");
    public static final ResourceKey<Biome> COAST_CORSAIR = register(134, "coast_corsair");
    public static final ResourceKey<Biome> GRASSLAND_ARID_FAR_HARAD = register(135, "grassland_arid_far_harad");
    public static final ResourceKey<Biome> MOUNTAINS_FAR_HARAD = register(136, "mountains_far_harad");
    public static final ResourceKey<Biome> HILLS_ARID_FAR_HARAD = register(137, "hills_arid_far_harad");
    public static final ResourceKey<Biome> GULF_FAR_HARAD = register(138, "gulf_far_harad");
    public static final ResourceKey<Biome> GRASSLAND_FAR_HARAD = register(139, "grassland_far_harad");
    public static final ResourceKey<Biome> FOREST_CLOUD_FAR_HARAD = register(140, "forest_cloud_far_harad");
    public static final ResourceKey<Biome> FOREST_FAR_HARAD = register(141, "forest_far_harad");
    public static final ResourceKey<Biome> TAUR_I_THOROGRIM = register(142, "taur_i_thorogrim");
    public static final ResourceKey<Biome> VOLCANO_FAR_HARAD = register(143, "volcano_far_harad");
    public static final ResourceKey<Biome> PEDOROGWAITH = register(144, "pedorogwaith");
    public static final ResourceKey<Biome> DESERT_RED = register(145, "desert_red");
    public static final ResourceKey<Biome> FOREST_GULF = register(146, "forest_gulf");
    public static final ResourceKey<Biome> JUNGLE_FAR_HARAD = register(147, "jungle_far_harad");
    public static final ResourceKey<Biome> JUNGLE_EDGE_FAR_HARAD = register(148, "jungle_edge_far_harad");
    public static final ResourceKey<Biome> SWAMPLAND_FAR_HARAD = register(149, "swampland_far_harad");
    public static final ResourceKey<Biome> MOUNTAINS_JUNGLE_FAR_HARAD = register(150, "mountains_jungle_far_harad");
    public static final ResourceKey<Biome> BUSHLAND_FAR_HARAD = register(151, "bushland_far_harad");
    public static final ResourceKey<Biome> HILLS_FAR_HARAD = register(152, "hills_far_harad");
    public static final ResourceKey<Biome> MANGROVE_FAR_HARAD = register(153, "mangrove_far_harad");
    public static final ResourceKey<Biome> FOREST_KANUKA = register(154, "forest_kanuka");
    public static final ResourceKey<Biome> JUNGLE_LAKE_FAR_HARAD = register(155, "jungle_lake_far_harad");
    public static final ResourceKey<Biome> LAKE = register(156, "lake");
    public static final ResourceKey<Biome> SEA = register(157, "sea");
    public static final ResourceKey<Biome> SEA_NUMEN = register(158, "sea_numen");
    public static final ResourceKey<Biome> RIVER = register(159, "river");

    public static ResourceKey<Biome> getBiomeByID(int id) {
        return BiomesByID.get(id);
    }
    public static int getIDByBiome(ResourceKey<Biome> biome) {
        return IDsByBiome.get(biome);
    }

    public static ArrayList<ResourceKey<Biome>> allWithPredicate(String predicate) {
        ArrayList<ResourceKey<Biome>> result = new ArrayList<>();

        for(String name : BiomesByName.keySet()){
            if(name.startsWith(predicate)){
                result.add(BiomesByName.get(name));
            }
        }

        return result;
    }

    private static ResourceKey<Biome> register(int id, String name) {
        ResourceKey<Biome> key = ResourceKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(Mewg.MOD_ID, name));
        BiomesByID.put(id, key);
        IDsByBiome.put(key, id);
        BiomesByName.put(name, key);
        return key;
    }
    public static void init() {
        Mewg.LOGGER.info("registering biomes...");
        for(int id : BiomesByID.keySet()) {
            Mewg.LOGGER.info("{}: {}", id, BiomesByID.get(id));
        }
    }
}
