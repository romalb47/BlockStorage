package mindless728.BlockStorage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class ChunkData {
	private Serializable [][][] data;
	private boolean dirty;
	
	public ChunkData() {
		data = new Serializable[16][16][128];
		dirty = false;
	}
	
	public Serializable getData(int x, int z, int y) {
		return data[x][z][y];
	}
	
	public void setData(int x, int z, int y, Serializable d) {
		if(d != data[x][z][y]) {
			data[x][z][y] = d;
			dirty = true;
		}
	}
	
	public boolean isDirty() {
		return dirty;
	}
	
	public void writeToFile(ObjectOutputStream out) {
		if(out == null)
			return;
		
		try {
			out.writeObject(data);
			out.close();
		}
		catch(IOException ioe) {}
	}
	
	public void readFromFile(ObjectInputStream in) {
		if(in == null)
			return;
		
		try {
			data = (Serializable[][][])in.readObject();
			in.close();
		}
		catch(IOException ioe) {}
		catch(ClassNotFoundException cnfe) {}
	}
}