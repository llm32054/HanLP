/*
 * <author>Hankcs</author>
 * <email>me@hankcs.com</email>
 * <create-date>2016-09-12 PM4:22</create-date>
 *
 * <copyright file="TfIdfKeyword.java" company="码农场">
 * Copyright (c) 2016, 码农场. All Right Reserved, http://www.hankcs.com/
 * This source is subject to Hankcs. Please contact Hankcs to get more information.
 * </copyright>
 */
package com.hankcs.hanlp.mining.word;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hankcs.hanlp.algorithm.MaxHeap;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.summary.KeywordExtractor;
import com.hankcs.hanlp.tokenizer.StandardTokenizer;

/**
 * TF-IDF统计工具兼关键词提取工具
 *
 * @author hankcs
 */
public class TfIdfCounter extends KeywordExtractor {
	private boolean filterStopWord;
	private Map<Object, Map<String, Double>> tfMap;
	private Map<Object, Map<String, Double>> tfidfMap;
	private Map<String, Double> idf;

	/**
	 * 构造方法
	 */
	public TfIdfCounter() {
		this(true);
	}

	/**
	 * 构造方法
	 */
	public TfIdfCounter(boolean filterStopWord) {
		this(StandardTokenizer.SEGMENT, filterStopWord);
	}

	/**
	 * 构造方法
	 */
	public TfIdfCounter(Segment defaultSegment,
			boolean filterStopWord) {
		super(defaultSegment);
		this.filterStopWord = filterStopWord;
		tfMap = new HashMap<Object, Map<String, Double>>();
	}

	/**
	 * 构造方法
	 */
	public TfIdfCounter(Segment defaultSegment) {
		this(defaultSegment, true);
	}

	/**
	 * 提取关键词
	 *
	 * @param document 文档内容
	 * @param size     希望提取几个关键词
	 * @return 一个列表
	 */
	public List<String> getKeywordList(TfIdfCounter tfIdfCounter,
			String document, int size) {
		return tfIdfCounter.getKeywords(document, size);
	}

	@Override
	public List<String> getKeywords(List<Term> termList, int size) {
		List<Map.Entry<String, Double>> entryList =
				getKeywordsWithTfIdf(termList, size);
		List<String> r = new ArrayList<String>(entryList.size());
		for (Map.Entry<String, Double> entry : entryList) {
			r.add(entry.getKey());
		}

		return r;
	}

	/**
	 * 关键词提取
	 * @param termList 分词结果
	 * @param size 提取个数
	 * @return
	 */
	public List<Map.Entry<String, Double>>
			getKeywordsWithTfIdf(List<Term> termList, int size) {
		if (idf == null)
			compute();

		Map<String, Double> tfIdf =
				TfIdf.tfIdf(TfIdf.tf(convert1(termList)), idf);
		return topN(tfIdf, size);
	}

	/**
	 * 计算逆文档频率idf和tfidfMap
	 * @return
	 */
	public Map<Object, Map<String, Double>> compute() {
		// 计算idf逆文档频率
		idf = TfIdf.idfFromTfs(tfMap.values());
		// 存储tfidf
		tfidfMap = new HashMap<Object, Map<String, Double>>(idf.size());
		// 遍及tfMap就算tfidf
		for (Map.Entry<Object, Map<String, Double>> entry : tfMap
				.entrySet()) {
			// 计算tf-idf值
			Map<String, Double> tfidf =
					TfIdf.tfIdf(entry.getValue(), idf);
			tfidfMap.put(entry.getKey(), tfidf);
		}
		return tfidfMap;
	}

	private List<Map.Entry<String, Double>>
			topN(Map<String, Double> tfidfs, int size) {
		MaxHeap<Map.Entry<String, Double>> heap =
				new MaxHeap<Map.Entry<String, Double>>(size,
						new Comparator<Map.Entry<String, Double>>() {
							@Override
							public int compare(
									Map.Entry<String, Double> o1,
									Map.Entry<String, Double> o2) {
								return o1.getValue()
										.compareTo(o2.getValue());
							}
						});

		heap.addAll(tfidfs.entrySet());
		return heap.toList();
	}

	public List<Map.Entry<String, Double>>
			getKeywordsWithTfIdf(String document, int size) {
		return getKeywordsWithTfIdf(preprocess(document), size);
	}

	public void add(List<Term> termList) {
		add(tfMap.size(), termList);
	}

	/**
	 * 添加文档，自动分配id
	 *
	 * @param text String类型文本
	 */
	public int add(String text) {
		int id = tfMap.size();
		add(id, text);
		return id;
	}

	/**
	 * 添加文档
	 *
	 * @param id   文档id
	 * @param text 文档内容
	 */
	public void add(Object id, String text) {
		List<Term> termList = preprocess(text);
		add(id, termList);
	}

	/**
	 * 预处理（分词和词性过滤）
	 * @param text 文本
	 * @return 分词、词性过滤后结果
	 */
	private List<Term> preprocess(String text) {
		List<Term> termList = defaultSegment.seg(text);
		if (filterStopWord) {
			filter(termList);
		}
		return termList;
	}

	/**
	 * 添加文档
	 * @param id 文档id
	 * @param termList 分词、词性过滤后结果
	 */
	public void add(Object id, List<Term> termList) {
		List<String> words = convert(termList);
		Map<String, Double> tf = TfIdf.tf(words);
		tfMap.put(id, tf);
		idf = null;
	}

	/**
	 * Term转换String
	 * @param termList 分词结果
	 * @return List<String>类型结果
	 */
	private static List<String> convert(List<Term> termList) {
		List<String> words = new ArrayList<String>(termList.size());
		for (Term term : termList) {
			words.add(term.word);
		}
		return words;
	}

	/**
	 * 类型转换（Term转换String）、去停用词和词性过滤
	 * @param termList 分词结果
	 * @return 去停用词和词性过滤后的List<String>类型结果
	 */
	private static List<String> convert1(List<Term> termList) {
		List<String> words = new ArrayList<String>(termList.size());
		TfIdfCounter tfIdfCounter = new TfIdfCounter();
		for (Term term : termList) {
			if (tfIdfCounter.shouldInclude(term)) { // 去停用词
				words.add(term.word);
			}
		}
		return words;
	}

	public List<Map.Entry<String, Double>> getKeywordsOf(Object id) {
		return getKeywordsOf(id, 10);
	}

	public List<Map.Entry<String, Double>> getKeywordsOf(Object id,
			int size) {
		Map<String, Double> tfidfs = tfidfMap.get(id);
		if (tfidfs == null)
			return null;

		return topN(tfidfs, size);
	}

	public Set<Object> documents() {
		return tfMap.keySet();
	}

	public Map<Object, Map<String, Double>> getTfMap() {
		return tfMap;
	}

	public List<Map.Entry<String, Double>> sortedAllTf() {
		return sort(allTf());
	}

	public List<Map.Entry<String, Integer>> sortedAllTfInt() {
		return doubleToInteger(sortedAllTf());
	}

	public Map<String, Double> allTf() {
		Map<String, Double> result = new HashMap<String, Double>();
		for (Map<String, Double> d : tfMap.values()) {
			for (Map.Entry<String, Double> tf : d.entrySet()) {
				Double f = result.get(tf.getKey());
				if (f == null) {
					result.put(tf.getKey(), tf.getValue());
				} else {
					result.put(tf.getKey(), f + tf.getValue());
				}
			}
		}

		return result;
	}

	private static List<Map.Entry<String, Double>>
			sort(Map<String, Double> map) {
		List<Map.Entry<String, Double>> list =
				new ArrayList<Map.Entry<String, Double>>(
						map.entrySet());
		Collections.sort(list,
				new Comparator<Map.Entry<String, Double>>() {
					@Override
					public int compare(Map.Entry<String, Double> o1,
							Map.Entry<String, Double> o2) {
						return o2.getValue().compareTo(o1.getValue());
					}
				});

		return list;
	}

	private static List<Map.Entry<String, Integer>>
			doubleToInteger(List<Map.Entry<String, Double>> list) {
		List<Map.Entry<String, Integer>> result =
				new ArrayList<Map.Entry<String, Integer>>(list.size());
		for (Map.Entry<String, Double> entry : list) {
			result.add(new AbstractMap.SimpleEntry<String, Integer>(
					entry.getKey(), entry.getValue().intValue()));
		}

		return result;
	}

	@Override
	public List<String> getKeywords(List<Term> titleTermList,
			List<Term> termList, int size) {
		// TODO Auto-generated method stub
		return null;
	}
}
