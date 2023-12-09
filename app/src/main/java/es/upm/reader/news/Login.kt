package es.upm.reader.news

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import es.upm.reader.news.serice.LoginService
import kotlinx.coroutines.launch


class Login : AppCompatActivity() {
    var username: EditText? = null
    var password:EditText? = null
    var loginBtn:Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loadBackButton()


        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        loginBtn = findViewById(R.id.login)

        username?.addTextChangedListener(LoginWatcher)
        password?.addTextChangedListener(LoginWatcher)
        loginBtn?.setOnClickListener{
            login()
        }
    }


        val LoginWatcher: TextWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                val userName: String = username?.text.toString()
                val userPass: String = password?.text.toString()
               loginBtn?.isEnabled = userName.isNotEmpty() && userPass.isNotEmpty()
            }

            override fun afterTextChanged(editable: Editable) {}
        }

    private fun loadBackButton() {
        findViewById<ImageView>(R.id.back_img).setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    private fun login() {
        lifecycleScope.launch {
            try{
                LoginService.login(username?.text.toString(), password?.text.toString())
                setResult(RESULT_OK)
                finish()
            } catch (ex: Exception) {
                Toast.makeText(this@Login, "Wrong username or password",
                    Toast.LENGTH_LONG).show();
            }
        }
    }
}