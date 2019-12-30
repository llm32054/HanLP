package com.hankcs.KeywordExtraction;

/**
 * 全局变量
 * @author liuliming
 *
 */
public class Global {
	// 测试集目录
	public static final String testDataSetDir =
			"F:\\2019学业相关资料\\17127122刘立明关键词提取数据集\\ShenCeBei2018\\test";
	// 测试集的关键词结果
	public static final String testDataSetAnswerFile =
			"F:\\2019学业相关资料\\17127122刘立明关键词提取数据集\\ShenCeBei2018\\test-answer\\test_answer.txt";
	// TextRank关键词提取结果
	public static final String textRankResultsFile =
			"F:\\2019学业相关资料\\17127122刘立明关键词提取数据集\\ShenCeBei2018\\TextRank\\textRank_answer.txt";
	// PositionRank关键词提取结果
	public static final String positionRankResultsFile =
			"F:\\2019学业相关资料\\17127122刘立明关键词提取数据集\\ShenCeBei2018\\PositionRank\\positionRank_answer.txt";
	// 改进型PositionRank关键词提取结果
	public static final String improvedPositionRankResultFile =
			"F:\\2019学业相关资料\\17127122刘立明关键词提取数据集\\ShenCeBei2018\\ImprovedPositionRank\\improvedPositionRank_answer.txt";
	/**
	 * 提取关键词数目，默认为5个
	 */
	public static int keyWordNum = 5;

	/**
	 * 滑动窗口大小，默认为5
	 */
	public static int slidingWindow = 5;
}
