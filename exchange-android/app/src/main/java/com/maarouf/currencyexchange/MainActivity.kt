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
import com.maarouf.currencyexchange.api.ExchangeService
import com.maarouf.currencyexchange.api.model.ListingsData
import com.maarouf.currencyexchange.api.model.Transaction
import com.maarouf.currencyexchange.utilities.ViewAnimation

class MainActivity : AppCompatActivity() {

    private var fab: FloatingActionButton? = null
    private var fabList: FloatingActionButton? = null
    private var fabTransaction: FloatingActionButton? = null
    private var transactionDialog: View? = null
    private var listingDialog: View? = null
    private var menu: Menu? = null
    private var tabLayout: TabLayout? = null
    private var tabsViewPager: ViewPager2? = null
    private var isRotate: Boolean? = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Authentication.initialize(this)
        setContentView(R.layout.activity_main)

        fab = findViewById(R.id.fab)
        fabList = findViewById(R.id.fabList)
        fabTransaction = findViewById(R.id.fabTransaction)

        ViewAnimation.init(fabList!!)
        ViewAnimation.init(fabTransaction!!)

        fab?.setOnClickListener { view ->
            isRotate = ViewAnimation.rotateFab(view, !isRotate!!)
            if(isRotate as Boolean){
                ViewAnimation.showIn(fabTransaction!!);
                ViewAnimation.showIn(fabList!!);
            }else{
                ViewAnimation.showOut(fabTransaction!!);
                ViewAnimation.showOut(fabList!!);
            }
        }

        fabTransaction?.setOnClickListener{
            showTransactionDialog()
        }
        
        fabList?.setOnClickListener{
            showListingDialog()
        }

        tabLayout = findViewById(R.id.tabLayout)
        tabsViewPager = findViewById(R.id.tabsViewPager)
        tabLayout?.tabMode = TabLayout.MODE_SCROLLABLE
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
                4 -> {
                        tab.text = "Listings"
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

    private fun showTransactionDialog() {
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

                val trans = Transaction()
                trans.lbpAmount = lbpAmount
                trans.usdAmount = usdAmount

                when(usdToLBP) {
                    R.id.rdBtnBuyUsd -> {
                        trans.usdToLbp = false
                        addTransaction(trans)
                        Log.d("TransactionCreated",trans.lbpAmount.toString()+"/"+trans.usdAmount.toString()+"/"+trans.usdToLbp.toString())
                    }
                    R.id.rdBtnSellUsd -> {
                        trans.usdToLbp = true
                        addTransaction(trans)
                        Log.d("TransactionCreated",trans.lbpAmount.toString()+"/"+trans.usdAmount.toString()+"/"+trans.usdToLbp.toString())
                    }
                    else -> {
                        Log.d("Error", "Transaction Type not chosen.")
                        showTransactionDialog()
                        Snackbar.make(fabTransaction as View, "Transaction Type Not Chosen!",
                        Snackbar.LENGTH_LONG).show()
                    }
                }

                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showListingDialog() {
        listingDialog = LayoutInflater.from(this)
            .inflate(R.layout.dialog_listing, null, false)
        MaterialAlertDialogBuilder(this).setView(listingDialog)
            .setTitle("Add Listing")
            .setMessage("Enter listing details")
            .setPositiveButton("Add") { dialog, _ ->
                val sellAmount = listingDialog
                    ?.findViewById<TextInputLayout>(R.id.txtInptSellAmount)
                    ?.editText?.text
                    .toString()
                    .toInt()
                val buyAmount = listingDialog
                    ?.findViewById<TextInputLayout>(R.id.txtInptBuyAskAmount)
                    ?.editText?.text
                    .toString()
                    .toInt()
                val phoneNumber = listingDialog
                    ?.findViewById<TextInputLayout>(R.id.txtInptPhoneNumber)
                    ?.editText?.text
                    .toString()
                val usdToLBP = listingDialog
                    ?.findViewById<RadioGroup>(R.id.rdGrpListingType)
                    ?.getCheckedRadioButtonId()

                val listing = ListingsData()
                listing.sellingAmount = sellAmount
                listing.buyingAmount = buyAmount
                listing.userPhoneNumber = phoneNumber

                when(usdToLBP) {
                    R.id.rdBtnLbpToUsd -> {
                        listing.usdToLbp = false
                        addListing(listing)
                        Log.d("ListingCreated",listing.toString())
                    }
                    R.id.rdBtnUsdToLbp -> {
                        listing.usdToLbp = true
                        addListing(listing)
                        Log.d("ListingCreated",listing.toString())
                    }
                    else -> {
                        Log.d("Error", "Listing Type not chosen.")
                        showTransactionDialog()
                        Snackbar.make(fabList as View, "Listing Type Not Chosen!",
                            Snackbar.LENGTH_LONG).show()
                    }
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun addListing(listing: ListingsData) {
        ExchangeService.exchangeApi().addListing(
            listing,
            if (Authentication.getToken() != null) "Bearer ${Authentication.getToken()}" else null
        ).enqueue(object :
            Callback<Any> {
            override fun onResponse(call: Call<Any>, response:
            Response<Any>) {
                Snackbar.make(fab as View, "Listing added!",
                    Snackbar.LENGTH_LONG)
                    .show()
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                Snackbar.make(fab as View, "Could not add listing.",
                    Snackbar.LENGTH_LONG)
                    .show()
            }
        })
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