/*
 * <summary></summary>
 * <author>He Han</author>
 * <email>hankcs.cn@gmail.com</email>
 * <create-date>2014/12/7 19:25</create-date>
 *
 * <copyright file="DemoChineseNameRecoginiton.java" company="上海林原信息科技有限公司">
 * Copyright (c) 2003-2014+ 上海林原信息科技有限公司. All Right Reserved+ http://www.linrunsoft.com/
 * This source is subject to the LinrunSpace License. Please contact 上海林原信息科技有限公司 to get more information.
 * </copyright>
 */
package com.hankcs.demo;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.summary.TextRankKeyword;

import java.util.List;

/**
 * 关键词提取
 * @author hankcs
 */
public class DemoKeyword
{
    public static void main(String[] args)
    {
        String content = "牢记总书记嘱托　为奋力谱写新时代中原更加出彩的绚丽篇章贡献公安力量 会议指出，在喜迎新中国成立70周年的重要时刻、第一第二批“不忘初心、牢记使命”主题教育的重要节点、推动高质量发展的重要关口，习近平总书记亲临河南考察调研，亲自主持召开黄河流域生态保护和高质量发展座谈会，并发表重要讲话，充分体现了总书记对河南工作的高度重视，对河南人民的关心厚爱。学习好、宣传好、贯彻好习近平总书记考察调研河南时的重要讲话精神，是全省公安机关当前和今后一个时期首要政治任务和头等大事，全省公安机关要紧密结合河南社会稳定形势和公安工作实践，努力将学习成果转化为推动高质量公安建设的强大动力，为奋力谱写新时代中原更加出彩的绚丽篇章贡献公安力量。\n" +
            "    会议要求，要深刻感受总书记对河南人民的亲切关怀、对河南工作的殷切期望，在深学细悟上下足功夫。要提升站位学，站在增强“四个意识”、坚定“四个自信”、做到“两个维护”的高度，自觉学、及时学、主动学，深入交流研讨，主动宣传宣讲，切实把总书记重要讲话精神作为引领河南公安工作发展的总方针总纲领总遵循。要融会贯通学，把学习贯彻习近平总书记重要讲话精神与学习贯彻总书记2014年视察指导河南时的重要讲话精神结合起来，与学习贯彻总书记参加今年全国两会河南代表团审议时的重要讲话精神贯通起来，与党的十八大以来总书记对河南工作的重要指示批示联系起来，紧密结合总书记关于新时代公安工作的重要论述，融会贯通，举一反三，深刻领会贯穿其中的精髓要义。要领导带头学，从厅党委和领导干部做起，先学一步，学深一层，以层层带头学习推动层层工作落实，切实用习近平总书记的重要讲话精神武装头脑、指导实践、推动工作。\n" +
            "    会议要求，要深刻认识总书记重要讲话的重大政治意义、鲜明里程碑意义和深远历史意义，在对标对表上下足功夫。这次考察调研，习近平总书记明确了我省在中部地区崛起中奋勇争先的指导方针、主攻方向和方法路径，全省公安机关要按照总书记强调的“河南安则天下安”的重要指示和“要加强社会治安综合治理，深化扫黑除恶专项斗争，提高基层社会治理能力，积极推广‘枫桥经验’”等重大要求，自觉校正偏差、找准定位，不断加强和改进工作，主动提升能力水平。要对实党对公安工作的绝对领导，在思想上更加自觉地强化理论武装，行动上更加自觉地践行“两个维护”，能力上更加自觉地打造高素质过硬公安队伍。要对准河南经济社会发展大势，深入推进“扫黑除恶”专项斗争，适时开展严厉打击破坏黄河流域生态违法犯罪专项行动，持续加强农村社会治安防控体系建设。要对严总书记重要指示批示，对涉及公安机关的任务，紧抓不放，一抓到底，始终在思想上行动上同习近平同志为核心的党中央保持高度一致。要对好现实和潜在的风险隐患，坚持底线思维、增强忧患意识，用好依靠群众、运用科技、严格执法三个有力武器，自觉扎紧防范篱笆，主动化解风险隐患。\n" +
            "    会议强调，要深刻领会总书记重要讲话的丰富内涵和精神实质，在埋头苦干上下足功夫。要把习近平总书记的肯定鼓励化为以习近平新时代中国特色社会主义思想指导一切工作、处理一切问题的政治自觉和行动自觉，坚持稳中求进工作总基调，纵深推进高质量公安建设。要在“稳”上重干，从社会稳定着眼，坚持稳中求进、行稳致远，扎实做好维护国家安全和社会稳定各项工作。要在“防”上实干，着力防控风险、筑牢防线、守住底线。要在“进”上苦干，始终保持锐意进取、奋发有为的精神状态，在扎实抓好今年维护安全稳定各项措施落实、确保社会大局持续稳定的基础上，统筹推进管长远、利全局、打基础的工作。要在“责”上大干，牢牢把握从严从实从细这个抓落实基本导向，切实担负起维护捍卫政治安全、维护社会安定、保障人民安宁的职责使命。\n";
        List<String> keywordList = HanLP.extractKeyword(content, 5);
        System.out.println(keywordList);
    }
}
