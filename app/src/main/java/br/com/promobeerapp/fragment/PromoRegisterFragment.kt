package br.com.promobeerapp.fragment

import android.Manifest.*
import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.PermissionRequest
import br.com.promobeerapp.MainActivity
import br.com.promobeerapp.Manifest
import br.com.promobeerapp.R
import br.com.promobeerapp.adapter.AddressListHorizontalAdapter
import br.com.promobeerapp.connection.GoogleWebClient
import br.com.promobeerapp.connection.ProductWebClient
import br.com.promobeerapp.fragment.listener.CallbackServiceResponse
import br.com.promobeerapp.fragment.listener.OnItemSelectedListener
import br.com.promobeerapp.model.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_promo_register_.*
import android.provider.MediaStore
import android.content.Intent
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat


class PromoRegisterFragment : Fragment(), OnItemSelectedListener<GoogleAddress> {

    var product: Product? = null
    var productBrand: ProductBrand? = null
    var productType: ProductType? = null
    var productSize: ProductSize? = null

    private val googleAddressList: MutableList<GoogleAddress> = mutableListOf()

    private var PROFILE_PIC_COUNT  = 0
    private val REQUEST_CAMERA = 1
    private val SELECT_FILE = 2
    companion object {
        private val ARG_PRODUCT_BRAND: String = "ARG_PRODUCT_BRAND"
        private val ARG_PRODUCT_TYPE: String = "ARG_PRODUCT_TYPE"
        private val ARG_PRODUCT_SIZE: String = "ARG_PRODUCT_SIZE"

        fun newInstance(productBrand: ProductBrand?, productType: ProductType?, productSize: ProductSize): PromoRegisterFragment {
            val args = Bundle()
            args.putSerializable(ARG_PRODUCT_BRAND, productBrand)
            args.putSerializable(ARG_PRODUCT_TYPE, productType)
            args.putSerializable(ARG_PRODUCT_SIZE, productSize)
            val fragment = PromoRegisterFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view: View = inflater.inflate(R.layout.fragment_promo_register_, container,
                false)
        productBrand= arguments?.getSerializable(PromoRegisterFragment.ARG_PRODUCT_BRAND) as ProductBrand?
        productType= arguments?.getSerializable(PromoRegisterFragment.ARG_PRODUCT_TYPE) as ProductType?
        productSize= arguments?.getSerializable(PromoRegisterFragment.ARG_PRODUCT_SIZE) as ProductSize?
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchIMG.setOnClickListener{
            (activity as MainActivity).changeFragment(AddressListFragment.newInstance(), true)
        }

        getProductByFilter()
        searchNearbyLocation();

        photoBTN.setOnClickListener {
            val items = arrayOf<CharSequence>("Take Photo", "Choose from Library", "Cancel")
            val builder = android.app.AlertDialog.Builder(activity)
            builder.setTitle("Add Photo!")
            builder.setItems(items) { dialog, item ->
                if (items[item] == "Take Photo") {
                    PROFILE_PIC_COUNT = 1
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, REQUEST_CAMERA)
                } else if (items[item] == "Choose from Library") {
                    PROFILE_PIC_COUNT = 1
                    val intent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(intent, SELECT_FILE)
                } else if (items[item] == "Cancel") {
                    PROFILE_PIC_COUNT = 0
                    dialog.dismiss()
                }
            }
            builder.show()
        }


    }

    private fun getProductByFilter() {

        productBrand?.let {
            productType?.let { it1 ->
                productSize?.let { it2 ->
                    ProductWebClient().listByFilter(it, it1, it2,
                            object : CallbackServiceResponse<List<Product>> {

                                override fun success(listProduct: List<Product>) {
                                    product = listProduct[0]
                                    detailProduct()
                                    //                        swipeRefreshLayout?.isRefreshing = false
                                 }

                                override fun fail(throwable: Throwable) {


                                }
                            })
                }
            }
        }

    }

    private fun searchNearbyLocation() {
        val location = (activity as MainActivity)?.mLastLocation?.latitude?.toString()+","+(activity as MainActivity)?.mLastLocation?.longitude
        GoogleWebClient().searchByDistance(location, getString(R.string.rank_by_distance),
                getString(R.string.type_supermarket),getString(R.string.google_maps_id),
                object : CallbackServiceResponse<List<GoogleAddress>> {

                   override fun success(googleAddressList: List<GoogleAddress>) {
                        this@PromoRegisterFragment.googleAddressList.clear()
                        this@PromoRegisterFragment.googleAddressList.addAll(googleAddressList)
                        createAddressListAdapter()
//                        swipeRefreshLayout?.isRefreshing = false
                    }

                    override fun fail(throwable: Throwable) {




                    }
                })
    }

    private fun createAddressListAdapter() {
        addressListRecyclerView?.layoutManager = LinearLayoutManager(context, OrientationHelper.HORIZONTAL, false)
        addressListRecyclerView?.adapter = AddressListHorizontalAdapter(googleAddressList, context, this)

    }

    private fun detailProduct() {
        Picasso.with( context )
                .load( product?.imagePath )
                .error( R.drawable.ic_error )
                .placeholder( R.drawable.progress_animation )
                .into( productIMG );

        nameProductTXV.text = getString(R.string.beer) + " "+ product?.productBrand?.name+ " "+ product?.productType?.name
        materialSizeTXV.text = product?.productSize?.material+": "+product?.productSize?.value + "ml"

    }

    override fun onItemSelected(t: GoogleAddress) {
    }
    private fun validatePermissions() {

//        var PERMISSIONS = {CAMERA_PERMISSION, READ_EXTERNAL_STORAGE_PERMISSION, WRITE_EXTERNAL_STORAGE_PERMISSION};
//        if (!hasPermissions(context, PERMISSIONS)) {
//            ActivityCompat.requestPermissions((activity) context, PERMISSIONS, permissionCode);
//        } else {
//            // Open your camera here.
//        }

    }

    private fun hasPermissions(vararg permissions: String): Boolean {

            for (permission in permissions) {
                if (context?.let { ActivityCompat.checkSelfPermission(it, permission) } !== PackageManager.PERMISSION_GRANTED) {
                    return false
                }
            }

        return true
    }

    private fun openCamera() {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

        }else{

        }
    }
}
