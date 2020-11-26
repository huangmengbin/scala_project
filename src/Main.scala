import scala.io.{BufferedSource, Source}
/**
  * @author hmb
  */
object Main {

  private val headsList: Seq[String] = "timestamp,volume,open,high,low,close,chg,percent,turnoverrate,amount,volume_post,amount_post,pe,pb,ps,pcf,market_capital,balance,hold_volume_cn,hold_ratio_cn,net_volume_cn,hold_volume_hk,hold_ratio_hk,net_volume_hk".split(",").toList
  private val headToIndex: Map[String, Int] = Array.range(0, headsList.length).map(i => (headsList(i), i)).toMap

  val HIGH = "high"//最高价
  val TURN_OVER_RATE = "turnoverrate"//周转率
  val PERCENT = "percent"//涨跌幅
  val VOLUME = "volume"//成交量
  val PB = "pb"//市盈
  val PE = "pe"//市净

  private val mostImportantMerits: Seq[String] = Array(HIGH, TURN_OVER_RATE, PERCENT, VOLUME, PE, PB).toList
  private val mostImportantIndexes: Seq[Int] = mostImportantMerits.map(s=>headToIndex(s))

  def myread(path:String) :Seq[String] = {
    val src: BufferedSource = Source.fromFile(path, "UTF-8")
    src.getLines().toList
  }

  def main(hmb666:Array[String]): Unit ={
//    val path:String = ""
//    val ssc = new StreamingContext(sc, Seconds(5))
//    val lines = ssc.textFileStream(path)
//    val words = lines.flatMap(_.split(" "))

//    headToIndex.foreach(println)
//    mostImportantIndexes.foreach(println)

    var lines: Seq[Seq[String]] = myread("SZ300008-1606154245000.csv")
  .drop(1)
  .map(line=>line.split(",", -1).map(s=> if (s.isEmpty) "0" else s).toList)

//    var newlines: Seq[Seq[String]] = lines.map(line =>mostImportantIndexes.map(ptr=>line(ptr) ) )
//    newlines.foreach(s=>println(s.mkString(",")))


    lines.foreach(println)

  }

}