package star.iota.kisssub.ui.selector.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.load.MultiTransformation
import jp.wasabeef.glide.transformations.BlurTransformation
import jp.wasabeef.glide.transformations.CropCircleTransformation
import star.iota.kisssub.R
import star.iota.kisssub.glide.GlideApp
import star.iota.kisssub.glide.GlideOptions.bitmapTransform
import star.iota.kisssub.ui.selector.bean.FolderBean


class DirAdapter : RecyclerView.Adapter<DirAdapter.ViewHolder>() {

    private var folders: ArrayList<FolderBean> = ArrayList()

    private var onDirSelectedListener: OnDirSelectedListener? = null

    interface OnDirSelectedListener {
        fun selected(folder: FolderBean)
    }

    fun setOnDirSelectedListener(onDirSelectedListener: OnDirSelectedListener) {
        this.onDirSelectedListener = onDirSelectedListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_photo_selector_dir, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val folder = folders[position]
        val multi = MultiTransformation(
                BlurTransformation(25),
                CropCircleTransformation())
        GlideApp.with(holder.context)
                .load(folder.firstImgPath)
                .apply(bitmapTransform(multi))
                .into(holder.image)
        holder.dir.text = folder.dir
        holder.count.text = folder.count.toString()
        holder.root.setOnClickListener {
            onDirSelectedListener?.selected(folder)
        }
    }

    override fun getItemCount(): Int {
        return folders.size
    }

    fun update(folders: ArrayList<FolderBean>) {
        this.folders = folders
        notifyDataSetChanged()
    }

    class ViewHolder(
            itemView: View,
            val context: Context = itemView.context,
            val image: ImageView = itemView.findViewById(R.id.image_view_photo_cover),
            val dir: TextView = itemView.findViewById(R.id.text_view_dir_name),
            val count: TextView = itemView.findViewById(R.id.text_view_photo_count),
            val root: LinearLayout = itemView.findViewById(R.id.linear_layout_item_root)
    ) : RecyclerView.ViewHolder(itemView)
}
