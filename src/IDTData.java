import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * @Author Jemaroo
 * @Function Storage object for handling ItemData, ShopData, and DropData data
 */
public class IDTData 
{
    ArrayList<ItemData> items = new ArrayList<ItemData>();
    ArrayList<ShopData> shops = new ArrayList<ShopData>();
    ArrayList<ShopData> shops2 = new ArrayList<ShopData>();
    ArrayList<DropData> drops = new ArrayList<DropData>();
    ArrayList<FieldLocationData> field = new ArrayList<FieldLocationData>();
}

/**
 * @Author Jemaroo
 * @Function Storage object for handling ItemData data
 * @Notes Built using information from ttyd-utils by Jdaster64: https://github.com/jdaster64/ttyd-utils/blob/master/docs/ttyd_structures_pseudocode.txt
 */
class ItemData
{    
    public String name;
    public Image icon;
    
    public boolean UseLocationShop = false;
    public boolean UseLocationBattle = false;
    public boolean UseLocationField = false;
    public int sortOrder = 0;
    public int buyPrice = 0;
    public int discountPrice = 0;
    public int starPiecePrice = 0;
    public int sellPrice = 0;
    public int BPCost = 0;
    public int HPRestored = 0;
    public int FPRestored = 0;
    public int SPRestored = 0;

    public final int UseLocationFlags_offset = 16;
    public final int sortOrder_offset = 18;
    public final int buyPrice_offset = 20;
    public final int discountPrice_offset = 22;
    public final int starPiecePrice_offset = 24;
    public final int sellPrice_offset = 26;
    public final int BPCost_offset = 28;
    public final int HPRestored_offset = 29;
    public final int FPRestored_offset = 30;
    public final int SPRestored_offset = 31;

    public static class BadgeProperty {String propertyName; int propertyValue;}
    public ArrayList<BadgeProperty> properties = new ArrayList<BadgeProperty>();

    public void setLocationFlagsValue(int input)
    {
        if(input == 7){this.UseLocationShop = true; this.UseLocationBattle = true; this.UseLocationField = true;}
        else if(input == 6){this.UseLocationShop = false; this.UseLocationBattle = true; this.UseLocationField = true;}
        else if(input == 5){this.UseLocationShop = true; this.UseLocationBattle = false; this.UseLocationField = true;}
        else if(input == 4){this.UseLocationShop = false; this.UseLocationBattle = false; this.UseLocationField = true;}
        else if(input == 3){this.UseLocationShop = true; this.UseLocationBattle = true; this.UseLocationField = false;}
        else if(input == 2){this.UseLocationShop = false; this.UseLocationBattle = true; this.UseLocationField = false;}
        else if(input == 1){this.UseLocationShop = true; this.UseLocationBattle = false; this.UseLocationField = false;}
        else{this.UseLocationShop = false; this.UseLocationBattle = false; this.UseLocationField = false;}
    }

    public int getUseLocationFlagsValue()
    {
        int retVal = 0;
        if(this.UseLocationShop){retVal += 1;}
        if(this.UseLocationBattle){retVal += 2;}
        if(this.UseLocationField){retVal += 4;}
        return retVal;
    }
}

/**
 * @Author Jemaroo
 * @Function Storage object for handling ShopData data
 */
class ShopData
{
    public ImageView icon;
    public String name;
    public int size;
    public String type;
    public ArrayList<Integer> ids = new ArrayList<Integer>();
    public ArrayList<Integer> throwWeights = new ArrayList<Integer>(); //Only used for audience throws
    public ArrayList<Integer> sellPrices = new ArrayList<Integer>(); //Only used for stores
    public ArrayList<Integer> pointRequirements = new ArrayList<Integer>(); //Only used for point rewards
    public float xCoord = 0L; //Only used for inn breakfasts
    public float yCoord = 0L; //Only used for inn breakfasts
    public float zCoord = 0L; //Only used for inn breakfasts
    public int coinCost = 0; //Only used for inn breakfasts and Coins
}

/**
 * @Author Jemaroo
 * @Function Storage object for handling ItemDropData data
 * @Notes Built using information from ttyd-utils by Jdaster64: https://github.com/jdaster64/ttyd-utils/blob/master/docs/ttyd_structures_pseudocode.txt
 */
class DropData
{
    public ImageView icon;
    public String name;
    public int size;
    public ArrayList<Integer> ids = new ArrayList<Integer>();
    public ArrayList<Integer> holdWeights = new ArrayList<Integer>();
    public ArrayList<Integer> dropWeights = new ArrayList<Integer>();
}

/**
 * @Author Jemaroo
 * @Function Storage object for handling Field Location data
 * @Notes Built using event decompliation from ttyd-utils by Jdaster64 which uses TTYDASM by PistonMiner: https://github.com/jdaster64/ttyd-utils/blob/master/source/export_events.py
 */
class FieldLocationData
{
    String map;
    long xCoord = 0;
    long yCoord = 0;
    long zCoord = 0;

    //Field Item
    public static class evt_item_entry extends FieldLocationData {String type = ""; long itemID = 0;}

    //Badge Block
    public static class evt_mobj_badgeblk extends FieldLocationData {long itemID = 0; long blockType = 0;}

    //Block
    public static class evt_mobj_blk extends FieldLocationData {}

    //Brick Block
    public static class evt_mobj_brick extends FieldLocationData {long itemID = 0; long blockType = 0;}

    //Chest
    public static class evt_mobj_itembox extends FieldLocationData {long chestType = 0;}

    //Floor Panel
    public static class evt_mobj_kururing_floor extends FieldLocationData {long itemID = 0;}

    //Shine Block
    public static class evt_mobj_powerupblk extends FieldLocationData {}

    //Recovery Block
    public static class evt_mobj_recovery_blk extends FieldLocationData {long coinCost = 0;}

    //Save Block
    public static class evt_mobj_save_blk extends FieldLocationData {}
}