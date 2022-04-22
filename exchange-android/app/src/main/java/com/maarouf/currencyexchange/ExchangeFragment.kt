package com.maarouf.currencyexchange

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.google.android.material.textfield.TextInputLayout
import com.maarouf.currencyexchange.api.ExchangeService
import com.maarouf.currencyexchange.api.model.ExchangeRates
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExchangeFragment : Fragment() {

    private var buyUsdTextView: TextView? = null
    private var sellUsdTextView: TextView? = null
    private var fromCurrency: TextView? = null
    private var toCurrency: TextView? = null
    var buyValue: TextView? = null
    var sellValue: TextView? = null

    private var switchButton: Button? = null
    private var calculateButton: Button? = null

    private var userInput: TextInputLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchRates()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_exchange, container, false)

        buyUsdTextView = view?.findViewById(R.id.txtBuyUsdRate)
        sellUsdTextView = view?.findViewById(R.id.txtSellUsdRate)

        switchButton = view.findViewById(R.id.bSwitch)
        fromCurrency = view.findViewById(R.id.fromCurrency)
        toCurrency = view.findViewById(R.id.toCurrency)

        switchButton?.setOnClickListener() {view ->
            Log.d("VALUES", "TRIGGERED")
            swapCurrencies()
        }

        buyValue = view.findViewById(R.id.txtBuyValue)
        sellValue = view.findViewById(R.id.txtSellValue)
        calculateButton = view.findViewById(R.id.bCalculate)
        userInput = view.findViewById(R.id.txtInputAmount)

        calculateButton?.setOnClickListener() {view ->
            doCalculation()
        }

        return view
    }

    private fun fetchRates(){
        ExchangeService.exchangeApi().getExchangeRates().enqueue(object :
            Callback<ExchangeRates> {
            override fun onResponse(call: Call<ExchangeRates>, response:
            Response<ExchangeRates>
            ) {
                val responseBody: ExchangeRates? = response.body();
                //Log.d("API", responseBody.toString());

                if(responseBody?.lbpToUsd==null){
                    buyUsdTextView?.text = "Not Available"
                }
                else{
                    buyUsdTextView?.text = responseBody?.lbpToUsd.toString()
                }

                if(responseBody?.usdToLbp==null) {
                    sellUsdTextView?.text = "Not Available"
                }
                else{
                    sellUsdTextView?.text = responseBody?.usdToLbp.toString()
                }
            }
            override fun onFailure(call: Call<ExchangeRates>, t: Throwable) {
                return;
                TODO("Not yet implemented")
            }
        })
    }

    private fun swapCurrencies(){
        var from: String? = fromCurrency?.getText().toString()
        var to: String? = toCurrency?.getText().toString()

        Log.d("VALUES", from!!)
        Log.d("VALUES", to!!)

        var temp: String? = from
        fromCurrency?.setText(to)
        toCurrency?.setText(temp)
    }

    private fun doCalculation() {
        if(sellUsdTextView?.text?.equals("Not Available") == false || sellUsdTextView?.text?.equals("Not Available") == false){
            var sellUsdRate: Float = sellUsdTextView?.text.toString().toFloat()
            var buyUsdRate: Float = buyUsdTextView?.text.toString().toFloat()
            if(fromCurrency?.text?.equals("USD") == true){
                buyValue?.setText((userInput?.editText?.text.toString().toFloat() * buyUsdRate).toString() + " LBP")
                sellValue?.setText((userInput?.editText?.text.toString().toFloat() * sellUsdRate).toString() + " LBP")
            }
            else{
                buyValue?.setText((userInput?.editText?.text.toString().toFloat() / buyUsdRate).toString() + " USD")
                sellValue?.setText((userInput?.editText?.text.toString().toFloat() / sellUsdRate).toString() + " USD")
            }
        }
    }
}
