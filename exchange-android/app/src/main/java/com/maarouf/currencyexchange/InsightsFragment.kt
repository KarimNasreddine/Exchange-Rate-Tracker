package com.maarouf.currencyexchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.maarouf.currencyexchange.api.ExchangeService
import com.maarouf.currencyexchange.api.model.InsightsData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InsightsFragment : Fragment() {

    private var txtBuyOpenClose: TextView? = null
    private var txtSellOpenClose: TextView? = null
    private var txtTransactionsNumber: TextView? = null
    private var txtTransactionsVolume: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchInsights()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View = inflater.inflate(R.layout.fragment_insights, container, false)
        txtBuyOpenClose = view?.findViewById(R.id.txtBuyOpenClose)
        txtSellOpenClose = view?.findViewById(R.id.txtSellOpenClose)
        txtTransactionsNumber = view?.findViewById(R.id.txtTransactionsNumber)
        txtTransactionsVolume = view?.findViewById(R.id.txtTransactionsVolume)
        return view
    }

    private fun fetchInsights() {
        ExchangeService.exchangeApi().getInsights().enqueue(object :
            Callback<InsightsData> {
            override fun onResponse(call: Call<InsightsData>, response:
            Response<InsightsData>
            ) {
                val insightsData: InsightsData? = response.body();
                constructInsightsView(insightsData)
            }
            override fun onFailure(call: Call<InsightsData>, t: Throwable) {
                return;
                TODO("Not yet implemented")
            }
        })
    }

    private fun constructInsightsView(insightsData: InsightsData?) {
        txtBuyOpenClose?.setText("")
        txtSellOpenClose?.setText("")
        txtTransactionsNumber?.setText("")
        txtTransactionsVolume?.setText("")

        insightsData?.usdToLbpOpen?.forEach { (key, value) ->
            txtSellOpenClose?.append(key + " : " + value + "-" + insightsData.usdToLbpClose?.get(key) + "\n")
        }

        insightsData?.lbpToUsdOpen?.forEach { (key, value) ->
            txtBuyOpenClose?.append(key + " : " + value + "-" + insightsData.lbpToUsdClose?.get(key) + "\n")
        }

        insightsData?.volumeInTransactions?.forEach { (key, value) ->
            txtTransactionsNumber?.append("$key : $value\n")
        }

        insightsData?.volumeInUsd?.forEach { (key, value) ->
            txtTransactionsVolume?.append("$key : $value\n")
        }
    }
}