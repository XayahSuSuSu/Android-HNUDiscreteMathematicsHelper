package com.xayah.hnudiscretemathematicshelper.Class

class CertainTaskQuestionClass {
    lateinit var qId: String // 生成试卷/题号
    lateinit var qPoint: String // 知识点
    lateinit var qTitle: String // 题干
    lateinit var qOption: MutableList<String> // 6个选项
    lateinit var qAnswer: String // 标答
    lateinit var qExplain: String // 讲解
    lateinit var qDegree: String // 难度
}