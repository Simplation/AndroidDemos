package com.simplation.androiddemos.function_summary.pdf_generate.pdf;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfTextExtractor;
import com.simplation.androiddemos.R;

import java.io.IOException;

public class PdfActivity extends Activity {

    // private EditText et_content; //读取pdf文件内容显示的控件
    private TextView et_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfs);
        et_content = findViewById(R.id.et_pdf);
        init();
    }

    /**
     * 初始化操作
     */
    private void init() {
//        initView();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        Intent intent = getIntent();
        String path = intent.getStringExtra("path");
        readPdfContent(path);
    }

    /**
     * 读取Pdf文件的内容
     *
     * @param path :文件地址
     */
    public void readPdfContent(String path) {
        try {
            PdfReader pr = new PdfReader(path);
            int page = pr.getNumberOfPages();
            String content = "";
            for (int i = 1; i < page + 1; i++) {
                content += PdfTextExtractor.getTextFromPage(pr, i);
            }
            et_content.setText(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
