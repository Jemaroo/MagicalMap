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
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import javafx.scene.control.skin.ComboBoxListViewSkin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;

public class IDTGUI extends Application 
{
    private ArrayList<String> validFileNames = new ArrayList<String>();
    boolean isFileOpened = false;
    Stage window;
    File givenFile;
    private IDTData fileData = new IDTData();
    private ListView<ItemData> itemList = null;
    private ListView<ShopData> shopList = null;
    private ListView<DropData> dropList = null;
    private ListView<FieldLocationData> fieldList = null;
    TextField searchField = new TextField();
    Button saveChangesButton = new Button("Save Struct Changes");
    HBox topMenu = new HBox();
    HBox centerMenu = new HBox();
    Button openButton = new Button("Open Directory");
    ComboBox<File> fileSelector = new ComboBox<>();
    Button exportButton = new Button("Export File");
    Button closeButton = new Button("Close File");
    Button optionButton = new Button("Options");
    Button aboutButton = new Button("About");
    HBox upperBox = new HBox();
    BorderPane borderPane = new BorderPane();
    GridPane leftMenuForm = new GridPane();
    ArrayList<ComboBox<String>> dropFields = null;
    ArrayList<TextField> holdWeightFields = null;
    ArrayList<TextField> dropWeightFields = null;
    ArrayList<ComboBox<String>> shopFields = null;
    ArrayList<TextField> propertyFields = null;
    ArrayList<TextField> throwWeightFields = null;
    ArrayList<TextField> sellPriceFields = null;
    ArrayList<TextField> pointRequirementFields = null;
    ArrayList<Object> fieldFields = null;

    HashMap<String, Image> images = new HashMap<String, Image>();
    Map<String, Integer> filePriority = new HashMap<String, Integer>();

    private CheckBox UseLocationShopBox = new CheckBox();
    private CheckBox UseLocationBattleBox = new CheckBox();
    private CheckBox UseLocationFieldBox = new CheckBox();
    private TextField sortOrderField = new TextField();
    private TextField buyPriceField = new TextField();
    private TextField discountPriceField = new TextField();
    private TextField starPiecePriceField = new TextField();
    private TextField piantaPriceField = new TextField();
    private TextField sellPriceField = new TextField();
    private TextField BPCostField = new TextField();
    private TextField HPRestoredField = new TextField();
    private TextField FPRestoredField = new TextField();
    private TextField SPRestoredField = new TextField();
    private TextField xCoordField = new TextField();
    private TextField yCoordField = new TextField();
    private TextField zCoordField = new TextField();
    private TextField coinCostField = new TextField();

    private static final String RED_STYLE = "-fx-text-fill: red; -fx-font-weight: bold;";
    private static final String BLACK_STYLE = "-fx-text-fill: black; -fx-font-weight: normal;";
    ChangeListener<String> redTextListener1 = (obs, oldText, newText) -> { if ("0".equals(newText)) {sortOrderField.setStyle(RED_STYLE);} else {sortOrderField.setStyle(BLACK_STYLE);}};
    ChangeListener<String> redTextListener2 = (obs, oldText, newText) -> { if ("0".equals(newText)) {buyPriceField.setStyle(RED_STYLE);} else {buyPriceField.setStyle(BLACK_STYLE);}};
    ChangeListener<String> redTextListener3 = (obs, oldText, newText) -> { if ("0".equals(newText)) {discountPriceField.setStyle(RED_STYLE);} else {discountPriceField.setStyle(BLACK_STYLE);}};
    ChangeListener<String> redTextListener4 = (obs, oldText, newText) -> { if ("0".equals(newText)) {starPiecePriceField.setStyle(RED_STYLE);} else {starPiecePriceField.setStyle(BLACK_STYLE);}};
    ChangeListener<String> redTextListener5 = (obs, oldText, newText) -> { if ("0".equals(newText)) {sellPriceField.setStyle(RED_STYLE);} else {sellPriceField.setStyle(BLACK_STYLE);}};
    ChangeListener<String> redTextListener6 = (obs, oldText, newText) -> { if ("0".equals(newText)) {BPCostField.setStyle(RED_STYLE);} else {BPCostField.setStyle(BLACK_STYLE);}};
    ChangeListener<String> redTextListener7 = (obs, oldText, newText) -> { if ("0".equals(newText)) {HPRestoredField.setStyle(RED_STYLE);} else {HPRestoredField.setStyle(BLACK_STYLE);}};
    ChangeListener<String> redTextListener8 = (obs, oldText, newText) -> { if ("0".equals(newText)) {FPRestoredField.setStyle(RED_STYLE);} else {FPRestoredField.setStyle(BLACK_STYLE);}};
    ChangeListener<String> redTextListener9 = (obs, oldText, newText) -> { if ("0".equals(newText)) {SPRestoredField.setStyle(RED_STYLE);} else {SPRestoredField.setStyle(BLACK_STYLE);}};
    ChangeListener<String> redTextListener10 = (obs, oldText, newText) -> { if ("0".equals(newText)) {xCoordField.setStyle(RED_STYLE);} else {xCoordField.setStyle(BLACK_STYLE);}};
    ChangeListener<String> redTextListener11 = (obs, oldText, newText) -> { if ("0".equals(newText)) {yCoordField.setStyle(RED_STYLE);} else {yCoordField.setStyle(BLACK_STYLE);}};
    ChangeListener<String> redTextListener12 = (obs, oldText, newText) -> { if ("0".equals(newText)) {zCoordField.setStyle(RED_STYLE);} else {zCoordField.setStyle(BLACK_STYLE);}};
    ChangeListener<String> redTextListener13 = (obs, oldText, newText) -> { if ("0".equals(newText)) {coinCostField.setStyle(RED_STYLE);} else {coinCostField.setStyle(BLACK_STYLE);}};

    @Override
    public void start(Stage primaryStage) 
    {
        try
        {
            File jsonFile = new File("src\\ItemData.json");
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
        window.setTitle("Item Data Tool");

        //Menu Buttons
        topMenu.getChildren().addAll(openButton, optionButton, aboutButton);
        topMenu.setPadding(new Insets(5));
        topMenu.setSpacing(5);

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

            if(((String)root.get("IDTLastFolder")).equals("true")) startPath = (String)root.get("startPath");
            if(((String)root.get("IDTRedFields")).equals("true")) setRed0TextFieldFormats(true);
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

                    byte[] newFileData = IDTMain.buildNewFile(fileSelector.getSelectionModel().getSelectedItem(), fileData);
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
                            successBox.getIcons().add(images.get("itemsIcon"));

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
                optionsBox.getIcons().add(images.get("itemsIcon"));

                CheckBox lastFolderBox = new CheckBox();
                CheckBox redFieldBox = new CheckBox();
                CheckBox mysteryBox = new CheckBox();
                Button saveOptionsButton = new Button("Save Options");

                try
                {
                    File jsonFile = new File("src\\options.json");
                    JSONParser parser = new JSONParser();
                    JSONObject root = (JSONObject)parser.parse(new FileReader(jsonFile));

                    if(((String)root.get("IDTLastFolder")).equals("true")) lastFolderBox.setSelected(true); else lastFolderBox.setSelected(false);
                    if(((String)root.get("IDTRedFields")).equals("true")) redFieldBox.setSelected(true); else redFieldBox.setSelected(false);
                    if(((String)root.get("IDTMystery")).equals("true")) mysteryBox.setSelected(true); else mysteryBox.setSelected(false);
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
                // optionsForm.add(unitImageViewCreator(images.get("cog")), 0, 2);
                // optionsForm.add(new Label("Patch Mystery Fix (Unstable)"), 1, 2);
                // optionsForm.add(mysteryBox, 2, 2);

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

                            if(lastFolderBox.isSelected()) root.put("IDTLastFolder", "true");
                            else root.put("IDTLastFolder", "false");

                            if(redFieldBox.isSelected()) root.put("IDTRedFields", "true");
                            else root.put("IDTRedFields", "false");

                            if(mysteryBox.isSelected()) root.put("IDTMystery", "true");
                            else root.put("IDTMystery", "false");

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
                alertBox.getIcons().add(images.get("itemsIcon"));

                VBox alertMenu = new VBox();
                alertMenu.setAlignment(Pos.CENTER);
                Text versionText = new Text("Magical Map Version: " + GUI.version);
                versionText.setWrappingWidth(290);
                versionText.setTextAlignment(TextAlignment.CENTER);
                Text creditText = new Text("Item Data Tool Written by Jemaroo");
                creditText.setWrappingWidth(290);
                creditText.setTextAlignment(TextAlignment.CENTER);
                Text description = new Text("Item Data Tool allows you to open up the game's main dol or any rel file containing item data and edit item data fields, shop item tables, field objects, and more.");
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

        window.getIcons().add(images.get("itemsIcon"));
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
            centerMenu.getChildren().clear();

            if(fileSelector.getSelectionModel().getSelectedItem().getName().equals("main.dol") || fileSelector.getSelectionModel().getSelectedItem().getName().equals("Start.dol"))
            {
                fileData = IDTMain.getTableData(fileSelector.getSelectionModel().getSelectedItem());
                isFileOpened = true;
                test.testUnitData(fileData);

                for(int i = 0; i < fileData.items.size(); i++)
                {
                    fileData.items.get(i).icon = listImageSelector(i);
                }

                fieldList = new ListView<>();

                //Setting struct names for ItemData
                itemList = new ListView<>();
                itemList.setCellFactory(lv -> new ListCell<ItemData>() 
                {
                    @Override protected void updateItem(ItemData item, boolean empty) 
                    {
                        super.updateItem(item, empty);
                        if (empty || item == null) 
                        {
                            setText(null);
                            setGraphic(null);
                        } 
                        else 
                        {
                            HBox imageBox = new HBox();
                            imageBox.getChildren().add(fieldImageViewCreator(item.icon));
                            setText(item.name);
                            setGraphic(imageBox);
                        }
                    }
                });

                for(int i = 0; i < fileData.drops.size(); i++)
                {
                    fileData.drops.get(i).icon = determineEnemyUnitIcon(i);
                }

                //Setting struct names for DropData
                dropList = new ListView<>();
                dropList.setCellFactory(lv -> new ListCell<DropData>() 
                {
                    @Override protected void updateItem(DropData drop, boolean empty) 
                    {
                        super.updateItem(drop, empty);
                        if (empty || drop == null) 
                        {
                            setText(null);
                            setGraphic(null);
                        } 
                        else 
                        {
                            HBox imageBox = new HBox();
                            imageBox.getChildren().add(drop.icon);
                            setText(drop.name);
                            setGraphic(imageBox);
                        }
                    }
                });

                for(int i = 0; i < fileData.shops.size(); i++)
                {
                    fileData.shops.get(i).icon = determineShopIcon(fileData.shops.get(i));
                }

                //Setting struct names for ShopData
                shopList = new ListView<>();
                shopList.setCellFactory(lv -> new ListCell<ShopData>() 
                {
                    @Override protected void updateItem(ShopData shop, boolean empty) 
                    {
                        super.updateItem(shop, empty);
                        if (empty || shop == null) 
                        {
                            setText(null);
                            setGraphic(null);
                        } 
                        else 
                        {
                            HBox imageBox = new HBox();
                            imageBox.getChildren().add(shop.icon);
                            setText(shop.name);
                            setGraphic(imageBox);
                        }
                    }
                });

                leftMenuForm = new GridPane();
                upperBox.setPadding(new Insets(3));
                searchField.setPromptText("Search...");
                upperBox.getChildren().clear();
                upperBox.getChildren().add(searchField);
                leftMenuForm.add(upperBox, 0, 0);

                HBox baseBox = new HBox();
                baseBox.setPadding(new Insets(3));
                baseBox.getChildren().addAll(new Label("Items:", fieldImageViewCreator(images.get("itemsIcon"))));
                leftMenuForm.add(baseBox, 0, 1);
                leftMenuForm.add(itemList, 0, 2);

                HBox dropBox = new HBox();
                dropBox.setPadding(new Insets(3));
                dropBox.getChildren().addAll(new Label("Item Drop Tables:", fieldImageViewCreator(images.get("unknown"))));
                leftMenuForm.add(dropBox, 0, 3);
                leftMenuForm.add(dropList, 0, 4);

                HBox shopBox = new HBox();
                shopBox.setPadding(new Insets(3));
                shopBox.getChildren().addAll(new Label("Item Tables:", fieldImageViewCreator(images.get("strangeSack"))));
                leftMenuForm.add(shopBox, 0, 5);
                leftMenuForm.add(shopList, 0, 6);
                
                borderPane.setLeft(leftMenuForm);

                GridPane form = new GridPane();
                GridPane form2 = new GridPane();
                TabPane tabPane = new TabPane();

                ObservableList<ItemData> observableItems = FXCollections.observableArrayList(fileData.items);
                FilteredList<ItemData> filteredItems = new FilteredList<ItemData>(observableItems, p -> true);
                itemList.setItems(filteredItems);

                ObservableList<DropData> observableDrops = FXCollections.observableArrayList(fileData.drops);
                FilteredList<DropData> filteredDrops = new FilteredList<DropData>(observableDrops, p -> true);
                dropList.setItems(filteredDrops);

                ObservableList<ShopData> observableShops = FXCollections.observableArrayList(fileData.shops);
                FilteredList<ShopData> filteredShops = new FilteredList<ShopData>(observableShops, p -> true);
                shopList.setItems(filteredShops);

                searchField.textProperty().addListener((obs, oldValue, newValue) -> 
                {
                    String filter = newValue.toLowerCase();
                    filteredItems.setPredicate(item -> 
                    {
                        if (filter == null || filter.isEmpty()) 
                        {
                            return true;
                        }
                        return item.name.toLowerCase().contains(filter);
                    });
                    filteredDrops.setPredicate(drop -> 
                    {
                        if (filter == null || filter.isEmpty()) 
                        {
                            return true;
                        }
                        return drop.name.toLowerCase().contains(filter);
                    });
                    filteredShops.setPredicate(shop -> 
                    {
                        if (filter == null || filter.isEmpty()) 
                        {
                            return true;
                        }
                        return shop.name.toLowerCase().contains(filter);
                    });
                });

                saveChangesButton.setOnAction(new EventHandler<ActionEvent>() 
                {
                    @Override public void handle(ActionEvent event)
                    {
                        saveFieldsToSelectedStruct();
                        saveChangesButton.setText("    Changes Saved     ");
                    }
                });

                itemList.setOnMouseClicked(e2 -> 
                {
                    centerMenu.getChildren().clear();
                    tabPane.getTabs().clear();
                    form.getChildren().clear();
                    form2.getChildren().clear();
                    dropList.getSelectionModel().clearSelection();
                    shopList.getSelectionModel().clearSelection();

                    upperBox.getChildren().clear();
                    upperBox.getChildren().addAll(searchField, saveChangesButton);

                    ItemData selected = itemList.getSelectionModel().getSelectedItem();
                    propertyFields = new ArrayList<TextField>();

                    if (selected != null) loadStructFields(selected);

                    saveChangesButton.setText("Save Struct Changes");

                    form.setVgap(5);
                    form.setHgap(10);
                    form.setPadding(new Insets(10));

                    form.add(fieldImageViewCreator(images.get("coin")), 0, 0);
                    form.add(new Label("Buy Price:"), 1, 0);
                    form.add(buyPriceField, 2, 0);
                    form.add(fieldImageViewCreator(images.get("coin")), 0, 1);
                    form.add(new Label("Discount Price:"), 1, 1);
                    form.add(discountPriceField, 2, 1);
                    form.add(fieldImageViewCreator(images.get("coin")), 0, 2);
                    form.add(new Label("Sell Price:"), 1, 2);
                    form.add(sellPriceField, 2, 2);
                    form.add(fieldImageViewCreator(images.get("starPiece")), 0, 3);
                    form.add(new Label("Star Piece Price:"), 1, 3);
                    form.add(starPiecePriceField, 2, 3);
                    form.add(fieldImageViewCreator(images.get("pianta")), 0, 4);
                    form.add(new Label("Pianta Price:"), 1, 4);
                    form.add(piantaPriceField, 2, 4);
                    form.add(fieldImageViewCreator(images.get("BPEmblemCustom")), 0, 5);
                    form.add(new Label("BP Cost:"), 1, 5);
                    form.add(BPCostField, 2, 5);
                    form.add(fieldImageViewCreator(images.get("heart")), 0, 6);
                    form.add(new Label("HP Restored:"), 1, 6);
                    form.add(HPRestoredField, 2, 6);
                    form.add(fieldImageViewCreator(images.get("flower")), 0, 7);
                    form.add(new Label("FP Restored:"), 1, 7);
                    form.add(FPRestoredField, 2, 7);
                    form.add(fieldImageViewCreator(images.get("SPOrb1")), 0, 8);
                    form.add(new Label("SP Restored:"), 1, 8);
                    form.add(SPRestoredField, 2, 8);
                    form.add(fieldImageViewCreator(images.get("strangeSack")), 0, 9);
                    form.add(new Label("Sort Order:"), 1, 9);
                    form.add(sortOrderField, 2, 9);
                    form.add(fieldImageViewCreator(images.get("appealAction")), 0, 10);
                    form.add(new Label("Can be Used in Shops:"), 1, 10);
                    form.add(UseLocationShopBox, 2, 10);
                    form.add(fieldImageViewCreator(images.get("appealAction")), 0, 11);
                    form.add(new Label("Can be Used in Battle:"), 1, 11);
                    form.add(UseLocationBattleBox, 2, 11);
                    form.add(fieldImageViewCreator(images.get("appealAction")), 0, 12);
                    form.add(new Label("Can be Used in the Field:"), 1, 12);
                    form.add(UseLocationFieldBox, 2, 12);

                    if(selected.properties.size() > 0 && selected != null)
                    {
                        form2.setVgap(5);
                        form2.setHgap(10);
                        form2.setPadding(new Insets(10));

                        for(int i = 0; i < selected.properties.size(); i++)
                        {
                            propertyFields.add(new TextField());
                            if(selected.name.equals("Last Stand") || selected.name.equals("Last Stand P"))
                            {
                                propertyFields.get(i).setText(String.valueOf(selected.properties.get(i).propertyValue) + "/" + String.valueOf(selected.properties.get(i).propertyValue + 1));                            
                            }
                            else
                            {
                                propertyFields.get(i).setText(String.valueOf(selected.properties.get(i).propertyValue));
                            }

                            form2.add(fieldImageViewCreator(selected.icon), 0, i);
                            form2.add(new Label((selected.properties.get(i).propertyName) + ":"), 1, i);
                            form2.add(propertyFields.get(i), 2, i);

                            if(selected.name.equals("HP Plus") || selected.name.equals("FP Plus") || selected.name.equals("HP Plus P")){break;}
                            else if(selected.name.equals("Heart Finder") && i > 0 || selected.name.equals("Flower Finder") && i > 0){break;}
                        }

                        Tab tab1 = new Tab("ItemData Struct", form);
                        Tab tab2 = new Tab("Badge Properties", form2);

                        tab1.setClosable(false);
                        tab2.setClosable(false);
                        tabPane.getTabs().addAll(tab1, tab2);
                        centerMenu.getChildren().add(tabPane);
                    }
                    else
                    {
                        centerMenu.getChildren().add(form);
                    }

                    ScrollPane centerScroll = new ScrollPane();
                    centerScroll.setContent(centerMenu);
                    borderPane.setCenter(centerScroll);
                });

                dropList.setOnMouseClicked(e2 -> 
                {
                    centerMenu.getChildren().clear();
                    form.getChildren().clear();
                    itemList.getSelectionModel().clearSelection();
                    shopList.getSelectionModel().clearSelection();

                    upperBox.getChildren().clear();
                    upperBox.getChildren().addAll(searchField, saveChangesButton);

                    DropData selected = dropList.getSelectionModel().getSelectedItem();
                    dropFields = new ArrayList<ComboBox<String>>();
                    holdWeightFields = new ArrayList<TextField>();
                    dropWeightFields = new ArrayList<TextField>();
                    shopFields = new ArrayList<ComboBox<String>>();
                    throwWeightFields = new ArrayList<TextField>();
                    pointRequirementFields = new ArrayList<TextField>();
                    sellPriceFields = new ArrayList<TextField>();

                    if (selected != null)
                    { 
                        dropFields = loadGrowingSelectionFields(selected);
                        if(selected.holdWeights.size() > 0) holdWeightFields = loadHoldWeightGrowingTextFields(selected);
                        if(selected.dropWeights.size() > 0) dropWeightFields = loadDropWeightGrowingTextFields(selected);
                    }

                    saveChangesButton.setText("Save Struct Changes");

                    form.setVgap(5);
                    form.setHgap(10);
                    form.setPadding(new Insets(10));

                    int dropChecker = 0;

                    for(int i = 0; dropChecker < (dropFields.size() * 3); i++)
                    {
                        form.add(fieldImageViewCreator(images.get("strangeSack")), 0, dropChecker);
                        form.add(new Label("Slot " + (i + 1) + " Item: "), 1, dropChecker);

                        ComboBox<String> box = dropFields.get(i);

                        setItemSelectionComboBox(box);

                        form.add(box, 2, dropChecker);
                        dropChecker++;

                        form.add(fieldImageViewCreator(images.get("unknown")), 0, dropChecker);
                        form.add(new Label("Slot " + (i + 1) + " Hold Weight: "), 1, dropChecker);
                        form.add(holdWeightFields.get(i), 2, dropChecker);
                        dropChecker++;

                        form.add(fieldImageViewCreator(images.get("unknown")), 0, dropChecker);
                        form.add(new Label("Slot " + (i + 1) + " Drop Weight: "), 1, dropChecker);
                        form.add(dropWeightFields.get(i), 2, dropChecker);
                        dropChecker++;
                    }

                    centerMenu.getChildren().add(form);
                    ScrollPane centerScroll = new ScrollPane();
                    centerScroll.setContent(centerMenu);
                    borderPane.setCenter(centerScroll);
                });

                shopList.setOnMouseClicked(e2 -> 
                {
                    centerMenu.getChildren().clear();
                    form.getChildren().clear();
                    dropList.getSelectionModel().clearSelection();
                    itemList.getSelectionModel().clearSelection();

                    upperBox.getChildren().clear();
                    upperBox.getChildren().addAll(searchField, saveChangesButton);

                    ShopData selected = shopList.getSelectionModel().getSelectedItem();
                    shopFields = new ArrayList<ComboBox<String>>();
                    throwWeightFields = new ArrayList<TextField>();
                    pointRequirementFields = new ArrayList<TextField>();
                    sellPriceFields = new ArrayList<TextField>();

                    dropFields = new ArrayList<ComboBox<String>>();
                    holdWeightFields = new ArrayList<TextField>();
                    dropWeightFields = new ArrayList<TextField>();

                    if (selected != null)
                    { 
                        shopFields = loadGrowingSelectionFields(selected);
                        if(selected.throwWeights.size() > 0) throwWeightFields = loadThrowWeightGrowingTextFields(selected);
                        if(selected.pointRequirements.size() > 0) pointRequirementFields = loadPointRequirementGrowingTextFields(selected);
                    }

                    saveChangesButton.setText("Save Struct Changes");

                    form.setVgap(5);
                    form.setHgap(10);
                    form.setPadding(new Insets(10));

                    if(selected.type.equals("Shop"))
                    {
                        for(int i = 0; i < shopFields.size(); i++)
                        {
                            form.add(fieldImageViewCreator(images.get("strangeSack")), 0, i);
                            form.add(new Label("Slot " + (i + 1) + " Item: "), 1, i);

                            ComboBox<String> box = shopFields.get(i);

                            setItemSelectionComboBox(box);

                            form.add(box, 2, i);
                        }
                    }
                    else if(selected.type.equals("Audience"))
                    {
                        int shopChecker = 0;

                        for(int i = 0; shopChecker < (shopFields.size() * 2); i++)
                        {
                            form.add(fieldImageViewCreator(images.get("strangeSack")), 0, shopChecker);
                            form.add(new Label("Slot " + (i + 1) + " Item: "), 1, shopChecker);

                            ComboBox<String> box = shopFields.get(i);

                            setItemSelectionComboBox(box);

                            form.add(box, 2, shopChecker);
                            shopChecker++;

                            form.add(fieldImageViewCreator(images.get("unknown")), 0, shopChecker);
                            form.add(new Label("Slot " + (i + 1) + " Throw Weight: "), 1, shopChecker);
                            form.add(throwWeightFields.get(i), 2, shopChecker);
                            shopChecker++;
                        }
                    }
                    else if(selected.type.equals("Point Rewards"))
                    {
                        int shopChecker = 0;

                        for(int i = 0; shopChecker < (shopFields.size() * 2); i++)
                        {
                            form.add(fieldImageViewCreator(images.get("routingSlip")), 0, shopChecker);
                            form.add(new Label("Slot " + (i + 1) + " Point Requirement: "), 1, shopChecker);
                            form.add(pointRequirementFields.get(i), 2, shopChecker);
                            shopChecker++;

                            form.add(fieldImageViewCreator(images.get("strangeSack")), 0, shopChecker);
                            form.add(new Label("Slot " + (i + 1) + " Item: "), 1, shopChecker);

                            ComboBox<String> box = shopFields.get(i);

                            setItemSelectionComboBox(box);
                    
                            form.add(box, 2, shopChecker);
                            shopChecker++;
                        }
                    }

                    centerMenu.getChildren().add(form);
                    ScrollPane centerScroll = new ScrollPane();
                    centerScroll.setContent(centerMenu);
                    borderPane.setCenter(centerScroll);
                });
            }
            else
            {
                fileData = IDTMain.getTableData(fileSelector.getSelectionModel().getSelectedItem());
                isFileOpened = true;
                test.testUnitData(fileData);

                for(int i = 0; i < fileData.shops.size(); i++)
                {
                    fileData.shops.get(i).icon = determineShopIcon(fileData.shops.get(i));
                }
                for(int i = 0; i < fileData.shops2.size(); i++)
                {
                    fileData.shops2.get(i).icon = determineShopIcon(fileData.shops2.get(i));
                }

                //Removing null for safety
                itemList = new ListView<>();
                dropList = new ListView<>();

                //Setting struct names for ShopData
                shopList = new ListView<>();
                shopList.setCellFactory(lv -> new ListCell<ShopData>() 
                {
                    @Override protected void updateItem(ShopData shop, boolean empty) 
                    {
                        super.updateItem(shop, empty);
                        if (empty || shop == null) 
                        {
                            setText(null);
                            setGraphic(null);
                        } 
                        else 
                        {
                            HBox imageBox = new HBox();
                            imageBox.getChildren().add(shop.icon);
                            setText(shop.name);
                            setGraphic(imageBox);
                        }
                    }
                });

                //Setting struct names for Field Data
                fieldList = new ListView<>();
                fieldList.setCellFactory(lv -> new ListCell<FieldLocationData>() 
                {
                    @Override protected void updateItem(FieldLocationData field, boolean empty) 
                    {
                        super.updateItem(field, empty);
                        if (empty || field == null) 
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
                                    if (field instanceof FieldLocationData.evt_item_entry && previousItem instanceof FieldLocationData.evt_item_entry && field.map.equals(((FieldLocationData)previousItem).map)) 
                                    {
                                        count++;
                                    } 
                                    else if (field instanceof FieldLocationData.evt_mobj_badgeblk && previousItem instanceof FieldLocationData.evt_mobj_badgeblk && field.map.equals(((FieldLocationData)previousItem).map)) 
                                    {
                                        count++;
                                    }
                                    else if (field instanceof FieldLocationData.evt_mobj_blk && previousItem instanceof FieldLocationData.evt_mobj_blk && field.map.equals(((FieldLocationData)previousItem).map)) 
                                    {
                                        count++;
                                    }
                                    else if (field instanceof FieldLocationData.evt_mobj_brick && previousItem instanceof FieldLocationData.evt_mobj_brick && field.map.equals(((FieldLocationData)previousItem).map)) 
                                    {
                                        count++;
                                    }
                                    else if (field instanceof FieldLocationData.evt_mobj_itembox && previousItem instanceof FieldLocationData.evt_mobj_itembox && field.map.equals(((FieldLocationData)previousItem).map)) 
                                    {
                                        count++;
                                    }
                                    else if (field instanceof FieldLocationData.evt_mobj_kururing_floor && previousItem instanceof FieldLocationData.evt_mobj_kururing_floor && field.map.equals(((FieldLocationData)previousItem).map)) 
                                    {
                                        count++;
                                    }
                                    else if (field instanceof FieldLocationData.evt_mobj_powerupblk && previousItem instanceof FieldLocationData.evt_mobj_powerupblk && field.map.equals(((FieldLocationData)previousItem).map)) 
                                    {
                                        count++;
                                    }
                                    else if (field instanceof FieldLocationData.evt_mobj_recovery_blk && previousItem instanceof FieldLocationData.evt_mobj_recovery_blk && field.map.equals(((FieldLocationData)previousItem).map)) 
                                    {
                                        count++;
                                    }
                                    else if (field instanceof FieldLocationData.evt_mobj_save_blk && previousItem instanceof FieldLocationData.evt_mobj_save_blk && field.map.equals(((FieldLocationData)previousItem).map)) 
                                    {
                                        count++;
                                    }
                                }
                            }

                            if(field instanceof FieldLocationData.evt_item_entry)
                            {
                                switch(((FieldLocationData.evt_item_entry)field).type)
                                {
                                    case "show": setText(field.map + " Field Item " + (count + 1) + " (Display)"); break;
                                    case "chest": setText(field.map + " Field Item " + (count + 1) + " (Chest)"); break;
                                    case "bush": setText(field.map + " Field Item " + (count + 1) + " (Bush)"); break;
                                    case "tree": setText(field.map + " Field Item " + (count + 1) + " (Tree)"); break;
                                    case "gift": setText(field.map + " Field Item " + (count + 1) + " (Gift)"); break;
                                    case "star": setText(field.map + " Field Item " + (count + 1) + " (Star)"); break;
                                    case "drawer": setText(field.map + " Field Item " + (count + 1) + " (Drawer)"); break;
                                    default: setText(field.map + " Field Item " + (count + 1)); break;
                                }
                                //setGraphic(fieldImageViewCreator(images.get("itemsIcon")));
                                setGraphic(fieldImageViewCreator(listImageSelector((int)((FieldLocationData.evt_item_entry)field).itemID)));
                            }
                            else if(field instanceof FieldLocationData.evt_mobj_badgeblk)
                            {
                                setText(field.map + " Item Block " + (count + 1));
                                setGraphic(fieldImageViewCreator(images.get("itemBlock")));
                            }
                            else if(field instanceof FieldLocationData.evt_mobj_blk)
                            {
                                setText(field.map + " Used Block " + (count + 1));
                                setGraphic(fieldImageViewCreator(images.get("usedBlock")));
                            }
                            else if(field instanceof FieldLocationData.evt_mobj_brick)
                            {
                                setText(field.map + " Brick Block " + (count + 1));
                                setGraphic(fieldImageViewCreator(images.get("brickBlock")));
                            }
                            else if(field instanceof FieldLocationData.evt_mobj_itembox)
                            {
                                setText(field.map + " Chest " + (count + 1));
                                setGraphic(fieldImageViewCreator(images.get("chest"))); 
                            }
                            else if(field instanceof FieldLocationData.evt_mobj_kururing_floor)
                            {
                                setText(field.map + " Flip Panel " + (count + 1));
                                setGraphic(fieldImageViewCreator(images.get("flipPanel")));
                            }
                            else if(field instanceof FieldLocationData.evt_mobj_powerupblk)
                            {
                                setText(field.map + " Shine Block " + (count + 1));
                                setGraphic(fieldImageViewCreator(images.get("shineBlock")));
                            }
                            else if(field instanceof FieldLocationData.evt_mobj_recovery_blk)
                            {
                                setText(field.map + " Recovery Block " + (count + 1));
                                setGraphic(fieldImageViewCreator(images.get("recoveryBlock")));
                            }
                            else if(field instanceof FieldLocationData.evt_mobj_save_blk)
                            {
                                setText(field.map + " Save Block " + (count + 1));
                                setGraphic(fieldImageViewCreator(images.get("saveBlock")));
                            }
                        }
                    }
                });

                leftMenuForm = new GridPane();
                upperBox.setPadding(new Insets(3));
                searchField.setPromptText("Search...");
                upperBox.getChildren().clear();
                upperBox.getChildren().add(searchField);
                leftMenuForm.add(upperBox, 0, 0);

                HBox shopBox = new HBox();
                shopBox.setPadding(new Insets(3));
                shopBox.getChildren().addAll(new Label("Item Tables:", fieldImageViewCreator(images.get("strangeSack"))));
                leftMenuForm.add(shopBox, 0, 1);
                leftMenuForm.add(shopList, 0, 2);

                HBox fieldBox = new HBox();
                fieldBox.setPadding(new Insets(3));
                fieldBox.getChildren().addAll(new Label("Field Objects:", fieldImageViewCreator(images.get("magicalMap1"))));
                leftMenuForm.add(fieldBox, 0, 3);
                leftMenuForm.add(fieldList, 0, 4);

                RowConstraints growingRow = new RowConstraints();
                growingRow.setVgrow(Priority.ALWAYS);
                leftMenuForm.getRowConstraints().addAll(new RowConstraints(), new RowConstraints(), growingRow);
                
                borderPane.setLeft(leftMenuForm);

                GridPane form = new GridPane();

                ObservableList<ShopData> observableShops = FXCollections.observableArrayList(fileData.shops);
                observableShops.addAll(FXCollections.observableArrayList(fileData.shops2));
                ObservableList<FieldLocationData> observableFields = FXCollections.observableArrayList(fileData.field);

                FilteredList<ShopData> filteredShops = new FilteredList<ShopData>(observableShops, p -> true);
                shopList.setItems(filteredShops);
                FilteredList<FieldLocationData> filteredFields = new FilteredList<FieldLocationData>(observableFields, p -> true);
                fieldList.setItems(filteredFields);

                searchField.textProperty().addListener((obs, oldValue, newValue) -> 
                {
                    String filter = newValue.toLowerCase();
                    filteredShops.setPredicate(shop -> 
                    {
                        if (filter == null || filter.isEmpty()) 
                        {
                            return true;
                        }
                        return shop.name.toLowerCase().contains(filter);
                    });
                    filteredFields.setPredicate(field ->
                    {
                        if (filter == null || filter.isEmpty())
                        {
                            return true;
                        }

                        String name;
                        if (field instanceof FieldLocationData.evt_item_entry)
                        {
                            name = field.map + " Field Item";
                        }
                        else if (field instanceof FieldLocationData.evt_mobj_badgeblk)
                        {
                            name = field.map + " Item Block";
                        }
                        else if (field instanceof FieldLocationData.evt_mobj_blk)
                        {
                            name = field.map + " Used Block";
                        }
                        else if (field instanceof FieldLocationData.evt_mobj_brick)
                        {
                            name = field.map + " Brick Block";
                        }
                        else if (field instanceof FieldLocationData.evt_mobj_itembox)
                        {
                            name = field.map + " Chest";
                        }
                        else if (field instanceof FieldLocationData.evt_mobj_kururing_floor)
                        {
                            name = field.map + " Flip Panel";
                        }
                        else if (field instanceof FieldLocationData.evt_mobj_powerupblk)
                        {
                            name = field.map + " Shine Block";
                        }
                        else if (field instanceof FieldLocationData.evt_mobj_recovery_blk)
                        {
                            name = field.map + " Recovery Block";
                        }
                        else if (field instanceof FieldLocationData.evt_mobj_save_blk)
                        {
                            name = field.map + " Save Block";
                        }
                        else
                        {
                            name = field.map;
                        }

                        return name.toLowerCase().contains(filter);
                    });
                });

                saveChangesButton.setOnAction(new EventHandler<ActionEvent>() 
                {
                    @Override public void handle(ActionEvent event)
                    {
                        saveFieldsToSelectedStruct();
                        saveChangesButton.setText("    Changes Saved     ");
                    }
                });

                shopList.setOnMouseClicked(e2 -> 
                {
                    centerMenu.getChildren().clear();
                    form.getChildren().clear();

                    fieldList.getSelectionModel().clearSelection();

                    upperBox.getChildren().clear();
                    upperBox.getChildren().addAll(searchField, saveChangesButton);

                    ShopData selected = shopList.getSelectionModel().getSelectedItem();
                    shopFields = new ArrayList<ComboBox<String>>();
                    throwWeightFields = new ArrayList<TextField>();
                    pointRequirementFields = new ArrayList<TextField>();
                    sellPriceFields = new ArrayList<TextField>();

                    if (selected != null)
                    { 
                        shopFields = loadGrowingSelectionFields(selected);
                        if(selected.sellPrices.size() != 0)
                        {
                            sellPriceFields = loadSellPriceGrowingTextFields(selected);
                        }
                    }

                    saveChangesButton.setText("Save Struct Changes");

                    form.setVgap(5);
                    form.setHgap(10);
                    form.setPadding(new Insets(10));

                    int shopChecker = 0;

                    for(int i = 0; shopChecker < (shopFields.size() * 2); i++)
                    {
                        if(selected.type.equals("Rewards"))
                        {
                            form.add(fieldImageViewCreator(images.get("strangeSack")), 0, shopChecker);
                            form.add(new Label("Floor " + ((i + 1) * 10) + " Item: "), 1, shopChecker);

                            ComboBox<String> box = shopFields.get(i);

                            setItemSelectionComboBox(box);

                            form.add(box, 2, shopChecker);
                            shopChecker += 2;
                        }
                        else if(selected.type.equals("Raw"))
                        {
                            if(selected.name.equals("Happy Lucky Lottery Table"))
                            {
                                form.add(fieldImageViewCreator(images.get("lotteryPick")), 0, shopChecker);
                                switch(i)
                                {
                                    case 0: form.add(new Label("Consolation prize: "), 1, shopChecker); break;
                                    case 1: form.add(new Label("1 Matching Number: "), 1, shopChecker); break;
                                    case 2: form.add(new Label("2 Matching Numbers: "), 1, shopChecker); break;
                                    case 3: form.add(new Label("3 Matching Numbers: "), 1, shopChecker); break;
                                    case 4: form.add(new Label("3 Matching Numbers: "), 1, shopChecker); break;
                                    case 5: form.add(new Label("4 Matching Numbers: "), 1, shopChecker); break;
                                    case 6: form.add(new Label("4 Matching Numbers: "), 1, shopChecker); break;
                                    default: form.add(new Label("Item " + i + ": "), 1, shopChecker); break;
                                }
                            }
                            else if(selected.name.equals("Boo Quiz Reward Table"))
                            {
                                form.add(fieldImageViewCreator(images.get("strangeSack")), 0, shopChecker);
                                switch(i)
                                {
                                    case 0: form.add(new Label("Answer 1: "), 1, shopChecker); break;
                                    case 1: form.add(new Label("Answer 2: "), 1, shopChecker); break;
                                    case 2: form.add(new Label("Answer 3: "), 1, shopChecker); break;
                                    default: form.add(new Label("Item " + i + ": "), 1, shopChecker); break;
                                }
                            }
                            else
                            {
                                form.add(fieldImageViewCreator(images.get("unknown")), 0, shopChecker);
                                form.add(new Label("Item " + i + ": "), 1, shopChecker);
                            }

                            ComboBox<String> box = shopFields.get(i);

                            setItemSelectionComboBox(box);

                            form.add(box, 2, shopChecker);
                            shopChecker += 2;
                        }
                        else if(selected.type.equals("Inn"))
                        {
                            form.add(fieldImageViewCreator(images.get("strangeSack")), 0, 0);
                            form.add(new Label("Breakfast Item: "), 1, 0);

                            ComboBox<String> box = shopFields.get(i);

                            setItemSelectionComboBox(box);

                            form.add(box, 2, 0);

                            xCoordField.setText(String.valueOf(selected.xCoord));
                            form.add(fieldImageViewCreator(images.get("magicalMap1")), 0, 1);
                            form.add(new Label("X Coordinate: "), 1, 1);
                            form.add(xCoordField, 2, 1);

                            yCoordField.setText(String.valueOf(selected.yCoord));
                            form.add(fieldImageViewCreator(images.get("magicalMap1")), 0, 2);
                            form.add(new Label("Y Coordinate: "), 1, 2);
                            form.add(yCoordField, 2, 2);

                            zCoordField.setText(String.valueOf(selected.zCoord));
                            form.add(fieldImageViewCreator(images.get("magicalMap1")), 0, 3);
                            form.add(new Label("Z Coordinate: "), 1, 3);
                            form.add(zCoordField, 2, 3);

                            coinCostField.setText(String.valueOf(selected.coinCost));
                            form.add(fieldImageViewCreator(images.get("coin")), 0, 4);
                            form.add(new Label("Coin Cost: "), 1, 4);
                            form.add(coinCostField, 2, 4);

                            shopChecker += 2;
                        }
                        else if(selected.type.equals("Coins"))
                        {
                            form.add(fieldImageViewCreator(images.get("strangeSack")), 0, 0);
                            form.add(new Label("Item: "), 1, 0);

                            ComboBox<String> box = shopFields.get(i);

                            setItemSelectionComboBox(box);

                            form.add(box, 2, 0);

                            if(selected.name.equals("Hot Dog Stand") || selected.name.equals("Businessman's Product"))
                            {coinCostField.setText(String.valueOf(selected.coinCost * -1));}
                            else {coinCostField.setText(String.valueOf(selected.coinCost));}
                            
                            form.add(fieldImageViewCreator(images.get("coin")), 0, 1);
                            form.add(new Label("Coin Cost: "), 1, 1);
                            form.add(coinCostField, 2, 1);

                            shopChecker += 2;
                        }
                        else
                        {
                            form.add(fieldImageViewCreator(images.get("strangeSack")), 0, shopChecker);
                            form.add(new Label("Slot " + (i + 1) + " Item: "), 1, shopChecker);

                            ComboBox<String> box = shopFields.get(i);

                            setItemSelectionComboBox(box);

                            form.add(box, 2, shopChecker);
                            shopChecker++;

                            form.add(fieldImageViewCreator(images.get("coin")), 0, shopChecker);
                            form.add(new Label("Slot " + (i + 1) + " Sell Price: "), 1, shopChecker);
                            form.add(sellPriceFields.get(i), 2, shopChecker);
                            shopChecker++;
                        }
                    }

                    centerMenu.getChildren().add(form);
                    ScrollPane centerScroll = new ScrollPane();
                    centerScroll.setContent(centerMenu);
                    borderPane.setCenter(centerScroll);
                });

                fieldList.setOnMouseClicked(e2 -> 
                {
                    centerMenu.getChildren().clear();
                    form.getChildren().clear();

                    shopList.getSelectionModel().clearSelection();

                    upperBox.getChildren().clear();
                    upperBox.getChildren().addAll(searchField, saveChangesButton);

                    FieldLocationData selected = fieldList.getSelectionModel().getSelectedItem();
                    fieldFields = new ArrayList<Object>();

                    if(selected != null)
                    {
                        loadFieldDataFields(selected);
                    }

                    saveChangesButton.setText("Save Struct Changes");

                    form.setVgap(5);
                    form.setHgap(10);
                    form.setPadding(new Insets(10));

                    form.add(fieldImageViewCreator(images.get("magicalMap1")), 0, 0);
                    form.add(new Label("X Coordinate:"), 1, 0);
                    form.add((TextField)(fieldFields.get(0)), 2, 0);
                    form.add(fieldImageViewCreator(images.get("magicalMap1")), 0, 1);
                    form.add(new Label("Y Coordinate:"), 1, 1);
                    form.add((TextField)(fieldFields.get(1)), 2, 1);
                    form.add(fieldImageViewCreator(images.get("magicalMap1")), 0, 2);
                    form.add(new Label("Z Coordinate:"), 1, 2);
                    form.add((TextField)(fieldFields.get(2)), 2, 2);

                    if(selected instanceof FieldLocationData.evt_item_entry || selected instanceof FieldLocationData.evt_mobj_kururing_floor)
                    {
                        form.add(fieldImageViewCreator(images.get("strangeSack")), 0, 3);
                        form.add(new Label("Item: "), 1, 3);
                        form.add((ComboBox<String>)(fieldFields.get(3)), 2, 3);
                    }
                    else if(selected instanceof FieldLocationData.evt_mobj_badgeblk || selected instanceof FieldLocationData.evt_mobj_brick)
                    {
                        form.add(fieldImageViewCreator(images.get("strangeSack")), 0, 3);
                        form.add(new Label("Item: "), 1, 3);
                        form.add((ComboBox<String>)(fieldFields.get(3)), 2, 3);
                        form.add(fieldImageViewCreator(images.get("itemBlock")), 0, 4);
                        form.add(new Label("Block Type: "), 1, 4);
                        form.add((ComboBox<String>)(fieldFields.get(4)), 2, 4);
                    }
                    else if(selected instanceof FieldLocationData.evt_mobj_itembox)
                    {
                        form.add(fieldImageViewCreator(images.get("chest")), 0, 3);
                        form.add(new Label("Chest Type: "), 1, 3);
                        form.add((ComboBox<String>)(fieldFields.get(3)), 2, 3);
                    }
                    else if(selected instanceof FieldLocationData.evt_mobj_recovery_blk)
                    {
                        form.add(fieldImageViewCreator(images.get("coin")), 0, 3);
                        form.add(new Label("Coin Cost: "), 1, 3);
                        form.add((TextField)(fieldFields.get(3)), 2, 3);
                    }

                    centerMenu.getChildren().add(form);
                    ScrollPane centerScroll = new ScrollPane();
                    centerScroll.setContent(centerMenu);
                    borderPane.setCenter(centerScroll);
                });
            }
        });
    }

    /**
     * @Author Jemaroo
     * @Function Loads ItemData Array Data into text fields
     */
    private void loadStructFields(Object struct) 
    {
        if (struct instanceof ItemData) 
        {
            ItemData b = (ItemData) struct;
            UseLocationShopBox.setSelected(b.UseLocationShop);
            UseLocationBattleBox.setSelected(b.UseLocationBattle);
            UseLocationFieldBox.setSelected(b.UseLocationField);
            sortOrderField.setText(String.valueOf(b.sortOrder));
            buyPriceField.setText(String.valueOf(b.buyPrice));
            discountPriceField.setText(String.valueOf(b.discountPrice));
            starPiecePriceField.setText(String.valueOf(b.starPiecePrice));
            
            piantaPriceField.setEditable(false);
            piantaPriceField.setDisable(true);
            piantaPriceField.setText((String.valueOf((int) Math.round((b.buyPrice / 3.0) * 2.0))));
            buyPriceField.textProperty().addListener((obs, oldText, newText) -> { 
                try
                {
                    int result = (int) Math.round(((Integer.parseInt(newText) / 3.0) * 2.0));
                    piantaPriceField.setText(String.valueOf(result));
                }
                catch(NumberFormatException e)
                {
                    piantaPriceField.clear();
                }
            });

            sellPriceField.setText(String.valueOf(b.sellPrice));
            BPCostField.setText(String.valueOf(b.BPCost));
            HPRestoredField.setText(String.valueOf(b.HPRestored));
            FPRestoredField.setText(String.valueOf(b.FPRestored));
            SPRestoredField.setText(String.valueOf(b.SPRestored));
        } 
    }

    /**
     * @Author Jemaroo
     * @Function Loads DropData Array Data into a certain amount of selection boxes
     */
    private ArrayList<ComboBox<String>> loadGrowingSelectionFields(DropData struct) 
    {
        ArrayList<ComboBox<String>> fields = new ArrayList<ComboBox<String>>();

        for(int i = 0; i < struct.ids.size(); i++)
        {
            ComboBox<String> temp = new ComboBox<String>();
            setItemSelectionComboBox(temp);
            temp.getItems().addAll("Nothing", "Strange Sack", "Paper Curse", "Tube Curse", "Plane Curse", "Boat Curse", "Boots", "Super Boots", "Ultra Boots", "Hammer", "Super Hammer", "Ultra Hammer", "Castle Key 1", "Castle Key 2", "Castle Key 3", "Castle Key 4", "Red Key", "Blue Key", "Storage Key 1", "Storage Key 2", "Grotto Key", "Shop Key", "Steeple Key 1", "Steeple Key 2", "Station Key 1", "Station Key 2", "Elevator Key 1", "Elevator Key 2", "Elevator Key 3", "Card Key 1", "Card Key 2", "Card Key 3", "Card Key 4", "Black Key 1", "Black Key 2", "Black Key 3", "Black Key 4", "Star Key", "Palace Key 1", "Palace Key 2", "Palace Key 3", "Palace Key 4", "Palace Key 5", "Palace Key 6", "Palace Key 7", "Palace Key 8", "Palace Key 9", "Palace Key 10", "Palace Key 11", "House Key", "Magical Map", "Contact Lens", "Blimp Ticket", "Train Ticket", "Mailbox SP", "Super Luigi", "Super Luigi 2", "Super Luigi 3", "Super Luigi 4", "Super Luigi 5", "Cookbook", "Moon Stone", "Sun Stone", "Necklace", "Puni Orb", "Champ's Belt", "Poisoned Cake", "Superbombomb", "The Letter \"P\"", "Old Letter", "Chuckola Cola", "Skull Gem", "Gate Handle", "Wedding Ring", "Galley Pot", "Gold Ring", "Shell Earrings", "Autograph", "Ragged Diary", "Blanket", "Vital Paper", "Briefcase", "Goldbob Guide", "Unused Goldbob Guide 1", "Unused Goldbob Guide 2", "Cog", "Data Disk", "Shine Sprite", "Ultra Stone", "Bowser Upgrade Meat", "Mario Wanted Poster", "Special Card", "Platinum Card", "Gold Card", "Silver Card", "Box", "Magical Map (Larger)", "Dubious Paper", "Routing Slip", "Wrestling Magazine", "Present", "Blue Potion", "Red Potion", "Orange Potion", "Green Potion", "???", "Lottery Pick", "Battle Trunks", "Up Arrow", "Package", "Attack FX B", "???", "???", "???", "Diamond Star", "Emerald Star", "Gold Star", "Ruby Star", "Sapphire Star", "Garnet Star", "Crystal Star", "Coin", "Pianta", "Heart Pickup", "Flower Pickup", "Star Piece", "Gold Bar", "Gold Bar x3", "Thunder Bolt", "Thunder Rage", "Shooting Star", "Ice Storm", "Fire Flower", "Earth Quake", "Boo's Sheet", "Volt Shroom", "Repel Cape", "Ruin Powder", "Sleepy Sheep", "POW Block", "Stopwatch", "Dizzy Dial", "Power Punch", "Courage Shell", "HP Sucker", "Trade Off", "Mini Mr. Mini", "Mr. Softener", "Mushroom", "Super Shroom", "Ultra Shroom", "Life Shroom", "Dried Shroom", "Tasty Tonic", "Honey Syrup", "Maple Syrup", "Jammin' Jelly", "Slow Shroom", "Gradual Syrup", "Hot Dog", "Cake", "Point Swap", "Fright Mask", "Mystery", "Inn Coupon", "Whacka Bump", "Coconut", "Dried Bouquet", "Mystic Egg", "Golden Leaf", "Keel Mango", "Fresh Pasta", "Cake Mix", "Hot Sauce", "Turtley Leaf", "Horsetail", "Peachy Peach", "Spite Pouch", "Koopa Curse", "Shroom Fry", "Shroom Roast", "Shroom Steak", "Mistake", "Honey Shroom", "Maple Shroom", "Jelly Shroom", "Honey Super", "Maple Super", "Jelly Super", "Honey Ultra", "Maple Ultra", "Jelly Ultra", "Spicy Soup", "Zess Dinner", "Zess Special", "Zess Deluxe", "Zess Dynamite", "Zess Tea", "Space Food", "Icicle Pop", "Zess Frappe", "Snow Bunny", "Coconut Bomb", "Courage Meal", "Shroom Cake", "Shroom Crepe", "Mousse Cake", "Fried Egg", "Fruit Parfait", "Egg Bomb", "Ink Pasta", "Spaghetti", "Shroom Broth", "Poison Shroom", "Choco Cake", "Mango Delight", "Love Pudding", "Meteor Meal", "Trial Stew", "Couple's Cake", "Inky Sauce", "Omelette Meal", "Koopa Tea", "Koopasta", "Spicy Pasta", "Heartful Cake", "Peach Tart", "Electro Pop", "Fire Pop", "Honey Candy", "Coco Candy", "Jelly Candy", "Zess Cookie", "Healthy Salad", "Koopa Bun", "Fresh Juice", "Audience Can", "Audience Rock", "Audience Bone", "Audience Hammer", "Power Jump", "Multibounce", "Power Bounce", "Tornado Jump", "Shrink Stomp", "Sleepy Stomp", "Soft Stomp", "Power Smash", "Quake Hammer", "Hammer Throw", "Piercing Blow", "Head Rattle", "Fire Drive", "Ice Smash", "Double Dip", "Double Dip P", "Charge", "Charge P", "Super Appeal", "Super Appeal P", "Power Plus", "Power Plus P", "P-Up D-Down", "P-Up D-Down P", "All or Nothing", "All or Nothing P", "Mega Rush", "Mega Rush P", "Power Rush", "Power Rush P", "P-Down D-Up", "P-Down D-Up P", "Last Stand", "Last Stand P", "Defend Plus", "Defend Plus P", "Damage Dodge", "Damage Dodge P", "HP Plus", "HP Plus P", "FP Plus", "Flower Saver", "Flower Saver P", "Ice Power", "Spike Shield", "Feeling Fine", "Feeling Fine P", "Zap Tap", "Double Pain", "Jumpman", "Hammerman", "Return Postage", "Happy Heart", "Happy Heart P", "Happy Flower", "HP Drain", "HP Drain P", "FP Drain", "FP Drain P", "Close Call", "Close Call P", "Pretty Lucky", "Pretty Lucky P", "Lucky Day", "Lucky Day P", "Refund", "Pity Flower", "Pity Flower P", "Quick Change", "Peekaboo", "Timing Tutor", "Heart Finder", "Flower Finder", "Money Money", "Item Hog", "Attack FX R", "Attack FX B", "Attack FX G", "Attack FX Y", "Attack FX P", "Chill Out", "First Attack", "Bump Attack", "Slow Go", "Simplifier", "Unsimplifier", "Lucky Start", "L Emblem", "W Emblem", "Triple Dip", "Lucky Start P", "Debug Badge", "Mega Jump", "Mega Smash", "Mega Quake", "Unused Defend Badge", "Unused Defend Badge P", "Super Charge", "Super Charge P");
            temp.getSelectionModel().select(struct.ids.get(i));

            fields.add(temp);
        }

        //Failsafe
        return fields;
    }

    /**
     * @Author Jemaroo
     * @Function Loads ShopData Array Data into a certain amount of selection boxes
     */
    private ArrayList<ComboBox<String>> loadGrowingSelectionFields(ShopData struct) 
    {
        ArrayList<ComboBox<String>> fields = new ArrayList<ComboBox<String>>();

        for(int i = 0; i < struct.ids.size(); i++)
        {
            ComboBox<String> temp = new ComboBox<String>();
            setItemSelectionComboBox(temp);
            temp.getItems().addAll("Nothing", "Strange Sack", "Paper Curse", "Tube Curse", "Plane Curse", "Boat Curse", "Boots", "Super Boots", "Ultra Boots", "Hammer", "Super Hammer", "Ultra Hammer", "Castle Key 1", "Castle Key 2", "Castle Key 3", "Castle Key 4", "Red Key", "Blue Key", "Storage Key 1", "Storage Key 2", "Grotto Key", "Shop Key", "Steeple Key 1", "Steeple Key 2", "Station Key 1", "Station Key 2", "Elevator Key 1", "Elevator Key 2", "Elevator Key 3", "Card Key 1", "Card Key 2", "Card Key 3", "Card Key 4", "Black Key 1", "Black Key 2", "Black Key 3", "Black Key 4", "Star Key", "Palace Key 1", "Palace Key 2", "Palace Key 3", "Palace Key 4", "Palace Key 5", "Palace Key 6", "Palace Key 7", "Palace Key 8", "Palace Key 9", "Palace Key 10", "Palace Key 11", "House Key", "Magical Map", "Contact Lens", "Blimp Ticket", "Train Ticket", "Mailbox SP", "Super Luigi", "Super Luigi 2", "Super Luigi 3", "Super Luigi 4", "Super Luigi 5", "Cookbook", "Moon Stone", "Sun Stone", "Necklace", "Puni Orb", "Champ's Belt", "Poisoned Cake", "Superbombomb", "The Letter \"P\"", "Old Letter", "Chuckola Cola", "Skull Gem", "Gate Handle", "Wedding Ring", "Galley Pot", "Gold Ring", "Shell Earrings", "Autograph", "Ragged Diary", "Blanket", "Vital Paper", "Briefcase", "Goldbob Guide", "Unused Goldbob Guide 1", "Unused Goldbob Guide 2", "Cog", "Data Disk", "Shine Sprite", "Ultra Stone", "Bowser Upgrade Meat", "Mario Wanted Poster", "Special Card", "Platinum Card", "Gold Card", "Silver Card", "Box", "Magical Map (Larger)", "Dubious Paper", "Routing Slip", "Wrestling Magazine", "Present", "Blue Potion", "Red Potion", "Orange Potion", "Green Potion", "???", "Lottery Pick", "Battle Trunks", "Up Arrow", "Package", "Attack FX B", "???", "???", "???", "Diamond Star", "Emerald Star", "Gold Star", "Ruby Star", "Sapphire Star", "Garnet Star", "Crystal Star", "Coin", "Pianta", "Heart Pickup", "Flower Pickup", "Star Piece", "Gold Bar", "Gold Bar x3", "Thunder Bolt", "Thunder Rage", "Shooting Star", "Ice Storm", "Fire Flower", "Earth Quake", "Boo's Sheet", "Volt Shroom", "Repel Cape", "Ruin Powder", "Sleepy Sheep", "POW Block", "Stopwatch", "Dizzy Dial", "Power Punch", "Courage Shell", "HP Sucker", "Trade Off", "Mini Mr. Mini", "Mr. Softener", "Mushroom", "Super Shroom", "Ultra Shroom", "Life Shroom", "Dried Shroom", "Tasty Tonic", "Honey Syrup", "Maple Syrup", "Jammin' Jelly", "Slow Shroom", "Gradual Syrup", "Hot Dog", "Cake", "Point Swap", "Fright Mask", "Mystery", "Inn Coupon", "Whacka Bump", "Coconut", "Dried Bouquet", "Mystic Egg", "Golden Leaf", "Keel Mango", "Fresh Pasta", "Cake Mix", "Hot Sauce", "Turtley Leaf", "Horsetail", "Peachy Peach", "Spite Pouch", "Koopa Curse", "Shroom Fry", "Shroom Roast", "Shroom Steak", "Mistake", "Honey Shroom", "Maple Shroom", "Jelly Shroom", "Honey Super", "Maple Super", "Jelly Super", "Honey Ultra", "Maple Ultra", "Jelly Ultra", "Spicy Soup", "Zess Dinner", "Zess Special", "Zess Deluxe", "Zess Dynamite", "Zess Tea", "Space Food", "Icicle Pop", "Zess Frappe", "Snow Bunny", "Coconut Bomb", "Courage Meal", "Shroom Cake", "Shroom Crepe", "Mousse Cake", "Fried Egg", "Fruit Parfait", "Egg Bomb", "Ink Pasta", "Spaghetti", "Shroom Broth", "Poison Shroom", "Choco Cake", "Mango Delight", "Love Pudding", "Meteor Meal", "Trial Stew", "Couple's Cake", "Inky Sauce", "Omelette Meal", "Koopa Tea", "Koopasta", "Spicy Pasta", "Heartful Cake", "Peach Tart", "Electro Pop", "Fire Pop", "Honey Candy", "Coco Candy", "Jelly Candy", "Zess Cookie", "Healthy Salad", "Koopa Bun", "Fresh Juice", "Audience Can", "Audience Rock", "Audience Bone", "Audience Hammer", "Power Jump", "Multibounce", "Power Bounce", "Tornado Jump", "Shrink Stomp", "Sleepy Stomp", "Soft Stomp", "Power Smash", "Quake Hammer", "Hammer Throw", "Piercing Blow", "Head Rattle", "Fire Drive", "Ice Smash", "Double Dip", "Double Dip P", "Charge", "Charge P", "Super Appeal", "Super Appeal P", "Power Plus", "Power Plus P", "P-Up D-Down", "P-Up D-Down P", "All or Nothing", "All or Nothing P", "Mega Rush", "Mega Rush P", "Power Rush", "Power Rush P", "P-Down D-Up", "P-Down D-Up P", "Last Stand", "Last Stand P", "Defend Plus", "Defend Plus P", "Damage Dodge", "Damage Dodge P", "HP Plus", "HP Plus P", "FP Plus", "Flower Saver", "Flower Saver P", "Ice Power", "Spike Shield", "Feeling Fine", "Feeling Fine P", "Zap Tap", "Double Pain", "Jumpman", "Hammerman", "Return Postage", "Happy Heart", "Happy Heart P", "Happy Flower", "HP Drain", "HP Drain P", "FP Drain", "FP Drain P", "Close Call", "Close Call P", "Pretty Lucky", "Pretty Lucky P", "Lucky Day", "Lucky Day P", "Refund", "Pity Flower", "Pity Flower P", "Quick Change", "Peekaboo", "Timing Tutor", "Heart Finder", "Flower Finder", "Money Money", "Item Hog", "Attack FX R", "Attack FX B", "Attack FX G", "Attack FX Y", "Attack FX P", "Chill Out", "First Attack", "Bump Attack", "Slow Go", "Simplifier", "Unsimplifier", "Lucky Start", "L Emblem", "W Emblem", "Triple Dip", "Lucky Start P", "Debug Badge", "Mega Jump", "Mega Smash", "Mega Quake", "Unused Defend Badge", "Unused Defend Badge P", "Super Charge", "Super Charge P");
            temp.getSelectionModel().select(struct.ids.get(i));

            fields.add(temp);
        }

        //Failsafe
        return fields;
    }

    /**
     * @Author Jemaroo
     * @Function Loads DropData Array Data into a certain amount of text boxes
     */
    private ArrayList<TextField> loadHoldWeightGrowingTextFields(DropData struct) 
    {
        ArrayList<TextField> fields = new ArrayList<TextField>();

        for(int i = 0; i < struct.ids.size(); i++)
        {
            TextField temp = new TextField();
            temp.setText(String.valueOf(struct.holdWeights.get(i)));

            fields.add(temp);
        }

        //Failsafe
        return fields;
    }

    /**
     * @Author Jemaroo
     * @Function Loads DropData Array Data into a certain amount of text boxes
     */
    private ArrayList<TextField> loadDropWeightGrowingTextFields(DropData struct) 
    {
        ArrayList<TextField> fields = new ArrayList<TextField>();

        for(int i = 0; i < struct.ids.size(); i++)
        {
            TextField temp = new TextField();
            temp.setText(String.valueOf(struct.dropWeights.get(i)));

            fields.add(temp);
        }

        //Failsafe
        return fields;
    }

    /**
     * @Author Jemaroo
     * @Function Loads ShopData Array Data into a certain amount of text boxes
     */
    private ArrayList<TextField> loadThrowWeightGrowingTextFields(ShopData struct) 
    {
        ArrayList<TextField> fields = new ArrayList<TextField>();

        for(int i = 0; i < struct.ids.size(); i++)
        {
            TextField temp = new TextField();
            temp.setText(String.valueOf(struct.throwWeights.get(i)));

            fields.add(temp);
        }

        //Failsafe
        return fields;
    }

    /**
     * @Author Jemaroo
     * @Function Loads ShopData Array Data into a certain amount of text boxes
     */
    private ArrayList<TextField> loadPointRequirementGrowingTextFields(ShopData struct) 
    {
        ArrayList<TextField> fields = new ArrayList<TextField>();

        for(int i = 0; i < struct.ids.size(); i++)
        {
            TextField temp = new TextField();
            temp.setText(String.valueOf(struct.pointRequirements.get(i)));

            fields.add(temp);
        }

        //Failsafe
        return fields;
    }

    /**
     * @Author Jemaroo
     * @Function Loads ShopData Array Data into a certain amount of text boxes
     */
    private ArrayList<TextField> loadSellPriceGrowingTextFields(ShopData struct) 
    {
        ArrayList<TextField> fields = new ArrayList<TextField>();

        for(int i = 0; i < struct.ids.size(); i++)
        {
            TextField temp = new TextField();
            temp.setText(String.valueOf(struct.sellPrices.get(i)));

            fields.add(temp);
        }

        //Failsafe
        return fields;
    }

    /**
     * @Author Jemaroo
     * @Function Saves the text fields to the loaded ItemData Data
     */
    private void saveFieldsToSelectedStruct() 
    {
        Object selected = null;
        if (itemList.getSelectionModel().getSelectedItem() != null) 
        {
            selected = itemList.getSelectionModel().getSelectedItem();
        }
        else if(dropList.getSelectionModel().getSelectedItem() != null)
        {
            selected = dropList.getSelectionModel().getSelectedItem();
        }
        else if(shopList.getSelectionModel().getSelectedItem() != null)
        {
            selected = shopList.getSelectionModel().getSelectedItem();
        }
        else if(fieldList.getSelectionModel().getSelectedItem() != null)
        {
            selected = fieldList.getSelectionModel().getSelectedItem();
        }

        if (selected instanceof ItemData b) 
        {
            b.UseLocationShop = UseLocationShopBox.isSelected();
            b.UseLocationBattle = UseLocationBattleBox.isSelected();
            b.UseLocationField = UseLocationFieldBox.isSelected();
            b.sortOrder = Integer.parseInt(sortOrderField.getText());
            b.buyPrice = Integer.parseInt(buyPriceField.getText());
            b.discountPrice = Integer.parseInt(discountPriceField.getText());
            b.starPiecePrice = Integer.parseInt(starPiecePriceField.getText());
            b.sellPrice = Integer.parseInt(sellPriceField.getText());
            b.BPCost = Integer.parseInt(BPCostField.getText());
            b.HPRestored = Integer.parseInt(HPRestoredField.getText());
            b.FPRestored = Integer.parseInt(FPRestoredField.getText());
            b.SPRestored = Integer.parseInt(SPRestoredField.getText());

            if(b.name.equals("HP Plus") || b.name.equals("FP Plus") || b.name.equals("HP Plus P"))
            {
                for(int i = 0; i < b.properties.size(); i++)
                {
                    b.properties.get(i).propertyValue = Integer.parseInt(propertyFields.get(0).getText());
                }
            }
            else if(b.name.equals("Heart Finder") || b.name.equals("Flower Finder"))
            {
                for(int i = 0; i < b.properties.size(); i += 2)
                {
                    b.properties.get(i).propertyValue = Integer.parseInt(propertyFields.get(0).getText());
                    b.properties.get(i + 1).propertyValue = Integer.parseInt(propertyFields.get(1).getText());
                }
            }
            else if(b.name.equals("Last Stand") || b.name.equals("Last Stand P"))
            {
                for(int i = 0; i < b.properties.size(); i++)
                {
                    String[] tempSpl = propertyFields.get(i).getText().split("/");
                    b.properties.get(i).propertyValue = Integer.parseInt(tempSpl[0]);
                    propertyFields.get(i).setText(tempSpl[0] + "/" + (Integer.parseInt(tempSpl[0]) + 1));
                }
            }
            else
            {
                for(int i = 0; i < b.properties.size(); i++)
                {
                    b.properties.get(i).propertyValue = Integer.parseInt(propertyFields.get(i).getText());
                }
            }
        }
        else if(selected instanceof DropData b)
        {       
            for(int i = 0; i < dropFields.size(); i++)
            {
                b.ids.set(i, dropFields.get(i).getSelectionModel().getSelectedIndex());
            }
            for(int i = 0; i < holdWeightFields.size(); i++)
            {
                b.holdWeights.set(i, Integer.parseInt(holdWeightFields.get(i).getText()));
            }
            for(int i = 0; i < dropWeightFields.size(); i++)
            {
                b.dropWeights.set(i, Integer.parseInt(dropWeightFields.get(i).getText()));
            }
        }
        else if(selected instanceof ShopData b)
        {
            for(int i = 0; i < shopFields.size(); i++)
            {
                b.ids.set(i, shopFields.get(i).getSelectionModel().getSelectedIndex());
            }
            for(int i = 0; i < throwWeightFields.size(); i++)
            {
                b.throwWeights.set(i, Integer.parseInt(throwWeightFields.get(i).getText()));
            }
            for(int i = 0; i < sellPriceFields.size(); i++)
            {
                b.sellPrices.set(i, Integer.parseInt(sellPriceFields.get(i).getText()));
            }
            for(int i = 0; i < pointRequirementFields.size(); i++)
            {
                b.pointRequirements.set(i, Integer.parseInt(pointRequirementFields.get(i).getText()));
            }

            if(((ShopData)selected).type.equals("Inn"))
            {
                b.xCoord = Float.parseFloat((xCoordField).getText());
                b.yCoord = Float.parseFloat((yCoordField).getText());
                b.zCoord = Float.parseFloat((zCoordField).getText());
                b.coinCost = Integer.parseInt((coinCostField).getText());
            }
            else if(((ShopData)selected).type.equals("Coins"))
            {
                if(b.name.equals("Hot Dog Stand") || b.name.equals("Businessman's Product"))
                {b.coinCost = Integer.parseInt((coinCostField).getText()) * -1;}
                else {b.coinCost = Integer.parseInt((coinCostField).getText());}
            }
        }
        else if(selected instanceof FieldLocationData.evt_item_entry b)
        {
            if(!((TextField)fieldFields.get(0)).isDisabled())
            {
                System.out.println("Saving X");
                b.xCoord = Long.parseLong(((TextField)fieldFields.get(0)).getText());
            }
            if(!((TextField)fieldFields.get(1)).isDisabled())
            {
                System.out.println("Saving Y");
                b.yCoord = Long.parseLong(((TextField)fieldFields.get(1)).getText());
            }
            if(!((TextField)fieldFields.get(2)).isDisabled())
            {
                System.out.println("Saving Z");
                b.zCoord = Long.parseLong(((TextField)fieldFields.get(2)).getText());
            }
            b.itemID = ((ComboBox<String>)fieldFields.get(3)).getSelectionModel().getSelectedIndex();
        }
        else if(selected instanceof FieldLocationData.evt_mobj_badgeblk b)
        {
            if(!((TextField)fieldFields.get(0)).isDisabled())
            {
                System.out.println("Saving X");
                b.xCoord = Long.parseLong(((TextField)fieldFields.get(0)).getText());
            }
            if(!((TextField)fieldFields.get(1)).isDisabled())
            {
                System.out.println("Saving Y");
                b.yCoord = Long.parseLong(((TextField)fieldFields.get(1)).getText());
            }
            if(!((TextField)fieldFields.get(2)).isDisabled())
            {
                System.out.println("Saving Z");
                b.zCoord = Long.parseLong(((TextField)fieldFields.get(2)).getText());
            }
            b.itemID = ((ComboBox<String>)fieldFields.get(3)).getSelectionModel().getSelectedIndex();
            b.blockType = ((ComboBox<String>)fieldFields.get(4)).getSelectionModel().getSelectedIndex();
        }
        else if(selected instanceof FieldLocationData.evt_mobj_blk b)
        {
            if(!((TextField)fieldFields.get(0)).isDisabled())
            {
                System.out.println("Saving X");
                b.xCoord = Long.parseLong(((TextField)fieldFields.get(0)).getText());
            }
            if(!((TextField)fieldFields.get(1)).isDisabled())
            {
                System.out.println("Saving Y");
                b.yCoord = Long.parseLong(((TextField)fieldFields.get(1)).getText());
            }
            if(!((TextField)fieldFields.get(2)).isDisabled())
            {
                System.out.println("Saving Z");
                b.zCoord = Long.parseLong(((TextField)fieldFields.get(2)).getText());
            }
        }
        else if(selected instanceof FieldLocationData.evt_mobj_brick b)
        {
            if(!((TextField)fieldFields.get(0)).isDisabled())
            {
                System.out.println("Saving X");
                b.xCoord = Long.parseLong(((TextField)fieldFields.get(0)).getText());
            }
            if(!((TextField)fieldFields.get(1)).isDisabled())
            {
                System.out.println("Saving Y");
                b.yCoord = Long.parseLong(((TextField)fieldFields.get(1)).getText());
            }
            if(!((TextField)fieldFields.get(2)).isDisabled())
            {
                System.out.println("Saving Z");
                b.zCoord = Long.parseLong(((TextField)fieldFields.get(2)).getText());
            }
            b.itemID = ((ComboBox<String>)fieldFields.get(3)).getSelectionModel().getSelectedIndex();
            b.blockType = IndexToBB(((ComboBox<String>)fieldFields.get(4)).getSelectionModel().getSelectedIndex());
        }
        else if(selected instanceof FieldLocationData.evt_mobj_itembox b)
        {
            if(!((TextField)fieldFields.get(0)).isDisabled())
            {
                System.out.println("Saving X");
                b.xCoord = Long.parseLong(((TextField)fieldFields.get(0)).getText());
            }
            if(!((TextField)fieldFields.get(1)).isDisabled())
            {
                System.out.println("Saving Y");
                b.yCoord = Long.parseLong(((TextField)fieldFields.get(1)).getText());
            }
            if(!((TextField)fieldFields.get(2)).isDisabled())
            {
                System.out.println("Saving Z");
                b.zCoord = Long.parseLong(((TextField)fieldFields.get(2)).getText());
            }
            b.chestType = ((ComboBox<String>)fieldFields.get(3)).getSelectionModel().getSelectedIndex();
        }
        else if(selected instanceof FieldLocationData.evt_mobj_kururing_floor b)
        {
            if(!((TextField)fieldFields.get(0)).isDisabled())
            {
                System.out.println("Saving X");
                b.xCoord = Long.parseLong(((TextField)fieldFields.get(0)).getText());
            }
            if(!((TextField)fieldFields.get(1)).isDisabled())
            {
                System.out.println("Saving Y");
                b.yCoord = Long.parseLong(((TextField)fieldFields.get(1)).getText());
            }
            if(!((TextField)fieldFields.get(2)).isDisabled())
            {
                System.out.println("Saving Z");
                b.zCoord = Long.parseLong(((TextField)fieldFields.get(2)).getText());
            }
            b.itemID = ((ComboBox<String>)fieldFields.get(3)).getSelectionModel().getSelectedIndex();
        }
        else if(selected instanceof FieldLocationData.evt_mobj_powerupblk b)
        {
            if(!((TextField)fieldFields.get(0)).isDisabled())
            {
                System.out.println("Saving X");
                b.xCoord = Long.parseLong(((TextField)fieldFields.get(0)).getText());
            }
            if(!((TextField)fieldFields.get(1)).isDisabled())
            {
                System.out.println("Saving Y");
                b.yCoord = Long.parseLong(((TextField)fieldFields.get(1)).getText());
            }
            if(!((TextField)fieldFields.get(2)).isDisabled())
            {
                System.out.println("Saving Z");
                b.zCoord = Long.parseLong(((TextField)fieldFields.get(2)).getText());
            }
        }
        else if(selected instanceof FieldLocationData.evt_mobj_recovery_blk b)
        {
            if(!((TextField)fieldFields.get(0)).isDisabled())
            {
                System.out.println("Saving X");
                b.xCoord = Long.parseLong(((TextField)fieldFields.get(0)).getText());
            }
            if(!((TextField)fieldFields.get(1)).isDisabled())
            {
                System.out.println("Saving Y");
                b.yCoord = Long.parseLong(((TextField)fieldFields.get(1)).getText());
            }
            if(!((TextField)fieldFields.get(2)).isDisabled())
            {
                System.out.println("Saving Z");
                b.zCoord = Long.parseLong(((TextField)fieldFields.get(2)).getText());
            }
            b.coinCost = Long.parseLong(((TextField)fieldFields.get(3)).getText());
        }
        else if(selected instanceof FieldLocationData.evt_mobj_save_blk b)
        {
            if(!((TextField)fieldFields.get(0)).isDisabled())
            {
                System.out.println("Saving X");
                b.xCoord = Long.parseLong(((TextField)fieldFields.get(0)).getText());
            }
            if(!((TextField)fieldFields.get(1)).isDisabled())
            {
                System.out.println("Saving Y");
                b.yCoord = Long.parseLong(((TextField)fieldFields.get(1)).getText());
            }
            if(!((TextField)fieldFields.get(2)).isDisabled())
            {
                System.out.println("Saving Z");
                b.zCoord = Long.parseLong(((TextField)fieldFields.get(2)).getText());
            }
        }
    }

    /**
     * @Author Jemaroo
     * @Function Adds or removes listeners to all text fields to set the text color to red if 0
     */
    public void setRed0TextFieldFormats(boolean activation)
    {
        if(activation)
        {
            sortOrderField.textProperty().addListener(redTextListener1);
            buyPriceField.textProperty().addListener(redTextListener2);
            discountPriceField.textProperty().addListener(redTextListener3);
            starPiecePriceField.textProperty().addListener(redTextListener4);
            sellPriceField.textProperty().addListener(redTextListener5);
            BPCostField.textProperty().addListener(redTextListener6);
            HPRestoredField.textProperty().addListener(redTextListener7);
            FPRestoredField.textProperty().addListener(redTextListener8);
            SPRestoredField.textProperty().addListener(redTextListener9);
            xCoordField.textProperty().addListener(redTextListener10);
            yCoordField.textProperty().addListener(redTextListener11);
            zCoordField.textProperty().addListener(redTextListener12);
            coinCostField.textProperty().addListener(redTextListener13);

            applyRBStyle(sortOrderField);
            applyRBStyle(buyPriceField);
            applyRBStyle(discountPriceField);
            applyRBStyle(starPiecePriceField);
            applyRBStyle(sellPriceField);
            applyRBStyle(BPCostField);
            applyRBStyle(HPRestoredField);
            applyRBStyle(FPRestoredField);
            applyRBStyle(SPRestoredField);
            applyRBStyle(xCoordField);
            applyRBStyle(yCoordField);
            applyRBStyle(zCoordField);
            applyRBStyle(coinCostField);
        }
        if(!activation)
        {
            sortOrderField.textProperty().removeListener(redTextListener1);
            buyPriceField.textProperty().removeListener(redTextListener2);
            discountPriceField.textProperty().removeListener(redTextListener3);
            starPiecePriceField.textProperty().removeListener(redTextListener4);
            sellPriceField.textProperty().removeListener(redTextListener5);
            BPCostField.textProperty().removeListener(redTextListener6);
            HPRestoredField.textProperty().removeListener(redTextListener7);
            FPRestoredField.textProperty().removeListener(redTextListener8);
            SPRestoredField.textProperty().removeListener(redTextListener9);
            xCoordField.textProperty().removeListener(redTextListener10);
            yCoordField.textProperty().removeListener(redTextListener11);
            zCoordField.textProperty().removeListener(redTextListener12);
            coinCostField.textProperty().removeListener(redTextListener13);

            sortOrderField.setStyle(BLACK_STYLE);
            buyPriceField.setStyle(BLACK_STYLE);
            discountPriceField.setStyle(BLACK_STYLE);
            starPiecePriceField.setStyle(BLACK_STYLE);
            sellPriceField.setStyle(BLACK_STYLE);
            BPCostField.setStyle(BLACK_STYLE);
            HPRestoredField.setStyle(BLACK_STYLE);
            FPRestoredField.setStyle(BLACK_STYLE);
            SPRestoredField.setStyle(BLACK_STYLE);
            xCoordField.setStyle(BLACK_STYLE);
            yCoordField.setStyle(BLACK_STYLE);
            zCoordField.setStyle(BLACK_STYLE);
            coinCostField.setStyle(BLACK_STYLE);
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
     * @Function Returns an image based on item name
     */
    public Image listImageSelector(int index)
    {
        switch(index)
        {
            case 0: {return images.get("nothing");}
            case 1: {return images.get("strangeSack");}
            case 2: {return images.get("paperCurse");}
            case 3: {return images.get("tubeCurse");}
            case 4: {return images.get("planeCurse");}
            case 5: {return images.get("boatCurse");}
            case 6: {return images.get("boots");}
            case 7: {return images.get("superBoots");}
            case 8: {return images.get("ultraBoots");}
            case 9: {return images.get("hammer");}
            case 10: {return images.get("superHammer");}
            case 11: {return images.get("ultraHammer");}
            case 12:
            case 13:
            case 14:
            case 15: {return images.get("castleKey");}
            case 16: {return images.get("redKey");}
            case 17: {return images.get("blueKey");}
            case 18:
            case 19: {return images.get("storageKey");}
            case 20: {return images.get("grottoKey");}
            case 21: {return images.get("storageKey");}
            case 22: {return images.get("steepleKey1");}
            case 23: {return images.get("steepleKey2");}
            case 24: {return images.get("stationKey1");}
            case 25: {return images.get("stationKey2");}
            case 26: {return images.get("elevatorKey1");}
            case 27: {return images.get("elevatorKey2");}
            case 28: {return images.get("elevatorKey3");}
            case 29: {return images.get("cardKey1");}
            case 30: {return images.get("cardKey2");}
            case 31: {return images.get("cardKey3");}
            case 32: {return images.get("cardKey4");}
            case 33:
            case 34:
            case 35:
            case 36: {return images.get("blackKey");}
            case 37: {return images.get("starKey");}
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45: {return images.get("palaceKey2");}
            case 46:
            case 47:
            case 48: {return images.get("palaceKey1");}
            case 49: {return images.get("houseKey");}
            case 50: {return images.get("magicalMap1");}
            case 51: {return images.get("contactLens");}
            case 52: {return images.get("blimpTicket");}
            case 53: {return images.get("trainTicket");}
            case 54: {return images.get("mailboxSP");}
            case 55: {return images.get("superLuigi1");}
            case 56: {return images.get("superLuigi2");}
            case 57: {return images.get("superLuigi3");}
            case 58: {return images.get("superLuigi4");}
            case 59: {return images.get("superLuigi5");}
            case 60: {return images.get("cookbook");}
            case 61: {return images.get("moonStone");}
            case 62: {return images.get("sunStone");}
            case 63: {return images.get("necklace");}
            case 64: {return images.get("puniOrb");}
            case 65: {return images.get("champsBelt");}
            case 66: {return images.get("poisonedCake");}
            case 67: {return images.get("superbombomb");}
            case 68: {return images.get("p");}
            case 69: {return images.get("oldLetter");}
            case 70: {return images.get("chuckolaCola");}
            case 71: {return images.get("skullGem");}
            case 72: {return images.get("gateHandle");}
            case 73: {return images.get("weddingRing");}
            case 74: {return images.get("galleyPot");}
            case 75: {return images.get("goldRing");}
            case 76: {return images.get("shellEarrings");}
            case 77: {return images.get("autograph");}
            case 78: {return images.get("raggedDiary");}
            case 79: {return images.get("blanket");}
            case 80: {return images.get("vitalPaper");}
            case 81: {return images.get("briefcase");}
            case 82: {return images.get("goldbobGuide1");}
            case 83: {return images.get("goldbobGuide2");}
            case 84: {return images.get("goldbobGuide3");}
            case 85: {return images.get("cog");}
            case 86: {return images.get("dataDisk");}
            case 87: {return images.get("shineSprite");}
            case 88: {return images.get("ultraStone");}
            case 89: {return images.get("bowserMeat");}
            case 90: {return images.get("marioWantedPoster");}
            case 91: {return images.get("specialCard");}
            case 92: {return images.get("platinumCard");}
            case 93: {return images.get("goldCard");}
            case 94: {return images.get("silverCard");}
            case 95: {return images.get("box");}
            case 96: {return images.get("magicalMap2");}
            case 97: {return images.get("dubiousPaper");}
            case 98: {return images.get("routingSlip");}
            case 99: {return images.get("wrestingMagazine");}
            case 100: {return images.get("present");}
            case 101: {return images.get("bluePotion");}
            case 102: {return images.get("redPotion");}
            case 103: {return images.get("orangePotion");}
            case 104: {return images.get("greenPotion");}
            case 105: {return images.get("unknown");}
            case 106: {return images.get("lotteryPick");}
            case 107: {return images.get("battleTrunks");}
            case 108: {return images.get("upArrow");}
            case 109: {return images.get("box");}
            case 110: {return images.get("attackFXB");}
            case 111:
            case 112:
            case 113: {return images.get("unknown");}
            case 114: {return images.get("diamondStar");}
            case 115: {return images.get("emeraldStar");}
            case 116: {return images.get("goldStar");}
            case 117: {return images.get("rubyStar");}
            case 118: {return images.get("sapphireStar");}
            case 119: {return images.get("garnetStar");}
            case 120: {return images.get("crystalStar");}
            case 121: {return images.get("coin");}
            case 122: {return images.get("pianta");}
            case 123: {return images.get("heart");}
            case 124: {return images.get("flower");}
            case 125: {return images.get("starPiece");}
            case 126: {return images.get("goldBar");}
            case 127: {return images.get("goldBarX3");}
            case 128: {return images.get("thunderBolt");}
            case 129: {return images.get("thunderRage");}
            case 130: {return images.get("shootingStar");}
            case 131: {return images.get("iceStorm");}
            case 132: {return images.get("fireFlower");}
            case 133: {return images.get("earthQuake");}
            case 134: {return images.get("boosSheet");}
            case 135: {return images.get("voltShroom");}
            case 136: {return images.get("repelCape");}
            case 137: {return images.get("ruinPowder");}
            case 138: {return images.get("sleepySheep");}
            case 139: {return images.get("powBlock");}
            case 140: {return images.get("stopwatch");}
            case 141: {return images.get("dizzyDial");}
            case 142: {return images.get("powerPunch");}
            case 143: {return images.get("courageShell");}
            case 144: {return images.get("HPDrain1");}
            case 145: {return images.get("tradeOff");}
            case 146: {return images.get("miniMrMini");}
            case 147: {return images.get("mrsoftener");}
            case 148: {return images.get("mushroom");}
            case 149: {return images.get("superShroom");}
            case 150: {return images.get("ultraShroom");}
            case 151: {return images.get("lifeShroom");}
            case 152: {return images.get("driedShroom");}
            case 153: {return images.get("tastyTonic");}
            case 154: {return images.get("honeySyrup");}
            case 155: {return images.get("mapleSyrup");}
            case 156: {return images.get("jamminJelly");}
            case 157: {return images.get("slowShroom");}
            case 158: {return images.get("gradualSyrup");}
            case 159: {return images.get("hotDog");}
            case 160: {return images.get("cake");}
            case 161: {return images.get("pointSwap");}
            case 162: {return images.get("frightMask");}
            case 163: {return images.get("mystery");}
            case 164: {return images.get("innCoupon");}
            case 165: {return images.get("whackaBump");}
            case 166: {return images.get("coconut");}
            case 167: {return images.get("driedbouquet");}
            case 168: {return images.get("mysticEgg");}
            case 169: {return images.get("goldenLeaf");}
            case 170: {return images.get("keelMango");}
            case 171: {return images.get("freshPasta");}
            case 172: {return images.get("cakeMix");}
            case 173: {return images.get("hotSauce");}
            case 174: {return images.get("turtleyLeaf");}
            case 175: {return images.get("horsetail");}
            case 176: {return images.get("peachyPeach");}
            case 177: {return images.get("spitePouch");}
            case 178: {return images.get("koopaCurse");}
            case 179: {return images.get("shroomFry");}
            case 180: {return images.get("shroomRoast");}
            case 181: {return images.get("shroomSteak");}
            case 182: {return images.get("mistake");}
            case 183: {return images.get("honeyShroom");}
            case 184: {return images.get("mapleShroom");}
            case 185: {return images.get("jellyShroom");}
            case 186: {return images.get("honeySuper");}
            case 187: {return images.get("mapleSuper");}
            case 188: {return images.get("jellySuper");}
            case 189: {return images.get("honeyUltra");}
            case 190: {return images.get("mapleUltra");}
            case 191: {return images.get("jellyUltra");}
            case 192: {return images.get("spicySoup");}
            case 193: {return images.get("zessDinner");}
            case 194: {return images.get("zessSpecial");}
            case 195: {return images.get("zessDeluxe");}
            case 196: {return images.get("zessDynamite");}
            case 197: {return images.get("zessTea");}
            case 198: {return images.get("spaceFood");}
            case 199: {return images.get("iciclePop");}
            case 200: {return images.get("zessFrappe");}
            case 201: {return images.get("snowBunny");}
            case 202: {return images.get("coconutBomb");}
            case 203: {return images.get("courageMeal");}
            case 204: {return images.get("shroomCake");}
            case 205: {return images.get("shroomCrepe");}
            case 206: {return images.get("mousseCake");}
            case 207: {return images.get("friedEgg");}
            case 208: {return images.get("fruitParfait");}
            case 209: {return images.get("eggBomb");}
            case 210: {return images.get("inkPasta");}
            case 211: {return images.get("spaghetti");}
            case 212: {return images.get("shroomBroth");}
            case 213: {return images.get("poisonShroom");}
            case 214: {return images.get("chocoCake");}
            case 215: {return images.get("mangoDelight");}
            case 216: {return images.get("lovePudding");}
            case 217: {return images.get("meteorMeal");}
            case 218: {return images.get("trialStew");}
            case 219: {return images.get("couplesCake");}
            case 220: {return images.get("inkySauce");}
            case 221: {return images.get("omeletteMeal");}
            case 222: {return images.get("koopaTea");}
            case 223: {return images.get("koopasta");}
            case 224: {return images.get("spicyPasta");}
            case 225: {return images.get("heartfulCake");}
            case 226: {return images.get("peachTart");}
            case 227: {return images.get("electroPop");}
            case 228: {return images.get("firePop");}
            case 229: {return images.get("honeyCandy");}
            case 230: {return images.get("cocoCandy");}
            case 231: {return images.get("jellyCandy");}
            case 232: {return images.get("zessCookie");}
            case 233: {return images.get("healthySalad");}
            case 234: {return images.get("koopaBun");}
            case 235: {return images.get("freshJuice");}
            case 236: {return images.get("audienceCan");}
            case 237: {return images.get("audienceRock");}
            case 238: {return images.get("audienceBone");}
            case 239: {return images.get("audienceHammer");}
            case 240: {return images.get("powerJump");}
            case 241: {return images.get("multibounce");}
            case 242: {return images.get("powerBounce");}
            case 243: {return images.get("tornadoJump");}
            case 244: {return images.get("shrinkStomp");}
            case 245: {return images.get("sleepyStomp");}
            case 246: {return images.get("softStomp");}
            case 247: {return images.get("powerSmash");}
            case 248: {return images.get("quakeHammer");}
            case 249: {return images.get("hammerThrow");}
            case 250: {return images.get("piercingBlow");}
            case 251: {return images.get("headRattle");}
            case 252: {return images.get("fireDrive");}
            case 253: {return images.get("iceSmash");}
            case 254: {return images.get("doubleDip");}
            case 255: {return images.get("doubleDipP");}
            case 256: {return images.get("charge");}
            case 257: {return images.get("chargeP");}
            case 258: {return images.get("superAppeal");}
            case 259: {return images.get("superAppealP");}
            case 260: {return images.get("powerPlus");}
            case 261: {return images.get("powerPlusP");}
            case 262: {return images.get("PUpDDown");}
            case 263: {return images.get("PUpDDownP");}
            case 264: {return images.get("allOrNothing");}
            case 265: {return images.get("allOrNothingP");}
            case 266: {return images.get("megaRush");}
            case 267: {return images.get("megaRushP");}
            case 268: {return images.get("powerRush");}
            case 269: {return images.get("powerRushP");}
            case 270: {return images.get("PDownDUp");}
            case 271: {return images.get("PDownDUpP");}
            case 272: {return images.get("lastStand");}
            case 273: {return images.get("lastStandP");}
            case 274: {return images.get("defendPlus");}
            case 275: {return images.get("defendPlusP");}
            case 276: {return images.get("damageDodge");}
            case 277: {return images.get("damageDodgeP");}
            case 278: {return images.get("HPPlus");}
            case 279: {return images.get("HPPlusP");}
            case 280: {return images.get("FPPlus");}
            case 281: {return images.get("flowerSaver");}
            case 282: {return images.get("flowerSaverP");}
            case 283: {return images.get("icePower");}
            case 284: {return images.get("spikeShield");}
            case 285: {return images.get("feelingFine");}
            case 286: {return images.get("feelingFineP");}
            case 287: {return images.get("zapTap");}
            case 288: {return images.get("doublePain");}
            case 289: {return images.get("jumpMan");}
            case 290: {return images.get("hammerMan");}
            case 291: {return images.get("returnPostage");}
            case 292: {return images.get("happyHeart");}
            case 293: {return images.get("happyHeartP");}
            case 294: {return images.get("happyFlower");}
            case 295: {return images.get("HPDrain2");}
            case 296: {return images.get("HPDrainP");}
            case 297: {return images.get("FPDrain");}
            case 298: {return images.get("FPDrainP");}
            case 299: {return images.get("closeCall");}
            case 300: {return images.get("closeCallP");}
            case 301: {return images.get("prettyLucky");}
            case 302: {return images.get("prettyLuckyP");}
            case 303: {return images.get("luckyDay");}
            case 304: {return images.get("luckyDayP");}
            case 305: {return images.get("refund");}
            case 306: {return images.get("pityFlower");}
            case 307: {return images.get("pityFlowerP");}
            case 308: {return images.get("quickChange");}
            case 309: {return images.get("peekaboo");}
            case 310: {return images.get("timingTutor");}
            case 311: {return images.get("heartFinder");}
            case 312: {return images.get("flowerFinder");}
            case 313: {return images.get("moneyMoney");}
            case 314: {return images.get("itemHog");}
            case 315: {return images.get("attackFXR");}
            case 316: {return images.get("attackFXB");}
            case 317: {return images.get("attackFXG");}
            case 318: {return images.get("attackFXY");}
            case 319: {return images.get("attackFXP");}
            case 320: {return images.get("chillOut");}
            case 321: {return images.get("firstAttack");}
            case 322: {return images.get("bumpAttack");}
            case 323: {return images.get("slowGo");}
            case 324: {return images.get("simplifier");}
            case 325: {return images.get("unsimplifier");}
            case 326: {return images.get("luckyStart");}
            case 327: {return images.get("lEmblem");}
            case 328: {return images.get("wEmblem");}
            case 329: {return images.get("tripleDip");}
            case 330: {return images.get("luckyStartP");}
            case 331: {return images.get("zapTap");}
            case 332: {return images.get("megaJump");}
            case 333: {return images.get("megaSmash");}
            case 334: {return images.get("megaQuake");}
            case 335: {return images.get("unusedDefend");}
            case 336: {return images.get("unusedDefendP");}
            case 337: {return images.get("superCharge");}
            case 338: {return images.get("superChargeP");}
            default: {return images.get("unknown");}
        }
    }

    /**
     * @Author Jemaroo
     * @Function Returns an image based on enemy unit name
     */
    public ImageView determineEnemyUnitIcon(int index)
    {
        switch(index)
        {
            case 0: {return fieldImageViewCreator(images.get("unitGoomba"));}
            case 1: {return fieldImageViewCreator(images.get("unitParagoomba"));}
            case 2: {return fieldImageViewCreator(images.get("unitSpikyGoomba"));}
            case 3: {return fieldImageViewCreator(images.get("unitHyperGoomba"));}
            case 4: {return fieldImageViewCreator(images.get("unitHyperParagoomba"));}
            case 5: {return fieldImageViewCreator(images.get("unitHyperSpikyGoomba"));}
            case 6: {return fieldImageViewCreator(images.get("unitGloomba"));}
            case 7: {return fieldImageViewCreator(images.get("unitParagloomba"));}
            case 8: {return fieldImageViewCreator(images.get("unitSpikyGloomba"));}
            case 9: {return fieldImageViewCreator(images.get("unitKoopaTroopa"));}
            case 10: {return fieldImageViewCreator(images.get("unitParatroopa"));}
            case 11: {return fieldImageViewCreator(images.get("unitKPKoopa"));}
            case 12: {return fieldImageViewCreator(images.get("unitKPParatroopa"));}
            case 13: {return fieldImageViewCreator(images.get("unitShadyKoopa"));}
            case 14: {return fieldImageViewCreator(images.get("unitShadyParatroopa"));}
            case 15: {return fieldImageViewCreator(images.get("unitDarkKoopa"));}
            case 16: {return fieldImageViewCreator(images.get("unitKoopatrol"));}
            case 17: {return fieldImageViewCreator(images.get("unitDarkKoopatrol"));}
            case 18: {return fieldImageViewCreator(images.get("unitDullBones"));}
            case 19: {return fieldImageViewCreator(images.get("unitRedBones"));}
            case 20: {return fieldImageViewCreator(images.get("unitDryBones"));}
            case 21: {return fieldImageViewCreator(images.get("unitHammerBro"));}
            case 22: {return fieldImageViewCreator(images.get("unitBoomerangBro"));}
            case 23: {return fieldImageViewCreator(images.get("unitFireBro"));}
            case 24: {return fieldImageViewCreator(images.get("unitLakitu"));}
            case 25: {return fieldImageViewCreator(images.get("unitDarkLakitu"));}
            case 26: {return fieldImageViewCreator(images.get("unitSpiny"));}
            case 27: {return fieldImageViewCreator(images.get("unitBuzzyBeetle"));}
            case 28: {return fieldImageViewCreator(images.get("unitSpikyBuzzy"));}
            case 29: {return fieldImageViewCreator(images.get("unitParabuzzy"));}
            case 30: {return fieldImageViewCreator(images.get("unitSpikyParabuzzy"));}
            case 31: {return fieldImageViewCreator(images.get("unknown"));}
            case 32: {return fieldImageViewCreator(images.get("unitMagikoopa"));}
            case 33: {return fieldImageViewCreator(images.get("unitGus"));}
            case 34: {return fieldImageViewCreator(images.get("unitDarkCraw"));}
            case 35: {return fieldImageViewCreator(images.get("unitBandit"));}
            case 36: {return fieldImageViewCreator(images.get("unitBigBandit"));}
            case 37: {return fieldImageViewCreator(images.get("unitBadgeBandit"));}
            case 38: {return fieldImageViewCreator(images.get("unitSpinia"));}
            case 39: {return fieldImageViewCreator(images.get("unitSpania"));}
            case 40: {return fieldImageViewCreator(images.get("unitSpunia"));}
            case 41: {return fieldImageViewCreator(images.get("unitFuzzy"));}
            case 42: {return fieldImageViewCreator(images.get("unitGoldFuzzy"));}
            case 43: {return fieldImageViewCreator(images.get("unitGreenFuzzy"));}
            case 44: {return fieldImageViewCreator(images.get("unitFlowerFuzzy"));}
            case 45: {return fieldImageViewCreator(images.get("unitPokey"));}
            case 46: {return fieldImageViewCreator(images.get("unitPoisonPokey"));}
            case 47: {return fieldImageViewCreator(images.get("unitPalePiranha"));}
            case 48: {return fieldImageViewCreator(images.get("unitPutridPiranha"));}
            case 49: {return fieldImageViewCreator(images.get("unitFrostPiranha"));}
            case 50: {return fieldImageViewCreator(images.get("unitPiranhaPlant"));}
            case 51: {return fieldImageViewCreator(images.get("unitCrazeeDayzee"));}
            case 52: {return fieldImageViewCreator(images.get("unitAmazyDayzee"));}
            case 53: {return fieldImageViewCreator(images.get("unitPider"));}
            case 54: {return fieldImageViewCreator(images.get("unitArantula"));}
            case 55: {return fieldImageViewCreator(images.get("unitSwooper"));}
            case 56: {return fieldImageViewCreator(images.get("unitSwoopula"));}
            case 57: {return fieldImageViewCreator(images.get("unitSwampire"));}
            case 58: {return fieldImageViewCreator(images.get("unitDarkPuff"));}
            case 59: {return fieldImageViewCreator(images.get("unitRuffPuff"));}
            case 60: {return fieldImageViewCreator(images.get("unitIcePuff"));}
            case 61: {return fieldImageViewCreator(images.get("unitPoisonPuff"));}
            case 62: {return fieldImageViewCreator(images.get("unitBoo"));}
            case 63: {return fieldImageViewCreator(images.get("unitAtomicBoo"));}
            case 64: {return fieldImageViewCreator(images.get("unitDarkBoo"));}
            case 65: {return fieldImageViewCreator(images.get("unitEmber"));}
            case 66: {return fieldImageViewCreator(images.get("unitLavaBubble"));}
            case 67: {return fieldImageViewCreator(images.get("unitPhantomEmber"));}
            case 68: {return fieldImageViewCreator(images.get("unitBaldCleft"));}
            case 69: {return fieldImageViewCreator(images.get("unitHyperBaldCleft"));}
            case 70: {return fieldImageViewCreator(images.get("unitCleft"));}
            case 71: {return fieldImageViewCreator(images.get("unitIronCleft1"));}
            case 72: {return fieldImageViewCreator(images.get("unitIronCleft2"));}
            case 73: {return fieldImageViewCreator(images.get("unitHyperCleft"));}
            case 74: {return fieldImageViewCreator(images.get("unitMoonCleft"));}
            case 75: {return fieldImageViewCreator(images.get("unitBristle"));}
            case 76: {return fieldImageViewCreator(images.get("unitDarkBristle"));}
            case 77: {return fieldImageViewCreator(images.get("unitBobomb"));}
            case 78: {return fieldImageViewCreator(images.get("unitBulkyBobomb"));}
            case 79: {return fieldImageViewCreator(images.get("unitBobulk"));}
            case 80: {return fieldImageViewCreator(images.get("unitChainChomp"));}
            case 81: {return fieldImageViewCreator(images.get("unitRedChomp"));}
            case 82: {return fieldImageViewCreator(images.get("unitBillBlaster"));}
            case 83: {return fieldImageViewCreator(images.get("unitBulletBill"));}
            case 84: {return fieldImageViewCreator(images.get("unitBombshellBillBlaster"));}
            case 85: {return fieldImageViewCreator(images.get("unitBombshellBill"));}
            case 86: {return fieldImageViewCreator(images.get("unitDarkWizzerd"));}
            case 87: {return fieldImageViewCreator(images.get("unitWizzerd"));}
            case 88: {return fieldImageViewCreator(images.get("unitEliteWizzerd"));}
            case 89: {return fieldImageViewCreator(images.get("unitBlooper"));}
            case 90: {return fieldImageViewCreator(images.get("unitHooktail"));}
            case 91: {return fieldImageViewCreator(images.get("unitBonetail"));}
            case 92: {return fieldImageViewCreator(images.get("unitRawkHawk"));}
            case 93: {return fieldImageViewCreator(images.get("unitMachoGrubba"));}
            case 94: {return fieldImageViewCreator(images.get("unitDoopliss"));}
            case 95: {return fieldImageViewCreator(images.get("unitDooplissMario"));}
            case 96: {return fieldImageViewCreator(images.get("unitCortez"));}
            case 97: {return fieldImageViewCreator(images.get("unitSmorg"));}
            case 98: {return fieldImageViewCreator(images.get("unitXNaut"));}
            case 99: {return fieldImageViewCreator(images.get("unitXNautPHD"));}
            case 100: {return fieldImageViewCreator(images.get("unitEliteXNaut"));}
            case 101: {return fieldImageViewCreator(images.get("unitYux"));}
            case 102: {return fieldImageViewCreator(images.get("unitZYux"));}
            case 103: {return fieldImageViewCreator(images.get("unitXYux"));}
            case 104: {return fieldImageViewCreator(images.get("unitMagnusVonGrapple"));}
            case 105: {return fieldImageViewCreator(images.get("unitLordCrump"));}
            case 106: {return fieldImageViewCreator(images.get("unitVivian"));}
            default: {return fieldImageViewCreator(images.get("unknown"));}
        }
    }

    /**
     * @Author Jemaroo
     * @Function Returns an image based on shop table name
     */
    public ImageView determineShopIcon(ShopData shop)
    {
        switch(shop.name)
        {
            case "Item Hog Table": return fieldImageViewCreator(images.get("itemHog"));
            case "Mystery Table": return fieldImageViewCreator(images.get("mystery"));
            case "Dazzle Table": return fieldImageViewCreator(images.get("dazzleCustom"));
            case "Charlieton Rogueport Table": return fieldImageViewCreator(images.get("charlietonCustom"));
            case "Prologue Badge Shop Table":
            case "Post-1 Badge Shop Table":
            case "Post-2 Badge Shop Table":
            case "Post-3 Badge Shop Table":
            case "Post-4 Badge Shop Table":
            case "Post-5 Badge Shop Table":
            case "Post-6 Badge Shop Table": return fieldImageViewCreator(images.get("badgeMouseCustom"));
            case "Charlieton Pit Table": return fieldImageViewCreator(images.get("charlietonCustom"));
            case "Pianta Parlor Table":
            case "Pianta Parlor Special Card Table":
            case "Pianta Parlor Silver Card Table":
            case "Pianta Parlor Gold Card Table":
            case "Pianta Parlor Platinum Card Table": return fieldImageViewCreator(images.get("lahlaCustom"));
            case "Shop Point Rewards Table": return fieldImageViewCreator(images.get("shopToadCustom"));
            case "Northwinds Mart Table":
            case "Northwinds Mart Sell Item Table": return fieldImageViewCreator(images.get("pinkBobombCustom"));
            case "Toad Bros. Bazaar Table 1":
            case "Toad Bros. Bazaar Table 2":
            case "Toad Bros. Bazaar Table 3":
            case "Toad Bros. Bazaar Table 4":
            case "Toad Bros. Bazaar Table 5":
            case "Toad Bros. Bazaar Table 6":
            case "Toad Bros. Bazaar Table 7":
            case "Toad Bros. Bazaar Sell Item Table": return fieldImageViewCreator(images.get("shopToadCustom"));
            case "Westside Goods Table":
            case "Westside Goods Sell Item Table": return fieldImageViewCreator(images.get("lahlaCustom"));
            case "Pungent's Great Tree Shop Table":
            case "Pungent's Great Tree Shop Sell Item Table": return fieldImageViewCreator(images.get("pungentCustom"));
            case "Keelhaul Galleria Table":
            case "Keelhaul Galleria Sell Item Table":
            case "Niff T.'s Shop Table":
            case "Niff T.'s Shop Sell Item Table": return fieldImageViewCreator(images.get("shopToadCustom"));
            case "Sales Stall Table":
            case "Sales Stall Sell Item Table": return fieldImageViewCreator(images.get("serverToadCustom"));
            case "Deepdown Depot Table":
            case "Deepdown Depot Sell Item Table": return fieldImageViewCreator(images.get("innkeeperToadCustom"));
            case "Souvenir Shop Table":
            case "Souvenir Shop Sell Item Table": return fieldImageViewCreator(images.get("shopToadCustom"));
            case "Twilight Shop Table":
            case "Twilight Shop Sell Item Table": return fieldImageViewCreator(images.get("twilightShopManagerWifeCustom"));
            case "Toad Audience Throw Table": return fieldImageViewCreator(images.get("audienceToadCustom"));
            case "Shy Guy Audience Throw Table": return fieldImageViewCreator(images.get("audienceShyGuyCustom"));
            case "Puni Audience Throw Table": return fieldImageViewCreator(images.get("audiencePuniCustom"));
            case "Koopa Troopa Audience Throw Table": return fieldImageViewCreator(images.get("audienceKoopaCustom"));
            case "Luigi Audience Throw Table": return fieldImageViewCreator(images.get("audienceLuigiCustom"));
            case "X-Naut Audience Throw Table": return fieldImageViewCreator(images.get("audienceXNautCustom"));
            case "Boo Audience Throw Table": return fieldImageViewCreator(images.get("audienceBooCustom"));
            case "Hammer Bro Audience Throw Table": return fieldImageViewCreator(images.get("audienceHammerBroCustom"));
            case "Dull Bones Audience Throw Table": return fieldImageViewCreator(images.get("audienceDullBonesCustom"));
            case "Crazee Dayzee Audience Throw Table": return fieldImageViewCreator(images.get("audienceDayzeeCustom"));
            case "Bulky Bob-omb Audience Throw Table": return fieldImageViewCreator(images.get("audienceBulkyBobombCustom"));
            case "Goomba Audience Throw Table": return fieldImageViewCreator(images.get("audienceGoombaCustom"));
            case "Piranha Plant Audience Throw Table": return fieldImageViewCreator(images.get("audiencePiranhaPlantCustom"));
            case "Pit Rewards Table": return fieldImageViewCreator(images.get("chest"));
            case "Happy Lucky Lottery Table": return fieldImageViewCreator(images.get("luckyCustom"));
            case "Boo Quiz Reward Table": return fieldImageViewCreator(images.get("unitBoo"));
            case "Podley's Place Breakfast": return fieldImageViewCreator(images.get("innkeeperToadCustom"));
            case "Petalburg Inn Breakfast": return fieldImageViewCreator(images.get("innkeeperToadCustom"));
            case "Twilight Inn Breakfast": return fieldImageViewCreator(images.get("twilightTownCitizenCustom"));
            case "Seaside Shanty Breakfast": return fieldImageViewCreator(images.get("innkeeperToadCustom"));
            case "Royal Poshley Park Tower Breakfast": return fieldImageViewCreator(images.get("innkeeperToadCustom"));
            case "Fahr Outpost Inn Breakfast": return fieldImageViewCreator(images.get("pinkBobombCustom"));
            case "Souvenir Stand": return fieldImageViewCreator(images.get("toadiaCustom"));
            case "Hot Dog Stand": return fieldImageViewCreator(images.get("hoggleCustom"));
            case "Businessman's Product": return fieldImageViewCreator(images.get("businessmanCustom"));
            
            default: return fieldImageViewCreator(images.get("unknown"));
        }
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
     * @Function Loads Field Location Data into the correct types of fields
     */
    public void loadFieldDataFields(FieldLocationData selected)
    {
        TextField xCoordField = new TextField();
        if(checkForLW(selected.xCoord))
        {
            xCoordField.setDisable(true);
            xCoordField.setEditable(false);
            xCoordField.setText(getLW(selected.xCoord));
        }
        else
        {
            xCoordField.setText(String.valueOf(selected.xCoord));
        }
        fieldFields.add(xCoordField);

        TextField yCoordField = new TextField();
        if(checkForLW(selected.yCoord))
        {
            yCoordField.setDisable(true);
            yCoordField.setEditable(false);
            yCoordField.setText(getLW(selected.yCoord));
        }
        else
        {
            yCoordField.setText(String.valueOf(selected.yCoord));
        }
        fieldFields.add(yCoordField);

        TextField zCoordField = new TextField();
        if(checkForLW(selected.zCoord))
        {
            zCoordField.setDisable(true);
            zCoordField.setEditable(false);
            zCoordField.setText(getLW(selected.zCoord));
        }
        else
        {
            zCoordField.setText(String.valueOf(selected.zCoord));
        }
        fieldFields.add(zCoordField);

        if(selected instanceof FieldLocationData.evt_item_entry)
        {
            ComboBox<String> box = new ComboBox<String>();
            box.getItems().addAll("Nothing", "Strange Sack", "Paper Curse", "Tube Curse", "Plane Curse", "Boat Curse", "Boots", "Super Boots", "Ultra Boots", "Hammer", "Super Hammer", "Ultra Hammer", "Castle Key 1", "Castle Key 2", "Castle Key 3", "Castle Key 4", "Red Key", "Blue Key", "Storage Key 1", "Storage Key 2", "Grotto Key", "Shop Key", "Steeple Key 1", "Steeple Key 2", "Station Key 1", "Station Key 2", "Elevator Key 1", "Elevator Key 2", "Elevator Key 3", "Card Key 1", "Card Key 2", "Card Key 3", "Card Key 4", "Black Key 1", "Black Key 2", "Black Key 3", "Black Key 4", "Star Key", "Palace Key 1", "Palace Key 2", "Palace Key 3", "Palace Key 4", "Palace Key 5", "Palace Key 6", "Palace Key 7", "Palace Key 8", "Palace Key 9", "Palace Key 10", "Palace Key 11", "House Key", "Magical Map", "Contact Lens", "Blimp Ticket", "Train Ticket", "Mailbox SP", "Super Luigi", "Super Luigi 2", "Super Luigi 3", "Super Luigi 4", "Super Luigi 5", "Cookbook", "Moon Stone", "Sun Stone", "Necklace", "Puni Orb", "Champ's Belt", "Poisoned Cake", "Superbombomb", "The Letter \"P\"", "Old Letter", "Chuckola Cola", "Skull Gem", "Gate Handle", "Wedding Ring", "Galley Pot", "Gold Ring", "Shell Earrings", "Autograph", "Ragged Diary", "Blanket", "Vital Paper", "Briefcase", "Goldbob Guide", "Unused Goldbob Guide 1", "Unused Goldbob Guide 2", "Cog", "Data Disk", "Shine Sprite", "Ultra Stone", "Bowser Upgrade Meat", "Mario Wanted Poster", "Special Card", "Platinum Card", "Gold Card", "Silver Card", "Box", "Magical Map (Larger)", "Dubious Paper", "Routing Slip", "Wrestling Magazine", "Present", "Blue Potion", "Red Potion", "Orange Potion", "Green Potion", "???", "Lottery Pick", "Battle Trunks", "Up Arrow", "Package", "Attack FX B", "???", "???", "???", "Diamond Star", "Emerald Star", "Gold Star", "Ruby Star", "Sapphire Star", "Garnet Star", "Crystal Star", "Coin", "Pianta", "Heart Pickup", "Flower Pickup", "Star Piece", "Gold Bar", "Gold Bar x3", "Thunder Bolt", "Thunder Rage", "Shooting Star", "Ice Storm", "Fire Flower", "Earth Quake", "Boo's Sheet", "Volt Shroom", "Repel Cape", "Ruin Powder", "Sleepy Sheep", "POW Block", "Stopwatch", "Dizzy Dial", "Power Punch", "Courage Shell", "HP Sucker", "Trade Off", "Mini Mr. Mini", "Mr. Softener", "Mushroom", "Super Shroom", "Ultra Shroom", "Life Shroom", "Dried Shroom", "Tasty Tonic", "Honey Syrup", "Maple Syrup", "Jammin' Jelly", "Slow Shroom", "Gradual Syrup", "Hot Dog", "Cake", "Point Swap", "Fright Mask", "Mystery", "Inn Coupon", "Whacka Bump", "Coconut", "Dried Bouquet", "Mystic Egg", "Golden Leaf", "Keel Mango", "Fresh Pasta", "Cake Mix", "Hot Sauce", "Turtley Leaf", "Horsetail", "Peachy Peach", "Spite Pouch", "Koopa Curse", "Shroom Fry", "Shroom Roast", "Shroom Steak", "Mistake", "Honey Shroom", "Maple Shroom", "Jelly Shroom", "Honey Super", "Maple Super", "Jelly Super", "Honey Ultra", "Maple Ultra", "Jelly Ultra", "Spicy Soup", "Zess Dinner", "Zess Special", "Zess Deluxe", "Zess Dynamite", "Zess Tea", "Space Food", "Icicle Pop", "Zess Frappe", "Snow Bunny", "Coconut Bomb", "Courage Meal", "Shroom Cake", "Shroom Crepe", "Mousse Cake", "Fried Egg", "Fruit Parfait", "Egg Bomb", "Ink Pasta", "Spaghetti", "Shroom Broth", "Poison Shroom", "Choco Cake", "Mango Delight", "Love Pudding", "Meteor Meal", "Trial Stew", "Couple's Cake", "Inky Sauce", "Omelette Meal", "Koopa Tea", "Koopasta", "Spicy Pasta", "Heartful Cake", "Peach Tart", "Electro Pop", "Fire Pop", "Honey Candy", "Coco Candy", "Jelly Candy", "Zess Cookie", "Healthy Salad", "Koopa Bun", "Fresh Juice", "Audience Can", "Audience Rock", "Audience Bone", "Audience Hammer", "Power Jump", "Multibounce", "Power Bounce", "Tornado Jump", "Shrink Stomp", "Sleepy Stomp", "Soft Stomp", "Power Smash", "Quake Hammer", "Hammer Throw", "Piercing Blow", "Head Rattle", "Fire Drive", "Ice Smash", "Double Dip", "Double Dip P", "Charge", "Charge P", "Super Appeal", "Super Appeal P", "Power Plus", "Power Plus P", "P-Up D-Down", "P-Up D-Down P", "All or Nothing", "All or Nothing P", "Mega Rush", "Mega Rush P", "Power Rush", "Power Rush P", "P-Down D-Up", "P-Down D-Up P", "Last Stand", "Last Stand P", "Defend Plus", "Defend Plus P", "Damage Dodge", "Damage Dodge P", "HP Plus", "HP Plus P", "FP Plus", "Flower Saver", "Flower Saver P", "Ice Power", "Spike Shield", "Feeling Fine", "Feeling Fine P", "Zap Tap", "Double Pain", "Jumpman", "Hammerman", "Return Postage", "Happy Heart", "Happy Heart P", "Happy Flower", "HP Drain", "HP Drain P", "FP Drain", "FP Drain P", "Close Call", "Close Call P", "Pretty Lucky", "Pretty Lucky P", "Lucky Day", "Lucky Day P", "Refund", "Pity Flower", "Pity Flower P", "Quick Change", "Peekaboo", "Timing Tutor", "Heart Finder", "Flower Finder", "Money Money", "Item Hog", "Attack FX R", "Attack FX B", "Attack FX G", "Attack FX Y", "Attack FX P", "Chill Out", "First Attack", "Bump Attack", "Slow Go", "Simplifier", "Unsimplifier", "Lucky Start", "L Emblem", "W Emblem", "Triple Dip", "Lucky Start P", "Debug Badge", "Mega Jump", "Mega Smash", "Mega Quake", "Unused Defend Badge", "Unused Defend Badge P", "Super Charge", "Super Charge P");
            box.getSelectionModel().select((int)(((FieldLocationData.evt_item_entry)selected).itemID));

            setItemSelectionComboBox(box);

            fieldFields.add(box);
        }
        else if(selected instanceof FieldLocationData.evt_mobj_badgeblk)
        {
            ComboBox<String> box = new ComboBox<String>();
            box.getItems().addAll("Nothing", "Strange Sack", "Paper Curse", "Tube Curse", "Plane Curse", "Boat Curse", "Boots", "Super Boots", "Ultra Boots", "Hammer", "Super Hammer", "Ultra Hammer", "Castle Key 1", "Castle Key 2", "Castle Key 3", "Castle Key 4", "Red Key", "Blue Key", "Storage Key 1", "Storage Key 2", "Grotto Key", "Shop Key", "Steeple Key 1", "Steeple Key 2", "Station Key 1", "Station Key 2", "Elevator Key 1", "Elevator Key 2", "Elevator Key 3", "Card Key 1", "Card Key 2", "Card Key 3", "Card Key 4", "Black Key 1", "Black Key 2", "Black Key 3", "Black Key 4", "Star Key", "Palace Key 1", "Palace Key 2", "Palace Key 3", "Palace Key 4", "Palace Key 5", "Palace Key 6", "Palace Key 7", "Palace Key 8", "Palace Key 9", "Palace Key 10", "Palace Key 11", "House Key", "Magical Map", "Contact Lens", "Blimp Ticket", "Train Ticket", "Mailbox SP", "Super Luigi", "Super Luigi 2", "Super Luigi 3", "Super Luigi 4", "Super Luigi 5", "Cookbook", "Moon Stone", "Sun Stone", "Necklace", "Puni Orb", "Champ's Belt", "Poisoned Cake", "Superbombomb", "The Letter \"P\"", "Old Letter", "Chuckola Cola", "Skull Gem", "Gate Handle", "Wedding Ring", "Galley Pot", "Gold Ring", "Shell Earrings", "Autograph", "Ragged Diary", "Blanket", "Vital Paper", "Briefcase", "Goldbob Guide", "Unused Goldbob Guide 1", "Unused Goldbob Guide 2", "Cog", "Data Disk", "Shine Sprite", "Ultra Stone", "Bowser Upgrade Meat", "Mario Wanted Poster", "Special Card", "Platinum Card", "Gold Card", "Silver Card", "Box", "Magical Map (Larger)", "Dubious Paper", "Routing Slip", "Wrestling Magazine", "Present", "Blue Potion", "Red Potion", "Orange Potion", "Green Potion", "???", "Lottery Pick", "Battle Trunks", "Up Arrow", "Package", "Attack FX B", "???", "???", "???", "Diamond Star", "Emerald Star", "Gold Star", "Ruby Star", "Sapphire Star", "Garnet Star", "Crystal Star", "Coin", "Pianta", "Heart Pickup", "Flower Pickup", "Star Piece", "Gold Bar", "Gold Bar x3", "Thunder Bolt", "Thunder Rage", "Shooting Star", "Ice Storm", "Fire Flower", "Earth Quake", "Boo's Sheet", "Volt Shroom", "Repel Cape", "Ruin Powder", "Sleepy Sheep", "POW Block", "Stopwatch", "Dizzy Dial", "Power Punch", "Courage Shell", "HP Sucker", "Trade Off", "Mini Mr. Mini", "Mr. Softener", "Mushroom", "Super Shroom", "Ultra Shroom", "Life Shroom", "Dried Shroom", "Tasty Tonic", "Honey Syrup", "Maple Syrup", "Jammin' Jelly", "Slow Shroom", "Gradual Syrup", "Hot Dog", "Cake", "Point Swap", "Fright Mask", "Mystery", "Inn Coupon", "Whacka Bump", "Coconut", "Dried Bouquet", "Mystic Egg", "Golden Leaf", "Keel Mango", "Fresh Pasta", "Cake Mix", "Hot Sauce", "Turtley Leaf", "Horsetail", "Peachy Peach", "Spite Pouch", "Koopa Curse", "Shroom Fry", "Shroom Roast", "Shroom Steak", "Mistake", "Honey Shroom", "Maple Shroom", "Jelly Shroom", "Honey Super", "Maple Super", "Jelly Super", "Honey Ultra", "Maple Ultra", "Jelly Ultra", "Spicy Soup", "Zess Dinner", "Zess Special", "Zess Deluxe", "Zess Dynamite", "Zess Tea", "Space Food", "Icicle Pop", "Zess Frappe", "Snow Bunny", "Coconut Bomb", "Courage Meal", "Shroom Cake", "Shroom Crepe", "Mousse Cake", "Fried Egg", "Fruit Parfait", "Egg Bomb", "Ink Pasta", "Spaghetti", "Shroom Broth", "Poison Shroom", "Choco Cake", "Mango Delight", "Love Pudding", "Meteor Meal", "Trial Stew", "Couple's Cake", "Inky Sauce", "Omelette Meal", "Koopa Tea", "Koopasta", "Spicy Pasta", "Heartful Cake", "Peach Tart", "Electro Pop", "Fire Pop", "Honey Candy", "Coco Candy", "Jelly Candy", "Zess Cookie", "Healthy Salad", "Koopa Bun", "Fresh Juice", "Audience Can", "Audience Rock", "Audience Bone", "Audience Hammer", "Power Jump", "Multibounce", "Power Bounce", "Tornado Jump", "Shrink Stomp", "Sleepy Stomp", "Soft Stomp", "Power Smash", "Quake Hammer", "Hammer Throw", "Piercing Blow", "Head Rattle", "Fire Drive", "Ice Smash", "Double Dip", "Double Dip P", "Charge", "Charge P", "Super Appeal", "Super Appeal P", "Power Plus", "Power Plus P", "P-Up D-Down", "P-Up D-Down P", "All or Nothing", "All or Nothing P", "Mega Rush", "Mega Rush P", "Power Rush", "Power Rush P", "P-Down D-Up", "P-Down D-Up P", "Last Stand", "Last Stand P", "Defend Plus", "Defend Plus P", "Damage Dodge", "Damage Dodge P", "HP Plus", "HP Plus P", "FP Plus", "Flower Saver", "Flower Saver P", "Ice Power", "Spike Shield", "Feeling Fine", "Feeling Fine P", "Zap Tap", "Double Pain", "Jumpman", "Hammerman", "Return Postage", "Happy Heart", "Happy Heart P", "Happy Flower", "HP Drain", "HP Drain P", "FP Drain", "FP Drain P", "Close Call", "Close Call P", "Pretty Lucky", "Pretty Lucky P", "Lucky Day", "Lucky Day P", "Refund", "Pity Flower", "Pity Flower P", "Quick Change", "Peekaboo", "Timing Tutor", "Heart Finder", "Flower Finder", "Money Money", "Item Hog", "Attack FX R", "Attack FX B", "Attack FX G", "Attack FX Y", "Attack FX P", "Chill Out", "First Attack", "Bump Attack", "Slow Go", "Simplifier", "Unsimplifier", "Lucky Start", "L Emblem", "W Emblem", "Triple Dip", "Lucky Start P", "Debug Badge", "Mega Jump", "Mega Smash", "Mega Quake", "Unused Defend Badge", "Unused Defend Badge P", "Super Charge", "Super Charge P");
            box.getSelectionModel().select((int)(((FieldLocationData.evt_mobj_badgeblk)selected).itemID));

            setItemSelectionComboBox(box);

            fieldFields.add(box);

            ComboBox<String> box2 = new ComboBox<String>();
            box2.getItems().addAll("Item Block", "Badge Block");
            box2.getSelectionModel().select((int)(((FieldLocationData.evt_mobj_badgeblk)selected).blockType));

            fieldFields.add(box2);
        }
        else if(selected instanceof FieldLocationData.evt_mobj_brick)
        {
            ComboBox<String> box = new ComboBox<String>();
            box.getItems().addAll("Nothing", "Strange Sack", "Paper Curse", "Tube Curse", "Plane Curse", "Boat Curse", "Boots", "Super Boots", "Ultra Boots", "Hammer", "Super Hammer", "Ultra Hammer", "Castle Key 1", "Castle Key 2", "Castle Key 3", "Castle Key 4", "Red Key", "Blue Key", "Storage Key 1", "Storage Key 2", "Grotto Key", "Shop Key", "Steeple Key 1", "Steeple Key 2", "Station Key 1", "Station Key 2", "Elevator Key 1", "Elevator Key 2", "Elevator Key 3", "Card Key 1", "Card Key 2", "Card Key 3", "Card Key 4", "Black Key 1", "Black Key 2", "Black Key 3", "Black Key 4", "Star Key", "Palace Key 1", "Palace Key 2", "Palace Key 3", "Palace Key 4", "Palace Key 5", "Palace Key 6", "Palace Key 7", "Palace Key 8", "Palace Key 9", "Palace Key 10", "Palace Key 11", "House Key", "Magical Map", "Contact Lens", "Blimp Ticket", "Train Ticket", "Mailbox SP", "Super Luigi", "Super Luigi 2", "Super Luigi 3", "Super Luigi 4", "Super Luigi 5", "Cookbook", "Moon Stone", "Sun Stone", "Necklace", "Puni Orb", "Champ's Belt", "Poisoned Cake", "Superbombomb", "The Letter \"P\"", "Old Letter", "Chuckola Cola", "Skull Gem", "Gate Handle", "Wedding Ring", "Galley Pot", "Gold Ring", "Shell Earrings", "Autograph", "Ragged Diary", "Blanket", "Vital Paper", "Briefcase", "Goldbob Guide", "Unused Goldbob Guide 1", "Unused Goldbob Guide 2", "Cog", "Data Disk", "Shine Sprite", "Ultra Stone", "Bowser Upgrade Meat", "Mario Wanted Poster", "Special Card", "Platinum Card", "Gold Card", "Silver Card", "Box", "Magical Map (Larger)", "Dubious Paper", "Routing Slip", "Wrestling Magazine", "Present", "Blue Potion", "Red Potion", "Orange Potion", "Green Potion", "???", "Lottery Pick", "Battle Trunks", "Up Arrow", "Package", "Attack FX B", "???", "???", "???", "Diamond Star", "Emerald Star", "Gold Star", "Ruby Star", "Sapphire Star", "Garnet Star", "Crystal Star", "Coin", "Pianta", "Heart Pickup", "Flower Pickup", "Star Piece", "Gold Bar", "Gold Bar x3", "Thunder Bolt", "Thunder Rage", "Shooting Star", "Ice Storm", "Fire Flower", "Earth Quake", "Boo's Sheet", "Volt Shroom", "Repel Cape", "Ruin Powder", "Sleepy Sheep", "POW Block", "Stopwatch", "Dizzy Dial", "Power Punch", "Courage Shell", "HP Sucker", "Trade Off", "Mini Mr. Mini", "Mr. Softener", "Mushroom", "Super Shroom", "Ultra Shroom", "Life Shroom", "Dried Shroom", "Tasty Tonic", "Honey Syrup", "Maple Syrup", "Jammin' Jelly", "Slow Shroom", "Gradual Syrup", "Hot Dog", "Cake", "Point Swap", "Fright Mask", "Mystery", "Inn Coupon", "Whacka Bump", "Coconut", "Dried Bouquet", "Mystic Egg", "Golden Leaf", "Keel Mango", "Fresh Pasta", "Cake Mix", "Hot Sauce", "Turtley Leaf", "Horsetail", "Peachy Peach", "Spite Pouch", "Koopa Curse", "Shroom Fry", "Shroom Roast", "Shroom Steak", "Mistake", "Honey Shroom", "Maple Shroom", "Jelly Shroom", "Honey Super", "Maple Super", "Jelly Super", "Honey Ultra", "Maple Ultra", "Jelly Ultra", "Spicy Soup", "Zess Dinner", "Zess Special", "Zess Deluxe", "Zess Dynamite", "Zess Tea", "Space Food", "Icicle Pop", "Zess Frappe", "Snow Bunny", "Coconut Bomb", "Courage Meal", "Shroom Cake", "Shroom Crepe", "Mousse Cake", "Fried Egg", "Fruit Parfait", "Egg Bomb", "Ink Pasta", "Spaghetti", "Shroom Broth", "Poison Shroom", "Choco Cake", "Mango Delight", "Love Pudding", "Meteor Meal", "Trial Stew", "Couple's Cake", "Inky Sauce", "Omelette Meal", "Koopa Tea", "Koopasta", "Spicy Pasta", "Heartful Cake", "Peach Tart", "Electro Pop", "Fire Pop", "Honey Candy", "Coco Candy", "Jelly Candy", "Zess Cookie", "Healthy Salad", "Koopa Bun", "Fresh Juice", "Audience Can", "Audience Rock", "Audience Bone", "Audience Hammer", "Power Jump", "Multibounce", "Power Bounce", "Tornado Jump", "Shrink Stomp", "Sleepy Stomp", "Soft Stomp", "Power Smash", "Quake Hammer", "Hammer Throw", "Piercing Blow", "Head Rattle", "Fire Drive", "Ice Smash", "Double Dip", "Double Dip P", "Charge", "Charge P", "Super Appeal", "Super Appeal P", "Power Plus", "Power Plus P", "P-Up D-Down", "P-Up D-Down P", "All or Nothing", "All or Nothing P", "Mega Rush", "Mega Rush P", "Power Rush", "Power Rush P", "P-Down D-Up", "P-Down D-Up P", "Last Stand", "Last Stand P", "Defend Plus", "Defend Plus P", "Damage Dodge", "Damage Dodge P", "HP Plus", "HP Plus P", "FP Plus", "Flower Saver", "Flower Saver P", "Ice Power", "Spike Shield", "Feeling Fine", "Feeling Fine P", "Zap Tap", "Double Pain", "Jumpman", "Hammerman", "Return Postage", "Happy Heart", "Happy Heart P", "Happy Flower", "HP Drain", "HP Drain P", "FP Drain", "FP Drain P", "Close Call", "Close Call P", "Pretty Lucky", "Pretty Lucky P", "Lucky Day", "Lucky Day P", "Refund", "Pity Flower", "Pity Flower P", "Quick Change", "Peekaboo", "Timing Tutor", "Heart Finder", "Flower Finder", "Money Money", "Item Hog", "Attack FX R", "Attack FX B", "Attack FX G", "Attack FX Y", "Attack FX P", "Chill Out", "First Attack", "Bump Attack", "Slow Go", "Simplifier", "Unsimplifier", "Lucky Start", "L Emblem", "W Emblem", "Triple Dip", "Lucky Start P", "Debug Badge", "Mega Jump", "Mega Smash", "Mega Quake", "Unused Defend Badge", "Unused Defend Badge P", "Super Charge", "Super Charge P");
            box.getSelectionModel().select((int)(((FieldLocationData.evt_mobj_brick)selected).itemID));

            setItemSelectionComboBox(box);

            fieldFields.add(box);

            ComboBox<String> box2 = new ComboBox<String>();
            box2.getItems().addAll("Empty Brick Block", "Item Brick Block", "Badge Brick Block", "10 Coin Brick Block", "Invisible Item Brick Block", "Invisible Badge Brick Block", "Invisible 10 Coin Brick Block");
            box2.getSelectionModel().select(BBtoIndex((int)(((FieldLocationData.evt_mobj_brick)selected).blockType)));

            fieldFields.add(box2);
        }
        else if(selected instanceof FieldLocationData.evt_mobj_itembox)
        {
            ComboBox<String> box = new ComboBox<String>();
            box.getItems().addAll("Small Chest", "Big Chest", "X-Naut Chest", "Black Chest", "Gold Chest");
            box.getSelectionModel().select((int)(((FieldLocationData.evt_mobj_itembox)selected).chestType));

            fieldFields.add(box);
        }
        else if(selected instanceof FieldLocationData.evt_mobj_kururing_floor)
        {
            ComboBox<String> box = new ComboBox<String>();
            box.getItems().addAll("Nothing", "Strange Sack", "Paper Curse", "Tube Curse", "Plane Curse", "Boat Curse", "Boots", "Super Boots", "Ultra Boots", "Hammer", "Super Hammer", "Ultra Hammer", "Castle Key 1", "Castle Key 2", "Castle Key 3", "Castle Key 4", "Red Key", "Blue Key", "Storage Key 1", "Storage Key 2", "Grotto Key", "Shop Key", "Steeple Key 1", "Steeple Key 2", "Station Key 1", "Station Key 2", "Elevator Key 1", "Elevator Key 2", "Elevator Key 3", "Card Key 1", "Card Key 2", "Card Key 3", "Card Key 4", "Black Key 1", "Black Key 2", "Black Key 3", "Black Key 4", "Star Key", "Palace Key 1", "Palace Key 2", "Palace Key 3", "Palace Key 4", "Palace Key 5", "Palace Key 6", "Palace Key 7", "Palace Key 8", "Palace Key 9", "Palace Key 10", "Palace Key 11", "House Key", "Magical Map", "Contact Lens", "Blimp Ticket", "Train Ticket", "Mailbox SP", "Super Luigi", "Super Luigi 2", "Super Luigi 3", "Super Luigi 4", "Super Luigi 5", "Cookbook", "Moon Stone", "Sun Stone", "Necklace", "Puni Orb", "Champ's Belt", "Poisoned Cake", "Superbombomb", "The Letter \"P\"", "Old Letter", "Chuckola Cola", "Skull Gem", "Gate Handle", "Wedding Ring", "Galley Pot", "Gold Ring", "Shell Earrings", "Autograph", "Ragged Diary", "Blanket", "Vital Paper", "Briefcase", "Goldbob Guide", "Unused Goldbob Guide 1", "Unused Goldbob Guide 2", "Cog", "Data Disk", "Shine Sprite", "Ultra Stone", "Bowser Upgrade Meat", "Mario Wanted Poster", "Special Card", "Platinum Card", "Gold Card", "Silver Card", "Box", "Magical Map (Larger)", "Dubious Paper", "Routing Slip", "Wrestling Magazine", "Present", "Blue Potion", "Red Potion", "Orange Potion", "Green Potion", "???", "Lottery Pick", "Battle Trunks", "Up Arrow", "Package", "Attack FX B", "???", "???", "???", "Diamond Star", "Emerald Star", "Gold Star", "Ruby Star", "Sapphire Star", "Garnet Star", "Crystal Star", "Coin", "Pianta", "Heart Pickup", "Flower Pickup", "Star Piece", "Gold Bar", "Gold Bar x3", "Thunder Bolt", "Thunder Rage", "Shooting Star", "Ice Storm", "Fire Flower", "Earth Quake", "Boo's Sheet", "Volt Shroom", "Repel Cape", "Ruin Powder", "Sleepy Sheep", "POW Block", "Stopwatch", "Dizzy Dial", "Power Punch", "Courage Shell", "HP Sucker", "Trade Off", "Mini Mr. Mini", "Mr. Softener", "Mushroom", "Super Shroom", "Ultra Shroom", "Life Shroom", "Dried Shroom", "Tasty Tonic", "Honey Syrup", "Maple Syrup", "Jammin' Jelly", "Slow Shroom", "Gradual Syrup", "Hot Dog", "Cake", "Point Swap", "Fright Mask", "Mystery", "Inn Coupon", "Whacka Bump", "Coconut", "Dried Bouquet", "Mystic Egg", "Golden Leaf", "Keel Mango", "Fresh Pasta", "Cake Mix", "Hot Sauce", "Turtley Leaf", "Horsetail", "Peachy Peach", "Spite Pouch", "Koopa Curse", "Shroom Fry", "Shroom Roast", "Shroom Steak", "Mistake", "Honey Shroom", "Maple Shroom", "Jelly Shroom", "Honey Super", "Maple Super", "Jelly Super", "Honey Ultra", "Maple Ultra", "Jelly Ultra", "Spicy Soup", "Zess Dinner", "Zess Special", "Zess Deluxe", "Zess Dynamite", "Zess Tea", "Space Food", "Icicle Pop", "Zess Frappe", "Snow Bunny", "Coconut Bomb", "Courage Meal", "Shroom Cake", "Shroom Crepe", "Mousse Cake", "Fried Egg", "Fruit Parfait", "Egg Bomb", "Ink Pasta", "Spaghetti", "Shroom Broth", "Poison Shroom", "Choco Cake", "Mango Delight", "Love Pudding", "Meteor Meal", "Trial Stew", "Couple's Cake", "Inky Sauce", "Omelette Meal", "Koopa Tea", "Koopasta", "Spicy Pasta", "Heartful Cake", "Peach Tart", "Electro Pop", "Fire Pop", "Honey Candy", "Coco Candy", "Jelly Candy", "Zess Cookie", "Healthy Salad", "Koopa Bun", "Fresh Juice", "Audience Can", "Audience Rock", "Audience Bone", "Audience Hammer", "Power Jump", "Multibounce", "Power Bounce", "Tornado Jump", "Shrink Stomp", "Sleepy Stomp", "Soft Stomp", "Power Smash", "Quake Hammer", "Hammer Throw", "Piercing Blow", "Head Rattle", "Fire Drive", "Ice Smash", "Double Dip", "Double Dip P", "Charge", "Charge P", "Super Appeal", "Super Appeal P", "Power Plus", "Power Plus P", "P-Up D-Down", "P-Up D-Down P", "All or Nothing", "All or Nothing P", "Mega Rush", "Mega Rush P", "Power Rush", "Power Rush P", "P-Down D-Up", "P-Down D-Up P", "Last Stand", "Last Stand P", "Defend Plus", "Defend Plus P", "Damage Dodge", "Damage Dodge P", "HP Plus", "HP Plus P", "FP Plus", "Flower Saver", "Flower Saver P", "Ice Power", "Spike Shield", "Feeling Fine", "Feeling Fine P", "Zap Tap", "Double Pain", "Jumpman", "Hammerman", "Return Postage", "Happy Heart", "Happy Heart P", "Happy Flower", "HP Drain", "HP Drain P", "FP Drain", "FP Drain P", "Close Call", "Close Call P", "Pretty Lucky", "Pretty Lucky P", "Lucky Day", "Lucky Day P", "Refund", "Pity Flower", "Pity Flower P", "Quick Change", "Peekaboo", "Timing Tutor", "Heart Finder", "Flower Finder", "Money Money", "Item Hog", "Attack FX R", "Attack FX B", "Attack FX G", "Attack FX Y", "Attack FX P", "Chill Out", "First Attack", "Bump Attack", "Slow Go", "Simplifier", "Unsimplifier", "Lucky Start", "L Emblem", "W Emblem", "Triple Dip", "Lucky Start P", "Debug Badge", "Mega Jump", "Mega Smash", "Mega Quake", "Unused Defend Badge", "Unused Defend Badge P", "Super Charge", "Super Charge P");
            box.getSelectionModel().select((int)(((FieldLocationData.evt_mobj_kururing_floor)selected).itemID));

            setItemSelectionComboBox(box);

            box.setDisable(true); // Fix star piece issue

            fieldFields.add(box);
        }
        else if(selected instanceof FieldLocationData.evt_mobj_recovery_blk)
        {
            TextField costField = new TextField();
            costField.setText(String.valueOf(((FieldLocationData.evt_mobj_recovery_blk)selected).coinCost));
            fieldFields.add(costField);
        }
    }

    /**
     * @Author Jemaroo
     * @Function Turns the value of a Brick Block into it's index
     */
    public static int BBtoIndex(int SA)
    {
        switch(SA)
        {
            case 0: return 0;
            case 1: return 1;
            case 2: return 2;
            case 3: return 3;
            case 11: return 4;
            case 12: return 5;
            case 13: return 6;
            default: return 0;
        }
    }

    /**
     * @Author Jemaroo
     * @Function Turns the value of a index into it's Brick Block
     */
    public static int IndexToBB(int index)
    {
        switch(index)
        {
            case 0: return 0;
            case 1: return 1;
            case 2: return 2;
            case 3: return 3;
            case 4: return 11;
            case 5: return 12;
            case 6: return 13;
            default: return 0;
        }
    }
    
    /**
     * @Author Jemaroo
     * @Function Checks to see if a long value is a LW variable
     */
    public static boolean checkForLW(long value)
    {
        if(value < -29999985L)
        {
            return true;
        }
        else
        {
            return false;
        }
    }
    
    /**
     * @Author Jemaroo
     * @Function Returns the corresponding LW variable name
     */
    public static String getLW(long value)
    {
        long base = -30000000L;
        long index = value - base;

        if (index >= 0 && index <= 15)
        {
            return "LW(" + index + ")";
        }

        return "LW(?)";
    }

    /**
     * @Author Jemaroo
     * @Function Sets up the item selection box
     */
    private void setItemSelectionComboBox(ComboBox<String> box)
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
                    setGraphic(index >= 0 ? fieldImageViewCreator(listImageSelector(index)) : null);
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
                    setGraphic(index >= 0 ? fieldImageViewCreator(listImageSelector(index)) : null);
                }
            }
        });

        box.setConverter(new StringConverter<String>()
        {
            @Override
            public String toString(String object)
            {
                return object;
            }

            @Override
            public String fromString(String string)
            {
                return string;
            }
        });

        box.setEditable(false);

        final StringBuilder typedBuffer = new StringBuilder();
        final long[] lastTypedTime = {0};

        box.setOnKeyTyped(e ->
        {
            long now = System.currentTimeMillis();

            if (now - lastTypedTime[0] > 1000)
            {
                typedBuffer.setLength(0);
            }

            typedBuffer.append(e.getCharacter().toLowerCase());
            lastTypedTime[0] = now;

            String prefix = typedBuffer.toString();

            int matchIndex = -1;
            for (int i = 0; i < box.getItems().size(); i++)
            {
                String item = box.getItems().get(i);
                if (item.toLowerCase().startsWith(prefix))
                {
                    matchIndex = i;
                    break;
                }
            }

            if (matchIndex >= 0)
            {
                box.show();

                final int indexToScroll = matchIndex;

                Platform.runLater(() ->
                {
                    Skin<?> skin = box.getSkin();
                    if (skin instanceof ComboBoxListViewSkin<?> cbSkin)
                    {
                        ListView<?> lv = (ListView<?>) cbSkin.getPopupContent();
                        lv.scrollTo(indexToScroll);
                        lv.getFocusModel().focus(indexToScroll);
                    }
                });
            }

            e.consume();
        });
    }
    
    public static void main(String[] args) 
    {
        launch(args);
    }
}