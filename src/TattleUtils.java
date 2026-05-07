import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.parser.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class TattleUtils 
{
    /**
     * @Author Jemaroo
     * @Function Turns the value of an enemy name into it's tattle id name
     */
    public static TattleNames TattleNameConverter(String name, File openedFile)
    {
        switch (name) 
        {
            case "Magnus Von Grapple 2.0": return new TattleNames("btl_hlp_magnum_battender_mkII", "menu_enemy_140");
            case "X-Punch": return new TattleNames("btl_hlp_rocket_punch_mkII");
            case "X-Naut": return new TattleNames("btl_hlp_gundan_zako", "menu_enemy_018");
            case "Mini-Z-Yux": return new TattleNames("btl_hlp_barriern_z_petit", "menu_enemy_074");
            case "Z-Yux": return new TattleNames("btl_hlp_barriern_z", "menu_enemy_073");
            case "Elite X-Naut": return new TattleNames("btl_hlp_gundan_zako_elite", "menu_enemy_076");
            case "X-Naut PHD": return new TattleNames("btl_hlp_gundan_zako_magician", "menu_enemy_075");
            case "X-Yux": return new TattleNames("btl_hlp_barriern_custom", "menu_enemy_179");
            case "Mini-X-Yux": return new TattleNames("btl_hlp_barriern_custom_satellite", "menu_enemy_180");
            case "Yux": return new TattleNames("btl_hlp_barriern", "menu_enemy_016");
            case "Mini-Yux": return new TattleNames("btl_hlp_barriern_petit", "menu_enemy_017");
            case "Goomba": return new TattleNames("btl_hlp_kuriboo", "menu_enemy_001");
            case "Ice Puff": return new TattleNames("btl_hlp_bllizard", "menu_enemy_066");
            case "Frost Piranha": return new TattleNames("btl_hlp_ice_pakkun", "menu_enemy_047");
            case "Lava Bubble": return new TattleNames("btl_hlp_bubble", "menu_enemy_080");
            case "Bullet Bill": return new TattleNames("btl_hlp_killer", "menu_enemy_057");
            case "Bill Blaster": return new TattleNames("btl_hlp_killer_cannon", "menu_enemy_059");
            case "Bulky Bob-omb": return new TattleNames("btl_hlp_heavy_bom", "menu_enemy_064");
            case "Ember": return new TattleNames("btl_hlp_hermos", "menu_enemy_081");
            case "Parabuzzy": return new TattleNames("btl_hlp_patamet", "menu_enemy_050");
            case "Pokey": return new TattleNames("btl_hlp_sambo", "menu_enemy_055");
            case "Poison Pokey": return new TattleNames("btl_hlp_sambo_mummy", "menu_enemy_056");
            case "Spiky Parabuzzy": return new TattleNames("btl_hlp_patatogemet", "menu_enemy_051");
            case "Ruff Puff": return new TattleNames("btl_hlp_kurokumorn", "menu_enemy_065");
            case "Hooktail": return new TattleNames("btl_hlp_gonbaba", "menu_enemy_117");
            case "Dull Bones": return new TattleNames("btl_hlp_honenoko", "menu_enemy_007");
            case "Red Bones": return new TattleNames("btl_hlp_red_honenoko", "menu_enemy_012");
            case "Koopa Troopa": return new TattleNames("btl_hlp_nokonoko", "menu_enemy_004");
            case "Paragoomba": return new TattleNames("btl_hlp_patakuri", "menu_enemy_003");
            case "Paratroopa": return new TattleNames("btl_hlp_patapata", "menu_enemy_005");
            case "Spiky Goomba": return new TattleNames("btl_hlp_togekuri", "menu_enemy_002");
            case "Gus": return new TattleNames("btl_hlp_monban", "menu_enemy_103");
            case "Lord Crump": 
                if(openedFile.getName().equals("muj.rel"))
                    {return new TattleNames("btl_hlp_kanbu3", "menu_enemy_144");} 
                else
                    {return new TattleNames("");}
            case "Hyper Goomba": return new TattleNames("btl_hlp_hyper_kuriboo", "menu_enemy_023");
            case "Hyper Paragoomba": return new TattleNames("btl_hlp_hyper_patakuri", "menu_enemy_025");
            case "Hyper Spiky Goomba": return new TattleNames("btl_hlp_hyper_togekuri", "menu_enemy_024");
            case "Hyper Cleft": return new TattleNames("btl_hlp_hyper_sinemon", "menu_enemy_037");
            case "Crazee Dayzee": return new TattleNames("btl_hlp_pansy", "menu_enemy_082");
            case "Amazy Dayzee": return new TattleNames("btl_hlp_twinkling_pansy", "menu_enemy_083");
            case "Fuzzy": return new TattleNames("btl_hlp_chorobon", "menu_enemy_006");
            case "Fuzzy Horde": return new TattleNames("btl_hlp_chorobon_gundan", "menu_enemy_011");
            case "Gold Fuzzy": return new TattleNames("btl_hlp_gold_chorobon", "menu_enemy_010");
            case "Moon Cleft": return new TattleNames("btl_hlp_sinemon", "menu_enemy_034");
            case "Bald Cleft": return new TattleNames("btl_hlp_sinnosuke", "menu_enemy_008");
            case "Bristle": return new TattleNames("btl_hlp_togedaruma", "menu_enemy_009");
            case "Cleft": return new TattleNames("btl_hlp_monochrome_sinemon", "menu_enemy_035");
            case "Atomic Boo": return new TattleNames("btl_hlp_atomic_teresa", "menu_enemy_054");
            case "Doopliss":
                if(openedFile.getName().equals("jin.rel"))
                    {return new TattleNames("btl_hlp_hatena", "menu_enemy_124");} 
                else if(openedFile.getName().equals("las.rel"))
                    {return new TattleNames("btl_hlp_ranperu_las", "menu_enemy_125");} 
                else 
                    {return new TattleNames("");}
            case "Buzzy Beetle": return new TattleNames("btl_hlp_met", "menu_enemy_048");
            case "Spiky Buzzy": return new TattleNames("btl_hlp_togemet", "menu_enemy_049");
            case "Swooper": return new TattleNames("btl_hlp_basabasa", "menu_enemy_040");
            case "Boo": return new TattleNames("btl_hlp_teresa", "menu_enemy_052");
            case "Bonetail": return new TattleNames("btl_hlp_zonbaba", "menu_enemy_195");
            case "Spania": return new TattleNames("btl_hlp_hannya", "menu_enemy_021");
            case "Spinia": return new TattleNames("btl_hlp_hinnya", "menu_enemy_020");
            case "Gloomba": return new TattleNames("btl_hlp_yami_kuriboo", "menu_enemy_026");
            case "Paragloomba": return new TattleNames("btl_hlp_yami_patakuri", "menu_enemy_028");
            case "Dark Puff": return new TattleNames("btl_hlp_monochrome_kurokumorn", "menu_enemy_172");
            case "Spiky Gloomba": return new TattleNames("btl_hlp_yami_togekuri", "menu_enemy_027");
            case "Bandit": return new TattleNames("btl_hlp_borodo", "menu_enemy_077");
            case "Lakitu": return new TattleNames("btl_hlp_jyugem", "menu_enemy_084");
            case "Bob-omb": return new TattleNames("btl_hlp_bomhei", "menu_enemy_061");
            case "Spiny": return new TattleNames("btl_hlp_togezo", "menu_enemy_085");
            case "Dark Koopa": return new TattleNames("btl_hlp_yami_noko", "menu_enemy_029");
            case "Shady Koopa": return new TattleNames("btl_hlp_ura_noko", "menu_enemy_031");
            case "Flower Fuzzy": return new TattleNames("btl_hlp_flower_chorobon", "menu_enemy_043");
            case "Dark Paratroopa": return new TattleNames("btl_hlp_yami_pata", "menu_enemy_030");
            case "Dark Bristle": return new TattleNames("btl_hlp_yamitogedaruma", "menu_enemy_174");
            case "Chain-Chomp": return new TattleNames("btl_hlp_wanwan", "menu_enemy_079");
            case "Dark Koopatrol": return new TattleNames("btl_hlp_togenoko_ace", "menu_enemy_175");
            case "Dark Wizzerd": return new TattleNames("btl_hlp_super_mahorn", "menu_enemy_071", "btl_hlp_super_mahorn_bunsin");
            case "Dark Boo": return new TattleNames("btl_hlp_purple_teresa", "menu_enemy_053");
            case "Phantom Ember": return new TattleNames("btl_hlp_phantom", "menu_enemy_178");
            case "Piranha Plant": return new TattleNames("btl_hlp_pakkun_flower", "menu_enemy_014");
            case "Elite Wizzerd": return new TattleNames("btl_hlp_mahorn_custom", "menu_enemy_183", "btl_hlp_mahorn_custom_bunshin");
            case "Wizzerd": return new TattleNames("btl_hlp_mahorn", "menu_enemy_070");
            case "Dry Bones": return new TattleNames("btl_hlp_karon", "menu_enemy_044");
            case "Dark Lakitu": return new TattleNames("btl_hlp_hyper_jyugem", "menu_enemy_186");
            case "Spunia": return new TattleNames("btl_hlp_hennya", "menu_enemy_022");
            case "Bob-ulk": return new TattleNames("btl_hlp_giant_bomb", "menu_enemy_182");
            case "Poison Puff": return new TattleNames("btl_hlp_dokugassun", "menu_enemy_013");
            case "Dark Craw": return new TattleNames("btl_hlp_dark_keeper", "menu_enemy_184");
            case "Red Chomp": return new TattleNames("btl_hlp_burst_wanwan", "menu_enemy_185");
            case "Swoopula": return new TattleNames("btl_hlp_basabasa_chururu", "menu_enemy_041");
            case "Swampire": return new TattleNames("btl_hlp_basabasa_green", "menu_enemy_177");
            case "Badge Bandit": return new TattleNames("btl_hlp_badge_borodo", "menu_enemy_078");
            case "Pider": return new TattleNames("btl_hlp_piders", "menu_enemy_015");
            case "Sky Blue Spiny": return new TattleNames("btl_hlp_hyper_togezo", "menu_enemy_173");
            case "Arantula": return new TattleNames("btl_hlp_churantalar", "menu_enemy_176");
            case "Bowser":
                if(openedFile.getName().equals("tou2.rel"))
                    {return new TattleNames("btl_hlp_koopa_tou", "menu_enemy_187");} 
                else if(openedFile.getName().equals("las.rel"))
                    {return new TattleNames("btl_hlp_koopa", "menu_enemy_149");} 
                else 
                    {return new TattleNames("");}
            case "Kammy Koopa": return new TattleNames("btl_hlp_kamec_obaba", "menu_enemy_150");
            case "Shadow Peach": return new TattleNames("btl_hlp_shadowqueen1", "menu_enemy_153");
            case "Shadow Queen": return new TattleNames("btl_hlp_shadowqueen2", "menu_enemy_154");
            case "Dark Bones": return new TattleNames("btl_hlp_black_karon", "menu_enemy_045");
            case "Grodus": return new TattleNames("btl_hlp_batsugalf", "menu_enemy_151");
            case "Grodus X": return new TattleNames("btl_hlp_batsu_satellite", "menu_enemy_152");
            case "Gloomtail": return new TattleNames("btl_hlp_bunbaba", "menu_enemy_196");
            case "Bombshell Bill": return new TattleNames("btl_hlp_super_killer", "menu_enemy_058");
            case "Bombshell Bill Blaster": return new TattleNames("btl_hlp_super_killer_cannon", "menu_enemy_060");
            case "Marilyn":
                if(openedFile.getName().equals("win.rel"))
                    {return new TattleNames("btl_hlp_maririn", "menu_enemy_108");} 
                else if(openedFile.getName().equals("las.rel"))
                    {return new TattleNames("btl_hlp_maririn2", "menu_enemy_111");} 
                else 
                    {return new TattleNames("");}
            case "Beldam":
                if(openedFile.getName().equals("win.rel"))
                    {return new TattleNames("btl_hlp_majyorin", "menu_enemy_104");} 
                else if(openedFile.getName().equals("las.rel"))
                    {return new TattleNames("btl_hlp_majyorin2", "menu_enemy_107");} 
                else 
                    {return new TattleNames("");}
            case "Magnus Von Grapple": return new TattleNames("btl_hlp_magnum_battender", "menu_enemy_139");
            case "X-Fist": return new TattleNames("btl_hlp_rocket_punch");
            case "Pale Piranha": return new TattleNames("btl_hlp_monochrome_pakkun", "menu_enemy_181");
            case "Cortez": return new TattleNames("btl_hlp_cortez", "menu_enemy_132");
            case "X-Naut Platoon Formation 1": return new TattleNames("btl_hlp_gundan_zako_group1", "menu_enemy_146");
            case "Green Fuzzy": return new TattleNames("btl_hlp_green_chorobon", "menu_enemy_042");
            case "Putrid Piranha": return new TattleNames("btl_hlp_poison_pakkun", "menu_enemy_046");
            case "Smorg": return new TattleNames("btl_hlp_moamoa", "menu_enemy_118");
            case "Blooper": return new TattleNames("btl_hlp_gesso", "menu_enemy_114");
            case "Hammer Bro": return new TattleNames("btl_hlp_hammer_bros", "menu_enemy_067");
            case "Magikoopa": return new TattleNames("btl_hlp_kamec", "menu_enemy_086", "btl_hlp_kamec_bunshin");
            case "Koopatrol": return new TattleNames("btl_hlp_togenoko", "menu_enemy_033");
            case "Macho Grubba": return new TattleNames("btl_hlp_macho_gansu", "menu_enemy_141");
            case "Rawk Hawk": return new TattleNames("btl_hlp_champion", "menu_enemy_142");
            case "Boomerang Bro": return new TattleNames("btl_hlp_boomerang_bros", "menu_enemy_068");
            case "Fire Bro": return new TattleNames("btl_hlp_fire_bros", "menu_enemy_069");
            case "Iron Cleft 1": return new TattleNames("btl_hlp_iron_sinemon", "menu_enemy_038");
            case "Iron Cleft 2": return new TattleNames("btl_hlp_iron_sinemon2", "menu_enemy_039");
            case "Shady Paratroopa": return new TattleNames("btl_hlp_ura_pata", "menu_enemy_032");
            case "Hyper Bald Cleft": return new TattleNames("btl_hlp_hyper_sinnosuke", "menu_enemy_036");
            case "Green Magikoopa": return new TattleNames("btl_hlp_kamec_green", "menu_enemy_171", "btl_hlp_kamec_green_bunshin");
            case "Red Magikoopa": return new TattleNames("btl_hlp_kamec_red", "menu_enemy_169", "btl_hlp_kamec_red_bunshin");
            case "White Magikoopa": return new TattleNames("btl_hlp_kamec_white", "menu_enemy_170", "btl_hlp_kamec_white_bunshin");
            case "Big Bandit": return new TattleNames("btl_hlp_borodo_king", "menu_enemy_190");
            case "KP Koopa": return new TattleNames("btl_hlp_nokonoko_fighter", "menu_enemy_188");
            case "KP Paratroopa": return new TattleNames("btl_hlp_patapata_fighter", "menu_enemy_189");
            case "Red Spiky Buzzy": return new TattleNames("btl_hlp_crimson_togemet", "menu_enemy_191");
            case "Vivian": return new TattleNames("btl_hlp_vivian", "menu_enemy_112");
            
            default: return new TattleNames("");
        }
    }

    /**
     * @Author Jemaroo
     * @Function Builds a new tattle file based off the specified formats and currently opened data
     */
    public static byte[] buildNewTattleFile(File givenGlobal, File givenFile, ArrayList<BUTData> units)
    {
        File jsonFile = new File("src\\TattleFormat.json");
        File excelFile = new File("src\\Tattle Data.xlsx");

        byte[] globalData = ByteUtils.readData(givenGlobal);
        String globalDataString = new String(globalData, StandardCharsets.ISO_8859_1);
        ArrayList<String> globalDataStringList = new ArrayList<String>();
        String[] splitGlobalDataString = globalDataString.split("\0");
        for(int i = 0; i < splitGlobalDataString.length; i++)
        {
            globalDataStringList.add(splitGlobalDataString[i]);
            //System.out.println(globalDataStringList.get(i));
        }

        try
        {
            //Get Format
            JSONParser parser = new JSONParser();
            JSONObject root = (JSONObject)parser.parse(new FileReader(jsonFile));
            String tattleDialogue = (String)root.get("Tattle Dialogue");
            String menuDialogue = (String)root.get("Tattle Menu");
            JSONArray uniqueTattles = (JSONArray)root.get("Unique Tattle Formats");
            ArrayList<String> additionalFormatsIDs = new ArrayList<String>();
            ArrayList<String> additionalFormats = new ArrayList<String>();
            for(int i = 0; i < uniqueTattles.size(); i++)
            {
                additionalFormatsIDs.add((String)((JSONObject)uniqueTattles.get(i)).get("ID"));
                additionalFormats.add((String)((JSONObject)uniqueTattles.get(i)).get("Text"));
            }

            //Get Excel Sheet
            FileInputStream inputStream = new FileInputStream(excelFile);
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet firstSheet = workbook.getSheetAt(0);

            for(int i = 0; i < units.size(); i++)
            {
                System.out.println(units.get(i).name);

                String retString_btl = "";
                String retString_menu = "";
                String retString_clone = "";
                TattleNames tempTattleNames = TattleNameConverter(units.get(i).name, givenFile);

                //Pull Start Message
                Iterator<Row> iterator = firstSheet.iterator();
                while (iterator.hasNext()) 
                {
                    Row nextRow = iterator.next();

                    if(nextRow.getCell(0).getStringCellValue().equals(tempTattleNames.btl) && nextRow.getCell(1) != null)
                    {
                        retString_btl += nextRow.getCell(1).getStringCellValue(); 
                    }
                }

                if(tempTattleNames.cloneFlag)
                {
                    iterator = firstSheet.iterator();
                    while (iterator.hasNext()) 
                    {
                        Row nextRow = iterator.next();

                        if(nextRow.getCell(0).getStringCellValue().equals(tempTattleNames.clone) && nextRow.getCell(1) != null)
                        {
                            retString_clone += nextRow.getCell(1).getStringCellValue(); 
                        }
                    }
                }
                
                String[] split_btl_layout = tattleDialogue.split(" ");
                String[] split_menu_layout = menuDialogue.split(" ");

                if(additionalFormatsIDs.contains(tempTattleNames.btl))
                {
                    int offset = additionalFormatsIDs.indexOf(tempTattleNames.btl);
                    String[] split_btl_custom_layout = additionalFormats.get(offset).split(" ");
                    if(!retString_btl.equals(""))
                    {
                        retString_btl += '\n';
                    }
                    for(int j = 0; j < split_btl_custom_layout.length; j++)
                    {
                        retString_btl += tattleCommandTranslation(split_btl_custom_layout[j], i, units);
                        if(j < split_btl_custom_layout.length - 1)
                        {
                            retString_btl += " ";
                        }
                    }
                }
                else
                {
                    if(!retString_btl.equals(""))
                    {
                        retString_btl += '\n';
                    }
                    for(int j = 0; j < split_btl_layout.length; j++)
                    {
                        retString_btl += tattleCommandTranslation(split_btl_layout[j], i, units);
                        if(j < split_btl_layout.length - 1)
                        {
                            retString_btl += " ";
                        }
                    }
                }

                if(tempTattleNames.menuFlag)
                {
                    if(additionalFormatsIDs.contains(tempTattleNames.menu))
                    {
                        int offset = additionalFormatsIDs.indexOf(tempTattleNames.menu);
                        String[] split_menu_custom_layout = additionalFormats.get(offset).split(" ");
                        if(!retString_menu.equals(""))
                        {
                            retString_menu += '\n';
                        }
                        for(int j = 0; j < split_menu_custom_layout.length; j++)
                        {
                            retString_menu += tattleCommandTranslation(split_menu_custom_layout[j], i, units);
                            if(j < split_menu_custom_layout.length - 1)
                            {
                                retString_menu += " ";
                            }
                        }
                    }
                    else
                    {
                        if(!retString_menu.equals(""))
                        {
                            retString_menu += '\n';
                        }
                        for(int j = 0; j < split_menu_layout.length; j++)
                        {
                            retString_menu += tattleCommandTranslation(split_menu_layout[j], i, units);
                            if(j < split_menu_layout.length - 1)
                            {
                                retString_menu += " ";
                            }
                        }
                    }
                }

                if(tempTattleNames.cloneFlag)
                {
                    if(additionalFormatsIDs.contains(tempTattleNames.clone))
                    {
                        int offset = additionalFormatsIDs.indexOf(tempTattleNames.clone);
                        String[] split_clone_custom_layout = additionalFormats.get(offset).split(" ");
                        if(!retString_clone.equals(""))
                        {
                            retString_clone += '\n';
                        }
                        for(int j = 0; j < split_clone_custom_layout.length; j++)
                        {
                            retString_clone += tattleCommandTranslation(split_clone_custom_layout[j], i, units);
                            if(j < split_clone_custom_layout.length - 1)
                            {
                                retString_clone += " ";
                            }
                        }
                    }
                    else
                    {
                        if(!retString_clone.equals(""))
                        {
                            retString_clone += '\n';
                        }
                        for(int j = 0; j < split_btl_layout.length; j++)
                        {
                            retString_clone += tattleCommandTranslation(split_btl_layout[j], i, units);
                            if(j < split_btl_layout.length - 1)
                            {
                                retString_clone += " ";
                            }
                        }
                    }
                }

                //Pull End Message
                iterator = firstSheet.iterator();
                while (iterator.hasNext()) 
                {
                    Row nextRow = iterator.next();

                    if(nextRow.getCell(0).getStringCellValue().equals(tempTattleNames.btl) && nextRow.getCell(2) != null)
                    {
                        retString_btl += '\n' + nextRow.getCell(2).getStringCellValue(); 
                    }
                }

                if(tempTattleNames.cloneFlag)
                {
                    iterator = firstSheet.iterator();
                    while (iterator.hasNext()) 
                    {
                        Row nextRow = iterator.next();

                        if(nextRow.getCell(0).getStringCellValue().equals(tempTattleNames.clone) && nextRow.getCell(2) != null)
                        {
                            retString_clone += '\n' +  nextRow.getCell(2).getStringCellValue(); 
                        }
                    }
                }

                //Replace old tattle string with new built one
                for(int j = 0; j < globalDataStringList.size(); j++)
                {
                    if(globalDataStringList.get(j).equals(tempTattleNames.btl))
                    {
                        globalDataStringList.set((j + 1), retString_btl);
                    }
                }
                
                for(int j = 0; j < globalDataStringList.size(); j++)
                {
                    if(globalDataStringList.get(j).equals(tempTattleNames.menu))
                    {
                        globalDataStringList.set((j + 1), retString_menu);
                    }
                }

                for(int j = 0; j < globalDataStringList.size(); j++)
                {
                    if(globalDataStringList.get(j).equals(tempTattleNames.clone))
                    {
                        globalDataStringList.set((j + 1), retString_clone);
                    }
                }
            }
            
            workbook.close();
            inputStream.close();

            String newRetString = "";
            for(int i = 0; i < globalDataStringList.size(); i++)
            {
                newRetString += globalDataStringList.get(i) + '\0';
            }

            return newRetString.getBytes(StandardCharsets.UTF_8);
        }
        catch (FileNotFoundException e)
        {
            System.out.println("There was an Error Finding the JSON File");
        }
        catch (IOException e)
        {
            System.out.println("There was an Error Reading the JSON File");
        }
        catch (ParseException e)
        {
            System.out.println("There was an Error Parsing the JSON File");
        }

        return null;
    }

    /**
     * @Author Jemaroo
     * @Function Checks for tattle command functions, splits off to other functions if struct type found
     */
    public static String tattleCommandTranslation(String phrase, int index, ArrayList<BUTData> units)
    {
        String[] splitPhrase = phrase.split("_");

        switch (splitPhrase[0]) 
        {
            case "[BUK": return BUK_TCT(phrase, index, units);
            case "[BUKP": return BUKP_TCT(phrase, index, units);
            case "[HU": return HU_TCT(phrase, index, units);
            case "[BUD": return BUD_TCT(phrase, index, units);
            case "[BUDA": return BUDA_TCT(phrase, index, units);
            case "[SV": return SV_TCT(phrase, index, units);
            case "[BW": return BW_TCT(phrase, index, units);
            case "[LOGIC": return LOGIC_TCT(phrase, index, units);           

            default: return phrase;
        }
    }

    /**
     * @Author Jemaroo
     * @Function BattleUnitKind Tattle Command Translation
     */
    public static String BUK_TCT(String phrase, int index, ArrayList<BUTData> units)
    {
        String[] splitPhrase = phrase.split("_");
        int structNUM = 0;

        if(splitPhrase.length == 3)
        {
            switch(splitPhrase[2])
            {
                case "Struct1]": structNUM = 1; break;
                case "Struct2]": structNUM = 2; break;
            }

            if(structNUM != 0 && units.get(index).BattleUnitKindData.size() >= structNUM)
            {
                switch(splitPhrase[1])
                {
                    case "HP": return String.valueOf(units.get(index).BattleUnitKindData.get((structNUM - 1)).HP);
                    case "dangerHP": return String.valueOf(units.get(index).BattleUnitKindData.get((structNUM - 1)).dangerHP);
                    case "perilHP": return String.valueOf(units.get(index).BattleUnitKindData.get((structNUM - 1)).perilHP);
                    case "level": return String.valueOf(units.get(index).BattleUnitKindData.get((structNUM - 1)).level);
                    case "bonusXP": return String.valueOf(units.get(index).BattleUnitKindData.get((structNUM - 1)).bonusXP);
                    case "bonusCoin": return String.valueOf(units.get(index).BattleUnitKindData.get((structNUM - 1)).bonusCoin);
                    case "bonusCoinRate": return String.valueOf(units.get(index).BattleUnitKindData.get((structNUM - 1)).bonusCoinRate);
                    case "baseCoin": return String.valueOf(units.get(index).BattleUnitKindData.get((structNUM - 1)).baseCoin);
                    case "runRate": return String.valueOf(units.get(index).BattleUnitKindData.get((structNUM - 1)).runRate);
                    case "pbCap": return String.valueOf(units.get(index).BattleUnitKindData.get((structNUM - 1)).pbCap);
                    case "swallowChance": return String.valueOf(units.get(index).BattleUnitKindData.get((structNUM - 1)).swallowChance);
                    case "swallowAttribute": return SAtoName(units.get(index).BattleUnitKindData.get((structNUM - 1)).swallowAttribute);
                    case "ultraHammerKnockChance": return String.valueOf(units.get(index).BattleUnitKindData.get((structNUM - 1)).ultraHammerKnockChance);
                    case "itemStealParameter": return String.valueOf(units.get(index).BattleUnitKindData.get((structNUM - 1)).itemStealParameter);
                    case "UAFx1": return BooltoYN(units.get(index).BattleUnitKindData.get((structNUM - 1)).MapObj);
                    case "OutOfReach": return BooltoYN(units.get(index).BattleUnitKindData.get((structNUM - 1)).OutOfReach);
                    case "Unquakeable": return BooltoYN(units.get(index).BattleUnitKindData.get((structNUM - 1)).Unquakeable);
                    case "UAFx8": return BooltoYN(units.get(index).BattleUnitKindData.get((structNUM - 1)).IsInvisible);
                    case "Veiled": return BooltoYN(units.get(index).BattleUnitKindData.get((structNUM - 1)).IsVeiled);
                    case "ShellShielded": return BooltoYN(units.get(index).BattleUnitKindData.get((structNUM - 1)).ShellShielded);
                    case "UAFx40": return BooltoYN(units.get(index).BattleUnitKindData.get((structNUM - 1)).NeverTargetable);
                    case "UAFx100": return BooltoYN(units.get(index).BattleUnitKindData.get((structNUM - 1)).LimitSwitch);
                    case "DisableZeroGravityFloat": return BooltoYN(units.get(index).BattleUnitKindData.get((structNUM - 1)).DisableZeroGravityFloat);
                    case "DisableZeroGravityImmobility": return BooltoYN(units.get(index).BattleUnitKindData.get((structNUM - 1)).DisableZeroGravityImmobility);
                    case "UAFx4000": return BooltoYN(units.get(index).BattleUnitKindData.get((structNUM - 1)).immuneToUltraHammerKnock);
                    case "UAFx10000": return BooltoYN(units.get(index).BattleUnitKindData.get((structNUM - 1)).IsUndead);
                    case "UAFx20000": return BooltoYN(units.get(index).BattleUnitKindData.get((structNUM - 1)).IsCorpse);
                    case "UAFx40000": return BooltoYN(units.get(index).BattleUnitKindData.get((structNUM - 1)).IsLeader);
                    case "UAFx80000": return BooltoYN(units.get(index).BattleUnitKindData.get((structNUM - 1)).CannotTakeActions);
                    case "UAFx200000": return BooltoYN(units.get(index).BattleUnitKindData.get((structNUM - 1)).NotSpunByLoveSlap);
                    case "UAFx400000": return BooltoYN(units.get(index).BattleUnitKindData.get((structNUM - 1)).DisableDamageStars);
                    case "UAFx1000000": return BooltoYN(units.get(index).BattleUnitKindData.get((structNUM - 1)).DisableAllPartVisibility);
                    case "UAFx2000000": return BooltoYN(units.get(index).BattleUnitKindData.get((structNUM - 1)).DisableHPGauge);
                    case "UAFx4000000": return BooltoYN(units.get(index).BattleUnitKindData.get((structNUM - 1)).LookCamera);
                    case "UAFx10000000": return BooltoYN(units.get(index).BattleUnitKindData.get((structNUM - 1)).NonCombatant);
                    case "UAFx20000000": return BooltoYN(units.get(index).BattleUnitKindData.get((structNUM - 1)).NoShadow);
                    case "DisableDamage": return BooltoYN(units.get(index).BattleUnitKindData.get((structNUM - 1)).DisableDamage);
                }
            }
        }

        //Passthrough
        return phrase;
    }

    /**
     * @Author Jemaroo
     * @Function BattleUnitKindPart Tattle Command Translation
     */
    public static String BUKP_TCT(String phrase, int index, ArrayList<BUTData> units)
    {
        String[] splitPhrase = phrase.split("_");
        int structNUM = 0;

        if(splitPhrase.length == 3)
        {
            switch(splitPhrase[2])
            {
                case "Struct1]": structNUM = 1; break;
                case "Struct2]": structNUM = 2; break;
                case "Struct3]": structNUM = 3; break;
                case "Struct4]": structNUM = 4; break;
                case "Struct5]": structNUM = 5; break;
                case "Struct6]": structNUM = 6; break;
                case "Struct7]": structNUM = 7; break;
                case "Struct8]": structNUM = 8; break;
                case "Struct9]": structNUM = 9; break;
                case "Struct10]": structNUM = 10; break;
                case "Struct11]": structNUM = 11; break;
                case "Struct12]": structNUM = 12; break;
                case "Struct13]": structNUM = 13; break;
                case "Struct14]": structNUM = 14; break;
                case "Struct15]": structNUM = 15; break;
                case "Struct16]": structNUM = 16; break;
                case "Struct17]": structNUM = 17; break;
                case "Struct18]": structNUM = 18; break;
                case "Struct19]": structNUM = 19; break;
            }

            if(structNUM != 0 && units.get(index).BattleUnitKindPartData.size() >= structNUM)
            {
                switch(splitPhrase[1])
                {
                    case "MostPreferredSelectTarget": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).MainBodyPart);
                    case "PreferredSelectTarget": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).SecondaryBodyPart);
                    case "SelectTarget": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).BombableBodyPart);
                    case "PAx8": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).GuardBodyPart);
                    case "PAx10": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).NotBombableBodyPart);
                    case "PAx40": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).InHole);
                    case "WeakToAttackFxR": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).WeakToAttackFxR);
                    case "WeakToIcePower": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).WeakToIcePower);
                    case "IsWinged": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).IsWinged);
                    case "IsShelled": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).IsShelled);
                    case "IsBombFlippable": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).IsBombFlippable);
                    case "PAx4000": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).IsClonelike);
                    case "PAx8000": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).DisableFlatPaperLayering);
                    case "NeverTargetable": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).NeverTargetable);
                    case "PAx20000": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).IgnoreMapObjectOffset);
                    case "PAx40000": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).IgnoreOnlyTargetSelectAndPreferredParts);
                    case "Untattleable": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).Untattleable);
                    case "JumplikeCannotTarget": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).JumplikeCannotTarget);
                    case "HammerlikeCannotTarget": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).HammerlikeCannotTarget);
                    case "ShellTosslikeCannotTarget": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).ShellTosslikeCannotTarget);
                    case "PAx800000": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).PreventHealthDecrease);
                    case "PAx1000000": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).DisablePartVisibility);
                    case "PAx2000000": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).ImmuneToCustom);
                    case "PAx4000000": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).BlurOn);
                    case "PAx8000000": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).ScaleIndependence);
                    case "PAx10000000": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).Independence);
                    case "IsImmuneToDamageOrStatus": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).IsImmuneToDamageOrStatus);
                    case "IsImmuneToOHKO": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).IsImmuneToOHKO);
                    case "IsImmuneToStatus": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).IsImmuneToStatus);
                    case "TopSpiky": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).TopSpiky);
                    case "PreemptiveFrontSpiky": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).PreemptiveFrontSpiky);
                    case "FrontSpiky": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).FrontSpiky);
                    case "Fiery": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).Fiery);
                    case "FieryStatus": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).FieryStatus);
                    case "Icy": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).Icy);
                    case "IcyStatus": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).IcyStatus);
                    case "Poison": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).Poison);
                    case "PoisonStatus": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).PoisonStatus);
                    case "Electric": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).Electric);
                    case "ElectricStatus": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).ElectricStatus);
                    case "Explosive": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).Explosive);
                    case "VolatileExplosive": return BooltoYN(units.get(index).BattleUnitKindPartData.get((structNUM - 1)).VolatileExplosive);
                }
            }
        }

        //Passthrough
        return phrase;
    }

    /**
     * @Author Jemaroo
     * @Function HealthUpgrades Tattle Command Translation
     */
    public static String HU_TCT(String phrase, int index, ArrayList<BUTData> units)
    {
        String[] splitPhrase = phrase.split("_");
        int structNUM = 0;

        if(splitPhrase.length == 3)
        {
            switch(splitPhrase[2])
            {
                case "Struct1]": structNUM = 1; break;
            }

            if(structNUM != 0 && units.get(index).HealthUpgradesData.size() >= structNUM)
            {
                switch(splitPhrase[1])
                {
                    case "startHP": return String.valueOf(units.get(index).HealthUpgradesData.get((structNUM - 1)).startHP);
                    case "startFP": return String.valueOf(units.get(index).HealthUpgradesData.get((structNUM - 1)).startFP);
                    case "startBP": return String.valueOf(units.get(index).HealthUpgradesData.get((structNUM - 1)).startBP);
                    case "upgradeHP": return String.valueOf(units.get(index).HealthUpgradesData.get((structNUM - 1)).upgradeHP);
                    case "upgradeFP": return String.valueOf(units.get(index).HealthUpgradesData.get((structNUM - 1)).upgradeFP);
                    case "upgradeBP": return String.valueOf(units.get(index).HealthUpgradesData.get((structNUM - 1)).upgradeBP);
                    case "PartnerLevel1HP": return String.valueOf(units.get(index).HealthUpgradesData.get((structNUM - 1)).startHP);
                    case "PartnerLevel2HP": return String.valueOf(units.get(index).HealthUpgradesData.get((structNUM - 1)).startFP);
                    case "PartnerLevel3HP": return String.valueOf(units.get(index).HealthUpgradesData.get((structNUM - 1)).startBP);
                }
            }
        }

        //Passthrough
        return phrase;
    }

    /**
     * @Author Jemaroo
     * @Function BattleUnitDefense Tattle Command Translation
     */
    public static String BUD_TCT(String phrase, int index, ArrayList<BUTData> units)
    {
        String[] splitPhrase = phrase.split("_");
        int structNUM = 0;

        if(splitPhrase.length == 3)
        {
            switch(splitPhrase[2])
            {
                case "Struct1]": structNUM = 1; break;
                case "Struct2]": structNUM = 2; break;
                case "Struct3]": structNUM = 3; break;
                case "Struct4]": structNUM = 4; break;
                case "Struct5]": structNUM = 5; break;
                case "Struct6]": structNUM = 6; break;
                case "Struct7]": structNUM = 7; break;
                case "Struct8]": structNUM = 8; break;
                case "Struct9]": structNUM = 9; break;
                case "Struct10]": structNUM = 10; break;
            }

            if(structNUM != 0 && units.get(index).BattleUnitDefenseData.size() >= structNUM)
            {
                switch(splitPhrase[1])
                {
                    case "normal": return String.valueOf(units.get(index).BattleUnitDefenseData.get((structNUM - 1)).normal);
                    case "fire": return String.valueOf(units.get(index).BattleUnitDefenseData.get((structNUM - 1)).fire);
                    case "ice": return String.valueOf(units.get(index).BattleUnitDefenseData.get((structNUM - 1)).ice);
                    case "explosion": return String.valueOf(units.get(index).BattleUnitDefenseData.get((structNUM - 1)).explosion);
                    case "electric": return String.valueOf(units.get(index).BattleUnitDefenseData.get((structNUM - 1)).electric);
                }
            }
        }

        //Passthrough
        return phrase;
    }

    /**
     * @Author Jemaroo
     * @Function BattleUnitDefenseAttr Tattle Command Translation
     */
    public static String BUDA_TCT(String phrase, int index, ArrayList<BUTData> units)
    {
        String[] splitPhrase = phrase.split("_");
        int structNUM = 0;

        if(splitPhrase.length == 3)
        {
            switch(splitPhrase[2])
            {
                case "Struct1]": structNUM = 1; break;
                case "Struct2]": structNUM = 2; break;
                case "Struct3]": structNUM = 3; break;
                case "Struct4]": structNUM = 4; break;
                case "Struct5]": structNUM = 5; break;
                case "Struct6]": structNUM = 6; break;
                case "Struct7]": structNUM = 7; break;
                case "Struct8]": structNUM = 8; break;
                case "Struct9]": structNUM = 9; break;
                case "Struct10]": structNUM = 10; break;
            }

            if(structNUM != 0 && units.get(index).BattleUnitDefenseAttrData.size() >= structNUM)
            {
                switch(splitPhrase[1])
                {
                    case "normal": return DAtoName(units.get(index).BattleUnitDefenseAttrData.get((structNUM - 1)).normal);
                    case "fire": return DAtoName(units.get(index).BattleUnitDefenseAttrData.get((structNUM - 1)).fire);
                    case "ice": return DAtoName(units.get(index).BattleUnitDefenseAttrData.get((structNUM - 1)).ice);
                    case "explosion": return DAtoName(units.get(index).BattleUnitDefenseAttrData.get((structNUM - 1)).explosion);
                    case "electric": return DAtoName(units.get(index).BattleUnitDefenseAttrData.get((structNUM - 1)).electric);
                }
            }
        }

        //Passthrough
        return phrase;
    }

    /**
     * @Author Jemaroo
     * @Function StatusVulnerability Tattle Command Translation
     */
    public static String SV_TCT(String phrase, int index, ArrayList<BUTData> units)
    {
        String[] splitPhrase = phrase.split("_");
        int structNUM = 0;

        if(splitPhrase.length == 3)
        {
            switch(splitPhrase[2])
            {
                case "Struct1]": structNUM = 1; break;
                case "Struct2]": structNUM = 2; break;
                case "Struct3]": structNUM = 3; break;
                case "Struct4]": structNUM = 4; break;
                case "Struct5]": structNUM = 5; break;
                case "Struct6]": structNUM = 6; break;
                case "Struct7]": structNUM = 7; break;
                case "Struct8]": structNUM = 8; break;
                case "Struct9]": structNUM = 9; break;
                case "Struct10]": structNUM = 10; break;
            }

            if(structNUM != 0 && units.get(index).StatusVulnerabilityData.size() >= structNUM)
            {
                switch(splitPhrase[1])
                {
                    case "sleep": return String.valueOf(units.get(index).StatusVulnerabilityData.get((structNUM - 1)).sleep);
                    case "stop": return String.valueOf(units.get(index).StatusVulnerabilityData.get((structNUM - 1)).stop);
                    case "dizzy": return String.valueOf(units.get(index).StatusVulnerabilityData.get((structNUM - 1)).dizzy);
                    case "poison": return String.valueOf(units.get(index).StatusVulnerabilityData.get((structNUM - 1)).poison);
                    case "confuse": return String.valueOf(units.get(index).StatusVulnerabilityData.get((structNUM - 1)).confuse);
                    case "electric": return String.valueOf(units.get(index).StatusVulnerabilityData.get((structNUM - 1)).electric);
                    case "burn": return String.valueOf(units.get(index).StatusVulnerabilityData.get((structNUM - 1)).burn);
                    case "freeze": return String.valueOf(units.get(index).StatusVulnerabilityData.get((structNUM - 1)).freeze);
                    case "huge": return String.valueOf(units.get(index).StatusVulnerabilityData.get((structNUM - 1)).huge);
                    case "tiny": return String.valueOf(units.get(index).StatusVulnerabilityData.get((structNUM - 1)).tiny);
                    case "attackUp": return String.valueOf(units.get(index).StatusVulnerabilityData.get((structNUM - 1)).attack_up);
                    case "attackDown": return String.valueOf(units.get(index).StatusVulnerabilityData.get((structNUM - 1)).attack_down);
                    case "defenseUp": return String.valueOf(units.get(index).StatusVulnerabilityData.get((structNUM - 1)).defense_up);
                    case "defenseDown": return String.valueOf(units.get(index).StatusVulnerabilityData.get((structNUM - 1)).defense_down);
                    case "allergic": return String.valueOf(units.get(index).StatusVulnerabilityData.get((structNUM - 1)).allergic);
                    case "fright": return String.valueOf(units.get(index).StatusVulnerabilityData.get((structNUM - 1)).fright);
                    case "galeForce": return String.valueOf(units.get(index).StatusVulnerabilityData.get((structNUM - 1)).gale_force);
                    case "fast": return String.valueOf(units.get(index).StatusVulnerabilityData.get((structNUM - 1)).fast);
                    case "slow": return String.valueOf(units.get(index).StatusVulnerabilityData.get((structNUM - 1)).slow);
                    case "dodgy": return String.valueOf(units.get(index).StatusVulnerabilityData.get((structNUM - 1)).dodgy);
                    case "invisible": return String.valueOf(units.get(index).StatusVulnerabilityData.get((structNUM - 1)).invisible);
                    case "ohko": return String.valueOf(units.get(index).StatusVulnerabilityData.get((structNUM - 1)).ohko);
                }
            }
        }

        //Passthrough
        return phrase;
    }

    /**
     * @Author Jemaroo
     * @Function BattleWeapon Tattle Command Translation
     */
    public static String BW_TCT(String phrase, int index, ArrayList<BUTData> units)
    {
        String[] splitPhrase = phrase.split("_");
        int structNUM = 0;

        if(splitPhrase.length == 3)
        {
            switch(splitPhrase[2])
            {
                case "Struct1]": structNUM = 1; break;
                case "Struct2]": structNUM = 2; break;
                case "Struct3]": structNUM = 3; break;
                case "Struct4]": structNUM = 4; break;
                case "Struct5]": structNUM = 5; break;
                case "Struct6]": structNUM = 6; break;
                case "Struct7]": structNUM = 7; break;
                case "Struct8]": structNUM = 8; break;
                case "Struct9]": structNUM = 9; break;
                case "Struct10]": structNUM = 10; break;
                case "Struct11]": structNUM = 11; break;
                case "Struct12]": structNUM = 12; break;
                case "Struct13]": structNUM = 13; break;
                case "Struct14]": structNUM = 14; break;
                case "Struct15]": structNUM = 15; break;
                case "Struct16]": structNUM = 16; break;
            }

            if(structNUM != 0 && units.get(index).BattleWeaponData.size() >= structNUM)
            {
                switch(splitPhrase[1])
                {
                    case "attackName": return units.get(index).BattleWeaponData.get((structNUM - 1)).attackName;
                    case "accuracy": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).accuracy);
                    case "fpCost": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).fp_cost);
                    case "spCost": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).sp_cost);
                    case "superguardState": return SStoName(units.get(index).BattleWeaponData.get((structNUM - 1)).superguard_state);
                    case "sylishMultiplier": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).sylish_multiplier);
                    case "bingoSlotIncChance": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).bingo_slot_inc_chance);
                    case "baseDamageFn": return BDFNToName(units.get(index).BattleWeaponData.get((structNUM - 1)).base_damage_fn);
                    case "baseDamage1": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).base_damage1);
                    case "baseDamage2": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).base_damage2);
                    case "baseDamage3": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).base_damage3);
                    case "baseDamage4": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).base_damage4);
                    case "baseDamage5": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).base_damage5);
                    case "baseDamage6": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).base_damage6);
                    case "baseDamage7": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).base_damage7);
                    case "baseDamage8": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).base_damage8);
                    case "baseFpdamage1": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).base_fpdamage1);
                    case "baseFpdamage2": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).base_fpdamage2);
                    case "baseFpdamage3": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).base_fpdamage3);
                    case "baseFpdamage4": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).base_fpdamage4);
                    case "baseFpdamage5": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).base_fpdamage5);
                    case "baseFpdamage6": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).base_fpdamage6);
                    case "baseFpdamage7": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).base_fpdamage7);
                    case "baseFpdamage8": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).base_fpdamage8);
                    case "CannotTargetMarioOrShellShield": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).CannotTargetMarioOrShellShield);
                    case "CannotTargetPartner": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).CannotTargetPartner);
                    case "CannotTargetEnemy": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).CannotTargetEnemy);
                    case "CannotTargetTreeOrSwitch": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).CannotTargetTreeOrSwitch);
                    case "CannotTargetSystem": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).CannotTargetSystem);
                    case "CannotTargetOppositeAlliance": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).CannotTargetOppositeAlliance);
                    case "CannotTargetOwnAlliance": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).CannotTargetOwnAlliance);
                    case "CannotTargetSelf": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).CannotTargetSelf);
                    case "CannotTargetSameSpecies": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).CannotTargetSameSpecies);
                    case "OnlyTargetSelf": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).OnlyTargetSelf);
                    case "OnlyTargetMario": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).OnlyTargetMario);
                    case "OnlyTargetTreeOrSwitch": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).OnlyTargetTreeOrSwitch);
                    case "OnlyTargetPreferredParts": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).OnlyTargetPreferredParts);
                    case "OnlyTargetSelectParts": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).OnlyTargetSelectParts);
                    case "SingleTarget": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).SingleTarget);
                    case "MultipleTarget": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).MultipleTarget);
                    case "CannotTargetAnything": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).CannotTargetAnything);
                    case "Tattlelike": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).Tattlelike);
                    case "TPFx2": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).CannotTargetFloating2);
                    case "CannotTargetCeiling": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).CannotTargetCeiling);
                    case "CannotTargetFloating": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).CannotTargetFloating);
                    case "CannotTargetGrounded": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).CannotTargetGrounded);
                    case "Jumplike": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).Jumplike);
                    case "Hammerlike": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).Hammerlike);
                    case "ShellTosslike": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).ShellTosslike);
                    case "TPFx8000": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).CannotTargetGroundedVariant);
                    case "RecoilDamage": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).RecoilDamage);
                    case "CanOnlyTargetFrontmost": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).CanOnlyTargetFrontmost);
                    case "CannotTargetShellShield": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).CannotTargetShellShield);
                    case "TPFx4000000": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).CannotTargetCustom);
                    case "TargetSameAllianceDirection": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).TargetSameAllianceDirection);
                    case "TargetOppositeAllianceDirection": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).TargetOppositeAllianceDirection);
                    case "element": return EtoName(units.get(index).BattleWeaponData.get((structNUM - 1)).element);
                    case "damagePattern": return DPtoName(units.get(index).BattleWeaponData.get((structNUM - 1)).damage_pattern);
                    case "acLevel": return ACLtoName(units.get(index).BattleWeaponData.get((structNUM - 1)).ac_level);
                    case "BadgeCanAffectPower": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).BadgeCanAffectPower);
                    case "StatusCanAffectPower": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).StatusCanAffectPower);
                    case "IsChargeable": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).IsChargeable);
                    case "CannotMiss": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).CannotMiss);
                    case "DiminishingReturnsByHit": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).DiminishingReturnsByHit);
                    case "DiminishingReturnsByTarget": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).DiminishingReturnsByTarget);
                    case "PiercesDefense": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).PiercesDefense);
                    case "CanBreakIce": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).CanBreakIce);
                    case "IgnoreTargetStatusVulnerability": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).IgnoreTargetStatusVulnerability);
                    case "SPFx200": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).SPFx200);
                    case "IgnitesIfBurned": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).IgnitesIfBurned);
                    case "PlayActiveFXSound": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).PlayActiveFXSound);
                    case "FlipsShellEnemies": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).FlipsShellEnemies);
                    case "FlipsBombFlippableEnemies": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).FlipsBombFlippableEnemies);
                    case "GroundsWingedEnemies": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).GroundsWingedEnemies);
                    case "SPFx8000": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).SPFx8000);
                    case "CanBeUsedAsConfusedAction": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).CanBeUsedAsConfusedAction);
                    case "Unguardable": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).Unguardable);
                    case "CanHitClonelike": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).CanHitClonelike);
                    case "Electric": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).Electric);
                    case "TopSpiky": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).TopSpiky);
                    case "PreemptiveFrontSpiky": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).PreemptiveFrontSpiky);
                    case "FrontSpiky": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).FrontSpiky);
                    case "Fiery": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).Fiery);
                    case "Icy": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).Icy);
                    case "Poison": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).Poison);
                    case "Explosive": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).Explosive);
                    case "VolatileExplosive": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).VolatileExplosive);
                    case "Payback": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).Payback);
                    case "HoldFast": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).HoldFast);
                    case "PreferMario": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).PreferMario);
                    case "PreferPartner": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).PreferPartner);
                    case "PreferFront": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).PreferFront);
                    case "PreferBack": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).PreferBack);
                    case "PreferSameAlliance": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).PreferSameAlliance);
                    case "PreferOppositeAlliance": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).PreferOppositeAlliance);
                    case "PreferLessHealthy": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).PreferLessHealthy);
                    case "GreatlyPreferLessHealthy": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).GreatlyPreferLessHealthy);
                    case "PreferLowerHP": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).PreferLowerHP);
                    case "PreferHigherHP": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).PreferHigherHP);
                    case "PreferInPeril": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).PreferInPeril);
                    case "TWFx2000": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).TWFx2000);
                    case "ChooseWeightedRandomly": return BooltoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).ChooseWeightedRandomly);
                    case "sleepChance": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).sleep_chance);
                    case "sleepTime": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).sleep_time);
                    case "stopChance": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).stop_chance);
                    case "stopTime": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).stop_time);
                    case "dizzyChance": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).dizzy_chance);
                    case "dizzyTime": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).dizzy_time);
                    case "poisonChance": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).poison_chance);
                    case "poisonTime": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).poison_time);
                    case "poisonStrength": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).poison_strength);
                    case "confuseChance": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).confuse_chance);
                    case "confuseTime": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).confuse_time);
                    case "electricChance": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).electric_chance);
                    case "electricTime": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).electric_time);
                    case "dodgyChance": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).dodgy_chance);
                    case "dodgyTime": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).dodgy_time);
                    case "burnChance": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).burn_chance);
                    case "burnTime": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).burn_time);
                    case "freezeChance": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).freeze_chance);
                    case "freezeTime": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).freeze_time);
                    case "sizeChangeChance": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).size_change_chance);
                    case "sizeChangeTime": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).size_change_time);
                    case "sizeChangeStrength": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).size_change_strength);
                    case "atkChangeChance": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).atk_change_chance);
                    case "atkChangeTime": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).atk_change_time);
                    case "atkChangeStrength": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).atk_change_strength);
                    case "defChangeChance": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).def_change_chance);
                    case "defChangeTime": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).def_change_time);
                    case "defChangeStrength": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).def_change_strength);
                    case "allergicChance": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).allergic_chance);
                    case "allergicTime": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).allergic_time);
                    case "ohkoChance": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).ohko_chance);
                    case "chargeStrength": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).charge_strength);
                    case "fastChance": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).fast_chance);
                    case "fastTime": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).fast_time);
                    case "slowChance": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).slow_chance);
                    case "slowTime": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).slow_time);
                    case "frightChance": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).fright_chance);
                    case "galeForceChance": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).gale_force_chance);
                    case "paybackTime": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).payback_time);
                    case "holdFastTime": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).hold_fast_time);
                    case "invisibleChance": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).invisible_chance);
                    case "invisibleTime": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).invisible_time);
                    case "hpRegenTime": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).hp_regen_time);
                    case "hpRegenStrength": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).hp_regen_strength);
                    case "fpRegenTime": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).fp_regen_time);
                    case "fpRegenStrength": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).fp_regen_strength);
                    case "attackEvt": return ASToName(units.get(index).BattleWeaponData.get((structNUM - 1)).attack_evt);
                    case "stageBackgroundFallweight1": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).stage_background_fallweight1);
                    case "stageBackgroundFallweight2": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).stage_background_fallweight2);
                    case "stageBackgroundFallweight3": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).stage_background_fallweight3);
                    case "stageBackgroundFallweight4": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).stage_background_fallweight4);
                    case "stageNozzleTurnChance": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).stage_nozzle_turn_chance);
                    case "stageNozzleFireChance": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).stage_nozzle_fire_chance);
                    case "stageCeilingFallChance": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).stage_ceiling_fall_chance);
                    case "stageObjectFallChance": return String.valueOf(units.get(index).BattleWeaponData.get((structNUM - 1)).stage_object_fall_chance);
                }
            }
        }

        //Passthrough
        return phrase;
    }

    /**
     * @Author Jemaroo
     * @Function Logic Tattle Command Translation
     */
    public static String LOGIC_TCT(String phrase, int index, ArrayList<BUTData> units)
    {
        String[] splitPhrase = phrase.split("_");
        int structNUM = 0;

        if(splitPhrase.length == 3)
        {
            switch(splitPhrase[2])
            {
                case "Struct1]": structNUM = 1; break;
                case "Struct2]": structNUM = 2; break;
                case "Struct3]": structNUM = 3; break;
                case "Struct4]": structNUM = 4; break;
                case "Struct5]": structNUM = 5; break;
                case "Struct6]": structNUM = 6; break;
                case "Struct7]": structNUM = 7; break;
                case "Struct8]": structNUM = 8; break;
                case "Struct9]": structNUM = 9; break;
                case "Struct10]": structNUM = 10; break;
                case "Struct11]": structNUM = 11; break;
                case "Struct12]": structNUM = 12; break;
                case "Struct13]": structNUM = 13; break;
                case "Struct14]": structNUM = 14; break;
                case "Struct15]": structNUM = 15; break;
                case "Struct16]": structNUM = 16; break;
                case "Struct17]": structNUM = 17; break;
                case "Struct18]": structNUM = 18; break;
                case "Struct19]": structNUM = 19; break;
            }

            if(structNUM != 0 && units.get(index).BattleUnitDefenseAttrData.size() >= structNUM)
            {
                switch(splitPhrase[1])
                {
                    case "ElementalWeakness": return ElementalWeaknessLogic(units.get(index).BattleUnitDefenseAttrData.get((structNUM - 1)));
                    case "ElementalImmunity": return ElementalImmunityLogic(units.get(index).BattleUnitDefenseAttrData.get((structNUM - 1)));
                }
            }
            else if(structNUM != 0 && units.get(index).StatusVulnerabilityData.size() >= structNUM)
            {
                switch(splitPhrase[1])
                {
                    case "StatusStrength": return StatusStrengthLogic(units.get(index).StatusVulnerabilityData.get((structNUM - 1)));
                    case "StatusWeakness": return StatusVulnerabilityLogic(units.get(index).StatusVulnerabilityData.get((structNUM - 1)));
                }
            }
            else if(structNUM != 0 && units.get(index).BattleWeaponData.size() >= structNUM)
            {
                switch(splitPhrase[1])
                {
                    case "SuperguardYN": return SStoYN(units.get(index).BattleWeaponData.get((structNUM - 1)).superguard_state);
                    case "StatusAffliction": return StatusAfflictionLogic(units.get(index).BattleWeaponData.get((structNUM - 1)));
                }
            }
        }

        //Passthrough
        return phrase;
    }

    /**
     * @Author Jemaroo
     * @Function Turns the value of a Swallow Attribute into it's name
     */
    public static String SAtoName(int SA)
    {
        switch(SA)
        {
            case 1: return "Swallow Bomb";
            case 2: return "Spit into Other Enemy";
            case 4: return "Spit Fireball";
            case 8: return "Swallow Part of Enemy";
            default: return "None";
        }
    }

    /**
     * @Author Jemaroo
     * @Function Turns the value of a boolean into a yes/no
     */
    public static String BooltoYN(boolean bool)
    {
        if(bool)
        {
            return "Yes";
        }
        else
        {
            return "No";
        }
    }

    /**
     * @Author Jemaroo
     * @Function Turns the value of a Defense Attribute into it's name
     */
    public static String DAtoName(int DA)
    {
        switch(DA)
        {
            case 1: return "Weak To";
            case 2: return "Immune To";
            case 3: return "Absorbs";
            case 4: return "Immune To (Except Gulp)";
            default: return "Normal";
        }
    }

    /**
     * @Author Jemaroo
     * @Function Turns the value of a Superguard State into it's name
     */
    public static String SStoName(int SS)
    {
        switch(SS)
        {
            case 1: return "Superguardable with Recoil";
            case 2: return "Superguardable without Recoil";
            default: return "Unsuperguardable";
        }
    }

    /**
     * @Author Jemaroo
     * @Function Turns the value of a Superguard State into a yes/no
     */
    public static String SStoYN(int SS)
    {
        switch(SS)
        {
            case 1:
            case 2: return "Yes";
            default: return "No";
        }
    }

    /**
     * @Author Jemaroo
     * @Function Turns the value of a Element into it's name
     */
    public static String EtoName(int E)
    {
        switch(E)
        {
            case 1: return "Fire";
            case 2: return "Ice";
            case 3: return "Explosive";
            case 4: return "Electric";
            default: return "Normal";
        }
    }

    /**
     * @Author Jemaroo
     * @Function Turns the value of a Damage Pattern into it's name
     */
    public static String DPtoName(int DP)
    {
        switch(DP)
        {
            case 4: return "Gulp Knocked onto Ground 1";
            case 5: return "Gulp Knocked onto Ground 2";
            case 6: return "Gulp Knocked into next Target";
            case 8: return "Remove Segment";
            case 10: return "Short Time Squashed";
            case 11: return "Medium Time Squashed";
            case 12: return "Long Time Squashed";
            case 13: return "Inked";
            case 14: return "Super Hammer Knocked Back";
            case 16: return "Ultra Hammer Knocked Back";
            case 19: return "Ultra Hammer Finisher";
            case 20: return "Confusion Effect";
            case 21: return "Love Slap Spin 1";
            case 22: return "Love Slap Spin 2";
            case 23: return "Love Slap Quick Spin 1";
            case 24: return "Love Slap Quick Spin 2";
            case 25: return "Blown Away";
            case 27: return "Play Additional Ending Hurt Animation";
            default: return "None";
        }
    }

    /**
     * @Author Jemaroo
     * @Function Turns the value of a AC Level into it's name
     */
    public static String ACLtoName(int ACL)
    {
        switch(ACL)
        {
            case 1: return "Very Easy";
            case 2: return "Easy";
            case 3: return "Normal";
            case 4: return "Difficult";
            case 5: return "Very Difficult";
            case 6: return "Extremely Difficult";
            default: return "Extremely Easy";
        }
    }

    /**
     * @Author Jemaroo
     * @Function Turns the value of a Base Damage fn into it's name
     */
    public static String BDFNToName(long BDFN)
    {
        if(BDFN == 2149096624L) return "Get Jump Power";
        if(BDFN == 2149096504L) return "Get Hammer Power";
        if(BDFN == 2149096752L) return "Get Default Power";
        if(BDFN == 2149096436L) return "Get Badge Stacked Jump Power";
        if(BDFN == 2149095976L) return "Get Tornado Power";
        if(BDFN == 2149096368L) return "Get Badge Stacked Hammer Power";
        if(BDFN == 2149788844L) return "Get Art Attack Power";
        if(BDFN == 2149801084L) return "Get Supernova Power";
        if(BDFN == 2149879880L) return "Get Earth Tremor Power";
        if(BDFN == 2149096096L) return "Get Poison Shroom Power";
        if(BDFN == 2149096272L) return "Get Partner Power";
        if(BDFN == 2149096156L) return "Get Partner Action Command Power";
        return "None";
    }

    /**
     * @Author Jemaroo
     * @Function Turns the value of a Attack Script into it's name
     */
    public static String ASToName(long AS)
    {
        if(AS == 2150980848L) return "Jump Script";
        if(AS == 2150984700L) return "Spin Jump Script";
        if(AS == 2150989540L) return "Spring Jump Script";
        if(AS == 2151021624L) return "First Strike Super Hammer Script";
        if(AS == 2151771200L) return "Hammer Script";
        if(AS == 2151017384L) return "Super Hammer Script";
        if(AS == 2151008952L) return "Multibounce Script";
        if(AS == 2151771096L) return "Power Jump Script";
        if(AS == 2151771124L) return "Mega Jump Script";
        if(AS == 2151005592L) return "Power Bounce Script";
        if(AS == 2150998876L) return "Tornado Jump Script";
        if(AS == 2151771152L) return "Shrink Stomp Script";
        if(AS == 2151771168L) return "Sleepy Stomp Script";
        if(AS == 2151771184L) return "Soft Stomp Script";
        if(AS == 2151771228L) return "Power Smash Script";
        if(AS == 2151021700L) return "Quake Hammer Script";
        if(AS == 2151025080L) return "Mega Quake Script";
        if(AS == 2151027840L) return "Hammer Throw Script";
        if(AS == 2151771284L) return "Piercing Blow Script";
        if(AS == 2151030656L) return "Fire Drive Script";
        if(AS == 2151771256L) return "Ice Smash Script";
        if(AS == 2151771312L) return "Charge Script";
        if(AS == 2151771340L) return "Charge P Script";
        if(AS == 2151771368L) return "Super Charge Script";
        if(AS == 2151771396L) return "Super Charge P Script";
        if(AS == 2151366176L) return "Clock Out Script";
        if(AS == 2151371896L) return "Art Attack Script";
        if(AS == 2151373376L) return "Supernova Script";
        if(AS == 2151800328L) return "Sweet Treat Script";
        if(AS == 2151800356L) return "Sweat Feast Script";
        if(AS == 2151380272L) return "Earth Tremor Script";
        if(AS == 2151383104L) return "Power Lift Script";
        if(AS == 2151384456L) return "Showstopper Script";
        if(AS == 2151042348L) return "Item Heal Script";
        if(AS == 2151046152L) return "Pow Block Script";
        if(AS == 2151048784L) return "Fire Flower Script";
        if(AS == 2151771704L) return "Thunder Rage Script";
        if(AS == 2151771732L) return "Thunder Bolt Script";
        if(AS == 2151052500L) return "Shooting Star Script";
        if(AS == 2151052808L) return "Ice Storm Script";
        if(AS == 2151054240L) return "Earth Quake Script";
        if(AS == 2151055800L) return "Boo's Sheet Script";
        if(AS == 2151056988L) return "Volt Shroom Script";
        if(AS == 2151057288L) return "Repel Cape Script";
        if(AS == 2151771760L) return "Ruin Powder Script";
        if(AS == 2151057728L) return "Sleepy Sheep Script";
        if(AS == 2151058856L) return "Stopwatch Script";
        if(AS == 2151059392L) return "Dizzy Dial Script";
        if(AS == 2151771788L) return "Power Punch Script";
        if(AS == 2151771816L) return "Courage Shell Script";
        if(AS == 2151060108L) return "HP Drain Script";
        if(AS == 2151061424L) return "Trade Off Script";
        if(AS == 2151771844L) return "Mini Mr. Mini Script";
        if(AS == 2151771872L) return "Mr. Softener Script";
        if(AS == 2151771900L) return "Tasty Tonic Script";
        if(AS == 2151063188L) return "Slow Shroom Script";
        if(AS == 2151063488L) return "Gradual Syrup Script";
        if(AS == 2151063736L) return "Point Swap Script";
        if(AS == 2151064556L) return "Fright Mask Script";
        if(AS == 2151065416L) return "Mystery Script";
        if(AS == 2151771928L) return "Spite Pouch Script";
        if(AS == 2151771956L) return "Koopa Curse Script";
        if(AS == 2151066876L) return "Item Heal and Status Script";
        if(AS == 2151071508L) return "Thrown Item Script";
        if(AS == 2151074004L) return "Poison Shroom Script";
        if(AS == 2151074540L) return "Trial Stew Script";
        if(AS == 2151126228L) return "Body Slam Script";
        if(AS == 2151129028L) return "Gale Force Script";
        if(AS == 2151131108L) return "Lip Lock Script";
        if(AS == 2151134608L) return "Dodgy Fog Script";
        if(AS == 2151141776L) return "Ground Pound Script";
        if(AS == 2151145876L) return "Gulp Script";
        if(AS == 2151149876L) return "Mini Egg Script";
        if(AS == 2151155720L) return "Stampede Script";
        if(AS == 2151162032L) return "Love Slap Script";
        if(AS == 2151168392L) return "Tease Script";
        if(AS == 2151176472L) return "Kiss Thief Script";
        if(AS == 2151180316L) return "Smooch Script";
        if(AS == 2151186208L) return "Bomb First Strike Script";
        if(AS == 2151187492L) return "Bomb Script";
        if(AS == 2151196812L) return "Bomb Squad Script";
        if(AS == 2151195844L) return "Hold Fast Script";
        if(AS == 2151191372L) return "Bob-ombast Script";
        if(AS == 2151203296L) return "Shade Fist Script";
        if(AS == 2151205620L) return "Veil Script";
        if(AS == 2151207340L) return "Fiery Jinx Script";
        if(AS == 2151209132L) return "Infatuate Script";
        if(AS == 2151218728L) return "Shell Toss First Strike Script";
        if(AS == 2151219672L) return "Shell Toss Script";
        if(AS == 2151222888L) return "Power Shell Script";
        if(AS == 2151225696L) return "Shell Shield Script";
        if(AS == 2151229048L) return "Shell Slam Script";
        if(AS == 2151236008L) return "Headbonk Script";
        if(AS == 2151239408L) return "Tattle Script";
        if(AS == 2151240996L) return "Multibonk Script";
        if(AS == 2151245308L) return "Rally Wink Script";
        return "None";
    }

    /**
     * @Author Jemaroo
     * @Function Determines the first Elemental Weakness
     */
    public static String ElementalWeaknessLogic(BattleUnitDefenseAttr unit)
    {
        if(unit.normal == 1)
        {
            return "Normal";
        }
        else if(unit.fire == 1)
        {
            return "Fire";
        }
        else if(unit.ice == 1)
        {
            return "Ice";
        }
        else if(unit.explosion == 1)
        {
            return "Explosion";
        }
        else if(unit.electric == 1)
        {
            return "Electric";
        }
        else
        {
            return "None";
        }
    }

    /**
     * @Author Jemaroo
     * @Function Determines the first Elemental Immunity
     */
    public static String ElementalImmunityLogic(BattleUnitDefenseAttr unit)
    {
        if(unit.normal == 2)
        {
            return "Normal";
        }
        else if(unit.fire == 2)
        {
            return "Fire";
        }
        else if(unit.ice == 2)
        {
            return "Ice";
        }
        else if(unit.explosion == 2)
        {
            return "Explosion";
        }
        else if(unit.electric == 2)
        {
            return "Electric";
        }
        else
        {
            return "None";
        }
    }

    /**
     * @Author Jemaroo
     * @Function Determines the lowest Status Vulnerability
     */
    public static String StatusStrengthLogic(StatusVulnerability unit)
    {
        String lowestName = "Sleep";
        int minimumValue = unit.sleep;

        if (unit.stop < minimumValue) {minimumValue = unit.stop; lowestName = "Stop";}
        if (unit.dizzy < minimumValue) {minimumValue = unit.dizzy; lowestName = "Dizzy";}
        if (unit.poison < minimumValue) {minimumValue = unit.poison; lowestName = "Poison";}
        if (unit.confuse < minimumValue) {minimumValue = unit.confuse; lowestName = "Confuse";}
        //if (unit.electric < minimumValue) {minimumValue = unit.electric; lowestName = "Electric";}
        if (unit.burn < minimumValue) {minimumValue = unit.burn; lowestName = "Burn";}
        if (unit.freeze < minimumValue) {minimumValue = unit.freeze; lowestName = "Freeze";}
        //if (unit.huge < minimumValue) {minimumValue = unit.huge; lowestName = "Huge";}
        if (unit.tiny < minimumValue) {minimumValue = unit.tiny; lowestName = "Tiny";}
        //if (unit.attack_up < minimumValue) {minimumValue = unit.attack_up; lowestName = "Attack Up";}
        if (unit.attack_down < minimumValue) {minimumValue = unit.attack_down; lowestName = "Attack Down";}
        //if (unit.defense_up < minimumValue) {minimumValue = unit.defense_up; lowestName = "Defense Up";}
        if (unit.defense_down < minimumValue) {minimumValue = unit.defense_down; lowestName = "Defense Down";}
        if (unit.allergic < minimumValue) {minimumValue = unit.allergic; lowestName = "Allergic";}
        if (unit.fright < minimumValue) {minimumValue = unit.fright; lowestName = "Fright";}
        if (unit.gale_force < minimumValue) {minimumValue = unit.gale_force; lowestName = "Gale Force";}
        //if (unit.fast < minimumValue) {minimumValue = unit.fast; lowestName = "Fast";}
        if (unit.slow < minimumValue) {minimumValue = unit.slow; lowestName = "Slow";}
        //if (unit.dodgy < minimumValue) {minimumValue = unit.dodgy; lowestName = "Dodgy";}
        //if (unit.invisible < minimumValue) {minimumValue = unit.invisible; lowestName = "Invisible";}
        if (unit.ohko < minimumValue) {minimumValue = unit.ohko; lowestName = "OHKO";}

        if(unit.sleep >= 100 && lowestName.equals("Sleep")) {lowestName = "None";}

        return lowestName;
    }

    /**
     * @Author Jemaroo
     * @Function Determines the highest Status Vulnerability
     */
    public static String StatusVulnerabilityLogic(StatusVulnerability unit)
    {
        String highestName = "Sleep";
        int maximumValue = unit.sleep;

        if (unit.stop > maximumValue) {maximumValue = unit.stop; highestName = "Stop";}
        if (unit.dizzy > maximumValue) {maximumValue = unit.dizzy; highestName = "Dizzy";}
        if (unit.poison > maximumValue) {maximumValue = unit.poison; highestName = "Poison";}
        if (unit.confuse > maximumValue) {maximumValue = unit.confuse; highestName = "Confuse";}
        //if (unit.electric > maximumValue) {maximumValue = unit.electric; highestName = "Electric";}
        if (unit.burn > maximumValue) {maximumValue = unit.burn; highestName = "Burn";}
        if (unit.freeze > maximumValue) {maximumValue = unit.freeze; highestName = "Freeze";}
        //if (unit.huge > maximumValue) {maximumValue = unit.huge; highestName = "Huge";}
        if (unit.tiny > maximumValue) {maximumValue = unit.tiny; highestName = "Tiny";}
        //if (unit.attack_up > maximumValue) {maximumValue = unit.attack_up; highestName = "Attack Up";}
        if (unit.attack_down > maximumValue) {maximumValue = unit.attack_down; highestName = "Attack Down";}
        //if (unit.defense_up > maximumValue) {maximumValue = unit.defense_up; highestName = "Defense Up";}
        if (unit.defense_down > maximumValue) {maximumValue = unit.defense_down; highestName = "Defense Down";}
        if (unit.allergic > maximumValue) {maximumValue = unit.allergic; highestName = "Allergic";}
        if (unit.fright > maximumValue) {maximumValue = unit.fright; highestName = "Fright";}
        if (unit.gale_force > maximumValue) {maximumValue = unit.gale_force; highestName = "Gale Force";}
        //if (unit.fast > maximumValue) {maximumValue = unit.fast; highestName = "Fast";}
        if (unit.slow > maximumValue) {maximumValue = unit.slow; highestName = "Slow";}
        //if (unit.dodgy > maximumValue) {maximumValue = unit.dodgy; highestName = "Dodgy";}
        //if (unit.invisible > maximumValue) {maximumValue = unit.invisible; highestName = "Invisible";}
        if (unit.ohko > maximumValue) {maximumValue = unit.ohko; highestName = "OHKO";}

        if(unit.sleep == 0 && highestName.equals("Sleep")) {highestName = "None";}

        return highestName;
    }

    /**
     * @Author Jemaroo
     * @Function Determines the highest Status Affliction
     */
    public static String StatusAfflictionLogic(BattleWeapon unit)
    {
        String highestName = "Sleep";
        int maximumValue = unit.sleep_chance;

        if (unit.stop_chance > maximumValue) {maximumValue = unit.stop_chance; highestName = "Stop";}
        if (unit.dizzy_chance > maximumValue) {maximumValue = unit.dizzy_chance; highestName = "Dizzy";}
        if (unit.poison_chance > maximumValue) {maximumValue = unit.poison_chance; highestName = "Poison";}
        if (unit.confuse_chance > maximumValue) {maximumValue = unit.confuse_chance; highestName = "Confuse";}
        if (unit.electric_chance > maximumValue) {maximumValue = unit.electric_chance; highestName = "Electric";}
        if (unit.dodgy_chance > maximumValue) {maximumValue = unit.dodgy_chance; highestName = "Dodgy";}
        if (unit.burn_chance > maximumValue) {maximumValue = unit.burn_chance; highestName = "Burn";}
        if (unit.freeze_chance > maximumValue) {maximumValue = unit.freeze_chance; highestName = "Freeze";}
        if (unit.size_change_chance > maximumValue && unit.size_change_strength < 128) {maximumValue = unit.size_change_chance; highestName = "Huge";}
        if (unit.size_change_chance > maximumValue && unit.size_change_strength > 127) {maximumValue = unit.size_change_chance; highestName = "Tiny";}
        if (unit.atk_change_chance > maximumValue && unit.atk_change_strength < 128) {maximumValue = unit.atk_change_chance; highestName = "Attack Up";}
        if (unit.atk_change_chance > maximumValue && unit.atk_change_strength > 127) {maximumValue = unit.atk_change_chance; highestName = "Attack Down";}
        if (unit.def_change_chance > maximumValue && unit.def_change_strength < 128) {maximumValue = unit.def_change_chance; highestName = "Defense Up";}
        if (unit.def_change_chance > maximumValue && unit.def_change_strength > 127) {maximumValue = unit.def_change_chance; highestName = "Defense Down";}
        if (unit.allergic_chance > maximumValue) {maximumValue = unit.allergic_chance; highestName = "Allergic";}
        if (unit.ohko_chance > maximumValue) {maximumValue = unit.ohko_chance; highestName = "OHKO";}
        if (unit.fast_chance > maximumValue) {maximumValue = unit.fast_chance; highestName = "Fast";}
        if (unit.slow_chance > maximumValue) {maximumValue = unit.slow_chance; highestName = "Slow";}
        if (unit.fright_chance > maximumValue) {maximumValue = unit.fright_chance; highestName = "Fright";}
        if (unit.gale_force_chance > maximumValue) {maximumValue = unit.gale_force_chance; highestName = "Gale Force";}
        if (unit.invisible_chance > maximumValue) {maximumValue = unit.invisible_chance; highestName = "Invisible";}

        if(unit.sleep_chance == 0 && highestName.equals("Sleep")) {highestName = "None";}

        return highestName;
    }
}