package br.com.promobeerapp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import br.com.promobeerapp.fragment.PromoListFragment
import br.com.promobeerapp.fragment.listener.OnSelectProductBrandListListener
import br.com.promobeerapp.model.ProductBrand
import android.support.design.widget.NavigationView
import android.support.v4.app.FragmentManager
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.view.View
import br.com.promobeerapp.model.User
import kotlinx.android.synthetic.main.activity_main.*
import android.content.Intent




class MainActivity : AppCompatActivity(), OnSelectProductBrandListListener, NavigationView.OnNavigationItemSelectedListener {

    lateinit var drawerToggle: ActionBarDrawerToggle
    lateinit var user: User


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    companion object {
        private val LOG_TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawerToggle = ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close)

        if (savedInstanceState == null) {
            changeFragment(PromoListFragment.newInstance(), false)
        }
        setSupportActionBar(toolbar);
        supportActionBar!!.title = null

        drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)

       intent.extras?.getSerializable("user")?.let {
           user = it as User

       }



    }

    override fun onOptionsItemSelected(item:MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{

                    drawerLayout.openDrawer(GravityCompat.START);  // OPEN DRAWER
                    return true
                }
            }
        return super.onOptionsItemSelected(item);
    }
    fun changeFragment(fragmentToChange: Fragment, hasBackPage: Boolean) {
        if (!hasBackPage) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container_fragment, fragmentToChange, fragmentToChange.javaClass.name)
                    .commit()
        } else {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.main_container_fragment, fragmentToChange, fragmentToChange.javaClass.name)
                    .addToBackStack(null)
                    .commit()
        }

        supportFragmentManager.addOnBackStackChangedListener(object : FragmentManager.OnBackStackChangedListener {
            override  fun onBackStackChanged() {
                if (supportFragmentManager.backStackEntryCount > 0) {
                    supportActionBar!!.setDisplayHomeAsUpEnabled(true) // show back button
                    toolbar.setNavigationOnClickListener { onBackPressed() }
                } else {
                    //show hamburger
                    supportActionBar!!.setDisplayHomeAsUpEnabled(false)
                    drawerToggle.syncState()
                    toolbar.setNavigationOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }
                }
            }
        })
    }




    override fun brandSelected(brand: ProductBrand) {
//        val fragment = fragmentManager.findFragmentById(R.id.main_container_fragment)//if you specify your fragment in xml
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
    }
}


