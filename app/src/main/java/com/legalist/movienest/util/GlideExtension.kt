package com.legalist.movienest.util

import android.provider.SyncStateContract
import android.widget.ImageView
import com.legalist.data.R
import com.legalist.data.util.Constant
import com.squareup.picasso.Picasso

fun ImageView.loadCircleImage(path: String?) {

        Picasso.get()
            .load(path)
            .error(com.legalist.movienest.R.drawable.close)
            .fit()
            .into(this)
    }


