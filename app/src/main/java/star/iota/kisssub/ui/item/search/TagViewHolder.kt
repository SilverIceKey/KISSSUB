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

package star.iota.kisssub.ui.item.search

import android.support.v7.app.AppCompatActivity
import android.view.View
import kotlinx.android.synthetic.main.item_filter_tag.view.*
import star.iota.kisssub.R
import star.iota.kisssub.base.BaseViewHolder
import star.iota.kisssub.ext.addFragmentToActivity

class TagViewHolder(itemView: View) : BaseViewHolder<FilterBean>(itemView) {
    override fun bindView(bean: FilterBean) {
        itemView.apply {
            textViewTag.text = bean.title
            textViewTag.setOnClickListener {
                if (!bean.url.isNullOrBlank()) {
                    (context as AppCompatActivity).addFragmentToActivity(SearchFragment.newFilterInstance(bean.url!!, SearchHelper.getParam(context)), R.id.frameLayoutContainer)
                }
            }
        }
    }

}