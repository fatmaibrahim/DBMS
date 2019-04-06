package eg.edu.alexu.csd.oop.db.cs43;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class D_base {

	
	public static String databasePath;

	public void dropDataBase(String databaseName) throws Exception  {
		File file=new File(databaseName);

		Path path = Paths.get(file.getAbsolutePath());
		if (!Files.exists(path)) {
			try {
				throw new Exception("Database folder doesn't exist!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			if (file.isDirectory()) {

				// directory is empty, then delete it
				if (file.list().length == 0) {

					file.delete();
					System.out.println("Directory is deleted : " + file.getAbsolutePath());

				} else {

					
					final String[] files = file.list();
					
					for (String temp : files) {
						// construct the file structure
						if (!file.isDirectory()) {
					        file.delete();
						}
						File fileDelete = new File(file, temp);
						

						// recursive delete
						deleteDirectory(fileDelete);
					}

					// check the directory again, if empty then delete it
					if (file.list().length == 0) {
						file.delete();
						System.out.println("Directory is deleted : " + file.getAbsolutePath());
					}
				}

			}else {
				// if file, then delete it
				file.delete();
				System.out.println("File is deleted : " + file.getAbsolutePath());
			}
		}
	
		
	}

	private void deleteDirectory(File file) {
		File[] contents = file.listFiles();
		if (contents != null) {
			for (File f : contents) {
				deleteDirectory(f);
			}
		}
		file.delete();
	}

	public String createDataBase(String dataBaseName) {
		File database = new File(dataBaseName);
		String absolutePath = database.getAbsolutePath();
		

		if (!database.exists()) {
			if (database.mkdir()) {
				System.out.println("Directory is created!");
			} else {
				System.out.println("Failed to create directory!");
			}
		}else{
			System.out.println("Database is already Existed");
		}
		return absolutePath;

	}
}
