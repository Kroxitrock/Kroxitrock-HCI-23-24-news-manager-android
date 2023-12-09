package es.upm.reader.news

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        loadBackButton()
    }
    private fun loadBackButton() {
        findViewById<ImageView>(R.id.back_img).setOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}