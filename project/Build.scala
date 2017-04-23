import sbt._

object BuildSettings extends Build {
  println(s"Os name: ${sys.props("os.name")}")

  val envKey: SettingKey[String] = settingKey[String]("env")

  val osMapping = Map(
    "windows" -> "natives-windows",
    "mac" -> "natives-macos",
    "linux" -> "natives-linux"
  )
  val os: String = sys.props("os.name").toLowerCase

  val env: String = osMapping.find{ case(k,v) => os.contains(k) }
    .getOrElse(throw new Exception(s"Cannot find OS [$os]"))
    ._2

  override def settings: Seq[Def.Setting[_]] = {
    super.settings ++ Seq(
      envKey := env
    )
  }

}
