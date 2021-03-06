package star.iota.kisssub.ui.details

import com.lzy.okgo.OkGo
import com.lzy.okgo.convert.StringConvert
import com.lzy.okgo.model.Response
import com.lzy.okrx2.adapter.ObservableResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup
import star.iota.kisssub.KisssubUrl

class DetailsPresenter(private val view: DetailsContract.View) : DetailsContract.Presenter {
    override fun get(url: String) {
        compositeDisposable.add(
                OkGo.get<String>(url)
                        .converter(StringConvert())
                        .adapt(ObservableResponse<String>())
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.computation())
                        .map { deal(it) }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            if (it == null) {
                                view.noData()
                            } else {
                                view.success(it)
                            }
                        }, {
                            view.error(it?.message)
                        })
        )
    }

    private fun deal(resp: Response<String>): DetailsBean? {
        val doc = Jsoup.parse(resp.body())?.select("#btm > div.main > div.slayout > div > div.c2")
        val bean = DetailsBean()
        val details = doc?.select("div:nth-child(1) > div.intro")?.html()?.replace("<br>", "")
        val tags = ArrayList<String>()
        doc?.select("div:nth-child(4) > div > a")?.forEach {
            tags.add(it.text())
        }
        val magnet = doc?.select("#magnet")?.attr("href")
        val torrent = KisssubUrl.BASE + doc?.select("#download")?.attr("href")

        val desc = doc?.select("div:nth-child(6) > h2 > span.right.text_normal")?.text()
        doc?.select("div:nth-child(6) > div.torrent_files")?.select("img")?.remove()
        val tree = doc?.select("div:nth-child(6) > div.torrent_files")?.html()
        bean.desc = desc
        bean.details = details
        bean.tree = tree
        bean.magnet = magnet
        bean.tags = tags
        bean.torrent = torrent
        return bean
    }

    companion object {
        private val compositeDisposable = CompositeDisposable()
    }

    override fun unsubscribe() {
        compositeDisposable.clear()
    }
}