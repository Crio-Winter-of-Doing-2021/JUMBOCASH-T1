package com.jumbotail.cashflow.ui.transaction;

import static com.jumbotail.cashflow.utils.Constants.APP_PACKAGE;
import static com.jumbotail.cashflow.utils.HelperFunctions.toDate;
import static com.jumbotail.cashflow.utils.ViewUtils.getIntentData;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.Nullable;
import com.jumbotail.cashflow.R;
import com.jumbotail.cashflow.data.model.Transaction;
import com.tejpratapsingh.pdfcreator.activity.PDFCreatorActivity;
import com.tejpratapsingh.pdfcreator.utils.PDFUtil;
import com.tejpratapsingh.pdfcreator.views.PDFBody;
import com.tejpratapsingh.pdfcreator.views.PDFFooterView;
import com.tejpratapsingh.pdfcreator.views.PDFHeaderView;
import com.tejpratapsingh.pdfcreator.views.PDFTableView;
import com.tejpratapsingh.pdfcreator.views.PDFTableView.PDFTableRowView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFHorizontalView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFImageView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFLineSeparatorView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFTextView;
import com.tejpratapsingh.pdfcreator.views.basic.PDFTextView.PDF_TEXT_SIZE;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TransactionPdfReportActivity extends PDFCreatorActivity {

  private static final String TAG = "TransactionPdfReportAct";
  List<Transaction> transactionList;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if (getSupportActionBar() != null) {
      getSupportActionBar().hide();
    }

    transactionList = getIntentData(this, APP_PACKAGE);



    createPDF("test", new PDFUtil.PDFUtilListener() {
      @Override
      public void pdfGenerationSuccess(File savedPDFFile) {
        Toast.makeText(TransactionPdfReportActivity.this, "PDF Created", Toast.LENGTH_SHORT).show();
      }

      @Override
      public void pdfGenerationFailure(Exception exception) {
        Toast.makeText(TransactionPdfReportActivity.this, "PDF NOT Created", Toast.LENGTH_SHORT).show();
      }
    });
  }

  @Override
  protected PDFHeaderView getHeaderView(int pageIndex) {
    PDFHeaderView headerView = new PDFHeaderView(getApplicationContext());

    PDFHorizontalView horizontalView = new PDFHorizontalView(getApplicationContext());

    PDFTextView pdfTextView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.HEADER);
    SpannableString word = new SpannableString("Jumbotail Cashflow");
    word.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0, word.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    pdfTextView.setText(word);
    pdfTextView.setLayout(new LinearLayout.LayoutParams(
        0,
        LinearLayout.LayoutParams.MATCH_PARENT, 1));
    pdfTextView.getView().setGravity(Gravity.CENTER_VERTICAL);
    pdfTextView.getView().setTypeface(pdfTextView.getView().getTypeface(), Typeface.BOLD);

    horizontalView.addView(pdfTextView);

    PDFImageView imageView = new PDFImageView(getApplicationContext());
    LinearLayout.LayoutParams imageLayoutParam = new LinearLayout.LayoutParams(
        100,
        60, 0);
    imageView.setImageScale(ImageView.ScaleType.CENTER_INSIDE);
    imageView.setImageResource(R.drawable.ic_logo_jumbotail);
    imageLayoutParam.setMargins(0, 0, 10, 10);
    imageView.setLayout(imageLayoutParam);

    horizontalView.addView(imageView);

    headerView.addView(horizontalView);

    PDFLineSeparatorView lineSeparatorView1 = new PDFLineSeparatorView(getApplicationContext()).setBackgroundColor(Color.WHITE);
    headerView.addView(lineSeparatorView1);

    return headerView;
  }

  @Override
  protected PDFBody getBodyViews() {
    PDFBody pdfBody = new PDFBody();

    PDFTextView pdfCompanyNameView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H2);
    pdfCompanyNameView.setText("Cashflow user");
    pdfBody.addView(pdfCompanyNameView);
    PDFLineSeparatorView lineSeparatorView1 = new PDFLineSeparatorView(getApplicationContext()).setBackgroundColor(Color.WHITE);
    pdfBody.addView(lineSeparatorView1);
    PDFTextView pdfAddressView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
    pdfAddressView.setText("Kolkata, 700112");
    pdfBody.addView(pdfAddressView);

    PDFLineSeparatorView lineSeparatorView2 = new PDFLineSeparatorView(getApplicationContext()).setBackgroundColor(Color.WHITE);
    lineSeparatorView2.setLayout(new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        8, 0));
    pdfBody.addView(lineSeparatorView2);

    String[] textInTable = {"ID", "DATE", "Entity", "Amount","Status","Type"};


    PDFLineSeparatorView lineSeparatorView3 = new PDFLineSeparatorView(getApplicationContext()).setBackgroundColor(Color.WHITE);
    pdfBody.addView(lineSeparatorView3);
    PDFTextView pdfTableTitleView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
    if(transactionList != null && !transactionList.isEmpty()) {
      pdfTableTitleView.setText("Transaction from " + toDate(transactionList.get(0).getTimestamp())
          + " to " + toDate(transactionList.get(transactionList.size() - 1).getTimestamp())
      );
    }
    pdfBody.addView(pdfTableTitleView);

    PDFLineSeparatorView lineSeparatorView5 = new PDFLineSeparatorView(getApplicationContext()).setBackgroundColor(Color.WHITE);
    lineSeparatorView5.setLayout(new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        10, 0));
    pdfBody.addView(lineSeparatorView5);

    PDFTableView.PDFTableRowView tableHeader = new PDFTableView.PDFTableRowView(getApplicationContext());
    for (String s : textInTable) {
      PDFTextView pdfTextView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H3);
      pdfTextView.setText(s);
      tableHeader.addToRow(pdfTextView);
    }
    PDFTableView.PDFTableRowView tableRowView1 = new PDFTableView.PDFTableRowView(getApplicationContext());
    for (String s : textInTable) {
      PDFTextView pdfTextView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.P);
      tableRowView1.addToRow(pdfTextView);
    }

    PDFTableView tableView = new PDFTableView(getApplicationContext(), tableHeader, tableRowView1);
    for (int i = 0; transactionList != null && i < transactionList.size(); i++) {
      // Create 10 rows
      PDFTableView.PDFTableRowView tableRowView = new PDFTableView.PDFTableRowView(getApplicationContext());
      Transaction t = transactionList.get(i);
      ArrayList<String> transactionContent = new ArrayList<>();
      transactionContent.add(String.valueOf(t.getId()));
      transactionContent.add(toDate(t.getTimestamp()));
      transactionContent.add(t.getEntityName());
      transactionContent.add(String.valueOf(t.getAmount()));
      transactionContent.add(t.getStatus());
      transactionContent.add(t.getType());
      for (String content : transactionContent) {
        PDFTextView pdfTextView = new PDFTextView(getApplicationContext(), PDF_TEXT_SIZE.SMALL);
        pdfTextView.setText(content);
        tableRowView.addToRow(pdfTextView);
      }
      tableView.addRow(tableRowView);
    }
    pdfBody.addView(tableView);

    PDFLineSeparatorView lineSeparatorView4 = new PDFLineSeparatorView(getApplicationContext()).setBackgroundColor(Color.WHITE);
    pdfBody.addView(lineSeparatorView4);

    PDFTextView pdfIconLicenseView = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.H3);
    Spanned icon8Link = Html.fromHtml("");
    pdfIconLicenseView.getView().setText(icon8Link);
    pdfBody.addView(pdfIconLicenseView);

    return pdfBody;
  }

  @Override
  protected PDFFooterView getFooterView(int pageIndex) {
    PDFFooterView footerView = new PDFFooterView(getApplicationContext());

    PDFTextView pdfTextViewPage = new PDFTextView(getApplicationContext(), PDFTextView.PDF_TEXT_SIZE.SMALL);
    pdfTextViewPage.setText(String.format(Locale.getDefault(), "Page: %d", pageIndex + 1));
    pdfTextViewPage.setLayout(new LinearLayout.LayoutParams(
        LinearLayout.LayoutParams.MATCH_PARENT,
        LinearLayout.LayoutParams.MATCH_PARENT, 0));
    pdfTextViewPage.getView().setGravity(Gravity.CENTER_HORIZONTAL);

    footerView.addView(pdfTextViewPage);

    return footerView;
  }

  @Nullable
  @Override
  protected PDFImageView getWatermarkView(int forPage) {
    PDFImageView pdfImageView = new PDFImageView(getApplicationContext());
    FrameLayout.LayoutParams childLayoutParams = new FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        200, Gravity.CENTER);
    pdfImageView.setLayout(childLayoutParams);

    pdfImageView.setImageResource(R.drawable.ic_logo_jumbotail);
    pdfImageView.setImageScale(ImageView.ScaleType.FIT_CENTER);
    pdfImageView.getView().setAlpha(0.3F);

    return pdfImageView;
  }

  @Override
  protected void onNextClicked(final File savedPDFFile) {

    PrintAttributes.Builder printAttributeBuilder = new PrintAttributes.Builder();
    printAttributeBuilder.setMediaSize(PrintAttributes.MediaSize.ISO_A4);
    printAttributeBuilder.setMinMargins(PrintAttributes.Margins.NO_MARGINS);

    PDFUtil.printPdf(TransactionPdfReportActivity.this,savedPDFFile, printAttributeBuilder.build());

  }
}
