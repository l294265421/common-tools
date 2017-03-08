package com.liyuncong.application.commontools;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PackageOperate {
	private static Logger logger = LoggerFactory.getLogger(PackageOperate.class);
	public static void addPackageDeclare(String srcDir) {
		if (srcDir == null || srcDir.length() == 0) {
			logger.error("srcDir is empty");
			return;
		}
		
		File root = new File(srcDir);
		if (!root.isDirectory()) {
			logger.error("{} is not a directory", srcDir);
			return;
		}
		addPackageDeclare(root, "");
	}
	
	private static void addPackageDeclare(File packageDir, String packageName) {
		File[] files = packageDir.listFiles();
		for (File file : files) {
			if (file.isFile()) {
				if (!"".equals(packageName) && file.getAbsolutePath().endsWith("java")) {
					
				}
			} else {
				addPackageDeclare(file, packageName + "." + file.getName());
			}
		}
	}
}
