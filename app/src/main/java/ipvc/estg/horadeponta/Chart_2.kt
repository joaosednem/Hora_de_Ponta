package ipvc.estg.horadeponta

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.enums.Anchor
import com.anychart.enums.HoverMode
import com.anychart.enums.Position
import com.anychart.enums.TooltipPositionMode
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class Chart_2 : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.chart_2)


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

                var BLE_Count = snapshot.child("/bluetooth/Counters/BLE_count").getValue().toString()
                var BLE_Count_5 = snapshot.child("/bluetooth/Counters/BLE_Count_5").getValue().toString()
                var BLE_Count_15 = snapshot.child("/bluetooth/Counters/BLE_Count_15").getValue().toString()
                var BLE_Count_30 = snapshot.child("/bluetooth/Counters/BLE_Count_30").getValue().toString()
                var Wifi_Count = snapshot.child("/wifi/Counters/counter").getValue().toString()
                var Wifi_Count_5 = snapshot.child("/wifi/Counters/counter5").getValue().toString()
                var Wifi_Count_15 = snapshot.child("/wifi/Counters/counter15").getValue().toString()
                var Wifi_Count_30 = snapshot.child("/wifi/Counters/counter30").getValue().toString()
                var Num_Scans = snapshot.child("/bluetooth/Counters/Num_Scans").getValue().toString()

                var ble_count = BLE_Count.toInt()
                var ble_count_5 = BLE_Count_5.toInt()
                var ble_count_15 = BLE_Count_15.toInt()
                var ble_count_30 = BLE_Count_30.toInt()
                var wifi_count = Wifi_Count.toInt()
                var wifi_count_5 = Wifi_Count_5.toInt()
                var wifi_count_15 = Wifi_Count_15.toInt()
                var wifi_count_30 = Wifi_Count_30.toInt()
                var num_scans = Num_Scans.toInt()

                var disp_5 = (ble_count_5 + wifi_count_5) / 2
                var disp_15 = (ble_count_15 + wifi_count_15) / 2
                var disp_30 = (ble_count_30 + wifi_count_30) / 2

                var dispositivos_5 = 0
                var dispositivos_15 = 0
                var dispositivos_30 = 0

                if (num_scans < 60)
                {
                    dispositivos_5 = disp_5/num_scans
                }
                else
                {
                    dispositivos_5 = disp_5/60
                }

                if (num_scans < 180)
                {
                    dispositivos_15 = disp_15/num_scans
                }
                else
                {
                    dispositivos_15 = disp_15/180
                }

                if (num_scans < 360)
                {
                    dispositivos_30 = disp_30/num_scans
                }
                else
                {
                    dispositivos_30 = disp_30/360
                }

                var dispositivos = (ble_count + wifi_count) / 2


                val anyChartView = findViewById<AnyChartView>(R.id.any_chart_view)
                anyChartView.setProgressBar(findViewById(R.id.progress_bar))

                val cartesian = AnyChart.column()

                val data: MutableList<DataEntry> = ArrayList()
                data.add(ValueDataEntry("30 mins", dispositivos_30))
                data.add(ValueDataEntry("15 mins", dispositivos_15))
                data.add(ValueDataEntry("5 mins", dispositivos_5))
                data.add(ValueDataEntry("1 min", dispositivos))

                val column = cartesian.column(data)

                column.tooltip()
                        .titleFormat("{%X}")
                        .position(Position.CENTER_BOTTOM)
                        .anchor(Anchor.CENTER_BOTTOM)
                        .offsetX(0.0)
                        .offsetY(5.0)
                        .format("{%Value}{groupsSeparator: }")

                cartesian.animation(true)
                cartesian.title("Ocupação / Tempo")

                cartesian.yScale().minimum(0.0)

                cartesian.yAxis(0).labels().format("{%Value}{groupsSeparator: }")

                cartesian.tooltip().positionMode(TooltipPositionMode.POINT)
                cartesian.interactivity().hoverMode(HoverMode.BY_X)

                cartesian.xAxis(0).title("Tempo")
                cartesian.yAxis(0).title("Ocupação")

                anyChartView.setChart(cartesian)

            }
        }

        database.addValueEventListener(getdata)
        database.addListenerForSingleValueEvent(getdata)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean   // função da escolha do menu por atividade
    {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu3, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean     // função da definição das abas do Menu
    {
        // Handle item selection
        return when (item.itemId)
        {
            R.id.bar_chart -> {
                val intent = Intent(this, MainActivity::class.java) // go to MainActivity
                startActivity(intent)
                true
            }
            R.id.pie_chart ->
            {
                val intent = Intent(this, Chart_1::class.java) // go to Chart_1 activity
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}