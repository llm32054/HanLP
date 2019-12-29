package com.hankcs.util;

import java.io.File;
import java.util.List;

/**
 * 模型评估类
 * @author liuliming
 *
 */
public class ModelEvaluationUtil {

	/**
	 * 模型评估结果
	 * @param testDataSetAnswerFile 测试集关键词文本文件
	 * @param ResultsFile 模型提取的关键词结果文本文件
	 */
	public static void getEvaluationResults(
			String testDataSetAnswerFile, String ResultsFile) {
		// 读取TextRank提取结果
		List<String> textRankResult =
				FileUtil.readContents1(new File(ResultsFile));
		// 读取真实结果
		List<String> realResult =
				FileUtil.readContents1(new File(testDataSetAnswerFile));
		// 分子
		int mol = 0;
		// 准确率分母
		int precisionDen = 0;
		// 召回率分母
		int RecallDen = 0;
		for (int i = 0; i < textRankResult.size(); i++) {
			// 遍历TextRank提取结果
			String[] textRankStr =
					textRankResult.get(i).split("\\：")[1].split("\\,");
			// 遍历真实结果
			String[] realStr =
					realResult.get(i).split("\\：")[1].split("\\，");
			// 计算正确提取关键词数目
			int sameNum = countSame(realStr, textRankStr);
			// 计算准确率和召回率公式的分子和分母
			mol = mol + sameNum;
			precisionDen = precisionDen + textRankStr.length;
			RecallDen = RecallDen + realStr.length;
		}

		// 计算准确率
		float precision = mol * 1.0f / precisionDen;
		System.out.println("准确率：" + precision);
		// 计算召回率
		float recall = mol * 1.0f / RecallDen;
		System.out.println("召回率：" + recall);
		// 计算F1值
		float F1 = 2 * precision * recall / (precision + recall);
		System.out.println("F1值：" + F1);
	}

	/**
	 * 计算两个字符串数组的相同元素数目
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static int countSame(String[] str1, String[] str2) {
		int num = 0;
		for (int i = 0; i < str1.length; i++) {
			for (int j = 0; j < str2.length; j++) {
				if (str1[i].trim().equals(str2[j].trim())) {
					++num;
					// break;
				}
			}
		}
		return num;
	}

}
