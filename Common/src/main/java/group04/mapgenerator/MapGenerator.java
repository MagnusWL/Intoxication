package group04.mapgenerator;

import java.io.File;
import group04.common.Entity;
import group04.common.EntityType;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.imageio.ImageIO;

public class MapGenerator {

    public MapGenerator(String fileName) throws FileNotFoundException, IOException {
        int tileSize = 16;

        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("src\\main\\resources\\test.png"));
        } catch (IOException e) {
        }

        if (img != null) {
            int width = img.getWidth() / tileSize;
            int height = img.getHeight() / tileSize;

            int[][] newMapInt = new int[width][height];

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {

                    int rgb = img.getRGB(i * tileSize, j * tileSize); 
                    int alpha = (rgb >> 24) & 0xFF;
                    int red = (rgb >> 16) & 0xFF;
                    int green = (rgb >> 8) & 0xFF;
                    int blue = (rgb) & 0xFF;

                    if (red > 10 && green > 10 && blue > 10) {
                        newMapInt[i][height - j - 1] = 0;
                    } else {
                        newMapInt[i][height - j - 1] = 1;
                    }
                }
            }

            FileOutputStream fout = new FileOutputStream("src\\main\\resources\\mapplat4.object");
//            FileOutputStream fout = new FileOutputStream(this.getClass().getregetResource() "main\\resources\\map.object");
            ObjectOutputStream oos = new ObjectOutputStream(fout);
            oos.writeObject(newMapInt);
            
        } else {
            System.out.println("image not found");
        }
    }

    public static void main(String[] args) throws IOException {
        new MapGenerator("test.png");
    }

}