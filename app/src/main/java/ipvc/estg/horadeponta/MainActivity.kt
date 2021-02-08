package ipvc.estg.horadeponta

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.SingleValueDataSet
import com.anychart.enums.*
import com.anychart.scales.OrdinalColor
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class MainActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var database = FirebaseDatabase.getInstance().reference

        // GetData
        var getdata = object : ValueEventListener
        {
            override fun onCancelled(error: DatabaseError)
            {
                Toast.makeText(baseContext, "Error Connecting to Realtime Database", Toast.LENGTH_SHORT).show()
            }

            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot)
            {

                var BLE_Count = snapshot.child("/bluetooth/Counters/BLE_count").getValue().toString()
                var Wifi_Count = snapshot.child("/wifi/Counters/counter").getValue().toString()

                var ble_count = BLE_Count.toInt()
                var wifi_count = Wifi_Count.toInt()

                var dispositivos = (ble_count + wifi_count) / 2
                var Dispositivos = dispositivos.toString()

                value_ble.text = BLE_Count
                value_wifi.text = Wifi_Count
                value_disp.text = Dispositivos

                //////////////////////////////////////////////////////////////////////
                // Current Time and Date
                val current_time = LocalDateTime.now()
                val t1 = current_time.minus(5, ChronoUnit.MINUTES)
                //////////////////////////////////////////////////////////////////////
                // Format to Time (HH:mm:ss)
                val formatter = DateTimeFormatter.ofPattern("HH:mm:ss")
                val t = current_time.format(formatter)
                val t2 = t1.format(formatter)
                //////////////////////////////////////////////////////////////////////
                // Current Millis
                val time = System.currentTimeMillis()
                val t5 = time + 300000

                //value_wifi.text = t.toString()
                //value_disp.text = t2.toString()


                val anyChartView = findViewById<AnyChartView>(R.id.any_chart_view)
                anyChartView.setProgressBar(findViewById(R.id.progress_bar))

                val linearGauge = AnyChart.linear()

                linearGauge.data(SingleValueDataSet(arrayOf(ble_count)))

                linearGauge.layout(Layout.HORIZONTAL)

                linearGauge.label(0)
                        .position(Position.LEFT_CENTER)
                        .anchor(Anchor.LEFT_CENTER)
                        .offsetY("-50px")
                        .offsetX("50px")
                        .fontColor("black")
                        .fontSize(17)
                linearGauge.label(0).text("BLE Count")


                linearGauge.label(1)
                        .position(Position.LEFT_CENTER)
                        .anchor(Anchor.LEFT_CENTER)
                        .offsetY("40px")
                        .offsetX("30px")
                        .fontColor("#777777")
                        .fontSize(15)
                linearGauge.label(1).text("Ocupação Baixa")

                linearGauge.label(2)
                        .position(Position.RIGHT_CENTER)
                        .anchor(Anchor.RIGHT_CENTER)
                        .offsetY("40px")
                        .offsetX("30px")
                        .fontColor("#777777")
                        .fontSize(15)
                linearGauge.label(2).text("Ocupação Alta")


                val scaleBarColorScale = OrdinalColor.instantiate()
                scaleBarColorScale.ranges(arrayOf(
                        "{ from: 0, to: 10, color: ['green 0.5'] }",
                        "{ from: 10, to: 20, color: ['yellow 0.5'] }",
                        "{ from: 20, to: 30, color: ['red 0.5'] }"
                ))

                linearGauge.scaleBar(0)
                        .width("10%")
                        .colorScale(scaleBarColorScale)

                linearGauge.marker(0)
                        .type(MarkerType.TRIANGLE_DOWN)
                        .color("red")
                        .offset("-3.5%")
                        .zIndex(10)

                linearGauge.scale()
                        .minimum(0)
                        .maximum(30)
                //        linearGauge.scale().ticks

                //        linearGauge.scale().ticks
                linearGauge.axis(0)
                        .minorTicks(false)
                        .width("1%")
                linearGauge.axis(0)
                        .offset("-1.5%")
                        .orientation(Orientation.TOP)
                        .labels("top")

                linearGauge.padding(0, 30, 0, 30)

                anyChartView.setChart(linearGauge)

            }
        }

        database.addValueEventListener(getdata)
        database.addListenerForSingleValueEvent(getdata)

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean   // função da escolha do menu por atividade
    {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean     // função da definição das abas do Menu
    {
        // Handle item selection
        return when (item.itemId)
        {
            R.id.pie_chart -> {
                val intent = Intent(this, Chart_1::class.java) // go to Chart_1 activity

                //intent.putExtra("L", true)

                startActivity(intent)
                true
            }
            R.id.chart2 -> {
                val intent = Intent(this, Chart_2::class.java) // go to Chart_2 activity
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}