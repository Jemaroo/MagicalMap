import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.parser.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * @Author Jemaroo
 * @Function Main Functions for reading and parsing input data
 */
public class BUTMain 
{
    /**
     * @Author Jemaroo
     * @Function Searches for matching files in a given directory
     */
    public static ArrayList<File> findMatchingFiles(File directory, ArrayList<String> targetFilenames) 
    {
        ArrayList<File> matchingFiles = new ArrayList<>();
        File[] files = directory.listFiles();

        if (files != null) 
        {
            for (File file : files) 
            {
                if (file.isDirectory()) 
                {
                    matchingFiles.addAll(findMatchingFiles(file, targetFilenames));
                } 
                else if (targetFilenames.contains(file.getName())) 
                {
                    matchingFiles.add(file);
                }
            }
        }

        return matchingFiles;
    }

    /**
     * @Author Jemaroo
     * @Function Will attempt to read the given file and the json file and parse them into an array of UnitData
     */
    public static ArrayList<BUTData> getTableData(File givenFile)
    {
        File jsonFile = new File("src\\UnitData.json");
        byte[] givenFiledata = ByteUtils.readData(givenFile);

        ArrayList<BUTData> units = new ArrayList<>();

        //Check for correct dol/rel in json
        try
        {
            JSONParser parser = new JSONParser();
            JSONObject root = (JSONObject)parser.parse(new FileReader(jsonFile));
            JSONArray fileArray = (JSONArray)root.get("File");
            JSONObject fileObj = null;
            JSONArray structArray = null;

            for(int i = 0; i < fileArray.size(); i ++)
            {
                fileObj = (JSONObject)fileArray.get(i);
                String name = (String)fileObj.get("Name");
                if(givenFile.getName().equalsIgnoreCase(name))
                {  
                    structArray = (JSONArray)fileObj.get("Struct");
                    System.out.println(name + " File Structs Found in: " + jsonFile.getName());
                    break;
                }
            }

            int unitCount = 0;
            JSONArray unitNameArray = null;
            int nameCounter = 0;
            if(structArray != null)
            {
                //Grabbing the amount of units in the dol/rel
                unitCount = ((Long)(((JSONObject)structArray.get(0)).get("Count"))).intValue();
                System.out.println("Unit Count Found: " + unitCount + "\n");

                //Grabbing the Unit name List
                unitNameArray = (JSONArray)fileObj.get("Units");
            }

            JSONArray unitOffsetArray = (JSONArray)((JSONObject)structArray.get(0)).get("Offsets");
            JSONArray multipleArray = (JSONArray)((JSONObject)structArray.get(0)).get("Multiple");
            
            //BattleUnitKind
            for(int i = 0; i < unitCount; i++)
            {
                BUTData unit = new BUTData();

                //Extract unit name and add it to unit
                unit.name = unitNameArray.get(nameCounter).toString();

                BattleUnitKind tempBattleUnitKind = new BattleUnitKind();
                
                //HP
                int locator = tempBattleUnitKind.HP_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitKind.HP = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]);

                //DangerHP
                locator = tempBattleUnitKind.dangerHP_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitKind.dangerHP = ByteUtils.bytesToInt(givenFiledata[locator]);

                //PerilHP
                locator = tempBattleUnitKind.perilHP_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitKind.perilHP = ByteUtils.bytesToInt(givenFiledata[locator]);

                //Level
                locator = tempBattleUnitKind.level_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitKind.level = ByteUtils.bytesToInt(givenFiledata[locator]);

                //BonusXP
                locator = tempBattleUnitKind.bonusXP_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitKind.bonusXP = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //BonusCoin
                locator = tempBattleUnitKind.bonusCoin_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitKind.bonusCoin = ByteUtils.bytesToInt(givenFiledata[locator]);

                //BonusCoinRate
                locator = tempBattleUnitKind.bonusCoinRate_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitKind.bonusCoinRate = ByteUtils.bytesToInt(givenFiledata[locator]);

                //BaseCoin
                locator = tempBattleUnitKind.baseCoin_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitKind.baseCoin = ByteUtils.bytesToInt(givenFiledata[locator]);

                //RunRate
                locator = tempBattleUnitKind.runRate_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitKind.runRate = ByteUtils.bytesToInt(givenFiledata[locator]);

                //pbCap
                locator = tempBattleUnitKind.pbCap_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitKind.pbCap = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]);

                //SwallowChance
                locator = tempBattleUnitKind.swallowChance_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitKind.swallowChance = ByteUtils.bytesToInt(givenFiledata[locator]);

                //SwallowAttributes
                locator = tempBattleUnitKind.swallowAttribute_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitKind.swallowAttribute = ByteUtils.bytesToInt(givenFiledata[locator]);

                //UltraHammerKnockChance
                locator = tempBattleUnitKind.ultraHammerKnockChance_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitKind.ultraHammerKnockChance = ByteUtils.bytesToInt(givenFiledata[locator]);

                //ItemStealParameter
                locator = tempBattleUnitKind.itemStealParameter_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitKind.itemStealParameter = ByteUtils.bytesToInt(givenFiledata[locator]);

                //PartsAttributeFlags
                locator = tempBattleUnitKind.UnitAttributeFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                int bitfieldData = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                tempBattleUnitKind.MapObj = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.MapObj);
                tempBattleUnitKind.OutOfReach = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.OutOfReach);
                tempBattleUnitKind.Unquakeable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.Unquakeable);
                tempBattleUnitKind.IsInvisible = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.IsInvisible);
                tempBattleUnitKind.IsVeiled = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.IsVeiled);
                tempBattleUnitKind.ShellShielded = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.ShellShielded);
                tempBattleUnitKind.NeverTargetable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.NeverTargetable);
                tempBattleUnitKind.LimitSwitch = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.LimitSwitch);
                tempBattleUnitKind.DisableZeroGravityFloat = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.DisableZeroGravityFloat);
                tempBattleUnitKind.DisableZeroGravityImmobility = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.DisableZeroGravityImmobility);
                tempBattleUnitKind.immuneToUltraHammerKnock = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.immuneToUltraHammerKnock);
                tempBattleUnitKind.IsUndead = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.IsUndead);
                tempBattleUnitKind.IsCorpse = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.IsCorpse);
                tempBattleUnitKind.IsLeader = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.IsLeader);
                tempBattleUnitKind.CannotTakeActions = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.CannotTakeActions);
                tempBattleUnitKind.NotSpunByLoveSlap = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.NotSpunByLoveSlap);
                tempBattleUnitKind.DisableDamageStars = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.DisableDamageStars);
                tempBattleUnitKind.DisableAllPartVisibility = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.DisableAllPartVisibility);
                tempBattleUnitKind.DisableHPGauge = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.DisableHPGauge);
                tempBattleUnitKind.LookCamera = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.LookCamera);
                tempBattleUnitKind.NonCombatant = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.NonCombatant);
                tempBattleUnitKind.NoShadow = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.NoShadow);
                tempBattleUnitKind.DisableDamage = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.DisableDamage);

                //Add to arrayList
                unit.addBattleUnitKindStruct(tempBattleUnitKind);

                //Check for multiples
                int multipleCheck = multipleExists(multipleArray, unitNameArray.get(nameCounter).toString());
                if(multipleArray.size() != 0 && multipleCheck != -1)
                {
                    int multipleCount = Math.toIntExact((Long)(((JSONObject)(multipleArray.get(multipleCheck))).get("Amount")));

                    for(int j = 1; j < multipleCount; j++)
                    {
                        tempBattleUnitKind = new BattleUnitKind();

                        //HP
                        locator = tempBattleUnitKind.HP_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitKind.HP = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]);

                        //DangerHP
                        locator = tempBattleUnitKind.dangerHP_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitKind.dangerHP = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //PerilHP
                        locator = tempBattleUnitKind.perilHP_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitKind.perilHP = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Level
                        locator = tempBattleUnitKind.level_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitKind.level = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //BonusXP
                        locator = tempBattleUnitKind.bonusXP_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitKind.bonusXP = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //BonusCoin
                        locator = tempBattleUnitKind.bonusCoin_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitKind.bonusCoin = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //BonusCoinRate
                        locator = tempBattleUnitKind.bonusCoinRate_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitKind.bonusCoinRate = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //BaseCoin
                        locator = tempBattleUnitKind.baseCoin_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitKind.baseCoin = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //RunRate
                        locator = tempBattleUnitKind.runRate_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitKind.runRate = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //pbCap
                        locator = tempBattleUnitKind.pbCap_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitKind.pbCap = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]);

                        //SwallowChance
                        locator = tempBattleUnitKind.swallowChance_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitKind.swallowChance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //SwallowAttribute
                        locator = tempBattleUnitKind.swallowAttribute_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitKind.swallowAttribute = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //UltraHammerKnockChance
                        locator = tempBattleUnitKind.ultraHammerKnockChance_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitKind.ultraHammerKnockChance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //ItemStealParameters
                        locator = tempBattleUnitKind.itemStealParameter_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitKind.itemStealParameter = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //PartsAttributeFlags
                        locator = tempBattleUnitKind.UnitAttributeFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        bitfieldData = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                        tempBattleUnitKind.MapObj = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.MapObj);
                        tempBattleUnitKind.OutOfReach = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.OutOfReach);
                        tempBattleUnitKind.Unquakeable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.Unquakeable);
                        tempBattleUnitKind.IsInvisible = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.IsInvisible);
                        tempBattleUnitKind.IsVeiled = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.IsVeiled);
                        tempBattleUnitKind.ShellShielded = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.ShellShielded);
                        tempBattleUnitKind.NeverTargetable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.NeverTargetable);
                        tempBattleUnitKind.LimitSwitch = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.LimitSwitch);
                        tempBattleUnitKind.DisableZeroGravityFloat = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.DisableZeroGravityFloat);
                        tempBattleUnitKind.DisableZeroGravityImmobility = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.DisableZeroGravityImmobility);
                        tempBattleUnitKind.immuneToUltraHammerKnock = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.immuneToUltraHammerKnock);
                        tempBattleUnitKind.IsUndead = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.IsUndead);
                        tempBattleUnitKind.IsCorpse = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.IsCorpse);
                        tempBattleUnitKind.IsLeader = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.IsLeader);
                        tempBattleUnitKind.CannotTakeActions = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.CannotTakeActions);
                        tempBattleUnitKind.NotSpunByLoveSlap = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.NotSpunByLoveSlap);
                        tempBattleUnitKind.DisableDamageStars = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.DisableDamageStars);
                        tempBattleUnitKind.DisableAllPartVisibility = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.DisableAllPartVisibility);
                        tempBattleUnitKind.DisableHPGauge = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.DisableHPGauge);
                        tempBattleUnitKind.LookCamera = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.LookCamera);
                        tempBattleUnitKind.NonCombatant = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.NonCombatant);
                        tempBattleUnitKind.NoShadow = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.NoShadow);
                        tempBattleUnitKind.DisableDamage = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.DisableDamage);

                        //Add to arrayList
                        unit.addBattleUnitKindStruct(tempBattleUnitKind);
                    }

                    //Incrementing i to account for multiples
                    i += (multipleCount - 1);
                }

                //Adding temp unit to units list
                units.add(unit);
                nameCounter++;
            }

            unitOffsetArray = (JSONArray)((JSONObject)structArray.get(1)).get("Offsets");
            multipleArray = (JSONArray)((JSONObject)structArray.get(1)).get("Multiple");
            unitCount = ((Long)(((JSONObject)structArray.get(1)).get("Count"))).intValue();
            nameCounter = 0;
            //BattleUnitDefense
            for(int i = 0; i < unitCount; i++)
            {
                BattleUnitDefense tempBattleUnitDefense = new BattleUnitDefense();
                
                //Normal
                int locator = tempBattleUnitDefense.normal_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitDefense.normal = ByteUtils.bytesToInt(givenFiledata[locator]);

                //Fire
                locator = tempBattleUnitDefense.fire_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitDefense.fire = ByteUtils.bytesToInt(givenFiledata[locator]);

                //Ice
                locator = tempBattleUnitDefense.ice_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitDefense.ice = ByteUtils.bytesToInt(givenFiledata[locator]);

                //Explosion
                locator = tempBattleUnitDefense.explosion_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitDefense.explosion = ByteUtils.bytesToInt(givenFiledata[locator]);

                //Electric
                locator = tempBattleUnitDefense.electric_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitDefense.electric = ByteUtils.bytesToInt(givenFiledata[locator]);

                //Add to arrayList
                units.get(nameCounter).addBattleUnitDefenseStruct(tempBattleUnitDefense);

                //Check for multiples
                int multipleCheck = multipleExists(multipleArray, unitNameArray.get(nameCounter).toString());
                if(multipleArray.size() != 0 && multipleCheck != -1)
                {
                    int multipleCount = Math.toIntExact((Long)(((JSONObject)(multipleArray.get(multipleCheck))).get("Amount")));

                    for(int j = 1; j < multipleCount; j++)
                    {
                        tempBattleUnitDefense = new BattleUnitDefense();

                        //Normal
                        locator = tempBattleUnitDefense.normal_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitDefense.normal = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Fire
                        locator = tempBattleUnitDefense.fire_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitDefense.fire = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Ice
                        locator = tempBattleUnitDefense.ice_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitDefense.ice = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Explosion
                        locator = tempBattleUnitDefense.explosion_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitDefense.explosion = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Electric
                        locator = tempBattleUnitDefense.electric_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitDefense.electric = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Add to arrayList
                        units.get(nameCounter).addBattleUnitDefenseStruct(tempBattleUnitDefense);
                    }

                    //Incrementing i to account for multiples
                    i += (multipleCount - 1);
                }

                nameCounter++;
                if(nameCounter >= units.size())
                {
                    break;
                }
            }
            
            unitOffsetArray = (JSONArray)((JSONObject)structArray.get(2)).get("Offsets");
            multipleArray = (JSONArray)((JSONObject)structArray.get(2)).get("Multiple");
            unitCount = ((Long)(((JSONObject)structArray.get(2)).get("Count"))).intValue();
            nameCounter = 0;
            //BattleUnitDefenseAttr
            for(int i = 0; i < unitCount; i++)
            {
                BattleUnitDefenseAttr tempBattleUnitDefenseAttr = new BattleUnitDefenseAttr();
                
                //Normal
                int locator = tempBattleUnitDefenseAttr.normal_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitDefenseAttr.normal = ByteUtils.bytesToInt(givenFiledata[locator]);

                //Fire
                locator = tempBattleUnitDefenseAttr.fire_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitDefenseAttr.fire = ByteUtils.bytesToInt(givenFiledata[locator]);

                //Ice
                locator = tempBattleUnitDefenseAttr.ice_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitDefenseAttr.ice = ByteUtils.bytesToInt(givenFiledata[locator]);

                //Explosion
                locator = tempBattleUnitDefenseAttr.explosion_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitDefenseAttr.explosion = ByteUtils.bytesToInt(givenFiledata[locator]);

                //Electric
                locator = tempBattleUnitDefenseAttr.electric_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempBattleUnitDefenseAttr.electric = ByteUtils.bytesToInt(givenFiledata[locator]);

                //Add to arrayList
                units.get(nameCounter).addBattleUnitDefenseAttrStruct(tempBattleUnitDefenseAttr);

                //Check for multiples
                int multipleCheck = multipleExists(multipleArray, unitNameArray.get(nameCounter).toString());
                if(multipleArray.size() != 0 && multipleCheck != -1)
                {
                    int multipleCount = Math.toIntExact((Long)(((JSONObject)(multipleArray.get(multipleCheck))).get("Amount")));

                    for(int j = 1; j < multipleCount; j++)
                    {
                        tempBattleUnitDefenseAttr = new BattleUnitDefenseAttr();

                        //Normal
                        locator = tempBattleUnitDefenseAttr.normal_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitDefenseAttr.normal = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Fire
                        locator = tempBattleUnitDefenseAttr.fire_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitDefenseAttr.fire = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Ice
                        locator = tempBattleUnitDefenseAttr.ice_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitDefenseAttr.ice = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Explosion
                        locator = tempBattleUnitDefenseAttr.explosion_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitDefenseAttr.explosion = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Electric
                        locator = tempBattleUnitDefenseAttr.electric_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempBattleUnitDefenseAttr.electric = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Add to arrayList
                        units.get(nameCounter).addBattleUnitDefenseAttrStruct(tempBattleUnitDefenseAttr);
                    }

                    //Incrementing i to account for multiples
                    i += (multipleCount - 1);
                }

                nameCounter++;
                if(nameCounter >= units.size())
                {
                    break;
                }
            }

            unitOffsetArray = (JSONArray)((JSONObject)structArray.get(3)).get("Offsets");
            multipleArray = (JSONArray)((JSONObject)structArray.get(3)).get("Multiple");
            unitCount = ((Long)(((JSONObject)structArray.get(3)).get("Count"))).intValue();
            nameCounter = 0;
            //StatusVulnerability
            for(int i = 0; i < unitCount; i++)
            {
                StatusVulnerability tempStatusVulnerability = new StatusVulnerability();
                
                //Sleep
                int locator = tempStatusVulnerability.sleep_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.sleep = ByteUtils.bytesToInt(givenFiledata[locator]);

                //Stop
                locator = tempStatusVulnerability.stop_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.stop = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Dizzy
                locator = tempStatusVulnerability.dizzy_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.dizzy = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Poison
                locator = tempStatusVulnerability.poison_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.poison = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Confuse
                locator = tempStatusVulnerability.confuse_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.confuse = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Electric
                locator = tempStatusVulnerability.electric_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.electric = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Burn
                locator = tempStatusVulnerability.burn_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.burn = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Freeze
                locator = tempStatusVulnerability.freeze_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.freeze = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Huge
                locator = tempStatusVulnerability.huge_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.huge = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Tiny
                locator = tempStatusVulnerability.tiny_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.tiny = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Attack Up
                locator = tempStatusVulnerability.attack_up_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.attack_up = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Attack Down
                locator = tempStatusVulnerability.attack_down_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.attack_down = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Defense Up
                locator = tempStatusVulnerability.defense_up_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.defense_up = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Defense Down
                locator = tempStatusVulnerability.defense_down_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.defense_down = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Allergic
                locator = tempStatusVulnerability.allergic_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.allergic = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Fright
                locator = tempStatusVulnerability.fright_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.fright = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Gale Force
                locator = tempStatusVulnerability.gale_force_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.gale_force = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Fast
                locator = tempStatusVulnerability.fast_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.fast = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Slow
                locator = tempStatusVulnerability.slow_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.slow = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Dodgy
                locator = tempStatusVulnerability.dodgy_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.dodgy = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //Invisible
                locator = tempStatusVulnerability.invisible_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.invisible = ByteUtils.bytesToInt(givenFiledata[locator]);
                
                //OHKO             
                locator = tempStatusVulnerability.ohko_offset + Math.toIntExact((Long)unitOffsetArray.get(i));
                tempStatusVulnerability.ohko = ByteUtils.bytesToInt(givenFiledata[locator]);

                //Add to arrayList
                units.get(nameCounter).addStatusVulnerabilityStruct(tempStatusVulnerability);

                //Check for multiples
                int multipleCheck = multipleExists(multipleArray, unitNameArray.get(nameCounter).toString());
                if(multipleArray.size() != 0 && multipleCheck != -1)
                {
                    int multipleCount = Math.toIntExact((Long)(((JSONObject)(multipleArray.get(multipleCheck))).get("Amount")));

                    for(int j = 1; j < multipleCount; j++)
                    {
                        tempStatusVulnerability = new StatusVulnerability();

                        //Sleep
                        locator = tempStatusVulnerability.sleep_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.sleep = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Stop
                        locator = tempStatusVulnerability.stop_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.stop = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Dizzy
                        locator = tempStatusVulnerability.dizzy_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.dizzy = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Poison
                        locator = tempStatusVulnerability.poison_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.poison = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Confuse
                        locator = tempStatusVulnerability.confuse_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.confuse = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Electric
                        locator = tempStatusVulnerability.electric_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.electric = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Burn
                        locator = tempStatusVulnerability.burn_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.burn = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Freeze
                        locator = tempStatusVulnerability.freeze_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.freeze = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Huge
                        locator = tempStatusVulnerability.huge_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.huge = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Tiny
                        locator = tempStatusVulnerability.tiny_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.tiny = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Attack Up
                        locator = tempStatusVulnerability.attack_up_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.attack_up = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Attack Down
                        locator = tempStatusVulnerability.attack_down_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.attack_down = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Defense Up
                        locator = tempStatusVulnerability.defense_up_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.defense_up = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Defense Down
                        locator = tempStatusVulnerability.defense_down_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.defense_down = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Allergic
                        locator = tempStatusVulnerability.allergic_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.allergic = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Fright
                        locator = tempStatusVulnerability.fright_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.fright = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Gale Force
                        locator = tempStatusVulnerability.gale_force_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.gale_force = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Fast
                        locator = tempStatusVulnerability.fast_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.fast = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Slow
                        locator = tempStatusVulnerability.slow_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.slow = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Dodgy
                        locator = tempStatusVulnerability.dodgy_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.dodgy = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //Invisible
                        locator = tempStatusVulnerability.invisible_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.invisible = ByteUtils.bytesToInt(givenFiledata[locator]);
                        
                        //OHKO             
                        locator = tempStatusVulnerability.ohko_offset + Math.toIntExact((Long)unitOffsetArray.get(i + j));
                        tempStatusVulnerability.ohko = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Add to arrayList
                        units.get(nameCounter).addStatusVulnerabilityStruct(tempStatusVulnerability);
                    }

                    //Incrementing i to account for multiples
                    i += (multipleCount - 1);
                }

                nameCounter++;
                if(nameCounter >= units.size())
                {
                    break;
                }
            }

            //BattleWeapon
            unitOffsetArray = (JSONArray)((JSONObject)structArray.get(4)).get("Offsets");
            multipleArray = (JSONArray)((JSONObject)structArray.get(4)).get("Multiple");
            JSONArray attackNamesArray = (JSONArray)((JSONObject)structArray.get(4)).get("AttackNames");
            nameCounter = 0;
            int attackNameCounter = 0;
            for(int i = 0; i < units.size(); i++)
            {
                int multipleCheck = multipleExists(multipleArray, unitNameArray.get(nameCounter).toString());
                if(multipleCheck == -1)
                {
                    BattleWeapon tempBattleWeapon = new BattleWeapon();

                    //Attack Name
                    tempBattleWeapon.attackName = attackNamesArray.get(attackNameCounter).toString();

                    //Accuracy
                    int locator = tempBattleWeapon.accuracy_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.accuracy = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //FP Cost
                    locator = tempBattleWeapon.fp_cost_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.fp_cost = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //SP Cost
                    locator = tempBattleWeapon.sp_cost_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.sp_cost = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Superguard State
                    locator = tempBattleWeapon.superguard_state_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.superguard_state = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Stylish Multiplier
                    locator = tempBattleWeapon.sylish_multiplier_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.sylish_multiplier = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Bingo Slot Increase Chance
                    locator = tempBattleWeapon.bingo_slot_inc_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.bingo_slot_inc_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Base Damage fn
                    locator = tempBattleWeapon.base_damage_fn_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_damage_fn = ByteUtils.bytesToLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //Base Damage 1
                    locator = tempBattleWeapon.base_damage1_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_damage1 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //Base Damage 2
                    locator = tempBattleWeapon.base_damage2_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_damage2 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //Base Damage 3
                    locator = tempBattleWeapon.base_damage3_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_damage3 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //Base Damage 4
                    locator = tempBattleWeapon.base_damage4_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_damage4 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //Base Damage 5
                    locator = tempBattleWeapon.base_damage5_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_damage5 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //Base Damage 6
                    locator = tempBattleWeapon.base_damage6_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_damage6 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //Base Damage 7
                    locator = tempBattleWeapon.base_damage7_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_damage7 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //Base Damage 8
                    locator = tempBattleWeapon.base_damage8_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_damage8 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //Base FP Damage 1
                    locator = tempBattleWeapon.base_fpdamage1_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_fpdamage1 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //Base FP Damage 2
                    locator = tempBattleWeapon.base_fpdamage2_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_fpdamage2 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //Base FP Damage 3
                    locator = tempBattleWeapon.base_fpdamage3_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_fpdamage3 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //Base FP Damage 4
                    locator = tempBattleWeapon.base_fpdamage4_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_fpdamage4 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //Base FP Damage 5
                    locator = tempBattleWeapon.base_fpdamage5_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_fpdamage5 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //Base FP Damage 6
                    locator = tempBattleWeapon.base_fpdamage6_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_fpdamage6 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //Base FP Damage 7
                    locator = tempBattleWeapon.base_fpdamage7_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_fpdamage7 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //Base FP Damage 8
                    locator = tempBattleWeapon.base_fpdamage8_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.base_fpdamage8 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //TargetClassFlags
                    locator = tempBattleWeapon.TargetClassFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    int bitfieldData = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                    tempBattleWeapon.CannotTargetMarioOrShellShield = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetMarioOrShellShield);
                    tempBattleWeapon.CannotTargetPartner = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetPartner);
                    tempBattleWeapon.CannotTargetEnemy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetEnemy);
                    tempBattleWeapon.CannotTargetTreeOrSwitch = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetTreeOrSwitch);
                    tempBattleWeapon.CannotTargetSystem = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetSystem);
                    tempBattleWeapon.CannotTargetOppositeAlliance = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetOppositeAlliance);
                    tempBattleWeapon.CannotTargetOwnAlliance = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetOwnAlliance);
                    tempBattleWeapon.CannotTargetSelf = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetSelf);
                    tempBattleWeapon.CannotTargetSameSpecies = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetSameSpecies);
                    tempBattleWeapon.OnlyTargetSelf = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.OnlyTargetSelf);
                    tempBattleWeapon.OnlyTargetMario = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.OnlyTargetMario);
                    tempBattleWeapon.OnlyTargetTreeOrSwitch = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.OnlyTargetTreeOrSwitch);
                    tempBattleWeapon.OnlyTargetPreferredParts = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.OnlyTargetPreferredParts);
                    tempBattleWeapon.OnlyTargetSelectParts = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.OnlyTargetSelectParts);
                    tempBattleWeapon.SingleTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.SingleTarget);
                    tempBattleWeapon.MultipleTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.MultipleTarget);
                    tempBattleWeapon.CannotTargetAnything = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetAnything);

                    //TargetPropertyFlags
                    locator = tempBattleWeapon.TargetPropertyFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    bitfieldData = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                    tempBattleWeapon.Tattlelike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Tattlelike);
                    tempBattleWeapon.CannotTargetFloating2 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetFloating2);
                    tempBattleWeapon.CannotTargetCeiling = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetCeiling);
                    tempBattleWeapon.CannotTargetFloating = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetFloating);
                    tempBattleWeapon.CannotTargetGrounded = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetGrounded);
                    tempBattleWeapon.Jumplike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Jumplike);
                    tempBattleWeapon.Hammerlike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Hammerlike);
                    tempBattleWeapon.ShellTosslike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.ShellTosslike);
                    tempBattleWeapon.CannotTargetGroundedVariant = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetGroundedVariant);
                    tempBattleWeapon.RecoilDamage = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.RecoilDamage);
                    tempBattleWeapon.CanOnlyTargetFrontmost = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CanOnlyTargetFrontmost);
                    tempBattleWeapon.CannotTargetShellShield = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetShellShield);
                    tempBattleWeapon.CannotTargetCustom = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetCustom);
                    tempBattleWeapon.TargetSameAllianceDirection = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.TargetSameAllianceDirection);
                    tempBattleWeapon.TargetOppositeAllianceDirection = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.TargetOppositeAllianceDirection);

                    //Element
                    locator = tempBattleWeapon.element_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.element = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Damage Pattern
                    locator = tempBattleWeapon.damage_pattern_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.damage_pattern = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //AC Level
                    locator = tempBattleWeapon.ac_level_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.ac_level = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //SpecialPropertyFlags
                    locator = tempBattleWeapon.SpecialPropertyFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    bitfieldData = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                    tempBattleWeapon.BadgeCanAffectPower = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.BadgeCanAffectPower);
                    tempBattleWeapon.StatusCanAffectPower = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.StatusCanAffectPower);
                    tempBattleWeapon.IsChargeable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.IsChargeable);
                    tempBattleWeapon.CannotMiss = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.CannotMiss);
                    tempBattleWeapon.DiminishingReturnsByHit = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.DiminishingReturnsByHit);
                    tempBattleWeapon.DiminishingReturnsByTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.DiminishingReturnsByTarget);
                    tempBattleWeapon.PiercesDefense = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.PiercesDefense);
                    tempBattleWeapon.CanBreakIce = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.CanBreakIce);
                    tempBattleWeapon.IgnoreTargetStatusVulnerability = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.IgnoreTargetStatusVulnerability);
                    tempBattleWeapon.SPFx200 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.SPFx200);
                    tempBattleWeapon.IgnitesIfBurned = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.IgnitesIfBurned);
                    tempBattleWeapon.PlayActiveFXSound = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.PlayActiveFXSound);
                    tempBattleWeapon.FlipsShellEnemies = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.FlipsShellEnemies);
                    tempBattleWeapon.FlipsBombFlippableEnemies = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.FlipsBombFlippableEnemies);
                    tempBattleWeapon.GroundsWingedEnemies = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.GroundsWingedEnemies);
                    tempBattleWeapon.SPFx8000 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.SPFx8000);
                    tempBattleWeapon.CanBeUsedAsConfusedAction = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.CanBeUsedAsConfusedAction);
                    tempBattleWeapon.Unguardable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.Unguardable);
                    tempBattleWeapon.CanHitClonelike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.CanHitClonelike);

                    //CounterResistanceFlags
                    locator = tempBattleWeapon.CounterResistanceFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    bitfieldData = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                    tempBattleWeapon.Electric = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Electric);
                    tempBattleWeapon.TopSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.TopSpiky);
                    tempBattleWeapon.PreemptiveFrontSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.PreemptiveFrontSpiky);
                    tempBattleWeapon.FrontSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.FrontSpiky);
                    tempBattleWeapon.Fiery = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Fiery);
                    tempBattleWeapon.Icy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Icy);
                    tempBattleWeapon.Poison = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Poison);
                    tempBattleWeapon.Explosive = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Explosive);
                    tempBattleWeapon.VolatileExplosive = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.VolatileExplosive);
                    tempBattleWeapon.Payback = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Payback);
                    tempBattleWeapon.HoldFast = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.HoldFast);

                    //TargetWeightingFlags
                    locator = tempBattleWeapon.TargetWeightingFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    bitfieldData = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                    tempBattleWeapon.PreferMario = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferMario);
                    tempBattleWeapon.PreferPartner = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferPartner);
                    tempBattleWeapon.PreferFront = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferFront);
                    tempBattleWeapon.PreferBack = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferBack);
                    tempBattleWeapon.PreferSameAlliance = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferSameAlliance);
                    tempBattleWeapon.PreferOppositeAlliance = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferOppositeAlliance);
                    tempBattleWeapon.PreferLessHealthy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferLessHealthy);
                    tempBattleWeapon.GreatlyPreferLessHealthy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.GreatlyPreferLessHealthy);
                    tempBattleWeapon.PreferLowerHP = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferLowerHP);
                    tempBattleWeapon.PreferHigherHP = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferHigherHP);
                    tempBattleWeapon.PreferInPeril = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferInPeril);
                    tempBattleWeapon.TWFx2000 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.TWFx2000);
                    tempBattleWeapon.ChooseWeightedRandomly = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.ChooseWeightedRandomly);

                    //Sleep Chance
                    locator = tempBattleWeapon.sleep_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.sleep_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Sleep Time
                    locator = tempBattleWeapon.sleep_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.sleep_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Stop Chance
                    locator = tempBattleWeapon.stop_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.stop_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Stop Time
                    locator = tempBattleWeapon.stop_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.stop_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Dizzy Chance
                    locator = tempBattleWeapon.dizzy_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.dizzy_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Dizzy Time
                    locator = tempBattleWeapon.dizzy_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.dizzy_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Poison Chance
                    locator = tempBattleWeapon.poison_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.poison_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Poison Time
                    locator = tempBattleWeapon.poison_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.poison_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Poison Strength
                    locator = tempBattleWeapon.poison_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.poison_strength = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Confuse Chance
                    locator = tempBattleWeapon.confuse_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.confuse_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Confuse Time
                    locator = tempBattleWeapon.confuse_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.confuse_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Electric Chance
                    locator = tempBattleWeapon.electric_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.electric_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Electric Time
                    locator = tempBattleWeapon.electric_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.electric_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Dodgy Chance
                    locator = tempBattleWeapon.dodgy_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.dodgy_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Dodgy Time
                    locator = tempBattleWeapon.dodgy_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.dodgy_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Burn Chance
                    locator = tempBattleWeapon.burn_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.burn_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Burn Time
                    locator = tempBattleWeapon.burn_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.burn_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Freeze Chance
                    locator = tempBattleWeapon.freeze_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.freeze_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Freeze Time
                    locator = tempBattleWeapon.freeze_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.freeze_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Size Change Chance
                    locator = tempBattleWeapon.size_change_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.size_change_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Size Change Time
                    locator = tempBattleWeapon.size_change_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.size_change_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Size Change Strength
                    locator = tempBattleWeapon.size_change_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.size_change_strength = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Atk Change Chance
                    locator = tempBattleWeapon.atk_change_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.atk_change_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Atk Change Time
                    locator = tempBattleWeapon.atk_change_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.atk_change_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Atk Change Strength
                    locator = tempBattleWeapon.atk_change_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.atk_change_strength = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Def Change Chance
                    locator = tempBattleWeapon.def_change_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.def_change_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Def Change Time
                    locator = tempBattleWeapon.def_change_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.def_change_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Def Change Strength
                    locator = tempBattleWeapon.def_change_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.def_change_strength = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Allergic Chance
                    locator = tempBattleWeapon.allergic_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.allergic_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Allergic Time
                    locator = tempBattleWeapon.allergic_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.allergic_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //OHKO Chance
                    locator = tempBattleWeapon.ohko_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.ohko_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Charge Strength
                    locator = tempBattleWeapon.charge_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.charge_strength = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Fast Chance
                    locator = tempBattleWeapon.fast_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.fast_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Fast Time
                    locator = tempBattleWeapon.fast_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.fast_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Slow Chance
                    locator = tempBattleWeapon.slow_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.slow_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Slow Time
                    locator = tempBattleWeapon.slow_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.slow_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Fright Chance
                    locator = tempBattleWeapon.fright_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.fright_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Gale Force Chance
                    locator = tempBattleWeapon.gale_force_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.gale_force_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Payback Time
                    locator = tempBattleWeapon.payback_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.payback_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Hold Fast Time
                    locator = tempBattleWeapon.hold_fast_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.hold_fast_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Invisible Chance
                    locator = tempBattleWeapon.invisible_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.invisible_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Invisible Time
                    locator = tempBattleWeapon.invisible_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.invisible_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //HP Regen Time
                    locator = tempBattleWeapon.hp_regen_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.hp_regen_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //HP Regen Strength
                    locator = tempBattleWeapon.hp_regen_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.hp_regen_strength = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //FP Regen Time
                    locator = tempBattleWeapon.fp_regen_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.fp_regen_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //FP Regen Strength
                    locator = tempBattleWeapon.fp_regen_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.fp_regen_strength = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Attack Script
                    locator = tempBattleWeapon.attack_evt_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.attack_evt = ByteUtils.bytesToLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                    //stage_background_fallweight1
                    locator = tempBattleWeapon.stage_background_fallweight1_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.stage_background_fallweight1 = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //stage_background_fallweight2
                    locator = tempBattleWeapon.stage_background_fallweight2_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.stage_background_fallweight2 = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //stage_background_fallweight3
                    locator = tempBattleWeapon.stage_background_fallweight3_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.stage_background_fallweight3 = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //stage_background_fallweight4
                    locator = tempBattleWeapon.stage_background_fallweight4_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.stage_background_fallweight4 = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //stage_nozzle_turn_chance
                    locator = tempBattleWeapon.stage_nozzle_turn_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.stage_nozzle_turn_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //stage_nozzle_fire_chance
                    locator = tempBattleWeapon.stage_nozzle_fire_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.stage_nozzle_fire_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //stage_ceiling_fall_chance
                    locator = tempBattleWeapon.stage_ceiling_fall_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.stage_ceiling_fall_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //stage_object_fall_chance
                    locator = tempBattleWeapon.stage_object_fall_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                    tempBattleWeapon.stage_object_fall_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                    attackNameCounter++;
                    units.get(i).addBattleWeaponStruct(tempBattleWeapon);
                }
                else if(Math.toIntExact((Long)(((JSONObject)(multipleArray.get(multipleCheck))).get("Amount"))) != 0)
                {
                    for(int j = 0; j < Math.toIntExact((Long)(((JSONObject)(multipleArray.get(multipleCheck))).get("Amount"))); j++)
                    {
                        BattleWeapon tempBattleWeapon = new BattleWeapon();

                        //Attack Name
                        tempBattleWeapon.attackName = attackNamesArray.get(attackNameCounter).toString();

                        //Accuracy
                        int locator = tempBattleWeapon.accuracy_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.accuracy = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //FP Cost
                        locator = tempBattleWeapon.fp_cost_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.fp_cost = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //SP Cost
                        locator = tempBattleWeapon.sp_cost_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.sp_cost = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Superguard State
                        locator = tempBattleWeapon.superguard_state_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.superguard_state = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Stylish Multiplier
                        locator = tempBattleWeapon.sylish_multiplier_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.sylish_multiplier = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Bingo Slot Increase Chance
                        locator = tempBattleWeapon.bingo_slot_inc_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.bingo_slot_inc_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Base Damage fn
                        locator = tempBattleWeapon.base_damage_fn_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_damage_fn = ByteUtils.bytesToLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Base Damage 1
                        locator = tempBattleWeapon.base_damage1_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_damage1 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Base Damage 2
                        locator = tempBattleWeapon.base_damage2_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_damage2 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Base Damage 3
                        locator = tempBattleWeapon.base_damage3_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_damage3 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Base Damage 4
                        locator = tempBattleWeapon.base_damage4_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_damage4 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Base Damage 5
                        locator = tempBattleWeapon.base_damage5_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_damage5 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Base Damage 6
                        locator = tempBattleWeapon.base_damage6_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_damage6 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Base Damage 7
                        locator = tempBattleWeapon.base_damage7_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_damage7 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Base Damage 8
                        locator = tempBattleWeapon.base_damage8_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_damage8 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Base FP Damage 1
                        locator = tempBattleWeapon.base_fpdamage1_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_fpdamage1 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Base FP Damage 2
                        locator = tempBattleWeapon.base_fpdamage2_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_fpdamage2 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Base FP Damage 3
                        locator = tempBattleWeapon.base_fpdamage3_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_fpdamage3 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Base FP Damage 4
                        locator = tempBattleWeapon.base_fpdamage4_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_fpdamage4 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Base FP Damage 5
                        locator = tempBattleWeapon.base_fpdamage5_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_fpdamage5 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Base FP Damage 6
                        locator = tempBattleWeapon.base_fpdamage6_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_fpdamage6 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Base FP Damage 7
                        locator = tempBattleWeapon.base_fpdamage7_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_fpdamage7 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Base FP Damage 8
                        locator = tempBattleWeapon.base_fpdamage8_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.base_fpdamage8 = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //TargetClassFlags
                        locator = tempBattleWeapon.TargetClassFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        int bitfieldData = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                        tempBattleWeapon.CannotTargetMarioOrShellShield = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetMarioOrShellShield);
                        tempBattleWeapon.CannotTargetPartner = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetPartner);
                        tempBattleWeapon.CannotTargetEnemy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetEnemy);
                        tempBattleWeapon.CannotTargetTreeOrSwitch = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetTreeOrSwitch);
                        tempBattleWeapon.CannotTargetSystem = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetSystem);
                        tempBattleWeapon.CannotTargetOppositeAlliance = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetOppositeAlliance);
                        tempBattleWeapon.CannotTargetOwnAlliance = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetOwnAlliance);
                        tempBattleWeapon.CannotTargetSelf = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetSelf);
                        tempBattleWeapon.CannotTargetSameSpecies = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetSameSpecies);
                        tempBattleWeapon.OnlyTargetSelf = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.OnlyTargetSelf);
                        tempBattleWeapon.OnlyTargetMario = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.OnlyTargetMario);
                        tempBattleWeapon.OnlyTargetTreeOrSwitch = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.OnlyTargetTreeOrSwitch);
                        tempBattleWeapon.OnlyTargetPreferredParts = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.OnlyTargetPreferredParts);
                        tempBattleWeapon.OnlyTargetSelectParts = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.OnlyTargetSelectParts);
                        tempBattleWeapon.SingleTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.SingleTarget);
                        tempBattleWeapon.MultipleTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.MultipleTarget);
                        tempBattleWeapon.CannotTargetAnything = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetAnything);

                        //TargetPropertyFlags
                        locator = tempBattleWeapon.TargetPropertyFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        bitfieldData = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                        tempBattleWeapon.Tattlelike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Tattlelike);
                        tempBattleWeapon.CannotTargetFloating2 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetFloating2);
                        tempBattleWeapon.CannotTargetCeiling = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetCeiling);
                        tempBattleWeapon.CannotTargetFloating = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetFloating);
                        tempBattleWeapon.CannotTargetGrounded = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetGrounded);
                        tempBattleWeapon.Jumplike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Jumplike);
                        tempBattleWeapon.Hammerlike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Hammerlike);
                        tempBattleWeapon.ShellTosslike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.ShellTosslike);
                        tempBattleWeapon.CannotTargetGroundedVariant = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetGroundedVariant);
                        tempBattleWeapon.RecoilDamage = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.RecoilDamage);
                        tempBattleWeapon.CanOnlyTargetFrontmost = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CanOnlyTargetFrontmost);
                        tempBattleWeapon.CannotTargetShellShield = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetShellShield);
                        tempBattleWeapon.CannotTargetCustom = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetCustom);
                        tempBattleWeapon.TargetSameAllianceDirection = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.TargetSameAllianceDirection);
                        tempBattleWeapon.TargetOppositeAllianceDirection = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.TargetOppositeAllianceDirection);

                        //Element
                        locator = tempBattleWeapon.element_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.element = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Damage Pattern
                        locator = tempBattleWeapon.damage_pattern_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.damage_pattern = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //AC Level
                        locator = tempBattleWeapon.ac_level_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.ac_level = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //SpecialPropertyFlags
                        locator = tempBattleWeapon.SpecialPropertyFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        bitfieldData = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                        tempBattleWeapon.BadgeCanAffectPower = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.BadgeCanAffectPower);
                        tempBattleWeapon.StatusCanAffectPower = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.StatusCanAffectPower);
                        tempBattleWeapon.IsChargeable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.IsChargeable);
                        tempBattleWeapon.CannotMiss = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.CannotMiss);
                        tempBattleWeapon.DiminishingReturnsByHit = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.DiminishingReturnsByHit);
                        tempBattleWeapon.DiminishingReturnsByTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.DiminishingReturnsByTarget);
                        tempBattleWeapon.PiercesDefense = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.PiercesDefense);
                        tempBattleWeapon.CanBreakIce = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.CanBreakIce);
                        tempBattleWeapon.IgnoreTargetStatusVulnerability = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.IgnoreTargetStatusVulnerability);
                        tempBattleWeapon.SPFx200 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.SPFx200);
                        tempBattleWeapon.IgnitesIfBurned = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.IgnitesIfBurned);
                        tempBattleWeapon.PlayActiveFXSound = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.PlayActiveFXSound);
                        tempBattleWeapon.FlipsShellEnemies = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.FlipsShellEnemies);
                        tempBattleWeapon.FlipsBombFlippableEnemies = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.FlipsBombFlippableEnemies);
                        tempBattleWeapon.GroundsWingedEnemies = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.GroundsWingedEnemies);
                        tempBattleWeapon.SPFx8000 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.SPFx8000);
                        tempBattleWeapon.CanBeUsedAsConfusedAction = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.CanBeUsedAsConfusedAction);
                        tempBattleWeapon.Unguardable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.Unguardable);
                        tempBattleWeapon.CanHitClonelike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.CanHitClonelike);

                        //CounterResistanceFlags
                        locator = tempBattleWeapon.CounterResistanceFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        bitfieldData = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                        tempBattleWeapon.Electric = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Electric);
                        tempBattleWeapon.TopSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.TopSpiky);
                        tempBattleWeapon.PreemptiveFrontSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.PreemptiveFrontSpiky);
                        tempBattleWeapon.FrontSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.FrontSpiky);
                        tempBattleWeapon.Fiery = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Fiery);
                        tempBattleWeapon.Icy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Icy);
                        tempBattleWeapon.Poison = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Poison);
                        tempBattleWeapon.Explosive = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Explosive);
                        tempBattleWeapon.VolatileExplosive = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.VolatileExplosive);
                        tempBattleWeapon.Payback = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Payback);
                        tempBattleWeapon.HoldFast = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.HoldFast);

                        //TargetWeightingFlags
                        locator = tempBattleWeapon.TargetWeightingFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        bitfieldData = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                        tempBattleWeapon.PreferMario = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferMario);
                        tempBattleWeapon.PreferPartner = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferPartner);
                        tempBattleWeapon.PreferFront = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferFront);
                        tempBattleWeapon.PreferBack = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferBack);
                        tempBattleWeapon.PreferSameAlliance = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferSameAlliance);
                        tempBattleWeapon.PreferOppositeAlliance = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferOppositeAlliance);
                        tempBattleWeapon.PreferLessHealthy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferLessHealthy);
                        tempBattleWeapon.GreatlyPreferLessHealthy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.GreatlyPreferLessHealthy);
                        tempBattleWeapon.PreferLowerHP = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferLowerHP);
                        tempBattleWeapon.PreferHigherHP = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferHigherHP);
                        tempBattleWeapon.PreferInPeril = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferInPeril);
                        tempBattleWeapon.TWFx2000 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.TWFx2000);
                        tempBattleWeapon.ChooseWeightedRandomly = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.ChooseWeightedRandomly);

                        //Sleep Chance
                        locator = tempBattleWeapon.sleep_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.sleep_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Sleep Time
                        locator = tempBattleWeapon.sleep_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.sleep_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Stop Chance
                        locator = tempBattleWeapon.stop_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.stop_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Stop Time
                        locator = tempBattleWeapon.stop_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.stop_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Dizzy Chance
                        locator = tempBattleWeapon.dizzy_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.dizzy_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Dizzy Time
                        locator = tempBattleWeapon.dizzy_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.dizzy_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Poison Chance
                        locator = tempBattleWeapon.poison_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.poison_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Poison Time
                        locator = tempBattleWeapon.poison_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.poison_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Poison Strength
                        locator = tempBattleWeapon.poison_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.poison_strength = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Confuse Chance
                        locator = tempBattleWeapon.confuse_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.confuse_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Confuse Time
                        locator = tempBattleWeapon.confuse_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.confuse_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Electric Chance
                        locator = tempBattleWeapon.electric_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.electric_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Electric Time
                        locator = tempBattleWeapon.electric_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.electric_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Dodgy Chance
                        locator = tempBattleWeapon.dodgy_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.dodgy_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Dodgy Time
                        locator = tempBattleWeapon.dodgy_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.dodgy_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Burn Chance
                        locator = tempBattleWeapon.burn_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.burn_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Burn Time
                        locator = tempBattleWeapon.burn_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.burn_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Freeze Chance
                        locator = tempBattleWeapon.freeze_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.freeze_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Freeze Time
                        locator = tempBattleWeapon.freeze_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.freeze_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Size Change Chance
                        locator = tempBattleWeapon.size_change_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.size_change_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Size Change Time
                        locator = tempBattleWeapon.size_change_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.size_change_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Size Change Strength
                        locator = tempBattleWeapon.size_change_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.size_change_strength = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Atk Change Chance
                        locator = tempBattleWeapon.atk_change_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.atk_change_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Atk Change Time
                        locator = tempBattleWeapon.atk_change_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.atk_change_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Atk Change Strength
                        locator = tempBattleWeapon.atk_change_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.atk_change_strength = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Def Change Chance
                        locator = tempBattleWeapon.def_change_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.def_change_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Def Change Time
                        locator = tempBattleWeapon.def_change_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.def_change_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Def Change Strength
                        locator = tempBattleWeapon.def_change_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.def_change_strength = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Allergic Chance
                        locator = tempBattleWeapon.allergic_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.allergic_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Allergic Time
                        locator = tempBattleWeapon.allergic_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.allergic_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //OHKO Chance
                        locator = tempBattleWeapon.ohko_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.ohko_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Charge Strength
                        locator = tempBattleWeapon.charge_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.charge_strength = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Fast Chance
                        locator = tempBattleWeapon.fast_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.fast_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Fast Time
                        locator = tempBattleWeapon.fast_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.fast_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Slow Chance
                        locator = tempBattleWeapon.slow_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.slow_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Slow Time
                        locator = tempBattleWeapon.slow_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.slow_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Fright Chance
                        locator = tempBattleWeapon.fright_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.fright_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Gale Force Chance
                        locator = tempBattleWeapon.gale_force_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.gale_force_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Payback Time
                        locator = tempBattleWeapon.payback_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.payback_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Hold Fast Time
                        locator = tempBattleWeapon.hold_fast_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.hold_fast_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Invisible Chance
                        locator = tempBattleWeapon.invisible_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.invisible_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Invisible Time
                        locator = tempBattleWeapon.invisible_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.invisible_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //HP Regen Time
                        locator = tempBattleWeapon.hp_regen_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.hp_regen_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //HP Regen Strength
                        locator = tempBattleWeapon.hp_regen_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.hp_regen_strength = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //FP Regen Time
                        locator = tempBattleWeapon.fp_regen_time_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.fp_regen_time = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //FP Regen Strength
                        locator = tempBattleWeapon.fp_regen_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.fp_regen_strength = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //Attack Script
                        locator = tempBattleWeapon.attack_evt_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.attack_evt = ByteUtils.bytesToLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //stage_background_fallweight1
                        locator = tempBattleWeapon.stage_background_fallweight1_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.stage_background_fallweight1 = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //stage_background_fallweight2
                        locator = tempBattleWeapon.stage_background_fallweight2_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.stage_background_fallweight2 = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //stage_background_fallweight3
                        locator = tempBattleWeapon.stage_background_fallweight3_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.stage_background_fallweight3 = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //stage_background_fallweight4
                        locator = tempBattleWeapon.stage_background_fallweight4_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.stage_background_fallweight4 = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //stage_nozzle_turn_chance
                        locator = tempBattleWeapon.stage_nozzle_turn_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.stage_nozzle_turn_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //stage_nozzle_fire_chance
                        locator = tempBattleWeapon.stage_nozzle_fire_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.stage_nozzle_fire_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //stage_ceiling_fall_chance
                        locator = tempBattleWeapon.stage_ceiling_fall_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.stage_ceiling_fall_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        //stage_object_fall_chance
                        locator = tempBattleWeapon.stage_object_fall_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(attackNameCounter));
                        tempBattleWeapon.stage_object_fall_chance = ByteUtils.bytesToInt(givenFiledata[locator]);

                        attackNameCounter++;
                        units.get(i).addBattleWeaponStruct(tempBattleWeapon);
                    }
                }

                nameCounter++;
                if(nameCounter >= units.size())
                {
                    break;
                }
            }

            //BattleUnitKindPart
            unitOffsetArray = (JSONArray)((JSONObject)structArray.get(5)).get("Offsets");
            multipleArray = (JSONArray)((JSONObject)structArray.get(5)).get("Multiple");
            nameCounter = 0;
            int offsetCounter = 0;
            for(int i = 0; i < units.size(); i++)
            {
                int multipleCheck = multipleExists(multipleArray, unitNameArray.get(nameCounter).toString());
                if(multipleCheck == -1)
                {
                    BattleUnitKindPart tempBattleUnitKindPart = new BattleUnitKindPart();

                    //PartsAttributeFlags
                    int locator = tempBattleUnitKindPart.PartsAttributeFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(offsetCounter));
                    int bitfieldData = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                    tempBattleUnitKindPart.MainBodyPart = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.MainBodyPart);
                    tempBattleUnitKindPart.SecondaryBodyPart = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.SecondaryBodyPart);
                    tempBattleUnitKindPart.BombableBodyPart = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.BombableBodyPart);
                    tempBattleUnitKindPart.GuardBodyPart = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.GuardBodyPart);
                    tempBattleUnitKindPart.NotBombableBodyPart = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.NotBombableBodyPart);
                    tempBattleUnitKindPart.InHole = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.InHole);
                    tempBattleUnitKindPart.WeakToAttackFxR = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.WeakToAttackFxR);
                    tempBattleUnitKindPart.WeakToIcePower = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.WeakToIcePower);
                    tempBattleUnitKindPart.IsWinged = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsWinged);
                    tempBattleUnitKindPart.IsShelled = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsShelled);
                    tempBattleUnitKindPart.IsBombFlippable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsBombFlippable);
                    tempBattleUnitKindPart.IsClonelike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsClonelike);
                    tempBattleUnitKindPart.DisableFlatPaperLayering = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.DisableFlatPaperLayering);
                    tempBattleUnitKindPart.NeverTargetable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.NeverTargetable);
                    tempBattleUnitKindPart.IgnoreMapObjectOffset = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IgnoreMapObjectOffset);
                    tempBattleUnitKindPart.IgnoreOnlyTargetSelectAndPreferredParts = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IgnoreOnlyTargetSelectAndPreferredParts);
                    tempBattleUnitKindPart.Untattleable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Untattleable);
                    tempBattleUnitKindPart.JumplikeCannotTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.JumplikeCannotTarget);
                    tempBattleUnitKindPart.HammerlikeCannotTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.HammerlikeCannotTarget);
                    tempBattleUnitKindPart.ShellTosslikeCannotTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.ShellTosslikeCannotTarget);
                    tempBattleUnitKindPart.PreventHealthDecrease = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.PreventHealthDecrease);
                    tempBattleUnitKindPart.DisablePartVisibility = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.DisablePartVisibility);
                    tempBattleUnitKindPart.ImmuneToCustom = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.ImmuneToCustom);
                    tempBattleUnitKindPart.BlurOn = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.BlurOn);
                    tempBattleUnitKindPart.ScaleIndependence = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.ScaleIndependence);
                    tempBattleUnitKindPart.Independence = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Independence);
                    tempBattleUnitKindPart.IsImmuneToDamageOrStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsImmuneToDamageOrStatus);
                    tempBattleUnitKindPart.IsImmuneToOHKO = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsImmuneToOHKO);
                    tempBattleUnitKindPart.IsImmuneToStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsImmuneToStatus);

                    //PartsCounterAttributeFlags
                    locator = tempBattleUnitKindPart.PartsCounterAttributeFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(offsetCounter));
                    bitfieldData = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                    tempBattleUnitKindPart.TopSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.TopSpiky);
                    tempBattleUnitKindPart.PreemptiveFrontSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.PreemptiveFrontSpiky);
                    tempBattleUnitKindPart.FrontSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.FrontSpiky);
                    tempBattleUnitKindPart.Fiery = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Fiery);
                    tempBattleUnitKindPart.FieryStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.FieryStatus);
                    tempBattleUnitKindPart.Icy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Icy);
                    tempBattleUnitKindPart.IcyStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.IcyStatus);
                    tempBattleUnitKindPart.Poison = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Poison);
                    tempBattleUnitKindPart.PoisonStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.PoisonStatus);
                    tempBattleUnitKindPart.Electric = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Electric);
                    tempBattleUnitKindPart.ElectricStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.ElectricStatus);
                    tempBattleUnitKindPart.Explosive = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Explosive);
                    tempBattleUnitKindPart.VolatileExplosive = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.VolatileExplosive);

                    offsetCounter++;
                    units.get(i).addBattleUnitKindPartStruct(tempBattleUnitKindPart);
                }
                else if(Math.toIntExact((Long)(((JSONObject)(multipleArray.get(multipleCheck))).get("Amount"))) != 0)
                {
                    for(int j = 0; j < Math.toIntExact((Long)(((JSONObject)(multipleArray.get(multipleCheck))).get("Amount"))); j++)
                    {
                        BattleUnitKindPart tempBattleUnitKindPart = new BattleUnitKindPart();

                        //PartsAttributeFlags
                        int locator = tempBattleUnitKindPart.PartsAttributeFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(offsetCounter));
                        int bitfieldData = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                        tempBattleUnitKindPart.MainBodyPart = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.MainBodyPart);
                        tempBattleUnitKindPart.SecondaryBodyPart = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.SecondaryBodyPart);
                        tempBattleUnitKindPart.BombableBodyPart = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.BombableBodyPart);
                        tempBattleUnitKindPart.GuardBodyPart = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.GuardBodyPart);
                        tempBattleUnitKindPart.NotBombableBodyPart = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.NotBombableBodyPart);
                        tempBattleUnitKindPart.InHole = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.InHole);
                        tempBattleUnitKindPart.WeakToAttackFxR = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.WeakToAttackFxR);
                        tempBattleUnitKindPart.WeakToIcePower = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.WeakToIcePower);
                        tempBattleUnitKindPart.IsWinged = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsWinged);
                        tempBattleUnitKindPart.IsShelled = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsShelled);
                        tempBattleUnitKindPart.IsBombFlippable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsBombFlippable);
                        tempBattleUnitKindPart.IsClonelike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsClonelike);
                        tempBattleUnitKindPart.DisableFlatPaperLayering = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.DisableFlatPaperLayering);
                        tempBattleUnitKindPart.NeverTargetable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.NeverTargetable);
                        tempBattleUnitKindPart.IgnoreMapObjectOffset = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IgnoreMapObjectOffset);
                        tempBattleUnitKindPart.IgnoreOnlyTargetSelectAndPreferredParts = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IgnoreOnlyTargetSelectAndPreferredParts);
                        tempBattleUnitKindPart.Untattleable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Untattleable);
                        tempBattleUnitKindPart.JumplikeCannotTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.JumplikeCannotTarget);
                        tempBattleUnitKindPart.HammerlikeCannotTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.HammerlikeCannotTarget);
                        tempBattleUnitKindPart.ShellTosslikeCannotTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.ShellTosslikeCannotTarget);
                        tempBattleUnitKindPart.PreventHealthDecrease = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.PreventHealthDecrease);
                        tempBattleUnitKindPart.DisablePartVisibility = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.DisablePartVisibility);
                        tempBattleUnitKindPart.ImmuneToCustom = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.ImmuneToCustom);
                        tempBattleUnitKindPart.BlurOn = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.BlurOn);
                        tempBattleUnitKindPart.ScaleIndependence = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.ScaleIndependence);
                        tempBattleUnitKindPart.Independence = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Independence);
                        tempBattleUnitKindPart.IsImmuneToDamageOrStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsImmuneToDamageOrStatus);
                        tempBattleUnitKindPart.IsImmuneToOHKO = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsImmuneToOHKO);
                        tempBattleUnitKindPart.IsImmuneToStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsImmuneToStatus);

                        //PartsCounterAttributeFlags
                        locator = tempBattleUnitKindPart.PartsCounterAttributeFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(offsetCounter));
                        bitfieldData = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                        tempBattleUnitKindPart.TopSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.TopSpiky);
                        tempBattleUnitKindPart.PreemptiveFrontSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.PreemptiveFrontSpiky);
                        tempBattleUnitKindPart.FrontSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.FrontSpiky);
                        tempBattleUnitKindPart.Fiery = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Fiery);
                        tempBattleUnitKindPart.FieryStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.FieryStatus);
                        tempBattleUnitKindPart.Icy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Icy);
                        tempBattleUnitKindPart.IcyStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.IcyStatus);
                        tempBattleUnitKindPart.Poison = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Poison);
                        tempBattleUnitKindPart.PoisonStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.PoisonStatus);
                        tempBattleUnitKindPart.Electric = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Electric);
                        tempBattleUnitKindPart.ElectricStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.ElectricStatus);
                        tempBattleUnitKindPart.Explosive = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Explosive);
                        tempBattleUnitKindPart.VolatileExplosive = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.VolatileExplosive);

                        offsetCounter++;
                        units.get(i).addBattleUnitKindPartStruct(tempBattleUnitKindPart);
                    }
                }

                nameCounter++;
                if(nameCounter >= units.size())
                {
                    break;
                }
            }

            //HealthUpgrades
            unitOffsetArray = (JSONArray)((JSONObject)structArray.get(6)).get("Offsets");
            multipleArray = (JSONArray)((JSONObject)structArray.get(6)).get("Multiple");
            nameCounter = 0;
            offsetCounter = 0;
            for(int i = 0; i < units.size(); i++)
            {
                if(multipleExists(multipleArray, unitNameArray.get(nameCounter).toString()) == -1)
                {
                    HealthUpgrades tempHealthUpgrades = new HealthUpgrades();

                    //startHP / Partner: Level 1 HP
                    int locator = Math.toIntExact((Long)unitOffsetArray.get(offsetCounter));
                    tempHealthUpgrades.startHP = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]);
                    offsetCounter++;

                    //startFP / Partner: Level 2 HP
                    locator = Math.toIntExact((Long)unitOffsetArray.get(offsetCounter));
                    tempHealthUpgrades.startFP = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]);
                    offsetCounter++;

                    //startBP / Partner: Level 3 HP
                    locator = Math.toIntExact((Long)unitOffsetArray.get(offsetCounter));
                    tempHealthUpgrades.startBP = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]);
                    offsetCounter++;

                    if(unitNameArray.get(nameCounter).toString().equals("Mario"))
                    {
                        //upgradeHP
                        locator = Math.toIntExact((Long)unitOffsetArray.get(offsetCounter));
                        tempHealthUpgrades.upgradeHP = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]);
                        offsetCounter += 2;

                        //upgradeFP
                        locator = Math.toIntExact((Long)unitOffsetArray.get(offsetCounter));
                        tempHealthUpgrades.upgradeFP = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]);
                        offsetCounter += 2;

                        //upgradeBP
                        locator = Math.toIntExact((Long)unitOffsetArray.get(offsetCounter));
                        tempHealthUpgrades.upgradeBP = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]);
                        offsetCounter += 2;
                    }

                    units.get(i).addHealthUpgradeStruct(tempHealthUpgrades);
                }

                nameCounter++;
                if(nameCounter >= units.size())
                {
                    break;
                }
            }

            return units;
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

        //Failsafe
        return null;
    }

    /**
     * @Author Jemaroo
     * @Function Will go through an array of multiples and compare the names for a match. Will return index if found, and -1 if not found
     */
    public static int multipleExists(JSONArray multipleArray, String name)
    {
        for(int i = 0; i < multipleArray.size(); i++)
        {
            if(((JSONObject)(multipleArray.get(i))).get("Name").equals(name))
            {
                //Found
                return i;
            }
        }

        //Not found
        return -1;
    }

    /**
     * @Author Jemaroo
     * @Function Will export the array of units back into the same format as the given file
     */
    public static byte[] buildNewFile(File givenFile, ArrayList<BUTData> units)
    {
        File jsonFile = new File("src\\UnitData.json");
        byte[] givenFiledata = ByteUtils.readData(givenFile);
        int locator = 0;
        int BUKOffsetCounter = 0;
        int BUDOffsetCounter = 0;
        int BUDAOffsetCounter = 0;
        int SVOffsetCounter = 0;
        int BWOffsetCounter = 0;
        int BUKPOffsetCounter = 0;
        int HUOffsetCounter = 0;

        try
        {
            JSONParser parser = new JSONParser();
            JSONObject root = (JSONObject)parser.parse(new FileReader(jsonFile));
            JSONArray fileArray = (JSONArray)root.get("File");
            JSONObject fileObj = null;
            JSONArray structArray = null;

            for(int i = 0; i < fileArray.size(); i ++)
            {
                fileObj = (JSONObject)fileArray.get(i);
                String name = (String)fileObj.get("Name");
                if(givenFile.getName().equalsIgnoreCase(name))
                {  
                    structArray = (JSONArray)fileObj.get("Struct");
                    break;
                }
            }

            JSONArray unitOffsetArray = null;

            for(int i = 0; i < units.size(); i++)
            {
                unitOffsetArray = (JSONArray)((JSONObject)structArray.get(0)).get("Offsets");
                //BattleUnitKind
                for(int j = 0; j < units.get(i).BattleUnitKindData.size(); j++)
                {
                    //HP
                    locator = units.get(i).BattleUnitKindData.get(j).HP_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKOffsetCounter));
                    byte[] tempHP = ByteUtils.intTo2Bytes(units.get(i).BattleUnitKindData.get(j).HP);
                    for(int k = 0; k < 2 ; k++)
                    {
                        givenFiledata[locator + k] = tempHP[k];
                    }

                    //DangerHP
                    locator = units.get(i).BattleUnitKindData.get(j).dangerHP_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).dangerHP);

                    //PerilHP
                    locator = units.get(i).BattleUnitKindData.get(j).perilHP_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).perilHP);

                    //Level
                    locator = units.get(i).BattleUnitKindData.get(j).level_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).level);

                    //BonusXP
                    locator = units.get(i).BattleUnitKindData.get(j).bonusXP_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).bonusXP);
                    
                    //BonusCoin
                    locator = units.get(i).BattleUnitKindData.get(j).bonusCoin_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).bonusCoin);

                    //BonusCoinRate
                    locator = units.get(i).BattleUnitKindData.get(j).bonusCoinRate_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).bonusCoinRate);

                    //BaseCoin
                    locator = units.get(i).BattleUnitKindData.get(j).baseCoin_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).baseCoin);

                    //RunRate
                    locator = units.get(i).BattleUnitKindData.get(j).runRate_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).runRate);

                    //pbCap
                    locator = units.get(i).BattleUnitKindData.get(j).pbCap_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKOffsetCounter));
                    byte[] tempPBCap = ByteUtils.intTo2Bytes(units.get(i).BattleUnitKindData.get(j).pbCap);
                    for(int k = 0; k < 2 ; k++)
                    {
                        givenFiledata[locator + k] = tempPBCap[k];
                    }

                    //SwallowChance
                    locator = units.get(i).BattleUnitKindData.get(j).swallowChance_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).swallowChance);

                    //SwallowAttribute
                    locator = units.get(i).BattleUnitKindData.get(j).swallowAttribute_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).swallowAttribute);

                    //UltraHammerKnockChance
                    locator = units.get(i).BattleUnitKindData.get(j).ultraHammerKnockChance_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).ultraHammerKnockChance);

                    //ItemStealParameters
                    locator = units.get(i).BattleUnitKindData.get(j).itemStealParameter_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).itemStealParameter);

                    //UnitAttributeFlags
                    locator = units.get(i).BattleUnitKindData.get(j).UnitAttributeFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKOffsetCounter));
                    byte[] tempUAF = ByteUtils.buildUAFBitfield(units.get(i).BattleUnitKindData.get(j));
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempUAF[k];
                    }

                    BUKOffsetCounter++;
                }

                unitOffsetArray = (JSONArray)((JSONObject)structArray.get(1)).get("Offsets");
                //BattleUnitDefense
                for(int j = 0; j < units.get(i).BattleUnitDefenseData.size(); j++)
                {
                    //Normal
                    locator = units.get(i).BattleUnitDefenseData.get(j).normal_offset + Math.toIntExact((Long)unitOffsetArray.get(BUDOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseData.get(j).normal);

                    //Fire
                    locator = units.get(i).BattleUnitDefenseData.get(j).fire_offset + Math.toIntExact((Long)unitOffsetArray.get(BUDOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseData.get(j).fire);

                    //Ice
                    locator = units.get(i).BattleUnitDefenseData.get(j).ice_offset + Math.toIntExact((Long)unitOffsetArray.get(BUDOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseData.get(j).ice);

                    //Explosion
                    locator = units.get(i).BattleUnitDefenseData.get(j).explosion_offset + Math.toIntExact((Long)unitOffsetArray.get(BUDOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseData.get(j).explosion);

                    //Electric
                    locator = units.get(i).BattleUnitDefenseData.get(j).electric_offset + Math.toIntExact((Long)unitOffsetArray.get(BUDOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseData.get(j).electric);

                    BUDOffsetCounter++;
                }

                unitOffsetArray = (JSONArray)((JSONObject)structArray.get(2)).get("Offsets");
                //BattleUnitDefenseAttr
                for(int j = 0; j < units.get(i).BattleUnitDefenseAttrData.size(); j++)
                {
                    //Normal
                    locator = units.get(i).BattleUnitDefenseAttrData.get(j).normal_offset + Math.toIntExact((Long)unitOffsetArray.get(BUDAOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseAttrData.get(j).normal);

                    //Fire
                    locator = units.get(i).BattleUnitDefenseAttrData.get(j).fire_offset + Math.toIntExact((Long)unitOffsetArray.get(BUDAOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseAttrData.get(j).fire);

                    //Ice
                    locator = units.get(i).BattleUnitDefenseAttrData.get(j).ice_offset + Math.toIntExact((Long)unitOffsetArray.get(BUDAOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseAttrData.get(j).ice);

                    //Explosion
                    locator = units.get(i).BattleUnitDefenseAttrData.get(j).explosion_offset + Math.toIntExact((Long)unitOffsetArray.get(BUDAOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseAttrData.get(j).explosion);

                    //Electric
                    locator = units.get(i).BattleUnitDefenseAttrData.get(j).electric_offset + Math.toIntExact((Long)unitOffsetArray.get(BUDAOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseAttrData.get(j).electric);

                    BUDAOffsetCounter++;
                }

                unitOffsetArray = (JSONArray)((JSONObject)structArray.get(3)).get("Offsets");
                //StatusVulnerability
                for(int j = 0; j < units.get(i).StatusVulnerabilityData.size(); j++)
                {
                    //Sleep
                    locator = units.get(i).StatusVulnerabilityData.get(j).sleep_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).sleep);

                    //Stop
                    locator = units.get(i).StatusVulnerabilityData.get(j).stop_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).stop);
                    
                    //Dizzy
                    locator = units.get(i).StatusVulnerabilityData.get(j).dizzy_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).dizzy);
                    
                    //Poison
                    locator = units.get(i).StatusVulnerabilityData.get(j).poison_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).poison);
                    
                    //Confuse
                    locator = units.get(i).StatusVulnerabilityData.get(j).confuse_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).confuse);
                    
                    //Electric
                    locator = units.get(i).StatusVulnerabilityData.get(j).electric_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).electric);
                    
                    //Burn
                    locator = units.get(i).StatusVulnerabilityData.get(j).burn_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).burn);
                    
                    //Freeze
                    locator = units.get(i).StatusVulnerabilityData.get(j).freeze_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).freeze);
                    
                    //Huge
                    locator = units.get(i).StatusVulnerabilityData.get(j).huge_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).huge);
                    
                    //Tiny
                    locator = units.get(i).StatusVulnerabilityData.get(j).tiny_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).tiny);
                    
                    //Attack Up
                    locator = units.get(i).StatusVulnerabilityData.get(j).attack_up_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).attack_up);
                    
                    //Attack Down
                    locator = units.get(i).StatusVulnerabilityData.get(j).attack_down_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).attack_down);
                    
                    //Defense Up
                    locator = units.get(i).StatusVulnerabilityData.get(j).defense_up_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).defense_up);
                    
                    //Defense Down
                    locator = units.get(i).StatusVulnerabilityData.get(j).defense_down_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).defense_down);
                    
                    //Allergic
                    locator = units.get(i).StatusVulnerabilityData.get(j).allergic_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).allergic);
                    
                    //Fright
                    locator = units.get(i).StatusVulnerabilityData.get(j).fright_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).fright);
                    
                    //Gale Force
                    locator = units.get(i).StatusVulnerabilityData.get(j).gale_force_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).gale_force);
                    
                    //Fast
                    locator = units.get(i).StatusVulnerabilityData.get(j).fast_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).fast);
                    
                    //Slow
                    locator = units.get(i).StatusVulnerabilityData.get(j).slow_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).slow);                    
                    
                    //Dodgy
                    locator = units.get(i).StatusVulnerabilityData.get(j).dodgy_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).dodgy);
                    
                    //Invisible
                    locator = units.get(i).StatusVulnerabilityData.get(j).invisible_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).invisible);
                    
                    //OHKO             
                    locator = units.get(i).StatusVulnerabilityData.get(j).ohko_offset + Math.toIntExact((Long)unitOffsetArray.get(SVOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).ohko);

                    SVOffsetCounter++;
                }

                unitOffsetArray = (JSONArray)((JSONObject)structArray.get(4)).get("Offsets");
                //BattleWeapon
                for(int j = 0; j < units.get(i).BattleWeaponData.size(); j++)
                {
                    //Accuracy
                    locator = units.get(i).BattleWeaponData.get(j).accuracy_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).accuracy);

                    //FP Cost
                    locator = units.get(i).BattleWeaponData.get(j).fp_cost_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).fp_cost);

                    //SP Cost
                    locator = units.get(i).BattleWeaponData.get(j).sp_cost_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).sp_cost);

                    //Superguard State
                    locator = units.get(i).BattleWeaponData.get(j).superguard_state_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).superguard_state);

                    //Stylish Multiplier
                    locator = units.get(i).BattleWeaponData.get(j).sylish_multiplier_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).sylish_multiplier);

                    //Bingo Slot Increase Chance
                    locator = units.get(i).BattleWeaponData.get(j).bingo_slot_inc_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).bingo_slot_inc_chance);

                    //Base Damage fn
                    locator = units.get(i).BattleWeaponData.get(j).base_damage_fn_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBDfn = ByteUtils.longTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage_fn);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBDfn[k];
                    }

                    //Base Damage 1
                    locator = units.get(i).BattleWeaponData.get(j).base_damage1_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBD1 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage1);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBD1[k];
                    }

                    //Base Damage 2
                    locator = units.get(i).BattleWeaponData.get(j).base_damage2_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBD2 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage2);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBD2[k];
                    }

                    //Base Damage 3
                    locator = units.get(i).BattleWeaponData.get(j).base_damage3_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBD3 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage3);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBD3[k];
                    }

                    //Base Damage 4
                    locator = units.get(i).BattleWeaponData.get(j).base_damage4_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBD4 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage4);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBD4[k];
                    }

                    //Base Damage 5
                    locator = units.get(i).BattleWeaponData.get(j).base_damage5_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBD5 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage5);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBD5[k];
                    }

                    //Base Damage 6
                    locator = units.get(i).BattleWeaponData.get(j).base_damage6_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBD6 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage6);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBD6[k];
                    }

                    //Base Damage 7
                    locator = units.get(i).BattleWeaponData.get(j).base_damage7_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBD7 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage7);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBD7[k];
                    }

                    //Base Damage 8
                    locator = units.get(i).BattleWeaponData.get(j).base_damage8_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBD8 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage8);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBD8[k];
                    }

                    //Base FP Damage 1
                    locator = units.get(i).BattleWeaponData.get(j).base_fpdamage1_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBFPD1 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_fpdamage1);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBFPD1[k];
                    }

                    //Base FP Damage 2
                    locator = units.get(i).BattleWeaponData.get(j).base_fpdamage2_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBFPD2 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_fpdamage2);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBFPD2[k];
                    }

                    //Base FP Damage 3
                    locator = units.get(i).BattleWeaponData.get(j).base_fpdamage3_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBFPD3 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_fpdamage3);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBFPD3[k];
                    }

                    //Base FP Damage 4
                    locator = units.get(i).BattleWeaponData.get(j).base_fpdamage4_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBFPD4 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_fpdamage4);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBFPD4[k];
                    }

                    //Base FP Damage 5
                    locator = units.get(i).BattleWeaponData.get(j).base_fpdamage5_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBFPD5 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_fpdamage5);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBFPD5[k];
                    }

                    //Base FP Damage 6
                    locator = units.get(i).BattleWeaponData.get(j).base_fpdamage6_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBFPD6 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_fpdamage6);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBFPD6[k];
                    }

                    //Base FP Damage 7
                    locator = units.get(i).BattleWeaponData.get(j).base_fpdamage7_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBFPD7 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_fpdamage7);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBFPD7[k];
                    }

                    //Base FP Damage 8
                    locator = units.get(i).BattleWeaponData.get(j).base_fpdamage8_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempBFPD8 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_fpdamage8);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempBFPD8[k];
                    }

                    //TargetClassFlags
                    locator = units.get(i).BattleWeaponData.get(j).TargetClassFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempTCF = ByteUtils.buildTCFBitfield(units.get(i).BattleWeaponData.get(j));
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempTCF[k];
                    }

                    //TargetPropertyFlags
                    locator = units.get(i).BattleWeaponData.get(j).TargetPropertyFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempTPF = ByteUtils.buildTPFBitfield(units.get(i).BattleWeaponData.get(j));
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempTPF[k];
                    }

                    //Element
                    locator = units.get(i).BattleWeaponData.get(j).element_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).element);

                    //Damage Pattern
                    locator = units.get(i).BattleWeaponData.get(j).damage_pattern_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).damage_pattern);

                    //AC Level
                    locator = units.get(i).BattleWeaponData.get(j).ac_level_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).ac_level);

                    //SpecialPropertyFlags
                    locator = units.get(i).BattleWeaponData.get(j).SpecialPropertyFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempSPF = ByteUtils.buildSPFBitfield(units.get(i).BattleWeaponData.get(j));
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempSPF[k];
                    }

                    //CounterResistanceFlags
                    locator = units.get(i).BattleWeaponData.get(j).CounterResistanceFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempCRF = ByteUtils.buildCRFBitfield(units.get(i).BattleWeaponData.get(j));
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempCRF[k];
                    }

                    //TargetWeightingFlags
                    locator = units.get(i).BattleWeaponData.get(j).TargetWeightingFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempTWF = ByteUtils.buildTWFBitfield(units.get(i).BattleWeaponData.get(j));
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempTWF[k];
                    }

                    //Sleep Chance
                    locator = units.get(i).BattleWeaponData.get(j).sleep_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).sleep_chance);

                    //Sleep Time
                    locator = units.get(i).BattleWeaponData.get(j).sleep_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).sleep_time);

                    //Stop Chance
                    locator = units.get(i).BattleWeaponData.get(j).stop_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stop_chance);

                    //Stop Time
                    locator = units.get(i).BattleWeaponData.get(j).stop_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stop_time);

                    //Dizzy Chance
                    locator = units.get(i).BattleWeaponData.get(j).dizzy_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).dizzy_chance);

                    //Dizzy Time
                    locator = units.get(i).BattleWeaponData.get(j).dizzy_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).dizzy_time);

                    //Poison Chance
                    locator = units.get(i).BattleWeaponData.get(j).poison_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).poison_chance);

                    //Poison Time
                    locator = units.get(i).BattleWeaponData.get(j).poison_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).poison_time);

                    //Poison Strength
                    locator = units.get(i).BattleWeaponData.get(j).poison_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).poison_strength);

                    //Confuse Chance
                    locator = units.get(i).BattleWeaponData.get(j).confuse_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).confuse_chance);

                    //Confuse Time
                    locator = units.get(i).BattleWeaponData.get(j).confuse_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).confuse_time);

                    //Electric Chance
                    locator = units.get(i).BattleWeaponData.get(j).electric_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).electric_chance);

                    //Electric Time
                    locator = units.get(i).BattleWeaponData.get(j).electric_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).electric_time);

                    //Dodgy Chance
                    locator = units.get(i).BattleWeaponData.get(j).dodgy_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).dodgy_chance);

                    //Dodgy Time
                    locator = units.get(i).BattleWeaponData.get(j).dodgy_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).dodgy_time);

                    //Burn Chance
                    locator = units.get(i).BattleWeaponData.get(j).burn_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).burn_chance);

                    //Burn Time
                    locator = units.get(i).BattleWeaponData.get(j).burn_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).burn_time);

                    //Freeze Chance
                    locator = units.get(i).BattleWeaponData.get(j).freeze_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).freeze_chance);

                    //Freeze Time
                    locator = units.get(i).BattleWeaponData.get(j).freeze_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).freeze_time);

                    //Size Change Chance
                    locator = units.get(i).BattleWeaponData.get(j).size_change_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).size_change_chance);

                    //Size Change Time
                    locator = units.get(i).BattleWeaponData.get(j).size_change_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).size_change_time);

                    //Size Change Strength
                    locator = units.get(i).BattleWeaponData.get(j).size_change_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).size_change_strength);

                    //Atk Change Chance
                    locator = units.get(i).BattleWeaponData.get(j).atk_change_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).atk_change_chance);

                    //Atk Change Time
                    locator = units.get(i).BattleWeaponData.get(j).atk_change_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).atk_change_time);

                    //Atk Change Strength
                    locator = units.get(i).BattleWeaponData.get(j).atk_change_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).atk_change_strength);

                    //Def Change Chance
                    locator = units.get(i).BattleWeaponData.get(j).def_change_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).def_change_chance);

                    //Def Change Time
                    locator = units.get(i).BattleWeaponData.get(j).def_change_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).def_change_time);

                    //Def Change Strength
                    locator = units.get(i).BattleWeaponData.get(j).def_change_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).def_change_strength);

                    //Allergic Chance
                    locator = units.get(i).BattleWeaponData.get(j).allergic_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).allergic_chance);

                    //Allergic Time
                    locator = units.get(i).BattleWeaponData.get(j).allergic_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).allergic_time);

                    //OHKO Chance
                    locator = units.get(i).BattleWeaponData.get(j).ohko_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).ohko_chance);

                    //Charge Strength
                    locator = units.get(i).BattleWeaponData.get(j).charge_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).charge_strength);

                    //Fast Chance
                    locator = units.get(i).BattleWeaponData.get(j).fast_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).fast_chance);

                    //Fast Time
                    locator = units.get(i).BattleWeaponData.get(j).fast_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).fast_time);

                    //Slow Chance
                    locator = units.get(i).BattleWeaponData.get(j).slow_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).slow_chance);

                    //Slow Time
                    locator = units.get(i).BattleWeaponData.get(j).slow_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).slow_time);

                    //Fright Chance
                    locator = units.get(i).BattleWeaponData.get(j).fright_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).fright_chance);

                    //Gale Force Chance
                    locator = units.get(i).BattleWeaponData.get(j).gale_force_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).gale_force_chance);

                    //Payback Time
                    locator = units.get(i).BattleWeaponData.get(j).payback_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).payback_time);

                    //Hold Fast Time
                    locator = units.get(i).BattleWeaponData.get(j).hold_fast_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).hold_fast_time);

                    //Invisible Chance
                    locator = units.get(i).BattleWeaponData.get(j).invisible_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).invisible_chance);

                    //Invisible Time
                    locator = units.get(i).BattleWeaponData.get(j).invisible_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).invisible_time);

                    //HP Regen Time
                    locator = units.get(i).BattleWeaponData.get(j).hp_regen_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).hp_regen_time);

                    //HP Regen Strength
                    locator = units.get(i).BattleWeaponData.get(j).hp_regen_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).hp_regen_strength);

                    //FP Regen Time
                    locator = units.get(i).BattleWeaponData.get(j).fp_regen_time_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).fp_regen_time);

                    //FP Regen Strength
                    locator = units.get(i).BattleWeaponData.get(j).fp_regen_strength_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).fp_regen_strength);

                    //Attack Script
                    locator = units.get(i).BattleWeaponData.get(j).attack_evt_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    byte[] tempAS = ByteUtils.longTo4Bytes(units.get(i).BattleWeaponData.get(j).attack_evt);
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempAS[k];
                    }

                    //stage_background_fallweight1
                    locator = units.get(i).BattleWeaponData.get(j).stage_background_fallweight1_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stage_background_fallweight1);

                    //stage_background_fallweight2
                    locator = units.get(i).BattleWeaponData.get(j).stage_background_fallweight2_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stage_background_fallweight2);

                    //stage_background_fallweight3
                    locator = units.get(i).BattleWeaponData.get(j).stage_background_fallweight3_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stage_background_fallweight3);

                    //stage_background_fallweight4
                    locator = units.get(i).BattleWeaponData.get(j).stage_background_fallweight4_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stage_background_fallweight4);

                    //stage_nozzle_turn_chance
                    locator = units.get(i).BattleWeaponData.get(j).stage_nozzle_turn_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stage_nozzle_turn_chance);

                    //stage_nozzle_fire_chance
                    locator = units.get(i).BattleWeaponData.get(j).stage_nozzle_fire_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stage_nozzle_fire_chance);

                    //stage_ceiling_fall_chance
                    locator = units.get(i).BattleWeaponData.get(j).stage_ceiling_fall_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stage_ceiling_fall_chance);

                    //stage_object_fall_chance
                    locator = units.get(i).BattleWeaponData.get(j).stage_object_fall_chance_offset + Math.toIntExact((Long)unitOffsetArray.get(BWOffsetCounter));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stage_object_fall_chance);

                    BWOffsetCounter++;
                }

                unitOffsetArray = (JSONArray)((JSONObject)structArray.get(5)).get("Offsets");
                //BattleUnitKindPart
                for(int j = 0; j < units.get(i).BattleUnitKindPartData.size(); j++)
                {
                    //PartsAttributeFlags
                    locator = units.get(i).BattleUnitKindPartData.get(j).PartsAttributeFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKPOffsetCounter));
                    byte[] tempPAF = ByteUtils.buildPAFBitfield(units.get(i).BattleUnitKindPartData.get(j));
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempPAF[k];
                    }

                    //PartsCounterAttributeFlags
                    locator = units.get(i).BattleUnitKindPartData.get(j).PartsCounterAttributeFlags_offset + Math.toIntExact((Long)unitOffsetArray.get(BUKPOffsetCounter));
                    byte[] tempPCAF = ByteUtils.buildPCAFBitfield(units.get(i).BattleUnitKindPartData.get(j));
                    for(int k = 0; k < 4 ; k++)
                    {
                        givenFiledata[locator + k] = tempPCAF[k];
                    }

                    BUKPOffsetCounter++;
                }

                //HealthUpgrades
                unitOffsetArray = (JSONArray)((JSONObject)structArray.get(6)).get("Offsets");
                for(int j = 0; j < units.get(i).HealthUpgradesData.size(); j++)
                {
                    //startHP / Partner: Level 1 HP
                    locator = Math.toIntExact((Long)unitOffsetArray.get(HUOffsetCounter));
                    byte[] tempStartHP = ByteUtils.intTo2Bytes(units.get(i).HealthUpgradesData.get(j).startHP);
                    for(int k = 0; k < 2 ; k++)
                    {
                        givenFiledata[locator + k] = tempStartHP[k];
                    }
                    HUOffsetCounter++;

                    //startFP / Partner: Level 2 HP
                    locator = Math.toIntExact((Long)unitOffsetArray.get(HUOffsetCounter));
                    byte[] tempStartFP = ByteUtils.intTo2Bytes(units.get(i).HealthUpgradesData.get(j).startFP);
                    for(int k = 0; k < 2 ; k++)
                    {
                        givenFiledata[locator + k] = tempStartFP[k];
                    }
                    HUOffsetCounter++;

                    //startBP / Partner: Level 3 HP
                    locator = Math.toIntExact((Long)unitOffsetArray.get(HUOffsetCounter));
                    byte[] tempStartBP = ByteUtils.intTo2Bytes(units.get(i).HealthUpgradesData.get(j).startBP);
                    for(int k = 0; k < 2 ; k++)
                    {
                        givenFiledata[locator + k] = tempStartBP[k];
                    }
                    HUOffsetCounter++;

                    if(units.get(i).name.equals("Mario"))
                    {
                        //upgradeHP
                        locator = Math.toIntExact((Long)unitOffsetArray.get(HUOffsetCounter));
                        byte[] tempUpgradeHP = ByteUtils.intTo2Bytes(units.get(i).HealthUpgradesData.get(j).upgradeHP);
                        for(int k = 0; k < 2 ; k++)
                        {
                            givenFiledata[locator + k] = tempUpgradeHP[k];
                        }
                        HUOffsetCounter++;
                        locator = Math.toIntExact((Long)unitOffsetArray.get(HUOffsetCounter));
                        for(int k = 0; k < 2 ; k++)
                        {
                            givenFiledata[locator + k] = tempUpgradeHP[k];
                        }
                        HUOffsetCounter++;

                        //upgradeFP
                        locator = Math.toIntExact((Long)unitOffsetArray.get(HUOffsetCounter));
                        byte[] tempUpgradeFP = ByteUtils.intTo2Bytes(units.get(i).HealthUpgradesData.get(j).upgradeFP);
                        for(int k = 0; k < 2 ; k++)
                        {
                            givenFiledata[locator + k] = tempUpgradeFP[k];
                        }
                        HUOffsetCounter++;
                        locator = Math.toIntExact((Long)unitOffsetArray.get(HUOffsetCounter));
                        for(int k = 0; k < 2 ; k++)
                        {
                            givenFiledata[locator + k] = tempUpgradeFP[k];
                        }
                        HUOffsetCounter++;

                        //upgradeBP
                        locator = Math.toIntExact((Long)unitOffsetArray.get(HUOffsetCounter));
                        byte[] tempUpgradeBP = ByteUtils.intTo2Bytes(units.get(i).HealthUpgradesData.get(j).upgradeBP);
                        for(int k = 0; k < 2 ; k++)
                        {
                            givenFiledata[locator + k] = tempUpgradeBP[k];
                        }
                        HUOffsetCounter++;
                        locator = Math.toIntExact((Long)unitOffsetArray.get(HUOffsetCounter));
                        for(int k = 0; k < 2 ; k++)
                        {
                            givenFiledata[locator + k] = tempUpgradeBP[k];
                        }
                        HUOffsetCounter++;
                    }
                }
            }

            return givenFiledata;
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

        //Failsafe
        return null;
    }
}