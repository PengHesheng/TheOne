package com.example.a14512.theone.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.a14512.theone.R
import com.example.a14512.theone.model.DateGank
import kotlinx.android.synthetic.main.item_study_recycler.view.*

/**
 * @author 14512 on 2018/6/9
 */
class StudyAdapter : BaseAdapter<RecyclerView.ViewHolder>() {
    private lateinit var mContext: Context
    private lateinit var mListener: OnRecyclerItemClickListener
    private var mDateDatas: ArrayList<DateGank.DateGankData> = ArrayList()
    private var mType = ""

    fun setDatas(dateDatas: ArrayList<DateGank.DateGankData>) {
        mDateDatas = dateDatas
        notifyDataSetChanged()
    }

    fun getData(position: Int): DateGank.DateGankData {
        return mDateDatas[position]
    }

    override fun setOnItemClickListener(listener: BaseClickListener) {
        mListener = listener as OnRecyclerItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        mContext = parent.context
        return StudyHolder(LayoutInflater.from(mContext).inflate(R.layout.item_study_recycler, parent, false))
    }

    override fun getItemCount(): Int {
        return mDateDatas.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is StudyHolder) {
            val data = mDateDatas[position]
            if (mType != data.type) {
                mType = data.type
                holder.tvType.text = mType
                holder.tvType.visibility = View.VISIBLE
            }
            holder.tvTitle.text = data.desc
            holder.tvAuthor.text = data.who
            holder.tvPublishTime.text = data.publishedAt
            if (data.images != null && data.images!!.isNotEmpty()) {
                Glide.with(mContext).load(data.images!![0])
                        .error(Glide.with(mContext).load(R.mipmap.iv_load_failure))
                        .into(holder.iv)
            }
            if (mListener != null) {
                holder.layout.setOnClickListener {
                    mListener.onChildViewClick(it, position)
                }
            }
        }
    }

    class StudyHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        lateinit var tvType: TextView
        lateinit var layout: RelativeLayout
        lateinit var tvTitle: TextView
        lateinit var tvAuthor: TextView
        lateinit var tvPublishTime: TextView
        lateinit var iv: ImageView

        init {
            if (itemView != null) {
                tvType = itemView.tvItemTypeStudy
                layout = itemView.layoutItemStudy
                tvTitle = itemView.tvItemTitleStudy
                tvAuthor = itemView.tvItemAuthorStudy
                tvPublishTime = itemView.tvItemTimeStudy
                iv = itemView.ivItemStudy
            }
        }
    }
}