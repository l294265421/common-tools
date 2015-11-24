package com.liyuncong.application.common_tools;

import java.io.File;
import java.io.RandomAccessFile;

public class FileTools {
	private FileTools() {
		
	}
	
	/**
	 * 删除文件的第一行
	 * @param file
	 */
	public static void removeFirstLine(File file) {
		try (RandomAccessFile randomAccessFile = new RandomAccessFile(file,
				"rw")) {
			// 这一句让文件指针跳到下一行的开头
			 randomAccessFile.readLine();
			long filePointer = randomAccessFile.getFilePointer();
			// 文件指针回到文件开头
			randomAccessFile.seek(0);
			
			for(long i = 0; i < filePointer; i++) {
				randomAccessFile.writeByte(' ');
			}
			System.out.println((int)' ');
		} catch (Exception e) {
		}
	}
	
	/**
	 * 删除一个文件夹下所有文件的第一行
	 * @param directory
	 */
	public static void removeFirstLineOfAllFile(File directory) {
		File[] fileArray = directory.listFiles();
		for (File file : fileArray) {
			if (file.isFile()) {
				removeFirstLine(file);
			}
		}
	}

	public static void main(String[] args) {
		File file = new File("D:\\dc");
		FileTools.removeFirstLineOfAllFile(file);
	}
}
