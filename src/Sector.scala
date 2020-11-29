import Main._

import scala.collection.mutable

/**
  * @author hmb
  *         板块
  *         存在很大的数据冗余，请无视之
  */
class Sector {

    val MAX_SIZE: Int = 30
    var sector_code: String = ""

    var allSharesMessages : mutable.Map[Long, mutable.Map[String, Seq[String]]] = mutable.Map()
    //每一个时间戳对应一个小map：股票编号->股票各种值（原始)


    var totalSumMessages : mutable.Map[Long, Map[String, String]] =mutable.Map()
    //记录总值，说不定有缓存的作用，使得平均值算的更快。暂时还没用
    //不再有股票编号这个信息了，小Map的key代表了属性,也就是head
    var averageMessages : mutable.Map[Long, Map[String, Double]] = mutable.Map()
    //板块和股票的不同点，就是它不一定是原始数据，很可能是累加之后再平均得到的
    //可以考虑加权平均
    //属性只选择有意义的那些

    var finalSectorMetricMap: mutable.Map[Long, Double] = mutable.Map()

    var finalSectorMetricAvg: Double = 0.0

    private def updateSectorMetricMap(currentTime:Long): Boolean = {
        val validMap = sectorMap
            .filter(tp=>tp._2.averageMessages.contains(currentTime))
            .filter(tp=>tp._2.averageMessages(currentTime)(PE)>0)
        val allPE_List: mutable.Iterable[Double] = validMap
            .map(tp=>tp._2.averageMessages(currentTime)(PE))

        if (allPE_List.isEmpty){
            false
        }
        else{
            val maxDistance: Double = allPE_List.max - allPE_List.min + 1
            val avgPE: Double = allPE_List.sum / allPE_List.size
            validMap.keys.foreach(stock_code=> {
                val currentPE = validMap(stock_code).averageMessages(currentTime)(PE)
                val currentTurnOverRate = validMap(stock_code).averageMessages(currentTime)(TURN_OVER_RATE)
                validMap(stock_code).finalSectorMetricMap(currentTime) =
                    if (currentPE >= avgPE) {
                        (currentPE-avgPE)/maxDistance*6.7*currentTurnOverRate-currentPE*0.1+67
                    }
                    else {
                        (avgPE-currentPE)/maxDistance*2.33*currentTurnOverRate-currentPE*0.1+67
                    }
                if(validMap(stock_code).finalSectorMetricMap.size>MAX_SIZE){
                    validMap(stock_code).finalSectorMetricMap.remove(validMap(stock_code).finalSectorMetricMap.keys.min)
                }
            })
            //更新均值
            finalSectorMetricAvg = finalSectorMetricMap.values.sum / finalSectorMetricMap.size
            true
        }
    }



    def getLatestAverageMessageString: String ={
        if (averageMessages.isEmpty)return "{}"
        val maxTime: Long = averageMessages.keys.max
        val latestMap: Map[String, Double] = averageMessages(maxTime)
        Map_StringDouble_toString(latestMap)
    }

    private var minimalTime:Long = Long.MaxValue


    var sharesSet : mutable.Set[Shares] = mutable.Set()    //这个有待讨论，如何维护此其中最好的股票

    def this(string: String){
        this()
        sector_code =string
    }



    def push(my_seq:Seq[String], stock_code: String): Boolean = {

        if(!sharesMap.contains(stock_code)){
            sharesMap += (stock_code->new Shares(stock_code))
        }
        var shares: Shares = sharesMap(stock_code)
        this.sharesSet += shares
        val shouldUpdate: Boolean = shares.push(my_seq)
        if(!shouldUpdate){
            return false
        }
        //根据以上的判定，一定不会有重复数据发生了。比如说更旧的

        val currentTime: Long = myGet(my_seq, TIME_STAMP).toLong
        if(!allSharesMessages.contains(currentTime)){
            allSharesMessages += (currentTime -> mutable.Map())
            totalSumMessages += (currentTime -> Map())
            averageMessages += (currentTime -> Map())
        }
        if(allSharesMessages.size > MAX_SIZE){
            allSharesMessages.remove(minimalTime)
            totalSumMessages.remove(minimalTime)
            averageMessages.remove(minimalTime)
        }
        minimalTime = allSharesMessages.keys.min

        val currentTuple: (String, Seq[String]) = (stock_code , my_seq)
        allSharesMessages(currentTime) += currentTuple

//        for (tempSeq: Seq[String] <-allSharesMessages(currentTime).values){
//            var a: Map[String, String] =mostImportantIndexes.map(i=>(headsList(i)-> tempSeq(i))).toMap
//        }
        val currentTotal: Map[String, String] = allSharesMessages(currentTime).values.map((tempSeq: Seq[String]) =>
            (mostImportantIndexes.map(ptr=>(headsList(ptr)-> tempSeq(ptr)))/*:+(TIME_STAMP,myGet(tempSeq, TIME_STAMP)):+(STOCK_CODE,myGet(tempSeq, STOCK_CODE))*/)
            .toMap).reduce( (map1,map2) => {
            map2.map(t => t._1 -> (t._2.toDouble + map1(t._1).toDouble).toString)
        })
        val currentAverage: Map[String, Double] = currentTotal.map(t=> (t._1 -> t._2.toDouble / allSharesMessages.size))

        totalSumMessages(currentTime) = currentTotal
        averageMessages(currentTime) = currentAverage

        updateSectorMetricMap(currentTime)

        true
    }




    //todo
    // 要求：要转成json格式的字符串
    override def toString: String = {
        averageMessages.map(tp1=>"\""+tp1._1+"\""+":"
            +tp1._2.map(tp2=>"\""+tp2._1+"\""+":"+"\""+tp2._2+"\"").mkString("{\n",",\n","\n}")
        ).mkString("{\n",",\n","\n}")
    }

}
