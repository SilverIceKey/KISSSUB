package star.iota.kisssub.ui.selector.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.github.florent37.glidepalette.BitmapPalette
import com.github.florent37.glidepalette.GlidePalette
import jp.wasabeef.glide.transformations.CropSquareTransformation
import star.iota.kisssub.R
import star.iota.kisssub.glide.GlideApp
import star.iota.kisssub.glide.GlideOptions


class PhotoAdapter(val context: Context, private val photos: ArrayList<String>) : RecyclerView.Adapter<PhotoAdapter.ViewHolder>() {


    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(inflater.inflate(R.layout.item_photo_selector_preview_photo, parent, false))
    }

    private var onPhotoSelected: OnPhotoSelected? = null

    interface OnPhotoSelected {
        fun selected(position: Int)
    }

    fun setOnPhotoSelected(onPhotoSelected: OnPhotoSelected) {
        this.onPhotoSelected = onPhotoSelected
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val filePath = photos[position]
        GlideApp.with(context)
                .load(filePath)
                .listener(GlidePalette.with(filePath)
                        .use(BitmapPalette.Profile.VIBRANT_LIGHT)
                        .intoBackground(holder.image, BitmapPalette.Swatch.RGB)
                        .crossfade(true))
                .apply(GlideOptions.bitmapTransform(CropSquareTransformation()))
                .into(holder.image)
        holder.image.setOnClickListener {
            onPhotoSelected?.selected(position)
        }

    }

    override fun getItemCount(): Int = photos.size

    class ViewHolder(
            itemView: View,
            val image: ImageView = itemView.findViewById(R.id.image_view_photo)
    ) : RecyclerView.ViewHolder(itemView)
}