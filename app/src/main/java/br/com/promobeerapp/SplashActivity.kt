package br.com.promobeerapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.util.Log
import br.com.promobeerapp.model.User
import io.reactivex.MaybeObserver
import io.reactivex.Observer
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers


class SplashActivity : AppCompatActivity() {
    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3 seconds
    val db = PromoBeerApplication.database

    companion object {
        private val LOG_TAG = SplashActivity::class.java.simpleName
    }

    internal val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
//            db?.userDAO()?.getUser()?.
//                    subscribeOn(Schedulers.io())?.
//                    observeOn(AndroidSchedulers.mainThread())?.
//                    subscribe(
//                            { user ->
//                                val intent = Intent(applicationContext, MainActivity::class.java)
//                                intent.putExtra("user", user)
//                                startActivity(intent)
//                                finish()
//                            },
//                            { error ->
//                                Log.d("SingleObserver", error.message)
//                            })
            val intent = Intent(applicationContext, MainActivity::class.java)
            startActivity(intent)
            finish()

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_initial)

        //Initialize the Handler
        mDelayHandler = Handler()

        //Navigate with delay
        mDelayHandler!!.postDelayed(mRunnable, SPLASH_DELAY)

    }



    override fun onDestroy() {

        if (mDelayHandler != null) {
            mDelayHandler!!.removeCallbacks(mRunnable)
        }

        super.onDestroy()
    }




}
