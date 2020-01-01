package com.hankcs.KeywordExtraction;

/**
 * 全局变量
 * @author liuliming
 *
 */
public class Global {
	// 数据集的名称
	public static final String dataSetName = "gaNews";
	// 测试集目录
	public static final String testDataSetDir =
			"F:\\2019学业相关资料\\17127122刘立明关键词提取数据集\\" + dataSetName
					+ "\\test";
	// 测试集的关键词结果
	public static final String testDataSetAnswerFile =
			"F:\\2019学业相关资料\\17127122刘立明关键词提取数据集\\" + dataSetName
					+ "\\test-answer\\test_answer.txt";
	// TextRank关键词提取结果
	public static final String textRankResultsFile =
			"F:\\2019学业相关资料\\17127122刘立明关键词提取数据集\\" + dataSetName
					+ "\\TextRank\\textRank_answer.txt";
	// PositionRank关键词提取结果
	public static final String positionRankResultsFile =
			"F:\\2019学业相关资料\\17127122刘立明关键词提取数据集\\" + dataSetName
					+ "\\PositionRank\\positionRank_answer.txt";
	// 改进型PositionRank关键词提取结果
	public static final String improvedPositionRankResultFile =
			"F:\\2019学业相关资料\\17127122刘立明关键词提取数据集\\" + dataSetName
					+ "\\ImprovedPositionRank\\improvedPositionRank_answer.txt";
	// 改进型PositionRank关键词提取结果
	public static final String improvedPositionRank2ResultFile =
			"F:\\2019学业相关资料\\17127122刘立明关键词提取数据集\\" + dataSetName
					+ "\\ImprovedPositionRank2\\improvedPositionRank_answer.txt";
	// 改进型PositionRank关键词提取结果
	public static final String improvedPositionRank3ResultFile =
			"F:\\2019学业相关资料\\17127122刘立明关键词提取数据集\\" + dataSetName
					+ "\\ImprovedPositionRank3\\improvedPositionRank_answer.txt";
	// 改进型PositionRank关键词提取结果
	public static final String improvedPositionRank4ResultFile =
			"F:\\2019学业相关资料\\17127122刘立明关键词提取数据集\\" + dataSetName
					+ "\\ImprovedPositionRank4\\improvedPositionRank_answer.txt";
	/**
	 * 提取关键词数目，默认为5个
	 */
	public static int keyWordNum = 3;

	/**
	 * 滑动窗口大小，默认为5
	 */
	public static int slidingWindow = 5;

}
