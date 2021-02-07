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

                var BLE_Count = snapshot.child("/bluetooth/BLE_count").getValue().toString()
                var Wifi_Count = snapshot.child("/wifi/count").getValue().toString()

                var ble_count = BLE_Count.toInt()
                var wifi_count = Wifi_Count.toInt()

                var dispositivos = (ble_count + wifi_count) / 2


                val anyChartView = findViewById<AnyChartView>(R.id.any_chart_view)
                anyChartView.setProgressBar(findViewById(R.id.progress_bar))

                val cartesian = AnyChart.column()

                val data: MutableList<DataEntry> = ArrayList()
                data.add(ValueDataEntry("30 mins", 6))
                data.add(ValueDataEntry("15 mins", 5))
                data.add(ValueDataEntry("5 mins", 4))
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