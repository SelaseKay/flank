package ftl.presentation.cli.firebase.test.ios.orientations

import ftl.config.FtlConstants
import ftl.domain.ListIosOrientations
import ftl.domain.invoke
import picocli.CommandLine

@CommandLine.Command(
    name = "list",
    headerHeading = "",
    synopsisHeading = "%n",
    descriptionHeading = "%n@|bold,underline Description:|@%n%n",
    parameterListHeading = "%n@|bold,underline Parameters:|@%n",
    optionListHeading = "%n@|bold,underline Options:|@%n",
    header = ["List of device orientations available to test against"],
    description = ["Print current list of iOS orientations available to test against"],
    usageHelpAutoWidth = true
)
class IosOrientationsListCommand :
    Runnable,
    ListIosOrientations {

    @CommandLine.Option(
        names = ["-c", "--config"],
        description = ["YAML config file path"]
    )
    override var configPath: String = FtlConstants.defaultIosConfig

    @CommandLine.Option(
        names = ["-h", "--help"],
        usageHelp = true,
        description = ["Prints this help message"]
    )
    var usageHelpRequested: Boolean = false

    override fun run() = invoke()
}