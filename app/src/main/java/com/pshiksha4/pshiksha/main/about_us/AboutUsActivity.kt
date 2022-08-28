package com.pshiksha4.pshiksha.main.about_us

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import mehdi.sakout.aboutpage.AboutPage
import com.pshiksha4.pshiksha.R
import com.pshiksha4.pshiksha.databinding.ActivityAboutUsBinding

class AboutUsActivity : AppCompatActivity() {
    var binding: ActivityAboutUsBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        supportActionBar!!.hide()
        val aboutPage = AboutPage(this)
            .isRTL(false)
            .setImage(R.drawable.logo)
            .setDescription(getString(R.string.pshiksha_description))
            .addGroup("Connect with us")
            .addEmail(getString(R.string.pshiksha_email_id))
            .addWebsite(getString(R.string.pshiksha_website))
            .addYoutube("UCzi3OWdA5YBLdYoqh5_ZTsA")
            .addPlayStore(packageName)
            .create()
        binding!!.linearLayout.addView(aboutPage)
        binding!!.homeUpButton.setOnClickListener { onBackPressed() }
    }
}