package net.corda.node.webserver

import com.google.common.net.HostAndPort
import net.corda.node.driver.driver
import net.corda.node.services.User
import net.corda.node.services.config.ConfigHelper
import net.corda.node.services.config.FullNodeConfiguration
import java.nio.file.Paths

fun main(args: Array<String>) {
    driver {
        val node = startNode().get()
        WebServer(FullNodeConfiguration(node.config)).start()
    }
}