package star.iota.kisssub.ui.history

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import star.iota.kisssub.R

class HistoryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var list: ArrayList<HistoryBean> = ArrayList()
    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder
            = HistoryViewHolder(LayoutInflater.from(parent.context)
            .inflate(R.layout.item_history, parent, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder?, position: Int) {
        (holder as HistoryViewHolder).bindView(list[position])
    }

    fun addAll(list: ArrayList<HistoryBean>) {
        val size = this.list.size
        this.list.addAll(list)
        notifyItemRangeInserted(size, list.size)
    }

    fun clear() {
        val size = list.size
        list.clear()
        notifyItemRangeRemoved(0, size)
    }

}