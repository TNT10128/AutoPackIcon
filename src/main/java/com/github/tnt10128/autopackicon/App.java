package com.github.tnt10128.autopackicon;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import com.github.tnt10128.autopackicon.logging.LogFormatter;

public class App {

    private static final Pattern ACCEPTABLE_IMAGE_PATTERN = Pattern.compile(getPatternProperty());
    private static final String IMAGE_FORMAT = "PNG";

    private static final String VERSION = "1.0.0";

    public static final Logger logger = Logger.getGlobal();

    private static String getPatternProperty() {
        return System.getProperty("autopackicon.iconimagepattern", "(?i)_Back\\.bmp");
    }

    private static BufferedImage resizeImage(BufferedImage original, int width, int height) {
        var resultingImage = original.getScaledInstance(width, height, Image.SCALE_DEFAULT);
        var outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        outputImage.getGraphics().drawImage(resultingImage, 0, 0, null);
        return outputImage;
    }

    private static void setupLogFormatter() {
        logger.setUseParentHandlers(false);
        var handler = new ConsoleHandler();
        handler.setFormatter(new LogFormatter());
        logger.addHandler(handler);
    }

    private static File findFirstFileOrThrowException(File directory, Predicate<File> predicate) {
        if (!directory.isDirectory())
            throw new IllegalArgumentException();

        return Arrays.stream(directory.listFiles())
                .filter(predicate)
                .findFirst().orElseThrow(RuntimeException::new);
    }

    private static File getWorkingDirectory() {
        return new File(System.getProperty("user.dir"));
    }

    public static void main(String[] args) {
        setupLogFormatter();

        logger.info("Initializing AutoPackIcon v" + VERSION);

        try {
            var packDirectory = findFirstFileOrThrowException(getWorkingDirectory(), file -> file.isDirectory());
            var toTurnIntoIcon = findFirstFileOrThrowException(getWorkingDirectory(), file -> ACCEPTABLE_IMAGE_PATTERN.matcher(file.getName()).find());

            var copyFile = Files.copy(
                            Path.of(toTurnIntoIcon.getPath()), 
                            Path.of(packDirectory.getPath(), "pack.png"), 
                            StandardCopyOption.REPLACE_EXISTING
                           ).toFile();
            
            var bufferedImage = ImageIO.read(copyFile);
            ImageIO.write(resizeImage(bufferedImage, 64, 64), IMAGE_FORMAT, copyFile);

            logger.info("Wrote pack.png to " + copyFile.getAbsolutePath());
        } catch (Throwable throwable) {
            logger.log(Level.SEVERE, "Oops, an error occured with the program!", throwable);
        } finally {
            logger.info("Closing!");
        }
    }

}
