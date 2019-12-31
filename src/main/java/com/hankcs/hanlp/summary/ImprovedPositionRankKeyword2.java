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

import com.hankcs.KeywordExtraction.Global;
import com.hankcs.hanlp.algorithm.MaxHeap;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;

/**
 * PositionRank的改进版
 * 
 * @author liuliming
 *
 */
public class ImprovedPositionRankKeyword2 extends KeywordExtractor {

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

	public ImprovedPositionRankKeyword2(Segment defaultSegment) {
		super(defaultSegment);
	}

	public ImprovedPositionRankKeyword2() {
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
		ImprovedPositionRankKeyword2 improvedPositionRankKeyword2 =
				new ImprovedPositionRankKeyword2();

		return improvedPositionRankKeyword2.getKeywords(document, size);
	}

	/**
	 * 提取关键词
	 *
	 * @param document 文档内容
	 * @param size     希望提取几个关键词
	 * @return 一个列表
	 */
	public static List<String> getKeywordList(String title,
			String document, int size) {
		ImprovedPositionRankKeyword2 improvedPositionRankKeyword2 =
				new ImprovedPositionRankKeyword2();

		return improvedPositionRankKeyword2.getKeywords(title, document,
				size);
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
	 * 改进版PositionRank 公式提取关键词
	 * @param termList
	 * @return
	 */
	public Map<String, Float> getTermAndRank(List<Term> termList) {
		// 只保留名词和形容词的结果
		List<String> wordList = new ArrayList<String>(termList.size());
		// for循环遍历去除（除名词和形容词）其他词性词汇
		for (Term t : termList) {
			if (shouldInclude(t)) {
				wordList.add(t.word);
			}
		}
		System.out.println("去停用词及词性过滤的结果：");
		System.out.println(wordList);
		// 存放单词共现结果（不包含重复单词）
		Map<String, Set<String>> words =
				new TreeMap<String, Set<String>>();
		// 存放单词共现结果（包含重复单词）
		Map<String, List<String>> coWords =
				new TreeMap<String, List<String>>();
		// 存放 PositionRank 中单词的重启概率
		Map<String, Float> wordCountMap =
				new HashMap<String, Float>(wordList.size());
		// 存放单词位置
		int position = 0;
		// 借助que统计滑动窗口为5的共现单词
		Queue<String> que = new LinkedList<String>();
		for (String w : wordList) {
			// 计算重启概率
			if (!wordCountMap.containsKey(w)) {

				wordCountMap.put(w, (wordList.size() - position++)
						* 1.0f / wordList.size());
			} else {
				wordCountMap.put(w,
						wordCountMap.get(w)
								+ (wordList.size() - position++) * 1.0f
										/ wordList.size());
			}
			// 构建words，用来存放单词的共现单词
			if (!words.containsKey(w)) {
				words.put(w, new TreeSet<String>());
				coWords.put(w, new ArrayList<String>());
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
				// 构建共现关系
				coWords.get(w).add(qWord);
				coWords.get(qWord).add(w);
			}
			que.offer(w);
		}

		// 归一化重启概率
		float sum = 0;
		for (String key : wordCountMap.keySet()) {
			sum = sum + wordCountMap.get(key);
		}

		System.out.println("归一化后的重启概率：");
		for (String key : wordCountMap.keySet()) {
			wordCountMap.put(key, wordCountMap.get(key) / sum);
			System.out.print(key + ":" + wordCountMap.get(key) + " ");
		}
		/*Iterator it = wordCountMap.entrySet().iterator();
		System.out.println("归一化后的重启概率：");
		while (it.hasNext()) {
			Map.Entry<String, Float> e =
					(Map.Entry<String, Float>) it.next();
			wordCountMap.put(e.getKey(), e.getValue() / sum);
			System.out.print(e.getKey() + ":" + e.getValue() + " ");
		}*/
		// System.out.println(words);
		// 用于存放单词的分数
		Map<String, Float> score = new HashMap<String, Float>();
		// 依据TF来设置初值
		for (Map.Entry<String, Set<String>> entry : words.entrySet()) {
			// score.put(entry.getKey(), sigMoid(entry.getValue().size()));
			score.put(entry.getKey(), 1.0f / words.size());
		}
		System.out.println();
		System.out.println("迭代分数：");
		// 迭代计算单词的分数
		for (int i = 0; i < max_iter; ++i) {
			Map<String, Float> m = new HashMap<String, Float>();
			float max_diff = 0;
			for (Map.Entry<String, Set<String>> entry : words
					.entrySet()) {
				String key = entry.getKey();
				Set<String> value = entry.getValue();
				// 修改重启概率的关键代码
				m.put(key, (1 - d) * wordCountMap.get(key));
				for (String element : value) {
					// 分母
					int den = coWords.get(element).size();
					// 分子
					int mol = 0;
					// 计算分子
					for (int j = 0; j < den; j++) {
						if (key.equals(coWords.get(element).get(j))) {
							++mol;
						}
					}
					// int size = words.get(element).size();
					if (key.equals(element) || den == 0)
						continue;
					// PositionRank 公式
					m.put(key,
							m.get(key) + d * mol / den
									* (score.get(element) == null ? 0
											: score.get(element)));
				}
				// 遍历一轮结束后，保留两次迭代的最大差值
				max_diff = Math.max(max_diff,
						Math.abs(m.get(key) - (score.get(key) == null
								? 0 : score.get(key))));
			}
			for (Map.Entry<String, Float> entry1 : m.entrySet()) {
				System.out.print(
						entry1.getKey() + ":" + entry1.getValue());
			}
			System.out.println("");
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
	 * 改进版PositionRank 公式提取关键词（标题）
	 * @param titleTermList 文本标题
	 * @param termList 文本正文
	 * @return
	 */
	public Map<String, Float> getTermAndRank(List<Term> titleTermList,
			List<Term> termList) {
		// 只保留文本【正文】名词和形容词的结果
		List<String> wordList = new ArrayList<String>(termList.size());
		// 只保留文本【标题】名词和形容词的结果
		List<String> titleWordList =
				new ArrayList<String>(titleTermList.size());
		// for循环遍历去除（除名词和形容词）其他词性词汇
		for (Term t : termList) {
			if (shouldInclude(t)) {
				wordList.add(t.word);
			}
		}
		// for循环遍历去除（除名词和形容词）其他词性词汇
		for (Term t : titleTermList) {
			if (shouldInclude(t)) {
				titleWordList.add(t.word);
			}
		}
		System.out.println("文本【正文】去停用词及词性过滤的结果：");
		System.out.println(wordList);
		System.out.println("文本【标题】去停用词及词性过滤的结果：");
		System.out.println(titleWordList);
		// 存放单词共现结果（不包含重复单词）
		Map<String, Set<String>> words =
				new TreeMap<String, Set<String>>();
		// 存放单词共现结果（包含重复单词）
		Map<String, List<String>> coWords =
				new TreeMap<String, List<String>>();
		// 存放 PositionRank 中单词的重启概率
		Map<String, Float> wordCountMap =
				new HashMap<String, Float>(wordList.size());
		// 存放标题中的候选关键词重启概率
		for (int i = 0; i < titleWordList.size(); i++) {
			// wordCountMap是否包含标题中的候选关键词
			if (!wordCountMap.containsKey(titleWordList.get(i))) { // 不包含的情况
				wordCountMap.put(titleWordList.get(i),
						1.0f / titleWordList.size());
			} else { // 包含的情况
				wordCountMap.put(titleWordList.get(i),
						wordCountMap.get(titleWordList.get(i))
								+ 1.0f / titleWordList.size());
			}
		}
		// 存放单词位置
		int position = 0;
		// 借助que统计滑动窗口为5的共现单词
		Queue<String> que = new LinkedList<String>();
		for (String w : wordList) {
			// 存放正文中的候选关键词重启概率
			if (!wordCountMap.containsKey(w)) { // wordCountMap中不包含此候选关键词的情况
				wordCountMap.put(w, (wordList.size() - position++)
						* 1.0f / wordList.size());
			} else { // wordCountMap中包含此候选关键词的情况
				wordCountMap.put(w,
						wordCountMap.get(w)
								+ (wordList.size() - position++) * 1.0f
										/ wordList.size());
			}
			// 构建words，用来存放单词的共现单词
			if (!words.containsKey(w)) {
				words.put(w, new TreeSet<String>());
				coWords.put(w, new ArrayList<String>());
			}
			// 复杂度O(n-1)
			if (que.size() >= Global.slidingWindow) {
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
				// 构建共现关系
				coWords.get(w).add(qWord);
				coWords.get(qWord).add(w);
			}
			que.offer(w);
		}

		// 归一化重启概率
		float sum = 0;
		for (String key : wordCountMap.keySet()) {
			sum = sum + wordCountMap.get(key);
		}

		System.out.println("归一化后的重启概率：");
		for (String key : wordCountMap.keySet()) {
			wordCountMap.put(key, wordCountMap.get(key) / sum);
			System.out.print(key + ":" + wordCountMap.get(key) + " ");
		}

		/*Iterator it = wordCountMap.entrySet().iterator();
		System.out.println("归一化后的重启概率：");
		while (it.hasNext()) {
			Map.Entry<String, Float> e =
					(Map.Entry<String, Float>) it.next();
			wordCountMap.put(e.getKey(), e.getValue() / sum);
			System.out.print(e.getKey() + ":" + e.getValue() + " ");
		}*/
		// System.out.println(words);
		// 用于存放单词的分数
		Map<String, Float> score = new HashMap<String, Float>();
		// 依据TF来设置初值
		for (Map.Entry<String, Set<String>> entry : words.entrySet()) {
			// score.put(entry.getKey(), sigMoid(entry.getValue().size()));
			score.put(entry.getKey(), 1.0f / words.size());
		}
		System.out.println();
		System.out.println("迭代分数：");
		// 迭代计算单词的分数
		for (int i = 0; i < max_iter; ++i) {
			Map<String, Float> m = new HashMap<String, Float>();
			float max_diff = 0;
			for (Map.Entry<String, Set<String>> entry : words
					.entrySet()) {
				String key = entry.getKey();
				Set<String> value = entry.getValue();
				// 修改重启概率的关键代码
				m.put(key, (1 - d) * wordCountMap.get(key));
				for (String element : value) {
					// 分母
					int den = coWords.get(element).size();
					// 分子
					int mol = 0;
					// 计算分子
					for (int j = 0; j < den; j++) {
						if (key.equals(coWords.get(element).get(j))) {
							++mol;
						}
					}
					// int size = words.get(element).size();
					if (key.equals(element) || den == 0)
						continue;
					// PositionRank 公式
					m.put(key,
							m.get(key) + d * mol / den
									* (score.get(element) == null ? 0
											: score.get(element)));
				}
				// 遍历一轮结束后，保留两次迭代的最大差值
				max_diff = Math.max(max_diff,
						Math.abs(m.get(key) - (score.get(key) == null
								? 0 : score.get(key))));
			}
			for (Map.Entry<String, Float> entry1 : m.entrySet()) {
				System.out.print(
						entry1.getKey() + ":" + entry1.getValue());
			}
			System.out.println("");
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
				top(size, getTermAndRank(titleTermList, termList))
						.entrySet();
		List<String> result = new ArrayList<String>(entrySet.size());
		for (Map.Entry<String, Float> entry : entrySet) {
			result.add(entry.getKey());
		}
		return result;
	}

}
