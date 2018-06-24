package roborally.view;

import java.awt.Image;
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;

public class Theme {
	private final String name;

	private Map<String, List<Image>> images = new HashMap<String, List<Image>>();
	private Random random = new Random();
	private Pattern imageNamePattern = Pattern.compile("^([^\\.0-9]+).*$");

	public Theme(String name, File root) {
		this(name);
		addDirectoryImages(root);
	}

	public Theme(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public Image getRobotImage(int index) {
		return getImage("robot", index);
	}

	public Image getRobotImage() {
		return getImage("robot");
	}

	public Image getBatteryImage(int index) {
		return getImage("battery", index);
	}

	public Image getBatteryImage() {
		return getImage("battery");
	}

	public Image getWallImage(int index) {
		return getImage("wall", index);
	}

	public Image getWallImage() {
		return getImage("wall");
	}
	
	public Image getBackgroundImage(int index) {
		return getImage("bg", index);
	}

	public Image getBackgroundImage() {
		return getImage("bg", 0);
	}

	public Image getImage(String image, int index) {
		if (images.containsKey(image)) {
			List<Image> imagesList = images.get(image);
			if (index < 0)
				index = random.nextInt(imagesList.size());
			else
				index = index % imagesList.size();
			return imagesList.get(index);
		}
		return null;
	}

	public Image getImage(String image) {
		return getImage(image, -1);
	}

	public boolean addImage(Image image, String name) {
		if (image == null)
			return false;

		Matcher matcher = imageNamePattern.matcher(name);
		if (!matcher.matches())
			return false;

		String imageName = matcher.group(1).toLowerCase();
		if (!images.containsKey(imageName)) {
			images.put(imageName, new ArrayList<Image>());
		}
		return images.get(imageName).add(image);
	}

	public void addDirectoryImages(File root) {
		for (File file : root.listFiles()) {
			if (file.isDirectory())
				continue;

			try {
				Image image = ImageIO.read(file);
				addImage(image, file.getName());
			} catch (IOException e) {
				continue;
			}
		}
	}

	public static List<Theme> getThemes(URL url, String rootName) {
		if ("jar".equalsIgnoreCase(url.getProtocol()))
			return getThemesFromJar(url, rootName);

		try {
			String path = URLDecoder.decode(url.getPath(), "UTF-8");
			return getThemesFromDirectory(new File(path), rootName);
		} catch (UnsupportedEncodingException e) {
		}
		return Collections.emptyList();
	}

	public static List<Theme> getThemes(URL url) {
		return getThemes(url, url.getPath());
	}

	private static List<Theme> getThemesFromDirectory(File root, String rootName) {
		List<Theme> themes = new ArrayList<Theme>();
		if (root == null || !root.exists())
			return themes;

		FileFilter themeFolderFilter = new ThemeFolderFilter();
		if (root.isDirectory()) {
			// Create theme from root folder
			if (themeFolderFilter.accept(root))
				themes.add(new Theme(rootName, root));
			// Create themes from sub folders
			for (File folder : root.listFiles(themeFolderFilter)) {
				themes.add(new Theme(folder.getName(), folder));
			}
		}
		return themes;
	}

	private static class ThemeFolderFilter implements FileFilter {
		private static ThemeFileFilter themeFilter = new ThemeFileFilter();

		@Override
		public boolean accept(File folder) {
			// Must be a folder and must contain
			// at least one valid theme image file
			return folder.isDirectory() && folder.listFiles(themeFilter).length != 0;
		}
	}

	private static class ThemeFileFilter implements FileFilter {
		private static final Pattern pattern = Pattern.compile("^robot.*$",
				Pattern.CASE_INSENSITIVE);

		@Override
		public boolean accept(File file) {
			// Must be a file and file name must start with "robot"
			return !file.isDirectory() && pattern.matcher(file.getName()).matches();
		}
	}

	private static List<Theme> getThemesFromJar(URL url, String rootName) {
		Map<String, Theme> themes = new LinkedHashMap<String, Theme>();

		// Split at resource qualifier
		String[] fileParts = url.getFile().split("!/", 2);
		if (fileParts.length != 2)
			return Collections.emptyList();

		// Make pattern to match resources in the given resource root
		String jarPath = fileParts[0], resourceRoot = fileParts[1];
		Pattern pattern = Pattern.compile("^" + Pattern.quote(resourceRoot) + "/.*$",
				Pattern.CASE_INSENSITIVE);

		// Open the JAR as a ZIP file
		ZipFile zip;
		try {
			URI baseUri = new URL(jarPath).toURI();
			zip = new ZipFile(baseUri.getPath());
		} catch (Exception e) {
			return Collections.emptyList();
		}

		// Iterate over all entries
		Enumeration<? extends ZipEntry> entries = zip.entries();
		while (entries.hasMoreElements()) {
			// Check if the name matches the pattern
			ZipEntry entry = entries.nextElement();
			String name = entry.getName();
			if (!pattern.matcher(name).matches())
				continue;

			// Get the theme and image names from the last two path segments
			String[] resultParts = name.split("/");
			if (resultParts.length < 2)
				continue;
			String imageName = resultParts[resultParts.length - 1];
			String themeName = resultParts[resultParts.length - 2];
			// If theme name is resource root, use given default name
			if (themeName.equalsIgnoreCase(resourceRoot))
				themeName = rootName;

			// Get theme
			if (!themes.containsKey(themeName))
				themes.put(themeName, new Theme(themeName));
			Theme theme = themes.get(themeName);

			// Add image to theme
			try {
				URL resultUrl = new URL("jar:" + jarPath + "!/" + name);
				Image image = ImageIO.read(resultUrl);
				theme.addImage(image, imageName);
			} catch (IOException e) {
				e.printStackTrace();
				continue;
			}
		}

		return new ArrayList<Theme>(themes.values());
	}
}
