import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

public class TextFileStats implements FileStats {
	@Override
	public void process(Path file) {
		try {
			FileReader fr = new FileReader(file.toFile());
			BufferedReader br = new BufferedReader(fr);
			String line;
			int wordCount = 0;
			int dotCount = 0;
			Map<String, Integer> wordNumber = new HashMap<>();
			while ((line = br.readLine()) != null) {
				String[] words = line.split("([.,!?:;'\"-]|\\s)+");
				words = Arrays.stream(words).filter(w -> w.length() > 0).toArray(String[]::new);
				wordCount += words.length;
				addWords(wordNumber, words);
				dotCount += line.length() - line.replace(".", "").length();
			}
			br.close();

			String common = wordNumber.entrySet()
					.stream()
					.max(Comparator.comparing(Map.Entry::getValue))
					.map(Map.Entry::getKey).orElse("");
			System.out.println(format("Words = %d, dots = %d, most common word = '%s'", wordCount, dotCount, common));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void addWords(Map<String, Integer> wordNumber, String[] words) {
		Arrays.stream(words).forEach(w -> {
			Integer number = wordNumber.getOrDefault(w, 0);
			wordNumber.put(w, number + 1);
		});
	}
}
