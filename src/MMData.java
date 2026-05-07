import java.util.ArrayList;

import javafx.scene.control.*;
import javafx.scene.paint.Color;

/**
 * @Author Jemaroo
 * @Function Storage object for handling Misc data
 */
public class MMData
{    
    public String type;
    ArrayList<Object> miscData = new ArrayList<Object>();
}

class Misc
{
    String name = "";

    //oneint
    public static class oneint extends Misc {int value = 0; TextField textField = new TextField();}

    //twoint
    public static class twoint extends Misc {int value = 0; TextField textField = new TextField();}

    //twointConstraint
    public static class twointConstraint extends Misc {int value = 0; int low; int high; TextField textField = new TextField();}

    //twointWpatch
    public static class twointWpatch extends Misc {int value = 0; boolean rewrite = false; TextField textField = new TextField();}

    //fourint
    public static class fourint extends Misc {long value = 0; TextField textField = new TextField();}

    //fourintPN
    public static class fourintPN extends Misc {long value = 0; TextField textField = new TextField();}

    //Float
    public static class Float extends Misc {float value = 0; TextField textField = new TextField();}

    //hexColor
    public static class hexColor extends Misc {Color colorValue = Color.BLACK; ColorPicker colorPicker = new ColorPicker();}

    //odds
    public static class odds extends Misc {long successRate = 0; long outOf = 0; TextField textField = new TextField();}

    //oddsRev
    public static class oddsRev extends Misc {long successRate = 0; long outOf = 0; TextField textField = new TextField();}

    //Function
    public static class Function extends Misc {boolean rewrite = false; CheckBox checkBox = new CheckBox();}

    //bingoSelectionBox
    public static class bingoSelectionBox extends Misc {int value = 0; ComboBox<String> comboBox = new ComboBox<String>();}
}