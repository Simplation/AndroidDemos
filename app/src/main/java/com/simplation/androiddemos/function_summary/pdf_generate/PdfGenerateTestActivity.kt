package com.simplation.androiddemos.function_summary.pdf_generate

import android.app.FragmentTransaction
import android.app.ProgressDialog
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.itextpdf.text.Document
import com.itextpdf.text.DocumentException
import com.itextpdf.text.Font
import com.itextpdf.text.Paragraph
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import com.simplation.androiddemos.R
import com.simplation.androiddemos.base.BaseActivity
import com.simplation.androiddemos.function_summary.pdf_generate.pdf.PdfCenterDialog
import com.simplation.androiddemos.function_summary.pdf_generate.pdf.PdfUtils
import kotlinx.android.synthetic.main.activity_pdf_generate_test.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class PdfGenerateTestActivity : BaseActivity() {

    private var myDialog // 保存进度框
            : ProgressDialog? = null

    // 保存 PDF 文件的开始意图
    private val PDF_SAVE_START = 1

    // 保存 PDF 文件的结果开始意图
    private val PDF_SAVE_RESULT = 2


    override fun getLayoutId(): Int {
        return R.layout.activity_pdf_generate_test
    }

    override fun setView(savedInstanceState: Bundle?) {
    }

    override fun initData() {
    }

    private val handler =
        Handler(Handler.Callback { msg ->
            when (msg.what) {
                PDF_SAVE_START -> if (!myDialog!!.isShowing) myDialog!!.show()
                PDF_SAVE_RESULT -> {
                    if (myDialog!!.isShowing) myDialog!!.dismiss()
                    Toast.makeText(this@PdfGenerateTestActivity, "保存成功", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            false
        })

    override fun initView() {
        // 生成
        btn_pdf.setOnClickListener {
            generatePDF()
        }

        // 查看
        btn_look_pdf.setOnClickListener {
            lookPDF()
        }

        myDialog = ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT)
        myDialog!!.setIndeterminateDrawable(
            resources.getDrawable(
                R.drawable.progress_ocr
            )
        )
        myDialog!!.setMessage("正在保存PDF文件...")
        myDialog!!.setCanceledOnTouchOutside(false)
        myDialog!!.setCancelable(false)
    }

    private fun lookPDF() {
        val dialog = PdfCenterDialog()
        val ft = fragmentManager.beginTransaction()
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        dialog.show(ft, "")
    }

    private fun generatePDF() {
        if (et_pdf.text.toString() == "") {
            Toast.makeText(this, "请输入内容", Toast.LENGTH_SHORT).show()
            return
        }
        val file = File(PdfUtils.ADDRESS)
        if (!file.exists()) file.mkdirs()
        val time = System.currentTimeMillis()
        val date = Date(time)
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA)
        val pdf_address: String =
            (PdfUtils.ADDRESS.toString() + File.separator + "PDF_"
                    + sdf.format(date) + ".pdf")
        handler.sendEmptyMessage(PDF_SAVE_START)
        Thread(Runnable {
            val doc = Document() // 创建一个document对象
            val fos: FileOutputStream
            try {
                fos = FileOutputStream(pdf_address) // pdf_address为Pdf文件保存到sd卡的路径
                PdfWriter.getInstance(doc, fos)
                doc.open()
                doc.setPageCount(1)
                doc.add(
                    Paragraph(
                        et_pdf.text.toString(),
                        setChineseFont()
                    )
                ) // result为保存的字符串
                // ,setChineseFont()为pdf字体
                // 一定要记得关闭document对象
                doc.close()
                fos.flush()
                fos.close()
                handler.sendEmptyMessage(PDF_SAVE_RESULT)
            } catch (e1: FileNotFoundException) {
                e1.printStackTrace()
            } catch (e: DocumentException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }).start()
    }

    /**
     * 设置 PDF 字体(较为耗时)
     */
    fun setChineseFont(): Font? {
        var bf: BaseFont? = null
        var fontChinese: Font? = null
        try { // STSong-Light : Adobe的字体
            // UniGB-UCS2-H : pdf 字体
            bf = BaseFont.createFont(
                "STSong-Light", "UniGB-UCS2-H",
                BaseFont.NOT_EMBEDDED
            )
            fontChinese = Font(bf, 12f, Font.NORMAL)
        } catch (e: DocumentException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return fontChinese
    }
}
