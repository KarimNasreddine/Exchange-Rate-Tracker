package com.maarouf.currencyexchange

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.maarouf.currencyexchange.api.ExchangeService
import com.maarouf.currencyexchange.api.model.ListingsData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListingsFragment : Fragment() {

    private var txtListings: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchListings();
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view: View =  inflater.inflate(R.layout.fragment_listings, container, false)
        txtListings = view?.findViewById(R.id.txtListings)
        return view
    }

    private fun fetchListings() {
        ExchangeService.exchangeApi().getListings().enqueue(object :
            Callback<List<ListingsData>> {
            override fun onResponse(call: Call<List<ListingsData>>, response:
            Response<List<ListingsData>>
            ) {
                val listings: List<ListingsData>? = response.body();
                displayListings(listings);
            }
            override fun onFailure(call: Call<List<ListingsData>>, t: Throwable) {
                return;
                TODO("Not yet implemented")
            }
        })
    }

    private fun displayListings(listings: List<ListingsData>?) {
        txtListings?.setText("")
        var counter = 1
        for (listing in listings!!) {
            if (!listing.resolved!!) {
                txtListings?.setText(
                    txtListings?.getText().toString() +
                            "-Listing: " + counter +
                            "\n\t\t\t\tAmount Selling: " + listing.sellingAmount + (if (listing.usdToLbp!!) " USD" else " LBP") +
                            "\n\t\t\t\tBuying Amount Ask: " + listing.buyingAmount + (if (listing.usdToLbp!!) " LBP" else " USD") +
                            "\n\t\t\t\tListing Type: " + (if (listing.usdToLbp!!) "USD To LBP" else "LBP To USD") +
                            "\n\t\t\t\tPhone Number: " + listing.userPhoneNumber +
                            "\n\n"
                )
                counter++
            }
        }
    }

}