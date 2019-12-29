package com.hankcs.KeywordExtraction;

import com.hankcs.util.ModelEvaluationUtil;

/**
 * TextRank模型评估
 * @author liuliming
 *
 */
public class TextRankEvaluation {
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

	public static void main(String[] args) {
		// 模型评估
		ModelEvaluationUtil.getEvaluationResults(testDataSetAnswerFile,
				textRankResultsFile);
	}

}
