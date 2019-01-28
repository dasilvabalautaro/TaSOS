package com.globalhiddenodds.tasos.extension

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import com.globalhiddenodds.tasos.R

fun addDecorationRecycler(rv: RecyclerView, context: Context){
    val horizontalDecoration =
            DividerItemDecoration(rv.context,
                    DividerItemDecoration.VERTICAL)
    val horizontalDivider: Drawable = context
            .getDrawable(R.drawable.horizontal_divider)
    horizontalDecoration.setDrawable(horizontalDivider)
    rv.addItemDecoration(horizontalDecoration)
}