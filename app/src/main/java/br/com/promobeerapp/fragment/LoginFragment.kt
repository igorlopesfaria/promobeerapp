package br.com.promobeerapp.fragment

import android.support.v4.app.Fragment


class LoginFragment : Fragment(){}
//, GoogleApiClient.OnConnectionFailedListener {
//
//    val TAG = "LoginFragment"
//    //Init views
//    lateinit var loginGoogleBTN: SignInButton
//    lateinit var facebookLoginBTN: LoginButton
//    //Request codes
//    val GOOGLE_LOG_IN_RC = 1
//    val FACEBOOK_LOG_IN_RC = 2
//    val TWITTER_LOG_IN_RC = 3
//    // Google API Client object.
//    var googleApiClient: GoogleApiClient? = null
//    // Firebase Auth Object.
//    var firebaseAuth: FirebaseAuth? = null
//
//
//    companion object {
//        fun newInstance(): LoginFragment {
//            return LoginFragment()
//        }
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
//                              savedInstanceState: Bundle?): View? {
//
//        val view: View = inflater.inflate(R.layout.fragment_login, container,
//                false)
//        return view
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//
//        firebaseAuth = FirebaseAuth.getInstance()
//
//        // Configure Google Sign In
//        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.request_client_id))
//                .requestEmail()
//                .build()
//        // Creating and Configuring Google Api Client.
//        googleApiClient = GoogleApiClient.Builder(context!!)
//                .enableAutoManage(activity as FragmentActivity /* FragmentActivity */, this /* OnConnectionFailedListener */)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
//                .build()
//        loginGoogleBTN.setOnClickListener{
//            Log.i(TAG, "Trying Google LogIn.")
//            googleLogin()
//        }
//
//    }
//
//    private fun googleLogin() {
//
//    }
//
//    override fun onConnectionFailed(p0: ConnectionResult) {
//        Log.i(TAG, "Starting Google LogIn Flow.")
//        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
//        startActivityForResult(signInIntent, GOOGLE_LOG_IN_RC)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        Log.i(TAG, "Got Result code ${requestCode}.")
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == GOOGLE_LOG_IN_RC) {
//            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
//            Log.i(TAG, "With Google LogIn, is result a success? ${result.isSuccess}.")
//            if (result.isSuccess) {
//                // Google Sign In was successful, authenticate with Firebase
//                firebaseAuthWithGoogle(result.signInAccount!!)
//            } else {
//                Toast.makeText(context, "Some error occurred.", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }
//
//    private fun firebaseAuthWithGoogle(signInAccount: GoogleSignInAccount) {
//        Log.i(TAG, "Authenticating user with firebase.")
//        val credential = GoogleAuthProvider.getCredential(signInAccount.idToken, null)
//        firebaseAuth?.signInWithCredential(credential)?.addOnCompleteListener(activity!!) { task ->
//            Log.i(TAG, "Firebase Authentication, is result a success? ${task.isSuccessful}.")
//            if (task.isSuccessful) {
//                // Sign in success, update UI with the signed-in user's information
//                startActivity(Intent(context, MainActivity::class.java))
//            } else {
//                // If sign in fails, display a message to the user.
//                Log.e(TAG, "Authenticating with Google credentials in firebase FAILED !!")
//            }
//        }
//    }
//
//}
////db.insert("User",
////"id" to 42,
////"name" to "John",
////"email" to "user@domain.org"
////)