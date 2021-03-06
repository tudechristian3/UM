/*
 * Copyright (c) 2020  PayMaya Philippines, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.paymaya.sdk.android.checkout

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.anyOrNull
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import com.paymaya.sdk.android.checkout.internal.CheckoutUseCase
import com.paymaya.sdk.android.checkout.models.Buyer
import com.paymaya.sdk.android.checkout.models.CheckoutRequest
import com.paymaya.sdk.android.checkout.models.Item
import com.paymaya.sdk.android.common.LogLevel
import com.paymaya.sdk.android.common.PaymentStatus
import com.paymaya.sdk.android.common.exceptions.InternalException
import com.paymaya.sdk.android.common.exceptions.PaymentFailedException
import com.paymaya.sdk.android.common.internal.*
import com.paymaya.sdk.android.common.internal.screen.PayMayaPaymentContract
import com.paymaya.sdk.android.common.internal.screen.PayMayaPaymentPresenter
import com.paymaya.sdk.android.common.models.RedirectUrl
import com.paymaya.sdk.android.common.models.TotalAmount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import java.math.BigDecimal
import java.net.UnknownHostException

class PayMayaCheckoutPresenterTest {
//
//    private lateinit var presenter: PayMayaPaymentContract.Presenter<CheckoutRequest>
//    private lateinit var json: Json
//
//    @Mock
//    private lateinit var checkoutUseCase: CheckoutUseCase
//
//    @Mock
//    private lateinit var checkStatusUseCase: CheckStatusUseCase
//
//    @Mock
//    private lateinit var view: PayMayaPaymentContract.View
//
//    @ExperimentalCoroutinesApi
//    @Before
//    fun setup() {
//        Dispatchers.setMain(TestCoroutineDispatcher())
//        MockitoAnnotations.initMocks(this)
//
//        json = Json(JsonConfiguration.Stable)
//        presenter = PayMayaPaymentPresenter(checkoutUseCase, checkStatusUseCase, Logger(LogLevel.DEBUG))
//    }
//
//    @Test
//    fun success() {
//        runBlocking {
//            whenever(checkoutUseCase.run(any())).thenReturn(
//                RedirectSuccessResponseWrapper(
//                    resultId = CHECKOUT_ID,
//                    redirectUrl = REDIRECT_CHECKOUT_URL
//                )
//            )
//            presenter.viewCreated(view, prepareCheckoutRequest())
//
//            verify(view).loadUrl(any())
//        }
//    }
//
//    @Test
//    fun failure() {
//        runBlocking {
//            whenever(checkoutUseCase.run(any())).thenReturn(
//                ErrorResponseWrapper(InternalException("Internal exception"))
//            )
//            presenter.viewCreated(view, prepareCheckoutRequest())
//
//            verify(view).finishFailure(resultId = anyOrNull(), exception = any())
//        }
//    }
//
//    @Test
//    fun cancel() {
//        runBlocking {
//            whenever(checkoutUseCase.run(any())).thenReturn(
//                RedirectSuccessResponseWrapper(
//                    resultId = CHECKOUT_ID,
//                    redirectUrl = REDIRECT_CHECKOUT_URL
//                )
//            )
//            whenever(checkStatusUseCase.run(any())).thenReturn(
//                StatusSuccessResponseWrapper(
//                    id = CHECKOUT_ID,
//                    status = PaymentStatus.PAYMENT_EXPIRED
//                )
//            )
//            presenter.viewCreated(view, prepareCheckoutRequest())
//            presenter.backButtonPressed()
//
//            verify(view).finishCanceled(CHECKOUT_ID)
//        }
//    }
//
//    @Test
//    fun cancelUninitialized() {
//        runBlocking {
//            whenever(checkoutUseCase.run(any())).thenReturn(
//                ErrorResponseWrapper(UnknownHostException())
//            )
//            presenter.viewCreated(view, prepareCheckoutRequest())
//            presenter.backButtonPressed()
//
//            verify(view).finishCanceled(resultId = null)
//        }
//    }
//
//    @Test
//    fun urlRedirectionSuccess() {
//        runBlocking {
//            whenever(checkoutUseCase.run(any())).thenReturn(
//                RedirectSuccessResponseWrapper(
//                    resultId = CHECKOUT_ID,
//                    redirectUrl = REDIRECT_CHECKOUT_URL
//                )
//            )
//            presenter.viewCreated(view, prepareCheckoutRequest())
//            presenter.urlBeingLoaded("$REDIRECT_URL_SUCCESS")
//
//            verify(view).finishSuccess(CHECKOUT_ID)
//        }
//    }
//
//    @Test
//    fun urlRedirectionCancel() {
//        runBlocking {
//            whenever(checkoutUseCase.run(any())).thenReturn(
//                RedirectSuccessResponseWrapper(
//                    resultId = CHECKOUT_ID,
//                    redirectUrl = REDIRECT_CHECKOUT_URL
//                )
//            )
//            presenter.viewCreated(view, prepareCheckoutRequest())
//            presenter.urlBeingLoaded("$REDIRECT_URL_CANCEL")
//
//            verify(view).finishCanceled(CHECKOUT_ID)
//        }
//    }
//
//    @Test
//    fun urlRedirectionFailure() {
//        runBlocking {
//            whenever(checkoutUseCase.run(any())).thenReturn(
//                RedirectSuccessResponseWrapper(
//                    resultId = CHECKOUT_ID,
//                    redirectUrl = REDIRECT_CHECKOUT_URL
//                )
//            )
//            presenter.viewCreated(view, prepareCheckoutRequest())
//            presenter.urlBeingLoaded("$REDIRECT_URL_FAILURE")
//
//            verify(view).finishFailure(CHECKOUT_ID, PaymentFailedException)
//        }
//    }
//
////    private fun prepareCheckoutRequest() =
////        CheckoutRequest(
////            totalAmount = TotalAmount(
////                value = BigDecimal(99999),
////                currency = "PHP"
////            ),
////            buyer = Buyer(
////                firstName = "John",
////                lastName = "Doe"
////            ),
////            items = listOf(
////                Item(
////                    name = "shoes",
////                    quantity = 1,
////                    totalAmount = TotalAmount(
////                        BigDecimal(10),
////                        "PHP"
////                    )
////                )
////            ),
////            requestReferenceNumber = "REQ_1",
////            redirectUrl = RedirectUrl(
////                success = REDIRECT_URL_SUCCESS,
////                failure = REDIRECT_URL_FAILURE,
////                cancel = REDIRECT_URL_CANCEL
////            )
////        )
//
//    companion object {
//        private const val CHECKOUT_ID = "SAMPLE CHECKOUT ID"
//        private const val REDIRECT_CHECKOUT_URL = "http://paymaya.com"
//        private const val REDIRECT_URL_SUCCESS = "http://staging-admin.ultramega.godvapps.com/paymentcallback/5042d146667518a1a5017644946b8650aafca44c/success.asp"
//        private const val REDIRECT_URL_FAILURE = "http://staging-admin.ultramega.godvapps.com/paymentcallback/5042d146667518a1a5017644946b8650aafca44c/failed.asp"
//        private const val REDIRECT_URL_CANCEL = "http://staging-admin.ultramega.godvapps.com/paymentcallback/5042d146667518a1a5017644946b8650aafca44c/cancelled.asp"
//    }
}