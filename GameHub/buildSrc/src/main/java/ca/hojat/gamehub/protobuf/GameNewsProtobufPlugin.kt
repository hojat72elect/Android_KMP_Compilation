package ca.hojat.gamehub.protobuf

import org.gradle.api.Plugin
import org.gradle.api.Project
import PLUGIN_PROTOBUF
import com.google.protobuf.gradle.generateProtoTasks
import com.google.protobuf.gradle.id
import com.google.protobuf.gradle.protobuf
import com.google.protobuf.gradle.protoc
import org.gradle.kotlin.dsl.invoke

class GameNewsProtobufPlugin : Plugin<Project> {

    override fun apply(project: Project) = with(project) {
        setupPlugin()
        configurePlugin()
        addProtobufDependency()
    }

    private fun Project.setupPlugin() {
        plugins.apply(PLUGIN_PROTOBUF)
    }

    private fun Project.configurePlugin() {
        protobuf {
            protoc {
                artifact = Tooling.protobufCompiler
            }

            generateProtoTasks {
                all().forEach { task ->
                    task.builtins {
                        id("java") {
                            option("lite")
                        }
                    }
                }
            }
        }
    }

    private fun Project.addProtobufDependency() {
        dependencies.add("implementation", Tooling.protobuf)
    }
}
