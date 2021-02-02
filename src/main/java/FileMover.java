import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileMover {
	public void moveFile(Path file) {
		try {
			System.err.println("Moving " + file);
			URI uri = file.getParent().toUri();
			File directory = new File(uri.getPath() + "/processed");
			if (!directory.exists()) {
				directory.mkdir();
			}
			System.err.println("Moving to " + Paths.get(directory.getAbsolutePath() + "/" + file.getFileName().toString()));
			Files.move(file, Paths.get(directory.getAbsolutePath() + "/" + file.getFileName().toString()), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
