package com.hankcs.KeywordExtraction;

import java.io.File;
import java.util.List;

import com.hankcs.hanlp.HanLP;
import com.hankcs.util.FileUtil;

/**
 * TextRank关键词提取
 * @author liuliming
 *
 */
public class TextRank {

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
	 * TextRank提取关键词文本文件
	 */
	public static String textRankResultsFile =
			Global.textRankResultsFile;

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
			// 读取文本内容（第二行开始读取）
			String content = FileUtil.readContents(list.get(i));
			// 生成关键词
			List<String> keywordList =
					textRankKeyWord(content, Global.keyWordNum);
			if (Global.log) {
				System.out.println("关键词提取结果：");
				System.out.println(keywordList);
			}
			// 输出文件
			FileUtil.appendMethod(textRankResultsFile,
					list.get(i).getName().split("\\.")[0] + "："
							+ keywordList.toString().substring(1,
									keywordList.toString().length() - 1)
							+ "\r\n");
			System.out.println(
					"************第" + i + "篇文本提取完成************");
		}
	}

	/**
	 * textRank提取关键词
	 * @param content 文本内容
	 * @param size 提取关键词个数
	 * @return
	 */
	public static List<String> textRankKeyWord(String content,
			int size) {
		return HanLP.extractKeywordByTextRank(content, size);
	}

	public static void main(String[] args) {
		// 输出关键词提取结果至文本文件
		Keyword2Txt(testDataSetDir, textRankResultsFile);
	}

}
