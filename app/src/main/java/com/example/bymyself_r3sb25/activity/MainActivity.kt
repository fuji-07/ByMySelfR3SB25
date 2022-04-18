package com.example.bymyself_r3sb25.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.example.bymyself_r3sb25.R
import com.example.bymyself_r3sb25.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = PreferenceManager.getDefaultSharedPreferences(this)
        val name = pref.getString("name", "名無しさん")
        binding.nameInput.setText(name)

        binding.r3saButton.setOnClickListener { onClassButtonTapped(it) }
        binding.r3sbButton.setOnClickListener { onClassButtonTapped(it) }

        binding.nameSetButton.setOnClickListener {
            val pref = PreferenceManager.getDefaultSharedPreferences(this)
            val name = binding.nameInput.text.toString()
            pref.edit {
                putString("name", name)

            }

            // トースト通知
            val text = "名前が保存されました"
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(applicationContext, text, duration)
            toast.show()
        }

        var idx = 0
        val schoolImageList = listOf<Int>(
            R.drawable.school_spring,
            R.drawable.school_summer,
            R.drawable.school_fall,
            R.drawable.school_winter
        )
        binding.schoolImage.setOnClickListener {
            binding.schoolImage.setImageResource(schoolImageList[idx])
            idx = (idx + 1) % schoolImageList.size
        }

    }

    private fun onClassButtonTapped(view: View?) {//画面遷移
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("class", view?.id)
        startActivity(intent)
    }
}