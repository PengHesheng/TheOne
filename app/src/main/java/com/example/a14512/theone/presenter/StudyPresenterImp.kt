package com.example.a14512.theone.presenter

import android.content.Context
import com.example.a14512.theone.model.DateGank
import com.example.a14512.theone.network.RetrofitManager
import com.example.a14512.theone.utils.PLog
import com.example.a14512.theone.utils.TimeUtil
import com.example.a14512.theone.view.IStudyView
import com.example.a14512.weatherkotlin.network.RxUtil.ApiObserver

/**
 * @author 14512 on 2018/6/9
 */
class StudyPresenterImp(private val mContext: Context,
                        private val mView: IStudyView) : IStudyPresenter {
    override fun getDateGank() {
        val date = TimeUtil.getOldYMD("yyyy/MM/dd", -1)
        PLog.e(date)
        val apiObserver = object : ApiObserver<DateGank>(mContext, true, false) {
            override fun onNext(t: DateGank) {
                PLog.e(t.category.toString())
            }
        }
        RetrofitManager.getInstance().getDateGank(date).subscribe(apiObserver)

    }
}