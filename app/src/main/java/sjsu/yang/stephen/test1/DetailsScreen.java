package sjsu.yang.stephen.test1;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toolbar;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DetailsScreen extends FragmentActivity {

    TextView avgDetail;
    TextView max;
    TextView min;
    DBOperations DBop;

    private CombinedChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_screen);

        Configuration c = getResources().getConfiguration();
        if(c.orientation == c.ORIENTATION_PORTRAIT){
            super.onBackPressed();
            finish();
        }

        avgDetail = (TextView) findViewById(R.id.averageDetail);
        max = (TextView) findViewById(R.id.maxDetail);
        min = (TextView) findViewById(R.id.minDetail);
        chart = (CombinedChart) findViewById(R.id.chart1);

        DBop = new DBOperations(this);
        DBop.open();

        //Get avg/min/max
        getData();
        //createChart();
        createChart();



    }

    private void getData() {
        List<UserData> allData = DBop.getAllData();
        float totalDist = 0;
        float totalTime = 0;
        float minDperTime = 0;
        float maxDperTime = 0;

        int count = 1;
        for(UserData ud: allData) {
            totalDist += ud.getwDist();
            totalTime += ud.getwTime();
            if(count ==  1) {
                minDperTime = ud.getwDist() / ud.getwTime();
                maxDperTime = ud.getwDist() / ud.getwTime();
                count++;
            }
            else {
                float curSpeed = ud.getwDist() / ud.getwTime();
                if(curSpeed < minDperTime) {
                    minDperTime = curSpeed;
                }
                if(curSpeed > maxDperTime) {
                    maxDperTime = curSpeed;
                }
            }
        }
        float average = totalDist / totalTime;

        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        avgDetail.setText(df.format(average));
        min.setText(df.format(minDperTime));
        max.setText(df.format(maxDperTime));

    }

    private void createChart() {
        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        chart.setHighlightFullBarEnabled(false);

        chart.setDrawOrder(new CombinedChart.DrawOrder[]{
                CombinedChart.DrawOrder.BAR, CombinedChart.DrawOrder.LINE });

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        Legend l = chart.getLegend();
        l.setWordWrapEnabled(true);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        CombinedData data = new CombinedData();
        data.setData(generateBarData());

        chart.setData(data);
        chart.animateXY(2000,2000);
        chart.invalidate();

    }

    private BarData generateBarData() {

        ArrayList<BarEntry> entries = new ArrayList<>();
        List<Float> distanceData = getDistanceData();

        for (int index = 0; index < distanceData.size(); index++) {
            entries.add(new BarEntry(index, distanceData.get(index)));
        }

        BarDataSet set1 = new BarDataSet(entries, "Calories");
        set1.setColor(Color.rgb(60, 220, 78));
        set1.setValueTextColor(Color.rgb(60, 220, 78));
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);

        return new BarData(set1);
    }
    private List<Float> getDistanceData() {

        List<Float> returnList = new ArrayList<>();
        int steps_per_mi = 2000;

        List <UserData> allData;
        allData = DBop.getAllData();

        for (UserData data: allData) {
            returnList.add(data.getwDist() * steps_per_mi);
        }

        return returnList;

    }

}
