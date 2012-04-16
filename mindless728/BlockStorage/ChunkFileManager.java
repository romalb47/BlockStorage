package mindless728.BlockStorage;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ChunkFileManager {
	String basePath;
	String pluginName;
	
	public ChunkFileManager(JavaPlugin p) {
		pluginName = p.getDescription().getName();
		basePath = "plugins"+File.separator+"BlockStorage"+File.separator+pluginName+File.separator;
	}
	
	public ObjectInputStream getInputStream(Location loc) {
		File file = getFilePath(loc);
		String fileName = getFileName(file,loc);
		ObjectInputStream ois = null;
		try {
			ois =  new ObjectInputStream(new GZIPInputStream(new FileInputStream(fileName)));
		} catch(FileNotFoundException fnfe) {
		} catch(IOException ioe) {
		}
		
		return ois;
	}
	
	public ObjectOutputStream getOutputStream(Location loc) {
		File file = getFilePath(loc);
		String fileName = getFileName(file,loc);
		ObjectOutputStream oos = null;
		try {
			oos = new ObjectOutputStream(new GZIPOutputStream(new FileOutputStream(fileName)));
		}catch(FileNotFoundException fnfe) {
		} catch(IOException ioe) {
		}
		
		return oos;
	}
	
	public File getFilePath(Location loc) {
		File file = new File(basePath+loc.getWorld().getName()+File.separator);
		file.mkdirs();
		return file;
	}
	
	public String getFileName(File file, Location loc) {
		return file.getPath()+File.separator+pluginName+"-"+loc.getWorld().getName()+"@("+loc.getBlockX()+","+loc.getBlockZ()+")";
	}
}