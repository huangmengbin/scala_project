import Main.TIME_STAMP
//import scala.collection.mutable.{Seq,Map}

/**
  * @author hmb 股票
  */
class Shares {

  val MAX_INT: Int = 30

  var sharesMessages : Seq[Map[String, String]] = Seq()//每一个map：股票的属性->值，所有的属性，很多天所以就是列表了
  private var allTimes: Seq[Int] =sharesMessages.map(each_map=>each_map(TIME_STAMP)).map(_.toInt)//用set可以优化

  def this(seq: Seq[String]) ={
    this()
  }

  def push(my_map:Map[String, String]): Unit = {
    val currentTime: Int = my_map(TIME_STAMP).toInt

    if (allTimes.contains(currentTime)){
      return
    }

    if(sharesMessages.length >= MAX_INT){
      val minimalTime:Int = allTimes.min//可以优化，若allTimes为空，可能报error
      if(currentTime < minimalTime){
        //我们的数据更旧，不操作
        return
      }
      else{
        //挑选一个最小的，删了那个最小的
        sharesMessages = sharesMessages.filter(each_map=>each_map(TIME_STAMP).toInt > minimalTime)//不可变的
      }
    }

    sharesMessages :+ my_map //不可变的，可能会报错
    allTimes = sharesMessages.map(each_map=>each_map(TIME_STAMP)).map(_.toInt)//优化了，就必须要同步更新了
  }



}

