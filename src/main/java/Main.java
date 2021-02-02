import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

public class Main {

	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			System.err.println("Usage: filewatch directory");
			System.exit(1);
		}
		WatchService watcher = FileSystems.getDefault().newWatchService();
		Path dir = Paths.get(args[0]);
		System.err.println(dir);
		WatchKey key = dir.register(watcher, ENTRY_CREATE);
		FileWatch fileWatch = new FileWatch();
		fileWatch.setDir(dir);
		fileWatch.setKey(key);
		fileWatch.run();
	}
}
