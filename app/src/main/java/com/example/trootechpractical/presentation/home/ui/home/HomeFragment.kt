package com.example.trootechpractical.presentation.home.ui.home

import android.app.DatePickerDialog
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.trootechpractical.R
import com.example.trootechpractical.BR
import com.example.trootechpractical.databinding.FragmentHomeBinding
import com.example.trootechpractical.domain.common.Output
import com.example.trootechpractical.domain.homeData.model.UserModel
import com.example.trootechpractical.utils.BindingAdapter
import com.example.trootechpractical.utils.FileDownloader
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var myBinding: FragmentHomeBinding? = null
    var userList: ArrayList<UserModel> = kotlin.collections.ArrayList<UserModel>()
    var fromDate = Calendar.getInstance()
    var toDate = Calendar.getInstance()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = myBinding!!
    lateinit var receiver: MyReceiver
    lateinit var homeViewModel: HomeViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

         homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        myBinding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        observe(homeViewModel)
        listner()
        receiver = MyReceiver(Handler()); // Create the receiver
        var intentfilter = IntentFilter()
        intentfilter.addAction("SELECT_ALL")
        intentfilter.addAction("UNSELECT_ALL")
        requireActivity().registerReceiver(receiver, IntentFilter(intentfilter))
        return root
    }

    private fun listner() {

        myBinding?.icCross?.setOnClickListener {
            fromDate = Calendar.getInstance()
            toDate = Calendar.getInstance()
            myBinding?.txtFromDate?.text = getString(R.string.from_date)
            myBinding?.txtToDate?.text = getString(R.string.to_date)
            setUserAdapter(userList)
            myBinding?.icCross?.visibility = View.GONE
        }

        // create an OnDateSetListener
        val fromDateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                fromDate.set(Calendar.YEAR, year)
                fromDate.set(Calendar.MONTH, monthOfYear)
                fromDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updateFromDateView()
            }
        }

        val toDateSetListener = object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker, year: Int, monthOfYear: Int,
                                   dayOfMonth: Int) {
                toDate.set(Calendar.YEAR, year)
                toDate.set(Calendar.MONTH, monthOfYear)
                toDate.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                updatetoDateView()
            }
        }

        myBinding?.lilFromDate?.setOnClickListener {
            DatePickerDialog(requireContext(),
                fromDateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                fromDate.get(Calendar.YEAR),
                fromDate.get(Calendar.MONTH),
                fromDate.get(Calendar.DAY_OF_MONTH)).show()
        }

        myBinding?.lilToDate?.setOnClickListener {
            DatePickerDialog(requireContext(),
                toDateSetListener,
                // set DatePickerDialog to point to today's date when it loads up
                toDate.get(Calendar.YEAR),
                toDate.get(Calendar.MONTH),
                toDate.get(Calendar.DAY_OF_MONTH)).show()
        }

        myBinding?.icFilter?.setOnClickListener {
            if(myBinding?.txtFromDate?.text?.contains("Date",true) == true)
            {
                showMessage(getString(R.string.please_select_from_date))
                return@setOnClickListener
            }
            else if(myBinding?.txtToDate?.text?.contains("Date",true) == true)
            {
                showMessage(getString(R.string.please_select_to_date))
                return@setOnClickListener
            }
            else if(fromDate.after(toDate))
            {
                showMessage(getString(R.string.please_select_proper_dates))
                return@setOnClickListener
            }
            myBinding?.icCross?.visibility = View.VISIBLE
            var dateFormat =  SimpleDateFormat("yyyy-MM-dd");
            var myDate = Calendar.getInstance();
            var myUserList = kotlin.collections.ArrayList<UserModel>()
            for (user in userList)
            {
                var mydate = user.date?.split(" ")?.get(0)
                Log.e("MyDate is","Date ${mydate}")
                if(mydate?.split("‑")?.size ==  3)
                {
                    var year  = mydate.split("‑").get(0).toInt()
                    var month = mydate.split("‑").get(1).toInt()
                    var day = mydate.split("‑").get(2).toInt()

                    myDate.set(year?:0,month?:0,day?:0)
                    Log.e("ToDate Is","Here year ${year} month $month day$day")
                }
                if(myDate.after(fromDate) || myDate.time == fromDate.time &&
                    myDate.before(toDate) || myDate.time == toDate.time) {
                    //display your report
                    myUserList.add(user)
                }


            }
            Log.e("myUserList SIze","Is ${myUserList.size}")
            setUserAdapter(myUserList)

        }

        myBinding?.btnDownload?.setOnClickListener {
            for (selectedUser in userList) {
                if(selectedUser.isSelected)
                {
                    try {
                        val downloadFileRef = FileDownloader.downloadFile(
                            Uri.parse(selectedUser.download_url),
                            "fileName_${System.currentTimeMillis()}.png",requireActivity()
                        )
                        if (downloadFileRef != 0L) {
                            Log.e("mContext", "Starting download...")
                        } else {
                            Log.e("mContext", "File is not available for download")
                        }
                    }
                    catch (e:Exception)
                    {
                        Log.e("mContext", "Exception...${e.message}")
                    }
                }

            }
        }

    }
    private fun updateFromDateView() {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        myBinding?.txtFromDate?.text = sdf.format(fromDate.time)
    }

    private fun updatetoDateView() {
        val myFormat = "dd/MM/yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        myBinding?.txtToDate?.text = sdf.format(toDate.time)
    }

    private fun observe(homeViewModel: HomeViewModel) {

        homeViewModel.errorMessage.observe(requireActivity()) { result ->
            showMessage(result)
        }


        homeViewModel.userListResponse.observe(requireActivity()) { result ->

            when (result.status) {
                Output.Status.SUCCESS -> {
                    result.data?.let { list ->
                        Log.e("Getting Success", "Is=== ${list.size}")
                        userList.clear()
                        userList.addAll(list)
                        setUserAdapter(userList)
                    }
                }
                Output.Status.ERROR -> {
                    result.message?.let {
                        showMessage(result.message)
                    }
                }
                Output.Status.LOADING -> {
                    Log.e("Inside Loading", "Display Progress")
                }
                else -> {
                    //No need to handle for this
                }
            }
        }
    }

    private fun setUserAdapter(list: ArrayList<UserModel>) {
        myBinding?.listProducts?.layoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)

        myBinding?.listProducts?.adapter = BindingAdapter(
            layoutId = R.layout.row_user,
            br = BR.model,
            list = list,
            clickListener = { view, position ->
                when (view.id) {
                    R.id.ll_product_root,R.id.lc_checked -> {
                        list.get(position).isSelected = !list.get(position).isSelected
                        myBinding?.listProducts?.adapter?.notifyDataSetChanged()
                        if(list.any { it.isSelected })
                        {
                            myBinding?.btnDownload?.visibility = View.VISIBLE
                        }
                        else{
                            myBinding?.btnDownload?.visibility = View.GONE
                        }
                    }
                }
            })

    }
     fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }


    inner class MyReceiver(handler: Handler) : BroadcastReceiver() {
        var handler: Handler = handler // Handler used to execute code on the UI thread
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.e("Intent", "Action ${intent?.action}")
            handler.post {
                run {
                    if (intent?.action.equals("SELECT_ALL")) {
                        userList.forEach {
                            it.isSelected = true
                        }
                        myBinding?.listProducts?.adapter?.notifyDataSetChanged()
                    }
                    else if (intent?.action.equals("UNSELECT_ALL")) {
                        userList.forEach {
                            it.isSelected = false
                        }
                        myBinding?.listProducts?.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        requireActivity().unregisterReceiver(receiver)
        myBinding = null
    }
}