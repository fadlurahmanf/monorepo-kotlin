package com.fadlurahmanf.mapp_example.presentation.session.view_model

import com.fadlurahmanf.mapp_example.domain.repositories.ExampleRepositoryImpl
import com.fadlurahmanf.mapp_storage.domain.datasource.MappLocalDatasource
import com.fadlurahmanf.mapp_ui.external.helper.view_model.BaseViewModel
import javax.inject.Inject

class AfterLoginViewModel @Inject constructor(
    exampleRepositoryImpl: ExampleRepositoryImpl,
    mappLocalDatasource: MappLocalDatasource,
) : AfterLoginBaseViewModel(exampleRepositoryImpl, mappLocalDatasource) {
}