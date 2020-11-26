import Main.{headsList, mostImportantIndexes}
/**
  * @author hmb
  *         板块
  *         存在很大的数据冗余，请无视之
  */
class Sector {

    val MAX_INT: Int = 30
    var sector_code: String = ""

    var averageMessages : Map[Long, Map[String, String]] = Map()
    //每一个map：版块的属性->值（均值之类），很多天所以就是列表了
    //板块和股票的不同点，就是它不一定是原始数据，很可能是累加之后再平均得到的
    //可以考虑加权平均
    //属性只选择有意义的那些

    var totalSumMessages : Map[Long, Map[String, String]] = Map()
    //记录总值，总不能只保留平均值，每次来一个数据乘了再除吧


    var sharesList : Seq[Shares] = Seq()    //这个有待讨论，如何维护此其中最好的股票

    def this(string: String){
        this()
        sector_code =string
    }

    def push(my_seq:Seq[String]): Unit = {

        val my_map: Map[String, String] = mostImportantIndexes.map(i => ( headsList(i), my_seq(i) )).toMap
    }

    //todo
    // 要求：要转成json格式的字符串
    override def toString: String = {
        super.toString
    }

}
