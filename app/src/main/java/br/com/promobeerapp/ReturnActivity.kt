package br.com.promobeerapp

import android.os.Bundle
import android.view.MenuItem
import br.com.promobeerapp.fragment.ProductBrandListFragment
import br.com.promobeerapp.fragment.PromoFilterFragment
import br.com.promobeerapp.model.User
import kotlinx.android.synthetic.main.activity_main.*


class ReturnActivity : BaseActivity(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_return)

        intent?.extras?.getSerializable("user")?.let {
            user = it as User
        }
        if (savedInstanceState == null) {
            val b = intent.extras
            val fragmentToStart = b.getString("fragment");

            if(fragmentToStart.equals(ProductBrandListFragment.javaClass.name,true))
                changeFragment(ProductBrandListFragment.newInstance(false), true)
            else
                changeFragment(PromoFilterFragment.newInstance(), true)

        }
        setSupportActionBar(toolbar);
        supportActionBar!!.title = null
        init()


    }

    override fun onBackPressed() {
        val fragments = supportFragmentManager.backStackEntryCount
        if (fragments == 1) {
            finish()
        } else {
            if (fragmentManager.backStackEntryCount > 1) {
                fragmentManager.popBackStack()
            } else {
                super.onBackPressed()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.getItemId() == android.R.id.home){
            onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    public override fun onStart() {
        super.onStart()

        verifyPermission()
    }
}


