package com.project.aidoctor.ui.login





import android.text.SpannableStringBuilder
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.project.aidoctor.data.entities.User
import com.project.aidoctor.data.remote.login.LoginListener
import com.project.aidoctor.data.repository.login.LoginRepository
import com.project.aidoctor.util.Coroutines
import com.project.aidoctor.util.SharedPreferencesManager
import java.util.regex.Pattern

class LoginViewModel(private val repository: LoginRepository, private val sharedPreferencesManager: SharedPreferencesManager): ViewModel(){


    var loginListener: LoginListener? = null

    val email: MutableLiveData<String> by lazy {
        MutableLiveData<String>().apply {
            postValue("")
        }
    }

    val pw: MutableLiveData<String> by lazy {
        MutableLiveData<String>().apply {
            postValue("")
        }
    }

    val pwCheck: MutableLiveData<String> by lazy {
        MutableLiveData<String>().apply {
            postValue("")
        }
    }
    val showPw: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>().apply {
            postValue(false)
        }
    }

    fun showPw(){
        showPw.postValue(!showPw.value!!)
    }

    fun emailBlankCheck() {
        if(!email.value.isNullOrEmpty()){
            val editable = SpannableStringBuilder("")
            loginListener!!.clearEmail(editable)
        }

    }

    fun checkUser() {
        val _email = email.value.toString()
        val _pw = pw.value.toString()

        if(_email.isEmpty()){
            loginListener!!.onCheckUserFailure( "이메일 주소를 입력해주세요.")
            return
        }

        if(!Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.0-]+\\.[a-zA-Z]{2,6}\$", _email)){
            loginListener!!.onCheckUserFailure( "정확한 이메일 주소를 입력해주세요.")
            return
        }

        if(_pw.isEmpty()){
            loginListener!!.onCheckUserFailure( "비밀번호를 입력해주세요.")
            return
        }

        startLogin()
    }

    fun startLogin() {

        Coroutines.main {

            try {
                val email = email.value.toString()
                val pw = pw.value.toString()

                val loginResponse = repository.login(User(userName = email, password = pw))

                if(loginResponse.isSuccess){
                    setToken()
                    sharedPreferencesManager.saveId(loginResponse.user.USER_ID)
                    sharedPreferencesManager.saveEmail(email)
                    sharedPreferencesManager.savePw(pw)
                    if(loginResponse.user.USER_ISADMIN == 1){
                        loginListener!!.onStartAdmin()
                    }else
                        loginListener!!.onStartMain()
                    return@main
                }
                loginListener!!.onLoginFailure(loginResponse.message)

            }catch (e:Exception){
                loginListener!!.onLoginFailure(e.message!!)
            }
        }
    }

    private fun setToken() {
        Coroutines.main {

            try {
                val Id = sharedPreferencesManager.getId()
                val token = sharedPreferencesManager.getToken()

                val loginResponse = repository.setToken(Id,token,"aos")

                if(loginResponse.isSuccess){
                    return@main
                }
                loginListener!!.onLoginFailure(loginResponse.message)

            }catch (e:Exception){
                loginListener!!.onLoginFailure(e.message!!)
            }
        }
    }
}