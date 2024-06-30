package com.techyourchance.coroutines.solutions.exercise3

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.techyourchance.coroutines.R
import com.techyourchance.coroutines.common.BaseFragment
import com.techyourchance.coroutines.common.ThreadInfoLogger
import com.techyourchance.coroutines.exercises.exercise1.GetReputationEndpoint
import com.techyourchance.coroutines.home.ScreenReachableFromHome
import kotlinx.coroutines.*

class Exercise3SolutionFragment : BaseFragment() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main.immediate)

    override val screenTitle get() = ScreenReachableFromHome.EXERCISE_3.description

    private lateinit var edtUserId: EditText
    private lateinit var btnGetReputation: Button
    private lateinit var txtElapsedTime: TextView


    private lateinit var getReputationEndpoint: GetReputationEndpoint

    private var countingJob: Job? = null
    private var startingTimestamp: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getReputationEndpoint = compositionRoot.getReputationEndpoint
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_exercise_3, container, false)

        txtElapsedTime = view.findViewById(R.id.txt_elapsed_time)

        edtUserId = view.findViewById(R.id.edt_user_id)
        edtUserId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                btnGetReputation.isEnabled = !s.isNullOrEmpty()
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        btnGetReputation = view.findViewById(R.id.btn_get_reputation)
        btnGetReputation.setOnClickListener {
            logThreadInfo("button callback")

            countingJob = coroutineScope.launch {
                startingTimestamp = System.currentTimeMillis()
                showElapsedTime()
            }

            coroutineScope.launch {
                btnGetReputation.isEnabled = false
                val reputation = getReputationForUser(edtUserId.text.toString())
                Toast.makeText(requireContext(), "reputation: $reputation", Toast.LENGTH_SHORT).show()
                btnGetReputation.isEnabled = true
                countingJob?.cancel()
            }
        }

        return view
    }

    @SuppressLint("DefaultLocale")
    private suspend fun showElapsedTime() {
        txtElapsedTime.text = String.format("Time elapsed: %.2f seconds", millisToSeconds(System.currentTimeMillis() - startingTimestamp))
        delay(10)
        showElapsedTime()
    }

    override fun onStop() {
        super.onStop()
        coroutineScope.coroutineContext.cancelChildren()
        if (startingTimestamp != 0L) {
            btnGetReputation.isEnabled = true
            txtElapsedTime.text = ""
        }
    }

    private suspend fun getReputationForUser(userId: String): Int {
        return withContext(Dispatchers.Default) {
            logThreadInfo("getReputationForUser()")
            getReputationEndpoint.getReputation(userId)
        }
    }

    private fun logThreadInfo(message: String) {
        ThreadInfoLogger.logThreadInfo(message)
    }

    private fun millisToSeconds(millis: Long): Double {
        return millis / 1000.0;
    }

    companion object {
        fun newInstance(): Fragment {
            return Exercise3SolutionFragment()
        }
    }
}