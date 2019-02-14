package sz.co.seke.seke

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import sz.co.seke.seke.dummy.DummyContent
import kotlinx.android.synthetic.main.activity_receit_list.*
import kotlinx.android.synthetic.main.receit_list_content.view.*
import kotlinx.android.synthetic.main.receit_list.*
import org.json.JSONArray
import java.util.*

/**
 * An activity representing a list of Pings. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a [ReceitDetailActivity] representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
class ReceitListActivity : AppCompatActivity() {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private var twoPane: Boolean = false

    private val receiptDates = arrayListOf<String>()
    fun loadReceipts() {
        val datesString = PreferenceManager
            .getDefaultSharedPreferences(this)
            .getString("dates", "")
        if (datesString.length > 0) {
            val _dates = datesString.split(",")
            _dates.forEach {
                receiptDates.add(it)
            }
        }
    }

    fun saveReceipts(_date: String, _items: String) {
        receiptDates.add(_date)
        PreferenceManager
            .getDefaultSharedPreferences(this)
            .edit()
            .putString("dates", receiptDates.reduce { acc, s: String -> "$s,$acc" })
            .putString(_date, _items)
            .apply()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_receit_list)

        val newReceiptString = intent.getStringExtra("receipt")

        loadReceipts()

        if (!newReceiptString.isNullOrEmpty()) {
            val its = JSONArray(newReceiptString)
            val receiptDate = Date().toString()
            saveReceipts(receiptDate, newReceiptString)
        }

        setSupportActionBar(toolbar)
        toolbar.title = title

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        if (receit_detail_container != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            twoPane = true
        }

        setupRecyclerView(receit_list)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = SimpleItemRecyclerViewAdapter(this, receiptDates, twoPane)
    }

    class SimpleItemRecyclerViewAdapter(
        private val parentActivity: ReceitListActivity,
        private val values: List<String>,
        private val twoPane: Boolean
    ) :
        RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {

        private val onClickListener: View.OnClickListener

        init {
            onClickListener = View.OnClickListener { v ->
                val item = v.tag as String

                val intent = Intent(v.context, ReceitDetailActivity::class.java).apply {
                    putExtra("receipt", item)
                }
                v.context.startActivity(intent)

            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.receit_list_content, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = values[position]
            holder.idView.text = item
            //holder.contentView.text = item

            with(holder.itemView) {
                tag = item
                setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount() = values.size

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val idView: TextView = view.id_text
            val contentView: TextView = view.content
        }
    }
}
