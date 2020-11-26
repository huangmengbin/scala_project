import Main.{TIME_STAMP, myGet}


/**
  * @author hmb 股票
  */
class Shares {

  val MAX_SIZE: Int = 30

  var sharesMessages : Seq[Seq[String]] = Seq()//每一个map：股票的属性->值，所有的属性，很多天所以就是列表了
  private var allTimes: Seq[Long] =sharesMessages.map(each_map=>myGet(each_map, TIME_STAMP)).map(_.toLong)//用set可以优化


  def push(my_seq:Seq[String]): Unit = {
    val currentTime: Long = myGet(my_seq, TIME_STAMP).toLong

//    if (allTimes.contains(currentTime)){    //看了看爬虫，可能不再需要太多的判断了。关于times的或许能删除了。
//      return
//    }

    val minimalTime:Long = if(allTimes.isEmpty) 0 else allTimes.min//可以优化

    if(sharesMessages.length >= MAX_SIZE){
      if(currentTime < minimalTime){
        //我们的数据更旧，不操作
        return
      }
      else{
        //挑选一个最小的，删了那个最小的
        sharesMessages = sharesMessages.filter(each_map=>myGet(each_map, TIME_STAMP).toLong > minimalTime)//不可变的
      }
    }

    sharesMessages = sharesMessages :+ my_seq //不可变的，可能会报错
    allTimes = sharesMessages.map(each_map=>myGet(each_map, TIME_STAMP)).map(_.toLong)//优化了，就必须要同步更新了
  }



}

