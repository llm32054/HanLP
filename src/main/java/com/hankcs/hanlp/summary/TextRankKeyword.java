package com.hankcs.hanlp.summary;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.hankcs.hanlp.algorithm.MaxHeap;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;

/**
 * 基于TextRank算法的关键字提取，适用于单文档
 *
 * @author hankcs
 */
public class TextRankKeyword extends KeywordExtractor {
	/**
	 * 阻尼系数（ＤａｍｐｉｎｇＦａｃｔｏｒ），一般取值为0.85
	 */
	final static float d = 0.85f;
	/**
	 * 最大迭代次数
	 */
	public static int max_iter = 200;
	/**
	 * 阈值
	 */
	final static float min_diff = 0.0000001f;

	public TextRankKeyword(Segment defaultSegment) {
		super(defaultSegment);
	}

	public TextRankKeyword() {
	}

	/**
	 * 提取关键词
	 *
	 * @param document 文档内容
	 * @param size     希望提取几个关键词
	 * @return 一个列表
	 */
	public static List<String> getKeywordList(String document,
			int size) {
		TextRankKeyword textRankKeyword = new TextRankKeyword();

		return textRankKeyword.getKeywords(document, size);
	}

	/**
	 * 提取关键词
	 *
	 * @param content
	 * @return
	 * @deprecated 请使用 {@link KeywordExtractor#getKeywords(java.lang.String)}
	 */
	public List<String> getKeyword(String content) {
		return getKeywords(content);
	}

	/**
	 * 返回全部分词结果和对应的rank
	 *
	 * @param content
	 * @return
	 */
	public Map<String, Float> getTermAndRank(String content) {
		assert content != null;
		List<Term> termList = defaultSegment.seg(content);
		return getTermAndRank(termList);
	}

	/**
	 * 返回分数最高的前size个分词结果和对应的rank
	 *
	 * @param content
	 * @param size
	 * @return
	 */
	public Map<String, Float> getTermAndRank(String content, int size) {
		Map<String, Float> map = getTermAndRank(content);
		Map<String, Float> result = top(size, map);

		return result;
	}

	private Map<String, Float> top(int size, Map<String, Float> map) {
		Map<String, Float> result = new LinkedHashMap<String, Float>();
		for (Map.Entry<String, Float> entry : new MaxHeap<Map.Entry<String, Float>>(
				size, new Comparator<Map.Entry<String, Float>>() {
					@Override
					public int compare(Map.Entry<String, Float> o1,
							Map.Entry<String, Float> o2) {
						return o1.getValue().compareTo(o2.getValue());
					}
				}).addAll(map.entrySet()).toList()) {
			result.put(entry.getKey(), entry.getValue());
		}
		return result;
	}

	/**
	 * 使用已经分好的词来计算rank（原始TextRank公式）
	 *
	 * @param termList
	 * @return
	 */
	public Map<String, Float> getTermAndRank(List<Term> termList) {
		// 存放去停用词、动词和介词等词性后的结果（只保留名词和形容词）
		List<String> wordList = new ArrayList<String>(termList.size());
		// for循环遍历去停用词、动词和介词等词性的词汇
		for (Term t : termList) {
			if (shouldInclude(t)) {
				wordList.add(t.word);
			}
		}
		System.out.println("去停用词及词性过滤的结果：");
		System.out.println(wordList);
		// 存放wordList去重后的结果
		Map<String, Set<String>> words =
				new TreeMap<String, Set<String>>();
		// 借助que统计滑动窗口为5的共现单词
		Queue<String> que = new LinkedList<String>();
		for (String w : wordList) {
			if (!words.containsKey(w)) {
				words.put(w, new TreeSet<String>());
			}
			// 复杂度O(n-1)
			if (que.size() >= 5) {
				que.poll();
			}
			// 遍历que统计共现单词
			for (String qWord : que) {
				if (w.equals(qWord)) {
					continue;
				}
				// 既然是邻居,那么关系是相互的,遍历一遍即可
				words.get(w).add(qWord);
				words.get(qWord).add(w);
			}
			que.offer(w);
		}
		// System.out.println(words);
		// 用于存放单词的分数
		Map<String, Float> score = new HashMap<String, Float>();
		// 依据TF来设置初值
		for (Map.Entry<String, Set<String>> entry : words.entrySet()) {
			// score.put(entry.getKey(), sigMoid(entry.getValue().size()));
			score.put(entry.getKey(), 1.0f / words.size());
		}
		// 迭代计算单词的分数
		for (int i = 0; i < max_iter; ++i) {
			Map<String, Float> m = new HashMap<String, Float>();
			float max_diff = 0;
			for (Map.Entry<String, Set<String>> entry : words
					.entrySet()) {
				String key = entry.getKey();
				Set<String> value = entry.getValue();
				m.put(key, (1 - d) * 1.0f / words.size());
				for (String element : value) {
					int size = words.get(element).size();
					if (key.equals(element) || size == 0)
						continue;
					// 原始TextRank公式
					m.put(key,
							m.get(key) + d / size
									* (score.get(element) == null ? 0
											: score.get(element)));
				}
				// 遍历一轮结束后，保留两次迭代的最大差值
				max_diff = Math.max(max_diff,
						Math.abs(m.get(key) - (score.get(key) == null
								? 0 : score.get(key))));
			}
			score = m;
			// 阈值小于min_diff，退出迭代
			if (max_diff <= min_diff)
				break;
		}
		System.out.println("候选关键词得分：");
		for (Map.Entry<String, Float> entry : score.entrySet()) {
			System.out.println(entry.getKey() + ":" + entry.getValue());
		}

		return score;
	}

	/**
	 * sigmoid函数
	 *
	 * @param value
	 * @return
	 */
	public static float sigMoid(float value) {
		return (float) (1d / (1d + Math.exp(-value)));
	}

	@Override
	public List<String> getKeywords(List<Term> termList, int size) {
		// 返回分数最高的前size个分词结果和对应的rank
		Set<Map.Entry<String, Float>> entrySet =
				top(size, getTermAndRank(termList)).entrySet();
		List<String> result = new ArrayList<String>(entrySet.size());
		for (Map.Entry<String, Float> entry : entrySet) {
			result.add(entry.getKey());
		}
		return result;
	}

	@Override
	public List<String> getKeywords(List<Term> titleTermList,
			List<Term> termList, int size) {
		// 返回分数最高的前size个分词结果和对应的rank
		Set<Map.Entry<String, Float>> entrySet =
				top(size, getTermAndRank(termList)).entrySet();
		List<String> result = new ArrayList<String>(entrySet.size());
		for (Map.Entry<String, Float> entry : entrySet) {
			result.add(entry.getKey());
		}
		return result;
	}
}
