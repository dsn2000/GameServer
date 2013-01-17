package server.services.virtualFile;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

public class VirtualFileImpl implements VirtualFile{
	private String root;

	public VirtualFileImpl(String root){
		this.root = root;
	}
	
	private class FileIterator implements Iterator<String>{
		
		private Queue<File> files = new LinkedList<File>();
		
		public FileIterator(String path){
			files.add(new File(root + path));
		}
		
		public boolean hasNext() {
			return !files.isEmpty();
		}
		
		public String next() {
			File file = files.peek();
			if(file.isDirectory()){
				for(File subFile : file.listFiles()){
					files.add(subFile);
				}
			}			
			return files.poll().getAbsolutePath();
		}
		
		public void remove() {
		}
	}

	public boolean isExist(String path) {
		File file = new File(root + path);
		return file.exists();
	}


	public boolean isDirectory(String path) {
		File file = new File(root + path);
		return file.isDirectory();
	}

	public String getAbsolutePath(String filePath) {
		File file = new File(root + filePath);
		return file.getAbsolutePath();
	}


	public byte[] getBytes(String filePath){
		try {
			File file = new File(root + filePath);
			InputStream is = new FileInputStream(file);
			
		    long length = file.length();
		    if (length > Integer.MAX_VALUE) {
		        return null;
		    }
		 
		    byte[] bytes = new byte[(int)length];
		    int offset = 0;
		    int numRead = 0;
		    while (offset < bytes.length && (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
		        offset += numRead;
		    }
		 
		    if (offset < bytes.length) {
		        throw new IOException("Could not completely read file " + file.getName());
		    }
		 
		    is.close();
		    return bytes;
		} catch(Exception err) {
			return null;
		}
	}

	public String getUTF8Text(String file) {
		return null;
	}

	public Iterator<String> getIterator(String startDir) {
		return new FileIterator(startDir);
	}
}



