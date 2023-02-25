package com.example.trootechpractical.presentation.base

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.io.Serializable
import java.lang.reflect.ParameterizedType
import com.example.trootechpractical.BR
/*
*
*   This activity is the base of all activity, While extending it to another activity
*   please pass corresponding activity's auto generated CategoryObject Binding Class.
*   also please do not forgot to pass ViewModel as we have use ViewModel in XML.
*
*/

abstract class BaseActivity<VM : ViewModel, B : ViewDataBinding>(val bindingFactory: (LayoutInflater) -> B) :
    AppCompatActivity() {
    lateinit var viewModel: VM
    lateinit var myBinding: B

    abstract fun initActivity()

    var savedInstanceState: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        this.savedInstanceState = savedInstanceState

        viewModel =
            ViewModelProvider(this, defaultViewModelProviderFactory).get(getViewModelClass())
        myBinding = bindingFactory(layoutInflater)
        setContentView(myBinding.root)
        myBinding.lifecycleOwner = this
        myBinding.setVariable(BR.viewModel, viewModel)
        initActivity()
    }

    private fun getViewModelClass(): Class<VM> {
        val type = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0]
        return type as Class<VM>
    }

    fun startActivity(cls: Class<*>) {
        val intent = Intent(this, cls)
        startActivity(intent)

    }

    fun startActivityWithData(cls: Class<*>, obj: Any) {
        val intent = Intent(this, cls)
        if (obj is Serializable) intent.putExtra("Extras", obj)
        startActivity(intent)

    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
    protected fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    fun isConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val capability = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return capability?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
    }
}