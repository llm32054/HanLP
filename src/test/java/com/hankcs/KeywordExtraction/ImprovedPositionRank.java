package com.hankcs.KeywordExtraction;

import java.io.File;
import java.util.List;

import com.hankcs.hanlp.HanLP;
import com.hankcs.util.FileUtil;

/**
 * 改进的PositionRank关键词提取
 * @author liuliming
 *
 */
public class ImprovedPositionRank {
	/**
	 * 测试集所在文件夹
	 */
	public static String testDataSetDir = Global.testDataSetDir;
	/**
	 * 测试集关键词文本文件
	 */
	public static String testDataSetAnswerFile =
			Global.testDataSetAnswerFile;
	/**
	 * 改进的PositionRank提取关键词文本文件
	 */
	public static String improvedPositionRankResultFile =
			Global.improvedPositionRankResultFile;

	/**
	 * 将生成的关键词输出至文本文件
	 * @param testDataSetDir 测试集目录
	 * @param ResultsFile 存放提取结果文件
	 */
	public static void Keyword2Txt(String testDataSetDir,
			String ResultsFile) {
		// 获取文件夹下的所有文件
		List<File> list = FileUtil.getFile(new File(testDataSetDir));

		for (int i = 0; i < list.size(); i++) {
			// 读取文本标题（只读取第二行）
			// String title = FileUtil.readTitle(list.get(i));
			String title = "";
			// 读取文本正文内容（从第三行开始）
			String content = FileUtil.readContents(list.get(i));
			// 生成关键词
			List<String> keywordList = improvedPositionRankKeyWord(
					title, content, Global.keyWordNum);
			// 输出文件
			FileUtil.appendMethod(ResultsFile,
					list.get(i).getName().split("\\.")[0] + "："
							+ keywordList.toString().substring(1,
									keywordList.toString().length() - 1)
							+ "\r\n");
		}
	}

	/**
	 * 改进的positionRank提取关键词
	 * @param title 文本标题
	 * @param content 文本正文
	 * @param size 提取数目
	 * @return
	 */
	public static List<String> improvedPositionRankKeyWord(String title,
			String content, int size) {
		return HanLP.extractKeywordByImprovedPositionRank(title,
				content, size);
	}

	public static void main(String[] args) {
		// 输出关键词提取结果至文本文件
		Keyword2Txt(testDataSetDir, improvedPositionRankResultFile);
	}

}
