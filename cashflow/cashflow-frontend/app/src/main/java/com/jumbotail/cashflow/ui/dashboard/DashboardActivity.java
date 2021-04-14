package com.jumbotail.cashflow.ui.dashboard;

import static com.jumbotail.cashflow.utils.ViewUtils.intent;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import android.os.Bundle;
import androidx.core.view.GravityCompat;
import com.github.clans.fab.Label;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.Legend.LegendForm;
import com.github.mikephil.charting.components.Legend.LegendOrientation;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener;
import com.jumbotail.cashflow.R;
import com.jumbotail.cashflow.ViewModelProviderFactory;
import com.jumbotail.cashflow.data.local.AppPreferencesHelper;
import com.jumbotail.cashflow.data.model.TopDebitEntities;
import com.jumbotail.cashflow.data.network.Resource.Status;
import com.jumbotail.cashflow.databinding.ActivityDashboardBinding;
import com.jumbotail.cashflow.ui.BaseActivity;
import com.jumbotail.cashflow.ui.entities.AddEntityActivity;
import com.jumbotail.cashflow.ui.entities.EntityActivity;
import com.jumbotail.cashflow.ui.entities.EntityViewModel;
import com.jumbotail.cashflow.ui.entities.FavouriteEntityActivity;
import com.jumbotail.cashflow.ui.transaction.AddTransactionActivity;
import com.jumbotail.cashflow.ui.transaction.TransactionHistoryActivity;
import com.jumbotail.cashflow.ui.transaction.TransactionPdfReportActivity;
import com.jumbotail.cashflow.ui.transaction.TransactionViewModel;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DashboardActivity extends BaseActivity<ActivityDashboardBinding, EntityViewModel>
    implements OnClickListener, OnNavigationItemSelectedListener {

  private static final String TAG = "DashboardActivity";
  private AppPreferencesHelper prefHelper;
  private TransactionViewModel transactionViewModel;
  private ViewModelProviderFactory<TransactionViewModel> factory;

  @Override
  protected Class<EntityViewModel> getViewModel() {
    return EntityViewModel.class;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    initializations();
    initDrawerMenu();

    viewBinding.chartTransactionTrend.getDescription().setEnabled(false);
    initTransactionTrendBar();
    viewBinding.chartTransactionTrend.setFitBars(true);




    viewModel.getTopDebitEntitiesByRange(1617215400,1619807399);

    viewModel.getTopCreditEntitiesByRange(1617215400,1619807399);

    viewModel.topDebitEntityList.observe(this, listResource -> {
      if(listResource.status == Status.SUCCESS){
        Log.d(TAG, "onCreate: "+listResource.data);
        if(!listResource.data.isEmpty())
        initTopDebitPieChart(listResource.data);
      }
    });

    viewModel.topCreditEntityList.observe(this, listResource -> {
      if(listResource.status == Status.SUCCESS){
        Log.d(TAG, "onCreate: "+listResource.data);
        if(!listResource.data.isEmpty())
        initTopCreditPieChart(listResource.data);
      }
    });


  }

  @Override
  public ActivityDashboardBinding getBinding() {
    return ActivityDashboardBinding.inflate(getLayoutInflater());
  }

  @Override
  public void initListeners() {

  }

  private void initializations() {

  }

  private void initDrawerMenu(){
    setSupportActionBar(viewBinding.toolbar);
    viewBinding.navMenu.bringToFront();
    ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
        this, viewBinding.drawerLayout,
        viewBinding.toolbar,
        R.string.navigation_open_drawer,
        R.string.navigation_close_drawer
    );

    drawerToggle.getDrawerArrowDrawable().setColor(Color.WHITE);

    viewBinding.drawerLayout.addDrawerListener(drawerToggle);
    drawerToggle.syncState();

    viewBinding.navMenu.setNavigationItemSelectedListener(this);
  }

  private void initTopDebitPieChart(List<TopDebitEntities> topDebits){
    PieChart pieChart = viewBinding.pieChartTopDebits;
    pieChart.setUsePercentValues(false);
    pieChart.getDescription().setEnabled(false);

    pieChart.setDragDecelerationFrictionCoef(0.95f);
    pieChart.setDrawHoleEnabled(true);
    pieChart.setHoleColor(Color.WHITE);
    pieChart.setTransparentCircleRadius(61f);

    ArrayList<PieEntry> pieEntries = new ArrayList<>();

    for(TopDebitEntities top : topDebits){
      pieEntries.add(new PieEntry(top.getAmount(), top.getEntityName()));
    }

    PieDataSet dataSet = new PieDataSet(pieEntries, "");
    dataSet.setSliceSpace(4f);
    dataSet.setSelectionShift(5f);
    dataSet.setColors(ColorTemplate.PASTEL_COLORS);
    dataSet.setValueLinePart1OffsetPercentage(5f);
    dataSet.setValueLinePart1Length(0.1f);
    dataSet.setValueLinePart2Length(0.5f);
    dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

    PieData data = new PieData(dataSet);
    data.setValueTextSize(10f);
    data.setValueTextColor(Color.WHITE);
    viewBinding.pieChartTopDebits.setEntryLabelColor(Color.BLACK);

    // legends
    Legend legend = pieChart.getLegend();
    legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
    legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
    legend.setOrientation(LegendOrientation.HORIZONTAL);
    legend.setDrawInside(false);
    legend.setForm(LegendForm.SQUARE);
    legend.setXEntrySpace(20f);
    legend.setYEntrySpace(0f);
    legend.setWordWrapEnabled(true);
    legend.setDrawInside(false);
    legend.getCalculatedLineSizes();
    legend.setYOffset(10f);
    legend.setTextSize(15f);
    pieChart.setData(data);
    pieChart.invalidate();
  }

  private void initTopCreditPieChart(List<TopDebitEntities> topCredits){
    PieChart pieChart = viewBinding.pieChartTopCredits;
    pieChart.setUsePercentValues(false);
    pieChart.getDescription().setEnabled(false);

    pieChart.setDragDecelerationFrictionCoef(0.95f);
    pieChart.setDrawHoleEnabled(true);
    pieChart.setHoleColor(Color.WHITE);
    pieChart.setTransparentCircleRadius(61f);

    ArrayList<PieEntry> pieEntries = new ArrayList<>();

    for(TopDebitEntities top : topCredits){
      pieEntries.add(new PieEntry(top.getAmount(), top.getEntityName()));
    }

    PieDataSet dataSet = new PieDataSet(pieEntries, "");
    dataSet.setSliceSpace(4f);
    dataSet.setSelectionShift(5f);
    dataSet.setColors(ColorTemplate.PASTEL_COLORS);
    dataSet.setValueLinePart1OffsetPercentage(5f);
    dataSet.setValueLinePart1Length(0.1f);
    dataSet.setValueLinePart2Length(0.5f);
    dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

    PieData data = new PieData(dataSet);
    data.setValueTextSize(10f);
    data.setValueTextColor(Color.WHITE);
    viewBinding.pieChartTopCredits.setEntryLabelColor(Color.BLACK);

    // legends
    Legend legend = pieChart.getLegend();
    legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
    legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
    legend.setOrientation(LegendOrientation.HORIZONTAL);
    legend.setDrawInside(false);
    legend.setForm(LegendForm.SQUARE);
    legend.setXEntrySpace(20f);
    legend.setYEntrySpace(0f);
    legend.setWordWrapEnabled(true);
    legend.setDrawInside(false);
    legend.getCalculatedLineSizes();
    legend.setYOffset(10f);
    legend.setTextSize(15f);
    pieChart.setData(data);
    pieChart.invalidate();

  }

  private void initTransactionTrendBar(){
    ArrayList<BarEntry> yVals = new ArrayList<>();

    yVals.add(new BarEntry(0, 5300));
    yVals.add(new BarEntry(1, 2840));

    BarDataSet dataSet = new BarDataSet(yVals, "MONTH APRIL");
    dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
    dataSet.setDrawValues(true);
    BarData data = new BarData(dataSet);

    data.setValueTextSize(10f);

    ArrayList<String> xAxisLables = new ArrayList();
    xAxisLables.add("CASH IN");
    xAxisLables.add("CASH OUT");


    viewBinding.chartTransactionTrend.getXAxis().setValueFormatter(new IndexAxisValueFormatter(
        xAxisLables
    ));

    viewBinding.chartTransactionTrend.getAxisLeft().setAxisMinimum(0);
    Legend l = viewBinding.chartTransactionTrend.getLegend();
    l.setTextSize(12f);

    viewBinding.chartTransactionTrend.setData(data);
    viewBinding.chartTransactionTrend.invalidate();
    viewBinding.chartTransactionTrend.animateY(500);

  }




  @SuppressLint("NonConstantResourceId")
  @Override
  public void onClick(View v) {
    switch (v.getId()){

    }
  }

  @Override
  public void onBackPressed() {

    if(viewBinding.drawerLayout.isDrawerOpen(GravityCompat.START)){
      viewBinding.drawerLayout.closeDrawer(GravityCompat.START);
      return;
    }
    super.onBackPressed();
  }

  @SuppressLint("NonConstantResourceId")
  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    switch (item.getItemId()){
      case R.id.menuDashboard:{
        break;
      }

      case R.id.menuBusinessEntities:{
        delayedIntent(EntityActivity.class);
        break;
      }

      case R.id.menuAddEntities:
        delayedIntent(AddEntityActivity.class);
        break;

      case R.id.menuAddTransactions:
        delayedIntent(AddTransactionActivity.class);

        break;

      case R.id.menuTransactionHistory:
        delayedIntent(TransactionHistoryActivity.class);
        break;

      case R.id.menuFavouriteEntities:
        delayedIntent(FavouriteEntityActivity.class);
    }

    viewBinding.drawerLayout.closeDrawer(GravityCompat.START);
    return true;
  }

  private void delayedIntent(Class<?> cls){ ;new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        intent(DashboardActivity.this, cls);
      }
    }, 220);
  }
}