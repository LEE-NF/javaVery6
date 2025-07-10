import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

/**
 * A utility class for the fox hound program.
 * 
 * It contains helper functions for all file input / output
 * related operations such as saving and loading a game.
 */
public class FoxHoundIO {
    public static void saveGame(String[] inpuStrings) {
        String path = "data.txt";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (int i = 0; i < inpuStrings.length; i++) {
                bw.write(inpuStrings[i]);
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String[] loadGame() {
        String path = "data.txt";
        int lineCount = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            while (br.readLine() != null) {
                lineCount++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        String[] sites = new String[lineCount];
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            int index = 0;
            while ((line = reader.readLine()) != null) {
                sites[index++] = line;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return sites;
    }
}
