package ipvc.estg.horadeponta

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.chart.common.listener.Event
import com.anychart.chart.common.listener.ListenersInterface
import com.anychart.enums.Align
import com.anychart.enums.LegendLayout
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*


class Chart_1 : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chart_1)

        var database = FirebaseDatabase.getInstance().reference

        // GetData
        var getdata = object : ValueEventListener
        {
            override fun onCancelled(error: DatabaseError)
            {
                Toast.makeText(
                    baseContext,
                    "Error Connecting to Realtime Database",
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun onDataChange(snapshot: DataSnapshot)
            {

                var BLE_Count = snapshot.child("/BLE_count").getValue().toString()

                var ble_count = BLE_Count.toInt()


                val anyChartView = findViewById<AnyChartView>(R.id.any_chart_view)
                anyChartView.setProgressBar(findViewById(R.id.progress_bar))

                val pie = AnyChart.pie()

                pie.setOnClickListener(object : ListenersInterface.OnClickListener(arrayOf("x", "value"))
                {
                    override fun onClick(event: Event)
                    {
                        Toast.makeText(this@Chart_1, event.data["x"].toString() + ":" + event.data["value"], Toast.LENGTH_SHORT).show()
                    }
                })

                val data: MutableList<DataEntry> = ArrayList()
                data.add(ValueDataEntry("Blutooth", ble_count))
                data.add(ValueDataEntry("Wi-Fi", 5))

                pie.data(data)

                pie.title("Dispositivos")

                pie.labels().position("outside")

                pie.legend().title().enabled(true)
                pie.legend().title()
                    .text("Métodos")
                    .padding(0.0, 0.0, 10.0, 0.0)

                pie.legend()
                    .position("center-bottom")
                    .itemsLayout(LegendLayout.HORIZONTAL)
                    .align(Align.CENTER)

                anyChartView.setChart(pie)

            }
        }

        database.addValueEventListener(getdata)
        database.addListenerForSingleValueEvent(getdata)

    }

}