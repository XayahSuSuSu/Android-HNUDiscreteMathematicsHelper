package com.xayah.hnudiscretemathematicshelper.Class

import kotlin.properties.Delegates

class CertainTaskClass {
    lateinit var knowpoint: String // 知识点
    lateinit var tkno: String // 题号
    lateinit var studans: String // 学生答案
    lateinit var scorestudnum: String // 得分
    lateinit var teachauditmsg: String // 评语
    lateinit var teachauditmsgqa: String // 主观题评语
    lateinit var studanstext: String // 学生答案2
    lateinit var studreply: String // 学生回复
    lateinit var datebegin: String // 开始时间
    lateinit var dateend: String // 截止时间
    lateinit var testtopic: String // 试题
    lateinit var id: String

    var isFirst by Delegates.notNull<Boolean>()// 是否第一次创建

    lateinit var certainTaskQuestionClass: CertainTaskQuestionClass // 具体题目类

}