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
public class IDTMain 
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
     * @Function Will attempt to read the given file and the json file and parse them into an array of ItemData
     */
    public static IDTData getTableData(File givenFile)
    {
        File jsonFile = new File("src\\ItemData.json");
        byte[] givenFiledata = ByteUtils.readData(givenFile);

        IDTData fileData = new IDTData();

        //Check for correct dol/rel in json
        try
        {
            JSONParser parser = new JSONParser();
            JSONObject root = (JSONObject)parser.parse(new FileReader(jsonFile));
            JSONArray fileArray = (JSONArray)root.get("File");
            JSONObject fileObj = null;
            int locator = 0;

            for(int i = 0; i < fileArray.size(); i ++)
            {
                fileObj = (JSONObject)fileArray.get(i);
                String name = (String)fileObj.get("Name");
                if(givenFile.getName().equalsIgnoreCase(name))
                {  
                    System.out.println(name + " File Structs Found in: " + jsonFile.getName());
                    break;
                }
            }

            if(givenFile.getName().equals("main.dol") || givenFile.getName().equals("Start.dol"))
            {
                JSONArray itemNameArray = (JSONArray)fileObj.get("Items");
                JSONArray itemOffsetArray = (JSONArray)fileObj.get("Offsets");
                
                //ItemData
                for(int i = 0; i < itemNameArray.size(); i++)
                {
                    ItemData item = new ItemData();

                    //Name
                    item.name = itemNameArray.get(i).toString();
                    
                    //UseLocationFlags
                    locator = item.UseLocationFlags_offset + Math.toIntExact((Long)itemOffsetArray.get(i));
                    item.setLocationFlagsValue(ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]));

                    //Sort Order
                    locator = item.sortOrder_offset + Math.toIntExact((Long)itemOffsetArray.get(i));
                    item.sortOrder = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]);

                    //Buy Price
                    locator = item.buyPrice_offset + Math.toIntExact((Long)itemOffsetArray.get(i));
                    item.buyPrice = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]);
                    
                    //Discount Price
                    locator = item.discountPrice_offset + Math.toIntExact((Long)itemOffsetArray.get(i));
                    item.discountPrice = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]);

                    //Star Piece Price
                    locator = item.starPiecePrice_offset + Math.toIntExact((Long)itemOffsetArray.get(i));
                    item.starPiecePrice = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]);

                    //Sell Price
                    locator = item.sellPrice_offset + Math.toIntExact((Long)itemOffsetArray.get(i));
                    item.sellPrice = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]);

                    //BP Cost
                    locator = item.BPCost_offset + Math.toIntExact((Long)itemOffsetArray.get(i));
                    item.BPCost = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //HP Restored
                    locator = item.HPRestored_offset + Math.toIntExact((Long)itemOffsetArray.get(i));
                    item.HPRestored = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //FP Restored
                    locator = item.FPRestored_offset + Math.toIntExact((Long)itemOffsetArray.get(i));
                    item.FPRestored = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //SP Restored
                    locator = item.SPRestored_offset + Math.toIntExact((Long)itemOffsetArray.get(i));
                    item.SPRestored = ByteUtils.bytesToInt(givenFiledata[locator]);

                    //Badge Properties Check
                    item = checkProperties(item, fileObj, givenFiledata);

                    //Adding to list
                    fileData.items.add(item);
                }

                //DropTables
                JSONArray dropTablesArray = (JSONArray)fileObj.get("DropTables");
                
                for(int i = 0; i < dropTablesArray.size(); i++)
                {
                    DropData drop = new DropData();
                    drop.name = (String)((JSONObject)(dropTablesArray.get(i))).get("Name");
                    drop.size = Math.toIntExact((Long)((JSONObject)(dropTablesArray.get(i))).get("Size"));

                    locator = Math.toIntExact((Long)((JSONObject)(dropTablesArray.get(i))).get("Offset"));
                    for(int j = 0; j < drop.size; j++)
                    {
                        int id = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                        drop.ids.add(id);
                        locator += 4;

                        int holdWeight = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]);
                        drop.holdWeights.add(holdWeight);
                        locator += 2;

                        int dropWeight = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]);
                        drop.dropWeights.add(dropWeight);
                        locator += 2;
                    }

                    //Adding to list
                    fileData.drops.add(drop);
                }

                //ShopTables
                JSONArray shopTablesArray = (JSONArray)fileObj.get("ShopTables");
                
                for(int i = 0; i < shopTablesArray.size(); i++)
                {
                    if (((String)((JSONObject)(shopTablesArray.get(i))).get("Type")).equals("Shop"))
                    {
                        ShopData shop = new ShopData();
                        shop.name = (String)((JSONObject)(shopTablesArray.get(i))).get("Name");
                        shop.size = Math.toIntExact((Long)((JSONObject)(shopTablesArray.get(i))).get("Size"));
                        shop.type = "Shop";

                        locator = Math.toIntExact((Long)((JSONObject)(shopTablesArray.get(i))).get("Offset"));
                        for(int j = 0; j < shop.size; j++)
                        {
                            int id = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                            shop.ids.add(id);
                            locator += 4;
                        }

                        //Adding to list
                        fileData.shops.add(shop);
                    }
                    else if(((String)((JSONObject)(shopTablesArray.get(i))).get("Type")).equals("Audience"))
                    {
                        ShopData shop = new ShopData();
                        shop.name = (String)((JSONObject)(shopTablesArray.get(i))).get("Name");
                        shop.size = Math.toIntExact((Long)((JSONObject)(shopTablesArray.get(i))).get("Size"));
                        shop.type = "Audience";

                        locator = Math.toIntExact((Long)((JSONObject)(shopTablesArray.get(i))).get("Offset"));
                        for(int j = 0; j < shop.size; j++)
                        {
                            int id = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                            shop.ids.add(id);
                            locator += 4;
                            int throwWeight = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                            shop.throwWeights.add(throwWeight);
                            locator += 4;
                        }

                        //Adding to list
                        fileData.shops.add(shop);
                    }
                    else if(((String)((JSONObject)(shopTablesArray.get(i))).get("Type")).equals("Point Rewards"))
                    {
                        ShopData shop = new ShopData();
                        shop.name = (String)((JSONObject)(shopTablesArray.get(i))).get("Name");
                        shop.size = Math.toIntExact((Long)((JSONObject)(shopTablesArray.get(i))).get("Size"));
                        shop.type = "Point Rewards";

                        locator = Math.toIntExact((Long)((JSONObject)(shopTablesArray.get(i))).get("Offset"));
                        for(int j = 0; j < shop.size; j++)
                        {
                            int pointRequirement = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                            shop.pointRequirements.add(pointRequirement);
                            locator += 4;
                            int id = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                            shop.ids.add(id);
                            locator += 4;
                        }

                        //Adding to list
                        fileData.shops.add(shop);
                    }
                }
            }
            else
            {
                //ShopTables
                JSONArray shopTablesArray = (JSONArray)fileObj.get("ShopTables");
                
                for(int i = 0; i < shopTablesArray.size(); i++)
                {
                    if (((String)((JSONObject)(shopTablesArray.get(i))).get("Type")).equals("Shop"))
                    {
                        ShopData shop = new ShopData();
                        shop.name = (String)((JSONObject)(shopTablesArray.get(i))).get("Name");
                        shop.size = Math.toIntExact((Long)((JSONObject)(shopTablesArray.get(i))).get("Size"));
                        shop.type = "Shop";

                        locator = Math.toIntExact((Long)((JSONObject)(shopTablesArray.get(i))).get("Offset"));
                        for(int j = 0; j < shop.size; j++)
                        {
                            int id = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                            shop.ids.add(id);
                            int sellPrice = ByteUtils.bytesToInt(givenFiledata[locator + 4], givenFiledata[locator + 5], givenFiledata[locator + 6], givenFiledata[locator + 7]);
                            shop.sellPrices.add(sellPrice);
                            locator += 8;
                        }

                        //Adding to list
                        fileData.shops.add(shop);
                    }
                    else if (((String)((JSONObject)(shopTablesArray.get(i))).get("Type")).equals("Raw"))
                    {
                        ShopData shop = new ShopData();
                        shop.name = (String)((JSONObject)(shopTablesArray.get(i))).get("Name");
                        shop.size = Math.toIntExact((Long)((JSONObject)(shopTablesArray.get(i))).get("Size"));
                        shop.type = "Raw";

                        JSONArray offsetsArray = (JSONArray)((JSONObject)shopTablesArray.get(i)).get("Offsets");
                        
                        for(int j = 0; j < offsetsArray.size(); j++)
                        {
                            locator = Math.toIntExact((Long)(offsetsArray.get(j)));
                            int id = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]);
                            shop.ids.add(id);
                        }

                        //Adding to list
                        fileData.shops.add(shop);
                    }
                    else if (((String)((JSONObject)(shopTablesArray.get(i))).get("Type")).equals("Inn"))
                    {
                        ShopData shop = new ShopData();
                        shop.name = (String)((JSONObject)(shopTablesArray.get(i))).get("Name");
                        shop.size = Math.toIntExact((Long)((JSONObject)(shopTablesArray.get(i))).get("Size"));
                        shop.type = "Inn";

                        locator = Math.toIntExact((Long)((JSONObject)(shopTablesArray.get(i))).get("Offset"));
                        int id = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                        shop.ids.add(id);

                        locator += 4;
                        shop.xCoord = ByteUtils.bytesFloatToFloat(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        locator += 4;
                        shop.yCoord = ByteUtils.bytesFloatToFloat(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        locator += 4;
                        shop.zCoord = ByteUtils.bytesFloatToFloat(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        locator += 4;
                        shop.coinCost = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Adding to list
                        fileData.shops.add(shop);
                    }
                    else if (((String)((JSONObject)(shopTablesArray.get(i))).get("Type")).equals("Coins"))
                    {
                        ShopData shop = new ShopData();
                        shop.name = (String)((JSONObject)(shopTablesArray.get(i))).get("Name");
                        shop.size = Math.toIntExact((Long)((JSONObject)(shopTablesArray.get(i))).get("Size"));
                        shop.type = "Coins";

                        locator = Math.toIntExact((Long)((JSONObject)(shopTablesArray.get(i))).get("Offset"));
                        int id = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                        shop.ids.add(id);

                        JSONArray coinOffsetsArray = (JSONArray)((JSONObject)shopTablesArray.get(i)).get("CoinOffsets");
                        locator = Math.toIntExact((Long)(coinOffsetsArray.get(0)));
                        shop.coinCost = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        //Adding to list
                        fileData.shops.add(shop);
                    }
                    else
                    {
                        ShopData shop = new ShopData();
                        shop.name = (String)((JSONObject)(shopTablesArray.get(i))).get("Name");
                        shop.size = Math.toIntExact((Long)((JSONObject)(shopTablesArray.get(i))).get("Size"));
                        shop.type = "Rewards";

                        locator = Math.toIntExact((Long)((JSONObject)(shopTablesArray.get(i))).get("Offset"));
                        for(int j = 0; j < shop.size; j++)
                        {
                            int id = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                            shop.ids.add(id);
                            locator += 4;
                        }

                        //Adding to list
                        fileData.shops.add(shop);
                    }
                }

                //SellTables
                JSONArray sellTablesArray = (JSONArray)fileObj.get("SellTables");

                for(int i = 0; i < sellTablesArray.size(); i++)
                {
                    ShopData shop = new ShopData();
                    shop.name = (String)((JSONObject)(sellTablesArray.get(i))).get("Name");
                    shop.size = Math.toIntExact((Long)((JSONObject)(sellTablesArray.get(i))).get("Size"));
                    shop.type = "Sell";

                    locator = Math.toIntExact((Long)((JSONObject)(sellTablesArray.get(i))).get("Offset"));
                    for(int j = 0; j < shop.size; j++)
                    {
                        int id = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                        shop.ids.add(id);
                        int sellPrice = ByteUtils.bytesToInt(givenFiledata[locator + 4], givenFiledata[locator + 5]);
                        shop.sellPrices.add(sellPrice);
                        locator += 8;
                    }

                    //Adding to list
                    fileData.shops2.add(shop);
                }

                //Field Data
                JSONArray fieldDataArray = (JSONArray)fileObj.get("FieldObjects");

                for(int i = 0; i < fieldDataArray.size(); i++)
                {
                    JSONArray mapObjects = (JSONArray)(((JSONObject)fieldDataArray.get(i)).get("Objects"));

                    for(int j = 0; j < mapObjects.size(); j++)
                    {
                        String type = (String)((JSONObject)(mapObjects.get(j))).get("Type");

                        switch (type) 
                        {
                            case "evt_item_entry":
                            {
                                //System.out.println("evt_item_entry");

                                JSONArray items = (JSONArray)(((JSONObject)(mapObjects.get(j))).get("Items"));

                                for(int k = 0; k < items.size(); k++)
                                {
                                    FieldLocationData.evt_item_entry tempField = new FieldLocationData.evt_item_entry();
                                    tempField.map = (String)((JSONObject)(fieldDataArray.get(i))).get("Map");
                                    tempField.type = (String)((JSONObject)(items.get(k))).get("Type");

                                    locator = Math.toIntExact((Long)(((JSONArray)(((JSONObject)items.get(k)).get("Offsets"))).get(0)));
                                    
                                    locator += 12;
                                    tempField.itemID = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    locator += 4;
                                    tempField.xCoord = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    locator += 4;
                                    tempField.yCoord = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    locator += 4;
                                    tempField.zCoord = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    //System.out.println("Map: " + tempField.map + " ID: " + tempField.itemID + " X: " + tempField.xCoord + " Y: " + tempField.yCoord + " Z: " + tempField.zCoord);

                                    fileData.field.add(tempField);
                                }
                            
                                break;
                            }
                            case "evt_mobj_badgeblk":
                            {
                                //System.out.println("evt_mobj_badgeblk");

                                JSONArray offsets = (JSONArray)(((JSONObject)(mapObjects.get(j))).get("Offsets"));

                                for(int k = 0; k < Math.toIntExact((Long)((JSONObject)(mapObjects.get(j))).get("Size")); k++)
                                {
                                    FieldLocationData.evt_mobj_badgeblk tempField = new FieldLocationData.evt_mobj_badgeblk();
                                    tempField.map = (String)((JSONObject)(fieldDataArray.get(i))).get("Map");

                                    locator = Math.toIntExact((Long)offsets.get(k));
                                    
                                    locator += 12;
                                    tempField.xCoord = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    locator += 4;
                                    tempField.yCoord = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    locator += 4;
                                    tempField.zCoord = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    locator += 4;
                                    tempField.itemID = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    locator += 12;
                                    tempField.blockType = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    //System.out.println("Map: " + tempField.map + " ID: " + tempField.itemID + " Type: " + tempField.blockType + " X: " + tempField.xCoord + " Y: " + tempField.yCoord + " Z: " + tempField.zCoord);

                                    fileData.field.add(tempField);
                                }
                            
                                break;
                            }
                            case "evt_mobj_blk":
                            {
                                //System.out.println("evt_mobj_blk");

                                JSONArray offsets = (JSONArray)(((JSONObject)(mapObjects.get(j))).get("Offsets"));

                                for(int k = 0; k < Math.toIntExact((Long)((JSONObject)(mapObjects.get(j))).get("Size")); k++)
                                {
                                    FieldLocationData.evt_mobj_blk tempField = new FieldLocationData.evt_mobj_blk();
                                    tempField.map = (String)((JSONObject)(fieldDataArray.get(i))).get("Map");

                                    locator = Math.toIntExact((Long)offsets.get(k));
                                    
                                    locator += 12;
                                    tempField.xCoord = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    locator += 4;
                                    tempField.yCoord = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    locator += 4;
                                    tempField.zCoord = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    //System.out.println("Map: " + tempField.map + " X: " + tempField.xCoord + " Y: " + tempField.yCoord + " Z: " + tempField.zCoord);

                                    fileData.field.add(tempField);
                                }
                            
                                break;
                            }
                            case "evt_mobj_brick":
                            {
                                //System.out.println("evt_mobj_brick");

                                JSONArray offsets = (JSONArray)(((JSONObject)(mapObjects.get(j))).get("Offsets"));

                                for(int k = 0; k < Math.toIntExact((Long)((JSONObject)(mapObjects.get(j))).get("Size")); k++)
                                {
                                    FieldLocationData.evt_mobj_brick tempField = new FieldLocationData.evt_mobj_brick();
                                    tempField.map = (String)((JSONObject)(fieldDataArray.get(i))).get("Map");

                                    locator = Math.toIntExact((Long)offsets.get(k));
                                    
                                    locator += 12;
                                    tempField.xCoord = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    locator += 4;
                                    tempField.yCoord = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    locator += 4;
                                    tempField.zCoord = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    locator += 4;
                                    tempField.itemID = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    locator += 4;
                                    tempField.blockType = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    //System.out.println("Map: " + tempField.map + " ID: " + tempField.itemID + " Type: " + tempField.blockType + " X: " + tempField.xCoord + " Y: " + tempField.yCoord + " Z: " + tempField.zCoord);

                                    fileData.field.add(tempField);
                                }
                            
                                break;
                            }
                            case "evt_mobj_itembox":
                            {
                                //System.out.println("evt_mobj_itembox");

                                JSONArray offsets = (JSONArray)(((JSONObject)(mapObjects.get(j))).get("Offsets"));

                                for(int k = 0; k < Math.toIntExact((Long)((JSONObject)(mapObjects.get(j))).get("Size")); k++)
                                {
                                    FieldLocationData.evt_mobj_itembox tempField = new FieldLocationData.evt_mobj_itembox();
                                    tempField.map = (String)((JSONObject)(fieldDataArray.get(i))).get("Map");

                                    locator = Math.toIntExact((Long)offsets.get(k));
                                    
                                    locator += 12;
                                    tempField.xCoord = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    locator += 4;
                                    tempField.yCoord = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    locator += 4;
                                    tempField.zCoord = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    locator += 4;
                                    tempField.chestType = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    //System.out.println("Map: " + tempField.map + " Type: " + tempField.chestType + " X: " + tempField.xCoord + " Y: " + tempField.yCoord + " Z: " + tempField.zCoord);

                                    fileData.field.add(tempField);
                                }
                            
                                break;
                            }
                            case "evt_mobj_kururing_floor":
                            {
                                //System.out.println("evt_mobj_kururing_floor");

                                JSONArray offsets = (JSONArray)(((JSONObject)(mapObjects.get(j))).get("Offsets"));

                                for(int k = 0; k < Math.toIntExact((Long)((JSONObject)(mapObjects.get(j))).get("Size")); k++)
                                {
                                    FieldLocationData.evt_mobj_kururing_floor tempField = new FieldLocationData.evt_mobj_kururing_floor();
                                    tempField.map = (String)((JSONObject)(fieldDataArray.get(i))).get("Map");

                                    locator = Math.toIntExact((Long)offsets.get(k));
                                    
                                    locator += 12;
                                    tempField.xCoord = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    locator += 4;
                                    tempField.yCoord = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    locator += 4;
                                    tempField.zCoord = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    locator += 8;
                                    tempField.itemID = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    //System.out.println("Map: " + tempField.map + " ID: " + tempField.itemID + " X: " + tempField.xCoord + " Y: " + tempField.yCoord + " Z: " + tempField.zCoord);

                                    fileData.field.add(tempField);
                                }
                            
                                break;
                            }
                            case "evt_mobj_powerupblk":
                            {
                                //System.out.println("evt_mobj_powerupblk");

                                JSONArray offsets = (JSONArray)(((JSONObject)(mapObjects.get(j))).get("Offsets"));

                                for(int k = 0; k < Math.toIntExact((Long)((JSONObject)(mapObjects.get(j))).get("Size")); k++)
                                {
                                    FieldLocationData.evt_mobj_powerupblk tempField = new FieldLocationData.evt_mobj_powerupblk();
                                    tempField.map = (String)((JSONObject)(fieldDataArray.get(i))).get("Map");

                                    locator = Math.toIntExact((Long)offsets.get(k));
                                    
                                    locator += 12;
                                    tempField.xCoord = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    locator += 4;
                                    tempField.yCoord = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    locator += 4;
                                    tempField.zCoord = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    //System.out.println("Map: " + tempField.map + " X: " + tempField.xCoord + " Y: " + tempField.yCoord + " Z: " + tempField.zCoord);

                                    fileData.field.add(tempField);
                                }
                            
                                break;
                            }
                            case "evt_mobj_recovery_blk":
                            {
                                //System.out.println("evt_mobj_recovery_blk");

                                JSONArray offsets = (JSONArray)(((JSONObject)(mapObjects.get(j))).get("Offsets"));

                                for(int k = 0; k < Math.toIntExact((Long)((JSONObject)(mapObjects.get(j))).get("Size")); k++)
                                {
                                    FieldLocationData.evt_mobj_recovery_blk tempField = new FieldLocationData.evt_mobj_recovery_blk();
                                    tempField.map = (String)((JSONObject)(fieldDataArray.get(i))).get("Map");

                                    locator = Math.toIntExact((Long)offsets.get(k));

                                    locator += 12;
                                    tempField.coinCost = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);
                                    
                                    locator += 4;
                                    tempField.xCoord = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    locator += 4;
                                    tempField.yCoord = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    locator += 4;
                                    tempField.zCoord = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    //System.out.println("Map: " + tempField.map + " Cost: " + tempField.coinCost + " X: " + tempField.xCoord + " Y: " + tempField.yCoord + " Z: " + tempField.zCoord);

                                    fileData.field.add(tempField);
                                }
                            
                                break;
                            }
                            case "evt_mobj_save_blk":
                            {
                                //System.out.println("evt_mobj_save_blk");

                                JSONArray offsets = (JSONArray)(((JSONObject)(mapObjects.get(j))).get("Offsets"));

                                for(int k = 0; k < Math.toIntExact((Long)((JSONObject)(mapObjects.get(j))).get("Size")); k++)
                                {
                                    FieldLocationData.evt_mobj_save_blk tempField = new FieldLocationData.evt_mobj_save_blk();
                                    tempField.map = (String)((JSONObject)(fieldDataArray.get(i))).get("Map");

                                    locator = Math.toIntExact((Long)offsets.get(k));
                                    
                                    locator += 12;
                                    tempField.xCoord = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    locator += 4;
                                    tempField.yCoord = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    locator += 4;
                                    tempField.zCoord = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                                    //System.out.println("Map: " + tempField.map + " X: " + tempField.xCoord + " Y: " + tempField.yCoord + " Z: " + tempField.zCoord);

                                    fileData.field.add(tempField);
                                }
                            
                                break;
                            }
                        }
                    }
                }
            }

            return fileData;
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
     * @Function Will export the array of units back into the same format as the given file
     */
    public static byte[] buildNewFile(File givenFile, IDTData fileData)
    {
        File jsonFile = new File("src\\ItemData.json");
        byte[] givenFiledata = ByteUtils.readData(givenFile);
        int locator = 0;

        try
        {
            JSONParser parser = new JSONParser();
            JSONObject root = (JSONObject)parser.parse(new FileReader(jsonFile));
            JSONArray fileArray = (JSONArray)root.get("File");
            JSONObject fileObj = null;

            for(int i = 0; i < fileArray.size(); i ++)
            {
                fileObj = (JSONObject)fileArray.get(i);
                String name = (String)fileObj.get("Name");
                if(givenFile.getName().equalsIgnoreCase(name))
                {  
                    break;
                }
            }

            if(givenFile.getName().equals("main.dol") || givenFile.getName().equals("Start.dol"))
            {
                JSONArray itemOffsetArray = (JSONArray)fileObj.get("Offsets");
                for(int i = 0; i < fileData.items.size(); i++)
                {
                    //ItemData
                    //UseLocationFlags
                    locator = fileData.items.get(i).UseLocationFlags_offset + Math.toIntExact((Long)itemOffsetArray.get(i));
                    byte[] tempFlag = ByteUtils.intTo2Bytes(fileData.items.get(i).getUseLocationFlagsValue());
                    for(int k = 0; k < 2 ; k++)
                    {
                        givenFiledata[locator + k] = tempFlag[k];
                    }

                    //Sort Order
                    locator = fileData.items.get(i).sortOrder_offset + Math.toIntExact((Long)itemOffsetArray.get(i));
                    byte[] TempSortOrder = ByteUtils.intTo2Bytes(fileData.items.get(i).sortOrder);
                    for(int k = 0; k < 2 ; k++)
                    {
                        givenFiledata[locator + k] = TempSortOrder[k];
                    }
                    
                    //Buy Price
                    locator = fileData.items.get(i).buyPrice_offset + Math.toIntExact((Long)itemOffsetArray.get(i));
                    byte[] TempBuyPrice = ByteUtils.intTo2Bytes(fileData.items.get(i).buyPrice);
                    for(int k = 0; k < 2 ; k++)
                    {
                        givenFiledata[locator + k] = TempBuyPrice[k];
                    }

                    //Discount Price
                    locator = fileData.items.get(i).discountPrice_offset + Math.toIntExact((Long)itemOffsetArray.get(i));
                    byte[] TempDiscountPrice = ByteUtils.intTo2Bytes(fileData.items.get(i).discountPrice);
                    for(int k = 0; k < 2 ; k++)
                    {
                        givenFiledata[locator + k] = TempDiscountPrice[k];
                    }

                    //Star Piece Price
                    locator = fileData.items.get(i).starPiecePrice_offset + Math.toIntExact((Long)itemOffsetArray.get(i));
                    byte[] TempStarPiecePrice = ByteUtils.intTo2Bytes(fileData.items.get(i).starPiecePrice);
                    for(int k = 0; k < 2 ; k++)
                    {
                        givenFiledata[locator + k] = TempStarPiecePrice[k];
                    }

                    //Sell Price
                    locator = fileData.items.get(i).sellPrice_offset + Math.toIntExact((Long)itemOffsetArray.get(i));
                    byte[] TempSellPrice = ByteUtils.intTo2Bytes(fileData.items.get(i).sellPrice);
                    for(int k = 0; k < 2 ; k++)
                    {
                        givenFiledata[locator + k] = TempSellPrice[k];
                    }

                    //BP Cost
                    locator = fileData.items.get(i).BPCost_offset + Math.toIntExact((Long)itemOffsetArray.get(i));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(fileData.items.get(i).BPCost);

                    //HP Restored
                    locator = fileData.items.get(i).HPRestored_offset + Math.toIntExact((Long)itemOffsetArray.get(i));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(fileData.items.get(i).HPRestored);

                    //FP Restored
                    locator = fileData.items.get(i).FPRestored_offset + Math.toIntExact((Long)itemOffsetArray.get(i));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(fileData.items.get(i).FPRestored);

                    //SP Restored
                    locator = fileData.items.get(i).SPRestored_offset + Math.toIntExact((Long)itemOffsetArray.get(i));
                    givenFiledata[locator] = ByteUtils.intTo1Byte(fileData.items.get(i).SPRestored);

                    //Badge Properties
                    JSONArray propertiesArray = (JSONArray)fileObj.get("Properties");

                    for(Object obj : propertiesArray)
                    {
                        JSONObject prop = (JSONObject)obj;
                        if(fileData.items.get(i).name.equals(prop.get("Name")))
                        {
                            JSONArray offsetsArray = (JSONArray)prop.get("Offsets");

                            for(int j = 0; j < offsetsArray.size(); j++)
                            {
                                locator = Math.toIntExact((Long)offsetsArray.get(j));
                                byte[] TempProperty = ByteUtils.intTo2Bytes(fileData.items.get(i).properties.get(j).propertyValue);
                                for(int k = 0; k < 2 ; k++)
                                {
                                    givenFiledata[locator + k] = TempProperty[k];
                                }

                                if(fileData.items.get(i).name.equals("Power Rush"))
                                {
                                    givenFiledata[locator - 2] = 0x1C;
                                }
                            }

                            break;
                        }
                    }
                }

                //DropData
                JSONArray dropTablesArray = (JSONArray)fileObj.get("DropTables");
                for(int i = 0; i < dropTablesArray.size(); i++)
                {
                    int dropSize = Math.toIntExact((Long)((JSONObject)(dropTablesArray.get(i))).get("Size"));
                    locator = Math.toIntExact((Long)((JSONObject)(dropTablesArray.get(i))).get("Offset"));

                    for(int j = 0; j < dropSize; j++)
                    {
                        byte[] tempID = ByteUtils.intTo4Bytes(fileData.drops.get(i).ids.get(j));
                        for(int k = 0; k < 4 ; k++)
                        {
                            givenFiledata[locator + k] = tempID[k];
                        }
                        locator += 4;

                        byte[] tempHW = ByteUtils.intTo2Bytes(fileData.drops.get(i).holdWeights.get(j));
                        for(int k = 0; k < 2 ; k++)
                        {
                            givenFiledata[locator + k] = tempHW[k];
                        }
                        locator += 2;

                        byte[] tempDW = ByteUtils.intTo2Bytes(fileData.drops.get(i).dropWeights.get(j));
                        for(int k = 0; k < 2 ; k++)
                        {
                            givenFiledata[locator + k] = tempDW[k];
                        }
                        locator += 2;
                    }
                }

                //ShopData
                JSONArray shopTablesArray = (JSONArray)fileObj.get("ShopTables");
                for(int i = 0; i < shopTablesArray.size(); i++)
                {
                    if (((String)((JSONObject)(shopTablesArray.get(i))).get("Type")).equals("Shop"))
                    {
                        int shopSize = Math.toIntExact((Long)((JSONObject)(shopTablesArray.get(i))).get("Size"));
                        locator = Math.toIntExact((Long)((JSONObject)(shopTablesArray.get(i))).get("Offset"));

                        for(int j = 0; j < shopSize; j++)
                        {
                            byte[] tempID = ByteUtils.intTo4Bytes(fileData.shops.get(i).ids.get(j));
                            for(int k = 0; k < 4 ; k++)
                            {
                                givenFiledata[locator + k] = tempID[k];
                            }

                            locator += 4;
                        }
                    }
                    else if(((String)((JSONObject)(shopTablesArray.get(i))).get("Type")).equals("Audience"))
                    {
                        int shopSize = Math.toIntExact((Long)((JSONObject)(shopTablesArray.get(i))).get("Size"));
                        locator = Math.toIntExact((Long)((JSONObject)(shopTablesArray.get(i))).get("Offset"));

                        for(int j = 0; j < shopSize; j++)
                        {
                            byte[] tempID = ByteUtils.intTo4Bytes(fileData.shops.get(i).ids.get(j));
                            for(int k = 0; k < 4 ; k++)
                            {
                                givenFiledata[locator + k] = tempID[k];
                            }
                            locator += 4;

                            byte[] tempTW = ByteUtils.intTo4Bytes(fileData.shops.get(i).throwWeights.get(j));
                            for(int k = 0; k < 4 ; k++)
                            {
                                givenFiledata[locator + k] = tempTW[k];
                            }
                            locator += 4;
                        }
                    }
                    else if(((String)((JSONObject)(shopTablesArray.get(i))).get("Type")).equals("Point Rewards"))
                    {
                        int shopSize = Math.toIntExact((Long)((JSONObject)(shopTablesArray.get(i))).get("Size"));
                        locator = Math.toIntExact((Long)((JSONObject)(shopTablesArray.get(i))).get("Offset"));

                        for(int j = 0; j < shopSize; j++)
                        {
                            byte[] tempPR = ByteUtils.intTo4Bytes(fileData.shops.get(i).pointRequirements.get(j));
                            for(int k = 0; k < 4 ; k++)
                            {
                                givenFiledata[locator + k] = tempPR[k];
                            }
                            locator += 4;

                            byte[] tempID = ByteUtils.intTo4Bytes(fileData.shops.get(i).ids.get(j));
                            for(int k = 0; k < 4 ; k++)
                            {
                                givenFiledata[locator + k] = tempID[k];
                            }
                            locator += 4;
                        }
                    }
                }
            }
            else
            {
                //ShopData
                JSONArray shopTablesArray = (JSONArray)fileObj.get("ShopTables");
                for(int i = 0; i < shopTablesArray.size(); i++)
                {
                    if (((String)((JSONObject)(shopTablesArray.get(i))).get("Type")).equals("Shop"))
                    {
                        int shopSize = Math.toIntExact((Long)((JSONObject)(shopTablesArray.get(i))).get("Size"));
                        locator = Math.toIntExact((Long)((JSONObject)(shopTablesArray.get(i))).get("Offset"));

                        for(int j = 0; j < shopSize; j++)
                        {
                            byte[] tempID = ByteUtils.intTo4Bytes(fileData.shops.get(i).ids.get(j));
                            for(int k = 0; k < 4 ; k++)
                            {
                                givenFiledata[locator + k] = tempID[k];
                            }

                            locator += 4;

                            byte[] tempsellPrice = ByteUtils.intTo4Bytes(fileData.shops.get(i).sellPrices.get(j));
                            for(int k = 0; k < 4 ; k++)
                            {
                                givenFiledata[locator + k] = tempsellPrice[k];
                            }

                            locator += 4;
                        }
                    }
                    else if (((String)((JSONObject)(shopTablesArray.get(i))).get("Type")).equals("Raw"))
                    {
                        int shopSize = Math.toIntExact((Long)((JSONObject)(shopTablesArray.get(i))).get("Size"));
                        JSONArray offsetsArray = (JSONArray)((JSONObject)shopTablesArray.get(i)).get("Offsets");

                        for(int j = 0; j < shopSize; j++)
                        {
                            locator = Math.toIntExact((Long)(offsetsArray.get(j)));

                            byte[] tempID = ByteUtils.intTo2Bytes(fileData.shops.get(i).ids.get(j));
                            for(int k = 0; k < 2 ; k++)
                            {
                                givenFiledata[locator + k] = tempID[k];
                            }
                        }
                    }
                    else if (((String)((JSONObject)(shopTablesArray.get(i))).get("Type")).equals("Inn"))
                    {
                        locator = Math.toIntExact((Long)((JSONObject)(shopTablesArray.get(i))).get("Offset"));
                        byte[] tempID = ByteUtils.intTo4Bytes(fileData.shops.get(i).ids.get(0));
                        for(int j = 0; j < 4 ; j++)
                        {
                            givenFiledata[locator + j] = tempID[j];
                        }

                        locator += 4;
                        byte[] tempX = ByteUtils.floatToBytesFloat(fileData.shops.get(i).xCoord);
                        for(int j = 0; j < 4 ; j++)
                        {
                            givenFiledata[locator + j] = tempX[j];
                        }

                        locator += 4;
                        byte[] tempY = ByteUtils.floatToBytesFloat(fileData.shops.get(i).yCoord);
                        for(int j = 0; j < 4 ; j++)
                        {
                            givenFiledata[locator + j] = tempY[j];
                        }

                        locator += 4;
                        byte[] tempZ = ByteUtils.floatToBytesFloat(fileData.shops.get(i).zCoord);
                        for(int j = 0; j < 4 ; j++)
                        {
                            givenFiledata[locator + j] = tempZ[j];
                        }

                        locator += 4;
                        byte[] tempCC = ByteUtils.intTo4Bytes(fileData.shops.get(i).coinCost);
                        for(int j = 0; j < 4 ; j++)
                        {
                            givenFiledata[locator + j] = tempCC[j];
                        }
                    }
                    else if (((String)((JSONObject)(shopTablesArray.get(i))).get("Type")).equals("Coins"))
                    {
                        //TODO
                        locator = Math.toIntExact((Long)((JSONObject)(shopTablesArray.get(i))).get("Offset"));
                        byte[] tempID = ByteUtils.intTo4Bytes(fileData.shops.get(i).ids.get(0));
                        for(int j = 0; j < 4 ; j++)
                        {
                            givenFiledata[locator + j] = tempID[j];
                        }

                        JSONArray coinOffsetsArray = (JSONArray)((JSONObject)shopTablesArray.get(i)).get("CoinOffsets");
                        locator = Math.toIntExact((Long)(coinOffsetsArray.get(0)));
                        byte[] tempCO = ByteUtils.intTo4Bytes(fileData.shops.get(i).coinCost);
                        for(int j = 0; j < 4 ; j++)
                        {
                            givenFiledata[locator + j] = tempCO[j];
                        }

                        if(fileData.shops.get(i).name.equals("Souvenir Stand"))
                        {
                            locator = Math.toIntExact((Long)(coinOffsetsArray.get(1)));
                            tempCO = ByteUtils.intTo4Bytes((fileData.shops.get(i).coinCost * -1));
                            for(int j = 0; j < 4 ; j++)
                            {
                                givenFiledata[locator + j] = tempCO[j];
                            }
                        }
                        else
                        {
                            for(int j = 1; j < coinOffsetsArray.size(); j++)
                            {
                                locator = Math.toIntExact((Long)(coinOffsetsArray.get(j)));
                                tempCO = ByteUtils.intTo4Bytes(fileData.shops.get(i).coinCost);
                                for(int k = 0; k < 4 ; k++)
                                {
                                    givenFiledata[locator + k] = tempCO[k];
                                }
                            }
                        }
                    }
                    else
                    {
                        int shopSize = Math.toIntExact((Long)((JSONObject)(shopTablesArray.get(i))).get("Size"));
                        locator = Math.toIntExact((Long)((JSONObject)(shopTablesArray.get(i))).get("Offset"));

                        for(int j = 0; j < shopSize; j++)
                        {
                            byte[] tempID = ByteUtils.intTo4Bytes(fileData.shops.get(i).ids.get(j));
                            for(int k = 0; k < 4 ; k++)
                            {
                                givenFiledata[locator + k] = tempID[k];
                            }

                            locator += 4;
                        }
                    }
                }

                //ShopSellData
                JSONArray shopSellTablesArray = (JSONArray)fileObj.get("SellTables");
                for(int i = 0; i < shopSellTablesArray.size(); i++)
                {
                    int shopSize = Math.toIntExact((Long)((JSONObject)(shopSellTablesArray.get(i))).get("Size"));
                    locator = Math.toIntExact((Long)((JSONObject)(shopSellTablesArray.get(i))).get("Offset"));

                    for(int j = 0; j < shopSize; j++)
                    {
                        byte[] tempID = ByteUtils.intTo4Bytes(fileData.shops2.get(i).ids.get(j));
                        for(int k = 0; k < 4 ; k++)
                        {
                            givenFiledata[locator + k] = tempID[k];
                        }

                        locator += 4;

                        byte[] tempsellPrice = ByteUtils.intTo4Bytes(fileData.shops2.get(i).sellPrices.get(j));
                        for(int k = 0; k < 4 ; k++)
                        {
                            givenFiledata[locator + k] = tempsellPrice[k];
                        }

                        locator += 4;
                    }
                }

                //Field Data
                JSONArray fieldDataArray = (JSONArray)fileObj.get("FieldObjects");
                int fieldTracker = 0;

                for(int i = 0; i < fieldDataArray.size(); i++)
                {
                    JSONArray mapObjects = (JSONArray)(((JSONObject)fieldDataArray.get(i)).get("Objects"));

                    for(int j = 0; j < mapObjects.size(); j++)
                    {
                        String type = (String)((JSONObject)(mapObjects.get(j))).get("Type");

                        switch (type) 
                        {
                            case "evt_item_entry":
                            {
                                JSONArray items = (JSONArray)(((JSONObject)(mapObjects.get(j))).get("Items"));

                                for(int k = 0; k < items.size(); k++)
                                {
                                    JSONArray offsets = (JSONArray)(((JSONObject)items.get(k)).get("Offsets"));
                                    locator = Math.toIntExact((Long)((offsets).get(0)));
                                    
                                    locator += 12;
                                    byte[] tempID = ByteUtils.longTo4Bytes(((FieldLocationData.evt_item_entry)(fileData.field.get(fieldTracker))).itemID);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempID[l];
                                    }
                                    
                                    locator += 4;
                                    byte[] tempX = ByteUtils.longTo4Bytes(((FieldLocationData.evt_item_entry)(fileData.field.get(fieldTracker))).xCoord);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempX[l];
                                    }

                                    locator += 4;
                                    byte[] tempY = ByteUtils.longTo4Bytes(((FieldLocationData.evt_item_entry)(fileData.field.get(fieldTracker))).yCoord);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempY[l];
                                    }

                                    locator += 4;
                                    byte[] tempZ = ByteUtils.longTo4Bytes(((FieldLocationData.evt_item_entry)(fileData.field.get(fieldTracker))).zCoord);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempZ[l];
                                    }

                                    if(offsets.size() > 1)
                                    {
                                        for(int l = 1; l < offsets.size(); l++)
                                        {
                                            locator = Math.toIntExact((Long)((offsets).get(l)));
                                            locator += 12;
                                            tempID = ByteUtils.longTo4Bytes(((FieldLocationData.evt_item_entry)(fileData.field.get(fieldTracker))).itemID);
                                            for(int m = 0; m < 4 ; m++)
                                            {
                                                givenFiledata[locator + m] = tempID[m];
                                            }
                                        }
                                    }

                                    fieldTracker++;
                                }
                            
                                break;
                            }
                            case "evt_mobj_badgeblk":
                            {
                                JSONArray offsets = (JSONArray)(((JSONObject)(mapObjects.get(j))).get("Offsets"));

                                for(int k = 0; k < Math.toIntExact((Long)((JSONObject)(mapObjects.get(j))).get("Size")); k++)
                                {
                                    locator = Math.toIntExact((Long)offsets.get(k));
                                    
                                    locator += 12;
                                    byte[] tempX = ByteUtils.longTo4Bytes(((FieldLocationData.evt_mobj_badgeblk)(fileData.field.get(fieldTracker))).xCoord);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempX[l];
                                    }

                                    locator += 4;
                                    byte[] tempY = ByteUtils.longTo4Bytes(((FieldLocationData.evt_mobj_badgeblk)(fileData.field.get(fieldTracker))).yCoord);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempY[l];
                                    }

                                    locator += 4;
                                    byte[] tempZ = ByteUtils.longTo4Bytes(((FieldLocationData.evt_mobj_badgeblk)(fileData.field.get(fieldTracker))).zCoord);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempZ[l];
                                    }

                                    locator += 4;
                                    byte[] tempID = ByteUtils.longTo4Bytes(((FieldLocationData.evt_mobj_badgeblk)(fileData.field.get(fieldTracker))).itemID);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempID[l];
                                    }
                                    
                                    locator += 12;
                                    byte[] tempBT = ByteUtils.longTo4Bytes(((FieldLocationData.evt_mobj_badgeblk)(fileData.field.get(fieldTracker))).blockType);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempBT[l];
                                    }

                                    fieldTracker++;
                                }
                            
                                break;
                            }
                            case "evt_mobj_blk":
                            {
                                JSONArray offsets = (JSONArray)(((JSONObject)(mapObjects.get(j))).get("Offsets"));
                                
                                for(int k = 0; k < Math.toIntExact((Long)((JSONObject)(mapObjects.get(j))).get("Size")); k++)
                                {
                                    locator = Math.toIntExact((Long)offsets.get(k));
                                    
                                    locator += 12;
                                    byte[] tempX = ByteUtils.longTo4Bytes(((FieldLocationData.evt_mobj_blk)(fileData.field.get(fieldTracker))).xCoord);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempX[l];
                                    }

                                    locator += 4;
                                    byte[] tempY = ByteUtils.longTo4Bytes(((FieldLocationData.evt_mobj_blk)(fileData.field.get(fieldTracker))).yCoord);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempY[l];
                                    }

                                    locator += 4;
                                    byte[] tempZ = ByteUtils.longTo4Bytes(((FieldLocationData.evt_mobj_blk)(fileData.field.get(fieldTracker))).zCoord);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempZ[l];
                                    }

                                    fieldTracker++;
                                }
                            
                                break;
                            }
                            case "evt_mobj_brick":
                            {
                                System.out.println("evt_mobj_brick");

                                JSONArray offsets = (JSONArray)(((JSONObject)(mapObjects.get(j))).get("Offsets"));
                                
                                for(int k = 0; k < Math.toIntExact((Long)((JSONObject)(mapObjects.get(j))).get("Size")); k++)
                                {
                                    locator = Math.toIntExact((Long)offsets.get(k));

                                    locator += 12;
                                    byte[] tempX = ByteUtils.longTo4Bytes(((FieldLocationData.evt_mobj_brick)(fileData.field.get(fieldTracker))).xCoord);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempX[l];
                                    }

                                    locator += 4;
                                    byte[] tempY = ByteUtils.longTo4Bytes(((FieldLocationData.evt_mobj_brick)(fileData.field.get(fieldTracker))).yCoord);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempY[l];
                                    }

                                    locator += 4;
                                    byte[] tempZ = ByteUtils.longTo4Bytes(((FieldLocationData.evt_mobj_brick)(fileData.field.get(fieldTracker))).zCoord);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempZ[l];
                                    }

                                    locator += 4;
                                    byte[] tempID = ByteUtils.longTo4Bytes(((FieldLocationData.evt_mobj_brick)(fileData.field.get(fieldTracker))).itemID);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempID[l];
                                    }
                                    
                                    locator += 4;
                                    byte[] tempBT = ByteUtils.longTo4Bytes(((FieldLocationData.evt_mobj_brick)(fileData.field.get(fieldTracker))).blockType);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempBT[l];
                                    }
                                    
                                    fieldTracker++;
                                }
                            
                                break;
                            }
                            case "evt_mobj_itembox":
                            {
                                JSONArray offsets = (JSONArray)(((JSONObject)(mapObjects.get(j))).get("Offsets"));
                                
                                for(int k = 0; k < Math.toIntExact((Long)((JSONObject)(mapObjects.get(j))).get("Size")); k++)
                                {
                                    locator = Math.toIntExact((Long)offsets.get(k));
                                    
                                    locator += 12;
                                    byte[] tempX = ByteUtils.longTo4Bytes(((FieldLocationData.evt_mobj_itembox)(fileData.field.get(fieldTracker))).xCoord);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempX[l];
                                    }

                                    locator += 4;
                                    byte[] tempY = ByteUtils.longTo4Bytes(((FieldLocationData.evt_mobj_itembox)(fileData.field.get(fieldTracker))).yCoord);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempY[l];
                                    }

                                    locator += 4;
                                    byte[] tempZ = ByteUtils.longTo4Bytes(((FieldLocationData.evt_mobj_itembox)(fileData.field.get(fieldTracker))).zCoord);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempZ[l];
                                    }

                                    locator += 4;
                                    byte[] tempCT = ByteUtils.longTo4Bytes(((FieldLocationData.evt_mobj_itembox)(fileData.field.get(fieldTracker))).chestType);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempCT[l];
                                    }

                                    fieldTracker++;
                                }
                            
                                break;
                            }
                            case "evt_mobj_kururing_floor":
                            {
                                JSONArray offsets = (JSONArray)(((JSONObject)(mapObjects.get(j))).get("Offsets"));
                                
                                for(int k = 0; k < Math.toIntExact((Long)((JSONObject)(mapObjects.get(j))).get("Size")); k++)
                                {
                                    locator = Math.toIntExact((Long)offsets.get(k));
                                    
                                    locator += 12;
                                    byte[] tempX = ByteUtils.longTo4Bytes(((FieldLocationData.evt_mobj_kururing_floor)(fileData.field.get(fieldTracker))).xCoord);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempX[l];
                                    }

                                    locator += 4;
                                    byte[] tempY = ByteUtils.longTo4Bytes(((FieldLocationData.evt_mobj_kururing_floor)(fileData.field.get(fieldTracker))).yCoord);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempY[l];
                                    }

                                    locator += 4;
                                    byte[] tempZ = ByteUtils.longTo4Bytes(((FieldLocationData.evt_mobj_kururing_floor)(fileData.field.get(fieldTracker))).zCoord);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempZ[l];
                                    }

                                    locator += 8;
                                    byte[] tempID = ByteUtils.longTo4Bytes(((FieldLocationData.evt_mobj_kururing_floor)(fileData.field.get(fieldTracker))).itemID);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempID[l];
                                    }

                                    fieldTracker++;
                                }
                            
                                break;
                            }
                            case "evt_mobj_powerupblk":
                            {
                                JSONArray offsets = (JSONArray)(((JSONObject)(mapObjects.get(j))).get("Offsets"));
                                
                                for(int k = 0; k < Math.toIntExact((Long)((JSONObject)(mapObjects.get(j))).get("Size")); k++)
                                {
                                    locator = Math.toIntExact((Long)offsets.get(k));
                                    
                                    locator += 12;
                                    byte[] tempX = ByteUtils.longTo4Bytes(((FieldLocationData.evt_mobj_powerupblk)(fileData.field.get(fieldTracker))).xCoord);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempX[l];
                                    }

                                    locator += 4;
                                    byte[] tempY = ByteUtils.longTo4Bytes(((FieldLocationData.evt_mobj_powerupblk)(fileData.field.get(fieldTracker))).yCoord);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempY[l];
                                    }

                                    locator += 4;
                                    byte[] tempZ = ByteUtils.longTo4Bytes(((FieldLocationData.evt_mobj_powerupblk)(fileData.field.get(fieldTracker))).zCoord);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempZ[l];
                                    }
                                    
                                    fieldTracker++;
                                }
                            
                                break;
                            }
                            case "evt_mobj_recovery_blk":
                            {
                                JSONArray offsets = (JSONArray)(((JSONObject)(mapObjects.get(j))).get("Offsets"));
                                
                                for(int k = 0; k < Math.toIntExact((Long)((JSONObject)(mapObjects.get(j))).get("Size")); k++)
                                {
                                    locator = Math.toIntExact((Long)offsets.get(k));

                                    locator += 12;
                                    byte[] tempCC = ByteUtils.longTo4Bytes(((FieldLocationData.evt_mobj_recovery_blk)(fileData.field.get(fieldTracker))).coinCost);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempCC[l];
                                    }
                                    
                                    locator += 4;
                                    byte[] tempX = ByteUtils.longTo4Bytes(((FieldLocationData.evt_mobj_recovery_blk)(fileData.field.get(fieldTracker))).xCoord);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempX[l];
                                    }

                                    locator += 4;
                                    byte[] tempY = ByteUtils.longTo4Bytes(((FieldLocationData.evt_mobj_recovery_blk)(fileData.field.get(fieldTracker))).yCoord);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempY[l];
                                    }

                                    locator += 4;
                                    byte[] tempZ = ByteUtils.longTo4Bytes(((FieldLocationData.evt_mobj_recovery_blk)(fileData.field.get(fieldTracker))).zCoord);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempZ[l];
                                    }

                                    fieldTracker++;
                                }
                            
                                break;
                            }
                            case "evt_mobj_save_blk":
                            {
                                JSONArray offsets = (JSONArray)(((JSONObject)(mapObjects.get(j))).get("Offsets"));
                                
                                for(int k = 0; k < Math.toIntExact((Long)((JSONObject)(mapObjects.get(j))).get("Size")); k++)
                                {
                                    locator = Math.toIntExact((Long)offsets.get(k));
                                    
                                    locator += 12;
                                    byte[] tempX = ByteUtils.longTo4Bytes(((FieldLocationData.evt_mobj_save_blk)(fileData.field.get(fieldTracker))).xCoord);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempX[l];
                                    }

                                    locator += 4;
                                    byte[] tempY = ByteUtils.longTo4Bytes(((FieldLocationData.evt_mobj_save_blk)(fileData.field.get(fieldTracker))).yCoord);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempY[l];
                                    }

                                    locator += 4;
                                    byte[] tempZ = ByteUtils.longTo4Bytes(((FieldLocationData.evt_mobj_save_blk)(fileData.field.get(fieldTracker))).zCoord);
                                    for(int l = 0; l < 4 ; l++)
                                    {
                                        givenFiledata[locator + l] = tempZ[l];
                                    }

                                    fieldTracker++;
                                }
                            
                                break;
                            }
                        }
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

    /**
     * @Author Jemaroo
     * @Function Will search for any badge properties and add them to the item
     */
    public static ItemData checkProperties(ItemData item, JSONObject fileObj, byte[] givenFiledata)
    {
        JSONArray propertiesArray = (JSONArray)fileObj.get("Properties");

        for(Object obj : propertiesArray)
        {
            JSONObject prop = (JSONObject)obj;
            if(item.name.equals(prop.get("Name")))
            {
                JSONArray propertyNamesArray = (JSONArray)prop.get("Properties");
                JSONArray offsetsArray = (JSONArray)prop.get("Offsets");

                for(int i = 0; i < offsetsArray.size(); i++)
                {
                    ItemData.BadgeProperty tempProp = new ItemData.BadgeProperty();
                    tempProp.propertyName = propertyNamesArray.get(i).toString();

                    int locator = Math.toIntExact((Long)offsetsArray.get(i));
                    tempProp.propertyValue = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]);
                    
                    if(item.name.equals("Power Rush") && tempProp.propertyValue == 3580)
                    {
                        tempProp.propertyValue = 2;
                    }

                    item.properties.add(tempProp);
                }

                break;
            }
        }
        
        return item;
    }
}