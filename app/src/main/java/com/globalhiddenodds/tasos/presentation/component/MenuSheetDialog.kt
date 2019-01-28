package com.globalhiddenodds.tasos.presentation.component

import android.content.Context
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import com.globalhiddenodds.tasos.R
import com.globalhiddenodds.tasos.presentation.interfaces.OptionsMenuSheet

class MenuSheetDialog: BottomSheetDialogFragment() {
    companion object {
        fun newInstance(): MenuSheetDialog = MenuSheetDialog().apply {  }
    }

    private val deleteAllMessages = 1
    private val toolsConfiguration = 2
    private lateinit var optionsMenuSheet : OptionsMenuSheet

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.view_bottom_sheet,
                container, false)
        view!!.findViewById<ImageButton>(R.id.ib_del_all).setOnClickListener(onClickListener)
        view.findViewById<ImageButton>(R.id.ib_tools).setOnClickListener(onClickListener)
        return  view

    }

    private val onClickListener = View.OnClickListener {
        view -> when(view.id){
            R.id.ib_del_all -> {
                optionsMenuSheet.callExecuteOptions(deleteAllMessages)
                dismiss()
            }
            R.id.ib_tools -> {
                optionsMenuSheet.callExecuteOptions(toolsConfiguration)
                dismiss()
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        optionsMenuSheet = context as OptionsMenuSheet
    }
}