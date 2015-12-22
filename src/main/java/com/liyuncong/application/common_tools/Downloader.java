package com.liyuncong.application.common_tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;

public class Downloader {
	/**
	 * 获取url指向的内容，然后保存在saveDirectory里，文件名为saveFileName
	 * @param url　指向带下载内容的url
	 * @param saveDirectory　保存下载下来的内容的目录
	 * @param saveFileName　保存下载下来的内容的文件的名字
	 */
	public static void download(URL url, String saveDirectory, String saveFileName) {
		Charset cs = Charset.forName(GlobalValue.CHARSET);
		try {
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			try(InputStream inputStream = connection.getInputStream();
				Reader reader = new InputStreamReader(inputStream, cs);
				BufferedReader bufferedReader = new BufferedReader(reader);) {
				List<String> lines = new LinkedList<>();
				String line = "";
				while ((line = bufferedReader.readLine()) != null) {
					lines.add(line);
				}
				Path path = Paths.get(saveDirectory, saveFileName);
				Files.write(path, lines, cs, StandardOpenOption.CREATE);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 测试
	 * @param args
	 * @throws MalformedURLException 
	 */
	public static void main(String[] args) throws MalformedURLException {
		URL url = new URL("http://www.iciba.com/back");
		Downloader.download(url, "/home/liyuncong/Downloads", "back");
	}
}
