package mlpt.siemo.digitalbanking

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.layout_item.*
import mlpt.siemo.digitalbanking.base.BaseRecyclerViewAdapter
import mlpt.siemo.digitalbanking.base.MainItem

class MainAdapter(
    private val listener: (MainItem) -> Unit
) : BaseRecyclerViewAdapter<MainAdapter.VHolder, MainItem>() {

    override fun onBindViewHolder(holder: VHolder, item: MainItem, position: Int) {
        holder.binding(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VHolder =
        VHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.layout_item, parent, false),
            listener
        )

    class VHolder(
        override val containerView: View,
        private val listener: (MainItem) -> Unit
    ) : LayoutContainer, RecyclerView.ViewHolder(containerView) {

        fun binding(data: MainItem) {
            tv_title.text = data.title
            containerView.setOnClickListener { listener(data) }
        }
    }
}