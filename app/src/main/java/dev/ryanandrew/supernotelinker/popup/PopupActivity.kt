package dev.ryanandrew.supernotelinker.popup

import android.app.Activity
import android.os.Bundle
import android.widget.Toast
import dev.ryanandrew.supernotelinker.common.keyword

class PopupActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText(this, keyword, Toast.LENGTH_LONG).show()
        finish()
    }
}
