import java.util.concurrent.ThreadLocalRandom;

public class RandomizerUtils 
{
    // /**
    //  * @Author Jemaroo
    //  * @Function Randomizes some BattleUnitKind data
    //  */
    // public BattleUnitKind randomizeBattleUnitKind(BattleUnitKind struct)
    // {
    //     struct.HP = (int)(Math.round(ThreadLocalRandom.current().nextDouble(0.2, 3.01) * struct.HP));
    //     if(struct.HP <= 0) struct.HP = 1;

    //     struct.dangerHP = (int)(Math.round((double)struct.HP / 3));
    //     if(struct.dangerHP <= 0) struct.dangerHP = 1;

    //     struct.perilHP = (int)(Math.round((double)struct.HP / 10));
    //     if(struct.perilHP <= 0) struct.perilHP = 1;

    //     struct.level = (int)(Math.round(ThreadLocalRandom.current().nextDouble(0.5, 1.5) * struct.level));
    //     if(struct.level <= 0) struct.level = 1;

    //     struct.bonusXP = (int)(Math.round(ThreadLocalRandom.current().nextDouble(0, 1.01))); 
    //     if(struct.bonusXP < 0) struct.bonusXP = 0;

    //     struct.bonusCoin = (int)(Math.round(ThreadLocalRandom.current().nextDouble(0, 3.01))); 
    //     if(struct.bonusCoin < 0) struct.bonusCoin = 0;

    //     struct.baseCoin = (int)(Math.round(ThreadLocalRandom.current().nextDouble(0.2, 3.01) * struct.baseCoin));
    //     if(struct.baseCoin < 0) struct.baseCoin = 0;

    //     struct.runRate = (int)(Math.round(ThreadLocalRandom.current().nextDouble(0.2, 3.01) * struct.runRate));
    //     if(struct.runRate <= 0) struct.runRate = 1;

    //     struct.pbCap = (int)(Math.round(ThreadLocalRandom.current().nextDouble(1, 8.01))); 
    //     if(struct.pbCap <= 0) struct.pbCap = 1;

    //     return struct;
    // }

    // /**
    //  * @Author Jemaroo
    //  * @Function Randomizes some BattleUnitKindPart data
    //  */
    // public BattleUnitKindPart randomizeBattleUnitKindPart(BattleUnitKindPart struct)
    // {
    //     int oddsRoll = ThreadLocalRandom.current().nextInt(1, 11);
    //     if(oddsRoll == 10) struct.WeakToIcePower = ThreadLocalRandom.current().nextBoolean();

    //     //Jumplike cannot target/etc
    //     //Is immune to OHKO/etc

    //     oddsRoll = ThreadLocalRandom.current().nextInt(1, 6);
    //     if(oddsRoll == 5)
    //     {
    //         struct.TopSpiky = false;
    //         struct.FrontSpiky = false;
    //         struct.Fiery = false;
    //         struct.FieryStatus = false;
    //         struct.Icy = false;
    //         struct.IcyStatus = false;
    //         struct.Poison = false;
    //         struct.PoisonStatus = false;
    //         struct.Electric = false;
    //         struct.ElectricStatus = false;
    //         struct.Explosive = false;

    //         oddsRoll = ThreadLocalRandom.current().nextInt(1, 8);
    //         switch(oddsRoll)
    //         {
    //             case 1: {struct.TopSpiky = true; break;}
    //             case 2: {struct.FrontSpiky = true; break;}
    //             case 3: {struct.Fiery = true; struct.FieryStatus = true; break;}
    //             case 4: {struct.Icy = true; struct.IcyStatus = true; break;}
    //             case 5: {struct.Poison = true; struct.PoisonStatus = true; break;}
    //             case 6: {struct.Electric = true; struct.ElectricStatus = true; break;}
    //             case 7: {struct.Explosive = true; break;}
    //         }
    //     }

    //     return struct;
    // }

    // /**
    //  * @Author Jemaroo
    //  * @Function Randomizes some HealthUpgrades data
    //  */
    // public HealthUpgrades randomizeHealthUpgrades(HealthUpgrades struct, String unitName)
    // {
    //     if(unitName.equals("Goombella") || unitName.equals("Koops") || unitName.equals("Flurrie") || unitName.equals("Yoshi") || unitName.equals("Vivian") || unitName.equals("Bobbery") || unitName.equals("Ms. Mowz"))
    //     {
    //         struct.startHP = (int)(Math.round(ThreadLocalRandom.current().nextDouble(2, 15.01))); 
    //         if(struct.startHP <= 0) struct.startHP = 1;

    //         struct.startFP = (int)(Math.round(ThreadLocalRandom.current().nextDouble(1, 2.01) * struct.startHP)); 
    //         if(struct.startFP <= 0) struct.startFP = 1;

    //         struct.startBP = (int)(Math.round(ThreadLocalRandom.current().nextDouble(2, 3.01) * struct.startHP)); 
    //         if(struct.startBP <= 0) struct.startBP = 1;
    //     }
    //     else
    //     {
    //         struct.startHP = (int)(Math.round(ThreadLocalRandom.current().nextDouble(2, 10.01))); 
    //         if(struct.startHP <= 0) struct.startHP = 1;

    //         struct.startFP = (int)(Math.round(ThreadLocalRandom.current().nextDouble(1, 10.01))); 
    //         if(struct.startFP <= 0) struct.startFP = 1;

    //         struct.startBP = (int)(Math.round(ThreadLocalRandom.current().nextDouble(1, 5.01))); 
    //         if(struct.startBP <= 0) struct.startBP = 1;

    //         struct.upgradeHP = (int)(Math.round(ThreadLocalRandom.current().nextDouble(1, 10.01))); 
    //         if(struct.upgradeHP <= 0) struct.upgradeHP = 1;

    //         struct.upgradeFP = (int)(Math.round(ThreadLocalRandom.current().nextDouble(1, 10.01))); 
    //         if(struct.upgradeFP <= 0) struct.upgradeFP = 1;

    //         struct.upgradeBP = (int)(Math.round(ThreadLocalRandom.current().nextDouble(1, 5.01))); 
    //         if(struct.upgradeBP <= 0) struct.upgradeBP = 1;
    //     }

    //     return struct;
    // }
}