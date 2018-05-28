package com.example.a14512.theone.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.a14512.theone.R
import com.example.a14512.theone.base.BaseFragment

/**
* @author 14512 on 2018/5/21
*/
class ContactFragment: BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_contact, container, false)
    }

    override fun initView(view: View) {

    }
}