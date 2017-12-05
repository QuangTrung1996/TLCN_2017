package com.travel.phuc.trung.tlcn.tlcn.Login

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.travel.phuc.trung.tlcn.tlcn.Home.HomeFragment
import com.travel.phuc.trung.tlcn.tlcn.R
import kotlinx.android.synthetic.main.login_fragment.view.*

class LoginFragment : Fragment(), View.OnClickListener, GoogleApiClient.OnConnectionFailedListener {

    var name = ""
    var email = ""
    var photoUrl = ""
    var emailVerified = false   // Kiểm tra xem email của người dùng có được xác minh hay không
    var uid = ""                // ma uid trong firebase

    val sharedprperences : String="taikhoan"

    private lateinit var dialog: ProgressDialog             // thong bao cho
    private lateinit var mAuth : FirebaseAuth               // khai bao ket noi firebase
    private lateinit var mGoogleApiClient: GoogleApiClient  // khai bao sign in google
    private val RC_SIGN_IN = 999
    var database    : DatabaseReference

    init {
        database    = FirebaseDatabase.getInstance().reference
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view =inflater!!.inflate(R.layout.login_fragment,container,false)

        // khai bao
        init(view)
        initGoogle()

        // ket noi firebase
        mAuth = FirebaseAuth.getInstance()
        return view
    }

    override fun onPause() {
        super.onPause()
        mGoogleApiClient.stopAutoManage(activity)
        mGoogleApiClient.disconnect()
    }

    private fun init(view: View) {
        view.btn_login_google.setOnClickListener(this)
    }

    //Định cấu hình đăng nhập Google
    private fun initGoogle() {
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
        // [END config_signin]

        mGoogleApiClient = GoogleApiClient.Builder(activity)
                .enableAutoManage(activity /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }

    //phan cua dang nhap google
    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    override fun onClick(p0: View?) {
        val id = p0!!.id
        when (id){

            R.id.btn_login_google ->{
                signInGoogle()
            }
        }
    }

    // [START signin]
    private fun signInGoogle() {
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Kết quả được trả về từ khi tung ra Intent từ GoogleSignInApi.getSignInIntent (...);
        if (requestCode == RC_SIGN_IN) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
            if (result.isSuccess) {
                // Google Sign In was successful, authenticate with Firebase
                val account = result.signInAccount
                firebaseAuthWithGoogle(account!!)
            } else {
                // Google Sign In failed, update UI appropriately
            }
        }
    }

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
        // thong bao cho
        dialog = ProgressDialog.show(activity,"Loading...","Please wait",true)

        val credential = GoogleAuthProvider.getCredential(account.getIdToken(), null)
        mAuth.signInWithCredential(credential).addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                route()
                dialog.dismiss()
            } else {
                // If sign in fails, display a message to the user.
                ShortToast("Login google Failed")
                dialog.dismiss()
            }
        }
    }

    private fun route() {

        val user = mAuth.currentUser
        if (user != null) {
            // Name, email address, and profile photo Url
            name = user.displayName.toString()
            email = user.email.toString()
            photoUrl = user.photoUrl.toString()
            emailVerified = user.isEmailVerified
            uid = user.uid

            KhoiTaoUser()
            // phan cua Phúc
            addtaikhoan(uid,name,email,photoUrl)
        }

        val fragmentManager = activity.supportFragmentManager
        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.content, HomeFragment()).commit()
    }

    // sharedpreferences
    private fun addtaikhoan(uid: String, name: String, email: String, photoUrl: String) {
        val sharedpreferences=this.activity.getSharedPreferences(sharedprperences,android.content.Context.MODE_PRIVATE);
        val editor=sharedpreferences.edit();
        editor.putString("Uid",uid);
        editor.putString("Uname",name);
        editor.putString("Uemail",email);
        editor.putString("UURLAnh",photoUrl);
        editor.apply()
    }

    private fun KhoiTaoUser() {
        database.child("Users").child(uid).setValue(UserData(uid,name,email,photoUrl))
    }

    // hien dong thong bao
    private fun ShortToast(messsage : String) {
        val length : Int = Toast.LENGTH_SHORT
        Toast.makeText(activity, messsage, length).show()
    }
}