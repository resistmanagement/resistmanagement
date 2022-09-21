/*
 * This software is in the public domain under CC0 1.0 Universal plus a
 * Grant of Patent License.
 *
 * To the extent possible under law, the author(s) have dedicated all
 * copyright and related and neighboring rights to this software to the
 * public domain worldwide. This software is distributed without any
 * warranty.
 *
 * You should have received a copy of the CC0 Public Domain Dedication
 * along with this software (see the LICENSE.md file). If not, see
 * <http://creativecommons.org/publicdomain/zero/1.0/>.
 */

import org.moqui.Moqui
import org.moqui.context.ExecutionContext
import org.moqui.screen.ScreenTest
import org.moqui.screen.ScreenTest.ScreenTestRender
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import java.util.Random

class RestApiTests extends Specification {
    protected final static Logger logger = LoggerFactory.getLogger(RestApiTests.class)

    static final String token = 'TestSessionToken'

    @Shared
    ExecutionContext ec = Moqui.getExecutionContext()
    @Shared
    ScreenTest screenTest = ec.screen.makeTest()
    // random generator
    @Shared
    Random rand = new Random()
    @Shared
    def num = rand.nextInt()

    def setupSpec() {
//        ec.user.loginUser("john.doe", "moqui")

//        ec.entity.tempSetSequencedIdPrimary("mantle.account.method.PaymentGatewayResponse", 55800, 10)
//        ec.entity.tempSetSequencedIdPrimary("mantle.ledger.transaction.AcctgTrans", 55800, 10)
//        ec.entity.tempSetSequencedIdPrimary("mantle.shipment.Shipment", 55800, 10)
//        ec.entity.tempSetSequencedIdPrimary("mantle.shipment.ShipmentItemSource", 55800, 10)
//        ec.entity.tempSetSequencedIdPrimary("mantle.product.asset.Asset", 55800, 10)
//        ec.entity.tempSetSequencedIdPrimary("mantle.product.asset.AssetDetail", 55800, 10)
//        ec.entity.tempSetSequencedIdPrimary("mantle.product.issuance.AssetReservation", 55800, 10)
//        ec.entity.tempSetSequencedIdPrimary("mantle.product.issuance.AssetIssuance", 55800, 10)
//        ec.entity.tempSetSequencedIdPrimary("mantle.account.invoice.Invoice", 55800, 10)
//        ec.entity.tempSetSequencedIdPrimary("mantle.account.payment.Payment", 55800, 10)
//        ec.entity.tempSetSequencedIdPrimary("mantle.account.payment.PaymentApplication", 55800, 10)
//        ec.entity.tempSetSequencedIdPrimary("mantle.order.OrderHeader", 55800, 10)
//        ec.entity.tempSetSequencedIdPrimary("mantle.order.OrderItemBilling", 55800, 10)
    }

    def cleanupSpec() {
        long totalTime = System.currentTimeMillis() - screenTest.startTime
        logger.info("Rendered ${screenTest.renderCount} screens (${screenTest.errorCount} errors) in ${ec.l10n.format(totalTime/1000, "0.000")}s, output ${ec.l10n.format(screenTest.renderTotalChars/1000, "#,##0")}k chars")

//        ec.entity.tempResetSequencedIdPrimary("mantle.account.method.PaymentGatewayResponse")
//        ec.entity.tempResetSequencedIdPrimary("mantle.ledger.transaction.AcctgTrans")
//        ec.entity.tempResetSequencedIdPrimary("mantle.shipment.Shipment")
//        ec.entity.tempResetSequencedIdPrimary("mantle.shipment.ShipmentItemSource")
//        ec.entity.tempResetSequencedIdPrimary("mantle.product.asset.Asset")
//        ec.entity.tempResetSequencedIdPrimary("mantle.product.asset.AssetDetail")
//        ec.entity.tempResetSequencedIdPrimary("mantle.product.issuance.AssetReservation")
//        ec.entity.tempResetSequencedIdPrimary("mantle.product.issuance.AssetIssuance")
//        ec.entity.tempResetSequencedIdPrimary("mantle.account.invoice.Invoice")
//        ec.entity.tempResetSequencedIdPrimary("mantle.account.payment.Payment")
//        ec.entity.tempResetSequencedIdPrimary("mantle.account.payment.PaymentApplication")
//        ec.entity.tempResetSequencedIdPrimary("mantle.order.OrderHeader")
//        ec.entity.tempResetSequencedIdPrimary("mantle.order.OrderItemBilling")
        ec.destroy()
    }

    def setup() {
//        ec.artifactExecution.disableAuthz()
    }

    def cleanup() {
//        ec.artifactExecution.enableAuthz()
    }

    @Unroll
    def "call Moqui Tools REST API (#requestMethod, #screenPath, #containsTextList)"() {
        expect:
        ScreenTestRender str = screenTest.render(screenPath, parameters, requestMethod)
        logger.info("Rendered ${screenPath} in ${str.getRenderTime()}ms, output:\n${str.output}")
        logger.info("postRenderContext ${str.postRenderContext.sri.response.getHeaders()}")
        boolean containsAll = true
        for (String containsText in containsTextList) {
            boolean contains = containsText ? str.assertContains(containsText) : true
            if (!contains) {
                logger.info("In ${screenPath} text [${containsText}] not found:\n${str.output}")
                containsAll = false
            }
        }

        // assertions
        !str.errorMessages
        containsAll

        where:
        requestMethod | screenPath | parameters | containsTextList

        // Actually create an account, logout, and log back in
        "get" | "Login" | [:] | ["Login","Reset Password","Change Password","Create Account"]
        "post" | "Login/createAccount" | [firstName:"asdf", lastName:"asdf", emailAddress:"asdf" + num + "@asdf.com",
                                          username:"asdf" + num + "@asdf.com", newPassword:"asdf" + num + "@asdf.com",
                                          newPasswordVerify:"asdf" + num + "@asdf.com"] | [""]
//        "get" | "qapps" | [:] | ["Site Map"]
    }
}
