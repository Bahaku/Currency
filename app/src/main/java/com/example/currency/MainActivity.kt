package com.example.currency

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import com.example.currency.BuildConfig.Api_Key
import com.example.currency.data.CurrencyModel
import com.example.currency.data.CurrencySpinnerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val values = arrayListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupListeners()
        setupNetwork()
    }

    private fun setupNetwork() {
        fetchCurencies()
    }

    private fun setupListeners() {
        etOne.doAfterTextChanged {
            calculate(it.toString())
        }
    }

    private fun calculate(value: String) {
        if (value.isNotEmpty()) {
            val result = value.toDouble() * values[spTwo.selectedItemPosition].toDouble() / values[spOne.selectedItemPosition].toDouble()
            etTwo.setText(result.toString())
        }
    }

    private fun workWithData(data: CurrencyModel?) {
        val keys = data?.rates?.keySet()?.toList()


        if (keys != null) {
            for (item in keys) {
                values.add(data.rates.get(item).toString())
            }
        }

        val adapter = CurrencySpinnerAdapter(applicationContext, R.layout.item_spinner, keys!!)

        spOne.adapter = adapter
        spTwo.adapter = adapter

        spOne.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {}
        }

        spTwo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {}
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                calculate(etTwo.text.toString())
            }
        }
    }

    private fun fetchCurencies() {
        RetrofitBuilder.getService()?.getCurrencies(Api_Key)
            ?.enqueue(object : Callback<CurrencyModel> {
                override fun onResponse(
                    call: Call<CurrencyModel>,
                    response: Response<CurrencyModel>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val data = response.body()
                        workWithData(data)
                    }
                }
                override fun onFailure(call: Call<CurrencyModel>, t: Throwable) {
                    Toast.makeText(applicationContext, "Нет подключения", Toast.LENGTH_LONG).show()
                }
            })
    }
}
