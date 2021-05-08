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
    // -------------------Component-------------------
    lateinit var login_navigationView: NavigationView
    lateinit var login_drawer_layout: DrawerLayout
    lateinit var navigationview_head_textView_version: TextView
    lateinit var login_imageView_menu: ImageButton
    lateinit var checkBox_rememberPwd: CheckBox
    lateinit var checkBox_autoLogin: CheckBox
    lateinit var checkBox_autoFillCode: CheckBox
    lateinit var textInputEditText_uname: TextInputEditText
    lateinit var textInputEditText_pwd: TextInputEditText
    lateinit var textInputEditText_code: TextInputEditText
    lateinit var floatingActionButton_login: ExtendedFloatingActionButton
    lateinit var imageView_code: NetImageView

    // -------------------Var-------------------
    lateinit var schoolnodeptno: String
    lateinit var imageUrl: String
    lateinit var cookie: String
    lateinit var verifycodeint: String

    // -------------------Utils-------------------
    lateinit var editor: SharedPreferences.Editor
    lateinit var prefs: SharedPreferences
    val dialogUtil = DialogUtil(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        bindView() // 绑定组件
        init() // 初始化
        setListener() // 设置监听器
    }

    private fun bindView() {
        login_navigationView = findViewById(R.id.login_navigationView)
        login_drawer_layout = findViewById(R.id.login_drawer_layout)
        val navigationview_head = login_navigationView.getHeaderView(0)
        navigationview_head_textView_version =
            navigationview_head.findViewById(R.id.navigationview_head_textView_version)
        login_imageView_menu = findViewById(R.id.login_imageView_menu)
        checkBox_rememberPwd = findViewById(R.id.checkBox_rememberPwd)
        checkBox_autoLogin = findViewById(R.id.checkBox_autoLogin)
        checkBox_autoFillCode = findViewById(R.id.checkBox_autoFillCode)
        textInputEditText_uname = findViewById(R.id.textInputEditText_uname)
        textInputEditText_pwd = findViewById(R.id.textInputEditText_pwd)
        textInputEditText_code = findViewById(R.id.textInputEditText_code)
        floatingActionButton_login = findViewById(R.id.floatingActionButton_login)
        imageView_code = findViewById(R.id.imageView_code)
    } // 绑定组件

    private fun setListener() {
        // 侧滑栏子项单击事件
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
                    val uri =
                        Uri.parse("https://github.com/XayahSuSuSu/Android-HNUDiscreteMathematicsHelper")
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    startActivity(intent)
                }
                R.id.item_about_author -> {
                    val intent =
                        Intent(this, AboutAuthorActivity::class.java)
                    startActivity(intent)
                }
                R.id.item_quit -> {
                    finish()
                }
            }
            false
        }
        // 左上角菜单按钮单击事件
        login_imageView_menu.setOnClickListener {
            login_drawer_layout.openDrawer(login_navigationView)
        }
        // 账号编辑框失焦保存信息
        textInputEditText_uname.setOnFocusChangeListener { v, hasFocus ->
            if (!hasFocus && textInputEditText_uname.text.toString() != "")
                editor.putString("uname", textInputEditText_uname.text.toString())
        }
        // 自动登录单选框保存信息
        checkBox_autoLogin.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkBox_rememberPwd.isChecked = true
                checkBox_autoFillCode.isChecked = true
            }
        }
        // 自动填写验证码单选框保存信息
        checkBox_autoFillCode.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                textInputEditText_code.isEnabled = false
                textInputEditText_code.setText(DataUtil.getVerifyCode(verifycodeint))
            } else {
                textInputEditText_code.isEnabled = true
            }
        }
        // 登录事件
        floatingActionButton_login.setOnClickListener {
            loginEvent() // 登录事件
        }
    } // 设置监听器

    private fun loginEvent() {
        dialogUtil.createProgressDialog {
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
                            it.dismiss()
                            dialogUtil.createPositiveButtonDialog("密码错误!", "好的", {})
                        }
                    } else if (mReturnBody.contains("不存在")) {
                        runOnUiThread {
                            it.dismiss()
                            dialogUtil.createPositiveButtonDialog("账号不存在!", "好的", {})
                        }
                    } else if (mReturnBody.contains("校验码不对")) {
                        runOnUiThread {
                            it.dismiss()
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
                            it.dismiss()
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
                            it.dismiss()
                            dialogUtil.createPositiveButtonDialog("未知错误!", "好的", {})
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }.start()
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun init() {
        // 初始化SharedPreferences
        editor = getSharedPreferences("login", Context.MODE_PRIVATE).edit()
        prefs = getSharedPreferences("login", Context.MODE_PRIVATE)
        // 保存本机UserAgent
        editor.putString("userAgent", WebView(this).getSettings().getUserAgentString())
        editor.apply()
        // 侧滑栏版本获取
        navigationview_head_textView_version.text = "HNU离散数学助手 v" + DataUtil.getVersion(this)
        // 自动登录
        dialogUtil.createProgressDialog {
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
                    it.dismiss()
                    runOnUiThread {
                        dialogUtil.createProgressDialog{
                            imageView_code.setImageURL(imageUrl)
                            it.dismiss()
                        }
                        if (prefs.getBoolean("isAutoFillCode", false)) {
                            textInputEditText_code.setText(DataUtil.getVerifyCode(verifycodeint))
                        }
                        val isLogOut = intent.getBooleanExtra("isLogOut", false)
                        if (prefs.getBoolean("isAutoLogin", false) && !isLogOut) {
                            loginEvent()
                        }
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }.start()
        }
        // 展示储存的账号信息
        textInputEditText_uname.setText(prefs.getString("uname", ""))
        textInputEditText_pwd.setText(prefs.getString("pwd", ""))
        checkBox_rememberPwd.isChecked = prefs.getBoolean("isRemembered", false)
        checkBox_autoLogin.isChecked = prefs.getBoolean("isAutoLogin", false)
        checkBox_autoFillCode.isChecked = prefs.getBoolean("isAutoFillCode", false)

    } // 初始化
}