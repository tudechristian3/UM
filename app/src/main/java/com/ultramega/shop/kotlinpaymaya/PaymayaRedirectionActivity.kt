package com.ultramega.shop.kotlinpaymaya

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.webkit.WebView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.gson.Gson
import com.paymaya.sdk.android.checkout.PayMayaCheckout
import com.paymaya.sdk.android.checkout.PayMayaCheckoutResult
import com.paymaya.sdk.android.checkout.models.Buyer
import com.paymaya.sdk.android.checkout.models.CheckoutRequest
import com.paymaya.sdk.android.checkout.models.Item
import com.paymaya.sdk.android.checkout.models.ItemAmount
import com.paymaya.sdk.android.common.LogLevel
import com.paymaya.sdk.android.common.PayMayaEnvironment
import com.paymaya.sdk.android.common.exceptions.BadRequestException
import com.paymaya.sdk.android.common.models.AmountDetails
import com.paymaya.sdk.android.common.models.RedirectUrl
import com.paymaya.sdk.android.common.models.TotalAmount
import com.ultramega.shop.R
import com.ultramega.shop.activity.MainActivity
import com.ultramega.shop.constants.Constants
import com.ultramega.shop.pojo.ConsumerProfile
import com.ultramega.shop.pojo.OrdersQueue
import com.ultramega.shop.rest.RetrofitBuild

class PaymayaRedirectionActivity : AppCompatActivity() {



    private val PRIVATE_KEY_LIVE = "pk-KkGRG8GuZberT5s0R5hl3lO9r7JEjyjfBFViFSt7BYX"  //KkGRG8GuZberT5s0R5hl3lO9r7JEjyjfBFViFSt7BYX
    private val PRIVATE_KEY_SANDBOX = "pk-WRERJcUO3vqmURpHBWKML8QvQHGYF2je9IVHRnzwo1C" //pk-X3WDHdoRWAnsDfOlZUqnKNTchhql9AvBtL1VR4P3wkg

    private val payMayaCheckoutClient = PayMayaCheckout.newBuilder()
            .clientPublicKey(PRIVATE_KEY_SANDBOX)
            .environment(PayMayaEnvironment.SANDBOX)
            .logLevel(LogLevel.INFO)
            .build()

    private var totalAmount : String? = null
    private var deliveryCharge : String? = null
    private var REQUEST_REFERENCE_NUMBER : String ? = null
    private var resultId: String? = null
    private val items: MutableList<Item> = mutableListOf()
    private val itemsGrocery: MutableList<Item> = mutableListOf()
    private var orderItems: MutableList<OrdersQueue> = mutableListOf()
    private var paymentMethod: PaymentMethod? = null
    private var consumerProfile : ConsumerProfile? = null
    private var webView: WebView? = null
    private var toolbar: Toolbar? = null
    private var groceriesTotal:Double = 0.0


    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paymaya_redirection)

        Log.d("Paymaya", "ref#: " + intent.getStringExtra("referencenumber"))
        Log.d("Paymaya", "total: " + intent.getStringExtra("totalamount"))
        Log.d("Paymaya", "deliveryCharge: " + intent.getStringExtra("deliveryCharge"))

        //REQUEST_REFERENCE_NUMBER = this.intent.getStringExtra("referencenumber")
        REQUEST_REFERENCE_NUMBER = intent.getStringExtra("referencenumber")
        totalAmount = intent.getStringExtra("totalamount")
        deliveryCharge = intent.getStringExtra("deliveryCharge")
        consumerProfile = Gson().fromJson(intent.getStringExtra("userinfo"),ConsumerProfile::class.java)
        orderItems = Gson().fromJson(intent.getStringExtra("items"), Array<OrdersQueue>::class.java).toMutableList()

        webView = findViewById(R.id.webView)
        toolbar = findViewById(R.id.paymayaToolbar);
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDefaultDisplayHomeAsUpEnabled(true)

        Log.d("okhttp", "DELIVERY: $deliveryCharge")


        for(queue in orderItems){

            Log.d("okhttp","DATA: "+ Gson().toJson(queue))

            groceriesTotal = groceriesTotal.plus(queue.totalPackageAmount)

            items.add(Item(queue.itemDescription,queue.quantity,queue.itemCode,queue.packageDescription,
            ItemAmount(queue.totalPackageAmount.toBigDecimal(), AmountDetails()),
                    TotalAmount(
                            queue.totalPackageAmount.toBigDecimal(),
                            Constants.CURRENCY,
                            AmountDetails()
                    )))

        }


        //for grocery
        itemsGrocery.add(Item("Groceries",null,null,null,
                ItemAmount(groceriesTotal.toBigDecimal(), AmountDetails()),
                TotalAmount(
                        groceriesTotal.toBigDecimal(),
                        Constants.CURRENCY,
                        AmountDetails()
                )))

        payWithCheckoutButtonClicked()
    }

    private fun buildCheckoutRequest() =
            CheckoutRequest(
                    TotalAmount(
                            totalAmount!!.toBigDecimal(),
                            Constants.CURRENCY,
                            AmountDetails(null,null,
                                    deliveryCharge?.toBigDecimal(),
                                    "0".toBigDecimal(),
                                    groceriesTotal.toBigDecimal())
                    ),
                    Buyer(
                            firstName = consumerProfile?.firstName,
                            middleName = null,
                            lastName = consumerProfile?.lastName,
                            contact = null,
                            shippingAddress = null,
                            billingAddress = null,
                            ipAddress = null
                    ),
                    itemsGrocery,
                    getRequestReferenceNumber(),
                    REDIRECT_URL
            )
    private fun getRequestReferenceNumber() =
            (REQUEST_REFERENCE_NUMBER).toString()


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        payMayaCheckoutClient.onActivityResult(requestCode, resultCode, data)?.let {
           checkoutCompleted(it)
            return
        }
    }

     fun checkoutCompleted(result: PayMayaCheckoutResult) {
       resultId = result.checkoutId
        when (result) {
            is PayMayaCheckoutResult.Success -> {
                val message = "Success, checkoutId: ${result.checkoutId}"
                showResultSuccessMessage(message)
            }
            is PayMayaCheckoutResult.Cancel -> {
                val message = "Canceled, checkoutId: ${result.checkoutId}"
                showResultCancelMessage(message)
            }
            is PayMayaCheckoutResult.Failure -> {
                val message = "Failure, checkoutId: ${result.checkoutId}, exception: ${result.exception}"
                showResultFailureMessage(message, result.exception)
            }
        }

    }

    fun showResultSuccessMessage(message: String) {
        Toast.makeText(applicationContext, "Operation succeeded", Toast.LENGTH_SHORT).show()
        Log.i("okhttp", message)
        webView?.loadUrl(REDIRECT_URL.success)

    }

    fun showResultCancelMessage(message: String) {
        Toast.makeText(applicationContext, "Operation cancelled", Toast.LENGTH_SHORT).show()
        Log.i("okhttp", message)
        webView?.loadUrl(REDIRECT_URL.cancel)
    }

    fun showResultFailureMessage(message: String, exception: Exception) {
        Toast.makeText(applicationContext, "Operation failed.", Toast.LENGTH_SHORT).show()
        Log.e("okhttp", message)
        if (exception is BadRequestException) {
            Log.d("okhttp", exception.error.toString())
        }
        webView?.loadUrl(REDIRECT_URL.failure)
    }


    companion object {
        private var REQUEST_REFERENCE_NUMBER = 0
        private val REDIRECT_URL = if(RetrofitBuild.ROOT_URL.contains("https")){
            RedirectUrl(
                    success = Constants.REDIRECT_URL_SUCCESS_LIVE,
                    failure = Constants.REDIRECT_URL_FAILURE_LIVE,
                    cancel = Constants.REDIRECT_URL_CANCEL_LIVE
            )
        }else{
            RedirectUrl(
                    success = Constants.REDIRECT_URL_SUCCESS,
                    failure = Constants.REDIRECT_URL_FAILURE,
                    cancel = Constants.REDIRECT_URL_CANCEL
            )
        }
    }


     fun payWithCheckoutButtonClicked() {
        paymentMethod = PaymentMethod.CHECKOUT
        resultId = null
         val checkoutRequest = buildCheckoutRequest()

         Log.d("okhttp","CHECKOUT REQUEST : "+Gson().toJson(checkoutRequest))

         payWithCheckout(checkoutRequest)
    }
    private enum class PaymentMethod {
        CHECKOUT,
        PAY_WITH_PAYMAYA_SINGLE_PAYMENT
    }

     fun payWithCheckout(checkoutRequest: CheckoutRequest) {
        payMayaCheckoutClient.startCheckoutActivityForResult(this, checkoutRequest)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        return when (item?.itemId) {
            android.R.id.home -> {
                val intent = Intent(applicationContext, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("index", 3)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("index", 3)
        startActivity(intent)
        finish()
    }



}