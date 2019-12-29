package com.hankcs.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.hankcs.demo.DemoImprovedPositionRankKeyword;

/**
 * 文件工具类
 * @author liuliming
 *
 */
public class FileUtil {

	/**
	 * 读取单个文件的内容（从第二行开始读）
	 * @param file
	 * @return 文件的内容，String
	 */
	public static String readContents(File file) {
		StringBuilder res = new StringBuilder();
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(file));
			// 从文本的第二行开始读取
			br.readLine();
			while (br.ready()) {
				// 这里可以作相关的处理过程 #todo your code#
				res.append(br.readLine() + "\n");
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return res.toString();
	}

	/**
	 * 读取单个文件的内容（从第三行开始读）
	 * @param file
	 * @return 文件的内容，String
	 */
	public static String readContentsFrom3(File file) {
		StringBuilder res = new StringBuilder();
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(file));
			// 从文本的第二行开始读取
			br.readLine();
			br.readLine();
			while (br.ready()) {
				// 这里可以作相关的处理过程 #todo your code#
				res.append(br.readLine() + "\n");
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return res.toString();
	}

	/**
	 * 读取文本文件的第二行内容（标题）
	 * @param file
	 * @return
	 */
	public static String readTitle(File file) {
		StringBuilder res = new StringBuilder();
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(file));
			// 从文本的第二行开始读取
			br.readLine();
			/*while (br.ready()) {
				// 这里可以作相关的处理过程 #todo your code#
				res.append(br.readLine() + "\n");
			}*/
			// 只读取第二行内容
			res.append(br.readLine() + "\n");
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return res.toString();
	}

	/**
	 * 读取单个文件内容
	 * @param file
	 * @return
	 */
	public static List<String> readContents1(File file) {
		List<String> list = new ArrayList<String>(700);
		// StringBuilder res = new StringBuilder();
		BufferedReader br = null;

		try {
			br = new BufferedReader(new FileReader(file));
			// 从文本的第二行开始读取
			// br.readLine();
			while (br.ready()) {
				// 这里可以作相关的处理过程 #todo your code#
				// res.append(br.readLine() + "\n");
				list.add(br.readLine());
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// return res.toString();
		return list;
	}

	/**
	 * 获取文件夹下所有的文件
	 * @param file
	 * @return
	 */
	public static List<File> getFile(File file) {
		List<File> listLocal = new ArrayList<>();
		if (file != null) {
			File[] f = null;
			// 排序
			if (file.listFiles() != null) {
				f = sort(file.listFiles());
			}
			if (f != null) {
				for (int i = 0; i < f.length; i++) {
					getFile(f[i]);
					listLocal.add(f[i]);
				}
			} else {
				// System.out.println(file);
			}
		}
		return listLocal;
	}

	/**
	 * 针对listFiles进行文件排序
	 * @param s
	 * @return
	 */
	public static File[] sort(File[] s) {
		// 中间值
		File temp = null;
		// 外循环:我认为最小的数,从0~长度-1
		for (int j = 0; j < s.length - 1; j++) {
			// 最小值:假设第一个数就是最小的
			String min = s[j].getName();
			// 记录最小数的下标的
			int minIndex = j;
			// 内循环:拿我认为的最小的数和后面的数一个个进行比较
			for (int k = j + 1; k < s.length; k++) {
				// 找到最小值
				if (Integer.parseInt(
						min.substring(0, min.indexOf("."))) > Integer
								.parseInt(s[k].getName().substring(0,
										s[k].getName().indexOf(".")))) {
					// 修改最小
					min = s[k].getName();
					minIndex = k;
				}
			}
			// 当退出内层循环就找到这次的最小值
			// 交换位置
			temp = s[j];
			s[j] = s[minIndex];
			s[minIndex] = temp;
		}
		return s;
	}

	/**
	 * 追加文件内容
	 * @param fileName
	 * @param content
	 */
	public static void appendMethod(String fileName, String content) {
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			FileWriter writer = new FileWriter(fileName, true);
			writer.write(content);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// 获取文件夹下的所有文件
		List<File> list = getFile(new File(
				"F:\\2019学业相关资料\\17127122刘立明关键词提取数据集\\公安要闻数据集\\test"));

		for (int i = 0; i < list.size(); i++) {
			// 读取文本内容
			String content = readContents(list.get(i));
			// 生成关键词
			List<String> keywordList = DemoImprovedPositionRankKeyword
					.xxRankKeyWord(content, 5);

			//
			String[] keyowrds = keywordList.toString()
					.substring(1, keywordList.toString().length() - 1)
					.split("\\,");
			// 输出文件
			appendMethod(
					"F:\\2019学业相关资料\\17127122刘立明关键词提取数据集\\公安要闻数据集\\test-answer\\test_answer.txt",
					list.get(i).getName().split("\\.")[0] + "："
							+ keyowrds[0].trim() + "，"
							+ keyowrds[1].trim() + "，"
							+ keyowrds[2].trim() + "，"
							+ keyowrds[3].trim() + "，"
							+ keyowrds[4].trim() + "\r\n");
		}
	}
}
