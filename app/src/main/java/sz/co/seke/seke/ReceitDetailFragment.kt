package sz.co.seke.seke

import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_receit_detail.*
import kotlinx.android.synthetic.main.receit_detail.view.*
import org.json.JSONArray

/**
 * A fragment representing a single Receit detail screen.
 * This fragment is either contained in a [ReceitListActivity]
 * in two-pane mode (on tablets) or a [ReceitDetailActivity]
 * on handsets.
 */
class ReceitDetailFragment : Fragment() {

    /**
     * The dummy content this fragment is presenting.
     */
    private var item: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.e("Arguments",arguments?.getString(ARG_ITEM_ID))

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                // Load the dummy content specified by the fragment
                // arguments. In a real-world scenario, use a Loader
                // to load content from a content provider.
                item = it.getString(ARG_ITEM_ID)
                activity?.toolbar_layout?.title = item

            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.receit_detail, container, false)

        val receitSTring = PreferenceManager.getDefaultSharedPreferences(context).getString(item,"[]")
        val a = JSONArray(receitSTring)
        /*
           name: String,
    price:Number,
    bar_code: String,
    quantity: Number
         */

        var receitDisplay = "";
        for (i in 0..a.length()-1){
            receitDisplay+="${a.getJSONObject(i).getString("name")}   E${a.getJSONObject(i).getString("price")} \n"
        }


            rootView.receit_detail.text = receitDisplay


        return rootView
    }

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "receipt"
    }
}
