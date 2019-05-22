package com.wechantloup.side.modules.details

import android.content.Intent
import com.wechantloup.side.R
import com.wechantloup.side.modules.core.BaseRouter

class DetailsRouter: BaseRouter(), DetailsContract.Router {

    override fun share(view: DetailsContract.View, text: String) {
        val sharingIntent = Intent(Intent.ACTION_SEND)
        sharingIntent.type = "text/plain"
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text)
        getActivity(view)?.startActivity(
            Intent.createChooser(
                sharingIntent,
                getActivity(view)?.getString(R.string.share_with)
            )
        )
    }
}