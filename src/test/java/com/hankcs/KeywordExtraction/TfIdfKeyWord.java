package com.hankcs.KeywordExtraction;

import java.io.File;
import java.util.List;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.mining.word.TfIdfCounter;
import com.hankcs.util.FileUtil;

/**
 * TfIdf关键词提取
 * 将文本（标题和正文）中候选关键词的所有位置信息合并到PageRank算法中，
 * 该方法为文本中出现位置靠前并且频繁出现的候选关键词分配较大的权重，每个
 * 候选关键词词的权重设置为其在文本中的出现位置的倒数。
 * 示例：如果某个候选关键词在文本中的第2，5，10个位置出现，则该候选关键词的
 * 权重为：1/2 + 1/5 + 1/10 = 0.8
 * @author liuliming
 *
 */
public class TfIdfKeyWord {

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
	 * tfIdf提取关键词文本文件
	 */
	public static String tfIdfResultFile = Global.tfIdfResultFile;

	/**
	 * 将生成的关键词输出至文本文件
	 * @param testDataSetDir 测试集目录
	 * @param ResultsFile 存放提取结果文件
	 */
	public static void Keyword2Txt(String testDataSetDir,
			String ResultsFile) {
		// 获取文件夹下的所有文件
		List<File> list = FileUtil.getFile(new File(testDataSetDir));
		// 构建tfIdfCounter类中的tfMap对象
		TfIdfCounter tfIdfCounter = new TfIdfCounter();
		for (int i = 0; i < list.size(); i++) {
			// 读取文本内容（第二行开始）
			String content = FileUtil.readContents(list.get(i));
			// 构建tfIdfCounter类中的tfMap对象
			tfIdfCounter.add(content);
		}

		for (int i = 0; i < list.size(); i++) {
			// 读取文本内容（第二行开始）
			String content = FileUtil.readContents(list.get(i));
			// 生成关键词
			List<String> keywordList = tfIdfKeyWord(tfIdfCounter,
					content, Global.keyWordNum);
			// 输出文件
			FileUtil.appendMethod(ResultsFile,
					list.get(i).getName().split("\\.")[0] + "："
							+ keywordList.toString().substring(1,
									keywordList.toString().length() - 1)
							+ "\r\n");
		}

	}

	/**
	 * positionRank提取关键词
	 * @param content 文本内容
	 * @param size 提取关键词个数
	 * @return
	 */
	public static List<String> tfIdfKeyWord(TfIdfCounter tfIdfCounter,
			String content, int size) {
		return HanLP.extractKeywordByTFIDF(tfIdfCounter, content, size);
	}

	public static void main(String[] args) {
		// 输出关键词提取结果至文本文件
		Keyword2Txt(testDataSetDir, tfIdfResultFile);
	}

}
