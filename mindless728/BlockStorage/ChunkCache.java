package mindless728.BlockStorage;

import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.Serializable;

import java.util.LinkedHashMap;
import java.util.Map;

public class ChunkCache extends LinkedHashMap<Location,ChunkData> {
	private static final long serialVersionUID = 1;
	private int maxSize;
	private JavaPlugin plugin;
	private ChunkFileManager manager;
	
	public ChunkCache(JavaPlugin p, int ms) {
		super(2*ms, 1.0f, true);
		maxSize = ms;
		plugin = p;
		manager = new ChunkFileManager(p);
	}
	
	public Serializable getData(Location loc) {
		ChunkData chunk = getChunkData(loc);
		Location block = getLocationInChunk(loc);
		
		return chunk.getData(block.getBlockX(), block.getBlockZ(), block.getBlockY());
	}
	
	public void setData(Location loc, Serializable data) {
		ChunkData chunk = getChunkData(loc);
		Location block = getLocationInChunk(loc);
		
		chunk.setData(block.getBlockX(), block.getBlockZ(), block.getBlockY(), data);
	}
	
	public ChunkData getChunkData(Location loc) {
		Location chunk = getChunkLocation(loc);
		ChunkData data = null;
		
		if(containsKey(chunk))
			data = super.get(chunk);
		else {
			data = loadChunkDataFromDisk(chunk);
			super.put(chunk,data);
		}
		
		return data;
	}
	
	public Location getChunkLocation(Location loc) {
		int x = (int)(loc.getBlockX() < 0 ? Math.floor(loc.getBlockX()/16.) : Math.round(loc.getBlockX()/16.));
		int z = (int)(loc.getBlockZ() < 0 ? Math.floor(loc.getBlockZ()/16.) : Math.round(loc.getBlockZ()/16.));
		return new Location(loc.getWorld(), x, 0 , z);
	}
	
	public Location getLocationInChunk(Location loc) {
		int x = (loc.getBlockX() % 16 < 0 ? loc.getBlockX() % 16 + 16 : loc.getBlockX() % 16);
		int z = (loc.getBlockZ() % 16 < 0 ? loc.getBlockZ() % 16 + 16 : loc.getBlockZ() % 16);
		return new Location(loc.getWorld(), x, loc.getBlockY(), z);
	}
	
	public void close() {
		for(Map.Entry<Location,ChunkData> entry : entrySet())
			if(entry.getValue().isDirty())
				saveChunkDataToDisk(entry.getKey(), entry.getValue());
	}
	
	@Override
	protected boolean removeEldestEntry(Map.Entry<Location,ChunkData> eldest) {
		boolean ret = size() > maxSize;
		if(ret && eldest.getValue().isDirty()) {
			saveChunkDataToDisk(eldest.getKey(), eldest.getValue());
		}
		
		return ret;
	}
	
	private void saveChunkDataToDisk(Location loc, ChunkData data) {
		data.writeToFile(manager.getOutputStream(loc));
	}
	
	private ChunkData loadChunkDataFromDisk(Location loc) {
		ChunkData data = new ChunkData();
		data.readFromFile(manager.getInputStream(loc));
		return data;
	}
}