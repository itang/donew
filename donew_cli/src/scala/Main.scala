package donew_cli

object Main {
  private val DefaultDenoProjectName = "deno_test1"

  def main(args: Array[String]): Unit = args match {
    case Array("deno")       => _donewDeno(DefaultDenoProjectName)
    case Array("deno", name) => _donewDeno(name)
    case _                   => println("any?")
  }

  private def _donewDeno(name: String, args: String*): Unit = {
    val root = new java.io.File(name)

    println(s"mkdir ${name}...")
    val _ = root.mkdir()

    println(s"cd ${name}; denoinit")
    val r = sys.process.Process(Seq("denoinit"), root).!!
    println(r)

    println(s"cd ${name} for your work!")
  }
}
