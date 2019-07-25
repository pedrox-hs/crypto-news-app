package com.pedrenrique.cryptonews.core.ext

import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.pedrenrique.cryptonews.features.MainActivity

val Fragment.supportActivity: MainActivity?
    get() = activity as? MainActivity

val Fragment.supportActionBar: ActionBar?
    get() = supportActivity?.supportActionBar