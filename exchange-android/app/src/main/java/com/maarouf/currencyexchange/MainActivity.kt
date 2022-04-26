package com.maarouf.currencyexchange

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.RadioGroup
import android.widget.TextView
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputLayout
import com.maarouf.currencyexchange.api.Authentication
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.maarouf.currencyexchange.api.model.ExchangeRates
import com.maarouf.currencyexchange.api.ExchangeService
import com.maarouf.currencyexchange.api.model.Transaction

class MainActivity : AppCompatActivity() {

    private var fab: FloatingActionButton? = null
    private var transactionDialog: View? = null
    private var menu: Menu? = null
    private var tabLayout: TabLayout? = null
    private var tabsViewPager: ViewPager2? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Authentication.initialize(this)
        setContentView(R.layout.activity_main)

        fab = findViewById(R.id.fab)
        fab?.setOnClickListener { view ->
            showDialog()
        }

        tabLayout = findViewById(R.id.tabLayout)
        tabsViewPager = findViewById(R.id.tabsViewPager)
        tabLayout?.tabMode = TabLayout.MODE_FIXED
        tabLayout?.isInlineLabel = true
        // Enable Swipe
        tabsViewPager?.isUserInputEnabled = true
        // Set the ViewPager Adapter
        val adapter = TabsPagerAdapter(supportFragmentManager, lifecycle)
        tabsViewPager?.adapter = adapter
        TabLayoutMediator(tabLayout!!, tabsViewPager!!) { tab, position ->
            when (position) {
                0 -> {
                    tab.text = "Exchange"
                }
                1 -> {
                    tab.text = "Transactions"
                }
                2 -> {
                    tab.text = "Graph"
                }
                3 -> {
                        tab.text = "Insights"
                }
            }
        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu
        setMenu()
        return true
    }

    private fun setMenu() {
        menu?.clear()
        menuInflater.inflate(if(Authentication.getToken() == null)
            R.menu.menu_logged_out else R.menu.menu_logged_in, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.login) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        } else if (item.itemId == R.id.register) {
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        } else if (item.itemId == R.id.logout) {
            Authentication.clearToken()
            setMenu()
        }
        return true
    }

    private fun showDialog() {
        transactionDialog = LayoutInflater.from(this)
            .inflate(R.layout.dialog_transaction, null, false)
        MaterialAlertDialogBuilder(this).setView(transactionDialog)
            .setTitle("Add Transaction")
            .setMessage("Enter transaction details")
            .setPositiveButton("Add") { dialog, _ ->
                val usdAmount =transactionDialog
                    ?.findViewById<TextInputLayout>(R.id.txtInptUsdAmount)
                    ?.editText?.text
                    .toString()
                    .toFloat()
                val lbpAmount =transactionDialog
                    ?.findViewById<TextInputLayout>(R.id.txtInptLbpAmount)
                    ?.editText?.text
                    .toString()
                    .toFloat()
                val usdToLBP = transactionDialog
                    ?.findViewById<RadioGroup>(R.id.rdGrpTransactionType)
                    ?.getCheckedRadioButtonId()

                Log.d("Transaction Type CHOICE", usdToLBP.toString())
                //2131231152 --> Buy USD
                //2131231153 --> Sell USD
                var usdToLbpBool : Boolean? =null
                if(usdToLBP == 2131231152){
                    usdToLbpBool = false
                }
                else if(usdToLBP == 2131231153){
                    usdToLbpBool = true;
                }

                val trans = Transaction()
                trans.lbpAmount = lbpAmount
                trans.usdAmount = usdAmount
                trans.usdToLbp = usdToLbpBool

                Log.d("TransactionCreated",trans.lbpAmount.toString()+"/"+trans.usdAmount.toString()+"/"+trans.usdToLbp.toString())
                addTransaction(trans)

                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun addTransaction(transaction: Transaction) {

        ExchangeService.exchangeApi().addTransaction(
            transaction,
            if (Authentication.getToken() != null) "Bearer ${Authentication.getToken()}" else null
            ).enqueue(object :
            Callback<Any> {
            override fun onResponse(call: Call<Any>, response:
            Response<Any>) {
                Snackbar.make(fab as View, "Transaction added!",
                    Snackbar.LENGTH_LONG)
                    .show()
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Snackbar.make(fab as View, "Could not add transaction.",
                    Snackbar.LENGTH_LONG)
                    .show()
            }
        })
    }
}