package com.simplation.androiddemos.function_summary.baidu_ai

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.baidu.ocr.sdk.OCR
import com.baidu.ocr.sdk.OnResultListener
import com.baidu.ocr.sdk.exception.OCRError
import com.baidu.ocr.sdk.model.AccessToken
import com.simplation.androiddemos.R
import com.simplation.androiddemos.base.BaseActivity
import com.simplation.androiddemos.function_summary.baidu_ai.ocr.camera.CameraActivity
import kotlinx.android.synthetic.main.activity_baidu_ai.*

class BaiduAIActivity : BaseActivity() {

    companion object {
        private const val REQUEST_CODE_GENERAL = 105
        private const val REQUEST_CODE_GENERAL_BASIC = 106
        private const val REQUEST_CODE_ACCURATE_BASIC = 107
        private const val REQUEST_CODE_ACCURATE = 108
        private const val REQUEST_CODE_GENERAL_ENHANCED = 109
        private const val REQUEST_CODE_GENERAL_WEBIMAGE = 110
        private const val REQUEST_CODE_BANKCARD = 111
        private const val REQUEST_CODE_VEHICLE_LICENSE = 120
        private const val REQUEST_CODE_DRIVING_LICENSE = 121
        private const val REQUEST_CODE_LICENSE_PLATE = 122
        private const val REQUEST_CODE_BUSINESS_LICENSE = 123
        private const val REQUEST_CODE_RECEIPT = 124

        private const val REQUEST_CODE_PASSPORT = 125
        private const val REQUEST_CODE_NUMBERS = 126
        private const val REQUEST_CODE_QRCODE = 127
        private const val REQUEST_CODE_BUSINESSCARD = 128
        private const val REQUEST_CODE_HANDWRITING = 129
        private const val REQUEST_CODE_LOTTERY = 130
        private const val REQUEST_CODE_VATINVOICE = 131
        private const val REQUEST_CODE_CUSTOM = 132

        const val AK = "LR0Hw41KtkkiT0wRS8UA42eU"
        const val SK = "stXq3Vh0zbFG0jGKfyijiFEddpDxig3G"

        private var alertDialog: AlertDialog.Builder? = null

    }

    private var hasGotToken = false

    override fun getLayoutId(): Int {
        return R.layout.activity_baidu_ai
    }

    override fun setView(savedInstanceState: Bundle?) {
    }

    override fun initData() {
    }

    override fun initView() {
        // ??????????????????????????????
        // initAccessToken();
        initAccessTokenWithAkSk()

        alertDialog = AlertDialog.Builder(this)

        // ??????????????????
        general_basic_button.setOnClickListener {
            if (!checkTokenStatus()) {
                return@setOnClickListener
            }
            val intent = Intent(this@BaiduAIActivity, CameraActivity::class.java)
            intent.putExtra(
                CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(application).absolutePath
            )
            intent.putExtra(
                CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_GENERAL
            )
            startActivityForResult(
                intent, REQUEST_CODE_GENERAL_BASIC
            )
        }

        // ??????????????????(????????????)
        accurate_basic_button.setOnClickListener {
            if (!checkTokenStatus()) {
                return@setOnClickListener
            }
            val intent = Intent(this@BaiduAIActivity, CameraActivity::class.java)
            intent.putExtra(
                CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(application).absolutePath
            )
            intent.putExtra(
                CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_GENERAL
            )
            startActivityForResult(
                intent, REQUEST_CODE_ACCURATE_BASIC
            )
        }

        // ??????????????????????????????????????????
        general_button.setOnClickListener {
            if (!checkTokenStatus()) {
                return@setOnClickListener
            }
            val intent = Intent(this@BaiduAIActivity, CameraActivity::class.java)
            intent.putExtra(
                CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(application).absolutePath
            )
            intent.putExtra(
                CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_GENERAL
            )
            startActivityForResult(
                intent, REQUEST_CODE_GENERAL
            )
        }

        // ???????????????????????????????????????????????????
        accurate_button.setOnClickListener {
            if (!checkTokenStatus()) {
                return@setOnClickListener
            }
            val intent = Intent(this@BaiduAIActivity, CameraActivity::class.java)
            intent.putExtra(
                CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(application).absolutePath
            )
            intent.putExtra(
                CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_GENERAL
            )
            startActivityForResult(
                intent, REQUEST_CODE_ACCURATE
            )
        }

        // ???????????????????????????????????????
        general_enhance_button.setOnClickListener {
            if (!checkTokenStatus()) {
                return@setOnClickListener
            }
            val intent = Intent(this@BaiduAIActivity, CameraActivity::class.java)
            intent.putExtra(
                CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(application).absolutePath
            )
            intent.putExtra(
                CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_GENERAL
            )
            startActivityForResult(
                intent,
                REQUEST_CODE_GENERAL_ENHANCED
            )
        }

        // ??????????????????
        general_webimage_button.setOnClickListener {
            if (!checkTokenStatus()) {
                return@setOnClickListener
            }
            val intent = Intent(this@BaiduAIActivity, CameraActivity::class.java)
            intent.putExtra(
                CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(application).absolutePath
            )
            intent.putExtra(
                CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_GENERAL
            )
            startActivityForResult(
                intent,
                REQUEST_CODE_GENERAL_WEBIMAGE
            )
        }

        // ???????????????
        idcard_button.setOnClickListener {
            if (!checkTokenStatus()) {
                return@setOnClickListener
            }
            val intent = Intent(this@BaiduAIActivity, IDCardActivity::class.java)
            startActivity(intent)
        }

        // ???????????????
        bankcard_button.setOnClickListener {
            if (!checkTokenStatus()) {
                return@setOnClickListener
            }
            val intent = Intent(this@BaiduAIActivity, CameraActivity::class.java)
            intent.putExtra(
                CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(application).absolutePath
            )
            intent.putExtra(
                CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_BANK_CARD
            )
            startActivityForResult(
                intent, REQUEST_CODE_BANKCARD
            )
        }

        // ???????????????
        vehicle_license_button.setOnClickListener {
            if (!checkTokenStatus()) {
                return@setOnClickListener
            }
            val intent = Intent(this@BaiduAIActivity, CameraActivity::class.java)
            intent.putExtra(
                CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(application).absolutePath
            )
            intent.putExtra(
                CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_GENERAL
            )
            startActivityForResult(
                intent, REQUEST_CODE_VEHICLE_LICENSE
            )
        }

        // ???????????????
        driving_license_button.setOnClickListener {
            if (!checkTokenStatus()) {
                return@setOnClickListener
            }
            val intent = Intent(this@BaiduAIActivity, CameraActivity::class.java)
            intent.putExtra(
                CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(application).absolutePath
            )
            intent.putExtra(
                CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_GENERAL
            )
            startActivityForResult(
                intent, REQUEST_CODE_DRIVING_LICENSE
            )
        }

        // ????????????
        license_plate_button.setOnClickListener {
            if (!checkTokenStatus()) {
                return@setOnClickListener
            }
            val intent = Intent(this@BaiduAIActivity, CameraActivity::class.java)
            intent.putExtra(
                CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(application).absolutePath
            )
            intent.putExtra(
                CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_GENERAL
            )
            startActivityForResult(
                intent, REQUEST_CODE_LICENSE_PLATE
            )
        }

        // ??????????????????
        business_license_button.setOnClickListener {
            if (!checkTokenStatus()) {
                return@setOnClickListener
            }
            val intent = Intent(this@BaiduAIActivity, CameraActivity::class.java)
            intent.putExtra(
                CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(application).absolutePath
            )
            intent.putExtra(
                CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_GENERAL
            )
            startActivityForResult(
                intent, REQUEST_CODE_BUSINESS_LICENSE
            )
        }

        // ??????????????????
        receipt_button.setOnClickListener {
            if (!checkTokenStatus()) {
                return@setOnClickListener
            }

            val intent = Intent(this@BaiduAIActivity, CameraActivity::class.java)
            intent.putExtra(
                CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(application).absolutePath
            )
            intent.putExtra(
                CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_PASSPORT
            )
            startActivityForResult(
                intent, REQUEST_CODE_PASSPORT
            )
        }

        // ????????????
        passport_button.setOnClickListener {
            if (!checkTokenStatus()) {
                return@setOnClickListener
            }
            val intent = Intent(this@BaiduAIActivity, CameraActivity::class.java)
            intent.putExtra(
                CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(application).absolutePath
            )
            intent.putExtra(
                CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_PASSPORT
            )
            startActivityForResult(
                intent, REQUEST_CODE_PASSPORT
            )
        }

        // ???????????????
        qrcode_button.setOnClickListener {
            if (!checkTokenStatus()) {
                return@setOnClickListener
            }
            val intent = Intent(this@BaiduAIActivity, CameraActivity::class.java)
            intent.putExtra(
                CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(application).absolutePath
            )
            intent.putExtra(
                CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_GENERAL
            )
            startActivityForResult(
                intent, REQUEST_CODE_QRCODE
            )
        }

        // ????????????
        numbers_button.setOnClickListener {
            if (!checkTokenStatus()) {
                return@setOnClickListener
            }
            val intent = Intent(this@BaiduAIActivity, CameraActivity::class.java)
            intent.putExtra(
                CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(application).absolutePath
            )
            intent.putExtra(
                CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_GENERAL
            )
            startActivityForResult(
                intent, REQUEST_CODE_NUMBERS
            )
        }

        // ????????????
        business_card_button.setOnClickListener {
            if (!checkTokenStatus()) {
                return@setOnClickListener
            }
            val intent = Intent(this@BaiduAIActivity, CameraActivity::class.java)
            intent.putExtra(
                CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(application).absolutePath
            )
            intent.putExtra(
                CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_GENERAL
            )
            startActivityForResult(
                intent, REQUEST_CODE_BUSINESSCARD
            )
        }

        // ?????????????????????
        vat_invoice_button.setOnClickListener {
            if (!checkTokenStatus()) {
                return@setOnClickListener
            }
            val intent = Intent(this@BaiduAIActivity, CameraActivity::class.java)
            intent.putExtra(
                CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(application).absolutePath
            )
            intent.putExtra(
                CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_GENERAL
            )
            startActivityForResult(
                intent, REQUEST_CODE_VATINVOICE
            )
        }

        // ????????????
        lottery_button.setOnClickListener {
            if (!checkTokenStatus()) {
                return@setOnClickListener
            }
            val intent = Intent(this@BaiduAIActivity, CameraActivity::class.java)
            intent.putExtra(
                CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(application).absolutePath
            )
            intent.putExtra(
                CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_GENERAL
            )
            startActivityForResult(
                intent, REQUEST_CODE_LOTTERY
            )
        }

        // ????????????
        handwritting_button.setOnClickListener {
            if (!checkTokenStatus()) {
                return@setOnClickListener
            }
            val intent = Intent(this@BaiduAIActivity, CameraActivity::class.java)
            intent.putExtra(
                CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(application).absolutePath
            )
            intent.putExtra(
                CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_GENERAL
            )
            startActivityForResult(
                intent, REQUEST_CODE_HANDWRITING
            )
        }

        // ???????????????
        custom_button.setOnClickListener {
            if (!checkTokenStatus()) {
                return@setOnClickListener
            }
            val intent = Intent(this@BaiduAIActivity, CameraActivity::class.java)
            intent.putExtra(
                CameraActivity.KEY_OUTPUT_FILE_PATH,
                FileUtil.getSaveFile(application).absolutePath
            )
            intent.putExtra(
                CameraActivity.KEY_CONTENT_TYPE,
                CameraActivity.CONTENT_TYPE_GENERAL
            )
            startActivityForResult(
                intent, REQUEST_CODE_CUSTOM
            )
        }
    }

    private fun checkTokenStatus(): Boolean {
        if (!hasGotToken) {
            Toast.makeText(applicationContext, "token??????????????????", Toast.LENGTH_LONG).show()
        }
        return hasGotToken
    }

    /**
     * ???license?????????????????????
     */
    private fun initAccessToken() {
        OCR.getInstance(this).initAccessToken(object : OnResultListener<AccessToken> {
            override fun onResult(accessToken: AccessToken) {
                val token = accessToken.accessToken
                hasGotToken = true
            }

            override fun onError(error: OCRError) {
                error.printStackTrace()
                alertText("licence????????????token??????", error.message)
            }
        }, applicationContext)
    }

    /**
     * ?????????ak???sk?????????
     */
    private fun initAccessTokenWithAkSk() {
        OCR.getInstance(this).initAccessTokenWithAkSk(
            object :
                OnResultListener<AccessToken> {
                override fun onResult(result: AccessToken) {
                    val token = result.accessToken
                    hasGotToken = true
                }

                override fun onError(error: OCRError) {
                    error.printStackTrace()
                    alertText("AK???SK????????????token??????", error.message)
                }
            },
            applicationContext,
            AK,
            SK
        )
    }

    /**
     * ?????????license????????????????????????????????????license?????????????????????
     */
    private fun initAccessTokenLicenseFile() {
        OCR.getInstance(this).initAccessToken(object : OnResultListener<AccessToken> {
            override fun onResult(accessToken: AccessToken) {
                val token = accessToken.accessToken
                hasGotToken = true
            }

            override fun onError(error: OCRError) {
                error.printStackTrace()
                alertText("?????????????????????licence????????????token??????", error.message)
            }
        }, "aip.license", applicationContext)
    }


    private fun alertText(title: String, message: String?) {
        runOnUiThread {
            alertDialog!!.setTitle(title)
                .setMessage(message)
                .setPositiveButton("??????", null)
                .show()
        }
    }

    private fun infoPopText(result: String) {
        alertText("", result)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initAccessToken()
        } else {
            Toast.makeText(
                applicationContext, "??????android.permission.READ_PHONE_STATE",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        // ????????????????????????????????????????????????????????????
        if (requestCode == REQUEST_CODE_GENERAL && resultCode == Activity.RESULT_OK) {
            RecognizeService.recGeneral(
                this,
                FileUtil.getSaveFile(applicationContext).absolutePath
            ) { result -> infoPopText(result) }
        }
        // ????????????????????????????????????????????????????????????????????????
        if (requestCode == REQUEST_CODE_ACCURATE && resultCode == Activity.RESULT_OK) {
            RecognizeService.recAccurate(
                this,
                FileUtil.getSaveFile(applicationContext).absolutePath
            ) { result -> infoPopText(result) }
        }
        // ???????????????????????????????????????
        if (requestCode == REQUEST_CODE_GENERAL_BASIC && resultCode == Activity.RESULT_OK) {
            RecognizeService.recGeneralBasic(
                this,
                FileUtil.getSaveFile(applicationContext).absolutePath
            ) { result -> infoPopText(result) }
        }
        // ?????????????????????????????????????????????????????????
        if (requestCode == REQUEST_CODE_ACCURATE_BASIC && resultCode == Activity.RESULT_OK) {
            RecognizeService.recAccurateBasic(
                this,
                FileUtil.getSaveFile(applicationContext).absolutePath
            ) { result -> infoPopText(result) }
        }
        // ????????????????????????????????????????????????????????????
        if (requestCode == REQUEST_CODE_GENERAL_ENHANCED && resultCode == Activity.RESULT_OK) {
            RecognizeService.recGeneralEnhanced(
                this,
                FileUtil.getSaveFile(applicationContext).absolutePath
            ) { result -> infoPopText(result) }
        }
        // ?????????????????????????????????????????????
        if (requestCode == REQUEST_CODE_GENERAL_WEBIMAGE && resultCode == Activity.RESULT_OK) {
            RecognizeService.recWebimage(
                this,
                FileUtil.getSaveFile(applicationContext).absolutePath
            ) { result -> infoPopText(result) }
        }
        // ????????????????????????????????????
        if (requestCode == REQUEST_CODE_BANKCARD && resultCode == Activity.RESULT_OK) {
            RecognizeService.recBankCard(
                this,
                FileUtil.getSaveFile(applicationContext).absolutePath
            ) { result -> infoPopText(result) }
        }
        // ????????????????????????????????????
        if (requestCode == REQUEST_CODE_VEHICLE_LICENSE && resultCode == Activity.RESULT_OK) {
            RecognizeService.recVehicleLicense(
                this,
                FileUtil.getSaveFile(applicationContext).absolutePath
            ) { result -> infoPopText(result) }
        }
        // ????????????????????????????????????
        if (requestCode == REQUEST_CODE_DRIVING_LICENSE && resultCode == Activity.RESULT_OK) {
            RecognizeService.recDrivingLicense(
                this,
                FileUtil.getSaveFile(applicationContext).absolutePath
            ) { result -> infoPopText(result) }
        }
        // ?????????????????????????????????
        if (requestCode == REQUEST_CODE_LICENSE_PLATE && resultCode == Activity.RESULT_OK) {
            RecognizeService.recLicensePlate(
                this,
                FileUtil.getSaveFile(applicationContext).absolutePath
            ) { result -> infoPopText(result) }
        }
        // ???????????????????????????????????????
        if (requestCode == REQUEST_CODE_BUSINESS_LICENSE && resultCode == Activity.RESULT_OK) {
            RecognizeService.recBusinessLicense(
                this,
                FileUtil.getSaveFile(applicationContext).absolutePath
            ) { result -> infoPopText(result) }
        }
        // ???????????????????????????????????????
        if (requestCode == REQUEST_CODE_RECEIPT && resultCode == Activity.RESULT_OK) {
            RecognizeService.recReceipt(
                this,
                FileUtil.getSaveFile(applicationContext).absolutePath
            ) { result -> infoPopText(result) }
        }
        // ???????????????????????????
        if (requestCode == REQUEST_CODE_PASSPORT && resultCode == Activity.RESULT_OK) {
            RecognizeService.recPassport(
                this,
                FileUtil.getSaveFile(applicationContext).absolutePath
            ) { result -> infoPopText(result) }
        }
        // ??????????????????????????????
        if (requestCode == REQUEST_CODE_QRCODE && resultCode == Activity.RESULT_OK) {
            RecognizeService.recQrcode(
                this,
                FileUtil.getSaveFile(applicationContext).absolutePath
            ) { result -> infoPopText(result) }
        }
        // ???????????????????????????
        if (requestCode == REQUEST_CODE_LOTTERY && resultCode == Activity.RESULT_OK) {
            RecognizeService.recLottery(
                this,
                FileUtil.getSaveFile(applicationContext).absolutePath
            ) { result -> infoPopText(result) }
        }
        // ????????????????????????????????????
        if (requestCode == REQUEST_CODE_VATINVOICE && resultCode == Activity.RESULT_OK) {
            RecognizeService.recVatInvoice(
                this,
                FileUtil.getSaveFile(applicationContext).absolutePath
            ) { result -> infoPopText(result) }
        }
        // ???????????????????????????
        if (requestCode == REQUEST_CODE_NUMBERS && resultCode == Activity.RESULT_OK) {
            RecognizeService.recNumbers(
                this,
                FileUtil.getSaveFile(applicationContext).absolutePath
            ) { result -> infoPopText(result) }
        }
        // ???????????????????????????
        if (requestCode == REQUEST_CODE_HANDWRITING && resultCode == Activity.RESULT_OK) {
            RecognizeService.recHandwriting(
                this,
                FileUtil.getSaveFile(applicationContext).absolutePath
            ) { result -> infoPopText(result) }
        }
        // ???????????????????????????
        if (requestCode == REQUEST_CODE_BUSINESSCARD && resultCode == Activity.RESULT_OK) {
            RecognizeService.recBusinessCard(
                this,
                FileUtil.getSaveFile(applicationContext).absolutePath
            ) { result -> infoPopText(result) }
        }
        // ????????????????????????????????????
        if (requestCode == REQUEST_CODE_CUSTOM && resultCode == Activity.RESULT_OK) {
            RecognizeService.recCustom(
                this,
                FileUtil.getSaveFile(applicationContext).absolutePath
            ) { result -> infoPopText(result) }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // ??????????????????
        OCR.getInstance(this).release()
    }
}
