package donew_cli.tasks

import java.io.File
import java.nio.file.Files

object NewInvoke {
  def doNewInvoke(): Unit = {
    val d = "\"\"\""
    val s =
      s"""|from invoke import task
          |
          |
          |@task
          |def version(c, cmd=None):
          |    "println version"
          |    cmd = 'invoke' if cmd == None else cmd
          |    v = ('-version' if cmd == 'java' else
          |         'version' if cmd == 'go' else
          |         '--version'
          |         )
          |
          |    c.run('{} {}'.format(cmd, v))
          |
          |
          |@task(default=True)
          |def usage(c):
          |    ${d}Usage${d}

          |    c.run('invoke -l')

          |""".stripMargin
    Files.write(new File("tasks.py").toPath, s.getBytes("UTF-8"))
  }
}
