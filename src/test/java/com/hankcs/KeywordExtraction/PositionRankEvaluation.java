package com.hankcs.KeywordExtraction;

import com.hankcs.util.ModelEvaluationUtil;

/**
 * PositionRank模型评估
 * @author liuliming
 *
 */
public class PositionRankEvaluation {
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
	 * PositionRank提取关键词文本文件
	 */
	public static String positionRankResultsFile =
			Global.positionRankResultsFile;

	public static void main(String[] args) {
		// 模型评估
		ModelEvaluationUtil.getEvaluationResults(testDataSetAnswerFile,
				positionRankResultsFile);
	}

}
