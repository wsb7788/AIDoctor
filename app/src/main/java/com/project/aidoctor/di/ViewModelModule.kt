package com.project.aidoctor.di

import com.project.aidoctor.ui.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {

    viewModel { LoginViewModel(get(), get()) }
}