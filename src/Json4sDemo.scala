import org.json4s.jackson.Serialization
import org.json4s.{Formats, NoTypeHints}
import org.json4s.jackson.Serialization.write

/**
  * 仅供学习使用
  * */
object Json4sDemo {
    // 需要添加隐式转换
    implicit val formats: AnyRef with Formats = Serialization.formats(NoTypeHints)

    def toJSON(obj: Object): String = {
        write(obj)
    }


    def __main__(args: Array[String]): Unit = {

        val user1 = User(1, name = "李明", 12)
        val user2 = User(2, name = "张杰", 43)
        val user3 = User(3, name = "王伟", 54)
        val user4 = User(4, name = "刘安", 24)

        val users = List[User](user1, user2, user3, user4)
        // 由scala 对象转换为 Json字符串
        val str = write(user1)

        println(str)

        val sts = write(users)
        println(sts)
    }
    case class User(id: Int, name: String, age: Int)
}
