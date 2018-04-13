package br.com.promobeerapp.fragment

import android.Manifest
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.SyncStateContract.Helpers.insert
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.promobeerapp.MainActivity
import br.com.promobeerapp.R
import br.com.promobeerapp.adapter.AddressListHorizontalAdapter
import br.com.promobeerapp.connection.GoogleWebClient
import br.com.promobeerapp.connection.ProductWebClient
import br.com.promobeerapp.fragment.listener.CallbackServiceResponse
import br.com.promobeerapp.fragment.listener.OnItemSelectedListener
import br.com.promobeerapp.model.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fragment_promo_register_.*
import java.util.*
import br.com.promobeerapp.fragment.listener.NumberTextWatcher
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener


class PromoRegisterFragment : Fragment(), OnItemSelectedListener<GoogleAddress> , SwipeRefreshLayout.OnRefreshListener{

    var product: Product? = null
    var productBrand: ProductBrand? = null
    var productType: ProductType? = null
    var productSize: ProductSize? = null

    var actionCount:Int = 0

    private var mainContainer: View? = null


    private val googleAddressList: MutableList<GoogleAddress> = mutableListOf()
    private var googleAddressSelected: GoogleAddress? = null

    private var PROFILE_PIC_COUNT  = 0
    private val TAKE_PHOTO_REQUEST: Int = 1
    private val SELECT_FILE = 2

    private var mCurrentPhotoPath: String = ""
    private val contentResolver: ContentResolver? = null


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
        mainContainer = view
        searchIMG.setOnClickListener{
            (activity as MainActivity).changeFragment(AddressListFragment.newInstance(), true)
        }
        priceETX.addTextChangedListener(NumberTextWatcher(priceETX, "#,###"))
        swipeRefreshLayout?.isRefreshing = true
        actionCount++
        getProductByFilter()
        actionCount++
        searchNearbyLocation()

        expireTXV.setOnClickListener{
            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)


            val dpd = DatePickerDialog(activity, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                // Display Selected date in textbox
                expireTXV.text = "" + dayOfMonth + "/" + monthOfYear + "/" + year
            }, year, month, day)
            dpd.show()
        }


        photoBTN.setOnClickListener {
//            val items = arrayOf<CharSequence>("Take Photo", "Choose from Library", "Cancel")
//            val builder = android.app.AlertDialog.Builder(activity)
//            builder.setTitle("Add Photo!")
//            builder.setItems(items) { dialog, item ->
//                if (items[item] == "Take Photo") {
//                    PROFILE_PIC_COUNT = 1
//                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//                    startActivityForResult(intent, REQUEST_CAMERA)
//                } else if (items[item] == "Choose from Library") {
//                    PROFILE_PIC_COUNT = 1
//                    val intent = Intent(
//                            Intent.ACTION_PICK,
//                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
//                    startActivityForResult(intent, SELECT_FILE)
//                } else if (items[item] == "Cancel") {
//                    PROFILE_PIC_COUNT = 0
//                    dialog.dismiss()
//                }
//            }
//            builder.show()

            validatePermissions()
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
                                    actionCount--

                                    if(actionCount==0)
                                        swipeRefreshLayout?.isRefreshing = false
                                 }

                                override fun fail(throwable: Throwable) {

                                    actionCount--

                                    if(actionCount==0)
                                        swipeRefreshLayout?.isRefreshing = false

                                }
                            })
                }
            }
        }

    }
    override fun onRefresh() {
        actionCount++
        getProductByFilter()
        actionCount++
        searchNearbyLocation()
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
                        actionCount--

                        if(actionCount==0)
                           swipeRefreshLayout?.isRefreshing = false

                   }

                    override fun fail(throwable: Throwable) {
                        actionCount--

                        if(actionCount==0)
                            swipeRefreshLayout?.isRefreshing = false

                    }
                })
    }

    private fun createAddressListAdapter() {
        addressListRecyclerView?.visibility = View.VISIBLE
        loadingSupermarketTXV?.visibility = View.GONE
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


    override fun onItemSelected(googleAddress: GoogleAddress) {
        addressListRecyclerView.visibility = View.GONE
        googleAddressSelected = googleAddress

        placeSelectedLayout.visibility = View.VISIBLE
        establishmentTXV.text = googleAddress.name
        vicinityTXV.text = googleAddress.vicinity
        cancelIMG.setOnClickListener{
            addressListRecyclerView.visibility = View.VISIBLE
            placeSelectedLayout.visibility = View.GONE

        }
    }


    private fun validatePermissions() {
        Dexter.withActivity(activity)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object: PermissionListener {
                    override fun onPermissionGranted(
                            response: PermissionGrantedResponse?) {
//                        launchCamera()
                    }

                    override fun onPermissionRationaleShouldBeShown(
                            permission: PermissionRequest?,
                            token: PermissionToken?) {
                        AlertDialog.Builder(context)
                                .setTitle(
                                        R.string.storage_permission_rationale_title)
                                .setMessage(
                                        R.string.storage_permition_rationale_message)
                                .setNegativeButton(
                                        android.R.string.cancel,
                                        { dialog, _ ->
                                            dialog.dismiss()
                                            token?.cancelPermissionRequest()
                                        })
                                .setPositiveButton(android.R.string.ok,
                                        { dialog, _ ->
                                            dialog.dismiss()
                                            token?.continuePermissionRequest()
                                        })
                                .setOnDismissListener({
                                    token?.cancelPermissionRequest() })
                                .show()
                    }

                    override fun onPermissionDenied(
                            response: PermissionDeniedResponse?) {
                        Snackbar.make(mainContainer!!,
                                R.string.storage_permission_denied_message,
                                Snackbar.LENGTH_LONG)
                                .show()
                    }
                })
                .check()

    }



    private fun launchCamera() {
        val values = ContentValues(1)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        val fileUri = activity?.getContentResolver()?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(intent.resolveActivity(packageManager) != null) {
            mCurrentPhotoPath = fileUri.toString()
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(intent, TAKE_PHOTO_REQUEST)
        }
    }


}
