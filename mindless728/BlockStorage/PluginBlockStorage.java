package mindless728.BlockStorage;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.Serializable;

public class PluginBlockStorage<T extends Serializable> {
	JavaPlugin plugin;
	ChunkCache cache;
	boolean invalid;
	
	public PluginBlockStorage(JavaPlugin p, int cacheSize) {
		plugin = p;
		cache = new ChunkCache(p, cacheSize);
		invalid = false;
	}
	
	@SuppressWarnings("unchecked")
	public T getData(Location loc) {
		if(invalid)
			throw new NullPointerException();
		return (T)(cache.getData(loc));
	}
	
	public void setData(Location loc, T data) {
		if(invalid)
			throw new NullPointerException();
		cache.setData(loc, data);
	}
	
	public void close() {
		if(invalid)
			throw new NullPointerException();
		cache.close();
		invalid = true;
	}
}