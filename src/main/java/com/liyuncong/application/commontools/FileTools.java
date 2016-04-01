package com.liyuncong.application.commontools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 本类提供的操作限于文本文件。
 * @author liyuncong
 *
 */
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
	
	/**
	 * 用指定的字符集把字符串写进文件里
	 * @param str 字符串
	 * @param fileName 文件路径
	 * @param charsetName 字符集
	 */
	public static void writeStringToFile(String str, String fileName, String charsetName) {
		try (OutputStream outputStream = new FileOutputStream(fileName);
				Writer writer = new OutputStreamWriter(outputStream, charsetName);
				BufferedWriter bufferedWriter = new BufferedWriter(writer);) {
			bufferedWriter.write(str);
			bufferedWriter.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 用默认的字符集(utf-8)把字符串写进文件里
	 * @param str 字符串
	 * @param fileName 文件路径
	 */
	public static void writeStringToFile(String str, String fileName) {
		writeStringToFile(str, fileName, GlobalValue.CHARSET);
	}
	
	/**
	 * 用指定的字符集读取文本中所有文本
	 * @param fileName 文件名
	 * @param charsetName 字符集名
	 * @return 文件中所有的文本
	 * @throws IOException
	 */
	public static String readFile(String fileName, String charsetName) {
		Path path = Paths.get(fileName);
		return readFile(path, charsetName);
	}
	
	public static String readFile(Path path, String charsetName) {
		try {
			byte[] bytes = Files.readAllBytes(path);
			String result = new String(bytes, charsetName);
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String readFile(File file, String charsetName) {
		return readFile(file.toPath(), charsetName);
	}
	
	public static String readFile(File file) {
		return readFile(file.toPath(), GlobalValue.CHARSET);
	}
	
	/**
	 * 用默认的字符集(utf-8)读取文本中所有文本
	 * @param fileName 文件名
	 * @return 文件中所有的文本
	 */
	public static String readFile(String fileName){
		return readFile(fileName, GlobalValue.CHARSET);
	}
	
	public static List<String> readAllLinesFromFile(Path path, Charset cs) {
		List<String> lines = null;
		try {
			lines = Files.readAllLines(path, cs);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}
	
	public static List<String> readAllLinesFromFile(Path path) {
		return readAllLinesFromFile(path, Charset.
				forName(GlobalValue.CHARSET));
	}
	
	public static List<String> readAllLinesFromFile(String path, Charset cs) {
		return readAllLinesFromFile(Paths.get(path), cs);
	}
	
	public static List<String> readAllLinesFromFile(String path, String cs) {
		return readAllLinesFromFile(path, Charset.
				forName(cs));
	}
	
	public static List<String> readAllLinesFromFile(String path) {
		return readAllLinesFromFile(path, Charset.
				forName(GlobalValue.CHARSET));
	}
	
	public static void writeLinesToFile(List<String> lines, 
			String charsetName, File target) {
		Path path = target.toPath();
		try {
			Files.write(path, lines, Charset.forName(charsetName), 
					StandardOpenOption.CREATE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeLinesToFile(List<String> lines, 
			String charsetName, String target) {
		writeLinesToFile(lines, charsetName, new File(target));;
	}
	
	public static void writeLinesToFile(List<String> lines, 
			String target) {
		writeLinesToFile(lines, "utf-8", new File(target));;
	}
	
	/**
	 * 单线程后根遍历文件树，对遇到的文件及文件夹执行指定的操作
	 * 
	 * @param startDirectory 起始目录
	 * @param fileOperate 对目录的操作
	 * @param fileOperate 对文件的操作
	 */
	public static void traverse(File startDirectory,
			FileOperate directoryOperate, FileOperate fileOperate) {
		File[] childFiles = startDirectory.listFiles();
		for (File file : childFiles) {
			if (file.isFile()) {
				fileOperate.action(file);
			} else {
				traverse(file, directoryOperate, fileOperate);
			}
		}
		directoryOperate.action(startDirectory);
	}

	/**
	 * 用指定数量的线程后根遍历文件树；对遇到的文件及文件夹执行指定的操作
	 * 
	 * @param startDirectory 起始目录
	 * @param directoryOperate 对目录的操作
	 * @param fileOperate 对文件的操作
	 * @param threadNum 线程数量
	 */
	public static void traverse(File startDirectory,
			FileOperate directoryOperate, FileOperate fileOperate, int threadNum) {
		if (threadNum <= 1) {
			traverse(startDirectory, directoryOperate, fileOperate);
			return;
		}

		// 树中某一层结束的标记
		File levelEndFlad = null;
		// 层次遍历文件树
		Queue<File> helper = new LinkedList<File>();
		helper.add(startDirectory);
		helper.add(levelEndFlad);
		// 记录已经访问过的父目录
		Stack<File> visitedDirectory = new Stack<>();
		while (!helper.isEmpty()) {
			File parent = helper.poll();
			// 说明某一层遍历已经结束
			if (parent == null) {
				// 下一层节点数目
				int count = helper.size();
				if (count >= threadNum || count == 0) {
					// 结束时，helper里面只有刚好满足threadNum那一层的结点
					break;
				} else {
					helper.add(levelEndFlad);
					continue;
				}
			}
			visitedDirectory.add(parent);
			
			File[] child = parent.listFiles();
			for (File file : child) {
				if (file.isFile()) {
					fileOperate.action(file);
				} else {
					helper.add(file);
				}
			}
		}

		// 如果自始至终都没有找到合适的那一层，说明文件树中的文件处理完毕了，但是
		// 文件夹还没有处理
		if (helper.size() == 0) {
			traverse(startDirectory, directoryOperate, new DoNothing());
			return;
		}

		// 用指定的线程处理helper中的元素
		ExecutorService pool = Executors.newFixedThreadPool(threadNum);
		while (!helper.isEmpty()) {
			File root = helper.poll();
			DirectoryTreeTraverseTask task = new DirectoryTreeTraverseTask(
					root, directoryOperate, fileOperate);
			pool.submit(task);
		}

		// 检测是否所有任务都已执行完
		while (((ThreadPoolExecutor) pool).getActiveCount() != 0) {
			System.out.println("活着的线程数： " + ((ThreadPoolExecutor) pool).getActiveCount());
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		pool.shutdown();
		
		// 对线程池处理范围外的文件夹进行处理
		while (!visitedDirectory.isEmpty()) {
			File top = visitedDirectory.pop();
			directoryOperate.action(top);
		}
	}

	private static class DirectoryTreeTraverseTask implements Runnable {
		private File startDirectory;
		private FileOperate directoryOperate;
		private FileOperate fileOperate;

		public DirectoryTreeTraverseTask(File startDirectory,
				FileOperate directoryOperate, FileOperate fileOperate) {
			super();
			this.startDirectory = startDirectory;
			this.directoryOperate = directoryOperate;
			this.fileOperate = fileOperate;
		}

		@Override
		public void run() {
			FileTools.traverse(startDirectory, directoryOperate,
					fileOperate);
		}

	}
	
	/**
	 * 把一行文本追加到目标文件中
	 * @param target
	 * @param line
	 */
	public static void appendLineToFile(File target, String line) {
		try(OutputStream os = new FileOutputStream(target, true);
				Writer writer = new OutputStreamWriter(os, "utf-8");
				BufferedWriter bw = new BufferedWriter(writer)) {
			bw.write(line);
			bw.write(System.lineSeparator());
			bw.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 把一行文本追加到目标文件中
	 * @param target
	 * @param line
	 */
	public static void appendLineToFile(String target, String line) {
		appendLineToFile(new File(target), line);
	}
	
	/**
	 * 把输出流转移给某个输出流
	 * @param is
	 */
	public static void transfer(InputStream is, OutputStream os, Charset fromCs, Charset ToCs) {
		try(Reader reader = new InputStreamReader(is, fromCs);) {
			transfer(reader, os, ToCs);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 把Reader转移给某个输出流
	 * @param is
	 */
	public static void transfer(Reader reader, OutputStream os, Charset ToCs) {
		try(Writer writer = new OutputStreamWriter(os, ToCs)) {
			transfer(reader, writer);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
	}
	
	/**
	 * 把Reader转移给某个Writer
	 * @param is
	 */
	public static void transfer(Reader reader, Writer os) {
		try(BufferedReader br = new BufferedReader(reader);
				BufferedWriter bw = new BufferedWriter(os);) {
			String line;
			while ((line = br.readLine()) != null) {
				bw.write(line);
				bw.write(System.lineSeparator());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void convertCharset(File file, Charset origin, 
			Charset target) {
		String pathName = file.toString();
		String content = readFile(pathName, origin.toString());
		writeStringToFile(content, pathName, target.toString());
	}
	
	public static void convertCharset(File file, String origin, 
			String target) {
		convertCharset(file, Charset.forName(origin), 
				Charset.forName(target));
	}
	
	public static void allFileConvertCharset(File startDirectory, 
			final String origin, final String target) {
		traverse(startDirectory, new DoNothing(), new FileOperate() {
			
			@Override
			public void action(File file) {
				convertCharset(file, origin, target);
			}
		});
	}
	
	public static void main(String[] args) {
		File file = new File("D:\\bishe\\china.com.cn\\");
		allFileConvertCharset(file, "gbk", "utf-8");
	}
	
}
