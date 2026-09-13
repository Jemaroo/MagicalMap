import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import javafx.scene.image.Image;

/**
 * @Author Jemaroo
 * @Function Storage object for handling Text
 */
public class TETData
{
    public TETData(String nIdentifier, String tnTextData)
    {
        this.identifier = nIdentifier;
        this.textData = tnTextData;
    }

    public String identifier;
    public String textData;
}

/**
 * @Author Jemaroo
 * @Function Storage object for handling Message Preview Text Formats
 */
class PreviewLayout
{
    int textX;
    int textY;
    int fontSize;
    double characterSpacing;
    double lineSpacing;
    String color;
    double posScaleX;
    double posScaleY;
    double iconBaseX;
    double iconBaseY;
    double iconPosScaleX;
    double iconPosScaleY;
    String messageStarIcon;
    double messageStarX;
    double messageStarY;

    PreviewLayout(int textX, int textY, int fontSize, double characterSpacing, double lineSpacing)
    {
        this(textX, textY, fontSize, characterSpacing, lineSpacing, "#1f1f1f", 1.0, 1.0);
    }

    PreviewLayout(int textX, int textY, int fontSize, double characterSpacing, double lineSpacing, double posScaleX, double posScaleY)
    {
        this(textX, textY, fontSize, characterSpacing, lineSpacing, "#1f1f1f", posScaleX, posScaleY);
    }
    
    PreviewLayout(int textX, int textY, int fontSize, double characterSpacing, double lineSpacing, double posScaleX, double posScaleY, String messageStarIcon, double messageStarX, double messageStarY)
    {
        this(textX, textY, fontSize, characterSpacing, lineSpacing, "#1f1f1f", posScaleX, posScaleY, 0.0, 0.0, 1.0, 1.0, messageStarIcon, messageStarX, messageStarY);
    }

    PreviewLayout(int textX, int textY, int fontSize, double characterSpacing, double lineSpacing, String color, double posScaleX, double posScaleY)
    {
        this(textX, textY, fontSize, characterSpacing, lineSpacing, color, posScaleX, posScaleY, 0.0, 0.0, 1.0, 1.0);
    }

    PreviewLayout(int textX, int textY, int fontSize, double characterSpacing, double lineSpacing, String color, double posScaleX, double posScaleY, String messageStarIcon, double messageStarX, double messageStarY)
    {
        this(textX, textY, fontSize, characterSpacing, lineSpacing, color, posScaleX, posScaleY, 0.0, 0.0, 1.0, 1.0, messageStarIcon, messageStarX, messageStarY);
    }

    PreviewLayout(int textX, int textY, int fontSize, double characterSpacing, double lineSpacing, String color, double posScaleX, double posScaleY, double iconBaseX, double iconBaseY, double iconPosScaleX, double iconPosScaleY)
    {
        this(textX, textY, fontSize, characterSpacing, lineSpacing, color, posScaleX, posScaleY, iconBaseX, iconBaseY, iconPosScaleX, iconPosScaleY, "bingoStar", 0, 0);
    }

    PreviewLayout(int textX, int textY, int fontSize, double characterSpacing, double lineSpacing, String color, double posScaleX, double posScaleY, double iconBaseX, double iconBaseY, double iconPosScaleX, double iconPosScaleY, String messageStarIcon, double messageStarX, double messageStarY)
    {
        this.textX = textX;
        this.textY = textY;
        this.fontSize = fontSize;
        this.characterSpacing = characterSpacing;
        this.lineSpacing = lineSpacing;
        this.color = color;
        this.posScaleX = posScaleX;
        this.posScaleY = posScaleY;
        this.iconBaseX = iconBaseX;
        this.iconBaseY = iconBaseY;
        this.iconPosScaleX = iconPosScaleX;
        this.iconPosScaleY = iconPosScaleY;
        this.messageStarIcon = messageStarIcon;
        this.messageStarX = messageStarX;
        this.messageStarY = messageStarY;
    }
}

/**
 * @Author Jemaroo
 * @Function Storage object for handling Message Preview <Tag> Text Formats
 */
class PreviewState
{
    double x = 0;
    double y = 0;

    int fontSize;
    double characterSpacing;
    double lineSpacing;

    double currentScale = 1.0;
    String color = "#1f1f1f";

    java.util.ArrayDeque<Double> scaleStack = new java.util.ArrayDeque<Double>();
    java.util.ArrayDeque<String> colorStack = new java.util.ArrayDeque<String>();

    PreviewState(PreviewLayout layout)
    {
        this.fontSize = layout.fontSize;
        this.characterSpacing = layout.characterSpacing;
        this.lineSpacing = layout.lineSpacing;
        this.color = layout.color;
    }
}

/**
 * @Author Jemaroo
 * @Function Storage object for Model Data
 */
class AnimModelData
{
    ArrayList<float[]> vertices = new ArrayList<>();
    ArrayList<float[]> textureCoordinates = new ArrayList<>();
    ArrayList<Integer> vertexIndices = new ArrayList<>();
    ArrayList<Integer> textureCoordinateIndices = new ArrayList<>();
    ArrayList<ModelPolygon> polygons = new ArrayList<>();
    ArrayList<ModelGroup> groups = new ArrayList<>();
    ArrayList<Image> textures = new ArrayList<>();
    ArrayList<Integer> textureMapBaseTextureIds = new ArrayList<>();
    ArrayList<Integer> textureIdToTplIndex = new ArrayList<>();
    ArrayList<Integer> textureMapBaseFrameOffsets = new ArrayList<>();
    Map<String, AnimAnimationData> animations = new LinkedHashMap<>();
    byte[] baseVisibility;
}

/**
 * @Author Jemaroo
 * @Function Storage object for Model Polygons
 */
class ModelPolygon
{
    int vertexBaseIndex;
    int textureCoordinateBaseIndex;
    int vertexArrayBase;
    int textureCoordinateArrayBase;
    int vertexCount;
    int textureIndex = -1;
    int textureMapIndex = -1;
    int groupIndex = -1;
    int shapeIndex = -1;
    double depth;
}

/**
 * @Author Jemaroo
 * @Function Storage object for Model Groups
 */
class ModelGroup
{
    int nextGroupId = -1;
    int childGroupId = -1;
    int shapeId = -1;
    int parentGroupId = -1;
    int visibilityGroupId = -1;
    int transformBaseIndex = -1;
    boolean isJoint;
    double[] transformValues = new double[24];
}

/**
 * @Author Jemaroo
 * @Function Storage object for Model Animation Data
 */
class AnimAnimationData
{
    String name;
    boolean loop;
    float startFrame;
    float endFrame;
    ArrayList<AnimAnimationKeyframe> keyframes = new ArrayList<>();
}

/**
 * @Author Jemaroo
 * @Function Storage object for Model Animation Keyframes
 */
class AnimAnimationKeyframe
{
    float time;
    ArrayList<Integer> visibilityGroupIds = new ArrayList<>();
    ArrayList<Boolean> visibilityValues = new ArrayList<>();
    ArrayList<AnimGroupTransformDelta> groupTransformDeltas = new ArrayList<>();
    ArrayList<AnimVectorDelta> vertexDeltas = new ArrayList<>();
    ArrayList<AnimTextureDelta> textureDeltas = new ArrayList<>();
}

/**
 * @Author Jemaroo
 * @Function Storage object for Model Group Transform Deltas
 */
class AnimGroupTransformDelta
{
    int index;
    int valueDelta;
}

/**
 * @Author Jemaroo
 * @Function Storage object for Model Vector Deltas
 */
class AnimVectorDelta
{
    int index;
    double x;
    double y;
    double z;
}

/**
 * @Author Jemaroo
 * @Function Storage object for Model Texture Deltas
 */
class AnimTextureDelta
{
    int textureMapIndex;
    int textureIndexDelta;
}