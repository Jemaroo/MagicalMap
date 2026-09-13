import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * @Author Jemaroo
 * @Function Main Functions for reading and parsing input data
 */
public class TETMain 
{
    /**
     * @Author Jemaroo
     * @Function Searches for all .txt files in a given directory
     */
    public static ArrayList<File> findMatchingFiles(File directory) 
    {
        ArrayList<File> matchingFiles = new ArrayList<>();
        File[] files = directory.listFiles();

        if (files != null) 
        {
            for (File file : files) 
            {
                if (file.isDirectory()) 
                {
                    matchingFiles.addAll(findMatchingFiles(file));
                } 
                else if (file.getName().toLowerCase().endsWith(".txt")) 
                {
                    matchingFiles.add(file);
                }
            }
        }

        return matchingFiles;
    }

    /**
     * @Author Jemaroo
     * @Function Will attempt to read the given text file parse it into an array of TETData
     */
    public static ArrayList<TETData> getTextData(File givenFile)
    {
        byte[] givenFiledata = ByteUtils.readData(givenFile);
        ArrayList<TETData> items = new ArrayList<>();

        Charset japaneseCharset = Charset.forName("windows-31j");

        String fullString = new String(givenFiledata, japaneseCharset);
        String[] givenFileDataStrings = fullString.split("\0");

        for(int i = 0; i < givenFileDataStrings.length; i += 2)
        {
            TETData temp = new TETData("", "");
            temp.identifier = givenFileDataStrings[i];
            System.out.println(temp.identifier);
            temp.textData = givenFileDataStrings[i + 1];
            items.add(temp);
        }

        return items;
    }

    /**
     * @Author Jemaroo
     * @Function Will export the array of text back into the same format as the given file
     */
    public static byte[] buildNewFile(ArrayList<TETData> text)
    {
        String retString = "";
        for(TETData textData : text)
        {
            retString += textData.identifier + '\0' + textData.textData + '\0';
        }

        return retString.getBytes(StandardCharsets.UTF_8);
    }
}