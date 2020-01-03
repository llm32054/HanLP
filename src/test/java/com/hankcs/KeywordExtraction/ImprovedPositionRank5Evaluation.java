package com.hankcs.KeywordExtraction;

import com.hankcs.util.ModelEvaluationUtil;

/**
 * 改进的PositionRank模型评估
 * @author liuliming
 *
 */
public class ImprovedPositionRank5Evaluation {
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

	public static void main(String[] args) {
		// 模型评估
		ModelEvaluationUtil.getEvaluationResults(testDataSetAnswerFile,
				improvedPositionRank5ResultFile);
	}

}
