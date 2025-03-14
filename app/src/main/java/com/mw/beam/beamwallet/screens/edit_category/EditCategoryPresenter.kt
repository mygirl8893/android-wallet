/*
 * // Copyright 2018 Beam Development
 * //
 * // Licensed under the Apache License, Version 2.0 (the "License");
 * // you may not use this file except in compliance with the License.
 * // You may obtain a copy of the License at
 * //
 * //    http://www.apache.org/licenses/LICENSE-2.0
 * //
 * // Unless required by applicable law or agreed to in writing, software
 * // distributed under the License is distributed on an "AS IS" BASIS,
 * // WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * // See the License for the specific language governing permissions and
 * // limitations under the License.
 */

package com.mw.beam.beamwallet.screens.edit_category

import com.mw.beam.beamwallet.base_screen.BasePresenter
import com.mw.beam.beamwallet.core.helpers.CategoryColor

class EditCategoryPresenter(view: EditCategoryContract.View?, repository: EditCategoryContract.Repository, private val state: EditCategoryState)
    : BasePresenter<EditCategoryContract.View, EditCategoryContract.Repository>(view, repository), EditCategoryContract.Presenter {

    override fun onViewCreated() {
        super.onViewCreated()
        val id = view?.getCategoryId()
        var category = if (id == null) repository.createNewCategory() else repository.getCategoryFromId(id)

        if (category == null) {
            category = repository.createNewCategory()
        }

        state.category = category
        state.tempName = category.name
        state.tempColor = category.color

        state.allCategory = repository.getAllCategory()

        view?.init(category)
    }

    override fun onChangeColor(categoryColor: CategoryColor) {
        state.tempColor = categoryColor
        checkSaveButtonEnabled()
    }

    override fun onNameChanged(name: String) {
        state.tempName = name.trim()
        checkSaveButtonEnabled()
    }

    private fun checkSaveButtonEnabled() {
        val isNameChanged = state.category?.name != state.tempName && state.tempName.isNotBlank()
        val isColorChanged = state.category?.color != state.tempColor
        val isValidName = checkIsUniqueName(state.tempName)
        if (!state.category?.name.isNullOrBlank()) {
            view?.setSaveEnabled((isNameChanged || isColorChanged) && state.tempName.isNotBlank() && (isValidName || state.category?.name == state.tempName.trim()))
        } else view?.setSaveEnabled(isNameChanged && isValidName)
    }

    private fun checkIsUniqueName(name: String): Boolean {
        return state.allCategory.firstOrNull { category -> category.name == name.trim() } == null
    }

    override fun onSavePressed() {
        if (view == null) {
            return
        }

        state.category?.let {
            it.name = view!!.getName().trim()
            it.color = view!!.getSelectedCategoryColor()

            repository.saveCategory(it)
        }

        view?.finish()
    }
}