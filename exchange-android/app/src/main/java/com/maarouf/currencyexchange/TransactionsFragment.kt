package com.maarouf.currencyexchange

import android.os.Bundle
import android.os.Debug
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.TextView
import com.maarouf.currencyexchange.api.Authentication
import com.maarouf.currencyexchange.api.ExchangeService
import com.maarouf.currencyexchange.api.model.Transaction
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class TransactionsFragment : Fragment() {

    private var listview: ListView? = null
    private var transactions: ArrayList<Transaction>? = ArrayList()
    private var adapter: TransactionAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchTransactions()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_transactions, container, false)
        listview = view.findViewById(R.id.listview)
        adapter = TransactionAdapter(layoutInflater, transactions!!)
        listview?.adapter = adapter
        return view
    }

    private fun fetchTransactions() {
        if (Authentication.getToken() != null) {
            ExchangeService.exchangeApi()
                .getTransactions("Bearer ${Authentication.getToken()}")
                .enqueue(object : Callback<List<Transaction>> {
                    override fun onFailure(call: Call<List<Transaction>>, t: Throwable) {
                        Log.d("FAILURE",t.stackTraceToString())
                        return
                    }
                    override fun onResponse(
                        call: Call<List<Transaction>>,
                        response: Response<List<Transaction>>
                    ) {
                        transactions?.addAll(response.body()!!)
                        adapter?.notifyDataSetChanged()
                    }
                })
        }
    }


    class TransactionAdapter(
        private val inflater: LayoutInflater,
        private val dataSource: List<Transaction>
    ) : BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent:
        ViewGroup?): View {
            val view: View = inflater.inflate(R.layout.item_transaction,
                parent, false)
            view.findViewById<TextView>(R.id.txtView).text =
                "Transaction ID: " + dataSource[position].id.toString()

            view.findViewById<TextView>(R.id.usdAmount).text =
                "USD Amount: " +dataSource[position].usdAmount.toString()

            view.findViewById<TextView>(R.id.lbpAmount).text =
                "LBP Amount: " + dataSource[position].lbpAmount.toString()

            view.findViewById<TextView>(R.id.usdToLbp).text =
                if (dataSource[position].usdToLbp == true) "USD To LBP" else "LBP To USD"

            view.findViewById<TextView>(R.id.addedDate).text =
                "Added Date: " + dataSource[position].addedDate.toString()

            return view
        }
        override fun getItem(position: Int): Any {
            return dataSource[position]
        }
        override fun getItemId(position: Int): Long {
            return dataSource[position].id?.toLong() ?: 0
        }
        override fun getCount(): Int {
            return dataSource.size
        }

    }

}