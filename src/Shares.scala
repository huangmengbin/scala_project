import Main.{TIME_STAMP, headsList, myGet}

/**
  * @author hmb 股票
  */
class Shares {

    val MAX_SIZE: Int = 30
    var block_code: String = ""
    var sharesMessages : Seq[Seq[String]] = Seq()//每一个map：股票的属性->值，所有的属性. 很多天所以就是列表了
    private def getUpdatedAllTimes: Seq[Long] = sharesMessages.map(each_seq=>myGet(each_seq, TIME_STAMP)).map(_.toLong)//用set可以优化
    private var allTimes: Seq[Long] = getUpdatedAllTimes
    private var minimalTime:Long = 0

    def this(string: String){
        this()
        block_code =string
    }

    def push(my_seq:Seq[String]): Unit = {
        val currentTime: Long = myGet(my_seq, TIME_STAMP).toLong
        //    看了看爬虫，可能不再需要太多的判断了。关于times的或许能删除了。
        //    当然了。多一些判断也不是什么坏事，总是会更安全的
        //    if (allTimes.contains(currentTime)){
        //      return
        //    }


        if(sharesMessages.length >= MAX_SIZE){
            if(currentTime < minimalTime){
                //我们的数据更旧，不操作
                return
            }
            else{
                //挑选一个最小的，删了那个最小的
                sharesMessages = sharesMessages.filter(each_seq=>myGet(each_seq, TIME_STAMP).toLong > minimalTime)//不可变的
            }
        }

        sharesMessages = sharesMessages :+ my_seq
        allTimes = getUpdatedAllTimes//优化了，就必须要同步更新了
        minimalTime = allTimes.min//优化了，就必须要同步更新了
    }


    //todo
    // 要求：要转成json格式的字符串
    override def toString: String = {
        //时间戳->一个Map; 第二个Map中,属性->对应的值;
        val obj: Map[String, Map[String, String]] = sharesMessages.map(each_seq=>
            myGet(each_seq, TIME_STAMP) -> headsList.map( head=> head->myGet(each_seq,head) ).toMap
        ).toMap
        obj.map(i=>i._1+":"+i._2.mkString("{\n\t\t",",\n\t\t","\n\t}")).mkString("{\n\t",",\n\t","\n}\n").replace("->",":")
        //org.json4s.jackson.Serialization.write(obj)
    }

}

