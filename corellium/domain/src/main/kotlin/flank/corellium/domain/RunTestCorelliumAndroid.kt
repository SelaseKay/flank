package flank.corellium.domain

import flank.apk.Apk
import flank.corellium.api.Authorization
import flank.corellium.api.CorelliumApi
import flank.corellium.domain.RunTestCorelliumAndroid.Context
import flank.corellium.domain.RunTestCorelliumAndroid.State
import flank.corellium.domain.run.test.android.step.authorize
import flank.corellium.domain.run.test.android.step.cleanUpInstances
import flank.corellium.domain.run.test.android.step.createOutputDir
import flank.corellium.domain.run.test.android.step.dumpShards
import flank.corellium.domain.run.test.android.step.executeTests
import flank.corellium.domain.run.test.android.step.finish
import flank.corellium.domain.run.test.android.step.generateReport
import flank.corellium.domain.run.test.android.step.installApks
import flank.corellium.domain.run.test.android.step.invokeDevices
import flank.corellium.domain.run.test.android.step.parseApksInfo
import flank.corellium.domain.run.test.android.step.parseTestCasesFromApks
import flank.corellium.domain.run.test.android.step.prepareShards
import flank.corellium.domain.util.CreateTransformation
import flank.corellium.domain.util.execute
import flank.corellium.shard.Shard
import flank.instrument.log.Instrument
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import java.lang.System.currentTimeMillis
import java.text.SimpleDateFormat

/**
 * Use case for running android tests on corellium virtual devices.
 */
object RunTestCorelliumAndroid {

    /**
     * The context of android test execution on corellium.
     * Is providing all necessary data and operations for [Context.invoke].
     */
    interface Context {
        val api: CorelliumApi
        val apk: Apk.Api
        val args: Args
    }

    /**
     * The user arguments for the test execution.
     *
     * @param credentials The user credentials for authorizing connection with API.
     * @param apks List of app apks with related test apks for testing.
     * @param maxShardsCount Maximum amount of shards to create. For each shard Flank is invoking dedicated device instance, so do not use values grater than maximum number available instances in the Corellium account.
     * @param obfuscateDumpShards Obfuscate the test names in shards before dumping to file.
     * @param outputDir Set output dir. Default value is [DefaultOutputDir.new]
     */
    data class Args(
        val credentials: Authorization.Credentials,
        val apks: List<Apk.App>,
        val maxShardsCount: Int,
        val obfuscateDumpShards: Boolean = false,
        val outputDir: String = DefaultOutputDir.new,
    ) {
        /**
         * Default output directory scheme.
         *
         * @property new Directory name in format: `results/corellium/android/yyyy-MM-dd_HH-mm-ss-SSS`.
         */
        object DefaultOutputDir {
            private const val PATH = "results/corellium/android/"
            private val date = SimpleDateFormat("yyyy-MM-dd_HH-mm-ss-SSS")
            val new get() = PATH + date.format(currentTimeMillis())
        }

        /**
         * Abstraction for app and test apk files.
         *
         * @property path Absolut or relative path to apk file.
         */
        sealed class Apk {
            abstract val path: String

            /**
             * App apk data
             *
             * @property tests List of test apks to run on app apk.
             */
            data class App(
                override val path: String,
                val tests: List<Test>
            ) : Apk()

            /**
             * Test apk data
             */
            data class Test(
                override val path: String
            ) : Apk()
        }
    }

    /**
     * The State structure is holding all necessary data collected during the execution process.
     * For convenience the properties are sorted in order equal to its initialization.
     *
     * @param testCases key - path to the test apk, value - list of test method names.
     * @param shards each item is representing list of apps to run on another device instance.
     * @param ids the ids of corellium device instances.
     * @param packageNames key - path to the test apk, value - package name.
     * @param testRunners key - path to the test apk, value - fully qualified test runner name.
     */
    internal data class State(
        val testCases: Map<String, List<String>> = emptyMap(),
        val shards: List<List<Shard.App>> = emptyList(),
        val ids: List<String> = emptyList(),
        val packageNames: Map<String, String> = emptyMap(),
        val testRunners: Map<String, String> = emptyMap(),
        val testResult: List<List<Instrument>> = emptyList(),
    )

    /**
     * The reference to the step factory.
     * Invoke it to generate new execution step.
     */
    internal val step = CreateTransformation<State>()
}

operator fun Context.invoke(): Unit = runBlocking {
    State() execute flowOf(
        authorize(),
        parseTestCasesFromApks(),
        prepareShards(),
        createOutputDir(),
        dumpShards(),
        invokeDevices(),
        installApks(),
        parseApksInfo(),
        executeTests(),
        cleanUpInstances(),
        generateReport(),
        finish(),
    )
}