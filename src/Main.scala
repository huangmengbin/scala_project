import java.io.{File, FileWriter}

import scala.collection.mutable
import scala.io.{BufferedSource, Source}
/**
  * @author hmb
  */
object Main {

    val headsList: Seq[String] = "timestamp,volume,open,high,low,close,chg,percent,turnoverrate,amount,volume_post,amount_post,pe,pb,ps,pcf,market_capital,balance,hold_volume_cn,hold_ratio_cn,net_volume_cn,hold_volume_hk,hold_ratio_hk,net_volume_hk,stock_code".split(",").toList
    val headToIndex: Map[String, Int] = Array.range(0, headsList.length).map(i => (headsList(i), i)).toMap

    val TIME_STAMP = "timestamp"//冗余数据1
    val STOCK_CODE = "stock_code"//冗余数据2

    val HIGH = "high"//最高价
    val TURN_OVER_RATE = "turnoverrate"//周转率
    val PERCENT = "percent"//涨跌幅
    val VOLUME = "volume"//成交量
    val PB = "pb"//市盈
    val PE = "pe"//市净




    val WINDOWS = "windows"
    val LINUX = "linux"
    val environment: String = WINDOWS
    val PRE_PATH: String = if (environment==WINDOWS) "" else "/"

    //路径
    val INPUT_PATH = "testData.csv"
    val BEST_SECTOR_PATH: String = PRE_PATH + "output/bestSector.json"
    val BEST_SHARES_PATH: String = PRE_PATH + "output/bestShares.json"
    val FINAL_METRICS_PATH: String = PRE_PATH + "output/finalMetricOfAllSectors.json"
    val LATEST_MESSAGE_PATH: String = PRE_PATH + "output/latestMsgOfAllSectors.json"

    val mostImportantMerits: Seq[String] = Array(HIGH, TURN_OVER_RATE, PERCENT, VOLUME, PE, PB).toList
    val mostImportantIndexes: Seq[Int] = mostImportantMerits.map(s=>headToIndex(s))

    var sharesMap:mutable.Map[String, Shares] = mutable.Map()
    var sectorMap:mutable.Map[String, Sector] = mutable.Map()

    def Map_StringDouble_toString(myMap:Map[String, Double]): String ={
        myMap.map(t=>"\""+t._1+"\""+":"+"\""+t._2.toString+"\"").mkString("{",",","}")
    }
    def Map_StringString_toString(myMap:Map[String, String]): String ={
        myMap.map(t=>"\""+t._1+"\""+":"+"\""+t._2+"\"").mkString("{",",","}")
    }
    def Map_Long2Double_toString(myMap:mutable.Map[Long, Double]): String ={
        myMap.map(t=>"\""+t._1.toString+"\""+":"+"\""+t._2.toString+"\"").mkString("{",",","}")
    }
    def myRead(path:String) :Seq[String] = {
        val src: BufferedSource = Source.fromFile(path, "UTF-8")
        src.getLines().toList
    }

    def myWrite(path:String, data:String) :Unit = {
        //println(data)
        val printer = (new FileWriter(new File(path)))
        printer.write(data
//            .replace("\n","")
//            +"\n"
        )
        printer.close()
    }

    /**
      * 传入行、属性名
      * 返回属性值
      *
      * */
    def myGet(seq: Seq[String], string: String): String ={
        seq(headToIndex(string))
    }

    /**
      * 股票编号 -> 所在模块代号
      * 方便以后的变更。万一股票代号是不等长的呢？
      * */
    def getSectorCode(block_code:String): String ={
        block_code.substring(0,4)
    }

    def getBestSectorString: String = {
        val maxNum = sectorMap.map(_._2.finalSectorMetricAvg).max
        val tuple: (String, Sector) = sectorMap.filter(_._2.finalSectorMetricAvg == maxNum).head
        "{"+"\""+tuple._1+"\""+":"+tuple._2.toString+"}"
    }

    def getBestSharesString: String = {
        val maxNum = sharesMap.map(_._2.finalSharesMetric).max
        val tuple: (String, Shares) = sharesMap.filter(_._2.finalSharesMetric == maxNum).head
        "{"+"\""+tuple._1+"\""+":"+tuple._2.toString+"}"
    }

    def getLatestMessageOfAllSectorsString: String = {
        sectorMap.map(t=>"\""+t._1+"\""+":"+t._2.getLatestAverageMessageString).mkString("{\n",",\n","\n}")
    }


    def getFinalMetricOfAllSectorsString: String = {
        sectorMap.map(t=>"\""+t._1+"\""+":"+Map_Long2Double_toString(t._2.finalSectorMetricMap)).mkString("{\n",",\n","\n}")
    }




    def main(hmb666:Array[String]): Unit ={
        //    val path:String = ""
        //    val ssc = new StreamingContext(sc, Seconds(5))
        //    val lines = ssc.textFileStream(path)
        //    val words = lines.flatMap(_.split(" "))

        //    headToIndex.foreach(println)
        //    mostImportantIndexes.foreach(println)

        val lines: Seq[Seq[String]] = myRead(INPUT_PATH)
            .map(line=>line.split(",", -1).map(s=> if (s.isEmpty) "0" else s).toList)

        var shouldUpdate: Boolean = false   //我不知道这样会不会有不输出的问题。鬼知道传入的数据是什么形式的

        for (line: Seq[String] <- lines){
            val stock_code = myGet(line, STOCK_CODE)
            val sectorCode = getSectorCode(stock_code)

            if(!sectorMap.contains(sectorCode)){
                sectorMap += (sectorCode->new Sector(sectorCode))//注意new里面传入的是sectorCode
            }
            shouldUpdate = sectorMap(sectorCode).push(line, stock_code)   || shouldUpdate//注意push里面传入的是stock_code
        }

        if (!shouldUpdate){
            return
        }



//        print("{\n"+sharesMap.map(i=>"\""+i._1+"\""+":"+i._2.toString).mkString("{",",","}")+"\n}")
//        println(getLatestMessageOfAllSectorsString)


//
//
//            var newlines: Seq[Seq[String]] = lines.map(line =>mostImportantIndexes.map(ptr=>line(ptr) ) )
//            newlines.foreach(s=>println(s.mkString(",")))
//
//
//            lines.foreach(println)
//

//        myWrite(BEST_SHARES_PATH, sharesMap.map(i=>"\""+i._1+"\""+":"+i._2.toString).mkString("{\n",",\n","\n}"))
//        myWrite(BEST_SECTOR_PATH, sectorMap.map(i=>"\""+i._1+"\""+":"+i._2.toString).mkString("{\n",",\n","\n}"))


        myWrite(BEST_SECTOR_PATH, getBestSectorString)
        myWrite(BEST_SHARES_PATH, getBestSharesString)

        myWrite(LATEST_MESSAGE_PATH, getLatestMessageOfAllSectorsString)
        myWrite(FINAL_METRICS_PATH, getFinalMetricOfAllSectorsString)

    }

}