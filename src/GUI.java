import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.awt.Desktop;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import com.technicjelle.UpdateChecker;

//TODO Magical Map
// - Implement Palette Tool
// - Implement ISO Tools
// - Option to autoload a tool on startup?

//DONE Magical Map
// - 

//TODO Battle Unit Tool
// - Attack Property Tabs
// - Enemy Formations
// - Add BattleUnitSetup Alliance field?
// - Stage Objects

//DONE Battle Unit Tool
// - 

//TODO Item Data Tool
// - Tooltips?

//DONE Item Data Tool
// - 

//TODO Text Editor Tool
// -

//DONE Text Editor Tool
// - 

//TODO Miscellaneous Edits
// - Tooltips?

//DONE Miscellaneous Edits
// - 

public class GUI extends Application 
{
    //TODO Change version
    public static final String version = "1.1.0";
    public boolean doUpdateCheck = true;

    Stage window;
    BorderPane borderPane = new BorderPane();

    HBox topMenu = new HBox();
    Button aboutButton = new Button("About");

    Button BUTButton = new Button("Battle Unit Tool");
    Button IDTButton = new Button("Item Data Tool");
    Button TETButton = new Button("Text Editor Tool");
    //Button MPTButton = new Button("Mario Palette Tool");
    Button MPTButton = new Button("");
    //Button ISOButton = new Button("ISO Tools");
    Button ISOButton = new Button("");
    Button MMButton = new Button("Miscellaneous Edits");
    HBox centerMenu = new HBox();

    String buttonStyle = "-fx-font-size: 12px; -fx-font-weight: bold;";
    boolean darkModeActive = false;
    
    HashMap<String, Image> images = new HashMap<String, Image>();

    @Override
    public void start(Stage primaryStage) 
    {
        images = setImages(images);

        //Window
        window = primaryStage;
        window.setTitle("Magical Map");

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {showCrashWindow(thread, throwable);});

        //Menu Buttons
        topMenu.getChildren().add(aboutButton);
        topMenu.setPadding(new Insets(5));
        topMenu.setSpacing(5);

        //Alligning Menu Buttons to Top
        borderPane.setTop(topMenu);
        topMenu.setAlignment(Pos.CENTER_RIGHT);

        //Scene
        Scene emptyScene = new Scene(borderPane, 400, 250);
        window.setScene(emptyScene);

        try
        {
            File jsonFile = new File("src\\options.json");
            JSONParser parser = new JSONParser();
            JSONObject root = (JSONObject)parser.parse(new FileReader(jsonFile));

            if(((String)root.get("BUTDarkMode")).equals("true") && ((String)root.get("IDTDarkMode")).equals("true") &&
               ((String)root.get("TETDarkMode")).equals("true") && ((String)root.get("MMDarkMode")).equals("true"))
            {
                setDarkStyle(emptyScene, true);
            }
        }
        catch (FileNotFoundException e){System.out.println("There was an Error Finding the JSON File");}
        catch (IOException e){System.out.println("There was an Error Reading the JSON File");}
        catch (ParseException e){System.out.println("There was an Error Parsing the JSON File");}

        //Buttons
        BUTButton.setGraphic(fieldImageViewCreator(images.get("unit")));
        IDTButton.setGraphic(fieldImageViewCreator(images.get("itemsIcon")));
        TETButton.setGraphic(fieldImageViewCreator(images.get("textBubble")));
        //MPTButton.setGraphic(fieldImageViewCreator(images.get("marioHeadCustom")));
        //ISOButton.setGraphic(fieldImageViewCreator(images.get("dataDisk")));
        MMButton.setGraphic(fieldImageViewCreator(images.get("cog")));
        BUTButton.setPrefSize(170, 50);
        IDTButton.setPrefSize(170, 50);
        TETButton.setPrefSize(170, 50);
        MPTButton.setPrefSize(170, 50);
        ISOButton.setPrefSize(170, 50);
        MMButton.setPrefSize(170, 50);
        BUTButton.setStyle(buttonStyle);
        IDTButton.setStyle(buttonStyle);
        TETButton.setStyle(buttonStyle);
        MPTButton.setStyle(buttonStyle);
        ISOButton.setStyle(buttonStyle);
        MMButton.setStyle(buttonStyle);

        HBox topRow = new HBox();
        topRow.getChildren().addAll(BUTButton, IDTButton);
        topRow.setSpacing(5);
        topRow.setAlignment(Pos.CENTER);
        HBox middleRow = new HBox();
        middleRow.getChildren().addAll(TETButton, MPTButton);
        middleRow.setSpacing(5);
        middleRow.setAlignment(Pos.CENTER);
        HBox bottomRow = new HBox();
        bottomRow.getChildren().addAll(ISOButton, MMButton);
        bottomRow.setSpacing(5);
        bottomRow.setAlignment(Pos.CENTER);
        VBox buttons = new VBox();
        buttons.getChildren().addAll(topRow, middleRow, bottomRow);
        buttons.setSpacing(5);
        buttons.setAlignment(Pos.CENTER);
        centerMenu.getChildren().add(buttons);
        centerMenu.setAlignment(Pos.CENTER);
        borderPane.setCenter(centerMenu);

        window.getIcons().add(images.get("magicalMap1"));
        window.show();

        BUTButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                Platform.runLater(() -> 
                {
                    try 
                    {
                        Stage BUTStage = new Stage();
                        BUTStage.initOwner(window);
                        BUTStage.initModality(Modality.WINDOW_MODAL);

                        new BUTGUI().start(BUTStage);
                    } 
                    catch (Exception e) 
                    {
                        showCrashWindow(Thread.currentThread(), e);
                    }
                });
            }
        });

        IDTButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                Platform.runLater(() -> 
                {
                    try 
                    {
                        Stage IDTStage = new Stage();
                        IDTStage.initOwner(window);
                        IDTStage.initModality(Modality.WINDOW_MODAL);

                        new IDTGUI().start(IDTStage);
                    } 
                    catch (Exception e) 
                    {
                        showCrashWindow(Thread.currentThread(), e);
                    }
                });
            }
        });

        TETButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                Platform.runLater(() -> 
                {
                    try 
                    {
                        Stage TETStage = new Stage();
                        TETStage.initOwner(window);
                        TETStage.initModality(Modality.WINDOW_MODAL);

                        new TETGUI().start(TETStage);
                    } 
                    catch (Exception e) 
                    {
                        showCrashWindow(Thread.currentThread(), e);
                    }
                });
            }
        });

        MPTButton.setDisable(true);

        ISOButton.setDisable(true);

        MMButton.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override public void handle(ActionEvent event)
            {
                Platform.runLater(() -> 
                {
                    try 
                    {
                        Stage MMStage = new Stage();
                        MMStage.initOwner(window);
                        MMStage.initModality(Modality.WINDOW_MODAL);

                        new MMGUI().start(MMStage);
                    } 
                    catch (Exception e) 
                    {
                        showCrashWindow(Thread.currentThread(), e);
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
                alertBox.getIcons().add(images.get("magicalMap1"));
                alertBox.initModality(Modality.APPLICATION_MODAL);
                alertBox.setResizable(false);

                ImageView logo = new ImageView(images.get("magicalMapLogo"));
                logo.setFitWidth(100);
                logo.setFitHeight(100);

                Label versionLabel = new Label("Magical Map Version: " + version);
                versionLabel.setMaxWidth(Double.MAX_VALUE);
                versionLabel.setAlignment(Pos.CENTER);
                versionLabel.setTextAlignment(TextAlignment.CENTER);

                Label creditLabel = new Label("Magical Map Written by Jemaroo");
                creditLabel.setAlignment(Pos.CENTER);
                creditLabel.setTextAlignment(TextAlignment.CENTER);

                VBox informationBox = new VBox(6, versionLabel, creditLabel);
                informationBox.setAlignment(Pos.CENTER);

                HBox headerBox = new HBox(15, logo, informationBox);
                headerBox.setAlignment(Pos.CENTER);

                Label descriptionLabel = new Label("Magical Map contains a variety of modding tools that allow you to modify TTYD.");
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

        if(doUpdateCheck)
        {
            UpdateChecker updateChecker = new UpdateChecker("Jemaroo", "MagicalMap", version);
            updateChecker.check();
            if(updateChecker.isUpdateAvailable())
            {
                Stage updateBox = new Stage();
                updateBox.setTitle("Update Available");
                updateBox.getIcons().add(images.get("magicalMap1"));

                Label updateMessage = new Label(updateChecker.getUpdateMessage().get());
                updateMessage.setAlignment(Pos.CENTER);
                updateMessage.setTextAlignment(TextAlignment.CENTER);
                updateMessage.setOnMouseEntered(new EventHandler<MouseEvent>()
                {
                    @Override public void handle(MouseEvent event)
                    { 
                        updateMessage.setStyle("-fx-text-fill: blue; -fx-underline: true;");
                        updateMessage.setCursor(Cursor.HAND);
                    }
                });
                updateMessage.setOnMouseExited(new EventHandler<MouseEvent>() 
                {
                    @Override public void handle(MouseEvent event)
                    { 
                        if(darkModeActive) updateMessage.setStyle("-fx-text-fill: white; -fx-underline: false;");
                        else updateMessage.setStyle("-fx-text-fill: black; -fx-underline: false;");
                        updateMessage.setCursor(Cursor.DEFAULT);
                    }
                });
                updateMessage.setOnMouseClicked(new EventHandler<MouseEvent>() 
                {
                    @Override public void handle(MouseEvent event)
                    { 
                        Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                        if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) 
                        {
                            try 
                            {
                                desktop.browse(new URL("https://github.com/Jemaroo/MagicalMap/releases/latest").toURI());
                            } 
                            catch (Exception e) 
                            {
                                showCrashWindow(Thread.currentThread(), e);
                            }
                        }
                    }
                });

                VBox updateMenu = new VBox();
                updateMenu.setAlignment(Pos.CENTER);
                updateMenu.getChildren().add(updateMessage);

                StackPane alertPane = new StackPane();
                alertPane.getChildren().add(updateMenu);
                alertPane.setAlignment(Pos.CENTER);

                Scene alertScene = new Scene(alertPane, 450, 80);
                if(darkModeActive) setDarkStyle(alertScene, true);

                updateBox.setScene(alertScene);
                updateBox.initModality(Modality.APPLICATION_MODAL);
                updateBox.show();
            }
        }

        //TODO Disable Debug Commands
        //test.outputImageList(images);
        //BUTButton.fire();
        //IDTButton.fire();
        //TETButton.fire();
        //MMButton.fire();
    }

    /**
     * @Author Jemaroo
     * @Function Adds every image under the icons resource folder to the hashmap
     */
    public static HashMap<String, Image> setImages(HashMap<String, Image> imagesList)
    {
        String rootFolder = "icons/";
        URL rootURL = Thread.currentThread().getContextClassLoader().getResource(rootFolder);

        try
        {
            if (rootURL.getProtocol().equals("file"))
            {
                try (Stream<Path> paths = Files.walk(Paths.get(rootURL.toURI())))
                {
                    paths.filter(Files::isRegularFile).forEach(path ->
                    {
                        String fileName = path.getFileName().toString();
                        String lowerName = fileName.toLowerCase();

                        if (!lowerName.endsWith(".png")) {return;}

                        String key = fileName.substring(0, fileName.lastIndexOf('.'));
                        if (!key.equals("-"))
                        {
                            key = key.replace("-", "");
                        }
                        key = key.equals("unitParakoopa") ? "unitParatroopa" : key;

                        File customImage = new File("src/customIcons", fileName);
                        String imagePath = customImage.isFile() ? customImage.toURI().toString() : path.toUri().toString();

                        imagesList.put(key, new Image(imagePath));
                    });
                }
            }
            else if (rootURL.getProtocol().equals("jar"))
            {
                JarURLConnection connection = (JarURLConnection) rootURL.openConnection();
                connection.setUseCaches(false);

                try (JarFile jarFile = connection.getJarFile())
                {
                    for (JarEntry entry : Collections.list(jarFile.entries()))
                    {
                        String entryName = entry.getName();
                        String lowerName = entryName.toLowerCase();

                        if (entry.isDirectory() || !entryName.startsWith(rootFolder) || (!lowerName.endsWith(".png"))) {continue;}

                        String fileName = entryName.substring(entryName.lastIndexOf('/') + 1);
                        String key = fileName.substring(0, fileName.lastIndexOf('.'));

                        if (!key.equals("-"))
                        {
                            key = key.replace("-", "");
                        }
                        key = key.equals("unitParakoopa") ? "unitParatroopa" : key;

                        File customImage = new File("src/customIcons", fileName);
                        URL resource = Thread.currentThread().getContextClassLoader().getResource(entryName);

                        if (customImage.isFile())
                        {
                            imagesList.put(key, new Image(customImage.toURI().toString()));
                        }
                        else if (resource != null)
                        {
                            imagesList.put(key, new Image(resource.toExternalForm()));
                        }
                    }
                }
            }
        }
        catch (Exception exception) {System.err.println("Could not load images");}

        return imagesList;
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
     * @Function Displays a crash window with the error message and stack trace when an exception occurs
     */
    public void showCrashWindow(Thread thread, Throwable throwable)
    {
        throwable.printStackTrace();

        Runnable showWindow = () ->
        {
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            throwable.printStackTrace(printWriter);

            String crashLog = "Magical Map " + version + "\n" + "Thread: " + thread.getName() + "\n\n" + stringWriter.toString();

            Stage crashBox = new Stage();
            crashBox.setTitle("Magical Map - Crash");
            crashBox.initModality(Modality.APPLICATION_MODAL);

            if(images.get("magicalMap1") != null)
            {
                crashBox.getIcons().add(images.get("magicalMap1"));
            }

            Label crashLabel = new Label("Magical Map encountered an unexpected error.");
            crashLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

            Label descriptionLabel = new Label("Traceback:");

            TextArea crashText = new TextArea(crashLog);
            crashText.setEditable(false);
            crashText.setWrapText(false);
            crashText.setPrefWidth(750);
            crashText.setPrefHeight(400);

            Button copyButton = new Button("Copy Crash Log");
            copyButton.setOnAction(event ->
            {
                javafx.scene.input.ClipboardContent content = new javafx.scene.input.ClipboardContent();
                content.putString(crashLog);
                javafx.scene.input.Clipboard.getSystemClipboard().setContent(content);
            });

            Button closeButton = new Button("Close");
            closeButton.setOnAction(event -> crashBox.close());

            HBox buttons = new HBox(10, copyButton, closeButton);
            buttons.setAlignment(Pos.CENTER_RIGHT);

            VBox crashMenu = new VBox(10, crashLabel, descriptionLabel, crashText, buttons);
            crashMenu.setPadding(new Insets(15));

            Scene crashScene = new Scene(crashMenu);

            if(darkModeActive) {setDarkStyle(crashScene, true);}

            crashBox.setScene(crashScene);
            crashBox.show();
            crashBox.toFront();
        };

        if(Platform.isFxApplicationThread()) {showWindow.run();}
        else {Platform.runLater(showWindow);}
    }
    
    public static void main(String[] args) 
    {
        launch(args);
    }
}
