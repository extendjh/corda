package net.corda.attachmentdemo

import com.google.common.util.concurrent.Futures
import net.corda.core.getOrThrow
import net.corda.core.node.services.ServiceInfo
import net.corda.node.driver.driver
import net.corda.node.services.User
import net.corda.node.services.transactions.SimpleNotaryService
import net.corda.node.utilities.getHostAndPort
import org.junit.Test
import java.util.concurrent.CompletableFuture

class AttachmentDemoTest {
    @Test fun `runs attachment demo`() {
        driver(dsl = {
            startNode("Notary", setOf(ServiceInfo(SimpleNotaryService.Companion.type)))
            val nodeA = startNode("Bank A").getOrThrow()
            val nodeB = startNode("Bank B").getOrThrow()
            val nodeAApiAddr = startWebserver(nodeA).getOrThrow()
            val nodeBApiAddr = startWebserver(nodeB).getOrThrow()

            val senderThread = CompletableFuture.runAsync {
                nodeA.rpcClientToNode().use(demoUser[0].username, demoUser[0].password) {
                    sender(this)
                }
            }
            val recipientThread = CompletableFuture.runAsync {
                nodeB.rpcClientToNode().use(demoUser[0].username, demoUser[0].password) {
                    recipient(this)
                }
            }

            // Just check they don't throw any exceptions.d
            recipientThread.get()
            senderThread.get()
        }, isDebug = true)
    }
}
