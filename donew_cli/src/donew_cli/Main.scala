package donew_cli

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Main {
  private val DefaultDenoProjectName = "deno_test_"
  private val DefaultGoProjectName = "go_test_"

  def main(args: Array[String]): Unit = args match {
    case Array("deno") =>
      tasks.NewDeno.doNewDeno(DefaultDenoProjectName + dateStr())
    case Array("deno", name) =>
      tasks.NewDeno.doNewDeno(name)
    case Array("go") =>
      tasks.NewGo.doNewGo(DefaultGoProjectName + dateStr())
    case Array("go", name) =>
      tasks.NewGo.doNewGo(name)
    case Array("invoke") =>
      tasks.NewInvoke.doNewInvoke()
    case _ =>
      println("any?")
  }

  private def dateStr() =
    DateTimeFormatter.ofPattern("yyyyMMddHHmm").format(LocalDateTime.now())
}
