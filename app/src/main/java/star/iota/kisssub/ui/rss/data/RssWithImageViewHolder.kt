/*
 *
 *  *    Copyright 2017. iota9star
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

package star.iota.kisssub.ui.rss.data

import android.support.v7.app.AppCompatActivity
import android.view.View
import com.github.florent37.glidepalette.BitmapPalette
import com.github.florent37.glidepalette.GlidePalette
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import jp.wasabeef.glide.transformations.CropSquareTransformation
import kotlinx.android.synthetic.main.item_rss_with_image.view.*
import star.iota.kisssub.R
import star.iota.kisssub.base.BaseViewHolder
import star.iota.kisssub.ext.addFragmentToActivity
import star.iota.kisssub.glide.GlideApp
import star.iota.kisssub.glide.GlideOptions
import star.iota.kisssub.room.AppDatabaseHelper
import star.iota.kisssub.room.Record
import star.iota.kisssub.ui.details.DetailsFragment
import star.iota.kisssub.utils.SendUtils
import star.iota.kisssub.utils.ShareUtils
import star.iota.kisssub.utils.ToastUtils
import star.iota.kisssub.widget.MessageBar

class RssWithImageViewHolder(itemView: View) : BaseViewHolder<Record>(itemView) {
    override fun bindView(bean: Record) {
        itemView.apply {
            GlideApp.with(itemView)
                    .load(bean.cover)
                    .listener(GlidePalette.with(bean.cover)
                            .use(BitmapPalette.Profile.VIBRANT_LIGHT)
                            .intoBackground(imageViewCover, BitmapPalette.Swatch.RGB)
                            .crossfade(true))
                    .apply(GlideOptions.bitmapTransform(CropSquareTransformation()))
                    .into(imageViewCover)
            textViewTitle.text = bean.title
            textViewDate.text = bean.date
            textViewCategory.text = bean.category
            textViewSub.text = bean.sub
            textViewSub.setOnClickListener {
                if (!bean.sub.isNullOrBlank()) {
                    (context as AppCompatActivity).addFragmentToActivity(RssFragment.newInstance(bean.sub!!), R.id.frameLayoutContainer)
                }
            }
            textViewTitle.setOnClickListener {
                if (!bean.title.isNullOrBlank() && !bean.url.isNullOrBlank()) {
                    (context as AppCompatActivity).addFragmentToActivity(DetailsFragment.newInstance(bean.title!!, bean.url!!), R.id.frameLayoutContainer)
                }
            }
            textViewDownload.setOnClickListener {
                SendUtils.copy(context!!, bean.title, bean.magnet)
                SendUtils.open(context!!, bean.magnet)
                MessageBar.create(context!!, "已复制到剪切板，并尝试打开本地应用")
            }
            textViewShare.setOnClickListener {
                val content = "\n标题：${bean.title}\n\n" +
                        "分类：${bean.category}\n\n" +
                        "发布时间：${bean.date}\n\n" +
                        "字幕组：${bean.sub}\n\n" +
                        "种子地址：${bean.magnet}\n\n" +
                        "详细地址：${bean.url}\n"
                ShareUtils.share(context, content)
            }
            textViewCollection.setOnClickListener {
                Single.just(AppDatabaseHelper.getInstance(context))
                        .map { it.addRecord(bean) }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            ToastUtils.short(context, "收藏：${bean.title} 成功")
                        }, { ToastUtils.short(context, "错误：${it.message}") })
            }
        }
    }

}