import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.simple.parser.*;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.*;
import javafx.util.Duration;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BUTGUI extends Application 
{
    private ArrayList<String> validFileNames = new ArrayList<String>();
    boolean isFileOpened = false; 
    Stage window;
    File givenFile;
    private ArrayList<BUTData> units = new ArrayList<BUTData>();
    ComboBox<BUTData> unitSelector = null;
    private ListView<Object> kindList = null;
    private ListView<Object> defenseList = null;
    private ListView<BattleWeapon> weaponList = null;
    Button saveChangesButton = new Button("Save Struct Changes");
    Button saveStructPresetButton = new Button("Save Struct Preset");
    Button loadStructPresetButton = new Button("Load Struct Preset");
    HBox upperBox = new HBox();
    HBox upperBox2 = new HBox();

    HBox topMenu = new HBox();
    HBox centerMenu = new HBox();
    Button openButton = new Button("Open Directory");
    ComboBox<File> fileSelector = new ComboBox<>();
    Button exportButton = new Button("Export File");
    Button presetsButton = new Button("Presets");
    Stage presetBox = new Stage();
    Button saveFilePresetButton = new Button("Save File Preset");
    Button loadFilePresetButton = new Button("Load File Preset");
    Button updateTattlesButton = new Button("Update Tattles");
    Button closeButton = new Button("Close File");
    Button optionButton = new Button("Options");
    Button aboutButton = new Button("About");
    boolean hasStartPath = false;
    BorderPane borderPane = new BorderPane();

    private static final String presetButtonStyle = "-fx-font-size: 12px; -fx-font-weight: bold;";
    private static final String RED_STYLE = "-fx-text-fill: red; -fx-font-weight: bold;";
    private static final String BLACK_STYLE = "-fx-text-fill: black; -fx-font-weight: normal;";

    HashMap<String, Image> images = new HashMap<String, Image>();
    Map<String, Integer> filePriority = new HashMap<String, Integer>();
    ChangeListener<String>[] listeners; //75

    private TextField[] textFields; //75
    private ComboBox<String>[] comboBoxFields; //6
    private CheckBox[] checkBoxFields; //75

    @Override
    public void start(Stage primaryStage) 
    {
        try
        {
            File jsonFile = new File("src\\UnitData.json");
            JSONParser parser = new JSONParser();
            JSONObject root = (JSONObject)parser.parse(new FileReader(jsonFile));
            JSONArray fileArray = (JSONArray)root.get("File");
            JSONObject fileObj = null;

            for(int i = 0; i < fileArray.size(); i ++)
            {
                fileObj = (JSONObject)fileArray.get(i);
                validFileNames.add((String)fileObj.get("Name"));
            }
        }
        catch (FileNotFoundException e){System.out.println("There was an Error Finding the JSON File");}
        catch (IOException e){System.out.println("There was an Error Reading the JSON File");}
        catch (ParseException e){System.out.println("There was an Error Parsing the JSON File");}

        initializeFields();
        images = GUI.setImages(images);
        setFilePriority();

        //Window
        window = primaryStage;
        window.setTitle("Battle Unit Tool");

        //Menu Buttons
        fileSelector.getItems().clear();
        topMenu.getChildren().addAll(openButton, optionButton, aboutButton);
        topMenu.setPadding(new Insets(5));
        topMenu.setSpacing(5);

        saveFilePresetButton.setPrefSize(170, 50);
        loadFilePresetButton.setPrefSize(170, 50);
        saveFilePresetButton.setStyle(presetButtonStyle);
        loadFilePresetButton.setStyle(presetButtonStyle);

        //Alligning Menu Buttons to Top
        borderPane.setTop(topMenu);

        fileSelector.setMaxWidth(270);

        //Scene
        Scene emptyScene = new Scene(borderPane, 818, 600);
        window.setScene(emptyScene);

        String startPath = "";
        try
        {
            File jsonFile = new File("src\\options.json");
            JSONParser parser = new JSONParser();
            JSONObject root = (JSONObject)parser.parse(new FileReader(jsonFile));

            if(((String)root.get("BUTLastFolder")).equals("true")) startPath = (String)root.get("startPath");
            if(((String)root.get("BUTRedFields")).equals("true")) setRed0TextFieldFormats(true);
        }
        catch (FileNotFoundException e){System.out.println("There was an Error Finding the JSON File");}
        catch (IOException e){System.out.println("There was an Error Reading the JSON File");}
        catch (ParseException e){System.out.println("There was an Error Parsing the JSON File");}

        if(!startPath.equals(""))
        {
            givenFile = new File(startPath);
            loadGUIMenus();
        }

        openButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                DirectoryChooser directoryChooser = new DirectoryChooser();
                directoryChooser.setTitle("Select the root folder");
                givenFile = directoryChooser.showDialog(window);

                try
                {
                    File jsonFile = new File("src\\options.json");
                    JSONParser parser = new JSONParser();
                    JSONObject root = (JSONObject)parser.parse(new FileReader(jsonFile));

                    root.put("startPath", givenFile.getAbsolutePath());
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    Object asJson = gson.fromJson(root.toJSONString(), Object.class);
                    try (FileWriter writer = new FileWriter(jsonFile)) 
                    {
                        gson.toJson(asJson, writer);
                    }
                }
                catch (FileNotFoundException e){System.out.println("There was an Error Finding the JSON File");}
                catch (IOException e){System.out.println("There was an Error Reading the JSON File");}
                catch (ParseException e){System.out.println("There was an Error Parsing the JSON File");}

                closeButton.fire();
                loadGUIMenus();
            }
        });

        exportButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                if(isFileOpened)
                {
                    try
                    {
                        java.nio.file.Path backupPath = Paths.get("backup", (LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy HH-mm-ss")) + " " + fileSelector.getSelectionModel().getSelectedItem().getName()));
                        Files.createDirectories(backupPath.getParent());
                        Files.copy(fileSelector.getSelectionModel().getSelectedItem().toPath(), backupPath);
                    }   
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }

                    byte[] newFileData = BUTMain.buildNewFile(fileSelector.getSelectionModel().getSelectedItem(), units);
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setInitialFileName(fileSelector.getSelectionModel().getSelectedItem().getName());
                    if(fileSelector.getSelectionModel().getSelectedItem().getName().equals("main.dol") || fileSelector.getSelectionModel().getSelectedItem().getName().equals("Start.dol"))
                    {
                        FileChooser.ExtensionFilter dolFilter = new FileChooser.ExtensionFilter("Gamecube Main Executable File Format (*.dol)", "*.dol");
                        fileChooser.getExtensionFilters().addAll(dolFilter);
                    }
                    else
                    {
                        FileChooser.ExtensionFilter relFilter = new FileChooser.ExtensionFilter("Gamecube Relocatable Executable File Format (*.rel)", "*.rel");
                        fileChooser.getExtensionFilters().addAll(relFilter);
                    }
                    fileChooser.setTitle("Save As");
                    fileChooser.setInitialDirectory(givenFile);

                    File dest = fileChooser.showSaveDialog(window);
                    if (dest != null) 
                    {
                        try 
                        {
                            FileOutputStream fos = new FileOutputStream(dest);
                            fos.write(newFileData);
                            fos.close();

                            Stage successBox = new Stage();
                            successBox.setTitle("Export");
                            successBox.getIcons().add(images.get("unit"));

                            VBox successMenu = new VBox();
                            Text message = new Text("Successfully Saved!");
                            message.setWrappingWidth(290);
                            message.setTextAlignment(TextAlignment.CENTER);
                            successMenu.getChildren().addAll(new Label(""), message);

                            StackPane successPane = new StackPane();
                            successPane.getChildren().add(successMenu);
                            successPane.setAlignment(Pos.CENTER);

                            Scene successScene = new Scene(successPane, 150, 50);

                            successBox.setScene(successScene);
                            successBox.initModality(Modality.APPLICATION_MODAL);
                            successBox.show();

                            test.testUnitData(units);
                        } 
                        catch (IOException ex) 
                        {
                            System.out.println("There was an error creating the output file");
                        }
                    }
                }
                else
                {
                    showOpenFileWindow();
                }
            }
        });

        presetsButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                if(isFileOpened)
                {
                    presetBox = new Stage();
                    presetBox.setTitle("Preset Options");
                    presetBox.getIcons().add(images.get("unit"));

                    GridPane presetsForm = new GridPane();
                    presetsForm.setVgap(10);
                    presetsForm.setHgap(10);
                    presetsForm.setPadding(new Insets(10));
                    presetsForm.setAlignment(Pos.CENTER);

                    presetsForm.add(saveFilePresetButton, 0, 0);
                    presetsForm.add(loadFilePresetButton, 0, 1);

                    StackPane presetPane = new StackPane();
                    presetPane.getChildren().add(presetsForm);
                    presetPane.setAlignment(Pos.CENTER);

                    Scene presetScene = new Scene(presetPane, 250, 150);

                    presetBox.setScene(presetScene);
                    presetBox.initModality(Modality.APPLICATION_MODAL);
                    presetBox.show();
                }
                else
                {
                    showOpenFileWindow();
                }
            }
        });

        saveFilePresetButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                presetBox.close();

                if(isFileOpened)
                {
                    byte[] presetFileData = null;
                    FileChooser presetFileChooser = new FileChooser();
                    String[] fileName = fileSelector.getSelectionModel().getSelectedItem().getName().split("\\.");

                    presetFileData = PresetUtils.buildPresetFile(givenFile, units);
                    presetFileChooser.setInitialFileName(fileName[0]);
                    FileChooser.ExtensionFilter bupFilter = new FileChooser.ExtensionFilter("Battle Unit Preset File Format (*.bup)", "*.bup");
                    presetFileChooser.getExtensionFilters().addAll(bupFilter);
                    presetFileChooser.setTitle("Save As");

                    File dest = presetFileChooser.showSaveDialog(window);
                    if (dest != null) 
                    {
                        try 
                        {
                            FileOutputStream fos = new FileOutputStream(dest);
                            fos.write(presetFileData);
                            fos.close();

                            Stage successBox = new Stage();
                            successBox.setTitle("Export");
                            successBox.getIcons().add(images.get("unit"));

                            VBox successMenu = new VBox();
                            Text message = new Text("Successfully Saved!");
                            message.setWrappingWidth(290);
                            message.setTextAlignment(TextAlignment.CENTER);
                            successMenu.getChildren().addAll(new Label(""), message);

                            StackPane successPane = new StackPane();
                            successPane.getChildren().add(successMenu);
                            successPane.setAlignment(Pos.CENTER);

                            Scene successScene = new Scene(successPane, 150, 50);

                            successBox.setScene(successScene);
                            successBox.initModality(Modality.APPLICATION_MODAL);
                            successBox.show();

                            test.testUnitData(units);
                        } 
                        catch (IOException ex) 
                        {
                            System.out.println("There was an error creating the output file");
                        }
                    }
                }
                else
                {
                    showOpenFileWindow();
                }
            }
        });

        loadFilePresetButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                presetBox.close();

                if(isFileOpened)
                {
                    File givenPreset = null;
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Select a Preset File");
                    FileChooser.ExtensionFilter bupFilter = new FileChooser.ExtensionFilter("Battle Unit Preset File Format (*.bup)", "*.bup");
                    fileChooser.getExtensionFilters().addAll(bupFilter);
                    givenPreset = fileChooser.showOpenDialog(window);

                    byte[] givenPresetData = ByteUtils.readData(givenPreset);

                    if(givenPresetData[0] == 0x00)
                    {
                        ArrayList<BUTData> presetUnits = PresetUtils.getPresetData(givenPreset);

                        for(int i = 0; i < presetUnits.size(); i++)
                        {
                            ArrayList<String> attackNames = new ArrayList<String>();
                            for(int j = 0; j < units.get(i).BattleWeaponData.size(); j++)
                            {
                                attackNames.add(units.get(i).BattleWeaponData.get(j).attackName);
                            }

                            units.get(i).BattleUnitKindData = new ArrayList<BattleUnitKind>();
                            units.get(i).BattleUnitKindPartData = new ArrayList<BattleUnitKindPart>();
                            units.get(i).HealthUpgradesData = new ArrayList<HealthUpgrades>();
                            units.get(i).BattleUnitDefenseData = new ArrayList<BattleUnitDefense>();
                            units.get(i).BattleUnitDefenseAttrData = new ArrayList<BattleUnitDefenseAttr>();
                            units.get(i).StatusVulnerabilityData = new ArrayList<StatusVulnerability>();
                            units.get(i).BattleWeaponData = new ArrayList<BattleWeapon>();

                            for(int j = 0; j < presetUnits.get(i).BattleUnitKindData.size(); j++)
                            {
                                units.get(i).addBattleUnitKindStruct(presetUnits.get(i).BattleUnitKindData.get(j));
                            }
                            for(int j = 0; j < presetUnits.get(i).BattleUnitKindPartData.size(); j++)
                            {
                                units.get(i).addBattleUnitKindPartStruct(presetUnits.get(i).BattleUnitKindPartData.get(j));
                            }
                            for(int j = 0; j < presetUnits.get(i).BattleUnitDefenseData.size(); j++)
                            {
                                units.get(i).addBattleUnitDefenseStruct(presetUnits.get(i).BattleUnitDefenseData.get(j));
                            }
                            for(int j = 0; j < presetUnits.get(i).BattleUnitDefenseAttrData.size(); j++)
                            {
                                units.get(i).addBattleUnitDefenseAttrStruct(presetUnits.get(i).BattleUnitDefenseAttrData.get(j));
                            }
                            for(int j = 0; j < presetUnits.get(i).StatusVulnerabilityData.size(); j++)
                            {
                                units.get(i).addStatusVulnerabilityStruct(presetUnits.get(i).StatusVulnerabilityData.get(j));
                            }
                            for(int j = 0; j < presetUnits.get(i).BattleWeaponData.size(); j++)
                            {
                                units.get(i).addBattleWeaponStruct(presetUnits.get(i).BattleWeaponData.get(j));
                                units.get(i).BattleWeaponData.get(j).attackName = attackNames.get(j);
                            }
                            for(int j = 0; j < presetUnits.get(i).HealthUpgradesData.size(); j++)
                            {
                                units.get(i).addHealthUpgradeStruct(presetUnits.get(i).HealthUpgradesData.get(j));
                            }
                        }

                        loadSelectedUnit(unitSelector.getSelectionModel().getSelectedIndex());
                        
                        centerMenu.getChildren().clear();
                        upperBox.getChildren().clear();
                        upperBox2.getChildren().clear();
                        upperBox.getChildren().add(unitSelector);
                        upperBox2.getChildren().add(new Label(""));
                        
                        showPresetSuccessWindow();
                    }
                    else
                    {
                        showGiveValidFileWindow();
                    }
                }
                else
                {
                    showOpenFileWindow();
                }
            }
        });

        updateTattlesButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                if(isFileOpened)
                {
                    File givenGlobal = null;
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Select global.txt");
                    FileChooser.ExtensionFilter textFilter = new FileChooser.ExtensionFilter("Normal Text File Format (*.txt)", "*.txt");
                    fileChooser.getExtensionFilters().addAll(textFilter);
                    fileChooser.setInitialDirectory(givenFile);
                    givenGlobal = fileChooser.showOpenDialog(window);

                    try
                    {
                        java.nio.file.Path backupPath = Paths.get("backup", (LocalDateTime.now().format(DateTimeFormatter.ofPattern("MM-dd-yyyy HH-mm-ss")) + " " + givenGlobal.getName()));
                        Files.createDirectories(backupPath.getParent());
                        Files.copy(givenGlobal.toPath(), backupPath);
                    }   
                    catch(IOException e)
                    {
                        e.printStackTrace();
                    }

                    byte[] newGlobalData = TattleUtils.buildNewTattleFile(givenGlobal, givenFile, units);

                    FileChooser globalFileSaver = new FileChooser();
                    globalFileSaver.setInitialFileName(givenGlobal.getName());
                    globalFileSaver.getExtensionFilters().addAll(textFilter);
                    globalFileSaver.setTitle("Save As");
                    globalFileSaver.setInitialDirectory(givenFile);

                    File dest = globalFileSaver.showSaveDialog(window);
                    if (dest != null) 
                    {
                        try 
                        {
                            FileOutputStream fos = new FileOutputStream(dest);
                            fos.write(newGlobalData);
                            fos.close();

                            Stage successBox = new Stage();
                            successBox.setTitle("Export");
                            successBox.getIcons().add(images.get("unit"));

                            VBox successMenu = new VBox();
                            Text message = new Text("Successfully Saved!");
                            message.setWrappingWidth(290);
                            message.setTextAlignment(TextAlignment.CENTER);
                            successMenu.getChildren().addAll(new Label(""), message);

                            StackPane successPane = new StackPane();
                            successPane.getChildren().add(successMenu);
                            successPane.setAlignment(Pos.CENTER);

                            Scene successScene = new Scene(successPane, 150, 50);

                            successBox.setScene(successScene);
                            successBox.initModality(Modality.APPLICATION_MODAL);
                            successBox.show();
                        } 
                        catch (IOException ex) 
                        {
                            System.out.println("There was an error creating the output file");
                        }
                    }
                }
                else
                {
                    showOpenFileWindow();
                }
            }
        });

        closeButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                VBox emptyLeft = new VBox();
                borderPane.setLeft(emptyLeft);
                HBox emptyCenter = new HBox();
                borderPane.setCenter(emptyCenter);
                fileSelector.getSelectionModel().clearSelection();

                isFileOpened = false;
            }
        });

        optionButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                Stage optionsBox = new Stage();
                optionsBox.setTitle("Options");
                optionsBox.getIcons().add(images.get("unit"));

                CheckBox lastFolderBox = new CheckBox();
                CheckBox redFieldBox = new CheckBox();
                Button saveOptionsButton = new Button("Save Options");

                try
                {
                    File jsonFile = new File("src\\options.json");
                    JSONParser parser = new JSONParser();
                    JSONObject root = (JSONObject)parser.parse(new FileReader(jsonFile));

                    if(((String)root.get("BUTLastFolder")).equals("true")) lastFolderBox.setSelected(true); else lastFolderBox.setSelected(false);
                    if(((String)root.get("BUTRedFields")).equals("true")) redFieldBox.setSelected(true); else redFieldBox.setSelected(false);
                }
                catch (FileNotFoundException e){System.out.println("There was an Error Finding the JSON File");}
                catch (IOException e){System.out.println("There was an Error Reading the JSON File");}
                catch (ParseException e){System.out.println("There was an Error Parsing the JSON File");}

                GridPane optionsForm = new GridPane();
                optionsForm.setVgap(10);
                optionsForm.setHgap(10);
                optionsForm.setPadding(new Insets(10));
                optionsForm.setAlignment(Pos.CENTER);

                optionsForm.add(unitImageViewCreator(images.get("cog")), 0, 0);
                optionsForm.add(new Label("Remember Last Opened Folder"), 1, 0);
                optionsForm.add(lastFolderBox, 2, 0);
                optionsForm.add(unitImageViewCreator(images.get("cog")), 0, 1);
                optionsForm.add(new Label("Highlight 0's Red"), 1, 1);
                optionsForm.add(redFieldBox, 2, 1);

                VBox optionsVBox = new VBox();
                optionsVBox.setAlignment(Pos.CENTER);
                optionsVBox.setSpacing(10);
                optionsVBox.getChildren().addAll(optionsForm, saveOptionsButton);

                StackPane optionsPane = new StackPane();
                optionsPane.getChildren().add(optionsVBox);
                optionsPane.setAlignment(Pos.CENTER);

                Scene optionsScene = new Scene(optionsPane, 250, 200);

                optionsBox.setScene(optionsScene);
                optionsBox.initModality(Modality.APPLICATION_MODAL);
                optionsBox.show();

                saveOptionsButton.setOnAction(new EventHandler<ActionEvent>() 
                {
                    @Override public void handle(ActionEvent event)
                    {
                        try
                        {
                            File jsonFile = new File("src\\options.json");
                            JSONParser parser = new JSONParser();
                            JSONObject root = (JSONObject)parser.parse(new FileReader(jsonFile));

                            if(lastFolderBox.isSelected()) root.put("BUTLastFolder", "true");
                            else root.put("BUTLastFolder", "false");

                            if(redFieldBox.isSelected()) root.put("BUTRedFields", "true");
                            else root.put("BUTRedFields", "false");

                            Gson gson = new GsonBuilder().setPrettyPrinting().create();
                            Object asJson = gson.fromJson(root.toJSONString(), Object.class);
                            try (FileWriter writer = new FileWriter(jsonFile)) 
                            {
                                gson.toJson(asJson, writer);
                            }
                        }
                        catch (FileNotFoundException e){System.out.println("There was an Error Finding the JSON File");}
                        catch (IOException e){System.out.println("There was an Error Reading the JSON File");}
                        catch (ParseException e){System.out.println("There was an Error Parsing the JSON File");}

                        if(redFieldBox.isSelected()) setRed0TextFieldFormats(true);
                        else setRed0TextFieldFormats(false);

                        optionsBox.close();
                    }
                });
            }
        });

        aboutButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                Stage alertBox = new Stage();
                alertBox.setTitle("About");
                alertBox.getIcons().add(images.get("unit"));

                VBox alertMenu = new VBox();
                alertMenu.setAlignment(Pos.CENTER);
                Text versionText = new Text("Magical Map Version: " + GUI.version);
                versionText.setWrappingWidth(290);
                versionText.setTextAlignment(TextAlignment.CENTER);
                Text creditText = new Text("Battle Unit Tool Written by Jemaroo");
                creditText.setWrappingWidth(290);
                creditText.setTextAlignment(TextAlignment.CENTER);
                Text description = new Text("Battle Unit Tool allows you to open up the game's main dol or any rel file containing battle data and edit enemy and party unit fields.");
                description.setWrappingWidth(290);
                description.setTextAlignment(TextAlignment.CENTER);
                alertMenu.getChildren().addAll(new Label(""), versionText, creditText, new Label(""), description);

                StackPane alertPane = new StackPane();
                alertPane.getChildren().add(alertMenu);
                alertPane.setAlignment(Pos.CENTER);

                Scene alertScene = new Scene(alertPane, 350, 150);

                alertBox.setScene(alertScene);
                alertBox.initModality(Modality.APPLICATION_MODAL);
                alertBox.show();
            }
        });

        window.getIcons().add(images.get("unit"));
        window.show();
    }

    /**
     * @Author Jemaroo
     * @Function Opens the directory and sets up the correct fields
     */
    private void loadGUIMenus()
    {
        ArrayList<File> validFiles = BUTMain.findMatchingFiles(givenFile, validFileNames);

        fileSelector.getItems().clear();
        topMenu.getChildren().clear();
        topMenu.getChildren().addAll(openButton, fileSelector, exportButton, presetsButton, updateTattlesButton, closeButton, optionButton, aboutButton);

        validFiles.sort((fileA, fileB) -> 
        {
            Integer fileAPriority = filePriority.get(fileA.getName());
            Integer fileBPriority = filePriority.get(fileB.getName());

            if (fileAPriority != null && fileBPriority != null) return Integer.compare(fileAPriority, fileBPriority);
            if (fileAPriority != null) return -1;
            if (fileBPriority != null) return 1;

            return fileA.getName().compareToIgnoreCase(fileB.getName());
        });

        for (File f : validFiles)
        {
            fileSelector.getItems().add(f);
        }

        //Setting list names for Directory Chooser
        fileSelector.setCellFactory(lv -> new ListCell<File>() 
        {
            @Override protected void updateItem(File item, boolean empty) 
            {
                super.updateItem(item, empty);
                if (empty || item == null) 
                {
                    setText(null);
                    setGraphic(null);
                } 
                else 
                {
                    setText(fileNameSelector(item.getName()));
                    setGraphic(fileImageSelector(item.getName()));
                }
            }
        });
        fileSelector.setButtonCell(new ListCell<File>() 
        {
            @Override protected void updateItem(File item, boolean empty) 
            {
                super.updateItem(item, empty);

                if (empty || item == null) 
                {
                    setText(null);
                    setGraphic(null);
                } 
                else 
                {
                    setText(item.getName());
                    setGraphic(fileImageSelector(item.getName()));
                }
            }
        });

        fileSelector.setOnAction(e -> 
        {
            centerMenu.getChildren().clear();

            units = BUTMain.getTableData(fileSelector.getSelectionModel().getSelectedItem());
            isFileOpened = true;
            test.testUnitData(units);

            VBox leftMenu = new VBox();
            unitSelector = new ComboBox<>();
            for (BUTData unit : units) 
            {
                unitSelector.getItems().add(unit);
            }
            unitSelector.setMaxWidth(130);

            unitSelector.setCellFactory(lv -> new ListCell<BUTData>()
            {
                @Override protected void updateItem(BUTData unit, boolean empty)
                {
                    super.updateItem(unit, empty);

                    if(empty || unit == null)
                    {
                        setText(null);
                        setGraphic(null);
                    }
                    else
                    {
                        setText(unit.name);
                        setGraphic(unitImageSelector(unit.name));
                    }
                }
            });
            unitSelector.setButtonCell(new ListCell<BUTData>() 
            {
                @Override protected void updateItem(BUTData unit, boolean empty) 
                {
                    super.updateItem(unit, empty);

                    if (empty || unit == null) 
                    {
                        setText(null);
                        setGraphic(null);
                    } 
                    else 
                    {
                        setText(unit.name);
                        setGraphic(unitImageSelector(unit.name));
                    }
                }
            });

            //Setting struct names for BattleUnitKind
            kindList = new ListView<>();
            kindList.setCellFactory(lv -> new ListCell<Object>() 
            {
                @Override protected void updateItem(Object item, boolean empty) 
                {
                    super.updateItem(item, empty);
                    if (empty || item == null) 
                    {
                        setText(null);
                        setGraphic(null);
                    } 
                    else 
                    {
                        int count = 0;
                        if (getListView().getItems() != null) 
                        {
                            for (int i = 0; i < getIndex(); i++) 
                            {
                                Object previousItem = getListView().getItems().get(i);
                                if (item instanceof BattleUnitKind && previousItem instanceof BattleUnitKind) 
                                {
                                    count++;
                                } 
                                else if (item instanceof BattleUnitKindPart && previousItem instanceof BattleUnitKindPart) 
                                {
                                    count++;
                                }
                                else if (item instanceof HealthUpgrades && previousItem instanceof HealthUpgrades) 
                                {
                                    count++;
                                }
                            }
                        }

                        if(item instanceof BattleUnitKind)
                        {
                            setText("Kind Struct " + (count + 1));
                            setGraphic(unitImageViewCreator(images.get("unit")));
                        }
                        else if(item instanceof BattleUnitKindPart)
                        {
                            setText("Part Struct " + (count + 1));
                            setGraphic(unitImageViewCreator(images.get("unusedPaper")));
                        }
                        else if(item instanceof HealthUpgrades)
                        {
                            setText("Stat Upgrades");
                            setGraphic(unitImageViewCreator(images.get("shineSprite")));
                        }
                    }
                }
            });

            //Setting struct names for BattleUnitDefense, BattleUnitDefenseAttr, and StatusVulnerability
            defenseList = new ListView<>();
            defenseList.setCellFactory(lv -> new ListCell<Object>() 
            {
                @Override protected void updateItem(Object item, boolean empty) 
                {
                    super.updateItem(item, empty);
                    if (empty || item == null) 
                    {
                        setText(null);
                        setGraphic(null);
                    } 
                    else 
                    {
                        int count = 0;
                        if (getListView().getItems() != null) 
                        {
                            for (int i = 0; i < getIndex(); i++) 
                            {
                                Object previousItem = getListView().getItems().get(i);
                                if (item instanceof BattleUnitDefense && previousItem instanceof BattleUnitDefense) 
                                {
                                    count++;
                                } 
                                else if (item instanceof BattleUnitDefenseAttr && previousItem instanceof BattleUnitDefenseAttr) 
                                {
                                    count++;
                                }
                                else if (item instanceof StatusVulnerability && previousItem instanceof StatusVulnerability) 
                                {
                                    count++;
                                }
                            }
                        }

                        if(item instanceof BattleUnitDefense)
                        {
                            setText("Defense Struct " + (count + 1));
                            setGraphic(unitImageViewCreator(images.get("defendPlus")));
                        }
                        else if(item instanceof BattleUnitDefenseAttr)
                        {
                            setText("Defense Attribute Struct " + (count + 1));
                            setGraphic(unitImageViewCreator(images.get("spikeShieldCustom")));
                        }
                        else if(item instanceof StatusVulnerability)
                        {
                            setText("Status Vulnerability Struct " + (count + 1));
                            setGraphic(unitImageViewCreator(images.get("feelingFine")));
                        }
                    }
                }
            });

            //Setting struct names for BattleWeapon
            weaponList = new ListView<>();
            weaponList.setCellFactory(lv -> new ListCell<BattleWeapon>() 
            {
                @Override protected void updateItem(BattleWeapon item, boolean empty) 
                {
                    super.updateItem(item, empty);
                    if (empty || item == null) 
                    {
                        setText(null);
                        setGraphic(null);
                    } 
                    else 
                    {
                        setText(item.attackName);
                        setGraphic(attackNameImageSelector(item.attackName));
                    }
                }
            });

            GridPane leftMenuForm = new GridPane();
            upperBox.setPadding(new Insets(3));
            upperBox.getChildren().clear();
            upperBox.getChildren().add(unitSelector);
            upperBox.setSpacing(5);
            upperBox.setAlignment(Pos.CENTER_LEFT);
            leftMenuForm.add(upperBox, 0, 0);

            upperBox2 = new HBox();
            upperBox2.setPadding(new Insets(3));
            upperBox2.setSpacing(3);
            upperBox2.getChildren().clear();
            upperBox2.getChildren().add(new Label(""));
            upperBox2.setAlignment(Pos.CENTER);
            leftMenuForm.add(upperBox2, 0, 1);

            HBox baseBox = new HBox();
            baseBox.setPadding(new Insets(3));
            baseBox.getChildren().add(new Label("Base:"));
            leftMenuForm.add(baseBox, 0, 2);
            leftMenuForm.add(kindList, 0, 3);

            HBox defenseBox = new HBox();
            defenseBox.setPadding(new Insets(3));
            defenseBox.getChildren().add(new Label("Defense:"));
            leftMenuForm.add(defenseBox, 0, 4);
            leftMenuForm.add(defenseList, 0, 5);

            HBox weaponBox = new HBox();
            weaponBox.setPadding(new Insets(3));
            weaponBox.getChildren().add(new Label("Attacks:"));
            leftMenuForm.add(weaponBox, 0, 6);
            leftMenuForm.add(weaponList, 0, 7);
            
            borderPane.setLeft(leftMenuForm);

            GridPane form = new GridPane();

            unitSelector.setOnAction(e2 -> 
            {
                centerMenu.getChildren().clear();
                upperBox.getChildren().clear();
                upperBox2.getChildren().clear();
                upperBox.getChildren().add(unitSelector);
                upperBox2.getChildren().add(new Label(""));
                loadSelectedUnit(unitSelector.getSelectionModel().getSelectedIndex());
            });

            saveChangesButton.setOnAction(new EventHandler<ActionEvent>() 
            {
                @Override public void handle(ActionEvent event)
                {
                    saveFieldsToSelectedStruct();
                    saveChangesButton.setText("    Changes Saved     ");
                }
            });

            saveStructPresetButton.setOnAction(new EventHandler<ActionEvent>() 
            {
                @Override public void handle(ActionEvent event)
                {
                    Object selected = null;
                    byte[] presetFileData = null;
                    FileChooser presetFileChooser = new FileChooser();

                    if (kindList.getSelectionModel().getSelectedItem() != null) 
                    {
                        selected = kindList.getSelectionModel().getSelectedItem();
                    } 
                    else if (defenseList.getSelectionModel().getSelectedItem() != null) 
                    {
                        selected = defenseList.getSelectionModel().getSelectedItem();
                    } 
                    else if (weaponList.getSelectionModel().getSelectedItem() != null)
                    {
                        selected = weaponList.getSelectionModel().getSelectedItem();
                    }

                    if (selected instanceof BattleUnitKind b) 
                    {
                        presetFileData = PresetUtils.buildPresetFile(givenFile, b);
                        presetFileChooser.setInitialFileName(unitSelector.getSelectionModel().getSelectedItem().name + kindList.getSelectionModel().getSelectedItem().toString());
                    }
                    else if (selected instanceof BattleUnitKindPart b) 
                    {
                        presetFileData = PresetUtils.buildPresetFile(givenFile, b);
                        presetFileChooser.setInitialFileName(unitSelector.getSelectionModel().getSelectedItem().name + kindList.getSelectionModel().getSelectedItem().toString());
                    }
                    else if (selected instanceof HealthUpgrades b) 
                    {
                        presetFileData = PresetUtils.buildPresetFile(givenFile, b);
                        presetFileChooser.setInitialFileName(unitSelector.getSelectionModel().getSelectedItem().name + kindList.getSelectionModel().getSelectedItem().toString());
                    }
                    else if (selected instanceof BattleUnitDefense b) 
                    {
                        presetFileData = PresetUtils.buildPresetFile(givenFile, b);
                        presetFileChooser.setInitialFileName(unitSelector.getSelectionModel().getSelectedItem().name + defenseList.getSelectionModel().getSelectedItem().toString());
                    }
                    else if (selected instanceof BattleUnitDefenseAttr b) 
                    {
                        presetFileData = PresetUtils.buildPresetFile(givenFile, b);
                        presetFileChooser.setInitialFileName(unitSelector.getSelectionModel().getSelectedItem().name + defenseList.getSelectionModel().getSelectedItem().toString());
                    }
                    else if (selected instanceof StatusVulnerability b) 
                    {
                        presetFileData = PresetUtils.buildPresetFile(givenFile, b);
                        presetFileChooser.setInitialFileName(unitSelector.getSelectionModel().getSelectedItem().name + defenseList.getSelectionModel().getSelectedItem().toString());
                    }
                    else if (selected instanceof BattleWeapon b) 
                    {
                        presetFileData = PresetUtils.buildPresetFile(givenFile, b);
                        presetFileChooser.setInitialFileName(unitSelector.getSelectionModel().getSelectedItem().name + b.attackName.replace(" ", "") + weaponList.getSelectionModel().getSelectedItem().toString());
                    }

                    FileChooser.ExtensionFilter bupFilter = new FileChooser.ExtensionFilter("Battle Unit Preset File Format (*.bup)", "*.bup");
                    presetFileChooser.getExtensionFilters().addAll(bupFilter);
                    presetFileChooser.setTitle("Save As");
                    presetFileChooser.setInitialDirectory(givenFile);

                    File dest = presetFileChooser.showSaveDialog(window);
                    if (dest != null) 
                    {
                        try 
                        {
                            FileOutputStream fos = new FileOutputStream(dest);
                            fos.write(presetFileData);
                            fos.close();

                            Stage successBox = new Stage();
                            successBox.setTitle("Export");
                            successBox.getIcons().add(images.get("unit"));

                            VBox successMenu = new VBox();
                            Text message = new Text("Successfully Saved!");
                            message.setWrappingWidth(290);
                            message.setTextAlignment(TextAlignment.CENTER);
                            successMenu.getChildren().addAll(new Label(""), message);

                            StackPane successPane = new StackPane();
                            successPane.getChildren().add(successMenu);
                            successPane.setAlignment(Pos.CENTER);

                            Scene successScene = new Scene(successPane, 150, 50);

                            successBox.setScene(successScene);
                            successBox.initModality(Modality.APPLICATION_MODAL);
                            successBox.show();

                            test.testUnitData(units);
                        } 
                        catch (IOException ex) 
                        {
                            System.out.println("There was an error creating the output file");
                        }
                    }
                }
            });

            loadStructPresetButton.setOnAction(new EventHandler<ActionEvent>() 
            {
                @Override public void handle(ActionEvent event)
                {
                    File givenPreset = null;
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Select a Preset File");
                    FileChooser.ExtensionFilter bupFilter = new FileChooser.ExtensionFilter("Battle Unit Preset File Format (*.bup)", "*.bup");
                    fileChooser.getExtensionFilters().addAll(bupFilter);
                    givenPreset = fileChooser.showOpenDialog(window);

                    byte[] givenPresetData = ByteUtils.readData(givenPreset);

                    Object selected = null;
                    if (kindList.getSelectionModel().getSelectedItem() != null) 
                    {
                        selected = kindList.getSelectionModel().getSelectedItem();
                    } 
                    else if (defenseList.getSelectionModel().getSelectedItem() != null) 
                    {
                        selected = defenseList.getSelectionModel().getSelectedItem();
                    } 
                    else if (weaponList.getSelectionModel().getSelectedItem() != null)
                    {
                        selected = weaponList.getSelectionModel().getSelectedItem();
                    }

                    if(givenPresetData[0] == 0x00)
                    {
                        showGiveValidFileWindow();
                    }
                    else if (selected instanceof BattleUnitKind b) 
                    {
                        if(givenPresetData[0] == 0x01)
                        {
                            ArrayList<BUTData> tempPreset = PresetUtils.getPresetData(givenPreset);
                            savePresetFieldsToSelectedStruct(tempPreset.get(0).BattleUnitKindData.get(0));
                            loadStructFields(selected);
                            showPresetSuccessWindow();
                        }
                        else
                        {
                            showGiveValidFileWindow();
                        }
                    }
                    else if (selected instanceof BattleUnitKindPart b) 
                    {
                        if(givenPresetData[0] == 0x02)
                        {
                            ArrayList<BUTData> tempPreset = PresetUtils.getPresetData(givenPreset);
                            savePresetFieldsToSelectedStruct(tempPreset.get(0).BattleUnitKindPartData.get(0));
                            loadStructFields(selected);
                            showPresetSuccessWindow();
                        }
                        else
                        {
                            showGiveValidFileWindow();
                        }
                    }
                    else if (selected instanceof HealthUpgrades b) 
                    {
                        if(givenPresetData[0] == 0x07)
                        {
                            ArrayList<BUTData> tempPreset = PresetUtils.getPresetData(givenPreset);
                            savePresetFieldsToSelectedStruct(tempPreset.get(0).HealthUpgradesData.get(0));
                            loadStructFields(selected);
                            showPresetSuccessWindow();
                        }
                        else
                        {
                            showGiveValidFileWindow();
                        }
                    }
                    else if (selected instanceof BattleUnitDefense b) 
                    {
                        if(givenPresetData[0] == 0x03)
                        {
                            ArrayList<BUTData> tempPreset = PresetUtils.getPresetData(givenPreset);
                            savePresetFieldsToSelectedStruct(tempPreset.get(0).BattleUnitDefenseData.get(0));
                            loadStructFields(selected);
                            showPresetSuccessWindow();
                        }
                        else
                        {
                            showGiveValidFileWindow();
                        }
                    }
                    else if (selected instanceof BattleUnitDefenseAttr b) 
                    {
                        if(givenPresetData[0] == 0x04)
                        {
                            ArrayList<BUTData> tempPreset = PresetUtils.getPresetData(givenPreset);
                            savePresetFieldsToSelectedStruct(tempPreset.get(0).BattleUnitDefenseAttrData.get(0));
                            loadStructFields(selected);
                            showPresetSuccessWindow();
                        }
                        else
                        {
                            showGiveValidFileWindow();
                        }
                    }
                    else if (selected instanceof StatusVulnerability b) 
                    {
                        if(givenPresetData[0] == 0x05)
                        {
                            ArrayList<BUTData> tempPreset = PresetUtils.getPresetData(givenPreset);
                            savePresetFieldsToSelectedStruct(tempPreset.get(0).StatusVulnerabilityData.get(0));
                            loadStructFields(selected);
                            showPresetSuccessWindow();
                        }
                        else
                        {
                            showGiveValidFileWindow();
                        }
                    }
                    else if (selected instanceof BattleWeapon b) 
                    {
                        if(givenPresetData[0] == 0x06)
                        {
                            ArrayList<BUTData> tempPreset = PresetUtils.getPresetData(givenPreset);
                            savePresetFieldsToSelectedStruct(tempPreset.get(0).BattleWeaponData.get(0));
                            loadStructFields(selected);
                            showPresetSuccessWindow();
                        }
                        else
                        {
                            showGiveValidFileWindow();
                        }
                    }
                }
            });

            kindList.setOnMouseClicked(e2 -> 
            {                    
                centerMenu.getChildren().clear();
                form.getChildren().clear();
                defenseList.getSelectionModel().clearSelection();
                weaponList.getSelectionModel().clearSelection();

                upperBox.getChildren().clear();
                upperBox.getChildren().addAll(unitSelector, saveChangesButton);

                upperBox2.getChildren().clear();
                upperBox2.getChildren().addAll(saveStructPresetButton, loadStructPresetButton);

                if(kindList.getSelectionModel().getSelectedItem() instanceof BattleUnitKind)
                {
                    comboBoxFields[0].getItems().clear();
                    comboBoxFields[0].getItems().addAll("None", "Swallow Bomb", "Spit into Other Enemy", "Spit Fireball", "Swallow Part of Enemy");

                    BattleUnitKind selected = (BattleUnitKind) kindList.getSelectionModel().getSelectedItem();

                    if (selected != null) loadStructFields(selected);

                    saveChangesButton.setText("Save Struct Changes");

                    form.setVgap(5);
                    form.setHgap(10);
                    form.setPadding(new Insets(10));

                    form.add(fieldImageViewCreator(images.get("heart")), 0, 0);
                    form.add(labelANDTooltipMaker("HP:", 1, 1), 1, 0);
                    form.add(textFields[0], 2, 0);
                    form.add(fieldImageViewCreator(images.get("dangerHeartCustom")), 0, 1);
                    form.add(labelANDTooltipMaker("Danger HP:", 1, 2), 1, 1);
                    form.add(textFields[1], 2, 1);
                    form.add(fieldImageViewCreator(images.get("dangerHeartCustom")), 0, 2);
                    form.add(labelANDTooltipMaker("Peril HP:", 1, 3), 1, 2);
                    form.add(textFields[2], 2, 2);
                    form.add(fieldImageViewCreator(images.get("audienceStar")), 0, 3);
                    form.add(labelANDTooltipMaker("Level:", 1, 4), 1, 3);
                    form.add(textFields[3], 2, 3);
                    form.add(fieldImageViewCreator(images.get("XP")), 0, 4);
                    form.add(labelANDTooltipMaker("Bonus XP:", 1, 5), 1, 4);
                    form.add(textFields[4], 2, 4);
                    form.add(fieldImageViewCreator(images.get("coin")), 0, 5);
                    form.add(labelANDTooltipMaker("Bonus Coin:", 1, 6), 1, 5);
                    form.add(textFields[5], 2, 5);
                    form.add(fieldImageViewCreator(images.get("coin")), 0, 6);
                    form.add(labelANDTooltipMaker("Bonus Coin Rate:", 1, 7), 1, 6);
                    form.add(textFields[6], 2, 6);
                    form.add(fieldImageViewCreator(images.get("coin")), 0, 7);
                    form.add(labelANDTooltipMaker("Base Coin:", 1, 8), 1, 7);
                    form.add(textFields[7], 2, 7);
                    form.add(fieldImageViewCreator(images.get("runArrow")), 0, 8);
                    form.add(labelANDTooltipMaker("Run Rate:", 1, 9), 1, 8);
                    form.add(textFields[8], 2, 8);
                    form.add(fieldImageViewCreator(images.get("powerBounce")), 0, 9);
                    form.add(labelANDTooltipMaker("PB Cap:", 1, 10), 1, 9);
                    form.add(textFields[9], 2, 9);
                    form.add(fieldImageViewCreator(images.get("yoshiPartnerSwitch")), 0, 10);
                    form.add(labelANDTooltipMaker("Swallow Chance:", 1, 11), 1, 10);
                    form.add(textFields[10], 2, 10);
                    form.add(fieldImageViewCreator(images.get("yoshiPartnerSwitch")), 0, 11);
                    form.add(labelANDTooltipMaker("Swallow Attribute:", 1, 12), 1, 11);
                    form.add(comboBoxFields[0], 2, 11);
                    form.add(fieldImageViewCreator(images.get("ultraHammer")), 0, 12);
                    form.add(labelANDTooltipMaker("Hammer Knockback Chance:", 1, 13), 1, 12);
                    form.add(textFields[11], 2, 12);
                    form.add(fieldImageViewCreator(images.get("mowzPartnerSwitch")), 0, 13);
                    form.add(labelANDTooltipMaker("Item Steal Difficulty:", 1, 14), 1, 13);
                    form.add(textFields[12], 2, 13);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 14);
                    form.add(labelANDTooltipMaker("Is a Map Object:", 1, 15), 1, 14);
                    form.add(checkBoxFields[0], 2, 14);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 15);
                    form.add(labelANDTooltipMaker("Out of Reach:", 1, 16), 1, 15);
                    form.add(checkBoxFields[1], 2, 15);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 16);
                    form.add(labelANDTooltipMaker("Is Unquakeable:", 1, 17), 1, 16);
                    form.add(checkBoxFields[2], 2, 16);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 17);
                    form.add(labelANDTooltipMaker("Is Invisible:", 1, 18), 1, 17);
                    form.add(checkBoxFields[3], 2, 17);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 18);
                    form.add(labelANDTooltipMaker("Is Veiled:", 1, 19), 1, 18);
                    form.add(checkBoxFields[4], 2, 18);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 19);
                    form.add(labelANDTooltipMaker("Is Shell Shielded:", 1, 20), 1, 19);
                    form.add(checkBoxFields[5], 2, 19);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 20);
                    form.add(labelANDTooltipMaker("Never Targetable:", 1, 21), 1, 20);
                    form.add(checkBoxFields[6], 2, 20);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 21);
                    form.add(labelANDTooltipMaker("Limit Switch:", 1, 22), 1, 21);
                    form.add(checkBoxFields[7], 2, 21);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 22);
                    form.add(labelANDTooltipMaker("Disable Zero Gravity Floating:", 1, 23), 1, 22);
                    form.add(checkBoxFields[8], 2, 22);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 23);
                    form.add(labelANDTooltipMaker("Disable Zero Gravity Immobility:", 1, 24), 1, 23);
                    form.add(checkBoxFields[9], 2, 23);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 24);
                    form.add(labelANDTooltipMaker("Immune to Spin Hammer Knockback:", 1, 25), 1, 24);
                    form.add(checkBoxFields[10], 2, 24);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 25);
                    form.add(labelANDTooltipMaker("Is Undead:", 1, 26), 1, 25);
                    form.add(checkBoxFields[11], 2, 25);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 26);
                    form.add(labelANDTooltipMaker("Is Corpse:", 1, 27), 1, 26);
                    form.add(checkBoxFields[12], 2, 26);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 27);
                    form.add(labelANDTooltipMaker("Is Leader:", 1, 28), 1, 27);
                    form.add(checkBoxFields[13], 2, 27);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 28);
                    form.add(labelANDTooltipMaker("Cannot Take Actions:", 1, 29), 1, 28);
                    form.add(checkBoxFields[14], 2, 28);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 29);
                    form.add(labelANDTooltipMaker("Not Spun by Love Slap:", 1, 30), 1, 29);
                    form.add(checkBoxFields[15], 2, 29);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 30);
                    form.add(labelANDTooltipMaker("Disable Damage Stars:", 1, 31), 1, 30);
                    form.add(checkBoxFields[16], 2, 30);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 31);
                    form.add(labelANDTooltipMaker("Disable All Part Visibility:", 1, 32), 1, 31);
                    form.add(checkBoxFields[17], 2, 31);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 32);
                    form.add(labelANDTooltipMaker("Disable HP Gauge:", 1, 33), 1, 32);
                    form.add(checkBoxFields[18], 2, 32);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 33);
                    form.add(labelANDTooltipMaker("Look Camera:", 1, 34), 1, 33);
                    form.add(checkBoxFields[19], 2, 33);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 34);
                    form.add(labelANDTooltipMaker("Is Non-Combatant:", 1, 35), 1, 34);
                    form.add(checkBoxFields[20], 2, 34);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 35);
                    form.add(labelANDTooltipMaker("No Shadow:", 1, 36), 1, 35);
                    form.add(checkBoxFields[21], 2, 35);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 36);
                    form.add(labelANDTooltipMaker("Disable Damage:", 1, 37), 1, 36);
                    form.add(checkBoxFields[22], 2, 36);

                    centerMenu.getChildren().add(form);
                    ScrollPane centerScroll = new ScrollPane();
                    centerScroll.setContent(centerMenu);
                    borderPane.setCenter(centerScroll);
                }
                else if(kindList.getSelectionModel().getSelectedItem() instanceof BattleUnitKindPart)
                {
                    BattleUnitKindPart selected = (BattleUnitKindPart) kindList.getSelectionModel().getSelectedItem();

                    if (selected != null) loadStructFields(selected);

                    saveChangesButton.setText("Save Struct Changes");

                    form.setVgap(5);
                    form.setHgap(10);
                    form.setPadding(new Insets(10));
                    
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 0);
                    form.add(labelANDTooltipMaker("Main Body Part:", 2, 1), 1, 0);
                    form.add(checkBoxFields[0], 2, 0);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 1);
                    form.add(labelANDTooltipMaker("Secondary Body Part:", 2, 2), 1, 1);
                    form.add(checkBoxFields[1], 2, 1);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 2);
                    form.add(labelANDTooltipMaker("Bombable Body Part:", 2, 3), 1, 2);
                    form.add(checkBoxFields[2], 2, 2);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 3);
                    form.add(labelANDTooltipMaker("Guard Body Part:", 2, 4), 1, 3);
                    form.add(checkBoxFields[3], 2, 3);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 4);
                    form.add(labelANDTooltipMaker("Not Bombable Body Part:", 2, 5), 1, 4);
                    form.add(checkBoxFields[4], 2, 4);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 5);
                    form.add(labelANDTooltipMaker("In Hole:", 2, 6), 1, 5);
                    form.add(checkBoxFields[5], 2, 5);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 6);
                    form.add(labelANDTooltipMaker("Weak to Attack FX R:", 2, 7), 1, 6);
                    form.add(checkBoxFields[6], 2, 6);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 7);
                    form.add(labelANDTooltipMaker("Weak to Ice Power:", 2, 8), 1, 7);
                    form.add(checkBoxFields[7], 2, 7);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 8);
                    form.add(labelANDTooltipMaker("Is Winged:", 2, 9), 1, 8);
                    form.add(checkBoxFields[8], 2, 8);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 9);
                    form.add(labelANDTooltipMaker("Is Shelled:", 2, 10), 1, 9);
                    form.add(checkBoxFields[9], 2, 9);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 10);
                    form.add(labelANDTooltipMaker("Is Bomb Flippable:", 2, 11), 1, 10);
                    form.add(checkBoxFields[10], 2, 10);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 11);
                    form.add(labelANDTooltipMaker("Is Clonelike:", 2, 12), 1, 11);
                    form.add(checkBoxFields[11], 2, 11);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 12);
                    form.add(labelANDTooltipMaker("Disable Flat Paper Layering:", 2, 13), 1, 12);
                    form.add(checkBoxFields[12], 2, 12);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 13);
                    form.add(labelANDTooltipMaker("Never Targetable:", 2, 14), 1, 13);
                    form.add(checkBoxFields[13], 2, 13);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 14);
                    form.add(labelANDTooltipMaker("Ignore Map Object Offset:", 2, 15), 1, 14);
                    form.add(checkBoxFields[14], 2, 14);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 15);
                    form.add(labelANDTooltipMaker("Ignore Only Target Select and Preferred Parts:", 2, 16), 1, 15);
                    form.add(checkBoxFields[15], 2, 15);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 16);
                    form.add(labelANDTooltipMaker("Cannot be Tattled:", 2, 17), 1, 16);
                    form.add(checkBoxFields[16], 2, 16);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 17);
                    form.add(labelANDTooltipMaker("Jump-like Attacks Cannot Target:", 2, 18), 1, 17);
                    form.add(checkBoxFields[17], 2, 17);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 18);
                    form.add(labelANDTooltipMaker("Hammer-like Attacks Cannot Target:", 2, 19), 1, 18);
                    form.add(checkBoxFields[18], 2, 18);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 19);
                    form.add(labelANDTooltipMaker("Shell Toss-like Attacks Cannot Target:", 2, 20), 1, 19);
                    form.add(checkBoxFields[19], 2, 19);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 20);
                    form.add(labelANDTooltipMaker("Prevent Health Decrease:", 2, 21), 1, 20);
                    form.add(checkBoxFields[20], 2, 20);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 21);
                    form.add(labelANDTooltipMaker("Disable Part Visibility:", 2, 22), 1, 21);
                    form.add(checkBoxFields[21], 2, 21);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 22);
                    form.add(labelANDTooltipMaker("Is Custom Type:", 2, 23), 1, 22);
                    form.add(checkBoxFields[22], 2, 22);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 23);
                    form.add(labelANDTooltipMaker("Blur On:", 2, 24), 1, 23);
                    form.add(checkBoxFields[23], 2, 23);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 24);
                    form.add(labelANDTooltipMaker("Scale Independence:", 2, 25), 1, 24);
                    form.add(checkBoxFields[24], 2, 24);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 25);
                    form.add(labelANDTooltipMaker("Independence:", 2, 26), 1, 25);
                    form.add(checkBoxFields[25], 2, 25);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 26);
                    form.add(labelANDTooltipMaker("Is Immune to Damage and Status:", 2, 27), 1, 26);
                    form.add(checkBoxFields[26], 2, 26);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 27);
                    form.add(labelANDTooltipMaker("Is Immune to OHKO:", 2, 28), 1, 27);
                    form.add(checkBoxFields[27], 2, 27);
                    form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 28);
                    form.add(labelANDTooltipMaker("Is Immune to Status:", 2, 29), 1, 28);
                    form.add(checkBoxFields[28], 2, 28);
                    form.add(fieldImageViewCreator(images.get("audienceStar")), 0, 29);
                    form.add(labelANDTooltipMaker("Is Top Spiky:", 2, 30), 1, 29);
                    form.add(checkBoxFields[29], 2, 29);
                    form.add(fieldImageViewCreator(images.get("audienceStar")), 0, 30);
                    form.add(labelANDTooltipMaker("Is Preemptive Front Spiky:", 2, 31), 1, 30);
                    form.add(checkBoxFields[30], 2, 30);
                    form.add(fieldImageViewCreator(images.get("audienceStar")), 0, 31);
                    form.add(labelANDTooltipMaker("Is Front Spiky:", 2, 32), 1, 31);
                    form.add(checkBoxFields[31], 2, 31);
                    form.add(fieldImageViewCreator(images.get("audienceStar")), 0, 32);
                    form.add(labelANDTooltipMaker("Is Fiery:", 2, 33), 1, 32);
                    form.add(checkBoxFields[32], 2, 32);
                    form.add(fieldImageViewCreator(images.get("audienceStar")), 0, 33);
                    form.add(labelANDTooltipMaker("Has a Fiery Status:", 2, 34), 1, 33);
                    form.add(checkBoxFields[33], 2, 33);
                    form.add(fieldImageViewCreator(images.get("audienceStar")), 0, 34);
                    form.add(labelANDTooltipMaker("Is Icy:", 2, 35), 1, 34);
                    form.add(checkBoxFields[34], 2, 34);
                    form.add(fieldImageViewCreator(images.get("audienceStar")), 0, 35);
                    form.add(labelANDTooltipMaker("Has an Icy Status:", 2, 36), 1, 35);
                    form.add(checkBoxFields[35], 2, 35);
                    form.add(fieldImageViewCreator(images.get("audienceStar")), 0, 36);
                    form.add(labelANDTooltipMaker("Is Poisonous:", 2, 37), 1, 36);
                    form.add(checkBoxFields[36], 2, 36);
                    form.add(fieldImageViewCreator(images.get("audienceStar")), 0, 37);
                    form.add(labelANDTooltipMaker("Has a Poison Status:", 2, 38), 1, 37);
                    form.add(checkBoxFields[37], 2, 37);
                    form.add(fieldImageViewCreator(images.get("audienceStar")), 0, 38);
                    form.add(labelANDTooltipMaker("Is Electric:", 2, 39), 1, 38);
                    form.add(checkBoxFields[38], 2, 38);
                    form.add(fieldImageViewCreator(images.get("audienceStar")), 0, 39);
                    form.add(labelANDTooltipMaker("Has an Electric Status:", 2, 40), 1, 39);
                    form.add(checkBoxFields[39], 2, 39);
                    form.add(fieldImageViewCreator(images.get("audienceStar")), 0, 40);
                    form.add(labelANDTooltipMaker("Is Explosive:", 2, 41), 1, 40);
                    form.add(checkBoxFields[40], 2, 40);
                    form.add(fieldImageViewCreator(images.get("audienceStar")), 0, 41);
                    form.add(labelANDTooltipMaker("Is Volatile Explosive:", 2, 42), 1, 41);
                    form.add(checkBoxFields[41], 2, 41);

                    centerMenu.getChildren().add(form);
                    ScrollPane centerScroll = new ScrollPane();
                    centerScroll.setContent(centerMenu);
                    borderPane.setCenter(centerScroll);
                }
                else if(kindList.getSelectionModel().getSelectedItem() instanceof HealthUpgrades)
                {
                    HealthUpgrades selected = (HealthUpgrades) kindList.getSelectionModel().getSelectedItem();

                    if (selected != null) loadStructFields(selected);

                    saveChangesButton.setText("Save Struct Changes");

                    form.setVgap(5);
                    form.setHgap(10);
                    form.setPadding(new Insets(10));

                    String namecheck = unitSelector.getSelectionModel().getSelectedItem().name;
                    if(namecheck.equals("Goombella") || namecheck.equals("Koops") || namecheck.equals("Flurrie") || namecheck.equals("Yoshi") ||
                        namecheck.equals("Vivian") || namecheck.equals("Bobbery") || namecheck.equals("Ms. Mowz"))
                    {
                        form.add(fieldImageViewCreator(images.get("partnerlvl1Custom")), 0, 0);
                        form.add(labelANDTooltipMaker("Level 1 HP:", 3, 7), 1, 0);
                        form.add(textFields[0], 2, 0);
                        form.add(fieldImageViewCreator(images.get("lvl3Move")), 0, 1);
                        form.add(labelANDTooltipMaker("Level 2 HP:", 3, 8), 1, 1);
                        form.add(textFields[1], 2, 1);
                        form.add(fieldImageViewCreator(images.get("lvl4Move")), 0, 2);
                        form.add(labelANDTooltipMaker("Level 3 HP:", 3, 9), 1, 2);
                        form.add(textFields[2], 2, 2);
                    }
                    else
                    {
                        form.add(fieldImageViewCreator(images.get("heart")), 0, 0);
                        form.add(labelANDTooltipMaker("Starting HP:", 3, 1), 1, 0);
                        form.add(textFields[0], 2, 0);
                        form.add(fieldImageViewCreator(images.get("flower")), 0, 1);
                        form.add(labelANDTooltipMaker("Starting FP:", 3, 2), 1, 1);
                        form.add(textFields[1], 2, 1);
                        form.add(fieldImageViewCreator(images.get("BPEmblemCustom")), 0, 2);
                        form.add(labelANDTooltipMaker("Starting BP:", 3, 3), 1, 2);
                        form.add(textFields[2], 2, 2);
                        form.add(fieldImageViewCreator(images.get("HPUpgrade")), 0, 3);
                        form.add(labelANDTooltipMaker("Upgrade HP:", 3, 4), 1, 3);
                        form.add(textFields[3], 2, 3);
                        form.add(fieldImageViewCreator(images.get("FPUpgrade")), 0, 4);
                        form.add(labelANDTooltipMaker("Upgrade FP:", 3, 5), 1, 4);
                        form.add(textFields[4], 2, 4);
                        form.add(fieldImageViewCreator(images.get("BPUpgrade")), 0, 5);
                        form.add(labelANDTooltipMaker("Upgrade BP:", 3, 6), 1, 5);
                        form.add(textFields[5], 2, 5);
                    }

                    centerMenu.getChildren().add(form);
                    ScrollPane centerScroll = new ScrollPane();
                    centerScroll.setContent(centerMenu);
                    borderPane.setCenter(centerScroll);
                }
            });  
            defenseList.setOnMouseClicked(e2 -> 
            {
                centerMenu.getChildren().clear();
                form.getChildren().clear();
                kindList.getSelectionModel().clearSelection();
                weaponList.getSelectionModel().clearSelection();

                upperBox.getChildren().clear();
                upperBox.getChildren().addAll(unitSelector, saveChangesButton);

                upperBox2.getChildren().clear();
                upperBox2.getChildren().addAll(saveStructPresetButton, loadStructPresetButton);

                if(defenseList.getSelectionModel().getSelectedItem() instanceof BattleUnitDefense)
                {
                    BattleUnitDefense selected = (BattleUnitDefense) defenseList.getSelectionModel().getSelectedItem();

                    if (selected != null) loadStructFields(selected);

                    saveChangesButton.setText("Save Struct Changes");

                    form.setVgap(5);
                    form.setHgap(10);
                    form.setPadding(new Insets(10));
                    
                    form.add(fieldImageViewCreator(images.get("defenseUpStatus")), 0, 0);
                    form.add(labelANDTooltipMaker("Normal Defense:", 4, 1), 1, 0);
                    form.add(textFields[0], 2, 0);
                    form.add(fieldImageViewCreator(images.get("defenseUpStatus")), 0, 1);
                    form.add(labelANDTooltipMaker("Fire Defense:", 4, 2), 1, 1);
                    form.add(textFields[1], 2, 1);
                    form.add(fieldImageViewCreator(images.get("defenseUpStatus")), 0, 2);
                    form.add(labelANDTooltipMaker("Ice Defense:", 4, 3), 1, 2);
                    form.add(textFields[2], 2, 2);
                    form.add(fieldImageViewCreator(images.get("defenseUpStatus")), 0, 3);
                    form.add(labelANDTooltipMaker("Explosion Defense:", 4, 4), 1, 3);
                    form.add(textFields[3], 2, 3);
                    form.add(fieldImageViewCreator(images.get("defenseUpStatus")), 0, 4);
                    form.add(labelANDTooltipMaker("Electric Defense:", 4, 5), 1, 4);
                    form.add(textFields[4], 2, 4);

                    centerMenu.getChildren().add(form);
                    ScrollPane centerScroll = new ScrollPane();
                    centerScroll.setContent(centerMenu);
                    borderPane.setCenter(centerScroll);
                }
                else if(defenseList.getSelectionModel().getSelectedItem() instanceof BattleUnitDefenseAttr)
                {
                    comboBoxFields[0].getItems().clear();
                    comboBoxFields[1].getItems().clear();
                    comboBoxFields[2].getItems().clear();
                    comboBoxFields[3].getItems().clear();
                    comboBoxFields[4].getItems().clear();

                    comboBoxFields[0].getItems().addAll("No Effect", "Weak To", "Immune To", "Absorbs", "Immune To (Except Gulp)");
                    comboBoxFields[1].getItems().addAll("No Effect", "Weak To", "Immune To", "Absorbs", "Immune To (Except Gulp)");
                    comboBoxFields[2].getItems().addAll("No Effect", "Weak To", "Immune To", "Absorbs", "Immune To (Except Gulp)");
                    comboBoxFields[3].getItems().addAll("No Effect", "Weak To", "Immune To", "Absorbs", "Immune To (Except Gulp)");
                    comboBoxFields[4].getItems().addAll("No Effect", "Weak To", "Immune To", "Absorbs", "Immune To (Except Gulp)");

                    comboBoxFields[1].setDisable(false);
                    comboBoxFields[5].setDisable(false);

                    BattleUnitDefenseAttr selected = (BattleUnitDefenseAttr) defenseList.getSelectionModel().getSelectedItem();

                    if (selected != null) loadStructFields(selected);

                    saveChangesButton.setText("Save Struct Changes");

                    form.setVgap(5);
                    form.setHgap(10);
                    form.setPadding(new Insets(10));
                    
                    form.add(fieldImageViewCreator(images.get("spikeShieldCustom")), 0, 0);
                    form.add(labelANDTooltipMaker("Normal Attribute Defense:", 5, 1), 1, 0);
                    form.add(comboBoxFields[0], 2, 0);
                    form.add(fieldImageViewCreator(images.get("fireShieldCustom")), 0, 1);
                    form.add(labelANDTooltipMaker("Fire Attribute Defense:", 5, 2), 1, 1);
                    form.add(comboBoxFields[1], 2, 1);
                    form.add(fieldImageViewCreator(images.get("iceShieldCustom")), 0, 2);
                    form.add(labelANDTooltipMaker("Ice Attribute Defense:", 5, 3), 1, 2);
                    form.add(comboBoxFields[2], 2, 2);
                    form.add(fieldImageViewCreator(images.get("burstShieldCustom")), 0, 3);
                    form.add(labelANDTooltipMaker("Explosion Attribute Defense:", 5, 4), 1, 3);
                    form.add(comboBoxFields[3], 2, 3);
                    form.add(fieldImageViewCreator(images.get("zapShieldCustom")), 0, 4);
                    form.add(labelANDTooltipMaker("Electric Attribute Defense:", 5, 5), 1, 4);
                    form.add(comboBoxFields[4], 2, 4);

                    centerMenu.getChildren().add(form);
                    ScrollPane centerScroll = new ScrollPane();
                    centerScroll.setContent(centerMenu);
                    borderPane.setCenter(centerScroll);
                }
                else if(defenseList.getSelectionModel().getSelectedItem() instanceof StatusVulnerability)
                {
                    centerMenu.getChildren().clear();
                    form.getChildren().clear();
                    kindList.getSelectionModel().clearSelection();
                    weaponList.getSelectionModel().clearSelection();

                    upperBox.getChildren().clear();
                    upperBox.getChildren().addAll(unitSelector, saveChangesButton);

                    upperBox2.getChildren().clear();
                    upperBox2.getChildren().addAll(saveStructPresetButton, loadStructPresetButton);

                    StatusVulnerability selected = (StatusVulnerability) defenseList.getSelectionModel().getSelectedItem();

                    if (selected != null) loadStructFields(selected);

                    saveChangesButton.setText("Save Struct Changes");

                    form.setVgap(5);
                    form.setHgap(10);
                    form.setPadding(new Insets(10));

                    form.add(fieldImageViewCreator(images.get("sleepStatus")), 0, 0);
                    form.add(labelANDTooltipMaker("Sleep Susceptibility:", 6, 1), 1, 0);
                    form.add(textFields[0], 2, 0);
                    form.add(fieldImageViewCreator(images.get("stopStatus")), 0, 1);
                    form.add(labelANDTooltipMaker("Stop Susceptibility:", 6, 2), 1, 1);
                    form.add(textFields[1], 2, 1);
                    form.add(fieldImageViewCreator(images.get("dizzyStatus")), 0, 2);
                    form.add(labelANDTooltipMaker("Dizzy Susceptibility:", 6, 3), 1, 2);
                    form.add(textFields[2], 2, 2);
                    form.add(fieldImageViewCreator(images.get("poisonStatus")), 0, 3);
                    form.add(labelANDTooltipMaker("Poison Susceptibility:", 6, 4), 1, 3);
                    form.add(textFields[3], 2, 3);
                    form.add(fieldImageViewCreator(images.get("confuseStatus")), 0, 4);
                    form.add(labelANDTooltipMaker("Confuse Susceptibility:", 6, 5), 1, 4);
                    form.add(textFields[4], 2, 4);
                    form.add(fieldImageViewCreator(images.get("electricStatus")), 0, 5);
                    form.add(labelANDTooltipMaker("Electric Susceptibility:", 6, 6), 1, 5);
                    form.add(textFields[5], 2, 5);
                    form.add(fieldImageViewCreator(images.get("burnStatus")), 0, 6);
                    form.add(labelANDTooltipMaker("Burn Susceptibility:", 6, 7), 1, 6);
                    form.add(textFields[6], 2, 6);
                    form.add(fieldImageViewCreator(images.get("freezeStatus")), 0, 7);
                    form.add(labelANDTooltipMaker("Freeze Susceptibility:", 6, 8), 1, 7);
                    form.add(textFields[7], 2, 7);
                    form.add(fieldImageViewCreator(images.get("hugeStatus")), 0, 8);
                    form.add(labelANDTooltipMaker("Huge Susceptibility:", 6, 9), 1, 8);
                    form.add(textFields[8], 2, 8);
                    form.add(fieldImageViewCreator(images.get("tinyStatus")), 0, 9);
                    form.add(labelANDTooltipMaker("Tiny Susceptibility:", 6, 10), 1, 9);
                    form.add(textFields[9], 2, 9);
                    form.add(fieldImageViewCreator(images.get("attackUpStatus")), 0, 10);
                    form.add(labelANDTooltipMaker("Attack Up Susceptibility:", 6, 11), 1, 10);
                    form.add(textFields[10], 2, 10);
                    form.add(fieldImageViewCreator(images.get("attackDownStatus")), 0, 11);
                    form.add(labelANDTooltipMaker("Attack Down Susceptibility:", 6, 12), 1, 11);
                    form.add(textFields[11], 2, 11);
                    form.add(fieldImageViewCreator(images.get("defenseUpStatus")), 0, 12);
                    form.add(labelANDTooltipMaker("Defense Up Susceptibility:", 6, 13), 1, 12);
                    form.add(textFields[12], 2, 12);
                    form.add(fieldImageViewCreator(images.get("defenseDownStatus")), 0, 13);
                    form.add(labelANDTooltipMaker("Defense Down Susceptibility:", 6, 14), 1, 13);
                    form.add(textFields[13], 2, 13);
                    form.add(fieldImageViewCreator(images.get("allergicStatus")), 0, 14);
                    form.add(labelANDTooltipMaker("Allergic Susceptibility:", 6, 15), 1, 14);
                    form.add(textFields[14], 2, 14);
                    form.add(fieldImageViewCreator(images.get("frightMask")), 0, 15);
                    form.add(labelANDTooltipMaker("Fright Susceptibility:", 6, 16), 1, 15);
                    form.add(textFields[15], 2, 15);
                    form.add(fieldImageViewCreator(images.get("flurriePartnerSwitch")), 0, 16);
                    form.add(labelANDTooltipMaker("Gale Force Susceptibility:", 6, 17), 1, 16);
                    form.add(textFields[16], 2, 16);
                    form.add(fieldImageViewCreator(images.get("fastStatus")), 0, 17);
                    form.add(labelANDTooltipMaker("Fast Susceptibility:", 6, 18), 1, 17);
                    form.add(textFields[17], 2, 17);
                    form.add(fieldImageViewCreator(images.get("slowStatus")), 0, 18);
                    form.add(labelANDTooltipMaker("Slow Susceptibility:", 6, 19), 1, 18);
                    form.add(textFields[18], 2, 18);
                    form.add(fieldImageViewCreator(images.get("dodgyStatus")), 0, 19);
                    form.add(labelANDTooltipMaker("Dodgy Susceptibility:", 6, 20), 1, 19);
                    form.add(textFields[19], 2, 19);
                    form.add(fieldImageViewCreator(images.get("invisibleStatus")), 0, 20);
                    form.add(labelANDTooltipMaker("Invisible Susceptibility:", 6, 21), 1, 20);
                    form.add(textFields[20], 2, 20);
                    form.add(fieldImageViewCreator(images.get("garnetStar")), 0, 21);
                    form.add(labelANDTooltipMaker("OHKO Susceptibility:", 6, 22), 1, 21);
                    form.add(textFields[21], 2, 21);

                    centerMenu.getChildren().add(form);
                    ScrollPane centerScroll = new ScrollPane();
                    centerScroll.setContent(centerMenu);
                    borderPane.setCenter(centerScroll);
                }
            });
            weaponList.setOnMouseClicked(e2 -> 
            {
                centerMenu.getChildren().clear();
                form.getChildren().clear();
                kindList.getSelectionModel().clearSelection();
                defenseList.getSelectionModel().clearSelection();

                upperBox.getChildren().clear();
                upperBox.getChildren().addAll(unitSelector, saveChangesButton);

                upperBox2.getChildren().clear();
                upperBox2.getChildren().addAll(saveStructPresetButton, loadStructPresetButton);

                comboBoxFields[0].getItems().clear();
                comboBoxFields[1].getItems().clear();
                comboBoxFields[2].getItems().clear();
                comboBoxFields[3].getItems().clear();
                comboBoxFields[4].getItems().clear();
                comboBoxFields[5].getItems().clear();

                comboBoxFields[0].getItems().addAll("Unsuperguardable", "Superguardable with Recoil", "Superguardable without Recoil");
                comboBoxFields[1].getItems().addAll("None", "Get Jump Power", "Get Hammer Power", "Get Default Power", "Get Badge Stacked Jump Power", "Get Tornado Power", "Get Badge Stacked Hammer Power", "Get Art Attack Power", "Get Supernova Power", "Get Earth Tremor Power", "Get Poison Shroom Power", "Get Partner Power", "Get Partner Action Command Power");
                comboBoxFields[2].getItems().addAll("Normal", "Fire", "Ice", "Explosive", "Electric");
                comboBoxFields[3].getItems().addAll("None", "Gulp Knocked onto Ground 1", "Gulp Knocked onto Ground 2", "Gulp Knocked into next Target", "Remove Segment", "Short Time Squashed", "Medium Time Squashed", "Long Time Squashed", "Inked", "Super Hammer Knocked Back", "Ultra Hammer Knocked Back", "Ultra Hammer Finisher", "Confusion Effect", "Love Slap Spin 1", "Love Slap Spin 2", "Love Slap Quick Spin 1", "Love Slap Quick Spin 2", "Blown Away", "Play Additional Ending Hurt Animation");
                comboBoxFields[4].getItems().addAll("Extremely Easy", "Very Easy", "Easy", "Normal", "Difficult", "Very Difficult", "Extremely Difficult");
                comboBoxFields[5].getItems().addAll("None", "Jump Script", "Spin Jump Script", "Spring Jump Script", "First Strike Super Hammer Script", "Hammer Script", "Super Hammer Script", "Multibounce Script", "Power Jump Script", "Mega Jump Script", "Power Bounce Script", "Tornado Jump Script", "Shrink Stomp Script", "Sleepy Stomp Script", "Soft Stomp Script", "Power Smash Script", "Quake Hammer Script", "Mega Quake Script", "Hammer Throw Script", "Piercing Blow Script", "Fire Drive Script", "Ice Smash Script", "Charge Script", "Charge P Script", "Super Charge Script", "Super Charge P Script", "Clock Out Script", "Art Attack Script", "Supernova Script", "Sweet Treat Script", "Sweat Feast Script", "Earth Tremor Script", "Power Lift Script", "Showstopper Script", "Item Heal Script", "Pow Block Script", "Fire Flower Script", "Thunder Rage Script", "Thunder Bolt Script", "Shooting Star Script", "Ice Storm Script", "Earth Quake Script", "Boo's Sheet Script", "Volt Shroom Script", "Repel Cape Script", "Ruin Powder Script", "Sleepy Sheep Script", "Stopwatch Script", "Dizzy Dial Script", "Power Punch Script", "Courage Shell Script", "HP Drain Script", "Trade Off Script", "Mini Mr. Mini Script", "Mr. Softener Script", "Tasty Tonic Script", "Slow Shroom Script", "Gradual Syrup Script", "Point Swap Script", "Fright Mask Script", "Mystery Script", "Spite Pouch Script", "Koopa Curse Script", "Item Heal and Status Script", "Thrown Item Script", "Poison Shroom Script", "Trial Stew Script", "Body Slam Script", "Gale Force Script", "Lip Lock Script", "Dodgy Fog Script", "Ground Pound Script", "Gulp Script", "Mini Egg Script", "Stampede Script", "Love Slap Script", "Tease Script", "Kiss Thief Script", "Smooch Script", "Bomb First Strike Script", "Bomb Script", "Bomb Squad Script", "Hold Fast Script", "Bob-ombast Script", "Shade Fist Script", "Veil Script", "Fiery Jinx Script", "Infatuate Script", "Shell Toss First Strike Script", "Shell Toss Script", "Power Shell Script", "Shell Shield Script", "Shell Slam Script", "Headbonk Script", "Tattle Script", "Multibonk Script", "Rally Wink Script");

                comboBoxFields[1].setDisable(true);
                comboBoxFields[5].setDisable(true);
                if(fileSelector.getSelectionModel().getSelectedItem().getName().equals("main.dol") || fileSelector.getSelectionModel().getSelectedItem().getName().equals("Start.dol"))
                {
                    comboBoxFields[1].setDisable(false);
                    comboBoxFields[5].setDisable(false);
                }

                BattleWeapon selected = weaponList.getSelectionModel().getSelectedItem();

                if (selected != null) loadStructFields(selected);

                saveChangesButton.setText("Save Struct Changes");

                form.setVgap(5);
                form.setHgap(10);
                form.setPadding(new Insets(10));

                form.add(fieldImageViewCreator(images.get("appealAction")), 0, 0);
                form.add(labelANDTooltipMaker("Accuracy:", 7, 1), 1, 0);
                form.add(textFields[0], 2, 0);
                form.add(fieldImageViewCreator(images.get("flower")), 0, 1);
                form.add(labelANDTooltipMaker("FP Cost:", 7, 2), 1, 1);
                form.add(textFields[1], 2, 1);
                form.add(fieldImageViewCreator(images.get("SPOrb1")), 0, 2);
                form.add(labelANDTooltipMaker("SP Cost:", 7, 3), 1, 2);
                form.add(textFields[2], 2, 2);
                form.add(fieldImageViewCreator(images.get("superChargeAction")), 0, 3);
                form.add(labelANDTooltipMaker("Superguard State:", 7, 4), 1, 3);
                form.add(comboBoxFields[0], 2, 3);
                form.add(fieldImageViewCreator(images.get("audienceStar")), 0, 4);
                form.add(labelANDTooltipMaker("Stylish Multiplier:", 7, 5), 1, 4);
                form.add(textFields[3], 2, 4);
                form.add(fieldImageViewCreator(images.get("shineSprite")), 0, 5);
                form.add(labelANDTooltipMaker("Increase Bingo Slot Chance:", 7, 6), 1, 5);
                form.add(textFields[4], 2, 5);
                form.add(fieldImageViewCreator(images.get("routingSlip")), 0, 6);
                form.add(labelANDTooltipMaker("Base Damage Function:", 7, 7), 1, 6);
                form.add(comboBoxFields[1], 2, 6);
                form.add(fieldImageViewCreator(images.get("hammer")), 0, 7);
                form.add(labelANDTooltipMaker("Base Damage Parameter 1:", 7, 8), 1, 7);
                form.add(textFields[5], 2, 7);
                form.add(fieldImageViewCreator(images.get("hammer")), 0, 8);
                form.add(labelANDTooltipMaker("Base Damage Parameter 2:", 7, 9), 1, 8);
                form.add(textFields[6], 2, 8);
                form.add(fieldImageViewCreator(images.get("hammer")), 0, 9);
                form.add(labelANDTooltipMaker("Base Damage Parameter 3:", 7, 10), 1, 9);
                form.add(textFields[7], 2, 9);
                form.add(fieldImageViewCreator(images.get("hammer")), 0, 10);
                form.add(labelANDTooltipMaker("Base Damage Parameter 4:", 7, 11), 1, 10);
                form.add(textFields[8], 2, 10);
                form.add(fieldImageViewCreator(images.get("hammer")), 0, 11);
                form.add(labelANDTooltipMaker("Base Damage Parameter 5:", 7, 12), 1, 11);
                form.add(textFields[9], 2, 11);
                form.add(fieldImageViewCreator(images.get("hammer")), 0, 12);
                form.add(labelANDTooltipMaker("Base Damage Parameter 6:", 7, 13), 1, 12);
                form.add(textFields[10], 2, 12);
                form.add(fieldImageViewCreator(images.get("hammer")), 0, 13);
                form.add(labelANDTooltipMaker("Base Damage Parameter 7:", 7, 14), 1, 13);
                form.add(textFields[11], 2, 13);
                form.add(fieldImageViewCreator(images.get("hammer")), 0, 14);
                form.add(labelANDTooltipMaker("Base Damage Parameter 8:", 7, 15), 1, 14);
                form.add(textFields[12], 2, 14);
                form.add(fieldImageViewCreator(images.get("flower")), 0, 15);
                form.add(labelANDTooltipMaker("Base FP Damage Parameter 1:", 7, 16), 1, 15);
                form.add(textFields[13], 2, 15);
                form.add(fieldImageViewCreator(images.get("flower")), 0, 16);
                form.add(labelANDTooltipMaker("Base FP Damage Parameter 2:", 7, 17), 1, 16);
                form.add(textFields[14], 2, 16);
                form.add(fieldImageViewCreator(images.get("flower")), 0, 17);
                form.add(labelANDTooltipMaker("Base FP Damage Parameter 3:", 7, 18), 1, 17);
                form.add(textFields[15], 2, 17);
                form.add(fieldImageViewCreator(images.get("flower")), 0, 18);
                form.add(labelANDTooltipMaker("Base FP Damage Parameter 4:", 7, 19), 1, 18);
                form.add(textFields[16], 2, 18);
                form.add(fieldImageViewCreator(images.get("flower")), 0, 19);
                form.add(labelANDTooltipMaker("Base FP Damage Parameter 5:", 7, 20), 1, 19);
                form.add(textFields[17], 2, 19);
                form.add(fieldImageViewCreator(images.get("flower")), 0, 20);
                form.add(labelANDTooltipMaker("Base FP Damage Parameter 6:", 7, 21), 1, 20);
                form.add(textFields[18], 2, 20);
                form.add(fieldImageViewCreator(images.get("flower")), 0, 21);
                form.add(labelANDTooltipMaker("Base FP Damage Parameter 7:", 7, 22), 1, 21);
                form.add(textFields[19], 2, 21);
                form.add(fieldImageViewCreator(images.get("flower")), 0, 22);
                form.add(labelANDTooltipMaker("Base FP Damage Parameter 8:", 7, 23), 1, 22);
                form.add(textFields[20], 2, 22);
                form.add(fieldImageViewCreator(images.get("zapTap")), 0, 23);
                form.add(labelANDTooltipMaker("Element:", 7, 24), 1, 23);
                form.add(comboBoxFields[2], 2, 23);
                form.add(fieldImageViewCreator(images.get("tacticsFlag")), 0, 24);
                form.add(labelANDTooltipMaker("Damage Pattern:", 7, 25), 1, 24);
                form.add(comboBoxFields[3], 2, 24);
                form.add(fieldImageViewCreator(images.get("actionCommandStar")), 0, 25);
                form.add(labelANDTooltipMaker("Action Command Difficulty:", 7, 26), 1, 25);
                form.add(comboBoxFields[4], 2, 25);
                form.add(fieldImageViewCreator(images.get("sleepStatus")), 0, 26);
                form.add(labelANDTooltipMaker("Sleep Chance:", 7, 27), 1, 26);
                form.add(textFields[21], 2, 26);
                form.add(fieldImageViewCreator(images.get("sleepStatus")), 0, 27);
                form.add(labelANDTooltipMaker("Sleep Time:", 7, 28), 1, 27);
                form.add(textFields[22], 2, 27);
                form.add(fieldImageViewCreator(images.get("stopStatus")), 0, 28);
                form.add(labelANDTooltipMaker("Stop Chance:", 7, 29), 1, 28);
                form.add(textFields[23], 2, 28);
                form.add(fieldImageViewCreator(images.get("stopStatus")), 0, 29);
                form.add(labelANDTooltipMaker("Stop Time:", 7, 30), 1, 29);
                form.add(textFields[24], 2, 29);
                form.add(fieldImageViewCreator(images.get("dizzyStatus")), 0, 30);
                form.add(labelANDTooltipMaker("Dizzy Chance:", 7, 31), 1, 30);
                form.add(textFields[25], 2, 30);
                form.add(fieldImageViewCreator(images.get("dizzyStatus")), 0, 31);
                form.add(labelANDTooltipMaker("Dizzy Time:", 7, 32), 1, 31);
                form.add(textFields[26], 2, 31);
                form.add(fieldImageViewCreator(images.get("poisonStatus")), 0, 32);
                form.add(labelANDTooltipMaker("Poison Chance:", 7, 33), 1, 32);
                form.add(textFields[27], 2, 32);
                form.add(fieldImageViewCreator(images.get("poisonStatus")), 0, 33);
                form.add(labelANDTooltipMaker("Poison Time:", 7, 34), 1, 33);
                form.add(textFields[28], 2, 33);
                form.add(fieldImageViewCreator(images.get("poisonStatus")), 0, 34);
                form.add(labelANDTooltipMaker("Poison Strength:", 7, 35), 1, 34);
                form.add(textFields[29], 2, 34);
                form.add(fieldImageViewCreator(images.get("confuseStatus")), 0, 35);
                form.add(labelANDTooltipMaker("Confuse Chance:", 7, 36), 1, 35);
                form.add(textFields[30], 2, 35);
                form.add(fieldImageViewCreator(images.get("confuseStatus")), 0, 36);
                form.add(labelANDTooltipMaker("Confuse Time:", 7, 37), 1, 36);
                form.add(textFields[31], 2, 36);
                form.add(fieldImageViewCreator(images.get("electricStatus")), 0, 37);
                form.add(labelANDTooltipMaker("Electric Chance:", 7, 38), 1, 37);
                form.add(textFields[32], 2, 37);
                form.add(fieldImageViewCreator(images.get("electricStatus")), 0, 38);
                form.add(labelANDTooltipMaker("Electric Time:", 7, 39), 1, 38);
                form.add(textFields[33], 2, 38);
                form.add(fieldImageViewCreator(images.get("dodgyStatus")), 0, 39);
                form.add(labelANDTooltipMaker("Dodgy Chance:", 7, 40), 1, 39);
                form.add(textFields[34], 2, 39);
                form.add(fieldImageViewCreator(images.get("dodgyStatus")), 0, 40);
                form.add(labelANDTooltipMaker("Dodgy Time:", 7, 41), 1, 40);
                form.add(textFields[35], 2, 40);
                form.add(fieldImageViewCreator(images.get("burnStatus")), 0, 41);
                form.add(labelANDTooltipMaker("Burn Chance:", 7, 42), 1, 41);
                form.add(textFields[36], 2, 41);
                form.add(fieldImageViewCreator(images.get("burnStatus")), 0, 42);
                form.add(labelANDTooltipMaker("Burn Time:", 7, 43), 1, 42);
                form.add(textFields[37], 2, 42);
                form.add(fieldImageViewCreator(images.get("freezeStatus")), 0, 43);
                form.add(labelANDTooltipMaker("Freeze Chance:", 7, 44), 1, 43);
                form.add(textFields[38], 2, 43);
                form.add(fieldImageViewCreator(images.get("freezeStatus")), 0, 44);
                form.add(labelANDTooltipMaker("Freeze Time:", 7, 45), 1, 44);
                form.add(textFields[39], 2, 44);
                form.add(fieldImageViewCreator(images.get("hugeStatus")), 0, 45);
                form.add(labelANDTooltipMaker("Size Change Chance:", 7, 46), 1, 45);
                form.add(textFields[40], 2, 45);
                form.add(fieldImageViewCreator(images.get("hugeStatus")), 0, 46);
                form.add(labelANDTooltipMaker("Size Change Time:", 7, 47), 1, 46);
                form.add(textFields[41], 2, 46);
                form.add(fieldImageViewCreator(images.get("hugeStatus")), 0, 47);
                form.add(labelANDTooltipMaker("Size Change Strength:", 7, 48), 1, 47);
                form.add(textFields[42], 2, 47);
                form.add(fieldImageViewCreator(images.get("attackDownStatus")), 0, 48);
                form.add(labelANDTooltipMaker("Atk Change Chance:", 7, 49), 1, 48);
                form.add(textFields[43], 2, 48);
                form.add(fieldImageViewCreator(images.get("attackDownStatus")), 0, 49);
                form.add(labelANDTooltipMaker("Atk Change Time:", 7, 50), 1, 49);
                form.add(textFields[44], 2, 49);
                form.add(fieldImageViewCreator(images.get("attackDownStatus")), 0, 50);
                form.add(labelANDTooltipMaker("Atk Change Strength:", 7, 51), 1, 50);
                form.add(textFields[45], 2, 50);
                form.add(fieldImageViewCreator(images.get("defenseUpStatus")), 0, 51);
                form.add(labelANDTooltipMaker("Def Change Chance:", 7, 52), 1, 51);
                form.add(textFields[46], 2, 51);
                form.add(fieldImageViewCreator(images.get("defenseUpStatus")), 0, 52);
                form.add(labelANDTooltipMaker("Def Change Time:", 7, 53), 1, 52);
                form.add(textFields[47], 2, 52);
                form.add(fieldImageViewCreator(images.get("defenseUpStatus")), 0, 53);
                form.add(labelANDTooltipMaker("Def Change Strength:", 7, 54), 1, 53);
                form.add(textFields[48], 2, 53);
                form.add(fieldImageViewCreator(images.get("allergicStatus")), 0, 54);
                form.add(labelANDTooltipMaker("Allergic Chance:", 7, 55), 1, 54);
                form.add(textFields[49], 2, 54);
                form.add(fieldImageViewCreator(images.get("allergicStatus")), 0, 55);
                form.add(labelANDTooltipMaker("Allergic Time:", 7, 56), 1, 55);
                form.add(textFields[50], 2, 55);
                form.add(fieldImageViewCreator(images.get("garnetStar")), 0, 56);
                form.add(labelANDTooltipMaker("OHKO Chance:", 7, 57), 1, 56);
                form.add(textFields[51], 2, 56);
                form.add(fieldImageViewCreator(images.get("chargeAction")), 0, 57);
                form.add(labelANDTooltipMaker("Charge Strength:", 7, 58), 1, 57);
                form.add(textFields[52], 2, 57);
                form.add(fieldImageViewCreator(images.get("fastStatus")), 0, 58);
                form.add(labelANDTooltipMaker("Fast Chance:", 7, 59), 1, 58);
                form.add(textFields[53], 2, 58);
                form.add(fieldImageViewCreator(images.get("fastStatus")), 0, 59);
                form.add(labelANDTooltipMaker("Fast Time:", 7, 60), 1, 59);
                form.add(textFields[54], 2, 59);
                form.add(fieldImageViewCreator(images.get("slowStatus")), 0, 60);
                form.add(labelANDTooltipMaker("Slow Chance:", 7, 61), 1, 60);
                form.add(textFields[55], 2, 60);
                form.add(fieldImageViewCreator(images.get("slowStatus")), 0, 61);
                form.add(labelANDTooltipMaker("Slow Time:", 7, 62), 1, 61);
                form.add(textFields[56], 2, 61);
                form.add(fieldImageViewCreator(images.get("frightMask")), 0, 62);
                form.add(labelANDTooltipMaker("Fright Chance:", 7, 63), 1, 62);
                form.add(textFields[57], 2, 62);
                form.add(fieldImageViewCreator(images.get("flurriePartnerSwitch")), 0, 63);
                form.add(labelANDTooltipMaker("Gale Force Chance:", 7, 64), 1, 63);
                form.add(textFields[58], 2, 63);
                form.add(fieldImageViewCreator(images.get("paybackStatus")), 0, 64);
                form.add(labelANDTooltipMaker("Payback Time:", 7, 65), 1, 64);
                form.add(textFields[59], 2, 64);
                form.add(fieldImageViewCreator(images.get("bobberyPartnerSwitch")), 0, 65);
                form.add(labelANDTooltipMaker("Hold Fast Time:", 7, 66), 1, 65);
                form.add(textFields[60], 2, 65);
                form.add(fieldImageViewCreator(images.get("invisibleStatus")), 0, 66);
                form.add(labelANDTooltipMaker("Invisible Chance:", 7, 67), 1, 66);
                form.add(textFields[61], 2, 66);
                form.add(fieldImageViewCreator(images.get("invisibleStatus")), 0, 67);
                form.add(labelANDTooltipMaker("Invisible Time:", 7, 68), 1, 67);
                form.add(textFields[62], 2, 67);
                form.add(fieldImageViewCreator(images.get("HPRegenStatus")), 0, 68);
                form.add(labelANDTooltipMaker("HP Regen Time:", 7, 69), 1, 68);
                form.add(textFields[63], 2, 68);
                form.add(fieldImageViewCreator(images.get("HPRegenStatus")), 0, 69);
                form.add(labelANDTooltipMaker("HP Regen Strength:", 7, 70), 1, 69);
                form.add(textFields[64], 2, 69);
                form.add(fieldImageViewCreator(images.get("FPRegenStatus")), 0, 70);
                form.add(labelANDTooltipMaker("FP Regen Time:", 7, 71), 1, 70);
                form.add(textFields[65], 2, 70);
                form.add(fieldImageViewCreator(images.get("FPRegenStatus")), 0, 71);
                form.add(labelANDTooltipMaker("FP Regen Strength:", 7, 72), 1, 71);
                form.add(textFields[66], 2, 71);
                form.add(fieldImageViewCreator(images.get("routingSlip")), 0, 72);
                form.add(labelANDTooltipMaker("Attack Script:", 7, 73), 1, 72);
                form.add(comboBoxFields[5], 2, 72);
                form.add(fieldImageViewCreator(images.get("gateHandle")), 0, 73);
                form.add(labelANDTooltipMaker("Stage Background Fall Chance 1:", 7, 74), 1, 73);
                form.add(textFields[67], 2, 73);
                form.add(fieldImageViewCreator(images.get("gateHandle")), 0, 74);
                form.add(labelANDTooltipMaker("Stage Background Fall Chance 2:", 7, 75), 1, 74);
                form.add(textFields[68], 2, 74);
                form.add(fieldImageViewCreator(images.get("gateHandle")), 0, 75);
                form.add(labelANDTooltipMaker("Stage Background Fall Chance 3:", 7, 76), 1, 75);
                form.add(textFields[69], 2, 75);
                form.add(fieldImageViewCreator(images.get("gateHandle")), 0, 76);
                form.add(labelANDTooltipMaker("Stage Background Fall Chance 4:", 7, 77), 1, 76);
                form.add(textFields[70], 2, 76);
                form.add(fieldImageViewCreator(images.get("gateHandle")), 0, 77);
                form.add(labelANDTooltipMaker("Stage Nozzle Turn Chance:", 7, 78), 1, 77);
                form.add(textFields[71], 2, 77);
                form.add(fieldImageViewCreator(images.get("gateHandle")), 0, 78);
                form.add(labelANDTooltipMaker("Stage Nozzle Fire Chance:", 7, 79), 1, 78);
                form.add(textFields[72], 2, 78);
                form.add(fieldImageViewCreator(images.get("gateHandle")), 0, 79);
                form.add(labelANDTooltipMaker("Stage Ceiling Fall Chance:", 7, 80), 1, 79);
                form.add(textFields[73], 2, 79);
                form.add(fieldImageViewCreator(images.get("gateHandle")), 0, 80);
                form.add(labelANDTooltipMaker("Stage Object Fall Chance:", 7, 81), 1, 80);
                form.add(textFields[74], 2, 80);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 81);
                form.add(labelANDTooltipMaker("Cannot Target Mario or Shell Shield:", 7, 82), 1, 81);
                form.add(checkBoxFields[0], 2, 81);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 82);
                form.add(labelANDTooltipMaker("Cannot Target Partner:", 7, 83), 1, 82);
                form.add(checkBoxFields[1], 2, 82);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 83);
                form.add(labelANDTooltipMaker("Cannot Target Enemy:", 7, 84), 1, 83);
                form.add(checkBoxFields[2], 2, 83);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 84);
                form.add(labelANDTooltipMaker("Cannot Target Tree or Switch:", 7, 85), 1, 84);
                form.add(checkBoxFields[3], 2, 84);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 85);
                form.add(labelANDTooltipMaker("Cannot Target System:", 7, 86), 1, 85);
                form.add(checkBoxFields[4], 2, 85);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 86);
                form.add(labelANDTooltipMaker("Cannot Target Opposite Alliance:", 7, 87), 1, 86);
                form.add(checkBoxFields[5], 2, 86);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 87);
                form.add(labelANDTooltipMaker("Cannot Target Own Alliance:", 7, 88), 1, 87);
                form.add(checkBoxFields[6], 2, 87);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 88);
                form.add(labelANDTooltipMaker("Cannot Target Self:", 7, 89), 1, 88);
                form.add(checkBoxFields[7], 2, 88);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 89);
                form.add(labelANDTooltipMaker("Cannot Target Same Species:", 7, 90), 1, 89);
                form.add(checkBoxFields[8], 2, 89);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 90);
                form.add(labelANDTooltipMaker("Only Target Self:", 7, 91), 1, 90);
                form.add(checkBoxFields[9], 2, 90);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 91);
                form.add(labelANDTooltipMaker("Only Target Mario:", 7, 92), 1, 91);
                form.add(checkBoxFields[10], 2, 91);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 92);
                form.add(labelANDTooltipMaker("Only Target Tree or Switch:", 7, 93), 1, 92);
                form.add(checkBoxFields[11], 2, 92);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 93);
                form.add(labelANDTooltipMaker("Only Target Preferred Parts:", 7, 94), 1, 93);
                form.add(checkBoxFields[12], 2, 93);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 94);
                form.add(labelANDTooltipMaker("Only Target Select Parts:", 7, 95), 1, 94);
                form.add(checkBoxFields[13], 2, 94);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 95);
                form.add(labelANDTooltipMaker("Single Target:", 7, 96), 1, 95);
                form.add(checkBoxFields[14], 2, 95);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 96);
                form.add(labelANDTooltipMaker("Multiple Target:", 7, 97), 1, 96);
                form.add(checkBoxFields[15], 2, 96);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 97);
                form.add(labelANDTooltipMaker("Cannot Target Anything:", 7, 98), 1, 97);
                form.add(checkBoxFields[16], 2, 97);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 98);
                form.add(labelANDTooltipMaker("Is Tattle-like:", 7, 99), 1, 98);
                form.add(checkBoxFields[17], 2, 98);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 99);
                form.add(labelANDTooltipMaker("Cannot Target Floating 1:", 7, 100), 1, 99);
                form.add(checkBoxFields[18], 2, 99);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 100);
                form.add(labelANDTooltipMaker("Cannot Target Ceiling:", 7, 101), 1, 100);
                form.add(checkBoxFields[19], 2, 100);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 101);
                form.add(labelANDTooltipMaker("Cannot Target Floating 2:", 7, 102), 1, 101);
                form.add(checkBoxFields[20], 2, 101);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 102);
                form.add(labelANDTooltipMaker("Cannot Target Grounded:", 7, 103), 1, 102);
                form.add(checkBoxFields[21], 2, 102);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 103);
                form.add(labelANDTooltipMaker("Is Jump-like:", 7, 104), 1, 103);
                form.add(checkBoxFields[22], 2, 103);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 104);
                form.add(labelANDTooltipMaker("Is Hammer-like:", 7, 105), 1, 104);
                form.add(checkBoxFields[23], 2, 104);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 105);
                form.add(labelANDTooltipMaker("Is Shell Toss-like:", 7, 106), 1, 105);
                form.add(checkBoxFields[24], 2, 105);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 106);
                form.add(labelANDTooltipMaker("Cannot Target Grounded Variant:", 7, 107), 1, 106);
                form.add(checkBoxFields[25], 2, 106);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 107);
                form.add(labelANDTooltipMaker("Has Recoil Damage:", 7, 108), 1, 107);
                form.add(checkBoxFields[26], 2, 107);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 108);
                form.add(labelANDTooltipMaker("Can Only Target Frontmost:", 7, 109), 1, 108);
                form.add(checkBoxFields[27], 2, 108);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 109);
                form.add(labelANDTooltipMaker("Cannot Target Shell Shield:", 7, 110), 1, 109);
                form.add(checkBoxFields[28], 2, 109);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 110);
                form.add(labelANDTooltipMaker("Cannot Target Custom Type:", 7, 111), 1, 110);
                form.add(checkBoxFields[29], 2, 110);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 111);
                form.add(labelANDTooltipMaker("Target Same Alliance Direction:", 7, 112), 1, 111);
                form.add(checkBoxFields[30], 2, 111);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 112);
                form.add(labelANDTooltipMaker("Target Opposite Alliance Direction:", 7, 113), 1, 112);
                form.add(checkBoxFields[31], 2, 112);
                form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 113);
                form.add(labelANDTooltipMaker("Badge Can Affect Power:", 7, 114), 1, 113);
                form.add(checkBoxFields[32], 2, 113);
                form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 114);
                form.add(labelANDTooltipMaker("Status Can Affect Power:", 7, 115), 1, 114);
                form.add(checkBoxFields[33], 2, 114);
                form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 115);
                form.add(labelANDTooltipMaker("Is Chargeable:", 7, 116), 1, 115);
                form.add(checkBoxFields[34], 2, 115);
                form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 116);
                form.add(labelANDTooltipMaker("Cannot Miss:", 7, 117), 1, 116);
                form.add(checkBoxFields[35], 2, 116);
                form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 117);
                form.add(labelANDTooltipMaker("Has Diminishing Damage by Hit:", 7, 118), 1, 117);
                form.add(checkBoxFields[36], 2, 117);
                form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 118);
                form.add(labelANDTooltipMaker("Has Diminishing Damage by Target:", 7, 119), 1, 118);
                form.add(checkBoxFields[37], 2, 118);
                form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 119);
                form.add(labelANDTooltipMaker("Pierces Defense:", 7, 120), 1, 119);
                form.add(checkBoxFields[38], 2, 119);
                form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 120);
                form.add(labelANDTooltipMaker("Can Break Ice:", 7, 121), 1, 120);
                form.add(checkBoxFields[39], 2, 120);
                form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 121);
                form.add(labelANDTooltipMaker("Ingore Target Status Vulnerability:", 7, 122), 1, 121);
                form.add(checkBoxFields[40], 2, 121);
                form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 122);
                form.add(labelANDTooltipMaker("Unknown SPF Flag 0x200:", 7, 123), 1, 122);
                form.add(checkBoxFields[41], 2, 122);
                form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 123);
                form.add(labelANDTooltipMaker("Change Element to Fire if Target Burned:", 7, 124), 1, 123);
                form.add(checkBoxFields[42], 2, 123);
                form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 124);
                form.add(labelANDTooltipMaker("Play Active FX Sound:", 7, 125), 1, 124);
                form.add(checkBoxFields[43], 2, 124);
                form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 125);
                form.add(labelANDTooltipMaker("Flips Shell Enemies:", 7, 126), 1, 125);
                form.add(checkBoxFields[44], 2, 125);
                form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 126);
                form.add(labelANDTooltipMaker("Flips Bomb-Flippable Enemies:", 7, 127), 1, 126);
                form.add(checkBoxFields[45], 2, 126);
                form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 127);
                form.add(labelANDTooltipMaker("Grounds Winged Enemies:", 7, 128), 1, 127);
                form.add(checkBoxFields[46], 2, 127);
                form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 128);
                form.add(labelANDTooltipMaker("Unknown SPF Flag 0x8000:", 7, 129), 1, 128);
                form.add(checkBoxFields[47], 2, 128);
                form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 129);
                form.add(labelANDTooltipMaker("Can be Used when Confused:", 7, 130), 1, 129);
                form.add(checkBoxFields[48], 2, 129);
                form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 130);
                form.add(labelANDTooltipMaker("Is Unguardable:", 7, 131), 1, 130);
                form.add(checkBoxFields[49], 2, 130);
                form.add(fieldImageViewCreator(images.get("diminishingStarsCustom")), 0, 131);
                form.add(labelANDTooltipMaker("Can hit Clonelike:", 7, 132), 1, 131);
                form.add(checkBoxFields[50], 2, 131);
                form.add(fieldImageViewCreator(images.get("ignoreStatus")), 0, 132);
                form.add(labelANDTooltipMaker("Ignore Electric Resistance:", 7, 133), 1, 132);
                form.add(checkBoxFields[51], 2, 132);
                form.add(fieldImageViewCreator(images.get("ignoreStatus")), 0, 133);
                form.add(labelANDTooltipMaker("Ignore Top Spiky Resistance:", 7, 134), 1, 133);
                form.add(checkBoxFields[52], 2, 133);
                form.add(fieldImageViewCreator(images.get("ignoreStatus")), 0, 134);
                form.add(labelANDTooltipMaker("Ignore Pre-emptive Front Spiky Resistance:", 7, 135), 1, 134);
                form.add(checkBoxFields[53], 2, 134);
                form.add(fieldImageViewCreator(images.get("ignoreStatus")), 0, 135);
                form.add(labelANDTooltipMaker("Ignore Front Spiky Resistance:", 7, 136), 1, 135);
                form.add(checkBoxFields[54], 2, 135);
                form.add(fieldImageViewCreator(images.get("ignoreStatus")), 0, 136);
                form.add(labelANDTooltipMaker("Ignore Fire Resistance:", 7, 137), 1, 136);
                form.add(checkBoxFields[55], 2, 136);
                form.add(fieldImageViewCreator(images.get("ignoreStatus")), 0, 137);
                form.add(labelANDTooltipMaker("Ignore Ice Resistance:", 7, 138), 1, 137);
                form.add(checkBoxFields[56], 2, 137);
                form.add(fieldImageViewCreator(images.get("ignoreStatus")), 0, 138);
                form.add(labelANDTooltipMaker("Ignore Poison Resistance:", 7, 139), 1, 138);
                form.add(checkBoxFields[57], 2, 138);
                form.add(fieldImageViewCreator(images.get("ignoreStatus")), 0, 139);
                form.add(labelANDTooltipMaker("Ignore Explosive Resistance:", 7, 140), 1, 139);
                form.add(checkBoxFields[58], 2, 139);
                form.add(fieldImageViewCreator(images.get("ignoreStatus")), 0, 140);
                form.add(labelANDTooltipMaker("Ignore Volatile Explosive Resistance:", 7, 141), 1, 140);
                form.add(checkBoxFields[59], 2, 140);
                form.add(fieldImageViewCreator(images.get("ignoreStatus")), 0, 141);
                form.add(labelANDTooltipMaker("Ignore Payback Resistance:", 7, 142), 1, 141);
                form.add(checkBoxFields[60], 2, 141);
                form.add(fieldImageViewCreator(images.get("ignoreStatus")), 0, 142);
                form.add(labelANDTooltipMaker("Ignore Hold Fast Resistance:", 7, 143), 1, 142);
                form.add(checkBoxFields[61], 2, 142);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 143);
                form.add(labelANDTooltipMaker("Prefer Targeting Mario:", 7, 144), 1, 143);
                form.add(checkBoxFields[62], 2, 143);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 144);
                form.add(labelANDTooltipMaker("Prefer Targeting Partner:", 7, 145), 1, 144);
                form.add(checkBoxFields[63], 2, 144);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 145);
                form.add(labelANDTooltipMaker("Prefer Targeting Front:", 7, 146), 1, 145);
                form.add(checkBoxFields[64], 2, 145);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 146);
                form.add(labelANDTooltipMaker("Prefer Targeting Back:", 7, 147), 1, 146);
                form.add(checkBoxFields[65], 2, 146);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 147);
                form.add(labelANDTooltipMaker("Prefer Targeting Same Alliance:", 7, 148), 1, 147);
                form.add(checkBoxFields[66], 2, 147);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 148);
                form.add(labelANDTooltipMaker("Prefer Targeting Opposite Alliance:", 7, 149), 1, 148);
                form.add(checkBoxFields[67], 2, 148);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 149);
                form.add(labelANDTooltipMaker("Prefer Targeting Less Healthy:", 7, 150), 1, 149);
                form.add(checkBoxFields[68], 2, 149);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 150);
                form.add(labelANDTooltipMaker("Greatly Prefer Targeting Less Healthy:", 7, 151), 1, 150);
                form.add(checkBoxFields[69], 2, 150);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 151);
                form.add(labelANDTooltipMaker("Prefer Targeting Lower HP Target:", 7, 152), 1, 151);
                form.add(checkBoxFields[70], 2, 151);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 152);
                form.add(labelANDTooltipMaker("Prefer Targeting Higher HP Target:", 7, 153), 1, 152);
                form.add(checkBoxFields[71], 2, 152);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 153);
                form.add(labelANDTooltipMaker("Prefer Targeting Target in Peril:", 7, 154), 1, 153);
                form.add(checkBoxFields[72], 2, 153);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 154);
                form.add(labelANDTooltipMaker("Unknown TWF Flag 0x2000:", 7, 155), 1, 154);
                form.add(checkBoxFields[73], 2, 154);
                form.add(fieldImageViewCreator(images.get("selectCursor")), 0, 155);
                form.add(labelANDTooltipMaker("Choose Targeting Randomly:", 7, 156), 1, 155);
                form.add(checkBoxFields[74], 2, 155);

                centerMenu.getChildren().add(form);
                ScrollPane centerScroll = new ScrollPane();
                centerScroll.setContent(centerMenu);
                borderPane.setCenter(centerScroll);
            });
        });
    }

    /**
     * @Author Jemaroo
     * @Function Loads selected Unit Data into gui lists
     */
    private void loadSelectedUnit(int index) 
    {
        if (index >= 0 && index < units.size()) 
        {
            kindList.getItems().clear();
            defenseList.getItems().clear();
            weaponList.getItems().clear();

            BUTData unit = units.get(index);
            kindList.getItems().setAll(unit.BattleUnitKindData);
            kindList.getItems().addAll(unit.BattleUnitKindPartData);
            kindList.getItems().addAll(unit.HealthUpgradesData);
            defenseList.getItems().setAll(unit.BattleUnitDefenseData);
            defenseList.getItems().addAll(unit.BattleUnitDefenseAttrData);
            defenseList.getItems().addAll(unit.StatusVulnerabilityData);
            weaponList.getItems().setAll(unit.BattleWeaponData);
        }
    }

    /**
     * @Author Jemaroo
     * @Function Loads BattleUnit Array Data into text fields
     */
    private void loadStructFields(Object struct) 
    {
        if (struct instanceof BattleUnitKind) 
        {
            BattleUnitKind b = (BattleUnitKind) struct;
            textFields[0].setText(String.valueOf(b.HP));
            textFields[1].setText(String.valueOf(b.dangerHP));
            textFields[2].setText(String.valueOf(b.perilHP));
            textFields[3].setText(String.valueOf(b.level));
            textFields[4].setText(String.valueOf(b.bonusXP));
            textFields[5].setText(String.valueOf(b.bonusCoin));
            textFields[6].setText(String.valueOf(b.bonusCoinRate));
            textFields[7].setText(String.valueOf(b.baseCoin));
            textFields[8].setText(String.valueOf(b.runRate));
            textFields[9].setText(String.valueOf(b.pbCap));
            textFields[10].setText(String.valueOf(b.swallowChance));
            comboBoxFields[0].getSelectionModel().select(SAtoIndex(b.swallowAttribute));
            textFields[11].setText(String.valueOf(b.ultraHammerKnockChance));
            textFields[12].setText(String.valueOf(b.itemStealParameter));
            checkBoxFields[0].setSelected(b.MapObj);
            checkBoxFields[1].setSelected(b.OutOfReach);
            checkBoxFields[2].setSelected(b.Unquakeable);
            checkBoxFields[3].setSelected(b.IsInvisible);
            checkBoxFields[4].setSelected(b.IsVeiled);
            checkBoxFields[5].setSelected(b.ShellShielded);
            checkBoxFields[6].setSelected(b.NeverTargetable);
            checkBoxFields[7].setSelected(b.LimitSwitch);
            checkBoxFields[8].setSelected(b.DisableZeroGravityFloat);
            checkBoxFields[9].setSelected(b.DisableZeroGravityImmobility);
            checkBoxFields[10].setSelected(b.immuneToUltraHammerKnock);
            checkBoxFields[11].setSelected(b.IsUndead);
            checkBoxFields[12].setSelected(b.IsCorpse);
            checkBoxFields[13].setSelected(b.IsLeader);
            checkBoxFields[14].setSelected(b.CannotTakeActions);
            checkBoxFields[15].setSelected(b.NotSpunByLoveSlap);
            checkBoxFields[16].setSelected(b.DisableDamageStars);
            checkBoxFields[17].setSelected(b.DisableAllPartVisibility);
            checkBoxFields[18].setSelected(b.DisableHPGauge);
            checkBoxFields[19].setSelected(b.LookCamera);
            checkBoxFields[20].setSelected(b.NonCombatant);
            checkBoxFields[21].setSelected(b.NoShadow);
            checkBoxFields[22].setSelected(b.DisableDamage);
        } 
        else if (struct instanceof BattleUnitDefense) 
        {
            BattleUnitDefense b = (BattleUnitDefense) struct;
            textFields[0].setText(String.valueOf(b.normal));
            textFields[1].setText(String.valueOf(b.fire));
            textFields[2].setText(String.valueOf(b.ice));
            textFields[3].setText(String.valueOf(b.explosion));
            textFields[4].setText(String.valueOf(b.electric));
        }
        else if (struct instanceof BattleUnitDefenseAttr)
        {
            BattleUnitDefenseAttr b = (BattleUnitDefenseAttr) struct;
            comboBoxFields[0].getSelectionModel().select(b.normal);
            comboBoxFields[1].getSelectionModel().select(b.fire);
            comboBoxFields[2].getSelectionModel().select(b.ice);
            comboBoxFields[3].getSelectionModel().select(b.explosion);
            comboBoxFields[4].getSelectionModel().select(b.electric);
        }
        else if (struct instanceof StatusVulnerability)
        {
            StatusVulnerability b = (StatusVulnerability) struct;
            textFields[0].setText(String.valueOf(b.sleep));
            textFields[1].setText(String.valueOf(b.stop));
            textFields[2].setText(String.valueOf(b.dizzy));
            textFields[3].setText(String.valueOf(b.poison));
            textFields[4].setText(String.valueOf(b.confuse));
            textFields[5].setText(String.valueOf(b.electric));
            textFields[6].setText(String.valueOf(b.burn));
            textFields[7].setText(String.valueOf(b.freeze));
            textFields[8].setText(String.valueOf(b.huge));
            textFields[9].setText(String.valueOf(b.tiny));
            textFields[10].setText(String.valueOf(b.attack_up));
            textFields[11].setText(String.valueOf(b.attack_down));
            textFields[12].setText(String.valueOf(b.defense_up));
            textFields[13].setText(String.valueOf(b.defense_down));
            textFields[14].setText(String.valueOf(b.allergic));
            textFields[15].setText(String.valueOf(b.fright));
            textFields[16].setText(String.valueOf(b.gale_force));
            textFields[17].setText(String.valueOf(b.fast));
            textFields[18].setText(String.valueOf(b.slow));
            textFields[19].setText(String.valueOf(b.dodgy));
            textFields[20].setText(String.valueOf(b.invisible));
            textFields[21].setText(String.valueOf(b.ohko));
        }
        else if (struct instanceof BattleWeapon)
        {
            BattleWeapon b = (BattleWeapon) struct;
            textFields[0].setText(String.valueOf(b.accuracy));
            textFields[1].setText(String.valueOf(b.fp_cost));
            textFields[2].setText(String.valueOf(b.sp_cost));
            comboBoxFields[0].getSelectionModel().select(b.superguard_state);
            textFields[3].setText(String.valueOf(b.sylish_multiplier));
            textFields[4].setText(String.valueOf(b.bingo_slot_inc_chance));
            comboBoxFields[1].getSelectionModel().select(BDFNtoIndex(b.base_damage_fn));
            textFields[5].setText(String.valueOf(b.base_damage1));
            textFields[6].setText(String.valueOf(b.base_damage2));
            textFields[7].setText(String.valueOf(b.base_damage3));
            textFields[8].setText(String.valueOf(b.base_damage4));
            textFields[9].setText(String.valueOf(b.base_damage5));
            textFields[10].setText(String.valueOf(b.base_damage6));
            textFields[11].setText(String.valueOf(b.base_damage7));
            textFields[12].setText(String.valueOf(b.base_damage8));
            textFields[13].setText(String.valueOf(b.base_fpdamage1));
            textFields[14].setText(String.valueOf(b.base_fpdamage2));
            textFields[15].setText(String.valueOf(b.base_fpdamage3));
            textFields[16].setText(String.valueOf(b.base_fpdamage4));
            textFields[17].setText(String.valueOf(b.base_fpdamage5));
            textFields[18].setText(String.valueOf(b.base_fpdamage6));
            textFields[19].setText(String.valueOf(b.base_fpdamage7));
            textFields[20].setText(String.valueOf(b.base_fpdamage8));
            checkBoxFields[0].setSelected(b.CannotTargetMarioOrShellShield);
            checkBoxFields[1].setSelected(b.CannotTargetPartner);
            checkBoxFields[2].setSelected(b.CannotTargetEnemy);
            checkBoxFields[3].setSelected(b.CannotTargetTreeOrSwitch);
            checkBoxFields[4].setSelected(b.CannotTargetSystem);
            checkBoxFields[5].setSelected(b.CannotTargetOppositeAlliance);
            checkBoxFields[6].setSelected(b.CannotTargetOwnAlliance);
            checkBoxFields[7].setSelected(b.CannotTargetSelf);
            checkBoxFields[8].setSelected(b.CannotTargetSameSpecies);
            checkBoxFields[9].setSelected(b.OnlyTargetSelf);
            checkBoxFields[10].setSelected(b.OnlyTargetMario);
            checkBoxFields[11].setSelected(b.OnlyTargetTreeOrSwitch);
            checkBoxFields[12].setSelected(b.OnlyTargetPreferredParts);
            checkBoxFields[13].setSelected(b.OnlyTargetSelectParts);
            checkBoxFields[14].setSelected(b.SingleTarget);
            checkBoxFields[15].setSelected(b.MultipleTarget);
            checkBoxFields[16].setSelected(b.CannotTargetAnything);
            checkBoxFields[17].setSelected(b.Tattlelike);
            checkBoxFields[18].setSelected(b.CannotTargetFloating2);
            checkBoxFields[19].setSelected(b.CannotTargetCeiling);
            checkBoxFields[20].setSelected(b.CannotTargetFloating);
            checkBoxFields[21].setSelected(b.CannotTargetGrounded);
            checkBoxFields[22].setSelected(b.Jumplike);
            checkBoxFields[23].setSelected(b.Hammerlike);
            checkBoxFields[24].setSelected(b.ShellTosslike);
            checkBoxFields[25].setSelected(b.CannotTargetGroundedVariant);
            checkBoxFields[26].setSelected(b.RecoilDamage);
            checkBoxFields[27].setSelected(b.CanOnlyTargetFrontmost);
            checkBoxFields[28].setSelected(b.CannotTargetShellShield);
            checkBoxFields[29].setSelected(b.CannotTargetCustom);
            checkBoxFields[30].setSelected(b.TargetSameAllianceDirection);
            checkBoxFields[31].setSelected(b.TargetOppositeAllianceDirection);
            comboBoxFields[2].getSelectionModel().select(b.element);
            comboBoxFields[3].getSelectionModel().select(DPtoIndex(b.damage_pattern));
            comboBoxFields[4].getSelectionModel().select(b.ac_level);
            checkBoxFields[32].setSelected(b.BadgeCanAffectPower);
            checkBoxFields[33].setSelected(b.StatusCanAffectPower);
            checkBoxFields[34].setSelected(b.IsChargeable);
            checkBoxFields[35].setSelected(b.CannotMiss);
            checkBoxFields[36].setSelected(b.DiminishingReturnsByHit);
            checkBoxFields[37].setSelected(b.DiminishingReturnsByTarget);
            checkBoxFields[38].setSelected(b.PiercesDefense);
            checkBoxFields[39].setSelected(b.CanBreakIce);
            checkBoxFields[40].setSelected(b.IgnoreTargetStatusVulnerability);
            checkBoxFields[41].setSelected(b.SPFx200);
            checkBoxFields[42].setSelected(b.IgnitesIfBurned);
            checkBoxFields[43].setSelected(b.PlayActiveFXSound);
            checkBoxFields[44].setSelected(b.FlipsShellEnemies);
            checkBoxFields[45].setSelected(b.FlipsBombFlippableEnemies);
            checkBoxFields[46].setSelected(b.GroundsWingedEnemies);
            checkBoxFields[47].setSelected(b.SPFx8000);
            checkBoxFields[48].setSelected(b.CanBeUsedAsConfusedAction);
            checkBoxFields[49].setSelected(b.Unguardable);
            checkBoxFields[50].setSelected(b.CanHitClonelike);
            checkBoxFields[51].setSelected(b.Electric);
            checkBoxFields[52].setSelected(b.TopSpiky);
            checkBoxFields[53].setSelected(b.PreemptiveFrontSpiky);
            checkBoxFields[54].setSelected(b.FrontSpiky);
            checkBoxFields[55].setSelected(b.Fiery);
            checkBoxFields[56].setSelected(b.Icy);
            checkBoxFields[57].setSelected(b.Poison);
            checkBoxFields[58].setSelected(b.Explosive);
            checkBoxFields[59].setSelected(b.VolatileExplosive);
            checkBoxFields[60].setSelected(b.Payback);
            checkBoxFields[61].setSelected(b.HoldFast);
            checkBoxFields[62].setSelected(b.PreferMario);
            checkBoxFields[63].setSelected(b.PreferPartner);
            checkBoxFields[64].setSelected(b.PreferFront);
            checkBoxFields[65].setSelected(b.PreferBack);
            checkBoxFields[66].setSelected(b.PreferSameAlliance);
            checkBoxFields[67].setSelected(b.PreferOppositeAlliance);
            checkBoxFields[68].setSelected(b.PreferLessHealthy);
            checkBoxFields[69].setSelected(b.GreatlyPreferLessHealthy);
            checkBoxFields[70].setSelected(b.PreferLowerHP);
            checkBoxFields[71].setSelected(b.PreferHigherHP);
            checkBoxFields[72].setSelected(b.PreferInPeril);
            checkBoxFields[73].setSelected(b.TWFx2000);
            checkBoxFields[74].setSelected(b.ChooseWeightedRandomly);
            textFields[21].setText(String.valueOf(b.sleep_chance));
            textFields[22].setText(String.valueOf(b.sleep_time));
            textFields[23].setText(String.valueOf(b.stop_chance));
            textFields[24].setText(String.valueOf(b.stop_time));
            textFields[25].setText(String.valueOf(b.dizzy_chance));
            textFields[26].setText(String.valueOf(b.dizzy_time));
            textFields[27].setText(String.valueOf(b.poison_chance));
            textFields[28].setText(String.valueOf(b.poison_time));
            textFields[29].setText(String.valueOf(b.poison_strength));
            textFields[30].setText(String.valueOf(b.confuse_chance));
            textFields[31].setText(String.valueOf(b.confuse_time));
            textFields[32].setText(String.valueOf(b.electric_chance));
            textFields[33].setText(String.valueOf(b.electric_time));
            textFields[34].setText(String.valueOf(b.dodgy_chance));
            textFields[35].setText(String.valueOf(b.dodgy_time));
            textFields[36].setText(String.valueOf(b.burn_chance));
            textFields[37].setText(String.valueOf(b.burn_time));
            textFields[38].setText(String.valueOf(b.freeze_chance));
            textFields[39].setText(String.valueOf(b.freeze_time));
            textFields[40].setText(String.valueOf(b.size_change_chance));
            textFields[41].setText(String.valueOf(b.size_change_time));
            textFields[42].setText(String.valueOf(b.size_change_strength));
            textFields[43].setText(String.valueOf(b.atk_change_chance));
            textFields[44].setText(String.valueOf(b.atk_change_time));
            textFields[45].setText(String.valueOf(b.atk_change_strength));
            textFields[46].setText(String.valueOf(b.def_change_chance));
            textFields[47].setText(String.valueOf(b.def_change_time));
            textFields[48].setText(String.valueOf(b.def_change_strength));
            textFields[49].setText(String.valueOf(b.allergic_chance));
            textFields[50].setText(String.valueOf(b.allergic_time));
            textFields[51].setText(String.valueOf(b.ohko_chance));
            textFields[52].setText(String.valueOf(b.charge_strength));
            textFields[53].setText(String.valueOf(b.fast_chance));
            textFields[54].setText(String.valueOf(b.fast_time));
            textFields[55].setText(String.valueOf(b.slow_chance));
            textFields[56].setText(String.valueOf(b.slow_time));
            textFields[57].setText(String.valueOf(b.fright_chance));
            textFields[58].setText(String.valueOf(b.gale_force_chance));
            textFields[59].setText(String.valueOf(b.payback_time));
            textFields[60].setText(String.valueOf(b.hold_fast_time));
            textFields[61].setText(String.valueOf(b.invisible_chance));
            textFields[62].setText(String.valueOf(b.invisible_time));
            textFields[63].setText(String.valueOf(b.hp_regen_time));
            textFields[64].setText(String.valueOf(b.hp_regen_strength));
            textFields[65].setText(String.valueOf(b.fp_regen_time));
            textFields[66].setText(String.valueOf(b.fp_regen_strength));
            comboBoxFields[5].getSelectionModel().select(AStoIndex(b.attack_evt));
            textFields[67].setText(String.valueOf(b.stage_background_fallweight1));
            textFields[68].setText(String.valueOf(b.stage_background_fallweight2));
            textFields[69].setText(String.valueOf(b.stage_background_fallweight3));
            textFields[70].setText(String.valueOf(b.stage_background_fallweight4));
            textFields[71].setText(String.valueOf(b.stage_nozzle_turn_chance));
            textFields[72].setText(String.valueOf(b.stage_nozzle_fire_chance));
            textFields[73].setText(String.valueOf(b.stage_ceiling_fall_chance));
            textFields[74].setText(String.valueOf(b.stage_object_fall_chance));
        }
        else if (struct instanceof BattleUnitKindPart)
        {
            BattleUnitKindPart b = (BattleUnitKindPart) struct;
            checkBoxFields[0].setSelected(b.MainBodyPart);
            checkBoxFields[1].setSelected(b.SecondaryBodyPart);
            checkBoxFields[2].setSelected(b.BombableBodyPart);
            checkBoxFields[3].setSelected(b.GuardBodyPart);
            checkBoxFields[4].setSelected(b.NotBombableBodyPart);
            checkBoxFields[5].setSelected(b.InHole);
            checkBoxFields[6].setSelected(b.WeakToAttackFxR);
            checkBoxFields[7].setSelected(b.WeakToIcePower);
            checkBoxFields[8].setSelected(b.IsWinged);
            checkBoxFields[9].setSelected(b.IsShelled);
            checkBoxFields[10].setSelected(b.IsBombFlippable);
            checkBoxFields[11].setSelected(b.IsClonelike);
            checkBoxFields[12].setSelected(b.DisableFlatPaperLayering);
            checkBoxFields[13].setSelected(b.NeverTargetable);
            checkBoxFields[14].setSelected(b.IgnoreMapObjectOffset);
            checkBoxFields[15].setSelected(b.IgnoreOnlyTargetSelectAndPreferredParts);
            checkBoxFields[16].setSelected(b.Untattleable);
            checkBoxFields[17].setSelected(b.JumplikeCannotTarget);
            checkBoxFields[18].setSelected(b.HammerlikeCannotTarget);
            checkBoxFields[19].setSelected(b.ShellTosslikeCannotTarget);
            checkBoxFields[20].setSelected(b.PreventHealthDecrease);
            checkBoxFields[21].setSelected(b.DisablePartVisibility);
            checkBoxFields[22].setSelected(b.ImmuneToCustom);
            checkBoxFields[23].setSelected(b.BlurOn);
            checkBoxFields[24].setSelected(b.ScaleIndependence);
            checkBoxFields[25].setSelected(b.Independence);
            checkBoxFields[26].setSelected(b.IsImmuneToDamageOrStatus);
            checkBoxFields[27].setSelected(b.IsImmuneToOHKO);
            checkBoxFields[28].setSelected(b.IsImmuneToStatus);
            checkBoxFields[29].setSelected(b.TopSpiky);
            checkBoxFields[30].setSelected(b.PreemptiveFrontSpiky);
            checkBoxFields[31].setSelected(b.FrontSpiky);
            checkBoxFields[32].setSelected(b.Fiery);
            checkBoxFields[33].setSelected(b.FieryStatus);
            checkBoxFields[34].setSelected(b.Icy);
            checkBoxFields[35].setSelected(b.IcyStatus);
            checkBoxFields[36].setSelected(b.Poison);
            checkBoxFields[37].setSelected(b.PoisonStatus);
            checkBoxFields[38].setSelected(b.Electric);
            checkBoxFields[39].setSelected(b.ElectricStatus);
            checkBoxFields[40].setSelected(b.Explosive);
            checkBoxFields[41].setSelected(b.VolatileExplosive);
        }
        else if (struct instanceof HealthUpgrades)
        {
            HealthUpgrades b = (HealthUpgrades) struct;
            textFields[0].setText(String.valueOf(b.startHP));
            textFields[1].setText(String.valueOf(b.startFP));
            textFields[2].setText(String.valueOf(b.startBP));
            textFields[3].setText(String.valueOf(b.upgradeHP));
            textFields[4].setText(String.valueOf(b.upgradeFP));
            textFields[5].setText(String.valueOf(b.upgradeBP));
        }
    }

    /**
     * @Author Jemaroo
     * @Function Saves the text fields to the loaded BattleUnit Data
     */
    private void saveFieldsToSelectedStruct() 
    {
        Object selected = null;
        if (kindList.getSelectionModel().getSelectedItem() != null) 
        {
            selected = kindList.getSelectionModel().getSelectedItem();
        } 
        else if (defenseList.getSelectionModel().getSelectedItem() != null) 
        {
            selected = defenseList.getSelectionModel().getSelectedItem();
        }
        else if (weaponList.getSelectionModel().getSelectedItem() != null)
        {
            selected = weaponList.getSelectionModel().getSelectedItem();
        }

        if (selected instanceof BattleUnitKind b) 
        {
            b.HP = Integer.parseInt(textFields[0].getText());
            b.dangerHP = Integer.parseInt(textFields[1].getText());
            b.perilHP = Integer.parseInt(textFields[2].getText());
            b.level = Integer.parseInt(textFields[3].getText());
            b.bonusXP = Integer.parseInt(textFields[4].getText());
            b.bonusCoin = Integer.parseInt(textFields[5].getText());
            b.bonusCoinRate = Integer.parseInt(textFields[6].getText());
            b.baseCoin = Integer.parseInt(textFields[7].getText());
            b.runRate = Integer.parseInt(textFields[8].getText());
            b.pbCap = Integer.parseInt(textFields[9].getText());
            b.swallowChance = Integer.parseInt(textFields[10].getText());
            b.swallowAttribute = IndexToSA(comboBoxFields[0].getSelectionModel().getSelectedIndex());
            b.ultraHammerKnockChance = Integer.parseInt(textFields[11].getText());
            b.itemStealParameter = Integer.parseInt(textFields[12].getText());
            b.MapObj = checkBoxFields[0].isSelected();
            b.OutOfReach = checkBoxFields[1].isSelected();
            b.Unquakeable = checkBoxFields[2].isSelected();
            b.IsInvisible = checkBoxFields[3].isSelected();
            b.IsVeiled = checkBoxFields[4].isSelected();
            b.ShellShielded = checkBoxFields[5].isSelected();
            b.NeverTargetable = checkBoxFields[6].isSelected();
            b.LimitSwitch = checkBoxFields[7].isSelected();
            b.DisableZeroGravityFloat = checkBoxFields[8].isSelected();
            b.DisableZeroGravityImmobility = checkBoxFields[9].isSelected();
            b.immuneToUltraHammerKnock = checkBoxFields[10].isSelected();
            b.IsUndead = checkBoxFields[11].isSelected();
            b.IsCorpse = checkBoxFields[12].isSelected();
            b.IsLeader = checkBoxFields[13].isSelected();
            b.CannotTakeActions = checkBoxFields[14].isSelected();
            b.NotSpunByLoveSlap = checkBoxFields[15].isSelected();
            b.DisableDamageStars = checkBoxFields[16].isSelected();
            b.DisableAllPartVisibility = checkBoxFields[17].isSelected();
            b.DisableHPGauge = checkBoxFields[18].isSelected();
            b.LookCamera = checkBoxFields[19].isSelected();
            b.NonCombatant = checkBoxFields[20].isSelected();
            b.NoShadow = checkBoxFields[21].isSelected();
            b.DisableDamage = checkBoxFields[22].isSelected();
        } 
        else if (selected instanceof BattleUnitDefense b) 
        {
            b.normal = Integer.parseInt(textFields[0].getText());
            b.fire = Integer.parseInt(textFields[1].getText());
            b.ice = Integer.parseInt(textFields[2].getText());
            b.explosion = Integer.parseInt(textFields[3].getText());
            b.electric = Integer.parseInt(textFields[4].getText());
        }
        else if (selected instanceof BattleUnitDefenseAttr b)
        {
            b.normal = comboBoxFields[0].getSelectionModel().getSelectedIndex();
            b.fire = comboBoxFields[1].getSelectionModel().getSelectedIndex();
            b.ice = comboBoxFields[2].getSelectionModel().getSelectedIndex();
            b.explosion = comboBoxFields[3].getSelectionModel().getSelectedIndex();
            b.electric = comboBoxFields[4].getSelectionModel().getSelectedIndex();
        }
        else if (selected instanceof StatusVulnerability b)
        {
            b.sleep = Integer.parseInt(textFields[0].getText());
            b.stop = Integer.parseInt(textFields[1].getText());
            b.dizzy = Integer.parseInt(textFields[2].getText());
            b.poison = Integer.parseInt(textFields[3].getText());
            b.confuse = Integer.parseInt(textFields[4].getText());
            b.electric = Integer.parseInt(textFields[5].getText());
            b.burn = Integer.parseInt(textFields[6].getText());
            b.freeze = Integer.parseInt(textFields[7].getText());
            b.huge = Integer.parseInt(textFields[8].getText());
            b.tiny = Integer.parseInt(textFields[9].getText());
            b.attack_up = Integer.parseInt(textFields[10].getText());
            b.attack_down = Integer.parseInt(textFields[11].getText());
            b.defense_up = Integer.parseInt(textFields[12].getText());
            b.defense_down = Integer.parseInt(textFields[13].getText());
            b.allergic = Integer.parseInt(textFields[14].getText());
            b.fright = Integer.parseInt(textFields[15].getText());
            b.gale_force = Integer.parseInt(textFields[16].getText());
            b.fast = Integer.parseInt(textFields[17].getText());
            b.slow = Integer.parseInt(textFields[18].getText());
            b.dodgy = Integer.parseInt(textFields[19].getText());
            b.invisible = Integer.parseInt(textFields[20].getText());
            b.ohko = Integer.parseInt(textFields[21].getText());
        }
        else if (selected instanceof BattleWeapon b)
        {
            b.accuracy = Integer.parseInt(textFields[0].getText());
            b.fp_cost = Integer.parseInt(textFields[1].getText());
            b.sp_cost = Integer.parseInt(textFields[2].getText());
            b.superguard_state = comboBoxFields[0].getSelectionModel().getSelectedIndex();
            b.sylish_multiplier = Integer.parseInt(textFields[3].getText());
            b.bingo_slot_inc_chance = Integer.parseInt(textFields[4].getText());
            b.base_damage_fn = IndexToBDFN(comboBoxFields[1].getSelectionModel().getSelectedIndex());
            b.base_damage1 = Integer.parseInt(textFields[5].getText());
            b.base_damage2 = Integer.parseInt(textFields[6].getText());
            b.base_damage3 = Integer.parseInt(textFields[7].getText());
            b.base_damage4 = Integer.parseInt(textFields[8].getText());
            b.base_damage5 = Integer.parseInt(textFields[9].getText());
            b.base_damage6 = Integer.parseInt(textFields[10].getText());
            b.base_damage7 = Integer.parseInt(textFields[11].getText());
            b.base_damage8 = Integer.parseInt(textFields[12].getText());
            b.base_fpdamage1 = Integer.parseInt(textFields[13].getText());
            b.base_fpdamage2 = Integer.parseInt(textFields[14].getText());
            b.base_fpdamage3 = Integer.parseInt(textFields[15].getText());
            b.base_fpdamage4 = Integer.parseInt(textFields[16].getText());
            b.base_fpdamage5 = Integer.parseInt(textFields[17].getText());
            b.base_fpdamage6 = Integer.parseInt(textFields[18].getText());
            b.base_fpdamage7 = Integer.parseInt(textFields[19].getText());
            b.base_fpdamage8 = Integer.parseInt(textFields[20].getText());
            b.CannotTargetMarioOrShellShield = checkBoxFields[0].isSelected();
            b.CannotTargetPartner = checkBoxFields[1].isSelected();
            b.CannotTargetEnemy = checkBoxFields[2].isSelected();
            b.CannotTargetTreeOrSwitch = checkBoxFields[3].isSelected();
            b.CannotTargetSystem = checkBoxFields[4].isSelected();
            b.CannotTargetOppositeAlliance = checkBoxFields[5].isSelected();
            b.CannotTargetOwnAlliance = checkBoxFields[6].isSelected();
            b.CannotTargetSelf = checkBoxFields[7].isSelected();
            b.CannotTargetSameSpecies = checkBoxFields[8].isSelected();
            b.OnlyTargetSelf = checkBoxFields[9].isSelected();
            b.OnlyTargetMario = checkBoxFields[10].isSelected();
            b.OnlyTargetTreeOrSwitch = checkBoxFields[11].isSelected();
            b.OnlyTargetPreferredParts = checkBoxFields[12].isSelected();
            b.OnlyTargetSelectParts = checkBoxFields[13].isSelected();
            b.SingleTarget = checkBoxFields[14].isSelected();
            b.MultipleTarget = checkBoxFields[15].isSelected();
            b.CannotTargetAnything = checkBoxFields[16].isSelected();
            b.Tattlelike = checkBoxFields[17].isSelected();
            b.CannotTargetFloating2 = checkBoxFields[18].isSelected();
            b.CannotTargetCeiling = checkBoxFields[19].isSelected();
            b.CannotTargetFloating = checkBoxFields[20].isSelected();
            b.CannotTargetGrounded = checkBoxFields[21].isSelected();
            b.Jumplike = checkBoxFields[22].isSelected();
            b.Hammerlike = checkBoxFields[23].isSelected();
            b.ShellTosslike = checkBoxFields[24].isSelected();
            b.CannotTargetGroundedVariant = checkBoxFields[25].isSelected();
            b.RecoilDamage = checkBoxFields[26].isSelected();
            b.CanOnlyTargetFrontmost = checkBoxFields[27].isSelected();
            b.CannotTargetShellShield = checkBoxFields[28].isSelected();
            b.CannotTargetCustom = checkBoxFields[29].isSelected();
            b.TargetSameAllianceDirection = checkBoxFields[30].isSelected();
            b.TargetOppositeAllianceDirection = checkBoxFields[31].isSelected();
            b.element = comboBoxFields[2].getSelectionModel().getSelectedIndex();
            b.damage_pattern = IndexToDP(comboBoxFields[3].getSelectionModel().getSelectedIndex());
            b.ac_level = comboBoxFields[4].getSelectionModel().getSelectedIndex();
            b.BadgeCanAffectPower = checkBoxFields[32].isSelected();
            b.StatusCanAffectPower = checkBoxFields[33].isSelected();
            b.IsChargeable = checkBoxFields[34].isSelected();
            b.CannotMiss = checkBoxFields[35].isSelected();
            b.DiminishingReturnsByHit = checkBoxFields[36].isSelected();
            b.DiminishingReturnsByTarget = checkBoxFields[37].isSelected();
            b.PiercesDefense = checkBoxFields[38].isSelected();
            b.CanBreakIce = checkBoxFields[39].isSelected();
            b.IgnoreTargetStatusVulnerability = checkBoxFields[40].isSelected();
            b.SPFx200 = checkBoxFields[41].isSelected();
            b.IgnitesIfBurned = checkBoxFields[42].isSelected();
            b.PlayActiveFXSound = checkBoxFields[43].isSelected();
            b.FlipsShellEnemies = checkBoxFields[44].isSelected();
            b.FlipsBombFlippableEnemies = checkBoxFields[45].isSelected();
            b.GroundsWingedEnemies = checkBoxFields[46].isSelected();
            b.SPFx8000 = checkBoxFields[47].isSelected();
            b.CanBeUsedAsConfusedAction = checkBoxFields[48].isSelected();
            b.Unguardable = checkBoxFields[49].isSelected();
            b.CanHitClonelike = checkBoxFields[50].isSelected();
            b.Electric = checkBoxFields[51].isSelected();
            b.TopSpiky = checkBoxFields[52].isSelected();
            b.PreemptiveFrontSpiky = checkBoxFields[53].isSelected();
            b.FrontSpiky = checkBoxFields[54].isSelected();
            b.Fiery = checkBoxFields[55].isSelected();
            b.Icy = checkBoxFields[56].isSelected();
            b.Poison = checkBoxFields[57].isSelected();
            b.Explosive = checkBoxFields[58].isSelected();
            b.VolatileExplosive = checkBoxFields[59].isSelected();
            b.Payback = checkBoxFields[60].isSelected();
            b.HoldFast = checkBoxFields[61].isSelected();
            b.PreferMario = checkBoxFields[62].isSelected();
            b.PreferPartner = checkBoxFields[63].isSelected();
            b.PreferFront = checkBoxFields[64].isSelected();
            b.PreferBack = checkBoxFields[65].isSelected();
            b.PreferSameAlliance = checkBoxFields[66].isSelected();
            b.PreferOppositeAlliance = checkBoxFields[67].isSelected();
            b.PreferLessHealthy = checkBoxFields[68].isSelected();
            b.GreatlyPreferLessHealthy = checkBoxFields[69].isSelected();
            b.PreferLowerHP = checkBoxFields[70].isSelected();
            b.PreferHigherHP = checkBoxFields[71].isSelected();
            b.PreferInPeril = checkBoxFields[72].isSelected();
            b.TWFx2000 = checkBoxFields[73].isSelected();
            b.ChooseWeightedRandomly = checkBoxFields[74].isSelected();
            b.sleep_chance = Integer.parseInt(textFields[21].getText());
            b.sleep_time = Integer.parseInt(textFields[22].getText());
            b.stop_chance = Integer.parseInt(textFields[23].getText());
            b.stop_time = Integer.parseInt(textFields[24].getText());
            b.dizzy_chance = Integer.parseInt(textFields[25].getText());
            b.dizzy_time = Integer.parseInt(textFields[26].getText());
            b.poison_chance = Integer.parseInt(textFields[27].getText());
            b.poison_time = Integer.parseInt(textFields[28].getText());
            b.poison_strength = Integer.parseInt(textFields[29].getText());
            b.confuse_chance = Integer.parseInt(textFields[30].getText());
            b.confuse_time = Integer.parseInt(textFields[31].getText());
            b.electric_chance = Integer.parseInt(textFields[32].getText());
            b.electric_time = Integer.parseInt(textFields[33].getText());
            b.dodgy_chance = Integer.parseInt(textFields[34].getText());
            b.dodgy_time = Integer.parseInt(textFields[35].getText());
            b.burn_chance = Integer.parseInt(textFields[36].getText());
            b.burn_time = Integer.parseInt(textFields[37].getText());
            b.freeze_chance = Integer.parseInt(textFields[38].getText());
            b.freeze_time = Integer.parseInt(textFields[39].getText());
            b.size_change_chance = Integer.parseInt(textFields[40].getText());
            b.size_change_time = Integer.parseInt(textFields[41].getText());
            b.size_change_strength = Integer.parseInt(textFields[42].getText());
            b.atk_change_chance = Integer.parseInt(textFields[43].getText());
            b.atk_change_time = Integer.parseInt(textFields[44].getText());
            b.atk_change_strength = Integer.parseInt(textFields[45].getText());
            b.def_change_chance = Integer.parseInt(textFields[46].getText());
            b.def_change_time = Integer.parseInt(textFields[47].getText());
            b.def_change_strength = Integer.parseInt(textFields[48].getText());
            b.allergic_chance = Integer.parseInt(textFields[49].getText());
            b.allergic_time = Integer.parseInt(textFields[50].getText());
            b.ohko_chance = Integer.parseInt(textFields[51].getText());
            b.charge_strength = Integer.parseInt(textFields[52].getText());
            b.fast_chance = Integer.parseInt(textFields[53].getText());
            b.fast_time = Integer.parseInt(textFields[54].getText());
            b.slow_chance = Integer.parseInt(textFields[55].getText());
            b.slow_time = Integer.parseInt(textFields[56].getText());
            b.fright_chance = Integer.parseInt(textFields[57].getText());
            b.gale_force_chance = Integer.parseInt(textFields[58].getText());
            b.payback_time = Integer.parseInt(textFields[59].getText());
            b.hold_fast_time = Integer.parseInt(textFields[60].getText());
            b.invisible_chance = Integer.parseInt(textFields[61].getText());
            b.invisible_time = Integer.parseInt(textFields[62].getText());
            b.hp_regen_time = Integer.parseInt(textFields[63].getText());
            b.hp_regen_strength = Integer.parseInt(textFields[64].getText());
            b.fp_regen_time = Integer.parseInt(textFields[65].getText());
            b.fp_regen_strength = Integer.parseInt(textFields[66].getText());
            b.attack_evt = IndexToAS(comboBoxFields[5].getSelectionModel().getSelectedIndex());
            b.stage_background_fallweight1 = Integer.parseInt(textFields[67].getText());
            b.stage_background_fallweight2 = Integer.parseInt(textFields[68].getText());
            b.stage_background_fallweight3 = Integer.parseInt(textFields[69].getText());
            b.stage_background_fallweight4 = Integer.parseInt(textFields[70].getText());
            b.stage_nozzle_turn_chance = Integer.parseInt(textFields[71].getText());
            b.stage_nozzle_fire_chance = Integer.parseInt(textFields[72].getText());
            b.stage_ceiling_fall_chance = Integer.parseInt(textFields[73].getText());
            b.stage_object_fall_chance = Integer.parseInt(textFields[74].getText());
        }
        else if (selected instanceof BattleUnitKindPart b)
        {
            b.MainBodyPart = checkBoxFields[0].isSelected();
            b.SecondaryBodyPart = checkBoxFields[1].isSelected();
            b.BombableBodyPart = checkBoxFields[2].isSelected();
            b.GuardBodyPart = checkBoxFields[3].isSelected();
            b.NotBombableBodyPart = checkBoxFields[4].isSelected();
            b.InHole = checkBoxFields[5].isSelected();
            b.WeakToAttackFxR = checkBoxFields[6].isSelected();
            b.WeakToIcePower = checkBoxFields[7].isSelected();
            b.IsWinged = checkBoxFields[8].isSelected();
            b.IsShelled = checkBoxFields[9].isSelected();
            b.IsBombFlippable = checkBoxFields[10].isSelected();
            b.IsClonelike = checkBoxFields[11].isSelected();
            b.DisableFlatPaperLayering = checkBoxFields[12].isSelected();
            b.NeverTargetable = checkBoxFields[13].isSelected();
            b.IgnoreMapObjectOffset = checkBoxFields[14].isSelected();
            b.IgnoreOnlyTargetSelectAndPreferredParts = checkBoxFields[15].isSelected();
            b.Untattleable = checkBoxFields[16].isSelected();
            b.JumplikeCannotTarget = checkBoxFields[17].isSelected();
            b.HammerlikeCannotTarget = checkBoxFields[18].isSelected();
            b.ShellTosslikeCannotTarget = checkBoxFields[19].isSelected();
            b.PreventHealthDecrease = checkBoxFields[20].isSelected();
            b.DisablePartVisibility = checkBoxFields[21].isSelected();
            b.ImmuneToCustom = checkBoxFields[22].isSelected();
            b.BlurOn = checkBoxFields[23].isSelected();
            b.ScaleIndependence = checkBoxFields[24].isSelected();
            b.Independence = checkBoxFields[25].isSelected();
            b.IsImmuneToDamageOrStatus = checkBoxFields[26].isSelected();
            b.IsImmuneToOHKO = checkBoxFields[27].isSelected();
            b.IsImmuneToStatus = checkBoxFields[28].isSelected();
            b.TopSpiky = checkBoxFields[29].isSelected();
            b.PreemptiveFrontSpiky = checkBoxFields[30].isSelected();
            b.FrontSpiky = checkBoxFields[31].isSelected();
            b.Fiery = checkBoxFields[32].isSelected();
            b.FieryStatus = checkBoxFields[33].isSelected();
            b.Icy = checkBoxFields[34].isSelected();
            b.IcyStatus = checkBoxFields[35].isSelected();
            b.Poison = checkBoxFields[36].isSelected();
            b.PoisonStatus = checkBoxFields[37].isSelected();
            b.Electric = checkBoxFields[38].isSelected();
            b.ElectricStatus = checkBoxFields[39].isSelected();
            b.Explosive = checkBoxFields[40].isSelected();
            b.VolatileExplosive = checkBoxFields[41].isSelected();
        }
        else if (selected instanceof HealthUpgrades b)
        {
            b.startHP = Integer.parseInt(textFields[0].getText());
            b.startFP = Integer.parseInt(textFields[1].getText());
            b.startBP = Integer.parseInt(textFields[2].getText());
            b.upgradeHP = Integer.parseInt(textFields[3].getText());
            b.upgradeFP = Integer.parseInt(textFields[4].getText());
            b.upgradeBP = Integer.parseInt(textFields[5].getText());
        }
    }

    /**
     * @Author Jemaroo
     * @Function Saves the preset fields into the variables
     */
    private void savePresetFieldsToSelectedStruct(Object preset) 
    {
        if (preset instanceof BattleUnitKind b) 
        {
            
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).HP = b.HP;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).dangerHP = b.dangerHP;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).perilHP = b.perilHP;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).level = b.level;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).bonusXP = b.bonusXP;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).bonusCoin = b.bonusCoin;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).bonusCoinRate = b.bonusCoinRate;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).baseCoin = b.baseCoin;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).runRate = b.runRate;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).pbCap = b.pbCap;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).swallowChance = b.swallowChance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).swallowAttribute = b.swallowAttribute;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).ultraHammerKnockChance = b.ultraHammerKnockChance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).itemStealParameter = b.itemStealParameter;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).MapObj = b.MapObj;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).OutOfReach = b.OutOfReach;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).Unquakeable = b.Unquakeable;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).IsInvisible = b.IsInvisible;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).IsVeiled = b.IsVeiled;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).ShellShielded = b.ShellShielded;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).NeverTargetable = b.NeverTargetable;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).LimitSwitch = b.LimitSwitch;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).DisableZeroGravityFloat = b.DisableZeroGravityFloat;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).DisableZeroGravityImmobility = b.DisableZeroGravityImmobility;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).immuneToUltraHammerKnock = b.immuneToUltraHammerKnock;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).IsUndead = b.IsUndead;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).IsCorpse = b.IsCorpse;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).IsLeader = b.IsLeader;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).CannotTakeActions = b.CannotTakeActions;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).NotSpunByLoveSlap = b.NotSpunByLoveSlap;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).DisableDamageStars = b.DisableDamageStars;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).DisableAllPartVisibility = b.DisableAllPartVisibility;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).DisableHPGauge = b.DisableHPGauge;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).LookCamera = b.LookCamera;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).NonCombatant = b.NonCombatant;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).NoShadow = b.NoShadow;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.get(kindList.getSelectionModel().getSelectedIndex()).DisableDamage = b.DisableDamage;
        } 
        else if (preset instanceof BattleUnitDefense b) 
        {
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitDefenseData.get(defenseList.getSelectionModel().getSelectedIndex()).normal = b.normal;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitDefenseData.get(defenseList.getSelectionModel().getSelectedIndex()).fire = b.fire;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitDefenseData.get(defenseList.getSelectionModel().getSelectedIndex()).ice = b.ice;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitDefenseData.get(defenseList.getSelectionModel().getSelectedIndex()).explosion = b.explosion;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitDefenseData.get(defenseList.getSelectionModel().getSelectedIndex()).electric = b.electric;
        }
        else if (preset instanceof BattleUnitDefenseAttr b)
        {
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitDefenseAttrData.get((defenseList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitDefenseData.size())).normal = b.normal;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitDefenseAttrData.get((defenseList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitDefenseData.size())).fire = b.fire;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitDefenseAttrData.get((defenseList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitDefenseData.size())).ice = b.ice;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitDefenseAttrData.get((defenseList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitDefenseData.size())).explosion = b.explosion;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitDefenseAttrData.get((defenseList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitDefenseData.size())).electric = b.electric;
        }
        else if (preset instanceof StatusVulnerability b)
        {
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(defenseList.getSelectionModel().getSelectedIndex()).sleep = b.sleep;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(defenseList.getSelectionModel().getSelectedIndex()).stop = b.stop;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(defenseList.getSelectionModel().getSelectedIndex()).dizzy = b.dizzy;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(defenseList.getSelectionModel().getSelectedIndex()).poison = b.poison;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(defenseList.getSelectionModel().getSelectedIndex()).confuse = b.confuse;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(defenseList.getSelectionModel().getSelectedIndex()).electric = b.electric;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(defenseList.getSelectionModel().getSelectedIndex()).burn = b.burn;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(defenseList.getSelectionModel().getSelectedIndex()).freeze = b.freeze;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(defenseList.getSelectionModel().getSelectedIndex()).huge = b.huge;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(defenseList.getSelectionModel().getSelectedIndex()).tiny = b.tiny;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(defenseList.getSelectionModel().getSelectedIndex()).attack_up = b.attack_up;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(defenseList.getSelectionModel().getSelectedIndex()).attack_down = b.attack_down;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(defenseList.getSelectionModel().getSelectedIndex()).defense_up = b.defense_up;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(defenseList.getSelectionModel().getSelectedIndex()).defense_down = b.defense_down;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(defenseList.getSelectionModel().getSelectedIndex()).allergic = b.allergic;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(defenseList.getSelectionModel().getSelectedIndex()).fright = b.fright;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(defenseList.getSelectionModel().getSelectedIndex()).gale_force = b.gale_force;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(defenseList.getSelectionModel().getSelectedIndex()).fast = b.fast;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(defenseList.getSelectionModel().getSelectedIndex()).slow = b.slow;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(defenseList.getSelectionModel().getSelectedIndex()).dodgy = b.dodgy;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(defenseList.getSelectionModel().getSelectedIndex()).invisible = b.invisible;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).StatusVulnerabilityData.get(defenseList.getSelectionModel().getSelectedIndex()).ohko = b.ohko;
        }
        else if (preset instanceof BattleWeapon b)
        {
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).accuracy = b.accuracy;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).fp_cost = b.fp_cost;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).sp_cost = b.sp_cost;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).superguard_state = b.superguard_state;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).sylish_multiplier = b.sylish_multiplier;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).bingo_slot_inc_chance = b.bingo_slot_inc_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_damage_fn = b.base_damage_fn;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_damage1 = b.base_damage1;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_damage2 = b.base_damage2;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_damage3 = b.base_damage3;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_damage4 = b.base_damage4;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_damage5 = b.base_damage5;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_damage6 = b.base_damage6;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_damage7 = b.base_damage7;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_damage8 = b.base_damage8;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_fpdamage1 = b.base_fpdamage1;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_fpdamage2 = b.base_fpdamage2;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_fpdamage3 = b.base_fpdamage3;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_fpdamage4 = b.base_fpdamage4;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_fpdamage5 = b.base_fpdamage5;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_fpdamage6 = b.base_fpdamage6;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_fpdamage7 = b.base_fpdamage7;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).base_fpdamage8 = b.base_fpdamage8;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).CannotTargetMarioOrShellShield = b.CannotTargetMarioOrShellShield;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).CannotTargetPartner = b.CannotTargetPartner;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).CannotTargetEnemy = b.CannotTargetEnemy;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).CannotTargetOppositeAlliance = b.CannotTargetOppositeAlliance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).CannotTargetOwnAlliance = b.CannotTargetOwnAlliance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).CannotTargetSelf = b.CannotTargetSelf;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).CannotTargetSameSpecies = b.CannotTargetSameSpecies;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).OnlyTargetSelf = b.OnlyTargetSelf;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).OnlyTargetMario = b.OnlyTargetMario;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).SingleTarget = b.SingleTarget;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).MultipleTarget = b.MultipleTarget;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).Tattlelike = b.Tattlelike;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).CannotTargetCeiling = b.CannotTargetCeiling;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).CannotTargetFloating = b.CannotTargetFloating;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).CannotTargetGrounded = b.CannotTargetGrounded;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).Jumplike = b.Jumplike;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).Hammerlike = b.Hammerlike;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).ShellTosslike = b.ShellTosslike;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).RecoilDamage = b.RecoilDamage;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).CanOnlyTargetFrontmost = b.CanOnlyTargetFrontmost;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).TargetSameAllianceDirection = b.TargetSameAllianceDirection;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).TargetOppositeAllianceDirection = b.TargetOppositeAllianceDirection;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).element = b.element;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).damage_pattern = b.damage_pattern;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).ac_level = b.ac_level;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).BadgeCanAffectPower = b.BadgeCanAffectPower;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).StatusCanAffectPower = b.StatusCanAffectPower;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).IsChargeable = b.IsChargeable;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).CannotMiss = b.CannotMiss;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).DiminishingReturnsByHit = b.DiminishingReturnsByHit;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).DiminishingReturnsByTarget = b.DiminishingReturnsByTarget;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).PiercesDefense = b.PiercesDefense;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).IgnoreTargetStatusVulnerability = b.IgnoreTargetStatusVulnerability;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).IgnitesIfBurned = b.IgnitesIfBurned;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).FlipsShellEnemies = b.FlipsShellEnemies;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).FlipsBombFlippableEnemies = b.FlipsBombFlippableEnemies;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).GroundsWingedEnemies = b.GroundsWingedEnemies;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).CanBeUsedAsConfusedAction = b.CanBeUsedAsConfusedAction;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).Unguardable = b.Unguardable;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).Electric = b.Electric;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).TopSpiky = b.TopSpiky;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).PreemptiveFrontSpiky = b.PreemptiveFrontSpiky;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).FrontSpiky = b.FrontSpiky;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).Fiery = b.Fiery;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).Icy = b.Icy;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).Poison = b.Poison;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).Explosive = b.Explosive;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).VolatileExplosive = b.VolatileExplosive;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).Payback = b.Payback;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).HoldFast = b.HoldFast;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).PreferMario = b.PreferMario;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).PreferPartner = b.PreferPartner;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).PreferFront = b.PreferFront;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).PreferBack = b.PreferBack;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).PreferSameAlliance = b.PreferSameAlliance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).PreferOppositeAlliance = b.PreferOppositeAlliance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).PreferLessHealthy = b.PreferLessHealthy;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).GreatlyPreferLessHealthy = b.GreatlyPreferLessHealthy;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).PreferLowerHP = b.PreferLowerHP;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).PreferHigherHP = b.PreferHigherHP;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).PreferInPeril = b.PreferInPeril;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).ChooseWeightedRandomly = b.ChooseWeightedRandomly;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).sleep_chance = b.sleep_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).sleep_time = b.sleep_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).stop_chance = b.stop_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).stop_time = b.stop_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).dizzy_chance = b.dizzy_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).dizzy_time = b.dizzy_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).poison_chance = b.poison_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).poison_time = b.poison_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).poison_strength = b.poison_strength;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).confuse_chance = b.confuse_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).confuse_time = b.confuse_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).electric_chance = b.electric_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).electric_time = b.electric_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).dodgy_chance = b.dodgy_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).dodgy_time = b.dodgy_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).burn_chance = b.burn_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).burn_time = b.burn_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).freeze_chance = b.freeze_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).freeze_time = b.freeze_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).size_change_chance = b.size_change_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).size_change_time = b.size_change_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).size_change_strength = b.size_change_strength;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).atk_change_chance = b.atk_change_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).atk_change_time = b.atk_change_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).atk_change_strength = b.atk_change_strength;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).def_change_chance = b.def_change_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).def_change_time = b.def_change_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).def_change_strength = b.def_change_strength;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).allergic_chance = b.allergic_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).allergic_time = b.allergic_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).ohko_chance = b.ohko_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).charge_strength = b.charge_strength;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).fast_chance = b.fast_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).fast_time = b.fast_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).slow_chance = b.slow_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).slow_time = b.slow_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).fright_chance = b.fright_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).gale_force_chance = b.gale_force_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).payback_time = b.payback_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).hold_fast_time = b.hold_fast_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).invisible_chance = b.invisible_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).invisible_time = b.invisible_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).hp_regen_time = b.hp_regen_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).hp_regen_strength = b.hp_regen_strength;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).fp_regen_time = b.fp_regen_time;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).fp_regen_strength = b.fp_regen_strength;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).attack_evt = b.attack_evt;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).stage_background_fallweight1 = b.stage_background_fallweight1;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).stage_background_fallweight2 = b.stage_background_fallweight2;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).stage_background_fallweight3 = b.stage_background_fallweight3;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).stage_background_fallweight4 = b.stage_background_fallweight4;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).stage_nozzle_turn_chance = b.stage_nozzle_turn_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).stage_nozzle_fire_chance = b.stage_nozzle_fire_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).stage_ceiling_fall_chance = b.stage_ceiling_fall_chance;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleWeaponData.get(weaponList.getSelectionModel().getSelectedIndex()).stage_object_fall_chance = b.stage_object_fall_chance;
        }
        else if (preset instanceof BattleUnitKindPart b)
        {
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).MainBodyPart = b.MainBodyPart;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).SecondaryBodyPart = b.SecondaryBodyPart;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).BombableBodyPart = b.BombableBodyPart;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).WeakToAttackFxR = b.WeakToAttackFxR;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).WeakToIcePower = b.WeakToIcePower;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).IsWinged = b.IsWinged;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).IsShelled = b.IsShelled;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).IsBombFlippable = b.IsBombFlippable;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).NeverTargetable = b.NeverTargetable;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).Untattleable = b.Untattleable;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).JumplikeCannotTarget = b.JumplikeCannotTarget;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).HammerlikeCannotTarget = b.HammerlikeCannotTarget;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).ShellTosslikeCannotTarget = b.ShellTosslikeCannotTarget;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).IsImmuneToDamageOrStatus = b.IsImmuneToDamageOrStatus;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).IsImmuneToOHKO = b.IsImmuneToOHKO;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).IsImmuneToStatus = b.IsImmuneToStatus;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).TopSpiky = b.TopSpiky;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).PreemptiveFrontSpiky = b.PreemptiveFrontSpiky;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).FrontSpiky = b.FrontSpiky;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).Fiery = b.Fiery;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).FieryStatus = b.FieryStatus;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).Icy = b.Icy;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).IcyStatus = b.IcyStatus;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).Poison = b.Poison;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).PoisonStatus = b.PoisonStatus;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).Electric = b.Electric;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).ElectricStatus = b.ElectricStatus;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).Explosive = b.Explosive;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindPartData.get((kindList.getSelectionModel().getSelectedIndex() - units.get(unitSelector.getSelectionModel().getSelectedIndex()).BattleUnitKindData.size())).VolatileExplosive = b.VolatileExplosive;
        }
        else if (preset instanceof HealthUpgrades b)
        {
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).HealthUpgradesData.get(kindList.getSelectionModel().getSelectedIndex()).startHP = b.startHP;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).HealthUpgradesData.get(kindList.getSelectionModel().getSelectedIndex()).startFP = b.startFP;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).HealthUpgradesData.get(kindList.getSelectionModel().getSelectedIndex()).startBP = b.startBP;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).HealthUpgradesData.get(kindList.getSelectionModel().getSelectedIndex()).upgradeHP = b.upgradeHP;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).HealthUpgradesData.get(kindList.getSelectionModel().getSelectedIndex()).upgradeFP = b.upgradeFP;
            units.get(unitSelector.getSelectionModel().getSelectedIndex()).HealthUpgradesData.get(kindList.getSelectionModel().getSelectedIndex()).upgradeBP = b.upgradeBP;
        }
    }

    /**
     * @Author Jemaroo
     * @Function Creates a popup window to ask for a valid .bup file
     */
    public void showGiveValidFileWindow()
    {
        Stage errorBox = new Stage();
        errorBox.setTitle("Error");
        errorBox.getIcons().add(images.get("unit"));

        VBox errorMenu = new VBox();
        Text message = new Text("Please Give a Valid .bup File for the Given Type You're Trying to Replace.");
        message.setWrappingWidth(210);
        message.setTextAlignment(TextAlignment.CENTER);
        errorMenu.setAlignment(Pos.CENTER);
        errorMenu.getChildren().add(message);

        StackPane successPane = new StackPane();
        successPane.getChildren().add(errorMenu);
        successPane.setAlignment(Pos.CENTER);

        Scene successScene = new Scene(successPane, 220, 50);

        errorBox.setScene(successScene);
        errorBox.initModality(Modality.APPLICATION_MODAL);
        errorBox.show();
    }

    /**
     * @Author Jemaroo
     * @Function Creates a popup window to share that preset was successfully imported
     */
    public void showPresetSuccessWindow()
    {
        Stage successBox = new Stage();
        successBox.setTitle("Success");
        successBox.getIcons().add(images.get("unit"));

        VBox successMenu = new VBox();
        Text message = new Text("Successfully Imported!");
        message.setWrappingWidth(290);
        message.setTextAlignment(TextAlignment.CENTER);
        successMenu.setAlignment(Pos.CENTER);
        successMenu.getChildren().add(message);

        StackPane successPane = new StackPane();
        successPane.getChildren().add(successMenu);
        successPane.setAlignment(Pos.CENTER);

        Scene successScene = new Scene(successPane, 150, 50);

        successBox.setScene(successScene);
        successBox.initModality(Modality.APPLICATION_MODAL);
        successBox.show();
    }

    /**
     * @Author Jemaroo
     * @Function Creates a popup window to ask to select a file before hitting this option
     */
    public void showOpenFileWindow()
    {
        Stage errorBox = new Stage();
        errorBox.setTitle("Error");
        errorBox.getIcons().add(images.get("unit"));

        VBox errorMenu = new VBox();
        Text message = new Text("Unable to Proceed, Please Select a File Before Selecting this Option");
        message.setWrappingWidth(210);
        message.setTextAlignment(TextAlignment.CENTER);
        errorMenu.setAlignment(Pos.CENTER);
        errorMenu.getChildren().add(message);

        StackPane successPane = new StackPane();
        successPane.getChildren().add(errorMenu);
        successPane.setAlignment(Pos.CENTER);

        Scene successScene = new Scene(successPane, 220, 50);

        errorBox.setScene(successScene);
        errorBox.initModality(Modality.APPLICATION_MODAL);
        errorBox.show();
    }

    /**
     * @Author Jemaroo
     * @Function Turns the value of a Swallow Attribute into it's index
     */
    public static int SAtoIndex(int SA)
    {
        switch(SA)
        {
            case 0: return 0;
            case 1: return 1;
            case 2: return 2;
            case 4: return 3;
            case 8: return 4;
            default: return 0;
        }
    }

    /**
     * @Author Jemaroo
     * @Function Turns the value of a index into it's Swallow Attribute
     */
    public static int IndexToSA(int index)
    {
        switch(index)
        {
            case 0: return 0;
            case 1: return 1;
            case 2: return 2;
            case 3: return 4;
            case 4: return 8;
            default: return 0;
        }
    }

    /**
     * @Author Jemaroo
     * @Function Turns the value of a Damage Pattern into it's index
     */
    public static int DPtoIndex(int DP)
    {
        switch(DP)
        {
            case 0: return 0;
            case 4: return 1;
            case 5: return 2;
            case 6: return 3;
            case 8: return 4;
            case 10: return 5;
            case 11: return 6;
            case 12: return 7;
            case 13: return 8;
            case 14: return 9;
            case 16: return 10;
            case 19: return 11;
            case 20: return 12;
            case 21: return 13;
            case 22: return 14;
            case 23: return 15;
            case 24: return 16;
            case 25: return 17;
            case 27: return 18;
            default: return 0;
        }
    }

    /**
     * @Author Jemaroo
     * @Function Turns the value of a index into it's Damage Pattern
     */
    public static int IndexToDP(int index)
    {
        switch(index)
        {
            case 0: return 0;
            case 1: return 4;
            case 2: return 5;
            case 3: return 6;
            case 4: return 8;
            case 5: return 10;
            case 6: return 11;
            case 7: return 12;
            case 8: return 13;
            case 9: return 14;
            case 10: return 16;
            case 11: return 19;
            case 12: return 20;
            case 13: return 21;
            case 14: return 22;
            case 15: return 23;
            case 16: return 24;
            case 17: return 25;
            case 18: return 27;
            default: return 0;
        }
    }

    /**
     * @Author Jemaroo
     * @Function Turns the value of a Base Damage fn into it's index
     */
    public static int BDFNtoIndex(long BDFN)
    {
        if(BDFN == 2149096624L) return 1;
        if(BDFN == 2149096504L) return 2;
        if(BDFN == 2149096752L) return 3;
        if(BDFN == 2149096436L) return 4;
        if(BDFN == 2149095976L) return 5;
        if(BDFN == 2149096368L) return 6;
        if(BDFN == 2149788844L) return 7;
        if(BDFN == 2149801084L) return 8;
        if(BDFN == 2149879880L) return 9;
        if(BDFN == 2149096096L) return 10;
        if(BDFN == 2149096272L) return 11;
        if(BDFN == 2149096156L) return 12;
        return 0;
    }

    /**
     * @Author Jemaroo
     * @Function Turns the value of a index into it's Base Damage fn
     */
    public static long IndexToBDFN(int index)
    {
        if(index == 1) return 2149096624L;
        if(index == 2) return 2149096504L;
        if(index == 3) return 2149096752L;
        if(index == 4) return 2149096436L;
        if(index == 5) return 2149095976L;
        if(index == 6) return 2149096368L;
        if(index == 7) return 2149788844L;
        if(index == 8) return 2149801084L;
        if(index == 9) return 2149879880L;
        if(index == 10) return 2149096096L;
        if(index == 11) return 2149096272L;
        if(index == 12) return 2149096156L;
        return 0L;
    }

    /**
     * @Author Jemaroo
     * @Function Turns the value of a Attack Script into it's index
     */
    public static int AStoIndex(long AS)
    {
        if(AS == 2150980848L) return 1;
        if(AS == 2150984700L) return 2;
        if(AS == 2150989540L) return 3;
        if(AS == 2151021624L) return 4;
        if(AS == 2151771200L) return 5;
        if(AS == 2151017384L) return 6;
        if(AS == 2151008952L) return 7;
        if(AS == 2151771096L) return 8;
        if(AS == 2151771124L) return 9;
        if(AS == 2151005592L) return 10;
        if(AS == 2150998876L) return 11;
        if(AS == 2151771152L) return 12;
        if(AS == 2151771168L) return 13;
        if(AS == 2151771184L) return 14;
        if(AS == 2151771228L) return 15;
        if(AS == 2151021700L) return 16;
        if(AS == 2151025080L) return 17;
        if(AS == 2151027840L) return 18;
        if(AS == 2151771284L) return 19;
        if(AS == 2151030656L) return 20;
        if(AS == 2151771256L) return 21;
        if(AS == 2151771312L) return 22;
        if(AS == 2151771340L) return 23;
        if(AS == 2151771368L) return 24;
        if(AS == 2151771396L) return 25;
        if(AS == 2151366176L) return 26;
        if(AS == 2151371896L) return 27;
        if(AS == 2151373376L) return 28;
        if(AS == 2151800328L) return 29;
        if(AS == 2151800356L) return 30;
        if(AS == 2151380272L) return 31;
        if(AS == 2151383104L) return 32;
        if(AS == 2151384456L) return 33;
        if(AS == 2151042348L) return 34;
        if(AS == 2151046152L) return 35;
        if(AS == 2151048784L) return 36;
        if(AS == 2151771704L) return 37;
        if(AS == 2151771732L) return 38;
        if(AS == 2151052500L) return 39;
        if(AS == 2151052808L) return 40;
        if(AS == 2151054240L) return 41;
        if(AS == 2151055800L) return 42;
        if(AS == 2151056988L) return 43;
        if(AS == 2151057288L) return 44;
        if(AS == 2151771760L) return 45;
        if(AS == 2151057728L) return 46;
        if(AS == 2151058856L) return 47;
        if(AS == 2151059392L) return 48;
        if(AS == 2151771788L) return 49;
        if(AS == 2151771816L) return 50;
        if(AS == 2151060108L) return 51;
        if(AS == 2151061424L) return 52;
        if(AS == 2151771844L) return 53;
        if(AS == 2151771872L) return 54;
        if(AS == 2151771900L) return 55;
        if(AS == 2151063188L) return 56;
        if(AS == 2151063488L) return 57;
        if(AS == 2151063736L) return 58;
        if(AS == 2151064556L) return 59;
        if(AS == 2151065416L) return 60;
        if(AS == 2151771928L) return 61;
        if(AS == 2151771956L) return 62;
        if(AS == 2151066876L) return 63;
        if(AS == 2151071508L) return 64;
        if(AS == 2151074004L) return 65;
        if(AS == 2151074540L) return 66;
        if(AS == 2151126228L) return 67;
        if(AS == 2151129028L) return 68;
        if(AS == 2151131108L) return 69;
        if(AS == 2151134608L) return 70;
        if(AS == 2151141776L) return 71;
        if(AS == 2151145876L) return 72;
        if(AS == 2151149876L) return 73;
        if(AS == 2151155720L) return 74;
        if(AS == 2151162032L) return 75;
        if(AS == 2151168392L) return 76;
        if(AS == 2151176472L) return 77;
        if(AS == 2151180316L) return 78;
        if(AS == 2151186208L) return 79;
        if(AS == 2151187492L) return 80;
        if(AS == 2151196812L) return 81;
        if(AS == 2151195844L) return 82;
        if(AS == 2151191372L) return 83;
        if(AS == 2151203296L) return 84;
        if(AS == 2151205620L) return 85;
        if(AS == 2151207340L) return 86;
        if(AS == 2151209132L) return 87;
        if(AS == 2151218728L) return 88;
        if(AS == 2151219672L) return 89;
        if(AS == 2151222888L) return 90;
        if(AS == 2151225696L) return 91;
        if(AS == 2151229048L) return 92;
        if(AS == 2151236008L) return 93;
        if(AS == 2151239408L) return 94;
        if(AS == 2151240996L) return 95;
        if(AS == 2151245308L) return 96;
        return 0;
    }

    /**
     * @Author Jemaroo
     * @Function Turns the value of a index into it's Attack Script
     */
    public static long IndexToAS(int index)
    {
        if(index == 1) return 2150980848L;
        if(index == 2) return 2150984700L;
        if(index == 3) return 2150989540L;
        if(index == 4) return 2151021624L;
        if(index == 5) return 2151771200L;
        if(index == 6) return 2151017384L;
        if(index == 7) return 2151008952L;
        if(index == 8) return 2151771096L;
        if(index == 9) return 2151771124L;
        if(index == 10) return 2151005592L;
        if(index == 11) return 2150998876L;
        if(index == 12) return 2151771152L;
        if(index == 13) return 2151771168L;
        if(index == 14) return 2151771184L;
        if(index == 15) return 2151771228L;
        if(index == 16) return 2151021700L;
        if(index == 17) return 2151025080L;
        if(index == 18) return 2151027840L;
        if(index == 19) return 2151771284L;
        if(index == 20) return 2151030656L;
        if(index == 21) return 2151771256L;
        if(index == 22) return 2151771312L;
        if(index == 23) return 2151771340L;
        if(index == 24) return 2151771368L;
        if(index == 25) return 2151771396L;
        if(index == 26) return 2151366176L;
        if(index == 27) return 2151371896L;
        if(index == 28) return 2151373376L;
        if(index == 29) return 2151800328L;
        if(index == 30) return 2151800356L;
        if(index == 31) return 2151380272L;
        if(index == 32) return 2151383104L;
        if(index == 33) return 2151384456L;
        if(index == 34) return 2151042348L;
        if(index == 35) return 2151046152L;
        if(index == 36) return 2151048784L;
        if(index == 37) return 2151771704L;
        if(index == 38) return 2151771732L;
        if(index == 39) return 2151052500L;
        if(index == 40) return 2151052808L;
        if(index == 41) return 2151054240L;
        if(index == 42) return 2151055800L;
        if(index == 43) return 2151056988L;
        if(index == 44) return 2151057288L;
        if(index == 45) return 2151771760L;
        if(index == 46) return 2151057728L;
        if(index == 47) return 2151058856L;
        if(index == 48) return 2151059392L;
        if(index == 49) return 2151771788L;
        if(index == 50) return 2151771816L;
        if(index == 51) return 2151060108L;
        if(index == 52) return 2151061424L;
        if(index == 53) return 2151771844L;
        if(index == 54) return 2151771872L;
        if(index == 55) return 2151771900L;
        if(index == 56) return 2151063188L;
        if(index == 57) return 2151063488L;
        if(index == 58) return 2151063736L;
        if(index == 59) return 2151064556L;
        if(index == 60) return 2151065416L;
        if(index == 61) return 2151771928L;
        if(index == 62) return 2151771956L;
        if(index == 63) return 2151066876L;
        if(index == 64) return 2151071508L;
        if(index == 65) return 2151074004L;
        if(index == 66) return 2151074540L;
        if(index == 67) return 2151126228L;
        if(index == 68) return 2151129028L;
        if(index == 69) return 2151131108L;
        if(index == 70) return 2151134608L;
        if(index == 71) return 2151141776L;
        if(index == 72) return 2151145876L;
        if(index == 73) return 2151149876L;
        if(index == 74) return 2151155720L;
        if(index == 75) return 2151162032L;
        if(index == 76) return 2151168392L;
        if(index == 77) return 2151176472L;
        if(index == 78) return 2151180316L;
        if(index == 79) return 2151186208L;
        if(index == 80) return 2151187492L;
        if(index == 81) return 2151196812L;
        if(index == 82) return 2151195844L;
        if(index == 83) return 2151191372L;
        if(index == 84) return 2151203296L;
        if(index == 85) return 2151205620L;
        if(index == 86) return 2151207340L;
        if(index == 87) return 2151209132L;
        if(index == 88) return 2151218728L;
        if(index == 89) return 2151219672L;
        if(index == 90) return 2151222888L;
        if(index == 91) return 2151225696L;
        if(index == 92) return 2151229048L;
        if(index == 93) return 2151236008L;
        if(index == 94) return 2151239408L;
        if(index == 95) return 2151240996L;
        if(index == 96) return 2151245308L;
        return 0L;
    }

    /**
     * @Author Jemaroo
     * @Function Adds listeners to all text fields to set the text color to red if 0
     */
    public void setRed0TextFieldFormats()
    {
        for(TextField field : textFields)
        {
            field.textProperty().addListener((obs, oldText, newText) -> 
            { 
                if ("0".equals(newText)) 
                {
                    field.setStyle("-fx-text-fill: red; -fx-font-weight: bold;");
                } 
                else 
                {
                    field.setStyle("-fx-text-fill: black;");
                }
            });
        }
    }

    /**
     * @Author Jemaroo
     * @Function Creates an imageview of an unit from the name of an image from the hashmap
     */
    public ImageView unitImageSelector(String name)
    {
        ImageView retIV = new ImageView(images.get("unknown"));
        retIV.setFitHeight(20); retIV.setFitWidth(20);

        switch (name) 
        {
            case "Goomba": retIV.setImage(images.get("unitGoomba")); return retIV;
            case "Paragoomba": retIV.setImage(images.get("unitParagoomba")); return retIV;
            case "Spiky Goomba": retIV.setImage(images.get("unitSpikyGoomba")); return retIV;
            case "Hyper Goomba": retIV.setImage(images.get("unitHyperGoomba")); return retIV;
            case "Hyper Paragoomba": retIV.setImage(images.get("unitHyperParagoomba")); return retIV;
            case "Hyper Spiky Goomba": retIV.setImage(images.get("unitHyperSpikyGoomba")); return retIV;
            case "Gloomba": retIV.setImage(images.get("unitGloomba")); return retIV;
            case "Paragloomba": retIV.setImage(images.get("unitParagloomba")); return retIV;
            case "Spiky Gloomba": retIV.setImage(images.get("unitSpikyGloomba")); return retIV;
            case "Koopa Troopa": retIV.setImage(images.get("unitKoopaTroopa")); return retIV;
            case "Paratroopa": retIV.setImage(images.get("unitParatroopa")); return retIV;
            case "KP Koopa": retIV.setImage(images.get("unitKPKoopa")); return retIV;
            case "KP Paratroopa": retIV.setImage(images.get("unitKPParatroopa")); return retIV;
            case "Shady Koopa": retIV.setImage(images.get("unitShadyKoopa")); return retIV;
            case "Shady Paratroopa": retIV.setImage(images.get("unitShadyParatroopa")); return retIV;
            case "Dark Koopa": retIV.setImage(images.get("unitDarkKoopa")); return retIV;
            case "Dark Paratroopa": retIV.setImage(images.get("unitDarkParatroopa")); return retIV;
            case "Koopatrol": retIV.setImage(images.get("unitKoopatrol")); return retIV;
            case "Dark Koopatrol": retIV.setImage(images.get("unitDarkKoopatrol")); return retIV;
            case "Dull Bones": retIV.setImage(images.get("unitDullBones")); return retIV;
            case "Red Bones": retIV.setImage(images.get("unitRedBones")); return retIV;
            case "Dry Bones": retIV.setImage(images.get("unitDryBones")); return retIV;
            case "Dark Bones": retIV.setImage(images.get("unitDarkBones")); return retIV;
            case "Hammer Bro": retIV.setImage(images.get("unitHammerBro")); return retIV;
            case "Boomerang Bro": retIV.setImage(images.get("unitBoomerangBro")); return retIV;
            case "Fire Bro": retIV.setImage(images.get("unitFireBro")); return retIV;
            case "Lakitu": retIV.setImage(images.get("unitLakitu")); return retIV;
            case "Dark Lakitu": retIV.setImage(images.get("unitDarkLakitu")); return retIV;
            case "Spiny": retIV.setImage(images.get("unitSpiny")); return retIV;
            case "Sky Blue Spiny": retIV.setImage(images.get("unitSkyBlueSpiny")); return retIV;
            case "Buzzy Beetle": retIV.setImage(images.get("unitBuzzyBeetle")); return retIV;
            case "Spiky Buzzy": retIV.setImage(images.get("unitSpikyBuzzy")); return retIV;
            case "Parabuzzy": retIV.setImage(images.get("unitParabuzzy")); return retIV;
            case "Spiky Parabuzzy": retIV.setImage(images.get("unitSpikyParabuzzy")); return retIV;
            case "Red Spiky Buzzy": retIV.setImage(images.get("unitRedSpikyBuzzy")); return retIV;
            case "Magikoopa": retIV.setImage(images.get("unitMagikoopa")); return retIV;
            case "Red Magikoopa": retIV.setImage(images.get("unitRedMagikoopa")); return retIV;
            case "White Magikoopa": retIV.setImage(images.get("unitWhiteMagikoopa")); return retIV;
            case "Green Magikoopa": retIV.setImage(images.get("unitGreenMagikoopa")); return retIV;
            case "Kammy Koopa": retIV.setImage(images.get("unitKammyKoopa")); return retIV;
            case "Bowser": retIV.setImage(images.get("unitBowser")); return retIV;
            case "Gus": retIV.setImage(images.get("unitGus")); return retIV;
            case "Dark Craw": retIV.setImage(images.get("unitDarkCraw")); return retIV;
            case "Bandit": retIV.setImage(images.get("unitBandit")); return retIV;
            case "Big Bandit": retIV.setImage(images.get("unitBigBandit")); return retIV;
            case "Badge Bandit": retIV.setImage(images.get("unitBadgeBandit")); return retIV;
            case "Spinia": retIV.setImage(images.get("unitSpinia")); return retIV;
            case "Spania": retIV.setImage(images.get("unitSpania")); return retIV;
            case "Spunia": retIV.setImage(images.get("unitSpunia")); return retIV;
            case "Fuzzy":
            case "Fuzzy Horde": retIV.setImage(images.get("unitFuzzy")); return retIV;
            case "Gold Fuzzy": retIV.setImage(images.get("unitGoldFuzzy")); return retIV;
            case "Green Fuzzy": retIV.setImage(images.get("unitGreenFuzzy")); return retIV;
            case "Flower Fuzzy": retIV.setImage(images.get("unitFlowerFuzzy")); return retIV;
            case "Pokey": retIV.setImage(images.get("unitPokey")); return retIV;
            case "Poison Pokey": retIV.setImage(images.get("unitPoisonPokey")); return retIV;
            case "Pale Piranha": retIV.setImage(images.get("unitPalePiranha")); return retIV;
            case "Putrid Piranha": retIV.setImage(images.get("unitPutridPiranha")); return retIV;
            case "Frost Piranha": retIV.setImage(images.get("unitFrostPiranha")); return retIV;
            case "Piranha Plant": retIV.setImage(images.get("unitPiranhaPlant")); return retIV;
            case "Crazee Dayzee": retIV.setImage(images.get("unitCrazeeDayzee")); return retIV;
            case "Amazy Dayzee": retIV.setImage(images.get("unitAmazyDayzee")); return retIV;
            case "Pider": retIV.setImage(images.get("unitPider")); return retIV;
            case "Arantula": retIV.setImage(images.get("unitArantula")); return retIV;
            case "Swooper": retIV.setImage(images.get("unitSwooper")); return retIV;
            case "Swoopula": retIV.setImage(images.get("unitSwoopula")); return retIV;
            case "Swampire": retIV.setImage(images.get("unitSwampire")); return retIV;
            case "Dark Puff": retIV.setImage(images.get("unitDarkPuff")); return retIV;
            case "Ruff Puff": retIV.setImage(images.get("unitRuffPuff")); return retIV;
            case "Ice Puff": retIV.setImage(images.get("unitIcePuff")); return retIV;
            case "Poison Puff": retIV.setImage(images.get("unitPoisonPuff")); return retIV;
            case "Boo": retIV.setImage(images.get("unitBoo")); return retIV;
            case "Atomic Boo": retIV.setImage(images.get("unitAtomicBoo")); return retIV;
            case "Dark Boo": retIV.setImage(images.get("unitDarkBoo")); return retIV;
            case "Ember": retIV.setImage(images.get("unitEmber")); return retIV;
            case "Lava Bubble": retIV.setImage(images.get("unitLavaBubble")); return retIV;
            case "Phantom Ember": retIV.setImage(images.get("unitPhantomEmber")); return retIV;
            case "Bald Cleft": retIV.setImage(images.get("unitBaldCleft")); return retIV;
            case "Hyper Bald Cleft": retIV.setImage(images.get("unitHyperBaldCleft")); return retIV;
            case "Cleft": retIV.setImage(images.get("unitCleft")); return retIV;
            case "Iron Cleft 1": retIV.setImage(images.get("unitIronCleft1")); return retIV;
            case "Iron Cleft 2": retIV.setImage(images.get("unitIronCleft2")); return retIV;
            case "Hyper Cleft": retIV.setImage(images.get("unitHyperCleft")); return retIV;
            case "Moon Cleft": retIV.setImage(images.get("unitMoonCleft")); return retIV;
            case "Bristle": retIV.setImage(images.get("unitBristle")); return retIV;
            case "Dark Bristle": retIV.setImage(images.get("unitDarkBristle")); return retIV;
            case "Bob-omb": retIV.setImage(images.get("unitBobomb")); return retIV;
            case "Bulky Bob-omb": retIV.setImage(images.get("unitBulkyBobomb")); return retIV;
            case "Bob-ulk": retIV.setImage(images.get("unitBobulk")); return retIV;
            case "Chain-Chomp": retIV.setImage(images.get("unitChainChomp")); return retIV;
            case "Red Chomp": retIV.setImage(images.get("unitRedChomp")); return retIV;
            case "Bill Blaster": retIV.setImage(images.get("unitBillBlaster")); return retIV;
            case "Bullet Bill": retIV.setImage(images.get("unitBulletBill")); return retIV;
            case "Bombshell Bill Blaster": retIV.setImage(images.get("unitBombshellBillBlaster")); return retIV;
            case "Bombshell Bill": retIV.setImage(images.get("unitBombshellBill")); return retIV;
            case "Dark Wizzerd": retIV.setImage(images.get("unitDarkWizzerd")); return retIV;
            case "Wizzerd": retIV.setImage(images.get("unitWizzerd")); return retIV;
            case "Elite Wizzerd": retIV.setImage(images.get("unitEliteWizzerd")); return retIV;
            case "Blooper":
            case "Blooper Right Tentacle":
            case "Blooper Left Tentacle": retIV.setImage(images.get("unitBlooper")); return retIV;
            case "Hooktail": retIV.setImage(images.get("unitHooktail")); return retIV;
            case "Gloomtail": retIV.setImage(images.get("unitGloomtail")); return retIV;
            case "Bonetail": retIV.setImage(images.get("unitBonetail")); return retIV;
            case "Rawk Hawk": retIV.setImage(images.get("unitRawkHawk")); return retIV;
            case "Macho Grubba": retIV.setImage(images.get("unitMachoGrubba")); return retIV;
            case "Doopliss": retIV.setImage(images.get("unitDoopliss")); return retIV;
            case "Doopliss Mario": retIV.setImage(images.get("unitDooplissMario")); return retIV;
            case "Cortez": retIV.setImage(images.get("unitCortez")); return retIV;
            case "Bone Pile": retIV.setImage(images.get("unitCortezBonePile")); return retIV;
            case "Cortez Hook": retIV.setImage(images.get("unitCortezHook")); return retIV;
            case "Cortez Rapier": retIV.setImage(images.get("unitCortezRapier")); return retIV;
            case "Cortez Saber": retIV.setImage(images.get("unitCortezSaber")); return retIV;
            case "Cortez Sword": retIV.setImage(images.get("unitCortezSword")); return retIV;
            case "Smorg":
            case "Smorg Tentacle 1":
            case "Smorg Tentacle 2":
            case "Smorg Tentacle 3":
            case "Smorg Claw": retIV.setImage(images.get("unitSmorg")); return retIV;
            case "X-Naut":
            case "X-Naut Platoon Formation 1":
            case "X-Naut Platoon Formation 2":
            case "X-Naut Platoon Formation 3": retIV.setImage(images.get("unitXNaut")); return retIV;
            case "X-Naut PHD": retIV.setImage(images.get("unitXNautPHD")); return retIV;
            case "Elite X-Naut": retIV.setImage(images.get("unitEliteXNaut")); return retIV;
            case "Yux": retIV.setImage(images.get("unitYux")); return retIV;
            case "Mini-Yux": retIV.setImage(images.get("unitMiniYux")); return retIV;
            case "Z-Yux": retIV.setImage(images.get("unitZYux")); return retIV;
            case "Mini-Z-Yux": retIV.setImage(images.get("unitMiniZYux")); return retIV;
            case "X-Yux": retIV.setImage(images.get("unitXYux")); return retIV;
            case "Mini-X-Yux": retIV.setImage(images.get("unitMiniXYux")); return retIV;
            case "Grodus X": retIV.setImage(images.get("unitGrodusX")); return retIV;
            case "Magnus Von Grapple":
            case "X-Fist": retIV.setImage(images.get("unitMagnusVonGrapple")); return retIV;
            case "Magnus Von Grapple 2.0":
            case "X-Punch": retIV.setImage(images.get("unitMagnusVonGrapple2")); return retIV;
            case "Lord Crump": retIV.setImage(images.get("unitLordCrump")); return retIV;
            case "Grodus": retIV.setImage(images.get("unitGrodus")); return retIV;
            case "Beldam": retIV.setImage(images.get("unitBeldam")); return retIV;
            case "Marilyn": retIV.setImage(images.get("unitMarilyn")); return retIV;
            case "Shadow Peach": retIV.setImage(images.get("unitShadowPeach")); return retIV;
            case "Shadow Queen": retIV.setImage(images.get("unitShadowQueen")); return retIV;
            case "Shadow Hand 1": retIV.setImage(images.get("unitShadowHand1")); return retIV;
            case "Shadow Hand 2": retIV.setImage(images.get("unitShadowHand2")); return retIV;
            case "Small Shadow Hands": retIV.setImage(images.get("unitSmallShadowHands")); return retIV;
            case "Mario": retIV.setImage(images.get("marioHeadCustom")); return retIV;
            case "Goombella": retIV.setImage(images.get("goombellaPartnerSwitch")); return retIV;
            case "Koops": retIV.setImage(images.get("koopsPartnerSwitch")); return retIV;
            case "Flurrie": retIV.setImage(images.get("flurriePartnerSwitch")); return retIV;
            case "Yoshi": retIV.setImage(images.get("yoshiPartnerSwitch")); return retIV;
            case "Vivian": retIV.setImage(images.get("vivianPartnerSwitch")); return retIV;
            case "Bobbery": retIV.setImage(images.get("bobberyPartnerSwitch")); return retIV;
            case "Ms. Mowz": retIV.setImage(images.get("mowzPartnerSwitch")); return retIV;
            case "Bomb Squad Bomb": retIV.setImage(images.get("unitBombSquadBomb")); return retIV;
            case "Shell Shield": retIV.setImage(images.get("unitShellShield")); return retIV;
            case "unit_system": retIV.setImage(images.get("itemsIcon")); return retIV;
            case "unit_npc_christine":
            case "unit_lecture_christine": retIV.setImage(images.get("goombellaPartnerSwitch")); return retIV;
            case "unit_lecture_frankli":
            case "unit_lecture_frankli_sac":
            case "unit_lecture_frankli_kuriboo": retIV.setImage(images.get("unitProfessorFrankly")); return retIV;
            case "unit_act_atmic_teresa": retIV.setImage(images.get("unitAtomicBoo")); return retIV;
            case "unit_act_clauda": retIV.setImage(images.get("flurriePartnerSwitch")); return retIV;
            case "unit_act_kinopiko": retIV.setImage(images.get("femaleToadCustom")); return retIV;
            case "unit_act_kinopio": retIV.setImage(images.get("toadCustom")); return retIV;
            case "unit_act_mario": retIV.setImage(images.get("unitDooplissMario")); return retIV;
            case "unit_act_teresa": retIV.setImage(images.get("unitBoo")); return retIV;

            default: return retIV;
        }
    }

    /**
     * @Author Jemaroo
     * @Function Returns the name of an area based on the file name
     */
    public String fileNameSelector(String name)
    {
        String retString = name;

        switch (name) 
        {
            case "main.dol":
            case "Start.dol": retString = "System"; return retString;
            case "aji.rel": retString = "X-Naut Fortress"; return retString;
            case "bom.rel": retString = "Fahr Outpost"; return retString;
            case "dou.rel": retString = "Pirate's Grotto"; return retString;
            case "eki.rel": retString = "Riverside Station"; return retString;
            case "gon.rel": retString = "Hooktail Castle"; return retString;
            case "gor.rel": retString = "Rogueport"; return retString;
            case "gra.rel": retString = "Twilight Trail"; return retString;
            case "hei.rel": retString = "Petal Meadows"; return retString;
            case "jin.rel": retString = "Creepy Steeple"; return retString;
            case "jon.rel": retString = "Pit of 100 Trials"; return retString;
            case "las.rel": retString = "Palace of Shadow"; return retString;
            case "moo.rel": retString = "The Moon"; return retString;
            case "mri.rel": retString = "Boggly Tree"; return retString;
            case "muj.rel": retString = "Keelhaul Key"; return retString;
            case "nok.rel": retString = "Petalburg"; return retString;
            case "pik.rel": retString = "Poshley Heights"; return retString;
            case "rsh.rel": retString = "Excess Express"; return retString;
            case "tik.rel": retString = "Rogueport Sewers"; return retString;
            case "tou2.rel": retString = "Glitz Pit"; return retString;
            case "win.rel": retString = "Boggly Woods"; return retString;

            default: return retString;
        }
    }

    /**
     * @Author Jemaroo
     * @Function Creates an imageview of a file Area from the name of an image from the hashmap
     */
    public ImageView fileImageSelector(String name)
    {
        ImageView retIV = new ImageView(images.get("unknown"));
        retIV.setFitHeight(15); retIV.setFitWidth(15);

        switch (name) 
        {
            case "main.dol":
            case "Start.dol": retIV.setImage(images.get("magicalMap2")); return retIV;
            case "aji.rel": retIV.setImage(images.get("unitXNaut")); return retIV;
            case "bom.rel": retIV.setImage(images.get("fahrOutpostBombCustom")); return retIV;
            case "dou.rel": retIV.setImage(images.get("unitBillBlaster")); return retIV;
            case "eki.rel": retIV.setImage(images.get("unitRuffPuff")); return retIV;
            case "gon.rel": retIV.setImage(images.get("unitHooktail")); return retIV;
            case "gor.rel": retIV.setImage(images.get("unitProfessorFrankly")); return retIV;
            case "gra.rel": retIV.setImage(images.get("unitHyperGoomba")); return retIV;
            case "hei.rel": retIV.setImage(images.get("unitKoopaTroopa")); return retIV;
            case "jin.rel": retIV.setImage(images.get("unitDoopliss")); return retIV;
            case "jon.rel": retIV.setImage(images.get("unitBonetail")); return retIV;
            case "las.rel": retIV.setImage(images.get("unitShadowPeach")); return retIV;
            case "moo.rel": retIV.setImage(images.get("unitMoonCleft")); return retIV;
            case "mri.rel": retIV.setImage(images.get("puniCustom")); return retIV;
            case "muj.rel": retIV.setImage(images.get("unitPutridPiranha")); return retIV;
            case "nok.rel": retIV.setImage(images.get("koopaCustom")); return retIV;
            case "pik.rel": retIV.setImage(images.get("unitDarkBoo")); return retIV;
            case "rsh.rel": retIV.setImage(images.get("unitSmorg")); return retIV;
            case "tik.rel": retIV.setImage(images.get("unitBlooper")); return retIV;
            case "tou2.rel": retIV.setImage(images.get("unitRawkHawk")); return retIV;
            case "win.rel": retIV.setImage(images.get("unitPalePiranha")); return retIV;

            default: return retIV;
        }
    }

    /**
     * @Author Jemaroo
     * @Function Creates a 20x20 icon imageView
     */
    public ImageView fieldImageViewCreator(Image image)
    {
        ImageView retIV = new ImageView(image);
        retIV.setFitHeight(20); retIV.setFitWidth(20);
        
        return retIV;
    }

    /**
     * @Author Jemaroo
     * @Function Creates a 15x15 icon imageView
     */
    public ImageView unitImageViewCreator(Image image)
    {
        ImageView retIV = new ImageView(image);
        retIV.setFitHeight(15); retIV.setFitWidth(15);
        
        return retIV;
    }

    /**
     * @Author Jemaroo
     * @Function Creates an imageview of an attack from the name of an image from the hashmap
     */
    public ImageView attackNameImageSelector(String name)
    {
        ImageView retIV = new ImageView(images.get("unknown"));
        retIV.setFitHeight(15); retIV.setFitWidth(15);

        switch (name) 
        {
            case "Jump": retIV.setImage(images.get("boots")); return retIV;
            case "Spin Jump 1":
            case "Spin Jump 2":
            case "Spin Jump": retIV.setImage(images.get("superBoots")); return retIV;
            case "Spring Jump 1":
            case "Spring Jump 2": retIV.setImage(images.get("ultraBoots")); return retIV;
            case "Hammer": retIV.setImage(images.get("hammer")); return retIV;
            case "First Strike Super Hammer":
            case "Super Hammer 1":
            case "Super Hammer 2":
            case "Super Hammer": retIV.setImage(images.get("superHammer")); return retIV;
            case "First Strike Ultra Hammer":
            case "Ultra Hammer 1":
            case "Ultra Hammer 2":
            case "Ultra Hammer 3": retIV.setImage(images.get("ultraHammer")); return retIV;
            case "Sweet Treat": retIV.setImage(images.get("magicalMap2")); return retIV;
            case "Earth Tremor": retIV.setImage(images.get("diamondStar")); return retIV;
            case "Clock Out": retIV.setImage(images.get("emeraldStar")); return retIV;
            case "Power Lift": retIV.setImage(images.get("goldStar")); return retIV;
            case "Art Attack": retIV.setImage(images.get("rubyStar")); return retIV;
            case "Sweet Feast": retIV.setImage(images.get("sapphireStar")); return retIV;
            case "Showstopper": retIV.setImage(images.get("garnetStar")); return retIV;
            case "Supernova": retIV.setImage(images.get("crystalStar")); return retIV;
            case "Body Slam":
            case "Ground Pound":
            case "Love Slap 1":
            case "Love Slap 2":
            case "Love Slap 3":
            case "Love Slap 4":
            case "Bomb First Strike":
            case "Bomb":
            case "Shade Fist":
            case "Shell Toss First Strike":
            case "Shell Toss":
            case "Headbonk":
            case "Shell Toss 1":
            case "Shell Toss 2": retIV.setImage(images.get("lvl1Move")); return retIV;
            case "Gale Force":
            case "Gulp 1":
            case "Gulp 2":
            case "Gulp 3":
            case "Gulp 4":
            case "Gulp 5":
            case "Kiss Thief":
            case "Bomb Squad":
            case "Veil":
            case "Power Shell":
            case "Tattle": retIV.setImage(images.get("lvl2Move")); return retIV;
            case "Lip Lock":
            case "Mini Egg":
            case "Tease":
            case "Hold Fast":
            case "Fiery Jinx":
            case "Shell Shield":
            case "Multibonk": retIV.setImage(images.get("lvl3Move")); return retIV;
            case "Dodgy Fog":
            case "Stampede":
            case "Smooch":
            case "Bob-ombast":
            case "Infatuate":
            case "Shell Slam":
            case "Rally Wink": retIV.setImage(images.get("lvl4Move")); return retIV;
            case "Mushroom": retIV.setImage(images.get("mushroom")); return retIV;
            case "Life Shroom": retIV.setImage(images.get("lifeShroom")); return retIV;
            case "Super Shroom": retIV.setImage(images.get("superShroom")); return retIV;
            case "Ultra Shroom": retIV.setImage(images.get("ultraShroom")); return retIV;
            case "Dried Shroom": retIV.setImage(images.get("driedShroom")); return retIV;
            case "Honey Syrup": retIV.setImage(images.get("honeySyrup")); return retIV;
            case "Maple Syrup": retIV.setImage(images.get("mapleSyrup")); return retIV;
            case "Jammin' Jelly": retIV.setImage(images.get("jamminJelly")); return retIV;
            case "Whacka Bump": retIV.setImage(images.get("whackaBump")); return retIV;
            case "Pow Block": retIV.setImage(images.get("powBlock")); return retIV;
            case "Fire Flower": retIV.setImage(images.get("fireFlower")); return retIV;
            case "Thunder Bolt": retIV.setImage(images.get("thunderBolt")); return retIV;
            case "Shooting Star": retIV.setImage(images.get("shootingStar")); return retIV;
            case "Ice Storm": retIV.setImage(images.get("iceStorm")); return retIV;
            case "Earth Quake": retIV.setImage(images.get("earthQuake")); return retIV;
            case "Boo's Sheet": retIV.setImage(images.get("boosSheet")); return retIV;
            case "Volt Shroom": retIV.setImage(images.get("voltShroom")); return retIV;
            case "Repel Cape": retIV.setImage(images.get("repelCape")); return retIV;
            case "Ruin Powder": retIV.setImage(images.get("ruinPowder")); return retIV;
            case "Sleepy Sheep": retIV.setImage(images.get("sleepySheep")); return retIV;
            case "Dizzy Dial": retIV.setImage(images.get("dizzyDial")); return retIV;
            case "Power Punch": retIV.setImage(images.get("powerPunch")); return retIV;
            case "Courage Shell": retIV.setImage(images.get("courageShell")); return retIV;
            case "HP Drain": retIV.setImage(images.get("HPDrain1")); return retIV;
            case "Trade Off": retIV.setImage(images.get("tradeOff")); return retIV;
            case "Mini Mr. Mini": retIV.setImage(images.get("miniMrMini")); return retIV;
            case "Mr. Softener": retIV.setImage(images.get("mrsoftener")); return retIV;
            case "Tasty Tonic": retIV.setImage(images.get("tastyTonic")); return retIV;
            case "Slow Shroom": retIV.setImage(images.get("slowShroom")); return retIV;
            case "Gradual Syrup": retIV.setImage(images.get("gradualSyrup")); return retIV;
            case "Point Swap": retIV.setImage(images.get("pointSwap")); return retIV;
            case "Fright Mask": retIV.setImage(images.get("frightMask")); return retIV;
            case "Mystery": retIV.setImage(images.get("mystery")); return retIV;
            case "Spite Pouch": retIV.setImage(images.get("spitePouch")); return retIV;
            case "Koopa Curse": retIV.setImage(images.get("koopaCurse")); return retIV;
            case "Space Food": retIV.setImage(images.get("spaceFood")); return retIV;
            case "Icicle Pop": retIV.setImage(images.get("iciclePop")); return retIV;
            case "Zess Frappe": retIV.setImage(images.get("zessFrappe")); return retIV;
            case "Snow Bunny": retIV.setImage(images.get("snowBunny")); return retIV;
            case "Shroom Broth": retIV.setImage(images.get("shroomBroth")); return retIV;
            case "Meteor Meal": retIV.setImage(images.get("meteorMeal")); return retIV;
            case "Spicy Pasta": retIV.setImage(images.get("spicyPasta")); return retIV;
            case "Heartful Cake": retIV.setImage(images.get("heartfulCake")); return retIV;
            case "Electro Pop": retIV.setImage(images.get("electroPop")); return retIV;
            case "Healthy Salad": retIV.setImage(images.get("healthySalad")); return retIV;
            case "Fresh Juice": retIV.setImage(images.get("freshJuice")); return retIV;
            case "Hot Sauce": retIV.setImage(images.get("hotSauce")); return retIV;
            case "Couple's Cake": retIV.setImage(images.get("couplesCake")); return retIV;
            case "Zess Dynamite": retIV.setImage(images.get("zessDynamite")); return retIV;
            case "Coconut Bomb": retIV.setImage(images.get("coconutBomb")); return retIV;
            case "Courage Meal": retIV.setImage(images.get("courageMeal")); return retIV;
            case "Egg Bomb": retIV.setImage(images.get("eggBomb")); return retIV;
            case "Poison Shroom": retIV.setImage(images.get("poisonShroom")); return retIV;
            case "Trial Stew": retIV.setImage(images.get("trialStew")); return retIV;
            case "Love Pudding 1":
            case "Love Pudding 2":
            case "Love Pudding 3": retIV.setImage(images.get("lovePudding")); return retIV;
            case "Peach Tart 1":
            case "Peach Tart 2":
            case "Peach Tart 3": retIV.setImage(images.get("peachTart")); return retIV;
            case "Potion Drink":
            case "Dodgy Potion": retIV.setImage(images.get("greenPotion")); return retIV;
            case "Burn Potion": retIV.setImage(images.get("orangePotion")); return retIV;
            case "Defense Down Potion":
            case "Tiny Potion": retIV.setImage(images.get("bluePotion")); return retIV;
            case "Single Heal Potion":
            case "Multi Heal Potion":
            case "HP Regen Potion": retIV.setImage(images.get("redPotion")); return retIV;
            case "Multibounce": retIV.setImage(images.get("multibounce")); return retIV;
            case "Power Jump": retIV.setImage(images.get("powerJump")); return retIV;
            case "Mega Jump": retIV.setImage(images.get("megaJump")); return retIV;
            case "Power Bounce": retIV.setImage(images.get("powerBounce")); return retIV;
            case "Shrink Stomp": retIV.setImage(images.get("shrinkStomp")); return retIV;
            case "Sleepy Stomp": retIV.setImage(images.get("sleepyStomp")); return retIV;
            case "Soft Stomp": retIV.setImage(images.get("softStomp")); return retIV;
            case "Power Smash": retIV.setImage(images.get("powerSmash")); return retIV;
            case "Mega Smash": retIV.setImage(images.get("megaSmash")); return retIV;
            case "Quake Hammer": retIV.setImage(images.get("quakeHammer")); return retIV;
            case "Mega Quake": retIV.setImage(images.get("megaQuake")); return retIV;
            case "Hammer Throw": retIV.setImage(images.get("hammerThrow")); return retIV;
            case "Piercing Blow": retIV.setImage(images.get("piercingBlow")); return retIV;
            case "Head Rattle": retIV.setImage(images.get("headRattle")); return retIV;
            case "Ice Smash": retIV.setImage(images.get("iceSmash")); return retIV;
            case "Lucky Start": retIV.setImage(images.get("luckyStart")); return retIV;
            case "Double Dip": retIV.setImage(images.get("doubleDip")); return retIV;
            case "Triple Dip": retIV.setImage(images.get("tripleDip")); return retIV;
            case "Tornado Jump 1":
            case "Tornado Jump 2": retIV.setImage(images.get("tornadoJump")); return retIV;
            case "Fire Drive 1":
            case "Fire Drive 2": retIV.setImage(images.get("fireDrive")); return retIV;
            case "Charge": retIV.setImage(images.get("chargeBadge")); return retIV;
            case "Charge P": retIV.setImage(images.get("chargeP")); return retIV;
            case "Super Charge": retIV.setImage(images.get("superChargeBadge")); return retIV;
            case "Super Charge P": retIV.setImage(images.get("superChargeP")); return retIV;
            case "Mario Defend":
            case "Partner Defend": retIV.setImage(images.get("defend")); return retIV;
            case "Drill Attack":
            case "Boomerang Attack":
            case "Audience Attack":
            case "Punch Attack":
            case "Chest Attack":
            case "Jump Attack":
            case "Beam Attack":
            case "Charged Beam Attack":
            case "Headbonk ":
            case "Bash Attack":
            case "Bite Attack":
            case "Shoot Attack":
            case "Explosion":
            case "Shell Toss ":
            case "Ceiling Drop Attack":
            case "Bonk Attack (4 Segments)":
            case "Bonk Attack (3 Segments)":
            case "Bonk Attack (2 Segments)":
            case "Bonk Attack (1 Segment)":
            case "Spike Launch Attack (4 Segments)":
            case "Spike Launch Attack (3 Segments)":
            case "Spike Launch Attack (2 Segments)":
            case "Stomp Attack":
            case "Bone Throw":
            case "Ram Attack":
            case "Spear Throw Attack":
            case "Butt Attack":
            case "Spin Attack":
            case "Slam Attack":
            case "Boo Spray Attack":
            case "Ceiling Attack":
            case "Ram Attack 1":
            case "Ram Attack 2":
            case "Scare Attack":
            case "Explosion Attack":
            case "Roll Attack":
            case "Flipped Shell Attack":
            case "Charged Attack":
            case "Shell Attack":
            case "Green Beam Attack":
            case "Hands Attack":
            case "Multiple Bone Throw":
            case "Multishot Attack":
            case "Critical Bite Attack":
            case "Magic Attack":
            case "Dark Wave Attack":
            case "Slap Attack":
            case "Drag Down Attack 1":
            case "Drag Down Attack 2":
            case "Run Attack":
            case "Earthquake Attack":
            case "Front Weapon Attack":
            case "Back Weapon Attack":
            case "Multiweapon Attack":
            case "Head Bash Attack":
            case "Bone Toss Attack":
            case "Swing":
            case "Fall Attack":
            case "Fork Attack":
            case "Ball Attack":
            case "Ink Attack":
            case "Front Slap Attack":
            case "Back Slap Attack":
            case "Hammer Throw Attack":
            case "Multi Hammer Throw Attack 1":
            case "Multi Hammer Throw Attack 2":
            case "Backflip Attack":
            case "Slide Attack":
            case "Flying Punch Attack":
            case "Ceiling Shake 1":
            case "Ceiling Shake 2":
            case "Multi Boomerang Attack": retIV.setImage(images.get("chargeAction")); return retIV;
            case "Frost Breath Attack":
            case "Ice Breath":
            case "Ice Attack":
            case "Blizzard Attack": retIV.setImage(images.get("iceCustom")); return retIV;
            case "Burn Attack":
            case "Flame Launch Attack":
            case "Flamethrower Attack":
            case "Fire Breath Attack":
            case "Fire Attack":
            case "Mega Breath":
            case "Fire Spit Attack":
            case "Multi Fire Spit Attack 1":
            case "Multi Fire Spit Attack 2": retIV.setImage(images.get("burnCustom")); return retIV;
            case "Thunder Rage":
            case "Thunder Attack":
            case "Lightning Attack": retIV.setImage(images.get("thunderRage")); return retIV;
            case "Charge 1":
            case "Charge 2":
            case "Charge 3":
            case "Charge ": retIV.setImage(images.get("superChargeAction")); return retIV;
            case "Leech Attack 1":
            case "Leech Attack 2":
            case "Heal":
            case "Heal Magic":
            case "Drain Attack":
            case "Heal All":
            case "Ceiling Heal": retIV.setImage(images.get("heart")); return retIV;
            case "Stop Scare Attack":
            case "Stop Attack":
            case "Stopwatch": retIV.setImage(images.get("stopStatus")); return retIV;
            case "Confuse Scare Attack":
            case "Confuse Breath":
            case "Confuse Magic": retIV.setImage(images.get("confuseStatus")); return retIV;
            case "Turn Invisible":
            case "Turn Other Invisible":
            case "Invisible Magic": retIV.setImage(images.get("invisibleStatus")); return retIV;
            case "Sleep Breath": retIV.setImage(images.get("sleepStatus")); return retIV;
            case "Tiny Breath":
            case "Tiny Magic": retIV.setImage(images.get("tinyStatus")); return retIV;
            case "Dodgy Magic":
            case "Dodgy Power": retIV.setImage(images.get("dodgyStatus")); return retIV;
            case "Huge Magic": retIV.setImage(images.get("hugeStatus")); return retIV;
            case "Defense Up Magic":
            case "Defense Up Power": retIV.setImage(images.get("defenseUpStatus")); return retIV;
            case "Attack and Defense Up Magic":
            case "Attack Up Power": retIV.setImage(images.get("attackUpStatus")); return retIV;
            case "Poison Gas Attack":
            case "Poison Breath":
            case "Poison Spray Attack": retIV.setImage(images.get("poisonCustom")); return retIV;
            case "Poison Magic": retIV.setImage(images.get("poisonStatus")); return retIV;
            case "Electric Magic": retIV.setImage(images.get("electricStatus")); return retIV;
            case "Allergic Breath": retIV.setImage(images.get("allergicStatus")); return retIV;
            case "Slow Magic": retIV.setImage(images.get("slowStatus")); return retIV;
            case "Fast Magic":
            case "Fast Power": retIV.setImage(images.get("fastStatus")); return retIV;
            case "Payback Magic": retIV.setImage(images.get("paybackStatus")); return retIV;
            case "Bomb Squad Explosion": retIV.setImage(images.get("unitBombSquadBomb")); return retIV;
            case "Change Height": retIV.setImage(images.get("unitBoo")); return retIV;
            case "Spiny Throw Attack": retIV.setImage(images.get("unitSpiny")); return retIV;
            case "Sing Attack": retIV.setImage(images.get("musicNoteCustom")); return retIV;
            case "FP Leech Attack": retIV.setImage(images.get("flower")); return retIV;
            case "Audience Can": retIV.setImage(images.get("audienceCan")); return retIV;
            case "Audience Rock": retIV.setImage(images.get("audienceRock")); return retIV;
            case "Audience Bone": retIV.setImage(images.get("audienceBone")); return retIV;
            case "Audience Hammer": retIV.setImage(images.get("audienceHammer")); return retIV;
            case "lec_weapon_zutsuki_1":
            case "lec_weapon_zutsuki_2":
            case "_lec_weapon_jump":
            case "_lec_weapon_hammer": retIV.setImage(images.get("unusedPaper")); return retIV;

            default: return retIV;
        }
    }

    /**
     * @Author Jemaroo
     * @Function Creates a label with it's corresponding tooltip
     */
    public Label labelANDTooltipMaker(String text, int structIndex, int fieldIndex) 
    {
        Label temp = new Label(text);
        File jsonFile = new File("src\\Tooltips.json");

        try 
        {
            JSONParser parser = new JSONParser();
            JSONObject root = (JSONObject) parser.parse(new FileReader(jsonFile));

            String tooltipText = null;

            switch (structIndex) 
            {
                case 1: 
                {
                    JSONArray structArray = (JSONArray) root.get("BattleUnitKind");
                    JSONObject obj = (JSONObject) structArray.get(0);

                    switch (fieldIndex) 
                    {
                        case 1:  tooltipText = (String) obj.get("HP"); break;
                        case 2:  tooltipText = (String) obj.get("dangerHP"); break;
                        case 3:  tooltipText = (String) obj.get("perilHP"); break;
                        case 4:  tooltipText = (String) obj.get("level"); break;
                        case 5:  tooltipText = (String) obj.get("bonusXP"); break;
                        case 6:  tooltipText = (String) obj.get("bonusCoin"); break;
                        case 7:  tooltipText = (String) obj.get("bonusCoinRate"); break;
                        case 8:  tooltipText = (String) obj.get("baseCoin"); break;
                        case 9:  tooltipText = (String) obj.get("runRate"); break;
                        case 10: tooltipText = (String) obj.get("pbCap"); break;
                        case 11: tooltipText = (String) obj.get("swallowChance"); break;
                        case 12: tooltipText = (String) obj.get("swallowAttribute"); break;
                        case 13: tooltipText = (String) obj.get("ultraHammerKnockChance"); break;
                        case 14: tooltipText = (String) obj.get("itemStealParameter"); break;
                        case 15: tooltipText = (String) obj.get("MapObj"); break;
                        case 16: tooltipText = (String) obj.get("OutOfReach"); break;
                        case 17: tooltipText = (String) obj.get("Unquakeable"); break;
                        case 18: tooltipText = (String) obj.get("IsInvisible"); break;
                        case 19: tooltipText = (String) obj.get("IsVeiled"); break;
                        case 20: tooltipText = (String) obj.get("ShellShielded"); break;
                        case 21: tooltipText = (String) obj.get("NeverTargetable"); break;
                        case 22: tooltipText = (String) obj.get("LimitSwitch"); break;
                        case 23: tooltipText = (String) obj.get("DisableZeroGravityFloat"); break;
                        case 24: tooltipText = (String) obj.get("DisableZeroGravityImmobility"); break;
                        case 25: tooltipText = (String) obj.get("immuneToUltraHammerKnock"); break;
                        case 26: tooltipText = (String) obj.get("IsUndead"); break;
                        case 27: tooltipText = (String) obj.get("IsCorpse"); break;
                        case 28: tooltipText = (String) obj.get("IsLeader"); break;
                        case 29: tooltipText = (String) obj.get("CannotTakeActions"); break;
                        case 30: tooltipText = (String) obj.get("NotSpunByLoveSlap"); break;
                        case 31: tooltipText = (String) obj.get("DisableDamageStars"); break;
                        case 32: tooltipText = (String) obj.get("DisableAllPartVisibility"); break;
                        case 33: tooltipText = (String) obj.get("DisableHPGauge"); break;
                        case 34: tooltipText = (String) obj.get("LookCamera"); break;
                        case 35: tooltipText = (String) obj.get("NonCombatant"); break;
                        case 36: tooltipText = (String) obj.get("NoShadow"); break;
                        case 37: tooltipText = (String) obj.get("DisableDamage"); break;
                    }
                    break;
                }

                case 2: 
                {
                    JSONArray structArray = (JSONArray) root.get("BattleUnitKindPart");
                    JSONObject obj = (JSONObject) structArray.get(0);

                    switch (fieldIndex) 
                    {
                        case 1:  tooltipText = (String) obj.get("MainBodyPart"); break;
                        case 2:  tooltipText = (String) obj.get("SecondaryBodyPart"); break;
                        case 3:  tooltipText = (String) obj.get("BombableBodyPart"); break;
                        case 4:  tooltipText = (String) obj.get("GuardBodyPart"); break;
                        case 5:  tooltipText = (String) obj.get("NotBombableBodyPart"); break;
                        case 6:  tooltipText = (String) obj.get("InHole"); break;
                        case 7:  tooltipText = (String) obj.get("WeakToAttackFxR"); break;
                        case 8:  tooltipText = (String) obj.get("WeakToIcePower"); break;
                        case 9:  tooltipText = (String) obj.get("IsWinged"); break;
                        case 10: tooltipText = (String) obj.get("IsShelled"); break;
                        case 11: tooltipText = (String) obj.get("IsBombFlippable"); break;
                        case 12: tooltipText = (String) obj.get("IsClonelike"); break;
                        case 13: tooltipText = (String) obj.get("DisableFlatPaperLayering"); break;
                        case 14: tooltipText = (String) obj.get("NeverTargetable"); break;
                        case 15:  tooltipText = (String) obj.get("IgnoreMapObjectOffset"); break;
                        case 16:  tooltipText = (String) obj.get("IgnoreOnlyTargetSelectAndPreferredParts"); break;
                        case 17:  tooltipText = (String) obj.get("Untattleable"); break;
                        case 18:  tooltipText = (String) obj.get("JumplikeCannotTarget"); break;
                        case 19:  tooltipText = (String) obj.get("HammerlikeCannotTarget"); break;
                        case 20:  tooltipText = (String) obj.get("ShellTosslikeCannotTarget"); break;
                        case 21:  tooltipText = (String) obj.get("PreventHealthDecrease"); break;
                        case 22:  tooltipText = (String) obj.get("DisablePartVisibility"); break;
                        case 23:  tooltipText = (String) obj.get("ImmuneToCustom"); break;
                        case 24: tooltipText = (String) obj.get("BlurOn"); break;
                        case 25: tooltipText = (String) obj.get("ScaleIndependence"); break;
                        case 26: tooltipText = (String) obj.get("Independence"); break;
                        case 27: tooltipText = (String) obj.get("IsImmuneToDamageOrStatus"); break;
                        case 28: tooltipText = (String) obj.get("IsImmuneToOHKO"); break;
                        case 29:  tooltipText = (String) obj.get("IsImmuneToStatus"); break;
                        case 30:  tooltipText = (String) obj.get("TopSpiky"); break;
                        case 31:  tooltipText = (String) obj.get("PreemptiveFrontSpiky"); break;
                        case 32:  tooltipText = (String) obj.get("FrontSpiky"); break;
                        case 33:  tooltipText = (String) obj.get("Fiery"); break;
                        case 34:  tooltipText = (String) obj.get("FieryStatus"); break;
                        case 35:  tooltipText = (String) obj.get("Icy"); break;
                        case 36:  tooltipText = (String) obj.get("IcyStatus"); break;
                        case 37:  tooltipText = (String) obj.get("Poison"); break;
                        case 38: tooltipText = (String) obj.get("PoisonStatus"); break;
                        case 39: tooltipText = (String) obj.get("Electric"); break;
                        case 40: tooltipText = (String) obj.get("ElectricStatus"); break;
                        case 41: tooltipText = (String) obj.get("Explosive"); break;
                        case 42: tooltipText = (String) obj.get("VolatileExplosive"); break;
                    }
                    break;
                }

                case 3: 
                { 
                    JSONArray structArray = (JSONArray) root.get("HealthUpgrades");
                    JSONObject obj = (JSONObject) structArray.get(0);

                    switch (fieldIndex) 
                    {
                        case 1:  tooltipText = (String) obj.get("startHP"); break;
                        case 2:  tooltipText = (String) obj.get("startFP"); break;
                        case 3:  tooltipText = (String) obj.get("startBP"); break;
                        case 4:  tooltipText = (String) obj.get("upgradeHP"); break;
                        case 5:  tooltipText = (String) obj.get("upgradeFP"); break;
                        case 6:  tooltipText = (String) obj.get("upgradeBP"); break;
                        case 7:  tooltipText = (String) obj.get("PARTNER1"); break;
                        case 8:  tooltipText = (String) obj.get("PARTNER2"); break;
                        case 9:  tooltipText = (String) obj.get("PARTNER3"); break;
                    }
                    break; 
                }

                case 4: 
                {
                    JSONArray structArray = (JSONArray) root.get("BattleUnitDefense");
                    JSONObject obj = (JSONObject) structArray.get(0);

                    switch (fieldIndex) 
                    {
                        case 1:  tooltipText = (String) obj.get("normal"); break;
                        case 2:  tooltipText = (String) obj.get("fire"); break;
                        case 3:  tooltipText = (String) obj.get("ice"); break;
                        case 4:  tooltipText = (String) obj.get("explosion"); break;
                        case 5:  tooltipText = (String) obj.get("electric"); break;
                    }
                    break; 
                }

                case 5: 
                {
                    JSONArray structArray = (JSONArray) root.get("BattleUnitDefenseAttr");
                    JSONObject obj = (JSONObject) structArray.get(0);

                    switch (fieldIndex) 
                    {
                        case 1:  tooltipText = (String) obj.get("normal"); break;
                        case 2:  tooltipText = (String) obj.get("fire"); break;
                        case 3:  tooltipText = (String) obj.get("ice"); break;
                        case 4:  tooltipText = (String) obj.get("explosion"); break;
                        case 5:  tooltipText = (String) obj.get("electric"); break;
                    }
                    break; 
                }

                case 6: 
                {
                    JSONArray structArray = (JSONArray) root.get("StatusVulnerability");
                    JSONObject obj = (JSONObject) structArray.get(0);

                    switch (fieldIndex) 
                    {
                        case 1:  tooltipText = (String) obj.get("sleep"); break;
                        case 2:  tooltipText = (String) obj.get("stop"); break;
                        case 3:  tooltipText = (String) obj.get("dizzy"); break;
                        case 4:  tooltipText = (String) obj.get("poison"); break;
                        case 5:  tooltipText = (String) obj.get("confuse"); break;
                        case 6:  tooltipText = (String) obj.get("electric"); break;
                        case 7:  tooltipText = (String) obj.get("burn"); break;
                        case 8:  tooltipText = (String) obj.get("freeze"); break;
                        case 9:  tooltipText = (String) obj.get("huge"); break;
                        case 10: tooltipText = (String) obj.get("tiny"); break;
                        case 11: tooltipText = (String) obj.get("attack_up"); break;
                        case 12: tooltipText = (String) obj.get("attack_down"); break;
                        case 13: tooltipText = (String) obj.get("defense_up"); break;
                        case 14: tooltipText = (String) obj.get("defense_down"); break;
                        case 15:  tooltipText = (String) obj.get("allergic"); break;
                        case 16:  tooltipText = (String) obj.get("fright"); break;
                        case 17:  tooltipText = (String) obj.get("gale_force"); break;
                        case 18:  tooltipText = (String) obj.get("fast"); break;
                        case 19:  tooltipText = (String) obj.get("slow"); break;
                        case 20:  tooltipText = (String) obj.get("dodgy"); break;
                        case 21:  tooltipText = (String) obj.get("invisible"); break;
                        case 22:  tooltipText = (String) obj.get("ohko"); break;
                    }
                    break;
                }

                case 7: 
                {
                    JSONArray structArray = (JSONArray) root.get("BattleWeapon");
                    JSONObject obj = (JSONObject) structArray.get(0);

                    switch (fieldIndex) 
                    {
                        case 1: tooltipText = (String) obj.get("accuracy"); break;
                        case 2: tooltipText = (String) obj.get("fp_cost"); break;
                        case 3: tooltipText = (String) obj.get("sp_cost"); break;
                        case 4: tooltipText = (String) obj.get("superguard_state"); break;
                        case 5: tooltipText = (String) obj.get("sylish_multiplier"); break;
                        case 6: tooltipText = (String) obj.get("bingo_slot_inc_chance"); break;
                        case 7: tooltipText = (String) obj.get("base_damage_fn"); break;
                        case 8: tooltipText = (String) obj.get("base_damage1"); break;
                        case 9: tooltipText = (String) obj.get("base_damage2"); break;
                        case 10: tooltipText = (String) obj.get("base_damage3"); break;
                        case 11: tooltipText = (String) obj.get("base_damage4"); break;
                        case 12: tooltipText = (String) obj.get("base_damage5"); break;
                        case 13: tooltipText = (String) obj.get("base_damage6"); break;
                        case 14: tooltipText = (String) obj.get("base_damage7"); break;
                        case 15: tooltipText = (String) obj.get("base_damage8"); break;
                        case 16: tooltipText = (String) obj.get("base_fpdamage1"); break;
                        case 17: tooltipText = (String) obj.get("base_fpdamage2"); break;
                        case 18: tooltipText = (String) obj.get("base_fpdamage3"); break;
                        case 19: tooltipText = (String) obj.get("base_fpdamage4"); break;
                        case 20: tooltipText = (String) obj.get("base_fpdamage5"); break;
                        case 21: tooltipText = (String) obj.get("base_fpdamage6"); break;
                        case 22: tooltipText = (String) obj.get("base_fpdamage7"); break;
                        case 23: tooltipText = (String) obj.get("base_fpdamage8"); break;
                        case 24: tooltipText = (String) obj.get("element"); break;
                        case 25: tooltipText = (String) obj.get("damage_pattern"); break;
                        case 26: tooltipText = (String) obj.get("ac_level"); break;
                        case 27: tooltipText = (String) obj.get("sleep_chance"); break;
                        case 28: tooltipText = (String) obj.get("sleep_time"); break;
                        case 29: tooltipText = (String) obj.get("stop_chance"); break;
                        case 30: tooltipText = (String) obj.get("stop_time"); break;
                        case 31: tooltipText = (String) obj.get("dizzy_chance"); break;
                        case 32: tooltipText = (String) obj.get("dizzy_time"); break;
                        case 33: tooltipText = (String) obj.get("poison_chance"); break;
                        case 34: tooltipText = (String) obj.get("poison_time"); break;
                        case 35: tooltipText = (String) obj.get("poison_strength"); break;
                        case 36: tooltipText = (String) obj.get("confuse_chance"); break;
                        case 37: tooltipText = (String) obj.get("confuse_time"); break;
                        case 38: tooltipText = (String) obj.get("electric_chance"); break;
                        case 39: tooltipText = (String) obj.get("electric_time"); break;
                        case 40: tooltipText = (String) obj.get("dodgy_chance"); break;
                        case 41: tooltipText = (String) obj.get("dodgy_time"); break;
                        case 42: tooltipText = (String) obj.get("burn_chance"); break;
                        case 43: tooltipText = (String) obj.get("burn_time"); break;
                        case 44: tooltipText = (String) obj.get("freeze_chance"); break;
                        case 45: tooltipText = (String) obj.get("freeze_time"); break;
                        case 46: tooltipText = (String) obj.get("size_change_chance"); break;
                        case 47: tooltipText = (String) obj.get("size_change_time"); break;
                        case 48: tooltipText = (String) obj.get("size_change_strength"); break;
                        case 49: tooltipText = (String) obj.get("atk_change_chance"); break;
                        case 50: tooltipText = (String) obj.get("atk_change_time"); break;
                        case 51: tooltipText = (String) obj.get("atk_change_strength"); break;
                        case 52: tooltipText = (String) obj.get("def_change_chance"); break;
                        case 53: tooltipText = (String) obj.get("def_change_time"); break;
                        case 54: tooltipText = (String) obj.get("def_change_strength"); break;
                        case 55: tooltipText = (String) obj.get("allergic_chance"); break;
                        case 56: tooltipText = (String) obj.get("allergic_time"); break;
                        case 57: tooltipText = (String) obj.get("ohko_chance"); break;
                        case 58: tooltipText = (String) obj.get("charge_strength"); break;
                        case 59: tooltipText = (String) obj.get("fast_chance"); break;
                        case 60: tooltipText = (String) obj.get("fast_time"); break;
                        case 61: tooltipText = (String) obj.get("slow_chance"); break;
                        case 62: tooltipText = (String) obj.get("slow_time"); break;
                        case 63: tooltipText = (String) obj.get("fright_chance"); break;
                        case 64: tooltipText = (String) obj.get("gale_force_chance"); break;
                        case 65: tooltipText = (String) obj.get("payback_time"); break;
                        case 66: tooltipText = (String) obj.get("hold_fast_time"); break;
                        case 67: tooltipText = (String) obj.get("invisible_chance"); break;
                        case 68: tooltipText = (String) obj.get("invisible_time"); break;
                        case 69: tooltipText = (String) obj.get("hp_regen_time"); break;
                        case 70: tooltipText = (String) obj.get("hp_regen_strength"); break;
                        case 71: tooltipText = (String) obj.get("fp_regen_time"); break;
                        case 72: tooltipText = (String) obj.get("fp_regen_strength"); break;
                        case 73: tooltipText = (String) obj.get("attack_evt"); break;
                        case 74: tooltipText = (String) obj.get("stage_background_fallweight1"); break;
                        case 75: tooltipText = (String) obj.get("stage_background_fallweight2"); break;
                        case 76: tooltipText = (String) obj.get("stage_background_fallweight3"); break;
                        case 77: tooltipText = (String) obj.get("stage_background_fallweight4"); break;
                        case 78: tooltipText = (String) obj.get("stage_nozzle_turn_chance"); break;
                        case 79: tooltipText = (String) obj.get("stage_nozzle_fire_chance"); break;
                        case 80: tooltipText = (String) obj.get("stage_ceiling_fall_chance"); break;
                        case 81: tooltipText = (String) obj.get("stage_object_fall_chance"); break;
                        case 82: tooltipText = (String) obj.get("CannotTargetMarioOrShellShield"); break;
                        case 83: tooltipText = (String) obj.get("CannotTargetPartner"); break;
                        case 84: tooltipText = (String) obj.get("CannotTargetEnemy"); break;
                        case 85: tooltipText = (String) obj.get("CannotTargetTreeOrSwitch"); break;
                        case 86: tooltipText = (String) obj.get("CannotTargetSystem"); break;
                        case 87: tooltipText = (String) obj.get("CannotTargetOppositeAlliance"); break;
                        case 88: tooltipText = (String) obj.get("CannotTargetOwnAlliance"); break;
                        case 89: tooltipText = (String) obj.get("CannotTargetSelf"); break;
                        case 90: tooltipText = (String) obj.get("CannotTargetSameSpecies"); break;
                        case 91: tooltipText = (String) obj.get("OnlyTargetSelf"); break;
                        case 92: tooltipText = (String) obj.get("OnlyTargetMario"); break;
                        case 93: tooltipText = (String) obj.get("OnlyTargetTreeOrSwitch"); break;
                        case 94: tooltipText = (String) obj.get("OnlyTargetPreferredParts"); break;
                        case 95: tooltipText = (String) obj.get("OnlyTargetSelectParts"); break;
                        case 96: tooltipText = (String) obj.get("SingleTarget"); break;
                        case 97: tooltipText = (String) obj.get("MultipleTarget"); break;
                        case 98: tooltipText = (String) obj.get("CannotTargetAnything"); break;
                        case 99: tooltipText = (String) obj.get("Tattlelike"); break;
                        case 100: tooltipText = (String) obj.get("CannotTargetFloating2"); break;
                        case 101: tooltipText = (String) obj.get("CannotTargetCeiling"); break;
                        case 102: tooltipText = (String) obj.get("CannotTargetFloating"); break;
                        case 103: tooltipText = (String) obj.get("CannotTargetGrounded"); break;
                        case 104: tooltipText = (String) obj.get("Jumplike"); break;
                        case 105: tooltipText = (String) obj.get("Hammerlike"); break;
                        case 106: tooltipText = (String) obj.get("ShellTosslike"); break;
                        case 107: tooltipText = (String) obj.get("CannotTargetGroundedVariant"); break;
                        case 108: tooltipText = (String) obj.get("RecoilDamage"); break;
                        case 109: tooltipText = (String) obj.get("CanOnlyTargetFrontmost"); break;
                        case 110: tooltipText = (String) obj.get("CannotTargetShellShield"); break;
                        case 111: tooltipText = (String) obj.get("CannotTargetCustom"); break;
                        case 112: tooltipText = (String) obj.get("TargetSameAllianceDirection"); break;
                        case 113: tooltipText = (String) obj.get("TargetOppositeAllianceDirection"); break;
                        case 114: tooltipText = (String) obj.get("BadgeCanAffectPower"); break;
                        case 115: tooltipText = (String) obj.get("StatusCanAffectPower"); break;
                        case 116: tooltipText = (String) obj.get("IsChargeable"); break;
                        case 117: tooltipText = (String) obj.get("CannotMiss"); break;
                        case 118: tooltipText = (String) obj.get("DiminishingReturnsByHit"); break;
                        case 119: tooltipText = (String) obj.get("DiminishingReturnsByTarget"); break;
                        case 120: tooltipText = (String) obj.get("PiercesDefense"); break;
                        case 121: tooltipText = (String) obj.get("CanBreakIce"); break;
                        case 122: tooltipText = (String) obj.get("IgnoreTargetStatusVulnerability"); break;
                        case 123: tooltipText = (String) obj.get("SPFx200"); break;
                        case 124: tooltipText = (String) obj.get("IgnitesIfBurned"); break;
                        case 125: tooltipText = (String) obj.get("PlayActiveFXSound"); break;
                        case 126: tooltipText = (String) obj.get("FlipsShellEnemies"); break;
                        case 127: tooltipText = (String) obj.get("FlipsBombFlippableEnemies"); break;
                        case 128: tooltipText = (String) obj.get("GroundsWingedEnemies"); break;
                        case 129: tooltipText = (String) obj.get("SPFx8000"); break;
                        case 130: tooltipText = (String) obj.get("CanBeUsedAsConfusedAction"); break;
                        case 131: tooltipText = (String) obj.get("Unguardable"); break;
                        case 132: tooltipText = (String) obj.get("CanHitClonelike"); break;
                        case 133: tooltipText = (String) obj.get("Electric"); break;
                        case 134: tooltipText = (String) obj.get("TopSpiky"); break;
                        case 135: tooltipText = (String) obj.get("PreemptiveFrontSpiky"); break;
                        case 136: tooltipText = (String) obj.get("FrontSpiky"); break;
                        case 137: tooltipText = (String) obj.get("Fiery"); break;
                        case 138: tooltipText = (String) obj.get("Icy"); break;
                        case 139: tooltipText = (String) obj.get("Poison"); break;
                        case 140: tooltipText = (String) obj.get("Explosive"); break;
                        case 141: tooltipText = (String) obj.get("VolatileExplosive"); break;
                        case 142: tooltipText = (String) obj.get("Payback"); break;
                        case 143: tooltipText = (String) obj.get("HoldFast"); break;
                        case 144: tooltipText = (String) obj.get("PreferMario"); break;
                        case 145: tooltipText = (String) obj.get("PreferPartner"); break;
                        case 146: tooltipText = (String) obj.get("PreferFront"); break;
                        case 147: tooltipText = (String) obj.get("PreferBack"); break;
                        case 148: tooltipText = (String) obj.get("PreferSameAlliance"); break;
                        case 149: tooltipText = (String) obj.get("PreferOppositeAlliance"); break;
                        case 150: tooltipText = (String) obj.get("PreferLessHealthy"); break;
                        case 151: tooltipText = (String) obj.get("GreatlyPreferLessHealthy"); break;
                        case 152: tooltipText = (String) obj.get("PreferLowerHP"); break;
                        case 153: tooltipText = (String) obj.get("PreferHigherHP"); break;
                        case 154: tooltipText = (String) obj.get("PreferInPeril"); break;
                        case 155: tooltipText = (String) obj.get("TWFx2000"); break;
                        case 156: tooltipText = (String) obj.get("ChooseWeightedRandomly"); break;
                    }
                    break;
                }
            }

            if (tooltipText != null && !tooltipText.isBlank()) 
            {
                Tooltip tooltip = new Tooltip(tooltipText);

                tooltip.setShowDelay(Duration.millis(150));
                tooltip.setHideDelay(Duration.ZERO);

                temp.setOnMouseEntered(e -> 
                {
                    if (temp.getScene() == null || temp.getScene().getWindow() == null)
                    {
                        return;
                    }
                    tooltip.show(temp, e.getScreenX() + 10, e.getScreenY() + 10);
                });

                temp.setOnMouseMoved(e -> 
                {
                    if (tooltip.isShowing()) 
                    {
                        tooltip.setX(e.getScreenX() + 10);
                        tooltip.setY(e.getScreenY() + 10);
                    }
                });

                temp.setOnMouseExited(e -> tooltip.hide());
                temp.sceneProperty().addListener((obs, oldScene, newScene) -> tooltip.hide());
            }

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

        return temp;
    }

    /**
     * @Author Jemaroo
     * @Function Initializes the data field arrays to their max sizes
     */
    public void initializeFields()
    {
        textFields = new TextField[75];
        for(int i = 0; i < textFields.length; i++)
        {
            textFields[i] = new TextField();
        }

        comboBoxFields = new ComboBox[6];
        for(int i = 0; i < comboBoxFields.length; i++)
        {
            comboBoxFields[i] = new ComboBox<String>();
            comboBoxFields[i].setMaxWidth(255);
        }

        checkBoxFields = new CheckBox[75];
        for(int i = 0; i < checkBoxFields.length; i++)
        {
            checkBoxFields[i] = new CheckBox();
        }

        listeners = new ChangeListener[75];
        for(int i = 0; i < listeners.length; i++)
        {
            int temp = i;
            listeners[temp] = (obs, oldText, newText) -> { if ("0".equals(newText)) {textFields[temp].setStyle(RED_STYLE);} else {textFields[temp].setStyle(BLACK_STYLE);}};
        }
    }

    /**
     * @Author Jemaroo
     * @Function Adds every file priority to the hashmap
     */
    public void setFilePriority()
    {
        filePriority.put("main.dol", 1);
        filePriority.put("Start.dol", 2);
        filePriority.put("aaa.rel", 3);
        filePriority.put("gor.rel", 4);
        filePriority.put("tik.rel", 5);
        filePriority.put("hei.rel", 6);
        filePriority.put("nok.rel", 7);
        filePriority.put("gon.rel", 8);
        filePriority.put("win.rel", 9);
        filePriority.put("mri.rel", 10);
        filePriority.put("tou.rel", 11);
        filePriority.put("tou2.rel", 12);
        filePriority.put("usu.rel", 13);
        filePriority.put("gra.rel", 14);
        filePriority.put("jin.rel", 15);
        filePriority.put("muj.rel", 16);
        filePriority.put("dou.rel", 17);
        filePriority.put("rsh.rel", 18);
        filePriority.put("hom.rel", 19);
        filePriority.put("eki.rel", 20);
        filePriority.put("pik.rel", 21);
        filePriority.put("bom.rel", 22);
        filePriority.put("moo.rel", 23);
        filePriority.put("aji.rel", 24);
        filePriority.put("las.rel", 25);
        filePriority.put("jon.rel", 26);
    }

    /**
     * @Author Jemaroo
     * @Function Adds or removes listeners to all text fields to set the text color to red if 0
     */
    public void setRed0TextFieldFormats(boolean activation)
    {
        if(activation)
        {
            for(int i = 0; i < textFields.length; i++)
            {
                textFields[i].textProperty().addListener(listeners[i]);
            }

            for(TextField tF : textFields)
            {
                applyRBStyle(tF);
            }
        }
        if(!activation)
        {
            for(int i = 0; i < textFields.length; i++)
            {
                textFields[i].textProperty().removeListener(listeners[i]);
            }

            for(TextField tF : textFields)
            {
                tF.setStyle(BLACK_STYLE);
            }
        }
    }

    /**
     * @Author Jemaroo
     * @Function Sets the text style to red or black depending on value
     */
    private void applyRBStyle(TextField field) 
    {
        field.setStyle("0".equals(field.getText()) ? RED_STYLE : BLACK_STYLE);
    }

    public static void main(String[] args) 
    {
        launch(args);
    }
}