package donew_cli.tasks

import java.io.File
import java.nio.file.Files

object NewDeno {

  def doNewDeno(name: String, args: String*): Unit = {
    val root = new java.io.File(name)

    println(s"mkdir ${name}...")
    val _ = root.mkdir()

    println(s"cd ${name}; denoinit")
    val r = sys.process.Process(Seq("denoinit"), root).!!
    println(r)

    val rakeFileContent = s"""task default: :usage

    task :usage do
      sh "rake -T"
    end

    desc "build"
    task :build do
      sh "mkdir dist && deno compile --unstable main.ts -o dist/${name}"
    end
   """
    Files.write(
      new File(root, "Rakefile").toPath,
      rakeFileContent.getBytes("UTF-8")
    )

    println(s"cd ${name} for your work!")
  }
}
