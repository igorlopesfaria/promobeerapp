package br.com.promobeerapp.fragment

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.promobeerapp.BaseActivity
import br.com.promobeerapp.R
import br.com.promobeerapp.adapter.AddressListHorizontalAdapter
import br.com.promobeerapp.connection.GoogleWebClient
import br.com.promobeerapp.connection.ProductWebClient
import br.com.promobeerapp.connection.PromoWebClient
import br.com.promobeerapp.fragment.listener.CallbackServiceResponse
import br.com.promobeerapp.fragment.listener.OnItemSelectedListener
import br.com.promobeerapp.model.*
import com.squareup.picasso.Picasso
import java.util.*
import br.com.promobeerapp.fragment.listener.NumberTextWatcher
import com.facebook.drawee.backends.pipeline.Fresco
import com.facebook.imagepipeline.common.ResizeOptions
import com.facebook.imagepipeline.request.ImageRequestBuilder
import com.google.gson.annotations.SerializedName
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import kotlinx.android.synthetic.main.fragment_promo_register_.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat


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
    private var file: File? = null


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
            (activity as BaseActivity).changeFragment(AddressListFragment.newInstance(), true)
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

            validatePermissions()
        }

        registerBTN.setOnClickListener {
            val description: String = descriptionETX.text.toString()
            if(description.isEmpty()) {
                showSnackErro( getString(R.string.please_add_description))
                return@setOnClickListener;
            }
            val price: String = priceETX.text.toString()
            if(price.isEmpty()) {
                showSnackErro( getString(R.string.please_add_price))
                return@setOnClickListener;
            }
            var expiredDate: String = expireTXV.text.toString()
            if(expiredDate.isEmpty()) {
                showSnackErro( getString(R.string.please_add_expire_date))
                return@setOnClickListener;
            }

            val sdfDestination = SimpleDateFormat("yyyy-MM-dd");
            val sdfSource = SimpleDateFormat("dd/MM/yyyy");

            // parse the date into another format
            expiredDate =  sdfDestination.format(sdfSource.parse(expiredDate));

            if(googleAddressSelected == null) {
                showSnackErro( getString(R.string.please_select_establishment))
                return@setOnClickListener;
            }
            val establishment = Establishment(googleAddressSelected!!.id,
                    googleAddressSelected!!.googleGeometry.location.lat,
                    googleAddressSelected!!.googleGeometry.location.lng,
                    googleAddressSelected!!.placeId,
                    googleAddressSelected!!.name,
                    googleAddressSelected!!.vicinity)
            if(product == null) {
                showSnackErro( getString(R.string.please_select_correct_product))
                return@setOnClickListener;
            }

            if(file == null) {
                showSnackErro( getString(R.string.please_take_photo))
                return@setOnClickListener;
            }

            val promo = Promo(null,
                    product!!,
                    description,
                    price.replace("R$","").replace(",","."),
                    expiredDate,
                    null,
                    establishment,
                    file!!)


            swipeRefreshLayout?.isRefreshing = true
            context?.let {
                PromoWebClient().insert(promo, object : CallbackServiceResponse<Promo> {

                    override fun success(response:Promo) {
                        swipeRefreshLayout?.isRefreshing = false
                        (activity as BaseActivity).preffs?.setPromoRegistered(response)
                        (activity as BaseActivity).changeFragment(PromoListFragment.newInstance(), false)
                        activity?.finish()
                    }

                    override fun fail(throwable: Throwable) {
                        showSnackErro(throwable.message!!)
                        swipeRefreshLayout?.isRefreshing = false

                    }
                }, it)
            }

        }
    }

    fun showSnackErro(message:String){

        val snackbar = view?.let { Snackbar.make(it,  message, Snackbar.LENGTH_LONG) }
        val sbView = snackbar?.view
        sbView?.setBackgroundColor(Color.RED)
        snackbar?.show()
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
        val location = (activity as BaseActivity).mLastLocation?.latitude?.toString()+","+(activity as BaseActivity)?.mLastLocation?.longitude
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
            googleAddressSelected = null
            addressListRecyclerView.visibility = View.VISIBLE
            placeSelectedLayout.visibility = View.GONE

        }
    }


    private fun validatePermissions() {
        Dexter.withActivity(activity)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(object: MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport?) {
                        // check if all permissions are granted
                        if (report?.areAllPermissionsGranted()!=null && report.areAllPermissionsGranted()) {
                            launchCamera()
                        }

                        // check for permanent denial of any permission
                        if (report?.isAnyPermissionPermanentlyDenied() != null && report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showAlertCameraPermission()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permissions: MutableList<PermissionRequest>?, token: PermissionToken?) {
                        token?.continuePermissionRequest();
                    }
                }).check();

    }

    private fun showAlertCameraPermission() {
        AlertDialog.Builder(context)
                .setTitle(
                        R.string.storage_permission_rationale_title)
                .setMessage(
                        R.string.storage_permition_rationale_message)
                .setNegativeButton(
                        android.R.string.cancel,
                        { dialog, _ ->

                            dialog.dismiss()
                        })
                .setPositiveButton(R.string.go_settings,
                        { dialog, _ ->
                            openSettings();
                            dialog.dismiss()
                        })
                .show()

    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        val uri = Uri.fromParts("package", activity?.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);    }


    private fun launchCamera() {
        val values = ContentValues(1)
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
        val fileUri = activity?.getContentResolver()?.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if(intent.resolveActivity(activity?.getPackageManager()) != null) {
            mCurrentPhotoPath = fileUri.toString()
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
            startActivityForResult(intent, TAKE_PHOTO_REQUEST)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int,
                                  data: Intent?) {

        if (resultCode == Activity.RESULT_OK
                && requestCode == TAKE_PHOTO_REQUEST) {
            processCapturedPhoto(data)
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    private fun processCapturedPhoto(data: Intent?) {
        val photo = data?.getExtras()?.get("data") as Bitmap;
        val stream = ByteArrayOutputStream()
        photo.compress(Bitmap.CompressFormat.PNG, 100,stream )
        val byteArray = stream.toByteArray()
        val path = Environment.getExternalStorageDirectory().toString()+"/_camera.png"
        file = File(path)
        val fo  = FileOutputStream(file);

        fo.write(byteArray);
        fo.flush();
        fo.close();


        var uri:Uri? = null
        if(file!=null)
            uri = Uri.fromFile(file)

        val height = resources.getDimensionPixelSize(R.dimen.profile_height)
        val width = resources.getDimensionPixelSize(R.dimen.photo_width)

        if(uri!=null) {
            val request = ImageRequestBuilder.newBuilderWithSource(uri)
                    .setResizeOptions(ResizeOptions(width, height))
                    .build()
            val controller = Fresco.newDraweeControllerBuilder()
                    .setOldController(photoBTN?.controller)
                    .setImageRequest(request)
                    .build()
            photoBTN?.controller = controller
        }
    }
}
