import lombok.Setter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.util.Map;

import static java.nio.file.StandardWatchEventKinds.OVERFLOW;


public class FileWatch implements Runnable {
	private Map<String, FileStats> statsMap = Map.of("text/plain", new TextFileStats());
	@Setter
	private WatchKey key;
	@Setter
	private Path dir;
	private FileMover fileMover = new FileMover();

	@Override
	public void run() {
		while (true) {
			for (WatchEvent<?> event : key.pollEvents()) {
				try {
					WatchEvent.Kind<?> kind = event.kind();

					if (kind == OVERFLOW) {
						continue;
					}

					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path filename = ev.context();
					Path child = dir.resolve(filename);
					if ("processed".equals(child.getFileName().toString())) {
						continue;
					}
					System.err.println(child);
					System.err.println(Files.probeContentType(child));
					FileStats fileStats = statsMap.get(Files.probeContentType(child));
					if (fileStats != null) {
						fileStats.process(child);
					}
					fileMover.moveFile(child);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			boolean valid = key.reset();
			if (!valid) {
				break;
			}
		}
	}
}
