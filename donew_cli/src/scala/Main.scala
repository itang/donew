package donew_cli

import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Main {
  private val DefaultDenoProjectName = "deno_test_"
  private val DefaultGoProjectName = "go_test_"

  def main(args: Array[String]): Unit = args match {
    case Array("deno")       => _donewDeno(DefaultDenoProjectName + dateStr())
    case Array("deno", name) => _donewDeno(name)
    case Array("go")         => _donewGo(DefaultGoProjectName + dateStr())
    case Array("go", name)   => _donewGo(name)
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

  private def _donewGo(name: String, args: String*): Unit = {
    new GoProjectMaker(name, args).gen()
  }

  private def dateStr() =
    DateTimeFormatter.ofPattern("yyyyMMddHHmm").format(LocalDateTime.now())
}

//TODO:
trait ProjectMaker {
  def gen(): Unit
}

abstract class AbstractProjectMaker extends ProjectMaker {
  override def gen(): Unit = {
    println(s"mkdir ${name}...")
    val ok = root.mkdir()
    println(s"mkdir ${name} ok?: ${ok}")
    doWithShellInProjectDir()
    doWithWriteFiles()

    println(s"cd ${name} for your work!")
  }

  private def doWithShellInProjectDir(): Unit = {
    for (cmd <- getShells()) {
      val r = sys.process.Process(cmd, root).!!
      println(r)
    }
  }

  private def doWithWriteFiles() {
    import java.nio.file.Files
    for (FileWithContent(path, content) <- getFileWithContent()) {
      println(s"write ${path}...")
      Files.write(new File(root, path).toPath(), content.getBytes("utf-8"));
    }
  }

  def root = new File(name)

  def name: String

  def args: Seq[String]

  def getShells(): Seq[Seq[String]]

  def getFileWithContent(): Seq[FileWithContent]
}

case class FileWithContent(path: String, content: String)
class GoProjectMaker(val name: String, val args: Seq[String])
    extends AbstractProjectMaker {
  override def getShells =
    Seq(Seq("go", "mod", "init", s"github.com/itang/${name}"))

  override def getFileWithContent() =
    Seq(FileWithContent("main.go", s"""package main

import "fmt"

func main(){
    fmt.Println("Hello, World!")
}
"""))
}
