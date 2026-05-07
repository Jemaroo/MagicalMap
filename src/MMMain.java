import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.simple.parser.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * @Author Jemaroo
 * @Function Main Functions for reading and parsing input data
 */
public class MMMain 
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
     * @Function Will attempt to read the given file and the json file and parse them into an ArrayList
     */
    public static ArrayList<MMData> getMiscData(File givenFile)
    {
        File jsonFile = new File("src\\MiscData.json");
        byte[] givenFiledata = ByteUtils.readData(givenFile);
        ArrayList<MMData> miscDataSorted = new ArrayList<MMData>();

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
                    System.out.println(name + " File Data Found in: " + jsonFile.getName());
                    break;
                }
            }

            JSONArray dataArray = (JSONArray)fileObj.get("Data");

            for(int i = 0; i < dataArray.size(); i++)
            {
                String tempDataType = (String)((JSONObject)(dataArray.get(i))).get("DataType");

                switch(tempDataType)
                {
                    case "oneint":
                    {
                        JSONArray offsetsArray = (JSONArray)(((JSONObject)dataArray.get(i)).get("Offsets"));

                        Misc.oneint tempMisc = new Misc.oneint();
                        tempMisc.name = (String)((JSONObject)(dataArray.get(i))).get("Name");

                        locator = Math.toIntExact((Long)offsetsArray.get(0));
                        tempMisc.value = ByteUtils.bytesToInt(givenFiledata[locator]);

                        tempMisc.textField.setText(Integer.toString(tempMisc.value));

                        if(miscDataSorted.size() == 0)
                        {
                            miscDataSorted.add(new MMData());
                            miscDataSorted.get(0).type = (String)((JSONObject)(dataArray.get(i))).get("Type");
                            miscDataSorted.get(0).miscData.add(tempMisc);
                        }
                        else
                        {
                            boolean matchFound = false;
                            for(int j = 0; j < miscDataSorted.size(); j++)
                            {
                                if(miscDataSorted.get(j).type.equals((String)((JSONObject)(dataArray.get(i))).get("Type")))
                                {
                                    matchFound = true;
                                    miscDataSorted.get(j).miscData.add(tempMisc);
                                    break;
                                }
                            }

                            if(!matchFound)
                            {
                                MMData tempMmData = new MMData();
                                tempMmData.type = (String)((JSONObject)(dataArray.get(i))).get("Type");
                                tempMmData.miscData.add(tempMisc);

                                miscDataSorted.add(tempMmData);
                            }
                        }

                        break;
                    }
                    case "twoint":
                    {
                        JSONArray offsetsArray = (JSONArray)(((JSONObject)dataArray.get(i)).get("Offsets"));

                        Misc.twoint tempMisc = new Misc.twoint();
                        tempMisc.name = (String)((JSONObject)(dataArray.get(i))).get("Name");

                        locator = Math.toIntExact((Long)offsetsArray.get(0));
                        tempMisc.value = ByteUtils.bytesToSignedInt(givenFiledata[locator], givenFiledata[locator + 1]);

                        tempMisc.textField.setText(Integer.toString(tempMisc.value));

                        if(miscDataSorted.size() == 0)
                        {
                            miscDataSorted.add(new MMData());
                            miscDataSorted.get(0).type = (String)((JSONObject)(dataArray.get(i))).get("Type");
                            miscDataSorted.get(0).miscData.add(tempMisc);
                        }
                        else
                        {
                            boolean matchFound = false;
                            for(int j = 0; j < miscDataSorted.size(); j++)
                            {
                                if(miscDataSorted.get(j).type.equals((String)((JSONObject)(dataArray.get(i))).get("Type")))
                                {
                                    matchFound = true;
                                    miscDataSorted.get(j).miscData.add(tempMisc);
                                    break;
                                }
                            }

                            if(!matchFound)
                            {
                                MMData tempMmData = new MMData();
                                tempMmData.type = (String)((JSONObject)(dataArray.get(i))).get("Type");
                                tempMmData.miscData.add(tempMisc);

                                miscDataSorted.add(tempMmData);
                            }
                        }

                        break;
                    }
                    case "twointConstraint":
                    {
                        JSONArray offsetsArray = (JSONArray)(((JSONObject)dataArray.get(i)).get("Offsets"));

                        Misc.twointConstraint tempMisc = new Misc.twointConstraint();
                        tempMisc.name = (String)((JSONObject)(dataArray.get(i))).get("Name");
                        tempMisc.low = Math.toIntExact((long)((JSONObject)(dataArray.get(i))).get("Lower"));
                        tempMisc.high = Math.toIntExact((long)((JSONObject)(dataArray.get(i))).get("Upper"));

                        locator = Math.toIntExact((Long)offsetsArray.get(0));
                        tempMisc.value = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]);
                        if(tempMisc.value < tempMisc.low) tempMisc.value = tempMisc.low;
                        if(tempMisc.value > tempMisc.high) tempMisc.value = tempMisc.high;

                        tempMisc.textField.setText(Integer.toString(tempMisc.value));

                        if(miscDataSorted.size() == 0)
                        {
                            miscDataSorted.add(new MMData());
                            miscDataSorted.get(0).type = (String)((JSONObject)(dataArray.get(i))).get("Type");
                            miscDataSorted.get(0).miscData.add(tempMisc);
                        }
                        else
                        {
                            boolean matchFound = false;
                            for(int j = 0; j < miscDataSorted.size(); j++)
                            {
                                if(miscDataSorted.get(j).type.equals((String)((JSONObject)(dataArray.get(i))).get("Type")))
                                {
                                    matchFound = true;
                                    miscDataSorted.get(j).miscData.add(tempMisc);
                                    break;
                                }
                            }

                            if(!matchFound)
                            {
                                MMData tempMmData = new MMData();
                                tempMmData.type = (String)((JSONObject)(dataArray.get(i))).get("Type");
                                tempMmData.miscData.add(tempMisc);

                                miscDataSorted.add(tempMmData);
                            }
                        }

                        break;
                    }
                    case "twointWpatch":
                    {
                        JSONArray offsetsArray = (JSONArray)(((JSONObject)dataArray.get(i)).get("Offsets"));

                        Misc.twointWpatch tempMisc = new Misc.twointWpatch();
                        tempMisc.name = (String)((JSONObject)(dataArray.get(i))).get("Name");

                        locator = Math.toIntExact((Long)offsetsArray.get(0));
                        tempMisc.value = ByteUtils.bytesToInt(givenFiledata[locator], givenFiledata[locator + 1]);

                        if(tempMisc.name.equals("Merlee Experience Multiplier") && tempMisc.value == 2108) {tempMisc.value = 2;}
                        if(tempMisc.name.equals("Experience Multiplier") && tempMisc.value == 888) {tempMisc.value = 1;}
                        if(tempMisc.name.equals("Merluvlee Next Path Cost") && tempMisc.value == 27777) {tempMisc.value = 10;}

                        tempMisc.textField.setText(Integer.toString(tempMisc.value));
                        
                        //Check if already patched
                        tempMisc.rewrite = checkForPatch(givenFiledata, (JSONObject)dataArray.get(i), "PatchOffsets");

                        if(miscDataSorted.size() == 0)
                        {
                            miscDataSorted.add(new MMData());
                            miscDataSorted.get(0).type = (String)((JSONObject)(dataArray.get(i))).get("Type");
                            miscDataSorted.get(0).miscData.add(tempMisc);
                        }
                        else
                        {
                            boolean matchFound = false;
                            for(int j = 0; j < miscDataSorted.size(); j++)
                            {
                                if(miscDataSorted.get(j).type.equals((String)((JSONObject)(dataArray.get(i))).get("Type")))
                                {
                                    matchFound = true;
                                    miscDataSorted.get(j).miscData.add(tempMisc);
                                    break;
                                }
                            }

                            if(!matchFound)
                            {
                                MMData tempMmData = new MMData();
                                tempMmData.type = (String)((JSONObject)(dataArray.get(i))).get("Type");
                                tempMmData.miscData.add(tempMisc);

                                miscDataSorted.add(tempMmData);
                            }
                        }

                        break;
                    }
                    case "fourint":
                    {
                        JSONArray offsetsArray = (JSONArray)(((JSONObject)dataArray.get(i)).get("Offsets"));

                        Misc.fourint tempMisc = new Misc.fourint();
                        tempMisc.name = (String)((JSONObject)(dataArray.get(i))).get("Name");

                        locator = Math.toIntExact((Long)offsetsArray.get(0));
                        tempMisc.value = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        tempMisc.textField.setText(Long.toString(tempMisc.value));

                        if (tempMisc.name.equals("Mowz Smooch 0% - 33% Success Heal Amount") ||
                            tempMisc.name.equals("Mowz Smooch 33% - 66% Success Heal Amount") || 
                            tempMisc.name.equals("Mowz Smooch 66% - 99% Success Heal Amount") ||
                            tempMisc.name.equals("Mowz Smooch 100% Success Heal Amount"))
                        {
                            tempMisc.textField.disableProperty().bind(MMGUI.mowzFlag);
                        }

                        if(miscDataSorted.size() == 0)
                        {
                            miscDataSorted.add(new MMData());
                            miscDataSorted.get(0).type = (String)((JSONObject)(dataArray.get(i))).get("Type");
                            miscDataSorted.get(0).miscData.add(tempMisc);
                        }
                        else
                        {
                            boolean matchFound = false;
                            for(int j = 0; j < miscDataSorted.size(); j++)
                            {
                                if(miscDataSorted.get(j).type.equals((String)((JSONObject)(dataArray.get(i))).get("Type")))
                                {
                                    matchFound = true;
                                    miscDataSorted.get(j).miscData.add(tempMisc);
                                    break;
                                }
                            }

                            if(!matchFound)
                            {
                                MMData tempMmData = new MMData();
                                tempMmData.type = (String)((JSONObject)(dataArray.get(i))).get("Type");
                                tempMmData.miscData.add(tempMisc);

                                miscDataSorted.add(tempMmData);
                            }
                        }

                        break;
                    }
                    case "fourintPN":
                    {
                        JSONArray offsetsArray = (JSONArray)(((JSONObject)dataArray.get(i)).get("Offsets"));

                        Misc.fourintPN tempMisc = new Misc.fourintPN();
                        tempMisc.name = (String)((JSONObject)(dataArray.get(i))).get("Name");

                        locator = Math.toIntExact((Long)offsetsArray.get(0));
                        tempMisc.value = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        tempMisc.textField.setText(Long.toString(tempMisc.value));

                        if(miscDataSorted.size() == 0)
                        {
                            miscDataSorted.add(new MMData());
                            miscDataSorted.get(0).type = (String)((JSONObject)(dataArray.get(i))).get("Type");
                            miscDataSorted.get(0).miscData.add(tempMisc);
                        }
                        else
                        {
                            boolean matchFound = false;
                            for(int j = 0; j < miscDataSorted.size(); j++)
                            {
                                if(miscDataSorted.get(j).type.equals((String)((JSONObject)(dataArray.get(i))).get("Type")))
                                {
                                    matchFound = true;
                                    miscDataSorted.get(j).miscData.add(tempMisc);
                                    break;
                                }
                            }

                            if(!matchFound)
                            {
                                MMData tempMmData = new MMData();
                                tempMmData.type = (String)((JSONObject)(dataArray.get(i))).get("Type");
                                tempMmData.miscData.add(tempMisc);

                                miscDataSorted.add(tempMmData);
                            }
                        }

                        break;
                    }
                    case "Float":
                    {
                        JSONArray offsetsArray = (JSONArray)(((JSONObject)dataArray.get(i)).get("Offsets"));

                        Misc.Float tempMisc = new Misc.Float();
                        tempMisc.name = (String)((JSONObject)(dataArray.get(i))).get("Name");

                        locator = Math.toIntExact((Long)offsetsArray.get(0));
                        tempMisc.value = ByteUtils.bytesFloatToFloat(givenFiledata[locator], givenFiledata[locator + 1],  givenFiledata[locator + 2],  givenFiledata[locator + 3]);

                        tempMisc.textField.setText(Float.toString(tempMisc.value));
                        
                        if(miscDataSorted.size() == 0)
                        {
                            miscDataSorted.add(new MMData());
                            miscDataSorted.get(0).type = (String)((JSONObject)(dataArray.get(i))).get("Type");
                            miscDataSorted.get(0).miscData.add(tempMisc);
                        }
                        else
                        {
                            boolean matchFound = false;
                            for(int j = 0; j < miscDataSorted.size(); j++)
                            {
                                if(miscDataSorted.get(j).type.equals((String)((JSONObject)(dataArray.get(i))).get("Type")))
                                {
                                    matchFound = true;
                                    miscDataSorted.get(j).miscData.add(tempMisc);
                                    break;
                                }
                            }

                            if(!matchFound)
                            {
                                MMData tempMmData = new MMData();
                                tempMmData.type = (String)((JSONObject)(dataArray.get(i))).get("Type");
                                tempMmData.miscData.add(tempMisc);

                                miscDataSorted.add(tempMmData);
                            }
                        }

                        break;
                    }
                    case "hexColor":
                    {
                        JSONArray offsetsArray = (JSONArray)(((JSONObject)dataArray.get(i)).get("Offsets"));

                        Misc.hexColor tempMisc = new Misc.hexColor();
                        tempMisc.name = (String)((JSONObject)(dataArray.get(i))).get("Name");

                        locator = Math.toIntExact((Long)offsetsArray.get(0));
                        tempMisc.colorValue = ByteUtils.colorFromBytes(givenFiledata[locator], givenFiledata[locator + 1],  givenFiledata[locator + 2],  givenFiledata[locator + 3]);

                        tempMisc.colorPicker.setValue(tempMisc.colorValue);

                        if(miscDataSorted.size() == 0)
                        {
                            miscDataSorted.add(new MMData());
                            miscDataSorted.get(0).type = (String)((JSONObject)(dataArray.get(i))).get("Type");
                            miscDataSorted.get(0).miscData.add(tempMisc);
                        }
                        else
                        {
                            boolean matchFound = false;
                            for(int j = 0; j < miscDataSorted.size(); j++)
                            {
                                if(miscDataSorted.get(j).type.equals((String)((JSONObject)(dataArray.get(i))).get("Type")))
                                {
                                    matchFound = true;
                                    miscDataSorted.get(j).miscData.add(tempMisc);
                                    break;
                                }
                            }

                            if(!matchFound)
                            {
                                MMData tempMmData = new MMData();
                                tempMmData.type = (String)((JSONObject)(dataArray.get(i))).get("Type");
                                tempMmData.miscData.add(tempMisc);

                                miscDataSorted.add(tempMmData);
                            }
                        }

                        break;
                    }
                    case "odds":
                    {
                        JSONArray offsetsArray = (JSONArray)(((JSONObject)dataArray.get(i)).get("Offsets"));

                        Misc.odds tempMisc = new Misc.odds();
                        tempMisc.name = (String)((JSONObject)(dataArray.get(i))).get("Name");

                        locator = Math.toIntExact((Long)offsetsArray.get(0));
                        tempMisc.successRate = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        locator = Math.toIntExact((Long)offsetsArray.get(1));
                        tempMisc.outOf = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        tempMisc.textField.setText(Long.toString(tempMisc.successRate) + "/" + Long.toString(tempMisc.outOf));

                        if(miscDataSorted.size() == 0)
                        {
                            miscDataSorted.add(new MMData());
                            miscDataSorted.get(0).type = (String)((JSONObject)(dataArray.get(i))).get("Type");
                            miscDataSorted.get(0).miscData.add(tempMisc);
                        }
                        else
                        {
                            boolean matchFound = false;
                            for(int j = 0; j < miscDataSorted.size(); j++)
                            {
                                if(miscDataSorted.get(j).type.equals((String)((JSONObject)(dataArray.get(i))).get("Type")))
                                {
                                    matchFound = true;
                                    miscDataSorted.get(j).miscData.add(tempMisc);
                                    break;
                                }
                            }

                            if(!matchFound)
                            {
                                MMData tempMmData = new MMData();
                                tempMmData.type = (String)((JSONObject)(dataArray.get(i))).get("Type");
                                tempMmData.miscData.add(tempMisc);

                                miscDataSorted.add(tempMmData);
                            }
                        }

                        break;
                    }
                    case "oddsRev":
                    {
                        JSONArray offsetsArray = (JSONArray)(((JSONObject)dataArray.get(i)).get("Offsets"));

                        Misc.oddsRev tempMisc = new Misc.oddsRev();
                        tempMisc.name = (String)((JSONObject)(dataArray.get(i))).get("Name");

                        locator = Math.toIntExact((Long)offsetsArray.get(1));
                        tempMisc.outOf = ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        locator = Math.toIntExact((Long)offsetsArray.get(0));
                        tempMisc.successRate = tempMisc.outOf - ByteUtils.bytesToSignedLong(givenFiledata[locator], givenFiledata[locator + 1], givenFiledata[locator + 2], givenFiledata[locator + 3]);

                        tempMisc.textField.setText(Long.toString(tempMisc.successRate) + "/" + Long.toString(tempMisc.outOf));

                        if(miscDataSorted.size() == 0)
                        {
                            miscDataSorted.add(new MMData());
                            miscDataSorted.get(0).type = (String)((JSONObject)(dataArray.get(i))).get("Type");
                            miscDataSorted.get(0).miscData.add(tempMisc);
                        }
                        else
                        {
                            boolean matchFound = false;
                            for(int j = 0; j < miscDataSorted.size(); j++)
                            {
                                if(miscDataSorted.get(j).type.equals((String)((JSONObject)(dataArray.get(i))).get("Type")))
                                {
                                    matchFound = true;
                                    miscDataSorted.get(j).miscData.add(tempMisc);
                                    break;
                                }
                            }

                            if(!matchFound)
                            {
                                MMData tempMmData = new MMData();
                                tempMmData.type = (String)((JSONObject)(dataArray.get(i))).get("Type");
                                tempMmData.miscData.add(tempMisc);

                                miscDataSorted.add(tempMmData);
                            }
                        }

                        break;
                    }
                    case "Function":
                    {
                        Misc.Function tempMisc = new Misc.Function();
                        tempMisc.name = (String)((JSONObject)(dataArray.get(i))).get("Name");

                        //Check if already patched
                        tempMisc.rewrite = checkForPatch(givenFiledata, (JSONObject)dataArray.get(i), "Offsets");
                        
                        tempMisc.checkBox.setSelected(tempMisc.rewrite);

                        if (tempMisc.name.equals("Mowz Smooch Heal Amount Fix"))
                        {
                            tempMisc.checkBox.selectedProperty().addListener((obs, wasSelected, isSelected) ->
                            {
                                MMGUI.mowzFlag.set(!isSelected);
                            });
                        }

                        if(miscDataSorted.size() == 0)
                        {
                            miscDataSorted.add(new MMData());
                            miscDataSorted.get(0).type = (String)((JSONObject)(dataArray.get(i))).get("Type");
                            miscDataSorted.get(0).miscData.add(tempMisc);
                        }
                        else
                        {
                            boolean matchFound = false;
                            for(int j = 0; j < miscDataSorted.size(); j++)
                            {
                                if(miscDataSorted.get(j).type.equals((String)((JSONObject)(dataArray.get(i))).get("Type")))
                                {
                                    matchFound = true;
                                    miscDataSorted.get(j).miscData.add(tempMisc);
                                    break;
                                }
                            }

                            if(!matchFound)
                            {
                                MMData tempMmData = new MMData();
                                tempMmData.type = (String)((JSONObject)(dataArray.get(i))).get("Type");
                                tempMmData.miscData.add(tempMisc);

                                miscDataSorted.add(tempMmData);
                            }
                        }

                        break;
                    }
                    case "bingoSelectionBox":
                    {
                        JSONArray offsetsArray = (JSONArray)(((JSONObject)dataArray.get(i)).get("Offsets"));

                        Misc.bingoSelectionBox tempMisc = new Misc.bingoSelectionBox();
                        tempMisc.name = (String)((JSONObject)(dataArray.get(i))).get("Name");

                        locator = Math.toIntExact((Long)offsetsArray.get(0));
                        tempMisc.value = ByteUtils.bytesToInt(givenFiledata[locator]);

                        tempMisc.comboBox.getItems().addAll("Mushroom", "Flower", "Star", "Shine", "Poison Mushroom");
                        tempMisc.comboBox.getSelectionModel().select(tempMisc.value);

                        if(miscDataSorted.size() == 0)
                        {
                            miscDataSorted.add(new MMData());
                            miscDataSorted.get(0).type = (String)((JSONObject)(dataArray.get(i))).get("Type");
                            miscDataSorted.get(0).miscData.add(tempMisc);
                        }
                        else
                        {
                            boolean matchFound = false;
                            for(int j = 0; j < miscDataSorted.size(); j++)
                            {
                                if(miscDataSorted.get(j).type.equals((String)((JSONObject)(dataArray.get(i))).get("Type")))
                                {
                                    matchFound = true;
                                    miscDataSorted.get(j).miscData.add(tempMisc);
                                    break;
                                }
                            }

                            if(!matchFound)
                            {
                                MMData tempMmData = new MMData();
                                tempMmData.type = (String)((JSONObject)(dataArray.get(i))).get("Type");
                                tempMmData.miscData.add(tempMisc);

                                miscDataSorted.add(tempMmData);
                            }
                        }

                        break;
                    }
                }
            }

            return miscDataSorted;
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
     * @Function Will export the array of MMData back into the same format as the given file
     */
    public static byte[] buildNewFile(File givenFile, ArrayList<MMData> fileData)
    {
        File jsonFile = new File("src\\MiscData.json");
        byte[] givenFiledata = ByteUtils.readData(givenFile);
        int locator = 0;

        HashMap<String, Integer> counts = new HashMap<String, Integer>();
        for(MMData mm : fileData)
        {
            counts.put(mm.type, 0);
        }

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

            JSONArray dataArray = (JSONArray)fileObj.get("Data");

            for(int i = 0; i < dataArray.size(); i++)
            {
                String tempDataType = (String)((JSONObject)(dataArray.get(i))).get("DataType");

                switch(tempDataType)
                {
                    case "oneint":
                    {
                        JSONArray offsetsArray = (JSONArray)(((JSONObject)dataArray.get(i)).get("Offsets"));
                        String tempType = (String)((JSONObject)(dataArray.get(i))).get("Type");

                        int tracker = findMMDataIndexByType(fileData, tempType);
                        
                        //System.out.println("oneint at: " + tempType + " " + counts.get(tempType));
                        
                        for(int j = 0; j < offsetsArray.size(); j++)
                        {
                            locator = Math.toIntExact((Long)offsetsArray.get(j));
                            givenFiledata[locator] = ByteUtils.intTo1Byte(((Misc.oneint)(fileData.get(tracker).miscData.get(counts.get(tempType)))).value);
                            //System.out.println("Writing " + String.format("0x%02X", (ByteUtils.intTo1Byte(((Misc.oneint)(fileData.get(tracker).miscData.get(counts.get(tempType)))).value)) & 0xFF) + " to " + locator);
                        }

                        counts.put(tempType, (counts.get(tempType) + 1));

                        break;
                    }
                    case "twoint":
                    {
                        JSONArray offsetsArray = (JSONArray)(((JSONObject)dataArray.get(i)).get("Offsets"));
                        String tempType = (String)((JSONObject)(dataArray.get(i))).get("Type");

                        int tracker = findMMDataIndexByType(fileData, tempType);
                        
                        //System.out.println("twoint at: " + tempType + " " + counts.get(tempType));
                        
                        for(int j = 0; j < offsetsArray.size(); j++)
                        {
                            locator = Math.toIntExact((Long)offsetsArray.get(j));
                            byte[] temptwoint = ByteUtils.intTo2Bytes(((Misc.twoint)(fileData.get(tracker).miscData.get(counts.get(tempType)))).value);
                            for(int k = 0; k < 2 ; k++)
                            {
                                givenFiledata[locator + k] = temptwoint[k];
                                //System.out.println("Writing " + String.format("0x%02X", temptwoint[k] & 0xFF) + " to " + (locator + k));
                            }
                        }

                        counts.put(tempType, (counts.get(tempType) + 1));

                        break;
                    }
                    case "twointConstraint":
                    {
                        JSONArray offsetsArray = (JSONArray)(((JSONObject)dataArray.get(i)).get("Offsets"));
                        String tempType = (String)((JSONObject)(dataArray.get(i))).get("Type");

                        int tracker = findMMDataIndexByType(fileData, tempType);
                        
                        //System.out.println("twointConstraint at: " + tempType + " " + counts.get(tempType));
                        
                        for(int j = 0; j < offsetsArray.size(); j++)
                        {
                            locator = Math.toIntExact((Long)offsetsArray.get(j));
                            byte[] temptwointConstraint = ByteUtils.intTo2Bytes(((Misc.twointConstraint)(fileData.get(tracker).miscData.get(counts.get(tempType)))).value);
                            for(int k = 0; k < 2 ; k++)
                            {
                                givenFiledata[locator + k] = temptwointConstraint[k];
                                //System.out.println("Writing " + String.format("0x%02X", temptwointConstraint[k] & 0xFF) + " to " + (locator + k));
                            }
                        }

                        counts.put(tempType, (counts.get(tempType) + 1));

                        break;
                    }
                    case "twointWpatch":
                    {
                        JSONArray offsetsArray = (JSONArray)(((JSONObject)dataArray.get(i)).get("Offsets"));
                        String tempType = (String)((JSONObject)(dataArray.get(i))).get("Type");

                        int tracker = findMMDataIndexByType(fileData, tempType);

                        //System.out.println("twointWpatch at: " + tempType + " " + counts.get(tempType));

                        for(int j = 0; j < offsetsArray.size(); j++)
                        {
                            locator = Math.toIntExact((Long)offsetsArray.get(j));
                            byte[] temptwointWpatch = ByteUtils.intTo2Bytes(((Misc.twointWpatch)(fileData.get(tracker).miscData.get(counts.get(tempType)))).value);
                            for(int k = 0; k < 2 ; k++)
                            {
                                givenFiledata[locator + k] = temptwointWpatch[k];
                                //System.out.println("Writing " + String.format("0x%02X", temptwointWpatch[k] & 0xFF) + " to " + (locator + k));
                            }
                        }

                        JSONArray patchOffsetsArray = (JSONArray)(((JSONObject)dataArray.get(i)).get("PatchOffsets"));
                        JSONArray rewritesArray = (JSONArray)(((JSONObject)dataArray.get(i)).get("Rewrites"));

                        for(int j = 0; j < patchOffsetsArray.size(); j++)
                        {
                            locator = Math.toIntExact((Long)patchOffsetsArray.get(j));
                            byte[] bytes = ByteUtils.hexStringToBytes((String)rewritesArray.get(j));
                            for(int k = 0; k < bytes.length; k++)
                            {
                                givenFiledata[locator + k] = bytes[k];
                                //System.out.println("Writing " + String.format("0x%02X", bytes[k] & 0xFF) + " to " + (locator + k));
                            }
                        }

                        counts.put(tempType, (counts.get(tempType) + 1));

                        break;
                    }
                    case "fourint":
                    {
                        JSONArray offsetsArray = (JSONArray)(((JSONObject)dataArray.get(i)).get("Offsets"));
                        String tempType = (String)((JSONObject)(dataArray.get(i))).get("Type");

                        int tracker = findMMDataIndexByType(fileData, tempType);
                        
                        //System.out.println("fourint at: " + tempType + " " + counts.get(tempType));
                        
                        for(int j = 0; j < offsetsArray.size(); j++)
                        {
                            locator = Math.toIntExact((Long)offsetsArray.get(j));
                            byte[] tempfourint = ByteUtils.longTo4Bytes(((Misc.fourint)(fileData.get(tracker).miscData.get(counts.get(tempType)))).value);
                            for(int k = 0; k < 4 ; k++)
                            {
                                givenFiledata[locator + k] = tempfourint[k];
                                //System.out.println("Writing " + String.format("0x%02X", tempfourint[k] & 0xFF) + " to " + (locator + k));
                            }
                        }

                        counts.put(tempType, (counts.get(tempType) + 1));

                        break;
                    }
                    case "fourintPN":
                    {
                        JSONArray offsetsArray = (JSONArray)(((JSONObject)dataArray.get(i)).get("Offsets"));
                        String tempType = (String)((JSONObject)(dataArray.get(i))).get("Type");

                        int tracker = findMMDataIndexByType(fileData, tempType);
                        
                        System.out.println("fourintPN at: " + tempType + " " + counts.get(tempType));
                        
                        for(int j = 0; j < (offsetsArray.size() / 2); j += 2)
                        {
                            locator = Math.toIntExact((Long)offsetsArray.get(j));
                            byte[] tempfourintPN = ByteUtils.longTo4Bytes(((Misc.fourintPN)(fileData.get(tracker).miscData.get(counts.get(tempType)))).value);
                            for(int k = 0; k < 4 ; k++)
                            {
                                givenFiledata[locator + k] = tempfourintPN[k];
                                System.out.println("Writing " + String.format("0x%02X", tempfourintPN[k] & 0xFF) + " to " + (locator + k));
                            }

                            locator = Math.toIntExact((Long)offsetsArray.get(j + 1));
                            tempfourintPN = ByteUtils.longTo4Bytes(((Misc.fourintPN)(fileData.get(tracker).miscData.get(counts.get(tempType)))).value * -1);
                            for(int k = 0; k < 4 ; k++)
                            {
                                givenFiledata[locator + k] = tempfourintPN[k];
                                System.out.println("Writing " + String.format("0x%02X", tempfourintPN[k] & 0xFF) + " to " + (locator + k));
                            }
                        }

                        counts.put(tempType, (counts.get(tempType) + 1));

                        break;
                    }
                    case "Float":
                    {
                        JSONArray offsetsArray = (JSONArray)(((JSONObject)dataArray.get(i)).get("Offsets"));
                        String tempType = (String)((JSONObject)(dataArray.get(i))).get("Type");

                        int tracker = findMMDataIndexByType(fileData, tempType);
                        
                        //System.out.println("Float at: " + tempType + " " + counts.get(tempType));
                        
                        for(int j = 0; j < offsetsArray.size(); j++)
                        {
                            locator = Math.toIntExact((Long)offsetsArray.get(j));
                            byte[] tempFloat = ByteUtils.floatToBytesFloat(((Misc.Float)(fileData.get(tracker).miscData.get(counts.get(tempType)))).value);
                            for(int k = 0; k < 4 ; k++)
                            {
                                givenFiledata[locator + k] = tempFloat[k];
                                //System.out.println("Writing " + String.format("0x%02X", tempFloat[k] & 0xFF) + " to " + (locator + k));
                            }
                        }

                        counts.put(tempType, (counts.get(tempType) + 1));

                        break;
                    }
                    case "hexColor":
                    {
                        JSONArray offsetsArray = (JSONArray)(((JSONObject)dataArray.get(i)).get("Offsets"));
                        String tempType = (String)((JSONObject)(dataArray.get(i))).get("Type");

                        int tracker = findMMDataIndexByType(fileData, tempType);
                        
                        //System.out.println("hexColor at: " + tempType + " " + counts.get(tempType));
                        
                        for(int j = 0; j < offsetsArray.size(); j++)
                        {
                            locator = Math.toIntExact((Long)offsetsArray.get(j));
                            byte[] temphexColor = ByteUtils.colorToBytes(((Misc.hexColor)(fileData.get(tracker).miscData.get(counts.get(tempType)))).colorValue);
                            for(int k = 0; k < 4 ; k++)
                            {
                                givenFiledata[locator + k] = temphexColor[k];
                                //System.out.println("Writing " + String.format("0x%02X", temphexColor[k] & 0xFF) + " to " + (locator + k));
                            }
                        }

                        counts.put(tempType, (counts.get(tempType) + 1));

                        break;
                    }
                    case "odds":
                    {
                        JSONArray offsetsArray = (JSONArray)(((JSONObject)dataArray.get(i)).get("Offsets"));
                        String tempType = (String)((JSONObject)(dataArray.get(i))).get("Type");

                        int tracker = findMMDataIndexByType(fileData, tempType);
                        
                        //System.out.println("odds at: " + tempType + " " + counts.get(tempType));
                        
                        for(int j = 0; j < (offsetsArray.size() / 2); j += 2)
                        {
                            locator = Math.toIntExact((Long)offsetsArray.get(j));
                            byte[] tempodds = ByteUtils.longTo4Bytes(((Misc.odds)(fileData.get(tracker).miscData.get(counts.get(tempType)))).successRate);
                            for(int k = 0; k < 4 ; k++)
                            {
                                givenFiledata[locator + k] = tempodds[k];
                                //System.out.println("Writing " + String.format("0x%02X", tempodds[k] & 0xFF) + " to " + (locator + k));
                            }

                            locator = Math.toIntExact((Long)offsetsArray.get(j + 1));
                            tempodds = ByteUtils.longTo4Bytes(((Misc.odds)(fileData.get(tracker).miscData.get(counts.get(tempType)))).outOf);
                            for(int k = 0; k < 4 ; k++)
                            {
                                givenFiledata[locator + k] = tempodds[k];
                                //System.out.println("Writing " + String.format("0x%02X", tempodds[k] & 0xFF) + " to " + (locator + k));
                            }
                        }

                        counts.put(tempType, (counts.get(tempType) + 1));

                        break;
                    }
                    case "oddsRev":
                    {
                        JSONArray offsetsArray = (JSONArray)(((JSONObject)dataArray.get(i)).get("Offsets"));
                        String tempType = (String)((JSONObject)(dataArray.get(i))).get("Type");

                        int tracker = findMMDataIndexByType(fileData, tempType);
                        
                        //System.out.println("oddsRev at: " + tempType + " " + counts.get(tempType));
                        
                        for(int j = 0; j < (offsetsArray.size() / 2); j += 2)
                        {
                            long difference = ((Misc.oddsRev)(fileData.get(tracker).miscData.get(counts.get(tempType)))).outOf - ((Misc.oddsRev)(fileData.get(tracker).miscData.get(counts.get(tempType)))).successRate;

                            locator = Math.toIntExact((Long)offsetsArray.get(j));
                            byte[] tempoddsRev = ByteUtils.longTo4Bytes(difference);
                            for(int k = 0; k < 4 ; k++)
                            {
                                givenFiledata[locator + k] = tempoddsRev[k];
                                //System.out.println("Writing " + String.format("0x%02X", tempoddsRev[k] & 0xFF) + " to " + (locator + k));
                            }

                            locator = Math.toIntExact((Long)offsetsArray.get(j + 1));
                            tempoddsRev = ByteUtils.longTo4Bytes(((Misc.oddsRev)(fileData.get(tracker).miscData.get(counts.get(tempType)))).outOf);
                            for(int k = 0; k < 4 ; k++)
                            {
                                givenFiledata[locator + k] = tempoddsRev[k];
                                //System.out.println("Writing " + String.format("0x%02X", tempoddsRev[k] & 0xFF) + " to " + (locator + k));
                            }
                        }

                        counts.put(tempType, (counts.get(tempType) + 1));

                        break;
                    }
                    case "Function":
                    {
                        JSONArray offsetsArray = (JSONArray)(((JSONObject)dataArray.get(i)).get("Offsets"));
                        JSONArray vanillaArray = (JSONArray)(((JSONObject)dataArray.get(i)).get("Vanilla"));
                        JSONArray rewritesArray = (JSONArray)(((JSONObject)dataArray.get(i)).get("Rewrites"));
                        String tempType = (String)((JSONObject)(dataArray.get(i))).get("Type");

                        int tracker = findMMDataIndexByType(fileData, tempType);

                        //System.out.println("Function at: " + tempType + " " + counts.get(tempType));

                        if(((Misc.Function)(fileData.get(tracker).miscData.get(counts.get(tempType)))).rewrite)
                        {
                            for(int j = 0; j < offsetsArray.size(); j++)
                            {
                                locator = Math.toIntExact((Long)offsetsArray.get(j));
                                byte[] bytes = ByteUtils.hexStringToBytes((String)rewritesArray.get(j));
                                for(int k = 0; k < bytes.length; k++)
                                {
                                    givenFiledata[locator + k] = bytes[k];
                                    //System.out.println("Writing " + String.format("0x%02X", bytes[k] & 0xFF) + " to " + (locator + k));
                                }
                            }
                        }
                        else
                        {
                            for(int j = 0; j < offsetsArray.size(); j++)
                            {
                                locator = Math.toIntExact((Long)offsetsArray.get(j));
                                byte[] bytes = ByteUtils.hexStringToBytes((String)vanillaArray.get(j));
                                for(int k = 0; k < bytes.length; k++)
                                {
                                    givenFiledata[locator + k] = bytes[k];
                                    //System.out.println("Writing " + String.format("0x%02X", bytes[k] & 0xFF) + " to " + (locator + k));
                                }
                            }
                        }

                        counts.put(tempType, (counts.get(tempType) + 1));

                        break;
                    }
                    case "bingoSelectionBox":
                    {
                        JSONArray offsetsArray = (JSONArray)(((JSONObject)dataArray.get(i)).get("Offsets"));
                        String tempType = (String)((JSONObject)(dataArray.get(i))).get("Type");

                        int tracker = findMMDataIndexByType(fileData, tempType);
                        
                        System.out.println("bingoSelectionBox at: " + tempType + " " + counts.get(tempType));
                        
                        for(int j = 0; j < offsetsArray.size(); j++)
                        {
                            locator = Math.toIntExact((Long)offsetsArray.get(j));
                            givenFiledata[locator] = ByteUtils.intTo1Byte(((Misc.bingoSelectionBox)(fileData.get(tracker).miscData.get(counts.get(tempType)))).value);
                            System.out.println("Writing " + String.format("0x%02X", (ByteUtils.intTo1Byte(((Misc.bingoSelectionBox)(fileData.get(tracker).miscData.get(counts.get(tempType)))).value)) & 0xFF) + " to " + locator);
                        }

                        counts.put(tempType, (counts.get(tempType) + 1));

                        break;
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
     * @Function Compares JSON data value to file data lists, returns index if match found
     */
    public static int findMMDataIndexByType(ArrayList<MMData> fileData, String type)
    {
        for(int i = 0; i < fileData.size(); i++)
        {
            if(fileData.get(i).type.equals(type)) {return i;}
        }

        return -1;
    }

    /**
     * @Author Jemaroo
     * @Function Compares rewrite values to file data, returns true if matching
     */
    public static boolean checkForPatch(byte[] givenFiledata, JSONObject misc, String offsetsName)
    {
        JSONArray offsetsArray = (JSONArray)misc.get(offsetsName);
        JSONArray rewritesArray = (JSONArray)misc.get("Rewrites");

        for (int i = 0; i < rewritesArray.size(); i++)
        {
            byte[] bytes = ByteUtils.hexStringToBytes((String)rewritesArray.get(i));
            int locator = Math.toIntExact((Long)offsetsArray.get(i));

            for (int j = 0; j < bytes.length; j++)
            {
                //System.out.println("Comparing: " + givenFiledata[locator + j] + " to " + bytes[j]);
                if (bytes[j] != givenFiledata[locator + j]) {return false;}
            }
        }

        return true;
    }
}