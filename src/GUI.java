import java.net.URL;
import java.util.HashMap;

import java.awt.Desktop;

import javafx.application.Application;
import javafx.application.Platform;
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
import javafx.scene.input.MouseEvent;

import com.technicjelle.UpdateChecker;

//TODO Magical Map
// - Implement Text Tool

//DONE Magical Map
// - Built GUI
// - Added Miscellaneous
// - Clean up methods and give headers

//TODO Battle Unit Tool
// - Attack Property Tabs
// - Enemy Formations?
// - Research Unknown Flags more
// - Randomizer Option
// - Add BattleUnitSetup Alliance field?
// - Stage Objects

//DONE Battle Unit Tool
// - Reorder files to be in logical order
// - Combine preset buttons into one
// - Add options menu

//TODO Item Data Tool
// - Implement Mystery Patch Option [ItemEvent_Naniga_Okorukana battle_item_data.o]
// - Flip panel item not working? Implement patch?
// - Implement missing badge fields
// - More field objects? (Big Blocks?)
// - Tooltips?

//DONE Item Data Tool
// - Add options menu/button
// - Add Pit rewards table from jon   11320
// - Happy Lucky Lottery Table from gor
// - Add Boo Quiz table from jin
// - Add Fresh Pasta shop from pik
// - Inn items
// - Add additonal Badge fields (Pretty Lucky odds/etc)
// - Implement Field item changes/coordinates
// - Open previously opened root
// - Have it open all specified rels in json
// - Make saving start from root
// - Reorder files to be in logical order
// - Fix bug, clear selections from other tables when clicking on new one

//TODO Miscellaneous Edits
// - SP Multiplier fields
// - Merlee Probability fields
// - Tooltips?

//DONE Miscellaneous Edits
// - Implemented

public class GUI extends Application 
{
    //TODO Change version
    public static final String version = "1.0.0";
    public boolean doUpdateCheck = true;

    Stage window;
    BorderPane borderPane = new BorderPane();

    HBox topMenu = new HBox();
    Button aboutButton = new Button("About");

    Button BUTButton = new Button("Battle Unit Tool");
    Button IDTButton = new Button("Item Data Tool");
    //Button TETButton = new Button("Text Editor Tool");
    Button TETButton = new Button();
    Button MMButton = new Button("Miscellaneous Edits");
    HBox centerMenu = new HBox();

    String buttonStyle = "-fx-font-size: 12px; -fx-font-weight: bold;";
    
    HashMap<String, Image> images = new HashMap<String, Image>();

    @Override
    public void start(Stage primaryStage) 
    {
        images = setImages(images);

        //Window
        window = primaryStage;
        window.setTitle("Magical Map");

        //Menu Buttons
        topMenu.getChildren().add(aboutButton);
        topMenu.setPadding(new Insets(5));
        topMenu.setSpacing(5);

        //Alligning Menu Buttons to Top
        borderPane.setTop(topMenu);
        topMenu.setAlignment(Pos.CENTER_RIGHT);

        //Scene
        Scene emptyScene = new Scene(borderPane, 400, 200);
        window.setScene(emptyScene);

        //Buttons
        BUTButton.setGraphic(fieldImageViewCreator(images.get("unit")));
        IDTButton.setGraphic(fieldImageViewCreator(images.get("itemsIcon")));
        //TETButton.setGraphic(fieldImageViewCreator(images.get("textBubble")));
        MMButton.setGraphic(fieldImageViewCreator(images.get("cog")));
        BUTButton.setPrefSize(170, 50);
        IDTButton.setPrefSize(170, 50);
        TETButton.setPrefSize(170, 50);
        MMButton.setPrefSize(170, 50);
        BUTButton.setStyle(buttonStyle);
        IDTButton.setStyle(buttonStyle);
        TETButton.setStyle(buttonStyle);
        MMButton.setStyle(buttonStyle);
        TETButton.setDisable(true);

        HBox topRow = new HBox();
        topRow.getChildren().addAll(BUTButton, IDTButton);
        topRow.setSpacing(5);
        topRow.setAlignment(Pos.CENTER);
        HBox bottomRow = new HBox();
        bottomRow.getChildren().addAll(TETButton, MMButton);
        bottomRow.setSpacing(5);
        bottomRow.setAlignment(Pos.CENTER);
        VBox buttons = new VBox();
        buttons.getChildren().addAll(topRow, bottomRow);
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
                        e.printStackTrace();
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
                        e.printStackTrace();
                    }
                });
            }
        });

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
                        e.printStackTrace();
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

                VBox alertMenu = new VBox();
                alertMenu.setAlignment(Pos.CENTER);
                Text versionText = new Text("Magical Map Version: " + version);
                versionText.setWrappingWidth(290);
                versionText.setTextAlignment(TextAlignment.CENTER);
                Text creditText = new Text("Magical Map Written by Jemaroo");
                creditText.setWrappingWidth(290);
                creditText.setTextAlignment(TextAlignment.CENTER);
                Text description = new Text("Magical Map contains a variety of modding tools that allow you to modify TTYD.");
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
                        updateMessage.setStyle("-fx-text-fill: black; -fx-underline: false;");
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
                                e.printStackTrace();
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

                updateBox.setScene(alertScene);
                updateBox.initModality(Modality.APPLICATION_MODAL);
                updateBox.show();
            }
        }

        //BUTButton.fire();
        //IDTButton.fire();
        //MMButton.fire();
    }

    /**
     * @Author Jemaroo
     * @Function Adds every image to the hashmap
     */
    public static HashMap<String, Image> setImages(HashMap<String, Image> imagesList)
    {
        addImage(imagesList, "actionCommandStar", "actionCommandStar.png");
        addImage(imagesList, "allergicStatus", "allergicStatus.png");
        addImage(imagesList, "allOrNothing", "allOrNothing.png");
        addImage(imagesList, "allOrNothingP", "allOrNothingP.png");
        addImage(imagesList, "appealAction", "appealAction.png");
        addImage(imagesList, "attackDownStatus", "attackDownStatus.png");
        addImage(imagesList, "attackFXB", "attackFXB.png");
        addImage(imagesList, "attackFXG", "attackFXG.png");
        addImage(imagesList, "attackFXP", "attackFXP.png");
        addImage(imagesList, "attackFXR", "attackFXR.png");
        addImage(imagesList, "attackFXY", "attackFXY.png");
        addImage(imagesList, "attackUpStatus", "attackUpStatus.png");
        addImage(imagesList, "audienceBone", "audienceBone.png");
        addImage(imagesList, "audienceBooCustom", "audienceBooCustom.png");
        addImage(imagesList, "audienceBulkyBobombCustom", "audienceBulkyBob-ombCustom.png");
        addImage(imagesList, "audienceCan", "audienceCan.png");
        addImage(imagesList, "audienceDayzeeCustom", "audienceDayzeeCustom.png");
        addImage(imagesList, "audienceDullBonesCustom", "audienceDullBonesCustom.png");
        addImage(imagesList, "audienceGoombaCustom", "audienceGoombaCustom.png");
        addImage(imagesList, "audienceHammer", "audienceHammer.png");
        addImage(imagesList, "audienceHammerBroCustom", "audienceHammerBroCustom.png");
        addImage(imagesList, "audienceKoopaCustom", "audienceKoopaCustom.png");
        addImage(imagesList, "audienceLuigiCustom", "audienceLuigiCustom.png");
        addImage(imagesList, "audiencePiranhaPlantCustom", "audiencePiranhaPlantCustom.png");
        addImage(imagesList, "audiencePuniCustom", "audiencePuniCustom.png");
        addImage(imagesList, "audienceRock", "audienceRock.png");
        addImage(imagesList, "audienceShyGuyCustom", "audienceShyGuyCustom.png");
        addImage(imagesList, "audienceStar", "audienceStar.png");
        addImage(imagesList, "audienceToadCustom", "audienceToadCustom.png");
        addImage(imagesList, "audienceXNautCustom", "audienceX-NautCustom.png");
        addImage(imagesList, "autograph", "autograph.png");
        addImage(imagesList, "badgeMenuCustom", "badgeMenuCustom.png");
        addImage(imagesList, "badgeMouseCustom", "badgeMouseCustom.png");
        addImage(imagesList, "banditCustom", "banditCustom.png");
        addImage(imagesList, "battleTrunks", "battleTrunks.png");
        addImage(imagesList, "bingoCustom", "bingoCustom.png");
        addImage(imagesList, "bingoFlower", "bingoFlower.png");
        addImage(imagesList, "bingoMushroom", "bingoMushroom.png");
        addImage(imagesList, "bingoPoisonMushroom", "bingoPoisonMushroom.png");
        addImage(imagesList, "bingoShineSprite", "bingoShineSprite.png");
        addImage(imagesList, "bingoStar", "bingoStar.png");
        addImage(imagesList, "blackKey", "blackKey.png");
        addImage(imagesList, "blanket", "blanket.png");
        addImage(imagesList, "blimpTicket", "blimpTicket.png");
        addImage(imagesList, "blueKey", "blueKey.png");
        addImage(imagesList, "bluePotion", "bluePotion.png");
        addImage(imagesList, "boatCurse", "boatCurse.png");
        addImage(imagesList, "bobberyPartnerSwitch", "bobberyPartnerSwitch.png");
        addImage(imagesList, "bombertoCustom", "bombertoCustom.png");
        addImage(imagesList, "boosSheet", "boosSheet.png");
        addImage(imagesList, "boots", "boots.png");
        addImage(imagesList, "bowserMeat", "bowserMeat.png");
        addImage(imagesList, "box", "box.png");
        addImage(imagesList, "BPEmblemCustom", "BPEmblemCustom.png");
        addImage(imagesList, "BPUpgrade", "BPUpgrade.png");
        addImage(imagesList, "brickBlock", "brickBlock.png");
        addImage(imagesList, "briefcase", "briefcase.png");
        addImage(imagesList, "bubCustom", "bubCustom.png");
        addImage(imagesList, "bumpAttack", "bumpAttack.png");
        addImage(imagesList, "burnCustom", "burnCustom.png");
        addImage(imagesList, "burnStatus", "burnStatus.png");
        addImage(imagesList, "burstShieldCustom", "burstShieldCustom.png");
        addImage(imagesList, "businessmanCustom", "businessmanCustom.png");
        addImage(imagesList, "cake", "cake.png");
        addImage(imagesList, "cakeMix", "cakeMix.png");
        addImage(imagesList, "cardKey1", "cardKey1.png");
        addImage(imagesList, "cardKey2", "cardKey2.png");
        addImage(imagesList, "cardKey3", "cardKey3.png");
        addImage(imagesList, "cardKey4", "cardKey4.png");
        addImage(imagesList, "castleKey", "castleKey.png");
        addImage(imagesList, "charlietonCustom", "charlietonCustom.png");
        addImage(imagesList, "charge", "charge.png");
        addImage(imagesList, "chargeAction", "chargeAction.png");
        addImage(imagesList, "chargeBadge", "chargeBadge.png");
        addImage(imagesList, "chargeP", "chargeP.png");
        addImage(imagesList, "chefShimiCustom", "chefShimiCustom.png");
        addImage(imagesList, "chest", "chest.png");
        addImage(imagesList, "chetRippoCustom", "chetRippoCustom.png");
        addImage(imagesList, "chillOut", "chillOut.png");
        addImage(imagesList, "chocoCake", "chocoCake.png");
        addImage(imagesList, "chuckolaCola", "chuckolaCola.png");
        addImage(imagesList, "closeCall", "closeCall.png");
        addImage(imagesList, "closeCallP", "closeCallP.png");
        addImage(imagesList, "coconut", "coconut.png");
        addImage(imagesList, "coconutBomb", "coconutBomb.png");
        addImage(imagesList, "cocoCandy", "cocoCandy.png");
        addImage(imagesList, "cog", "cog.png");
        addImage(imagesList, "coin", "coin.png");
        addImage(imagesList, "colorWheelCustom", "colorWheelCustom.png");
        addImage(imagesList, "confuseStatus", "confuseStatus.png");
        addImage(imagesList, "contactLens", "contactLens.png");
        addImage(imagesList, "cookbook", "cookbook.png");
        addImage(imagesList, "couplesCake", "couplesCake.png");
        addImage(imagesList, "courageMeal", "courageMeal.png");
        addImage(imagesList, "courageShell", "courageShell.png");
        addImage(imagesList, "crystalStar", "crystalStar.png");
        addImage(imagesList, "damageDodge", "damageDodge.png");
        addImage(imagesList, "damageDodgeP", "damageDodgeP.png");
        addImage(imagesList, "dangerHeartCustom", "dangerHeartCustom.png");
        addImage(imagesList, "dataDisk", "dataDisk.png");
        addImage(imagesList, "dazzleCustom", "dazzleCustom.png");
        addImage(imagesList, "defend", "defend.png");
        addImage(imagesList, "defendPlus", "defendPlus.png");
        addImage(imagesList, "defendPlusP", "defendPlusP.png");
        addImage(imagesList, "defenseDownStatus", "defenseDownStatus.png");
        addImage(imagesList, "defenseUpStatus", "defenseUpStatus.png");
        addImage(imagesList, "diamondStar", "diamondStar.png");
        addImage(imagesList, "diminishingStarsCustom", "diminishingStarsCustom.png");
        addImage(imagesList, "dizzyDial", "dizzyDial.png");
        addImage(imagesList, "dizzyStatus", "dizzyStatus.png");
        addImage(imagesList, "dodgyStatus", "dodgyStatus.png");
        addImage(imagesList, "dooganCustom", "dooganCustom.png");
        addImage(imagesList, "doubleDip", "doubleDip.png");
        addImage(imagesList, "doubleDipP", "doubleDipP.png");
        addImage(imagesList, "doublePain", "doublePain.png");
        addImage(imagesList, "driedbouquet", "driedbouquet.png");
        addImage(imagesList, "driedShroom", "driedShroom.png");
        addImage(imagesList, "dubiousPaper", "dubiousPaper.png");
        addImage(imagesList, "earthQuake", "earthQuake.png");
        addImage(imagesList, "eggBomb", "eggBomb.png");
        addImage(imagesList, "electricStatus", "electricStatus.png");
        addImage(imagesList, "electroPop", "electroPop.png");
        addImage(imagesList, "elevatorKey1", "elevatorKey1.png");
        addImage(imagesList, "elevatorKey2", "elevatorKey2.png");
        addImage(imagesList, "elevatorKey3", "elevatorKey3.png");
        addImage(imagesList, "emeraldStar", "emeraldStar.png");
        addImage(imagesList, "emptyBingoCustom", "emptyBingoCustom.png");
        addImage(imagesList, "fahrOutpostBombCustom", "fahrOutpostBombCustom.png");
        addImage(imagesList, "fastStatus", "fastStatus.png");
        addImage(imagesList, "feelingFine", "feelingFine.png");
        addImage(imagesList, "feelingFineP", "feelingFineP.png");
        addImage(imagesList, "femaleToadCustom", "femaleToadCustom.png");
        addImage(imagesList, "fireDrive", "fireDrive.png");
        addImage(imagesList, "fireFlower", "fireFlower.png");
        addImage(imagesList, "firePop", "firePop.png");
        addImage(imagesList, "fireShieldCustom", "fireShieldCustom.png");
        addImage(imagesList, "firstAttack", "firstAttack.png");
        addImage(imagesList, "fishCustom", "fishCustom.png");
        addImage(imagesList, "flipPanel", "flipPanel.png");
        addImage(imagesList, "flower", "flower.png");
        addImage(imagesList, "flowerFinder", "flowerFinder.png");
        addImage(imagesList, "flowerSaver", "flowerSaver.png");
        addImage(imagesList, "flowerSaverP", "flowerSaverP.png");
        addImage(imagesList, "flurriePartnerSwitch", "flurriePartnerSwitch.png");
        addImage(imagesList, "FPDrain", "FPDrain.png");
        addImage(imagesList, "FPDrainP", "FPDrainP.png");
        addImage(imagesList, "FPPlus", "FPPlus.png");
        addImage(imagesList, "FPRegenStatus", "FPRegenStatus.png");
        addImage(imagesList, "FPUpgrade", "FPUpgrade.png");
        addImage(imagesList, "freezeStatus", "freezeStatus.png");
        addImage(imagesList, "freshJuice", "freshJuice.png");
        addImage(imagesList, "freshPasta", "freshPasta.png");
        addImage(imagesList, "friedEgg", "friedEgg.png");
        addImage(imagesList, "frightMask", "frightMask.png");
        addImage(imagesList, "frontSpikeCustom", "frontSpikeCustom.png");
        addImage(imagesList, "fruitParfait", "fruitParfait.png");
        addImage(imagesList, "galleyPot", "galleyPot.png");
        addImage(imagesList, "garnetStar", "garnetStar.png");
        addImage(imagesList, "gateHandle", "gateHandle.png");
        addImage(imagesList, "gearMenuCustom", "gearMenuCustom.png");
        addImage(imagesList, "goldBar", "goldBar.png");
        addImage(imagesList, "goldBarX3", "goldBarX3.png");
        addImage(imagesList, "goldbobCustom", "goldbobCustom.png");
        addImage(imagesList, "goldbobGuide1", "goldbobGuide1.png");
        addImage(imagesList, "goldbobGuide2", "goldbobGuide2.png");
        addImage(imagesList, "goldbobGuide3", "goldbobGuide3.png");
        addImage(imagesList, "goldCard", "goldCard.png");
        addImage(imagesList, "goldenLeaf", "goldenLeaf.png");
        addImage(imagesList, "goldRing", "goldRing.png");
        addImage(imagesList, "goldStar", "goldStar.png");
        addImage(imagesList, "goombellaPartnerSwitch", "goombellaPartnerSwitch.png");
        addImage(imagesList, "gradualSyrup", "gradualSyrup.png");
        addImage(imagesList, "greenPotion", "greenPotion.png");
        addImage(imagesList, "grottoKey", "grottoKey.png");
        addImage(imagesList, "hammer", "hammer.png");
        addImage(imagesList, "hammerMan", "hammerMan.png");
        addImage(imagesList, "hammerThrow", "hammerThrow.png");
        addImage(imagesList, "happyFlower", "happyFlower.png");
        addImage(imagesList, "happyHeart", "happyHeart.png");
        addImage(imagesList, "happyHeartP", "happyHeartP.png");
        addImage(imagesList, "headRattle", "headRattle.png");
        addImage(imagesList, "healthySalad", "healthySalad.png");
        addImage(imagesList, "heart", "heart.png");
        addImage(imagesList, "heartFinder", "heartFinder.png");
        addImage(imagesList, "heartfulCake", "heartfulCake.png");
        addImage(imagesList, "hoggleCustom", "hoggleCustom.png");
        addImage(imagesList, "honeyCandy", "honeyCandy.png");
        addImage(imagesList, "honeyShroom", "honeyShroom.png");
        addImage(imagesList, "honeySuper", "honeySuper.png");
        addImage(imagesList, "honeySyrup", "honeySyrup.png");
        addImage(imagesList, "honeyUltra", "honeyUltra.png");
        addImage(imagesList, "horsetail", "horsetail.png");
        addImage(imagesList, "hotDog", "hotDog.png");
        addImage(imagesList, "hotSauce", "hotSauce.png");
        addImage(imagesList, "houseKey", "houseKey.png");
        addImage(imagesList, "HPDrain1", "HPDrain1.png");
        addImage(imagesList, "HPDrain2", "HPDrain2.png");
        addImage(imagesList, "HPDrainP", "HPDrainP.png");
        addImage(imagesList, "HPPlus", "HPPlus.png");
        addImage(imagesList, "HPPlusP", "HPPlusP.png");
        addImage(imagesList, "HPRegenStatus", "HPRegenStatus.png");
        addImage(imagesList, "HPUpgrade", "HPUpgrade.png");
        addImage(imagesList, "hugeStatus", "hugeStatus.png");
        addImage(imagesList, "iceCustom", "iceCustom.png");
        addImage(imagesList, "icePower", "icePower.png");
        addImage(imagesList, "iceShieldCustom", "iceShieldCustom.png");
        addImage(imagesList, "iceSmash", "iceSmash.png");
        addImage(imagesList, "iceStorm", "iceStorm.png");
        addImage(imagesList, "iciclePop", "iciclePop.png");
        addImage(imagesList, "ignoreStatus", "ignoreStatus.png");
        addImage(imagesList, "inkPasta", "inkPasta.png");
        addImage(imagesList, "inkySauce", "inkySauce.png");
        addImage(imagesList, "innCoupon", "innCoupon.png");
        addImage(imagesList, "innkeeperToadCustom", "innkeeperToadCustom.png");
        addImage(imagesList, "invisibleStatus", "invisibleStatus.png");
        addImage(imagesList, "ishnailCustom", "ishnailCustom.png");
        addImage(imagesList, "itemBlock", "itemBlock.png");
        addImage(imagesList, "itemHog", "itemHog.png");
        addImage(imagesList, "itemsIcon", "itemsIcon.png");
        addImage(imagesList, "jamminJelly", "jamminJelly.png");
        addImage(imagesList, "jellyCandy", "jellyCandy.png");
        addImage(imagesList, "jellyShroom", "jellyShroom.png");
        addImage(imagesList, "jellySuper", "jellySuper.png");
        addImage(imagesList, "jellyUltra", "jellyUltra.png");
        addImage(imagesList, "joleneCustom", "joleneCustom.png");
        addImage(imagesList, "journalMenuCustom", "journalMenuCustom.png");
        addImage(imagesList, "jumpMan", "jumpMan.png");
        addImage(imagesList, "keelMango", "keelMango.png");
        addImage(imagesList, "koopaBun", "koopaBun.png");
        addImage(imagesList, "koopaCurse", "koopaCurse.png");
        addImage(imagesList, "koopaCustom", "koopaCustom.png");
        addImage(imagesList, "koopaTea", "koopaTea.png");
        addImage(imagesList, "koopasta", "koopasta.png");
        addImage(imagesList, "koopsPartnerSwitch", "koopsPartnerSwitch.png");
        addImage(imagesList, "lahlaCustom", "lahlaCustom.png");
        addImage(imagesList, "lastStand", "lastStand.png");
        addImage(imagesList, "lastStandP", "lastStandP.png");
        addImage(imagesList, "lEmblem", "lEmblem.png");
        addImage(imagesList, "lifeShroom", "lifeShroom.png");
        addImage(imagesList, "lotteryPick", "lotteryPick.png");
        addImage(imagesList, "lovePudding", "lovePudding.png");
        addImage(imagesList, "luckyCustom", "luckyCustom.png");
        addImage(imagesList, "luckyDay", "luckyDay.png");
        addImage(imagesList, "luckyDayP", "luckyDayP.png");
        addImage(imagesList, "luckyStart", "luckyStart.png");
        addImage(imagesList, "luckyStartP", "luckyStartP.png");
        addImage(imagesList, "lumpyCustom", "lumpyCustom.png");
        addImage(imagesList, "lvl1Move", "lvl1Move.png");
        addImage(imagesList, "lvl2Move", "lvl2Move.png");
        addImage(imagesList, "lvl3Move", "lvl3Move.png");
        addImage(imagesList, "lvl4Move", "lvl4Move.png");
        addImage(imagesList, "magicalMap1", "magicalMap1.png");
        addImage(imagesList, "magicalMap2", "magicalMap2.png");
        addImage(imagesList, "mailboxSP", "mailboxSP.png");
        addImage(imagesList, "mailCustom", "mailCustom.png");
        addImage(imagesList, "mangoDelight", "mangoDelight.png");
        addImage(imagesList, "mapleShroom", "mapleShroom.png");
        addImage(imagesList, "mapleSuper", "mapleSuper.png");
        addImage(imagesList, "mapleSyrup", "mapleSyrup.png");
        addImage(imagesList, "mapleUltra", "mapleUltra.png");
        addImage(imagesList, "marioHeadCustom", "marioHeadCustom.png");
        addImage(imagesList, "marioMenuCustom", "marioMenuCustom.png");
        addImage(imagesList, "marioWantedPoster", "marioWantedPoster.png");
        addImage(imagesList, "mayorDourCustom", "mayorDourCustom.png");
        addImage(imagesList, "mcgoombaCustom", "mcgoombaCustom.png");
        addImage(imagesList, "megaJump", "megaJump.png");
        addImage(imagesList, "megaQuake", "megaQuake.png");
        addImage(imagesList, "megaRush", "megaRush.png");
        addImage(imagesList, "megaRushP", "megaRushP.png");
        addImage(imagesList, "megaSmash", "megaSmash.png");
        addImage(imagesList, "merleeCustom", "merleeCustom.png");
        addImage(imagesList, "merlonCustom", "merlonCustom.png");
        addImage(imagesList, "merluvleeCustom", "merluvleeCustom.png");
        addImage(imagesList, "meteorMeal", "meteorMeal.png");
        addImage(imagesList, "miniMrMini", "miniMrMini.png");
        addImage(imagesList, "mistake", "mistake.png");
        addImage(imagesList, "moneyMoney", "moneyMoney.png");
        addImage(imagesList, "moonStone", "moonStone.png");
        addImage(imagesList, "mousseCake", "mousseCake.png");
        addImage(imagesList, "moverCustom", "moverCustom.png");
        addImage(imagesList, "mowzPartnerSwitch", "mowzPartnerSwitch.png");
        addImage(imagesList, "mrsoftener", "mrsoftener.png");
        addImage(imagesList, "multibounce", "multibounce.png");
        addImage(imagesList, "mushroom", "mushroom.png");
        addImage(imagesList, "musicNoteCustom", "musicNoteCustom.png");
        addImage(imagesList, "mystery", "mystery.png");
        addImage(imagesList, "mysticEgg", "mysticEgg.png");
        addImage(imagesList, "necklace", "necklace.png");
        addImage(imagesList, "nothing", "nothing.png");
        addImage(imagesList, "oldLetter", "oldLetter.png");
        addImage(imagesList, "omeletteMeal", "omeletteMeal.png");
        addImage(imagesList, "orangePotion", "orangePotion.png");
        addImage(imagesList, "p", "p.png");
        addImage(imagesList, "palaceKey1", "palaceKey1.png");
        addImage(imagesList, "palaceKey2", "palaceKey2.png");
        addImage(imagesList, "paperCurse", "paperCurse.png");
        addImage(imagesList, "partnerlvl1Custom", "partnerlvl1Custom.png");
        addImage(imagesList, "partyMenuCustom", "partyMenuCustom.png");
        addImage(imagesList, "paybackStatus", "paybackStatus.png");
        addImage(imagesList, "PDownDUp", "PDownDUp.png");
        addImage(imagesList, "PDownDUpP", "PDownDUpP.png");
        addImage(imagesList, "peachTart", "peachTart.png");
        addImage(imagesList, "peachyPeach", "peachyPeach.png");
        addImage(imagesList, "peekaboo", "peekaboo.png");
        addImage(imagesList, "pianta", "pianta.png");
        addImage(imagesList, "piercingBlow", "piercingBlow.png");
        addImage(imagesList, "pinkBobombCustom", "pinkBob-ombCustom.png");
        addImage(imagesList, "pityFlower", "pityFlower.png");
        addImage(imagesList, "pityFlowerP", "pityFlowerP.png");
        addImage(imagesList, "planeCurse", "planeCurse.png");
        addImage(imagesList, "platinumCard", "platinumCard.png");
        addImage(imagesList, "poisonCustom", "poisonCustom.png");
        addImage(imagesList, "poisonedCake", "poisonedCake.png");
        addImage(imagesList, "poisonShroom", "poisonShroom.png");
        addImage(imagesList, "poisonStatus", "poisonStatus.png");
        addImage(imagesList, "pointSwap", "pointSwap.png");
        addImage(imagesList, "powBlock", "powBlock.png");
        addImage(imagesList, "powerBounce", "powerBounce.png");
        addImage(imagesList, "powerJump", "powerJump.png");
        addImage(imagesList, "powerPlus", "powerPlus.png");
        addImage(imagesList, "powerPlusP", "powerPlusP.png");
        addImage(imagesList, "powerPunch", "powerPunch.png");
        addImage(imagesList, "powerRush", "powerRush.png");
        addImage(imagesList, "powerRushP", "powerRushP.png");
        addImage(imagesList, "powerSmash", "powerSmash.png");
        addImage(imagesList, "preFrontSpikeCustom", "preFrontSpikeCustom.png");
        addImage(imagesList, "present", "present.png");
        addImage(imagesList, "prettyLucky", "prettyLucky.png");
        addImage(imagesList, "prettyLuckyP", "prettyLuckyP.png");
        addImage(imagesList, "pungentCustom", "pungentCustom.png");
        addImage(imagesList, "puniCustom", "puniCustom.png");
        addImage(imagesList, "puniElderCustom", "puniElderCustom.png");
        addImage(imagesList, "puniOrb", "puniOrb.png");
        addImage(imagesList, "PUpDDown", "PUpDDown.png");
        addImage(imagesList, "PUpDDownP", "PUpDDownP.png");
        addImage(imagesList, "quakeHammer", "quakeHammer.png");
        addImage(imagesList, "quickChange", "quickChange.png");
        addImage(imagesList, "raggedDiary", "raggedDiary.png");
        addImage(imagesList, "rankMedalCustom", "rankMedalCustom.png");
        addImage(imagesList, "recoveryBlock", "recoveryBlock.png");
        addImage(imagesList, "redKey", "redKey.png");
        addImage(imagesList, "redPotion", "redPotion.png");
        addImage(imagesList, "refund", "refund.png");
        addImage(imagesList, "repelCape", "repelCape.png");
        addImage(imagesList, "returnPostage", "returnPostage.png");
        addImage(imagesList, "routingSlip", "routingSlip.png");
        addImage(imagesList, "rubyStar", "rubyStar.png");
        addImage(imagesList, "ruinPowder", "ruinPowder.png");
        addImage(imagesList, "runArrow", "runArrow.png");
        addImage(imagesList, "sapphireStar", "sapphireStar.png");
        addImage(imagesList, "saveBlock", "saveBlock.png");
        addImage(imagesList, "selectCursor", "selectCursor.png");
        addImage(imagesList, "serverToadCustom", "serverToadCustom.png");
        addImage(imagesList, "shellEarrings", "shellEarrings.png");
        addImage(imagesList, "shineBlock", "shineBlock.png");
        addImage(imagesList, "shineSprite", "shineSprite.png");
        addImage(imagesList, "shootingStar", "shootingStar.png");
        addImage(imagesList, "shopToadCustom", "shopToadCustom.png");
        addImage(imagesList, "shroomBroth", "shroomBroth.png");
        addImage(imagesList, "shroomCake", "shroomCake.png");
        addImage(imagesList, "shroomCrepe", "shroomCrepe.png");
        addImage(imagesList, "shroomFry", "shroomFry.png");
        addImage(imagesList, "shroomRoast", "shroomRoast.png");
        addImage(imagesList, "shroomSteak", "shroomSteak.png");
        addImage(imagesList, "shrinkStomp", "shrinkStomp.png");
        addImage(imagesList, "silverCard", "silverCard.png");
        addImage(imagesList, "simplifier", "simplifier.png");
        addImage(imagesList, "skullGem", "skullGem.png");
        addImage(imagesList, "sleepStatus", "sleepStatus.png");
        addImage(imagesList, "sleepySheep", "sleepySheep.png");
        addImage(imagesList, "sleepyStomp", "sleepyStomp.png");
        addImage(imagesList, "slowGo", "slowGo.png");
        addImage(imagesList, "slowShroom", "slowShroom.png");
        addImage(imagesList, "slowStatus", "slowStatus.png");
        addImage(imagesList, "snowBunny", "snowBunny.png");
        addImage(imagesList, "softStomp", "softStomp.png");
        addImage(imagesList, "spaceFood", "spaceFood.png");
        addImage(imagesList, "spaghetti", "spaghetti.png");
        addImage(imagesList, "specialCard", "specialCard.png");
        addImage(imagesList, "spicyPasta", "spicyPasta.png");
        addImage(imagesList, "spicySoup", "spicySoup.png");
        addImage(imagesList, "spikeShield", "spikeShield.png");
        addImage(imagesList, "spikeShieldCustom", "spikeShieldCustom.png");
        addImage(imagesList, "spitePouch", "spitePouch.png");
        addImage(imagesList, "SPOrb1", "SPOrb1.png");
        addImage(imagesList, "starKey", "starKey.png");
        addImage(imagesList, "starPiece", "starPiece.png");
        addImage(imagesList, "stationKey1", "stationKey1.png");
        addImage(imagesList, "stationKey2", "stationKey2.png");
        addImage(imagesList, "steepleKey1", "steepleKey1.png");
        addImage(imagesList, "steepleKey2", "steepleKey2.png");
        addImage(imagesList, "stopStatus", "stopStatus.png");
        addImage(imagesList, "stopwatch", "stopwatch.png");
        addImage(imagesList, "storageKey", "storageKey.png");
        addImage(imagesList, "strangeSack", "strangeSack.png");
        addImage(imagesList, "sunStone", "sunStone.png");
        addImage(imagesList, "superAppeal", "superAppeal.png");
        addImage(imagesList, "superAppealP", "superAppealP.png");
        addImage(imagesList, "superBoots", "superBoots.png");
        addImage(imagesList, "superbombomb", "superbombomb.png");
        addImage(imagesList, "superCharge", "superCharge.png");
        addImage(imagesList, "superChargeAction", "superChargeAction.png");
        addImage(imagesList, "superChargeBadge", "superChargeBadge.png");
        addImage(imagesList, "superChargeP", "superChargeP.png");
        addImage(imagesList, "superHammer", "superHammer.png");
        addImage(imagesList, "superLuigi1", "superLuigi1.png");
        addImage(imagesList, "superLuigi2", "superLuigi2.png");
        addImage(imagesList, "superLuigi3", "superLuigi3.png");
        addImage(imagesList, "superLuigi4", "superLuigi4.png");
        addImage(imagesList, "superLuigi5", "superLuigi5.png");
        addImage(imagesList, "superShroom", "superShroom.png");
        addImage(imagesList, "tacticsFlag", "tacticsFlag.png");
        addImage(imagesList, "tastyTonic", "tastyTonic.png");
        addImage(imagesList, "tattleBookCustom", "tattleBookCustom.png");
        addImage(imagesList, "textBubble", "textBubble.png");
        addImage(imagesList, "thunderBolt", "thunderBolt.png");
        addImage(imagesList, "thunderRage", "thunderRage.png");
        addImage(imagesList, "timingTutor", "timingTutor.png");
        addImage(imagesList, "tinyStatus", "tinyStatus.png");
        addImage(imagesList, "toadCustom", "toadCustom.png");
        addImage(imagesList, "toadiaCustom", "toadiaCustom.png");
        addImage(imagesList, "toodlesCustom", "toodlesCustom.png");
        addImage(imagesList, "topSpikeCustom", "topSpikeCustom.png");
        addImage(imagesList, "tornadoJump", "tornadoJump.png");
        addImage(imagesList, "tradeOff", "tradeOff.png");
        addImage(imagesList, "trainTicket", "trainTicket.png");
        addImage(imagesList, "trialStew", "trialStew.png");
        addImage(imagesList, "tripleDip", "tripleDip.png");
        addImage(imagesList, "tubeCurse", "tubeCurse.png");
        addImage(imagesList, "turtleyLeaf", "turtleyLeaf.png");
        addImage(imagesList, "twilightShopManagerWifeCustom", "twilightShopManagerWifeCustom.png");
        addImage(imagesList, "twilightTownCitizenCustom", "twilightTownCitizenCustom.png");
        addImage(imagesList, "ultraBoots", "ultraBoots.png");
        addImage(imagesList, "ultraHammer", "ultraHammer.png");
        addImage(imagesList, "ultraShroom", "ultraShroom.png");
        addImage(imagesList, "ultraStone", "ultraStone.png");
        addImage(imagesList, "unit", "unit.png");
        addImage(imagesList, "unitAmazyDayzee", "unitAmazyDayzee.png");
        addImage(imagesList, "unitArantula", "unitArantula.png");
        addImage(imagesList, "unitAtomicBoo", "unitAtomicBoo.png");
        addImage(imagesList, "unitBadgeBandit", "unitBadgeBandit.png");
        addImage(imagesList, "unitBaldCleft", "unitBaldCleft.png");
        addImage(imagesList, "unitBandit", "unitBandit.png");
        addImage(imagesList, "unitBeldam", "unitBeldam.png");
        addImage(imagesList, "unitBigBandit", "unitBigBandit.png");
        addImage(imagesList, "unitBillBlaster", "unitBillBlaster.png");
        addImage(imagesList, "unitBlooper", "unitBlooper.png");
        addImage(imagesList, "unitBobomb", "unitBob-omb.png");
        addImage(imagesList, "unitBobulk", "unitBob-ulk.png");
        addImage(imagesList, "unitBombshellBill", "unitBombshellBill.png");
        addImage(imagesList, "unitBombshellBillBlaster", "unitBombshellBillBlaster.png");
        addImage(imagesList, "unitBombSquadBomb", "unitBombSquadBomb.png");
        addImage(imagesList, "unitBonetail", "unitBonetail.png");
        addImage(imagesList, "unitBoo", "unitBoo.png");
        addImage(imagesList, "unitBoomerangBro", "unitBoomerangBro.png");
        addImage(imagesList, "unitBowser", "unitBowser.png");
        addImage(imagesList, "unitBristle", "unitBristle.png");
        addImage(imagesList, "unitBulkyBobomb", "unitBulkyBob-omb.png");
        addImage(imagesList, "unitBulletBill", "unitBulletBill.png");
        addImage(imagesList, "unitBuzzyBeetle", "unitBuzzyBeetle.png");
        addImage(imagesList, "unitChainChomp", "unitChainChomp.png");
        addImage(imagesList, "unitCleft", "unitCleft.png");
        addImage(imagesList, "unitCortez", "unitCortez.png");
        addImage(imagesList, "unitCortezBonePile", "unitCortezBonePile.png");
        addImage(imagesList, "unitCortezHook", "unitCortezHook.png");
        addImage(imagesList, "unitCortezRapier", "unitCortezRapier.png");
        addImage(imagesList, "unitCortezSaber", "unitCortezSaber.png");
        addImage(imagesList, "unitCortezSword", "unitCortezSword.png");
        addImage(imagesList, "unitCrazeeDayzee", "unitCrazeeDayzee.png");
        addImage(imagesList, "unitDarkBones", "unitDarkBones.png");
        addImage(imagesList, "unitDarkBoo", "unitDarkBoo.png");
        addImage(imagesList, "unitDarkBristle", "unitDarkBristle.png");
        addImage(imagesList, "unitDarkCraw", "unitDarkCraw.png");
        addImage(imagesList, "unitDarkKoopa", "unitDarkKoopa.png");
        addImage(imagesList, "unitDarkKoopatrol", "unitDarkKoopatrol.png");
        addImage(imagesList, "unitDarkLakitu", "unitDarkLakitu.png");
        addImage(imagesList, "unitDarkParatroopa", "unitDarkParatroopa.png");
        addImage(imagesList, "unitDarkPuff", "unitDarkPuff.png");
        addImage(imagesList, "unitDarkWizzerd", "unitDarkWizzerd.png");
        addImage(imagesList, "unitDoopliss", "unitDoopliss.png");
        addImage(imagesList, "unitDooplissMario", "unitDooplissMario.png");
        addImage(imagesList, "unitDryBones", "unitDryBones.png");
        addImage(imagesList, "unitDullBones", "unitDullBones.png");
        addImage(imagesList, "unitEliteWizzerd", "unitEliteWizzerd.png");
        addImage(imagesList, "unitEliteXNaut", "unitEliteX-Naut.png");
        addImage(imagesList, "unitEmber", "unitEmber.png");
        addImage(imagesList, "unitFireBro", "unitFireBro.png");
        addImage(imagesList, "unitFlowerFuzzy", "unitFlowerFuzzy.png");
        addImage(imagesList, "unitFrostPiranha", "unitFrostPiranha.png");
        addImage(imagesList, "unitFuzzy", "unitFuzzy.png");
        addImage(imagesList, "unitGloomba", "unitGloomba.png");
        addImage(imagesList, "unitGloomtail", "unitGloomtail.png");
        addImage(imagesList, "unitGoldFuzzy", "unitGoldFuzzy.png");
        addImage(imagesList, "unitGoomba", "unitGoomba.png");
        addImage(imagesList, "unitGreenFuzzy", "unitGreenFuzzy.png");
        addImage(imagesList, "unitGreenMagikoopa", "unitGreenMagikoopa.png");
        addImage(imagesList, "unitGrodus", "unitGrodus.png");
        addImage(imagesList, "unitGrodusX", "unitGrodusX.png");
        addImage(imagesList, "unitGus", "unitGus.png");
        addImage(imagesList, "unitHammerBro", "unitHammerBro.png");
        addImage(imagesList, "unitHooktail", "unitHooktail.png");
        addImage(imagesList, "unitHyperBaldCleft", "unitHyperBaldCleft.png");
        addImage(imagesList, "unitHyperCleft", "unitHyperCleft.png");
        addImage(imagesList, "unitHyperGoomba", "unitHyperGoomba.png");
        addImage(imagesList, "unitHyperParagoomba", "unitHyperParagoomba.png");
        addImage(imagesList, "unitHyperSpikyGoomba", "unitHyperSpikyGoomba.png");
        addImage(imagesList, "unitIcePuff", "unitIcePuff.png");
        addImage(imagesList, "unitIronCleft1", "unitIronCleft1.png");
        addImage(imagesList, "unitIronCleft2", "unitIronCleft2.png");
        addImage(imagesList, "unitKammyKoopa", "unitKammyKoopa.png");
        addImage(imagesList, "unitKoopaTroopa", "unitKoopaTroopa.png");
        addImage(imagesList, "unitKoopatrol", "unitKoopatrol.png");
        addImage(imagesList, "unitKPKoopa", "unitKPKoopa.png");
        addImage(imagesList, "unitKPParatroopa", "unitKPParatroopa.png");
        addImage(imagesList, "unitLakitu", "unitLakitu.png");
        addImage(imagesList, "unitLavaBubble", "unitLavaBubble.png");
        addImage(imagesList, "unitLordCrump", "unitLordCrump.png");
        addImage(imagesList, "unitMachoGrubba", "unitMachoGrubba.png");
        addImage(imagesList, "unitMagikoopa", "unitMagikoopa.png");
        addImage(imagesList, "unitMagnusVonGrapple", "unitMagnusVonGrapple.png");
        addImage(imagesList, "unitMagnusVonGrapple2", "unitMagnusVonGrapple2.png");
        addImage(imagesList, "unitMarilyn", "unitMarilyn.png");
        addImage(imagesList, "unitMiniXYux", "unitMini-X-Yux.png");
        addImage(imagesList, "unitMiniYux", "unitMini-Yux.png");
        addImage(imagesList, "unitMiniZYux", "unitMini-Z-Yux.png");
        addImage(imagesList, "unitMoonCleft", "unitMoonCleft.png");
        addImage(imagesList, "unitPalePiranha", "unitPalePiranha.png");
        addImage(imagesList, "unitParabuzzy", "unitParabuzzy.png");
        addImage(imagesList, "unitParagloomba", "unitParagloomba.png");
        addImage(imagesList, "unitParagoomba", "unitParagoomba.png");
        addImage(imagesList, "unitParatroopa", "unitParakoopa.png");
        addImage(imagesList, "unitPhantomEmber", "unitPhantomEmber.png");
        addImage(imagesList, "unitPider", "unitPider.png");
        addImage(imagesList, "unitPiranhaPlant", "unitPiranhaPlant.png");
        addImage(imagesList, "unitPoisonPokey", "unitPoisonPokey.png");
        addImage(imagesList, "unitPoisonPuff", "unitPoisonPuff.png");
        addImage(imagesList, "unitPokey", "unitPokey.png");
        addImage(imagesList, "unitProfessorFrankly", "unitProfessorFrankly.png");
        addImage(imagesList, "unitPutridPiranha", "unitPutridPiranha.png");
        addImage(imagesList, "unitRawkHawk", "unitRawkHawk.png");
        addImage(imagesList, "unitRedBones", "unitRedBones.png");
        addImage(imagesList, "unitRedChomp", "unitRedChomp.png");
        addImage(imagesList, "unitRedMagikoopa", "unitRedMagikoopa.png");
        addImage(imagesList, "unitRedSpikyBuzzy", "unitRedSpikyBuzzy.png");
        addImage(imagesList, "unitRuffPuff", "unitRuffPuff.png");
        addImage(imagesList, "unitShadowHand1", "unitShadowHand1.png");
        addImage(imagesList, "unitShadowHand2", "unitShadowHand2.png");
        addImage(imagesList, "unitShadowPeach", "unitShadowPeach.png");
        addImage(imagesList, "unitShadowQueen", "unitShadowQueen.png");
        addImage(imagesList, "unitShadyKoopa", "unitShadyKoopa.png");
        addImage(imagesList, "unitShadyParatroopa", "unitShadyParatroopa.png");
        addImage(imagesList, "unitShellShield", "unitShellShield.png");
        addImage(imagesList, "unitSkyBlueSpiny", "unitSkyBlueSpiny.png");
        addImage(imagesList, "unitSmallShadowHands", "unitSmallShadowHands.png");
        addImage(imagesList, "unitSmorg", "unitSmorg.png");
        addImage(imagesList, "unitSpania", "unitSpania.png");
        addImage(imagesList, "unitSpikyBuzzy", "unitSpikyBuzzy.png");
        addImage(imagesList, "unitSpikyGloomba", "unitSpikyGloomba.png");
        addImage(imagesList, "unitSpikyGoomba", "unitSpikyGoomba.png");
        addImage(imagesList, "unitSpikyParabuzzy", "unitSpikyParabuzzy.png");
        addImage(imagesList, "unitSpinia", "unitSpinia.png");
        addImage(imagesList, "unitSpiny", "unitSpiny.png");
        addImage(imagesList, "unitSpunia", "unitSpunia.png");
        addImage(imagesList, "unitSwampire", "unitSwampire.png");
        addImage(imagesList, "unitSwooper", "unitSwooper.png");
        addImage(imagesList, "unitSwoopula", "unitSwoopula.png");
        addImage(imagesList, "unitVivian", "unitVivian.png");
        addImage(imagesList, "unitWhiteMagikoopa", "unitWhiteMagikoopa.png");
        addImage(imagesList, "unitWizzerd", "unitWizzerd.png");
        addImage(imagesList, "unitXNaut", "unitX-Naut.png");
        addImage(imagesList, "unitXNautPHD", "unitX-NautPHD.png");
        addImage(imagesList, "unitXYux", "unitX-Yux.png");
        addImage(imagesList, "unitYux", "unitYux.png");
        addImage(imagesList, "unitZYux", "unitZ-Yux.png");
        addImage(imagesList, "unknown", "unknown.png");
        addImage(imagesList, "unsimplifier", "unsimplifier.png");
        addImage(imagesList, "unusedDefend", "unusedDefend.png");
        addImage(imagesList, "unusedDefendP", "unusedDefendP.png");
        addImage(imagesList, "unusedPaper", "unusedPaper.png");
        addImage(imagesList, "upArrow", "upArrow.png");
        addImage(imagesList, "usedBlock", "usedBlock.png");
        addImage(imagesList, "vitalPaper", "vitalPaper.png");
        addImage(imagesList, "vivianPartnerSwitch", "vivianPartnerSwitch.png");
        addImage(imagesList, "voltShroom", "voltShroom.png");
        addImage(imagesList, "weddingRing", "weddingRing.png");
        addImage(imagesList, "wEmblem", "wEmblem.png");
        addImage(imagesList, "whackaBump", "whackaBump.png");
        addImage(imagesList, "wonkyCustom", "wonkyCustom.png");
        addImage(imagesList, "wrestingMagazine", "wrestingMagazine.png");
        addImage(imagesList, "XP", "XP.png");
        addImage(imagesList, "yoshiPartnerSwitch", "yoshiPartnerSwitch.png");
        addImage(imagesList, "zapShieldCustom", "zapShieldCustom.png");
        addImage(imagesList, "zapTap", "zapTap.png");
        addImage(imagesList, "zessCookie", "zessCookie.png");
        addImage(imagesList, "zessDeluxe", "zessDeluxe.png");
        addImage(imagesList, "zessDinner", "zessDinner.png");
        addImage(imagesList, "zessDynamite", "zessDynamite.png");
        addImage(imagesList, "zessFrappe", "zessFrappe.png");
        addImage(imagesList, "zessSpecial", "zessSpecial.png");
        addImage(imagesList, "zessTea", "zessTea.png");

        return imagesList;
    }

    /**
     * @Author Jemaroo
     * @Function Adds a single image from the icons resource folder
     */
    private static void addImage(HashMap<String, Image> imagesList, String key, String fileName)
    {
        imagesList.put(key, new Image(GUI.class.getResource("/icons/" + fileName).toExternalForm()));
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

    public static void main(String[] args) 
    {
        launch(args);
    }
}