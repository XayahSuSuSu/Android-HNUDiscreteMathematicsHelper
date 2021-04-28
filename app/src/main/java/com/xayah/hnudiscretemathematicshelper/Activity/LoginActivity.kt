package com.xayah.hnudiscretemathematicshelper.Activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.webkit.WebView
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.textfield.TextInputEditText
import com.xayah.hnudiscretemathematicshelper.Component.NetImageView
import com.xayah.hnudiscretemathematicshelper.R
import com.xayah.hnudiscretemathematicshelper.Util.DataUtil
import com.xayah.hnudiscretemathematicshelper.Util.DialogUtil
import com.xayah.hnudiscretemathematicshelper.Util.NetUtil
import org.json.JSONException


class LoginActivity : AppCompatActivity() {
    lateinit var editor: SharedPreferences.Editor
    lateinit var schoolnodeptno: String
    lateinit var imageUrl: String
    lateinit var cookie: String
    lateinit var verifycodeint: String

    lateinit var prefs: SharedPreferences

    val dialogUtil = DialogUtil(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
    }

    @SuppressLint("CommitPrefEdits")
    private fun init() {
        editor = getSharedPreferences("login", Context.MODE_PRIVATE).edit()
        prefs = getSharedPreferences("login", Context.MODE_PRIVATE)
        //        保存本机UserAgent
        editor.putString("userAgent", WebView(this).getSettings().getUserAgentString())
        editor.apply()
//---------------------------------------------------------------------------------------------------
        val login_navigationView: NavigationView = findViewById(R.id.login_navigationView)
        val login_drawer_layout: DrawerLayout = findViewById(R.id.login_drawer_layout)
        val navigationview_head = login_navigationView.getHeaderView(0)
        val navigationview_head_textView_version: TextView =
            navigationview_head.findViewById(R.id.navigationview_head_textView_version)
        navigationview_head_textView_version.text = "HNU离散数学助手 v" + DataUtil.getVersion(this)
        login_navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_website_system -> {
                    val uri = Uri.parse("http://server.wuyou.com.cn/hnuysh/")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
                R.id.item_website_AcmeZone -> {
                    val uri = Uri.parse("http://acmezone.tk/")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
                R.id.item_about_app -> {

                }
                R.id.item_about_author -> {

                }
                R.id.item_quit -> {
                    finish()
                }
            }
            false
        }

        val login_imageView_menu: ImageButton = findViewById(R.id.login_imageView_menu)
        login_imageView_menu.setOnClickListener {
            login_drawer_layout.openDrawer(login_navigationView)
        }
//---------------------------------------------------------------------------------------------------

        val checkBox_rememberPwd: CheckBox = findViewById(R.id.checkBox_rememberPwd)
        val checkBox_autoLogin: CheckBox = findViewById(R.id.checkBox_autoLogin)
        val checkBox_autoFillCode: CheckBox = findViewById(R.id.checkBox_autoFillCode)
        val textInputEditText_uname: TextInputEditText = findViewById(R.id.textInputEditText_uname)
        val textInputEditText_pwd: TextInputEditText = findViewById(R.id.textInputEditText_pwd)
        val textInputEditText_code: TextInputEditText = findViewById(R.id.textInputEditText_code)
        val floatingActionButton_login: ExtendedFloatingActionButton =
            findViewById(R.id.floatingActionButton_login)
        val imageView_code: NetImageView = findViewById(R.id.imageView_code)

        Thread {
            try {
                schoolnodeptno =
                    NetUtil.getSchoolnodeptno(prefs.getString("userAgent", "")!!)
                val getImageJSON = NetUtil.getImage(prefs.getString("userAgent", "")!!)
                imageUrl = getImageJSON.getString("url")
                cookie = getImageJSON.getString("SessionId")
                verifycodeint = getImageJSON.getString("verifycodeint")
                Log.d("mTAG", "init: " + schoolnodeptno)
                Log.d("mTAG", "init: " + imageUrl)
                Log.d("mTAG", "init: " + cookie)
                Log.d("mTAG", "init: " + verifycodeint)
                runOnUiThread {
                    imageView_code.setImageURL(imageUrl)
                    if (prefs.getBoolean("isAutoFillCode", false)) {
                        textInputEditText_code.setText(DataUtil.getVerifyCode(verifycodeint))
                    }
                    if (prefs.getBoolean("isAutoLogin", false)) {
                        Thread {
                            try {
                                val mReturnBody = NetUtil.login(
                                    textInputEditText_uname.text.toString(),
                                    textInputEditText_pwd.text.toString(),
                                    textInputEditText_code.text.toString(),
                                    verifycodeint,
                                    schoolnodeptno,
                                    prefs.getString("userAgent", "")!!,
                                    cookie
                                )
                                Log.d("mTAG", "init: " + mReturnBody)
                                if (mReturnBody.contains("密码错")) {
                                    runOnUiThread {
                                        dialogUtil.createPositiveButtonDialog("密码错误!", "好的", {})
                                    }
                                } else if (mReturnBody.contains("不存在")) {
                                    runOnUiThread {
                                        dialogUtil.createPositiveButtonDialog("账号不存在!", "好的", {})
                                    }
                                } else if (mReturnBody.contains("校验码不对")) {
                                    runOnUiThread {
                                        dialogUtil.createPositiveButtonDialog("验证码错误!", "好的", {})
                                    }
                                } else if (mReturnBody.contains("username")) {
                                    if (checkBox_rememberPwd.isChecked) {
                                        editor.putString(
                                            "pwd",
                                            textInputEditText_pwd.text.toString()
                                        )
                                        editor.putBoolean("isRemembered", true)
                                        editor.apply()
                                    } else {
                                        editor.putString("pwd", "")
                                        editor.putBoolean("isRemembered", false)
                                        editor.apply()
                                    }
                                    if (checkBox_autoLogin.isChecked) {
                                        editor.putBoolean("isRemembered", true)
                                        editor.putBoolean("isAutoLogin", true)
                                        editor.putBoolean("isAutoFillCode", true)
                                        editor.apply()
                                    } else {
                                        editor.putBoolean("isAutoLogin", false)
                                        editor.apply()
                                    }
                                    if (checkBox_autoFillCode.isChecked) {
                                        editor.putBoolean("isAutoFillCode", true)
                                        editor.apply()
                                    } else {
                                        editor.putBoolean("isAutoFillCode", false)
                                        editor.apply()
                                    }
                                    runOnUiThread {
                                        dialogUtil.createPositiveButtonDialog("登录成功!", "好的") {
                                            val intent =
                                                Intent(this, MainActivity::class.java)
                                            val returnArr = mReturnBody.split("`")
                                            for (item in returnArr) {
                                                if (item.contains("zh"))
                                                    intent.putExtra("zh", item.split(":")[1])
                                                if (item.contains("rolename"))
                                                    intent.putExtra(
                                                        "rolename",
                                                        item.split(":")[1]
                                                    )
                                                if (item.contains("username"))
                                                    intent.putExtra(
                                                        "username",
                                                        item.split(":")[1]
                                                    )
                                                if (item.contains("schoolno"))
                                                    intent.putExtra(
                                                        "schoolno",
                                                        item.split(":")[1]
                                                    )
                                                if (item.contains("schoolname"))
                                                    intent.putExtra(
                                                        "schoolname",
                                                        item.split(":")[1]
                                                    )
                                                if (item.contains("ip"))
                                                    intent.putExtra("ip", item.split(":")[1])
                                                if (item.contains("paperplannoList"))
                                                    intent.putExtra(
                                                        "paperplannoList",
                                                        item.split(":")[1]
                                                    )
                                            }
                                            intent.putExtra(
                                                "userAgent",
                                                prefs.getString("userAgent", "")!!
                                            )
                                            intent.putExtra("cookie", cookie)
                                            startActivity(intent)
                                            finish()
                                        }
                                    }
                                } else {
                                    runOnUiThread {
                                        dialogUtil.createPositiveButtonDialog("未知错误!", "好的", {})
                                    }
                                }
                            } catch (e: JSONException) {
                                e.printStackTrace()
                            }
                        }.start()
                    }
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }.start()
        textInputEditText_uname.setText(prefs.getString("uname", ""))
        textInputEditText_pwd.setText(prefs.getString("pwd", ""))
        checkBox_rememberPwd.isChecked = prefs.getBoolean("isRemembered", false)
        checkBox_autoLogin.isChecked = prefs.getBoolean("isAutoLogin", false)
        checkBox_autoFillCode.isChecked = prefs.getBoolean("isAutoFillCode", false)

        textInputEditText_uname.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && textInputEditText_uname.text.toString() != "")
                editor.putString("uname", textInputEditText_uname.text.toString())
        }
        checkBox_autoLogin.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBox_rememberPwd.isChecked = true
                checkBox_autoFillCode.isChecked = true
            }
        }
        checkBox_autoFillCode.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                textInputEditText_code.isEnabled = false
                textInputEditText_code.setText(DataUtil.getVerifyCode(verifycodeint))
            } else {
                textInputEditText_code.isEnabled = true
            }
        }
        floatingActionButton_login.setOnClickListener {
            Thread {
                try {
                    val mReturnBody = NetUtil.login(
                        textInputEditText_uname.text.toString(),
                        textInputEditText_pwd.text.toString(),
                        textInputEditText_code.text.toString(),
                        verifycodeint,
                        schoolnodeptno,
                        prefs.getString("userAgent", "")!!,
                        cookie
                    )
                    Log.d("mTAG", "init: " + mReturnBody)
                    if (mReturnBody.contains("密码错")) {
                        runOnUiThread {
                            dialogUtil.createPositiveButtonDialog("密码错误!", "好的", {})
                        }
                    } else if (mReturnBody.contains("不存在")) {
                        runOnUiThread {
                            dialogUtil.createPositiveButtonDialog("账号不存在!", "好的", {})
                        }
                    } else if (mReturnBody.contains("校验码不对")) {
                        runOnUiThread {
                            dialogUtil.createPositiveButtonDialog("验证码错误!", "好的", {})
                        }
                    } else if (mReturnBody.contains("username")) {
                        if (checkBox_rememberPwd.isChecked) {
                            editor.putString("pwd", textInputEditText_pwd.text.toString())
                            editor.putBoolean("isRemembered", true)
                            editor.apply()
                        } else {
                            editor.putString("pwd", "")
                            editor.putBoolean("isRemembered", false)
                            editor.apply()
                        }
                        if (checkBox_autoLogin.isChecked) {
                            editor.putBoolean("isRemembered", true)
                            editor.putBoolean("isAutoLogin", true)
                            editor.putBoolean("isAutoFillCode", true)
                            editor.apply()
                        } else {
                            editor.putBoolean("isAutoLogin", false)
                            editor.apply()
                        }
                        if (checkBox_autoFillCode.isChecked) {
                            editor.putBoolean("isAutoFillCode", true)
                            editor.apply()
                        } else {
                            editor.putBoolean("isAutoFillCode", false)
                            editor.apply()
                        }
                        runOnUiThread {
                            dialogUtil.createPositiveButtonDialog("登录成功!", "好的") {
                                val intent =
                                    Intent(this, MainActivity::class.java)
                                val returnArr = mReturnBody.split("`")
                                for (item in returnArr) {
                                    if (item.contains("zh"))
                                        intent.putExtra("zh", item.split(":")[1])
                                    if (item.contains("rolename"))
                                        intent.putExtra("rolename", item.split(":")[1])
                                    if (item.contains("username"))
                                        intent.putExtra("username", item.split(":")[1])
                                    if (item.contains("schoolno"))
                                        intent.putExtra("schoolno", item.split(":")[1])
                                    if (item.contains("schoolname"))
                                        intent.putExtra("schoolname", item.split(":")[1])
                                    if (item.contains("ip"))
                                        intent.putExtra("ip", item.split(":")[1])
                                    if (item.contains("paperplannoList"))
                                        intent.putExtra("paperplannoList", item.split(":")[1])
                                }
                                intent.putExtra("userAgent", prefs.getString("userAgent", "")!!)
                                intent.putExtra("cookie", cookie)
                                startActivity(intent)
                                finish()
                            }
                        }
                    } else {
                        runOnUiThread {
                            dialogUtil.createPositiveButtonDialog("未知错误!", "好的", {})
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }.start()
        }
    }
}