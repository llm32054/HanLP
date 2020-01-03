package com.hankcs.KeywordExtraction;

import java.io.File;
import java.util.List;

import com.hankcs.hanlp.HanLP;
import com.hankcs.util.FileUtil;

/**
 * 改进的PositionRank关键词提取
 * 该方法不同于PositionRank方法之处在于：
 * （1）本方法将文本正文和文本标题分开处理，只提取文本正文中的关键词。
 * 即：本方法将标题中的候选关键词视为均匀分布；文本正文中的候选关键词和PositionRank
 * 方法相同，即位置越靠前的候选关键词越重要。如果正文中的候选关键词在标题中出现，则增加
 * 该候选关键词的权重。
 * （2）修改了候选关键词的权重计算公式：(|d| - pos(p,d)) / |d|
 * 其中 |d|代表文本中的候选关键词总数，pos(p,d)代表该候选关键词p在文本中
 * 出现的位置，即p前的候选关键词数目。
 * 
 * 示例（1）：假设谋篇文本【正文】中的候选关键词共计出现了20次，其中某个候选关键词p
 *       在文本正文中的第2，5，10个位置出现，没有在标题中出现，
 *       则该候选关键词的权重为：
 *       (20 - 2 + 1) / 20 + (20 - 5 + 1) / 20 + (20 - 10 + 1) / 20 = 2.3
 * 示例（2）：假设谋篇文本【正文】中的候选关键词共计出现了20次，文本【标题】
 *       中的候选关键词共计出现了5次，其中某个候选关键词p在文本【正文】中的
 *       第2，5，10个位置出现，在标题中出现1次，
 *       则该候选关键词的权重为：
 *       (20 - 2 + 1) / 20 + (20 - 5 + 1) / 20 + (20 - 10 + 1) / 20 + 
 *       1 / 5 = 2.5
 * @author liuliming
 *
 */
public class ImprovedPositionRank5 {
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
	public static String improvedPositionRank5ResultFile =
			Global.improvedPositionRank5ResultFile;

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
			String title = FileUtil.readTitle(list.get(i));
			// 读取文本正文内容（从第三行开始）
			String content = FileUtil.readContentsFrom3(list.get(i));
			// 生成关键词
			List<String> keywordList = improvedPositionRankKeyWord5(
					title, content, Global.keyWordNum);
			if (Global.log) {
				System.out.println("关键词提取结果：");
				System.out.println(keywordList);
			}
			// 输出文件
			FileUtil.appendMethod(ResultsFile,
					list.get(i).getName().split("\\.")[0] + "："
							+ keywordList.toString().substring(1,
									keywordList.toString().length() - 1)
							+ "\r\n");
			System.out.println(
					"************第" + i + "篇文本提取完成************");
		}
	}

	/**
	 * 改进的positionRank提取关键词
	 * @param title 文本标题
	 * @param content 文本正文
	 * @param size 提取数目
	 * @return
	 */
	public static List<String> improvedPositionRankKeyWord5(
			String title, String content, int size) {
		return HanLP.extractKeywordByImprovedPositionRank5(title,
				content, size);
	}

	public static void main(String[] args) {
		// 输出关键词提取结果至文本文件
		Keyword2Txt(testDataSetDir, improvedPositionRank5ResultFile);
	}

}
