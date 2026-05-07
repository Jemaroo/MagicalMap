import java.io.File;
import java.util.ArrayList;

public class PresetUtils 
{
    /**
     * @Author Jemaroo
     * @Function Will attempt to read the given Battle Unit Preset file and the parse it into an array of UnitData
     */
    public static ArrayList<BUTData> getPresetData(File presetFile)
    {
        byte[] presetFileData = ByteUtils.readData(presetFile);
        ArrayList<BUTData> units = new ArrayList<BUTData>();
        BUTData tempUnit = new BUTData();
        int locator = 0;
        int unitSize = 0;
        int size = 0;

        if(presetFileData[locator] == 0x00)
        {
            //File
            locator++;
            unitSize = presetFileData[locator];
            locator++;

            for(int j = 0; j < unitSize; j++)
            {
                tempUnit = new BUTData();

                if(presetFileData[locator] == 0x00)
                {
                    locator++;
                }
                else
                {
                    size = ByteUtils.bytesToInt(presetFileData[locator]);
                    locator++;
                    for(int i = 0; i < size; i++)
                    {
                        BattleUnitKind tempBUK = new BattleUnitKind();

                        //HP
                        tempBUK.HP = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1]);
                        locator += 2;

                        //DangerHP
                        tempBUK.dangerHP = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //PerilHP
                        tempBUK.perilHP = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Level
                        tempBUK.level = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //BonusXP
                        tempBUK.bonusXP = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //BonusCoin
                        tempBUK.bonusCoin = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //BonusCoinRate
                        tempBUK.bonusCoinRate = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //BaseCoin
                        tempBUK.baseCoin = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //RunRate
                        tempBUK.runRate = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //pbCap
                        tempBUK.pbCap = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1]);
                        locator += 2;

                        //SwallowChance
                        tempBUK.swallowChance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //SwallowAttribute
                        tempBUK.swallowAttribute = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //UltraHammerKnockChance
                        tempBUK.ultraHammerKnockChance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //ItemStealParameter
                        tempBUK.itemStealParameter = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //UnitAttributeFlags
                        int bitfieldData = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        tempBUK.MapObj = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.MapObj);
                        tempBUK.OutOfReach = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.OutOfReach);
                        tempBUK.Unquakeable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.Unquakeable);
                        tempBUK.IsInvisible = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.IsInvisible);
                        tempBUK.IsVeiled = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.IsVeiled);
                        tempBUK.ShellShielded = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.ShellShielded);
                        tempBUK.NeverTargetable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.NeverTargetable);
                        tempBUK.LimitSwitch = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.LimitSwitch);
                        tempBUK.DisableZeroGravityFloat = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.DisableZeroGravityFloat);
                        tempBUK.DisableZeroGravityImmobility = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.DisableZeroGravityImmobility);
                        tempBUK.immuneToUltraHammerKnock = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.immuneToUltraHammerKnock);
                        tempBUK.IsUndead = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.IsUndead);
                        tempBUK.IsCorpse = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.IsCorpse);
                        tempBUK.IsLeader = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.IsLeader);
                        tempBUK.CannotTakeActions = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.CannotTakeActions);
                        tempBUK.NotSpunByLoveSlap = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.NotSpunByLoveSlap);
                        tempBUK.DisableDamageStars = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.DisableDamageStars);
                        tempBUK.DisableAllPartVisibility = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.DisableAllPartVisibility);
                        tempBUK.DisableHPGauge = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.DisableHPGauge);
                        tempBUK.LookCamera = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.LookCamera);
                        tempBUK.NonCombatant = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.NonCombatant);
                        tempBUK.NoShadow = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.NoShadow);
                        tempBUK.DisableDamage = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.DisableDamage);
                        locator += 4;

                        tempUnit.addBattleUnitKindStruct(tempBUK);
                    }
                }

                if(presetFileData[locator] == 0x00)
                {
                    locator++;
                }
                else
                {
                    size = ByteUtils.bytesToInt(presetFileData[locator]);
                    locator++;
                    for(int i = 0; i < size; i++)
                    {
                        BattleUnitKindPart tempBUKP = new BattleUnitKindPart();

                        int bitfieldData = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        tempBUKP.MainBodyPart = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.MainBodyPart);
                        tempBUKP.SecondaryBodyPart = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.SecondaryBodyPart);
                        tempBUKP.BombableBodyPart = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.BombableBodyPart);
                        tempBUKP.GuardBodyPart = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.GuardBodyPart);
                        tempBUKP.NotBombableBodyPart = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.NotBombableBodyPart);
                        tempBUKP.InHole = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.InHole);
                        tempBUKP.WeakToAttackFxR = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.WeakToAttackFxR);
                        tempBUKP.WeakToIcePower = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.WeakToIcePower);
                        tempBUKP.IsWinged = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsWinged);
                        tempBUKP.IsShelled = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsShelled);
                        tempBUKP.IsBombFlippable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsBombFlippable);
                        tempBUKP.IsClonelike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsClonelike);
                        tempBUKP.DisableFlatPaperLayering = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.DisableFlatPaperLayering);
                        tempBUKP.NeverTargetable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.NeverTargetable);
                        tempBUKP.IgnoreMapObjectOffset = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IgnoreMapObjectOffset);
                        tempBUKP.IgnoreOnlyTargetSelectAndPreferredParts = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IgnoreOnlyTargetSelectAndPreferredParts);
                        tempBUKP.Untattleable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Untattleable);
                        tempBUKP.JumplikeCannotTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.JumplikeCannotTarget);
                        tempBUKP.HammerlikeCannotTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.HammerlikeCannotTarget);
                        tempBUKP.ShellTosslikeCannotTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.ShellTosslikeCannotTarget);
                        tempBUKP.PreventHealthDecrease = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.PreventHealthDecrease);
                        tempBUKP.DisablePartVisibility = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.DisablePartVisibility);
                        tempBUKP.ImmuneToCustom = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.ImmuneToCustom);
                        tempBUKP.BlurOn = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.BlurOn);
                        tempBUKP.ScaleIndependence = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.ScaleIndependence);
                        tempBUKP.Independence = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Independence);
                        tempBUKP.IsImmuneToDamageOrStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsImmuneToDamageOrStatus);
                        tempBUKP.IsImmuneToOHKO = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsImmuneToOHKO);
                        tempBUKP.IsImmuneToStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsImmuneToStatus);
                        locator += 4;

                        //PartsCounterAttributeFlags
                        bitfieldData = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        tempBUKP.TopSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.TopSpiky);
                        tempBUKP.PreemptiveFrontSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.PreemptiveFrontSpiky);
                        tempBUKP.FrontSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.FrontSpiky);
                        tempBUKP.Fiery = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Fiery);
                        tempBUKP.FieryStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.FieryStatus);
                        tempBUKP.Icy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Icy);
                        tempBUKP.IcyStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.IcyStatus);
                        tempBUKP.Poison = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Poison);
                        tempBUKP.PoisonStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.PoisonStatus);
                        tempBUKP.Electric = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Electric);
                        tempBUKP.ElectricStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.ElectricStatus);
                        tempBUKP.Explosive = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Explosive);
                        tempBUKP.VolatileExplosive = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.VolatileExplosive);
                        locator += 4;

                        tempUnit.addBattleUnitKindPartStruct(tempBUKP);
                    }
                }

                if(presetFileData[locator] == 0x00)
                {
                    locator++;
                }
                else
                {
                    size = ByteUtils.bytesToInt(presetFileData[locator]);
                    locator++;
                    for(int i = 0; i < size; i++)
                    {
                        BattleUnitDefense tempBUD = new BattleUnitDefense();

                        //Normal
                        tempBUD.normal = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Fire
                        tempBUD.fire = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Ice
                        tempBUD.ice = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Explosion
                        tempBUD.explosion = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Electric
                        tempBUD.electric = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        tempUnit.addBattleUnitDefenseStruct(tempBUD);
                    }
                }

                if(presetFileData[locator] == 0x00)
                {
                    locator++;
                }
                else
                {
                    size = ByteUtils.bytesToInt(presetFileData[locator]);
                    locator++;
                    for(int i = 0; i < size; i++)
                    {
                        BattleUnitDefenseAttr tempBUDA = new BattleUnitDefenseAttr();

                        //Normal
                        tempBUDA.normal = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Fire
                        tempBUDA.fire = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Ice
                        tempBUDA.ice = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Explosion
                        tempBUDA.explosion = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Electric
                        tempBUDA.electric = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        tempUnit.addBattleUnitDefenseAttrStruct(tempBUDA);
                    }
                }

                if(presetFileData[locator] == 0x00)
                {
                    locator++;
                }
                else
                {
                    size = ByteUtils.bytesToInt(presetFileData[locator]);
                    locator++;
                    for(int i = 0; i < size; i++)
                    {
                        StatusVulnerability tempSV = new StatusVulnerability();

                        //Sleep
                        tempSV.sleep = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Stop
                        tempSV.stop = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Dizzy
                        tempSV.dizzy = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Poison
                        tempSV.poison = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Confuse
                        tempSV.confuse = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Electric
                        tempSV.electric = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Burn
                        tempSV.burn = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Freeze
                        tempSV.freeze = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Huge
                        tempSV.huge = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Tiny
                        tempSV.tiny = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Attack Up
                        tempSV.attack_up = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Attack Down
                        tempSV.attack_down = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Defense Up
                        tempSV.defense_up = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Defense Down
                        tempSV.defense_down = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Allergic
                        tempSV.allergic = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Fright
                        tempSV.fright = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Gale Force
                        tempSV.gale_force = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Fast
                        tempSV.fast = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Slow
                        tempSV.slow = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;               
                        
                        //Dodgy
                        tempSV.dodgy = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //Invisible
                        tempSV.invisible = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;
                        
                        //OHKO             
                        tempSV.ohko = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        tempUnit.addStatusVulnerabilityStruct(tempSV);
                    }
                }

                if(presetFileData[locator] == 0x00)
                {
                    locator++;
                }
                else
                {
                    size = ByteUtils.bytesToInt(presetFileData[locator]);
                    locator++;
                    for(int i = 0; i < size; i++)
                    {
                        BattleWeapon tempBW = new BattleWeapon();

                        //Accuracy
                        tempBW.accuracy = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //FP Cost
                        tempBW.fp_cost = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //SP Cost
                        tempBW.sp_cost = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Superguard State
                        tempBW.superguard_state = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Stylish Multiplier
                        tempBW.sylish_multiplier = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Bingo Slot Increase Chance
                        tempBW.bingo_slot_inc_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Base Damage fn
                        tempBW.base_damage_fn = ByteUtils.bytesToLong(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //Base Damage 1
                        tempBW.base_damage1 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //Base Damage 2
                        tempBW.base_damage2 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //Base Damage 3
                        tempBW.base_damage3 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //Base Damage 4
                        tempBW.base_damage4 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //Base Damage 5
                        tempBW.base_damage5 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //Base Damage 6
                        tempBW.base_damage6 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //Base Damage 7
                        tempBW.base_damage7 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //Base Damage 8
                        tempBW.base_damage8 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //Base FP Damage 1
                        tempBW.base_fpdamage1 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //Base FP Damage 2
                        tempBW.base_fpdamage2 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //Base FP Damage 3
                        tempBW.base_fpdamage3 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //Base FP Damage 4
                        tempBW.base_fpdamage4 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //Base FP Damage 5
                        tempBW.base_fpdamage5 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //Base FP Damage 6
                        tempBW.base_fpdamage6 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //Base FP Damage 7
                        tempBW.base_fpdamage7 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //Base FP Damage 8
                        tempBW.base_fpdamage8 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //TargetClassFlags
                        int bitfieldData = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        tempBW.CannotTargetMarioOrShellShield = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetMarioOrShellShield);
                        tempBW.CannotTargetPartner = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetPartner);
                        tempBW.CannotTargetEnemy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetEnemy);
                        tempBW.CannotTargetTreeOrSwitch = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetTreeOrSwitch);
                        tempBW.CannotTargetSystem = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetSystem);
                        tempBW.CannotTargetOppositeAlliance = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetOppositeAlliance);
                        tempBW.CannotTargetOwnAlliance = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetOwnAlliance);
                        tempBW.CannotTargetSelf = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetSelf);
                        tempBW.CannotTargetSameSpecies = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetSameSpecies);
                        tempBW.OnlyTargetSelf = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.OnlyTargetSelf);
                        tempBW.OnlyTargetMario = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.OnlyTargetMario);
                        tempBW.OnlyTargetTreeOrSwitch = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.OnlyTargetTreeOrSwitch);
                        tempBW.OnlyTargetPreferredParts = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.OnlyTargetPreferredParts);
                        tempBW.OnlyTargetSelectParts = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.OnlyTargetSelectParts);
                        tempBW.SingleTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.SingleTarget);
                        tempBW.MultipleTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.MultipleTarget);
                        tempBW.CannotTargetAnything = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetAnything);
                        locator += 4;

                        //TargetPropertyFlags
                        bitfieldData = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        tempBW.Tattlelike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Tattlelike);
                        tempBW.CannotTargetFloating2 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetFloating2);
                        tempBW.CannotTargetCeiling = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetCeiling);
                        tempBW.CannotTargetFloating = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetFloating);
                        tempBW.CannotTargetGrounded = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetGrounded);
                        tempBW.Jumplike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Jumplike);
                        tempBW.Hammerlike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Hammerlike);
                        tempBW.ShellTosslike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.ShellTosslike);
                        tempBW.CannotTargetGroundedVariant = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetGroundedVariant);
                        tempBW.RecoilDamage = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.RecoilDamage);
                        tempBW.CanOnlyTargetFrontmost = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CanOnlyTargetFrontmost);
                        tempBW.CannotTargetShellShield = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetShellShield);
                        tempBW.CannotTargetCustom = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetCustom);
                        tempBW.TargetSameAllianceDirection = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.TargetSameAllianceDirection);
                        tempBW.TargetOppositeAllianceDirection = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.TargetOppositeAllianceDirection);
                        locator += 4;

                        //Element
                        tempBW.element = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Damage Pattern
                        tempBW.damage_pattern = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //AC Level
                        tempBW.ac_level = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //SpecialPropertyFlags
                        bitfieldData = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        tempBW.BadgeCanAffectPower = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.BadgeCanAffectPower);
                        tempBW.StatusCanAffectPower = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.StatusCanAffectPower);
                        tempBW.IsChargeable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.IsChargeable);
                        tempBW.CannotMiss = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.CannotMiss);
                        tempBW.DiminishingReturnsByHit = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.DiminishingReturnsByHit);
                        tempBW.DiminishingReturnsByTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.DiminishingReturnsByTarget);
                        tempBW.PiercesDefense = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.PiercesDefense);
                        tempBW.CanBreakIce = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.CanBreakIce);
                        tempBW.IgnoreTargetStatusVulnerability = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.IgnoreTargetStatusVulnerability);
                        tempBW.SPFx200 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.SPFx200);
                        tempBW.IgnitesIfBurned = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.IgnitesIfBurned);
                        tempBW.PlayActiveFXSound = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.PlayActiveFXSound);
                        tempBW.FlipsShellEnemies = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.FlipsShellEnemies);
                        tempBW.FlipsBombFlippableEnemies = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.FlipsBombFlippableEnemies);
                        tempBW.GroundsWingedEnemies = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.GroundsWingedEnemies);
                        tempBW.SPFx8000 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.SPFx8000);
                        tempBW.CanBeUsedAsConfusedAction = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.CanBeUsedAsConfusedAction);
                        tempBW.Unguardable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.Unguardable);
                        tempBW.CanHitClonelike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.CanHitClonelike);
                        locator += 4;

                        //CounterResistanceFlags
                        bitfieldData = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        tempBW.Electric = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Electric);
                        tempBW.TopSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.TopSpiky);
                        tempBW.PreemptiveFrontSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.PreemptiveFrontSpiky);
                        tempBW.FrontSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.FrontSpiky);
                        tempBW.Fiery = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Fiery);
                        tempBW.Icy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Icy);
                        tempBW.Poison = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Poison);
                        tempBW.Explosive = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Explosive);
                        tempBW.VolatileExplosive = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.VolatileExplosive);
                        tempBW.Payback = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Payback);
                        tempBW.HoldFast = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.HoldFast);
                        locator += 4;

                        //TargetWeightingFlags
                        bitfieldData = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        tempBW.PreferMario = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferMario);
                        tempBW.PreferPartner = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferPartner);
                        tempBW.PreferFront = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferFront);
                        tempBW.PreferBack = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferBack);
                        tempBW.PreferSameAlliance = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferSameAlliance);
                        tempBW.PreferOppositeAlliance = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferOppositeAlliance);
                        tempBW.PreferLessHealthy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferLessHealthy);
                        tempBW.GreatlyPreferLessHealthy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.GreatlyPreferLessHealthy);
                        tempBW.PreferLowerHP = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferLowerHP);
                        tempBW.PreferHigherHP = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferHigherHP);
                        tempBW.PreferInPeril = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferInPeril);
                        tempBW.TWFx2000 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.TWFx2000);
                        tempBW.ChooseWeightedRandomly = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.ChooseWeightedRandomly);
                        locator += 4;

                        //Sleep Chance
                        tempBW.sleep_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Sleep Time
                        tempBW.sleep_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Stop Chance
                        tempBW.stop_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Stop Time
                        tempBW.stop_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Dizzy Chance
                        tempBW.dizzy_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Dizzy Time
                        tempBW.dizzy_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Poison Chance
                        tempBW.poison_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Poison Time
                        tempBW.poison_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Poison Strength
                        tempBW.poison_strength = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Confuse Chance
                        tempBW.confuse_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Confuse Time
                        tempBW.confuse_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Electric Chance
                        tempBW.electric_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Electric Time
                        tempBW.electric_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Dodgy Chance
                        tempBW.dodgy_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Dodgy Time
                        tempBW.dodgy_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Burn Chance
                        tempBW.burn_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Burn Time
                        tempBW.burn_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Freeze Chance
                        tempBW.freeze_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Freeze Time
                        tempBW.freeze_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Size Change Chance
                        tempBW.size_change_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Size Change Time
                        tempBW.size_change_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Size Change Strength
                        tempBW.size_change_strength = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Atk Change Chance
                        tempBW.atk_change_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Atk Change Time
                        tempBW.atk_change_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Atk Change Strength
                        tempBW.atk_change_strength = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Def Change Chance
                        tempBW.def_change_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Def Change Time
                        tempBW.def_change_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Def Change Strength
                        tempBW.def_change_strength = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Allergic Chance
                        tempBW.allergic_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Allergic Time
                        tempBW.allergic_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //OHKO Chance
                        tempBW.ohko_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Charge Strength
                        tempBW.charge_strength = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Fast Chance
                        tempBW.fast_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Fast Time
                        tempBW.fast_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Slow Chance
                        tempBW.slow_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Slow Time
                        tempBW.slow_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Fright Chance
                        tempBW.fright_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Gale Force Chance
                        tempBW.gale_force_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Payback Time
                        tempBW.payback_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Hold Fast Time
                        tempBW.hold_fast_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Invisible Chance
                        tempBW.invisible_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Invisible Time
                        tempBW.invisible_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //HP Regen Time
                        tempBW.hp_regen_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //HP Regen Strength
                        tempBW.hp_regen_strength = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //FP Regen Time
                        tempBW.fp_regen_time = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //FP Regen Strength
                        tempBW.fp_regen_strength = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //Attack Script
                        tempBW.attack_evt = ByteUtils.bytesToLong(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
                        locator += 4;

                        //stage_background_fallweight1
                        tempBW.stage_background_fallweight1 = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //stage_background_fallweight2
                        tempBW.stage_background_fallweight2 = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //stage_background_fallweight3
                        tempBW.stage_background_fallweight3 = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //stage_background_fallweight4
                        tempBW.stage_background_fallweight4 = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //stage_nozzle_turn_chance
                        tempBW.stage_nozzle_turn_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //stage_nozzle_fire_chance
                        tempBW.stage_nozzle_fire_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //stage_ceiling_fall_chance
                        tempBW.stage_ceiling_fall_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        //stage_object_fall_chance
                        tempBW.stage_object_fall_chance = ByteUtils.bytesToInt(presetFileData[locator]);
                        locator++;

                        tempUnit.addBattleWeaponStruct(tempBW);
                    }
                }

                if(presetFileData[locator] == 0x00)
                {
                    locator++;
                }
                else
                {
                    size = ByteUtils.bytesToInt(presetFileData[locator]);
                    locator++;
                    for(int i = 0; i < size; i++)
                    {
                        HealthUpgrades tempHU = new HealthUpgrades();

                        //startHP
                        tempHU.startHP = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1]);
                        locator += 2;

                        //startFP
                        tempHU.startFP = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1]);
                        locator += 2;

                        //startHP
                        tempHU.startBP = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1]);
                        locator += 2;

                        //upgradeHP
                        tempHU.upgradeHP = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1]);
                        locator += 2;

                        //upgradeFP
                        tempHU.upgradeFP = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1]);
                        locator += 2;

                        //upgradeBP
                        tempHU.upgradeBP = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1]);
                        locator += 2;

                        tempUnit.addHealthUpgradeStruct(tempHU);
                    }
                }
                    
                units.add(tempUnit);
            }

            return units;
        }
        else if(presetFileData[locator] == 0x01)
        {
            //BattleUnitKind
            locator++;
            BattleUnitKind tempBUK = new BattleUnitKind();

            //HP
            tempBUK.HP = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1]);
            locator += 2;

            //DangerHP
            tempBUK.dangerHP = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //PerilHP
            tempBUK.perilHP = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Level
            tempBUK.level = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //BonusXP
            tempBUK.bonusXP = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //BonusCoin
            tempBUK.bonusCoin = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //BonusCoinRate
            tempBUK.bonusCoinRate = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //BaseCoin
            tempBUK.baseCoin = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //RunRate
            tempBUK.runRate = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //pbCap
            tempBUK.pbCap = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1]);
            locator += 2;

            //SwallowChance
            tempBUK.swallowChance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //SwallowAttribute
            tempBUK.swallowAttribute = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //UltraHammerKnockChance
            tempBUK.ultraHammerKnockChance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //ItemStealParameter
            tempBUK.itemStealParameter = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //UnitAttributeFlags
            int bitfieldData = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            tempBUK.MapObj = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.MapObj);
            tempBUK.OutOfReach = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.OutOfReach);
            tempBUK.Unquakeable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.Unquakeable);
            tempBUK.IsInvisible = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.IsInvisible);
            tempBUK.IsVeiled = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.IsVeiled);
            tempBUK.ShellShielded = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.ShellShielded);
            tempBUK.NeverTargetable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.NeverTargetable);
            tempBUK.LimitSwitch = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.LimitSwitch);
            tempBUK.DisableZeroGravityFloat = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.DisableZeroGravityFloat);
            tempBUK.DisableZeroGravityImmobility = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.DisableZeroGravityImmobility);
            tempBUK.immuneToUltraHammerKnock = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.immuneToUltraHammerKnock);
            tempBUK.IsUndead = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.IsUndead);
            tempBUK.IsCorpse = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.IsCorpse);
            tempBUK.IsLeader = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.IsLeader);
            tempBUK.CannotTakeActions = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.CannotTakeActions);
            tempBUK.NotSpunByLoveSlap = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.NotSpunByLoveSlap);
            tempBUK.DisableDamageStars = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.DisableDamageStars);
            tempBUK.DisableAllPartVisibility = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.DisableAllPartVisibility);
            tempBUK.DisableHPGauge = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.DisableHPGauge);
            tempBUK.LookCamera = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.LookCamera);
            tempBUK.NonCombatant = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.NonCombatant);
            tempBUK.NoShadow = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.NoShadow);
            tempBUK.DisableDamage = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKind.UnitAttributeFlags.DisableDamage);
            locator += 4;

            tempUnit.addBattleUnitKindStruct(tempBUK);
            units.add(tempUnit);
            return units;
        }
        else if(presetFileData[locator] == 0x02)
        {
            //BattleUnitKindPart
            locator++;
            BattleUnitKindPart tempBUKP = new BattleUnitKindPart();

            int bitfieldData = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            tempBUKP.MainBodyPart = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.MainBodyPart);
            tempBUKP.SecondaryBodyPart = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.SecondaryBodyPart);
            tempBUKP.BombableBodyPart = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.BombableBodyPart);
            tempBUKP.GuardBodyPart = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.GuardBodyPart);
            tempBUKP.NotBombableBodyPart = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.NotBombableBodyPart);
            tempBUKP.InHole = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.InHole);
            tempBUKP.WeakToAttackFxR = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.WeakToAttackFxR);
            tempBUKP.WeakToIcePower = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.WeakToIcePower);
            tempBUKP.IsWinged = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsWinged);
            tempBUKP.IsShelled = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsShelled);
            tempBUKP.IsBombFlippable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsBombFlippable);
            tempBUKP.IsClonelike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsClonelike);
            tempBUKP.DisableFlatPaperLayering = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.DisableFlatPaperLayering);
            tempBUKP.NeverTargetable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.NeverTargetable);
            tempBUKP.IgnoreMapObjectOffset = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IgnoreMapObjectOffset);
            tempBUKP.IgnoreOnlyTargetSelectAndPreferredParts = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IgnoreOnlyTargetSelectAndPreferredParts);
            tempBUKP.Untattleable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Untattleable);
            tempBUKP.JumplikeCannotTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.JumplikeCannotTarget);
            tempBUKP.HammerlikeCannotTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.HammerlikeCannotTarget);
            tempBUKP.ShellTosslikeCannotTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.ShellTosslikeCannotTarget);
            tempBUKP.PreventHealthDecrease = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.PreventHealthDecrease);
            tempBUKP.DisablePartVisibility = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.DisablePartVisibility);
            tempBUKP.ImmuneToCustom = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.ImmuneToCustom);
            tempBUKP.BlurOn = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.BlurOn);
            tempBUKP.ScaleIndependence = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.ScaleIndependence);
            tempBUKP.Independence = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.Independence);
            tempBUKP.IsImmuneToDamageOrStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsImmuneToDamageOrStatus);
            tempBUKP.IsImmuneToOHKO = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsImmuneToOHKO);
            tempBUKP.IsImmuneToStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsAttributeFlags.IsImmuneToStatus);
            locator += 4;

            //PartsCounterAttributeFlags
            bitfieldData = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            tempBUKP.TopSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.TopSpiky);
            tempBUKP.PreemptiveFrontSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.PreemptiveFrontSpiky);
            tempBUKP.FrontSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.FrontSpiky);
            tempBUKP.Fiery = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Fiery);
            tempBUKP.FieryStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.FieryStatus);
            tempBUKP.Icy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Icy);
            tempBUKP.IcyStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.IcyStatus);
            tempBUKP.Poison = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Poison);
            tempBUKP.PoisonStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.PoisonStatus);
            tempBUKP.Electric = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Electric);
            tempBUKP.ElectricStatus = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.ElectricStatus);
            tempBUKP.Explosive = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.Explosive);
            tempBUKP.VolatileExplosive = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleUnitKindPart.PartsCounterAttributeFlags.VolatileExplosive);
            locator += 4;

            tempUnit.addBattleUnitKindPartStruct(tempBUKP);
            units.add(tempUnit);
            return units;
        }
        else if(presetFileData[locator] == 0x03)
        {
            //BattleUnitDefense
            locator++;
            BattleUnitDefense tempBUD = new BattleUnitDefense();

            //Normal
            tempBUD.normal = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Fire
            tempBUD.fire = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Ice
            tempBUD.ice = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Explosion
            tempBUD.explosion = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Electric
            tempBUD.electric = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            tempUnit.addBattleUnitDefenseStruct(tempBUD);
            units.add(tempUnit);
            return units;
        }
        else if(presetFileData[locator] == 0x04)
        {
            //BattleUnitDefenseAttr
            locator++;
            BattleUnitDefenseAttr tempBUDA = new BattleUnitDefenseAttr();

            //Normal
            tempBUDA.normal = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Fire
            tempBUDA.fire = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Ice
            tempBUDA.ice = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Explosion
            tempBUDA.explosion = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Electric
            tempBUDA.electric = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            tempUnit.addBattleUnitDefenseAttrStruct(tempBUDA);
            units.add(tempUnit);
            return units;
        }
        else if(presetFileData[locator] == 0x05)
        {
            //StatusVulnerability
            locator++;
            StatusVulnerability tempSV = new StatusVulnerability();

            //Sleep
            tempSV.sleep = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Stop
            tempSV.stop = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Dizzy
            tempSV.dizzy = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Poison
            tempSV.poison = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Confuse
            tempSV.confuse = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Electric
            tempSV.electric = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Burn
            tempSV.burn = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Freeze
            tempSV.freeze = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Huge
            tempSV.huge = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Tiny
            tempSV.tiny = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Attack Up
            tempSV.attack_up = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Attack Down
            tempSV.attack_down = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Defense Up
            tempSV.defense_up = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Defense Down
            tempSV.defense_down = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Allergic
            tempSV.allergic = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Fright
            tempSV.fright = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Gale Force
            tempSV.gale_force = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Fast
            tempSV.fast = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Slow
            tempSV.slow = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;               
            
            //Dodgy
            tempSV.dodgy = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //Invisible
            tempSV.invisible = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;
            
            //OHKO             
            tempSV.ohko = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            tempUnit.addStatusVulnerabilityStruct(tempSV);
            units.add(tempUnit);
            return units;
            
        }
        else if(presetFileData[locator] == 0x06)
        {
            //BattleWeapon
            locator++;
            BattleWeapon tempBW = new BattleWeapon();

            //Accuracy
            tempBW.accuracy = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //FP Cost
            tempBW.fp_cost = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //SP Cost
            tempBW.sp_cost = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Superguard State
            tempBW.superguard_state = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Stylish Multiplier
            tempBW.sylish_multiplier = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Bingo Slot Increase Chance
            tempBW.bingo_slot_inc_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Base Damage fn
            tempBW.base_damage_fn = ByteUtils.bytesToLong(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //Base Damage 1
            tempBW.base_damage1 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //Base Damage 2
            tempBW.base_damage2 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //Base Damage 3
            tempBW.base_damage3 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //Base Damage 4
            tempBW.base_damage4 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //Base Damage 5
            tempBW.base_damage5 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //Base Damage 6
            tempBW.base_damage6 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //Base Damage 7
            tempBW.base_damage7 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //Base Damage 8
            tempBW.base_damage8 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //Base FP Damage 1
            tempBW.base_fpdamage1 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //Base FP Damage 2
            tempBW.base_fpdamage2 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //Base FP Damage 3
            tempBW.base_fpdamage3 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //Base FP Damage 4
            tempBW.base_fpdamage4 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //Base FP Damage 5
            tempBW.base_fpdamage5 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //Base FP Damage 6
            tempBW.base_fpdamage6 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //Base FP Damage 7
            tempBW.base_fpdamage7 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //Base FP Damage 8
            tempBW.base_fpdamage8 = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //TargetClassFlags
            int bitfieldData = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            tempBW.CannotTargetMarioOrShellShield = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetMarioOrShellShield);
            tempBW.CannotTargetPartner = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetPartner);
            tempBW.CannotTargetEnemy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetEnemy);
            tempBW.CannotTargetTreeOrSwitch = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetTreeOrSwitch);
            tempBW.CannotTargetSystem = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetSystem);
            tempBW.CannotTargetOppositeAlliance = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetOppositeAlliance);
            tempBW.CannotTargetOwnAlliance = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetOwnAlliance);
            tempBW.CannotTargetSelf = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetSelf);
            tempBW.CannotTargetSameSpecies = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetSameSpecies);
            tempBW.OnlyTargetSelf = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.OnlyTargetSelf);
            tempBW.OnlyTargetMario = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.OnlyTargetMario);
            tempBW.OnlyTargetTreeOrSwitch = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.OnlyTargetTreeOrSwitch);
            tempBW.OnlyTargetPreferredParts = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.OnlyTargetPreferredParts);
            tempBW.OnlyTargetSelectParts = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.OnlyTargetSelectParts);
            tempBW.SingleTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.SingleTarget);
            tempBW.MultipleTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.MultipleTarget);
            tempBW.CannotTargetAnything = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetClassFlags.CannotTargetAnything);
            locator += 4;

            //TargetPropertyFlags
            bitfieldData = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            tempBW.Tattlelike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Tattlelike);
            tempBW.CannotTargetFloating2 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetFloating2);
            tempBW.CannotTargetCeiling = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetCeiling);
            tempBW.CannotTargetFloating = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetFloating);
            tempBW.CannotTargetGrounded = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetGrounded);
            tempBW.Jumplike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Jumplike);
            tempBW.Hammerlike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.Hammerlike);
            tempBW.ShellTosslike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.ShellTosslike);
            tempBW.CannotTargetGroundedVariant = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetGroundedVariant);
            tempBW.RecoilDamage = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.RecoilDamage);
            tempBW.CanOnlyTargetFrontmost = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CanOnlyTargetFrontmost);
            tempBW.CannotTargetShellShield = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetShellShield);
            tempBW.CannotTargetCustom = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.CannotTargetCustom);
            tempBW.TargetSameAllianceDirection = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.TargetSameAllianceDirection);
            tempBW.TargetOppositeAllianceDirection = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetPropertyFlags.TargetOppositeAllianceDirection);
            locator += 4;

            //Element
            tempBW.element = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Damage Pattern
            tempBW.damage_pattern = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //AC Level
            tempBW.ac_level = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //SpecialPropertyFlags
            bitfieldData = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            tempBW.BadgeCanAffectPower = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.BadgeCanAffectPower);
            tempBW.StatusCanAffectPower = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.StatusCanAffectPower);
            tempBW.IsChargeable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.IsChargeable);
            tempBW.CannotMiss = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.CannotMiss);
            tempBW.DiminishingReturnsByHit = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.DiminishingReturnsByHit);
            tempBW.DiminishingReturnsByTarget = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.DiminishingReturnsByTarget);
            tempBW.PiercesDefense = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.PiercesDefense);
            tempBW.CanBreakIce = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.CanBreakIce);
            tempBW.IgnoreTargetStatusVulnerability = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.IgnoreTargetStatusVulnerability);
            tempBW.SPFx200 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.SPFx200);
            tempBW.IgnitesIfBurned = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.IgnitesIfBurned);
            tempBW.PlayActiveFXSound = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.PlayActiveFXSound);
            tempBW.FlipsShellEnemies = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.FlipsShellEnemies);
            tempBW.FlipsBombFlippableEnemies = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.FlipsBombFlippableEnemies);
            tempBW.GroundsWingedEnemies = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.GroundsWingedEnemies);
            tempBW.SPFx8000 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.SPFx8000);
            tempBW.CanBeUsedAsConfusedAction = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.CanBeUsedAsConfusedAction);
            tempBW.Unguardable = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.Unguardable);
            tempBW.CanHitClonelike = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.SpecialPropertyFlags.CanHitClonelike);
            locator += 4;

            //CounterResistanceFlags
            bitfieldData = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            tempBW.Electric = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Electric);
            tempBW.TopSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.TopSpiky);
            tempBW.PreemptiveFrontSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.PreemptiveFrontSpiky);
            tempBW.FrontSpiky = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.FrontSpiky);
            tempBW.Fiery = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Fiery);
            tempBW.Icy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Icy);
            tempBW.Poison = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Poison);
            tempBW.Explosive = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Explosive);
            tempBW.VolatileExplosive = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.VolatileExplosive);
            tempBW.Payback = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.Payback);
            tempBW.HoldFast = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.CounterResistanceFlags.HoldFast);
            locator += 4;

            //TargetWeightingFlags
            bitfieldData = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            tempBW.PreferMario = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferMario);
            tempBW.PreferPartner = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferPartner);
            tempBW.PreferFront = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferFront);
            tempBW.PreferBack = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferBack);
            tempBW.PreferSameAlliance = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferSameAlliance);
            tempBW.PreferOppositeAlliance = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferOppositeAlliance);
            tempBW.PreferLessHealthy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferLessHealthy);
            tempBW.GreatlyPreferLessHealthy = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.GreatlyPreferLessHealthy);
            tempBW.PreferLowerHP = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferLowerHP);
            tempBW.PreferHigherHP = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferHigherHP);
            tempBW.PreferInPeril = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.PreferInPeril);
            tempBW.TWFx2000 = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.TWFx2000);
            tempBW.ChooseWeightedRandomly = ByteUtils.bitFieldFlagCheck(bitfieldData, BattleWeapon.TargetWeightingFlags.ChooseWeightedRandomly);
            locator += 4;

            //Sleep Chance
            tempBW.sleep_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Sleep Time
            tempBW.sleep_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Stop Chance
            tempBW.stop_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Stop Time
            tempBW.stop_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Dizzy Chance
            tempBW.dizzy_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Dizzy Time
            tempBW.dizzy_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Poison Chance
            tempBW.poison_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Poison Time
            tempBW.poison_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Poison Strength
            tempBW.poison_strength = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Confuse Chance
            tempBW.confuse_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Confuse Time
            tempBW.confuse_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Electric Chance
            tempBW.electric_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Electric Time
            tempBW.electric_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Dodgy Chance
            tempBW.dodgy_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Dodgy Time
            tempBW.dodgy_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Burn Chance
            tempBW.burn_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Burn Time
            tempBW.burn_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Freeze Chance
            tempBW.freeze_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Freeze Time
            tempBW.freeze_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Size Change Chance
            tempBW.size_change_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Size Change Time
            tempBW.size_change_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Size Change Strength
            tempBW.size_change_strength = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Atk Change Chance
            tempBW.atk_change_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Atk Change Time
            tempBW.atk_change_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Atk Change Strength
            tempBW.atk_change_strength = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Def Change Chance
            tempBW.def_change_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Def Change Time
            tempBW.def_change_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Def Change Strength
            tempBW.def_change_strength = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Allergic Chance
            tempBW.allergic_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Allergic Time
            tempBW.allergic_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //OHKO Chance
            tempBW.ohko_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Charge Strength
            tempBW.charge_strength = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Fast Chance
            tempBW.fast_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Fast Time
            tempBW.fast_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Slow Chance
            tempBW.slow_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Slow Time
            tempBW.slow_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Fright Chance
            tempBW.fright_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Gale Force Chance
            tempBW.gale_force_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Payback Time
            tempBW.payback_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Hold Fast Time
            tempBW.hold_fast_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Invisible Chance
            tempBW.invisible_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Invisible Time
            tempBW.invisible_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //HP Regen Time
            tempBW.hp_regen_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //HP Regen Strength
            tempBW.hp_regen_strength = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //FP Regen Time
            tempBW.fp_regen_time = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //FP Regen Strength
            tempBW.fp_regen_strength = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //Attack Script
            tempBW.attack_evt = ByteUtils.bytesToLong(presetFileData[locator], presetFileData[locator + 1], presetFileData[locator + 2], presetFileData[locator + 3]);
            locator += 4;

            //stage_background_fallweight1
            tempBW.stage_background_fallweight1 = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //stage_background_fallweight2
            tempBW.stage_background_fallweight2 = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //stage_background_fallweight3
            tempBW.stage_background_fallweight3 = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //stage_background_fallweight4
            tempBW.stage_background_fallweight4 = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //stage_nozzle_turn_chance
            tempBW.stage_nozzle_turn_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //stage_nozzle_fire_chance
            tempBW.stage_nozzle_fire_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //stage_ceiling_fall_chance
            tempBW.stage_ceiling_fall_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            //stage_object_fall_chance
            tempBW.stage_object_fall_chance = ByteUtils.bytesToInt(presetFileData[locator]);
            locator++;

            tempUnit.addBattleWeaponStruct(tempBW);
            units.add(tempUnit);
            return units;
        }
        else if(presetFileData[locator] == 0x07)
        {
            //HealthUpgrades
            locator++;
            HealthUpgrades tempHU = new HealthUpgrades();

            //startHP
            tempHU.startHP = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1]);
            locator += 2;

            //startFP
            tempHU.startFP = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1]);
            locator += 2;

            //startHP
            tempHU.startBP = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1]);
            locator += 2;

            //upgradeHP
            tempHU.upgradeHP = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1]);
            locator += 2;

            //upgradeFP
            tempHU.upgradeFP = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1]);
            locator += 2;

            //upgradeBP
            tempHU.upgradeBP = ByteUtils.bytesToInt(presetFileData[locator], presetFileData[locator + 1]);
            locator += 2;

            tempUnit.addHealthUpgradeStruct(tempHU);
            units.add(tempUnit);
            return units;
        }

        return units;
    }

    /**
     * @Author Jemaroo
     * @Function Will export the array of units into a Battle Unit Preset file
     */
    public static byte[] buildPresetFile(File givenFile, ArrayList<BUTData> units)
    {
        ArrayList<Byte> presetFileDataList = new ArrayList<Byte>();
        final Byte marker = 0x00;
        presetFileDataList.add(marker);

        presetFileDataList.add(ByteUtils.intTo1Byte(units.size()));
        for(int i = 0; i < units.size(); i++)
        {
            //BattleUnitKind
            presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.size()));
            for(int j = 0; j < units.get(i).BattleUnitKindData.size(); j++)
            {
                //HP
                byte[] tempHP = ByteUtils.intTo2Bytes(units.get(i).BattleUnitKindData.get(j).HP);
                for(int k = 0; k < 2 ; k++)
                {
                    presetFileDataList.add(tempHP[k]);
                }

                //DangerHP
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).dangerHP));

                //PerilHP
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).perilHP));

                //Level
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).level));

                //BonusXP
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).bonusXP));
                
                //BonusCoin
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).bonusCoin));

                //BonusCoinRate
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).bonusCoinRate));

                //BaseCoin
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).baseCoin));

                //RunRate
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).runRate));

                //pbCap
                byte[] tempPBCap = ByteUtils.intTo2Bytes(units.get(i).BattleUnitKindData.get(j).pbCap);
                for(int k = 0; k < 2 ; k++)
                {
                    presetFileDataList.add(tempPBCap[k]);
                }

                //SwallowChance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).swallowChance));

                //SwallowAttribute
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).swallowAttribute));

                //UltraHammerKnockChance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).ultraHammerKnockChance));

                //ItemStealParameter
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitKindData.get(j).itemStealParameter));

                //UnitAttributeFlags
                byte[] tempUAF = ByteUtils.buildUAFBitfield(units.get(i).BattleUnitKindData.get(j));
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempUAF[k]);
                }
            }

            //BattleUnitKindPart
            presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitKindPartData.size()));
            for(int j = 0; j < units.get(i).BattleUnitKindPartData.size(); j++)
            {
                //PartsAttributeFlags
                byte[] tempPAF = ByteUtils.buildPAFBitfield(units.get(i).BattleUnitKindPartData.get(j));
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempPAF[k]);
                }

                //PartsCounterAttributeFlags
                byte[] tempPCAF = ByteUtils.buildPCAFBitfield(units.get(i).BattleUnitKindPartData.get(j));
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempPCAF[k]);
                }
            }

            //BattleUnitDefense
            presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseData.size()));
            for(int j = 0; j < units.get(i).BattleUnitDefenseData.size(); j++)
            {
                //Normal
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseData.get(j).normal));

                //Fire
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseData.get(j).fire));

                //Ice
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseData.get(j).ice));

                //Explosion
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseData.get(j).explosion));

                //Electric
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseData.get(j).electric));
            }

            //BattleUnitDefenseAttr
            presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseAttrData.size()));
            for(int j = 0; j < units.get(i).BattleUnitDefenseAttrData.size(); j++)
            {
                //Normal
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseAttrData.get(j).normal));

                //Fire
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseAttrData.get(j).fire));

                //Ice
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseAttrData.get(j).ice));

                //Explosion
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseAttrData.get(j).explosion));

                //Electric
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleUnitDefenseAttrData.get(j).electric));
            }

            //StatusVulnerability
            presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.size()));
            for(int j = 0; j < units.get(i).StatusVulnerabilityData.size(); j++)
            {
                //Sleep
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).sleep));

                //Stop
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).stop));
                
                //Dizzy
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).dizzy));
                
                //Poison
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).poison));
                
                //Confuse
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).confuse));
                
                //Electric
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).electric));
                
                //Burn
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).burn));
                
                //Freeze
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).freeze));
                
                //Huge
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).huge));
                
                //Tiny
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).tiny));
                
                //Attack Up
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).attack_up));
                
                //Attack Down
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).attack_down));
                
                //Defense Up
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).defense_up));
                
                //Defense Down
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).defense_down));
                
                //Allergic
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).allergic));
                
                //Fright
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).fright));
                
                //Gale Force
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).gale_force));
                
                //Fast
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).fast));
                
                //Slow
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).slow));                 
                
                //Dodgy
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).dodgy));
                
                //Invisible
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).invisible));
                
                //OHKO             
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).StatusVulnerabilityData.get(j).ohko));
            }

            //BattleWeapon
            presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.size()));
            for(int j = 0; j < units.get(i).BattleWeaponData.size(); j++)
            {
                //Accuracy
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).accuracy));

                //FP Cost
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).fp_cost));

                //SP Cost
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).sp_cost));

                //Superguard State
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).superguard_state));

                //Stylish Multiplier
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).sylish_multiplier));

                //Bingo Slot Increase Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).bingo_slot_inc_chance));

                //Base Damage fn
                byte[] tempBDfn = ByteUtils.longTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage_fn);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBDfn[k]);
                }

                //Base Damage 1
                byte[] tempBD1 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage1);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBD1[k]);
                }

                //Base Damage 2
                byte[] tempBD2 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage2);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBD2[k]);
                }

                //Base Damage 3
                byte[] tempBD3 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage3);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBD3[k]);
                }

                //Base Damage 4
                byte[] tempBD4 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage4);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBD4[k]);
                }

                //Base Damage 5
                byte[] tempBD5 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage5);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBD5[k]);
                }

                //Base Damage 6
                byte[] tempBD6 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage6);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBD6[k]);
                }

                //Base Damage 7
                byte[] tempBD7 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage7);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBD7[k]);
                }

                //Base Damage 8
                byte[] tempBD8 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_damage8);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBD8[k]);
                }

                //Base FP Damage 1
                byte[] tempBFPD1 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_fpdamage1);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBFPD1[k]);
                }

                //Base FP Damage 2
                byte[] tempBFPD2 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_fpdamage2);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBFPD2[k]);
                }

                //Base FP Damage 3
                byte[] tempBFPD3 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_fpdamage3);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBFPD3[k]);
                }

                //Base FP Damage 4
                byte[] tempBFPD4 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_fpdamage4);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBFPD4[k]);
                }

                //Base FP Damage 5
                byte[] tempBFPD5 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_fpdamage5);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBFPD5[k]);
                }

                //Base FP Damage 6
                byte[] tempBFPD6 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_fpdamage6);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBFPD6[k]);
                }

                //Base FP Damage 7
                byte[] tempBFPD7 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_fpdamage7);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBFPD7[k]);
                }

                //Base FP Damage 8
                byte[] tempBFPD8 = ByteUtils.intTo4Bytes(units.get(i).BattleWeaponData.get(j).base_fpdamage8);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempBFPD8[k]);
                }

                //TargetClassFlags
                byte[] tempTCF = ByteUtils.buildTCFBitfield(units.get(i).BattleWeaponData.get(j));
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempTCF[k]);
                }

                //TargetPropertyFlags
                byte[] tempTPF = ByteUtils.buildTPFBitfield(units.get(i).BattleWeaponData.get(j));
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempTPF[k]);
                }

                //Element
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).element));

                //Damage Pattern
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).damage_pattern));

                //AC Level
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).ac_level));

                //SpecialPropertyFlags
                byte[] tempSPF = ByteUtils.buildSPFBitfield(units.get(i).BattleWeaponData.get(j));
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempSPF[k]);
                }

                //CounterResistanceFlags
                byte[] tempCRF = ByteUtils.buildCRFBitfield(units.get(i).BattleWeaponData.get(j));
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempCRF[k]);
                }

                //TargetWeightingFlags
                byte[] tempTWF = ByteUtils.buildTWFBitfield(units.get(i).BattleWeaponData.get(j));
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempTWF[k]);
                }

                //Sleep Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).sleep_chance));

                //Sleep Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).sleep_time));

                //Stop Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stop_chance));

                //Stop Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stop_time));

                //Dizzy Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).dizzy_chance));

                //Dizzy Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).dizzy_time));

                //Poison Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).poison_chance));

                //Poison Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).poison_time));

                //Poison Strength
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).poison_strength));

                //Confuse Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).confuse_chance));

                //Confuse Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).confuse_time));

                //Electric Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).electric_chance));

                //Electric Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).electric_time));

                //Dodgy Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).dodgy_chance));

                //Dodgy Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).dodgy_time));

                //Burn Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).burn_chance));

                //Burn Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).burn_time));

                //Freeze Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).freeze_chance));

                //Freeze Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).freeze_time));

                //Size Change Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).size_change_chance));

                //Size Change Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).size_change_time));

                //Size Change Strength
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).size_change_strength));

                //Atk Change Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).atk_change_chance));

                //Atk Change Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).atk_change_time));

                //Atk Change Strength
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).atk_change_strength));

                //Def Change Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).def_change_chance));

                //Def Change Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).def_change_time));

                //Def Change Strength
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).def_change_strength));

                //Allergic Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).allergic_chance));

                //Allergic Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).allergic_time));

                //OHKO Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).ohko_chance));

                //Charge Strength
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).charge_strength));

                //Fast Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).fast_chance));

                //Fast Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).fast_time));

                //Slow Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).slow_chance));

                //Slow Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).slow_time));

                //Fright Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).fright_chance));

                //Gale Force Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).gale_force_chance));

                //Payback Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).payback_time));

                //Hold Fast Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).hold_fast_time));

                //Invisible Chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).invisible_chance));

                //Invisible Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).invisible_time));

                //HP Regen Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).hp_regen_time));

                //HP Regen Strength
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).hp_regen_strength));

                //FP Regen Time
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).fp_regen_time));

                //FP Regen Strength
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).fp_regen_strength));

                //Attack Script
                byte[] tempAS = ByteUtils.longTo4Bytes(units.get(i).BattleWeaponData.get(j).attack_evt);
                for(int k = 0; k < 4 ; k++)
                {
                    presetFileDataList.add(tempAS[k]);
                }

                //stage_background_fallweight1
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stage_background_fallweight1));

                //stage_background_fallweight2
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stage_background_fallweight2));

                //stage_background_fallweight3
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stage_background_fallweight3));

                //stage_background_fallweight4
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stage_background_fallweight4));

                //stage_nozzle_turn_chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stage_nozzle_turn_chance));

                //stage_nozzle_fire_chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stage_nozzle_fire_chance));

                //stage_ceiling_fall_chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stage_ceiling_fall_chance));

                //stage_object_fall_chance
                presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).BattleWeaponData.get(j).stage_object_fall_chance));
            }

            //HealthUpgrades
            presetFileDataList.add(ByteUtils.intTo1Byte(units.get(i).HealthUpgradesData.size()));
            for(int j = 0; j < units.get(i).HealthUpgradesData.size(); j++)
            {
                //startHP
                byte[] tempStartHP = ByteUtils.intTo2Bytes(units.get(i).HealthUpgradesData.get(j).startHP);
                for(int k = 0; k < 2 ; k++)
                {
                    presetFileDataList.add(tempStartHP[k]);
                }

                //startFP
                byte[] tempStartFP = ByteUtils.intTo2Bytes(units.get(i).HealthUpgradesData.get(j).startFP);
                for(int k = 0; k < 2 ; k++)
                {
                    presetFileDataList.add(tempStartFP[k]);
                }

                //startBP
                byte[] tempStartBP = ByteUtils.intTo2Bytes(units.get(i).HealthUpgradesData.get(j).startBP);
                for(int k = 0; k < 2 ; k++)
                {
                    presetFileDataList.add(tempStartBP[k]);
                }

                //upgradeHP
                byte[] tempUpgradeHP = ByteUtils.intTo2Bytes(units.get(i).HealthUpgradesData.get(j).upgradeHP);
                for(int k = 0; k < 2 ; k++)
                {
                    presetFileDataList.add(tempUpgradeHP[k]);
                }

                //upgradeFP
                byte[] tempUpgradeFP = ByteUtils.intTo2Bytes(units.get(i).HealthUpgradesData.get(j).upgradeFP);
                for(int k = 0; k < 2 ; k++)
                {
                    presetFileDataList.add(tempUpgradeFP[k]);
                }

                //upgradeBP
                byte[] tempUpgradeBP = ByteUtils.intTo2Bytes(units.get(i).HealthUpgradesData.get(j).upgradeBP);
                for(int k = 0; k < 2 ; k++)
                {
                    presetFileDataList.add(tempUpgradeBP[k]);
                }
            }
        }

        byte[] presetFileData = new byte[presetFileDataList.size()];
        for(int i = 0; i < presetFileDataList.size(); i++)
        {
            presetFileData[i] = presetFileDataList.get(i);
        }

        return presetFileData;
    }

    /**
     * @Author Jemaroo
     * @Function Will export a BattleUnitKind into a Battle Unit Preset file
     */
    public static byte[] buildPresetFile(File givenFile, BattleUnitKind buk)
    {
        ArrayList<Byte> presetFileDataList = new ArrayList<Byte>();
        final Byte marker = 0x01;
        presetFileDataList.add(marker);

        //HP
        byte[] tempHP = ByteUtils.intTo2Bytes(buk.HP);
        for(int k = 0; k < 2 ; k++)
        {
            presetFileDataList.add(tempHP[k]);
        }

        //DangerHP
        presetFileDataList.add(ByteUtils.intTo1Byte(buk.dangerHP));

        //PerilHP
        presetFileDataList.add(ByteUtils.intTo1Byte(buk.perilHP));

        //Level
        presetFileDataList.add(ByteUtils.intTo1Byte(buk.level));

        //BonusXP
        presetFileDataList.add(ByteUtils.intTo1Byte(buk.bonusXP));
        
        //BonusCoin
        presetFileDataList.add(ByteUtils.intTo1Byte(buk.bonusCoin));

        //BonusCoinRate
        presetFileDataList.add(ByteUtils.intTo1Byte(buk.bonusCoinRate));

        //BaseCoin
        presetFileDataList.add(ByteUtils.intTo1Byte(buk.baseCoin));

        //RunRate
        presetFileDataList.add(ByteUtils.intTo1Byte(buk.runRate));

        //pbCap
        byte[] tempPBCap = ByteUtils.intTo2Bytes(buk.pbCap);
        for(int k = 0; k < 2 ; k++)
        {
            presetFileDataList.add(tempPBCap[k]);
        }

        //SwallowChance
        presetFileDataList.add(ByteUtils.intTo1Byte(buk.swallowChance));

        //SwallowAttribute
        presetFileDataList.add(ByteUtils.intTo1Byte(buk.swallowAttribute));

        //UltraHammerKnockChance
        presetFileDataList.add(ByteUtils.intTo1Byte(buk.ultraHammerKnockChance));

        //ItemStealParameter
        presetFileDataList.add(ByteUtils.intTo1Byte(buk.itemStealParameter));

        //UnitAttributeFlags
        byte[] tempUAF = ByteUtils.buildUAFBitfield(buk);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempUAF[k]);
        }

        byte[] presetFileData = new byte[presetFileDataList.size()];
        for(int i = 0; i < presetFileDataList.size(); i++)
        {
            presetFileData[i] = presetFileDataList.get(i);
        }

        return presetFileData;
    }

    /**
     * @Author Jemaroo
     * @Function Will export a BattleUnitKindPart into a Battle Unit Preset file
     */
    public static byte[] buildPresetFile(File givenFile, BattleUnitKindPart bukp)
    {
        ArrayList<Byte> presetFileDataList = new ArrayList<Byte>();
        final Byte marker = 0x02;
        presetFileDataList.add(marker);

        //PartsAttributeFlags
        byte[] tempPAF = ByteUtils.buildPAFBitfield(bukp);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempPAF[k]);
        }

        //PartsCounterAttributeFlags
        byte[] tempPCAF = ByteUtils.buildPCAFBitfield(bukp);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempPCAF[k]);
        }

        byte[] presetFileData = new byte[presetFileDataList.size()];
        for(int i = 0; i < presetFileDataList.size(); i++)
        {
            presetFileData[i] = presetFileDataList.get(i);
        }

        return presetFileData;
    }

    /**
     * @Author Jemaroo
     * @Function Will export a BattleUnitDefense into a Battle Unit Preset file
     */
    public static byte[] buildPresetFile(File givenFile, BattleUnitDefense bud)
    {
        ArrayList<Byte> presetFileDataList = new ArrayList<Byte>();
        final Byte marker = 0x03;
        presetFileDataList.add(marker);

        //Normal
        presetFileDataList.add(ByteUtils.intTo1Byte(bud.normal));

        //Fire
        presetFileDataList.add(ByteUtils.intTo1Byte(bud.fire));

        //Ice
        presetFileDataList.add(ByteUtils.intTo1Byte(bud.ice));

        //Explosion
        presetFileDataList.add(ByteUtils.intTo1Byte(bud.explosion));

        //Electric
        presetFileDataList.add(ByteUtils.intTo1Byte(bud.electric));

        byte[] presetFileData = new byte[presetFileDataList.size()];
        for(int i = 0; i < presetFileDataList.size(); i++)
        {
            presetFileData[i] = presetFileDataList.get(i);
        }

        return presetFileData;
    }

    /**
     * @Author Jemaroo
     * @Function Will export a BattleUnitDefenseAttr into a Battle Unit Preset file
     */
    public static byte[] buildPresetFile(File givenFile, BattleUnitDefenseAttr buda)
    {
        ArrayList<Byte> presetFileDataList = new ArrayList<Byte>();
        final Byte marker = 0x04;
        presetFileDataList.add(marker);

        //Normal
        presetFileDataList.add(ByteUtils.intTo1Byte(buda.normal));

        //Fire
        presetFileDataList.add(ByteUtils.intTo1Byte(buda.fire));

        //Ice
        presetFileDataList.add(ByteUtils.intTo1Byte(buda.ice));

        //Explosion
        presetFileDataList.add(ByteUtils.intTo1Byte(buda.explosion));

        //Electric
        presetFileDataList.add(ByteUtils.intTo1Byte(buda.electric));

        byte[] presetFileData = new byte[presetFileDataList.size()];
        for(int i = 0; i < presetFileDataList.size(); i++)
        {
            presetFileData[i] = presetFileDataList.get(i);
        }

        return presetFileData;
    }

    /**
     * @Author Jemaroo
     * @Function Will export a StatusVulnerability into a Battle Unit Preset file
     */
    public static byte[] buildPresetFile(File givenFile, StatusVulnerability sv)
    {
        ArrayList<Byte> presetFileDataList = new ArrayList<Byte>();
        final Byte marker = 0x05;
        presetFileDataList.add(marker);

        //Sleep
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.sleep));

        //Stop
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.stop));
        
        //Dizzy
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.dizzy));
        
        //Poison
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.poison));
        
        //Confuse
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.confuse));
        
        //Electric
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.electric));
        
        //Burn
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.burn));
        
        //Freeze
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.freeze));
        
        //Huge
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.huge));
        
        //Tiny
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.tiny));
        
        //Attack Up
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.attack_up));
        
        //Attack Down
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.attack_down));
        
        //Defense Up
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.defense_up));
        
        //Defense Down
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.defense_down));
        
        //Allergic
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.allergic));
        
        //Fright
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.fright));
        
        //Gale Force
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.gale_force));
        
        //Fast
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.fast));
        
        //Slow
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.slow));                 
        
        //Dodgy
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.dodgy));
        
        //Invisible
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.invisible));
        
        //OHKO             
        presetFileDataList.add(ByteUtils.intTo1Byte(sv.ohko));

        byte[] presetFileData = new byte[presetFileDataList.size()];
        for(int i = 0; i < presetFileDataList.size(); i++)
        {
            presetFileData[i] = presetFileDataList.get(i);
        }

        return presetFileData;
    }

    /**
     * @Author Jemaroo
     * @Function Will export a BattleWeapon into a Battle Unit Preset file
     */
    public static byte[] buildPresetFile(File givenFile, BattleWeapon bw)
    {
        ArrayList<Byte> presetFileDataList = new ArrayList<Byte>();
        final Byte marker = 0x06;
        presetFileDataList.add(marker);

        //Accuracy
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.accuracy));

        //FP Cost
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.fp_cost));

        //SP Cost
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.sp_cost));

        //Superguard State
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.superguard_state));

        //Stylish Multiplier
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.sylish_multiplier));

        //Bingo Slot Increase Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.bingo_slot_inc_chance));

        //Base Damage fn
        byte[] tempBDfn = ByteUtils.longTo4Bytes(bw.base_damage_fn);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBDfn[k]);
        }

        //Base Damage 1
        byte[] tempBD1 = ByteUtils.intTo4Bytes(bw.base_damage1);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBD1[k]);
        }

        //Base Damage 2
        byte[] tempBD2 = ByteUtils.intTo4Bytes(bw.base_damage2);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBD2[k]);
        }

        //Base Damage 3
        byte[] tempBD3 = ByteUtils.intTo4Bytes(bw.base_damage3);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBD3[k]);
        }

        //Base Damage 4
        byte[] tempBD4 = ByteUtils.intTo4Bytes(bw.base_damage4);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBD4[k]);
        }

        //Base Damage 5
        byte[] tempBD5 = ByteUtils.intTo4Bytes(bw.base_damage5);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBD5[k]);
        }

        //Base Damage 6
        byte[] tempBD6 = ByteUtils.intTo4Bytes(bw.base_damage6);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBD6[k]);
        }

        //Base Damage 7
        byte[] tempBD7 = ByteUtils.intTo4Bytes(bw.base_damage7);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBD7[k]);
        }

        //Base Damage 8
        byte[] tempBD8 = ByteUtils.intTo4Bytes(bw.base_damage8);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBD8[k]);
        }

        //Base FP Damage 1
        byte[] tempBFPD1 = ByteUtils.intTo4Bytes(bw.base_fpdamage1);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBFPD1[k]);
        }

        //Base FP Damage 2
        byte[] tempBFPD2 = ByteUtils.intTo4Bytes(bw.base_fpdamage2);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBFPD2[k]);
        }

        //Base FP Damage 3
        byte[] tempBFPD3 = ByteUtils.intTo4Bytes(bw.base_fpdamage3);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBFPD3[k]);
        }

        //Base FP Damage 4
        byte[] tempBFPD4 = ByteUtils.intTo4Bytes(bw.base_fpdamage4);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBFPD4[k]);
        }

        //Base FP Damage 5
        byte[] tempBFPD5 = ByteUtils.intTo4Bytes(bw.base_fpdamage5);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBFPD5[k]);
        }

        //Base FP Damage 6
        byte[] tempBFPD6 = ByteUtils.intTo4Bytes(bw.base_fpdamage6);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBFPD6[k]);
        }

        //Base FP Damage 7
        byte[] tempBFPD7 = ByteUtils.intTo4Bytes(bw.base_fpdamage7);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBFPD7[k]);
        }

        //Base FP Damage 8
        byte[] tempBFPD8 = ByteUtils.intTo4Bytes(bw.base_fpdamage8);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempBFPD8[k]);
        }

        //TargetClassFlags
        byte[] tempTCF = ByteUtils.buildTCFBitfield(bw);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempTCF[k]);
        }

        //TargetPropertyFlags
        byte[] tempTPF = ByteUtils.buildTPFBitfield(bw);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempTPF[k]);
        }

        //Element
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.element));

        //Damage Pattern
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.damage_pattern));

        //AC Level
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.ac_level));

        //SpecialPropertyFlags
        byte[] tempSPF = ByteUtils.buildSPFBitfield(bw);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempSPF[k]);
        }

        //CounterResistanceFlags
        byte[] tempCRF = ByteUtils.buildCRFBitfield(bw);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempCRF[k]);
        }

        //TargetWeightingFlags
        byte[] tempTWF = ByteUtils.buildTWFBitfield(bw);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempTWF[k]);
        }

        //Sleep Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.sleep_chance));

        //Sleep Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.sleep_time));

        //Stop Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.stop_chance));

        //Stop Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.stop_time));

        //Dizzy Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.dizzy_chance));

        //Dizzy Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.dizzy_time));

        //Poison Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.poison_chance));

        //Poison Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.poison_time));

        //Poison Strength
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.poison_strength));

        //Confuse Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.confuse_chance));

        //Confuse Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.confuse_time));

        //Electric Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.electric_chance));

        //Electric Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.electric_time));

        //Dodgy Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.dodgy_chance));

        //Dodgy Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.dodgy_time));

        //Burn Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.burn_chance));

        //Burn Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.burn_time));

        //Freeze Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.freeze_chance));

        //Freeze Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.freeze_time));

        //Size Change Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.size_change_chance));

        //Size Change Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.size_change_time));

        //Size Change Strength
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.size_change_strength));

        //Atk Change Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.atk_change_chance));

        //Atk Change Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.atk_change_time));

        //Atk Change Strength
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.atk_change_strength));

        //Def Change Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.def_change_chance));

        //Def Change Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.def_change_time));

        //Def Change Strength
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.def_change_strength));

        //Allergic Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.allergic_chance));

        //Allergic Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.allergic_time));

        //OHKO Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.ohko_chance));

        //Charge Strength
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.charge_strength));

        //Fast Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.fast_chance));

        //Fast Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.fast_time));

        //Slow Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.slow_chance));

        //Slow Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.slow_time));

        //Fright Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.fright_chance));

        //Gale Force Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.gale_force_chance));

        //Payback Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.payback_time));

        //Hold Fast Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.hold_fast_time));

        //Invisible Chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.invisible_chance));

        //Invisible Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.invisible_time));

        //HP Regen Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.hp_regen_time));

        //HP Regen Strength
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.hp_regen_strength));

        //FP Regen Time
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.fp_regen_time));

        //FP Regen Strength
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.fp_regen_strength));

        //Attack Script
        byte[] tempAS = ByteUtils.longTo4Bytes(bw.attack_evt);
        for(int k = 0; k < 4 ; k++)
        {
            presetFileDataList.add(tempAS[k]);
        }

        //stage_background_fallweight1
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.stage_background_fallweight1));

        //stage_background_fallweight2
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.stage_background_fallweight2));

        //stage_background_fallweight3
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.stage_background_fallweight3));

        //stage_background_fallweight4
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.stage_background_fallweight4));

        //stage_nozzle_turn_chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.stage_nozzle_turn_chance));

        //stage_nozzle_fire_chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.stage_nozzle_fire_chance));

        //stage_ceiling_fall_chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.stage_ceiling_fall_chance));

        //stage_object_fall_chance
        presetFileDataList.add(ByteUtils.intTo1Byte(bw.stage_object_fall_chance));

        byte[] presetFileData = new byte[presetFileDataList.size()];
        for(int i = 0; i < presetFileDataList.size(); i++)
        {
            presetFileData[i] = presetFileDataList.get(i);
        }

        return presetFileData;
    }

    /**
     * @Author Jemaroo
     * @Function Will export a HealthUpgrade into a Battle Unit Preset file
     */
    public static byte[] buildPresetFile(File givenFile, HealthUpgrades hu)
    {
        ArrayList<Byte> presetFileDataList = new ArrayList<Byte>();
        final Byte marker = 0x07;
        presetFileDataList.add(marker);

        //startHP
        byte[] tempStartHP = ByteUtils.intTo2Bytes(hu.startHP);
        for(int k = 0; k < 2 ; k++)
        {
            presetFileDataList.add(tempStartHP[k]);
        }

        //startFP
        byte[] tempStartFP = ByteUtils.intTo2Bytes(hu.startFP);
        for(int k = 0; k < 2 ; k++)
        {
            presetFileDataList.add(tempStartFP[k]);
        }

        //startBP
        byte[] tempStartBP = ByteUtils.intTo2Bytes(hu.startBP);
        for(int k = 0; k < 2 ; k++)
        {
            presetFileDataList.add(tempStartBP[k]);
        }

        //upgradeHP
        byte[] tempUpgradeHP = ByteUtils.intTo2Bytes(hu.upgradeHP);
        for(int k = 0; k < 2 ; k++)
        {
            presetFileDataList.add(tempUpgradeHP[k]);
        }

        //upgradeFP
        byte[] tempUpgradeFP = ByteUtils.intTo2Bytes(hu.upgradeFP);
        for(int k = 0; k < 2 ; k++)
        {
            presetFileDataList.add(tempUpgradeFP[k]);
        }

        //upgradeBP
        byte[] tempUpgradeBP = ByteUtils.intTo2Bytes(hu.upgradeBP);
        for(int k = 0; k < 2 ; k++)
        {
            presetFileDataList.add(tempUpgradeBP[k]);
        }

        byte[] presetFileData = new byte[presetFileDataList.size()];
        for(int i = 0; i < presetFileDataList.size(); i++)
        {
            presetFileData[i] = presetFileDataList.get(i);
        }

        return presetFileData;
    }
}