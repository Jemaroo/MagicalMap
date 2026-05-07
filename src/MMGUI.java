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

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MMGUI extends Application 
{
    private ArrayList<String> validFileNames = new ArrayList<String>();
    boolean isFileOpened = false;
    Stage window;
    File givenFile;
    ArrayList<MMData> fileData = new ArrayList<MMData>();
    HBox topMenu = new HBox();
    Button openButton = new Button("Open Directory");
    ComboBox<File> fileSelector = new ComboBox<>();
    Button exportButton = new Button("Save and Export File");
    Button closeButton = new Button("Close File");
    Button optionButton = new Button("Options");
    Button aboutButton = new Button("About");
    HBox upperBox = new HBox();
    BorderPane borderPane = new BorderPane();

    HashMap<String, Image> images = new HashMap<String, Image>();
    Map<String, Integer> filePriority = new HashMap<String, Integer>();

    public static BooleanProperty mowzFlag = new SimpleBooleanProperty(true);

    @Override
    public void start(Stage primaryStage) 
    {
        try
        {
            File jsonFile = new File("src\\MiscData.json");
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

        setFilePriority();
        images = GUI.setImages(images);

        //Window
        window = primaryStage;
        window.setTitle("Miscellaneous Edits");

        //Menu Buttons
        topMenu.getChildren().addAll(openButton, optionButton, aboutButton);
        topMenu.setPadding(new Insets(5));
        topMenu.setSpacing(5);

        //Alligning Menu Buttons to Top
        borderPane.setTop(topMenu);

        fileSelector.setMaxWidth(270);

        //Scene
        Scene emptyScene = new Scene(borderPane, 650, 500);
        window.setScene(emptyScene);

        String startPath = "";
        try
        {
            File jsonFile = new File("src\\options.json");
            JSONParser parser = new JSONParser();
            JSONObject root = (JSONObject)parser.parse(new FileReader(jsonFile));

            if(((String)root.get("MMLastFolder")).equals("true")) startPath = (String)root.get("startPath");
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
                    saveFields();

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

                    byte[] newFileData = MMMain.buildNewFile(fileSelector.getSelectionModel().getSelectedItem(), fileData);
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
                            successBox.getIcons().add(images.get("cog"));

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
            }
        });

        closeButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
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
                optionsBox.getIcons().add(images.get("cog"));

                CheckBox lastFolderBox = new CheckBox();
                Button saveOptionsButton = new Button("Save Options");

                try
                {
                    File jsonFile = new File("src\\options.json");
                    JSONParser parser = new JSONParser();
                    JSONObject root = (JSONObject)parser.parse(new FileReader(jsonFile));

                    if(((String)root.get("MMLastFolder")).equals("true")) lastFolderBox.setSelected(true); else lastFolderBox.setSelected(false);
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

                            if(lastFolderBox.isSelected()) root.put("MMLastFolder", "true");
                            else root.put("MMLastFolder", "false");

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
                alertBox.getIcons().add(images.get("cog"));

                VBox alertMenu = new VBox();
                alertMenu.setAlignment(Pos.CENTER);
                Text versionText = new Text("Magical Map Version: " + GUI.version);
                versionText.setWrappingWidth(290);
                versionText.setTextAlignment(TextAlignment.CENTER);
                Text creditText = new Text("Miscellaneous Edits Written by Jemaroo");
                creditText.setWrappingWidth(290);
                creditText.setTextAlignment(TextAlignment.CENTER);
                Text description = new Text("Miscellaneous Edits allows changing some various features of the game that don't fit into any other tool.");
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

        window.getIcons().add(images.get("cog"));
        window.show();
    }

    /**
     * @Author Jemaroo
     * @Function Opens the directory and sets up the correct fields
     */
    private void loadGUIMenus()
    {
        ArrayList<File> validFiles = IDTMain.findMatchingFiles(givenFile, validFileNames);

        fileSelector.getItems().clear();
        topMenu.getChildren().clear();
        topMenu.getChildren().addAll(openButton, fileSelector, exportButton, closeButton, optionButton, aboutButton);

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
            fileData = MMMain.getMiscData(fileSelector.getSelectionModel().getSelectedItem());
            isFileOpened = true;
            test.testMiscData(fileData);

            ArrayList<Tab> tabs = new ArrayList<Tab>();

            for(MMData mm : fileData)
            {
                Tab temp = new Tab(mm.type);
                temp.setClosable(false);

                GridPane tabForm = new GridPane();

                for(int i = 0; i < mm.miscData.size(); i++)
                {
                    if(mm.miscData.get(i) instanceof Misc.oneint)
                    {
                        tabForm.add(determineMiscIcon((Misc.oneint)mm.miscData.get(i)), 0, i);
                        tabForm.add(new Label(((Misc.oneint)mm.miscData.get(i)).name + ":"), 1, i);
                        tabForm.add(((Misc.oneint)mm.miscData.get(i)).textField, 2, i);
                    }
                    else if(mm.miscData.get(i) instanceof Misc.twoint)
                    {
                        tabForm.add(determineMiscIcon((Misc.twoint)mm.miscData.get(i)), 0, i);
                        tabForm.add(new Label(((Misc.twoint)mm.miscData.get(i)).name + ":"), 1, i);
                        tabForm.add(((Misc.twoint)mm.miscData.get(i)).textField, 2, i);
                    }
                    else if(mm.miscData.get(i) instanceof Misc.twointConstraint)
                    {
                        tabForm.add(determineMiscIcon((Misc.twointConstraint)mm.miscData.get(i)), 0, i);
                        tabForm.add(new Label(((Misc.twointConstraint)mm.miscData.get(i)).name + ":"), 1, i);
                        tabForm.add(((Misc.twointConstraint)mm.miscData.get(i)).textField, 2, i);
                    }
                    else if(mm.miscData.get(i) instanceof Misc.twointWpatch)
                    {
                        tabForm.add(determineMiscIcon((Misc.twointWpatch)mm.miscData.get(i)), 0, i);
                        tabForm.add(new Label(((Misc.twointWpatch)mm.miscData.get(i)).name + ":"), 1, i);
                        tabForm.add(((Misc.twointWpatch)mm.miscData.get(i)).textField, 2, i);
                    }
                    else if(mm.miscData.get(i) instanceof Misc.fourint)
                    {
                        tabForm.add(determineMiscIcon((Misc.fourint)mm.miscData.get(i)), 0, i);
                        tabForm.add(new Label(((Misc.fourint)mm.miscData.get(i)).name + ":"), 1, i);
                        tabForm.add(((Misc.fourint)mm.miscData.get(i)).textField, 2, i);
                    }
                    else if(mm.miscData.get(i) instanceof Misc.fourintPN)
                    {
                        tabForm.add(determineMiscIcon((Misc.fourintPN)mm.miscData.get(i)), 0, i);
                        tabForm.add(new Label(((Misc.fourintPN)mm.miscData.get(i)).name + ":"), 1, i);
                        tabForm.add(((Misc.fourintPN)mm.miscData.get(i)).textField, 2, i);
                    }
                    else if(mm.miscData.get(i) instanceof Misc.Float)
                    {
                        tabForm.add(determineMiscIcon((Misc.Float)mm.miscData.get(i)), 0, i);
                        tabForm.add(new Label(((Misc.Float)mm.miscData.get(i)).name + ":"), 1, i);
                        tabForm.add(((Misc.Float)mm.miscData.get(i)).textField, 2, i);
                    }
                    else if(mm.miscData.get(i) instanceof Misc.hexColor)
                    {
                        tabForm.add(determineMiscIcon((Misc.hexColor)mm.miscData.get(i)), 0, i);
                        tabForm.add(new Label(((Misc.hexColor)mm.miscData.get(i)).name + ":"), 1, i);
                        tabForm.add(((Misc.hexColor)mm.miscData.get(i)).colorPicker, 2, i);
                    }
                    else if(mm.miscData.get(i) instanceof Misc.odds)
                    {
                        tabForm.add(determineMiscIcon((Misc.odds)mm.miscData.get(i)), 0, i);
                        tabForm.add(new Label(((Misc.odds)mm.miscData.get(i)).name + ":"), 1, i);
                        tabForm.add(((Misc.odds)mm.miscData.get(i)).textField, 2, i);
                    }
                    else if(mm.miscData.get(i) instanceof Misc.oddsRev)
                    {
                        tabForm.add(determineMiscIcon((Misc.oddsRev)mm.miscData.get(i)), 0, i);
                        tabForm.add(new Label(((Misc.oddsRev)mm.miscData.get(i)).name + ":"), 1, i);
                        tabForm.add(((Misc.oddsRev)mm.miscData.get(i)).textField, 2, i);
                    }
                    else if(mm.miscData.get(i) instanceof Misc.Function)
                    {
                        tabForm.add(determineMiscIcon((Misc.Function)mm.miscData.get(i)), 0, i);
                        tabForm.add(new Label(((Misc.Function)mm.miscData.get(i)).name + ":"), 1, i);
                        tabForm.add(((Misc.Function)mm.miscData.get(i)).checkBox, 2, i);
                    }
                    else if(mm.miscData.get(i) instanceof Misc.bingoSelectionBox)
                    {
                        tabForm.add(fieldImageViewCreator(images.get("emptyBingoCustom")), 0, i);
                        tabForm.add(new Label(((Misc.bingoSelectionBox)mm.miscData.get(i)).name + ":"), 1, i);
                        setBingoSelectionBox(((Misc.bingoSelectionBox)(mm.miscData.get(i))).comboBox);
                        tabForm.add(((Misc.bingoSelectionBox)mm.miscData.get(i)).comboBox, 2, i);
                    }
                    else
                    {
                        tabForm.add(fieldImageViewCreator(images.get("unknown")), 0, i);
                        tabForm.add(new Label(((Misc.Function)mm.miscData.get(i)).name + ":"), 1, i);
                    }
                }

                tabForm.setStyle("-fx-font-size: 12px;");
                tabForm.setVgap(5);
                tabForm.setHgap(10);
                tabForm.setPadding(new Insets(10));

                ScrollPane scrollTab = new ScrollPane();
                scrollTab.setContent(tabForm);
                temp.setContent(scrollTab);
                tabs.add(temp);
            }
            
            TabPane miscPane = new TabPane();
            for(Tab t : tabs)
            {
                miscPane.getTabs().add(t);
            }

            miscPane.setStyle("-fx-tab-min-width: 80px; -fx-tab-max-width: 80px; -fx-tab-min-height: 25px; -fx-font-size: 16px;");
            borderPane.setCenter(miscPane);
        });
    }

    /**
     * @Author Jemaroo
     * @Function Saves the fields to the values
     */
    private void saveFields() 
    {
        for(MMData mm : fileData)
        {
            for(Object o : mm.miscData)
            {
                if(o instanceof Misc.oneint oo)
                {
                    oo.value = Integer.parseInt(oo.textField.getText());
                }
                else if(o instanceof Misc.twoint oo)
                {
                    oo.value = Integer.parseInt(oo.textField.getText());
                }
                else if(o instanceof Misc.twointConstraint oo)
                {
                    if(Integer.parseInt(oo.textField.getText()) < oo.low) {oo.value = oo.low;}
                    else if(Integer.parseInt(oo.textField.getText()) > oo.high) {oo.value = oo.high;}
                    else{oo.value = Integer.parseInt(oo.textField.getText());}
                }
                else if(o instanceof Misc.twointWpatch oo)
                {
                    oo.value = Integer.parseInt(oo.textField.getText());
                }
                else if(o instanceof Misc.fourint oo)
                {
                    oo.value = Long.parseLong(oo.textField.getText());
                }
                else if(o instanceof Misc.fourintPN oo)
                {
                    oo.value = Long.parseLong(oo.textField.getText());
                }
                else if(o instanceof Misc.Float oo)
                {
                    oo.value = Float.parseFloat(oo.textField.getText());
                }
                else if(o instanceof Misc.hexColor oo)
                {
                    oo.colorValue = oo.colorPicker.getValue();
                }
                else if(o instanceof Misc.odds oo)
                {
                    String[] tempStrings = oo.textField.getText().split("/");
                    oo.successRate = Long.parseLong(tempStrings[0]);
                    if(tempStrings.length > 1) {oo.outOf = Long.parseLong(tempStrings[1]);}
                }
                else if(o instanceof Misc.oddsRev oo)
                {
                    String[] tempStrings = oo.textField.getText().split("/");
                    oo.successRate = Long.parseLong(tempStrings[0]);
                    if(tempStrings.length > 1) {oo.outOf = Long.parseLong(tempStrings[1]);}
                }
                else if(o instanceof Misc.Function oo)
                {
                    oo.rewrite = oo.checkBox.isSelected();
                }
                else if(o instanceof Misc.bingoSelectionBox oo)
                {
                    oo.value = oo.comboBox.getSelectionModel().getSelectedIndex();
                }
            }
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
     * @Function Returns the area name based on file name
     */
    public String fileNameSelector(String name)
    {
        switch(name)
        {
            case "main.dol": return "System";
            case "Start.dol": return "System";
            case "aji.rel": return "X-Naut Fortress";
            case "bom.rel": return "Fahr Outpost";
            case "dou.rel": return "Pirate's Grotto";
            case "eki.rel": return "Riverside Station";
            case "gon.rel": return "Hooktail Castle";
            case "gor.rel": return "Rogueport";
            case "gra.rel": return "Twilight Trail";
            case "hei.rel": return "Petal Meadows";
            case "hom.rel": return "Train Cutscenes";
            case "jin.rel": return "Creepy Steeple";
            case "jon.rel": return "Pit of 100 Trials";
            case "las.rel": return "Palace of Shadow";
            case "moo.rel": return "The Moon";
            case "mri.rel": return "Boggly Tree";
            case "muj.rel": return "Keelhaul Key";
            case "nok.rel": return "Petalburg";
            case "pik.rel": return "Poshley Heights";
            case "rsh.rel": return "Excess Express";
            case "tik.rel": return "Rogueport Sewers";
            case "tou.rel": return "Glitzville";
            case "tou2.rel": return "Glitz Pit";
            case "usu.rel": return "Twilight Town";
            case "win.rel": return "Boggly Woods";
            default: return name;
        }
    }

    /**
     * @Author Jemaroo
     * @Function Returns an image based on file name
     */
    public ImageView fileImageSelector(String name)
    {
        switch(name)
        {
            case "main.dol": return unitImageViewCreator(images.get("magicalMap2"));
            case "Start.dol": return unitImageViewCreator(images.get("magicalMap2"));
            case "aji.rel": return unitImageViewCreator(images.get("unitXNaut"));
            case "bom.rel": return unitImageViewCreator(images.get("fahrOutpostBombCustom"));
            case "dou.rel": return unitImageViewCreator(images.get("unitBillBlaster"));
            case "eki.rel": return unitImageViewCreator(images.get("unitRuffPuff"));
            case "gon.rel": return unitImageViewCreator(images.get("unitHooktail"));
            case "gor.rel": return unitImageViewCreator(images.get("unitProfessorFrankly"));
            case "gra.rel": return unitImageViewCreator(images.get("unitHyperGoomba"));
            case "hei.rel": return unitImageViewCreator(images.get("unitKoopaTroopa"));
            case "hom.rel": return unitImageViewCreator(images.get("unitSmorg"));
            case "jin.rel": return unitImageViewCreator(images.get("unitDoopliss"));
            case "jon.rel": return unitImageViewCreator(images.get("unitBonetail"));
            case "las.rel": return unitImageViewCreator(images.get("unitShadowPeach"));
            case "moo.rel": return unitImageViewCreator(images.get("unitMoonCleft"));
            case "mri.rel": return unitImageViewCreator(images.get("puniCustom"));
            case "muj.rel": return unitImageViewCreator(images.get("unitPutridPiranha"));
            case "nok.rel": return unitImageViewCreator(images.get("koopaCustom"));
            case "pik.rel": return unitImageViewCreator(images.get("unitDarkBoo"));
            case "rsh.rel": return unitImageViewCreator(images.get("serverToadCustom"));
            case "tik.rel": return unitImageViewCreator(images.get("unitBlooper"));
            case "tou.rel": return unitImageViewCreator(images.get("hoggleCustom"));
            case "tou2.rel": return unitImageViewCreator(images.get("unitRawkHawk"));
            case "usu.rel": return unitImageViewCreator(images.get("twilightShopManagerWifeCustom"));
            case "win.rel": return unitImageViewCreator(images.get("unitPalePiranha"));
            default: return unitImageViewCreator(images.get("unknown"));
        }
    }

    /**
     * @Author Jemaroo
     * @Function Returns an image based on misc field name
     */
    public ImageView determineMiscIcon(Misc.oneint misc)
    {
        switch(misc.name)
        {
            case "Normal Guard Frames":
            case "Normal Guard (1 Simp.) Frames":
            case "Normal Guard (2 Simp.) Frames":
            case "Normal Guard (3 Simp.) Frames":
            case "Normal Guard (1 Unsimp.) Frames":
            case "Normal Guard (2 Unsimp.) Frames":
            case "Normal Guard (3 Unsimp.) Frames":
            case "Super Guard Frames":
            case "Super Guard (1 Simp.) Frames":
            case "Super Guard (2 Simp.) Frames":
            case "Super Guard (3 Simp.) Frames":
            case "Super Guard (1 Unsimp.) Frames":
            case "Super Guard (2 Unsimp.) Frames":
            case "Super Guard (3 Unsimp.) Frames": return fieldImageViewCreator(images.get("superChargeAction"));
            
            default: return fieldImageViewCreator(images.get("routingSlip"));
        }
    }

    /**
     * @Author Jemaroo
     * @Function Returns an image based on misc field name
     */
    public ImageView determineMiscIcon(Misc.twoint misc)
    {
        switch(misc.name)
        {
            case "Mario HP Limit": return fieldImageViewCreator(images.get("HPUpgrade"));
            case "Mario FP Limit": return fieldImageViewCreator(images.get("FPUpgrade"));
            case "Mario BP Limit": return fieldImageViewCreator(images.get("BPUpgrade"));
            case "Star Points Required for Level Up":
            case "Experience Level Cutoff": return fieldImageViewCreator(images.get("XP"));
            case "Starting Coin Amount": 
            case "Major League Entry Bonus":
            case "Max Coin Limit": return fieldImageViewCreator(images.get("coin"));
            case "Mario Heart Pickup Heal":
            case "Partner Heart Pickup Heal": return fieldImageViewCreator(images.get("heart"));
            case "Flower Pickup Heal": return fieldImageViewCreator(images.get("flower"));
            case "Inn Stays Special Event Coin Reward":
            case "Inn Stays Special Event Requirement": return fieldImageViewCreator(images.get("innkeeperToadCustom"));
            case "Sleep Wake Chance": return fieldImageViewCreator(images.get("sleepStatus"));
            case "Dodgy Status Dodge Chance": return fieldImageViewCreator(images.get("dodgyStatus"));
            case "Confusion Confused Chance": return fieldImageViewCreator(images.get("confuseStatus"));
            case "Electrified Unable to Act Chance": return fieldImageViewCreator(images.get("electricStatus"));
            case "Burn HP Damage":
            case "Burn FP Damage": return fieldImageViewCreator(images.get("burnStatus"));
            case "Chet Rippo HP Modifier":
            case "Chet Rippo FP Modifier":    
            case "Chet Rippo Mario Cost":
            case "Chet Rippo Partner Cost":
            case "Chet Rippo BP Modifier": return fieldImageViewCreator(images.get("chetRippoCustom"));
            case "Merlee Attack Boost":
            case "Merlee Defense Boost":
            case "Merlee Cheap Path Turn Count":
            case "Merlee Normal Path Turn Count":
            case "Merlee Special Path Turn Count":
            case "Merlee Cheap Path Cost":
            case "Merlee Normal Path Cost":
            case "Merlee Special Path Cost":
            case "Merlee Coin Multiplier": return fieldImageViewCreator(images.get("merleeCustom"));
            case "Happy Lucky Lottery Fourth Prize Base Required Days":
            case "Happy Lucky Lottery Fourth Prize Random Subtraction":
            case "Happy Lucky Lottery Third Prize Base Required Days":
            case "Happy Lucky Lottery Third Prize Random Subtraction":
            case "Happy Lucky Lottery Second Prize Base Required Days":
            case "Happy Lucky Lottery Second Prize Random Subtraction":
            case "Happy Lucky Lottery Grand Prize Base Required Days":
            case "Happy Lucky Lottery Grand Prize Random Subtraction": return fieldImageViewCreator(images.get("lotteryPick"));
            case "Wonky/Grifty Story Price": return fieldImageViewCreator(images.get("wonkyCustom"));
            case "Mushroom Bingo Success SP Multiplier Turn Count": return fieldImageViewCreator(images.get("bingoMushroom"));
            case "Flower Bingo Success SP Multiplier Turn Count": return fieldImageViewCreator(images.get("bingoFlower"));
            case "Star Bingo Success SP Multiplier Turn Count": return fieldImageViewCreator(images.get("bingoStar"));
            case "Shine Bingo Success SP Multiplier Turn Count": return fieldImageViewCreator(images.get("bingoShineSprite"));
            case "Bandit Lost Coins Division": return fieldImageViewCreator(images.get("banditCustom"));
            case "Lumpy 300 Reward":
            case "Lumpy 600 Reward":
            case "Lumpy 999 Reward": return fieldImageViewCreator(images.get("lumpyCustom"));
            case "Mover Move Down 2 Floors Price":
            case "Mover Move Down 2 Floors Floor Amount":
            case "Mover Move Down 5 Floors Price":
            case "Mover Move Down 5 Floors Floor Amount":
            case "Mover Move to Surface Price": return fieldImageViewCreator(images.get("moverCustom"));
            case "Top Spiky Contact Damage": return fieldImageViewCreator(images.get("topSpikeCustom"));
            case "Preemptive Front Spiky Contact Damage": return fieldImageViewCreator(images.get("preFrontSpikeCustom"));
            case "Front Spiky Contact Damage": return fieldImageViewCreator(images.get("frontSpikeCustom"));
            case "Fiery Status Contact Damage": return fieldImageViewCreator(images.get("burnCustom"));
            case "Icy Status Contact Damage": return fieldImageViewCreator(images.get("iceCustom"));
            case "Electric Status Contact Damage": return fieldImageViewCreator(images.get("thunderRage"));
            case "Volatile Explosive Contact Damage": return fieldImageViewCreator(images.get("chargeAction"));
            case "Electrified Contact Damage": return fieldImageViewCreator(images.get("electricStatus"));
            case "Freeze Thaw Damage": return fieldImageViewCreator(images.get("freezeStatus"));
            case "B-List Star Level Requirement": return fieldImageViewCreator(images.get("rankMedalCustom"));
            case "A-List Star Level Requirement": return fieldImageViewCreator(images.get("rankMedalCustom"));
            case "Superstar Level Requirement": return fieldImageViewCreator(images.get("rankMedalCustom"));
            case "Spike/Fire Hazard Damage":
            case "Water Hazard Damage":
            case "Pit Hazard Damage": return fieldImageViewCreator(images.get("fishCustom"));

            default: return fieldImageViewCreator(images.get("routingSlip"));
        }
    }

    /**
     * @Author Jemaroo
     * @Function Returns an image based on misc field name
     */
    public ImageView determineMiscIcon(Misc.twointConstraint misc)
    {
        switch(misc.name)
        {
            case "Max Item Inventory Size":
            case "Strange Sack Max Item Inventory Size":
            case "Max Badge Inventory Size": return fieldImageViewCreator(images.get("strangeSack"));
            
            default: return fieldImageViewCreator(images.get("routingSlip"));
        }
    }

    /**
     * @Author Jemaroo
     * @Function Returns an image based on misc field name
     */
    public ImageView determineMiscIcon(Misc.twointWpatch misc)
    {
        switch(misc.name)
        {
            case "Experience Multiplier": return fieldImageViewCreator(images.get("XP"));
            case "Merlee Experience Multiplier": return fieldImageViewCreator(images.get("merleeCustom"));
            case "Merluvlee Next Path Cost": return fieldImageViewCreator(images.get("merluvleeCustom"));
            case "Merlon Parter Upgrade Shine Cost": return fieldImageViewCreator(images.get("merlonCustom"));
            case "Can Break Ice Flag Probability": return fieldImageViewCreator(images.get("freezeStatus"));
            
            default: return fieldImageViewCreator(images.get("routingSlip"));
        }
    }

    /**
     * @Author Jemaroo
     * @Function Returns an image based on misc field name
     */
    public ImageView determineMiscIcon(Misc.fourint misc)
    {
        switch(misc.name)
        {
            case "Merluvlee Star Piece Path Cost":
            case "Merluvlee Shine Path Cost": return fieldImageViewCreator(images.get("merluvleeCustom"));
            case "1 Pianta Price":
            case "1 Pianta Amount":
            case "5 Pianta Price":
            case "5 Pianta Amount":
            case "10 Pianta Price":
            case "10 Pianta Amount": return fieldImageViewCreator(images.get("pianta"));
            case "Garf Trouble Coin Reward": return fieldImageViewCreator(images.get("unitGus"));
            case "McGoomba Trouble Coin Reward": return fieldImageViewCreator(images.get("mcgoombaCustom"));
            case "Arfur Trouble Coin Reward": return fieldImageViewCreator(images.get("dooganCustom"));
            case "Goomther Trouble Coin Reward": return fieldImageViewCreator(images.get("mcgoombaCustom"));
            case "Bomberto Trouble Coin Reward": return fieldImageViewCreator(images.get("bombertoCustom"));
            case "Puni Elder Trouble Coin Reward": return fieldImageViewCreator(images.get("puniElderCustom"));
            case "Lahla Trouble Pianta Reward": return fieldImageViewCreator(images.get("lahlaCustom"));
            case "Jolene Trouble Coin Reward": return fieldImageViewCreator(images.get("joleneCustom"));
            case "Merlee Trouble Coin Reward": return fieldImageViewCreator(images.get("merleeCustom"));
            case "Mayor Dour Trouble Coin Reward": return fieldImageViewCreator(images.get("mayorDourCustom"));
            case "Chef Shimi Trouble Coin Reward": return fieldImageViewCreator(images.get("chefShimiCustom"));
            case "Goldbob Trouble Coin Reward": return fieldImageViewCreator(images.get("goldbobCustom"));
            case "Gob Trouble Coin Reward": return fieldImageViewCreator(images.get("fahrOutpostBombCustom"));
            case "Doe T. Trouble Coin Reward": return fieldImageViewCreator(images.get("toadCustom"));
            case "Bub Trouble Coin Reward": return fieldImageViewCreator(images.get("bubCustom"));
            case "Toodles Ring Coin Reward": return fieldImageViewCreator(images.get("toodlesCustom"));
            case "Mowz Smooch 0% - 33% Success Heal Amount":
            case "Mowz Smooch 33% - 66% Success Heal Amount":
            case "Mowz Smooch 66% - 99% Success Heal Amount":
            case "Mowz Smooch 100% Success Heal Amount": return fieldImageViewCreator(images.get("mowzPartnerSwitch"));
            
            default: return fieldImageViewCreator(images.get("routingSlip"));
        }
    }

    /**
     * @Author Jemaroo
     * @Function Returns an image based on misc field name
     */
    public ImageView determineMiscIcon(Misc.fourintPN misc)
    {
        switch(misc.name)
        {
            case "Gus Pass Price": return fieldImageViewCreator(images.get("unitGus"));
            case "Ishnail Clue Price": return fieldImageViewCreator(images.get("ishnailCustom"));
            case "Trouble Cancellation Price": return fieldImageViewCreator(images.get("coin"));
            case "Lumpy Donation Price": return fieldImageViewCreator(images.get("lumpyCustom"));
            case "Lottery Ticket Price":
            case "Lottery New Ticket Price":
            case "Lottery Cheating Penalty Price": return fieldImageViewCreator(images.get("lotteryPick"));
            
            default: return fieldImageViewCreator(images.get("routingSlip"));
        }
    }

    /**
     * @Author Jemaroo
     * @Function Returns an image based on misc field name
     */
    public ImageView determineMiscIcon(Misc.Float misc)
    {
        switch(misc.name)
        {
            case "Mario's Run Speed":
            case "Mario's Jump Speed":
            case "Mario's Jump Acceleration":
            case "Mario's Jump Jerk":
            case "Mario's Jump Snap": return fieldImageViewCreator(images.get("marioHeadCustom"));
            case "Bingo Success SP Multiplier Scale":
            case "Shine Bingo Success SP Multiplier Scale": return fieldImageViewCreator(images.get("bingoCustom"));
            
            default: return fieldImageViewCreator(images.get("routingSlip"));
        }
    }

    /**
     * @Author Jemaroo
     * @Function Returns an image based on misc field name
     */
    public ImageView determineMiscIcon(Misc.hexColor misc)
    {
        switch(misc.name)
        {
            case "Mario Menu Color 1":
            case "Mario Menu Color 2":
            case "Mario Menu Color 3": return fieldImageViewCreator(images.get("marioMenuCustom"));
            case "Party Menu Color 1":
            case "Party Menu Color 2":
            case "Party Menu Color 3": return fieldImageViewCreator(images.get("partyMenuCustom"));
            case "Gear Menu Color 1":
            case "Gear Menu Color 2":
            case "Gear Menu Color 3": return fieldImageViewCreator(images.get("gearMenuCustom"));
            case "Badges Menu Color 1":
            case "Badges Menu Color 2":
            case "Badges Menu Color 3": return fieldImageViewCreator(images.get("badgeMenuCustom"));
            case "Journal Menu Color 1":
            case "Journal Menu Color 2":
            case "Journal Menu Color 3": return fieldImageViewCreator(images.get("journalMenuCustom"));
            case "Mailbox SP Normal Mail Color 1":
            case "Mailbox SP Normal Mail Color 2":
            case "Mailbox SP RDM Mail Color 1":
            case "Mailbox SP RDM Mail Color 2":
            case "Mailbox SP Trouble Mail Color 1":
            case "Mailbox SP Trouble Mail Color 2":
            case "Mailbox SP Peach Mail Color 1":
            case "Mailbox SP Peach Mail Color 2": return fieldImageViewCreator(images.get("mailCustom"));
            case "Diamond Star Body Color 1":
            case "Diamond Star Body Color 2":
            case "Diamond Star Body Color 3":
            case "Diamond Star Body Color 4":
            case "Diamond Star Body Color 5":
            case "Diamond Star Overlay Color 1":
            case "Diamond Star Overlay Color 2":
            case "Diamond Star Overlay Color 3":
            case "Diamond Star Overlay Color 4":
            case "Diamond Star Overlay Color 5":
            case "Diamond Star Overlay Color 6":
            case "Diamond Star Overlay Color 7":
            case "Diamond Star Overlay Color 8":
            case "Diamond Star Overlay Color 9":
            case "Diamond Star Overlay Color 10":
            case "Diamond Star Overlay Color 11":
            case "Diamond Star Overlay Color 12": return fieldImageViewCreator(images.get("diamondStar"));
            case "Emerald Star Body Color 1":
            case "Emerald Star Body Color 2":
            case "Emerald Star Body Color 3":
            case "Emerald Star Body Color 4":
            case "Emerald Star Body Color 5":
            case "Emerald Star Body Color 6": return fieldImageViewCreator(images.get("emeraldStar"));
            case "Gold Star Body Color 1":
            case "Gold Star Body Color 2":
            case "Gold Star Body Color 3":
            case "Gold Star Body Color 4":
            case "Gold Star Body Color 5":
            case "Gold Star Body Color 6": return fieldImageViewCreator(images.get("goldStar"));
            case "Ruby Star Body Color 1":
            case "Ruby Star Body Color 2":
            case "Ruby Star Body Color 3":
            case "Ruby Star Body Color 4":
            case "Ruby Star Body Color 5":
            case "Ruby Star Body Color 6": return fieldImageViewCreator(images.get("rubyStar"));
            case "Sapphire Star Body Color 1":
            case "Sapphire Star Body Color 2":
            case "Sapphire Star Body Color 3":
            case "Sapphire Star Body Color 4":
            case "Sapphire Star Body Color 5":
            case "Sapphire Star Body Color 6": return fieldImageViewCreator(images.get("sapphireStar"));
            case "Garnet Star Body Color 1":
            case "Garnet Star Body Color 2":
            case "Garnet Star Body Color 3":
            case "Garnet Star Body Color 4":
            case "Garnet Star Body Color 5":
            case "Garnet Star Body Color 6": return fieldImageViewCreator(images.get("garnetStar"));
            case "Crystal Star Body Color 1":
            case "Crystal Star Body Color 2":
            case "Crystal Star Body Color 3":
            case "Crystal Star Body Color 4":
            case "Crystal Star Body Color 5":
            case "Crystal Star Overlay Color 1":
            case "Crystal Star Overlay Color 2":
            case "Crystal Star Overlay Color 3":
            case "Crystal Star Overlay Color 4":
            case "Crystal Star Overlay Color 5":
            case "Crystal Star Overlay Color 6":
            case "Crystal Star Overlay Color 7":
            case "Crystal Star Overlay Color 8":
            case "Crystal Star Overlay Color 9":
            case "Crystal Star Overlay Color 10":
            case "Crystal Star Overlay Color 11":
            case "Crystal Star Overlay Color 12":
            case "Crystal Star Overlay Color 13":
            case "Crystal Star Overlay Color 14":
            case "Crystal Star Overlay Color 15": return fieldImageViewCreator(images.get("crystalStar"));
            
            default: return fieldImageViewCreator(images.get("colorWheelCustom"));
        }
    }

    /**
     * @Author Jemaroo
     * @Function Returns an image based on misc field name
     */
    public ImageView determineMiscIcon(Misc.odds misc)
    {
        switch(misc.name)
        {
            case "Mover Appear Odds": return fieldImageViewCreator(images.get("moverCustom"));
            
            default: return fieldImageViewCreator(images.get("routingSlip"));
        }
    }

    /**
     * @Author Jemaroo
     * @Function Returns an image based on misc field name
     */
    public ImageView determineMiscIcon(Misc.oddsRev misc)
    {
        switch(misc.name)
        {
            case "Charlieton Appear Odds": return fieldImageViewCreator(images.get("charlietonCustom"));
            
            default: return fieldImageViewCreator(images.get("routingSlip"));
        }
    }

    /**
     * @Author Jemaroo
     * @Function Returns an image based on misc field name
     */
    public ImageView determineMiscIcon(Misc.Function misc)
    {
        switch(misc.name)
        {
            case "Fog Always Disabled": return fieldImageViewCreator(images.get("gateHandle"));
            case "Remove Run from Confuse": return fieldImageViewCreator(images.get("confuseStatus"));
            case "Prevent Switching Position in Battle":
            case "Prevent Switching Partners in Battle": return fieldImageViewCreator(images.get("goombellaPartnerSwitch"));
            case "Tattle No Longer Takes Turn":
            case "Tattle No Longer Gives SP": return fieldImageViewCreator(images.get("goombellaPartnerSwitch"));
            case "Mowz Smooch Heal Amount Fix": return fieldImageViewCreator(images.get("mowzPartnerSwitch"));
            case "No Game Overs From Field Hazards":
            case "Take No Damage From Field Hazards": return fieldImageViewCreator(images.get("fishCustom"));
            case "Never Lose Coins When Running From Battle": return fieldImageViewCreator(images.get("runArrow"));
            case "Plane Mode Anywhere": return fieldImageViewCreator(images.get("planeCurse"));
            case "Make Hooktail immune to Attack FXR Damage Drop": return fieldImageViewCreator(images.get("unitHooktail"));
            case "Puni's Increased Speed and Sight": return fieldImageViewCreator(images.get("puniCustom"));
            
            default: return fieldImageViewCreator(images.get("cog"));
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
     * @Function Sets up the bingo selection box
     */
    private void setBingoSelectionBox(ComboBox<String> box)
    {
        box.setCellFactory(lv -> new ListCell<String>()
        {
            @Override protected void updateItem(String item, boolean empty)
            {
                super.updateItem(item, empty);

                if (empty || item == null)
                {
                    setText(null);
                    setGraphic(null);
                }
                else
                {
                    setText(item);
                    int index = box.getItems().indexOf(item);
                    switch(index)
                    {
                        case 0: setGraphic(fieldImageViewCreator(images.get("bingoMushroom"))); break;
                        case 1: setGraphic(fieldImageViewCreator(images.get("bingoFlower"))); break;
                        case 2: setGraphic(fieldImageViewCreator(images.get("bingoStar"))); break;
                        case 3: setGraphic(fieldImageViewCreator(images.get("bingoShineSprite"))); break;
                        case 4: setGraphic(fieldImageViewCreator(images.get("bingoPoisonMushroom"))); break;
                        default: setGraphic(fieldImageViewCreator(images.get("unknown")));
                    }
                }
            }
        });

        box.setButtonCell(new ListCell<String>()
        {
            @Override protected void updateItem(String item, boolean empty)
            {
                super.updateItem(item, empty);

                if (empty || item == null)
                {
                    setText(null);
                    setGraphic(null);
                }
                else
                {
                    setText(item);
                    int index = box.getItems().indexOf(item);
                    switch(index)
                    {
                        case 0: setGraphic(fieldImageViewCreator(images.get("bingoMushroom"))); break;
                        case 1: setGraphic(fieldImageViewCreator(images.get("bingoFlower"))); break;
                        case 2: setGraphic(fieldImageViewCreator(images.get("bingoStar"))); break;
                        case 3: setGraphic(fieldImageViewCreator(images.get("bingoShineSprite"))); break;
                        case 4: setGraphic(fieldImageViewCreator(images.get("bingoPoisonMushroom"))); break;
                        default: setGraphic(fieldImageViewCreator(images.get("unknown")));
                    }
                }
            }
        });

        box.setEditable(false);
    }
    
    public static void main(String[] args) 
    {
        launch(args);
    }
}