import Main.{Map_StringString_toString, TIME_STAMP, headsList, myGet}

/**
  * @author hmb 股票
  */
class Shares {

    val MAX_SIZE: Int = 30
    var block_code: String = ""
    var sharesMessages : Seq[Seq[String]] = Seq()//小列表放属性对应的值. 很多天所以就是大列表了.没有体现时间，因为它很容易得到
    private def getUpdatedAllTimes: Seq[Long] = sharesMessages.map(each_seq=>myGet(each_seq, TIME_STAMP)).map(_.toLong)//用set可以优化
    private var allTimes: Seq[Long] = getUpdatedAllTimes
    private var minimalTime:Long = 0

    var finalSharesMetric: Double = 0.0       //最终指标，放在这里

    def this(string: String){
        this()
        block_code =string
    }

    def push(my_seq:Seq[String]): Boolean = {
        val currentTime: Long = myGet(my_seq, TIME_STAMP).toLong

        if(sharesMessages.length >= MAX_SIZE) {
            if (currentTime <= minimalTime) {
                //我们的数据更旧，不操作
                return false
            }
        }

        if (allTimes.contains(currentTime)){
          return false
        }

        if(sharesMessages.length >= MAX_SIZE) {//挑选一个最小的，删了那个最小的
            sharesMessages = sharesMessages.filter(each_seq => myGet(each_seq, TIME_STAMP).toLong > minimalTime) //不可变的
        }

        sharesMessages = sharesMessages :+ my_seq
        allTimes = getUpdatedAllTimes//优化了，就必须要同步更新了
        minimalTime = allTimes.min//优化了，就必须要同步更新了
        true
    }


    //todo
    // 要求：要转成json格式的字符串
    override def toString: String = {
        //时间戳->一个Map; 第二个Map中,属性->对应的值;
        val obj: Map[String, Map[String, String]] = sharesMessages.map(each_seq=>
            myGet(each_seq, TIME_STAMP) -> headsList.map( head=> head->myGet(each_seq,head) ).toMap
        ).toMap
        obj.map(i=>"\""+i._1+"\""+":"+Map_StringString_toString(i._2)).mkString("{\n\t",",\n\t","\n}").replace("->",":")
        //org.json4s.jackson.Serialization.write(obj)
    }

}

