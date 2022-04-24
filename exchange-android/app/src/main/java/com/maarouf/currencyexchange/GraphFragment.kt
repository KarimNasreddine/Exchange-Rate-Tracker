package com.maarouf.currencyexchange

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.maarouf.currencyexchange.api.ExchangeService
import com.maarouf.currencyexchange.api.model.GraphDataPoints
import com.maarouf.currencyexchange.utilities.CustomMarker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class GraphFragment : Fragment() {

//    var graphView: GraphView? = null
    var lineChart: LineChart? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchGraph()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_graph, container, false)
//        graphView = view.findViewById(R.id.idGraphView);
        lineChart = view.findViewById(R.id.lineChart)
        return view
    }

    private fun fetchGraph() {
        ExchangeService.exchangeApi().getGraphDataPoints().enqueue(object :
            Callback<GraphDataPoints> {
            override fun onResponse(
                call: Call<GraphDataPoints>, response:
                Response<GraphDataPoints>
            ) {
                val responseBody: GraphDataPoints? = response.body()

                buildGraph(responseBody)


                //for debugging purposes
                Log.d("GRAPH", "Buy")
                responseBody?.buyDataPoints?.forEach { entry ->
                    Log.d("GRAPH",
                        "Key = " + entry.key +
                                ", Value = " + entry.value
                    )
                }

                //for debugging purposes
                Log.d("GRAPH", "Sell")
                responseBody?.sellDataPoints?.forEach { entry ->
                    Log.d("GRAPH",
                        "Key = " + entry.key +
                                ", Value = " + entry.value
                    )
                }
            }

            override fun onFailure(call: Call<GraphDataPoints>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    private fun buildGraph(responseBody: GraphDataPoints?) {
        var xAxisValues: List<String> = responseBody?.buyDataPoints?.keys!!.toList()
        var buyRateValue: List<Entry> = getRates(responseBody).get(0)
        var sellRateValue: List<Entry> = getRates(responseBody).get(1)

        var dataSets: ArrayList<ILineDataSet?>? = ArrayList()

        var set1: LineDataSet
        set1 = LineDataSet(buyRateValue, "Buy")
        set1.color = Color.rgb(65, 168, 121)
        dataSets!!.add(set1)

        var set2: LineDataSet
        set2 = LineDataSet(sellRateValue, "Sell")
        set2.color = Color.rgb(200, 10, 10)
        dataSets.add(set2)

        lineChart!!.xAxis.labelRotationAngle = 0f
        lineChart!!.xAxis.valueFormatter = IndexAxisValueFormatter(xAxisValues)

        lineChart!!.axisRight.isEnabled = false

        lineChart!!.setTouchEnabled(true)
        lineChart!!.setPinchZoom(true)

        lineChart!!.animateX(1800, Easing.EaseInExpo)

        val data = LineData(dataSets)
        lineChart!!.data = data
        lineChart!!.description.isEnabled = false
        lineChart!!.invalidate()

        val markerView = CustomMarker(this.requireContext(), R.layout.marker_view)
        lineChart!!.marker = markerView
    }

    private fun getRates(responseBody: GraphDataPoints): List<List<Entry>> {
        val rates: ArrayList<ArrayList<Entry>> = ArrayList()
        val buyRates: ArrayList<Entry> = ArrayList()
        var iter : Int = 0
        responseBody.buyDataPoints?.values?.forEach { item ->
            buyRates.add(Entry(iter.toFloat(), item))
            iter ++
        }
        val sellRates: ArrayList<Entry> = ArrayList()
        iter = 0
        responseBody.sellDataPoints?.values?.forEach { item ->
            sellRates.add(Entry(iter.toFloat(), item))
            iter ++
        }

        rates.add(buyRates)
        rates.add(sellRates)
        return rates
    }

    /*private fun buildGraph(graphDataPoints: GraphDataPoints?) {
        val series: LineGraphSeries<DataPoint> = LineGraphSeries(
            arrayOf( // on below line we are adding
                // each point on our x and y axis.
                DataPoint(0.0, 1.0),
                DataPoint(1.0, 3.0),
                DataPoint(2.0, 4.0),
                DataPoint(3.0, 9.0),
                DataPoint(4.0, 6.0),
                DataPoint(5.0, 3.0),
                DataPoint(6.0, 6.0),
                DataPoint(7.0, 1.0),
                DataPoint(8.0, 2.0)
            )
        )
        graphView?.setTitle("My Graph View");

        // on below line we are setting
        // text color to our graph view.
        graphView?.setTitleColor(R.color.purple_200);

        // on below line we are setting
        // our title text size.
        graphView?.setTitleTextSize(18F);

        // on below line we are adding
        // data series to our graph view.
        graphView?.addSeries(series);
    }*/



    /*private fun buildGraph(graphDataPoints: GraphDataPoints?) {

        var xAxisIndex: Int = 1
        var entriesBuy: ArrayList<Entry> = ArrayList()
        graphDataPoints?.buyDataPoints?.forEach { entry ->
            entriesBuy.add(Entry(xAxisIndex.toFloat(),entry.value))
            xAxisIndex++
        }
        var set1 = LineDataSet(entriesBuy, "Buy")

        xAxisIndex = 1
        var entriesSell: ArrayList<Entry> = ArrayList()
        graphDataPoints?.sellDataPoints?.forEach { entry ->
            entriesSell.add(Entry(xAxisIndex.toFloat(),entry.value))
            xAxisIndex++
        }
        var set2 = LineDataSet(entriesSell, "Sell")

        var dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1)
        dataSets.add(set2)

        var data = LineData(dataSets)
        data.setValueTextColor(Color.BLUE)
        data.setValueTextSize(9f)

        lineChart?.data = data;
    }*/
}
