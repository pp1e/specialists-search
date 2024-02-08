package ru.ac.uniyar.handlers

import org.http4k.lens.FormField
import org.http4k.lens.Path
import org.http4k.lens.Query
import org.http4k.lens.boolean
import org.http4k.lens.int
import org.http4k.lens.nonEmptyString
import org.http4k.lens.string

val serviceCategoryIDField = FormField.int().required("serviceCategoryID")
val fullNameField = FormField.nonEmptyString().required("fullName")
val titleField = FormField.nonEmptyString().required("title")
val educationField = FormField.nonEmptyString().required("education")
val descriptionField = FormField.nonEmptyString().required("description")
val phoneNumberField = FormField.nonEmptyString().required("phoneNumber")
val experienceField = FormField.nonEmptyString().required("experience")
val cityIDField = FormField.int().required("cityID")
val specialistIDField = FormField.int().required("specialistID")
val loginField = FormField.nonEmptyString().required("login")
val nameField = FormField.nonEmptyString().required("name")
val passwordField = FormField.nonEmptyString().required("password")
val passwordRepeatField = FormField.nonEmptyString().required("passwordRepeat")
val commentaryField = FormField.string().defaulted("commentary", "")
val confirmButton = FormField.string().optional("confirm")
val rejectButton = FormField.string().optional("reject")
val qPage = Query.int().defaulted("page", 1)
val qId = Path.int().of("id")
val titleFilter = Query.string().optional("title")
val fullNameFilter = Query.string().optional("full_name")
val serviceCategoryTitleFilter = Query.string().optional("service_category_title")
val cityTitleFilter = Query.string().optional("city_title")
val educationFilter = Query.string().optional("education")
val qShowInfo = Query.boolean().defaulted("info", false)
val qLogin = Path.string().of("login")
