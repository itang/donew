# frozen_string_literal: true

task default: :usage

task :usage do
  sh 'rake -T'
end

namespace :gen do
  desc 'gen idea project'
  task :idea do
    sh 'rake clean'
    sh 'rake clean_idea'
    sh 'mill mill.scalalib.GenIdea/idea'
  end
end

desc 'run dev mode'
task :run do
  sh 'tally mill donew_cli.run'
end

desc 'dist'
task :dist do
  sh 'mill donew_cli.assembly'
  sh 'cd out;
$GRAALVM_HOME/bin/native-image --no-server -cp donew_cli/assembly/dest/out.jar -H:Name=donew -H:+ReportUnsupportedElementsAtRuntime -H:+RemoveSaturatedTypeFlows donew_cli.Main'
  # sh 'cd out;upx donew_cli'
end

desc 'clean'
task :clean do
  sh 'rm -rf out/'
end

desc 'clean_idea'
task :clean_idea do
  sh 'rm -rf .idea .idea_modules'
end

desc 'install'
task install: :dist do
  sh 'cp out/donew ~/.local/bin/'
end
