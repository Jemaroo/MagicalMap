import java.util.ArrayList;

public class test 
{
    public static void testUnitData(ArrayList<BUTData> units)
    {
        //testUnits(units);
        //checkForThis(units);
    }

    public static void testUnitData(IDTData fileData)
    {
        //checkForThis(fileData);
        //testItemData(fileData.items);
        //testShopData(fileData.shops);
        //testDropData(fileData.drops);
    }

    public static void testMiscData(ArrayList<MMData> fileData)
    {
        //testInput(fileData);
    }

    private static void testUnits(ArrayList<BUTData> units)
    {
        String retString = "";
        for(int i = 0; i < units.size(); i++)
        {
            retString += "Name: " + units.get(i).name + "\n"
            + "     BattleUnitKind Structs: " + units.get(i).BattleUnitKindData.size() + "\n"
            + "     BattleUnitDefense Structs: " + units.get(i).BattleUnitDefenseData.size() + "\n"
            + "     BattleUnitDefenseAttr Structs: " + units.get(i).BattleUnitDefenseAttrData.size() + "\n"
            + "     StatusVulnerability Structs: " + units.get(i).StatusVulnerabilityData.size() + "\n"
            + "     BattleWeapon Structs: " + units.get(i).BattleWeaponData.size() + "\n"
            + "     BattleUnitKindPart Structs: " + units.get(i).BattleUnitKindPartData.size() + "\n"
            + "     HealthUpgrades: " + units.get(i).HealthUpgradesData.size() + "\n";
        }
        System.out.println("Units: \n" + retString);
    }

    private static void checkForThis(ArrayList<BUTData> units)
    {
        for(int i = 0; i < units.size(); i++)
        {
            for(int j = 0; j < units.get(i).BattleUnitKindData.size(); j++)
            {
                if(units.get(i).BattleUnitKindData.get(j).LimitSwitch)
                {
                    System.out.println(units.get(i).name);
                }
            }
        }
    }

    private static void checkForThis(IDTData items)
    {
        for(int i = 0; i < items.items.size(); i++)
        {
            if(!items.items.get(i).UseLocationShop)
            {
                System.out.println(items.items.get(i).name);
            }
        }
    }

    private static void testItemData(ArrayList<ItemData> items)
    {
        if(items.size() > 0)
        {
            String retVal = "";
            for(int i = 0; i < items.size(); i++)
            {
                retVal += "Name: " + items.get(i).name + "\n"
                + "UseLocationShop: " + items.get(i).UseLocationShop + " | "
                + "UseLocationBattle: " + items.get(i).UseLocationBattle + " | "
                + "UseLocationField: " + items.get(i).UseLocationField + " | "
                + "sortOrder: " + items.get(i).sortOrder + " | "
                + "buyPrice: " + items.get(i).buyPrice + " | "
                + "discountPrice: " + items.get(i).discountPrice + " | "
                + "starPiecePrice: " + items.get(i).starPiecePrice + " | "
                + "sellPrice: " + items.get(i).sellPrice + " | "
                + "BPCost: " + items.get(i).BPCost + " | "
                + "HPRestored: " + items.get(i).HPRestored + " | "
                + "FPRestored: " + items.get(i).FPRestored + " | "
                + "SPRestored: " + items.get(i).SPRestored + "\n";

                for(int j = 0; j < items.get(i).properties.size(); j++)
                {
                    retVal += items.get(i).name + " " + items.get(i).properties.get(j).propertyName + ": " + items.get(i).properties.get(j).propertyValue + "\n";
                }

                retVal += "\n";
            }
            System.out.println(retVal);
        }
    }

    private static void testShopData(ArrayList<ShopData> shops)
    {
        String retVal = "";
        for(int i = 0; i < shops.size(); i++)
        {
            retVal += "Name: " + shops.get(i).name + "\n"
            + "Size: " + shops.get(i).size + "\nItem ID's: ";

            for(int j = 0; j < shops.get(i).ids.size(); j++)
            {
                if((j + 1) == shops.get(i).ids.size())
                {
                    retVal += shops.get(i).ids.get(j) + "\n";
                }
                else
                {
                    retVal += shops.get(i).ids.get(j) + ", ";
                }
            }

            if(shops.get(i).type != null)
            {
                retVal += "Type: " + shops.get(i).type + "\n";
            }

            if(shops.get(i).throwWeights.size() > 0)
            {
                retVal += "Throw Weights: ";
                for(int j = 0; j < shops.get(i).throwWeights.size(); j++)
                {
                    if((j + 1) == shops.get(i).throwWeights.size())
                    {
                        retVal += shops.get(i).throwWeights.get(j) + "\n";
                    }
                    else
                    {
                        retVal += shops.get(i).throwWeights.get(j) + ", ";
                    }
                }
            }

            if(shops.get(i).sellPrices.size() > 0)
            {
                retVal += "Sell Prices: ";
                for(int j = 0; j < shops.get(i).sellPrices.size(); j++)
                {
                    if((j + 1) == shops.get(i).sellPrices.size())
                    {
                        retVal += shops.get(i).sellPrices.get(j) + "\n";
                    }
                    else
                    {
                        retVal += shops.get(i).sellPrices.get(j) + ", ";
                    }
                }
            }

           retVal += "\n";
        }
        System.out.println(retVal);
    }

    private static void testDropData(ArrayList<DropData> drops)
    {
        if(drops.size() > 0)
        {
            String retVal = "";
            for(int i = 0; i < drops.size(); i++)
            {
                retVal += "Name: " + drops.get(i).name + "\n"
                + "Size: " + drops.get(i).size + "\nItem ID's: ";

                if(drops.get(i).ids.size() > 0)
                {
                    for(int j = 0; j < drops.get(i).ids.size(); j++)
                    {
                        if((j + 1) == drops.get(i).ids.size())
                        {
                            retVal += drops.get(i).ids.get(j) + "  |  ";
                        }
                        else
                        {
                            retVal += drops.get(i).ids.get(j) + ", ";
                        }
                    }
                }

                if(drops.get(i).holdWeights.size() > 0)
                {
                    retVal += "Hold Weights: ";
                    for(int j = 0; j < drops.get(i).holdWeights.size(); j++)
                    {
                        if((j + 1) == drops.get(i).holdWeights.size())
                        {
                            retVal += drops.get(i).holdWeights.get(j) + "  |  ";
                        }
                        else
                        {
                            retVal += drops.get(i).holdWeights.get(j) + ", ";
                        }
                    }
                }

                if(drops.get(i).dropWeights.size() > 0)
                {
                    retVal += "Hold Weights: ";
                    for(int j = 0; j < drops.get(i).dropWeights.size(); j++)
                    {
                        if((j + 1) == drops.get(i).dropWeights.size())
                        {
                            retVal += drops.get(i).dropWeights.get(j) + "  |  ";
                        }
                        else
                        {
                            retVal += drops.get(i).dropWeights.get(j) + ", ";
                        }
                    }
                }

                retVal += "\n\n";
            }

            System.out.println(retVal);
        }
    }

    private static void testInput(ArrayList<MMData> fileData)
    {
        for(MMData m : fileData)
        {
            System.out.println(m.type + " Size: " + m.miscData.size());
            for(Object o : m.miscData)
            {
                if(o instanceof Misc.oneint)
                {
                    Misc.oneint temp = (Misc.oneint)o;
                    System.out.println(temp.name + ": " + temp.value);
                }
                else if(o instanceof Misc.twoint)
                {
                    Misc.twoint temp = (Misc.twoint)o;
                    System.out.println(temp.name + ": " + temp.value);
                }
                else if(o instanceof Misc.twointConstraint)
                {
                    Misc.twointConstraint temp = (Misc.twointConstraint)o;
                    System.out.println(temp.name + ": " + temp.value);
                }
                else if(o instanceof Misc.twointWpatch)
                {
                    Misc.twointWpatch temp = (Misc.twointWpatch)o;
                    System.out.println(temp.name + ": " + temp.value + " " + temp.rewrite);
                }
                else if(o instanceof Misc.fourint)
                {
                    Misc.fourint temp = (Misc.fourint)o;
                    System.out.println(temp.name + ": " + temp.value);
                }
                else if(o instanceof Misc.fourintPN)
                {
                    Misc.fourintPN temp = (Misc.fourintPN)o;
                    System.out.println(temp.name + ": " + temp.value);
                }
                else if(o instanceof Misc.Float)
                {
                    Misc.Float temp = (Misc.Float)o;
                    System.out.println(temp.name + ": " + temp.value);
                }
                else if(o instanceof Misc.hexColor)
                {
                    Misc.hexColor temp = (Misc.hexColor)o;
                    System.out.println(temp.name + ": " + temp.colorValue);
                }
                else if(o instanceof Misc.odds)
                {
                    Misc.odds temp = (Misc.odds)o;
                    System.out.println(temp.name + ": " + temp.successRate + "/" + temp.outOf);
                }
                else if(o instanceof Misc.oddsRev)
                {
                    Misc.oddsRev temp = (Misc.oddsRev)o;
                    System.out.println(temp.name + ": " + temp.successRate + "/" + temp.outOf);
                }
                else if(o instanceof Misc.Function)
                {
                    Misc.Function temp = (Misc.Function)o;
                    System.out.println(temp.name + ": " + temp.rewrite);
                }
                else if(o instanceof Misc.bingoSelectionBox)
                {
                    Misc.bingoSelectionBox temp = (Misc.bingoSelectionBox)o;
                    System.out.println(temp.name + ": " + temp.value);
                }
            }
        }
    }
}