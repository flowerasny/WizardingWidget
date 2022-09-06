package com.flowerasny.widget

import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.flowerasny.widget.dogs.ElixirDifficulty
import com.flowerasny.widget.dogs.ElixirsRepository
import kotlinx.coroutines.runBlocking

class MyAppWidgetService : RemoteViewsService() {

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return ItemRemoteViewFactory(applicationContext, ElixirsRepository())
    }

}

class ItemRemoteViewFactory(
    private val context: Context,
    private val dogsRepository: ElixirsRepository,
) : RemoteViewsService.RemoteViewsFactory {

    private var items: List<SingleItem> = emptyList()

    override fun onCreate() {
        runBlocking {
            items = dogsRepository.getElixirs().map {
                SingleItem(
                    title = it.name,
                    subtitle = "Difficulty: ${it.difficulty.toDifficultyString()}",
                )
            }
        }
    }

    override fun getViewAt(position: Int): RemoteViews {
        return RemoteViews(context.packageName, R.layout.widget_item).apply {
            setTextViewText(R.id.tvTitle, items[position].title)
            setTextViewText(R.id.tvSubtitle, items[position].subtitle)
        }
    }

    override fun onDataSetChanged() {
    }

    override fun onDestroy() {
        items = emptyList()
    }

    override fun getCount(): Int = items.size

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(position: Int): Long = position.toLong()

    override fun hasStableIds(): Boolean = true

}

data class SingleItem(
    val title: String,
    val subtitle: String,
)

fun ElixirDifficulty.toDifficultyString(): String = when(this) {
    ElixirDifficulty.Unknown -> "❓"
    ElixirDifficulty.Beginner -> "\uD83E\uDDEA".repeat(1)
    ElixirDifficulty.Moderate -> "\uD83E\uDDEA".repeat(2)
    ElixirDifficulty.Advanced -> "\uD83E\uDDEA".repeat(3)
    ElixirDifficulty.OrdinaryWizardingLevel -> "\uD83E\uDDEA".repeat(4)
    ElixirDifficulty.OneOfAKind -> "\uD83E\uDDEA".repeat(5)
}
