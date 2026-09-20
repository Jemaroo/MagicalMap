import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javafx.application.Application;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.util.Duration;

public class TETGUI extends Application 
{
    boolean isFileOpened = false;
    Stage window;
    File givenFile;
    private ArrayList<TETData> items = new ArrayList<TETData>();
    private ListView<TETData> itemList = null;
    TextField searchField = new TextField();
    Button saveChangesButton = new Button("Save Text Changes");
    Button openButton = new Button("Open Directory");
    ComboBox<File> fileSelector = new ComboBox<>();
    Button exportButton = new Button("Export File");
    Button closeButton = new Button("Close File");
    Button optionButton = new Button("Options");
    Button aboutButton = new Button("About");
    HBox topMenu = new HBox();
    HBox centerMenu = new HBox();
    HBox upperBox = new HBox();
    BorderPane borderPane = new BorderPane();
    GridPane leftMenuForm = new GridPane();
    Button addIdentifierButton = new Button("Add New Identifier");
    Button shiftUpButton = new Button("↑");
    Button shiftDownButton = new Button("↓");

    ImageView messageWindow = new ImageView();
    StackPane messagePreviewPane = new StackPane();
    Pane previewTextPane = new Pane();
    int currentPreviewPage = 0;
    int lastMessageWindowTagPosition = -1;
    String removedMessageWindowTagNewline = "";
    TextArea messageField = new TextArea();
    ComboBox<String> messageWindowType = new ComboBox<String>();
    ComboBox<String> messageWindowSpecialType = new ComboBox<String>();
    Button previousPage = new Button("Previous Page");
    Button nextPage = new Button("Next Page");
    Label pageDisplay = new Label("1/1");
    Boolean showMessageStar = false;
    ImageView messageStar = new ImageView();

    ColorPicker messageColorPicker = new ColorPicker();
    int selectedColorStart = -1;
    int selectedColorEnd = -1;
    boolean updatingMessageColorPicker = false;
    ComboBox<String> animationNameSelector = new ComboBox<>();
    int selectedAnimationStart = -1;
    int selectedAnimationEnd = -1;
    boolean updatingAnimationNameSelector = false;

    Button kButton = new Button("<k>");
    Button pButton = new Button("<p>");
    Button waitButton = new Button("<wait>");
    Button dkeyButton = new Button("<dkey>");
    Button dynamicButton = new Button("<dynamic>");
    Button waveButton = new Button("<wave>");
    Button shakeButton = new Button("<shake>");
    Button noiseButton = new Button("<noise>");
    Button scaleButton = new Button("<scale>");
    Button colButton = new Button("<col>");
    Button speedButton = new Button("<speed>");
    Button posButton = new Button("<pos>");
    Button iconButton = new Button("<icon>");
    Button animButton = new Button("<anim>");

    Canvas previewModelCanvas = new Canvas(560, 176);

    HashMap<String, Image> images = new HashMap<String, Image>();
    Map<String, Integer> filePriority = new HashMap<String, Integer>();
    Map<Integer, String> previewIcons = new HashMap<Integer, String>();
    Map<String, String> specialPreviewIcons = new HashMap<String, String>();
    Map<String, AnimModelData> previewModels = new HashMap<>();

    boolean darkModeActive = false;

    Font paperMario2Font = Font.loadFont(TETGUI.class.getResourceAsStream("/fonts/PaperMario2.otf"), 18);
    String PM2Style = "-fx-font-family: '" + paperMario2Font.getFamily() + "';" + "-fx-font-size: 16px;";
    String NormalStyle = "-fx-font-family: 'Yu Gothic UI';" + "-fx-font-size: 12px;";

    private final String[] messageWindowTags = {"", "<small>", "<system>", "<housou>", "<tec>", "<diary>", "<kanban>", "<plate>", "<boss>", "<majo>", "<clear>"};
    boolean updatingMessageWindow = false;

    @Override
    public void start(Stage primaryStage) 
    {
        setFilePriority();
        images = GUI.setImages(images);
        setPreviewIcons();
        
        messageWindowType.getItems().addAll("Normal Textbox", "Small Message Box", "System Textbox", "Mechanical Textbox", "TEC Textbox", "Journal Textbox", "Wooden Sign Textbox", "Notice Board Textbox", "Boss Textbox", "Shadow Queen Textbox", "Clear Textbox");
        messageWindowType.getSelectionModel().selectedIndexProperty().addListener((obs, oldIndex, newIndex) -> 
        {
            messageWindowSpecialType.setVisible(newIndex.intValue() == 0);
            messageWindowSpecialType.setManaged(newIndex.intValue() == 0);

            updateSelectedMessageWindowType();
        });
        messageField.textProperty().addListener((obs, oldText, newText) ->
        {
            if (updatingMessageWindow) return;

            int detectedIndex = getMessageWindowIndexFromText(newText);
            if (messageWindowType.getSelectionModel().getSelectedIndex() != detectedIndex)
            {
                updatingMessageWindow = true;
                messageWindowType.getSelectionModel().select(detectedIndex);
                updatingMessageWindow = false;
            }

            updateMessageWindowImage();
            updateMessagePreview();

            saveChangesButton.setText("Save Text Changes");
        });

        messageWindowSpecialType.getItems().addAll("No Effect", "Description Box", "Battle Tooltip");
        messageWindowSpecialType.getSelectionModel().selectFirst();
        messageWindowSpecialType.getSelectionModel().selectedIndexProperty().addListener((obs, oldIndex, newIndex) ->
        {
            if (messageWindowType.getSelectionModel().getSelectedIndex() != 0) return;
            updateMessagePreview();
        });

        previousPage.setOnAction(e ->
        {
            if (currentPreviewPage > 0)
            {
                currentPreviewPage--;
                updateMessagePreview();
            }
        });
        nextPage.setOnAction(e ->
        {
            String[] pages = getPreviewPages(messageField.getText());
            if (currentPreviewPage < pages.length - 1)
            {
                currentPreviewPage++;
                updateMessagePreview();
            }
        });

        initializeFormattingButtons();
        initializeMessagePreview();

        messageField.addEventFilter(MouseEvent.MOUSE_CLICKED, event ->
        {
            String text = messageField.getText();
            if (text == null || text.isEmpty())
            {
                selectedColorStart = -1;
                selectedColorEnd = -1;

                messageColorPicker.setVisible(false);
                messageColorPicker.setManaged(false);

                colButton.setVisible(true);
                colButton.setManaged(true);

                return;
            }

            int caretPosition = messageField.getCaretPosition();

            Pattern pattern = Pattern.compile("<col\\s+([0-9a-fA-F]{6,8})\\s*>");
            Matcher matcher = pattern.matcher(text);

            boolean foundColorTag = false;

            while (matcher.find())
            {
                int colorStart = matcher.start(1);
                int colorEnd = matcher.end(1);

                if (caretPosition >= colorStart && caretPosition <= colorEnd)
                {
                    foundColorTag = true;

                    String colorText = matcher.group(1);

                    selectedColorStart = colorStart;
                    selectedColorEnd = colorEnd;

                    messageField.selectRange(colorStart, colorEnd);

                    Color startingColor = Color.BLACK;

                    try
                    {
                        int red = Integer.parseInt(colorText.substring(0, 2), 16);
                        int green = Integer.parseInt(colorText.substring(2, 4), 16);
                        int blue = Integer.parseInt(colorText.substring(4, 6), 16);

                        double alpha = 1.0;
                        if (colorText.length() >= 8)
                        {
                            alpha = Integer.parseInt(colorText.substring(6, 8), 16) / 255.0;
                        }

                        startingColor = Color.rgb(red, green, blue, alpha);
                    }
                    catch (Exception ex)
                    {
                        startingColor = Color.BLACK;
                    }

                    updatingMessageColorPicker = true;
                    messageColorPicker.setValue(startingColor);
                    updatingMessageColorPicker = false;

                    colButton.setVisible(false);
                    colButton.setManaged(false);

                    messageColorPicker.setVisible(true);
                    messageColorPicker.setManaged(true);

                    break;
                }
            }

            Pattern animPattern = Pattern.compile("<anim\\s+(\\S+)\\s+(\\S+)\\s+[^>]*>");
            Matcher animMatcher = animPattern.matcher(text);

            boolean foundAnimationName = false;

            while (animMatcher.find())
            {
                int animationStart = animMatcher.start(2);
                int animationEnd = animMatcher.end(2);

                if (caretPosition >= animationStart && caretPosition <= animationEnd)
                {
                    foundAnimationName = true;

                    String modelName = animMatcher.group(1);
                    selectedAnimationStart = animationStart;
                    selectedAnimationEnd = animationEnd;

                    messageField.selectRange(animationStart, animationEnd);

                    animationNameSelector.getItems().clear();

                    try
                    {
                        AnimModelData model = previewModels.get(modelName);

                        if (model == null)
                        {
                            model = loadPreviewModel(modelName);
                            previewModels.put(modelName, model);
                        }

                        ArrayList<String> animationNames = new ArrayList<>(model.animations.keySet());
                        animationNameSelector.getItems().addAll(animationNames);

                        updatingAnimationNameSelector = true;
                        animationNameSelector.getSelectionModel().select(animMatcher.group(2));
                        updatingAnimationNameSelector = false;

                        animButton.setVisible(false);
                        animButton.setManaged(false);

                        animationNameSelector.setVisible(true);
                        animationNameSelector.setManaged(true);
                    }
                    catch (Exception ex)
                    {
                        selectedAnimationStart = -1;
                        selectedAnimationEnd = -1;

                        animationNameSelector.setVisible(false);
                        animationNameSelector.setManaged(false);

                        animButton.setVisible(true);
                        animButton.setManaged(true);
                    }

                    break;
                }
            }

            if (!foundColorTag)
            {
                selectedColorStart = -1;
                selectedColorEnd = -1;

                messageColorPicker.setVisible(false);
                messageColorPicker.setManaged(false);

                colButton.setVisible(true);
                colButton.setManaged(true);
            }

            if (!foundAnimationName)
            {
                selectedAnimationStart = -1;
                selectedAnimationEnd = -1;

                animationNameSelector.setVisible(false);
                animationNameSelector.setManaged(false);

                animButton.setVisible(true);
                animButton.setManaged(true);
            }
        });

        messageColorPicker.setOnAction(event ->
        {
            if (updatingMessageColorPicker) return;
            if (selectedColorStart < 0 || selectedColorEnd < 0) return;

            String currentText = messageField.getText();
            if (currentText == null) return;
            if (selectedColorStart >= currentText.length()) return;
            if (selectedColorEnd > currentText.length()) return;

            String oldHexColor = currentText.substring(selectedColorStart, selectedColorEnd);
            boolean includeAlpha = oldHexColor.length() >= 8;

            Color newColor = messageColorPicker.getValue();

            int red = (int)Math.round(newColor.getRed() * 255.0);
            int green = (int)Math.round(newColor.getGreen() * 255.0);
            int blue = (int)Math.round(newColor.getBlue() * 255.0);
            int alpha = (int)Math.round(newColor.getOpacity() * 255.0);

            String newHexColor;

            if (includeAlpha)
            {
                newHexColor = String.format("%02X%02X%02X%02X", red, green, blue, alpha);
            }
            else
            {
                newHexColor = String.format("%02X%02X%02X", red, green, blue);
            }

            messageField.replaceText(selectedColorStart, selectedColorEnd, newHexColor);
            messageField.selectRange(selectedColorStart, selectedColorStart + newHexColor.length());

            selectedColorEnd = selectedColorStart + newHexColor.length();

            updateMessagePreview();
            saveChangesButton.setText("Save Text Changes");

            colButton.setVisible(true);
            colButton.setManaged(true);

            messageColorPicker.setVisible(false);
            messageColorPicker.setManaged(false);

            selectedColorStart = -1;
            selectedColorEnd = -1;

            messageField.requestFocus();
        });

        animationNameSelector.setOnAction(event ->
        {
            if (updatingAnimationNameSelector) return;
            if (selectedAnimationStart < 0 || selectedAnimationEnd < 0) return;

            String newAnimationName = animationNameSelector.getSelectionModel().getSelectedItem();
            if (newAnimationName == null) return;

            String currentText = messageField.getText();
            if (currentText == null) return;
            if (selectedAnimationStart >= currentText.length()) return;
            if (selectedAnimationEnd > currentText.length()) return;

            messageField.replaceText(selectedAnimationStart, selectedAnimationEnd, newAnimationName);
            messageField.selectRange(selectedAnimationStart, selectedAnimationStart + newAnimationName.length());

            selectedAnimationEnd = selectedAnimationStart + newAnimationName.length();

            updateMessagePreview();
            saveChangesButton.setText("Save Text Changes");

            animButton.setVisible(true);
            animButton.setManaged(true);

            animationNameSelector.setVisible(false);
            animationNameSelector.setManaged(false);

            selectedAnimationStart = -1;
            selectedAnimationEnd = -1;

            messageField.requestFocus();
        });

        messageColorPicker.setOnHidden(event ->
        {
            PauseTransition delay = new PauseTransition(Duration.millis(0));

            delay.setOnFinished(e2 ->
            {
                if (window == null) return;

                window.setIconified(false);
                window.setAlwaysOnTop(true);
                window.toFront();
                window.requestFocus();

                Platform.runLater(() -> window.setAlwaysOnTop(false));
            });

            delay.play();
        });

        //Window
        window = primaryStage;
        window.setTitle("Text Editor Tool");

        //Menu Buttons
        topMenu.getChildren().addAll(openButton, optionButton, aboutButton);
        topMenu.setPadding(new Insets(5));
        topMenu.setSpacing(5);

        //Alligning Menu Buttons to Top
        borderPane.setTop(topMenu);
        fileSelector.setMaxWidth(270);

        //Scene
        Scene emptyScene = new Scene(borderPane, 855, 600);
        window.setScene(emptyScene);

        String startPath = "";
        try
        {
            File jsonFile = new File("src\\options.json");
            JSONParser parser = new JSONParser();
            JSONObject root = (JSONObject)parser.parse(new FileReader(jsonFile));

            if(((String)root.get("TETLastFolder")).equals("true")) startPath = (String)root.get("startPath");
            if(((String)root.get("TETDarkMode")).equals("true")) setDarkStyle(emptyScene, true);
            if(((String)root.get("TETPM2Font")).equals("true")) messageField.setStyle(PM2Style);
            else messageField.setStyle(NormalStyle);
            if(((String)root.get("TETMessageStar")).equals("true")) showMessageStar = true;
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
                if(givenFile == null) return;

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

                    byte[] newFileData = TETMain.buildNewFile(items);
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setInitialFileName(fileSelector.getSelectionModel().getSelectedItem().getName());
                    FileChooser.ExtensionFilter txtFilter = new FileChooser.ExtensionFilter("Text File (*.txt)", "*.txt");
                    fileChooser.getExtensionFilters().addAll(txtFilter);
                    fileChooser.setTitle("Save As");
                    fileChooser.setInitialDirectory(fileSelector.getSelectionModel().getSelectedItem().getParentFile());

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
                            successBox.getIcons().add(images.get("textBubble"));

                            VBox successMenu = new VBox();
                            Text message = new Text("Successfully Saved!");
                            message.setWrappingWidth(290);
                            message.setTextAlignment(TextAlignment.CENTER);
                            successMenu.getChildren().addAll(new Label(""), message);

                            StackPane successPane = new StackPane();
                            successPane.getChildren().add(successMenu);
                            successPane.setAlignment(Pos.CENTER);

                            Scene successScene = new Scene(successPane, 150, 50);
                            if(darkModeActive) setDarkStyle(successScene, true);

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
                optionsBox.getIcons().add(images.get("cog"));

                CheckBox lastFolderBox = new CheckBox();
                CheckBox PM2TextBox = new CheckBox();
                CheckBox messageStarBox = new CheckBox();
                CheckBox darkModeBox = new CheckBox();
                Button saveOptionsButton = new Button("Save Options");

                try
                {
                    File jsonFile = new File("src\\options.json");
                    JSONParser parser = new JSONParser();
                    JSONObject root = (JSONObject)parser.parse(new FileReader(jsonFile));

                    if(((String)root.get("TETLastFolder")).equals("true")) lastFolderBox.setSelected(true); else lastFolderBox.setSelected(false);
                    if(((String)root.get("TETPM2Font")).equals("true")) PM2TextBox.setSelected(true); else PM2TextBox.setSelected(false);
                    if(((String)root.get("TETMessageStar")).equals("true")) messageStarBox.setSelected(true); else messageStarBox.setSelected(false);
                    if(((String)root.get("TETDarkMode")).equals("true")) darkModeBox.setSelected(true); else darkModeBox.setSelected(false);
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
                optionsForm.add(new Label("Use Paper Mario 2 Font"), 1, 1);
                optionsForm.add(PM2TextBox, 2, 1);

                optionsForm.add(unitImageViewCreator(images.get("cog")), 0, 2);
                optionsForm.add(new Label("Show Message Star"), 1, 2);
                optionsForm.add(messageStarBox, 2, 2);

                optionsForm.add(unitImageViewCreator(images.get("cog")), 0, 3);
                optionsForm.add(new Label("Dark Mode"), 1, 3);
                optionsForm.add(darkModeBox, 2, 3);

                VBox optionsVBox = new VBox();
                optionsVBox.setAlignment(Pos.CENTER);
                optionsVBox.setSpacing(10);
                optionsVBox.getChildren().addAll(optionsForm, saveOptionsButton);

                StackPane optionsPane = new StackPane();
                optionsPane.getChildren().add(optionsVBox);
                optionsPane.setAlignment(Pos.CENTER);

                Scene optionsScene = new Scene(optionsPane, 250, 200);
                if(darkModeActive) setDarkStyle(optionsScene, true);

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

                            if(lastFolderBox.isSelected()) root.put("TETLastFolder", "true");
                            else root.put("TETLastFolder", "false");

                            if(PM2TextBox.isSelected()) {root.put("TETPM2Font", "true"); messageField.setStyle(PM2Style);}
                            else {root.put("TETPM2Font", "false"); messageField.setStyle(NormalStyle);}

                            if(messageStarBox.isSelected()) {root.put("TETMessageStar", "true"); showMessageStar = true; updateMessagePreview();}
                            else {root.put("TETMessageStar", "false"); showMessageStar = false; updateMessagePreview();}

                            if(darkModeBox.isSelected()) root.put("TETDarkMode", "true");
                            else root.put("TETDarkMode", "false");

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

                        if(darkModeBox.isSelected()) {setDarkStyle(emptyScene, true); darkModeActive = true;}
                        else {setDarkStyle(emptyScene, false); darkModeActive = false;}

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
                alertBox.getIcons().add(images.get("textBubble"));
                alertBox.initModality(Modality.APPLICATION_MODAL);
                alertBox.setResizable(false);

                ImageView logo = new ImageView(images.get("magicalMapLogo"));
                logo.setFitWidth(100);
                logo.setFitHeight(100);

                Label versionLabel = new Label("Magical Map Version: " + GUI.version);
                versionLabel.setMaxWidth(Double.MAX_VALUE);
                versionLabel.setAlignment(Pos.CENTER);
                versionLabel.setTextAlignment(TextAlignment.CENTER);

                Label creditLabel = new Label("Text Editor Tool Written by Jemaroo");
                creditLabel.setAlignment(Pos.CENTER);
                creditLabel.setTextAlignment(TextAlignment.CENTER);

                VBox informationBox = new VBox(6, versionLabel, creditLabel);
                informationBox.setAlignment(Pos.CENTER);

                HBox headerBox = new HBox(15, logo, informationBox);
                headerBox.setAlignment(Pos.CENTER);

                Label descriptionLabel = new Label("Text Editor Tool allows you to open up the game's text files and edit the text fields with a live preview.");
                descriptionLabel.setWrapText(true);
                descriptionLabel.setMaxWidth(300);
                descriptionLabel.setAlignment(Pos.CENTER);
                descriptionLabel.setTextAlignment(TextAlignment.CENTER);

                VBox alertMenu = new VBox(15, headerBox, descriptionLabel);
                alertMenu.setAlignment(Pos.CENTER);
                alertMenu.setPadding(new Insets(15, 20, 15, 20));

                Scene alertScene = new Scene(alertMenu);

                if(darkModeActive) setDarkStyle(alertScene, true);

                alertBox.setScene(alertScene);
                alertBox.show();
            }
        });

        window.getIcons().add(images.get("textBubble"));
        window.show();
    }

    /**
     * @Author Jemaroo
     * @Function Opens the directory and sets up the correct fields
     */
    private void loadGUIMenus()
    {
        ArrayList<File> validFiles = TETMain.findMatchingFiles(givenFile);

        fileSelector.getItems().clear();
        topMenu.getChildren().clear();
        topMenu.getChildren().addAll(openButton, fileSelector, exportButton, closeButton, optionButton, aboutButton);

        validFiles.sort((fileA, fileB) -> 
        {
            Integer fileAPriority = getFilePriority(fileA);
            Integer fileBPriority = getFilePriority(fileB);

            if (fileAPriority != null && fileBPriority != null)
            {
                return Integer.compare(fileAPriority, fileBPriority);
            }

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
                    String[] tempSplit = item.getName().split("_");
                    if(tempSplit.length > 1) 
                    {
                        String[] tempSplit2 = tempSplit[1].split("\\.");
                        if(tempSplit2.length > 0) {setText(fileNameSelector(item.getName()) + " " + tempSplit2[0]);}
                        else setText(fileNameSelector(item.getName()) + " " + tempSplit[1]);
                    }
                    else setText(item.getName());
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
            if(fileSelector.getSelectionModel().getSelectedItem() == null) return;
            centerMenu.getChildren().clear();

            items = TETMain.getTextData(fileSelector.getSelectionModel().getSelectedItem());
            isFileOpened = true;

            itemList = new ListView<>();
            itemList.setEditable(true);
            itemList.setCellFactory(lv -> new ListCell<TETData>() 
            {
                private final TextField textField = new TextField();
                {
                    textField.setOnAction(e -> commitEdit(getItem()));
                    textField.focusedProperty().addListener((obs, wasFocused, isFocused) -> 
                    {
                        if (!isFocused && isEditing()) 
                        {
                            commitEdit(getItem());
                        }
                    });
                }

                @Override
                public void startEdit() 
                {
                    super.startEdit();

                    TETData item = getItem();
                    if (item == null) return;

                    textField.setText(item.identifier);
                    setText(null);
                    setGraphic(textField);

                    textField.requestFocus();
                    textField.selectAll();
                }

                @Override
                public void cancelEdit() 
                {
                    super.cancelEdit();

                    TETData item = getItem();

                    setGraphic(null);
                    setText(item == null ? null : item.identifier);
                }

                @Override
                public void commitEdit(TETData item) 
                {
                    if (item != null) 
                    {
                        item.identifier = textField.getText();
                    }

                    super.commitEdit(item);

                    setGraphic(null);
                    setText(item == null ? null : item.identifier);
                }

                @Override
                protected void updateItem(TETData item, boolean empty) 
                {
                    super.updateItem(item, empty);

                    if (empty || item == null) 
                    {
                        setText(null);
                        setGraphic(null);
                    } 
                    else if (isEditing()) 
                    {
                        textField.setText(item.identifier);
                        setText(null);
                        setGraphic(textField);
                    } 
                    else 
                    {
                        setStyle(NormalStyle);
                        setText(item.identifier);
                        setGraphic(null);
                    }
                }
            });

            GridPane leftMenuForm = new GridPane();
            upperBox.setPadding(new Insets(5));
            upperBox.setSpacing(5);
            searchField.setPromptText("Search...");
            upperBox.getChildren().clear();
            upperBox.getChildren().add(searchField);
            leftMenuForm.add(upperBox, 0, 0);

            HBox baseBox = new HBox();
            baseBox.setPadding(new Insets(3));
            baseBox.getChildren().addAll(new Label("Identifiers:", fieldImageViewCreator(images.get("textBubble"))));
            leftMenuForm.add(baseBox, 0, 1);
            leftMenuForm.add(itemList, 0, 2);

            HBox listButtonsBox = new HBox();
            HBox.setHgrow(addIdentifierButton, Priority.ALWAYS);
            listButtonsBox.getChildren().addAll(addIdentifierButton, shiftUpButton, shiftDownButton);
            addIdentifierButton.setMaxWidth(Double.MAX_VALUE);
            shiftUpButton.setMinSize(27, 27);
            shiftDownButton.setMinSize(27, 27);
            leftMenuForm.add(listButtonsBox, 0, 3);
            GridPane.setHgrow(listButtonsBox, Priority.ALWAYS);

            RowConstraints growingRow = new RowConstraints();
            growingRow.setVgrow(Priority.ALWAYS);
            leftMenuForm.getRowConstraints().addAll(new RowConstraints(), new RowConstraints(), growingRow);

            // Make itemList fill the cell
            GridPane.setVgrow(itemList, Priority.ALWAYS);
            
            borderPane.setLeft(leftMenuForm);

            GridPane form = new GridPane();

            ObservableList<TETData> observableItems = FXCollections.observableArrayList(items);
            FilteredList<TETData> filteredItems = new FilteredList<TETData>(observableItems, p -> true);
            itemList.setItems(filteredItems);

            Runnable updateShiftButtons = () ->
            {
                shiftUpButton.setDisable(itemList.getSelectionModel().getSelectedIndex() <= 0);
                shiftDownButton.setDisable(itemList.getSelectionModel().getSelectedIndex() < 0 || itemList.getSelectionModel().getSelectedIndex() >= filteredItems.size() - 1);
            };
            itemList.getSelectionModel().selectedIndexProperty().addListener((obs, oldIndex, newIndex) -> updateShiftButtons.run());
            filteredItems.addListener((javafx.collections.ListChangeListener<TETData>)change -> updateShiftButtons.run());
            updateShiftButtons.run();

            itemList.setOnEditCommit(event -> 
            {
                TETData item = event.getNewValue();

                if (item != null) 
                {
                    itemList.refresh();
                }
            });

            searchField.textProperty().addListener((obs, oldValue, newValue) -> 
            {
                String filter = newValue.toLowerCase();
                filteredItems.setPredicate(item -> 
                {
                    if (filter == null || filter.isEmpty()) 
                    {
                        return true;
                    }
                    return item.identifier.toLowerCase().contains(filter) || item.textData.toLowerCase().contains(filter);
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

            addIdentifierButton.setOnAction(new EventHandler<ActionEvent>() 
            {
                @Override public void handle(ActionEvent event)
                {
                    TETData selected = itemList.getSelectionModel().getSelectedItem();
                    TETData newItem = new TETData("New Identifier", "");
                    int insertIndex = selected == null ? items.size() : items.indexOf(selected) + 1;

                    items.add(insertIndex, newItem);
                    searchField.clear();
                    observableItems.setAll(items);

                    itemList.getSelectionModel().select(newItem);
                    itemList.getFocusModel().focus(itemList.getSelectionModel().getSelectedIndex());
                }
            });

            shiftUpButton.setOnAction(new EventHandler<ActionEvent>() 
            {
                @Override public void handle(ActionEvent event)
                {
                    TETData selected = itemList.getSelectionModel().getSelectedItem();
                    int visibleIndex = filteredItems.indexOf(selected);
                    ScrollBar verticalScrollBar = (ScrollBar)itemList.lookup(".scroll-bar:vertical");
                    double scrollValue = verticalScrollBar == null ? 0 : verticalScrollBar.getValue();
                    TETData previousItem = filteredItems.get(visibleIndex - 1);

                    items.remove(selected);
                    items.add(items.indexOf(previousItem), selected);
                    observableItems.setAll(items);

                    itemList.getSelectionModel().select(selected);
                    itemList.getFocusModel().focus(filteredItems.indexOf(selected));

                    Platform.runLater(() -> {if (verticalScrollBar != null) verticalScrollBar.setValue(scrollValue);});
                }
            });

            shiftDownButton.setOnAction(new EventHandler<ActionEvent>() 
            {
                @Override public void handle(ActionEvent event)
                {
                    TETData selected = itemList.getSelectionModel().getSelectedItem();
                    int visibleIndex = filteredItems.indexOf(selected);
                    ScrollBar verticalScrollBar = (ScrollBar)itemList.lookup(".scroll-bar:vertical");
                    double scrollValue = verticalScrollBar == null ? 0 : verticalScrollBar.getValue();
                    TETData nextItem = filteredItems.get(visibleIndex + 1);

                    items.remove(selected);
                    items.add(items.indexOf(nextItem) + 1, selected);
                    observableItems.setAll(items);

                    itemList.getSelectionModel().select(selected);
                    itemList.getFocusModel().focus(filteredItems.indexOf(selected));

                    Platform.runLater(() -> {if (verticalScrollBar != null) verticalScrollBar.setValue(scrollValue);});
                }
            });

            itemList.setOnMouseClicked(e2 -> 
            {
                TETData selected = itemList.getSelectionModel().getSelectedItem();

                if (selected == null) return;

                if (e2.getClickCount() == 2) 
                {
                    itemList.edit(itemList.getSelectionModel().getSelectedIndex());
                    return;
                }
    
                centerMenu.getChildren().clear();
                form.getChildren().clear();

                upperBox.getChildren().clear();
                upperBox.getChildren().addAll(searchField, saveChangesButton);

                if (selected != null) loadStructFields(selected);

                saveChangesButton.setText("Save Text Changes");

                form.setVgap(5);
                form.setHgap(10);
                form.setPadding(new Insets(10));

                updateMessageWindowImage();

                GridPane.setHalignment(messagePreviewPane, HPos.CENTER);
                GridPane.setValignment(messagePreviewPane, VPos.CENTER);
                form.add(messagePreviewPane, 0, 0);

                HBox messageWindowButtons = new HBox();
                messageWindowButtons.setSpacing(5);
                messageWindowButtons.setAlignment(Pos.CENTER);
                messageWindowButtons.getChildren().addAll(messageWindowSpecialType, messageWindowType, previousPage, nextPage, pageDisplay);

                HBox textWindowButtons = new HBox();
                textWindowButtons.setSpacing(5);
                textWindowButtons.setAlignment(Pos.CENTER);
                textWindowButtons.getChildren().addAll(pButton, kButton, waitButton, dkeyButton, dynamicButton, waveButton, shakeButton, noiseButton);
                HBox textWindowButtons2 = new HBox();
                textWindowButtons2.setSpacing(5);
                textWindowButtons2.setAlignment(Pos.CENTER);
                textWindowButtons2.getChildren().addAll(scaleButton, colButton, messageColorPicker, speedButton, posButton, iconButton, animButton, animationNameSelector);
                colButton.setVisible(true);
                colButton.setManaged(true);
                animButton.setVisible(true);
                animButton.setManaged(true);
                messageColorPicker.setVisible(false);
                messageColorPicker.setManaged(false);
                animationNameSelector.setVisible(false);
                animationNameSelector.setManaged(false);

                VBox buttonsBox = new VBox();
                buttonsBox.setSpacing(5);
                buttonsBox.setAlignment(Pos.CENTER);
                buttonsBox.getChildren().addAll(messageWindowButtons, textWindowButtons, textWindowButtons2);
                form.add(buttonsBox, 0, 1);

                messageField.setPrefWidth(Region.USE_COMPUTED_SIZE);
                messageField.setPrefHeight(Region.USE_COMPUTED_SIZE);
                messageField.setMaxWidth(Double.MAX_VALUE);
                messageField.setMaxHeight(Double.MAX_VALUE);
                GridPane.setHgrow(messageField, Priority.ALWAYS);
                GridPane.setVgrow(messageField, Priority.ALWAYS);
                ColumnConstraints col = new ColumnConstraints();
                col.setHgrow(Priority.ALWAYS);
                col.setFillWidth(true);
                form.getColumnConstraints().setAll(col);
                RowConstraints imageRow = new RowConstraints();
                imageRow.setVgrow(Priority.NEVER);
                RowConstraints buttonsRow = new RowConstraints();
                buttonsRow.setVgrow(Priority.NEVER);
                RowConstraints textAreaRow = new RowConstraints();
                textAreaRow.setVgrow(Priority.ALWAYS);
                textAreaRow.setFillHeight(true);
                form.getRowConstraints().setAll(imageRow, buttonsRow, textAreaRow);
                form.add(messageField, 0, 2);

                form.setMaxWidth(Double.MAX_VALUE);
                form.setMaxHeight(Double.MAX_VALUE);
                centerMenu.setMaxWidth(Double.MAX_VALUE);
                centerMenu.setMaxHeight(Double.MAX_VALUE);
                VBox.setVgrow(form, Priority.ALWAYS);
                HBox.setHgrow(form, Priority.ALWAYS);
                centerMenu.getChildren().add(form);
                borderPane.setCenter(centerMenu);
            });

            itemList.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, event ->
            {
                if (event.getCode() != KeyCode.ENTER) return;
                if (itemList.getEditingIndex() >= 0) return;

                TETData selected = itemList.getSelectionModel().getSelectedItem();
                if (selected == null) return;

                centerMenu.getChildren().clear();
                form.getChildren().clear();
                upperBox.getChildren().clear();
                upperBox.getChildren().addAll(searchField, saveChangesButton);

                loadStructFields(selected);

                saveChangesButton.setText("Save Text Changes");

                form.setVgap(5);
                form.setHgap(10);
                form.setPadding(new Insets(10));

                updateMessageWindowImage();

                GridPane.setHalignment(messagePreviewPane, HPos.CENTER);
                GridPane.setValignment(messagePreviewPane, VPos.CENTER);
                form.add(messagePreviewPane, 0, 0);

                HBox messageWindowButtons = new HBox();
                messageWindowButtons.setSpacing(5);
                messageWindowButtons.setAlignment(Pos.CENTER);
                messageWindowButtons.getChildren().addAll(messageWindowSpecialType, messageWindowType, previousPage, nextPage, pageDisplay);
                HBox textWindowButtons = new HBox();
                textWindowButtons.setSpacing(5);
                textWindowButtons.setAlignment(Pos.CENTER);
                textWindowButtons.getChildren().addAll(pButton, kButton, waitButton, dkeyButton, dynamicButton, waveButton, shakeButton, noiseButton);
                HBox textWindowButtons2 = new HBox();
                textWindowButtons2.setSpacing(5);
                textWindowButtons2.setAlignment(Pos.CENTER);
                textWindowButtons2.getChildren().addAll(scaleButton, colButton, messageColorPicker, speedButton, posButton, iconButton, animButton);
                colButton.setVisible(true);
                colButton.setManaged(true);
                messageColorPicker.setVisible(false);
                messageColorPicker.setManaged(false);

                VBox buttonsBox = new VBox();
                buttonsBox.setSpacing(5);
                buttonsBox.setAlignment(Pos.CENTER);
                buttonsBox.getChildren().addAll(messageWindowButtons, textWindowButtons, textWindowButtons2);
                form.add(buttonsBox, 0, 1);

                messageField.setPrefWidth(Region.USE_COMPUTED_SIZE);
                messageField.setPrefHeight(Region.USE_COMPUTED_SIZE);
                messageField.setMaxWidth(Double.MAX_VALUE);
                messageField.setMaxHeight(Double.MAX_VALUE);
                GridPane.setHgrow(messageField, Priority.ALWAYS);
                GridPane.setVgrow(messageField, Priority.ALWAYS);
                ColumnConstraints col = new ColumnConstraints();
                col.setHgrow(Priority.ALWAYS);
                col.setFillWidth(true);
                form.getColumnConstraints().setAll(col);

                RowConstraints imageRow = new RowConstraints();
                imageRow.setVgrow(Priority.NEVER);
                RowConstraints buttonsRow = new RowConstraints();
                buttonsRow.setVgrow(Priority.NEVER);
                RowConstraints textAreaRow = new RowConstraints();
                textAreaRow.setVgrow(Priority.ALWAYS);
                textAreaRow.setFillHeight(true);
                form.getRowConstraints().setAll(imageRow, buttonsRow, textAreaRow);
                form.add(messageField, 0, 2);

                form.setMaxWidth(Double.MAX_VALUE);
                form.setMaxHeight(Double.MAX_VALUE);
                centerMenu.setMaxWidth(Double.MAX_VALUE);
                centerMenu.setMaxHeight(Double.MAX_VALUE);

                VBox.setVgrow(form, Priority.ALWAYS);
                HBox.setHgrow(form, Priority.ALWAYS);

                centerMenu.getChildren().add(form);
                borderPane.setCenter(centerMenu);

                event.consume();
            });
        });
    }

    /**
     * @Author Jemaroo
     * @Function Loads Text Data into text field
     */
    private void loadStructFields(TETData selected) 
    {
        updatingMessageWindow = true;
        messageField.setText(selected.textData);

        lastMessageWindowTagPosition = -1;
        removedMessageWindowTagNewline = "";

        for (String tag : messageWindowTags)
        {
            if (tag.isEmpty()) continue;

            int position = selected.textData.indexOf(tag);

            if (position != -1)
            {
                lastMessageWindowTagPosition = position;
                break;
            }
        }

        if (selected.textData.contains("<small>")) messageWindowType.getSelectionModel().select(1);
        else if (selected.textData.contains("<system>")) messageWindowType.getSelectionModel().select(2);
        else if (selected.textData.contains("<housou>")) messageWindowType.getSelectionModel().select(3);
        else if (selected.textData.contains("<tec>")) messageWindowType.getSelectionModel().select(4);
        else if (selected.textData.contains("<diary>")) messageWindowType.getSelectionModel().select(5);
        else if (selected.textData.contains("<kanban>")) messageWindowType.getSelectionModel().select(6);
        else if (selected.textData.contains("<plate>")) messageWindowType.getSelectionModel().select(7);
        else if (selected.textData.contains("<boss>")) messageWindowType.getSelectionModel().select(8);
        else if (selected.textData.contains("<majo>")) messageWindowType.getSelectionModel().select(9);
        else if (selected.textData.contains("<clear>")) messageWindowType.getSelectionModel().select(10);
        else 
        {    
            messageWindowType.getSelectionModel().select(0);
            messageWindowSpecialType.getSelectionModel().selectFirst();
        }

        currentPreviewPage = 0;
        updateMessagePreview();
        updateMessageWindowImage();
        updatingMessageWindow = false;
    }

    /**
     * @Author Jemaroo
     * @Function Saves the text fields to the loaded ItemData Data
     */
    private void saveFieldsToSelectedStruct() 
    {
        Object selected = null;
        selected = itemList.getSelectionModel().getSelectedItem();

        if (selected instanceof TETData b) 
        {
            b.textData = messageField.getText();
        }
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
     * @Function Adds every file priority to the hashmap
     */
    public void setFilePriority()
    {
        filePriority.put("global.txt", 1);

        filePriority.put("aaa_", 3);
        filePriority.put("gor_", 4);
        filePriority.put("tik_", 5);
        filePriority.put("hei_", 6);
        filePriority.put("nok_", 7);
        filePriority.put("gon_", 8);
        filePriority.put("win_", 9);
        filePriority.put("mri_", 10);
        filePriority.put("tou_", 11);
        filePriority.put("tou2_", 12);
        filePriority.put("usu_", 13);
        filePriority.put("gra_", 14);
        filePriority.put("jin_", 15);
        filePriority.put("muj_", 16);
        filePriority.put("dou_", 17);
        filePriority.put("rsh_", 18);
        filePriority.put("hom_", 19);
        filePriority.put("eki_", 20);
        filePriority.put("pik_", 21);
        filePriority.put("bom_", 22);
        filePriority.put("moo_", 23);
        filePriority.put("aji_", 24);
        filePriority.put("las_", 25);
        filePriority.put("jon_", 26);
    }

    /**
     * @Author Jemaroo
     * @Function Searches the filePriority hashmap for the file and returns its priority
     */
    private Integer getFilePriority(File file)
    {
        String fileName = file.getName().toLowerCase();

        for (Map.Entry<String, Integer> entry : filePriority.entrySet())
        {
            String key = entry.getKey().toLowerCase();

            if (fileName.startsWith(key))
            {
                return entry.getValue();
            }
        }

        return null;
    }

    /**
     * @Author Jemaroo
     * @Function Returns the area name based on file name
     */
    public String fileNameSelector(String name)
    {
        if(name.startsWith("global")) return "Global";
        else if(name.startsWith("aaa_")) return "Mario's House";
        else if(name.startsWith("gor_")) return "Rogueport";
        else if(name.startsWith("tik_")) return "Rogueport Sewers";
        else if(name.startsWith("hei_")) return "Petal Meadows";
        else if(name.startsWith("nok_")) return "Petalburg";
        else if(name.startsWith("gon_")) return "Hooktail Castle";
        else if(name.startsWith("win_")) return "Boggly Woods";
        else if(name.startsWith("mri_")) return "Boggly Tree";
        else if(name.startsWith("tou_")) return "Glitzville";
        else if(name.startsWith("tou2_")) return "Glitz Pit";
        else if(name.startsWith("usu_")) return "Twilight Town";
        else if(name.startsWith("gra_")) return "Twilight Trail";
        else if(name.startsWith("jin_")) return "Creepy Steeple";
        else if(name.startsWith("muj_")) return "Keelhaul Key";
        else if(name.startsWith("dou_")) return "Pirate's Grotto";
        else if(name.startsWith("rsh_")) return "Excess Express";
        else if(name.startsWith("hom_")) return "Train Cutscenes";
        else if(name.startsWith("eki_")) return "Riverside Station";
        else if(name.startsWith("pik_")) return "Poshley Heights";
        else if(name.startsWith("bom_")) return "Fahr Outpost";
        else if(name.startsWith("moo_")) return "The Moon";
        else if(name.startsWith("aji_")) return "X-Naut Fortress";
        else if(name.startsWith("las_")) return "Palace of Shadow";
        else if(name.startsWith("jon_")) return "Pit of 100 Trials";
        else if(name.startsWith("dmo_")) return "Intro Cutscene";
        else if(name.startsWith("end_")) return "Credits";
        else if(name.startsWith("kpa_")) return "Bowser Mission";
        else if(name.startsWith("yuu_")) return "Pianta Parlor Game";
        else return "Unknown";
    }

    /**
     * @Author Jemaroo
     * @Function Returns an image based on file name
     */
    public ImageView fileImageSelector(String name)
    {
        if(name.startsWith("global")) return unitImageViewCreator(images.get("magicalMap2"));
        else if(name.startsWith("aaa_")) return unitImageViewCreator(images.get("marioHeadCustom"));
        else if(name.startsWith("gor_")) return unitImageViewCreator(images.get("unitProfessorFrankly"));
        else if(name.startsWith("tik_")) return unitImageViewCreator(images.get("unitBlooper"));
        else if(name.startsWith("hei_")) return unitImageViewCreator(images.get("unitKoopaTroopa"));
        else if(name.startsWith("nok_")) return unitImageViewCreator(images.get("koopaCustom"));
        else if(name.startsWith("gon_")) return unitImageViewCreator(images.get("unitHooktail"));
        else if(name.startsWith("win_")) return unitImageViewCreator(images.get("unitPalePiranha"));
        else if(name.startsWith("mri_")) return unitImageViewCreator(images.get("puniCustom"));
        else if(name.startsWith("tou_")) return unitImageViewCreator(images.get("hoggleCustom"));
        else if(name.startsWith("tou2_")) return unitImageViewCreator(images.get("unitRawkHawk"));
        else if(name.startsWith("usu_")) return unitImageViewCreator(images.get("twilightShopManagerWifeCustom"));
        else if(name.startsWith("gra_")) return unitImageViewCreator(images.get("unitHyperGoomba"));
        else if(name.startsWith("jin_")) return unitImageViewCreator(images.get("unitDoopliss"));
        else if(name.startsWith("muj_")) return unitImageViewCreator(images.get("unitPutridPiranha"));
        else if(name.startsWith("dou_")) return unitImageViewCreator(images.get("unitBillBlaster"));
        else if(name.startsWith("rsh_")) return unitImageViewCreator(images.get("serverToadCustom"));
        else if(name.startsWith("hom_")) return unitImageViewCreator(images.get("unitSmorg"));
        else if(name.startsWith("eki_")) return unitImageViewCreator(images.get("unitRuffPuff"));
        else if(name.startsWith("pik_")) return unitImageViewCreator(images.get("unitDarkBoo"));
        else if(name.startsWith("bom_")) return unitImageViewCreator(images.get("fahrOutpostBombCustom"));
        else if(name.startsWith("moo_")) return unitImageViewCreator(images.get("unitMoonCleft"));
        else if(name.startsWith("aji_")) return unitImageViewCreator(images.get("unitXNaut"));
        else if(name.startsWith("las_")) return unitImageViewCreator(images.get("unitShadowPeach"));
        else if(name.startsWith("jon_")) return unitImageViewCreator(images.get("unitBonetail"));
        else if(name.startsWith("dmo_")) return unitImageViewCreator(images.get("magicalMap2"));
        else if(name.startsWith("end_")) return unitImageViewCreator(images.get("magicalMap2"));
        else if(name.startsWith("kpa_")) return unitImageViewCreator(images.get("unitBowser"));
        else if(name.startsWith("yuu_")) return unitImageViewCreator(images.get("pianta"));
        else return unitImageViewCreator(images.get("unknown"));
    }

    /**
     * @Author Jemaroo
     * @Function Initializes the buttons for formatting text
     */
    public void initializeFormattingButtons()
    {
        kButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                String tempText = messageField.getSelectedText() + "<k>";
                messageField.replaceText(messageField.getSelection().getStart(), messageField.getSelection().getEnd(), tempText);
                messageField.requestFocus();
            }
        });

        pButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                String tempText = messageField.getSelectedText() + "<p>";
                messageField.replaceText(messageField.getSelection().getStart(), messageField.getSelection().getEnd(), tempText);
                messageField.requestFocus();
            }
        });

        waitButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                String tempText = messageField.getSelectedText() + "<wait FRAMES>";
                messageField.replaceText(messageField.getSelection().getStart(), messageField.getSelection().getEnd(), tempText);
                messageField.requestFocus();
            }
        });

        dkeyButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                String tempText = "<dkey>" + messageField.getSelectedText() + "</dkey>";
                messageField.replaceText(messageField.getSelection().getStart(), messageField.getSelection().getEnd(), tempText);
                messageField.requestFocus();
            }
        });

        dynamicButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                String tempText = "<dynamic VALUE>" + messageField.getSelectedText() + "</dynamic>";
                messageField.replaceText(messageField.getSelection().getStart(), messageField.getSelection().getEnd(), tempText);
                messageField.requestFocus();
            }
        });

        waveButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                String tempText = "<wave>" + messageField.getSelectedText() + "</wave>";
                messageField.replaceText(messageField.getSelection().getStart(), messageField.getSelection().getEnd(), tempText);
                messageField.requestFocus();
            }
        });

        shakeButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                String tempText = "<shake>" + messageField.getSelectedText() + "</shake>";
                messageField.replaceText(messageField.getSelection().getStart(), messageField.getSelection().getEnd(), tempText);
                messageField.requestFocus();
            }
        });

        noiseButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                String tempText = "<noise>" + messageField.getSelectedText() + "</noise>";
                messageField.replaceText(messageField.getSelection().getStart(), messageField.getSelection().getEnd(), tempText);
                messageField.requestFocus();
            }
        });

        scaleButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                String tempText = "<scale SIZE>" + messageField.getSelectedText() + "</scale>";
                messageField.replaceText(messageField.getSelection().getStart(), messageField.getSelection().getEnd(), tempText);
                messageField.requestFocus();
            }
        });

        colButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                String tempText = "<col 000000FF>" + messageField.getSelectedText() + "</col>";
                messageField.replaceText(messageField.getSelection().getStart(), messageField.getSelection().getEnd(), tempText);
                messageField.requestFocus();
            }
        });

        speedButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                String tempText = "<speed VALUE>" + messageField.getSelectedText() + "</speed>";
                messageField.replaceText(messageField.getSelection().getStart(), messageField.getSelection().getEnd(), tempText);
                messageField.requestFocus();
            }
        });

        posButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                String tempText = "<pos XPOS YPOS>" + messageField.getSelectedText();
                messageField.replaceText(messageField.getSelection().getStart(), messageField.getSelection().getEnd(), tempText);
                messageField.requestFocus();
            }
        });

        iconButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                String tempText = messageField.getSelectedText() + "<icon ID SCALE XPOS YPOS SPACING>";
                messageField.replaceText(messageField.getSelection().getStart(), messageField.getSelection().getEnd(), tempText);
                messageField.requestFocus();
            }
        });

        animButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                String tempText = messageField.getSelectedText() + "<anim MODELNAME ANIMATIONNAME XPOS YPOS SCALE FLIP>";
                messageField.replaceText(messageField.getSelection().getStart(), messageField.getSelection().getEnd(), tempText);
                messageField.requestFocus();
            }
        });
    }

    /**
     * @Author Jemaroo
     * @Function Sets the text style to red or black depending on value
     */
    private void setDarkStyle(Scene scene, boolean yesno) 
    {
        darkModeActive = yesno;
        scene.getStylesheets().clear();
        String css = yesno ? "/css/dark.css" : "/css/light.css";
        scene.getStylesheets().add(getClass().getResource(css).toExternalForm());
    }

    /**
     * @Author Jemaroo
     * @Function Applies the selected message window type to the currently opened text entry
     */
    private void updateSelectedMessageWindowType()
    {
        if (updatingMessageWindow) return;
        if (itemList == null) return;

        TETData selected = itemList.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        String formattedText = messageField.getText();
        if (formattedText == null) formattedText = "";

        int oldTagPosition = -1;
        String oldTag = "";

        for (String tag : messageWindowTags)
        {
            if (tag.isEmpty()) continue;

            int tagPosition = formattedText.indexOf(tag);

            if (tagPosition != -1)
            {
                oldTagPosition = tagPosition;
                oldTag = tag;
                break;
            }
        }

        if (!oldTag.isEmpty())
        {
            lastMessageWindowTagPosition = oldTagPosition;
            formattedText = formattedText.substring(0, oldTagPosition) + formattedText.substring(oldTagPosition + oldTag.length());
        }

        int selectedIndex = messageWindowType.getSelectionModel().getSelectedIndex();

        String newTag = messageWindowTags[selectedIndex];
        String newText;

        if (newTag.isEmpty())
        {
            removedMessageWindowTagNewline = "";

            if (oldTagPosition >= 0)
            {
                if (formattedText.startsWith("\r\n", oldTagPosition))
                {
                    removedMessageWindowTagNewline = "\r\n";
                    formattedText = formattedText.substring(0, oldTagPosition) + formattedText.substring(oldTagPosition + 2);
                }
                else if (oldTagPosition < formattedText.length() && (formattedText.charAt(oldTagPosition) == '\n' || formattedText.charAt(oldTagPosition) == '\r'))
                {
                    removedMessageWindowTagNewline = String.valueOf(formattedText.charAt(oldTagPosition));
                    formattedText = formattedText.substring(0, oldTagPosition) + formattedText.substring(oldTagPosition + 1);
                }
            }

            newText = formattedText;
        }
        else
        {
            int insertionPosition;

            if (oldTagPosition >= 0) insertionPosition = oldTagPosition;
            else if (lastMessageWindowTagPosition >= 0) insertionPosition = Math.min(lastMessageWindowTagPosition, formattedText.length());
            else insertionPosition = 0;

            String newlineToRestore = oldTagPosition < 0 ? removedMessageWindowTagNewline : "";

            newText = formattedText.substring(0, insertionPosition) + newTag + newlineToRestore + formattedText.substring(insertionPosition);

            lastMessageWindowTagPosition = insertionPosition;
            removedMessageWindowTagNewline = "";
        }

        updatingMessageWindow = true;
        messageField.setText(newText);
        updatingMessageWindow = false;

        updateMessageWindowImage();
        updateMessagePreview();

        saveChangesButton.setText("Save Text Changes");
    }

    /**
     * @Author Jemaroo
     * @Function Returns the textbox type index based on the text's current tag
     */
    private int getMessageWindowIndexFromText(String text)
    {
        if (text == null) return 0;

        for (int i = 1; i < messageWindowTags.length; i++)
        {
            if (text.contains(messageWindowTags[i]))
            {
                return i;
            }
        }

        return 0;
    }

    /**
     * @Author Jemaroo
     * @Function Updates the message window image to match the selected combo box index
     */
    private void updateMessageWindowImage()
    {
        int messageTypeIndex = messageWindowType.getSelectionModel().getSelectedIndex();

        if (messageTypeIndex == 0)
        {
            switch (messageWindowSpecialType.getSelectionModel().getSelectedIndex())
            {
                case 1: {messageWindow.setImage(images.get("messageWindowS1")); break;}
                case 2: {messageWindow.setImage(images.get("messageWindowS2")); break;}
                default: {messageWindow.setImage(images.get("messageWindow1")); break;}
            }
        }
        else messageWindow.setImage(images.get("messageWindow" + (messageTypeIndex + 1)));
    }
    
    /**
     * @Author Jemaroo
     * @Function Sets up the message preview
     */
    private void initializeMessagePreview()
    {
        messagePreviewPane.setPrefSize(560, 176);
        messagePreviewPane.setMinSize(560, 176);
        messagePreviewPane.setMaxSize(560, 176);

        messageWindow.setFitWidth(560);
        messageWindow.setFitHeight(176);
        messageWindow.setPreserveRatio(false);

        previewTextPane.setMouseTransparent(true);
        previewTextPane.setPrefSize(465, 100);
        previewTextPane.setMaxSize(465, 100);

        StackPane.setAlignment(messageWindow, Pos.CENTER);
        StackPane.setAlignment(previewTextPane, Pos.TOP_LEFT);
        StackPane.setMargin(previewTextPane, new Insets(40, 0, 0, 60));

        previewModelCanvas.setMouseTransparent(true);

        StackPane.setAlignment(previewModelCanvas, Pos.TOP_LEFT);
        StackPane.setMargin(previewModelCanvas, Insets.EMPTY);

        messageStar.setMouseTransparent(true);
        messageStar.setVisible(false);
        StackPane.setAlignment(messageStar, Pos.TOP_LEFT);
        
        messagePreviewPane.getChildren().addAll(messageWindow, previewModelCanvas, previewTextPane, messageStar);
    }

    /**
     * @Author Jemaroo
     * @Function Gets how many pages are needed for preview
     */
    private String[] getPreviewPages(String text)
    {
        if (text == null || text.isEmpty()) {return new String[]{""};}

        String normalizedText = text.replace("\r\n", "\n").replace('\r', '\n');
        String[] originalPages = normalizedText.split("<p>", -1);

        boolean twoLinePages = messageWindowType.getSelectionModel().getSelectedIndex() == 0 && (messageWindowSpecialType.getSelectionModel().getSelectedIndex() == 1 || messageWindowSpecialType.getSelectionModel().getSelectedIndex() == 2);
        
        if (!twoLinePages)
        {
            for (int i = 0; i < originalPages.length; i++)
            {
                if (originalPages[i] == null || originalPages[i].isEmpty()) originalPages[i] = "";
                else if (originalPages[i].startsWith("\n")) originalPages[i] = originalPages[i].substring(1);
            }

            return originalPages;
        }

        ArrayList<String> splitPages = new ArrayList<>();

        for (String originalPage : originalPages)
        {
            if (originalPage == null) originalPage = "";
            if (originalPage.startsWith("\n")) originalPage = originalPage.substring(1);

            String[] lines = originalPage.split("\n", -1);
            StringBuilder currentPage = new StringBuilder();
            int validLineCount = 0;

            for (String line : lines)
            {
                if (currentPage.length() > 0) currentPage.append("\n");
                currentPage.append(line);

                String visibleText = line.replaceAll("<[^>]*>", "").trim();
                boolean containsVisibleObject = line.matches("(?i).*<\\s*(icon|anim)\\b[^>]*>.*");

                if (!visibleText.isEmpty() || containsVisibleObject) validLineCount++;

                if (validLineCount == 2)
                {
                    splitPages.add(currentPage.toString());
                    currentPage.setLength(0);
                    validLineCount = 0;
                }
            }

            if (currentPage.length() > 0) splitPages.add(currentPage.toString());
            else if (lines.length == 0) splitPages.add("");
        }

        if (splitPages.isEmpty()) splitPages.add("");

        return splitPages.toArray(new String[0]);
    }

    /**
     * @Author Jemaroo
     * @Function Updates the preview text
     */
    private void updateMessagePreview()
    {
        updateMessageWindowImage();

        String[] pages = getPreviewPages(messageField.getText());
        if (pages.length == 0)
        {
            PreviewLayout layout = getPreviewLayoutForCurrentTextbox();
            double previewWidth = 560;
            double previewHeight = 176;

            messageWindow.setFitWidth(previewWidth);
            messageWindow.setFitHeight(previewHeight);
            messagePreviewPane.setPrefSize(previewWidth, previewHeight);
            messagePreviewPane.setMinSize(previewWidth, previewHeight);
            messagePreviewPane.setMaxSize(previewWidth, previewHeight);

            previewTextPane.setPrefSize(Math.max(1, previewWidth - layout.textX - 20), Math.max(1, previewHeight - layout.textY - 15));
            previewTextPane.setMinSize(Math.max(1, previewWidth - layout.textX - 20), Math.max(1, previewHeight - layout.textY - 15));
            previewTextPane.setMaxSize(Math.max(1, previewWidth - layout.textX - 20), Math.max(1, previewHeight - layout.textY - 15));

            StackPane.setMargin(previewTextPane, new Insets(layout.textY, 0, 0, layout.textX));

            renderPreviewText("", layout, new PreviewState(layout));

            currentPreviewPage = 0;
            pageDisplay.setText("1/1");
            previousPage.setDisable(true);
            nextPage.setDisable(true);

            return;
        }

        if (currentPreviewPage < 0) currentPreviewPage = 0;
        if (currentPreviewPage >= pages.length) currentPreviewPage = pages.length - 1;

        pageDisplay.setText((currentPreviewPage + 1) + "/" + pages.length);

        String pageText = pages[currentPreviewPage];
        PreviewLayout layout = getPreviewLayoutForCurrentTextbox();
        double previewWidth = 560;
        double previewHeight = 176;

        if (showMessageStar && layout.messageStarIcon != null && images.get(layout.messageStarIcon) != null)
        {
            messageStar.setImage(images.get(layout.messageStarIcon));
            messageStar.setVisible(true);
            StackPane.setMargin(messageStar, new Insets(layout.messageStarY, 0, 0, layout.messageStarX));
        }
        else messageStar.setVisible(false);

        messageWindow.setFitWidth(previewWidth);
        messageWindow.setFitHeight(previewHeight);
        messagePreviewPane.setPrefSize(previewWidth, previewHeight);
        messagePreviewPane.setMinSize(previewWidth, previewHeight);
        messagePreviewPane.setMaxSize(previewWidth, previewHeight);

        double textPaneWidth = Math.max(1, previewWidth - layout.textX - 20);
        double textPaneHeight = Math.max(1, previewHeight - layout.textY - 15);

        previewTextPane.setPrefSize(textPaneWidth, textPaneHeight);
        previewTextPane.setMinSize(textPaneWidth, textPaneHeight);
        previewTextPane.setMaxSize(textPaneWidth, textPaneHeight);

        StackPane.setMargin(previewTextPane, new Insets(layout.textY, 0, 0, layout.textX));

        PreviewState state = new PreviewState(layout);
        for (int i = 0; i < currentPreviewPage && i < pages.length; i++)
        {
            if (pages[i] == null) break;

            for (int j = 0; j < pages[i].length(); j++)
            {
                char c = pages[i].charAt(j);

                if (c == '<')
                {
                    int endIndex = pages[i].indexOf('>', j);
                    if (endIndex != -1)
                    {
                        String tag = pages[i].substring(j + 1, endIndex).trim();
                        handlePreviewTag(tag, state, layout, false);

                        j = endIndex;
                    }
                }
            }

            state.x = 0;
            state.y = 0;
        }
        state.x = 0;
        state.y = 0;

        renderPreviewText(pageText, layout, state);

        previousPage.setDisable(currentPreviewPage <= 0);
        nextPage.setDisable(currentPreviewPage >= pages.length - 1);
    }

    /**
     * @Author Jemaroo
     * @Function Draws message preview text while interpereting tags
     */
    private void renderPreviewText(String text, PreviewLayout layout, PreviewState state)
    {
        previewTextPane.getChildren().clear();
        GraphicsContext modelGraphics = previewModelCanvas.getGraphicsContext2D();
        modelGraphics.clearRect(0, 0, previewModelCanvas.getWidth(), previewModelCanvas.getHeight());

        if (text == null) return;

        for (int i = 0; i < text.length(); i++)
        {
            char c = text.charAt(i);

            if (c == '\r')
            {
                continue;
            }
            if (c == '\n')
            {
                state.x = 0;
                state.y += state.fontSize + state.lineSpacing;
                continue;
            }
            if (c == '<')
            {
                int endIndex = text.indexOf('>', i);

                if (endIndex != -1)
                {
                    String tag = text.substring(i + 1, endIndex).trim();
                    handlePreviewTag(tag, state, layout, true);

                    i = endIndex;

                    if (shouldSkipNewlineAfterTag(tag))
                    {
                        if (i + 1 < text.length() && text.charAt(i + 1) == '\r')
                        {
                            i++;
                        }
                        if (i + 1 < text.length() && text.charAt(i + 1) == '\n')
                        {
                            i++;
                        }
                    }

                    continue;
                }
            }

            Text letter = new Text(String.valueOf(c));
            letter.setFont(Font.font(paperMario2Font.getFamily(), state.fontSize));
            letter.setStyle("-fx-fill: " + state.color + ";");

            double charWidth = letter.getLayoutBounds().getWidth();

            letter.setX(state.x);
            letter.setY(state.y + state.fontSize);

            previewTextPane.getChildren().add(letter);

            state.x += charWidth + state.characterSpacing;
        }
    }

    /**
     * @Author Jemaroo
     * @Function Returns the layout changes for each textbox
     */
    private PreviewLayout getPreviewLayoutForCurrentTextbox()
    {
        int selectedIndex = messageWindowType.getSelectionModel().getSelectedIndex();
        messageStar.setFitHeight(35);
        messageStar.setFitWidth(35);

        if (selectedIndex == 0)
        {
            int specialIndex = messageWindowSpecialType.getSelectionModel().getSelectedIndex();

            switch (specialIndex)
            {
                //Description Box
                case 1: 
                {
                    messageStar.setFitHeight(55); 
                    messageStar.setFitWidth(55); 
                    return new PreviewLayout(60, 55, 24, 2.2, 4.5, "#1f1f1f", 1.0, 1.0, 0.0, 8.0, 1.0, 0.25, "stickC", 490, 60);
                }
                //Battle Tooltip
                case 2: return new PreviewLayout(100, 65, 20, 1.2, 2.5, "#1f1f1f", 1.0, 1.0, 0, 0, 0.83, 0.83, "nothing", 0, 0);
                //Normal Textbox
                default: return new PreviewLayout(60, 45, 24, 2.2, 8.5, 1.0, 1.0, "bingoStar", 490, 108);
            }
        }

        switch (selectedIndex)
        {
            //<small>
            case 1: return new PreviewLayout(197, 69, 24, 2.5, 0.8, 1.0, 1.0, "nothing", 0, 0);
            //<system>
            case 2: return new PreviewLayout(60, 45, 24, 2.2, 8.5, "#dcc8dc", 1.0, 1.0, "bingoStar", 490, 108);
            //<housou>
            case 3: return new PreviewLayout(60, 45, 24, 2.2, 8.5, 1.0, 1.0, "bingoStar", 490, 108);
            //<tec>
            case 4: return new PreviewLayout(60, 45, 24, 2.2, 8.5, "#00d914", 1.0, 1.0, "bingoStar", 490, 108);
            //<diary>
            case 5: return new PreviewLayout(64, 37, 24, 2.2, 8.5, 1.0, 1.0, "bingoStar", 490, 108);
            //<kanban>
            case 6: return new PreviewLayout(60, 40, 24, 2.2, 8.5, 1.0, 1.0, "bingoStar", 490, 108);
            //<plate>
            case 7: return new PreviewLayout(60, 40, 24, 2.2, 8.5, 1.0, 1.0, "bingoStar", 490, 108);
            //<boss>
            case 8: return new PreviewLayout(60, 45, 24, 2.2, 8.5, 1.0, 1.0, "bingoStar", 490, 108);
            //<majo>
            case 9: return new PreviewLayout(60, 45, 24, 2.2, 8.5, 1.0, 1.0, "bingoStar", 490, 108);
            //<clear>
            case 10: return new PreviewLayout(60, 45, 24, 2.2, 8.5, 1.0, 1.0, "bingoStar", 490, 108);
            //Normal Textbox
            default: return new PreviewLayout(60, 45, 24, 2.2, 8.5, 1.0, 1.0, "bingoStar", 490, 108);
        }
    }

    /**
     * @Author Jemaroo
     * @Function Applies a scale value to the preview state
     */
    private void applyPreviewScale(PreviewState state, PreviewLayout layout, double scale)
    {
        state.currentScale = scale;
        state.fontSize = (int)Math.round(layout.fontSize * scale);
        state.characterSpacing = layout.characterSpacing * scale;
        state.lineSpacing = layout.lineSpacing * scale;
    }

    /**
     * @Author Jemaroo
     * @Function Handles static preview tags
     */
    private void handlePreviewTag(String tag, PreviewState state, PreviewLayout layout, boolean drawObjects)
    {
        if (tag == null || tag.isEmpty()) return;
        String formattedTag = tag.toLowerCase();

        //<k>, <key>, <o>, <once_stop>, <p>, <scrl_auto>
        //<wait>, <speed>, <se>, <vol>
        //</speed>
        //<wave>, </wave>, <shake>, </shake>, <noise, </noise>, <dynamic>, </dynamic>, <dkey>, </dkey>
        if (formattedTag.equals("k") || 
            formattedTag.equals("key") || 
            formattedTag.equals("o") || 
            formattedTag.equals("once_stop") || 
            formattedTag.equals("p") || 
            formattedTag.equals("scrl_auto") ||
            formattedTag.startsWith("wait ") || 
            formattedTag.startsWith("speed ") || 
            formattedTag.startsWith("se ") || 
            formattedTag.startsWith("vol ") ||
            formattedTag.equals("/speed") ||
            formattedTag.equals("wave") || 
            formattedTag.equals("/wave") ||
            formattedTag.equals("shake") || 
            formattedTag.equals("/shake") ||
            formattedTag.equals("noise") || 
            formattedTag.equals("/noise") ||
            formattedTag.startsWith("dynamic ") || 
            formattedTag.equals("/dynamic") ||
            formattedTag.equals("dkey") || 
            formattedTag.equals("/dkey"))
        {
            return;
        }
        //<col>
        if (formattedTag.startsWith("col "))
        {
            String[] parts = tag.split("\\s+");

            if (parts.length >= 2)
            {
                state.colorStack.push(state.color);
                state.color = colorTagToCss(parts[1]);
            }

            return;
        }
        //</col>
        if (formattedTag.equals("/col"))
        {
            if (!state.colorStack.isEmpty())
            {
                state.color = state.colorStack.pop();
            }
            else
            {
                state.color = "#1f1f1f";
            }

            return;
        }
        //<scale>
        if (formattedTag.startsWith("scale "))
        {
            String[] parts = tag.split("\\s+");

            if (parts.length >= 2)
            {
                try
                {
                    double scale = Double.parseDouble(parts[1]);

                    state.scaleStack.push(state.currentScale);
                    applyPreviewScale(state, layout, scale);
                }
                catch (NumberFormatException e){}
            }

            return;
        }
        //</scale>
        if (formattedTag.equals("/scale"))
        {
            if (!state.scaleStack.isEmpty())
            {
                double previousScale = state.scaleStack.pop();
                applyPreviewScale(state, layout, previousScale);
            }
            else
            {
                applyPreviewScale(state, layout, 1.0);
            }

            return;
        }
        //<pos>
        if (formattedTag.startsWith("pos "))
        {
            String[] parts = tag.split("\\s+");

            if (parts.length >= 3)
            {
                try
                {
                    double xValue = Double.parseDouble(parts[1]);
                    double yValue = Double.parseDouble(parts[2]);

                    state.x = xValue * layout.posScaleX;

                    if (yValue != 999)
                    {
                        state.y = yValue * layout.posScaleY;
                    }
                }
                catch (NumberFormatException ex){}
            }

            return;
        }
        //<icon>
        if (formattedTag.startsWith("icon "))
        {
            String[] parts = tag.split("\\s+");

            String iconID = "0";
            double scale = 1.0;
            double xOffset = 0;
            double yOffset = 0;
            double spacing = 0;

            try
            {
                if (parts.length >= 2) iconID = parts[1];
                if (parts.length >= 3) scale = Double.parseDouble(parts[2]);
                if (parts.length >= 4) xOffset = Double.parseDouble(parts[3]);
                if (parts.length >= 5) yOffset = Double.parseDouble(parts[4]);
                if (parts.length >= 6) spacing = Double.parseDouble(parts[5]);
            }
            catch (NumberFormatException ex){}

            // Special case: HM is rendered as regular text instead of an icon
            if (iconID.equals("HM"))
            {
                Text hmText = new Text("ﾐ");
                hmText.setFont(Font.font(paperMario2Font.getFamily(), state.fontSize));

                double charWidth = hmText.getLayoutBounds().getWidth();

                if (drawObjects)
                {
                    hmText.setStyle("-fx-fill: " + state.color + ";");
                    hmText.setX(state.x);
                    hmText.setY(state.y + state.fontSize);

                    previewTextPane.getChildren().add(hmText);
                }

                state.x += charWidth + state.characterSpacing;
                return;
            }

            String iconKey;
            if (iconID == null || iconID.isEmpty()) iconKey = null;
            else
            {
                iconID = iconID.trim();
                if (specialPreviewIcons.containsKey(iconID)) iconKey = specialPreviewIcons.get(iconID);
                else
                {
                    try {iconKey = previewIcons.get(Integer.parseInt(iconID));}
                    catch (NumberFormatException e) {iconKey = null;}
                }
            }
            Image iconImage = iconKey == null ? null : images.get(iconKey);

            double originalX = state.x;
            double iconWidth = 24.0 * scale;
            double iconHeight = 24.0 * scale;

            if (iconImage != null)
            {
                iconWidth = iconImage.getWidth() * scale;
                iconHeight = iconImage.getHeight() * scale;
            }

            double iconDrawX;
            double iconDrawY;
            double nextX;

            if (messageWindowType.getSelectionModel().getSelectedIndex() == 0 && messageWindowSpecialType.getSelectionModel().getSelectedIndex() != 0)
            {
                double iconSlotWidth = 32.0 * scale;
                double iconSlotHeight = 32.0 * scale;

                double iconPaddingBefore = messageWindowSpecialType.getSelectionModel().getSelectedIndex() == 1 ? 10.0 : 0.0;
                double iconPaddingAfter = messageWindowSpecialType.getSelectionModel().getSelectedIndex() == 1 ? 10.0 : 0.0;

                iconDrawX = originalX + iconPaddingBefore + (xOffset * layout.iconPosScaleX) + (iconSlotWidth / 2.0) - (iconWidth / 2.0) + layout.iconBaseX;
                iconDrawY = state.y + (yOffset * layout.iconPosScaleY) + (iconSlotHeight / 2.0) - (iconHeight / 2.0) + layout.iconBaseY;

                double iconAdvanceAdjustment = messageWindowSpecialType.getSelectionModel().getSelectedIndex() == 1 ? 10.0 : 8.3;

                nextX = originalX + iconPaddingBefore + iconSlotWidth - spacing + iconAdvanceAdjustment + iconPaddingAfter;
            }
            else
            {
                iconDrawX = originalX + xOffset + (iconWidth * 0.75);
                iconDrawY = state.y + yOffset - iconHeight - 5;
                nextX = originalX + iconWidth - spacing;
            }

            if (!drawObjects)
            {
                state.x = nextX;
                return;
            }

            if (iconImage == null)
            {
                Text placeholder = new Text("[I]");
                placeholder.setFont(Font.font(paperMario2Font.getFamily(), state.fontSize));
                placeholder.setStyle("-fx-fill: " + state.color + ";");
                placeholder.setX(iconDrawX);
                placeholder.setY(iconDrawY + iconHeight);

                previewTextPane.getChildren().add(placeholder);

                state.x = nextX;
                return;
            }

            ImageView iconView = new ImageView(iconImage);

            iconView.setFitWidth(iconWidth);
            iconView.setFitHeight(iconHeight);
            iconView.setPreserveRatio(true);
            iconView.setLayoutX(iconDrawX);
            iconView.setLayoutY(iconDrawY);
            previewTextPane.getChildren().add(iconView);

            state.x = nextX;
            return;
        }
        //<anim>
        if (formattedTag.startsWith("anim "))
        {
            String[] parts = tag.split("\\s+");
            if (parts.length < 7) return;

            String modelName = parts[1];
            String animationName = parts[2];
            double xPosition = state.x;
            double yPosition = state.y;
            double scale = 1.0;
            boolean flip = false;

            try
            {
                xPosition = Double.parseDouble(parts[3]);
                yPosition = Double.parseDouble(parts[4]);
                scale = Double.parseDouble(parts[5]);
                flip = !parts[6].equals("0");
            }
            catch (NumberFormatException ex)
            {
                if (drawObjects)
                {
                    Text placeholder = new Text("[MODEL ERROR]");
                    placeholder.setFont(Font.font(paperMario2Font.getFamily(), state.fontSize));
                    placeholder.setStyle("-fx-fill: #c00000;");
                    placeholder.setX(xPosition);
                    placeholder.setY(yPosition);
                    previewTextPane.getChildren().add(placeholder);
                }

                return;
            }

            if (drawObjects)
            {
                try
                {
                    AnimModelData model = previewModels.get(modelName);

                    if (model == null)
                    {
                        model = loadPreviewModel(modelName);
                        previewModels.put(modelName, model);
                    }

                    double modelDrawX = layout.textX + xPosition + 4.0;
                    double modelDrawY = layout.textY + layout.fontSize + yPosition + 10.0;

                    drawPreviewModel(model, animationName, modelDrawX, modelDrawY, scale, flip, 0.0);
                }
                catch (Exception ex)
                {
                    Text placeholder = new Text("[MODEL ERROR]");
                    placeholder.setFont(Font.font(paperMario2Font.getFamily(), state.fontSize));
                    placeholder.setStyle("-fx-fill: #c00000;");
                    placeholder.setX(xPosition);
                    placeholder.setY(yPosition);
                    previewTextPane.getChildren().add(placeholder);
                }
            }

            return;
        }
    }

    /**
     * @Author Jemaroo
     * @Function Converts hex color into javaFX color
     */
    private String colorTagToCss(String colorText)
    {
        if (colorText == null) return "#1f1f1f";

        colorText = colorText.trim();

        if (colorText.length() >= 6)
        {
            return "#" + colorText.substring(0, 6);
        }

        return "#1f1f1f";
    }

    /**
     * @Author Jemaroo
     * @Function Checks if a newline should be entered after a tag
     */
    private boolean shouldSkipNewlineAfterTag(String tag)
    {
        if (tag == null) return false;

        String lowerTag = tag.toLowerCase();
        if (lowerTag.equals("p") || lowerTag.equals("scrl_auto"))
        {
            return false;
        }

        return true;
    }

    /**
     * @Author Jemaroo
     * @Function Adds icons to the preview <icon> map
     */
    private void setPreviewIcons()
    {
        //Special Icons
        specialPreviewIcons.put("PAD_A", "buttonA");
        specialPreviewIcons.put("PAD_B", "buttonB");
        specialPreviewIcons.put("STICK_LEFT", "stickLeft");
        specialPreviewIcons.put("STICK", "stick");
        specialPreviewIcons.put("AC_ON", "actionCommandStar");
        specialPreviewIcons.put("BUTTON_L", "buttonL");
        specialPreviewIcons.put("BUTTON_R", "buttonR");
        specialPreviewIcons.put("PAD_X", "buttonX");
        specialPreviewIcons.put("PAD_X_ON", "buttonXPressed");
        specialPreviewIcons.put("PAD_Y", "buttonY");
        specialPreviewIcons.put("PAD_Y_ON", "buttonYPressed");
        specialPreviewIcons.put("PAD_Z_OFF", "buttonZ");
        specialPreviewIcons.put("PAD_ST_OFF", "buttonStart");
        specialPreviewIcons.put("black_key", "blackKey");
        specialPreviewIcons.put("ANM_PAD_A", "buttonA");
        specialPreviewIcons.put("ANM_STICK_RIGHT", "stickRight");
        specialPreviewIcons.put("ANM_PAD_START", "buttonStart");

        //Icon ID
        previewIcons.put(0, "zapTap");
        previewIcons.put(1, "moneyMoney");
        previewIcons.put(2, "wEmblem");
        previewIcons.put(3, "lEmblem");
        previewIcons.put(4, "flowerFinder");
        previewIcons.put(5, "FPPlus");
        previewIcons.put(6, "FPDrain");
        previewIcons.put(7, "feelingFine");
        previewIcons.put(8, "heartFinder");
        previewIcons.put(9, "HPPlus");
        previewIcons.put(10, "HPDrain2");
        previewIcons.put(11, "quakeHammer");
        previewIcons.put(12, "megaQuake");
        previewIcons.put(13, "piercingBlow");
        previewIcons.put(14, "hammerThrow");
        previewIcons.put(15, "fireDrive");
        previewIcons.put(16, "headRattle");
        previewIcons.put(17, "happyFlower");
        previewIcons.put(18, "itemHog");
        previewIcons.put(19, "icePower");
        previewIcons.put(20, "doublePain");
        previewIcons.put(21, "attackFXG");
        previewIcons.put(22, "attackFXB");
        previewIcons.put(23, "attackFXR");
        previewIcons.put(24, "attackFXP");
        previewIcons.put(25, "attackFXY");
        previewIcons.put(26, "chillOut");
        previewIcons.put(27, "prettyLucky");
        previewIcons.put(28, "spikeShield");
        previewIcons.put(29, "firstAttack");
        previewIcons.put(30, "bumpAttack");
        previewIcons.put(31, "multibounce");
        previewIcons.put(32, "powerBounce");
        previewIcons.put(33, "tornadoJump");
        previewIcons.put(34, "shrinkStomp");
        previewIcons.put(35, "sleepyStomp");
        previewIcons.put(36, "softStomp");
        previewIcons.put(37, "charge");
        previewIcons.put(38, "superCharge");
        previewIcons.put(39, "doubleDip");
        previewIcons.put(40, "tripleDip");
        previewIcons.put(41, "quickChange");
        previewIcons.put(42, "jumpMan");
        previewIcons.put(43, "hammerMan");
        previewIcons.put(44, "unusedDamageDodge");
        previewIcons.put(45, "happyHeart");
        previewIcons.put(46, "flowerSaver");
        previewIcons.put(47, "powerPlus");
        previewIcons.put(48, "defendPlus");
        previewIcons.put(49, "damageDodge");
        previewIcons.put(50, "peekaboo");
        previewIcons.put(51, "timingTutor");
        previewIcons.put(52, "PDownDUp");
        previewIcons.put(53, "PUpDDown");
        previewIcons.put(54, "allOrNothing");
        previewIcons.put(55, "megaRush");
        previewIcons.put(56, "lastStand");
        previewIcons.put(57, "closeCall");
        previewIcons.put(58, "luckyDay");
        previewIcons.put(59, "pityFlower");
        previewIcons.put(60, "icePower");
        previewIcons.put(61, "refund");
        previewIcons.put(62, "luckyStart");
        previewIcons.put(63, "returnPostage");
        previewIcons.put(64, "slowGo");
        previewIcons.put(65, "simplifier");
        previewIcons.put(66, "unsimplifier");
        previewIcons.put(67, "megaJump");
        previewIcons.put(68, "powerJump");
        previewIcons.put(69, "powerSmash");
        previewIcons.put(70, "megaSmash");
        previewIcons.put(71, "superAppeal");
        previewIcons.put(72, "iceSmash");
        previewIcons.put(73, "powerRush");
        previewIcons.put(74, "powerRush");
        previewIcons.put(75, "kPowerRush");
        previewIcons.put(76, "aPowerRush");
        previewIcons.put(77, "cPowerRush");
        previewIcons.put(78, "PUpDDownP");
        previewIcons.put(79, "PDownDUpP");
        previewIcons.put(80, "feelingFineP");
        previewIcons.put(81, "luckyDayP");
        previewIcons.put(82, "prettyLuckyP");
        previewIcons.put(83, "closeCallP");
        previewIcons.put(84, "lastStandP");
        previewIcons.put(85, "FPDrainP");
        previewIcons.put(86, "defendPlusP");
        previewIcons.put(87, "unusedDamageDodgeP");
        previewIcons.put(88, "HPPlusP");
        previewIcons.put(89, "damageDodgeP");
        previewIcons.put(90, "HPDrainP");
        previewIcons.put(91, "happyHeartP");
        previewIcons.put(92, "flowerSaverP");
        previewIcons.put(93, "powerPlusP");
        previewIcons.put(94, "happyFlowerP");
        previewIcons.put(95, "chargeP");
        previewIcons.put(96, "superChargeP");
        previewIcons.put(97, "superAppealP");
        previewIcons.put(98, "megaRushP");
        previewIcons.put(99, "allOrNothingP");
        previewIcons.put(100, "doubleDipP");
        previewIcons.put(101, "tripleDipP");
        previewIcons.put(102, "pityFlowerP");
        previewIcons.put(103, "powerRushP");
        previewIcons.put(104, "PowerRushP");
        previewIcons.put(105, "kPowerRushP");
        previewIcons.put(106, "aPowerRushP");
        previewIcons.put(107, "cPowerRushP");
        previewIcons.put(108, "buttonA");
        previewIcons.put(109, "buttonAPressed");
        previewIcons.put(110, "buttonB");
        previewIcons.put(111, "buttonBPressed");
        previewIcons.put(112, "buttonX");
        previewIcons.put(113, "buttonXPressed");
        previewIcons.put(114, "buttonY");
        previewIcons.put(115, "buttonYPressed");
        previewIcons.put(116, "stickCDownLeft");
        previewIcons.put(117, "stickCDown");
        previewIcons.put(118, "stickCDownRight");
        previewIcons.put(119, "stickCLeft");
        previewIcons.put(120, "stickC");
        previewIcons.put(121, "stickCRight");
        previewIcons.put(122, "stickCUpLeft");
        previewIcons.put(123, "stickCUp");
        previewIcons.put(124, "stickCUpRight");
        previewIcons.put(125, "stickDownLeft");
        previewIcons.put(126, "stickDown");
        previewIcons.put(127, "stickDownRight");
        previewIcons.put(128, "stickLeft");
        previewIcons.put(129, "stick");
        previewIcons.put(130, "stickRight");
        previewIcons.put(131, "stickUpLeft");
        previewIcons.put(132, "stickUp");
        previewIcons.put(133, "stickUpRight");
        previewIcons.put(134, "buttonL");
        previewIcons.put(135, "buttonLPressed");
        previewIcons.put(136, "buttonR");
        previewIcons.put(137, "buttonRPressed");
        previewIcons.put(138, "buttonZ");
        previewIcons.put(139, "buttonZPressed");
        previewIcons.put(140, "buttonStart");
        previewIcons.put(141, "buttonStartPressed");
        previewIcons.put(142, "buttonDpadUp");
        previewIcons.put(143, "buttonDpadUpPressed");
        previewIcons.put(144, "buttonDpadDown");
        previewIcons.put(145, "buttonDpadDownPressed");
        previewIcons.put(146, "buttonDpadUpDown");
        previewIcons.put(147, "buttonDpadUpDownPressed");
        previewIcons.put(148, "blueBar");
        previewIcons.put(149, "greenBar");
        previewIcons.put(150, "brownSquare");
        previewIcons.put(151, "brownLeftCircle");
        previewIcons.put(152, "brownRightCircle");
        previewIcons.put(153, "blueCircle");
        previewIcons.put(154, "actionCommandGreenCircle");
        previewIcons.put(155, "actionCommandYellowCircle");
        previewIcons.put(156, "actionCommandOrangeCircle");
        previewIcons.put(157, "actionCommandStar");
        previewIcons.put(158, "ok");
        previewIcons.put(159, "hpRegenStatus2");
        previewIcons.put(160, "fpRegenStatus2");
        previewIcons.put(161, "marioIndicator");
        previewIcons.put(162, "exclamationIndicator");
        previewIcons.put(163, "strangeSack");
        previewIcons.put(164, "hammer");
        previewIcons.put(165, "superHammer");
        previewIcons.put(166, "ultraHammer");
        previewIcons.put(167, "boots");
        previewIcons.put(168, "superBoots");
        previewIcons.put(169, "ultraBoots");
        previewIcons.put(170, "unusedBoots");
        previewIcons.put(171, "planeCurse");
        previewIcons.put(172, "boatCurse");
        previewIcons.put(173, "paperCurse");
        previewIcons.put(174, "tubeCurse");
        previewIcons.put(175, "coconutBomb");
        previewIcons.put(176, "driedbouquet");
        previewIcons.put(177, "zessFrappe");
        previewIcons.put(178, "iciclePop");
        previewIcons.put(179, "shroomFry");
        previewIcons.put(180, "shroomRoast");
        previewIcons.put(181, "shroomSteak");
        previewIcons.put(182, "zessDeluxe");
        previewIcons.put(183, "unusedCoconutDish");
        previewIcons.put(184, "mistake");
        previewIcons.put(185, "spicySoup");
        previewIcons.put(186, "spaceFood");
        previewIcons.put(187, "snowBunny");
        previewIcons.put(188, "unusedSnowBunny");
        previewIcons.put(189, "honeyShroom");
        previewIcons.put(190, "mapleShroom");
        previewIcons.put(191, "jellyShroom");
        previewIcons.put(192, "honeySuper");
        previewIcons.put(193, "mapleSuper");
        previewIcons.put(194, "jellySuper");
        previewIcons.put(195, "honeyUltra");
        previewIcons.put(196, "mapleUltra");
        previewIcons.put(197, "jellyUltra");
        previewIcons.put(198, "zessTea");
        previewIcons.put(199, "zessDinner");
        previewIcons.put(200, "zessSpecial");
        previewIcons.put(201, "zessDynamite");
        previewIcons.put(202, "courageMeal");
        previewIcons.put(203, "spitePouch");
        previewIcons.put(204, "mysticEgg");
        previewIcons.put(205, "turtleyLeaf");
        previewIcons.put(206, "keelMango");
        previewIcons.put(207, "freshPasta");
        previewIcons.put(208, "goldenLeaf");
        previewIcons.put(209, "cocoCandy");
        previewIcons.put(210, "honeyCandy");
        previewIcons.put(211, "jellyCandy");
        previewIcons.put(212, "healthySalad");
        previewIcons.put(213, "meteorMeal");
        previewIcons.put(214, "inkPasta");
        previewIcons.put(215, "koopasta");
        previewIcons.put(216, "shroomBroth");
        previewIcons.put(217, "heartfulCake");
        previewIcons.put(218, "omeletteMeal");
        previewIcons.put(219, "peachTart");
        previewIcons.put(220, "trialStew");
        previewIcons.put(221, "inkySauce");
        previewIcons.put(222, "shroomCrepe");
        previewIcons.put(223, "poisonShroom");
        previewIcons.put(224, "freshJuice");
        previewIcons.put(225, "koopaTea");
        previewIcons.put(226, "hotSauce");
        previewIcons.put(227, "shroomCake");
        previewIcons.put(228, "mangoDelight");
        previewIcons.put(229, "friedEgg");
        previewIcons.put(230, "cakeMix");
        previewIcons.put(231, "mousseCake");
        previewIcons.put(232, "peachyPeach");
        previewIcons.put(233, "fruitParfait");
        previewIcons.put(234, "spaghetti");
        previewIcons.put(235, "chocoCake");
        previewIcons.put(236, "eggBomb");
        previewIcons.put(237, "horsetail");
        previewIcons.put(238, "koopaBun");
        previewIcons.put(239, "zessCookie");
        previewIcons.put(240, "electroPop");
        previewIcons.put(241, "firePop");
        previewIcons.put(242, "spicyPasta");
        previewIcons.put(243, "lovePudding");
        previewIcons.put(244, "couplesCake");
        previewIcons.put(245, "blueKey");
        previewIcons.put(246, "redKey");
        previewIcons.put(247, "grottoKey");
        previewIcons.put(248, "stationKey1");
        previewIcons.put(249, "stationKey2");
        previewIcons.put(250, "castleKey");
        previewIcons.put(251, "houseKey");
        previewIcons.put(252, "storageKey");
        previewIcons.put(253, "elevatorKey1");
        previewIcons.put(254, "steepleKey1");
        previewIcons.put(255, "steepleKey2");
        previewIcons.put(256, "elevatorKey2");
        previewIcons.put(257, "elevatorKey3");
        previewIcons.put(258, "cardKey1");
        previewIcons.put(259, "cardKey2");
        previewIcons.put(260, "cardKey3");
        previewIcons.put(261, "cardKey4");
        previewIcons.put(262, "palaceKey1");
        previewIcons.put(263, "palaceKey2");
        previewIcons.put(264, "cake");
        previewIcons.put(265, "poisonedCake");
        previewIcons.put(266, "shineSprite");
        previewIcons.put(267, "contactLens");
        previewIcons.put(268, "skullGem");
        previewIcons.put(269, "oldLetter");
        previewIcons.put(270, "raggedDiary");
        previewIcons.put(271, "cog");
        previewIcons.put(272, "hotDog");
        previewIcons.put(273, "superbombomb");
        previewIcons.put(274, "briefcase");
        previewIcons.put(275, "mailboxSP");
        previewIcons.put(276, "blanket");
        previewIcons.put(277, "galleyPot");
        previewIcons.put(278, "necklace");
        previewIcons.put(279, "vitalPaper");
        previewIcons.put(280, "shellEarrings");
        previewIcons.put(281, "puniOrb");
        previewIcons.put(282, "goldRing");
        previewIcons.put(283, "weddingRing");
        previewIcons.put(284, "smallWeddingRing");
        previewIcons.put(285, "superLuigi1");
        previewIcons.put(286, "superLuigi2");
        previewIcons.put(287, "superLuigi3");
        previewIcons.put(288, "superLuigi4");
        previewIcons.put(289, "superLuigi5");
        previewIcons.put(290, "autograph");
        previewIcons.put(291, "gateHandle");
        previewIcons.put(292, "innCoupon");
        previewIcons.put(293, "blimpTicket");
        previewIcons.put(294, "trainTicket");
        previewIcons.put(295, "ultraStone");
        previewIcons.put(296, "chuckolaCola");
        previewIcons.put(297, "coconut");
        previewIcons.put(298, "champsBelt");
        previewIcons.put(299, "moonStone");
        previewIcons.put(300, "sunStone");
        previewIcons.put(301, "goldbobGuide1");
        previewIcons.put(302, "goldbobGuide2");
        previewIcons.put(303, "goldbobGuide3");
        previewIcons.put(304, "bowserMeat");
        previewIcons.put(305, "cookbook");
        previewIcons.put(306, "p");
        previewIcons.put(307, "box");
        previewIcons.put(308, "battleTrunks");
        previewIcons.put(309, "routingSlip");
        previewIcons.put(310, "wrestingMagazine");
        previewIcons.put(311, "present");
        previewIcons.put(312, "blackKey");
        previewIcons.put(313, "magicalMap1");
        previewIcons.put(314, "magicalMap2");
        previewIcons.put(315, "marioWantedPoster");
        previewIcons.put(316, "unknownPapers");
        previewIcons.put(317, "dubiousPaper");
        previewIcons.put(318, "bluePotion");
        previewIcons.put(319, "redPotion");
        previewIcons.put(320, "orangePotion");
        previewIcons.put(321, "greenPotion");
        previewIcons.put(322, "starKey");
        previewIcons.put(323, "specialCard");
        previewIcons.put(324, "platinumCard");
        previewIcons.put(325, "goldCard");
        previewIcons.put(326, "silverCard");
        previewIcons.put(327, "pianta");
        previewIcons.put(328, "lotteryPick");
        previewIcons.put(329, "goldBar");
        previewIcons.put(330, "goldBarX3");
        previewIcons.put(331, "poisonMushroom");
        previewIcons.put(332, "upArrow");
        previewIcons.put(333, "whackaBump");
        previewIcons.put(334, "jumpAction");
        previewIcons.put(335, "superJumpAction");
        previewIcons.put(336, "ultraJumpAction");
        previewIcons.put(337, "hammerAction");
        previewIcons.put(338, "superHammerAction");
        previewIcons.put(339, "ultraHammerAction");
        previewIcons.put(340, "itemsIcon");
        previewIcons.put(341, "specialAction");
        previewIcons.put(342, "tacticsFlag");
        previewIcons.put(343, "partnerAction");
        previewIcons.put(344, "lvl1Move");
        previewIcons.put(345, "lvl2Move");
        previewIcons.put(346, "lvl3Move");
        previewIcons.put(347, "lvl4Move");
        previewIcons.put(348, "unusedF");
        previewIcons.put(349, "goombellaPartnerSwitch");
        previewIcons.put(350, "koopsPartnerSwitch");
        previewIcons.put(351, "bobberyPartnerSwitch");
        previewIcons.put(352, "yoshiPartnerSwitch");
        previewIcons.put(353, "yoshiPartnerSwitch2");
        previewIcons.put(354, "yoshiPartnerSwitch3");
        previewIcons.put(355, "yoshiPartnerSwitch4");
        previewIcons.put(356, "yoshiPartnerSwitch5");
        previewIcons.put(357, "yoshiPartnerSwitch6");
        previewIcons.put(358, "yoshiPartnerSwitch7");
        previewIcons.put(359, "flurriePartnerSwitch");
        previewIcons.put(360, "vivianPartnerSwitch");
        previewIcons.put(361, "mowzPartnerSwitch");
        previewIcons.put(362, "unknownX");
        previewIcons.put(363, "runArrow");
        previewIcons.put(364, "appealAction");
        previewIcons.put(365, "defend");
        previewIcons.put(366, "chargeAction");
        previewIcons.put(367, "superChargeAction");
        previewIcons.put(368, "audienceCan");
        previewIcons.put(369, "audienceRock");
        previewIcons.put(370, "audienceBone");
        previewIcons.put(371, "audienceHammer");
        previewIcons.put(372, "courageShell");
        previewIcons.put(373, "tradeOff");
        previewIcons.put(374, "unusedFireFlowerRod");
        previewIcons.put(375, "powerPunch");
        previewIcons.put(376, "thunderRage");
        previewIcons.put(377, "fireFlower");
        previewIcons.put(378, "thunderBolt");
        previewIcons.put(379, "dizzyDial");
        previewIcons.put(380, "honeySyrup");
        previewIcons.put(381, "pointSwap");
        previewIcons.put(382, "mushroom");
        previewIcons.put(383, "superShroom");
        previewIcons.put(384, "ultraShroom");
        previewIcons.put(385, "lifeShroom");
        previewIcons.put(386, "slowShroom");
        previewIcons.put(387, "driedShroom");
        previewIcons.put(388, "voltShroom");
        previewIcons.put(389, "gradualSyrup");
        previewIcons.put(390, "koopaCurse");
        previewIcons.put(391, "shootingStar");
        previewIcons.put(392, "iceStorm");
        previewIcons.put(393, "unusedPillow");
        previewIcons.put(394, "mapleSyrup");
        previewIcons.put(395, "miniMrMini");
        previewIcons.put(396, "mystery");
        previewIcons.put(397, "sleepySheep");
        previewIcons.put(398, "stopwatch");
        previewIcons.put(399, "HPDrain1");
        previewIcons.put(400, "earthQuake");
        previewIcons.put(401, "heart");
        previewIcons.put(402, "flower");
        previewIcons.put(403, "coin");
        previewIcons.put(404, "XP");
        previewIcons.put(405, "starPiece");
        previewIcons.put(406, "powBlock");
        previewIcons.put(407, "mrsoftener");
        previewIcons.put(408, "boosSheet");
        previewIcons.put(409, "ruinPowder");
        previewIcons.put(410, "jamminJelly");
        previewIcons.put(411, "tastyTonic");
        previewIcons.put(412, "repelCape");
        previewIcons.put(413, "dataDisk");
        previewIcons.put(414, "frightMask");
        previewIcons.put(415, "diamondStar");
        previewIcons.put(416, "emeraldStar");
        previewIcons.put(417, "garnetStar");
        previewIcons.put(418, "goldStar");
        previewIcons.put(419, "crystalStar");
        previewIcons.put(420, "rubyStar");
        previewIcons.put(421, "sapphireStar");
        previewIcons.put(422, "menuMarioHead");
        previewIcons.put(423, "menuHeart");
        previewIcons.put(424, "menuFlower");
        previewIcons.put(425, "menuBP");
        previewIcons.put(426, "menuClock");
        previewIcons.put(427, "HUDMario");
        previewIcons.put(428, "HUDGoombella");
        previewIcons.put(429, "HUDKoops");
        previewIcons.put(430, "HUDBobbery");
        previewIcons.put(431, "HUDYoshi");
        previewIcons.put(432, "HUDYoshi2");
        previewIcons.put(433, "HUDYoshi3");
        previewIcons.put(434, "HUDYoshi4");
        previewIcons.put(435, "HUDYoshi5");
        previewIcons.put(436, "HUDYoshi6");
        previewIcons.put(437, "HUDYoshi7");
        previewIcons.put(438, "HUDFlurrie");
        previewIcons.put(439, "HUDVivian");
        previewIcons.put(440, "HUDMowz");
        previewIcons.put(441, "HUDFlower");
        previewIcons.put(442, "HUDStarPower");
        previewIcons.put(443, "HUDStarPoint");
        previewIcons.put(444, "HUDCoin");
        previewIcons.put(445, "menuArrowUp");
        previewIcons.put(446, "menuArrowDown");
        previewIcons.put(447, "SPOrb1");
        previewIcons.put(448, "spOrb2");
        previewIcons.put(449, "spOrb3");
        previewIcons.put(450, "spOrb4");
        previewIcons.put(451, "spOrb5");
        previewIcons.put(452, "spOrb6");
        previewIcons.put(453, "spOrb7");
        previewIcons.put(454, "spOrb8");
        previewIcons.put(455, "spOrbEmpty");
        previewIcons.put(456, "spBar1");
        previewIcons.put(457, "spBar2");
        previewIcons.put(458, "spBar3");
        previewIcons.put(459, "spBar4");
        previewIcons.put(460, "spBar5");
        previewIcons.put(461, "spBar6");
        previewIcons.put(462, "spBar7");
        previewIcons.put(463, "spBar8");
        previewIcons.put(464, "spBar9");
        previewIcons.put(465, "spBar10");
        previewIcons.put(466, "spBar11");
        previewIcons.put(467, "spBar12");
        previewIcons.put(468, "spBar13");
        previewIcons.put(469, "spBar14");
        previewIcons.put(470, "spBar15");
        previewIcons.put(471, "spBar16");
        previewIcons.put(472, "HUDRedBar");
        previewIcons.put(473, "HUDOrangeBar");
        previewIcons.put(474, "HUDBlueBar");
        previewIcons.put(475, "HUDGreenBar");
        previewIcons.put(476, "unknownWhiteBox");
        previewIcons.put(477, "unknownWhiteX");
        previewIcons.put(478, "unknownWhiteTinyX");
        previewIcons.put(479, "unknownSlash");
        previewIcons.put(480, "unknownTinySlash");
        previewIcons.put(481, "0");
        previewIcons.put(482, "0Small");
        previewIcons.put(483, "1");
        previewIcons.put(484, "1Small");
        previewIcons.put(485, "2");
        previewIcons.put(486, "2Small");
        previewIcons.put(487, "3");
        previewIcons.put(488, "3Small");
        previewIcons.put(489, "4");
        previewIcons.put(490, "4Small");
        previewIcons.put(491, "5");
        previewIcons.put(492, "5Small");
        previewIcons.put(493, "6");
        previewIcons.put(494, "6Small");
        previewIcons.put(495, "7");
        previewIcons.put(496, "7Small");
        previewIcons.put(497, "8");
        previewIcons.put(498, "8Small");
        previewIcons.put(499, "9");
        previewIcons.put(500, "9Small");
        previewIcons.put(501, "+");
        previewIcons.put(502, "-");
        previewIcons.put(503, "selectCursor2");
        previewIcons.put(504, "selectCursor");
        previewIcons.put(505, "unknownWhiteCircleBubble");
        previewIcons.put(506, "unknownButtonA");
        previewIcons.put(507, "unknownButtonB");
        previewIcons.put(508, "unknownButtonX");
        previewIcons.put(509, "unknownGradient");
        previewIcons.put(510, "0Timer");
        previewIcons.put(511, "1Timer");
        previewIcons.put(512, "2Timer");
        previewIcons.put(513, "3Timer");
        previewIcons.put(514, "4Timer");
        previewIcons.put(515, "5Timer");
        previewIcons.put(516, "6Timer");
        previewIcons.put(517, "7Timer");
        previewIcons.put(518, "8Timer");
        previewIcons.put(519, "9Timer");
        previewIcons.put(520, "colonTimer");
        previewIcons.put(521, "periodTimer");
        previewIcons.put(522, "whitePuniBar");
        previewIcons.put(523, "whitePuni");
        previewIcons.put(524, "unknownRedArrow");
        previewIcons.put(525, "audienceCount");
        previewIcons.put(526, "buttonA");
        previewIcons.put(527, "stickRight");
        previewIcons.put(528, "buttonStart");
        previewIcons.put(529, "infinite");
        previewIcons.put(530, "audienceStar");
        previewIcons.put(531, "whiteCircle");
        previewIcons.put(532, "buttonDpadAll");
        previewIcons.put(533, "partnerMenuJ");
        previewIcons.put(534, "gearMenuJ");
        previewIcons.put(535, "badgeMenuJ");
        previewIcons.put(536, "journalMenuJ");
        previewIcons.put(537, "buttonZ2");
        previewIcons.put(538, "textKoopa");
        previewIcons.put(539, "textBowser");
        previewIcons.put(540, "textTime");
        previewIcons.put(541, "textZeit");
        previewIcons.put(542, "textTemps");
        previewIcons.put(543, "textTiempo");
        previewIcons.put(544, "textTempo");
        previewIcons.put(545, "textWorld");
        previewIcons.put(546, "textWelt");
        previewIcons.put(547, "textMonde");
        previewIcons.put(548, "textMundo");
        previewIcons.put(549, "textMondo");
        previewIcons.put(550, "textPointsJ");
        previewIcons.put(551, "textPoints");
        previewIcons.put(552, "textPunkte");
        previewIcons.put(553, "textPuntos");
        previewIcons.put(554, "textPunti");
        previewIcons.put(555, "partyMenu");
        previewIcons.put(556, "partyMenu2");
        previewIcons.put(557, "partyMenu3");
        previewIcons.put(558, "partyMenu4");
        previewIcons.put(559, "partyMenu5");
        previewIcons.put(560, "gearMenu");
        previewIcons.put(561, "gearMenu2");
        previewIcons.put(562, "gearMenu3");
        previewIcons.put(563, "gearMenu4");
        previewIcons.put(564, "gearMenu5");
        previewIcons.put(565, "badgeMenu");
        previewIcons.put(566, "badgeMenu2");
        previewIcons.put(567, "badgeMenu3");
        previewIcons.put(568, "badgeMenu4");
        previewIcons.put(569, "journalMenu");
        previewIcons.put(570, "journalMenu2");
        previewIcons.put(571, "journalMenu3");
    }

    /**
     * @Author Jemaroo
     * @Function Loads a model from the given file and returns an AnimModelData object containing the model's data
     */
    private AnimModelData loadPreviewModel(String modelName) throws IOException
    {
        AnimModelData model = new AnimModelData();

        File modelFolder = new File(givenFile, "files" + File.separator + "a");
        File modelFile = new File(modelFolder, modelName);

        if(!modelFile.isFile())
        {
            try(java.util.stream.Stream<java.nio.file.Path> paths = Files.walk(givenFile.toPath()))
            {
                java.nio.file.Path foundModel = paths.filter(Files::isRegularFile).filter(path -> path.getFileName().toString().equals(modelName)).filter(path -> path.getParent() != null && path.getParent().getFileName().toString().equalsIgnoreCase("a")).findFirst().orElse(null);

                if(foundModel == null) throw new IOException("Model file not found below: " + givenFile.getAbsolutePath());

                modelFile = foundModel.toFile();
                modelFolder = modelFile.getParentFile();
            }
        }

        if(!modelFile.exists()) throw new IOException("Model file not found: " + modelFile.getAbsolutePath());

        byte[] modelBytes = Files.readAllBytes(modelFile.toPath());
        if(modelBytes.length < 0x84) throw new IOException("Model header is too small: " + modelFile.getAbsolutePath());

        int textureNameEnd = 0x44;
        while(textureNameEnd < 0x84 && modelBytes[textureNameEnd] != 0) textureNameEnd++;

        String textureName = new String(modelBytes, 0x44, textureNameEnd - 0x44, java.nio.charset.StandardCharsets.US_ASCII).trim();
        if(textureName.isEmpty()) throw new IOException("Model does not define a texture filename: " + modelFile.getAbsolutePath());

        File textureFile = new File(modelFolder, textureName.endsWith("-") ? textureName : textureName + "-");
        if(!textureFile.exists()) throw new IOException("TPL file not found: " + textureFile.getAbsolutePath());

        byte[] tplBytes = Files.readAllBytes(textureFile.toPath());

        ByteBuffer data = ByteBuffer.wrap(modelBytes);
        data.order(ByteOrder.BIG_ENDIAN);

        int shapeCount = data.getInt(0xE8);
        int polygonCount = data.getInt(0xEC);
        int vertexCount = data.getInt(0xF0);
        int vertexIndexCount = data.getInt(0xF4);
        int textureCoordinateCount = data.getInt(0x128);
        int textureCoordinateTransformCount = data.getInt(0x12C);
        int samplerCount = data.getInt(0x130);
        int textureCount = data.getInt(0x134);
        int subshapeCount = data.getInt(0x138);
        int visibilityGroupCount = data.getInt(0x13C);
        int groupTransformFloatCount = data.getInt(0x140);
        int groupCount = data.getInt(0x144);
        int animationCount = data.getInt(0x148);

        int shapesOffset = data.getInt(0x14C);
        int polygonsOffset = data.getInt(0x150);
        int verticesOffset = data.getInt(0x154);
        int vertexIndicesOffset = data.getInt(0x158);
        int textureCoordinate0IndicesOffset = data.getInt(0x16C);
        int textureCoordinatesOffset = data.getInt(0x18C);
        int textureCoordinateTransformsOffset = data.getInt(0x190);
        int samplersOffset = data.getInt(0x194);
        int texturesOffset = data.getInt(0x198);
        int subshapesOffset = data.getInt(0x19C);
        int visibilityGroupsOffset = data.getInt(0x1A0);
        int groupTransformsOffset = data.getInt(0x1A4);
        int groupsOffset = data.getInt(0x1A8);
        int animationsOffset = data.getInt(0x1AC);

        model.textureIdToTplIndex.clear();

        for(int textureId = 0; textureId < textureCount; textureId++)
        {
            int textureOffset = texturesOffset + textureId * 0x40;
            int tplIndex = data.getInt(textureOffset + 4);

            model.textureIdToTplIndex.add(tplIndex);
        }

        model.textureMapBaseTextureIds.clear();

        for(int textureMapIndex = 0; textureMapIndex < samplerCount; textureMapIndex++)
        {
            int textureMapOffset = samplersOffset + textureMapIndex * 8;
            int textureId = data.getInt(textureMapOffset);

            model.textureMapBaseTextureIds.add(textureId);
        }

        model.textureMapBaseFrameOffsets.clear();

        for(int textureMapIndex = 0; textureMapIndex < samplerCount; textureMapIndex++)
        {
            int frameOffset = 0;

            if(textureMapIndex < textureCoordinateTransformCount)
            {
                int transformOffset = textureCoordinateTransformsOffset + textureMapIndex * 24;
                frameOffset = Byte.toUnsignedInt(modelBytes[transformOffset]);
            }

            model.textureMapBaseFrameOffsets.add(frameOffset);
        }

        for(int i = 0; i < vertexCount; i++)
        {
            int offset = verticesOffset + i * 12;

            model.vertices.add(new float[] {data.getFloat(offset), data.getFloat(offset + 4), data.getFloat(offset + 8)});
        }

        for(int i = 0; i < textureCoordinateCount; i++)
        {
            int offset = textureCoordinatesOffset + i * 8;

            model.textureCoordinates.add(new float[] {data.getFloat(offset), data.getFloat(offset + 4)});
        }

        for(int i = 0; i < vertexIndexCount; i++) model.vertexIndices.add(data.getInt(vertexIndicesOffset + i * 4));
        for(int i = 0; i < vertexIndexCount; i++) model.textureCoordinateIndices.add(data.getInt(textureCoordinate0IndicesOffset + i * 4));

        for(int i = 0; i < polygonCount; i++)
        {
            int offset = polygonsOffset + i * 8;

            ModelPolygon polygon = new ModelPolygon();
            polygon.vertexBaseIndex = data.getInt(offset);
            polygon.textureCoordinateBaseIndex = data.getInt(offset);
            polygon.vertexCount = data.getInt(offset + 4);

            model.polygons.add(polygon);
        }

        for(int shapeIndex = 0; shapeIndex < shapeCount; shapeIndex++)
        {
            int shapeOffset = shapesOffset + shapeIndex * 0xA8;
            int subshapeBaseIndex = data.getInt(shapeOffset + 0x98);
            int shapeSubshapeCount = data.getInt(shapeOffset + 0x9C);
            int shapeVertexBase = data.getInt(shapeOffset + 0x40);
            int shapeTextureCoordinateBase = data.getInt(shapeOffset + 0x58);

            for(int subshapeNumber = 0; subshapeNumber < shapeSubshapeCount; subshapeNumber++)
            {
                int subshapeIndex = subshapeBaseIndex + subshapeNumber;

                if(subshapeIndex < 0 || subshapeIndex >= subshapeCount) continue;

                int subshapeOffset = subshapesOffset + subshapeIndex * 0x6C;
                int subshapeSamplerCount = data.getInt(subshapeOffset);

                if(subshapeSamplerCount <= 0) continue;

                int samplerIndex = data.getInt(subshapeOffset + 0x10);
                int polygonBaseIndex = data.getInt(subshapeOffset + 0x38);
                int subshapePolygonCount = data.getInt(subshapeOffset + 0x3C);
                int vertexIndexBase = data.getInt(subshapeOffset + 0x40);
                int textureCoordinateIndexBase = data.getInt(subshapeOffset + 0x4C);

                if(samplerIndex < 0 || samplerIndex >= samplerCount) continue;

                int samplerOffset = samplersOffset + samplerIndex * 8;
                int textureBaseId = data.getInt(samplerOffset);

                if(textureBaseId < 0 || textureBaseId >= textureCount) continue;

                int textureOffset = texturesOffset + textureBaseId * 0x40;
                int tplIndex = data.getInt(textureOffset + 4);

                for(int polygonNumber = 0; polygonNumber < subshapePolygonCount; polygonNumber++)
                {
                    int polygonIndex = polygonBaseIndex + polygonNumber;
                    if(polygonIndex < 0 || polygonIndex >= model.polygons.size()) continue;

                    ModelPolygon polygon = model.polygons.get(polygonIndex);
                    polygon.vertexBaseIndex += vertexIndexBase;
                    polygon.textureCoordinateBaseIndex += textureCoordinateIndexBase;
                    polygon.vertexArrayBase = shapeVertexBase;
                    polygon.textureCoordinateArrayBase = shapeTextureCoordinateBase;
                    polygon.textureIndex = tplIndex;
                    polygon.textureMapIndex = samplerIndex;
                    polygon.shapeIndex = shapeIndex;
                }
            }
        }

        model.baseVisibility = new byte[visibilityGroupCount];
        for(int i = 0; i < visibilityGroupCount; i++) model.baseVisibility[i] = modelBytes[visibilityGroupsOffset + i];

        int groupTransformCount = groupTransformFloatCount / 24;

        for(int i = 0; i < groupCount; i++)
        {
            int offset = groupsOffset + i * 88;

            ModelGroup group = new ModelGroup();
            group.nextGroupId = data.getInt(offset + 64);
            group.childGroupId = data.getInt(offset + 68);
            group.shapeId = data.getInt(offset + 72);
            group.visibilityGroupId = data.getInt(offset + 76);
            group.transformBaseIndex = data.getInt(offset + 80);
            group.isJoint = data.getInt(offset + 84) != 0;

            int transformIndex = group.transformBaseIndex / 24;

            if(transformIndex >= 0 && transformIndex < groupTransformCount)
            {
                int transformOffset = groupTransformsOffset + group.transformBaseIndex * 4;

                for(int transformValueIndex = 0; transformValueIndex < 24; transformValueIndex++)
                {
                    group.transformValues[transformValueIndex] = data.getFloat(transformOffset + transformValueIndex * 4);
                }
            }

            model.groups.add(group);
        }

        for(int parentIndex = 0; parentIndex < model.groups.size(); parentIndex++)
        {
            int childIndex = model.groups.get(parentIndex).childGroupId;
            int safety = 0;

            while(childIndex >= 0 && childIndex < model.groups.size() && safety < model.groups.size())
            {
                model.groups.get(childIndex).parentGroupId = parentIndex;
                childIndex = model.groups.get(childIndex).nextGroupId;

                safety++;
            }
        }

        for(int groupIndex = 0; groupIndex < model.groups.size(); groupIndex++)
        {
            ModelGroup group = model.groups.get(groupIndex);

            if(group.shapeId >= 0 && group.shapeId < shapeCount)
            {
                for(ModelPolygon polygon : model.polygons)
                {
                    if(polygon.shapeIndex == group.shapeId) polygon.groupIndex = groupIndex;
                }
            }
        }

        for(int animationIndex = 0; animationIndex < animationCount; animationIndex++)
        {
            int animationOffset = animationsOffset + animationIndex * 0x40;
            int nameEnd = animationOffset;

            while(nameEnd < animationOffset + 16 && modelBytes[nameEnd] != 0) nameEnd++;

            String animationName = new String(modelBytes, animationOffset, nameEnd - animationOffset, java.nio.charset.StandardCharsets.US_ASCII);
            int animationDataOffset = data.getInt(animationOffset + 0x3C);

            if(animationDataOffset <= 0 || animationDataOffset + 0x5C > modelBytes.length) continue;

            AnimAnimationData animation = new AnimAnimationData();
            animation.name = animationName;

            int baseInfoCount = data.getInt(animationDataOffset + 0x04);
            int keyframeCount = data.getInt(animationDataOffset + 0x08);
            int visibilityDeltaCount = data.getInt(animationDataOffset + 0x18);
            int baseInfoPointer = data.getInt(animationDataOffset + 0x24);
            int keyframePointer = data.getInt(animationDataOffset + 0x28);
            int visibilityDeltaPointer = data.getInt(animationDataOffset + 0x38);
            int groupTransformDeltaCount = data.getInt(animationDataOffset + 0x1C);
            int groupTransformDeltaPointer = data.getInt(animationDataOffset + 0x3C);

            int unknownDeltaCount1 = data.getInt(animationDataOffset + 0x0C);
            int unknownDeltaCount3 = data.getInt(animationDataOffset + 0x14);
            int unknownDeltaPointer1 = data.getInt(animationDataOffset + 0x2C);
            int unknownDeltaPointer3 = data.getInt(animationDataOffset + 0x34);

            if(baseInfoCount > 0)
            {
                int baseInfoOffset = animationDataOffset + baseInfoPointer;
                animation.loop = data.getInt(baseInfoOffset) != 0;
                animation.startFrame = data.getFloat(baseInfoOffset + 4);
                animation.endFrame = data.getFloat(baseInfoOffset + 8);
            }

            for(int keyframeIndex = 0; keyframeIndex < keyframeCount; keyframeIndex++)
            {
                int keyframeOffset = animationDataOffset + keyframePointer + keyframeIndex * 0x2C;
                int visibilityBaseIndex = data.getInt(keyframeOffset + 0x1C);
                int keyframeVisibilityCount = data.getInt(keyframeOffset + 0x20);
                int transformBaseIndex = data.getInt(keyframeOffset + 0x24);
                int keyframeTransformCount = data.getInt(keyframeOffset + 0x28);

                int unknownBaseIndex1 = data.getInt(keyframeOffset + 0x04);
                int unknownCount1 = data.getInt(keyframeOffset + 0x08);
                int unknownBaseIndex3 = data.getInt(keyframeOffset + 0x14);
                int unknownCount3 = data.getInt(keyframeOffset + 0x18);

                AnimAnimationKeyframe keyframe = new AnimAnimationKeyframe();
                keyframe.time = data.getFloat(keyframeOffset);

                int vertexDeltaIndex = 0;

                for(int deltaIndex = 0; deltaIndex < unknownCount1; deltaIndex++)
                {
                    int globalDeltaIndex = unknownBaseIndex1 + deltaIndex;
                    if(globalDeltaIndex < 0 || globalDeltaIndex >= unknownDeltaCount1) break;

                    int deltaOffset = animationDataOffset + unknownDeltaPointer1 + globalDeltaIndex * 4;
                    vertexDeltaIndex += Byte.toUnsignedInt(modelBytes[deltaOffset]);

                    AnimVectorDelta delta = new AnimVectorDelta();
                    delta.index = vertexDeltaIndex;
                    delta.x = modelBytes[deltaOffset + 1] / 16.0;
                    delta.y = modelBytes[deltaOffset + 2] / 16.0;
                    delta.z = modelBytes[deltaOffset + 3] / 16.0;

                    keyframe.vertexDeltas.add(delta);
                }

                int textureMapIndex = 0;

                for(int deltaIndex = 0; deltaIndex < unknownCount3; deltaIndex++)
                {
                    int globalDeltaIndex = unknownBaseIndex3 + deltaIndex;
                    if(globalDeltaIndex < 0 || globalDeltaIndex >= unknownDeltaCount3) break;

                    int deltaOffset = animationDataOffset + unknownDeltaPointer3 + globalDeltaIndex * 12;

                    textureMapIndex += Byte.toUnsignedInt(modelBytes[deltaOffset]);

                    AnimTextureDelta delta = new AnimTextureDelta();
                    delta.textureMapIndex = textureMapIndex;
                    delta.textureIndexDelta = modelBytes[deltaOffset + 1];

                    keyframe.textureDeltas.add(delta);
                }

                int visibilityGroupId = 0;

                for(int deltaIndex = 0; deltaIndex < keyframeVisibilityCount; deltaIndex++)
                {
                    int globalDeltaIndex = visibilityBaseIndex + deltaIndex;
                    if(globalDeltaIndex < 0 || globalDeltaIndex >= visibilityDeltaCount) break;

                    int deltaOffset = animationDataOffset + visibilityDeltaPointer + globalDeltaIndex * 2;
                    visibilityGroupId += Byte.toUnsignedInt(modelBytes[deltaOffset]);

                    keyframe.visibilityGroupIds.add(visibilityGroupId);
                    keyframe.visibilityValues.add(Byte.toUnsignedInt(modelBytes[deltaOffset + 1]) == 1);
                }

                int transformIndex = 0;

                for(int deltaIndex = 0; deltaIndex < keyframeTransformCount; deltaIndex++)
                {
                    int globalDeltaIndex = transformBaseIndex + deltaIndex;
                    if(globalDeltaIndex < 0 || globalDeltaIndex >= groupTransformDeltaCount) break;

                    int deltaOffset = animationDataOffset + groupTransformDeltaPointer + globalDeltaIndex * 4;
                    transformIndex += Byte.toUnsignedInt(modelBytes[deltaOffset]);

                    AnimGroupTransformDelta delta = new AnimGroupTransformDelta();
                    delta.index = transformIndex;
                    delta.valueDelta = modelBytes[deltaOffset + 1];

                    keyframe.groupTransformDeltas.add(delta);
                }

                animation.keyframes.add(keyframe);
            }

            model.animations.put(animation.name, animation);
        }

        ByteBuffer tpl = ByteBuffer.wrap(tplBytes);
        tpl.order(ByteOrder.BIG_ENDIAN);

        int tplTextureCount = tpl.getInt(4);
        int tplTableOffset = tpl.getInt(8);

        for(int textureIndex = 0; textureIndex < tplTextureCount; textureIndex++)
        {
            int tableEntryOffset = tplTableOffset + textureIndex * 8;
            int imageHeaderOffset = tpl.getInt(tableEntryOffset);

            if(imageHeaderOffset <= 0 || imageHeaderOffset + 12 > tplBytes.length)
            {
                model.textures.add(null);
                continue;
            }

            int height = Short.toUnsignedInt(tpl.getShort(imageHeaderOffset));
            int width = Short.toUnsignedInt(tpl.getShort(imageHeaderOffset + 2));
            int format = tpl.getInt(imageHeaderOffset + 4);
            int imageDataOffset = tpl.getInt(imageHeaderOffset + 8);

            if(format != 14)
            {
                System.out.println("Unsupported TPL format " + format + " for texture " + textureIndex);

                model.textures.add(null);
                continue;
            }

            WritableImage image = new WritableImage(width, height);
            PixelWriter writer = image.getPixelWriter();
            int sourceOffset = imageDataOffset;

            for(int blockY = 0; blockY < height; blockY += 8)
            {
                for(int blockX = 0; blockX < width; blockX += 8)
                {
                    for(int subBlock = 0; subBlock < 4; subBlock++)
                    {
                        int subX = blockX + (subBlock % 2) * 4;
                        int subY = blockY + (subBlock / 2) * 4;
                        int color0 = Short.toUnsignedInt(tpl.getShort(sourceOffset));
                        int color1 = Short.toUnsignedInt(tpl.getShort(sourceOffset + 2));
                        long selectorBits = Integer.toUnsignedLong(tpl.getInt(sourceOffset + 4));

                        sourceOffset += 8;

                        int red0 = ((color0 >> 11) & 0x1F) * 255 / 31;
                        int green0 = ((color0 >> 5) & 0x3F) * 255 / 63;
                        int blue0 = (color0 & 0x1F) * 255 / 31;
                        int red1 = ((color1 >> 11) & 0x1F) * 255 / 31;
                        int green1 = ((color1 >> 5) & 0x3F) * 255 / 63;
                        int blue1 = (color1 & 0x1F) * 255 / 31;

                        int[] red = new int[4];
                        int[] green = new int[4];
                        int[] blue = new int[4];
                        int[] alpha = new int[4];

                        red[0] = red0;
                        green[0] = green0;
                        blue[0] = blue0;
                        alpha[0] = 255;

                        red[1] = red1;
                        green[1] = green1;
                        blue[1] = blue1;
                        alpha[1] = 255;

                        if(color0 > color1)
                        {
                            red[2] = (2 * red0 + red1) / 3;
                            green[2] = (2 * green0 + green1) / 3;
                            blue[2] = (2 * blue0 + blue1) / 3;
                            alpha[2] = 255;

                            red[3] = (red0 + 2 * red1) / 3;
                            green[3] = (green0 + 2 * green1) / 3;
                            blue[3] = (blue0 + 2 * blue1) / 3;
                            alpha[3] = 255;
                        }
                        else
                        {
                            red[2] = (red0 + red1) / 2;
                            green[2] = (green0 + green1) / 2;
                            blue[2] = (blue0 + blue1) / 2;
                            alpha[2] = 255;

                            red[3] = 0;
                            green[3] = 0;
                            blue[3] = 0;
                            alpha[3] = 0;
                        }

                        for(int pixelY = 0; pixelY < 4; pixelY++)
                        {
                            for(int pixelX = 0; pixelX < 4; pixelX++)
                            {
                                int destinationX = subX + pixelX;
                                int destinationY = subY + pixelY;

                                if(destinationX >= width || destinationY >= height) continue;

                                int selectorShift = 30 - ((pixelY * 4 + pixelX) * 2);
                                int selector = (int)((selectorBits >> selectorShift) & 3);

                                writer.setColor(destinationX, destinationY, Color.rgb(red[selector], green[selector], blue[selector], alpha[selector] / 255.0));
                            }
                        }
                    }
                }
            }

            model.textures.add(image);
        }

        return model;
    }

    /**
     * @Author Jemaroo
     * @Function Draws the given model on the preview, applying the specified animation and transformations
     */
    private void drawPreviewModel(AnimModelData model, String animationName, double centerX, double centerY, double scale, boolean flip, double frame)
    {
        GraphicsContext graphics = previewModelCanvas.getGraphicsContext2D();
        graphics.setImageSmoothing(false);
        ArrayList<ModelPolygon> polygons = new ArrayList<>(model.polygons);
        double[][] animatedTransforms = new double[model.groups.size()][24];
        for(int groupIndex = 0; groupIndex < model.groups.size(); groupIndex++) System.arraycopy(model.groups.get(groupIndex).transformValues, 0, animatedTransforms[groupIndex], 0, 24);

        double[][] animatedVertices = new double[model.vertices.size()][3];
        for(int i = 0; i < model.vertices.size(); i++)
        {
            animatedVertices[i][0] = model.vertices.get(i)[0];
            animatedVertices[i][1] = model.vertices.get(i)[1];
            animatedVertices[i][2] = model.vertices.get(i)[2];
        }

        int[] animatedTextureMapFrameOffsets = new int[model.textureMapBaseFrameOffsets.size()];
        for(int i = 0; i < animatedTextureMapFrameOffsets.length; i++)
        {
            animatedTextureMapFrameOffsets[i] = model.textureMapBaseFrameOffsets.get(i);
        }

        boolean[] visibility = new boolean[model.baseVisibility.length];
        for(int i = 0; i < visibility.length; i++) visibility[i] = model.baseVisibility[i] == 1;

        AnimAnimationData animation = model.animations.get(animationName);

        if(animation != null)
        {
            double animationFrame = animation.startFrame;

            if(!animation.keyframes.isEmpty())
            {
                animationFrame = Math.max(animationFrame, animation.keyframes.get(0).time);
            }

            for(AnimAnimationKeyframe keyframe : animation.keyframes)
            {
                if(keyframe.time > animationFrame) break;

                for(AnimVectorDelta delta : keyframe.vertexDeltas)
                {
                    if(delta.index < 0 || delta.index >= animatedVertices.length) continue;

                    animatedVertices[delta.index][0] += delta.x;
                    animatedVertices[delta.index][1] += delta.y;
                    animatedVertices[delta.index][2] += delta.z;
                }

                for(AnimTextureDelta delta : keyframe.textureDeltas)
                {
                    if(delta.textureMapIndex < 0 || delta.textureMapIndex >= animatedTextureMapFrameOffsets.length) continue;

                    animatedTextureMapFrameOffsets[delta.textureMapIndex] += delta.textureIndexDelta;
                }

                for(int i = 0; i < keyframe.visibilityGroupIds.size(); i++)
                {
                    int visibilityGroupId = keyframe.visibilityGroupIds.get(i);

                    if(visibilityGroupId >= 0 && visibilityGroupId < visibility.length)
                    {
                        visibility[visibilityGroupId] = keyframe.visibilityValues.get(i);
                    }
                }
            }

            for(int groupIndex = 0; groupIndex < animatedTransforms.length; groupIndex++)
            {
                ModelGroup group = model.groups.get(groupIndex);
                if(group.transformBaseIndex < 0) continue;

                for(int componentIndex = 0; componentIndex < 24; componentIndex++)
                {
                    int targetIndex = group.transformBaseIndex + componentIndex;
                    double currentValue = group.transformValues[componentIndex];
                    double previousValue = currentValue;
                    double nextValue = currentValue;
                    double previousTime = animation.startFrame;
                    double nextTime = animation.endFrame;
                    boolean foundPrevious = false;
                    boolean foundNext = false;

                    for(AnimAnimationKeyframe keyframe : animation.keyframes)
                    {
                        int combinedValueDelta = 0;
                        boolean foundDelta = false;

                        for(AnimGroupTransformDelta delta : keyframe.groupTransformDeltas)
                        {
                            if(delta.index != targetIndex) continue;

                            combinedValueDelta += delta.valueDelta;
                            foundDelta = true;
                        }

                        if(!foundDelta) continue;

                        currentValue += combinedValueDelta / 16.0;

                        if(keyframe.time <= animationFrame)
                        {
                            previousValue = currentValue;
                            previousTime = keyframe.time;
                            foundPrevious = true;
                        }
                        else
                        {
                            nextValue = currentValue;
                            nextTime = keyframe.time;
                            foundNext = true;
                            break;
                        }
                    }

                    if(!foundPrevious)
                    {
                        previousValue = model.groups.get(groupIndex).transformValues[componentIndex];
                        previousTime = animation.startFrame;
                    }

                    if(!foundNext)
                    {
                        animatedTransforms[groupIndex][componentIndex] = previousValue;
                        continue;
                    }

                    double duration = nextTime - previousTime;
                    double amount = duration <= 0.0 ? 0.0 : (animationFrame - previousTime) / duration;
                    amount = Math.max(0.0, Math.min(1.0, amount));

                    animatedTransforms[groupIndex][componentIndex] = previousValue + (nextValue - previousValue) * amount;
                }
            }
        }

        for(ModelPolygon polygon : polygons)
        {
            polygon.depth = 0.0;
            int validDepthPoints = 0;

            for(int i = 0; i < polygon.vertexCount; i++)
            {
                int indexPosition = polygon.vertexBaseIndex + i;
                if(indexPosition < 0 || indexPosition >= model.vertexIndices.size()) continue;

                int vertexIndex = polygon.vertexArrayBase + model.vertexIndices.get(indexPosition);
                if(vertexIndex < 0 || vertexIndex >= animatedVertices.length) continue;

                double transformedX = animatedVertices[vertexIndex][0];
                double transformedY = animatedVertices[vertexIndex][1];
                double transformedZ = animatedVertices[vertexIndex][2];

                ArrayList<Integer> groupChain = new ArrayList<>();
                int groupIndex = polygon.groupIndex;
                int safety = 0;

                while(groupIndex >= 0 && groupIndex < model.groups.size() && safety < model.groups.size())
                {
                    groupChain.add(groupIndex);
                    groupIndex = model.groups.get(groupIndex).parentGroupId;
                    safety++;
                }

                for(int chainIndex = 0; chainIndex < groupChain.size(); chainIndex++)
                {
                    int currentGroupIndex = groupChain.get(chainIndex);
                    ModelGroup currentGroup = model.groups.get(currentGroupIndex);
                    double[] transform = animatedTransforms[currentGroupIndex];

                    double translationX = transform[0];
                    double translationY = transform[1];
                    double translationZ = transform[2];

                    double scaleX = transform[3];
                    double scaleY = transform[4];
                    double scaleZ = transform[5];

                    double rotationX = transform[6];
                    double rotationY = transform[7];
                    double rotationZ = transform[8];

                    double jointPostRotationX = transform[9];
                    double jointPostRotationY = transform[10];
                    double jointPostRotationZ = transform[11];

                    double rotationPivotX = transform[12];
                    double rotationPivotY = transform[13];
                    double rotationPivotZ = transform[14];

                    double scalePivotX = transform[15];
                    double scalePivotY = transform[16];
                    double scalePivotZ = transform[17];

                    double rotationOffsetX = transform[18];
                    double rotationOffsetY = transform[19];
                    double rotationOffsetZ = transform[20];

                    double scaleOffsetX = transform[21];
                    double scaleOffsetY = transform[22];
                    double scaleOffsetZ = transform[23];

                    transformedX -= scalePivotX;
                    transformedY -= scalePivotY;
                    transformedZ -= scalePivotZ;

                    transformedX *= scaleX;
                    transformedY *= scaleY;
                    transformedZ *= scaleZ;

                    transformedX += scalePivotX + scaleOffsetX;
                    transformedY += scalePivotY + scaleOffsetY;
                    transformedZ += scalePivotZ + scaleOffsetZ;

                    transformedX -= rotationPivotX;
                    transformedY -= rotationPivotY;
                    transformedZ -= rotationPivotZ;

                    double angleX = Math.toRadians(rotationX * 2.0);
                    double angleY = Math.toRadians(rotationY * 2.0);
                    double angleZ = Math.toRadians(rotationZ * 2.0);

                    double cosX = Math.cos(angleX);
                    double sinX = Math.sin(angleX);
                    double cosY = Math.cos(angleY);
                    double sinY = Math.sin(angleY);
                    double cosZ = Math.cos(angleZ);
                    double sinZ = Math.sin(angleZ);

                    double rotatedY = transformedY * cosX - transformedZ * sinX;
                    double rotatedZ = transformedY * sinX + transformedZ * cosX;
                    transformedY = rotatedY;
                    transformedZ = rotatedZ;

                    double rotatedX = transformedX * cosY + transformedZ * sinY;
                    rotatedZ = -transformedX * sinY + transformedZ * cosY;
                    transformedX = rotatedX;
                    transformedZ = rotatedZ;

                    rotatedX = transformedX * cosZ - transformedY * sinZ;
                    rotatedY = transformedX * sinZ + transformedY * cosZ;
                    transformedX = rotatedX;
                    transformedY = rotatedY;

                    double postAngleX = Math.toRadians(jointPostRotationX);
                    double postAngleY = Math.toRadians(jointPostRotationY);
                    double postAngleZ = Math.toRadians(jointPostRotationZ);

                    double postCosX = Math.cos(postAngleX);
                    double postSinX = Math.sin(postAngleX);
                    double postCosY = Math.cos(postAngleY);
                    double postSinY = Math.sin(postAngleY);
                    double postCosZ = Math.cos(postAngleZ);
                    double postSinZ = Math.sin(postAngleZ);

                    rotatedY = transformedY * postCosX - transformedZ * postSinX;
                    rotatedZ = transformedY * postSinX + transformedZ * postCosX;
                    transformedY = rotatedY;
                    transformedZ = rotatedZ;

                    rotatedX = transformedX * postCosY + transformedZ * postSinY;
                    rotatedZ = -transformedX * postSinY + transformedZ * postCosY;
                    transformedX = rotatedX;
                    transformedZ = rotatedZ;

                    rotatedX = transformedX * postCosZ - transformedY * postSinZ;
                    rotatedY = transformedX * postSinZ + transformedY * postCosZ;
                    transformedX = rotatedX;
                    transformedY = rotatedY;

                    if(currentGroup.isJoint)
                    {
                        int parentGroupIndex = currentGroup.parentGroupId;

                        if(parentGroupIndex >= 0 && parentGroupIndex < model.groups.size() && model.groups.get(parentGroupIndex).isJoint)
                        {
                            double[] parentTransform = animatedTransforms[parentGroupIndex];
                            double parentScaleX = parentTransform[3];
                            double parentScaleY = parentTransform[4];
                            double parentScaleZ = parentTransform[5];

                            if(Math.abs(parentScaleX) > 0.000001) transformedX /= parentScaleX;
                            if(Math.abs(parentScaleY) > 0.000001) transformedY /= parentScaleY;
                            if(Math.abs(parentScaleZ) > 0.000001) transformedZ /= parentScaleZ;
                        }
                    }

                    transformedX += rotationPivotX + rotationOffsetX + translationX;
                    transformedY += rotationPivotY + rotationOffsetY + translationY;
                    transformedZ += rotationPivotZ + rotationOffsetZ + translationZ;
                }

                polygon.depth += transformedZ;
                validDepthPoints++;
            }

            if(validDepthPoints > 0)
            {
                polygon.depth /= validDepthPoints;
            }
        }

        Collections.sort(polygons, Comparator.comparingDouble(polygon -> polygon.depth));

        graphics.save();

        try
        {
            graphics.translate(centerX, centerY);
            if(flip) graphics.scale(-1.0, 1.0);

            graphics.scale(scale, scale);
            graphics.setStroke(Color.RED);
            graphics.setLineWidth(1.0 / Math.max(scale, 0.01));

            for(ModelPolygon polygon : polygons)
            {
                if(polygon.vertexCount < 3) continue;

                if(polygon.groupIndex >= 0 && polygon.groupIndex < model.groups.size())
                {
                    boolean polygonVisible = true;
                    int visibilityGroupIndex = polygon.groupIndex;
                    int visibilitySafety = 0;

                    while(visibilityGroupIndex >= 0 && visibilityGroupIndex < model.groups.size() && visibilitySafety < model.groups.size())
                    {
                        ModelGroup visibilityGroup = model.groups.get(visibilityGroupIndex);
                        int visibilityGroupId = visibilityGroup.visibilityGroupId;

                        if(visibilityGroupId >= 0 && visibilityGroupId < visibility.length && !visibility[visibilityGroupId])
                        {
                            polygonVisible = false;
                            break;
                        }

                        visibilityGroupIndex = visibilityGroup.parentGroupId;
                        visibilitySafety++;
                    }

                    if(!polygonVisible) continue;
                }

                double[] xPoints = new double[polygon.vertexCount];
                double[] yPoints = new double[polygon.vertexCount];
                double[] uPoints = new double[polygon.vertexCount];
                double[] vPoints = new double[polygon.vertexCount];
                int validPoints = 0;

                for(int i = 0; i < polygon.vertexCount; i++)
                {
                    int vertexIndexPosition = polygon.vertexBaseIndex + i;
                    int textureIndexPosition = polygon.textureCoordinateBaseIndex + i;

                    if(vertexIndexPosition < 0 || vertexIndexPosition >= model.vertexIndices.size()) break;
                    if(textureIndexPosition < 0 || textureIndexPosition >= model.textureCoordinateIndices.size()) break;

                    int vertexIndex = polygon.vertexArrayBase + model.vertexIndices.get(vertexIndexPosition);
                    int textureCoordinateIndex = polygon.textureCoordinateArrayBase + model.textureCoordinateIndices.get(textureIndexPosition);

                    if(vertexIndex < 0 || vertexIndex >= model.vertices.size()) break;
                    if(textureCoordinateIndex < 0 || textureCoordinateIndex >= model.textureCoordinates.size()) break;

                    double textureU = model.textureCoordinates.get(textureCoordinateIndex)[0];
                    double textureV = model.textureCoordinates.get(textureCoordinateIndex)[1];

                    double transformedX = animatedVertices[vertexIndex][0];
                    double transformedY = animatedVertices[vertexIndex][1];
                    double transformedZ = animatedVertices[vertexIndex][2];

                    ArrayList<Integer> groupChain = new ArrayList<>();
                    int groupIndex = polygon.groupIndex;
                    int safety = 0;

                    while(groupIndex >= 0 && groupIndex < model.groups.size() && safety < model.groups.size())
                    {
                        groupChain.add(groupIndex);
                        groupIndex = model.groups.get(groupIndex).parentGroupId;
                        safety++;
                    }

                    for(int chainIndex = 0; chainIndex < groupChain.size(); chainIndex++)
                    {
                        int currentGroupIndex = groupChain.get(chainIndex);
                        ModelGroup currentGroup = model.groups.get(currentGroupIndex);
                        double[] transform = animatedTransforms[currentGroupIndex];

                        double translationX = transform[0];
                        double translationY = transform[1];
                        double translationZ = transform[2];

                        double scaleX = transform[3];
                        double scaleY = transform[4];
                        double scaleZ = transform[5];

                        double rotationX = transform[6];
                        double rotationY = transform[7];
                        double rotationZ = transform[8];

                        double jointPostRotationX = transform[9];
                        double jointPostRotationY = transform[10];
                        double jointPostRotationZ = transform[11];

                        double rotationPivotX = transform[12];
                        double rotationPivotY = transform[13];
                        double rotationPivotZ = transform[14];

                        double scalePivotX = transform[15];
                        double scalePivotY = transform[16];
                        double scalePivotZ = transform[17];

                        double rotationOffsetX = transform[18];
                        double rotationOffsetY = transform[19];
                        double rotationOffsetZ = transform[20];

                        double scaleOffsetX = transform[21];
                        double scaleOffsetY = transform[22];
                        double scaleOffsetZ = transform[23];

                        transformedX -= scalePivotX;
                        transformedY -= scalePivotY;
                        transformedZ -= scalePivotZ;

                        transformedX *= scaleX;
                        transformedY *= scaleY;
                        transformedZ *= scaleZ;

                        transformedX += scalePivotX + scaleOffsetX;
                        transformedY += scalePivotY + scaleOffsetY;
                        transformedZ += scalePivotZ + scaleOffsetZ;

                        transformedX -= rotationPivotX;
                        transformedY -= rotationPivotY;
                        transformedZ -= rotationPivotZ;

                        double angleX = Math.toRadians(rotationX * 2.0);
                        double angleY = Math.toRadians(rotationY * 2.0);
                        double angleZ = Math.toRadians(rotationZ * 2.0);

                        double cosX = Math.cos(angleX);
                        double sinX = Math.sin(angleX);
                        double cosY = Math.cos(angleY);
                        double sinY = Math.sin(angleY);
                        double cosZ = Math.cos(angleZ);
                        double sinZ = Math.sin(angleZ);

                        double rotatedY = transformedY * cosX - transformedZ * sinX;
                        double rotatedZ = transformedY * sinX + transformedZ * cosX;
                        transformedY = rotatedY;
                        transformedZ = rotatedZ;

                        double rotatedX = transformedX * cosY + transformedZ * sinY;
                        rotatedZ = -transformedX * sinY + transformedZ * cosY;
                        transformedX = rotatedX;
                        transformedZ = rotatedZ;

                        rotatedX = transformedX * cosZ - transformedY * sinZ;
                        rotatedY = transformedX * sinZ + transformedY * cosZ;
                        transformedX = rotatedX;
                        transformedY = rotatedY;

                        double postAngleX = Math.toRadians(jointPostRotationX);
                        double postAngleY = Math.toRadians(jointPostRotationY);
                        double postAngleZ = Math.toRadians(jointPostRotationZ);

                        double postCosX = Math.cos(postAngleX);
                        double postSinX = Math.sin(postAngleX);
                        double postCosY = Math.cos(postAngleY);
                        double postSinY = Math.sin(postAngleY);
                        double postCosZ = Math.cos(postAngleZ);
                        double postSinZ = Math.sin(postAngleZ);

                        rotatedY = transformedY * postCosX - transformedZ * postSinX;
                        rotatedZ = transformedY * postSinX + transformedZ * postCosX;
                        transformedY = rotatedY;
                        transformedZ = rotatedZ;

                        rotatedX = transformedX * postCosY + transformedZ * postSinY;
                        rotatedZ = -transformedX * postSinY + transformedZ * postCosY;
                        transformedX = rotatedX;
                        transformedZ = rotatedZ;

                        rotatedX = transformedX * postCosZ - transformedY * postSinZ;
                        rotatedY = transformedX * postSinZ + transformedY * postCosZ;
                        transformedX = rotatedX;
                        transformedY = rotatedY;

                        if(currentGroup.isJoint)
                        {
                            int parentGroupIndex = currentGroup.parentGroupId;

                            if(parentGroupIndex >= 0 && parentGroupIndex < model.groups.size() && model.groups.get(parentGroupIndex).isJoint)
                            {
                                double[] parentTransform = animatedTransforms[parentGroupIndex];
                                double parentScaleX = parentTransform[3];
                                double parentScaleY = parentTransform[4];
                                double parentScaleZ = parentTransform[5];

                                if(Math.abs(parentScaleX) > 0.000001) transformedX /= parentScaleX;
                                if(Math.abs(parentScaleY) > 0.000001) transformedY /= parentScaleY;
                                if(Math.abs(parentScaleZ) > 0.000001) transformedZ /= parentScaleZ;
                            }
                        }

                        transformedX += rotationPivotX + rotationOffsetX + translationX;
                        transformedY += rotationPivotY + rotationOffsetY + translationY;
                        transformedZ += rotationPivotZ + rotationOffsetZ + translationZ;
                    }

                    xPoints[i] = transformedX;
                    yPoints[i] = -transformedY;
                    uPoints[i] = textureU;
                    vPoints[i] = textureV;

                    validPoints++;
                }

                if(validPoints >= 3)
                {
                    int animatedTextureIndex = polygon.textureIndex;

                    if(polygon.textureMapIndex >= 0 && polygon.textureMapIndex < animatedTextureMapFrameOffsets.length)
                    {
                        int animatedTextureId = model.textureMapBaseTextureIds.get(polygon.textureMapIndex) + animatedTextureMapFrameOffsets[polygon.textureMapIndex];

                        if(animatedTextureId >= 0 && animatedTextureId < model.textureIdToTplIndex.size())
                        {
                            animatedTextureIndex = model.textureIdToTplIndex.get(animatedTextureId);
                        }
                    }

                    if(animatedTextureIndex >= 0 && animatedTextureIndex < model.textures.size() && model.textures.get(animatedTextureIndex) != null)
                    {
                        Image texture = model.textures.get(animatedTextureIndex);
                        double textureWidth = texture.getWidth();
                        double textureHeight = texture.getHeight();

                        for(int triangleIndex = 1; triangleIndex < validPoints - 1; triangleIndex++)
                        {
                            int point0 = 0;
                            int point1 = triangleIndex;
                            int point2 = triangleIndex + 1;

                            double sourceX0 = uPoints[point0] * textureWidth;
                            double sourceY0 = vPoints[point0] * textureHeight;
                            double sourceX1 = uPoints[point1] * textureWidth;
                            double sourceY1 = vPoints[point1] * textureHeight;
                            double sourceX2 = uPoints[point2] * textureWidth;
                            double sourceY2 = vPoints[point2] * textureHeight;

                            double destinationX0 = xPoints[point0];
                            double destinationY0 = yPoints[point0];
                            double destinationX1 = xPoints[point1];
                            double destinationY1 = yPoints[point1];
                            double destinationX2 = xPoints[point2];
                            double destinationY2 = yPoints[point2];

                            double determinant = sourceX0 * (sourceY1 - sourceY2) + sourceX1 * (sourceY2 - sourceY0) + sourceX2 * (sourceY0 - sourceY1);
                            if(Math.abs(determinant) < 0.000001) continue;

                            double mxx = (destinationX0 * (sourceY1 - sourceY2) + destinationX1 * (sourceY2 - sourceY0) + destinationX2 * (sourceY0 - sourceY1)) / determinant;
                            double mxy = (destinationX0 * (sourceX2 - sourceX1) + destinationX1 * (sourceX0 - sourceX2) + destinationX2 * (sourceX1 - sourceX0)) / determinant;
                            double tx = (destinationX0 * (sourceX1 * sourceY2 - sourceX2 * sourceY1) + destinationX1 * (sourceX2 * sourceY0 - sourceX0 * sourceY2) + destinationX2 * (sourceX0 * sourceY1 - sourceX1 * sourceY0)) / determinant;

                            double myx = (destinationY0 * (sourceY1 - sourceY2) + destinationY1 * (sourceY2 - sourceY0) + destinationY2 * (sourceY0 - sourceY1)) / determinant;
                            double myy = (destinationY0 * (sourceX2 - sourceX1) + destinationY1 * (sourceX0 - sourceX2) + destinationY2 * (sourceX1 - sourceX0)) / determinant;
                            double ty = (destinationY0 * (sourceX1 * sourceY2 - sourceX2 * sourceY1) + destinationY1 * (sourceX2 * sourceY0 - sourceX0 * sourceY2) + destinationY2 * (sourceX0 * sourceY1 - sourceX1 * sourceY0)) / determinant;

                            double clipCenterX = (destinationX0 + destinationX1 + destinationX2) / 3.0;
                            double clipCenterY = (destinationY0 + destinationY1 + destinationY2) / 3.0;
                            double clipPadding = 0.35 / Math.max(scale, 0.01);

                            double clipDirectionX0 = destinationX0 - clipCenterX;
                            double clipDirectionY0 = destinationY0 - clipCenterY;
                            double clipDirectionX1 = destinationX1 - clipCenterX;
                            double clipDirectionY1 = destinationY1 - clipCenterY;
                            double clipDirectionX2 = destinationX2 - clipCenterX;
                            double clipDirectionY2 = destinationY2 - clipCenterY;

                            double clipLength0 = Math.max(Math.hypot(clipDirectionX0, clipDirectionY0), 0.000001);
                            double clipLength1 = Math.max(Math.hypot(clipDirectionX1, clipDirectionY1), 0.000001);
                            double clipLength2 = Math.max(Math.hypot(clipDirectionX2, clipDirectionY2), 0.000001);

                            double clipX0 = destinationX0 + clipDirectionX0 / clipLength0 * clipPadding;
                            double clipY0 = destinationY0 + clipDirectionY0 / clipLength0 * clipPadding;
                            double clipX1 = destinationX1 + clipDirectionX1 / clipLength1 * clipPadding;
                            double clipY1 = destinationY1 + clipDirectionY1 / clipLength1 * clipPadding;
                            double clipX2 = destinationX2 + clipDirectionX2 / clipLength2 * clipPadding;
                            double clipY2 = destinationY2 + clipDirectionY2 / clipLength2 * clipPadding;

                            graphics.save();
                            graphics.beginPath();
                            graphics.moveTo(clipX0, clipY0);
                            graphics.lineTo(clipX1, clipY1);
                            graphics.lineTo(clipX2, clipY2);
                            graphics.closePath();
                            graphics.clip();
                            graphics.transform(mxx, myx, mxy, myy, tx, ty);
                            graphics.drawImage(texture, 0, 0);

                            graphics.restore();
                        }
                    }
                    else graphics.strokePolygon(xPoints, yPoints, validPoints);
                }
            }
        }
        finally {graphics.restore();}
    }

    public static void main(String[] args) 
    {
        launch(args);
    }
}
