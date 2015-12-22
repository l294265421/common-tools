package com.liyuncong.application.common_tools;

import java.io.File;

public class UseTool {
/**
 * 删除leetcode项目中global包下全局使用的类在其它包中的副本；
 * @param args
 */
	public static void main(String[] args) {
		FileTools.traverse(new File("D:\\program\\git\\leetcode"), 
				new DoNothing(), new FileOperate() {
					
					@Override
					public void action(File file) {
						if (file.isFile()) {
							String fileName = file.getName();
							String path = file.getAbsolutePath();
							if ((fileName.equals("ListNode.java") 
									|| fileName.equals("TreeLinkNode.java")
									|| fileName.equals("TreeNode.java")
									|| fileName.equals("UndirectedGraphNode.java"))
									&& !path.contains("global")) {
								System.out.println(path);
								file.delete();
							}
						}
					}
				});
		
	}

}
