import java.util.ArrayList;

/**
 * @Author Jemaroo
 * @Function Storage and functions for handling TTYD Unit Data
 * @Notes Storage types built using information from ttyd-utils by Jdaster64: https://github.com/jdaster64/ttyd-utils/blob/master/docs/ttyd_structures_pseudocode.txt
 */
public class BUTData 
{
    public String name = "";
    public ArrayList<BattleUnitKind> BattleUnitKindData = new ArrayList<>();
    public ArrayList<BattleUnitDefense> BattleUnitDefenseData = new ArrayList<>();
    public ArrayList<BattleUnitDefenseAttr> BattleUnitDefenseAttrData = new ArrayList<>();
    public ArrayList<StatusVulnerability> StatusVulnerabilityData = new ArrayList<>();
    public ArrayList<BattleWeapon> BattleWeaponData = new ArrayList<>();
    public ArrayList<BattleUnitKindPart> BattleUnitKindPartData = new ArrayList<>();
    public ArrayList<HealthUpgrades> HealthUpgradesData = new ArrayList<>();

    /**
     * @Author Jemaroo
     * @Function Will add a BattleUnitKind struct to the stored data list
     */
    public void addBattleUnitKindStruct(BattleUnitKind addedStruct)
    {
        BattleUnitKindData.add(addedStruct);
    }

    /**
     * @Author Jemaroo
     * @Function Will add a BattleUnitDefense struct to the stored data list
     */
    public void addBattleUnitDefenseStruct(BattleUnitDefense addedStruct)
    {
        BattleUnitDefenseData.add(addedStruct);
    }

    /**
     * @Author Jemaroo
     * @Function Will add a BattleUnitDefenseAttr struct to the stored data list
     */
    public void addBattleUnitDefenseAttrStruct(BattleUnitDefenseAttr addedStruct)
    {
        BattleUnitDefenseAttrData.add(addedStruct);
    }

    /**
     * @Author Jemaroo
     * @Function Will add a StatusVulnerability struct to the stored data list
     */
    public void addStatusVulnerabilityStruct(StatusVulnerability addedStruct)
    {
        StatusVulnerabilityData.add(addedStruct);
    }

    /**
     * @Author Jemaroo
     * @Function Will add a BattleWeapon struct to the stored data list
     */
    public void addBattleWeaponStruct(BattleWeapon addedStruct)
    {
        BattleWeaponData.add(addedStruct);
    }

    /**
     * @Author Jemaroo
     * @Function Will add a BattleUnitKindPart struct to the stored data list
     */
    public void addBattleUnitKindPartStruct(BattleUnitKindPart addedStruct)
    {
        BattleUnitKindPartData.add(addedStruct);
    }

    /**
     * @Author Jemaroo
     * @Function Will add a HealthUpgrades struct to the stored data list
     */
    public void addHealthUpgradeStruct(HealthUpgrades addedStruct)
    {
        HealthUpgradesData.add(addedStruct);
    }
}

/**
 * @Author Jemaroo
 * @Function Storage object for handling BattleUnitKind data
 * @Notes Built using information from ttyd-utils by Jdaster64: https://github.com/jdaster64/ttyd-utils/blob/master/docs/ttyd_structures_pseudocode.txt
 */
class BattleUnitKind
{
    public int HP = 0;
    public int dangerHP = 0;
    public int perilHP = 0;
    public int level = 0;
    public int bonusXP = 0;
    public int bonusCoin = 0;
    public int bonusCoinRate = 0;
    public int baseCoin = 0;
    public int runRate = 0;
    public int pbCap = 0;
    public int swallowChance = 0;
    public int swallowAttribute = 0;
    public int ultraHammerKnockChance = 0;
    public int itemStealParameter = 0;

    //UnitAttributeFlags
    public boolean MapObj = false;
    public boolean OutOfReach = false;
    public boolean Unquakeable = false;
    public boolean IsInvisible = false;
    public boolean IsVeiled = false;
    public boolean ShellShielded = false;
    public boolean NeverTargetable = false;
    public boolean LimitSwitch = false;
    public boolean DisableZeroGravityFloat = false;
    public boolean DisableZeroGravityImmobility = false;
    public boolean immuneToUltraHammerKnock = false;
    public boolean IsUndead = false;
    public boolean IsCorpse = false;
    public boolean IsLeader = false;
    public boolean CannotTakeActions = false;
    public boolean NotSpunByLoveSlap = false;
    public boolean DisableDamageStars = false;
    public boolean DisableAllPartVisibility = false;
    public boolean DisableHPGauge = false;
    public boolean LookCamera = false;
    public boolean NonCombatant = false;
    public boolean NoShadow = false;
    public boolean DisableDamage = false;

    public final int HP_offset = 8;
    public final int dangerHP_offset = 12;
    public final int perilHP_offset = 13;
    public final int level_offset = 14;
    public final int bonusXP_offset = 15;
    public final int bonusCoin_offset = 16;
    public final int bonusCoinRate_offset = 17;
    public final int baseCoin_offset = 18;
    public final int runRate_offset = 19;
    public final int pbCap_offset = 20;
    public final int swallowChance_offset = 138;
    public final int swallowAttribute_offset = 139;
    public final int ultraHammerKnockChance_offset = 140;
    public final int itemStealParameter_offset = 141;
    public final int UnitAttributeFlags_offset = 172;

    public enum UnitAttributeFlags 
    {
        MapObj(0x1),
        OutOfReach(0x2),
        Unquakeable(0x4),
        IsInvisible(0x8),
        IsVeiled(0x10),
        ShellShielded(0x20),
        NeverTargetable(0x40),
        LimitSwitch(0x100),
        DisableZeroGravityFloat(0x1000),
        DisableZeroGravityImmobility(0x2000),
        immuneToUltraHammerKnock(0x4000),
        IsUndead(0x10000),
        IsCorpse(0x20000),
        IsLeader(0x40000),
        CannotTakeActions(0x80000),
        NotSpunByLoveSlap(0x200000),
        DisableDamageStars(0x400000),
        DisableAllPartVisibility(0x1000000),
        DisableHPGauge(0x2000000),
        LookCamera(0x4000000),
        NonCombatant(0x10000000),
        NoShadow(0x20000000),
        DisableDamage(0x40000000);

        public final int mask;
        
        UnitAttributeFlags(int mask) 
        {
            this.mask = mask;
        }
    }
}

/**
 * @Author Jemaroo
 * @Function Storage object for handling BattleUnitKindPart data
 * @Notes Built using information from ttyd-utils by Jdaster64: https://github.com/jdaster64/ttyd-utils/blob/master/docs/ttyd_structures_pseudocode.txt
 */
class BattleUnitKindPart
{
    //PartsAttributeFlags
    public boolean MainBodyPart = false;
    public boolean SecondaryBodyPart = false;
    public boolean BombableBodyPart = false;
    public boolean GuardBodyPart = false;
    public boolean NotBombableBodyPart = false;
    public boolean InHole = false;
    public boolean WeakToAttackFxR = false;
    public boolean WeakToIcePower = false;
    public boolean IsWinged = false;
    public boolean IsShelled = false;
    public boolean IsBombFlippable = false;
    public boolean IsClonelike = false;
    public boolean DisableFlatPaperLayering = false;
    public boolean NeverTargetable = false;
    public boolean IgnoreMapObjectOffset = false;
    public boolean IgnoreOnlyTargetSelectAndPreferredParts = false;
    public boolean Untattleable = false;
    public boolean JumplikeCannotTarget = false;
    public boolean HammerlikeCannotTarget = false;
    public boolean ShellTosslikeCannotTarget = false;
    public boolean PreventHealthDecrease = false;
    public boolean DisablePartVisibility = false;
    public boolean ImmuneToCustom = false;
    public boolean BlurOn = false;
    public boolean ScaleIndependence = false;
    public boolean Independence = false;
    public boolean IsImmuneToDamageOrStatus = false;
    public boolean IsImmuneToOHKO = false;
    public boolean IsImmuneToStatus = false;

    //PartsCounterAttributeFlags
    public boolean TopSpiky = false;
    public boolean PreemptiveFrontSpiky = false;
    public boolean FrontSpiky = false;
    public boolean Fiery = false;
    public boolean FieryStatus = false;
    public boolean Icy = false;
    public boolean IcyStatus = false;
    public boolean Poison = false;
    public boolean PoisonStatus = false;
    public boolean Electric = false;
    public boolean ElectricStatus = false;
    public boolean Explosive = false;
    public boolean VolatileExplosive = false;

    public final int PartsAttributeFlags_offset = 64;
    public final int PartsCounterAttributeFlags_offset = 68;

    public enum PartsAttributeFlags 
    {
        MainBodyPart(0x1),
        SecondaryBodyPart(0x2),
        BombableBodyPart(0x4),
        GuardBodyPart(0x8),
        NotBombableBodyPart(0x10),
        InHole(0x40),
        WeakToAttackFxR(0x80),
        WeakToIcePower(0x100),
        IsWinged(0x800),
        IsShelled(0x1000),
        IsBombFlippable(0x2000),
        IsClonelike(0x4000),
        DisableFlatPaperLayering(0x8000),
        NeverTargetable(0x10000),
        IgnoreMapObjectOffset(0x20000),
        IgnoreOnlyTargetSelectAndPreferredParts(0x40000),
        Untattleable(0x80000),
        JumplikeCannotTarget(0x100000),
        HammerlikeCannotTarget(0x200000),
        ShellTosslikeCannotTarget(0x400000),
        PreventHealthDecrease(0x800000),
        DisablePartVisibility(0x1000000),
        ImmuneToCustom(0x2000000),
        BlurOn(0x4000000),
        ScaleIndependence(0x8000000),
        Independence(0x10000000),
        IsImmuneToDamageOrStatus(0x20000000),
        IsImmuneToOHKO(0x40000000),
        IsImmuneToStatus(0x80000000);

        public final int mask;
        
        PartsAttributeFlags(int mask) 
        {
            this.mask = mask;
        }
    }

    public enum PartsCounterAttributeFlags 
    {
        TopSpiky(0x1),
        PreemptiveFrontSpiky(0x2),
        FrontSpiky(0x4),
        Fiery(0x10),
        FieryStatus(0x20),
        Icy(0x40),
        IcyStatus(0x80),
        Poison(0x100),
        PoisonStatus(0x200),
        Electric(0x400),
        ElectricStatus(0x800),
        Explosive(0x1000),
        VolatileExplosive(0x2000);

        public final int mask;
        
        PartsCounterAttributeFlags(int mask) 
        {
            this.mask = mask;
        }
    }
}

/**
 * @Author Jemaroo
 * @Function Storage object for handling HealthUpgrade data
 */
class HealthUpgrades
{
    public int startHP = 0; //Partner: Level 1 HP
    public int startFP = 0; //Partner: Level 2 HP
    public int startBP = 0; //Partner: Level 3 HP
    public int upgradeHP = 0; 
    public int upgradeFP = 0; 
    public int upgradeBP = 0; 
}

/**
 * @Author Jemaroo
 * @Function Storage object for handling BattleUnitDefense data
 * @Notes Built using information from ttyd-utils by Jdaster64: https://github.com/jdaster64/ttyd-utils/blob/master/docs/ttyd_structures_pseudocode.txt
 */
class BattleUnitDefense
{
    public int normal = 0;
    public int fire = 0;
    public int ice = 0;
    public int explosion = 0;
    public int electric = 0;

    public final int normal_offset = 0;
    public final int fire_offset = 1;
    public final int ice_offset = 2;
    public final int explosion_offset = 3;
    public final int electric_offset =  4;
}

/**
 * @Author Jemaroo
 * @Function Storage object for handling BattleUnitDefenseAttr data
 * @Notes Built using information from ttyd-utils by Jdaster64: https://github.com/jdaster64/ttyd-utils/blob/master/docs/ttyd_structures_pseudocode.txt
 */
class BattleUnitDefenseAttr
{
    public int normal = 0;
    public int fire = 0;
    public int ice = 0;
    public int explosion = 0;
    public int electric = 0;

    public final int normal_offset = 0;
    public final int fire_offset = 1;
    public final int ice_offset = 2;
    public final int explosion_offset = 3;
    public final int electric_offset =  4;
}

/**
 * @Author Jemaroo
 * @Function Storage object for handling StatusVulnerability data
 * @Notes Built using information from ttyd-utils by Jdaster64: https://github.com/jdaster64/ttyd-utils/blob/master/docs/ttyd_structures_pseudocode.txt
 */
class StatusVulnerability
{
    public int sleep = 0;
    public int stop = 0;
    public int dizzy = 0;
    public int poison = 0;
    public int confuse = 0;
    public int electric = 0;
    public int burn = 0;
    public int freeze = 0;
    public int huge = 0;
    public int tiny = 0;
    public int attack_up = 0;
    public int attack_down = 0;
    public int defense_up = 0;
    public int defense_down = 0;
    public int allergic = 0;
    public int fright = 0;
    public int gale_force = 0;
    public int fast = 0;
    public int slow = 0;
    public int dodgy = 0;
    public int invisible = 0;
    public int ohko = 0;

    public final int sleep_offset = 0;
    public final int stop_offset = 1;
    public final int dizzy_offset = 2;
    public final int poison_offset = 3;
    public final int confuse_offset = 4;
    public final int electric_offset = 5;
    public final int burn_offset = 6;
    public final int freeze_offset = 7;
    public final int huge_offset = 8;
    public final int tiny_offset = 9;
    public final int attack_up_offset = 10;
    public final int attack_down_offset = 11;
    public final int defense_up_offset = 12;
    public final int defense_down_offset = 13;
    public final int allergic_offset = 14;
    public final int fright_offset = 15;
    public final int gale_force_offset = 16;
    public final int fast_offset = 17;
    public final int slow_offset = 18;
    public final int dodgy_offset = 19;
    public final int invisible_offset = 20;
    public final int ohko_offset = 21;
}

/**
 * @Author Jemaroo
 * @Function Storage object for handling BattleWeapon data
 * @Notes Built using information from ttyd-utils by Jdaster64: https://github.com/jdaster64/ttyd-utils/blob/master/docs/ttyd_structures_pseudocode.txt
 */
class BattleWeapon
{
    public String attackName = "";

    public int accuracy = 0;
    public int fp_cost = 0;
    public int sp_cost = 0;
    public int superguard_state = 0;
    public int sylish_multiplier = 0;
    public int bingo_slot_inc_chance = 0;
    public long base_damage_fn = 0;
    public int base_damage1 = 0;
    public int base_damage2 = 0;
    public int base_damage3 = 0;
    public int base_damage4 = 0;
    public int base_damage5 = 0;
    public int base_damage6 = 0;
    public int base_damage7 = 0;
    public int base_damage8 = 0;
    public int base_fpdamage1 = 0;
    public int base_fpdamage2 = 0;
    public int base_fpdamage3 = 0;
    public int base_fpdamage4 = 0;
    public int base_fpdamage5 = 0;
    public int base_fpdamage6 = 0;
    public int base_fpdamage7 = 0;
    public int base_fpdamage8 = 0;

    //TargetClassFlags
    public boolean CannotTargetMarioOrShellShield = false;
    public boolean CannotTargetPartner = false;
    public boolean CannotTargetEnemy = false;
    public boolean CannotTargetTreeOrSwitch = false;
    public boolean CannotTargetSystem = false;
    public boolean CannotTargetOppositeAlliance = false;
    public boolean CannotTargetOwnAlliance = false;
    public boolean CannotTargetSelf = false;
    public boolean CannotTargetSameSpecies = false;
    public boolean OnlyTargetSelf = false;
    public boolean OnlyTargetMario = false;
    public boolean OnlyTargetTreeOrSwitch = false;
    public boolean OnlyTargetPreferredParts = false;
    public boolean OnlyTargetSelectParts = false;
    public boolean SingleTarget = false;
    public boolean MultipleTarget = false;
    public boolean CannotTargetAnything = false;

    //TargetPropertyFlags
    public boolean Tattlelike = false;
    public boolean CannotTargetFloating2 = false;
    public boolean CannotTargetCeiling = false;
    public boolean CannotTargetFloating = false;
    public boolean CannotTargetGrounded = false;
    public boolean Jumplike = false;
    public boolean Hammerlike = false;
    public boolean ShellTosslike = false;
    public boolean CannotTargetGroundedVariant = false;
    public boolean RecoilDamage = false;
    public boolean CanOnlyTargetFrontmost = false;
    public boolean CannotTargetShellShield = false;
    public boolean CannotTargetCustom = false;
    public boolean TargetSameAllianceDirection = false;
    public boolean TargetOppositeAllianceDirection = false;

    public int element = 0;
    public int damage_pattern = 0;
    public int ac_level = 0;

    //SpecialPropertyFlags
    public boolean BadgeCanAffectPower = false;
    public boolean StatusCanAffectPower = false;
    public boolean IsChargeable = false;
    public boolean CannotMiss = false;
    public boolean DiminishingReturnsByHit = false;
    public boolean DiminishingReturnsByTarget = false;
    public boolean PiercesDefense = false;
    public boolean CanBreakIce = false;
    public boolean IgnoreTargetStatusVulnerability = false;
    public boolean SPFx200 = false;
    public boolean IgnitesIfBurned = false;
    public boolean PlayActiveFXSound = false;
    public boolean FlipsShellEnemies = false;
    public boolean FlipsBombFlippableEnemies = false;
    public boolean GroundsWingedEnemies = false;
    public boolean SPFx8000 = false;
    public boolean CanBeUsedAsConfusedAction = false;
    public boolean Unguardable = false;
    public boolean CanHitClonelike = false;

    //CounterResistanceFlags
    public boolean Electric = false;
    public boolean TopSpiky = false;
    public boolean PreemptiveFrontSpiky = false;
    public boolean FrontSpiky = false;
    public boolean Fiery = false;
    public boolean Icy = false;
    public boolean Poison = false;
    public boolean Explosive = false;
    public boolean VolatileExplosive = false;
    public boolean Payback = false;
    public boolean HoldFast = false;

    //TargetWeightingFlags
    public boolean PreferMario = false;
    public boolean PreferPartner = false;
    public boolean PreferFront = false;
    public boolean PreferBack = false;
    public boolean PreferSameAlliance = false;
    public boolean PreferOppositeAlliance = false;
    public boolean PreferLessHealthy = false;
    public boolean GreatlyPreferLessHealthy = false;
    public boolean PreferLowerHP = false;
    public boolean PreferHigherHP = false;
    public boolean PreferInPeril = false;
    public boolean TWFx2000 = false;
    public boolean ChooseWeightedRandomly = false;

    public int sleep_chance = 0;
    public int sleep_time = 0;
    public int stop_chance = 0;
    public int stop_time = 0;
    public int dizzy_chance = 0;
    public int dizzy_time = 0;
    public int poison_chance = 0;
    public int poison_time = 0;
    public int poison_strength = 0;
    public int confuse_chance = 0;
    public int confuse_time = 0;
    public int electric_chance = 0;
    public int electric_time = 0;
    public int dodgy_chance = 0;
    public int dodgy_time = 0;
    public int burn_chance = 0;
    public int burn_time = 0;
    public int freeze_chance = 0;
    public int freeze_time = 0;
    public int size_change_chance = 0;
    public int size_change_time = 0;
    public int size_change_strength = 0;
    public int atk_change_chance = 0;
    public int atk_change_time = 0;
    public int atk_change_strength = 0;
    public int def_change_chance = 0;
    public int def_change_time = 0;
    public int def_change_strength = 0;
    public int allergic_chance = 0;
    public int allergic_time = 0;
    public int ohko_chance = 0;
    public int charge_strength = 0;
    public int fast_chance = 0;
    public int fast_time = 0;
    public int slow_chance = 0;
    public int slow_time = 0;
    public int fright_chance = 0;
    public int gale_force_chance = 0;
    public int payback_time = 0;
    public int hold_fast_time = 0;
    public int invisible_chance = 0;
    public int invisible_time = 0;
    public int hp_regen_time = 0;
    public int hp_regen_strength = 0;
    public int fp_regen_time = 0;
    public int fp_regen_strength = 0;
    public long attack_evt = 0;
    public int stage_background_fallweight1 = 0;
    public int stage_background_fallweight2 = 0;
    public int stage_background_fallweight3 = 0;
    public int stage_background_fallweight4 = 0;
    public int stage_nozzle_turn_chance = 0;
    public int stage_nozzle_fire_chance = 0;
    public int stage_ceiling_fall_chance = 0;
    public int stage_object_fall_chance = 0;

    public final int accuracy_offset = 16;
    public final int fp_cost_offset = 17;
    public final int sp_cost_offset = 18;
    public final int superguard_state_offset = 19;
    public final int sylish_multiplier_offset = 24;
    public final int bingo_slot_inc_chance_offset = 26;
    public final int base_damage_fn_offset = 28;
    public final int base_damage1_offset = 32;
    public final int base_damage2_offset = 36;
    public final int base_damage3_offset = 40;
    public final int base_damage4_offset = 44;
    public final int base_damage5_offset = 48;
    public final int base_damage6_offset = 52;
    public final int base_damage7_offset = 56;
    public final int base_damage8_offset = 60;
    public final int base_fpdamage1_offset = 68;
    public final int base_fpdamage2_offset = 72;
    public final int base_fpdamage3_offset = 76;
    public final int base_fpdamage4_offset = 80;
    public final int base_fpdamage5_offset = 84;
    public final int base_fpdamage6_offset = 88;
    public final int base_fpdamage7_offset = 92;
    public final int base_fpdamage8_offset = 96;
    public final int TargetClassFlags_offset = 100;
    public final int TargetPropertyFlags_offset = 104;
    public final int element_offset = 108;
    public final int damage_pattern_offset = 109;
    public final int ac_level_offset = 110;
    public final int SpecialPropertyFlags_offset = 116;
    public final int CounterResistanceFlags_offset = 120;
    public final int TargetWeightingFlags_offset = 124;
    public final int sleep_chance_offset = 128;
    public final int sleep_time_offset = 129;
    public final int stop_chance_offset = 130;
    public final int stop_time_offset = 131;
    public final int dizzy_chance_offset = 132;
    public final int dizzy_time_offset = 133;
    public final int poison_chance_offset = 134;
    public final int poison_time_offset = 135;
    public final int poison_strength_offset = 136;
    public final int confuse_chance_offset = 137;
    public final int confuse_time_offset = 138;
    public final int electric_chance_offset = 139;
    public final int electric_time_offset = 140;
    public final int dodgy_chance_offset = 141;
    public final int dodgy_time_offset = 142;
    public final int burn_chance_offset = 143;
    public final int burn_time_offset = 144;
    public final int freeze_chance_offset = 145;
    public final int freeze_time_offset = 146;
    public final int size_change_chance_offset = 147;
    public final int size_change_time_offset = 148;
    public final int size_change_strength_offset = 149;
    public final int atk_change_chance_offset = 150;
    public final int atk_change_time_offset = 151;
    public final int atk_change_strength_offset = 152;
    public final int def_change_chance_offset = 153;
    public final int def_change_time_offset = 154;
    public final int def_change_strength_offset = 155;
    public final int allergic_chance_offset = 156;
    public final int allergic_time_offset = 157;
    public final int ohko_chance_offset = 158;
    public final int charge_strength_offset = 159;
    public final int fast_chance_offset = 160;
    public final int fast_time_offset = 161;
    public final int slow_chance_offset = 162;
    public final int slow_time_offset = 163;
    public final int fright_chance_offset = 164;
    public final int gale_force_chance_offset = 165;
    public final int payback_time_offset = 166;
    public final int hold_fast_time_offset = 167;
    public final int invisible_chance_offset = 168;
    public final int invisible_time_offset = 169;
    public final int hp_regen_time_offset = 170;
    public final int hp_regen_strength_offset = 171;
    public final int fp_regen_time_offset = 172;
    public final int fp_regen_strength_offset = 173;
    public final int attack_evt_offset = 176;
    public final int stage_background_fallweight1_offset = 180;
    public final int stage_background_fallweight2_offset = 181;
    public final int stage_background_fallweight3_offset = 182;
    public final int stage_background_fallweight4_offset = 184;
    public final int stage_nozzle_turn_chance_offset = 185;
    public final int stage_nozzle_fire_chance_offset = 186;
    public final int stage_ceiling_fall_chance_offset = 187;
    public final int stage_object_fall_chance_offset = 188;

    public enum TargetClassFlags 
    {
        CannotTargetMarioOrShellShield(0x1),
        CannotTargetPartner(0x2),
        CannotTargetEnemy(0x10),
        CannotTargetTreeOrSwitch(0x20),
        CannotTargetSystem(0x40),
        CannotTargetOppositeAlliance(0x100),
        CannotTargetOwnAlliance(0x200),
        CannotTargetSelf(0x1000),
        CannotTargetSameSpecies(0x2000),
        OnlyTargetSelf(0x4000),
        OnlyTargetMario(0x10000),
        OnlyTargetTreeOrSwitch(0x20000),
        OnlyTargetPreferredParts(0x100000),
        OnlyTargetSelectParts(0x200000),
        SingleTarget(0x1000000),
        MultipleTarget(0x2000000),
        CannotTargetAnything(0x80000000);

        public final int mask;
        
        TargetClassFlags(int mask) 
        {
            this.mask = mask;
        }
    }

    public enum TargetPropertyFlags 
    {
        Tattlelike(0x1),
        CannotTargetFloating2(0x2),
        CannotTargetCeiling(0x4),
        CannotTargetFloating(0x8),
        CannotTargetGrounded(0x10),
        Jumplike(0x1000),
        Hammerlike(0x2000),
        ShellTosslike(0x4000),
        CannotTargetGroundedVariant(0x8000),
        RecoilDamage(0x100000),
        CanOnlyTargetFrontmost(0x1000000),
        CannotTargetShellShield(0x2000000),
        CannotTargetCustom(0x4000000),
        TargetSameAllianceDirection(0x10000000),
        TargetOppositeAllianceDirection(0x20000000);

        public final int mask;
        
        TargetPropertyFlags(int mask) 
        {
            this.mask = mask;
        }
    }

    public enum SpecialPropertyFlags 
    {
        BadgeCanAffectPower(0x1),
        StatusCanAffectPower(0x2),
        IsChargeable(0x4),
        CannotMiss(0x8),
        DiminishingReturnsByHit(0x10),
        DiminishingReturnsByTarget(0x20),
        PiercesDefense(0x40),
        CanBreakIce(0x80),
        IgnoreTargetStatusVulnerability(0x100),
        SPFx200(0x200),
        IgnitesIfBurned(0x400),
        PlayActiveFXSound(0x800),
        FlipsShellEnemies(0x1000),
        FlipsBombFlippableEnemies(0x2000),
        GroundsWingedEnemies(0x4000),
        SPFx8000(0x8000),
        CanBeUsedAsConfusedAction(0x10000),
        Unguardable(0x20000),
        CanHitClonelike(0x40000);

        public final int mask;
        
        SpecialPropertyFlags(int mask) 
        {
            this.mask = mask;
        }
    }

    public enum CounterResistanceFlags 
    {
        Electric(0x1),
        TopSpiky(0x2),
        PreemptiveFrontSpiky(0x4),
        FrontSpiky(0x8),
        Fiery(0x10),
        Icy(0x20),
        Poison(0x40),
        Explosive(0x80),
        VolatileExplosive(0x100),
        Payback(0x200),
        HoldFast(0x400);

        public final int mask;
        
        CounterResistanceFlags(int mask) 
        {
            this.mask = mask;
        }
    }

    public enum TargetWeightingFlags 
    {
        PreferMario(0x1),
        PreferPartner(0x2),
        PreferFront(0x4),
        PreferBack(0x8),
        PreferSameAlliance(0x10),
        PreferOppositeAlliance(0x20),
        PreferLessHealthy(0x100),
        GreatlyPreferLessHealthy(0x200),
        PreferLowerHP(0x400),
        PreferHigherHP(0x800),
        PreferInPeril(0x1000),
        TWFx2000(0x2000),
        ChooseWeightedRandomly(0x80000000);

        public final int mask;
        
        TargetWeightingFlags(int mask) 
        {
            this.mask = mask;
        }
    }
}

/**
 * @Author Jemaroo
 * @Function Storage object for handling the names of Tattle Entries
 */
class TattleNames
{
    public String btl = "";
    public String menu = "";
    public boolean menuFlag = false;
    public String clone = "";
    public boolean cloneFlag = false;

    public TattleNames(String Nbtl)
    {
        this.btl = Nbtl;
    }

    public TattleNames(String Nbtl, String Nmenu)
    {
        this.btl = Nbtl;
        this.menu = Nmenu;
        this.menuFlag = true;
    }

    public TattleNames(String Nbtl, String Nmenu, String Nclone)
    {
        this.btl = Nbtl;
        this.menu = Nmenu;
        this.clone = Nclone;
        this.cloneFlag = true;
    }
}
